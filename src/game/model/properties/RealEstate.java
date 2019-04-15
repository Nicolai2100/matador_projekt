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
        //this.setRent(this.getRent() + houseCount * priceForHouse);
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

    /*Udvid klassen for grund (RealEstate) med private attributer
    og getter- og setter-metoder som tilgår og ændrer antal byggede huse
    på en grund. Sørg for at metoden notifyChange() bliver kaldt, så snart der
    er en relevant ændring i grundens attributer.

    Udvid ejendom (Property) og grund (RealEstate) med private
    attributer og getter- og setter-metoder som tilgår og ændrer

     pris for
    at bygge et hus, basisleje og husleje; og implementer en metode som bereggner
     den aktuelle leje ud fra basis og husleje og den aktuelle situation (antal huse).
     Metoden til at beregne lejen kunne navngives computeRent().

*/

}
