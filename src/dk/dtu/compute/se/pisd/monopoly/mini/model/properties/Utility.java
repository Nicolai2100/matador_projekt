package dk.dtu.compute.se.pisd.monopoly.mini.model.properties;

import dk.dtu.compute.se.pisd.monopoly.mini.model.Property;

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
