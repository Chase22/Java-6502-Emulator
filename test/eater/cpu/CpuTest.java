package eater.cpu;

import assertions.CpuAssertions;
import eater.EaterEmulator;
import org.junit.jupiter.api.Test;
import testutils.CpuTestBuilder;
import testutils.CpuTestUtils;

public class CpuTest {

	@Test
	void testADC_IMM() {
		new CpuTestBuilder()
				.withProgramCounter((short) 0x8000) // Set program counter to point to ROM
				.withRom(ramBuilder -> {
					ramBuilder.addInstruction(InstructionSet.ADC_IMM);
					ramBuilder.addValue(5);
				}).build();

		// Run the actual test
		CpuTestUtils.clockCpuUntilNextInstruction(EaterEmulator.cpu);

		CpuAssertions.asserThat(EaterEmulator.cpu).registerAIs((byte) 5);
	}

}
