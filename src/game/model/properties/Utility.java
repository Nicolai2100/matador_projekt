package game.model.properties;

import game.model.Property;

/**
 * A specific property, which represents a utility which can
 * not be developed with houses or hotels.
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Utility extends Property {
	
	// TODO to be implemented
    //Husk færgen har 3 super owned til stande!

    public Utility(){
    }

    public void computeRent(){
        //calculerer den nye leje
        this.setRent(this.getRent());
        notifyChange();
    }

    public void setHouseCount(int houseCount) {
        computeRent();
        notifyChange();
    }

}
