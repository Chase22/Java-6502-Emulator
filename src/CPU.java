import cpu.*;
import cpu.AddressMode;
import cpu.OpCode;

public class CPU {
	public byte flags = 0x00;
	//C,Z,I,D,B,U,V,N
	//Carry, Zero, Interrupt Disable, Decimal, Break, Unused, Overflow, Negative

	public byte a = 0x00;
	public byte x = 0x00;
	public byte y = 0x00;
	public byte stackPointer = 0x00;
	public short programCounter = 0x0000;

	public boolean debug = false;

	public short addressAbsolute = 0x0000;
	public short addressRelative = 0x0000;
	public byte opcode = 0x00;
	public int cycles = 0;

	public double ClocksPerSecond = 0;
	public int clockDelta = 1;
	public int lastClocks = 0;

	public long startTime = 0;
	public long timeDelta = 1;
	public long lastTime = System.nanoTime();

	public int additionalCycles = 0;

	public boolean interruptRequested = false;
	public boolean NMinterruptRequested = false;

	public boolean stopped = false, waiting = false;

	public CPU() {
		reset();
	}

	void setFlag(char flag, boolean condition) {
		flag = Character.toUpperCase(flag);
		switch (flag) {
		case 'C':
			flags = setBit(flags,0,condition);
				break;
		case 'Z':
			flags = setBit(flags,1,condition);
				break;
		case 'I':
			flags = setBit(flags,2,condition);
				break;
		case 'D':
			flags = setBit(flags,3,condition);
				break;
		case 'B':
			flags = setBit(flags,4,condition);
				break;
		case 'U':
			flags = setBit(flags,5,condition);
				break;
		case 'V':
			flags = setBit(flags,6,condition);
				break;
		case 'N':
			flags = setBit(flags,7,condition);
				break;
		}
	}

	boolean getFlag(char flag) {
		flag = Character.toUpperCase(flag);
		switch (flag) {
		case 'N':
			return ((flags&0b10000000) == 0b10000000);
		case 'V':
			return ((flags&0b01000000) == 0b01000000);
		case 'U':
			return ((flags&0b00100000) == 0b00100000);
		case 'B':
			return ((flags&0b00010000) == 0b00010000);
		case 'D':
			return ((flags&0b00001000) == 0b00001000);
		case 'I':
			return ((flags&0b00000100) == 0b00000100);
		case 'Z':
			return ((flags&0b00000010) == 0b00000010);
		case 'C':
			return ((flags&0b00000001) == 0b00000001);
		}
		if (EaterEmulator.verbose) System.out.println("Something has gone wrong in getFlag!");
		return false;
	}

	public static byte setBit(byte b, int bit, boolean value) {
		if (value) {
			b |= (byte)(0x00+Math.pow(2, bit));
		} else {
			b &= (byte)(0xFF-Math.pow(2, bit));
		}
		return b;
	}

	void clock() {
		if (interruptRequested || NMinterruptRequested) waiting = false;
		if (waiting || stopped) return;

		if (cycles == 0) {
			InstructionSet currentInstruction = InstructionSet.getByAddress(opcode);
			AddressMode currentAddressMode = currentInstruction.addressMode;
			OpCode currentOpCode = currentInstruction.opcode;

			if (interruptRequested)
				irq();
			else if (NMinterruptRequested)
				nmi();
			else {
				additionalCycles = 0;
				opcode = Bus.read(programCounter);
				programCounter++;

				cycles = currentInstruction.cycles;

				//Execute the functions corresponding to the addressing mode and opcode

				executeAddressModeFunction(currentAddressMode);
				executeOpcodeFunction(currentOpCode);

			}

			if (debug) {
				System.out.print(Integer.toHexString(Short.toUnsignedInt(programCounter))+"   "+ currentOpCode +" "+ROMLoader.byteToHexString(opcode)+" ");
				if (!(currentAddressMode == AddressMode.IMP || currentAddressMode == AddressMode.ACC || currentAddressMode == AddressMode.REL)) {
					if (currentAddressMode == AddressMode.IMM) {
						System.out.print("#$"+Integer.toHexString(Byte.toUnsignedInt(fetched)));
					} else if (currentAddressMode == AddressMode.REL) { //fixme REL is being filtered out here, is that correct?
						System.out.print("$"+Integer.toHexString(Byte.toUnsignedInt((byte)addressAbsolute)));
					} else {
						System.out.print("$"+Integer.toHexString(Short.toUnsignedInt(addressAbsolute)));
					}
				} else if (!(currentAddressMode == AddressMode.IMP || currentAddressMode == AddressMode.ACC)) {
					System.out.print("$"+Integer.toHexString(Short.toUnsignedInt(addressRelative)));
				}
				if (currentAddressMode == AddressMode.ABX || currentAddressMode == AddressMode.IZX || currentAddressMode == AddressMode.ZPX) {
					System.out.print(",X");
				} else if (currentAddressMode == AddressMode.ABY || currentAddressMode == AddressMode.IZY || currentAddressMode == AddressMode.ZPY) {
					System.out.print(",Y");
				}
				System.out.print("  A:"+Integer.toHexString(Byte.toUnsignedInt(a))+" X:"+Integer.toHexString(Byte.toUnsignedInt(x))+" Y:"+Integer.toHexString(Byte.toUnsignedInt(y))+" Flags:"+ROMLoader.padStringWithZeroes(Integer.toBinaryString(Byte.toUnsignedInt(flags)), 8));
				if (EaterEmulator.verbose) System.out.println();
			}

			if (DisplayPanel.breakpoints.contains(programCounter)) EaterEmulator.clockState = false;
		}

		EaterEmulator.clocks++;

		cycles--;

		if (cycles < 0) {
			cycles = 0;
		}
		CpuStatePublisher.notifyListeners(buildState());
	}

