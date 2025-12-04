package eater.cpu;

import java.util.Arrays;
//instructions\[(0x[0-9A-F]+)\] = new Instruction\((OpCode.([A-Z0-9]+)), (AddressMode.([A-Z]+)), (\d+), (true|false)\);$

public enum InstructionSet {
	// useful reference for instructions: https://www.masswerk.at/6502/6502_instruction_set.html

	// Invalid OpCode evaluates to NOOP
	XXX(-1, OpCode.XXX, AddressMode.IMP, 2, false),

	//ADC
	ADC_IMM(0x69, OpCode.ADC, AddressMode.IMM, 2, false),
	ADC_ZPP(0x65, OpCode.ADC, AddressMode.ZPP, 3, false),
	ADC_ZPX(0x75, OpCode.ADC, AddressMode.ZPX, 4, false),
	ADC_ABS(0x6D, OpCode.ADC, AddressMode.ABS, 4, false),
	ADC_ABX(0x7D, OpCode.ADC, AddressMode.ABX, 4, false),
	ADC_ABY(0x79, OpCode.ADC, AddressMode.ABY, 4, false),
	ADC_IZX(0x61, OpCode.ADC, AddressMode.IZX, 6, false),
	ADC_IZY(0x71, OpCode.ADC, AddressMode.IZY, 5, false),
	ADC_ZPI(0x72, OpCode.ADC, AddressMode.ZPI, 5, true),

	AND_IMM(0x29, OpCode.AND, AddressMode.IMM, 2, false),
	AND_ZPP(0x25, OpCode.AND, AddressMode.ZPP, 3, false),
	AND_ZPX(0x35, OpCode.AND, AddressMode.ZPX, 4, false),
	AND_ABS(0x2D, OpCode.AND, AddressMode.ABS, 4, false),
	AND_ABX(0x3D, OpCode.AND, AddressMode.ABX, 4, false),
	AND_ABY(0x39, OpCode.AND, AddressMode.ABY, 4, false),
	AND_IZX(0x21, OpCode.AND, AddressMode.IZX, 6, false),
	AND_IZY(0x31, OpCode.AND, AddressMode.IZY, 5, false),
	AND_ZPI(0x32, OpCode.AND, AddressMode.ZPI, 5, true),

	ASL_ACC(0x0A, OpCode.ASL, AddressMode.ACC, 2, false),
	ASL_ZPP(0x06, OpCode.ASL, AddressMode.ZPP, 5, false),
	ASL_ZPX(0x16, OpCode.ASL, AddressMode.ZPX, 6, false),
	ASL_ABS(0x0E, OpCode.ASL, AddressMode.ABS, 6, false),
	ASL_ABX(0x1E, OpCode.ASL, AddressMode.ABX, 7, false),

	BBR0_ZPP(0x0F, OpCode.BBR0, AddressMode.ZPP, 5, true),
	BBR1_ZPP(0x1F, OpCode.BBR1, AddressMode.ZPP, 5, true),
	BBR2_ZPP(0x2F, OpCode.BBR2, AddressMode.ZPP, 5, true),
	BBR3_ZPP(0x3F, OpCode.BBR3, AddressMode.ZPP, 5, true),
	BBR4_ZPP(0x4F, OpCode.BBR4, AddressMode.ZPP, 5, true),
	BBR5_ZPP(0x5F, OpCode.BBR5, AddressMode.ZPP, 5, true),
	BBR6_ZPP(0x6F, OpCode.BBR6, AddressMode.ZPP, 5, true),
	BBR7_ZPP(0x7F, OpCode.BBR7, AddressMode.ZPP, 5, true),

