package game.model;

import game.controller.GameController;
import game.model.exceptions.GameEndedException;
import game.model.exceptions.PlayerBrokeException;

/**
 * Represents a space, where the user has to draw a chance card.
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Chance extends Space {

	@Override
	public void doAction(GameController controller, Player player) throws PlayerBrokeException, GameEndedException {
		controller.playSound("chance.wav");
		controller.takeChanceCard(player);
	}
}
