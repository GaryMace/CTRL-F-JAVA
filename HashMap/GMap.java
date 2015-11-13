
public class GMap<K, V> {
	public static final int HASH_MAP_SIZE = 5;
	GEntry<K, V>[] map;
	public GMap(){
        map =  new GEntry[HASH_MAP_SIZE];
	}
	
	public int size() {
		int mapSize = 0;
		for(GEntry<K, V> entry : map) {
			if(entry != null)
				mapSize++;
		}
		return mapSize;
	}
	
	public boolean isEmpty() {
		return size() == 0; 
	}
	
	public V get(K key) {
        return map[getHash(key)].getValue();
	}
	
	public void put(K key, V value) {
        if(containsKey(key)) {
            remove(key);
        }
        if(map[getHash(key)] != null && !containsKey(key)) {
            int keyIndex = find(key);
			if(keyIndex != -1) {
				map[keyIndex] = new GEntry<K, V>(key, value);
			}
            else {
                //TODO: Exception handeling for invalid input? Or implement dynamic hash map size increasing
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
        int searchIndex = 0;
		int keyIndex = getHash(key);
		
		for(int numSearches = 1; numSearches < HASH_MAP_SIZE; numSearches++) {
			searchIndex = (keyIndex + numSearches) % HASH_MAP_SIZE;
            if(map[searchIndex] == null) {
                return searchIndex;
            }
            else {
                continue;
            }
		}
		return -1;
	}

	private boolean containsKey(K key) {
        for(GEntry<K, V> entry: map) {
            if(!(entry == null)) {
                if(entry.getKey().equals(key)) {
                    return true;
                }
            }
            else {
                continue;
            }
        }
        return false;
	}

    public String toString() {
        String output = "";
        for(GEntry<K, V> element: map) {
            if(element == null) {
                output += "{null}";
            }
            else {
                output += element.toString();
            }
        }
        return "{ " + output + " }";
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

        public String toString() {
            return "{"+key.toString()+", "+ value.toString()+"}";
        }
	}
}