	private CpuState buildState() {
		return new CpuState(
				EaterEmulator.clocks,
				ClocksPerSecond,
				EaterEmulator.slowerClock,
				programCounter,
				stackPointer,
				flags,
				a,
				x,
				y,
				addressAbsolute,
				addressRelative,
				opcode,
				InstructionSet.getByAddress(opcode),
				cycles,
				EaterEmulator.ram.getData(),
				EaterEmulator.rom.getData()
		);
	}

	void executeAddressModeFunction(AddressMode addressMode) {
		switch (addressMode) {
			case ACC, IMP -> IMP();
			case ABS -> ABS();
			case ABX -> ABX();
			case ABY -> ABY();
			case IMM -> IMM();
			case IND -> IND();
			case IZX -> IZX();
			case IZY -> IZY();
			case REL -> REL();
			case ZPP -> ZPP();
			case ZPX -> ZPX();
			case ZPY -> ZPY();
			case ZPI -> ZPI();
			case IAX -> IAX();
			default -> {
				if (EaterEmulator.verbose)
					System.out.println("Something has gone seriously wrong! AddressMode: " + addressMode);
			}
		}
	}

	void executeOpcodeFunction(OpCode opcode) {
		switch (opcode) {
			case ADC -> ADC();
			case AND -> AND();
			case ASL -> ASL();
			case BBR0 -> BBR0();
			case BBR1 -> BBR1();
			case BBR2 -> BBR2();
			case BBR3 -> BBR3();
			case BBR4 -> BBR4();
			case BBR5 -> BBR5();
			case BBR6 -> BBR6();
			case BBR7 -> BBR7();
			case BBS0 -> BBS0();
			case BBS1 -> BBS1();
			case BBS2 -> BBS2();
			case BBS3 -> BBS3();
			case BBS4 -> BBS4();
			case BBS5 -> BBS5();
			case BBS6 -> BBS6();
			case BBS7 -> BBS7();
			case BCC -> BCC();
			case BCS -> BCS();
			case BEQ -> BEQ();
			case BIT -> BIT();
			case BMI -> BMI();
			case BNE -> BNE();
			case BPL -> BPL();
			case BRA -> BRA();
			case BRK -> BRK();
			case BVC -> BVC();
			case BVS -> BVS();
			case CLC -> CLC();
			case CLD -> CLD();
			case CLI -> CLI();
			case CLV -> CLV();
			case CMP -> CMP();
			case CPX -> CPX();
			case CPY -> CPY();
			case DEC -> DEC();
			case DEX -> DEX();
			case DEY -> DEY();
			case EOR -> EOR();
			case INC -> INC();
			case INX -> INX();
			case INY -> INY();
			case JMP -> JMP();
			case JSR -> JSR();
			case LDA -> LDA();
			case LDX -> LDX();
			case LDY -> LDY();
			case LSR -> LSR();
			case NOP -> NOP();
			case ORA -> ORA();
			case PHA -> PHA();
			case PHP -> PHP();
			case PHX -> PHX();
			case PHY -> PHY();
			case PLA -> PLA();
			case PLP -> PLP();
			case PLX -> PLX();
			case PLY -> PLY();
			case RMB0 -> RMB0();
			case RMB1 -> RMB1();
			case RMB2 -> RMB2();
			case RMB3 -> RMB3();
			case RMB4 -> RMB4();
			case RMB5 -> RMB5();
			case RMB6 -> RMB6();
			case RMB7 -> RMB7();
			case ROL -> ROL();
			case ROR -> ROR();
			case RTI -> RTI();
			case RTS -> RTS();
			case SBC -> SBC();
			case SEC -> SEC();
			case SED -> SED();
			case SEI -> SEI();
			case SMB0 -> SMB0();
			case SMB1 -> SMB1();
			case SMB2 -> SMB2();
			case SMB3 -> SMB3();
			case SMB4 -> SMB4();
			case SMB5 -> SMB5();
			case SMB6 -> SMB6();
			case SMB7 -> SMB7();
			case STA -> STA();
			case STP -> STP();
			case STX -> STX();
			case STY -> STY();
			case STZ -> STZ();
			case TAX -> TAX();
			case TAY -> TAY();
			case TRB -> TRB();
			case TSB -> TSB();
			case TSX -> TSX();
			case TXA -> TXA();
			case TXS -> TXS();
			case TYA -> TYA();
			case WAI -> WAI();
			case XXX -> XXX();
			default -> {
				if (EaterEmulator.verbose)
					System.out.println("Something has gone seriously wrong! OpCode: " + opcode);
			}
		}
	}

