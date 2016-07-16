package SuperScrabble;

import java.util.Scanner;

/**
 * Created by Gary on 19/06/2016.
 */
public class UI {
        private static final int UI_COMMAND_INVALID = -2;
        public static final int UI_COMMAND_QUIT = -1;
        public static final int UI_COMMAND_PLAY = 0;
        public static final int UI_COMMAND_PASS = 1;
        public static final int UI_COMMAND_EXCHANGE = 2;
        public static final int UI_COMMAND_CHALLENGE = 3;
        public static final int UI_COMMAND_HELP = 4;

        public static final int UI_NO_ERROR = 0;
        public static final int UI_ERROR_INVALID_DIRECTION = 1;
        public static final int UI_ERROR_FIRST_WORD_NOT_AT_CENTER = 2;
        public static final int UI_ERROR_WORD_OUT_OF_BOUNDS = 3;
        public static final int UI_ERROR_WORD_CONNECTION_NOT_FOUND = 4;
        public static final int UI_ERROR_INCORRECT_TILES = 5;
        public static final int UI_ERROR_WORD_PLACEMENT_CONFLICT = 6;
        public static final int UI_ERROR_NOT_ENOUGH_TILES_IN_POOL_TO_EXCHANGE = 7;
        public static final int UI_ERROR_TILE_TO_EXCHANGE_NOT_IN_FRAME = 8;
        private Move currentPlayerMove;
        private String exchangeLetters;


        public UI() {
                currentPlayerMove = new Move();
        }

        public static void welcomeMessageAndDoop() {
                System.out.println("+---------+-----------------------WELCOME TO SCRABBLE-----------------------+");
                System.out.println("| {'-'}:: My Name's Doop. I'll be your trusty UI bot for the game. Whenever |");
                System.out.println("|         you see me I want you to type some stuff! If at any point you get |");
                System.out.println("|         sad or lost type 'Help' or 'H' and I'll remind you how to play.   |");
                System.out.println("|                                                                           |");
                System.out.println("|         Move syntax is row(1-15) col(A-O) across|down 'word'              |");
                System.out.println("|         E.g. '8 H Across doop'                                            |");
                System.out.println("+---------+-----------------------------------------------------------------+");
                System.out.println();
        }

        public static void displayHelp() {
                System.out.println("+---------+-----------------------------------------------------------------+");
                System.out.println("|{'-'}    |  Game Info:                                                     |");
                System.out.println("+---------+-----------------------------------------------------------------+");
                System.out.println("|Quit     | Enter 'Quit' or 'Q' to end the game                             |");
                System.out.println("|Pass     | Enter 'Pass' or 'P' to pass turn                                |");
                System.out.println("|Exchange | Enter 'Exchange <letters>' or 'E <letters>' to exchange letters |");
                System.out.println("|Challenge| Enter 'Challenge' or 'C' to challenge previous play             |");
                System.out.println("|Play word| Enter '<Row> <Col> <Across|Down> <letters>' to make a play      |");
                System.out.println("+---------+-----------------------------------------------------------------+");
        }

        public static void goodbyeMessage() {
                System.out.println("{'-'} Thanks for playing!");
        }

        public static void displayExchangeSuccessful() {
                System.out.println("{'-'}:: Exchange Successful, ending your turn!");
        }

        public static void displayPlayerScores(Player p1, Player p2) {
                System.out.println(p1.getName()+"'s score is: "+p1.getScore()+"\t\t"+p2.getName()+"'s score is:"+p2.getScore());
        }

        public static void displayWordScore() {

        }

        private void displayBoardHeader() {
                char column = 'A';
                System.out.print("    ");
                for (int c = 0; c < Board.BOARD_DIMENSION; c++) {
                        System.out.printf("%c ", column);
                        column++;
                }
                System.out.println();
        }

        public static void displayError(int commandCode) {
                switch(commandCode) {
                        case UI_ERROR_INVALID_DIRECTION:
                                System.out.println("{'o'}:: You entered an invalid direction, try using HELP!");
                                break;
                        case UI_ERROR_FIRST_WORD_NOT_AT_CENTER:
                                System.out.println("{'o'}:: The first move must be in the centre of the board! {8 H}");
                                break;
                        case UI_ERROR_WORD_OUT_OF_BOUNDS:
                                System.out.println("{'o'}:: Your move went out of bounds somewhere!");
                                break;
                        case UI_ERROR_WORD_CONNECTION_NOT_FOUND:
                                System.out.println("{'o'}:: Your word didn't connect to another word on the board!");
                                break;
                        case UI_ERROR_INCORRECT_TILES:
                                System.out.println("{'o'}:: You don't have the correct tiles for that move!");
                                break;
                        case UI_ERROR_WORD_PLACEMENT_CONFLICT:
                                System.out.println("{'o'}:: Your move conflicts with existing board tiles!");
                                break;
                        case UI_ERROR_NOT_ENOUGH_TILES_IN_POOL_TO_EXCHANGE:
                                System.out.println("{'o'}:: There's not enough tiles left in the pool to exchange your input!");
                                break;
                        case UI_ERROR_TILE_TO_EXCHANGE_NOT_IN_FRAME:
                                System.out.println("{'o'}:: You can't exchange a tile that's not in your frame!");
                                break;
                }
        }

