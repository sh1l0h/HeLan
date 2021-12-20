package ge.helan.main;

public class Operation {
    private final OperationType operationType;
    private Block block = null;

    public Operation(OperationType operationType) {
        this.operationType = operationType;
    }

    public Operation(OperationType operationType, Block block) {
        this.operationType = operationType;
        this.block = block;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public Block getBlock() {
        return block;
    }
}
