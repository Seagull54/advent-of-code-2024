package info.lbov.y2024;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day17 {

    public static void main(String[] args) {
        ElvenVM testVM = new ElvenVM(0b110001001001010111110100111100010101111001101101L, 0, 0, "2,4,1,3,7,5,0,3,1,5,4,1,5,5,3,0");
        testVM.solvePart1();
        System.out.println(testVM.getStringCache());

        ElvenVM pt1VM = new ElvenVM(63687530, 0, 0, "2,4,1,3,7,5,0,3,1,5,4,1,5,5,3,0");
        pt1VM.solvePart1();
        System.out.println(pt1VM.getStringCache());
    }


    private static class ElvenVM {
        private long registerA;
        private long registerB;
        private long registerC;
        private Instruction instruction;
        private final List<Integer> stack = new ArrayList<>();
        private final List<Integer> returnStack = new ArrayList<>();

        public ElvenVM(long registerA, int registerB, int registerC, String operations) {
            this.registerA = registerA;
            this.registerB = registerB;
            this.registerC = registerC;
            for (String op : operations.split(",")) {
                stack.add(Integer.parseInt(op));
            }
        }

        public void solvePart1() {
            int instructionPointer = 0;
            while (instructionPointer < stack.size()) {
                instruction = new Instruction(stack.get(instructionPointer), stack.get(instructionPointer + 1), comboOperand(stack.get(instructionPointer + 1)));
                if (instruction.opcode == 3) {
                    if (registerA != 0) {
                        instructionPointer = instruction.literal;
                        continue;
                    }
                }
                switch (instruction.opcode) {
                    case 0 -> adv();
                    case 1 -> bxl();
                    case 2 -> bst();
                    case 3 -> jhz();
                    case 4 -> bxc();
                    case 5 -> out();
                    case 6 -> bdv();
                    case 7 -> cdv();
                }
                instructionPointer+=2;
            }
        }

        private String getStringCache() {
            String result = returnStack.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            returnStack.clear();;
            return result;
        }

        private void adv() {
            registerA = registerA >> instruction.combo;
        }

        private void bxl() {
            registerB = registerB ^ instruction.literal;
        }

        private void bst() {
            registerB = instruction.combo % 8;
        }

        private void jhz() {}

        private void bxc() {
            registerB = registerB ^ registerC;
        }

        private void out() {
            returnStack.add((int) (instruction.combo % 8));
        }

        private void bdv() {
            registerB = registerA >> instruction.combo;
        }

        private void cdv() {
            registerC = registerA >> instruction.combo;
        }

        private long comboOperand(long operand) {
            return switch ((int) operand) {
                case 0 -> 0;
                case 1 -> 1;
                case 2 -> 2;
                case 3 -> 3;
                case 4 -> registerA;
                case 5 -> registerB;
                case 6 -> registerC;
                default -> throw new IllegalArgumentException("Invalid operand: " + operand);
            };
        }

        private record Instruction(int opcode, int literal, long combo) {}
    }
}
