package game.view;

import designpattern.Observer;
import designpattern.Subject;
import game.model.*;
import game.model.properties.Brewery;
import game.model.properties.RealEstate;
import gui_fields.*;
import gui_main.GUI;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements a view on the Monopoly game based
 * on the original Matador GUI; it serves as a kind of
 * adapter to the Matador GUI. This class realizes the
 * MVC-principle on top of the Matador GUI. In particular,
 * the view implements an observer of the model in the
 * sense of the MVC-principle, which updates the GUI when
 * the state of the game (model) changes.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * Sørg for at udvide klassen View, så at dens metode
 */
public class View implements Observer, Runnable {

    private Game game;
    private GUI gui;

    private Map<Player, GUI_Player> player2GuiPlayer = new HashMap<Player, GUI_Player>();
    private Map<Player, Integer> player2position = new HashMap<Player, Integer>();
    private Map<Space, GUI_Field> space2GuiField = new HashMap<Space, GUI_Field>();
    private Map<Player, PlayerPanel> player2PlayerPanel = new HashMap<Player, PlayerPanel>();
    private boolean disposed = false;

    /**
     * Constructor for the view of a game based on a game and an already
     * running Matador GUI.
     *
     * @param game the game
     * @param gui  the GUI
     */
    public View(Game game, GUI gui) {
        this.game = game;
        this.gui = gui;

        GUI_Field[] guiFields = gui.getFields();

        int i = 0;
        for (Space space : game.getSpaces()) {
            // TODO, here we assume that the games fields fit to the GUI's fields;
            // the GUI fields should actually be created according to the game's
            // fields
            space2GuiField.put(space, guiFields[i]);
            space.attach(this);
            i++;
        }
    }

    @Override
    public void update(Subject subject) {
        if (!disposed) {

            if (subject instanceof Player) {
                updatePlayer((Player) subject);
            }
            if (subject instanceof Property) {
                updateProperty((Property) subject);
            }
            if (subject instanceof Game) {
                createPlayers();
            }
        }
    }

    public void updateProperty(Property property) {
        GUI_Field gui_field = this.space2GuiField.get(property);
        if (gui_field instanceof GUI_Ownable) {
            GUI_Ownable gui_ownable = (GUI_Ownable) gui_field;
            Player owner = property.getOwner();
            if (owner != null) {
                gui_ownable.setBorder(owner.getColor());
                gui_ownable.setOwnerName(owner.getName());
            } else {
                gui_ownable.setBorder(null);
                gui_ownable.setOwnerName(null);
            }

            gui_ownable.setRent(property.getRent() + "");
        }
        if (property instanceof RealEstate) {
            GUI_Street street = (GUI_Street) gui_field;
            street.setHotel(false);
            if (((RealEstate) property).getHouseCount() > 0 && ((RealEstate) property).getHouseCount() < 5) {
                street.setHouses(((RealEstate) property).getHouseCount());
            } else if (((RealEstate) property).getHouseCount() == 5) {
                street.setHotel(true);
            }
            //street.setRent(property.getRent() + "");
        }

        if (property instanceof Brewery) {
            ((GUI_Ownable) gui_field).setRent(property.getRent() + " gange øjnene.");
        }

        if (property.getOwner() != null) {
            player2PlayerPanel.get(property.getOwner()).update();
        }
    }

    /**
     * This method updates a player's state in the GUI. Right now, this
     * concerns the players position and balance only. But, this should
     * also include other information (being i prison, available cards,
     * ...)
     *
     * @param player the player who's state is to be updated
     */
    private void updatePlayer(Player player) {
        GUI_Player guiPlayer = this.player2GuiPlayer.get(player);
        if (guiPlayer != null) {
            guiPlayer.setBalance(player.getBalance());

            GUI_Field[] guiFields = gui.getFields();
            Integer oldPosition = player2position.get(player);
            int pos = player.getCurrentPosition().getIndex();

            if (oldPosition != null && oldPosition < guiFields.length) {

                int moves = calcNumOfMoves(player);
                for (int i = 1; i <= moves; i++) {
                    run();
                    guiFields[((oldPosition + i - 1) % 40)].setCar(player2GuiPlayer.get(player), false);
                    player2position.put(player, pos);
                    guiFields[((oldPosition + i) % 40)].setCar(player2GuiPlayer.get(player), true);
                }
            }
            String name = player.getName();
            if (player.isBroke()) {
                guiPlayer.setName(player.getName() + " (konkurs)");
            } else if (player.isInPrison()) {
                guiPlayer.setName(player.getName() + " (i fængsel)");
            } else if (!name.equals(guiPlayer.getName())) {
                guiPlayer.setName(name);
            }
            player2PlayerPanel.get(player).update();
        }
    }

