package cpu;

public record CpuState(
		int clocks,
		double speed,
		boolean slowerClock,
		short programCounter,
		byte stackPointer,
		byte flags,
		byte a,
		byte x,
		byte y,
		short addressAbsolute,
		short addressRelative,
		byte opcode,
		Instruction instruction,
		int cycles,
		byte[] ram,
		byte[] rom
) {
}

