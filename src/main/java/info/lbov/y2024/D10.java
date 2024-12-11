package info.lbov.y2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class D10 {

    public static void main(String[] args) throws IOException {
        D10 d10 = new D10();
        int[][] map = d10.readMap("src/main/resources/y2024/d10_input");

        System.out.println(d10.solvePt1(map));
        System.out.println(d10.solvePt2(map));

    }

    public int[][] readMap(String fileName) throws IOException {
        char[][] map = Files.readAllLines(Path.of(fileName)).stream().map(String::toCharArray).toList().toArray(new char[0][]);
        int[][] result = new int[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                result[i][j] = map[i][j] - '0';
            }
        }
        return result;
    }

    public int solvePt1(int[][] map) {
        Map<Integer, Map<Integer, Set<Pair>>> endsMap = new HashMap<>();
        Set<Pair> pairs = new HashSet<>();

        Deque<PairWithMap> placesToCheck = new ArrayDeque<>();

        for (int y = 0; y < map.length; y++) {
            endsMap.put(y, new HashMap<>());
            for (int x = 0; x < map[y].length; x++) {
                endsMap.get(y).put(x, new HashSet<>());
                if (map[y][x] == 9) {
                    placesToCheck.add(new PairWithMap(y, x, Set.of(new Pair(y, x))));
                }
            }
        }

        while (!placesToCheck.isEmpty()) {
            PairWithMap currentPair = placesToCheck.pop();
            int value = map[currentPair.y][currentPair.x];
            if (value == 0 ) pairs.add(new Pair(currentPair.y, currentPair.x));
            endsMap.get(currentPair.y).get(currentPair.x).addAll(currentPair.pairs);
            try {
                if (map[currentPair.y - 1][currentPair.x]+1 == value) {
                    placesToCheck.add(new PairWithMap(currentPair.y - 1, currentPair.x, new HashSet<>(currentPair.pairs)));
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
            try {
                if (map[currentPair.y + 1][currentPair.x]+1 == value) {
                    placesToCheck.add(new PairWithMap(currentPair.y + 1, currentPair.x, new HashSet<>(currentPair.pairs)));
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
            try {
                if (map[currentPair.y][currentPair.x-1]+1 == value) {
                    placesToCheck.add(new PairWithMap(currentPair.y, currentPair.x - 1, new HashSet<>(currentPair.pairs)));
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
            try {
                if (map[currentPair.y][currentPair.x+1]+1 == value) {
                    placesToCheck.add(new PairWithMap(currentPair.y, currentPair.x + 1, new HashSet<>(currentPair.pairs)));
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
        }

        return pairs.stream().map(
            pair -> endsMap.get(pair.y).get(pair.x).size()
        ).reduce(Integer::sum).orElse(0);
    }

    public int solvePt2(int[][] map) {
        Set<Pair> pairs = new HashSet<>();

        Deque<Pair> placesToCheck = new ArrayDeque<>();

        int[][] ratings = new int[map.length][map[0].length];

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == 9) {
                    ratings[y][x] = 1;
                    placesToCheck.add(new Pair(y, x));
                }
            }
        }

        while (!placesToCheck.isEmpty()) {
            Pair currentPair = placesToCheck.pop();
            int value = map[currentPair.y][currentPair.x];
            if (value == 0 ) pairs.add(new Pair(currentPair.y, currentPair.x));
            ratings[currentPair.y][currentPair.x]++;

            try {
                if (map[currentPair.y - 1][currentPair.x]+1 == value) {
                    placesToCheck.add(new Pair(currentPair.y - 1, currentPair.x));
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
            try {
                if (map[currentPair.y + 1][currentPair.x]+1 == value) {
                    placesToCheck.add(new Pair(currentPair.y + 1, currentPair.x));
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
            try {
                if (map[currentPair.y][currentPair.x-1]+1 == value) {
                    placesToCheck.add(new Pair(currentPair.y, currentPair.x - 1));
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
            try {
                if (map[currentPair.y][currentPair.x+1]+1 == value) {
                    placesToCheck.add(new Pair(currentPair.y, currentPair.x + 1));
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
        }

        return pairs.stream().map(
                pair -> ratings[pair.y][pair.x]
        ).reduce(Integer::sum).orElse(0);
    }



    public static record Pair(int y, int x) {}
    public static record PairWithMap(int y, int x, Set<Pair> pairs) {}
}
