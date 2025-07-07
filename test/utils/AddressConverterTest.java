package utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddressConverterTest {
	public static Stream<Arguments> externalToInternalAddressProvider() {
		return Stream.of(
				Arguments.of((short) 0, 0),
				Arguments.of((short) 1, 1),
				Arguments.of((short) 255, 255),
				Arguments.of((short) 256, 256),
				Arguments.of(Short.MAX_VALUE, 32767),
				Arguments.of((short) -1, 65535),
				Arguments.of((short) 0xFFFF, 65535)

		);
	}

	public static Stream<Arguments> externalToInternalAddressWithOffsetProvider() {
		return Stream.of(
				List.of((short) 0x8000, 0),
				List.of((short) 0x8FFF, 0x0FFF)
		).map(args -> Arguments.argumentSet(
				"internal: 0x%04X -> external: 0x%04X".formatted(args.getFirst(), args.get(1)),
				args.getFirst(),
				args.get(1))
		);
	}

	@ParameterizedTest
	@MethodSource("externalToInternalAddressProvider")
	void testConvertToInternalAddress(short externalAddress, int expectedInternalAddress) {
		final AddressConverter converter = new AddressConverter(0);
		assertThat(converter.toInternalAddress(externalAddress)).isEqualTo(expectedInternalAddress);
	}

	@ParameterizedTest
	@MethodSource("externalToInternalAddressProvider")
	void testConvertToExternalAddress(short expectedExternalAddress, int internalAddress) {
		final AddressConverter converter = new AddressConverter(0);
		assertThat(converter.toExternalAddress(internalAddress)).isEqualTo(expectedExternalAddress);
	}

	@ParameterizedTest
	@MethodSource("externalToInternalAddressWithOffsetProvider")
	void convertWithOffsetToInternal(short externalAddress, int expectedInternalAddress) {
		final int addressOffset = 0x8000;
		final AddressConverter converter = new AddressConverter(addressOffset);

		assertThat(converter.toInternalAddress(externalAddress)).isEqualTo(expectedInternalAddress);
	}

	@ParameterizedTest
	@MethodSource("externalToInternalAddressWithOffsetProvider")
	void convertWithOffsetToExternal(short expectedExternalAddress, int internalAddress) {
		final int addressOffset = 0x8000;
		final AddressConverter converter = new AddressConverter(addressOffset);

		assertThat(converter.toExternalAddress(internalAddress)).isEqualTo(expectedExternalAddress);
	}

	@Test
	void throwExceptionOnTooSmallInputAddress() {
		final AddressConverter converter = new AddressConverter(0x8000);
		assertThatThrownBy(() -> converter.toInternalAddress((short) 0x7FFF))
				.isInstanceOf(IndexOutOfBoundsException.class)
				.hasMessageContaining("Given address is smaller than address offset:");
	}
}