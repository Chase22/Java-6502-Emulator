import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class RomTest {
	@Test
	public void shouldInitializeROM() {
		ROM rom = new ROM();

		assertThat(rom.getROMArray()).hasSize(0x8000);
		for (byte b : rom.getROMArray()) {
			assertThat(b).isEqualTo((byte) 0);
		}
	}

	@Test
	public void shouldSetRomFromArray() {
		ROM rom = new ROM();
		byte[] testArray = createTestArray(0x8);

		rom.setROMArray(testArray);

		assertThat(rom.getROMArray()).isEqualTo(testArray);
	}

	@Test
	public void shouldReadByteFromROM() {
		ROM rom = new ROM();
		byte[] testArray = createTestArray(0x8000);
		rom.setROMArray(testArray);

		assertThat(rom.read((short) 0)).isEqualTo((byte) 0);
		assertThat(rom.read((short) 1)).isEqualTo((byte) 1);
		assertThat(rom.read((short) 255)).isEqualTo((byte) 255);
		assertThat(rom.read((short) 256)).isEqualTo((byte) 0);
	}

	@Test
	public void shouldConvertROMToString() {
		byte[] testArray = createTestArray(0x8);
		ROM rom = new ROM(testArray);

		String romString = rom.toString(8, true);
		assertThat(romString).isEqualTo("0000: 00 01 02 03 04 05 06 07");
	}

	@Test
	public void shouldConvertROMToStringWithPartialLines() {
		byte[] testArray = createTestArray(0x6);
		ROM rom = new ROM(testArray);

		String romString = rom.toString(8, true);
		assertThat(romString).isEqualTo("0000: 00 01 02 03 04 05");
	}

	@Test
	public void shouldConvertROMToStringWithoutAddresses() {
		byte[] testArray = createTestArray(0x8);
		ROM rom = new ROM(testArray);

		String romString = rom.toString(8, false);
		assertThat(romString).isEqualTo("00 01 02 03 04 05 06 07");
	}

	@Test
	public void shouldConvertROMToStringWithOffset() {
		byte[] testArray = createTestArray(0x8);
		ROM rom = new ROM(testArray);

		String romString = rom.toStringWithOffset(8, 0x8000, true);
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
