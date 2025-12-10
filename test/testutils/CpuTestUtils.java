package testutils;

import eater.Bus;
import eater.cpu.CPU;
import eater.cpu.InstructionSet;

public class CpuTestUtils {
	public static void clockCpuUntilNextInstruction(CPU cpu) {
		do {
			cpu.clock();
		} while (cpu.cycles != 0);
	}

	public static void clockCpuUntilBreak(CPU cpu) {
		InstructionSet current = null;
		do {
			cpu.clock();
			if (cpu.cycles == 0) {
				current = InstructionSet.getByAddress(Bus.read(cpu.programCounter));
			}
		} while (cpu.cycles != 0 && current != InstructionSet.BRK_IMP && current != InstructionSet.XXX);
	}
}
