package priv.lee.cad.util;

public class Base64Utils {

	private static char alphabet[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
	private static byte codes[];

	static {
		codes = new byte[256];
		for (int i = 0; i < 256; i++) {
			codes[i] = -1;
		}

		for (int j = 65; j <= 90; j++) {
			codes[j] = (byte) (j - 65);
		}

		for (int k = 97; k <= 122; k++) {
			codes[k] = (byte) ((26 + k) - 97);
		}

		for (int l = 48; l <= 57; l++) {
			codes[l] = (byte) ((52 + l) - 48);
		}

		codes[43] = 62;
		codes[47] = 63;
	}

	public static byte[] decode(char value[]) {
		int i = value.length;
		for (int j = 0; j < value.length; j++) {
			if (value[j] > '\377' || codes[value[j]] < 0) {
				i--;
			}
		}

		int k = (i / 4) * 3;
		if (i % 4 == 3) {
			k += 2;
		}
		if (i % 4 == 2) {
			k++;
		}
		byte abyte0[] = new byte[k];
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		for (int k1 = 0; k1 < value.length; k1++) {
			byte byte0 = value[k1] <= '\377' ? codes[value[k1]] : -1;
			if (byte0 < 0) {
				continue;
			}
			i1 <<= 6;
			l += 6;
			i1 |= byte0;
			if (l >= 8) {
				l -= 8;
				abyte0[j1++] = (byte) (i1 >> l & 0xff);
			}
		}

		if (j1 != abyte0.length) {
			throw new Error("Miscalculated data length (wrote " + j1 + " instead of " + abyte0.length + ")");
		} else {
			return abyte0;
		}
	}

	public static char[] encode(byte value[]) {
		char result[] = new char[((value.length + 2) / 3) * 4];
		int i = 0;
		for (int j = 0; i < value.length; j += 4) {
			boolean flag = false;
			boolean flag1 = false;
			int k = 0xff & value[i];
			k <<= 8;
			if (i + 1 < value.length) {
				k |= 0xff & value[i + 1];
				flag1 = true;
			}
			k <<= 8;
			if (i + 2 < value.length) {
				k |= 0xff & value[i + 2];
				flag = true;
			}
			result[j + 3] = alphabet[flag ? k & 0x3f : 64];
			k >>= 6;
			result[j + 2] = alphabet[flag1 ? k & 0x3f : 64];
			k >>= 6;
			result[j + 1] = alphabet[k & 0x3f];
			k >>= 6;
			result[j + 0] = alphabet[k & 0x3f];
			i += 3;
		}
		return result;
	}
}