    /**
     * @param player
     * @return
     * @author Nicolai L
     */
    public int calcNumOfMoves(Player player) {
        Integer oldPos = player2position.get(player);
        if (oldPos == null) {
            oldPos = 0;
        }
        int curPos = player.getCurrentPosition().getIndex();
        int moves;
        if (curPos > oldPos) {
            moves = curPos - oldPos;
        } else {
            try {
                //moves = (oldPos - curPos) % 4     0;
                moves = (curPos - oldPos + 40) % 40;
            } catch (ArithmeticException e) {
                moves = 0;
            }
        }
        return moves;
    }

    public void dispose() {
        if (!disposed) {
            disposed = true;
            for (Player player : game.getPlayers()) {
                // unregister from the player as observer
                player.detach(this);
            }
            for (Space space : game.getSpaces()) {
                space.detach(this);
            }
            /*
            for (GUI_Player guiPlayer : player2GuiPlayer.values()) {
                gui.getFields()[0].setCar(guiPlayer, false);
            }*/
        }
    }

    /**
     * Nicolai L
     */
    public void createPlayers() {
        for (Player player : game.getPlayers()) {
            if (player.getColorEnumType() != null) player.setColor(transformPlayerColor(player.getColorEnumType()));
            GUI_Car car = new GUI_Car(player.getColor(), Color.BLUE, transformCarType(player.getCarType()), GUI_Car.Pattern.FILL);
            GUI_Player guiPlayer = new GUI_Player(player.getName(), player.getBalance(), car);
            player2GuiPlayer.put(player, guiPlayer);
            gui.addPlayer(guiPlayer);

            if (player.getCurrentPosition() != null) {
                player2position.put(player, player.getCurrentPosition().getIndex());
            } else {
                player2position.put(player, 0);
            }

            PlayerPanel playerPanel = new PlayerPanel(game, player, gui);
            player2PlayerPanel.put(player, playerPanel);
            space2GuiField.get(player.getCurrentPosition()).setCar(player2GuiPlayer.get(player), true);
            player.attach(this);
            //updatePlayer(player);
        }

        /*
        TokenColor tokenColor = new TokenColor();
        for (Player player : game.getPlayers()) {
            enterNamePlayer(player);
            Color userColor = chooseCarColor(tokenColor, player);
            player.setColor(userColor);
            GUI_Car car = choosePlayerCar(player);
            GUI_Player guiPlayer = new GUI_Player(player.getName(), player.getBalance(), car);
            player2GuiPlayer.put(player, guiPlayer);
            gui.addPlayer(guiPlayer);
            player2position.put(player, 0);
            PlayerPanel playerPanel = new PlayerPanel(game, player, gui);
            player2PlayerPanel.put(player, playerPanel);
            space2GuiField.get(player.getCurrentPosition()).setCar(player2GuiPlayer.get(player), true);
            player.attach(this);
            updatePlayer(player);
        }
        */
    }

