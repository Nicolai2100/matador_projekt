package game.model;

public class DiceCup implements IDiceCup {

    int[] dice = new int[2];

    @Override
    public void rollDice() {
        dice[0] = (int) (1 + 6.0 * Math.random());
        dice[1] = (int) (1 + 6.0 * Math.random());
    }

    @Override
    public int[] getDice() {
        return dice;
    }
}
