import java.util.Arrays;
import java.util.Iterator;

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
        //TODO: Handle too big
        map[find(key)] = new GEntry<K, V>(key, value);
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

	public Iterator<GEntry<K, V>> entries() {

        return new Iterator<GEntry<K, V>>(){
            private int index = 0;


            @Override
            public boolean hasNext() {
                return index<HASH_MAP_SIZE;
            }

            @Override
            public GEntry<K, V> next() {
                while(map[index] == null) {
                    index++;
                }
                return map[index++];
            }
        };
    }

    public Iterator<K> keys() {

        return new Iterator<K>(){
            private int index = 0;


            @Override
            public boolean hasNext() {
                return index<HASH_MAP_SIZE;
            }

            @Override
            public K next() {
                while(map[index] == null) {
                    index++;
                }
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

        @Override
        public String toString() {
            return "GEntry{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }
}
