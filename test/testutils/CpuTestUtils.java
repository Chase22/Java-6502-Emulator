package testutils;

import eater.cpu.CPU;

public class CpuTestUtils {
	public static void clockCpuUntilNextInstruction(CPU cpu) {
		do {
			cpu.clock();
		} while (cpu.cycles != 0);
	}
}
