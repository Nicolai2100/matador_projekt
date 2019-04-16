package game.Test;

import game.controller.GameController;
import game.model.Game;
import game.view.View;
import gui_main.GUI;
import org.junit.Before;
import org.junit.Test;

public class ViewTest {
    Game game;
    GUI gui;
    View view;
    @Before
    public void ini(){
         game = new Game();
        gui = new GUI();
         view = new View(game, gui);


    }
    @Test
    public void colorsFromStreets() {

         }
}