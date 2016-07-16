package SuperScrabble;

/**
 * Created by Gary on 14/06/2016.
 */
public class Move {
        public static final String MOVE_ACROSS = "Across";
        public static final String MOVE_DOWN = "Down";
        private String word;
        private String direction;
        private GridRef location;

        public Move() {

        }

        public Move(GridRef location, String direction, String word) {
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

        public String getOppositeDirection(String direction) {
                if ( direction.equalsIgnoreCase(MOVE_ACROSS))
                        return MOVE_DOWN;
                else
                        return MOVE_ACROSS;
        }
}
