package game.model.cards;

import game.controller.GameController;
import game.model.Card;
import game.model.Player;
import game.model.Space;
import game.model.exceptions.GameEndedException;
import game.model.exceptions.PlayerBrokeException;
import game.model.properties.Ship;
import java.util.List;

/**
 * A card that directs the player to a move to a specific space (location) of the game.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class MoveEffect extends Card {

    private int targetIndex;
    public enum TargetTypes {SPACE, NEAREST_SHIP_1, NEAREST_SHIP_2, GO_TO_JAIL, THREE_FORWARDS, THREE_BACKWARDS}
    private TargetTypes targetType;

    public MoveEffect(TargetTypes targetType) {
        this.targetType = targetType;
    }

    /**
     * Returns the target space to which this card directs the player to go.
     *
     * @return the target of the move
     */
    public int getTarget() {
        return targetIndex;
    }

    /**
     * Sets the target space of this card.
     *
     * @param targetIndex the new target of the move
     */
    public void setTarget(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public void doAction(GameController controller, Player player) throws PlayerBrokeException, GameEndedException {
        Space target = controller.getGame().getSpaces().get(targetIndex);
        try {
            switch (targetType) {
                case NEAREST_SHIP_1:
                    setTargetToNearestShip(controller, player);
                    ((Ship) target).setRent(((Ship) target).getRent() * 2);
                    player.setCurrentPosition(target);
                    target.doAction(controller, player);
                    ((Ship) target).setRent(((Ship) target).getRent() / 2);
                    break;
                case NEAREST_SHIP_2:
                    setTargetToNearestShip(controller, player);
                    controller.moveToSpace(player, target);
                    break;
                case GO_TO_JAIL:
                    controller.gotoJail(player);
                    break;
                case THREE_FORWARDS:
                    controller.moveToSpace(player, controller.getGame().getSpaces().get((player.getCurrentPosition().getIndex() + 3) % 40));
                    break;
                case THREE_BACKWARDS:
                    //TODO: Ændre eventuel i View, så spilleren rent faktisk rykker bagud, og ikke hele pladen rundt,
                    // til han ender tre felter bag, hvor han først var.
                    Space space = controller.getGame().getSpaces().get((player.getCurrentPosition().getIndex() - 3 + 40) % 40);
                    player.setCurrentPosition(space);
                    space.doAction(controller, player);
                    break;
                default:
                    controller.moveToSpace(player, target);
                    //controller.moveToSpace(player, controller.getGame().getSpaces().get(target));
                    break;
            }
        } finally {
            super.doAction(controller, player);
        }
    }

    private void setTargetToNearestShip(GameController controller, Player player) {
        Space space = null;
        int i = 1;
        while (!(space instanceof Ship)) {
            space = controller.getGame().getSpaces().get((player.getCurrentPosition().getIndex() + i) % 40);
            i++;
        }
        targetIndex = space.getIndex();
    }
}
