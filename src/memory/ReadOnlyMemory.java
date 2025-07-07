package memory;

import utils.AddressConverter;
import utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadOnlyMemory {
	protected byte[] data;
	protected final int addressOffset;
	protected final AddressConverter addressConverter;

	/**
	 * Constructor for ReadOnlyMemory with specified data and no address offset.
	 * @param data Byte array containing the initial data for the memory.
	 */
	public ReadOnlyMemory(byte[] data) {
		this(data, (short) 0);
	}

	/**
	 * Constructor for ReadOnlyMemory with specified data and address offset.
	 * @param data Byte array containing the initial data for the memory.
	 * @param addressOffset Offset of the memory in the address space.
	 */
	public ReadOnlyMemory(byte[] data, short addressOffset) {
		this.data = data.clone();
		this.addressOffset = Short.toUnsignedInt(addressOffset);
		this.addressConverter = new AddressConverter(this.addressOffset);
	}

	/**
	 * Default constructor for ReadOnlyMemory with no address offset.
	 * @param size Size of the memory in bytes.
	 */
	public ReadOnlyMemory(int size) {
		this(size, (short) 0);
	}

	/**
	 * Constructor for ReadOnlyMemory with specified size and address offset.
	 * @param size Size of the memory in bytes.
	 * @param addressOffset Offset of the memory in the address space.
	 */
	public ReadOnlyMemory(int size, short addressOffset) {
		this(new byte[size], addressOffset);
		Arrays.fill(data, (byte) 0);
	}

	public byte read(short address) {
		return read(address, 1)[0];
	};

	byte[] read(short address, int length) {
		int startAddress = addressConverter.toInternalAddress(address);

		if (startAddress < 0) {
			throw new IndexOutOfBoundsException("Address cannot be negative: " + address);
		}

		if (startAddress + length > data.length) {
			throw new IndexOutOfBoundsException("Read exceeds memory bounds: %04X + %d".formatted(address, length));
		}

		return Arrays.copyOfRange(data, startAddress, startAddress+length);
	};

	/**
	 * Returns a copy of the data in this memory.
	 * @return A byte array containing the data in this memory.
	 */
	public byte[] getData() {
		return data.clone();
	}

	/**
	 * Returns a hex dump representation of the memory.
	 * @param bytesPerLine Number of bytes to display per line.
	 * @param addresses Whether to include addresses in the output.
	 * @return A string representation of the memory in hex format.
	 */
	public String formatToString(int bytesPerLine, boolean addresses) {
		List<byte[]> chunks = new ArrayList<>();
		int chunkAmount = (int) Math.ceil(data.length / (double) bytesPerLine);

		for (int i = 0; i < chunkAmount; i++) {
			int end = Math.min(data.length, (i + 1) * bytesPerLine);
			byte[] chunk = Arrays.copyOfRange(data, i * bytesPerLine, end);
			chunks.add(chunk);
		}

		var lines = CollectionUtils.withIndex(chunks).stream().map(indexedValue -> {
					var localSb = new StringBuilder();
					Byte[] chunk = CollectionUtils.toBoxedArray(indexedValue.value());

					if (addresses)
						localSb.append(String.format("%04X: ",  addressConverter.toExternalAddress(indexedValue.index() * bytesPerLine)));

					localSb.append(String.join(" ", CollectionUtils.mapArray(chunk, (b -> String.format("%02X", b)))));
					return localSb.toString();
				}
		).toList();

		return String.join(System.lineSeparator(), lines);
	}

	/**
	 * Returns a hex dump representation of the memory with default parameters. (8 bytes per line, addresses included).
	 * @return A string representation of the memory in hex format.
	 */
	public String formatToString() {
		return formatToString(8, true);
	}
}
