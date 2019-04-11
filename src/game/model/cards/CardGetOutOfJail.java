package game.model.cards;

import game.controller.GameController;
import game.model.Card;
import game.model.Player;
import game.model.exceptions.GameEndedException;
import game.model.exceptions.PlayerBrokeException;

import java.util.List;

/**
 * A get-out-of-jail-card that a player obtains
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author s185039
 *
 */
public class CardGetOutOfJail extends Card {

	@Override
	public void doAction(GameController controller, Player player) throws PlayerBrokeException, GameEndedException {

		try {
			List<Card> ownedcards = player.getOwnedCards();
			ownedcards.add(this);
			player.setOwnedCards(ownedcards);
		} finally {
			// Make sure that the card is returned to the deck even when
			// an Exception should occur!
			super.doAction(controller, player);
		}
	}
}
