package eater.cpu;

import eater.EaterEmulator;
import eater.ROM;
import org.junit.jupiter.api.Test;
import testutils.CpuTestBuilder;
import testutils.CpuTestUtils;

import java.util.Arrays;

import static assertions.CpuAssertions.asserThat;

public class CpuTest {

	@Test
	void testADC_IMM() {
		var cpuTestBuilder = new CpuTestBuilder().withAccVal(5);
		runInstruction(cpuTestBuilder,InstructionSet.ADC_IMM, 5);

		asserThat(EaterEmulator.cpu).registerAIs(10);
	}

	@Test
	void testADC_ZPP() {
		short address = (short) 0x00A0;
		EaterEmulator.ram.write(address, (byte) 5);
		runInstruction(InstructionSet.ADC_ZPP, address);
		asserThat(EaterEmulator.cpu).registerAIs(5);
	}

	@Test
	void testADC_ZPX() {
		short address = (short) 0xA0;
		short offset = (short) 0x0A;
		EaterEmulator.ram.write((short) (address+offset), (byte) 5);
		CpuTestBuilder builder = new CpuTestBuilder().withXVal(offset);
		runInstruction(builder, InstructionSet.ADC_ZPX, address);
		asserThat(EaterEmulator.cpu).registerAIs(5);
	}

	@Test
	void testADC_ABX() {
		short address = (short) 0x0F00;
		short offset = (short) 0x00AA;
		EaterEmulator.ram.write((short) (address+offset), (byte) 5);
		CpuTestBuilder builder = new CpuTestBuilder().withXVal(offset);
		runInstruction(builder, InstructionSet.ADC_ABX, 0x00, 0x0F);
		asserThat(EaterEmulator.cpu).registerAIs(5);
	}

	@Test
	void testADC_ABY() {
		short address = (short) 0x0F00;
		short offset = (short) 0x00AA;
		EaterEmulator.ram.write((short) (address+offset), (byte) 5);
		CpuTestBuilder builder = new CpuTestBuilder().withYVal(offset);
		runInstruction(builder, InstructionSet.ADC_ABY, 0x00, 0x0F);
		asserThat(EaterEmulator.cpu).registerAIs(5);
	}

	private void runInstruction(CpuTestBuilder builder, InstructionSet instruction, int... args) {
		CpuTestBuilder cpuTestBuilder = builder == null ? new CpuTestBuilder() : builder;

		cpuTestBuilder = cpuTestBuilder
				.withProgramCounter(ROM.ADDRESS_OFFSET)
				.withRom(romBuilder -> {
					romBuilder.addInstruction(instruction);
					Arrays.stream(args).forEach(romBuilder::addValue);
				});

		cpuTestBuilder.build();

		CpuTestUtils.clockCpuUntilBreak(EaterEmulator.cpu);
	}

	private void runInstruction(InstructionSet instruction, int... args) {
		runInstruction(null, instruction, args);
	}

}
