import java.util.Arrays;
import java.util.Iterator;

public class GMap<K, V> {
	public static int HASH_MAP_SIZE = 5;
	private GEntry<K, V>[] map;

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
        for(GEntry<K, V> entry : map) {
            if(entry != null)
                return false;
        }
        return true;
	}
	
	public V get(K key) {
        return map[findKey(key)].getValue();
	}
	
	public void put(K key, V value) {
        if(containsKey(key)) {
            remove(key);
        }
       if(size() == HASH_MAP_SIZE) {
           reHash();
       }
        map[find(key)] = new GEntry<>(key, value);
	}
	
	public V remove(K key) {
        int keyIndex = findKey(key);
        V value = map[keyIndex].getValue();

		map[keyIndex] = null;
		return value;
	}
	
	public void clear() {
		for(int counter = 0; counter < HASH_MAP_SIZE; counter++) { 
			map[counter] = null;
		}
	}

	public Iterator<GEntry<K, V>> entries() {

        return new Iterator<GEntry<K, V>>(){
            private int index = 0;
            private int numEntriesLeft = size();

            public boolean hasNext() {
                return index < HASH_MAP_SIZE && numEntriesLeft > 0;
            }

            public GEntry<K, V> next() {
                while(map[index] == null) {
                    index++;
                }
                numEntriesLeft--;
                return map[index++];
            }
        };
    }

    public Iterator<K> keys() {

        return new Iterator<K>(){
            private int index = 0;
            private int numEntriesLeft = size();

            public boolean hasNext() {
                return index < HASH_MAP_SIZE && numEntriesLeft > 0;
            }

            public K next() {
                while(map[index] == null) {
                    index++;
                }
                numEntriesLeft--;
                return map[index++].getKey();
            }
        };
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
		
		for(int numSearches = 0; numSearches < HASH_MAP_SIZE; numSearches++) {
			searchIndex = (keyIndex + numSearches) % HASH_MAP_SIZE;
            if(map[searchIndex] == null) {
                return searchIndex;
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
        }
        return false;
	}

    private int findKey(K key) {
        int entryIndex = 0;
        for(GEntry<K, V> currEntry: map) {
            if(currEntry != null) {
                if(currEntry.getKey() == key) {
                    break;
                }
            }
            entryIndex++;
        }
        return entryIndex;
    }

	private void reHash() {
        HASH_MAP_SIZE = 2*HASH_MAP_SIZE;
		GEntry<K, V>[] newMap = new GEntry[HASH_MAP_SIZE];

        for(int index=0; index < map.length; index++) {
            newMap[index] = map[index];
        }
        map = newMap;
	}

    public String toString() {
        return Arrays.toString(map);
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
            return "GEntry{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }
}
