package ge.helan.main;

import java.util.*;
import java.util.regex.Pattern;

public class HeLan {

    private final List<Operation> program;
    private final List<Line> lines;
    private final Stack<Block> stack;
    private final Map<String, Integer> labels;

    public HeLan(String code) {
        stack = new Stack<>();
        lines = getLines(code);
        labels = getLabeling(code);
        program = tokenize(code);
    }

    private void exception(String s, int line) {
        System.out.println("\u001B[31m" + "EXCEPTION(L:" + line + "): " + s + "\u001B[0m");
        System.exit(1);
    }

    private void syntaxError(String s, int line) {
        System.out.println("\u001B[31m" + "SYNTAX-ERROR(L:" + line + "): " + s + "\u001B[0m");
        System.exit(1);
    }

    public void execute() {
        Block[] storage = null;
        Scanner s = new Scanner(System.in);
        for (int pc = 0; pc < program.size(); pc++) {
            Operation curr = program.get(pc);
            switch (curr.getOperationType()) {
                case ADD -> {
                    if (stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"ADD\".", curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if ((a instanceof IntBlock) && (b instanceof IntBlock)) {
                        int aVal = (int) a.getData();
                        int bVal = (int) b.getData();
                        stack.push(new IntBlock(aVal + bVal));
                    } else
                        exception("\"ADD\" takes two NUMBER-s from the stack(The top elements were " + a.getTypeName() + ", " + b.getTypeName() + ").", curr.getLine());
                }
                case CONST, TRUE, FALSE -> stack.push(curr.getBlock());
                case WRITE -> System.out.println(stack.pop());
                case SUB -> {
                    if (stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"SUB\".", curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if ((a instanceof IntBlock) && (b instanceof IntBlock)) {
                        int aVal = (int) a.getData();
                        int bVal = (int) b.getData();
                        stack.push(new IntBlock(bVal - aVal));
                    } else
                        exception("\"SUB\" takes two NUMBER-s from the stack(The top elements were " + a.getTypeName() + ", " + b.getTypeName() + ").", curr.getLine());
                }
                case MUL -> {
                    if (stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"MUL\".", curr.getLine());
                    Block a = stack.pop();
                    Block b = stack.pop();
                    if ((a instanceof IntBlock) && (b instanceof IntBlock)) {
                        int aVal = (int) a.getData();
                        int bVal = (int) b.getData();
                        stack.push(new IntBlock(bVal * aVal));
                    } else
                        exception("\"MUL\" takes two NUMBER-s from the stack(The top elements were " + a.getTypeName() + ", " + b.getTypeName() + ").", curr.getLine());
                }
                case DIV -> {
                    if (stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"DIV\".", curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if ((a instanceof IntBlock) && (b instanceof IntBlock)) {
                        int aVal = (int) a.getData();
                        int bVal = (int) b.getData();
                        stack.push(new IntBlock(bVal / aVal));
                    } else
                        exception("\"DIV\" takes two NUMBER-s from the stack(The top elements were " + a.getTypeName() + ", " + b.getTypeName() + ").", curr.getLine());
                }
                case NEG -> {
                    if (stack.isEmpty())
                        exception("There must be at least one element in the stack before using \"NEG\".", curr.getLine());

                    Block a = stack.pop();
                    if ((a instanceof IntBlock)) {
                        int aVal = (int) a.getData();
                        stack.push(new IntBlock(-aVal));
                    } else
                        exception("\"NEG\" takes a NUMBER from the stack(The top element was " + a.getTypeName() + ").", curr.getLine());
                }
                case MOD -> {
                    if (stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"MOD\".", curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if ((a instanceof IntBlock) && (b instanceof IntBlock)) {
                        int aVal = (int) a.getData();
                        int bVal = (int) b.getData();
                        stack.push(new IntBlock(bVal % aVal));
                    } else
                        exception("\"MOD\" takes two NUMBER-s from the stack(The top elements were " + a.getTypeName() + ", " + b.getTypeName() + ").", curr.getLine());
                }
                case LESS -> {
                    if (stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"LESS\".", curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if ((a instanceof IntBlock) && (b instanceof IntBlock)) {
                        stack.push(new BoolBlock(b.compareTo(a) < 0));
                    } else
                        exception("\"LESS\" takes two NUMBER-s from the stack(The top elements were " + a.getTypeName() + ", " + b.getTypeName() + ").", curr.getLine());
                }
                case LEQ -> {
                    if (stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"LEQ\".", curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if ((a instanceof IntBlock) && (b instanceof IntBlock)) {
                        stack.push(new BoolBlock(b.compareTo(a) <= 0));
                    } else
                        exception("\"LEQ\" takes two NUMBER-s from the stack(The top elements were " + a.getTypeName() + ", " + b.getTypeName() + ").", curr.getLine());
                }
                case EQ -> {
                    if (stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"EQ\".", curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if ((a instanceof IntBlock) && (b instanceof IntBlock)) {
                        stack.push(new BoolBlock(a.compareTo(b) == 0));
                    } else
                        exception("\"EQ\" takes two NUMBER-s from the stack(The top elements were " + a.getTypeName() + ", " + b.getTypeName() + ").", curr.getLine());
                }
                case NEQ -> {
                    if (stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"NEQ\".", curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if ((a instanceof IntBlock) && (b instanceof IntBlock)) {
                        int aVal = (int) a.getData();
                        int bVal = (int) b.getData();
                        stack.push(new BoolBlock(bVal != aVal));
                    } else
                        exception("\"NEQ\" takes two NUMBER-s from the stack(The top elements were " + a.getTypeName() + ", " + b.getTypeName() + ").", curr.getLine());
                }
                case OR -> {
                    if (stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"OR\".", curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if ((a instanceof BoolBlock) && (b instanceof BoolBlock)) {
                        boolean aVal = (boolean) a.getData();
                        boolean bVal = (boolean) b.getData();
                        stack.push(new BoolBlock(bVal || aVal));
                    } else
                        exception("\"OR\" takes two BOOLEAN-s from the stack(The top elements were " + a.getTypeName() + ", " + b.getTypeName() + ").", curr.getLine());
                }
                case AND -> {
                    if (stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"AND\".", curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if ((a instanceof BoolBlock) && (b instanceof BoolBlock)) {
                        boolean aVal = (boolean) a.getData();
                        boolean bVal = (boolean) b.getData();
                        stack.push(new BoolBlock(bVal && aVal));
                    } else
                        exception("\"AND\" takes a BOOLEAN from the stack(The top elements were " + a.getTypeName() + ", " + b.getTypeName() + ").", curr.getLine());
                }
                case NOT -> {
                    if (stack.isEmpty())
                        exception("There must be at least one element in the stack before using \"NOT\".", curr.getLine());

                    Block a = stack.pop();
                    if ((a instanceof BoolBlock)) {
                        boolean aVal = (boolean) a.getData();
                        stack.push(new BoolBlock(!aVal));
                    } else
                        exception("\"NOT\" takes a BOOLEAN from the stack(The top element was " + a.getTypeName() + ").", curr.getLine());
                }
                case ALLOC -> {
                    int data = (int) curr.getBlock().getData();
                    if (data <= 0)
                        exception("A negative number of memory cells cannot be allocated.", curr.getLine());
                    storage = new Block[data];
                }
                case LOAD -> {
                    if (storage == null)
                        exception("Before using \"LOAD\", memory must be allocated(Use \"ALLOC\" to allocate memory).", curr.getLine());

                    int data = (int) curr.getBlock().getData();

                    if (data < 0 || data >= storage.length)
                        exception("Memory cell with address " + data + " does not exist(Addresses[0-" + (storage.length - 1) + "] are available).", curr.getLine());

                    stack.push(storage[data]);
                }
                case STORE -> {
                    if (storage == null)
                        exception("Before using \"STORE\", memory must be allocated(Use \"ALLOC\" to allocate memory).", curr.getLine());

                    int data = (int) curr.getBlock().getData();

                    if (data < 0 || data >= storage.length)
                        exception("Memory cell with address " + data + " does not exist(Addresses[0-" + (storage.length - 1) + "] are available).", curr.getLine());

                    if (stack.isEmpty())
                        exception("There must be at least one element in the stack before using \"STORE\".", curr.getLine());

                    storage[data] = stack.pop();
                }
                case READ -> stack.push(new IntBlock(s.nextInt()));
                case JUMP -> pc = (int) curr.getBlock().getData() - 1;
                case FJUMP -> {
                    if (stack.isEmpty())
                        exception("There must be at least one element in the stack before using \"FJUMP\".", curr.getLine());

                    Block a = stack.pop();
                    if ((a instanceof BoolBlock)) {
                        boolean aVal = (boolean) a.getData();
                        pc = aVal ? pc : (int) curr.getBlock().getData() - 1;
                    } else
                        exception("\"FJUMP\" takes a BOOLEAN from the stack(The top element was " + a.getTypeName() + ").", curr.getLine());
                }
                case HALT -> {
                    return;
                }
            }

        }
        s.close();
    }

    private Map<String, Integer> getLabeling(String s) {
        Map<String, Integer> result = new HashMap<>();
        List<Line> linesToDelete = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            if (line.isLabel()) {
                result.put(line.getLine().replaceAll(":", ""), i - result.size());
                linesToDelete.add(line);
            }
        }
        lines.removeAll(linesToDelete);
        return result;
    }

    private List<Line> getLines(String s) {
        List<Line> result = new ArrayList<>();

        String[] lines = s.split("\n");

        for (int i = 0; i < lines.length; i++) {

            String str = lines[i].replaceAll("(#.*)","").trim();

            if (Pattern.matches("", str)) continue;

            if (Pattern.matches("(([A-Z]+)(([\\t ]+)((\\d+(\\.\\d+)?)|(\".*\")|(\\w+)))?)", str)) {
                result.add(new Line(str, i + 1, false));
            } else if (Pattern.matches("(\\w+:)", str)) {
                result.add(new Line(str, i + 1, true));
            } else {
                /* TODO: add errors */
                if (Pattern.matches("[a-z]+", str))
                    syntaxError("Label name must end with \':\'.", i + 1);

                syntaxError("", i + 1);
            }

        }
        return result;
    }

    private List<Operation> tokenize(String s) {
        List<Operation> result = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);

            String str = line.getLine();

            str = str.replaceAll("([ \t]+)", " ");
            String[] ope = str.split(" ");
            int lineNum = line.getLineNumber();
            switch (ope[0]) {
                case "CONST" -> result.add(new Operation(OperationType.CONST, new IntBlock(Integer.parseInt(ope[1])), lineNum));
                case "ADD" -> result.add(new Operation(OperationType.ADD, lineNum));
                case "WRITE" -> result.add(new Operation(OperationType.WRITE, lineNum));
                case "READ" -> result.add(new Operation(OperationType.READ, lineNum));
                case "SUB" -> result.add(new Operation(OperationType.SUB, lineNum));
                case "MUL" -> result.add(new Operation(OperationType.MUL, lineNum));
                case "DIV" -> result.add(new Operation(OperationType.DIV, lineNum));
                case "NEG" -> result.add(new Operation(OperationType.NEG, lineNum));
                case "MOD" -> result.add(new Operation(OperationType.MOD, lineNum));
                case "TRUE" -> result.add(new Operation(OperationType.TRUE, new BoolBlock(true), lineNum));
                case "FALSE" -> result.add(new Operation(OperationType.FALSE, new BoolBlock(false), lineNum));
                case "LESS" -> result.add(new Operation(OperationType.LESS, lineNum));
                case "LEQ" -> result.add(new Operation(OperationType.LEQ, lineNum));
                case "EQ" -> result.add(new Operation(OperationType.EQ, lineNum));
                case "NEQ" -> result.add(new Operation(OperationType.NEQ, lineNum));
                case "OR" -> result.add(new Operation(OperationType.OR, lineNum));
                case "AND" -> result.add(new Operation(OperationType.AND, lineNum));
                case "NOT" -> result.add(new Operation(OperationType.NOT, lineNum));
                case "ALLOC" -> result.add(new Operation(OperationType.ALLOC, new IntBlock(Integer.parseInt(ope[1])), lineNum));
                case "LOAD" -> result.add(new Operation(OperationType.LOAD, new IntBlock(Integer.parseInt(ope[1])), lineNum));
                case "STORE" -> result.add(new Operation(OperationType.STORE, new IntBlock(Integer.parseInt(ope[1])), lineNum));
                case "JUMP" -> result.add(new Operation(OperationType.JUMP, new IntBlock(labels.get(ope[1])), lineNum));
                case "FJUMP" -> result.add(new Operation(OperationType.FJUMP, new IntBlock(labels.get(ope[1])), lineNum));
                case "HALT" -> result.add(new Operation(OperationType.HALT, lineNum));
                default -> exception("Unknown operation \"" + ope[0] + "\".", lineNum);

            }


        }

        return result;
    }

}
