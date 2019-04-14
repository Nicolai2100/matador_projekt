package game.controller;

import game.Test.GameStub;
import game.database.GameDAO;
import game.model.*;
import game.model.exceptions.GameEndedException;
import game.model.exceptions.PlayerBrokeException;
import game.model.properties.RealEstate;
import game.view.View;
import gui_main.GUI;

import java.util.*;

/**
 * The overall controller of a Monopoly game. It provides access
 * to all basic actions and activities for the game. All other
 * activities of the game, should be implemented by referring
 * to the basic actions and activities in this class.
 * <p>
 * Note that this controller is far from being finished and many
 * things could be done in a much nicer and cleaner way! But, it
 * shows the general idea of how the model, view (GUI), and the
 * controller could work with each other, and how different parts
 * of the game's activities can be separated from each other, so
 * that different parts can be added and extended independently
 * from each other.
 * <p>
 * For fully implementing the game, it will probably be necessary
 * to add more of these basic actions in this class.
 * <p>
 * The <code>doAction()</code> methods of the
 * {@link Space} and
 * the {@link Card}
 * can be implemented  on the basic actions and activities
 * of this game controller.
 * based
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class GameController {

    private Game game;
    private GameStub gameStub;
    private GUI gui;
    private GameDAO gameDb;

    private View view;

    private boolean disposed = false;

    /**
     * Constructor for a controller of a game.
     *
     * @param game the game
     */
    public GameController(Game game) {
        super();
        this.game = game;
        gui = new GUI();
        gameDb = new GameDAO();
    }
    // Til tests
    public GameController(GameStub game) {
        super();
        this.gameStub = game;
        gui = new GUI();
    }

    /**
     * This method will initialize the GUI. It should be called after
     * the players of the game are created. As of now, the initialization
     * assumes that the spaces of the game fit to the fields of the GUI;
     * this could eventually be changed, by creating the GUI fields
     * based on the underlying game's spaces (fields).
     */
    public void initializeGUI() {
        this.view = new View(game, gui);
    }

    /**
     * @author Nicolai L, Nicolai W
     */
    public void playOrLoadGame() {
        String userSelection = gui.getUserButtonPressed("", "Start nyt spil", "Hent spil", "Afslut");
        if (userSelection.substring(0, 5).equalsIgnoreCase("start")) {
            game.shuffleCardDeck();
            ArrayList<Integer> options = new ArrayList<>(Arrays.asList(3, 4, 5, 6));
            Integer numOfPlayers = chooseFromOptions(options, "Hvor mange spillere?", "Annuller", null, null, null);
            if (numOfPlayers != null) {
                game.createPlayers(numOfPlayers);
                initializeGUI();
                view.createPlayers();
                play();
            }
        } else if (userSelection.equals("Afslut")) {
            System.exit(0);
        } else {
            String gameSelection = chooseFromOptions(gameDb.getGamesList(), "Vælg spil:", "Annuller", null, null, null);
            if (gameSelection != null) {
                game = gameDb.loadGame(game, gameSelection);
                game.shuffleCardDeck();
                initializeGUI();
                view.loadPlayers();
                play();
            }
        }
        playOrLoadGame();
    }

    /**
     * The main method to start the game. The game is started with the
     * current player of the game; this makes it possible to resume a
     * game at any point.
     */
    public void play() {
        List<Player> players = game.getPlayers();
        Player c = game.getCurrentPlayer();

        int current = 0;
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            if (c.equals(p)) {
                current = i;
            }
        }

        boolean terminated = false;
        while (!terminated) {
            Player player = players.get(current);

            if (!player.isBroke()) {
                try {
                    showTurnMenu(player, true);
                    this.makeMove(player);
                    showTurnMenu(player, false);
                } catch (PlayerBrokeException e) {
                } catch (GameEndedException w) {
                    gui.showMessage(w.getMessage());
                    if (winner())
                        terminated = true;
                } finally {

                }
                // We could react to the player having gone broke
            }

            current = (current + 1) % players.size();
            game.setCurrentPlayer(players.get(current));
            /* Commented out, because I think it's not neccesary.
            if (current == 0) {
                String selection = gui.getUserButtonPressed("En runde er slut. Vil I fortsætte spillet?", "Ja", "Nej");
                if (selection.equals("Nej")) {
                    terminated = true;
                }
            }
            */
        }

        dispose();
    }

    /**
     * @param player
     * @param startOfTurn
     * @author Nicolai Wulff, s185036
     */
    public void showTurnMenu(Player player, Boolean startOfTurn) {
        boolean continueChoosing = true;
        while (continueChoosing) {
            String choice = null;

            if (startOfTurn) {
                choice = gui.getUserButtonPressed("Det er " + player.getName() + "s tur. Hvad skal der ske?" , "Kør", "Byg huse", "Sælg huse", "Handel", "Pantsættelser", "Gem spil");
            } else {
                choice = gui.getUserButtonPressed("Det er stadig " + player.getName() + "'s tur. Hvad skal der ske?" , "Slut turen", "Byg huse", "Sælg huse", "Handel", "Pantsættelser");
            }

            if (choice.equals("Byg huse")) {
                buyHouseAction();
            } else if (choice.equals("Handel")) {
                trade(null);
            } else if (choice.equals("Sælg huse")) {
                sellHouseAction(null);
            } else if (choice.equals("Pantsættelser")) {
                String input = gui.getUserButtonPressed("Vælg:", "Pantsætte", "Indfri gæld", "Tilbage til menu");
                if (input.equals("Pantsætte")) {
                    mortgageAction(null);
                } else if (input.equals("Indfri gæld")) {
                    unmortgageAction();
                }
            } else if (choice.equals("Gem spil")) {
                saveGame();
            } else {
                continueChoosing = false;
            }
        }
    }

    public void saveGame() {
        if (game.getGameId() < 0) {
            gameDb.saveGame(game);
        } else {
            gameDb.updateGame(game);
        }
        gui.showMessage("Spillet blev gemt!");
    }

    public boolean gameEnds() {
        boolean returnBool = false;

        int countBroke = 0;
        for (Player p : game.getPlayers()) {
            if (p.isBroke()) {
                countBroke++;
            }
        }
        if (countBroke >= game.getPlayers().size() - 1) {
            returnBool = true;

        }
        return returnBool;
    }

    public boolean winner() {
        // Check whether we have a winner
        boolean returnBool = false;
        Player winner = null;
        int countActive = 0;
        for (Player p : game.getPlayers()) {
            if (!p.isBroke()) {
                countActive++;
                winner = p;
            }
        }
        if (countActive == 1) {
            gui.showMessage(
                    "Player " + winner.getName() +
                            " has won with " + winner.getBalance() + "$.");
            returnBool = true;
        } else if (countActive < 1) {
            // This can actually happen in very rare conditions and only
            // if the last player makes a stupid mistake (like buying something
            // in an auction in the same round when the last but one player went
            // bankrupt)
            gui.showMessage(
                    "All players are broke.");
            returnBool = false;
        }
        return returnBool;
    }

    /**
     * This method implements a activity of asingle move of the given player.
     * It throws a {@link PlayerBrokeException}
     * if the player goes broke in this move. Note that this is still a very
     * basic implementation of the move of a player; many aspects are still
     * missing.
     *
     * @param player the player making the move
     * @throws PlayerBrokeException if the player goes broke during the move
     */
    public void makeMove(Player player) throws PlayerBrokeException, GameEndedException {

        boolean castDouble;
        int doublesCount = 0;
        do {
            int die1 = (int) (1 + 6.0 * Math.random());
            int die2 = (int) (1 + 6.0 * Math.random());
            castDouble = (die1 == die2);
            gui.setDice(die1, die2);

            if (player.isInPrison() && castDouble) {
                player.setInPrison(false);
                gui.showMessage("Player " + player.getName() + " leaves prison now since he cast a double!");
            } else if (player.isInPrison()) {
                gui.showMessage("Player " + player.getName() + " stays in prison since he did not cast a double!");
            }
            // TODO note that the player could also pay to get out of prison,
            //      which is not yet implemented
            if (castDouble) {
                doublesCount++;
                if (doublesCount > 2) {
                    gui.showMessage("Player " + player.getName() + " has cast the third double and goes to jail!");
                    gotoJail(player);
                    return;
                }
            }
            if (!player.isInPrison()) {
                // make the actual move by computing the new position and then
                // executing the action moving the player to that space
                int pos = player.getCurrentPosition().getIndex();
                List<Space> spaces = game.getSpaces();
                int newPos = (pos + die1 + die2) % spaces.size();
                Space space = spaces.get(newPos);
                moveToSpace(player, space);
                if (castDouble) {
                    gui.showMessage("Player " + player.getName() + " cast a double and gets to make another move.");
                    showTurnMenu(player, true);
                }
            }
        } while (castDouble);


    }

    /**
     * This method implements the activity of moving the player to the new position,
     * including all actions associated with moving the player to the new position.
     *
     * @param player the moved player
     * @param space  the space to which the player moves
     * @throws PlayerBrokeException when the player goes broke doing the action on that space
     */
    public void moveToSpace(Player player, Space space) throws PlayerBrokeException, GameEndedException {
        int posOld = player.getCurrentPosition().getIndex();
        player.setCurrentPosition(space);

        if (posOld > player.getCurrentPosition().getIndex()) {
            // Note that this assumes that the game has more than 12 spaces here!
            // TODO: the amount of 2000$ should not be a fixed constant here (could also
            //       be configured in the Game class.
            gui.showMessage("Player " + player.getName() + " receives 2000$ for passing Go!");
            this.paymentFromBank(player, 2000);
        }
        gui.showMessage("Player " + player.getName() + " arrives at " + space.getIndex() + ": " + space.getName() + ".");

        // Execute the action associated with the respective space. Note
        // that this is delegated to the field, which implements this action
        space.doAction(this, player);
    }

    /**
     * The method implements the action of a player going directly to jail.
     *
     * @param player the player going to jail
     */
    public void gotoJail(Player player) {
        // Field #10 is in the default game board of Monopoly the field
        // representing the prison.
        // TODO the 10 should not be hard coded
        player.setCurrentPosition(game.getSpaces().get(10));
        player.setInPrison(true);
    }

    /**
     * The method implementing the activity of taking a chance card.
     *
     * @param player the player taking a chance card
     * @throws PlayerBrokeException if the player goes broke by this activity
     */
    public void takeChanceCard(Player player) throws PlayerBrokeException, GameEndedException {
        /*
        todo få metoden til at virke!
        Card card = game.drawCardFromDeck();

        System.out.println("Der er kommenteret en metode ud fordi den smed fejl");

        gui.displayChanceCard(card.getText());
        gui.showMessage("Player " + player.getName() + " draws a chance card.");

        try {
            card.doAction(this, player);
        } finally {
            gui.displayChanceCard("done");
        }*/
    }

    /**
     * This method implements the action returning a drawn card or a card keep with
     * the player for some time back to the bottom of the card deck.
     *
     * @param card returned card
     */
    public void returnChanceCardToDeck(Card card) {
        game.returnCardToDeck(card);
    }

    /**
     * This method implements the activity where a player can obtain
     * cash by selling houses back to the bank, by mortgaging own properties,
     * or by selling properties to other players. This method is called, whenever
     * the player does not have enough cash available for an action. If
     * the player does not manage to free at least the given amount of money,
     * the player will be broke; this is to help the player make the right
     * choices for freeing enough money.
     *
     * @param player the player
     * @param amount the amount the player should have available after the act
     * @author Nicolai Wulff s185036
     */
    public void obtainCash(Player player, int amount, boolean required) {
        boolean tryToObtain = true;
        while (tryToObtain) {

            if (player.getOwnedProperties().size() == 0 && player.getBalance() < amount && required) {
                gui.showMessage(player + ", du har desværre intet tilbage, du kan sælge, og må derfor erklæres konkurs.");
                return;
            }

            String choice = "";
            if (required) {
                if (player.getBalance() < amount) choice = gui.getUserButtonPressed(player + ", du har i øjeblikket ikke nok penge til at betale " + amount + "kr. Hvad vil du gøre?", "Sælg huse", "Pantsæt grunde", "Handle", "Erklær dig konkurs");
                if (player.getBalance() >= amount) choice = gui.getUserButtonPressed(player + ", du har nu råd til at betale " + amount + "kr. Du kan nu fortsætte med at sælge/handle eller gå til betaling.", "Sælg huse", "Pantsæt grunde", "Handle", "Betal");
            } else {
                if (player.getBalance() < amount) choice = gui.getUserButtonPressed(player + ", du har i øjeblikket ikke nok penge til at betale " + amount + "kr. Hvad vil du gøre?", "Sælg huse", "Pantsæt grunde", "Handle", "Opgiv at købe");
                if (player.getBalance() >= amount) choice = gui.getUserButtonPressed(player + ", du har nu råd til at købe grunden for " + amount + "kr. Du kan nu fortsætte med at sælge/handle eller gå til betaling.", "Sælg huse", "Pantsæt grunde", "Handle", "Køb", "Fortryd købet");
            }

            if (choice.equals("Sælg huse")) {
                sellHouseAction(player);
            } else if (choice.equals("Pantsæt grunde")) {
                mortgageAction(player);
            } else if (choice.equals("Handle")) {
                trade(player);
            } else if(choice.equals("Betal") || choice.equals("Køb")) {
                if (player.getBalance() < amount && choice.equals("Betal")) {
                    gui.showMessage("Du har stadig ikke råd til at betale!");
                } else if (player.getBalance() < amount && choice.equals("Køb")) {
                    gui.showMessage("Du har stadig ikke råd til at købe!");
                } else {
                    tryToObtain = false;
                }
            } else if (choice.equals("Erklær dig konkurs")) {
                if (player.getBalance() > amount) {
                    gui.showMessage("Du kan ikke erklære dig konkurs, da du har råd til at betale.");
                } else if (player.getOwnedPropertiesNotMortgaged().size() > 0) {
                    boolean ownsHouses = false;
                    for (Property property : player.getOwnedPropertiesNotMortgaged()) {
                        if (property instanceof RealEstate) {
                            if (((RealEstate) property).getHouseCount() > 0) ownsHouses = true;
                        }
                    }
                    if (ownsHouses) {
                        gui.showMessage("Du har stadig huse/hoteller, du kan sælge!");
                    } else {
                        gui.showMessage("Du har stadig grunde, du kan pantsætte eller handle med!");
                    }
                } else if (player.getOwnedProperties().size() > 0 && player.getOwnedPropertiesNotMortgaged().size() == 0) {
                    String choice2 = gui.getUserButtonPressed("Er du sikker på, du ikke vil forsøge at sælge dine pantsatte grunde til andre spillere?", "Ja", "Nej");
                    if (choice2.equals("Ja")) tryToObtain = false;
                }
            } else {
                tryToObtain = false;
            }
        }
    }

    /**
     * This method implements the activity of offering a player to buy
     * a property. This is typically triggered by a player arriving on
     * an property that is not sold yet. If the player chooses not to
     * buy, the property will be set for auction.
     *
     * @param property the property to be sold
     * @param player   the player the property is offered to
     * @throws PlayerBrokeException when the player chooses to buy but could not afford it
     */
    public void offerToBuy(Property property, Player player) throws PlayerBrokeException, GameEndedException {

        if (player.getBalance() < property.getCost()) {
            String choice = gui.getUserButtonPressed(player + ", du har i øjeblikket ikke penge nok til at købe " + property + ". Vil du forsøge at finde pengene ved at sælge, handle eller pantsætte noget?", "Ja", "Nej");
            if (choice.equals("Ja")) {
                obtainCash(player, property.getCost(), false);
            }
        }

        String choice = "Nej";
        if (player.getBalance() > property.getCost()) {
            choice = gui.getUserSelection(player + ", ønsker du at købe " + property + " for " + property.getCost() + "kr?", "Ja", "Nej");
        }

        if (choice.equals("Ja")) {
            try {
                paymentToBank(player, property.getCost());
            } catch (PlayerBrokeException e) {
                // if the payment fails due to the player being broke,
                // an auction (among the other players is started
                player.setBroke(true);
                auction(property);
                // then the current move is aborted by casting the
                // PlayerBrokeException again
                throw e;
            }
            property.setOwner(player);
            player.addOwnedProperty(property);
            return;
        }

        // In case the player does not buy the property,
        // an auction is started
        auction(property);
    }

    /**
     * This method implements a payment activity to another player,
     * which involves the player to obtain some cash on the way, in case he does
     * not have enough cash available to pay right away. If he cannot free
     * enough money in the process, the player will go bankrupt.
     *
     * @param payer    the player making the payment
     * @param amount   the payed amount
     * @param receiver the beneficiary of the payment
     * @throws PlayerBrokeException when the payer goes broke by this payment
     */
    public void payment(Player payer, int amount, Player receiver) throws PlayerBrokeException, GameEndedException {
        if (payer.getBalance() < amount) {
            obtainCash(payer, amount, true);
            if (payer.getBalance() < amount) {
                playerBrokeTo(payer, receiver);
                if (gameEnds())
                    throw new GameEndedException();
                throw new PlayerBrokeException(payer);
            }
        }
        gui.showMessage("Player " + payer.getName() + " pays " + amount + "$ to player " + receiver.getName() + ".");
        payer.payMoney(amount);
        receiver.receiveMoney(amount);
    }
    /**
     * This method implements the action of a player receiving money from
     * the bank.
     *
     * @param player the player receiving the money
     * @param amount the amount
     */
    public void paymentFromBank(Player player, int amount) {
        player.receiveMoney(amount);
    }

    /**
     * This method implements the activity of a player making a payment to
     * the bank. Note that this might involve the player to obtain some
     * cash; in case he cannot free enough cash, he will go bankrupt
     * to the bank.
     *
     * @param player the player making the payment
     * @param amount the amount
     * @throws PlayerBrokeException when the player goes broke by the payment
     */
    public void paymentToBank(Player player, int amount) throws PlayerBrokeException, GameEndedException {
        if (amount > player.getBalance()) {
            obtainCash(player, amount, true);
            if (amount > player.getBalance()) {
                playerBrokeToBank(player);
                if (gameEnds())
                    throw new GameEndedException();
                throw new PlayerBrokeException(player);
            }
        }
        gui.showMessage("Player " + player.getName() + " pays " + amount + "$ to the bank.");
        player.payMoney(amount);
    }

    /**
     * This method implements the activity of auctioning a property.
     *
     * @param property the property which is for auction
     * @author Jeppe s170196, Mads s170185
     */
    public void auction(Property property) throws GameEndedException {
        List<Player> bidders = new ArrayList<>();
        int currentBid;
        int highBid = 0;
        Player highBidder = new Player();

        bidders.addAll(game.getPlayers());
        Collections.shuffle(bidders);

        gui.showMessage("Der vil nu blive afholdt en auktion for at købe " + property + ". Grunden er vurderet til " + property.getCost() + "kr. Man udgår af auktionen ved ikke at byde over.");

        while (bidders.size() != 1) {
            for (int i = 0; i < bidders.size(); i++) {
                Player p = bidders.get(i);
                if (!p.equals(highBidder)) {

                    currentBid = highBid == 0 ? gui.getUserInteger(p + ", hvad vil du byde?", 0, p.getBalance())
                            : gui.getUserInteger(p + ", hvad vil du byde?. Højeste bud er: " + highBid + "kr af " + highBidder, 0, p.getBalance());

                    if (currentBid > highBid) {
                        highBid = currentBid;
                        highBidder = p;
                    } else {
                        gui.showMessage(p + "du har ikke budt over og udgår derfor af auktionen.");
                        bidders.remove(p);
                    }
                }
            }
        }
        gui.showMessage("Første.. Anden.. Tredje.. " + highBidder + " vinder auktionen og køber " + property + " for " + highBid + "kr.");
        try {
            paymentToBank(highBidder, highBid);
            property.setOwner(highBidder);
            highBidder.addOwnedProperty(property);
        } catch (PlayerBrokeException e) {
            e.printStackTrace();
        }
    }
    /**
     * Action handling the situation when one player is broke to another
     * player. All money and properties are transferred to the other player.
     *
     * @param brokePlayer the broke player
     * @param benificiary the player who receives the money and assets
     */
    public void playerBrokeTo(Player brokePlayer, Player benificiary) {
        int amount = brokePlayer.getBalance();
        benificiary.receiveMoney(amount);
        brokePlayer.setBalance(0);
        brokePlayer.setBroke(true);

        // TODO We assume here, that the broke player has already sold all his houses! But, if
        // not, we could make sure at this point that all houses are removed from
        // properties (properties with houses on are not supposed to be transferred, neither
        // in a trade between players, nor when  player goes broke to another player)
        for (Property property : brokePlayer.getOwnedProperties()) {
            property.setOwner(benificiary);
            benificiary.addOwnedProperty(property);
        }
        brokePlayer.removeAllProperties();

        while (!brokePlayer.getOwnedCards().isEmpty()) {
            game.returnCardToDeck(brokePlayer.getOwnedCards().get(0));
        }

        gui.showMessage("Player " + brokePlayer.getName() + "went broke and transfered all"
                + "assets to " + benificiary.getName());
    }
    /**
     * Action handling the situation when a player is broke to the bank.
     *
     * @param player the broke player
     */
    public void playerBrokeToBank(Player player) {

        player.setBalance(0);
        player.setBroke(true);

        // TODO we also need to remove the houses and the mortgage from the properties

        for (Property property : player.getOwnedProperties()) {
            property.setOwner(null);
        }
        player.removeAllProperties();

        gui.showMessage("Player " + player.getName() + " went broke");

        while (!player.getOwnedCards().isEmpty()) {
            game.returnCardToDeck(player.getOwnedCards().get(0));
        }
    }
    /**
     * Method for disposing of this controller and cleaning up its resources.
     */
    public void dispose() {
        if (!disposed && view != null) {
            disposed = true;
            if (view != null) {
                view.dispose();
                view = null;
            }
            // TODO we should also dispose of the GUI here. But this works only
            //      for my private version of the GUI and not for the GUI currently
            //      deployed via Maven (or other official versions);
        }
    }
    /**
     * Buys a house, if the player can afford it.
     * @param player
     * @param re
     * @Author Nicolai Wulff s185036
     */
    private void buyHouse(Player player, RealEstate re) {
        int lowestHouseCount = 5;
        for (Property property : player.getOwnedProperties()) {
            if (property.getColorGroup() == re.getColorGroup() && ((RealEstate) property).getHouseCount() < lowestHouseCount) {
                lowestHouseCount = ((RealEstate) property).getHouseCount();
            }
        }
        if(re.getHouseCount() == 5) {
            gui.showMessage("Du kan ikke bygge mere på denne grund!");
        } else if (re.getHouseCount() > lowestHouseCount){
            gui.showMessage("Du skal bygge jævnt!");
        } else {
            try {
                paymentToBank(player, re.getPriceForHouse());
                re.setHouseCount(re.getHouseCount() + 1);
            } catch (PlayerBrokeException e){
                gui.showMessage(player.getName() + " har ikke råd til at bygge et hus på denne grund.");
            } catch (GameEndedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param player
     * @param re
     * @author Nicolai Wulff s185036
     */
    private void sellHouse(Player player, RealEstate re) {
        int highestHouseCount = 0;
        for (Property property : player.getOwnedProperties()) {
            if (property.getColorGroup() == re.getColorGroup() && ((RealEstate) property).getHouseCount() > highestHouseCount) {
                highestHouseCount = ((RealEstate) property).getHouseCount();
            }
        }
        if (re.getHouseCount() == 0) {
            gui.showMessage("Du har ingen huse på denne grund!");
        } else if (re.getHouseCount() < highestHouseCount) {
            gui.showMessage("Du skal sælge dine huse jævnt!");
        } else {
            paymentFromBank(player, re.getPriceForHouse() / 2);
            re.setHouseCount(re.getHouseCount() - 1);
        }
    }

    /** Asks the player, if he/she wants to build houses, if the player owns any real estate.
     * @Author Nicolai Wulff s185036
     */
    private void buyHouseAction() {
        Player player = choosePlayer("Hvilken spiller ønsker at købe huse?", null, false);
        if (player == null) return;
        boolean continueBuying = true;
        while (continueBuying) {
            ArrayList<RealEstate> potentialProperties = new ArrayList<>();
            ArrayList<Integer> housePrices = new ArrayList<>();
            ArrayList<String> houseOrHotel = new ArrayList<>();
            for (Property property : player.getOwnedProperties()) {
                if (property instanceof RealEstate && property.getSuperOwned()) {
                    if (((RealEstate) property).getHouseCount() < 5) {
                        potentialProperties.add((RealEstate) property);
                        housePrices.add(((RealEstate) property).getPriceForHouse());
                        if (((RealEstate) property).getHouseCount() < 4) {
                            houseOrHotel.add("kr/hus");
                        } else if (((RealEstate) property).getHouseCount() == 4) {
                            houseOrHotel.add("kr/hotel");
                        }
                    }
                }
            }

            RealEstate re = chooseFromOptions(
                    potentialProperties,
                    "På hvilken grund vil du købe et hus/hotel?",
                    "Stop med at købe",
                    ", ",
                    housePrices,
                    houseOrHotel);
            if (re == null) {
                continueBuying = false;
            } else {
                buyHouse(player, re);
            }
        }
    }

    /**
     * @author Nicolai Wulff s185036
     */
    private void sellHouseAction(Player player) {
        if (player == null) player = choosePlayer("Hvilken spiller ønsker at sælge huse?", null, false);
        if (player == null) return;
        boolean continueSelling = true;
        while (continueSelling) {
            //Makes a list of all real estate, that has at least one house built on it.
            ArrayList<RealEstate> propertyOptions = new ArrayList<>();
            ArrayList<Integer> housePrices = new ArrayList<>();
            ArrayList<String> houseOrHotel = new ArrayList<>();
            for (Property property : player.getOwnedProperties()) {
                if (property instanceof RealEstate) {
                    if (((RealEstate) property).getHouseCount() > 0) {
                        propertyOptions.add((RealEstate) property);
                        housePrices.add(((RealEstate) property).getPriceForHouse() / 2);
                        if (((RealEstate) property).getHouseCount() < 5) {
                            houseOrHotel.add("kr/hus");
                        } else if (((RealEstate) property).getHouseCount() == 5) {
                            houseOrHotel.add("kr/hotel");
                        }
                    }
                }
            }

            RealEstate re = chooseFromOptions(
                    propertyOptions,
                    "Fra hvilken grund ønsker du at sælge huse?",
                    "Stop med at sælge",
                    ", ",
                    housePrices,
                    houseOrHotel);
            if (re == null) {
                continueSelling = false;
            } else {
                sellHouse(player, re);
            }
        }
    }

    /**
     * @author Nicolai Wulff, s185036
     */
    private void trade(Player firstParty) {
        Player[] tradingPlayers = new Player[2];
        tradingPlayers[0] = firstParty;
        int[] moneyInOffers = new int[2];
        ArrayList<Property>[] propertiesInOffers = new ArrayList[2];
        propertiesInOffers[0] = new ArrayList<>();
        propertiesInOffers[1] = new ArrayList<>();

        //Keep asking which players wants to trade. Check if each player is in jail).
        boolean choosing = true;
        int i = 0;
        while (choosing) {
            Player player = null;
            if (firstParty != null) i++;
            if (i == 0 && firstParty == null) player = choosePlayer("Hvem er den ene part i handlen?", null, true);
            if (i == 1) player = choosePlayer("Hvem er den anden part i handlen?", tradingPlayers[0], true);
            if (player != null && player.isInPrison()) {
                gui.showMessage(player.getName() + " er i fængsel, og må derfor ikke handle!");
            } else if (player != null) {
                tradingPlayers[i] = player;
                if (i == 1) choosing = false;
                i++;
            } else {
                return;
            }
        }

        //Loop through the two trading players.
        for (int j = 0; j < 2; j++) {
            //Ask what amount of money, the player wants to trade.
            moneyInOffers[j] = gui.getUserInteger(tradingPlayers[j] + ", hvilket beløb vil du tilføje i handlen?", 0, tradingPlayers[j].getBalance());

            //Keep adding properties to the trade, until the player chooses to stop.
            boolean continueAdding = true;
            while(continueAdding) {

                ArrayList<Property> propertyOptions = new ArrayList<>();
                for (Property p : tradingPlayers[j].getOwnedProperties()) {
                    if (!propertiesInOffers[j].contains(p)) propertyOptions.add(p);
                }

                Property property = chooseFromOptions(propertyOptions, "Hvilken grund vil du tilføje til handlen?", "Stop med at tilføje grunde", null, null, null);
                if (property == null) {
                    continueAdding = false;
                } else {
                    //Check if the chosen property or any property with the same color group has any houses.
                    //If they do, refuse to trade it, and tell the player to sell their houses first.
                    boolean tradeAble = true;
                    for (Property p : tradingPlayers[j].getOwnedProperties()) {
                        if (p instanceof RealEstate && p.getColorGroup() == property.getColorGroup()) {
                            if (((RealEstate) p).getHouseCount() > 0) {
                                gui.showMessage("Du kan ikke handle med grunde, hvor du har bygget huse i dens farvegruppe.\nSælg din huse i gruppen, før du handler.");
                                tradeAble = false;
                            }
                        }
                    }

                    if (tradeAble) {
                        propertiesInOffers[j].add(property);
                    }
                }
            }
        }

        //Construct a string for each player, containing all the properties,
        // that each player added to the trade, seperated by commas.
        String[] playerProperties = {"", ""};
        for (i = 0; i < 2; i++) {
            for (int j = 0; j < propertiesInOffers[i].size(); j++) {
                playerProperties[i] += propertiesInOffers[i].get(j);
                if (j != 0) {
                    playerProperties[i] += ", ";
                }
            }
        }

        //Construct a string showing a complete overview of the trade.
        String tradeOverview = tradingPlayers[0].getName() + ": " + moneyInOffers[0] + "kr.\n"
                + tradingPlayers[1].getName() + ": " + moneyInOffers[1] + "kr.\n"
                + tradingPlayers[0].getName() + ": " + playerProperties[0] + "\n"
                + tradingPlayers[1].getName() + ": " + playerProperties[1] + "\n";

        //Ask if the players want to accept and complete the trade.
        String accept = gui.getUserButtonPressed("Jeres byttehandel ser således ud:\n" + tradeOverview + "\nVil I acceptere og lukke handlen?", "Ja", "Nej");

        //If yes, make the actual trade (exchange money and properties).
        if (accept.equals("Ja")) {
            try {
                payment(tradingPlayers[0], moneyInOffers[0], tradingPlayers[1]);
                payment(tradingPlayers[1], moneyInOffers[1], tradingPlayers[0]);

                for (Property property : propertiesInOffers[0]) {
                    transferProperty(tradingPlayers[0], property, tradingPlayers[1]);
                }
                for (Property property : propertiesInOffers[1]) {
                    transferProperty(tradingPlayers[1], property, tradingPlayers[0]);
                }

            } catch (PlayerBrokeException e) {
                e.printStackTrace();
            } catch (GameEndedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Transfers a property from one player (giver) to another (receiver)
     * @param giver
     * @param property
     * @param receiver
     * @author Nicolai Wulff s185036
     */
    private void transferProperty(Player giver, Property property, Player receiver) {
        property.setOwner(null);
        giver.removeOwnedProperty(property);
        property.setOwner(receiver);
        receiver.addOwnedProperty(property);
    }

    private void mortgageAction(Player player) {
        if (player == null) player = choosePlayer("Hvilken spiller ønsker at pantsætte?", null, false);
        if (player == null) return;
        boolean continuePawning = true;
        while (continuePawning) {
            ArrayList<Property> potentialProperties = new ArrayList<>();
            for (Property property : player.getOwnedProperties()) {
                if (!property.getMortgaged()) {
                    potentialProperties.add(property);
                }
            }

            if (potentialProperties.size() == 0) {
                gui.showMessage("Du ejer ingen grunde, du kan pantsætte!");
                return;
            }

            ArrayList<Integer> mortgageValues = new ArrayList<>();
            for (Property p : potentialProperties) mortgageValues.add(p.getCost() / 2);
            Property property = chooseFromOptions(
                    potentialProperties,
                    "Hvilken grund ønsker du at pantsætte?",
                    "Stop med at pantsætte",
                    ", pantsætningsværdi: ",
                    mortgageValues,
                    " kr.");

            if (property == null) {
                return;
            }

            if (property instanceof RealEstate) {
                boolean ableToMortgage = true;
                for (Property p : player.getOwnedProperties()) {
                    if (p.getColorGroup() == property.getColorGroup() && ((RealEstate) p).getHouseCount() > 0) {
                        ableToMortgage = false;
                    }
                }

                if (!ableToMortgage) {
                    gui.showMessage("Du skal sælge alle huse i farvegruppen, før du kan pantsætte!");
                } else {
                    mortgage(player, property);
                }
            } else {
                mortgage(player, property);
            }
        }
    }

    /**
     * @param player
     * @param property
     * @author Nicolai Wulff s185036
     */
    private void mortgage(Player player, Property property) {
        property.setMortgaged(true);
        paymentFromBank(player, property.getCost() / 2);
    }

    /**
     * Nicolai Wulff s185036
     */
    private void unmortgageAction() {
        Player player = choosePlayer("Hvilken spiller ønsker at indfri sin gæld i pantsættelser?", null, false);
        if (player == null) return;
        boolean continuePawning = true;
        while (continuePawning) {
            ArrayList<Property> potentialProperties = new ArrayList<>();
            for (Property property : player.getOwnedProperties()) {
                if (property.getMortgaged()) {
                    potentialProperties.add(property);
                }
            }

            if (potentialProperties.size() == 0) {
                gui.showMessage("Du har ingen pantsatte grunde!");
                return;
            }

            ArrayList<Integer> debts = new ArrayList<>();
            for (Property p : potentialProperties) debts.add(p.getCost() / 2);
            Property property = chooseFromOptions(
                    potentialProperties,
                    "Hvilken pantsat grund vil du tilbagebetale?",
                    "Tilbage til hovedmenu",
                    ", pantsætningsgæld: ",
                    debts,
                    " kr.");
            if (property == null) return;
            unmortgage(player, property);
        }
    }

    /**
     * @param player
     * @param property
     * @author Nicolai Wulff s185036
     */
    private void unmortgage(Player player, Property property) {
        try {
            paymentToBank(player, property.getCost() / 2);
            property.setMortgaged(false);
        } catch (PlayerBrokeException e) {
            e.printStackTrace();
        } catch (GameEndedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use to choose an active player from a dropdown menu in the gui.
     * May exclude a specific player from the list and may exclude players in prison.
     * @param msg Message over dropdown menu.
     * @param excludedPlayer Exlude a specific player.
     * @param mayBeInPrison If true, include players in prison. If false, exclude players in prison.
     * @return the chosen player.
     * @author Nicolai Wulff s185036
     */
    private Player choosePlayer(String msg, Player excludedPlayer, boolean mayBeInPrison) {
        //Make list of active players, that either may be or may not be in prison (depending on mayBeInPrison).
        ArrayList<Player> playerList = new ArrayList<>();
        for (Player player : game.getActivePlayers(mayBeInPrison)) {
            if (player != excludedPlayer) playerList.add(player);
        }
        return chooseFromOptions(playerList, msg, "Annuller", null, null, null);
    }

    /** Method with generic return type used to show a dropdown menu in the gui
     * with a certain list of options. The last option in the list is used to
     * cancel and return to menu with no action. Each option may have strings
     * and values added to the end, eg: ", price: 100$/house".
     * @param c A collection of options.
     * @param msg Message to be shown over the dropdown menu.
     * @param stopOption The string to represent the option to cancel, eg: "Cancel".
     * @param addToEnd1 String to be added to end of each option, before a certain value.
     * @param values Values to be added to end of each option.
     * @param addToEnd2 String or Arraylist with values to be added to end of each option, after a certain value.
     *                  If it's a string, the same string will be added to each option.
     *                  If it's an arraylist (of the same size as the collection, c) each element will be added
     *                  to the end of the element of the collection with the same index.
     * @param <T> Type of the objects listed.
     * @return The chosen option of type T.
     * @author Nicolai Wulff s185036
     */
    private <T> T chooseFromOptions(Collection<T> c, String msg, String stopOption, String addToEnd1, ArrayList values, Object addToEnd2) {
        String[] options = new String[c.size() + 1];
        Iterator iterator = c.iterator();
        for (int i = 0; i < options.length - 1; i++) {
            if (addToEnd1 == null) addToEnd1 = "";
            if (values != null && addToEnd2 instanceof String) {
                options[i] = iterator.next() + addToEnd1 + values.get(i) + addToEnd2;
            } else if (values != null && addToEnd2 instanceof ArrayList) {
                options[i] = iterator.next() + addToEnd1 + values.get(i) + ((ArrayList) addToEnd2).get(i);
            } else {
                options[i] = iterator.next().toString();
            }
        }
        options[options.length - 1] = stopOption;

        String choiceString = gui.getUserSelection(msg, options);
        if (choiceString.equals(stopOption)) return null;
        T choice = null;
        for (T t : c) {
            if (choiceString.contains(t.toString()))
                choice = t;
        }
        return choice;
    }

    /**
     * Used by Space objects to display a message in the gui,
     * instead of letting them access the gui.
     * @param message
     */
    public void showMessage(String message) {
        gui.showMessage(message);
    }
}