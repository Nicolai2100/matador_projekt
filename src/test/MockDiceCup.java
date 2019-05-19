package test;

import game.model.DiceCup;
import game.model.IDiceCup;

/**
 * This class is a "fake" dice cup, that can be used when
 * testing the game. This dice cup differs from the regular
 * in that it can set predetermined values through the constructor.
 * When all predetermined values have been used, the dice cup
 * will then begin to act like a regular dice cup.
 */
public class MockDiceCup implements IDiceCup {

    private int[] dice = new int[2];
    private int[] predeterminedValues;
    private int i = 0;
    private DiceCup realDiceCup = new DiceCup();

    public MockDiceCup(int[] predeterminedValues) {
        this.predeterminedValues = predeterminedValues;
    }

    @Override
    public void rollDice() {
        realDiceCup.rollDice();
        for (int j = 0; j < 2; j++) {
            if (i < predeterminedValues.length) {
                dice[j] = predeterminedValues[i++];
            } else {
                dice[j] = realDiceCup.getDice()[j];
            }
        }
    }

    @Override
    public int[] getDice() {
        return dice;
    }
}
