package SuperScrabble;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by Gary on 14/06/2016.
 */
public class Board {
        public static final int SINGLE_LETTER = 1;
        public static final int DOUBLE_LETTER = 2;
        public static final int TRIPLE_LETTER = 3;
        public static final int DOUBLE_WORD = 4;
        public static final int TRIPLE_WORD = 5;

        private static final char BOARD_TILE_EMPTY = ' ';
        private static final String BOARD_ACROSS = "Across";
        private static final String BOARD_DOWN = "Down";
        private static final int BOARD_DIMENSION = 15;
        private static final int BOARD_CENTER = 7;
        private static final boolean BOARD_WORD_OUT_OF_BOUNDS = false;
        private static final boolean BOARD_WORD_INBOUNDS = true;
        private static final boolean BOARD_HAS_CORRECT_TILES = true;
        private static final boolean BOARD_HAS_INCORRECT_TILES = false;
        private static final boolean BOARD_WORD_CONFLICT_IGNORABLE_FLAG = true;
        private static final boolean BOARD_WORD_POS_UNACCEPTABLE= false;
        private static final boolean BOARD_WORD_POS_ACCEPTABLE = true;
        private static final boolean BOARD_WORD_CONNECTION_FOUND = true;
        private static final boolean BOARD_WORD_CONNECTION_NOT_FOUND = false;
        private static final boolean BOARD_INVALID_DIRECTION = false;
        private static final boolean BOARD_VALID_DIRECTION = true;
        private boolean BOARD_IS_CONFLICT_IGNORABLE = false;


        private char[][] board;
        private boolean[][] isBoardPosOccupied;
        private ArrayList<GridRef> boardTilePositions;
        public static final int[][] BOARD_POSITION_VALUES = {
                //1, 2, 3, 4, 5, 6, 7, 8, 9, A, B, C, D, E, F
                {5, 1, 1, 2, 1, 1, 1, 5, 1, 1, 1, 2, 1, 1, 5},  // 1
                {1, 4, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 4, 1},  // 2
                {1, 1, 4, 1, 1, 1, 2, 1, 2, 1, 1, 1, 4, 1, 1},  // 3
                {2, 1, 1, 4, 1, 1, 1, 2, 1, 1, 1, 4, 1, 1, 2},  // 4
                {1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 4, 1, 1, 1, 1},  // 5
                {1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1},  // 6
                {1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1},  // 7
                {5, 1, 1, 2, 1, 1, 1, 4, 1, 1, 1, 2, 1, 1, 5},  // 8
                {1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1},  // 9
                {1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1},  // 10
                {1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 4, 1, 1, 1, 1},  // 11
                {2, 1, 1, 4, 1, 1, 1, 2, 1, 1, 1, 4, 1, 1, 2},  // 12
                {1, 1, 4, 1, 1, 1, 2, 1, 2, 1, 1, 1, 4, 1, 1},  // 13
                {1, 4, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 4, 1},  // 14
                {5, 1, 1, 2, 1, 1, 1, 5, 1, 1, 1, 2, 1, 1, 5}   // 15
        };

        public Board() {
                initialize();
        }

        public void playWord(Player player, Move m) {
                String direction = m.getDirection();
                String word = m.getWord();
                int row = m.getLocation().getRow();
                int col = m.getLocation().getColumn();

                if( isAcross(direction) ) {
                        for(int i = 0; i < word.length(); i++) {
                                if(!isBoardPosOccupied[row][col+i]) {
                                        addToBoard(row, col, 0, i, word.charAt(i));
                                        player.getFrame().removeTile(word.charAt(i));
                                }
                        }
                } else {
                        for(int i = 0; i < word.length(); i++) {
                                if(!isBoardPosOccupied[row+i][col]) {
                                        addToBoard(row, col, i, 0, word.charAt(i));
                                        player.getFrame().removeTile(word.charAt(i));
                                }
                        }
                }
                //remove words from frame
                printBoard();
        }

        public boolean checkWord(Frame playerFrame, Move m) {
                //If not first word then needs to connect to another word, Uses at least one tile from rack
                if( !validDirection(m) ) {
                        System.out.println("Invalid direction");
                        return BOARD_INVALID_DIRECTION;
                }
                if( !firstWordAtBoardCenter(m) ) {
                        System.out.println("Not at center");
                        return BOARD_WORD_POS_UNACCEPTABLE;
                }
                if( !isWordInBounds(m) ) {
                        System.out.println("Word out of bounds");
                        return BOARD_WORD_OUT_OF_BOUNDS;
                }
                if( !isWordConnectedToAnotherTile(m) ) {
                        System.out.println("Not connected");
                        return BOARD_WORD_POS_UNACCEPTABLE;
                }
                if( !doesPlayerHaveNecessaryTiles(playerFrame, m) ) {
                        System.out.println("Dont have necessary tiles");
                        return BOARD_HAS_INCORRECT_TILES;
                }
                if( !doesWordPositionCauseConflicts(m) ) {
                        System.out.println("Causes conflicts");
                        return BOARD_WORD_POS_UNACCEPTABLE;
                }

                return BOARD_WORD_POS_ACCEPTABLE;
        }

