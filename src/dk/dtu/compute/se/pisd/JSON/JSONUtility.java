package dk.dtu.compute.se.pisd.JSON;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dk.dtu.compute.se.pisd.designpatterns.Subject;
import dk.dtu.compute.se.pisd.monopoly.mini.model.*;
import dk.dtu.compute.se.pisd.monopoly.mini.model.properties.RealEstate;
import dk.dtu.compute.se.pisd.monopoly.mini.model.properties.Utility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
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
    public static void main(String[] args) throws IOException {

        //creating data for the game's spaces
        ArrayList<Space> spaces = new ArrayList<>(); //TODO ændr priserne til de rigtige på GUI'en

        Space go = new Space();
        go.setName("Start");
        spaces.add(go);

        Space roedovrevej = new RealEstate();
        roedovrevej.setName("Rødovrevej");
        ((RealEstate) roedovrevej).setCost(1200);
        ((RealEstate) roedovrevej).setRent(50);
        ((RealEstate) roedovrevej).setColorGroup(ColorGroup.lightblue);
        ((RealEstate) roedovrevej).setPriceForHouse(1000);
        spaces.add(roedovrevej);

        Space chance = new Chance(); //colorgroup?
        chance.setName("chance");
        spaces.add(chance);

        Space hvidovrevej = new RealEstate();
        hvidovrevej.setName("Hvidovrevej");
        ((RealEstate) hvidovrevej).setCost(1200);
        ((RealEstate) hvidovrevej).setRent(50);
        ((RealEstate) hvidovrevej).setColorGroup(ColorGroup.lightblue);
        ((RealEstate) hvidovrevej).setPriceForHouse(1000);
        spaces.add(hvidovrevej);

        Space indkomstskat = new Tax();
        indkomstskat.setName("Indkomstskat");
        spaces.add(indkomstskat);

        Space oeresund = new Utility();
        //TODO ændr alle utilities til enten Ship eller Brewery (som kan have begyndelsespris sat fra start)
        oeresund.setName("Øresund");
        ((Utility) oeresund).setCost(4000);
        ((Utility) oeresund).setRent(500);
        ((Utility) oeresund).setColorGroup(ColorGroup.navy);
        spaces.add(oeresund);

        Space roskildevej = new RealEstate();
        roskildevej.setName("Roskildevej");
        ((RealEstate) roskildevej).setCost(2000);
        ((RealEstate) roskildevej).setRent(100);
        ((RealEstate) roskildevej).setColorGroup(ColorGroup.pink);
        ((RealEstate) roskildevej).setPriceForHouse(1000);
        spaces.add(roskildevej);

        //spaces.add(chance); //virker dette?

        Space valbyLanggade = new RealEstate();
        valbyLanggade.setName("Valby Langgade");
        ((RealEstate) valbyLanggade).setCost(2000);
        ((RealEstate) valbyLanggade).setRent(100);
        ((RealEstate) valbyLanggade).setColorGroup(ColorGroup.pink);
        ((RealEstate) valbyLanggade).setPriceForHouse(1000);
        spaces.add(valbyLanggade);

        Space allegade = new RealEstate();
        allegade.setName("Allégade");
        ((RealEstate) allegade).setCost(2400);
        ((RealEstate) allegade).setRent(150);
        ((RealEstate) allegade).setColorGroup(ColorGroup.pink);
        ((RealEstate) valbyLanggade).setPriceForHouse(1000);
        spaces.add(valbyLanggade);

        Space faengsel = new Space();
        faengsel.setName("I fængsel/På besøg");
        spaces.add(faengsel);

        Space frederiksbergAlle = new RealEstate();
        frederiksbergAlle.setName("Fredriksberg Allé");
        ((RealEstate) frederiksbergAlle).setCost(2800);
        ((RealEstate) frederiksbergAlle).setRent(200);
        ((RealEstate) frederiksbergAlle).setColorGroup(ColorGroup.green);
        ((RealEstate) frederiksbergAlle).setPriceForHouse(2000);
        spaces.add(frederiksbergAlle);

        Space tuborg = new Utility();
        tuborg.setName("Tuborg");
        ((Utility) tuborg).setCost(3000);
        ((Utility) tuborg).setRent(0);
        ((Utility) tuborg).setColorGroup(ColorGroup.darkgreen);
        spaces.add(tuborg);

        Space bulowsvej = new RealEstate();
        bulowsvej.setName("Bülowsvej");
        ((RealEstate) bulowsvej).setCost(2800);
        ((RealEstate) bulowsvej).setRent(200);
        ((RealEstate) bulowsvej).setColorGroup(ColorGroup.green);
        ((RealEstate) bulowsvej).setPriceForHouse(2000);
        spaces.add(bulowsvej);

        Space gammelkongevej = new RealEstate();
        gammelkongevej.setName("Gammelkongevej");
        ((RealEstate) gammelkongevej).setCost(3200);
        ((RealEstate) gammelkongevej).setRent(250);
        ((RealEstate) gammelkongevej).setColorGroup(ColorGroup.green);
        ((RealEstate) gammelkongevej).setPriceForHouse(2000);
        spaces.add(gammelkongevej);

        Space dfds = new Utility();
        dfds.setName("D.F.D.S");
        ((Utility) dfds).setCost(4000);
        ((Utility) dfds).setRent(500);
        ((Utility) dfds).setColorGroup(ColorGroup.navy);
        spaces.add(dfds);

        Space bernstorffsvej = new RealEstate();
        bernstorffsvej.setName("Bernstorffsvej");
        ((RealEstate) bernstorffsvej).setCost(3600);
        ((RealEstate) bernstorffsvej).setRent(300);
        ((RealEstate) bernstorffsvej).setColorGroup(ColorGroup.darkgrey);
        ((RealEstate) bernstorffsvej).setPriceForHouse(2000);
        spaces.add(bernstorffsvej);

        //spaces.add(chance);

        Space hellerupvej = new RealEstate();
        hellerupvej.setName("Hellerupvej");
        ((RealEstate) hellerupvej).setCost(3600);
        ((RealEstate) hellerupvej).setRent(300);
        ((RealEstate) hellerupvej).setColorGroup(ColorGroup.darkgrey);
        ((RealEstate) hellerupvej).setPriceForHouse(2000);
        spaces.add(hellerupvej);

        Space strandvej = new RealEstate();
        strandvej.setName("Strandvej");
        ((RealEstate) strandvej).setCost(4000);
        ((RealEstate) strandvej).setRent(350);
        ((RealEstate) strandvej).setColorGroup(ColorGroup.darkgrey);
        ((RealEstate) strandvej).setPriceForHouse(2000);
        spaces.add(strandvej);

        Space parkering = new Space();
        parkering.setName("Helle");
        spaces.add(parkering);

        Space trianglen = new RealEstate();
        trianglen.setName("Trianglen");
        ((RealEstate) trianglen).setCost(4400);
        ((RealEstate) trianglen).setRent(350);
        ((RealEstate) trianglen).setColorGroup(ColorGroup.red);
        ((RealEstate) trianglen).setPriceForHouse(3000);
        spaces.add(trianglen);

        //spaces.add(chance);

        Space oesterbrogade = new RealEstate();
        oesterbrogade.setName("Østerbrogade");
        ((RealEstate) oesterbrogade).setCost(4400);
        ((RealEstate) oesterbrogade).setRent(350);
        ((RealEstate) oesterbrogade).setColorGroup(ColorGroup.red);
        ((RealEstate) oesterbrogade).setPriceForHouse(3000);
        spaces.add(oesterbrogade);

        Space gronningen = new RealEstate();
        gronningen.setName("Grønningen");
        ((RealEstate) gronningen).setCost(4800);
        ((RealEstate) gronningen).setRent(400);
        ((RealEstate) gronningen).setColorGroup(ColorGroup.red);
        ((RealEstate) gronningen).setPriceForHouse(3000);
        spaces.add(gronningen);

        Space oes = new Utility();
        oes.setName("Ø.S.");
        ((Utility) oes).setCost(4000);
        ((Utility) oes).setCost(500);
        ((Utility) oes).setColorGroup(ColorGroup.navy);
        spaces.add(oes);

        Space bredgade = new RealEstate();
        bredgade.setName("Bredgade");
        ((RealEstate) bredgade).setCost(5200);
        ((RealEstate) bredgade).setRent(450);
        ((RealEstate) bredgade).setColorGroup(ColorGroup.white);
        ((RealEstate) bredgade).setPriceForHouse(3000);
        spaces.add(bredgade);

        Space kgsnytorv = new RealEstate();
        kgsnytorv.setName("Kgs. Nytorv");
        ((RealEstate) kgsnytorv).setCost(5200);
        ((RealEstate) kgsnytorv).setRent(450);
        ((RealEstate) kgsnytorv).setColorGroup(ColorGroup.white);
        ((RealEstate) kgsnytorv).setPriceForHouse(3000);
        spaces.add(kgsnytorv);

        Space carlsberg = new Utility();
        carlsberg.setName("Carlsberg");
        ((Utility) carlsberg).setCost(3000);
        ((Utility) carlsberg).setRent(0);
        ((Utility) carlsberg).setColorGroup(ColorGroup.darkgreen);
        spaces.add(carlsberg);

        Space oestergade = new RealEstate();
        oestergade.setName("Østergade");
        ((RealEstate) oestergade).setCost(5600);
        ((RealEstate) oestergade).setRent(500);
        ((RealEstate) oestergade).setColorGroup(ColorGroup.white);
        ((RealEstate) oestergade).setPriceForHouse(3000);
        spaces.add(oestergade);

        Space ifaengsel = new Space();
        ifaengsel.setName("I fængsel");
        spaces.add(ifaengsel);

        Space amagertorv = new RealEstate();
        amagertorv.setName("Amagertorv");
        ((RealEstate) amagertorv).setCost(6000);
        ((RealEstate) amagertorv).setRent(550);
        ((RealEstate) amagertorv).setColorGroup(ColorGroup.yellow);
        ((RealEstate) amagertorv).setPriceForHouse(4000);
        spaces.add(amagertorv);

        Space vimmelskaftet = new RealEstate();
        vimmelskaftet.setName("Vimmelskaftet");
        ((RealEstate) vimmelskaftet).setCost(6000);
        ((RealEstate) vimmelskaftet).setRent(550);
        ((RealEstate) vimmelskaftet).setColorGroup(ColorGroup.yellow);
        ((RealEstate) vimmelskaftet).setPriceForHouse(4000);
        spaces.add(vimmelskaftet);

        //spaces.add(chance);

        Space nygade = new RealEstate();
        nygade.setName("Nygade");
        ((RealEstate) nygade).setCost(6400);
        ((RealEstate) nygade).setRent(600);
        ((RealEstate) nygade).setColorGroup(ColorGroup.yellow);
        spaces.add(nygade);

        Space bornholm = new Utility();
        bornholm.setName("Bornholm");
        ((Utility) bornholm).setCost(4000);
        ((Utility) bornholm).setRent(500);
        spaces.add(bornholm);

        //spaces.add(chance);

        Space frederiksberggade = new RealEstate();
        frederiksberggade.setName("Frederiksberggade");
        ((RealEstate) frederiksberggade).setCost(7000);
        ((RealEstate) frederiksberggade).setRent(700);
        ((RealEstate) frederiksberggade).setColorGroup(ColorGroup.purple);
        ((RealEstate) frederiksberggade).setPriceForHouse(4000);
        spaces.add(frederiksberggade);

        Space ekstraskat = new Space();
        ekstraskat.setName("Ekstraordinær statsskat");
        spaces.add(ekstraskat);

        Space raadhuspladsen = new RealEstate();
        raadhuspladsen.setName("Rådhuspladsen");
        ((RealEstate) raadhuspladsen).setCost(8000);
        ((RealEstate) raadhuspladsen).setRent(1000);
        ((RealEstate) raadhuspladsen).setColorGroup(ColorGroup.purple);
        ((RealEstate) raadhuspladsen).setPriceForHouse(4000);
        spaces.add(raadhuspladsen);

        Game game = new Game();
        game.setSpaces(spaces);

        //JSON-GSON operations
        Path path = Paths.get("resources/");
        Files.createDirectory(path);

        FileWriter fileWriter = new FileWriter(path+ "\\game.json");
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Subject.class, new Adapter<Subject>());

        Gson gson = builder.create();

        JsonWriter writer = gson.newJsonWriter(fileWriter);

        gson.toJson(game, game.getClass(), writer);

        writer.close();

    }

    public static Game createGame() {
        Game game = new Game();

        try {

            FileReader fileReader = new FileReader("resources\\game.json");

            Gson gson = new Gson();

            JsonReader reader = gson.newJsonReader(fileReader);

            game = gson.fromJson(reader, Game.class);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            return game;
        }
    }
}