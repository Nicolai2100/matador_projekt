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
        go.setName("Start");
        addSpace(go);

        Space roedovrevej = new RealEstate();
        roedovrevej.setName("Rødovrevej");
        ((RealEstate) roedovrevej).setCost(1200);
        ((RealEstate) roedovrevej).setRent(50);
        ((RealEstate) roedovrevej).setColorGroup(ColorGroup.lightblue);
        ((RealEstate) roedovrevej).setPriceForHouse(1000);
        addSpace(roedovrevej);

        Space chance = new Chance(); //colorgroup?
        chance.setName("chance");
        addSpace(chance);

        Space hvidovrevej = new RealEstate();
        hvidovrevej.setName("Hvidovrevej");
        ((RealEstate) hvidovrevej).setCost(1200);
        ((RealEstate) hvidovrevej).setRent(50);
        ((RealEstate) hvidovrevej).setColorGroup(ColorGroup.lightblue);
        ((RealEstate) hvidovrevej).setPriceForHouse(1000);
        addSpace(hvidovrevej);

        Space indkomstskat = new Tax();
        indkomstskat.setName("Indkomstskat");
        addSpace(indkomstskat);

        Space oeresund = new Utility();
        //TODO ændr alle utilities til enten Ship eller Brewery (som kan have begyndelsespris sat fra start)
        oeresund.setName("Øresund");
        ((Utility) oeresund).setCost(4000);
        ((Utility) oeresund).setRent(500);
        ((Utility) oeresund).setColorGroup(ColorGroup.navy);
        addSpace(oeresund);

        Space roskildevej = new RealEstate();
        roskildevej.setName("Roskildevej");
        ((RealEstate) roskildevej).setCost(2000);
        ((RealEstate) roskildevej).setRent(100);
        ((RealEstate) roskildevej).setColorGroup(ColorGroup.pink);
        ((RealEstate) roskildevej).setPriceForHouse(1000);
        addSpace(roskildevej);

        chance = new Chance(); //colorgroup?
        chance.setName("chance");
        addSpace(chance);

        Space valbyLanggade = new RealEstate();
        valbyLanggade.setName("Valby Langgade");
        ((RealEstate) valbyLanggade).setCost(2000);
        ((RealEstate) valbyLanggade).setRent(100);
        ((RealEstate) valbyLanggade).setColorGroup(ColorGroup.pink);
        ((RealEstate) valbyLanggade).setPriceForHouse(1000);
        addSpace(valbyLanggade);

        Space allegade = new RealEstate();
        allegade.setName("Allégade");
        ((RealEstate) allegade).setCost(2400);
        ((RealEstate) allegade).setRent(150);
        ((RealEstate) allegade).setColorGroup(ColorGroup.pink);
        ((RealEstate) allegade).setPriceForHouse(1000);
        addSpace(allegade);

        Space faengsel = new Space();
        faengsel.setName("I fængsel/På besøg");
        addSpace(faengsel);

        Space frederiksbergAlle = new RealEstate();
        frederiksbergAlle.setName("Fredriksberg Allé");
        ((RealEstate) frederiksbergAlle).setCost(2800);
        ((RealEstate) frederiksbergAlle).setRent(200);
        ((RealEstate) frederiksbergAlle).setColorGroup(ColorGroup.green);
        ((RealEstate) frederiksbergAlle).setPriceForHouse(2000);
        addSpace(frederiksbergAlle);

        Space tuborg = new Utility();
        tuborg.setName("Tuborg");
        ((Utility) tuborg).setCost(3000);
        ((Utility) tuborg).setRent(0);
        ((Utility) tuborg).setColorGroup(ColorGroup.darkgreen);
        addSpace(tuborg);

        Space bulowsvej = new RealEstate();
        bulowsvej.setName("Bülowsvej");
        ((RealEstate) bulowsvej).setCost(2800);
        ((RealEstate) bulowsvej).setRent(200);
        ((RealEstate) bulowsvej).setColorGroup(ColorGroup.green);
        ((RealEstate) bulowsvej).setPriceForHouse(2000);
        addSpace(bulowsvej);

        Space gammelkongevej = new RealEstate();
        gammelkongevej.setName("Gammelkongevej");
        ((RealEstate) gammelkongevej).setCost(3200);
        ((RealEstate) gammelkongevej).setRent(250);
        ((RealEstate) gammelkongevej).setColorGroup(ColorGroup.green);
        ((RealEstate) gammelkongevej).setPriceForHouse(2000);
        addSpace(gammelkongevej);

        Space dfds = new Utility();
        dfds.setName("D.F.D.S");
        ((Utility) dfds).setCost(4000);
        ((Utility) dfds).setRent(500);
        ((Utility) dfds).setColorGroup(ColorGroup.navy);
        addSpace(dfds);

        Space bernstorffsvej = new RealEstate();
        bernstorffsvej.setName("Bernstorffsvej");
        ((RealEstate) bernstorffsvej).setCost(3600);
        ((RealEstate) bernstorffsvej).setRent(300);
        ((RealEstate) bernstorffsvej).setColorGroup(ColorGroup.darkgrey);
        ((RealEstate) bernstorffsvej).setPriceForHouse(2000);
        addSpace(bernstorffsvej);

        chance = new Chance(); //colorgroup?
        chance.setName("chance");
        addSpace(chance);

        Space hellerupvej = new RealEstate();
        hellerupvej.setName("Hellerupvej");
        ((RealEstate) hellerupvej).setCost(3600);
        ((RealEstate) hellerupvej).setRent(300);
        ((RealEstate) hellerupvej).setColorGroup(ColorGroup.darkgrey);
        ((RealEstate) hellerupvej).setPriceForHouse(2000);
        addSpace(hellerupvej);

        Space strandvej = new RealEstate();
        strandvej.setName("Strandvej");
        ((RealEstate) strandvej).setCost(4000);
        ((RealEstate) strandvej).setRent(350);
        ((RealEstate) strandvej).setColorGroup(ColorGroup.darkgrey);
        ((RealEstate) strandvej).setPriceForHouse(2000);
        addSpace(strandvej);

        Space parkering = new Space();
        parkering.setName("Helle");
        addSpace(parkering);

        Space trianglen = new RealEstate();
        trianglen.setName("Trianglen");
        ((RealEstate) trianglen).setCost(4400);
        ((RealEstate) trianglen).setRent(350);
        ((RealEstate) trianglen).setColorGroup(ColorGroup.red);
        ((RealEstate) trianglen).setPriceForHouse(3000);
        addSpace(trianglen);

        chance = new Chance(); //colorgroup?
        chance.setName("chance");
        addSpace(chance);

        Space oesterbrogade = new RealEstate();
        oesterbrogade.setName("Østerbrogade");
        ((RealEstate) oesterbrogade).setCost(4400);
        ((RealEstate) oesterbrogade).setRent(350);
        ((RealEstate) oesterbrogade).setColorGroup(ColorGroup.red);
        ((RealEstate) oesterbrogade).setPriceForHouse(3000);
        addSpace(oesterbrogade);

        Space gronningen = new RealEstate();
        gronningen.setName("Grønningen");
        ((RealEstate) gronningen).setCost(4800);
        ((RealEstate) gronningen).setRent(400);
        ((RealEstate) gronningen).setColorGroup(ColorGroup.red);
        ((RealEstate) gronningen).setPriceForHouse(3000);
        addSpace(gronningen);

        Space oes = new Utility();
        oes.setName("Ø.S.");
        ((Utility) oes).setCost(4000);
        ((Utility) oes).setCost(500);
        ((Utility) oes).setColorGroup(ColorGroup.navy);
        addSpace(oes);

        Space bredgade = new RealEstate();
        bredgade.setName("Bredgade");
        ((RealEstate) bredgade).setCost(5200);
        ((RealEstate) bredgade).setRent(450);
        ((RealEstate) bredgade).setColorGroup(ColorGroup.white);
        ((RealEstate) bredgade).setPriceForHouse(3000);
        addSpace(bredgade);

        Space kgsnytorv = new RealEstate();
        kgsnytorv.setName("Kgs. Nytorv");
        ((RealEstate) kgsnytorv).setCost(5200);
        ((RealEstate) kgsnytorv).setRent(450);
        ((RealEstate) kgsnytorv).setColorGroup(ColorGroup.white);
        ((RealEstate) kgsnytorv).setPriceForHouse(3000);
        addSpace(kgsnytorv);

        Space carlsberg = new Utility();
        carlsberg.setName("Carlsberg");
        ((Utility) carlsberg).setCost(3000);
        ((Utility) carlsberg).setRent(0);
        ((Utility) carlsberg).setColorGroup(ColorGroup.darkgreen);
        addSpace(carlsberg);

        Space oestergade = new RealEstate();
        oestergade.setName("Østergade");
        ((RealEstate) oestergade).setCost(5600);
        ((RealEstate) oestergade).setRent(500);
        ((RealEstate) oestergade).setColorGroup(ColorGroup.white);
        ((RealEstate) oestergade).setPriceForHouse(3000);
        addSpace(oestergade);

        Space ifaengsel = new Space();
        ifaengsel.setName("I fængsel");
        addSpace(ifaengsel);

        Space amagertorv = new RealEstate();
        amagertorv.setName("Amagertorv");
        ((RealEstate) amagertorv).setCost(6000);
        ((RealEstate) amagertorv).setRent(550);
        ((RealEstate) amagertorv).setColorGroup(ColorGroup.yellow);
        ((RealEstate) amagertorv).setPriceForHouse(4000);
        addSpace(amagertorv);

        Space vimmelskaftet = new RealEstate();
        vimmelskaftet.setName("Vimmelskaftet");
        ((RealEstate) vimmelskaftet).setCost(6000);
        ((RealEstate) vimmelskaftet).setRent(550);
        ((RealEstate) vimmelskaftet).setColorGroup(ColorGroup.yellow);
        ((RealEstate) vimmelskaftet).setPriceForHouse(4000);
        addSpace(vimmelskaftet);

        chance = new Chance(); //colorgroup?
        chance.setName("chance");
        addSpace(chance);

        Space nygade = new RealEstate();
        nygade.setName("Nygade");
        ((RealEstate) nygade).setCost(6400);
        ((RealEstate) nygade).setRent(600);
        ((RealEstate) nygade).setColorGroup(ColorGroup.yellow);
        addSpace(nygade);

        Space bornholm = new Utility();
        bornholm.setName("Bornholm");
        ((Utility) bornholm).setCost(4000);
        ((Utility) bornholm).setRent(500);
        addSpace(bornholm);

        chance = new Chance(); //colorgroup?
        chance.setName("chance");
        addSpace(chance);

        Space frederiksberggade = new RealEstate();
        frederiksberggade.setName("Frederiksberggade");
        ((RealEstate) frederiksberggade).setCost(7000);
        ((RealEstate) frederiksberggade).setRent(700);
        ((RealEstate) frederiksberggade).setColorGroup(ColorGroup.purple);
        ((RealEstate) frederiksberggade).setPriceForHouse(4000);
        addSpace(frederiksberggade);

        Space ekstraskat = new Space();
        ekstraskat.setName("Ekstraordinær statsskat");
        addSpace(ekstraskat);

        Space raadhuspladsen = new RealEstate();
        raadhuspladsen.setName("Rådhuspladsen");
        ((RealEstate) raadhuspladsen).setCost(8000);
        ((RealEstate) raadhuspladsen).setRent(1000);
        ((RealEstate) raadhuspladsen).setColorGroup(ColorGroup.purple);
        ((RealEstate) raadhuspladsen).setPriceForHouse(4000);
        addSpace(raadhuspladsen);


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
		p.setCurrentPosition(getSpaces().get(0));
		p.setColor(Color.GREEN);
		addPlayer(p);

		p = new Player();
		p.setName("Player 4");
		p.setCurrentPosition(getSpaces().get(0));
		p.setColor(Color.BLACK);
		addPlayer(p);

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
