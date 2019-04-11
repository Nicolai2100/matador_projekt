package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import game.model.*;
import game.model.properties.RealEstate;
import game.model.properties.Utility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main-method creates data for JSON-file,
 * createGame() loads game configuration from said file
 *
 * @author s185039
 */

public class JSONUtility {
    public static void main(String[] args) throws IOException {

        //creating data for the game's spaces
        Game game = new Game();

        Space go = new NeutralSpace();
        go.setName("Start");
        game.addSpace(go);

        Space roedovrevej = new RealEstate();
        roedovrevej.setName("Rødovrevej");
        ((RealEstate) roedovrevej).setCost(1200);
        ((RealEstate) roedovrevej).setRent(50);
        ((RealEstate) roedovrevej).setColorGroup(ColorGroup.lightblue);
        ((RealEstate) roedovrevej).setPriceForHouse(1000);
        game.addSpace(roedovrevej);

        Space chance = new Chance(); //colorgroup?
        chance.setName("chance");
        game.addSpace(chance);

        Space hvidovrevej = new RealEstate();
        hvidovrevej.setName("Hvidovrevej");
        ((RealEstate) hvidovrevej).setCost(1200);
        ((RealEstate) hvidovrevej).setRent(50);
        ((RealEstate) hvidovrevej).setColorGroup(ColorGroup.lightblue);
        ((RealEstate) hvidovrevej).setPriceForHouse(1000);
        game.addSpace(hvidovrevej);

        Space indkomstskat = new Tax();
        indkomstskat.setName("Indkomstskat");
        game.addSpace(indkomstskat);

        Space oeresund = new Utility();
        //TODO ændr alle utilities til enten Ship eller Brewery (som kan have begyndelsespris sat fra start)
        oeresund.setName("Øresund");
        ((Utility) oeresund).setCost(4000);
        ((Utility) oeresund).setRent(500);
        ((Utility) oeresund).setColorGroup(ColorGroup.navy);
        game.addSpace(oeresund);

        Space roskildevej = new RealEstate();
        roskildevej.setName("Roskildevej");
        ((RealEstate) roskildevej).setCost(2000);
        ((RealEstate) roskildevej).setRent(100);
        ((RealEstate) roskildevej).setColorGroup(ColorGroup.pink);
        ((RealEstate) roskildevej).setPriceForHouse(1000);
        game.addSpace(roskildevej);

        chance = new Chance();
        chance.setName("chance");
        game.addSpace(chance); //virker dette?

        Space valbyLanggade = new RealEstate();
        valbyLanggade.setName("Valby Langgade");
        ((RealEstate) valbyLanggade).setCost(2000);
        ((RealEstate) valbyLanggade).setRent(100);
        ((RealEstate) valbyLanggade).setColorGroup(ColorGroup.pink);
        ((RealEstate) valbyLanggade).setPriceForHouse(1000);
        game.addSpace(valbyLanggade);

        Space allegade = new RealEstate();
        allegade.setName("Allégade");
        ((RealEstate) allegade).setCost(2400);
        ((RealEstate) allegade).setRent(150);
        ((RealEstate) allegade).setColorGroup(ColorGroup.pink);
        ((RealEstate) valbyLanggade).setPriceForHouse(1000);
        game.addSpace(allegade);

        Space faengsel = new NeutralSpace();
        faengsel.setName("I fængsel/På besøg");
        game.addSpace(faengsel);

        Space frederiksbergAlle = new RealEstate();
        frederiksbergAlle.setName("Fredriksberg Allé");
        ((RealEstate) frederiksbergAlle).setCost(2800);
        ((RealEstate) frederiksbergAlle).setRent(200);
        ((RealEstate) frederiksbergAlle).setColorGroup(ColorGroup.green);
        ((RealEstate) frederiksbergAlle).setPriceForHouse(2000);
        game.addSpace(frederiksbergAlle);

        Space tuborg = new Utility();
        tuborg.setName("Tuborg");
        ((Utility) tuborg).setCost(3000);
        ((Utility) tuborg).setRent(0);
        ((Utility) tuborg).setColorGroup(ColorGroup.darkgreen);
        game.addSpace(tuborg);

        Space bulowsvej = new RealEstate();
        bulowsvej.setName("Bülowsvej");
        ((RealEstate) bulowsvej).setCost(2800);
        ((RealEstate) bulowsvej).setRent(200);
        ((RealEstate) bulowsvej).setColorGroup(ColorGroup.green);
        ((RealEstate) bulowsvej).setPriceForHouse(2000);
        game.addSpace(bulowsvej);

        Space gammelkongevej = new RealEstate();
        gammelkongevej.setName("Gammelkongevej");
        ((RealEstate) gammelkongevej).setCost(3200);
        ((RealEstate) gammelkongevej).setRent(250);
        ((RealEstate) gammelkongevej).setColorGroup(ColorGroup.green);
        ((RealEstate) gammelkongevej).setPriceForHouse(2000);
        game.addSpace(gammelkongevej);

        Space dfds = new Utility();
        dfds.setName("D.F.D.S");
        ((Utility) dfds).setCost(4000);
        ((Utility) dfds).setRent(500);
        ((Utility) dfds).setColorGroup(ColorGroup.navy);
        game.addSpace(dfds);

        Space bernstorffsvej = new RealEstate();
        bernstorffsvej.setName("Bernstorffsvej");
        ((RealEstate) bernstorffsvej).setCost(3600);
        ((RealEstate) bernstorffsvej).setRent(300);
        ((RealEstate) bernstorffsvej).setColorGroup(ColorGroup.darkgrey);
        ((RealEstate) bernstorffsvej).setPriceForHouse(2000);
        game.addSpace(bernstorffsvej);

        chance = new Chance();
        chance.setName("chance");
        game.addSpace(chance);

        Space hellerupvej = new RealEstate();
        hellerupvej.setName("Hellerupvej");
        ((RealEstate) hellerupvej).setCost(3600);
        ((RealEstate) hellerupvej).setRent(300);
        ((RealEstate) hellerupvej).setColorGroup(ColorGroup.darkgrey);
        ((RealEstate) hellerupvej).setPriceForHouse(2000);
        game.addSpace(hellerupvej);

        Space strandvej = new RealEstate();
        strandvej.setName("Strandvej");
        ((RealEstate) strandvej).setCost(4000);
        ((RealEstate) strandvej).setRent(350);
        ((RealEstate) strandvej).setColorGroup(ColorGroup.darkgrey);
        ((RealEstate) strandvej).setPriceForHouse(2000);
        game.addSpace(strandvej);

        Space parkering = new NeutralSpace();
        parkering.setName("Helle");
        game.addSpace(parkering);

        Space trianglen = new RealEstate();
        trianglen.setName("Trianglen");
        ((RealEstate) trianglen).setCost(4400);
        ((RealEstate) trianglen).setRent(350);
        ((RealEstate) trianglen).setColorGroup(ColorGroup.red);
        ((RealEstate) trianglen).setPriceForHouse(3000);
        game.addSpace(trianglen);

        chance = new Chance();
        chance.setName("chance");
        game.addSpace(chance);

        Space oesterbrogade = new RealEstate();
        oesterbrogade.setName("Østerbrogade");
        ((RealEstate) oesterbrogade).setCost(4400);
        ((RealEstate) oesterbrogade).setRent(350);
        ((RealEstate) oesterbrogade).setColorGroup(ColorGroup.red);
        ((RealEstate) oesterbrogade).setPriceForHouse(3000);
        game.addSpace(oesterbrogade);

        Space gronningen = new RealEstate();
        gronningen.setName("Grønningen");
        ((RealEstate) gronningen).setCost(4800);
        ((RealEstate) gronningen).setRent(400);
        ((RealEstate) gronningen).setColorGroup(ColorGroup.red);
        ((RealEstate) gronningen).setPriceForHouse(3000);
        game.addSpace(gronningen);

        Space oes = new Utility();
        oes.setName("Ø.S.");
        ((Utility) oes).setCost(4000);
        ((Utility) oes).setCost(500);
        ((Utility) oes).setColorGroup(ColorGroup.navy);
        game.addSpace(oes);

        Space bredgade = new RealEstate();
        bredgade.setName("Bredgade");
        ((RealEstate) bredgade).setCost(5200);
        ((RealEstate) bredgade).setRent(450);
        ((RealEstate) bredgade).setColorGroup(ColorGroup.white);
        ((RealEstate) bredgade).setPriceForHouse(3000);
        game.addSpace(bredgade);

        Space kgsnytorv = new RealEstate();
        kgsnytorv.setName("Kgs. Nytorv");
        ((RealEstate) kgsnytorv).setCost(5200);
        ((RealEstate) kgsnytorv).setRent(450);
        ((RealEstate) kgsnytorv).setColorGroup(ColorGroup.white);
        ((RealEstate) kgsnytorv).setPriceForHouse(3000);
        game.addSpace(kgsnytorv);

        Space carlsberg = new Utility();
        carlsberg.setName("Carlsberg");
        ((Utility) carlsberg).setCost(3000);
        ((Utility) carlsberg).setRent(0);
        ((Utility) carlsberg).setColorGroup(ColorGroup.darkgreen);
        game.addSpace(carlsberg);

        Space oestergade = new RealEstate();
        oestergade.setName("Østergade");
        ((RealEstate) oestergade).setCost(5600);
        ((RealEstate) oestergade).setRent(500);
        ((RealEstate) oestergade).setColorGroup(ColorGroup.white);
        ((RealEstate) oestergade).setPriceForHouse(3000);
        game.addSpace(oestergade);

        Space ifaengsel = new NeutralSpace();
        ifaengsel.setName("I fængsel");
        game.addSpace(ifaengsel);

        Space amagertorv = new RealEstate();
        amagertorv.setName("Amagertorv");
        ((RealEstate) amagertorv).setCost(6000);
        ((RealEstate) amagertorv).setRent(550);
        ((RealEstate) amagertorv).setColorGroup(ColorGroup.yellow);
        ((RealEstate) amagertorv).setPriceForHouse(4000);
        game.addSpace(amagertorv);

        Space vimmelskaftet = new RealEstate();
        vimmelskaftet.setName("Vimmelskaftet");
        ((RealEstate) vimmelskaftet).setCost(6000);
        ((RealEstate) vimmelskaftet).setRent(550);
        ((RealEstate) vimmelskaftet).setColorGroup(ColorGroup.yellow);
        ((RealEstate) vimmelskaftet).setPriceForHouse(4000);
        game.addSpace(vimmelskaftet);

        chance = new Chance();
        chance.setName("chance");
        game.addSpace(chance);

        Space nygade = new RealEstate();
        nygade.setName("Nygade");
        ((RealEstate) nygade).setCost(6400);
        ((RealEstate) nygade).setRent(600);
        ((RealEstate) nygade).setColorGroup(ColorGroup.yellow);
        game.addSpace(nygade);

        Space bornholm = new Utility();
        bornholm.setName("Bornholm");
        ((Utility) bornholm).setCost(4000);
        ((Utility) bornholm).setRent(500);
        game.addSpace(bornholm);

        chance = new Chance();
        chance.setName("chance");
        game.addSpace(chance);

        Space frederiksberggade = new RealEstate();
        frederiksberggade.setName("Frederiksberggade");
        ((RealEstate) frederiksberggade).setCost(7000);
        ((RealEstate) frederiksberggade).setRent(700);
        ((RealEstate) frederiksberggade).setColorGroup(ColorGroup.purple);
        ((RealEstate) frederiksberggade).setPriceForHouse(4000);
        game.addSpace(frederiksberggade);

        Space ekstraskat = new Tax();
        ekstraskat.setName("Ekstraordinær statsskat");
        game.addSpace(ekstraskat);

        Space raadhuspladsen = new RealEstate();
        raadhuspladsen.setName("Rådhuspladsen");
        ((RealEstate) raadhuspladsen).setCost(8000);
        ((RealEstate) raadhuspladsen).setRent(1000);
        ((RealEstate) raadhuspladsen).setColorGroup(ColorGroup.purple);
        ((RealEstate) raadhuspladsen).setPriceForHouse(4000);
        game.addSpace(raadhuspladsen);


        //JSON-GSON operations
        Path path = Paths.get("resources");
        Files.createDirectory(path);

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
    public static Game createGame() {
        Game game = new Game();

        try {

            FileReader fileReader = new FileReader("resources/game.json");

            GsonBuilder builder = new GsonBuilder().setPrettyPrinting()
                    .registerTypeAdapter(Space.class, new Adapter<Space>());

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