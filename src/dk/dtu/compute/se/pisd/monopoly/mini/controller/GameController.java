package dk.dtu.compute.se.pisd.monopoly.mini.controller;

import dk.dtu.compute.se.pisd.monopoly.mini.database.GameDAO;
import dk.dtu.compute.se.pisd.monopoly.mini.model.*;
import dk.dtu.compute.se.pisd.monopoly.mini.model.exceptions.GameEndedException;
import dk.dtu.compute.se.pisd.monopoly.mini.model.exceptions.PlayerBrokeException;
import dk.dtu.compute.se.pisd.monopoly.mini.model.properties.RealEstate;
import dk.dtu.compute.se.pisd.monopoly.mini.view.View;
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
 * {@link dk.dtu.compute.se.pisd.monopoly.mini.model.Space} and
 * the {@link dk.dtu.compute.se.pisd.monopoly.mini.model.Card}
 * can be implemented  on the basic actions and activities
 * of this game controller.
 * based
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class GameController {

    private Game game;
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
     * Nicolai L
     */
    public void playOrLoadGame() {
        String userSelection = gui.getUserButtonPressed("", "Start nyt spil", "Hent spil");
        if (userSelection.substring(0, 5).equalsIgnoreCase("start")) {
            game.shuffleCardDeck();
            int numOfPlayers = gui.getUserInteger("Hvor mange spillere?", 3,4);
            game.createPlayers(numOfPlayers);
            initializeGUI();
            view.createPlayers();
            play();
        } else {
            List<String> games = gameDb.getGamesList();
            String[] gamesArray = games.toArray(new String[games.size()]);
            String userGameSelection = gui.getUserSelection("Vælg spil:", gamesArray);
            game = gameDb.loadGame(game, userGameSelection);
            game.shuffleCardDeck();
            initializeGUI();
            view.loadPlayers();
            play();
        }
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
            if (current == 0) {
                String selection = gui.getUserButtonPressed("En runde er slut. Vil I fortsætte spillet?", "Ja", "Nej");
                if (selection.equals("Nej")) {
                    terminated = true;
                }
            }
        }

        dispose();
    }

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
                trade();
            } else if (choice.equals("Sælg huse")) {
                sellHouseAction();
            } else if (choice.equals("Pantsættelser")) {
                String input = gui.getUserButtonPressed("Vælg:", "Pantsætte", "Indfri gæld", "Tilbage til menu");
                if (input.equals("Pantsætte")) {
                    mortgageAction();
                } else if (input.equals("Indfri gæld")) {
                    unmortgageAction();
                }
            } else if (choice.equals("Gem spil")) {
                if (game.getGameId() < 0) {
                    gameDb.saveGame(game);
                } else {
                    gameDb.updateGame(game);
                }
            } else {
                continueChoosing = false;
            }
        }
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
     * It throws a {@link dk.dtu.compute.se.pisd.monopoly.mini.model.exceptions.PlayerBrokeException}
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
            // TODO right now the dice are limited to the numbers 1, 2 and 3
            // for making the game faster. Eventually, this should be set
            // to 1 - 6 again (to this end, the constants 3.0 below should
            // be set to 6.0 again.
            int die1 = (int) (1 + 3.0 * Math.random());
            int die2 = (int) (1 + 3.0 * Math.random());
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
        Card card = game.drawCardFromDeck();
