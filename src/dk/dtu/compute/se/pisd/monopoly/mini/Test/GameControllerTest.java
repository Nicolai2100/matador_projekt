package dk.dtu.compute.se.pisd.monopoly.mini.Test;

import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Property;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Space;
import dk.dtu.compute.se.pisd.monopoly.mini.model.properties.RealEstate;
import org.junit.Before;

import java.util.List;

import static org.junit.Assert.*;

public class GameControllerTest {
    Game game;

    @Before
    public void ini(){

        game = new Game();


    }
    @org.junit.Test
    public void offerToBuild() {

        Property property = new Property();
        List<Space> spaces = game.getSpaces();

        RealEstate realEstate = (RealEstate) spaces.get(3);;
    }
}