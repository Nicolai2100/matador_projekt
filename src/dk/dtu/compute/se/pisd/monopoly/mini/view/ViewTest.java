package dk.dtu.compute.se.pisd.monopoly.mini.view;

import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;
import gui_main.GUI;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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

        view.colorsFromStreets();
         }
}