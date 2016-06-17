/**
 * Created by Gary on 13/06/2016.
 */
public class Tile {
        private char face;
        private int value;

        public Tile(char tileChar, int tileVal) {
                this.face = tileChar;
                this.value = tileVal;
        }

        public int getValue() {
                return value;
        }

        public char getFace() {
                return face;
        }
}
