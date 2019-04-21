package game.model.cards;

import game.controller.GameController;
import game.model.Card;
import game.model.Player;
import game.model.exceptions.GameEndedException;
import game.model.exceptions.PlayerBrokeException;

import java.util.List;

/**
 * A card that directs the bank to pay a specific amount to the player.
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class CardReceiveMoney extends Card {
	
	private int amount;
	public enum Payers {BANK, OTHER_PLAYERS}
	private Payers payer = Payers.BANK;
	private List<Player> players;

	/**
	 * Returns the amount this card directs the bank to pay to the player.
	 *  
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Sets the amount this card directs the bank to pay to the player.
	 * 
	 * @param amount the new amount
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

    public void setPayer(Payers payer) {
        this.payer = payer;
    }

    public void setPayer(Payers payer, List<Player> players) {
        this.payer = payer;
        this.players = players;

    }

    @Override
	public void doAction(GameController controller, Player player) throws PlayerBrokeException, GameEndedException {
		try {
		    switch (payer) {
                case BANK:
                    controller.paymentFromBank(player, amount);
                    break;
                case OTHER_PLAYERS:
                    for (Player p : players) {
                        if (p != player) {
                            controller.payment(p, amount, player);
                        }
                    }
                    break;
            }

		} finally {
			// Make sure that the card is returned to the deck even when
			// an Exception should occur!
			super.doAction(controller, player);
		}
	}
	
}
