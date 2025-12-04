package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HexDumpFormatter {
	/**
	 * Returns a hex dump representation of the memory.
	 * @param bytesPerLine Number of bytes to display per line.
	 * @param addresses Whether to include addresses in the output.
	 * @return A string representation of the memory in hex format.
	 */
	public static String hexDump(byte[] data, int bytesPerLine, boolean addresses, int addressOffset) {
		var addressConverter = new AddressConverter(addressOffset);

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
}
