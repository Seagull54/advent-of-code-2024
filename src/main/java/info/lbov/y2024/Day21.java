package info.lbov.y2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day21 {

    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(Path.of("src/main/resources/y2024/d21_test.txt"));
        DirectionKeyPad directionKeyPad = new DirectionKeyPad();
        long answer = input.stream().map(str -> {
            // расчет первичного пути на цифровом
            NumericKeyPad numericKeyPad = new NumericKeyPad();
            Map<String, Long> state = new HashMap<>();
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                String path = numericKeyPad.findPathToKey(c);
                numericKeyPad.setKey(c);
                state.merge(path, 1L, Long::sum);
            }


            int cycles = 25;
            for (int i = 0; i < cycles; i++) {
                state = iterate(directionKeyPad, state);
            }
            Long len = state.entrySet().stream().map(entry -> entry.getValue() * entry.getKey().length()).reduce(Long::sum).orElse(0L);
            System.out.println(len + " * " + Integer.parseInt(str.substring(0, str.length() - 1)));
            return len * Long.parseLong(str.substring(0, str.length() - 1));
        }).reduce(Long::sum).orElse(0L);
        directionKeyPad.cache.entrySet().forEach(e -> System.out.println(e));
        System.out.println(answer);
    }

    private static Map<String, Long> iterate(DirectionKeyPad keyPad, Map<String, Long> state2) {
        Map<String, Long> newState2 = new HashMap<>();
        state2.forEach(
                (key, value) -> {
                    Map<String, Long> addition = keyPad.parsePath(key);
                    addition.forEach(
                            (ak, av) -> {
                                newState2.merge(ak,  av * value, Long::sum);
                            }
                    );
                }
        );
        state2 = newState2;
        return state2;
    }

    static class NumericKeyPad {
        Map<Character, Key> keyMap = new HashMap<>();
        Key currentKey;
        NumericKeyPad() {
            keyMap.put('7', new Key(0, 0, '7'));
            keyMap.put('8', new Key(1, 0, '8'));
            keyMap.put('9', new Key(2, 0, '9'));
            keyMap.put('4', new Key(0, 1, '4'));
            keyMap.put('5', new Key(1, 1, '5'));
            keyMap.put('6', new Key(2, 1, '6'));
            keyMap.put('1', new Key(0, 2, '1'));
            keyMap.put('2', new Key(1, 2, '2'));
            keyMap.put('3', new Key(2, 2, '3'));
            keyMap.put('0', new Key(1, 3, '0'));
            keyMap.put('A', new Key(2, 3, 'A'));
            currentKey = keyMap.get('A');
        }

        String findPathToKey(Character k) {
            Key nextKey = keyMap.get(k);
            int x = currentKey.x - nextKey.x;
            int y = currentKey.y - nextKey.y;
            Direction horizontal = x > 0 ? Direction.LEFT : Direction.RIGHT;
            Direction vertical = y > 0 ? Direction.UP : Direction.DOWN;
            StringBuilder resStr = new StringBuilder();

            if ((currentKey.y == 3 && nextKey.x == 0) || horizontal != Direction.LEFT) {
                resStr.repeat(vertical.ch, Math.abs(y));
                resStr.repeat(horizontal.ch, Math.abs(x));
            } else {
                resStr.repeat(horizontal.ch, Math.abs(x));
                resStr.repeat(vertical.ch, Math.abs(y));
            }
            resStr.append(Direction.ACTION.ch);
            return resStr.toString();
        }

        void setKey(Character c) {
            currentKey = keyMap.get(c);
        }

    }

    static class DirectionKeyPad {
        Map<Direction, Key> keyMap = new HashMap<>();
        Map<Character, Key> charMap = new HashMap<>();
        Map<String, Map<String, Long>> cache = new HashMap<>();

        DirectionKeyPad() {
            keyMap.put(Direction.UP, new Key(1, 0, '^'));
            keyMap.put(Direction.ACTION, new Key(2, 0 , 'A'));
            keyMap.put(Direction.LEFT, new Key(0, 1 , '<'));
            keyMap.put(Direction.DOWN, new Key(1, 1 , 'v'));
            keyMap.put(Direction.RIGHT, new Key(2, 1 , '>'));

            charMap.put('<', keyMap.get(Direction.LEFT));
            charMap.put('>', keyMap.get(Direction.RIGHT));
            charMap.put('^', keyMap.get(Direction.UP));
            charMap.put('v', keyMap.get(Direction.DOWN));
            charMap.put('A', keyMap.get(Direction.ACTION));
        }

        public Map<String, Long> parsePath(String path) {
            if (cache.containsKey(path)) return cache.get(path);
            char prevChar = 'A';
            Map<String, Long> cacheAddition =  new HashMap<>();
            for (int i = 0; i < path.length(); i++) {
                char curChar = path.charAt(i);
                if (prevChar != curChar) {
                    cacheAddition.merge(findPathToKey(prevChar, curChar), 1L, Long::sum);
                    prevChar = curChar;
                    continue;
                }
                cacheAddition.merge("A", 1L, Long::sum);
            }
            cache.put(path, cacheAddition);
            return cacheAddition;
        }

        String findPathToKey(char from, char k) {
            Key currentKey = charMap.get(from);
            Key nextKey = charMap.get(k);
            int x = currentKey.x - nextKey.x;
            int y = currentKey.y - nextKey.y;
            Direction horizontal = x > 0 ? Direction.LEFT : Direction.RIGHT;
            Direction vertical = y > 0 ? Direction.UP : Direction.DOWN;
            StringBuilder resStr = new StringBuilder();
            if (((currentKey.y == 0 && nextKey.y == 1 && nextKey.x == 0) || horizontal != Direction.LEFT) && !(currentKey.x == 0 && currentKey.y == 1)) {
                resStr.repeat(vertical.ch, Math.abs(y));
                resStr.repeat(horizontal.ch, Math.abs(x));
            } else {
                resStr.repeat(horizontal.ch, Math.abs(x));
                resStr.repeat(vertical.ch, Math.abs(y));
            }
            resStr.append(Direction.ACTION.ch);
            return resStr.toString();
        }
    }

    enum Direction{
        UP('^'), DOWN('v'),  LEFT('<'), RIGHT('>'), ACTION('A');
        char ch;
        Direction(char ch) {
            this.ch = ch;
        }
    }

    record Key(int x, int y, char key) {}
}
