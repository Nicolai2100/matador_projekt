package game.model.cards;

import game.controller.GameController;
import game.model.Card;
import game.model.Player;
import game.model.exceptions.GameEndedException;
import game.model.exceptions.PlayerBrokeException;

/**
 * A card that directs the player to pay tax to the bank. The tax amounts
 * 10% of the balance of the player's account.
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class PayTax extends Card {

	@Override
	public void doAction(GameController controller, Player player) throws PlayerBrokeException, GameEndedException {
		// TODO note that tax concerns all assets an not just cash
		//      this is just a simple  way of implementing tax
		try {
			controller.paymentToBank(player, player.getBalance() / 10);
		} finally {
			// Make sure that the card is returned to the deck even when
			// an Exception should occur!
			super.doAction(controller, player);
		}
	}
}
