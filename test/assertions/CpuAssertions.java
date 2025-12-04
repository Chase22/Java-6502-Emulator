package assertions;

import eater.cpu.CPU;
import org.assertj.core.api.AbstractAssert;

public class CpuAssertions extends AbstractAssert<CpuAssertions, CPU> {

	CpuAssertions(CPU cpu) {
		super(cpu, CpuAssertions.class);
	}

	public static CpuAssertions asserThat(CPU actual) {
		return new CpuAssertions(actual);
	}

	private CpuAssertions registerHasValue(byte registerValue, String registerName, Number expectedValue) {
		if (registerValue != expectedValue.byteValue()) {
			failWithMessage("Expected Register %s to have value %02X but was %02X", registerName, expectedValue, registerValue);
		}
		return this;
	}

	public CpuAssertions registerAIs(Number value) {
		return registerHasValue(actual.a, "A", value);
	}

	public CpuAssertions registerXIs(Number value) {
		return registerHasValue(actual.x, "X", value);
	}

	public CpuAssertions registerYIs(Number value) {
		return registerHasValue(actual.y, "Y", value);
	}
}
