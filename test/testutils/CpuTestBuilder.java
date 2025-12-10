package testutils;

import eater.EaterEmulator;
import eater.RAM;
import eater.ROM;
import eater.cpu.CPU;
import eater.memory.RandomAccessMemory;
import eater.memory.ReadOnlyMemory;
import eater.utils.CollectionUtils;

import java.util.Arrays;
import java.util.function.Consumer;

public class CpuTestBuilder {
	// fixme: Fix once we have a proper encapsulation setup
	private final CPU cpu = EaterEmulator.cpu;
	private final ReadOnlyMemory rom = EaterEmulator.rom;
	private final RandomAccessMemory ram = EaterEmulator.ram;

	public CpuTestBuilder() {
		cpu.reset();
	}

	public CpuTestBuilder withAccVal(int value) {
		cpu.a = (byte) value;
		return this;
	}

	public CpuTestBuilder withXVal(int value) {
		cpu.x = (byte) value;
		return this;
	}

	public CpuTestBuilder withYVal(int value) {
		cpu.y = (byte) value;
		return this;
	}

	public CpuTestBuilder withProgramCounter(short programCounter) {
		cpu.programCounter = programCounter;
		return this;
	}

	public CpuTestBuilder withProgramCounter(int programCounter) {
		return withProgramCounter((short) programCounter);
	}

	public CpuTestBuilder withRamData(Number... data) {
		CollectionUtils.withIndex(Arrays.stream(data).toList())
				.forEach(indexValue -> ram.write(
						(short) indexValue.index(),
						indexValue.value().byteValue())
				);
		return this;
	}

	public CpuTestBuilder withRam(Consumer<MemoryBuilder<RAM>> ramBuilder) {
		var builder = MemoryBuilder.forRam(EaterEmulator.ram);
		ramBuilder.accept(builder);
		builder.build();
		return this;
	}

	public CpuTestBuilder withRom(Consumer<MemoryBuilder<ROM>> ramBuilder) {
		var builder = MemoryBuilder.forRom(EaterEmulator.rom);
		ramBuilder.accept(builder);
		builder.build();
		return this;
	}

	public CPU build() {
		// We want to make sure that the CPU is set up and ready to run the program
		cpu.cycles = 0;
		return cpu;
	}
}
