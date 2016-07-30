package SuperScrabble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * Created by Gary on 13/06/2016.
 */
public class Pool {
        private static Random rand;
        public static final char BLANK_TILE = '#';      //TODO: handle blank tiles
        private static HashMap<Character, Integer> TILE_QUANTITIES;
        private static HashMap<Character, Integer> TILE_VALUES;
        private ArrayList<Tile> pool;

        public Pool() {
                rand = new Random();
                pool = new ArrayList<>();

                //Every time someone uses double brace initialisation, a kitten gets killed.
                TILE_QUANTITIES = new HashMap<Character, Integer>() {{      //Double brace initialisation, #YOLO MAN
                        put('a', 9); put('b', 2); put('c', 2); put('d', 4);
                        put('e', 12); put('f', 2); put('g', 3); put('h', 2);
                        put('i', 9); put('j', 1); put('k', 1); put('l', 4);
                        put('m', 2); put('n', 6); put('o', 8); put('p', 2);
                        put('q', 1); put('r', 6); put('s', 4); put('t', 6);
                        put('u', 4); put('v', 2); put('w', 2); put('x', 1);
                        put('y', 2); put('z', 1); put('#', 2);
                }};
                TILE_VALUES = new HashMap<Character, Integer>() {{
                        put('a', 1); put('b', 3); put('c', 3); put('d', 2);
                        put('e', 1); put('f', 4); put('g', 2); put('h', 4);
                        put('i', 1); put('j', 8); put('k', 5); put('l', 1);
                        put('m', 3); put('n', 1); put('o', 1); put('p', 3);
                        put('q', 10); put('r', 1); put('s', 1); put('t', 1);
                        put('u', 1); put('v', 4); put('w', 4); put('x', 8);
                        put('y', 4); put('z', 10); put('#', 0);
                }};

                populatePool();
        }

        private void populatePool() {
                for(Map.Entry entry : TILE_QUANTITIES.entrySet()) {
                        for(int tileCount=0; tileCount < (int) entry.getValue(); tileCount++) {
                                char letter = (char) entry.getKey();
                                pool.add(
                                        new Tile( letter, TILE_VALUES.get(letter) )
                                );
                        }
                }
        }

        public void resetPool() {
                pool = new ArrayList<>();
                populatePool();
        }

        public int size() {
                return pool.size();
        }

        public boolean isEmpty() {
                return pool.size() == 0;
        }

        public Tile drawTile() {
                if( !isEmpty() ) {
                        return pool.remove(
                                rand.nextInt( pool.size() )
                        );
                }
                return null;
        }

        public void putBackTile(Tile t) {
                pool.add(t);
        }

        public static int tileValue(char key) {
                return Pool.TILE_VALUES.get(key); //Potential null pointer error?
        }
}
