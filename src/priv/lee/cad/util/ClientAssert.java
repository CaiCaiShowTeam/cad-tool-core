package priv.lee.cad.util;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import priv.lee.cad.ui.RuntimeExceptionPanel;

public abstract class ClientAssert extends Assert {

	private static void assignableCheckFailed(Class<?> superType, Class<?> subType, String msg) {
		String result = "";
		boolean defaultMessage = true;
		if (StringUtils.hasLength(msg)) {
			if (endsWithSeparator(msg)) {
				result = msg + " ";
			} else {
				result = messageWithTypeName(msg, subType);
				defaultMessage = false;
			}
		}

		if (defaultMessage) {
			result = result + (subType + " is not assignable to " + superType);
		}
		throwIllegalArgumentException(result);
	}

	public static void doesNotContain(String textToSearch, String substring, String message) {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring)
				&& textToSearch.contains(substring)) {
			throwIllegalArgumentException(message);
		}
	}

	public static void doesNotContain(String textToSearch, String substring, Supplier<String> messageSupplier) {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring)
				&& textToSearch.contains(substring)) {
			throwIllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}

	private static boolean endsWithSeparator(String msg) {
		return (msg.endsWith(":") || msg.endsWith(";") || msg.endsWith(",") || msg.endsWith("."));
	}

	public static void hasLength(String text, String message) {
		if (!StringUtils.hasLength(text)) {
			throwIllegalArgumentException(message);
		}
	}

	public static void hasLength(String text, Supplier<String> messageSupplier) {
		if (!StringUtils.hasLength(text)) {
			throwIllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}

	public static void hasText(String text, String message) {
		if (!StringUtils.hasText(text)) {
			throwIllegalArgumentException(message);
		}
	}

	public static void hasText(String text, Supplier<String> messageSupplier) {
		if (!StringUtils.hasText(text)) {
			throwIllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}

	private static void instanceCheckFailed(Class<?> type, Object obj, String msg) {
		String className = (obj != null ? obj.getClass().getName() : "null");
		String result = "";
		boolean defaultMessage = true;
		if (StringUtils.hasLength(msg)) {
			if (endsWithSeparator(msg)) {
				result = msg + " ";
			} else {
				result = messageWithTypeName(msg, className);
				defaultMessage = false;
			}
		}
		if (defaultMessage) {
			result = result + ("Object of class [" + className + "] must be an instance of " + type);
		}
		throwIllegalArgumentException(result);
	}

	public static void isAssignable(Class<?> superType, Class<?> subType) {
		isAssignable(superType, subType, "");
	}

	public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
		notNull(superType, "Super type to check against must not be null");
		if (subType == null || !superType.isAssignableFrom(subType)) {
			assignableCheckFailed(superType, subType, message);
		}
	}

	public static void isAssignable(Class<?> superType, Class<?> subType, Supplier<String> messageSupplier) {
		notNull(superType, "Super type to check against must not be null");
		if (subType == null || !superType.isAssignableFrom(subType)) {
			assignableCheckFailed(superType, subType, nullSafeGet(messageSupplier));
		}
	}

	public static void isInstanceOf(Class<?> type, Object obj) {
		isInstanceOf(type, obj, "");
	}

	public static void isInstanceOf(Class<?> type, Object obj, String message) {
		notNull(type, "Type to check against must not be null");
		if (!type.isInstance(obj)) {
			instanceCheckFailed(type, obj, message);
		}
	}

	public static void isInstanceOf(Class<?> type, Object obj, Supplier<String> messageSupplier) {
		notNull(type, "Type to check against must not be null");
		if (!type.isInstance(obj)) {
			instanceCheckFailed(type, obj, nullSafeGet(messageSupplier));
		}
	}

	public static void isNull(Object object, String message) {
		if (object != null) {
			throwIllegalArgumentException(message);
		}
	}

	public static void isNull(Object object, Supplier<String> messageSupplier) {
		if (object != null) {
			throwIllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}

	public static void isNumeric(String text, String message) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		if (!pattern.matcher(text).matches()) {
			throwIllegalArgumentException(message);
		}
	}

	public static void isNumeric(String text, Supplier<String> messageSupplier) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		if (!pattern.matcher(text).matches()) {
			throwIllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}

	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throwIllegalArgumentException(message);
		}
	}

	public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
		if (!expression) {
			throwIllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}

	private static String messageWithTypeName(String msg, Object typeName) {
		return msg + (msg.endsWith(" ") ? "" : ": ") + typeName;
	}

	public static void noNullElements(Object[] array, String message) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					throwIllegalArgumentException(message);
				}
			}
		}
	}

	public static void noNullElements(Object[] array, Supplier<String> messageSupplier) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					throwIllegalArgumentException(nullSafeGet(messageSupplier));
				}
			}
		}
	}

	public static void notEmpty(Collection<?> collection, String message) {
		if (CollectionUtils.isEmpty(collection)) {
			throwIllegalArgumentException(message);
		}
	}

	public static void notEmpty(Collection<?> collection, Supplier<String> messageSupplier) {
		if (CollectionUtils.isEmpty(collection)) {
			throwIllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}

	public static void notEmpty(Map<?, ?> map, String message) {
		if (CollectionUtils.isEmpty(map)) {
			throwIllegalArgumentException(message);
		}
	}

	public static void notEmpty(Map<?, ?> map, Supplier<String> messageSupplier) {
		if (CollectionUtils.isEmpty(map)) {
			throwIllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}

	public static void notEmpty(Object[] array, String message) {
		if (ObjectUtils.isEmpty(array)) {
			throwIllegalArgumentException(message);
		}
	}

	public static void notEmpty(Object[] array, Supplier<String> messageSupplier) {
		if (ObjectUtils.isEmpty(array)) {
			throwIllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}

	public static void notNull(Object object, String message) {
		if (object == null) {
			throwIllegalArgumentException(message);
		}
	}

	public static void notNull(Object object, Supplier<String> messageSupplier) {
		if (object == null) {
			throwIllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}

	private static String nullSafeGet(Supplier<String> messageSupplier) {
		return (messageSupplier != null ? messageSupplier.get() : null);
	}

	public static void state(boolean expression, String message) {
		if (!expression) {
			throwIllegalArgumentException(message);
		}
	}

	public static void state(boolean expression, Supplier<String> messageSupplier) {
		if (!expression) {
			throwIllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}

	public static void throwIllegalArgumentException(String message) {
		new RuntimeExceptionPanel(new IllegalArgumentException(message));
	}

	public static void throwIllegalStateException(String message) {
		new RuntimeExceptionPanel(new IllegalStateException(message));
	}
}
