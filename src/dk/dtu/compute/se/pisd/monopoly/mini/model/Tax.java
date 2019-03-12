package dk.dtu.compute.se.pisd.monopoly.mini.model;

import dk.dtu.compute.se.pisd.monopoly.mini.controller.GameController;
import dk.dtu.compute.se.pisd.monopoly.mini.model.exceptions.GameEndedException;
import dk.dtu.compute.se.pisd.monopoly.mini.model.exceptions.PlayerBrokeException;

/**
 * Represents a space, where the player has to pay tax.
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Tax extends Space {

	@Override
	public void doAction(GameController controller, Player player) throws PlayerBrokeException, GameEndedException {
		// TODO note that tax concerns all assets an not just cash
		controller.paymentToBank(player, player.getBalance() / 10);

		if (player.isBroke()){
			throw new PlayerBrokeException(player);
		}
	}

}
