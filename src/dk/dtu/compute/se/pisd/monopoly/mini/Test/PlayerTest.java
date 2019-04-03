package dk.dtu.compute.se.pisd.monopoly.mini.Test;

import dk.dtu.compute.se.pisd.monopoly.mini.model.ColorGroup;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Player;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Property;
import dk.dtu.compute.se.pisd.monopoly.mini.model.properties.RealEstate;
import dk.dtu.compute.se.pisd.monopoly.mini.model.properties.Utility;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class PlayerTest {
    Player player;
    Game game;
    Property p1;
    Property p2;
    Property utility1;
    Set<Property> properties;

    @Before
    public void initialize() {
        game = new Game();
        player = new Player();
        player.setName("Player 1");
        player.setColor(Color.RED);
        game.addPlayer(player);
        properties = new HashSet<>();


        p1 = new RealEstate();
        p1.setName("Rødovrevej");
        p1.setCost(1200);
        p1.setRent(50);
        p1.setColorGroup(ColorGroup.lightblue);
        ((RealEstate) p1).setPriceForHouse(50);
        game.addSpace(p1);
        properties.add(p1);


        p2 = new RealEstate();
        p2.setName("Hvidovrevej");
        p2.setCost(1200);
        p2.setRent(50);
        p2.setColorGroup(ColorGroup.lightblue);
        ((RealEstate) p2).setPriceForHouse(50);
        game.addSpace(p2);

        utility1 = new Utility();
        utility1.setName("Øresund");
        utility1.setCost(4000);
        utility1.setRent(500);
        utility1.setColorGroup(ColorGroup.navy);
        game.addSpace(utility1);


    }

    @Test
    public void checkIsSuperOwned() {

        //Virker metoden for almindelige RealEstate objekter?

        player.setOwnedProperties(properties);
        System.out.println( player.getOwnedProperties().size());
        for (Property prop : player.getOwnedProperties()) {
            assertTrue(!prop.getSuperOwned());
        }
        player.addOwnedProperty(p2);
        for (Property prop : player.getOwnedProperties()) {
            assertTrue(prop.getSuperOwned());
        }

        //Virker metoden for Utility objekter?

        player.addOwnedProperty(utility1);
        assertTrue(!utility1.getSuperOwned());

    }

    @Test
    public void setAllPropertiesSuperOwnedFalse() {

        Property utility1 = new Utility();
        utility1.setName("Øresund");
        utility1.setCost(4000);
        utility1.setRent(500);
        utility1.setColorGroup(ColorGroup.navy);
        game.addSpace(utility1);

        System.out.println(       utility1.getSuperOwned());
    }
}