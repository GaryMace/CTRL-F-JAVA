
public class GMap<K, V> {
	public static final int HASH_MAP_SIZE = 100;
	GEntry<K, V>[] map;
	
	public GMap(){
		map =  new GEntry[HASH_MAP_SIZE];
	}
	
	public int size() {
		int hashSize = 0;
		for(GEntry<K, V> entry : map) {
			if(entry != null)
				hashSize++;
		}
		return hashSize;
	}
	
	public boolean isEmpty() {
		return size() == 0; 
	}
	
	public V get(K key) {
		return map[getHash(key)].getValue();
	}
	
	public void put(K key, V value) {
		if(map[getHash(key)] != null) {
			int keyIndex = find(key);
			if(keyIndex != -1) {
				map[keyIndex] = new GEntry<K, V>(key, value);
			}
		}
		else {
			map[getHash(key)] = new GEntry<K, V>(key, value);
		}
	}
	
	public V remove(K key) {
		V value = map[getHash(key)].getValue();
		map[getHash(key)] = null;
		
		return value;
	}
	
	public void clear() {
		for(int counter = 0; counter < HASH_MAP_SIZE; counter++) { 
			map[counter] = null;
		}
	}
	
	/**
	 * 
	 * @param key Key to get hash code of for HashMap.
	 * @return HashCode of parameter Key
	 */
	private int getHash(K key) {
		int hashCode = key.hashCode() % HASH_MAP_SIZE;
		
		if(hashCode < 0) 
			hashCode += HASH_MAP_SIZE;
		return hashCode;
	}
	
	/**
	 * Finds next free slot in HashMap if one exists.
	 * 
	 * @param key Key of pair to be inserted into HashMap.
	 * @return Index of next free position in HashMap. Return -1 if none exists.
	 */
	private int find(K key) {
		int mapIndex = 0;
		int keyIndex = getHash(key);
		
		for(int numSearches = 1; numSearches < HASH_MAP_SIZE; numSearches++) {
			mapIndex = keyIndex + numSearches;   //TODO handle overlap here using modulus,,, then you dont need to consider going out of bounds
			
			if(mapIndex < HASH_MAP_SIZE) {
				if(map[mapIndex] == null)
					return mapIndex;
				else 
					continue;
			}
			else {
				keyIndex = 1 - numSearches; //TODO i'm not sure what this does, but will be redundant if you handle overlap above :)
			}
		}
		return -1;
	}
	
	public class GEntry<K, V> {
		K key;
		V value;
		
		public GEntry(K key, V value) {
			this.key = key;
			this.value =  value;
		}
		
		public K getKey() {
			return key;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public V getValue() {
			return value;
		}

		public void setValue(V value) {
			this.value = value;
		}
	}
}
