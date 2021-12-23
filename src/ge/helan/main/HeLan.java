package ge.helan.main;

import java.util.*;
import java.util.regex.Pattern;

public class HeLan {

    private final List<Operation> program;

    public HeLan(String code) {
        program = tokenize(code);
    }

    private void exception(String s, int line){
        System.out.println("\u001B[31m"+ "EXCEPTION(L:"+line+"): "+ s + "\u001B[0m");
        System.exit(1);
    }

    private void syntaxError(String s, int line){
        System.out.println("\u001B[31m"+ "SYNTAX-ERROR(L:"+line+"): "+ s + "\u001B[0m");
        System.exit(1);
    }
    public void execute(){
        Stack<Block> stack = new Stack<>();
        Block[] storage = null;
        Scanner s = new Scanner(System.in);
        for(int pc =0; pc < program.size(); pc++){
            Operation curr = program.get(pc);
            switch (curr.getOperationType()){
                case ADD -> {
                    if(stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"ADD\".",curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof IntBlock) && (b instanceof IntBlock)){
                        int aVal = (int) a.getData();
                        int bVal = (int) b.getData();
                        stack.push(new IntBlock(aVal + bVal));
                    }
                    else
                        exception("\"ADD\" takes two NUMBER-s from the stack(The top elements were " + a.getTypeName() +", " + b.getTypeName()+ ").",curr.getLine());
                }
                case CONST, TRUE, FALSE -> stack.push(curr.getBlock());
                case WRITE -> System.out.println(stack.pop());
                case SUB -> {
                    if(stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"SUB\".",curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof IntBlock) && (b instanceof IntBlock)){
                        int aVal = (int) a.getData();
                        int bVal = (int) b.getData();
                        stack.push(new IntBlock(bVal - aVal));
                    }
                    else
                        exception("\"SUB\" takes two NUMBER-s from the stack(The top elements were " + a.getTypeName() +", " + b.getTypeName()+ ").",curr.getLine());
                }
                case MUL -> {
                    if(stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"MUL\".",curr.getLine());
                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof IntBlock) && (b instanceof IntBlock)){
                        int aVal = (int) a.getData();
                        int bVal = (int) b.getData();
                        stack.push(new IntBlock(bVal * aVal));
                    }
                    else
                        exception("\"MUL\" takes two NUMBER-s from the stack(The top elements were " + a.getTypeName() +", " + b.getTypeName()+ ").",curr.getLine());
                }
                case DIV -> {
                    if(stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"DIV\".",curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof IntBlock) && (b instanceof IntBlock)){
                        int aVal = (int) a.getData();
                        int bVal = (int) b.getData();
                        stack.push(new IntBlock(bVal / aVal));
                    }
                    else
                        exception("\"DIV\" takes two NUMBER-s from the stack(The top elements were " + a.getTypeName() +", " + b.getTypeName()+ ").",curr.getLine());
                }
                case NEG -> {
                    if(stack.isEmpty())
                        exception("There must be at least one element in the stack before using \"NEG\".",curr.getLine());

                    Block a = stack.pop();
                    if((a instanceof IntBlock)){
                        int aVal = (int) a.getData();
                        stack.push(new IntBlock(-aVal));
                    }
                    else
                        exception("\"NEG\" takes a NUMBER from the stack(The top element was " + a.getTypeName() +").",curr.getLine());
                }
                case MOD -> {
                    if(stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"MOD\".",curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof IntBlock) && (b instanceof IntBlock)){
                        int aVal = (int) a.getData();
                        int bVal = (int) b.getData();
                        stack.push(new IntBlock(bVal % aVal));
                    }
                    else
                        exception("\"MOD\" takes two NUMBER-s from the stack(The top elements were " + a.getTypeName() +", " + b.getTypeName()+ ").",curr.getLine());
                }
                case LESS -> {
                    if(stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"LESS\".",curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof IntBlock) && (b instanceof IntBlock)){
                        stack.push(new BoolBlock(b.compareTo(a) < 0));
                    }
                    else
                        exception("\"LESS\" takes two NUMBER-s from the stack(The top elements were " + a.getTypeName() +", " + b.getTypeName()+ ").",curr.getLine());
                }
                case LEQ -> {
                    if(stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"LEQ\".",curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof IntBlock) && (b instanceof IntBlock)){
                        stack.push(new BoolBlock(b.compareTo(a) <= 0));
                    }
                    else
                        exception("\"LEQ\" takes two NUMBER-s from the stack(The top elements were " + a.getTypeName() +", " + b.getTypeName()+ ").",curr.getLine());
                }
                case EQ -> {
                    if(stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"EQ\".",curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof IntBlock) && (b instanceof IntBlock)){
                        stack.push(new BoolBlock(a.compareTo(b) == 0));
                    }
                    else
                        exception("\"EQ\" takes two NUMBER-s from the stack(The top elements were " + a.getTypeName() +", " + b.getTypeName()+ ").",curr.getLine());
                }
                case NEQ -> {
                    if(stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"NEQ\".",curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof IntBlock) && (b instanceof IntBlock)){
                        int aVal = (int) a.getData();
                        int bVal = (int) b.getData();
                        stack.push(new BoolBlock(bVal != aVal));
                    }
                    else
                        exception("\"NEQ\" takes two NUMBER-s from the stack(The top elements were " + a.getTypeName() +", " + b.getTypeName()+ ").",curr.getLine());
                }
                case OR -> {
                    if(stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"OR\".",curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof BoolBlock) && (b instanceof BoolBlock)){
                        boolean aVal = (boolean) a.getData();
                        boolean bVal = (boolean) b.getData();
                        stack.push(new BoolBlock(bVal || aVal));
                    }
                    else
                        exception("\"OR\" takes two BOOLEAN-s from the stack(The top elements were " + a.getTypeName() +", " + b.getTypeName()+ ").",curr.getLine());
                }
                case AND -> {
                    if(stack.size() < 2)
                        exception("There must be at least two elements in the stack before using \"AND\".",curr.getLine());

                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof BoolBlock) && (b instanceof BoolBlock)){
                        boolean aVal = (boolean) a.getData();
                        boolean bVal = (boolean) b.getData();
                        stack.push(new BoolBlock(bVal && aVal));
                    }
                    else
                        exception("\"AND\" takes a BOOLEAN from the stack(The top elements were " + a.getTypeName() +", " + b.getTypeName()+ ").",curr.getLine());
                }
                case NOT -> {
                    if(stack.isEmpty())
                        exception("There must be at least one element in the stack before using \"NOT\".",curr.getLine());

                    Block a = stack.pop();
                    if((a instanceof BoolBlock)){
                        boolean aVal = (boolean) a.getData();
                        stack.push(new BoolBlock(!aVal));
                    }
                    else
                        exception("\"NOT\" takes a BOOLEAN from the stack(The top element was " + a.getTypeName() +").",curr.getLine());
                }
                case ALLOC -> {
                    int data = (int) curr.getBlock().getData();
                    if(data <= 0)
                        exception("A negative number of memory cells cannot be allocated.", curr.getLine());
                    storage = new Block[data];
                }
                case LOAD -> {
                    if(storage == null)
                        exception("Before using \"LOAD\", memory must be allocated(Use \"ALLOC\" to allocate memory).", curr.getLine());

                    int data = (int) curr.getBlock().getData();

                    if(data < 0 || data >= storage.length)
                        exception("Memory cell with address "+data + " does not exist(Addresses[0-" + (storage.length-1) +"] are available).", curr.getLine());

                    stack.push(storage[data]);
                }
                case STORE -> {
                    if(storage == null)
                        exception("Before using \"STORE\", memory must be allocated(Use \"ALLOC\" to allocate memory).", curr.getLine());

                    int data = (int) curr.getBlock().getData();

                    if(data < 0 || data >= storage.length)
                        exception("Memory cell with address "+data + " does not exist(Addresses[0-" + (storage.length-1) +"] are available).", curr.getLine());

                    if(stack.isEmpty())
                        exception("There must be at least one element in the stack before using \"STORE\".",curr.getLine());

                    storage[data] = stack.pop();
                }
                case READ -> stack.push(new IntBlock(s.nextInt()));
                case JUMP -> pc = (int) curr.getBlock().getData() -1;
                case FJUMP -> {
                    if(stack.isEmpty())
                        exception("There must be at least one element in the stack before using \"FJUMP\".",curr.getLine());

                    Block a = stack.pop();
                    if((a instanceof BoolBlock)){
                        boolean aVal = (boolean) a.getData();
                        pc = aVal ? pc : (int) curr.getBlock().getData() -1;
                    }
                    else
                        exception("\"FJUMP\" takes a BOOLEAN from the stack(The top element was " + a.getTypeName() +").",curr.getLine());
                }
                case HALT -> {
                    return;
                }
            }

        }
        s.close();
    }

    private Map<String,Integer> getLabeling(String s){
        String[] lines = s.split("\n");
        Map<String,Integer> result = new HashMap<>();
        int uselessLines = 0;
        for(int i = 0; i < lines.length; i++) {
            String str = lines[i];
            if(str == "" || str.charAt(0) == '#'){
                uselessLines++;
                continue;
            }
            str = str.trim();
            if (str.endsWith(":")) {

                if(!Pattern.matches("^(\\w+):$",str))
                    syntaxError("Label name can only consist of letters and digits.", i+1);

                StringBuilder sb = new StringBuilder(str);
                sb.deleteCharAt(sb.length() - 1);
                result.put(sb.toString(), i - result.size() - uselessLines);
            }
        }
        return result;
    }

    private List<Operation> tokenize(String s) {

        String[] lines = s.split("\n");

        List<Operation> result = new ArrayList<>();
        Map<String,Integer> labels = getLabeling(s);

        for(int i = 0; i < lines.length; i++){
            String str = lines[i].trim().replaceAll("[\t ]+"," ");
            if (str.endsWith(":") || str == ""|| str.charAt(0) == '#') continue;

            String[] ope = str.split(" ");
            if (ope.length < 1)
                continue;
            else if (ope.length > 2){
                // TODO: add exception
            }
            else {
                switch (ope[0]){
                    case "CONST" -> result.add(new Operation(OperationType.CONST, new IntBlock(Integer.parseInt(ope[1])),i+1));
                    case "ADD" -> result.add(new Operation(OperationType.ADD,i+1));
                    case "WRITE" -> result.add(new Operation(OperationType.WRITE,i+1));
                    case "READ" -> result.add(new Operation(OperationType.READ,i+1));
                    case "SUB" -> result.add(new Operation(OperationType.SUB,i+1));
                    case "MUL" -> result.add(new Operation(OperationType.MUL,i+1));
                    case "DIV" -> result.add(new Operation(OperationType.DIV,i+1));
                    case "NEG" -> result.add(new Operation(OperationType.NEG,i+1));
                    case "MOD" -> result.add(new Operation(OperationType.MOD,i+1));
                    case "TRUE" -> result.add(new Operation(OperationType.TRUE, new BoolBlock(true),i+1));
                    case "FALSE" -> result.add(new Operation(OperationType.FALSE, new BoolBlock(false),i+1));
                    case "LESS" -> result.add(new Operation(OperationType.LESS,i+1));
                    case "LEQ" -> result.add(new Operation(OperationType.LEQ,i+1));
                    case "EQ" -> result.add(new Operation(OperationType.EQ,i+1));
                    case "NEQ" -> result.add(new Operation(OperationType.NEQ,i+1));
                    case "OR" -> result.add(new Operation(OperationType.OR,i+1));
                    case "AND" -> result.add(new Operation(OperationType.AND,i+1));
                    case "NOT" -> result.add(new Operation(OperationType.NOT,i+1));
                    case "ALLOC" -> result.add(new Operation(OperationType.ALLOC, new IntBlock(Integer.parseInt(ope[1])),i+1));
                    case "LOAD" -> result.add(new Operation(OperationType.LOAD, new IntBlock(Integer.parseInt(ope[1])),i+1));
                    case "STORE" -> result.add(new Operation(OperationType.STORE, new IntBlock(Integer.parseInt(ope[1])),i+1));
                    case "JUMP" -> result.add(new Operation(OperationType.JUMP, new IntBlock(labels.get(ope[1])),i+1));
                    case "FJUMP" -> result.add(new Operation(OperationType.FJUMP, new IntBlock(labels.get(ope[1])),i+1));
                    case "HALT" -> result.add(new Operation(OperationType.HALT,i+1));
                    default -> exception("Unknown operation \"" + ope[0] + "\".",i+1);
                }

            }


        }

        return result;
    }

}
