package game;

import game.controller.GameController;

/**
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Main {
    /**
     * The main method which creates a game, shuffles the chance
     * cards, creates players, and then starts the game. Note
     * that, eventually, the game could be loaded from a database.
     *
     * main metoden ændret af Nicolai J. Larsen, s185020@student.dtu.dk
     */
    public static void main(String[] args) {
        //System.setProperty("file.encoding", "UTF-8");
        new GameController().playOrLoadGame();
    }
}
