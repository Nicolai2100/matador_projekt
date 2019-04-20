package game.model.cards;

import game.controller.GameController;
import game.model.Card;
import game.model.Player;
import game.model.Space;
import game.model.exceptions.GameEndedException;
import game.model.exceptions.PlayerBrokeException;

/**
 * A card that directs the player to a move to a specific space (location) of the game.
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author s185039
 *
 */

public class CardMove extends Card {

	private int untargetedAmount;
	private boolean toPrison;
	private Space target;

	public CardMove(int untargetedAmount) {
		this.untargetedAmount = untargetedAmount;
	}

	public CardMove(boolean toPrison) {
		this.toPrison = toPrison;
	}

	public CardMove(Space target) {
		this.target = target;
	}

	/** 
	 * Returns the target space to which this card directs the player to go.
	 * 
	 * @return the target of the move
	 */
	public Space getTarget() {
		return target;
	}

	/**
	 * Sets the target space of this card.
	 * 
	 * @param target the new target of the move 
	 */
	public void setTarget(Space target) {
		this.target = target;
	}
	
	@Override
	public void doAction(GameController controller, Player player) throws PlayerBrokeException, GameEndedException {
		try {
			if (untargetedAmount != 0) {
				int targetIndex = player.getCurrentPosition().getIndex() + untargetedAmount;
				//TODO mangler makeMove(int) i controlleren
			} else if (toPrison) {
				controller.gotoJail(player);
			} else {
				controller.moveToSpace(player, target);
			}
		} finally {
			// Make sure that the card is returned to the deck even when
			// an Exception should occur!
			super.doAction(controller, player);
		}
	}
	

	
}