	BBS0_ZPP(0x8F, OpCode.BBS0, AddressMode.ZPP, 5, true),
	BBS1_ZPP(0x9F, OpCode.BBS1, AddressMode.ZPP, 5, true),
	BBS2_ZPP(0xAF, OpCode.BBS2, AddressMode.ZPP, 5, true),
	BBS3_ZPP(0xBF, OpCode.BBS3, AddressMode.ZPP, 5, true),
	BBS4_ZPP(0xCF, OpCode.BBS4, AddressMode.ZPP, 5, true),
	BBS5_ZPP(0xDF, OpCode.BBS5, AddressMode.ZPP, 5, true),
	BBS6_ZPP(0xEF, OpCode.BBS6, AddressMode.ZPP, 5, true),
	BBS7_ZPP(0xFF, OpCode.BBS7, AddressMode.ZPP, 5, true),

	BCC_REL(0x90, OpCode.BCC, AddressMode.REL, 2, false),

	BCS_REL(0xB0, OpCode.BCS, AddressMode.REL, 2, false),

	BEQ_REL(0xF0, OpCode.BEQ, AddressMode.REL, 2, false),

	BIT_IMM(0x89, OpCode.BIT, AddressMode.IMM, 2, true),
	BIT_ZPP(0x24, OpCode.BIT, AddressMode.ZPP, 3, false),
	BIT_ABS(0x2C, OpCode.BIT, AddressMode.ABS, 4, false),
	BIT_ZPX(0x34, OpCode.BIT, AddressMode.ZPX, 4, true),
	BIT_ABX(0x3C, OpCode.BIT, AddressMode.ABX, 4, true),

	BMI_REL(0x30, OpCode.BMI, AddressMode.REL, 2, false),

	BNE_REL(0xD0, OpCode.BNE, AddressMode.REL, 2, false),

	BPL_REL(0x10, OpCode.BPL, AddressMode.REL, 2, false),

	BRA_REL(0x80, OpCode.BRA, AddressMode.REL, 2, true),

	BRK_IMP(0x00, OpCode.BRK, AddressMode.IMP, 2, false),

	BVC_REL(0x50, OpCode.BVC, AddressMode.REL, 2, false),

	BVS_REL(0x70, OpCode.BVS, AddressMode.REL, 2, false),

	CLC_IMP(0x18, OpCode.CLC, AddressMode.IMP, 2, false),

	CLD_IMP(0xD8, OpCode.CLD, AddressMode.IMP, 2, false),

	CLI_IMP(0x58, OpCode.CLI, AddressMode.IMP, 2, false),

	CLV_IMP(0xB8, OpCode.CLV, AddressMode.IMP, 2, false),

	CMP_IMM(0xC9, OpCode.CMP, AddressMode.IMM, 2, false),
	CMP_ZPP(0xC5, OpCode.CMP, AddressMode.ZPP, 3, false),
	CMP_ZPX(0xD5, OpCode.CMP, AddressMode.ZPX, 4, false),
	CMP_ABS(0xCD, OpCode.CMP, AddressMode.ABS, 4, false),
	CMP_ABX(0xDD, OpCode.CMP, AddressMode.ABX, 4, false),
	CMP_ABY(0xD9, OpCode.CMP, AddressMode.ABY, 4, false),
	CMP_IZX(0xC1, OpCode.CMP, AddressMode.IZX, 6, false),
	CMP_IZY(0xD1, OpCode.CMP, AddressMode.IZY, 5, false),
	CMP_ZPI(0xD2, OpCode.CMP, AddressMode.ZPI, 5, true),

	CPX_IMM(0xE0, OpCode.CPX, AddressMode.IMM, 2, false),
	CPX_ZPP(0xE4, OpCode.CPX, AddressMode.ZPP, 3, false),
	CPX_ABS(0xEC, OpCode.CPX, AddressMode.ABS, 4, false),

	CPY_IMM(0xC0, OpCode.CPY, AddressMode.IMM, 2, false),
	CPY_ZPP(0xC4, OpCode.CPY, AddressMode.ZPP, 3, false),
	CPY_ABS(0xCC, OpCode.CPY, AddressMode.ABS, 4, false),

