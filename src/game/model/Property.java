package game.model;

import game.controller.GameController;
import game.model.exceptions.GameEndedException;
import game.model.exceptions.PlayerBrokeException;

/**
 * A property which is a space that can be owned by a player.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Property extends Space {

    private int cost;
    private int rent;
    private int[] rentLevels;
    private boolean superOwned = false;
    private boolean mortgaged = false;
    private Player owner;
    private ColorGroup colorGroup;

    public Property() {
    }

    public void computeRent() {

    }

    public void setColorGroup(ColorGroup colorGroup) {
        this.colorGroup = colorGroup;
    }

    public ColorGroup getColorGroup() {
        return colorGroup;
    }

    public boolean getSuperOwned() {
        return this.superOwned;
    }

    public void setSuperOwned(boolean bool) {
        this.superOwned = bool;
        this.computeRent();
    }

    public void setMortgaged(boolean bool) {
        this.mortgaged = bool;
        notifyChange();
    }

    public boolean getMortgaged() {
        return mortgaged;
    }

    /**
     * Returns the cost of this property.
     *
     * @return the cost of this property
     */
    public int getCost() {
        return cost;
    }

    /**
     * Sets the cost of this property.
     *
     * @param cost the new cost of this property
     */
    public void setCost(int cost) {
        this.cost = cost;
        notifyChange();
    }

    /**
     * Returns the rent to be payed for this property.
     *
     * @return the rent for this property
     */
    public int getRent() {
        return rent;
    }

    /**
     * Sets the rent for this property.
     *
     * @param rent the new rent for this property
     */
    public void setRent(int rent) {
        this.rent = rent;
        notifyChange();
    }

    /**
     * Returns the owner of this property. The value is <code>null</code>,
     * if the property currently does not have an owner.
     *
     * @return the owner of the property or <code>null</code>
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Sets the owner of this property  to the given owner (which can be
     * <code>null</code>).
     *
     * @param player the new owner of the property
     */
    public void setOwner(Player player) {
        this.owner = player;
        notifyChange();
    }

    public void setRentLevels(int[] rentLevels) {
        this.rentLevels = rentLevels;
    }

    public int[] getRentLevels() {
        return rentLevels;
    }

    @Override
    public void doAction(GameController controller, Player player) throws PlayerBrokeException, GameEndedException {
        if (owner == null) {
            controller.offerToBuy(this, player);

        } else if (!owner.equals(player) && !mortgaged && !owner.isInPrison()) {
            controller.payment(player, rent, owner);
        } else if (!owner.equals(player) && owner.isInPrison()) {
            controller.showMessage(player + "skal ikke betale leje til " + owner.getName() + ", fordi " + owner.getName() + " er i fængsel!");
        } else if (!owner.equals(player) && mortgaged) {
            controller.showMessage(this.getName() + " er pantsat, og " + player.getName() + " skal derfor ikke betale leje.");
        }
    }
}
