package dk.dtu.compute.se.pisd.monopoly.mini;

import dk.dtu.compute.se.pisd.JSON.JSONUtility;
import dk.dtu.compute.se.pisd.monopoly.mini.controller.GameController;
import dk.dtu.compute.se.pisd.monopoly.mini.model.*;
import dk.dtu.compute.se.pisd.monopoly.mini.model.cards.CardMove;
import dk.dtu.compute.se.pisd.monopoly.mini.model.cards.CardReceiveMoneyFromBank;
import dk.dtu.compute.se.pisd.monopoly.mini.model.cards.PayTax;
import dk.dtu.compute.se.pisd.monopoly.mini.model.properties.RealEstate;
import dk.dtu.compute.se.pisd.monopoly.mini.model.properties.Utility;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class for setting up and running a (Mini-)Monoploy game.
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class MiniMonopoly {
	
	/**
	 * This method will be called before the game is started to create
	 * the participating players.
	 */
	public static void createPlayers(Game game) {
		// TODO the players should eventually be created interactively or
		// be loaded from a database
		Player p = new Player();
		p.setName("Player 1");
		p.setCurrentPosition(game.getSpaces().get(0));
		p.setColor(Color.RED);
		game.addPlayer(p);

		p = new Player();
		p.setName("Player 2");
		p.setCurrentPosition(game.getSpaces().get(0));
		p.setColor(Color.YELLOW);
		game.addPlayer(p);
/*
		p = new Player();
		p.setName("Player 3");
		p.setCurrentPosition(game.getSpaces().get(0));
		p.setColor(Color.GREEN);
		game.addPlayer(p);

		p = new Player();
		p.setName("Player 4");
		p.setCurrentPosition(game.getSpaces().get(0));
		p.setColor(Color.BLACK);
		game.addPlayer(p);

		p = new Player();
		p.setName("Player 5");
		p.setCurrentPosition(game.getSpaces().get(0));
		p.setColor(Color.BLUE);
		game.addPlayer(p);

		p = new Player();
		p.setName("Player 6");
		p.setCurrentPosition(game.getSpaces().get(0));
		p.setColor(Color.cyan);
		game.addPlayer(p);*/
	}

	/**
	 * The main method which creates a game, shuffles the chance
	 * cards, creates players, and then starts the game. Note
	 * that, eventually, the game could be loaded from a database.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		Game game = JSONUtility.createGame();
		game.shuffleCardDeck();

		createPlayers(game);

		GameController controller = new GameController(game);
		controller.initializeGUI();

		controller.play();
	}

}
