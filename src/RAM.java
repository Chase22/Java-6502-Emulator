import memory.RandomAccessMemory;

public class RAM extends RandomAccessMemory {
	public String RAMString;
	
	public RAM() {
		super(0x8000);
		RAMString = this.formatToString();
	}
	
	public RAM(byte[] theArray) {
		super(theArray);
		RAMString = this.formatToString();
	}

	/**
	 * @deprecated Use {@link #getData()} instead to retrieve the RAM array.
	 */
	@Deprecated
	public byte[] getRAMArray() {
		return getData();
	}

	/**
	 * @deprecated Avoid replacing the entire RAM array. Create a new instance if needed.
	 */
	@Deprecated
	public void setRAMArray(byte[] array) {
		this.data = array;
		RAMString = this.formatToString();
	}

	@Override
	public void write(short address, byte data) {
		super.write(address, data);
		RAMString = this.formatToString();
	}
}
