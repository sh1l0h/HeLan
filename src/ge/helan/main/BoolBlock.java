package ge.helan.main;

public class BoolBlock implements Block{
    private final boolean date;

    public BoolBlock(boolean date) {
        this.date = date;
    }

    public boolean getDate() {
        return date;
    }

    @Override
    public String toString() {
        return date + "";
    }
}
