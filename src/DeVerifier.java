import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class DeVerifier {

	private static final Unsafe unsafe;
	private static final long _klass_offset;
	private static final long _misc_flags_offset;
	private static final long _misc_flags_should_verify;

	// openjdk offsets
	private static final long[] _klass_offsets = new long[] {
			0x48, // 8
			0x50, // 9
			0x48, // 10
			0x50, // 11
			0x50, // 12
			0x50, // 13
			0x50, // 14
			0x10, // 15
			0x10, // 16
			0x10, // 17
			0x10, // 18
			0x10, // 19
			0x10, // 20
	};

	private static final long[] _misc_flags_offsets = new long[] {
			0x110, // 8
			0x112, // 9
			0x110, // 10
			0x13A, // 11
			0x13A, // 12
			0x13A, // 13
			0x13A, // 14
			0x13E, // 15
			0x13E, // 16
			0x13E, // 17
			0x126, // 18
			0x126, // 19
			0x126, // 20
	};

	private static final int[] _misc_flags_should_verify_bits = {
			1 << 2, // 8
			1 << 4, // 9
			1 << 4, // 10
			1 << 4, // 11
			1 << 4, // 12
			1 << 4, // 13
			1 << 4, // 14
			1 << 2, // 15
			1 << 2, // 16
			1 << 2, // 17
			1 << 2, // 18
			1 << 2, // 19
			1 << 2, // 20
	};

	static {
		// version as integer
		int version = getJavaVersion();
		_klass_offset = _klass_offsets[version - 8];
		_misc_flags_offset = _misc_flags_offsets[version - 8];
		_misc_flags_should_verify = _misc_flags_should_verify_bits[version - 8];
		// access theUnsafe field of Unsafe class
		Field field;
		try {
			field = Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			unsafe = (Unsafe) field.get(null);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private static int getJavaVersion() {
		String javaVersion = System.getProperty("java.version");
		if(javaVersion.contains(".")) {
			int first = Integer.parseInt(javaVersion.substring(0, javaVersion.indexOf(".")));
			if(first == 1) {
				javaVersion = javaVersion.substring(javaVersion.indexOf(".") + 1);
			}
			return Integer.parseInt(javaVersion.substring(0, javaVersion.indexOf(".")));
		}
		return Integer.parseInt(javaVersion);
	}

	public static void disableVerifier(Class<?> clazz) {
		long klass_address = unsafe.getLong(clazz, _klass_offset);

		short flags = unsafe.getShort(klass_address + _misc_flags_offset);

		flags &= (short) ~_misc_flags_should_verify;

		unsafe.putShort(klass_address + _misc_flags_offset, flags);
	}

}
