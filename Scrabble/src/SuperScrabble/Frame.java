package SuperScrabble;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
                this.tiles = new ArrayList<>(originalFrame.getTiles());
        }

        public ArrayList<Tile> getTiles() {
                return tiles;
        }

        public Tile removeTile(char tileLetter) {
                for (int i = 0; i < tiles.size(); i++) {
                        Tile t = tiles.get(i);

                        if (t.getFace() == tileLetter) {
                                return tiles.remove(i);
                        }
                }
                return null;
        }

        public int hasTile(char tileLetter) {
                for (int i = 0; i < tiles.size(); i++) {
                        Tile t = tiles.get(i);

                        if (t.getFace() == tileLetter) {
                                return i;
                        }
                }
                return FRAME_TILE_NOT_FOUND;
        }

        public boolean hasTiles(String letters) {
                ArrayList<Tile> testFrame = new ArrayList<>(tiles);
                boolean foundChar = false;

                for (int i = 0; i < letters.length(); i++) {
                        char charToFind = letters.charAt(i);
                        for (int j = 0; j < testFrame.size(); j++) {
                                Tile t = testFrame.get(j);
                                if (charToFind == t.getFace()) {
                                        testFrame.remove(j);
                                        foundChar = true;
                                        break;
                                }
                        }
                        if(!foundChar) {
                                return false;
                        }
                        foundChar = false;
                }
                return true;
        }

        public boolean isEmpty() {
                return tiles.size() == 0;
        }

        public void refillFrame(Pool p) {
                while (tiles.size() < MAX_FRAME_SIZE_FLAG) {
                        Tile t = p.drawTile();
                        if (t == null) {
                                break;
                        }
                        tiles.add(t);
                }
        }

        public int size() {
                return tiles.size();
        }

        public String toString() {
                String s = tiles.stream()
                         .map(l -> l.getFace()+"")
                         .collect(Collectors.joining(", "));
                //Returns "[a, b, c, d, e, f, g]"
                return "[" + s + "]";
        }
}
