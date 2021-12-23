package ge.helan.main;

public class IntBlock implements Block, Comparable<Block> {
    private final int data;

    public IntBlock(int date) {
        this.data = date;
    }

    public String getTypeName(){
        return "INTEGER";
    }
    @Override
    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return data + "";
    }
    
    @Override
    public int compareTo(Block block) {
        return (int)getData() - (int) block.getData();
    }
}
