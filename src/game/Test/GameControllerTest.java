package game.Test;

import game.model.Game;
import game.model.Property;
import game.model.Space;
import game.model.properties.RealEstate;
import org.junit.Before;

import java.util.List;

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