	//Input Signal Handlers
	void reset() {
		stopped = false;
		waiting = false;

		EaterEmulator.clockState = false;
		if (EaterEmulator.serial != null) EaterEmulator.serial.reset();

		a = 0;
		x = 0;
		y = 0;
		stackPointer = (byte)0xFD;
		flags = (byte)(getFlag('U') ? 0b00000100 : 0);

		addressAbsolute = (short)(0xFFFC);

		byte lo = Bus.read(addressAbsolute);
		byte hi = Bus.read((short)(addressAbsolute+1));
		programCounter = (short)(Byte.toUnsignedInt(lo)+256*Byte.toUnsignedInt(hi));

		EaterEmulator.clocks = 0;
		ClocksPerSecond = 0;

		addressRelative = 0;
		addressAbsolute = 0;
		fetched = 0;

		cycles = 8;

		startTime = System.currentTimeMillis();

		opcode = Bus.read(programCounter);

		CpuStatePublisher.notifyListeners(buildState());
	}

	void irq() {
		if (!getFlag('I')) {
			if (debug)
				if (EaterEmulator.verbose) System.out.println("Interrupted!");

			Bus.write((short)(0x0100+Byte.toUnsignedInt(stackPointer)), (byte)(programCounter>>8));
			stackPointer--;
			Bus.write((short)(0x0100+Byte.toUnsignedInt(stackPointer)), (byte)(programCounter));
			stackPointer--;

			setFlag('B',false);
			setFlag('U',false);
			Bus.write((short)(0x0100+Byte.toUnsignedInt(stackPointer)), flags);
			stackPointer--;
			setFlag('I',true);

			addressAbsolute = (short)(0xFFFE);
			byte lo = Bus.read(addressAbsolute);
			byte hi = Bus.read((short)(addressAbsolute+1));
			programCounter = (short)(Byte.toUnsignedInt(lo)+256*Byte.toUnsignedInt(hi));

			cycles = 7;
		}
		interruptRequested = false;
	}

	void nmi() {
		Bus.write((short)(0x0100+Byte.toUnsignedInt(stackPointer)), (byte)(programCounter>>8));
		stackPointer--;
		Bus.write((short)(0x0100+Byte.toUnsignedInt(stackPointer)), (byte)(programCounter));
		stackPointer--;

		setFlag('B',false);
		setFlag('U',false);
		Bus.write((short)(0x0100+Byte.toUnsignedInt(stackPointer)), flags);
		stackPointer--;
		setFlag('I',true);

		addressAbsolute = (short)(0xFFFA);
		byte lo = Bus.read(addressAbsolute);
		byte hi = Bus.read((short)(addressAbsolute+1));
		programCounter = (short)(Byte.toUnsignedInt(lo)+256*Byte.toUnsignedInt(hi));

		cycles = 7;
		NMinterruptRequested = false;
	}

	//Data Getter
	byte fetched = 0x00;
	byte fetch() {
		if (!(InstructionSet.getByAddress(opcode).addressMode == AddressMode.IMP || InstructionSet.getByAddress(opcode).addressMode == AddressMode.ACC))
			fetched = Bus.read(addressAbsolute);
		return fetched;
	}

	//Addressing Modes
	public void IMP() {
		fetched = a;
	}

