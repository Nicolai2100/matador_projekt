package game.model.properties;

import game.controller.GameController;
import game.model.Player;
import game.model.exceptions.GameEndedException;
import game.model.exceptions.PlayerBrokeException;

public class Ship extends Utility {

    @Override
    public void setRentLevels(int[] rentLevels) {
        if (rentLevels.length != 4) throw new IllegalArgumentException();
        super.setRentLevels(rentLevels);
    }

    public void setCurrentRentLevel(int i) {
        if (i < 0 || i > 3) throw new IllegalArgumentException();
        this.setRent(this.getRentLevels()[i]);
        notifyChange();
    }

    @Override
    public void doAction(GameController controller, Player player) throws PlayerBrokeException, GameEndedException {
        controller.playSound("ship.wav");
        super.doAction(controller, player);
    }
}
