package dk.dtu.compute.se.pisd.monopoly.mini.model;

import dk.dtu.compute.se.pisd.monopoly.mini.controller.GameController;
import dk.dtu.compute.se.pisd.monopoly.mini.model.exceptions.GameEndedException;
import dk.dtu.compute.se.pisd.monopoly.mini.model.exceptions.PlayerBrokeException;

/**
 * Represents a space, where the user has to draw a chance card.
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Chance extends Space {

	@Override
	public void doAction(GameController controller, Player player) throws PlayerBrokeException, GameEndedException {
		controller.takeChanceCard(player);
	}

}
