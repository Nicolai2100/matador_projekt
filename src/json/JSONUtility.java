package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import game.model.*;
import game.model.cards.CardMove;
import game.model.cards.CardReceiveMoneyFromBank;
import game.model.properties.Brewery;
import game.model.properties.RealEstate;
import game.model.properties.Ship;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Main-method creates data for JSON-file,
 * createGame() loads game configuration from said file
 *
 * @author s185039
 */

public class JSONUtility {
    public void createData() throws IOException {

        //creating data for the game's spaces
        Game game = new Game();

        Space go = new NeutralSpace();
        go.setName("Start");
        game.addSpace(go);

        RealEstate roedovrevej = new RealEstate();
        roedovrevej.setName("Rødovrevej");
        roedovrevej.setCost(1200);
        int[] rentLevels = {50, 250, 750, 2250, 4000, 6000};
        roedovrevej.setRentLevels(rentLevels);
        roedovrevej.setColorGroup(ColorGroup.lightblue);
        roedovrevej.setPriceForHouse(1000);
        game.addSpace(roedovrevej);

        Space chance = new Chance(); //colorgroup?
        chance.setName("chance");
        game.addSpace(chance);

        RealEstate hvidovrevej = new RealEstate();
        hvidovrevej.setName("Hvidovrevej");
        hvidovrevej.setCost(1200);
        rentLevels = new int[]{50, 250, 750, 2250, 4000, 6000};
        hvidovrevej.setRentLevels(rentLevels);
        hvidovrevej.setColorGroup(ColorGroup.lightblue);
        hvidovrevej.setPriceForHouse(1000);
        game.addSpace(hvidovrevej);

        Space indkomstskat = new Tax();
        indkomstskat.setName("Indkomstskat");
        game.addSpace(indkomstskat);

        Ship oeresund = new Ship();
        //TODO ændr alle utilities til enten Ship eller Brewery (som kan have begyndelsespris sat fra start)
        oeresund.setName("Øresund");
        oeresund.setCost(4000);
        rentLevels = new int[]{500, 1000, 2000, 4000};
        oeresund.setRentLevels(rentLevels);
        oeresund.setColorGroup(ColorGroup.navy);
        game.addSpace(oeresund);

        RealEstate roskildevej = new RealEstate();
        roskildevej.setName("Roskildevej");
        roskildevej.setCost(2000);
        rentLevels = new int[]{100, 600, 1800, 5400, 8000, 11000};
        roskildevej.setRentLevels(rentLevels);
        roskildevej.setColorGroup(ColorGroup.pink);
        roskildevej.setPriceForHouse(1000);
        game.addSpace(roskildevej);

        chance = new Chance();
        chance.setName("chance");
        game.addSpace(chance); //virker dette?

        RealEstate valbyLanggade = new RealEstate();
        valbyLanggade.setName("Valby Langgade");
        valbyLanggade.setCost(2000);
        rentLevels = new int[]{100, 600, 1800, 5400, 8000, 11000};
        valbyLanggade.setRentLevels(rentLevels);
        valbyLanggade.setColorGroup(ColorGroup.pink);
        valbyLanggade.setPriceForHouse(1000);
        game.addSpace(valbyLanggade);

        RealEstate allegade = new RealEstate();
        allegade.setName("Allégade");
        allegade.setCost(2400);
        rentLevels = new int[]{150, 800, 2000, 6000, 9000, 12000};
        allegade.setRentLevels(rentLevels);
        allegade.setColorGroup(ColorGroup.pink);
        allegade.setPriceForHouse(1000);
        game.addSpace(allegade);

        Space faengsel = new NeutralSpace();
        faengsel.setName("I fængsel/På besøg");
        game.addSpace(faengsel);

        RealEstate frederiksbergAlle = new RealEstate();
        frederiksbergAlle.setName("Fredriksberg Allé");
        frederiksbergAlle.setCost(2800);
        rentLevels = new int[]{200, 1000, 3000, 9000, 12500, 15000};
        frederiksbergAlle.setRentLevels(rentLevels);
        frederiksbergAlle.setColorGroup(ColorGroup.green);
        frederiksbergAlle.setPriceForHouse(2000);
        game.addSpace(frederiksbergAlle);

        Brewery tuborg = new Brewery();
        tuborg.setName("Tuborg");
        tuborg.setCost(3000);
        rentLevels = new int[]{100, 200};
        tuborg.setRentLevels(rentLevels);
        tuborg.setColorGroup(ColorGroup.darkgreen);
        game.addSpace(tuborg);

        RealEstate bulowsvej = new RealEstate();
        bulowsvej.setName("Bülowsvej");
        bulowsvej.setCost(2800);
        rentLevels = new int[]{200, 1000, 3000, 9000, 12500, 15000};
        bulowsvej.setRentLevels(rentLevels);
        bulowsvej.setColorGroup(ColorGroup.green);
        bulowsvej.setPriceForHouse(2000);
        game.addSpace(bulowsvej);

        RealEstate gammelkongevej = new RealEstate();
        gammelkongevej.setName("Gammelkongevej");
        gammelkongevej.setCost(3200);
        rentLevels = new int[]{250, 1250, 3750, 10000, 14000, 18000};
        gammelkongevej.setRentLevels(rentLevels);
        gammelkongevej.setColorGroup(ColorGroup.green);
        gammelkongevej.setPriceForHouse(2000);
        game.addSpace(gammelkongevej);

        Ship dfds = new Ship();
        dfds.setName("D.F.D.S");
        dfds.setCost(4000);
        rentLevels = new int[]{500, 1000, 2000, 4000};
        dfds.setRentLevels(rentLevels);
        dfds.setColorGroup(ColorGroup.navy);
        game.addSpace(dfds);

        RealEstate bernstorffsvej = new RealEstate();
        bernstorffsvej.setName("Bernstorffsvej");
        bernstorffsvej.setCost(3600);
        rentLevels = new int[]{300, 1400, 4000, 11000, 15000, 19000};
        bernstorffsvej.setRentLevels(rentLevels);
        bernstorffsvej.setColorGroup(ColorGroup.darkgrey);
        bernstorffsvej.setPriceForHouse(2000);
        game.addSpace(bernstorffsvej);

        chance = new Chance();
        chance.setName("chance");
        game.addSpace(chance);

        RealEstate hellerupvej = new RealEstate();
        hellerupvej.setName("Hellerupvej");
        hellerupvej.setCost(3600);
        rentLevels = new int[]{300, 1400, 4000, 11000, 15000, 19000};
        hellerupvej.setRentLevels(rentLevels);
        hellerupvej.setColorGroup(ColorGroup.darkgrey);
        hellerupvej.setPriceForHouse(2000);
        game.addSpace(hellerupvej);

        RealEstate strandvej = new RealEstate();
        strandvej.setName("Strandvej");
        strandvej.setCost(4000);
        rentLevels = new int[]{350, 1600, 4400, 12000, 16000, 20000};
        strandvej.setRentLevels(rentLevels);
        strandvej.setColorGroup(ColorGroup.darkgrey);
        strandvej.setPriceForHouse(2000);
        game.addSpace(strandvej);

        Space parkering = new NeutralSpace();
        parkering.setName("Helle");
        game.addSpace(parkering);

        RealEstate trianglen = new RealEstate();
        trianglen.setName("Trianglen");
        trianglen.setCost(4400);
        rentLevels = new int[]{350, 1800, 5000, 14000, 17500, 21000};
        trianglen.setRentLevels(rentLevels);
        trianglen.setColorGroup(ColorGroup.red);
        trianglen.setPriceForHouse(3000);
        game.addSpace(trianglen);

        chance = new Chance();
        chance.setName("chance");
        game.addSpace(chance);

        RealEstate oesterbrogade = new RealEstate();
        oesterbrogade.setName("Østerbrogade");
        oesterbrogade.setCost(4400);
        rentLevels = new int[]{350, 1800, 5000, 14000, 17500, 21000};
        oesterbrogade.setRentLevels(rentLevels);
        oesterbrogade.setColorGroup(ColorGroup.red);
        oesterbrogade.setPriceForHouse(3000);
        game.addSpace(oesterbrogade);

        RealEstate gronningen = new RealEstate();
        gronningen.setName("Grønningen");
        gronningen.setCost(4800);
        rentLevels = new int[]{400, 2000, 6000, 15000, 18500, 22000};
        gronningen.setRentLevels(rentLevels);
        gronningen.setColorGroup(ColorGroup.red);
        gronningen.setPriceForHouse(3000);
        game.addSpace(gronningen);

        Ship oes = new Ship();
        oes.setName("Ø.S.");
        oes.setCost(4000);
        rentLevels = new int[]{500, 1000, 2000, 4000};
        oes.setRentLevels(rentLevels);
        oes.setColorGroup(ColorGroup.navy);
        game.addSpace(oes);

        RealEstate bredgade = new RealEstate();
        bredgade.setName("Bredgade");
        bredgade.setCost(5200);
        rentLevels = new int[]{450, 2200, 6600, 16000, 19500, 23000};
        bredgade.setRentLevels(rentLevels);
        bredgade.setColorGroup(ColorGroup.white);
        bredgade.setPriceForHouse(3000);
        game.addSpace(bredgade);

        RealEstate kgsnytorv = new RealEstate();
        kgsnytorv.setName("Kgs. Nytorv");
        kgsnytorv.setCost(5200);
        rentLevels = new int[]{450, 2200, 6600, 16000, 19500, 23000};
        kgsnytorv.setRentLevels(rentLevels);
        kgsnytorv.setColorGroup(ColorGroup.white);
        kgsnytorv.setPriceForHouse(3000);
        game.addSpace(kgsnytorv);

        Brewery carlsberg = new Brewery();
        carlsberg.setName("Carlsberg");
        carlsberg.setCost(3000);
        rentLevels = new int[]{100, 200};
        carlsberg.setRentLevels(rentLevels);
        carlsberg.setColorGroup(ColorGroup.darkgreen);
        game.addSpace(carlsberg);

        RealEstate oestergade = new RealEstate();
        oestergade.setName("Østergade");
        oestergade.setCost(5600);
        rentLevels = new int[]{500, 2400, 7200, 17000, 20500, 24000};
        oestergade.setRentLevels(rentLevels);
        oestergade.setColorGroup(ColorGroup.white);
        oestergade.setPriceForHouse(3000);
        game.addSpace(oestergade);

        Space ifaengsel = new GoToJail();
        ifaengsel.setName("Gå i fængsel");
        game.addSpace(ifaengsel);

        RealEstate amagertorv = new RealEstate();
        amagertorv.setName("Amagertorv");
        amagertorv.setCost(6000);
        rentLevels = new int[]{550, 2600, 7800, 18000, 22000, 25000};
        amagertorv.setRentLevels(rentLevels);
        amagertorv.setColorGroup(ColorGroup.yellow);
        amagertorv.setPriceForHouse(4000);
        game.addSpace(amagertorv);

        RealEstate vimmelskaftet = new RealEstate();
        vimmelskaftet.setName("Vimmelskaftet");
        vimmelskaftet.setCost(6000);
        rentLevels = new int[]{550, 2600, 7800, 18000, 22000, 25000};
        vimmelskaftet.setRentLevels(rentLevels);
        vimmelskaftet.setColorGroup(ColorGroup.yellow);
        vimmelskaftet.setPriceForHouse(4000);
        game.addSpace(vimmelskaftet);

        chance = new Chance();
        chance.setName("chance");
        game.addSpace(chance);

        RealEstate nygade = new RealEstate();
        nygade.setName("Nygade");
        nygade.setCost(6400);
        rentLevels = new int[]{600, 3000, 9000, 20000, 24000, 28000};
        nygade.setRentLevels(rentLevels);
        nygade.setPriceForHouse(4000);
        nygade.setColorGroup(ColorGroup.yellow);
        game.addSpace(nygade);

        Ship bornholm = new Ship();
        bornholm.setName("Bornholm");
        bornholm.setCost(4000);
        rentLevels = new int[]{500, 1000, 2000, 4000};
        bornholm.setRentLevels(rentLevels);
        bornholm.setColorGroup(ColorGroup.navy);
        game.addSpace(bornholm);

        chance = new Chance();
        chance.setName("chance");
        game.addSpace(chance);

        RealEstate frederiksberggade = new RealEstate();
        frederiksberggade.setName("Frederiksberggade");
        frederiksberggade.setCost(7000);
        rentLevels = new int[]{700, 3500, 10000, 22000, 26000, 30000};
        frederiksberggade.setRentLevels(rentLevels);
        frederiksberggade.setColorGroup(ColorGroup.purple);
        frederiksberggade.setPriceForHouse(4000);
        game.addSpace(frederiksberggade);

        Space ekstraskat = new Tax();
        ekstraskat.setName("Ekstraordinær statsskat");
        game.addSpace(ekstraskat);

        RealEstate raadhuspladsen = new RealEstate();
        raadhuspladsen.setName("Rådhuspladsen");
        raadhuspladsen.setCost(8000);
        rentLevels = new int[]{1000, 4000, 12000, 28000, 34000, 40000};
        raadhuspladsen.setRentLevels(rentLevels);
        raadhuspladsen.setColorGroup(ColorGroup.purple);
        raadhuspladsen.setPriceForHouse(4000);
        game.addSpace(raadhuspladsen);

        ArrayList<Card> cardList = new ArrayList<>();

        Card card = new CardReceiveMoneyFromBank();
        card.setText("Deres premieobligation er udtrukket. De modtager kr. 1000 af banken.");
        ((CardReceiveMoneyFromBank) card).setAmount(1000);
        cardList.add(card);

        card = new CardReceiveMoneyFromBank();
        card.setText("Værdien af egen avl fra nyttehaven udgør kr. 200, som De modtager af banken.");
        ((CardReceiveMoneyFromBank) card).setAmount(200);
        cardList.add(card);

        //2
        card = new CardReceiveMoneyFromBank();
        card.setText("Grundet dyrtiden har De fået gageforhøjelse. Modtag kr. 1000.");
        ((CardReceiveMoneyFromBank) card).setAmount(1000);
        cardList.add(card);

        //2
        card = new CardReceiveMoneyFromBank();
        card.setText("De modtager Deres aktieudbytte. Modtag kr. 1000 af banken.");
        ((CardReceiveMoneyFromBank) card).setAmount(1000);
        cardList.add(card);

        card = new CardReceiveMoneyFromBank();
        card.setText("De har solgt nogle gamle møbler på auktion. Modtag kr. 1000  af banken.");
        ((CardReceiveMoneyFromBank) card).setAmount(1000);
        cardList.add(card);

        //2
        card = new CardReceiveMoneyFromBank();
        card.setText("De har vundet i Klasselotterliet. Modtag kr. 500.");
        ((CardReceiveMoneyFromBank) card).setAmount(500);
        cardList.add(card);

        card = new CardReceiveMoneyFromBank();
        card.setText("Kommunen har eftergivet et kvartals skat. Hæv i banken kr. 3000.");
        ((CardReceiveMoneyFromBank) card).setAmount(3000);
        cardList.add(card);

        card = new CardReceiveMoneyFromBank();
        card.setText("De har en række med elleve rigtige i tipning. Modtag kr. 1000.");
        ((CardReceiveMoneyFromBank) card).setAmount(1000);
        cardList.add(card);

        card = new CardMove();
        card.setText("Ryk brikken frem til det nærmeste rederi og betal ejeren to gange den leje, han ellers er berettiget til." +
                " Hvis selskabet ikke ejes af nogen, kan De købe det af banken.");
        ((CardMove) card).setSpecialTarget(CardMove.SpecialTargets.NEAREST_SHIP_1, game.getSpaces());
        cardList.add(card);

        card = new CardMove();
        card.setText("Tag med den nærmeste færge. Flyt brikken frem, og hvis De passerer \"START\", indkassér da kr. 4000.");
        ((CardMove) card).setSpecialTarget(CardMove.SpecialTargets.NEAREST_SHIP_2, game.getSpaces());
        cardList.add(card);

        card = new CardMove();
        card.setText("Gå i fængsel. Ryk direkte til fængslet. Selv om De passerer \"START\", indkasserer De ikke kr. 4000.");
        ((CardMove) card).setSpecialTarget(CardMove.SpecialTargets.GO_TO_JAIL);
        cardList.add(card);

        card = new CardMove();
        card.setText("Ryk frem til " + game.getSpaces().get(32) + ". Hvis De passerer \"START\", indkassér da kr. 4000.");
        ((CardMove) card).setTarget(game.getSpaces().get(32));
        cardList.add(card);

        card = new CardMove();
        card.setText("Ryk frem til " + game.getSpaces().get(19) + ". Hvis De passerer \"START\", indkassér da kr. 4000.");
        ((CardMove) card).setTarget(game.getSpaces().get(19));
        cardList.add(card);

        card = new CardMove();
        card.setText("Ryk frem til " + game.getSpaces().get(24) + ". Hvis De passerer \"START\", indkassér da kr. 4000.");
        ((CardMove) card).setTarget(game.getSpaces().get(24));
        cardList.add(card);

        card = new CardMove();
        card.setText("Tag med " + game.getSpaces().get(15) + ". Flyt brikken frem, og hvis De passerer \"START\", indkassér da kr. 4000.");
        ((CardMove) card).setTarget(game.getSpaces().get(15));
        cardList.add(card);

        card = new CardMove();
        card.setText("Ryk frem til \"START\".");
        ((CardMove) card).setTarget(game.getSpaces().get(0));
        cardList.add(card);

        card = new CardMove();
        card.setText("Ryk tre felter frem.");
        ((CardMove) card).setSpecialTarget(CardMove.SpecialTargets.THREE_FORWARDS);
        cardList.add(card);

        card = new CardMove();
        card.setText("Ryk tre felter tilbage.");
        ((CardMove) card).setSpecialTarget(CardMove.SpecialTargets.THREE_BACKWARDS);
        cardList.add(card);

        game.setCardDeck(cardList);


        //JSON-GSON operations
        Path path = Paths.get("src/resources");
        //Files.createDirectory(path);

        FileWriter fileWriter = new FileWriter(path + "/game.json");
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Space.class, new Adapter<Space>());

        Gson gson = builder.create();

        JsonWriter writer = gson.newJsonWriter(fileWriter);

        gson.toJson(game, game.getClass(), writer);

        writer.close();

    }

    /**
     * Load a Game-object from a corresponding JSON-file.
     * @return Fully configured Game-object. Should return an empty game-object if something goes wrong.
     */
    public Game createGame() {


        File temp = new File("src/resources/game.json");
        if (temp.exists()) {
            try {
                temp.delete();
                createData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                createData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Game game = new Game();

        try {
            FileReader fileReader = new FileReader("src/resources/game.json");

            GsonBuilder builder = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Space.class, new Adapter<Space>());

            Gson gson = builder.create();

            JsonReader reader = gson.newJsonReader(fileReader);

            game = gson.fromJson(reader, Game.class);

            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            return game;
        }
    }
}