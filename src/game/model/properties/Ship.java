package game.model.properties;

import game.model.Property;

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
}
