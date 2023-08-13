package de.bentzin.ingwer.landfill;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class OneWaySwitch {

    private boolean flipped = false;

    public boolean isFlipped() {
        return flipped;
    }

    public void flip() {
        if(isFlipped()) throw new IllegalStateException("switch is already flipped");
        flipped = true;
    }
}
