package game.model.cards;

import game.controller.GameController;
import game.model.Card;
import game.model.Player;
import game.model.exceptions.GameEndedException;
import game.model.exceptions.PlayerBrokeException;

import java.util.ArrayList;

public class GetOutOfJail extends Card {

    @Override
    public void doAction(GameController controller, Player player) throws PlayerBrokeException, GameEndedException {
        ArrayList<Card> cards = new ArrayList<>(player.getOwnedCards());
        cards.add(this);
        player.setOwnedCards(cards);
        super.doAction(controller, player);
    }
}