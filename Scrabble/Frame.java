package SuperScrabble;

import java.util.ArrayList;

/**
 * Created by Gary on 13/06/2016.
 */
public class Frame {
        public static final int MAX_FRAME_SIZE_FLAG = 7;
        public static final int FRAME_TILE_NOT_FOUND = -1;
        private ArrayList<Tile> tiles;

        public Frame() {
                tiles = new ArrayList<>();
        }

        Frame(Frame originalFrame) {
                this.tiles = new ArrayList<>( originalFrame.getTiles() );
        }

        public ArrayList<Tile> getTiles() {
                return tiles;
        }

        public Tile removeTile(char tileLetter) {
                for(int i=0; i < tiles.size(); i++) {
                        Tile t = tiles.get(i);

                        if(t.getFace() == tileLetter) {
                                return tiles.remove(i);
                        }
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
                return FRAME_TILE_NOT_FOUND;
        }

        public boolean isEmpty() {
                return tiles.size() == 0;
        }

        public void refillFrame(Pool p) {
                while(tiles.size() < MAX_FRAME_SIZE_FLAG) {
                        Tile t = p.drawTile();
                        if(t == null) {
                                break;
                        }
                        tiles.add(t);
                }
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
