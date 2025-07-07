package memory;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadOnlyMemoryTest {
	@Test
	public void shouldInitializeROM() {
		ReadOnlyMemory rom = new ReadOnlyMemory(0x8000);

		assertThat(rom.getData()).hasSize(0x8000);
		for (byte b : rom.getData()) {
			assertThat(b).isEqualTo((byte) 0);
		}
	}

	@Test
	public void shouldSetRomFromArray() {
		byte[] testArray = createTestArray(0x8);
		ReadOnlyMemory rom = new ReadOnlyMemory(testArray);

		assertThat(rom.getData()).isEqualTo(testArray);
	}

	@Test
	public void shouldReadByteFromROM() {
		byte[] testArray = createTestArray(0x8000);
		ReadOnlyMemory rom = new ReadOnlyMemory(testArray);

		assertThat(rom.read((short) 0)).isEqualTo((byte) 0);
		assertThat(rom.read((short) 1)).isEqualTo((byte) 1);
		assertThat(rom.read((short) 255)).isEqualTo((byte) 255);
		assertThat(rom.read((short) 256)).isEqualTo((byte) 0);
	}

	@Test
	public void shouldReadMultipleBytesFromROM() {
		byte[] testArray = createTestArray(0x8000);
		ReadOnlyMemory rom = new ReadOnlyMemory(testArray);

		assertThat(rom.read((short) 0, 10))
				.hasSize(10)
				.containsExactly(
						(byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4,
						(byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9
				);
	}

	@Test
	public void shouldConvertROMToString() {
		byte[] testArray = createTestArray(0x8);
		ReadOnlyMemory rom = new ReadOnlyMemory(testArray);

		String romString = rom.formatToString(8, true);
		assertThat(romString).isEqualTo("0000: 00 01 02 03 04 05 06 07");
	}

	@Test
	public void shouldConvertROMToStringWithPartialLines() {
		byte[] testArray = createTestArray(0x6);
		ReadOnlyMemory rom = new ReadOnlyMemory(testArray);

		String romString = rom.formatToString(8, true);
		assertThat(romString).isEqualTo("0000: 00 01 02 03 04 05");
	}

	@Test
	public void shouldConvertROMToStringWithoutAddresses() {
		byte[] testArray = createTestArray(0x8);
		ReadOnlyMemory rom = new ReadOnlyMemory(testArray);

		String romString = rom.formatToString(8, false);
		assertThat(romString).isEqualTo("00 01 02 03 04 05 06 07");
	}

	@Test
	public void shouldConvertROMToStringWithOffset() {
		byte[] testArray = createTestArray(0x8);
		ReadOnlyMemory rom = new ReadOnlyMemory(testArray, (short) 0x8000);

		String romString = rom.formatToString(8, true);
		assertThat(romString).isEqualTo("8000: 00 01 02 03 04 05 06 07");
	}

	private byte[] createTestArray(int size) {
		byte[] testArray = new byte[size];
		for (int i1 = 0; i1 < testArray.length; i1++) {
			testArray[i1] = ((Function<Integer, Byte>) i -> (byte) (i % 256)).apply(i1);
		}
		return testArray;
	}
}
