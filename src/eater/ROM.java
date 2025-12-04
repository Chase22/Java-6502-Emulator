package eater;

import eater.memory.ReadOnlyMemory;

public class ROM extends ReadOnlyMemory {
	public final static short ADDRESS_OFFSET = (short) 0x8000;
	public String ROMString = "";

	public ROM() {
		super(0x8000, ADDRESS_OFFSET);
		ROMString = this.formatToString(8, true);
	}

	public ROM(byte[] theArray) {
		super(theArray, ADDRESS_OFFSET);
		ROMString = this.formatToString(8, true);
	}

	public void setROMArray(byte[] array) {
		this.data = array;
		ROMString = this.formatToString(8, true);
	}

	public String toString(int bytesPerLine, boolean addresses) {
		return formatToString(bytesPerLine, addresses);
	}
}
