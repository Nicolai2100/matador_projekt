package game.Test;

import game.controller.GameController;
import game.model.Game;
import game.model.Player;
import game.view.TokenColor;
import game.view.View;
import gui_fields.GUI_Car;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_main.GUI;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Scanner;

public class ViewTest {
    Game game;
    GameStub gameStub;
    GUI gui;
    View view;
    ViewStub viewStub;
    PlayerTest playerTest;

    @Before
    public void ini() {
        gui = new GUI();
        game = new Game();
        gameStub = new GameStub();
        view = new View(game, gui);
        viewStub = new ViewStub(game, gui);
        playerTest = new PlayerTest();

        game.setSpaces(gameStub.getSpaces());
        game.setPlayers(gameStub.getPlayers());

    }

    @Test
    public void calcNumOfMoves() {


        Player player1 = game.getPlayers().get(0);
        player1.setCurrentPosition(game.getSpaces().get(0));

        GUI_Field[] guiFields = gui.getFields();
        Integer oldPosition = viewStub.player2position.get(player1);

        GUI_Player gp = viewStub.getPlayer2GuiPlayer().get(player1);

        if (oldPosition != null && oldPosition < guiFields.length) {
            guiFields[oldPosition].setCar(gp, false);
            System.out.println(mover(player1));
        }


        int pos = player1.getCurrentPosition().getIndex();
        if (pos < guiFields.length) {
            viewStub.player2position.put(player1, pos);
            guiFields[pos].setCar(gp, true);
        }
    }

    public int mover(Player player) {

        return view.calcNumOfMoves(player);


/*
        Integer oldPos = viewStub.getPlayer2position().get(player1);

        if (oldPos == null) {
            oldPos = 0;
        }
        int curPos = player1.getCurrentPosition().getIndex();
        int moves;
        if (curPos > oldPos) {
            moves = curPos - oldPos;
        } else {
            try {
                moves = 40 % (oldPos - curPos);
            } catch (ArithmeticException e) {
                moves = 0;
            }
        }
        return moves;*/
    }


}