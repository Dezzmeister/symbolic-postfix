package com.dezzy.postfix.auxiliary;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A bidirectional Map, backed by two Maps with inverse key/value types.
 * 
 * @author Joe Desmond
 *
 * @param <K> key type
 * @param <V> value type
 */
public class BiMap<K, V> implements Map<K, V> {
	
	/**
	 * First map
	 */
	private final Map<K, V> map;
	
	/**
	 * Inverse map
	 */
	private final Map<V, K> inverse;
	
	/**
	 * Creates a bidirectional map backed by the two specified maps.
	 * 
	 * @param _map forward map
	 * @param _inverse inverse map
	 */
	public BiMap(final Map<K, V> _map, final Map<V, K> _inverse) {
		map = _map;
		inverse = _inverse;
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public V get(Object key) {
		return map.get(key);
	}

	@Override
	public V put(K key, V value) {
		inverse.put(value, key);
		return map.put(key, value);
	}

	@Override
	public V remove(Object key) {
		V value = map.remove(key);
		inverse.remove(value);
		return value;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		m.forEach((k, v) -> inverse.put(v, k));
		map.putAll(m);
	}

	@Override
	public void clear() {
		inverse.clear();
		map.clear();
	}

	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<V> values() {
		return map.values();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return map.entrySet();
	}
	
	/**
	 * Gets the key associated with this value.
	 * 
	 * @param value value
	 * @return associated key
	 */
	public K inverseGet(final V value) {
		return inverse.get(value);
	}
	
	/**
	 * Gets the inverse map's keyset.
	 * 
	 * @return inverse keyset
	 */
	public Set<V> inverseKeySet() {
		return inverse.keySet();
	}
	
	/**
	 * Gets the inverse map's values.
	 * 
	 * @return inverse values
	 */
	public Collection<K> inverseValues() {
		return inverse.values();
	}
	
	/**
	 * Gets the inverse maps' entryset.
	 * 
	 * @return inverse entryset
	 */
	public Set<Entry<V, K>> inverseEntrySet() {
		return inverse.entrySet();
	}

}
