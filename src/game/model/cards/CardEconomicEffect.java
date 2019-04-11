package game.model.cards;

import game.controller.GameController;
import game.model.Card;
import game.model.Player;
import game.model.Property;
import game.model.exceptions.GameEndedException;
import game.model.exceptions.PlayerBrokeException;
import game.model.properties.RealEstate;

/**
 * A card that directs the bank to pay a specific amount to the player.
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author s185039
 *
 */
public class CardEconomicEffect extends Card {
	
	private int amount;
	private boolean paymentFromOpponents;
	private boolean paymentPerHouse;
	private boolean isGrant;

	public CardEconomicEffect(int amount) {
		this.amount = amount;
	}

	public CardEconomicEffect(int amount, boolean fromOpponents) {
		this.amount = amount;
		this.paymentFromOpponents = fromOpponents;
	}

	public CardEconomicEffect(boolean perHouse) {
		this.paymentPerHouse = perHouse;
	}

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

	@Override
	public void doAction(GameController controller, Player player) throws PlayerBrokeException, GameEndedException {
		try {
			if (paymentFromOpponents) {
				//TODO kortet kender ikke til spillerne...
			} else if (paymentPerHouse){
				int houseCount = 0;
				for (Property property : player.getOwnedProperties()) {
					if (property instanceof RealEstate) {
						houseCount += ((RealEstate) property).getHouseCount();
					}
				}
				setAmount(houseCount * 500);
				controller.paymentToBank(player, amount);
			} else if (isGrant) {
				//TODO gider man dette?
			} else if (amount >= 0){
				controller.paymentFromBank(player, amount);
			} else {
				controller.paymentToBank(player, -amount);
			}
		} finally {
			// Make sure that the card is returned to the deck even when
			// an Exception should occur!
			super.doAction(controller, player);
		}
	}
	
}
