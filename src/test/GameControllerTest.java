package test;

import game.controller.GameController;
import game.model.Card;
import game.model.Game;
import game.model.Player;
import game.model.Property;
import game.model.cards.EconomicEffect;
import game.model.cards.GetOutOfJail;
import game.model.cards.MoveEffect;
import game.model.properties.Brewery;
import game.model.properties.RealEstate;
import json.JSONUtility;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
        controller.play(true);
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
        controller.play(true);
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
        controller.play(true);
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
        controller.play(true);
    }

    @Test
    void setupDemoGame() {
        JSONUtility ju = new JSONUtility();
        Game game = ju.createGame();
        game.createPlayers(3);
        List<Player> players = game.getPlayers();

        Player p = players.get(0);
        p.setName("Nicolai");
        p.setColorEnumType(Player.PlayerColor.RED);
        p.setCarType(Player.CarType.CAR);
        Property prop = (Property) game.getSpaces().get(1);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        ((RealEstate) prop).setHouseCount(5);
        prop = (Property) game.getSpaces().get(3);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        ((RealEstate) prop).setHouseCount(5);
        prop = (Property) game.getSpaces().get(16);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        ((RealEstate) prop).setHouseCount(4);
        prop = (Property) game.getSpaces().get(18);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        ((RealEstate) prop).setHouseCount(4);
        prop = (Property) game.getSpaces().get(19);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        ((RealEstate) prop).setHouseCount(3);
        prop = (Property) game.getSpaces().get(26);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        prop = (Property) game.getSpaces().get(27);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        prop = (Property) game.getSpaces().get(29);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        //prop = (Property) game.getSpaces().get(23);
        //p.addOwnedProperty(prop);
        //prop.setOwner(p);
        prop = (Property) game.getSpaces().get(31);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        prop = (Property) game.getSpaces().get(34);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        prop = (Property) game.getSpaces().get(25);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        prop = (Property) game.getSpaces().get(12);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        p.setBalance(14600);

        p = players.get(1);
        p.setName("Neal");
        p.setColorEnumType(Player.PlayerColor.GREEN);
        p.setCarType(Player.CarType.RACECAR);
        p.setCurrentPosition(game.getSpaces().get(12));
        p.setBalance(12750);
        prop = (Property) game.getSpaces().get(37);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        ((RealEstate) prop).setHouseCount(4);
        prop = (Property) game.getSpaces().get(39);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        ((RealEstate) prop).setHouseCount(5);
        prop = (Property) game.getSpaces().get(6);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        prop = (Property) game.getSpaces().get(8);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        prop = (Property) game.getSpaces().get(9);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        prop = (Property) game.getSpaces().get(11);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        prop = (Property) game.getSpaces().get(13);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        prop = (Property) game.getSpaces().get(14);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        //prop = (Property) game.getSpaces().get(21);
        //p.addOwnedProperty(prop);
        //prop.setOwner(p);
        //prop = (Property) game.getSpaces().get(24);
        //p.addOwnedProperty(prop);
        //prop.setOwner(p);
        prop = (Property) game.getSpaces().get(32);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        prop = (Property) game.getSpaces().get(28);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        prop = (Property) game.getSpaces().get(15);
        p.addOwnedProperty(prop);
        prop.setOwner(p);




        p = players.get(2);
        p.setName("Malte");
        p.setColorEnumType(Player.PlayerColor.YELLOW);
        p.setCarType(Player.CarType.UFO);
        p.setBalance(5000);
        p.setCurrentPosition(game.getSpaces().get(32));
        prop = (Property) game.getSpaces().get(21);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        ((RealEstate) prop).setHouseCount(2);
        prop = (Property) game.getSpaces().get(23);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        ((RealEstate) prop).setHouseCount(2);
        prop = (Property) game.getSpaces().get(24);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        ((RealEstate) prop).setHouseCount(2);
        prop = (Property) game.getSpaces().get(5);
        p.addOwnedProperty(prop);
        prop.setOwner(p);
        prop = (Property) game.getSpaces().get(35);
        p.addOwnedProperty(prop);
        prop.setOwner(p);

        Card card1 = new GetOutOfJail();
        card1.setText("I anledningen af kongens fødselsdag benådes De herved for fængsel. Dette kort kan opbevares, indtil De får brug for det, eller De kan sælge det.");
        ArrayList<Card> ownedCards = new ArrayList<>();
        ownedCards.add(card1);
        p.setOwnedCards(ownedCards);
        //p.setBroke(true);

        int[] diceValues = {3, 4};
        MockDiceCup diceCup = new MockDiceCup(diceValues);

        Card card2 = new MoveEffect(MoveEffect.TargetTypes.THREE_FORWARDS);
        card2.setText("Ryk tre felter frem.");

        game.shuffleCardDeck();
        List<Card> deck = new ArrayList<>(game.getCardDeck());
        deck.add(0, card2);
        game.setCardDeck(deck);

        GameController controller = new GameController();
        controller.setDiceCup(diceCup);
        controller.setGame(game);
        game.setCurrentPlayer(players.get(2));
        controller.play(false);
    }

    @Test
    void demoGame() {
        JSONUtility ju = new JSONUtility();
        Game game = ju.createGame();
        game.createPlayers(3);
        List<Player> players = game.getPlayers();

        Player p = players.get(0);
        p.setName("Nicolai W");
        p.setCarType(Player.CarType.CAR);
        p.setColorEnumType(Player.PlayerColor.GREY);
        p = players.get(1);
        p.setName("Neal");
        p.setCarType(Player.CarType.RACECAR);
        p.setColorEnumType(Player.PlayerColor.BlUE);
        p = players.get(2);
        p.setName("Malte");
        p.setColorEnumType(Player.PlayerColor.YELLOW);
        p.setCarType(Player.CarType.TRACTOR);

        List<Card> deck = new ArrayList<>(game.getCardDeck());
        game.shuffleCardDeck();

        Card card = new MoveEffect(MoveEffect.TargetTypes.THREE_FORWARDS);
        card.setText("Ryk tre felter frem.");
        deck.add(0, card);

        card = new EconomicEffect(EconomicEffect.EffectType.FROM_OTHER_PLAYERS);
        card.setText("De har lagt penge ud til et sammenskudsgilde. Mærkværdigvis betaler alle straks. Modtag fra hver medspiller kr. 500.");
        ((EconomicEffect) card).setAmount1(500);
        deck.add(0, card);

        card = new MoveEffect(MoveEffect.TargetTypes.NEAREST_SHIP_2);
        card.setText("Tag med den nærmeste færge. Flyt brikken frem, og hvis De passerer \"START\", indkassér da kr. 4000.");
        deck.add(0, card);

        card = new GetOutOfJail();
        card.setText("I anledningen af kongens fødselsdag benådes De herved for fængsel. Dette kort kan opbevares, indtil De får brug for det, eller De kan sælge det.");
        deck.add(0, card);

        game.setCardDeck(deck);

        //int[] diceValues = {3, 5, 1, 1, 3, 3, 6, 6, 3, 4, 1, 3, 6, 3, 3, 4};
        int[] diceValues = {3, 5, 1, 1, 3, 3, 6, 6, 3, 4, 1, 3, 6, 3, 6, 1, 3, 4};
        MockDiceCup diceCup = new MockDiceCup(diceValues);
        GameController controller = new GameController();
        controller.setDiceCup(diceCup);
        controller.playOrLoadGame(game);
    }
}