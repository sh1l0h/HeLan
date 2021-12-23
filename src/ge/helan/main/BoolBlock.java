package ge.helan.main;

public class BoolBlock implements Block{
    private final boolean data;

    public BoolBlock(boolean date) {
        this.data = date;
    }
    @Override
    public Object getData() {
        return data;
    }

    @Override
    public String getTypeName() {
        return "BOOLEAN";
    }

    @Override
    public String toString() {
        return data + "";
    }

    @Override
    public int compareTo(Block block) {
        return 0;
    }
}
