package game.model.properties;

import game.model.Property;

/**
 * A specific property, which represents real estate on which houses
 * and hotels can be built. Note that this class does not have details
 * yet and needs to be implemented.
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class RealEstate extends Property{
	private int houseCount = 0;
	private int priceForHouse = 0;

	public RealEstate(){
    }

    @Override
    public void computeRent(){
        //calculerer den nye leje
        if (this.getSuperOwned() && houseCount == 0) {
            this.setRent(this.getRentLevels()[0] * 2);
        } else if (houseCount > 0 && houseCount <= 5) {
            this.setRent(this.getRentLevels()[houseCount]);
        } else {
            this.setRent(this.getRentLevels()[0]);
        }
        notifyChange();
    }

    public int getPriceForHouse() {
        return priceForHouse;
    }

    public void setPriceForHouse(int priceForHouse) {
        this.priceForHouse = priceForHouse;
    }

    public int getHouseCount() {
        return houseCount;
    }

    public void setHouseCount(int houseCount) {
        this.houseCount = houseCount;
        computeRent();
        notifyChange();
    }

    @Override
    public void setRentLevels(int[] rentLevels) {
        if (rentLevels.length != 6) throw new IllegalArgumentException();
        super.setRentLevels(rentLevels);
    }
}