	public void IMM() {
		addressAbsolute = programCounter++;
	}

	public void ZPP() {
		addressAbsolute = Bus.read(programCounter++);
		addressAbsolute &= 0x00FF;
	}

	public void ZPX() {
		addressAbsolute = (short)(Byte.toUnsignedInt(Bus.read(programCounter++))+Byte.toUnsignedInt(x));
		addressAbsolute &= 0x00FF;
	}

	public void ZPY() {
		addressAbsolute = (short)(Byte.toUnsignedInt(Bus.read(programCounter++))+Byte.toUnsignedInt(y));
		addressAbsolute &= 0x00FF;
	}

	public void REL() {
		addressRelative = Bus.read(programCounter++);
		if ((addressRelative & 0x80)==0x80)
			addressRelative |= (short) 0xFF00;
	}

	public void ABS() {
		byte lo = Bus.read(programCounter++);
		byte hi = Bus.read(programCounter++);

		addressAbsolute = (short)(Byte.toUnsignedInt(lo)+256*Byte.toUnsignedInt(hi));
	}

	public void ABX() {
		byte lo = Bus.read(programCounter++);
		byte hi = Bus.read(programCounter++);

		addressAbsolute = (short)(Byte.toUnsignedInt(lo)+256*Byte.toUnsignedInt(hi)+Byte.toUnsignedInt(x));

		if ((addressAbsolute & 0xFF00) != (hi<<8))
			additionalCycles++;
	}

	public void ABY() {
		byte lo = Bus.read(programCounter++);
		byte hi = Bus.read(programCounter++);

		addressAbsolute = (short)(Byte.toUnsignedInt(lo)+256*Byte.toUnsignedInt(hi)+Byte.toUnsignedInt(y));

		if ((addressAbsolute & 0xFF00) != (hi<<8))
			additionalCycles++;
	}

	public void IND() {
 		short lowPointer = (short)(Bus.read(programCounter++)&0xff);
		short highPointer = (short)(Bus.read(programCounter++)&0xff);

		short pointer = (short)((highPointer << 8) | lowPointer);

		addressAbsolute = (short)(Byte.toUnsignedInt(Bus.read((short)(pointer+1)))*256+Byte.toUnsignedInt(Bus.read(pointer)));
	}

	public void IZX() {
		byte t = Bus.read(programCounter++);

		byte lo = Bus.read((short)((t+x)&0x00FF));
		byte hi = Bus.read((short)((t+x+1)&0x00FF));

		addressAbsolute = (short)(Byte.toUnsignedInt(lo)+256*Byte.toUnsignedInt(hi));
	}

	public void IZY() {
		byte t = Bus.read(programCounter++);

		byte lo = Bus.read((short)(t&0x00FF));
		byte hi = Bus.read((short)((t+1)&0x00FF));

		addressAbsolute = (short)(Byte.toUnsignedInt(lo)+256*Byte.toUnsignedInt(hi)+Byte.toUnsignedInt(y));

		if ((addressAbsolute & 0xFF00) != (hi<<8))
			additionalCycles++;
	}

	public void ZPI() {
		short lowPointer = (short) (Bus.read(programCounter++) & 0x00ff);

		addressAbsolute = (short)(Byte.toUnsignedInt(Bus.read((short)(lowPointer+1)))*256+Byte.toUnsignedInt(Bus.read(lowPointer)));
	}

	public void IAX() {
		byte lowPointer = Bus.read(programCounter++);
		byte highPointer = Bus.read(programCounter++);

		short pointer = (short)((highPointer << 8) | lowPointer);

		byte lo = Bus.read((short) (pointer+x));
		byte hi = Bus.read((short) (pointer+x+1));

		addressAbsolute = (short)(Byte.toUnsignedInt(lo)+256*Byte.toUnsignedInt(hi));
	}

	//INSTRUCTIONS
	public void ADC() {
		fetch();
		short temp = (short)((short)Byte.toUnsignedInt(a) + (short)Byte.toUnsignedInt(fetched) + (short)(getFlag('C') ? 1 : 0));
		setFlag('C', temp > 255);
		setFlag('Z', (temp & 0x00FF) == 0);
		setFlag('N', (temp & 0x80) == 0x80);
		setFlag('V', (~((short)a^(short)fetched) & ((short)a^(short)temp) & 0x0080)==0x0080);
		a = (byte)temp;
		additionalCycles++;
	}

	public void AND() {
		fetch();
		a &= fetched;
		setFlag('Z', a==0x00);
		setFlag('N', (a & 0x80)==0x80);

		additionalCycles++;
	}

