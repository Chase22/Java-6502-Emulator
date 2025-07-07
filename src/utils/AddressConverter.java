package utils;

public class AddressConverter {
	private final int addressOffset;

	public AddressConverter(int addressOffset) {
		this.addressOffset = addressOffset;
	}

	/**
	 * Converts an external address (short) to the component internal address space (int) dealing making sure the returned value is non-negative.
	 * @param address External address to convert.
	 * @return Internal address as an integer. Shifted by the address offset.
	 */
	public int toInternalAddress(short address) {
		int convertedAddress = Short.toUnsignedInt(address) - addressOffset;
		if (convertedAddress < 0) {
			throw new IndexOutOfBoundsException("Given address is smaller than address offset: " + convertedAddress);
		}
		return convertedAddress;
	}

	/**
	 * Converts an internal address (int) to the external address space (short).
	 * @param internalAddress Internal address to convert.
	 * @return External address as a short. Shifted by the address offset.
	 */
	public short toExternalAddress(int internalAddress) {
		if (internalAddress < 0) {
			throw new IndexOutOfBoundsException("Internal address cannot be negative: " + internalAddress);
		}
		return (short) (internalAddress + addressOffset);
	}
}