/*        gui.displayChanceCard(card.getText());
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
     */
    public void obtainCash(Player player, int amount) {
        // TODO implement
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
        // TODO We might also allow the player to obtainCash before
        // the actual offer, to see whether he can free enough cash
        // for the sale.

        String choice = gui.getUserSelection(
                "Player " + player.getName() +
                        ": Do you want to buy " + property.getName() +
                        " for " + property.getCost() + "$?",
                "yes",
                "no");

        if (choice.equals("yes")) {
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
            obtainCash(payer, amount);
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
            obtainCash(player, amount);
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

        while (bidders.size() != 1) {
            for (int i = 0; i < bidders.size(); i++) {
                Player p = bidders.get(i);
                if (!p.equals(highBidder)) {

                    currentBid = highBid == 0 ? gui.getUserInteger(p.getName() + ":\nPlace bid", 0, p.getBalance())
                            : gui.getUserInteger(p.getName() + ":\nPlace bid. Current high bid: " + highBid + " by " + highBidder.getName(), 0, p.getBalance());

                    if (currentBid > highBid) {
                        highBid = currentBid;
                        highBidder = p;
                    } else {
                        bidders.remove(p);
                    }
                }
            }
        }
        property.setCost(highBid);
        try {
            offerToBuy(property, bidders.get(0));
            bidders.clear();
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
    public void buyHouse(Player player, RealEstate re) {
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

    public void sellHouse(Player player, RealEstate re) {
        int highestHouseCount = 0;
        for (Property property : player.getOwnedProperties()) {
            if (property.getColorGroup() == re.getColorGroup() && ((RealEstate) property).getHouseCount() > highestHouseCount) {
                highestHouseCount = ((RealEstate) property).getHouseCount();
            }
        }
        if (re.getHouseCount() < highestHouseCount) {
            gui.showMessage("Du skal sælge dine huse jævnt!");
        } else {
            paymentFromBank(player, re.getPriceForHouse() / 2);
            re.setHouseCount(re.getHouseCount() - 1);
        }
    }

    /** Asks the player, if he/she wants to build houses, if the player owns any real estate.
     * @Author Nicolai Wulff s185036
     */
    public boolean buyHouseAction() {

        String[] players = new String[game.getActivePlayers(false).size() + 1];
        for (int i = 0; i < players.length - 1; i++) {
            players[i] = game.getPlayers().get(i).getName();
        }
        players[players.length - 1] = "Annuller";

        String playerName = gui.getUserSelection("Hvilken spiller ønsker at bygge huse?", players);
        if (playerName.equals("Annuller")) return false;
        Player player = null;
        for (Player p : game.getPlayers()) {
            if (p.getName().equals(playerName)) {
                player = p;
            }
        }
        //Makes a list of all superowned real estate owned by the player, if any.
        ArrayList<RealEstate> potentialProperties = new ArrayList<>();
        for (Property property : player.getOwnedProperties()) {
            if (property instanceof RealEstate && property.getSuperOwned()) {
                potentialProperties.add((RealEstate) property);
            }
        }
        //If the player owns superowned real estate, ask if he/she wants to build houses.
        if (potentialProperties.size() > 0) {
            boolean continueBuying = true;
            do {
                String[] propertyNames = new String[potentialProperties.size() + 1];
                for (int i = 0; i < propertyNames.length - 1; i++) {
                    propertyNames[i] = potentialProperties.get(i).getName();
                }
                propertyNames[propertyNames.length - 1] = "Stop med at bygge";
                String propertyString = gui.getUserSelection("On which property, do you wish to build houses?", propertyNames);

                if (!propertyString.equals("Stop med at bygge")) {
                    RealEstate chosenProperty = potentialProperties.get(0);
                    for (int i = 0; i < propertyNames.length - 1; i++) {
                        if (propertyNames[i].equals(propertyString)) chosenProperty = potentialProperties.get(i);
                    }
                    buyHouse(player, chosenProperty);
                } else {
                    continueBuying = false;
                }
            } while (continueBuying);
        } else {
            gui.showMessage(player.getName() + " kan ikke bygge huse endnu,\nda man først skal eje alle grunde af én farve.");
        }
        return true;
    }

    public boolean sellHouseAction() {
        String[] players = new String[game.getActivePlayers(false).size() + 1];
        for (int i = 0; i < players.length - 1; i++) {
            players[i] = game.getPlayers().get(i).getName();
        }
        players[players.length - 1] = "Annuller";

        String playerName = gui.getUserSelection("Hvilken spiller ønsker at sælge huse?", players);
        if (playerName.equals("Annuller")) return false;
        Player player = null;
        for (Player p : game.getPlayers()) {
            if (p.getName().equals(playerName)) {
                player = p;
            }
        }

        //Makes a list of all real estate, that has at least one house built on it.
        ArrayList<RealEstate> potentialProperties = new ArrayList<>();
        for (Property property : player.getOwnedProperties()) {
            if (property instanceof RealEstate) {
                if (((RealEstate) property).getHouseCount() > 0) {
                    potentialProperties.add((RealEstate) property);
                }
            }
        }

        if (potentialProperties.size() > 0) {
            boolean continueSelling = true;
            do {
                String[] propertyNames = new String[potentialProperties.size() + 1];
                for (int i = 0; i < propertyNames.length - 1; i++) {
                    propertyNames[i] = potentialProperties.get(i).getName();
                }
                propertyNames[propertyNames.length - 1] = "Stop med at sælge";
                String propertyString = gui.getUserSelection("Fra hvilken grund ønsker du at sælge huse?", propertyNames);

                if (!propertyString.equals("Stop med at sælge")) {
                    RealEstate chosenProperty = null;
                    for (int i = 0; i < propertyNames.length - 1; i++) {
                        if (propertyNames[i].equals(propertyString)) chosenProperty = potentialProperties.get(i);
                    }
                    sellHouse(player, chosenProperty);
                } else {
                    continueSelling = false;
                }
            } while (continueSelling);
        } else {
            gui.showMessage("Du kan ikke sælge huse endnu, da du ikke ejer nogen huse!");
        }
        return true;
    }

    /**
     * @Author Nicolai Wulff, s185036
     */
    public boolean trade() {

        //Make string array of all players that are not broke (they may be in jail).
        //Length is +1 because we need an option to cancel and return to menu.
        String[] playerOptions = new String[game.getActivePlayers(true).size() + 1];
        int i = 0;
        for (Player player : game.getActivePlayers(true)) {
            playerOptions[i] = player.getName();
            i++;
        }
        //Add option to cancel.
        playerOptions[i] = "Annuller";

        Boolean choosing = true;
        String player1name = null;
        String player2name = null;
        Player player1 = null;
        Player player2 = null;

        //Keep asking who the first player to trade is. Check if that player is in jail).
        while (choosing) {
            player1name = gui.getUserSelection("Hvem er den ene part i handlen?", playerOptions);
            if (player1name.equals("Annuller")) return false;
            for (Player player : game.getPlayers()) {
                if (player.getName().equals(player1name) && player.isInPrison()) {
                    gui.showMessage(player1name + " er i fængsel, og må derfor ikke handle!");
                } else if (player.getName().equals(player1name)){
                    player1 = player;
                    choosing = false;
                }
            }
        }

        //Construct new array of the active players, minus the one we just selected.
        playerOptions = new String[playerOptions.length - 1];
        i = 0;
        for (Player player : game.getActivePlayers(true)) {
            if (!player.getName().equals(player1name)) {
                playerOptions[i] = player.getName();
                i++;
            }
        }
        playerOptions[i] = "Annuller";

        //Keep asking who the second player to trade is. Check if that player is in jail).
        choosing = true;
        while(choosing) {
            player2name = gui.getUserSelection("Hvem er den anden part i handlen?", playerOptions);
            if (player2name.equals("Annuller")) return false;
            for (Player player : game.getPlayers()) {
                if (player.getName().equals(player2name) && player.isInPrison()) {
                    gui.showMessage(player2name + " er i fængsel, og må derfor ikke handle!");
                } else if (player.getName().equals(player2name)){
                    player2 = player;
                    choosing = false;
                }
            }
        }

        //Define the players from the strings of playernames, we just selected.
        //TODO: Each player must have a unique name, otherwise it won't work.
        for (Player player : game.getPlayers()) {
            if (player.getName().equals(player1name)) player1 = player;
            if (player.getName().equals(player2name)) player2 = player;
        }

        Player[] tradingPlayers = {player1, player2};
        int[] moneyInOffers = new int[2];
        ArrayList<String>[] propertiesInOffersString = new ArrayList[2];
        ArrayList<Property>[] propertiesInOffers = new ArrayList[2];
        propertiesInOffersString[0] = new ArrayList<>();
        propertiesInOffersString[1] = new ArrayList<>();
        propertiesInOffers[0] = new ArrayList<>();
        propertiesInOffers[1] = new ArrayList<>();

        //Loop through the two trading players.
        for (int j = 0; j < 2; j++) {
            //Ask what amount of money, the player wants to trade.
            moneyInOffers[j] = gui.getUserInteger(tradingPlayers[j].getName() + ", hvilket beløb vil du tilføje i handlen?", 0, tradingPlayers[j].getBalance());

            //Construct an array of strings of all the properties, the player owns.
            //The length is +1, because we need an option to stop adding properties.
            int numberOfoptions = tradingPlayers[j].getOwnedProperties().size() + 1;
            String[] propertyOptions = new String[numberOfoptions];
            i = 0;
            for (Property property : tradingPlayers[j].getOwnedProperties()) {
                propertyOptions[i] = property.getName();
                i++;
            }
            //Add option to stop adding properties.
            propertyOptions[i] = "Stop med at tilføje grunde";

            //Keep adding properties to the trade, until the player chooses to stop.
            boolean continueAdding = true;
            while(continueAdding) {
                String propertyToOffer = gui.getUserSelection(tradingPlayers[j].getName() + ", hvilke grunde vil du tilføje i handlen?", propertyOptions);
                if (propertyToOffer.equals("Stop med at tilføje grunde")) {
                    continueAdding = false;
                } else {

                    //Define the chosen property by finding the property with that name.
                    Property chosenProperty = null;
                    for (Property property : tradingPlayers[j].getOwnedProperties()) {
                        if (propertyToOffer.equals(property.getName())) {
                            chosenProperty = property;
                        }
                    }

                    //Check if the chosen property or any property with the same color group has any houses.
                    //If they do, refuse to trade it, and tell the player to sell their houses first.
                    for (Property property : tradingPlayers[j].getOwnedProperties()) {
                        if (property instanceof RealEstate && property.getColorGroup() == chosenProperty.getColorGroup()) {
                            if (((RealEstate) property).getHouseCount() > 0) {
                                gui.showMessage("Du kan ikke handle med grunde, hvor du har bygget huse i dens farvegruppe.\nSælg din huse i gruppen, før du handler.");
                                return false;
                            }
                        }
                    }
                    propertiesInOffers[j].add(chosenProperty);

                    //Add the added property to the list of strings of properties added to the trade.
                    propertiesInOffersString[j].add(propertyToOffer);

                    //Construct a new array, containing the remaining properties.
                    numberOfoptions--;
                    propertyOptions = new String[numberOfoptions];
                    i = 0;
                    for (Property property : tradingPlayers[j].getOwnedProperties()) {
                        boolean alreadyAdded = false;
                        for (Property p : propertiesInOffers[j]) {
                            if (property == p) alreadyAdded = true;
                        }
                        if (!alreadyAdded && !property.getName().equals(propertyToOffer)) {
                            propertyOptions[i] = property.getName();
                            i++;
                        }
                    }
                    //Add the option to stop.
                    propertyOptions[propertyOptions.length - 1] = "Stop med at tilføje grunde";
                }
            }
        }

        //Construct a string for each player, containing all the properties,
        // that each player added to the trade, seperated by commas.
        String[] playerProperties = {"", ""};
        for (i = 0; i < 2; i++) {
            for (String property : propertiesInOffersString[i]) {
                playerProperties[i] += property;
                if (!property.equals(propertiesInOffersString[i].get(propertiesInOffersString[i].size() - 1))) {
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
                payment(player1, moneyInOffers[0], player2);
                payment(player2, moneyInOffers[1], player1);

                for (Property property : propertiesInOffers[0]) {
                    transferProperty(player1, property, player2);
                }
                for (Property property : propertiesInOffers[1]) {
                    transferProperty(player2, property, player1);
                }

            } catch (PlayerBrokeException e) {
                e.printStackTrace();
            } catch (GameEndedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Transfers a property from one player (giver) to another (receiver)
     * @param giver
     * @param property
     * @param receiver
     * @Author Nicolai Wulff, s185036
     */
    public void transferProperty(Player giver, Property property, Player receiver) {
        property.setOwner(null);
        giver.removeOwnedProperty(property);
        property.setOwner(receiver);
        receiver.addOwnedProperty(property);
    }

    public boolean mortgageAction() {
        String[] players = new String[game.getActivePlayers(false).size() + 1];
        for (int i = 0; i < players.length - 1; i++) {
            players[i] = game.getPlayers().get(i).getName();
        }
        players[players.length - 1] = "Annuller";

        String playerName = gui.getUserSelection("Hvilken spiller ønsker at pantsætte grunde?", players);
        if (playerName.equals("Annuller")) return false;
        Player player = null;
        for (Player p : game.getPlayers()) {
            if (p.getName().equals(playerName)) {
                player = p;
            }
        }

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
                return false;
            }

            String[] properties = new String[potentialProperties.size() + 1];
            for (int i = 0; i < properties.length - 1; i++) {
                properties[i] = potentialProperties.get(i).getName();
            }
            properties[properties.length - 1] = "Stop med at pantsætte";

            String choice = gui.getUserSelection("Hvilken grund ønsker du at pantsætte?", properties);
            if (choice.equals("Stop med at pantsætte")) {
                continuePawning = false;
            } else {
                for (Property property : player.getOwnedProperties()) {
                    if (property.getName().equals(choice)) {
                        if (property instanceof RealEstate) {
                            boolean ableToMortgage = true;
                            for (Property p : player.getOwnedProperties()) {
                                if (p.getColorGroup() == property.getColorGroup() && ((RealEstate)p).getHouseCount() > 0) {
                                    ableToMortgage = false;
                                }
                            }

                            if (ableToMortgage) {
                                gui.showMessage("Du skal sælge alle huse i farvegruppen, før du kan pantsætte!");
                            } else {
                                mortgage(player, property);
                            }
                        } else {
                            mortgage(player, property);
                        }
                    }
                }
            }
        }
        return true;
    }

    public void mortgage(Player player, Property property) {
        property.setMortgaged(true);
        paymentFromBank(player, property.getCost() / 2);
    }

    public boolean unmortgageAction() {
        String[] players = new String[game.getActivePlayers(false).size() + 1];
        for (int i = 0; i < players.length - 1; i++) {
            players[i] = game.getPlayers().get(i).getName();
        }
        players[players.length - 1] = "Annuller";

        String playerName = gui.getUserSelection("Hvilken spiller ønsker at indfri sin gæld i pantsættelser?", players);
        if (playerName.equals("Annuller")) return false;
        Player player = null;
        for (Player p : game.getPlayers()) {
            if (p.getName().equals(playerName)) {
                player = p;
            }
        }

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
                return false;
            }

            String[] properties = new String[potentialProperties.size() + 1];
            for (int i = 0; i < properties.length - 1; i++) {
                properties[i] = potentialProperties.get(i).getName();
            }
            properties[properties.length - 1] = "Tilbage til hovedmenuen";

            String choice = gui.getUserSelection("Hvilken pantsat grund vil du tilbagebetale?", properties);
            if (choice.equals("Tilbage til hovedmenuen")) {
                continuePawning = false;
            } else {
                for (Property property : player.getOwnedProperties()) {
                    if (property.getName().equals(choice)) {
                        unmortgage(player, property);
                    }
                }
            }
        }
        return true;
    }

    public void unmortgage(Player player, Property property) {
        try {
            paymentToBank(player, property.getCost() / 2);
            property.setMortgaged(false);
        } catch (PlayerBrokeException e) {
            e.printStackTrace();
        } catch (GameEndedException e) {
            e.printStackTrace();
        }
    }

    public void showMessage(String message) {
        gui.showMessage(message);
    }
}