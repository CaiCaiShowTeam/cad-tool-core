package priv.lee.cad.model;

import java.util.List;
import java.util.Map;

public interface MultiValueMap<K, V> extends Map<K, List<V>> {

	public void add(K key, V value);

	public void addAll(K key, List<? extends V> values);

	public void addAll(MultiValueMap<K, V> values);

	public V getFirst(K key);

	public void set(K key, V value);

	public void setAll(Map<K, V> values);

	public Map<K, V> toSingleValueMap();
}
