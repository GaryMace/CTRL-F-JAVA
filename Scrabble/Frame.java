import java.util.ArrayList;

/**
 * Created by Gary on 13/06/2016.
 */
public class Frame {
        public static final int MAX_FRAME_SIZE_FLAG = 7;
        public static final int TILE_NOT_IN_FRAME_FLAG = -1;
        private ArrayList<Tile> tiles;

        public Frame() {
                tiles = new ArrayList<>();
        }

        public ArrayList<Tile> getTiles() {
                return tiles;
        }

        public Tile removeTile(char tileLetter) {
                int index;
                if( (index = hasTile(tileLetter)) != TILE_NOT_IN_FRAME_FLAG ) {
                        return tiles.remove(index);
                }
                return null;
        }

        public int hasTile(char tileLetter) {
                for(int i=0; i < tiles.size(); i++) {
                        Tile t = tiles.get(i);

                        if(t.getFace() == tileLetter) {
                                return i;
                        }
                }
                return -1;
        }

        public boolean isEmpty() {
                return tiles.size() == 0;
        }

        public void refillFrame(Tile t) {
                tiles.add(t);
        }

        public int size() {
                return tiles.size();
        }

        public String toString() {
                String s="";
                for(Tile t : tiles) {
                        s += t.getFace()+", ";
                }
                return s.substring(0, s.length()-2);
        }
}
