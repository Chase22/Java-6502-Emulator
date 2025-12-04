package eater.cpu;

import assertions.CpuAssertions;
import eater.EaterEmulator;
import eater.ROM;
import org.junit.jupiter.api.Test;
import testutils.CpuTestBuilder;
import testutils.CpuTestUtils;

public class CpuTest {

	@Test
	void testADC_IMM() {
		new CpuTestBuilder()
				.withProgramCounter(ROM.ADDRESS_OFFSET) // Set program counter to point to ROM
				.withRom(ramBuilder -> {
					ramBuilder.addInstruction(InstructionSet.ADC_IMM);
					ramBuilder.addValue(5);
				}).build();

		// Run the actual test
		CpuTestUtils.clockCpuUntilNextInstruction(EaterEmulator.cpu);

		CpuAssertions.asserThat(EaterEmulator.cpu).registerAIs(5);
	}

}
