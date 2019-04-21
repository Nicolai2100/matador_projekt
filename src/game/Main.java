package game;

import json.JSONUtility;
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
        JSONUtility ju = new JSONUtility();
        Game game = ju.createGame();

        /*
        //midlertidigt bare så det virker
        ArrayList<Card> temp = new ArrayList<>();
        CardReceiveMoney card = new CardReceiveMoney();
        card.setText("You receive 100$ from the bank.");
        card.setAmount(100);
        temp.add(card);
        game.setCardDeck(temp);
        //*/

        GameController controller = new GameController(game);
        controller.playOrLoadGame();
    }
}