        public void displayBoard(Board board) {
                int row = 1;
                char boardChar;

                displayBoardHeader();
                for(int r=0; r < Board.BOARD_DIMENSION; r++) {
                        System.out.printf("%2d| ", row);
                        for(int c=0; c < Board.BOARD_DIMENSION; c++) {
                                boardChar = board.getBoardValueAt(r, c);

                                if(boardChar == Board.BOARD_TILE_EMPTY) {
                                        switch( Board.BOARD_POSITION_VALUES[r][c] ) {
                                                case Board.SINGLE_LETTER:
                                                        System.out.print("  ");
                                                        break;
                                                case Board.DOUBLE_LETTER:
                                                        System.out.print("DL");
                                                        break;
                                                case Board.TRIPLE_LETTER:
                                                        System.out.print("TL");
                                                        break;
                                                case Board.DOUBLE_WORD:
                                                        System.out.print("DW");
                                                        break;
                                                case Board.TRIPLE_WORD:
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

        public String getPlayerName(int playerNumber) {
                Scanner sc = new Scanner(System.in);
                String playerName;
                System.out.println("{'-'}:: Player"+playerNumber+", enter your name: ");

                do {
                        playerName = sc.nextLine();
                        playerName = playerName.trim();
                        if( playerName.equals("") ) {
                                System.out.println("{'o'}:: You left your name blank....");
                        }
                } while(playerName.equals(""));

                return playerName;
        }

        public int getPlayerCommand(Player p) {
                Scanner sc = new Scanner(System.in);
                Frame playerFrame = p.getFrame();
                String playerCommand;
                System.out.println("{'-'}:: "+p.getName() + ", please enter your command: ");
                System.out.println("        Your frame contents are: " + playerFrame);
                int commandCode;

                do {
                        playerCommand = sc.nextLine();
                        playerCommand = playerCommand.trim();
                        commandCode = parsePlayerCommand(playerCommand);
                } while( commandCode == UI_COMMAND_INVALID );

                return commandCode;
        }


        private int parsePlayerCommand(String playerCommand) {
                String moveRegex = "([1-9]|1[0-5])(\\s+)[A-O](\\s+)(Across|Down|across|down)(\\s+)([A-Za-z]){1,15}";
                String exchangeRegex = "(EXCHANGE|exchange|E|e)(\\s+)([a-z*]){1,7}";

                if( playerCommand.matches(moveRegex) ) {
                        parseMove(playerCommand);
                        return UI_COMMAND_PLAY;
                } else if( playerCommand.equalsIgnoreCase("Help") || playerCommand.equalsIgnoreCase("H") ) {
                        return UI_COMMAND_HELP;
                } else if( playerCommand.equalsIgnoreCase("Quit") || playerCommand.equalsIgnoreCase("Q") ) {
                        return UI_COMMAND_QUIT;
                } else if( playerCommand.equalsIgnoreCase("Challenge") || playerCommand.equalsIgnoreCase("C") ) {
                        return UI_COMMAND_CHALLENGE;
                } else if( playerCommand.matches(exchangeRegex) ) {
                        parseExchange(playerCommand);
                        return UI_COMMAND_EXCHANGE;
                } else if( playerCommand.equalsIgnoreCase("Pass") || playerCommand.equalsIgnoreCase("P") ) {
                        return UI_COMMAND_PASS;
                }
                System.out.println("{'o'}:: Error, Incorrect syntax; Try using 'Help'");
                return UI_COMMAND_INVALID;
        }

        // 'B' - 'A' will yield a 2 which is then used for a board position etc...
        private void parseMove(String playerMove) {
                String[] moveParts = playerMove.split("\\s+");
                int row = Integer.parseInt(moveParts[0]) - 1;
                char colChar = moveParts[1].charAt(0);
                int col =  (int)colChar - (int)'A';
                String word = moveParts[3];
                String direction = moveParts[2];

                currentPlayerMove = new Move( new GridRef(row, col), direction, word);
        }

        //Removes E or Exchange and only takes the letters inputted
        private void parseExchange(String playerMove) {
                String[] moveParts = playerMove.split("( )+");
                exchangeLetters = moveParts[1];
        }

        public void displayPoolSize(Pool p) {
                System.out.println("Pool size is: "+p.size());
        }

        public Move currentMove() {
                return currentPlayerMove;
        }

        public String getExchangeLetters() {
                return exchangeLetters;
        }

        public static void displayPassMessage() {
                System.out.println("{'-'}:: You passed your move.. shame");
        }
}
