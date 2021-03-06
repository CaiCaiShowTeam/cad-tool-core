package priv.lee.cad.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;

import priv.lee.cad.model.MultiValueMap;

public abstract class CollectionUtils {

	private static class EnumerationIterator<E> implements Iterator<E> {

		private final Enumeration<E> enumeration;

		public EnumerationIterator(Enumeration<E> enumeration) {
			this.enumeration = enumeration;
		}

		@Override
		public boolean hasNext() {
			return this.enumeration.hasMoreElements();
		}

		@Override
		public E next() {
			return this.enumeration.nextElement();
		}

		@Override
		public void remove() throws UnsupportedOperationException {
			throw new UnsupportedOperationException("Not supported");
		}
	}

	@SuppressWarnings("serial")
	private static class MultiValueMapAdapter<K, V> implements MultiValueMap<K, V>, Serializable {

		private final Map<K, List<V>> map;

		public MultiValueMapAdapter(Map<K, List<V>> map) {
			ClientAssert.notNull(map, "'map' must not be null");
			this.map = map;
		}

		@Override
		public void add(K key, V value) {
			List<V> values = this.map.computeIfAbsent(key, k -> new LinkedList<>());
			values.add(value);
		}

		@Override
		public void addAll(K key, List<? extends V> values) {
			List<V> currentValues = this.map.computeIfAbsent(key, k -> new LinkedList<>());
			currentValues.addAll(values);
		}

		@Override
		public void addAll(MultiValueMap<K, V> values) {
			for (Entry<K, List<V>> entry : values.entrySet()) {
				addAll(entry.getKey(), entry.getValue());
			}
		}

		@Override
		public void clear() {
			this.map.clear();
		}

		@Override
		public boolean containsKey(Object key) {
			return this.map.containsKey(key);
		}

		@Override
		public boolean containsValue(Object value) {
			return this.map.containsValue(value);
		}

		@Override
		public Set<Entry<K, List<V>>> entrySet() {
			return this.map.entrySet();
		}

		@Override
		public boolean equals(Object other) {
			if (this == other) {
				return true;
			}
			return this.map.equals(other);
		}

		@Override
		public List<V> get(Object key) {
			return this.map.get(key);
		}

		@Override

		public V getFirst(K key) {
			List<V> values = this.map.get(key);
			return (!ObjectUtils.isEmpty(values) ? values.get(0) : null);
		}

		@Override
		public int hashCode() {
			return this.map.hashCode();
		}

		@Override
		public boolean isEmpty() {
			return this.map.isEmpty();
		}

		@Override
		public Set<K> keySet() {
			return this.map.keySet();
		}

		@Override
		public List<V> put(K key, List<V> value) {
			return this.map.put(key, value);
		}

		@Override
		public void putAll(Map<? extends K, ? extends List<V>> map) {
			this.map.putAll(map);
		}

		@Override
		public List<V> remove(Object key) {
			return this.map.remove(key);
		}

		@Override
		public void set(K key, V value) {
			List<V> values = new LinkedList<>();
			values.add(value);
			this.map.put(key, values);
		}

		@Override
		public void setAll(Map<K, V> values) {
			values.forEach(this::set);
		}

		@Override
		public int size() {
			return this.map.size();
		}

		@Override
		public Map<K, V> toSingleValueMap() {
			LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<>(this.map.size());
			this.map.forEach((key, value) -> singleValueMap.put(key, value.get(0)));
			return singleValueMap;
		}

		@Override
		public String toString() {
			return this.map.toString();
		}

		@Override
		public Collection<List<V>> values() {
			return this.map.values();
		}
	}

	@SuppressWarnings("rawtypes")
	public static List arrayToList(Object source) {
		return Arrays.asList(ObjectUtils.toObjectArray(source));
	}

