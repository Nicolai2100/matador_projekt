package game.model;

/**
 * This class represents a dice cuo, which can be used
 * to roll two dice and return an array with each value.
 * @author Nicolai d T. Wulff,	s185036@student.dtu.dk
 */
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
