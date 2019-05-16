package game.model.exceptions;

public class GameEndedException extends Exception{

    public GameEndedException(){
        super("Spillet er slut!");
    }
}