	DEC_ACC(0x3A, OpCode.DEC, AddressMode.ACC, 2, true),
	DEC_ZPP(0xC6, OpCode.DEC, AddressMode.ZPP, 5, false),
	DEC_ZPX(0xD6, OpCode.DEC, AddressMode.ZPX, 6, false),
	DEC_ABS(0xCE, OpCode.DEC, AddressMode.ABS, 6, false),
	DEC_ABX(0xDE, OpCode.DEC, AddressMode.ABX, 7, false),

	DEX_IMP(0xCA, OpCode.DEX, AddressMode.IMP, 2, false),

	DEY_IMP(0x88, OpCode.DEY, AddressMode.IMP, 2, false),

	EOR_IMM(0x49, OpCode.EOR, AddressMode.IMM, 2, false),
	EOR_ZPP(0x45, OpCode.EOR, AddressMode.ZPP, 3, false),
	EOR_ZPX(0x55, OpCode.EOR, AddressMode.ZPX, 4, false),
	EOR_ABS(0x4D, OpCode.EOR, AddressMode.ABS, 4, false),
	EOR_ABX(0x5D, OpCode.EOR, AddressMode.ABX, 4, false),
	EOR_ABY(0x59, OpCode.EOR, AddressMode.ABY, 4, false),
	EOR_IZX(0x41, OpCode.EOR, AddressMode.IZX, 6, false),
	EOR_IZY(0x51, OpCode.EOR, AddressMode.IZY, 5, false),
	EOR_ZPI(0x52, OpCode.EOR, AddressMode.ZPI, 5, true),

	INC_ACC(0x1A, OpCode.INC, AddressMode.ACC, 2, true),
	INC_ZPP(0xE6, OpCode.INC, AddressMode.ZPP, 5, false),
	INC_ZPX(0xF6, OpCode.INC, AddressMode.ZPX, 6, false),
	INC_ABS(0xEE, OpCode.INC, AddressMode.ABS, 6, false),
	INC_ABX(0xFE, OpCode.INC, AddressMode.ABX, 7, false),

	INX_IMP(0xE8, OpCode.INX, AddressMode.IMP, 2, false),

	INY_IMP(0xC8, OpCode.INY, AddressMode.IMP, 2, false),

	JMP_ABS(0x4C, OpCode.JMP, AddressMode.ABS, 3, false),
	JMP_IND(0x6C, OpCode.JMP, AddressMode.IND, 5, false),

	JSR_ABS(0x20, OpCode.JSR, AddressMode.ABS, 6, false),

	LDA_IMM(0xA9, OpCode.LDA, AddressMode.IMM, 2, false),
	LDA_ZPP(0xA5, OpCode.LDA, AddressMode.ZPP, 3, false),
	LDA_ZPX(0xB5, OpCode.LDA, AddressMode.ZPX, 4, false),
	LDA_ABS(0xAD, OpCode.LDA, AddressMode.ABS, 4, false),
	LDA_ABX(0xBD, OpCode.LDA, AddressMode.ABX, 4, false),
	LDA_ABY(0xB9, OpCode.LDA, AddressMode.ABY, 4, false),
	LDA_IZX(0xA1, OpCode.LDA, AddressMode.IZX, 6, false),
	LDA_IZY(0xB1, OpCode.LDA, AddressMode.IZY, 5, false),
	LDA_ZPI(0xB2, OpCode.LDA, AddressMode.ZPI, 5, true),

	LDX_IMM(0xA2, OpCode.LDX, AddressMode.IMM, 2, false),
	LDX_ZPP(0xA6, OpCode.LDX, AddressMode.ZPP, 3, false),
	LDX_ZPY(0xB6, OpCode.LDX, AddressMode.ZPY, 4, false),
	LDX_ABS(0xAE, OpCode.LDX, AddressMode.ABS, 4, false),
	LDX_ABY(0xBE, OpCode.LDX, AddressMode.ABY, 4, false),

