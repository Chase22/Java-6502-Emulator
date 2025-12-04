package testutils;

import eater.RAM;
import eater.ROM;
import eater.cpu.InstructionSet;
import eater.memory.ReadOnlyMemory;

import java.util.PrimitiveIterator;
import java.util.stream.IntStream;

public abstract class MemoryBuilder<T extends ReadOnlyMemory> {
	protected final byte[] data;
	protected final PrimitiveIterator.OfInt indices;

	public MemoryBuilder(T memory) {
		this.data = new byte[memory.getData().length];
		indices = IntStream.range(0, memory.getData().length).iterator();
	}

	public MemoryBuilder<T> addValue(byte value) {
		data[indices.nextInt()] = value;
		return this;
	}

	public MemoryBuilder<T> addValue(Number value) {
		return addValue(value.byteValue());
	}

	public MemoryBuilder<T> addInstruction(InstructionSet instruction) {
		return addValue(instruction.address);
	}

	public abstract T build();

	public static MemoryBuilder<ROM> forRom(ROM memory) {
		return new RomBuilder(memory);
	}

	public static MemoryBuilder<RAM> forRam(RAM memory) {
		return new RamBuilder(memory);
	}

	public static class RamBuilder extends MemoryBuilder<RAM> {
		private final RAM memory;

		public RamBuilder(RAM memory) {
			super(memory);
			this.memory = memory;
		}

		public RAM build() {
			memory.setRAMArray(data);
			return memory;
		}
	}

	public static class RomBuilder extends MemoryBuilder<ROM> {
		private final ROM memory;

		public RomBuilder(ROM memory) {
			super(memory);
			this.memory = memory;
		}

		public ROM build() {
			memory.setROMArray(data);
			return memory;
		}
	}



}
