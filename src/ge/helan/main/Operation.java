package ge.helan.main;

public class Operation {
    private final OperationType operationType;
    private final Block block;
    private final int line;

    public Operation(OperationType operationType, int line) {
        this.operationType = operationType;
        block = null;
        this.line = line;
    }

    public Operation(OperationType operationType, Block block, int line) {
        this.operationType = operationType;
        this.block = block;
        this.line = line;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public Block getBlock() {
        return block;
    }

    public int getLine() {
        return line;
    }
}