    /**
     * @author Jeppe s170196, Nicolai s185020, Nicolai W s185036
     */
    /*
    public void loadPlayers() {
        for (Player player : game.getPlayers()) {
            GUI_Car car = new GUI_Car(player.getColor(), Color.black, Type.valueOf(player.getToken()), Pattern.FILL);
            GUI_Player guiPlayer = new GUI_Player(player.getName(), player.getBalance(), car);
            player2GuiPlayer.put(player, guiPlayer);
            gui.addPlayer(guiPlayer);
            player2position.put(player, player.getCurrentPosition().getIndex());
            space2GuiField.get(player.getCurrentPosition()).setCar(player2GuiPlayer.get(player), true);
            PlayerPanel playerPanel = new PlayerPanel(game, player, gui);
            player2PlayerPanel.put(player, playerPanel);
            player.attach(this);
            updatePlayer(player);
            for (Property p : player.getOwnedProperties()) {
                updateProperty(p);
            }
        }
    }
/*
    /**
     * Nicolai L
     *
     * @param player
     *//*
    public void enterNamePlayer(Player player) {
        while (true) {
            String input = gui.getUserString("indtast navn");
            if (input.length() > 0) {
                player.setName(input);
                break;
            } else {
/*todo ret dette
                gui.showMessage("prøv igen");

                break;
            }
        }
    }

    /**
     * Nicolai L
     *
     * @param player
     * @return
     */
    /*
    public GUI_Car choosePlayerCar(Player player) {
        String carChoice = gui.getUserSelection("Choose car", "Car", "Ufo", "Tractor", "Racecar");
        GUI_Car car;
        HashMap<String, GUI_Car.Type> enumMap = new HashMap();
        enumMap.put("Ufo", UFO);
        enumMap.put("Car", CAR);
        enumMap.put("Tractor", TRACTOR);
        enumMap.put("Racecar", RACECAR);
        car = new GUI_Car(player.getColor(), Color.BLUE, enumMap.get(carChoice), GUI_Car.Pattern.FILL);
        player.setToken(enumMap.get(carChoice).toString());
        return car;
    }
*/
    /*
    /**
     * Nicolai L
     *
     * @param tokenColorObj
     * @param player
     * @return
     */

    /*
    public Color chooseCarColor(TokenColor tokenColorObj, Player player) {
        String[] chooseColorStrings = tokenColorObj.setColorsToChooseFrom().split(" ");
        String carColorS;

        for (int i = 0; i < chooseColorStrings.length; i++) {
        }
        if (chooseColorStrings.length == 6) {
            carColorS = gui.getUserSelection("Choose color", chooseColorStrings[0], chooseColorStrings[1], chooseColorStrings[2], chooseColorStrings[3], chooseColorStrings[4], chooseColorStrings[5]);
        } else if (chooseColorStrings.length == 5) {
            carColorS = gui.getUserSelection("Choose color", chooseColorStrings[0], chooseColorStrings[1], chooseColorStrings[2], chooseColorStrings[3], chooseColorStrings[4]);
        } else if (chooseColorStrings.length == 4) {
            carColorS = gui.getUserSelection("Choose color", chooseColorStrings[0], chooseColorStrings[1], chooseColorStrings[2], chooseColorStrings[3]);
        } else if (chooseColorStrings.length == 3) {
            carColorS = gui.getUserSelection("Choose color", chooseColorStrings[0], chooseColorStrings[1], chooseColorStrings[2]);
        } else if (chooseColorStrings.length == 2) {
            carColorS = gui.getUserSelection("Choose color", chooseColorStrings[0], chooseColorStrings[1]);
        } else {
            gui.showMessage(player.getName() + "'s color is " + chooseColorStrings[0]);
            carColorS = chooseColorStrings[0];
        }
        Color carColorChoice = tokenColorObj.colorChosen(carColorS);

        return carColorChoice;
    }
    */

    @Override
    public void run() {
        try {
            Thread.sleep(150);
        } catch (Exception e) {
        }
    }

    private Color transformPlayerColor(Player.PlayerColor playerColor) {
        switch (playerColor) {
            case GREY:
                return Color.DARK_GRAY;
            case GREEN:
                return Color.GREEN;
            case BlUE:
                return Color.BLUE.brighter();
            case MAGENTA:
                return Color.MAGENTA;
            case RED:
                return Color.RED;
            case YELLOW:
                return Color.yellow.brighter();
            default: return null;
        }
    }

    private GUI_Car.Type transformCarType(Player.CarType carType) {
        switch (carType) {
            case CAR: return GUI_Car.Type.CAR;
            case RACECAR: return GUI_Car.Type.RACECAR;
            case TRACTOR: return GUI_Car.Type.TRACTOR;
            case UFO: return GUI_Car.Type.UFO;
            default: return null;
        }
    }
}
