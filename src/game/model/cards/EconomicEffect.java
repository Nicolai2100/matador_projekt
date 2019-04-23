package game.model.cards;

import game.controller.GameController;
import game.model.Card;
import game.model.Player;
import game.model.Property;
import game.model.exceptions.GameEndedException;
import game.model.exceptions.PlayerBrokeException;
import game.model.properties.RealEstate;

import java.util.List;

/**
 * A card that directs the bank to pay a specific amount1 to the player.
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class EconomicEffect extends Card {
	
	private int amount1;
	private int amount2;
	public enum EffectType {FROM_BANK, FROM_OTHER_PLAYERS, TO_BANK, TO_BANK_PER_HOUSE_AND_HOTEL, MATADOR_GRANT}
	private EffectType effectType;

	public EconomicEffect(EffectType effectType) {
	    this.effectType = effectType;
    }

	/**
	 * Returns the amount1 this card directs the bank to pay to the player.
	 *  
	 * @return the amount1
	 */
	public int getAmount1() {
		return amount1;
	}

	/**
	 * Sets the amount1 this card directs the bank to pay to the player.
	 * 
	 * @param amount1 the new amount1
	 */
	public void setAmount1(int amount1) {
		this.amount1 = amount1;
	}

    public void setAmount2(int amount2) {
        this.amount2 = amount2;
    }

    @Override
	public void doAction(GameController controller, Player player) throws PlayerBrokeException, GameEndedException {
		try {
		    switch (effectType) {
                case FROM_BANK:
                    controller.paymentFromBank(player, amount1);
                    break;
                case FROM_OTHER_PLAYERS :
                    List<Player> players = controller.getGame().getActivePlayers(true);
                    for (Player p : players) {
                        if (p != player) {
                            controller.payment(p, amount1, player);
                        }
                    }
                    break;
                case TO_BANK:
                    controller.paymentToBank(player, amount1);
                    break;
                case TO_BANK_PER_HOUSE_AND_HOTEL:
                    int houses = 0;
                    int hotels = 0;
                    for (Property p : player.getOwnedProperties()) {
                        if (p instanceof RealEstate) {
                            if (((RealEstate) p).getHouseCount() < 5) {
                                houses += ((RealEstate) p).getHouseCount();
                            } else if (((RealEstate) p).getHouseCount() == 5) {
                                hotels++;
                            }
                        }
                    }
                    controller.paymentToBank(player, houses * amount1 + hotels * amount2);
                    break;
                case MATADOR_GRANT:
                    int totalSubstance = player.getBalance();
                    for (Property p : player.getOwnedProperties()) {
                        if (!p.getMortgaged()) {
                            totalSubstance += p.getCost();
                        } else if (p.getMortgaged()) {
                            totalSubstance += p.getCost() / 2;
                        }
                        if (p instanceof RealEstate) {
                            totalSubstance += ((RealEstate) p ).getHouseCount() * ((RealEstate) p ).getPriceForHouse();
                        }
                    }
                    if (totalSubstance <= 15000) {
                        controller.showMessage(player + ", din formue overstiger ikke 15000 kr, og du modtager derfor Matador-legatet på kr. 40.000, tillykke!");
                        controller.paymentFromBank(player, amount1);
                    } else {
                        controller.showMessage(player + ", din formue overstiger 15000 kr, og du modtager derfor ikke Matador-legatet.");
                    }

            }

		} finally {
			// Make sure that the card is returned to the deck even when
			// an Exception should occur!
			super.doAction(controller, player);
		}
	}
	
}
