package memory;

public class RandomAccessMemory extends ReadOnlyMemory {
	public RandomAccessMemory(byte[] data) {
		super(data);
	}

	public RandomAccessMemory(byte[] data, short addressOffset) {
		super(data, addressOffset);
	}

	public RandomAccessMemory(int size) {
		super(size);
	}

	public RandomAccessMemory(int size, short addressOffset) {
		super(size, addressOffset);
	}

	public void write(short address, byte data) {
		int internalAddress = addressConverter.toInternalAddress(address);
		this.data[internalAddress] = data;
	}
}
