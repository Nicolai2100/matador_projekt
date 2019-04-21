package game;

import game.model.cards.CardMove;
import game.model.cards.targetTypes;
import json.JSONUtility;
import game.controller.GameController;
import game.model.Card;
import game.model.Game;
import game.model.cards.CardReceiveMoneyFromBank;

import java.util.ArrayList;

/**
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Main {
    /**
     * The main method which creates a game, shuffles the chance
     * cards, creates players, and then starts the game. Note
     * that, eventually, the game could be loaded from a database.
     *
     * main metoden ændret af Nicolai L
     */
    public static void main(String[] args) {
        JSONUtility ju = new JSONUtility();
        Game game = ju.createGame();

        /*
        //midlertidigt bare så det virker
        ArrayList<Card> temp = new ArrayList<>();
        CardReceiveMoneyFromBank card = new CardReceiveMoneyFromBank();
        card.setText("You receive 100$ from the bank.");
        card.setAmount(100);
        temp.add(card);
        game.setCardDeck(temp);
        //*/


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
        ((CardMove) card).setSpecialTarget(targetTypes.NEAREST_SHIP_1, game.getSpaces());
        cardList.add(card);

        card = new CardMove();
        card.setText("Tag med den nærmeste færge. Flyt brikken frem, og hvis De passerer \"START\", indkassér da kr. 4000.");
        ((CardMove) card).setSpecialTarget(targetTypes.NEAREST_SHIP_2, game.getSpaces());
        cardList.add(card);

        card = new CardMove();
        card.setText("Gå i fængsel. Ryk direkte til fængslet. Selv om De passerer \"START\", indkasserer De ikke kr. 4000.");
        ((CardMove) card).setTargetType(targetTypes.GO_TO_JAIL);
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
        ((CardMove) card).setTargetType(targetTypes.THREE_FORWARDS);
        cardList.add(card);

        card = new CardMove();
        card.setText("Ryk tre felter tilbage.");
        ((CardMove) card).setTargetType(targetTypes.THREE_BACKWARDS);
        cardList.add(card);

        game.setCardDeck(cardList);

        GameController controller = new GameController(game);
        controller.playOrLoadGame();
    }
}