        private boolean validDirection(Move m) {
                String direction = m.getDirection();
                if( isAcross(direction) || isDown(direction) ) {
                        return BOARD_VALID_DIRECTION;
                }

                return BOARD_INVALID_DIRECTION;
        }

        private void initialize() {
                board = new char[BOARD_DIMENSION][BOARD_DIMENSION];
                isBoardPosOccupied = new boolean[BOARD_DIMENSION][BOARD_DIMENSION];
                boardTilePositions = new ArrayList<>();

                for(int row = 0; row < board.length; row++) {
                        for(int col = 0; col < board[0].length; col++) {
                                board[row][col] = ' ';
                                isBoardPosOccupied[row][col] = false;
                        }
                }
        }

        private boolean firstWordAtBoardCenter(Move m) {
                int row = m.getLocation().getRow();
                int col = m.getLocation().getColumn();

                if( boardTilePositions.isEmpty() && (row != BOARD_CENTER || col != BOARD_CENTER)) {
                    return BOARD_WORD_POS_UNACCEPTABLE;
                }
                return BOARD_WORD_POS_ACCEPTABLE;
        }

        private boolean isWordConnectedToAnotherTile(Move m) {
                if( boardTilePositions.isEmpty() ) {
                        return BOARD_WORD_POS_ACCEPTABLE;
                }
                if( isTileAlongWordSides(m) || isTileAtEitherEndOfWord(m) ) {
                        return BOARD_WORD_POS_ACCEPTABLE;
                }

                return BOARD_WORD_POS_UNACCEPTABLE;
        }

        private boolean isWordInBounds(Move m) {
                String direction = m.getDirection();
                String word = m.getWord();
                int row = m.getLocation().getRow();
                int col = m.getLocation().getColumn();

                if( isAcross(direction) ) {
                        if(col + word.length() >= BOARD_DIMENSION) {
                                return BOARD_WORD_OUT_OF_BOUNDS;
                        }
                } else {
                        if(row + word.length() >= BOARD_DIMENSION) {
                                return BOARD_WORD_OUT_OF_BOUNDS;
                        }
                }

                return BOARD_WORD_INBOUNDS;
        }

        private boolean doesWordPositionCauseConflicts(Move m) {
                String direction = m.getDirection();
                String word = m.getWord();
                int row = m.getLocation().getRow();
                int col = m.getLocation().getColumn();

                if( isAcross(direction) ) {
                        for(int i=0; i < word.length();i++) {
                                if( !hasConflictAt(row, col, 0, i) ) {
                                        return BOARD_WORD_POS_UNACCEPTABLE;
                                }
                        }
                } else {
                        for(int i=0; i < word.length();i++) {
                                if( !hasConflictAt(row, col, i, 0) ) {
                                        return BOARD_WORD_POS_UNACCEPTABLE;
                                }
                        }
                }
                return BOARD_WORD_POS_ACCEPTABLE;
        }

        private boolean hasConflictAt(int row, int col, int v, int h) {
                if(isBoardPosOccupied[row + v][col + h] ) {
                        if(BOARD_IS_CONFLICT_IGNORABLE) {
                                return BOARD_WORD_POS_ACCEPTABLE;
                        } else {
                                return BOARD_WORD_POS_UNACCEPTABLE;
                        }
                }

                return BOARD_WORD_POS_ACCEPTABLE;
        }

        /**
         * The stack prevents multiple identical characters from the input word being matched to the same tile letter
         * from the frame.
         *
         * @param playerFrame The player to check
         * @param m           The players move
         * @return true if player has correct tiles, false if they don't
         */
        private boolean doesPlayerHaveNecessaryTiles(Frame playerFrame, Move m) {
                Stack<Tile> lettersMatched = new Stack<>();
                Frame testFrame = new Frame(playerFrame);
                String word = m.getWord();
                int indexOfCharFromFrame;

                for(int i=0; i < word.length(); i++) {
                        char letter = word.charAt(i);
                        indexOfCharFromFrame = testFrame.hasTile(letter);

                        if( indexOfCharFromFrame == Frame.FRAME_TILE_NOT_FOUND ) {
                                if( !isLetterOnBoard(letter, m, i) ) {
                                        BOARD_IS_CONFLICT_IGNORABLE = !BOARD_WORD_CONFLICT_IGNORABLE_FLAG;

                                        return BOARD_HAS_INCORRECT_TILES;
                                }
                                BOARD_IS_CONFLICT_IGNORABLE = BOARD_WORD_CONFLICT_IGNORABLE_FLAG;
                        } else {
                                if( isLetterOnBoard(letter, m, i) ) {
                                        BOARD_IS_CONFLICT_IGNORABLE = BOARD_WORD_CONFLICT_IGNORABLE_FLAG;
                                } else {
                                        lettersMatched.push( testFrame.getTiles().get(indexOfCharFromFrame) );
                                }

                        }
                }

                return BOARD_HAS_CORRECT_TILES;
        }