	LDY_IMM(0xA0, OpCode.LDY, AddressMode.IMM, 2, false),
	LDY_ZPP(0xA4, OpCode.LDY, AddressMode.ZPP, 3, false),
	LDY_ZPX(0xB4, OpCode.LDY, AddressMode.ZPX, 4, false),
	LDY_ABS(0xAC, OpCode.LDY, AddressMode.ABS, 4, false),
	LDY_ABX(0xBC, OpCode.LDY, AddressMode.ABX, 4, false),

	LSR_ACC(0x4A, OpCode.LSR, AddressMode.ACC, 2, false),
	LSR_ZPP(0x46, OpCode.LSR, AddressMode.ZPP, 5, false),
	LSR_ZPX(0x56, OpCode.LSR, AddressMode.ZPX, 6, false),
	LSR_ABS(0x4E, OpCode.LSR, AddressMode.ABS, 6, false),
	LSR_ABX(0x5E, OpCode.LSR, AddressMode.ABX, 7, false),

	NOP_IMP(0xEA, OpCode.NOP, AddressMode.IMP, 2, false),

	ORA_IMM(0x09, OpCode.ORA, AddressMode.IMM, 2, false),
	ORA_ZPP(0x05, OpCode.ORA, AddressMode.ZPP, 3, false),
	ORA_ZPX(0x15, OpCode.ORA, AddressMode.ZPX, 4, false),
	ORA_ABS(0x0D, OpCode.ORA, AddressMode.ABS, 4, false),
	ORA_ABX(0x1D, OpCode.ORA, AddressMode.ABX, 4, false),
	ORA_ABY(0x19, OpCode.ORA, AddressMode.ABY, 4, false),
	ORA_IZX(0x01, OpCode.ORA, AddressMode.IZX, 6, false),
	ORA_IZY(0x11, OpCode.ORA, AddressMode.IZY, 5, false),
	ORA_ZPI(0x12, OpCode.ORA, AddressMode.ZPI, 5, true),

	PHA_IMP(0x48, OpCode.PHA, AddressMode.IMP, 3, false),

	PHP_IMP(0x08, OpCode.PHP, AddressMode.IMP, 3, false),

	PHX_IMP(0xDA, OpCode.PHX, AddressMode.IMP, 3, true),

	PHY_IMP(0x5A, OpCode.PHY, AddressMode.IMP, 3, true),

	PLA_IMP(0x68, OpCode.PLA, AddressMode.IMP, 4, false),

	PLP_IMP(0x28, OpCode.PLP, AddressMode.IMP, 4, false),

	PLX_IMP(0xFA, OpCode.PLX, AddressMode.IMP, 4, true),

	PLY_IMP(0x7A, OpCode.PLY, AddressMode.IMP, 4, true),

	RMB0_ZPP(0x07, OpCode.RMB0, AddressMode.ZPP, 5, true),
	RMB1_ZPP(0x17, OpCode.RMB1, AddressMode.ZPP, 5, true),
	RMB2_ZPP(0x27, OpCode.RMB2, AddressMode.ZPP, 5, true),
	RMB3_ZPP(0x37, OpCode.RMB3, AddressMode.ZPP, 5, true),
	RMB4_ZPP(0x47, OpCode.RMB4, AddressMode.ZPP, 5, true),
	RMB5_ZPP(0x57, OpCode.RMB5, AddressMode.ZPP, 5, true),
	RMB6_ZPP(0x67, OpCode.RMB6, AddressMode.ZPP, 5, true),
	RMB7_ZPP(0x77, OpCode.RMB7, AddressMode.ZPP, 5, true),

	ROL_ACC(0x2A, OpCode.ROL, AddressMode.ACC, 2, false),
	ROL_ZPP(0x26, OpCode.ROL, AddressMode.ZPP, 5, false),
	ROL_ZPX(0x36, OpCode.ROL, AddressMode.ZPX, 6, false),
	ROL_ABS(0x2E, OpCode.ROL, AddressMode.ABS, 6, false),
	ROL_ABX(0x3E, OpCode.ROL, AddressMode.ABX, 7, false),

