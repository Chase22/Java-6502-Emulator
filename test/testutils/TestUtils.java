package testutils;

public class TestUtils {
	public static byte[] createTestArray(int size) {
		byte[] testArray = new byte[size];
		for (int i1 = 0; i1 < testArray.length; i1++) {
			testArray[i1] = (byte) (i1 % 256);
		}
		return testArray;
	}
}