	public void ASL() {
		fetch();
		short temp = (short)(fetched << 1);
		setFlag('Z', (temp & 0x00FF)==0x00);
		setFlag('C', Short.toUnsignedInt((short)(temp & 0xFF00)) > 0);
		setFlag('N', (temp & 0x80)==0x80);

		if (InstructionSet.getByAddress(opcode).addressMode == AddressMode.ACC) {
			a = (byte)(temp & 0x00FF);
		} else {
			Bus.write(addressAbsolute, (byte)(temp & 0x00FF));
		}
	}

	public void BBR0() { BBRn(0); }
	public void BBR1() { BBRn(1); }
	public void BBR2() { BBRn(2); }
	public void BBR3() { BBRn(3); }
	public void BBR4() { BBRn(4); }
	public void BBR5() { BBRn(5); }
	public void BBR6() { BBRn(6); }
	public void BBR7() { BBRn(7); }

	private void BBRn(int n) {
		if ((a & (0b1<<n)) == 0) {
			cycles++;
			addressAbsolute = (short)(programCounter+addressRelative);

			if ((addressAbsolute&0xFF00) != (programCounter & 0xFF00))
				cycles++;

			programCounter = addressAbsolute;
		}
	}

	public void BBS0() { BBSn(0); }
	public void BBS1() { BBSn(1); }
	public void BBS2() { BBSn(2); }
	public void BBS3() { BBSn(3); }
	public void BBS4() { BBSn(4); }
	public void BBS5() { BBSn(5); }
	public void BBS6() { BBSn(6); }
	public void BBS7() { BBSn(7); }

	private void BBSn(int n) {
		if ((a & (0b1<<n)) != 0) {
			cycles++;
			addressAbsolute = (short)(programCounter+addressRelative);

			if ((addressAbsolute&0xFF00) != (programCounter & 0xFF00))
				cycles++;

			programCounter = addressAbsolute;
		}
	}

	public void BCC() {
		if (!getFlag('C')) {
			cycles++;
			addressAbsolute = (short)(programCounter+addressRelative);

			if ((addressAbsolute&0xFF00) != (programCounter & 0xFF00))
				cycles++;

			programCounter = addressAbsolute;
		}
	}

	public void BCS() {
		if (getFlag('C')) {
			cycles++;
			addressAbsolute = (short)(programCounter+addressRelative);

			if ((addressAbsolute&0xFF00) != (programCounter & 0xFF00))
				cycles++;

			programCounter = addressAbsolute;
		}
	}

	public void BEQ() {
		if (getFlag('Z')) {
			cycles++;
			addressAbsolute = (short)(programCounter+addressRelative);

			if ((addressAbsolute&0xFF00) != (programCounter & 0xFF00))
				cycles++;

			programCounter = addressAbsolute;
		}
	}

	public void BIT() {
		fetch();
		short temp = (short)(a&fetched);
		setFlag('Z',(temp&0x00FF)==0x00);
		setFlag('N',(fetched&0x80)==0x80);
		setFlag('V',(fetched&0x40)==0x40);
	}

	public void BMI() {
		if (getFlag('N')) {
			cycles++;
			addressAbsolute = (short)(programCounter+addressRelative);

			if ((addressAbsolute&0xFF00) != (programCounter & 0xFF00))
				cycles++;

			programCounter = addressAbsolute;
		}
	}

	public void BNE() {
		if (!getFlag('Z')) {
			cycles++;
			addressAbsolute = (short)(programCounter+addressRelative);

			if ((addressAbsolute&0xFF00) != (programCounter & 0xFF00))
				cycles++;

			programCounter = addressAbsolute;
		}
	}

	public void BPL() {
		if (!getFlag('N')) {
			cycles++;
			addressAbsolute = (short)(programCounter+addressRelative);

			if ((addressAbsolute&0xFF00) != (programCounter & 0xFF00))
				cycles++;

			programCounter = addressAbsolute;
		}
	}

	public void BRA() {
		cycles++;
		addressAbsolute = (short)(programCounter+addressRelative);

		if ((addressAbsolute&0xFF00) != (programCounter & 0xFF00))
			cycles++;

		programCounter = addressAbsolute;
	}