	ROR_ACC(0x6A, OpCode.ROR, AddressMode.ACC, 2, false),
	ROR_ZPP(0x66, OpCode.ROR, AddressMode.ZPP, 5, false),
	ROR_ZPX(0x76, OpCode.ROR, AddressMode.ZPX, 6, false),
	ROR_ABS(0x6E, OpCode.ROR, AddressMode.ABS, 6, false),
	ROR_ABX(0x7E, OpCode.ROR, AddressMode.ABX, 7, false),

	RTI_IMP(0x40, OpCode.RTI, AddressMode.IMP, 6, false),

	RTS_IMP(0x60, OpCode.RTS, AddressMode.IMP, 6, false),

	SBC_IMM(0xE9, OpCode.SBC, AddressMode.IMM, 2, false),
	SBC_ZPP(0xE5, OpCode.SBC, AddressMode.ZPP, 3, false),
	SBC_ZPX(0xF5, OpCode.SBC, AddressMode.ZPX, 4, false),
	SBC_ABS(0xED, OpCode.SBC, AddressMode.ABS, 4, false),
	SBC_ABX(0xFD, OpCode.SBC, AddressMode.ABX, 4, false),
	SBC_ABY(0xF9, OpCode.SBC, AddressMode.ABY, 4, false),
	SBC_IZX(0xE1, OpCode.SBC, AddressMode.IZX, 6, false),
	SBC_IZY(0xF1, OpCode.SBC, AddressMode.IZY, 5, false),
	SBC_ZPI(0xF2, OpCode.SBC, AddressMode.ZPI, 5, true),

	SEC_IMP(0x38, OpCode.SEC, AddressMode.IMP, 2, false),

	SED_IMP(0xF8, OpCode.SED, AddressMode.IMP, 2, false),

	SEI_IMP(0x78, OpCode.SEI, AddressMode.IMP, 2, false),

	SMB0_ZPP(0x87, OpCode.SMB0, AddressMode.ZPP, 5, true),
	SMB1_ZPP(0x97, OpCode.SMB1, AddressMode.ZPP, 5, true),
	SMB2_ZPP(0xA7, OpCode.SMB2, AddressMode.ZPP, 5, true),
	SMB3_ZPP(0xB7, OpCode.SMB3, AddressMode.ZPP, 5, true),
	SMB4_ZPP(0xC7, OpCode.SMB4, AddressMode.ZPP, 5, true),
	SMB5_ZPP(0xD7, OpCode.SMB5, AddressMode.ZPP, 5, true),
	SMB6_ZPP(0xE7, OpCode.SMB6, AddressMode.ZPP, 5, true),
	SMB7_ZPP(0xF7, OpCode.SMB7, AddressMode.ZPP, 5, true),

	STA_ZPP(0x85, OpCode.STA, AddressMode.ZPP, 3, false),
	STA_ZPX(0x95, OpCode.STA, AddressMode.ZPX, 4, false),
	STA_ABS(0x8D, OpCode.STA, AddressMode.ABS, 4, false),
	STA_ABX(0x9D, OpCode.STA, AddressMode.ABX, 5, false),
	STA_ABY(0x99, OpCode.STA, AddressMode.ABY, 5, false),
	STA_IZX(0x81, OpCode.STA, AddressMode.IZX, 6, false),
	STA_IZY(0x91, OpCode.STA, AddressMode.IZY, 6, false),
	STA_ZPI(0x92, OpCode.STA, AddressMode.ZPI, 5, true),

	STP_IMP(0xDB, OpCode.STP, AddressMode.IMP, 3, true),

	STX_ZPP(0x86, OpCode.STX, AddressMode.ZPP, 3, false),
	STX_ZPY(0x96, OpCode.STX, AddressMode.ZPY, 4, false),
	STX_ABS(0x8E, OpCode.STX, AddressMode.ABS, 4, false),

