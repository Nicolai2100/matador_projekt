package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import game.model.*;
import game.model.cards.GetOutOfJail;
import game.model.cards.MoveEffect;
import game.model.cards.EconomicEffect;
import game.model.properties.Brewery;
import game.model.properties.RealEstate;
import game.model.properties.Ship;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Main-method creates data for JSON-file,
 * createGame() loads game configuration from said file
 *
 *
 * TODO:
 * Original code: Suffers from unsafe downcasting and re-use of variable 'card', multiple opportunities for mistakes:
 *
 *         Card card = new EconomicEffect(EconomicEffect.EffectType.FROM_BANK);
 *         card.setText("Deres premieobligation er udtrukket. De modtager kr. 1000 af banken.");
 *         ((EconomicEffect) card).setAmount1(1000);
 *         cardList.add(card);
 *
 * Simple fix: Make a variable for each card and don't downcast:
 *
 *         EconomicEffect card37 = new EconomicEffect(EconomicEffect.EffectType.FROM_BANK);
 *         card37.setText("Grundet dyrtiden har De fået gageforhøjelse. Modtag kr. 1000.");
 *         card37.setAmount1(1000);
 *         cardList.add(card37);
 *
 * Better solution:
 *
 * You could add the constructor in the EconomicEffect class, moving all the card construction
 * logic into the constructor.
 *
 *     public EconomicEffect(EffectType effectType, String text, int amount1) {
 * 	    this.effectType = effectType;
 * 	    this.setText(text);
 * 	    this.setAmount1(amount1);
 *     }
 *
 * And in JSONUtility you can use that constructor to add new instances without
 * re-using variables and without having a bunch of different variables and without\\
 * downcasting:
 *
 *        cardList.add(new EconomicEffect(
 *                 EconomicEffect.EffectType.FROM_BANK,
 *                 "Deres premieobligation er udtrukket. De modtager kr. 1000 af banken.",
 *                 1000));
 *
 * @author Neal Patrick Norman, s060527@student.dtu.dk
 *
 *
 * @author Malte B. Kristensen,s185039@student.dtu.dk
 * @author Nicolai d T. Wulff,	s185036@student.dtu.dk
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
        chance.setName("Chance");
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
        chance.setName("Chance");
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
        chance.setName("Chance");
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
        chance.setName("Chance");
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
        chance.setName("Chance");
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
        chance.setName("Chance");
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

        Card card = new EconomicEffect(EconomicEffect.EffectType.FROM_BANK);
        card.setText("Deres premieobligation er udtrukket. De modtager kr. 1000 af banken.");
        ((EconomicEffect) card).setAmount1(1000);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.FROM_BANK);
        card.setText("Værdien af egen avl fra nyttehaven udgør kr. 200, som De modtager af banken.");
        ((EconomicEffect) card).setAmount1(200);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.FROM_BANK);
        card.setText("Grundet dyrtiden har De fået gageforhøjelse. Modtag kr. 1000.");
        ((EconomicEffect) card).setAmount1(1000);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.FROM_BANK);
        card.setText("De modtager Deres aktieudbytte. Modtag kr. 1000 af banken.");
        ((EconomicEffect) card).setAmount1(1000);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.FROM_BANK);
        card.setText("De har solgt nogle gamle møbler på auktion. Modtag kr. 1000  af banken.");
        ((EconomicEffect) card).setAmount1(1000);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.FROM_BANK);
        card.setText("De har vundet i Klasselotterliet. Modtag kr. 500.");
        ((EconomicEffect) card).setAmount1(500);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.FROM_BANK);
        card.setText("Kommunen har eftergivet et kvartals skat. Hæv i banken kr. 3000.");
        ((EconomicEffect) card).setAmount1(3000);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.FROM_BANK);
        card.setText("De har en række med elleve rigtige i tipning. Modtag kr. 1000.");
        ((EconomicEffect) card).setAmount1(1000);
        cardList.add(card);

        card = new MoveEffect(MoveEffect.TargetTypes.NEAREST_SHIP_1);
        card.setText("Ryk brikken frem til det nærmeste rederi og betal ejeren to gange den leje, han ellers er berettiget til." +
                " Hvis selskabet ikke ejes af nogen, kan De købe det af banken.");
        cardList.add(card);

        card = new MoveEffect(MoveEffect.TargetTypes.NEAREST_SHIP_2);
        card.setText("Tag med den nærmeste færge. Flyt brikken frem, og hvis De passerer \"START\", indkassér da kr. 4000.");
        cardList.add(card);

        card = new MoveEffect(MoveEffect.TargetTypes.GO_TO_JAIL);
        card.setText("Gå i fængsel. Ryk direkte til fængslet. Selv om De passerer \"START\", indkasserer De ikke kr. 4000.");
        cardList.add(card);

        card = new MoveEffect(MoveEffect.TargetTypes.SPACE);
        card.setText("Ryk frem til " + game.getSpaces().get(32) + ". Hvis De passerer \"START\", indkassér da kr. 4000.");
        ((MoveEffect) card).setTarget(32);
        cardList.add(card);

        card = new MoveEffect(MoveEffect.TargetTypes.SPACE);
        card.setText("Ryk frem til " + game.getSpaces().get(19) + ". Hvis De passerer \"START\", indkassér da kr. 4000.");
        ((MoveEffect) card).setTarget(19);
        cardList.add(card);

        card = new MoveEffect(MoveEffect.TargetTypes.SPACE);
        card.setText("Ryk frem til " + game.getSpaces().get(24) + ". Hvis De passerer \"START\", indkassér da kr. 4000.");
        ((MoveEffect) card).setTarget(24);
        cardList.add(card);

        card = new MoveEffect(MoveEffect.TargetTypes.SPACE);
        card.setText("Tag med " + game.getSpaces().get(15) + ". Flyt brikken frem, og hvis De passerer \"START\", indkassér da kr. 4000.");
        ((MoveEffect) card).setTarget(15);
        cardList.add(card);

        card = new MoveEffect(MoveEffect.TargetTypes.SPACE);
        card.setText("Ryk frem til \"START\".");
        ((MoveEffect) card).setTarget(0);
        cardList.add(card);

        card = new MoveEffect(MoveEffect.TargetTypes.THREE_FORWARDS);
        card.setText("Ryk tre felter frem.");
        cardList.add(card);

        card = new MoveEffect(MoveEffect.TargetTypes.THREE_BACKWARDS);
        card.setText("Ryk tre felter tilbage.");
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.FROM_OTHER_PLAYERS);
        card.setText("Det er Deres fødselsdag. Modtag af hver medspiller kr. 200.");
        ((EconomicEffect) card).setAmount1(200);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.FROM_OTHER_PLAYERS);
        card.setText("De har lagt penge ud til et sammenskudsgilde. Mærkværdigvis betaler alle straks. Modtag fra hver medspiller kr. 500.");
        ((EconomicEffect) card).setAmount1(500);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.FROM_OTHER_PLAYERS);
        card.setText("De skal holde familiefest og får et tilskud fra hver medspiller på kr. 500");
        ((EconomicEffect) card).setAmount1(500);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.TO_BANK_PER_HOUSE_AND_HOTEL);
        card.setText("Ejendomsskatterne er steget. Ekstraudgifterne er:<BR>kr. 800 pr. hus.<BR>kr. 2300 pr. hotel.");
        ((EconomicEffect) card).setAmount1(800);
        ((EconomicEffect) card).setAmount2(2300);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.TO_BANK_PER_HOUSE_AND_HOTEL);
        card.setText("Oliepriserne er steget, og De skal betale:<BR>kr. 500 pr. hus.<BR>kr. 2000 pr. hotel.");
        ((EconomicEffect) card).setAmount1(500);
        ((EconomicEffect) card).setAmount2(2000);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.TO_BANK);
        card.setText("De har kørt frem for \"Fuldt stop\". Betal kr. 1000 i bøde.");
        ((EconomicEffect) card).setAmount1(1000);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.TO_BANK);
        card.setText("Betal kr. 3000 for reperation af Deres vogn.");
        ((EconomicEffect) card).setAmount1(3000);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.TO_BANK);
        card.setText("De har været en tur i udlandet og haft for mange cigaretter med hjem. Betal told kr. 200.");
        ((EconomicEffect) card).setAmount1(200);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.TO_BANK);
        card.setText("Betal for vognvask og smøring kr. 300.");
        ((EconomicEffect) card).setAmount1(300);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.TO_BANK);
        card.setText("De har modtaget Deres tandlægeregning. Betal kr. 2000.");
        ((EconomicEffect) card).setAmount1(2000);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.TO_BANK);
        card.setText("Betal kr. 200 for levering af 2 kasser øl.");
        ((EconomicEffect) card).setAmount1(200);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.TO_BANK);
        card.setText("De har købt 4 nye dæk til Deres vogn. Betal kr. 1000");
        ((EconomicEffect) card).setAmount1(1000);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.TO_BANK);
        card.setText("Betal deres bilforsikring – kr. 1000.");
        ((EconomicEffect) card).setAmount1(1000);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.TO_BANK);
        card.setText("De har fået en parkeringsbøde. Betal kr 200 i bøde.");
        ((EconomicEffect) card).setAmount1(200);
        cardList.add(card);

        card = new EconomicEffect(EconomicEffect.EffectType.MATADOR_GRANT);
        card.setText("De modtager \"Matador-legatet for værdigt trængende\" på kr. 40.000. Ved værdigt trængende forstås, at Deres formue, dvs. Deres kontante penge + skøder + bygninger, ikke overstiger kr. 15.000.");
        ((EconomicEffect) card).setAmount1(40000);
        cardList.add(card);

        card = new GetOutOfJail();
        card.setText("I anledningen af kongens fødselsdag benådes De herved for fængsel. Dette kort kan opbevares, indtil De får brug for det, eller De kan sælge det.");
        cardList.add(card);

        card = new GetOutOfJail();
        card.setText("I anledningen af kongens fødselsdag benådes De herved for fængsel. Dette kort kan opbevares, indtil De får brug for det, eller De kan sælge det.");
        cardList.add(card);

        card = new GetOutOfJail();
        card.setText("I anledningen af kongens fødselsdag benådes De herved for fængsel. Dette kort kan opbevares, indtil De får brug for det, eller De kan sælge det.");
        cardList.add(card);

        game.setCardDeck(cardList);

        //JSON-GSON operations
        Path path = Paths.get("src/resources");
        //Files.createDirectory(path);

        FileWriter fileWriter = new FileWriter(path + "/game.json");
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Space.class, new Adapter<Space>())
                .registerTypeAdapter(Card.class, new Adapter<Card>());

        Gson gson = builder.create();

        JsonWriter writer = gson.newJsonWriter(fileWriter);

        gson.toJson(game, game.getClass(), writer);

        writer.close();

    }

    /**
     * Load a Game-object from a corresponding JSON-file.
     * @return Fully configured Game-object. Should return an empty game-object if something goes wrong.
     * @author Malte B. Kristensen,s185039@student.dtu.dk
     * @author Nicolai d T. Wulff,	s185036@student.dtu.dk
     */
    public Game createGame() {
/*
        File file = new File("src/resources/game.json");

        if (!file.exists()) {
            try {
                createData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        Game game = new Game();
        try {
            InputStream in = getClass().getResourceAsStream("/game.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            GsonBuilder builder = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Space.class, new Adapter<Space>()).registerTypeAdapter(Card.class, new Adapter<Card>());

            Gson gson = builder.create();

            JsonReader reader = gson.newJsonReader(bufferedReader);

            game = gson.fromJson(reader, Game.class);

            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            return game;
        }
    }
}