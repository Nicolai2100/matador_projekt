package game;

import game.controller.GameController;
import game.model.Game;

/**
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Main {
    /**
     * The main method which creates a game, shuffles the chance
     * cards, creates players, and then starts the game. Note
     * that, eventually, the game could be loaded from a database.
     *
     * main metoden ændret af Nicolai L
     */
    public static void main(String[] args) {
        Game game = new Game();
        GameController controller = new GameController(game);
        controller.playOrLoadGame();
    }
}
