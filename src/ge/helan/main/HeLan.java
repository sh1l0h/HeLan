package ge.helan.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class HeLan {

    private final List<Operation> program;

    public HeLan(String code) {
        program = tokenize(code);
    }

    public void execute(){
        Stack<Block> stack = new Stack<>();
        Block[] storage = null;
        Scanner s = new Scanner(System.in);
        for(int pc =0; pc < program.size(); pc++){
            Operation curr = program.get(pc);
            switch (curr.getOperationType()){
                case ADD -> {
                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof IntBlock) && (b instanceof IntBlock)){
                        int aVal = ((IntBlock) a).getDate();
                        int bVal = ((IntBlock) b).getDate();
                        stack.push(new IntBlock(aVal + bVal));
                    }
                }
                case CONST, TRUE, FALSE -> stack.push(curr.getBlock());
                case WRITE -> System.out.println(stack.pop());
                case SUB -> {
                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof IntBlock) && (b instanceof IntBlock)){
                        int aVal = ((IntBlock) a).getDate();
                        int bVal = ((IntBlock) b).getDate();
                        stack.push(new IntBlock(bVal - aVal));
                    }
                }
                case MUL -> {
                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof IntBlock) && (b instanceof IntBlock)){
                        int aVal = ((IntBlock) a).getDate();
                        int bVal = ((IntBlock) b).getDate();
                        stack.push(new IntBlock(bVal * aVal));
                    }
                }
                case DIV -> {
                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof IntBlock) && (b instanceof IntBlock)){
                        int aVal = ((IntBlock) a).getDate();
                        int bVal = ((IntBlock) b).getDate();
                        stack.push(new IntBlock(bVal / aVal));
                    }
                }
                case NEG -> {
                    Block a = stack.pop();
                    if((a instanceof IntBlock)){
                        int aVal = ((IntBlock) a).getDate();
                        stack.push(new IntBlock(-aVal));
                    }
                }
                case MOD -> {
                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof IntBlock) && (b instanceof IntBlock)){
                        int aVal = ((IntBlock) a).getDate();
                        int bVal = ((IntBlock) b).getDate();
                        stack.push(new IntBlock(bVal % aVal));
                    }
                }
                case LESS -> {
                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof IntBlock) && (b instanceof IntBlock)){
                        int aVal = ((IntBlock) a).getDate();
                        int bVal = ((IntBlock) b).getDate();
                        stack.push(new BoolBlock(bVal < aVal));
                    }
                }
                case LEQ -> {
                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof IntBlock) && (b instanceof IntBlock)){
                        int aVal = ((IntBlock) a).getDate();
                        int bVal = ((IntBlock) b).getDate();
                        stack.push(new BoolBlock(bVal <= aVal));
                    }
                }
                case EQ -> {
                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof IntBlock) && (b instanceof IntBlock)){
                        int aVal = ((IntBlock) a).getDate();
                        int bVal = ((IntBlock) b).getDate();
                        stack.push(new BoolBlock(bVal == aVal));
                    }
                }
                case NEQ -> {
                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof IntBlock) && (b instanceof IntBlock)){
                        int aVal = ((IntBlock) a).getDate();
                        int bVal = ((IntBlock) b).getDate();
                        stack.push(new BoolBlock(bVal != aVal));
                    }
                }
                case OR -> {
                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof BoolBlock) && (b instanceof BoolBlock)){
                        boolean aVal = ((BoolBlock) a).getDate();
                        boolean bVal = ((BoolBlock) b).getDate();
                        stack.push(new BoolBlock(bVal || aVal));
                    }
                }
                case AND -> {
                    Block a = stack.pop();
                    Block b = stack.pop();
                    if((a instanceof BoolBlock) && (b instanceof BoolBlock)){
                        boolean aVal = ((BoolBlock) a).getDate();
                        boolean bVal = ((BoolBlock) b).getDate();
                        stack.push(new BoolBlock(bVal && aVal));
                    }
                }
                case NOT -> {
                    Block a = stack.pop();
                    if((a instanceof BoolBlock)){
                        boolean aVal = ((BoolBlock) a).getDate();
                        stack.push(new BoolBlock(!aVal));
                    }
                }
                case ALLOC -> storage = new Block[((IntBlock)curr.getBlock()).getDate()];
                case LOAD -> stack.push(storage[((IntBlock)curr.getBlock()).getDate()]);
                case STORE -> storage[((IntBlock)curr.getBlock()).getDate()] = stack.pop();
                case READ -> stack.push(new IntBlock(s.nextInt()));
                case JUMP -> pc = ((IntBlock) curr.getBlock()).getDate()-1;
                case FJUMP -> {
                    Block a = stack.pop();
                    if((a instanceof BoolBlock)){
                        boolean aVal = ((BoolBlock) a).getDate();
                        pc = aVal ? pc : ((IntBlock) curr.getBlock()).getDate()-1;
                    }
                }
            }

        }
        s.close();
    }

    private List<Operation> tokenize(String s) {

        String[] lines = s.split("\n");

        List<Operation> result = new ArrayList<>();

        for(String str : lines){
            String[] ope = str.split(" ");
            if (ope.length < 1)
                continue;
            else if (ope.length > 3){
                // TODO: add exception
            }
            else {
                switch (ope[0]){
                    case "CONST" -> result.add(new Operation(OperationType.CONST, new IntBlock(Integer.parseInt(ope[1]))));
                    case "ADD" -> result.add(new Operation(OperationType.ADD));
                    case "WRITE" -> result.add(new Operation(OperationType.WRITE));
                    case "READ" -> result.add(new Operation(OperationType.READ));
                    case "SUB" -> result.add(new Operation(OperationType.SUB));
                    case "MUL" -> result.add(new Operation(OperationType.MUL));
                    case "DIV" -> result.add(new Operation(OperationType.DIV));
                    case "NEG" -> result.add(new Operation(OperationType.NEG));
                    case "MOD" -> result.add(new Operation(OperationType.MOD));
                    case "TRUE" -> result.add(new Operation(OperationType.TRUE, new BoolBlock(true)));
                    case "FALSE" -> result.add(new Operation(OperationType.FALSE, new BoolBlock(false)));
                    case "LESS" -> result.add(new Operation(OperationType.LESS));
                    case "LEQ" -> result.add(new Operation(OperationType.LEQ));
                    case "EQ" -> result.add(new Operation(OperationType.EQ));
                    case "NEQ" -> result.add(new Operation(OperationType.NEQ));
                    case "OR" -> result.add(new Operation(OperationType.OR));
                    case "AND" -> result.add(new Operation(OperationType.AND));
                    case "NOT" -> result.add(new Operation(OperationType.NOT));
                    case "ALLOC" -> result.add(new Operation(OperationType.ALLOC, new IntBlock(Integer.parseInt(ope[1]))));
                    case "LOAD" -> result.add(new Operation(OperationType.LOAD, new IntBlock(Integer.parseInt(ope[1]))));
                    case "STORE" -> result.add(new Operation(OperationType.STORE, new IntBlock(Integer.parseInt(ope[1]))));
                    case "JUMP" -> result.add(new Operation(OperationType.JUMP, new IntBlock(Integer.parseInt(ope[1]))));
                    case "FJUMP" -> result.add(new Operation(OperationType.FJUMP, new IntBlock(Integer.parseInt(ope[1]))));
                    default -> System.out.println("error");
                }

            }


        }

        return result;
    }

}
