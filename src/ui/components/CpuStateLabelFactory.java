package ui.components;

import cpu.CpuState;

import java.util.function.Function;

public class CpuStateLabelFactory {
	private CpuStateLabelFactory() {}

	public static CpuStateLabel forByte(String label, Function<CpuState, Byte> extractor) {
		return new CpuStateLabel(state -> {
			Byte value = extractor.apply(state);
			return String.format("%s: %s (%02X)", label, toBinaryPadded(Byte.toUnsignedInt(value), 8), Byte.toUnsignedInt(value));
		});
	}

	public static CpuStateLabel forShort(String label, Function<CpuState, Short> extractor) {
		return new CpuStateLabel(state -> {
			Short value = extractor.apply(state);
			return String.format("%s: %s (%04X)", label, toBinaryPadded(Short.toUnsignedInt(value), 16), Short.toUnsignedInt(value));
		});
	}

	public static CpuStateLabel forFlags() {
		return new CpuStateLabel(state -> {
			String flagChars = "NVUBDIZC";
			byte flags = state.flags();
			String formattedFlags = "";
			for (int i = 0; i < flagChars.length(); i++) {
				char flagChar = flagChars.charAt(i);
				boolean isSet = (flags & (1 << 7-i)) != 0; // we have to iterate the byte left to right instead of right-to-left

				// lime == #00FF00
				String formattedFlag = isSet ? "<font color='lime'>%c</font>" : "<font color='red'>%c</font>";
				formattedFlags += String.format(formattedFlag, flagChar) + " ";
			}
			return String.format("<html><b>Flags: %s (%02X)</b></html>", formattedFlags, Byte.toUnsignedInt(flags));
		});
	}

	public static CpuStateLabel forOpCode() {
		return new CpuStateLabel(state -> String.format("Opcode: %s (%02X)", state.instruction(), state.opcode()));
	}

	public static String toBinaryPadded(short value, int lengthPadded) {
		return toBinaryPadded(Short.toUnsignedInt(value), lengthPadded);
	}

	public static String toBinaryPadded(int value, int lengthPadded) {
		String fullString = "0".repeat(lengthPadded) + Integer.toBinaryString(value);
		return fullString.substring(fullString.length()-lengthPadded);
	}
}
