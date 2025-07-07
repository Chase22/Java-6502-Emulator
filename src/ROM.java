import memory.ReadOnlyMemory;

public class ROM extends ReadOnlyMemory {
	public String ROMString = "";

	public ROM() {
		super(0x8000, (short) 0x8000);
		ROMString = this.formatToString(8, true);
	}

	public ROM(byte[] theArray) {
		super(theArray, (short) 0x8000);
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
