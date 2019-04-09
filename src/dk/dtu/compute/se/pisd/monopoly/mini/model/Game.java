package dk.dtu.compute.se.pisd.monopoly.mini.model;

import dk.dtu.compute.se.pisd.designpatterns.Subject;
import dk.dtu.compute.se.pisd.monopoly.mini.model.cards.CardMove;
import dk.dtu.compute.se.pisd.monopoly.mini.model.cards.CardReceiveMoneyFromBank;
import dk.dtu.compute.se.pisd.monopoly.mini.model.cards.PayTax;
import dk.dtu.compute.se.pisd.monopoly.mini.model.properties.RealEstate;
import dk.dtu.compute.se.pisd.monopoly.mini.model.properties.Utility;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the top-level element of a Monopoly game's state. In order
 * to use this model with the MVC-pattern, it extends the
 * {@link dk.dtu.compute.se.pisd.designpatterns.Subject} of the observer
 * design pattern.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Game extends Subject {

    private List<Space> spaces = new ArrayList<Space>();

    private List<Card> cardDeck = new ArrayList<Card>();

    private List<Player> players = new ArrayList<Player>();

    private Player current;

    private int gameId = -1;

    public Game() {
        createGame();
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    /**
     * Returns a list of all the games spaces.
     *
     * @return an unmodifiable list of the games spaces
     */
    public List<Space> getSpaces() {
        return Collections.unmodifiableList(spaces);
    }

    /**
     * Sets all the spaces of the game. Note that the provided
     * list of spaces is copied, so that they cannot be changed
     * without the game being aware of the change.
     *
     * @param spaces the list of spaces
     */
    public void setSpaces(List<Space> spaces) {
        this.spaces = new ArrayList<Space>(spaces);
        notifyChange();
    }

    /**
     * Adds a space to the game at the end.
     *
     * @param space the added space
     */
    public void addSpace(Space space) {
        space.setIndex(spaces.size());
        spaces.add(space);
        notifyChange();
    }

    /**
     * Returns a list of the cards in the current deck.
     *
     * @return an unmodifiable list of all the cards currently in the deck
     */
    public List<Card> getCardDeck() {
        return Collections.unmodifiableList(cardDeck);
    }

    /**
     * Removes the topmost card from the deck and returns it.
     *
     * @return the topmost card of the deck
     */
    public Card drawCardFromDeck() {
        // TODO should be more defensive
        Card card = cardDeck.remove(0);
        notifyChange();
        return card;
    }

    /**
     * Add the given card to the bottom of the deck.
     *
     * @param card the card added to the bottom of the deck.
     */
    public void returnCardToDeck(Card card) {
        cardDeck.add(card);
        notifyChange();
    }

    /**
     * Uses the provided list of cards as the new deck. The
     * list will be copied in order to avoid, changes on it
     * without the game being aware of it.
     *
     * @param cardDeck the new deck of cards
     */
    public void setCardDeck(List<Card> cardDeck) {
        this.cardDeck = new ArrayList<Card>(cardDeck);
        notifyChange();
    }


    /**
     * Shuffles the cards in the deck.
     */
    public void shuffleCardDeck() {
        Collections.shuffle(cardDeck);
        // This notification is probably not needed, but for
        // completeness sake, we have it here
        notifyChange();
    }

    /**
     * Returns all the players of the game as an unmodifiable list.
     *
     * @return a list of the current players
     */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    /**
     * Sets the list of players. The list of players is actually copied
     * in order to avoid the list being modified without the game being
     * aware of it.
     *
     * @param players the list of players
     */
    public void setPlayers(List<Player> players) {
        this.players = new ArrayList<Player>(players);
        notifyChange();
    }

    /**
     * Adds a player to the game.
     *
     * @param player the player to be added.
     */
    public void addPlayer(Player player) {
        players.add(player);
        notifyChange();
    }

    /**
     * Returns the current player of the game. This is the player
     * who's turn it is to do the next move (or currently is doing a move).
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        if (current == null) {
            current = players.get(0);
        }
        return current;
    }

    /**
     * Sets the current player. It is required that the player is a
     * player of the game already; otherwise an IllegalArumentException
     * will be thrown.
     *
     * @param player the new current player
     */
    public void setCurrentPlayer(Player player) {
        if (player != null && players.contains(player)) {
            current = player;
        } else {
            throw new IllegalArgumentException("Player is not in the game!");
        }
        notifyChange();
    }

    /**
     * Returns a list of all players currently active in the game.
     * @param prisonIgnored if true, it returns only players that are not broke.
     *                      If false, it returns players that are not broke or in prison.
     * @return
     * @Author Nicolai Wulff, s185036
     */
	public List<Player> getActivePlayers(Boolean prisonIgnored) {
		List<Player> activePlayers = new ArrayList<>();
		for (Player player : players) {
			if (!prisonIgnored) {
				if (!player.isBroke() && !player.isInPrison()) activePlayers.add(player);
			} else {
				if (!player.isBroke()) activePlayers.add(player);
			}
		}
		return Collections.unmodifiableList(players);
	}


	/**
	 * Creates the initial static situation of a Monopoly game. Note
	 * that the players are not created here, and the chance cards
	 * are not shuffled here.
	 *
	 * @return the initial game board and (not shuffled) deck of chance cards
	 */

    public void createGame() {

        // Create the initial Game set up (note that, in this simple
        // setup, we use only 11 spaces). Note also that this setup
        // could actually be loaded from a file or database instead
        // of creating it programmatically. This will be discussed
        // later in this course.

        Space go = new Space();
        go.setName("Go");
        addSpace(go);

        Property p = new RealEstate();
        p.setName("Rødovrevej");
        p.setCost(1200);
        p.setRent(50);
        p.setColorGroup(ColorGroup.lightblue);
        ((RealEstate) p).setPriceForHouse(50);
        addSpace(p);

		/*Browns\Purples and Light Blues- £\$50
Pinks and Oranges- £\$100
Reds and Yellows- £\$150
Greens and Dark blues- £\$200
A hotel costs the same as a house but 4 houses are needed to build a hotel.*/

        Chance chance = new Chance();
        chance.setName("Chance");
        addSpace(chance);

        p = new RealEstate();
        p.setName("Hvidovrevej");
        p.setCost(1200);
        p.setRent(50);
        p.setColorGroup(ColorGroup.lightblue);
        ((RealEstate) p).setPriceForHouse(50);
        addSpace(p);

        Tax t = new Tax();
        t.setName("Pay tax (10% on Cash)");
        addSpace(t);

        Property s = new Utility();
        s.setName("Øresund");
        s.setCost(4000);
        s.setRent(500);
        s.setColorGroup(ColorGroup.navy);
        addSpace(s);

        p = new RealEstate();
        p.setName("Roskildevej");
        p.setCost(2000);
        p.setRent(100);
        p.setColorGroup(ColorGroup.pink);

        ((RealEstate) p).setPriceForHouse(50);
        addSpace(p);

        chance = new Chance();
        chance.setName("Chance");
        addSpace(chance);

        p = new RealEstate();
        p.setName("Valby Langgade");
        p.setCost(2000);
        p.setRent(100);
        ((RealEstate) p).setPriceForHouse(50);
        p.setColorGroup(ColorGroup.pink);
        addSpace(p);

        p = new RealEstate();
        p.setName("Allégade");
        p.setCost(2400);
        p.setRent(150);
        ((RealEstate) p).setPriceForHouse(50);
        p.setColorGroup(ColorGroup.pink);
        addSpace(p);

        Space prison = new Space();
        prison.setName("Prison");
        addSpace(prison);

        p = new RealEstate();
        p.setName("Frederiksberg Allé");
        p.setCost(2800);
        p.setRent(200);
        ((RealEstate) p).setPriceForHouse(50);
        p.setColorGroup(ColorGroup.green);

        addSpace(p);

        p = new Utility();
        p.setName("Coca-Cola Tapperi");
        p.setCost(3000);
        p.setRent(300);
        p.setColorGroup(ColorGroup.darkgreen);
        addSpace(p);

        p = new RealEstate();
        p.setName("Bülowsvej");
        p.setCost(2800);
        p.setRent(200);
        p.setColorGroup(ColorGroup.green);

        ((RealEstate) p).setPriceForHouse(50);

        addSpace(p);

        p = new RealEstate();
        p.setName("Gl. Kongevej");
        p.setCost(3200);
        p.setRent(250);
        ((RealEstate) p).setPriceForHouse(50);
        p.setColorGroup(ColorGroup.green);

        addSpace(p);

        List<Card> cards = new ArrayList<Card>();

        CardMove move = new CardMove();
        move.setTarget(getSpaces().get(9));
        move.setText("Move to Allégade!");
        cards.add(move);

        PayTax tax = new PayTax();
        tax.setText("Pay 10% income tax!");
        cards.add(tax);

        CardReceiveMoneyFromBank b = new CardReceiveMoneyFromBank();
        b.setText("You receive 100$ from the bank.");
        b.setAmount(100);
        cards.add(b);
        setCardDeck(cards);
    }

    /**
     * This method will be called before the game is started to create
     * the participating players.
     */
    public void createPlayers(int numOfPlayers) {
        for (int i = 0; i < numOfPlayers; i++) {
            Player player = new Player();
            player.setName("player " + i+1);
            player.setCurrentPosition(getSpaces().get(0));
            addPlayer(player);

        }

   /*
        // TODO the players should eventually be created interactively or
        // be loaded from a database
        Player p = new Player();
        p.setName("Player 1");
        p.setCurrentPosition(getSpaces().get(0));
        p.setColor(Color.RED);
        addPlayer(p);

        p = new Player();
        p.setName("Player 2");
        p.setCurrentPosition(getSpaces().get(0));
        p.setColor(Color.YELLOW);
        addPlayer(p);

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
}
