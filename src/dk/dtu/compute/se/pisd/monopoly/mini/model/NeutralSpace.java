package dk.dtu.compute.se.pisd.monopoly.mini.model;

import dk.dtu.compute.se.pisd.monopoly.mini.controller.GameController;
import dk.dtu.compute.se.pisd.monopoly.mini.model.exceptions.GameEndedException;
import dk.dtu.compute.se.pisd.monopoly.mini.model.exceptions.PlayerBrokeException;

/**
 * Represents a space that has no action associated with it.
 * This class is needed in order to allow the Space-class to be abstract, which makes the JSON-adapter work.
 * @author s185039
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class NeutralSpace extends Space {


    /**
     * The action taken when a player arrives on that fields.
     *
     * @param controller the controller in charge of the game
     * @param player the involved player
     * @throws PlayerBrokeException when the action results in the player going bankrupt
     */
    public void doAction(GameController controller, Player player) throws PlayerBrokeException, GameEndedException {
        // per default there is no action for a neutral space
    };

    @Override
    public String toString() {
        return getName();
    }
}