	public void BRK() {
		programCounter++;

		Bus.write((short)(0x0100+Byte.toUnsignedInt(stackPointer)), (byte)(programCounter>>8));
		stackPointer--;
		Bus.write((short)(0x0100+Byte.toUnsignedInt(stackPointer)), (byte)(programCounter));
		stackPointer--;

		setFlag('B',true);
		setFlag('U',true);
		Bus.write((short)(0x0100+Byte.toUnsignedInt(stackPointer)), flags);
		stackPointer--;
		setFlag('I',true);

		addressAbsolute = (short)0xFFFE;
		byte lo = Bus.read(addressAbsolute);
		byte hi = Bus.read((short)(addressAbsolute+1));
		programCounter = (short)(Byte.toUnsignedInt(lo)+256*Byte.toUnsignedInt(hi));
	}

	public void BVC() {
		if (!getFlag('V')) {
			cycles++;
			addressAbsolute = (short)(programCounter+addressRelative);

			if ((addressAbsolute&0xFF00) != (programCounter & 0xFF00))
				cycles++;

			programCounter = addressAbsolute;
		}
	}

	public void BVS() {
		if (getFlag('V')) {
			cycles++;
			addressAbsolute = (short)(programCounter+addressRelative);

			if ((addressAbsolute&0xFF00) != (programCounter & 0xFF00))
				cycles++;

			programCounter = addressAbsolute;
		}
	}

	public void CLC() {
		setFlag('C',false);
	}

	public void CLD() {
		setFlag('D',false);
	}

	public void CLI() {
		setFlag('I',false);
	}

	public void CLV() {
		setFlag('V',false);
	}

	public void CMP() {
		fetch();
		short temp = (short)(Byte.toUnsignedInt(a) - Byte.toUnsignedInt(fetched));
		setFlag('C',Byte.toUnsignedInt(a) >= Byte.toUnsignedInt(fetched));
		setFlag('Z',(temp&0x00FF)==0x0000);
		setFlag('N',(temp&0x0080)==0x0080);

		additionalCycles++;
	}

	public void CPX() {
		fetch();
		short temp = (short)(Byte.toUnsignedInt(x) - Byte.toUnsignedInt(fetched));
		setFlag('C',Byte.toUnsignedInt(x) >= Byte.toUnsignedInt(fetched));
		setFlag('Z',(temp&0x00FF)==0x0000);
		setFlag('N',(temp&0x0080)==0x0080);
	}

	public void CPY() {
		fetch();
		short temp = (short)(Byte.toUnsignedInt(y) - Byte.toUnsignedInt(fetched));
		setFlag('C',Byte.toUnsignedInt(y) >= Byte.toUnsignedInt(fetched));
		setFlag('Z',(temp&0x00FF)==0x0000);
		setFlag('N',(temp&0x0080)==0x0080);
	}

	public void DEC() {
		fetch();
		int temp = (Byte.toUnsignedInt(fetched)-1);
		if (InstructionSet.getByAddress(opcode).addressMode == AddressMode.ACC) {
			a = (byte)(temp&0x00FF);
		} else {
			Bus.write(addressAbsolute, (byte)(temp&0x00FF));
		}
		setFlag('Z',(temp&0x00FF)==0x0000);
		setFlag('N',(temp&0x0080)==0x0080);
	}

	public void DEX() {
		x--;
		setFlag('Z',x==0x00);
		setFlag('N',(x&0x80)==0x80);
	}

	public void DEY() {
		y--;
		setFlag('Z',y==0x00);
		setFlag('N',(y&0x80)==0x80);
	}

	public void EOR() {
		fetch();
		a ^= fetched;
		setFlag('Z', a==0x00);
		setFlag('N', (a & 0x80)==0x80);

		additionalCycles++;
	}

	public void INC() {
		fetch();
		short temp = (short)(fetched+1);
		if (InstructionSet.getByAddress(opcode).addressMode == AddressMode.ACC) {
			a = (byte)(temp&0x00FF);
		} else {
			Bus.write(addressAbsolute, (byte)(temp&0x00FF));
		}
		setFlag('Z',(temp&0x00FF)==0x0000);
		setFlag('N',(temp&0x0080)==0x0080);
	}

	public void INX() {
		x++;
		setFlag('Z',x==0x00);
		setFlag('N',(x&0x80)==0x80);
	}

	public void INY() {
		y++;
		setFlag('Z',y==0x00);
		setFlag('N',(y&0x80)==0x80);
	}

	public void JMP() {
		programCounter = addressAbsolute;
	}

	public void JSR() {
		programCounter--;

		Bus.write((short)(0x0100+Byte.toUnsignedInt(stackPointer)), (byte)((programCounter>>8)&0x00FF));
		stackPointer--;
		Bus.write((short)(0x0100+Byte.toUnsignedInt(stackPointer)), (byte)(programCounter&0x00FF));
		stackPointer--;

		programCounter = addressAbsolute;
	}

