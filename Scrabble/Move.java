/**
 * Created by Gary on 14/06/2016.
 */
public class Move {
        private String word;
        private String direction;
        private GridRef location;

        public Move(GridRef location, String word, String direction) {
                this.location = location;
                this.word = word;
                this.direction = direction;
        }

        public String getWord() {
                return word;
        }

        public GridRef getLocation() {
                return location;
        }

        public String getDirection() {
                return direction;
        }
}
