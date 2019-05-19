package test;

import game.controller.GameController;
import game.model.Game;
import game.model.Player;
import game.model.properties.Brewery;
import json.JSONUtility;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class can be used to try out different test games, that
 * have specific predetermined outcomes of the dice rolls. Note that as
 * of now, these tests only sets up and run the games, thus you as the
 * user will still need to enter input in the GUI, and since the play()
 * method of the controller runs until the game terminates, we cannot
 * make any assertions in the tests that will evaluate as passed or not.
 * This could definitely be improved in the future. One solution would be
 * to create a GUI-interface and a "mock"-GUI class, which could return
 * predetermined values each time the GUI is called for input. With this,
 * the tests could run automatically and make assertions.
 *
 * Currently, all test just need you to click "Kør" in the GUI each round.
 *
 * @author Nicolai d T. Wulff,	s185036@student.dtu.dk
 */
class GameControllerTest {

    /**
     * Method to setuo a test game with a given number of players and
     * predetermined dice values.
     * @param numOfPlayers int that sets number of players.
     * @param diceValues array of integers that determines future dice values.
     * @return Controller-object, setup with the test game.
     */
    private GameController setupTestGame(int numOfPlayers, int[] diceValues) {
        JSONUtility ju = new JSONUtility();
        Game game = ju.createGame();
        game.createPlayers(numOfPlayers);

        int i = 1;
        for (Player p : game.getPlayers()) {
            p.setName("Spiller " + i);
            p.setColorEnumType(Player.PlayerColor.values()[i - 1]);
            p.setCarType(Player.CarType.CAR);
            i++;
        }

        MockDiceCup diceCup = new MockDiceCup(diceValues);
        GameController controller = new GameController();
        controller.setGame(game);
        controller.setDiceCup(diceCup);
        return controller;
    }

    /**
     * This test can be used to try a game, where the starting player
     * will begin by rolling three doubles in a row, which will result
     * in the player being put in jail.
     * When you run the test, simply click "Kør" each round, until the
     * player is put in jail. Thereafter, the dice will act as normal.
     * @author Nicolai d T. Wulff,	s185036@student.dtu.dk
     */
    @Test
    void threeDoublesInARowTest() {
        int[] diceValues = {3, 3, 4, 4, 5, 5};
        GameController controller = setupTestGame(3, diceValues);
        controller.play();
    }

    /**
     * This test can be used to start a game that ensures that many of the first
     * moves will result in a player landing on a Chance space.
     * When you run the test, simply click "Kør" each round, and the players
     * will land on "Chance" possibly up to 18 times, before the dice will act as normal.
     * @author Nicolai d T. Wulff,	s185036@student.dtu.dk
     */
    @Test
    void landOnChance() {
        int[] diceValues = {3, 4, 3, 4, 3, 4, 3, 4, 3, 4, 3, 4, 6, 4, 6, 4, 6, 4, 6, 4, 6, 4, 6, 4, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3};
        GameController controller = setupTestGame(6, diceValues);
        controller.play();
    }

    /**
     * Tests when a player lands on a brewery, that is owned
     * by another player, who owns 1 brewery.
     * Click "Kør" when prompted.
     * @author Nicolai d T. Wulff,	s185036@student.dtu.dk
     */
    @Test
    void landOnOwnedBrewery() {
        int[] diceValues = {6, 6};
        GameController controller = setupTestGame(3, diceValues);
        Game game = controller.getGame();
        Player p2 = controller.getGame().getPlayers().get(1);
        Brewery brewery = (Brewery) game.getSpaces().get(12);
        brewery.setOwner(p2);
        p2.addOwnedProperty(brewery);
        controller.play();
    }

    /**
     * Tests when a player lands on a brewery, that is owned
     * by another player, who owns 2 breweries.
     * Click "Kør" when prompted.
     * @author Nicolai d T. Wulff,	s185036@student.dtu.dk
     */
    @Test
    void landOnOwnedBrewery2() {
        int[] diceValues = {6, 6};
        GameController controller = setupTestGame(3, diceValues);
        Game game = controller.getGame();
        Player p2 = controller.getGame().getPlayers().get(1);
        Brewery brewery1 = (Brewery) game.getSpaces().get(12);
        Brewery brewery2 = (Brewery) game.getSpaces().get(28);
        brewery1.setOwner(p2);
        p2.addOwnedProperty(brewery1);
        brewery2.setOwner(p2);
        p2.addOwnedProperty(brewery2);
        controller.play();
    }
}