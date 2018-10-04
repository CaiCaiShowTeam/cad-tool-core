package priv.lee.cad.bean;

import java.io.Serializable;

public class HandleResult<T> implements Serializable {

	public static final int INTERNAL_SERVER_ERROR = 500;
	public static final int OK = 200;
	private static final long serialVersionUID = -2842918834988931042L;
	private static final String UNEXPECTED = "Unexpected error";

	public static <T> HandleResult<T> toErrorResult(Exception exception) {
		return toErrorResult(null, exception.getLocalizedMessage());
	}

	public static <T> HandleResult<T> toErrorResult(T result, String message) {
		return new HandleResult<T>(result, INTERNAL_SERVER_ERROR, message);
	}

	public static <T> HandleResult<T> toSuccessedResult(T result) {
		return toSuccessedResult(result, null);
	}

	public static <T> HandleResult<T> toSuccessedResult(T result, String message) {
		return new HandleResult<T>(result, OK, message);
	}

	public static <T> HandleResult<T> toUnExpectedResult() {
		return toErrorResult(null, UNEXPECTED);
	}

	private int code;
	private String message;
	private T result;

	private HandleResult(T result, int code) {
		this(result, code, null);
	}

	private HandleResult(T result, int code, String message) {
		this.result = result;
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public T getResult() {
		return result;
	}
}
