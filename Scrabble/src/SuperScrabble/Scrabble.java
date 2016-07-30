package SuperScrabble;

import java.util.ArrayList;

/**
 * Created by Gary on 19/06/2016.
 */
public class Scrabble {
        private static final int MAX_PLAYERS = 2;
        private UI ui;
        private Board b;
        private Pool pool;
        private Player[] players;

        public Scrabble() {
                ui = new UI();
                b = new Board();
                pool = new Pool();
                players = new Player[MAX_PLAYERS];
        }

        public void intialisePlayers() {
                for (int i = 0; i < MAX_PLAYERS; i++) {
                        players[i] = new Player();
                }
        }

        public void initialisePlayerFrames() {
                for (Player p : players) {
                        p.getFrame().refillFrame(pool);
                }
        }

        public void getPlayerNames() {
                for (int i = 0; i < players.length; i++) {
                        Player p = players[i];
                        p.setName(ui.getPlayerName(i + 1));
                }
        }

        public ArrayList<Player> determineWhoGoesFirst() {
                char[] tilesDrawn = new char[MAX_PLAYERS];
                ArrayList<Player> playOrder;

                System.out.println("\n{'-'}:: Determining who goes first:");
                do {
                        playOrder = new ArrayList<>();
                        for (int i = 0; i < players.length; i++) {
                                do {
                                        tilesDrawn[i] = pool.drawTile().getFace();
                                } while (playerDrewBlankTile(tilesDrawn[i]));
                                System.out.println(players[i].getName() + " draws: " + tilesDrawn[i] + "\n");
                        }
                        if (tilesDrawn[0] < tilesDrawn[1]) {
                                playOrder = initialiseWhoGoesFirst(players[0], players[1]);
                                break;
                        } else if (tilesDrawn[1] < tilesDrawn[0]) {
                                playOrder = initialiseWhoGoesFirst(players[1], players[0]);
                                break;
                        }
                        System.out.println("Players tied, drawing again!");
                } while (tilesDrawn[0] == tilesDrawn[1]);

                pool.resetPool();
                return playOrder;
        }

        /**
         * Gary:: Handles player input codes
         *
         * @param player Current player
         * @param playerCommand Command code symbolising the users intent
         * @return true if the current player is to be reprompted, false if not
         */
        public boolean handlePlayerCommand(Player player, int playerCommand) {
                boolean keepPromptingCurrPlayer = false;
                int validityFlag;
                int wordScore;

                switch (playerCommand) {
                        case UI.UI_COMMAND_QUIT:
                                //UI.showGameResults();
                                UI.goodbyeMessage();
                                System.exit(0);
                                break;
                        case UI.UI_COMMAND_PLAY:
                                validityFlag = validMove(player);
                                if (validityFlag == UI.UI_NO_ERROR) {
                                        wordScore = makeMove(player);
                                        UI.displayWordScore(wordScore, player);
                                } else {
                                        UI.displayError(validityFlag);
                                        keepPromptingCurrPlayer = true;
                                }
                                break;
                        case UI.UI_COMMAND_HELP:
                                UI.displayHelp();
                                keepPromptingCurrPlayer = true;
                                break;
                        case UI.UI_COMMAND_PASS:
                                UI.displayPassMessage();
                                break;
                        case UI.UI_COMMAND_EXCHANGE:
                                validityFlag = isPlayerExchangeValid(player);
                                if (validityFlag == UI.UI_NO_ERROR) {
                                        exchangeLetters(player);
                                        UI.displayExchangeSuccessful();
                                } else {
                                        UI.displayError(validityFlag);
                                        keepPromptingCurrPlayer = true;
                                }
                                break;
                        case UI.UI_COMMAND_CHALLENGE:
                                break;
                }
                return keepPromptingCurrPlayer;
        }

        private boolean playerDrewBlankTile(char c) {
                return c == Pool.BLANK_TILE;
        }

        private ArrayList<Player> initialiseWhoGoesFirst(Player first, Player second) {
                ArrayList<Player> playOrder = new ArrayList<>();
                playOrder.add(first);
                playOrder.add(second);
                System.out.println(first + " goes first!\n");

                return playOrder;
        }

        private int isPlayerExchangeValid(Player player) {
                int moveCode = UI.UI_NO_ERROR;
                Frame playerFrame = player.getFrame();
                String exchangeLetters = ui.getExchangeLetters();

                if (pool.size() < exchangeLetters.length()) {
                        moveCode = UI.UI_ERROR_NOT_ENOUGH_TILES_IN_POOL_TO_EXCHANGE;
                } else {
                        if (!playerFrame.hasTiles(exchangeLetters)) {
                                moveCode = UI.UI_ERROR_INCORRECT_TILES;
                        }
                }
                return moveCode;
        }

        public void exchangeLetters(Player player) {
                Frame playerFrame = player.getFrame();
                String exchangeLetters = ui.getExchangeLetters();
                ArrayList<Tile> replacedLetters = new ArrayList<>();

                for(char letter : exchangeLetters.toCharArray()) {
                        replacedLetters.add(playerFrame.removeTile(letter));
                }
                playerFrame.refillFrame(pool);
                replacedLetters.stream()
                               .forEach(t -> pool.putBackTile(t));
        }

        public int validMove(Player currPlayer) {
                return b.checkWord(currPlayer.getFrame(), ui.currentMove());
        }

        public int makeMove(Player currPlayer) {
                return b.playWord(currPlayer, ui.currentMove());
        }

        public void runScrabble() {
                ArrayList<Player> playOrder;
                boolean gameOver = false;
                boolean keepPromptingCurrPlayer;

                UI.welcomeMessageAndDoop();
                UI.displayHelp();
                intialisePlayers();
                getPlayerNames();
                playOrder = determineWhoGoesFirst();
                initialisePlayerFrames();

                do {    //While the games not over
                        for (Player player : playOrder) {        //For each player
                                ui.displayBoard(b);
                                UI.displayPlayerScores(players[0], players[1]);
                                ui.displayPoolSize(pool);
                                System.out.println();
                                do {
                                        int playerCommand = ui.getPlayerCommand(player);

                                        //make we make this a standalone method?
                                        keepPromptingCurrPlayer = handlePlayerCommand(player, playerCommand);
                                        //TODO: Left off here, just accept the above BS....For now at least
                                } while (keepPromptingCurrPlayer);
                                player.getFrame().refillFrame(pool);
                        }
                } while (!gameOver);
        }
}
