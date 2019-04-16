package game.model.properties;

import game.controller.GameController;
import game.model.Player;
import game.model.exceptions.GameEndedException;
import game.model.exceptions.PlayerBrokeException;

public class Brewery extends Utility {

    @Override
    public void setRentLevels(int[] rentLevels) {
        if (rentLevels.length != 2) throw new IllegalArgumentException();
        super.setRentLevels(rentLevels);
    }

    public void setCurrentRentLevel(int i) {
        if (i < 0 || i > 1) throw new IllegalArgumentException();
        this.setRent(this.getRentLevels()[i]);
    }

    @Override
    public void doAction(GameController controller, Player player) throws PlayerBrokeException, GameEndedException {
        if (this.getOwner() == null) {
            controller.offerToBuy(this, player);
        } else if (!this.getOwner().equals(player) && !this.getMortgaged() && !this.getOwner().isInPrison()) {
            controller.payment(player, this.getRent() * controller.getSumOfDies(), this.getOwner());
        } else if (!this.getOwner().equals(player) && this.getOwner().isInPrison()) {
            controller.showMessage(player + "skal ikke betale leje til " + this.getOwner().getName() + ", fordi " + this.getOwner().getName() + " er i fængsel!");
        } else if (!this.getOwner().equals(player) && this.getMortgaged()) {
            controller.showMessage(this.getName() + " er pantsat, og " + player.getName() + " skal derfor ikke betale leje.");
        }
    }
}
