package game.model;

import game.controller.GameController;
import game.model.exceptions.GameEndedException;
import game.model.exceptions.PlayerBrokeException;

/**
 * Represents a space, where the player has to pay income tax.
 * The player may choose to either pay 4000, or pay 10% of all values.
 * @author Nicolai W s185036
 *
 */
public class Tax1 extends Space {

    @Override
    public void doAction(GameController controller, Player player) throws PlayerBrokeException, GameEndedException {
        controller.playSound("dissapointed.wav");
        String choice = controller.getGui().getUserButtonPressed("Vil du betale 4000 kr., eller 10% af alle dine værdier?", "4000 kr.", "10%");
        if (choice.equals("4000 kr.")) {
            controller.paymentToBank(player, 4000);
        } else {
            controller.paymentToBank(player, player.calculateNetWorth(true) / 10);
        }
        if (player.isBroke()){
            throw new PlayerBrokeException(player);
        }
    }
}
