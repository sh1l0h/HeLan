package ge.helan.main;

public interface Block extends Comparable<Block> {
    Object getData();

    String getTypeName();
}