	public static boolean contains(Enumeration<?> enumeration, Object element) {
		if (!ObjectUtils.isEmpty(enumeration)) {
			while (enumeration.hasMoreElements()) {
				Object candidate = enumeration.nextElement();
				if (ObjectUtils.nullSafeEquals(candidate, element)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean contains(Iterator<?> iterator, Object element) {
		if (!ObjectUtils.isEmpty(iterator)) {
			while (iterator.hasNext()) {
				Object candidate = iterator.next();
				if (ObjectUtils.nullSafeEquals(candidate, element)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean containsAny(Collection<?> source, Collection<?> candidates) {
		if (isEmpty(source) || isEmpty(candidates)) {
			return false;
		}
		for (Object candidate : candidates) {
			if (source.contains(candidate)) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsInstance(Collection<?> collection, Object element) {
		if (!ObjectUtils.isEmpty(collection)) {
			for (Object candidate : collection) {
				if (candidate == element) {
					return true;
				}
			}
		}
		return false;
	}

	public static Class<?> findCommonElementType(Collection<?> collection) {
		if (isEmpty(collection)) {
			return null;
		}
		Class<?> candidate = null;
		for (Object val : collection) {
			if (!ObjectUtils.isEmpty(val)) {
				if (ObjectUtils.isEmpty(candidate)) {
					candidate = val.getClass();
				} else if (candidate != val.getClass()) {
					return null;
				}
			}
		}
		return candidate;
	}

	@SuppressWarnings("unchecked")
	public static <E> E findFirstMatch(Collection<?> source, Collection<E> candidates) {
		if (isEmpty(source) || isEmpty(candidates)) {
			return null;
		}
		for (Object candidate : candidates) {
			if (source.contains(candidate)) {
				return (E) candidate;
			}
		}
		return null;
	}

	public static Object findValueOfType(Collection<?> collection, Class<?>[] types) {
		if (isEmpty(collection) || ObjectUtils.isEmpty(types)) {
			return null;
		}
		for (Class<?> type : types) {
			Object value = findValueOfType(collection, type);
			if (!ObjectUtils.isEmpty(value)) {
				return value;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T findValueOfType(Collection<?> collection, Class<T> type) {
		if (isEmpty(collection)) {
			return null;
		}
		T value = null;
		for (Object element : collection) {
			if (ObjectUtils.isEmpty(type) || type.isInstance(element)) {
				if (!ObjectUtils.isEmpty(value)) {
					// More than one value found... no clear single value.
					return null;
				}
				value = (T) element;
			}
		}
		return value;
	}

	public static boolean hasUniqueObject(Collection<?> collection) {
		if (isEmpty(collection)) {
			return false;
		}
		boolean hasCandidate = false;
		Object candidate = null;
		for (Object elem : collection) {
			if (!hasCandidate) {
				hasCandidate = true;
				candidate = elem;
			} else if (candidate != elem) {
				return false;
			}
		}
		return true;
	}

	public static boolean isEmpty(Collection<?> collection) {
		return (ObjectUtils.isEmpty(collection) || collection.isEmpty());
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return (ObjectUtils.isEmpty(map) || map.isEmpty());
	}

	public static <T> T lastElement(List<T> list) {
		if (isEmpty(list)) {
			return null;
		}
		return list.get(list.size() - 1);
	}

	public static <T> T lastElement(Set<T> set) {
		if (isEmpty(set)) {
			return null;
		}
		if (set instanceof SortedSet) {
			return ((SortedSet<T>) set).last();
		}

		// Full iteration necessary...
		Iterator<T> it = set.iterator();
		T last = null;
		while (it.hasNext()) {
			last = it.next();
		}
		return last;
	}

	@SuppressWarnings("unchecked")
	public static <E> void mergeArrayIntoCollection(Object array, Collection<E> collection) {
		Object[] arr = ObjectUtils.toObjectArray(array);
		for (Object elem : arr) {
			collection.add((E) elem);
		}
	}

	@SuppressWarnings("unchecked")
	public static <K, V> void mergePropertiesIntoMap(Properties props, Map<K, V> map) {
		if (!ObjectUtils.isEmpty(props)) {
			for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements();) {
				String key = (String) en.nextElement();
				Object value = props.get(key);
				if (ObjectUtils.isEmpty(value)) {
					// Allow for defaults fallback or potentially overridden accessor...
					value = props.getProperty(key);
				}
				map.put((K) key, (V) value);
			}
		}
	}

	public static <A, E extends A> A[] toArray(Enumeration<E> enumeration, A[] array) {
		ArrayList<A> elements = new ArrayList<>();
		while (enumeration.hasMoreElements()) {
			elements.add(enumeration.nextElement());
		}
		return elements.toArray(array);
	}

	public static <E> Iterator<E> toIterator(Enumeration<E> enumeration) {
		return new EnumerationIterator<>(enumeration);
	}

	public static <K, V> MultiValueMap<K, V> toMultiValueMap(Map<K, List<V>> map) {
		return new MultiValueMapAdapter<>(map);
	}

	@SuppressWarnings("unchecked")
	public static <K, V> MultiValueMap<K, V> unmodifiableMultiValueMap(MultiValueMap<? extends K, ? extends V> map) {
		ClientAssert.notNull(map, "'map' must not be null");
		Map<K, List<V>> result = new LinkedHashMap<>(map.size());
		map.forEach((key, value) -> {
			List<? extends V> values = Collections.unmodifiableList(value);
			result.put(key, (List<V>) values);
		});
		Map<K, List<V>> unmodifiableMap = Collections.unmodifiableMap(result);
		return toMultiValueMap(unmodifiableMap);
	}
}