package ge.helan.main;

public class IntBlock implements Block{
    private final int date;

    public IntBlock(int date) {
        this.date = date;
    }

    public int getDate() {
        return date;
    }

    @Override
    public String toString() {
        return date + "";
    }
}