        /**
         * This is a panic mode style method. If the user enters "hello" but dont have an 'e' in their frame this method
         * checks the index that would coincide with the 'e' character on the board to see if the user meant to use the
         * tile from the board.
         *
         * @param letterToFind  Character to find
         * @param m       Players move object
         * @param atIndex Expect to find character at this index
         * @return true if found on board, false if not
         */
        private boolean isLetterOnBoard(char letterToFind, Move m, int atIndex) {
                String direction = m.getDirection();
                int row = m.getLocation().getRow();
                int col = m.getLocation().getColumn();

                if( isAcross(direction) ) {
                        return letterToFind == board[row][col + atIndex];
                }

                return letterToFind == board[row + atIndex][col];
        }

        private boolean isTileAlongWordSides(Move m) {
                String direction = m.getDirection();
                String word = m.getWord();
                int row = m.getLocation().getRow();
                int col = m.getLocation().getColumn();

                if( isAcross(direction) ) {
                        for(int i=0; i < word.length(); i++) {
                                if( !(row-1 < 0) && isBoardPosOccupied[row - 1][col + i] ) {
                                        return BOARD_WORD_CONNECTION_FOUND;
                                }
                                if( !(row+1 > BOARD_DIMENSION) && isBoardPosOccupied[row + 1][col + i] ) {
                                        return BOARD_WORD_CONNECTION_FOUND;
                                }
                        }
                } else {
                        for(int i=0; i < word.length(); i++) {
                                if( !(col-1 < 0) && isBoardPosOccupied[row + i][col - 1] ) {
                                        return BOARD_WORD_CONNECTION_FOUND;
                                }
                                if( !(col+1 > BOARD_DIMENSION) && isBoardPosOccupied[row + i][col + 1] ) {
                                        return BOARD_WORD_CONNECTION_FOUND;
                                }
                        }
                }
                return BOARD_WORD_CONNECTION_NOT_FOUND;
        }

        private boolean isTileAtEitherEndOfWord(Move m) {
                String direction = m.getDirection();
                int row = m.getLocation().getRow();
                int col = m.getLocation().getColumn();

                if( isAcross(direction) ) {
                        if( !(col-1 < 0) && isBoardPosOccupied[row][col - 1] ) {
                                return BOARD_WORD_CONNECTION_FOUND;
                        }
                        if( !(col+1 > BOARD_DIMENSION) && isBoardPosOccupied[row][col + 1] ) {
                                return BOARD_WORD_CONNECTION_FOUND;
                        }
                } else {
                        if( !(row-1 < 0) && isBoardPosOccupied[row - 1][col] ) {
                                return BOARD_WORD_CONNECTION_FOUND;
                        }
                        if( !(row+1 > BOARD_DIMENSION) && isBoardPosOccupied[row + 1][col] ) {
                                return BOARD_WORD_CONNECTION_FOUND;
                        }
                }

                return BOARD_WORD_CONNECTION_NOT_FOUND;
        }

        private boolean isAcross(String direction) {
                return direction.equalsIgnoreCase(BOARD_ACROSS);
        }

        private boolean isDown(String direction) {
                return direction.equalsIgnoreCase(BOARD_DOWN);
        }

        // v for vertical play, h for horizontal play... can this be made better?
        private void addToBoard(int row, int col, int v, int h, char c) {
                board[row + v][col + h] = c;
                isBoardPosOccupied[row + v][col + h] = true;
                boardTilePositions.add( new GridRef(row, col) );
        }

        private void displayBoardHeader() {
                char column = 'A';
                System.out.print("    ");
                for (int c = 0; c < BOARD_DIMENSION; c++) {
                        System.out.printf("%c ", column);
                        column++;
                }
                System.out.println();
        }

        //Tests, remove later
        public void printBoard() {
                int row = 1;
                char boardChar;

                displayBoardHeader();
                for(int r=0; r < BOARD_DIMENSION; r++) {
                        System.out.printf("%2d| ", row);
                        for(int c=0; c < BOARD_DIMENSION; c++) {
                                boardChar = board[r][c];

                                if(boardChar == BOARD_TILE_EMPTY) {
                                        switch( BOARD_POSITION_VALUES[r][c] ) {
                                                case SINGLE_LETTER:
                                                        System.out.print("  ");
                                                        break;
                                                case DOUBLE_LETTER:
                                                        System.out.print("DL");
                                                        break;
                                                case TRIPLE_LETTER:
                                                        System.out.print("TL");
                                                        break;
                                                case DOUBLE_WORD:
                                                        System.out.print("DW");
                                                        break;
                                                case TRIPLE_WORD:
                                                        System.out.print("TW");
                                                        break;
                                        }
                                } else {
                                        System.out.printf("%c ", boardChar);
                                }
                        }
                        System.out.printf(" |%2d\n", row);
                        row++;
                }
                displayBoardHeader();
                System.out.println();
        }

        public void reset() {
                initialize();
        }
}