	STY_ZPP(0x84, OpCode.STY, AddressMode.ZPP, 3, false),
	STY_ZPX(0x94, OpCode.STY, AddressMode.ZPX, 4, false),
	STY_ABS(0x8C, OpCode.STY, AddressMode.ABS, 4, false),

	STZ_ZPP(0x64, OpCode.STZ, AddressMode.ZPP, 3, true),
	STZ_ZPX(0x74, OpCode.STZ, AddressMode.ZPX, 4, true),
	STZ_ABS(0x9C, OpCode.STZ, AddressMode.ABS, 4, true),
	STZ_ABX(0x9E, OpCode.STZ, AddressMode.ABX, 4, true),

	TAX_IMP(0xAA, OpCode.TAX, AddressMode.IMP, 2, false),

	TAY_IMP(0xA8, OpCode.TAY, AddressMode.IMP, 2, false),

	TRB_ZPP(0x14, OpCode.TRB, AddressMode.ZPP, 5, false),
	TRB_ABS(0x1C, OpCode.TRB, AddressMode.ABS, 6, false),

	TSB_ZPP(0x04, OpCode.TSB, AddressMode.ZPP, 5, false),
	TSB_ABS(0x0C, OpCode.TSB, AddressMode.ABS, 6, false),

	TSX_IMP(0xBA, OpCode.TSX, AddressMode.IMP, 2, false),

	TXA_IMP(0x8A, OpCode.TXA, AddressMode.IMP, 2, false),

	TXS_IMP(0x9A, OpCode.TXS, AddressMode.IMP, 2, false),

	TYA_IMP(0x98, OpCode.TYA, AddressMode.IMP, 2, false),

	WAI_IMP(0xCB, OpCode.WAI, AddressMode.IMP, 3, true);

	public final int address;
	public final OpCode opcode;
	public final AddressMode addressMode;
	public final int cycles;
	public final boolean wdc;

	/**
	 * Lookup for all instructions by their binary opcode.
	 * Prefer using {@link InstructionSet#getByAddress(byte)} and its overloads
	 */
	public static final InstructionSet[] lookup;

	static {
		lookup = new InstructionSet[0x100];
		Arrays.fill(lookup, InstructionSet.XXX);

		// Filter out the Invalid Instruction to avoid errors when adding it to the lookup
		Arrays.stream(values()).filter(value -> value != InstructionSet.XXX).forEach(value -> lookup[value.address] = value);
	}

	/**
	 * Returns the instruction for the given address.
	 * @param address The binary address to get the instruction for
	 * @return The {@link InstructionSet} for the given address or {@link InstructionSet#XXX InstructionSet.XXX} if the address is OutOfRange or not set to a valid opcode
	 */
	public static InstructionSet getByAddress(int address) {
		var instruction = getByAddressOrNull(address);

		return instruction == null ? InstructionSet.XXX : instruction;
	}

	/**
	 * Returns the instruction for the given address.
	 * @param address The binary address to get the instruction for
	 * @return The {@link InstructionSet} for the given address or {@link InstructionSet#XXX InstructionSet.XXX} if the address is OutOfRange or not set to a valid opcode
	 */
	public static InstructionSet getByAddress(byte address) {
		return getByAddress(Byte.toUnsignedInt(address));
	}


	/**
	 * Returns the instruction for the given address.
	 * @param address The binary address to get the instruction for
	 * @return The {@link InstructionSet} for the given address or null if the address is OutOfRange or not set to a valid opcode
	 */
	public static InstructionSet getByAddressOrNull(int address) {
		if (address < 0 || address > lookup.length) return null;

		var instruction = lookup[address];
		if (instruction.opcode == OpCode.XXX) return null;

		return instruction;
	}

	InstructionSet(int address, OpCode opcode, AddressMode addressMode, int cycles, boolean wdc) {
		this.address = address;
		this.opcode = opcode;
		this.addressMode = addressMode;
		this.cycles = cycles;
		this.wdc = wdc;
	}

	@Override
	public String toString() {
		return opcode+","+addressMode;
	}
}