	public void LDA() {
		fetch();
		a = fetched;
		setFlag('Z',a==0x00);
		setFlag('N',(a&0x80)==0x80);
		additionalCycles++;
	}

	public void LDX() {
		fetch();
		x = fetched;
		setFlag('Z',x==0x00);
		setFlag('N',(x&0x80)==0x80);
		additionalCycles++;
	}

	public void LDY() {
		fetch();
		y = fetched;
		setFlag('Z',y==0x00);
		setFlag('N',(y&0x80)==0x80);
		additionalCycles++;
	}

	public void LSR() {
		fetch();
		setFlag('C',(fetched&0x0001)==0x0001);
		short temp = (short)((0x00FF&fetched) >> 1);
		setFlag('Z',(temp&0x00FF)==0x0000);
		setFlag('N',(temp&0x0080)==0x0080);
		if (InstructionSet.getByAddress(opcode).addressMode == AddressMode.ACC) {
			a = (byte)((byte)(temp)&0x00FF);
		} else {
			Bus.write(addressAbsolute, (byte)(temp&0x00FF));
		}
	}

	public void NOP() {
		additionalCycles++;
	}

	public void ORA() {
		fetch();
		a |= fetched;
		setFlag('Z', a==0x00);
		setFlag('N', (a & 0x80)==0x80);

		additionalCycles++;
	}

	public void PHA() {
		Bus.write((short)(0x0100+Byte.toUnsignedInt(stackPointer)), a);
		stackPointer--;
	}

	public void PHP() {
		Bus.write((short)(0x0100+Byte.toUnsignedInt(stackPointer)), (byte)(flags|0b00110000));
		setFlag('B',false);
		setFlag('U',false);
		stackPointer--;
	}

	public void PHX() {
		Bus.write((short)(0x0100+Byte.toUnsignedInt(stackPointer)), x);
		stackPointer--;
	}

	public void PHY() {
		Bus.write((short)(0x0100+Byte.toUnsignedInt(stackPointer)), y);
		stackPointer--;
	}

	public void PLA() {
		stackPointer++;
		a = Bus.read((short)(0x0100+Byte.toUnsignedInt(stackPointer)));
		setFlag('Z', a == 0);
		setFlag('N', (a & 0x80) == 0x80);
	}

	public void PLP() {
		stackPointer++;
		flags = Bus.read((short)(0x0100+Byte.toUnsignedInt(stackPointer)));
		setFlag('U', true);
	}

	public void PLX() {
		stackPointer++;
		x = Bus.read((short)(0x0100+Byte.toUnsignedInt(stackPointer)));
		setFlag('Z', x == 0);
		setFlag('N', (x & 0x80) == 0x80);
	}

	public void PLY() {
		stackPointer++;
		y = Bus.read((short)(0x0100+Byte.toUnsignedInt(stackPointer)));
		setFlag('Z', y == 0);
		setFlag('N', (y & 0x80) == 0x80);
	}

	public void RMB0() { RMBn(0); }
	public void RMB1() { RMBn(1); }
	public void RMB2() { RMBn(2); }
	public void RMB3() { RMBn(3); }
	public void RMB4() { RMBn(4); }
	public void RMB5() { RMBn(5); }
	public void RMB6() { RMBn(6); }
	public void RMB7() { RMBn(7); }

	private void RMBn(int n) {
		fetch();
		short temp = (short)(fetched&(0xff^(0b1<<n))); // reset the nth bit with a mask of all 1's except a 0 in the nth place
		Bus.write(addressAbsolute, (byte)(temp&0x00FF));
	}

	public void ROL() {
		fetch();
		short temp = (short)((fetched<<1) | (getFlag('C') ? 1 : 0));
		setFlag('C',(temp&0xFF00) == 0xFF00);
		setFlag('Z',(temp&0x00FF) == 0x0000);
		setFlag('N',(temp&0x0080) == 0x0080);
		if (InstructionSet.getByAddress(opcode).addressMode == AddressMode.ACC) {
			a = (byte)(temp&0x00FF);
		} else {
			Bus.write(addressAbsolute, (byte)(temp&0x00FF));
		}
	}

	public void ROR() {
		fetch();
		short temp = (short)(((0x00FF&fetched)>>1) | (short)(getFlag('C') ? 0x0080 : 0));
		setFlag('C',(fetched&0x01) == 0x01);
		setFlag('Z',(temp&0x00FF) == 0x0000);
		setFlag('N',(temp&0x0080) == 0x0080);
		if (InstructionSet.getByAddress(opcode).addressMode == AddressMode.ACC) {
			a = (byte)(temp&0x00FF);
		} else {
			Bus.write(addressAbsolute, (byte)(temp&0x00FF));
		}
	}

