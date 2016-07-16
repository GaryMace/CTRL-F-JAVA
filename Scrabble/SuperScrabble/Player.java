package SuperScrabble;

/**
 * Created by Gary on 13/06/2016.
 */
public class Player {
        private String pName;
        private int pScore;
        private Frame pFrame;

        public Player() {
                pFrame = new Frame();
        }

        public String getName() {
                return pName;
        }

        public void setName(String pName) {
                this.pName = pName;
        }

        public int getScore() {
                return pScore;
        }

        public void setScore(int pScore) {
                this.pScore = pScore;
        }

        public Frame getFrame() {
                return pFrame;
        }

        public void reset() {
                pScore = 0;
                pFrame = new Frame();
                pName = "";
        }

        public String toString() {
                return pName;
        }
}
