package game.model;

import game.controller.GameController;
import game.model.exceptions.GameEndedException;
import game.model.exceptions.PlayerBrokeException;

/**
 * Represents a space, where the player has to pay tax.
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Tax extends Space {

	@Override
	public void doAction(GameController controller, Player player) throws PlayerBrokeException, GameEndedException {
		controller.paymentToBank(player, 2000);
		controller.playSound("dissapointed.wav");
		if (player.isBroke()){
			throw new PlayerBrokeException(player);
		}
	}
}