	public void RTI() {
		stackPointer++;
		flags = Bus.read((short)(0x100+Byte.toUnsignedInt(stackPointer)));
		flags = (byte)(flags & (getFlag('B') ? 0b11101111 : 0));
		flags = (byte)(flags & (getFlag('U') ? 0b11011111 : 0));

		stackPointer++;
		byte lo = Bus.read((short)(0x100+Byte.toUnsignedInt(stackPointer)));
		stackPointer++;
		byte hi = Bus.read((short)(0x100+Byte.toUnsignedInt(stackPointer)));
		programCounter = (short)(Byte.toUnsignedInt(lo)+256*Byte.toUnsignedInt(hi));
	}

	public void RTS() {
		stackPointer++;
		byte lo = Bus.read((short)(0x100+Byte.toUnsignedInt(stackPointer)));
		stackPointer++;
		byte hi = Bus.read((short)(0x100+Byte.toUnsignedInt(stackPointer)));
		programCounter = (short)(Byte.toUnsignedInt(lo)+256*Byte.toUnsignedInt(hi));

		programCounter++;
	}

	public void SBC() {
		fetch();
		short value = (short)(((short)fetched&0xff) ^ (short)0x00FF);
		short temp = (short)(((short)a&0xff) + value + (short)(getFlag('C') ? 1 : 0));
		setFlag('C', temp > 255);
		setFlag('Z', (temp & 0x00FF) == 0);
		setFlag('N', (temp & 0x80) == 0x80);
		setFlag('V', (~((short)a^(short)fetched) & ((short)a^(short)temp) & 0x0080)==0x0080);
		a = (byte)temp;
		additionalCycles++;
	}

	public void SEC() {
		setFlag('C',true);
	}

	public void SED() {
		setFlag('D',true);
	}

	public void SEI() {
		setFlag('I',true);
	}

	public void SMB0() { SMBn(0); }
	public void SMB1() { SMBn(1); }
	public void SMB2() { SMBn(2); }
	public void SMB3() { SMBn(3); }
	public void SMB4() { SMBn(4); }
	public void SMB5() { SMBn(5); }
	public void SMB6() { RMBn(6); }
	public void SMB7() { SMBn(7); }

	private void SMBn(int n) {
		fetch();
		short temp = (short)(fetched|(0b1<<n)); // set the nth bit
		Bus.write(addressAbsolute, (byte)(temp&0x00FF));
	}

	public void STA() {
		Bus.write(addressAbsolute, a);
	}

	public void STP() {
		stopped = true;
	}

	public void STX() {
		Bus.write(addressAbsolute, x);
	}

	public void STY() {
		Bus.write(addressAbsolute, y);
	}

	public void STZ() {
		Bus.write(addressAbsolute, (byte)0);
	}
		
	public void TAX() {
		x = a;
		setFlag('Z',x==0x00);
		setFlag('N',(x&0x80)==0x80);
	}
		
	public void TAY() {
		y = a;
		setFlag('Z',y==0x00);
		setFlag('N',(y&0x80)==0x80);
	}

	public void TRB() {
		fetch();
		short temp = (short)(a&fetched);
		setFlag('Z',(temp&0x00FF)==0x00);
		Bus.write(addressAbsolute, (byte)((temp^0x00FF)&0x00FF));
	}

	public void TSB() {
		fetch();
		short temp = (short)(a&fetched);
		setFlag('Z',(temp&0x00FF)==0x00);
		Bus.write(addressAbsolute, (byte)(temp&0x00FF));
	}

	public void TSX() {
		x = stackPointer;
		setFlag('Z',x==0x00);
		setFlag('N',(x&0x80)==0x80);
	}
		
	public void TXA() {
		a = x;
		setFlag('Z',a==0x00);
		setFlag('N',(a&0x80)==0x80);
	}
		
	public void TXS() {
		stackPointer = x;
	}
		
	public void TYA() {
		a = y;
		setFlag('Z',a==0x00);
		setFlag('N',(a&0x80)==0x80);
	}

	public void WAI() {
		waiting = true;
	}

	public void XXX() {
		if (EaterEmulator.verbose) System.out.println("Illegal Opcode at $"+Integer.toHexString(Short.toUnsignedInt(programCounter)).toUpperCase()+" (" + ROMLoader.byteToHexString(opcode) +") - "+InstructionSet.getByAddress(opcode).opcode);
	}
}
