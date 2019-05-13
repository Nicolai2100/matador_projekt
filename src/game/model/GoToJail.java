package game.model;


import game.controller.GameController;
import game.model.exceptions.GameEndedException;
import game.model.exceptions.PlayerBrokeException;

/**
 * Represents the space that imprisons a player, sending the player to the jail.
 * This class is needed in order to allow the Space-class to be abstract, which makes the JSON-adapter work.
 * @author s185039
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class GoToJail extends Space {

    /**
     * The action taken when a player arrives on that fields.
     *
     * @param controller the controller in charge of the game
     * @param player the involved player
     * @throws PlayerBrokeException when the action results in the player going bankrupt
     */
    public void doAction(GameController controller, Player player) throws PlayerBrokeException, GameEndedException {
        controller.showMessage(player + " ryger i fængsel!");
        controller.gotoJail(player);
    };

    @Override
    public String toString() {
        return getName();
    }
}

