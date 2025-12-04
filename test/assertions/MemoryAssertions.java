package assertions;

import memory.ReadOnlyMemory;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ByteArrayAssert;
import org.assertj.core.api.ByteAssert;

import java.util.Arrays;

import static java.lang.Double.doubleToLongBits;

public class MemoryAssertions extends AbstractAssert<MemoryAssertions, ReadOnlyMemory> {
	protected MemoryAssertions(ReadOnlyMemory actual) {
		super(actual, MemoryAssertions.class);
	}

	public static MemoryAssertions assertThat(ReadOnlyMemory actual) {
		return new MemoryAssertions(actual);
	}


	public MemoryAssertions hasValueAt(int address, int value) {
		double uShortSize = Math.pow(2, Short.SIZE) - 1;
		double uByteSize = Math.pow(2, Byte.SIZE) - 1;

		if (address < 0 || address > uShortSize) {
			throw new IllegalArgumentException("Address out of range. Must be between 0 and %d, was %d".formatted((long) uShortSize, address));
		}
		if (value < 0 || value > uByteSize) {
			throw new IllegalArgumentException("Value out of range. Must be between 0 and %d, was %d".formatted((long) uByteSize, value));
		}
		return hasValueAt((short) address, (byte) value);
	}

	public MemoryAssertions hasValueAt(short address, byte value) {
		var actualValue = actual.read(address);

		if (actualValue != value) {
			failWithMessage("Expect value at %04X to be %02X but was %02X", address, value, actualValue);
		}
		return this;
	}

	public MemoryAssertions hasValuesAt(short address, int length, byte[] values) {
		new ByteArrayAssert(actual.read(address, length)).containsExactly(values);
		return this;
	}
}
