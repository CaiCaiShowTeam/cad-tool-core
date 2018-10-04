package priv.lee.cad.model;

import java.io.Serializable;

public interface ClientTemporary extends Serializable {

	static String lowercase(String name) {
		byte[] bytes = name.getBytes();
		bytes[0] = (byte) ((char) bytes[0] - 'A' + 'a');
		return new String(bytes);
	}
}
