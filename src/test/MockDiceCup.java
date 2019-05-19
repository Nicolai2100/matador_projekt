package test;

import game.model.DiceCup;
import game.model.IDiceCup;

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
