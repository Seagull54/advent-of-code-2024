package info.lbov.y2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class D8 {

    public static void main(String[] args) throws IOException {
        D8 d8 = new D8();
        System.out.println(d8.solveAntinodes("src/main/resources/y2024/d8_test.txt"));
    }

    public int solveAntinodes(String mapPath) throws IOException {
        char[][] map = Files.readAllLines(Path.of(mapPath)).stream().map(String::toCharArray).toList().toArray(new char[0][]);
        char[][] antinodeMap = new char[map.length][map[0].length];
        Map<Character, List<Pair>> antennas = new HashMap<>();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] != '.') {
                    putCharToMap(antennas, map[i][j], new Pair(i, j));
                }
                antinodeMap[i][j] = '.';
            }
        }

        antennas.forEach((key, value) -> {
            while (value.size() > 1) {
                Pair first = value.getFirst();
                value.removeFirst();
                for (Pair other : value) {
                    solveAntinodes(antinodeMap, first, other);
                }
            }
        });

        int count = 0;
        for (char[] chars : antinodeMap) {
            System.out.println(chars);
            for (char aChar : chars) {
                if (aChar == '#') {
                    count++;
                }
            }
        }

        return count;
    }

    public record Pair(int y, int x) {}

    public void putCharToMap(Map<Character, List<Pair>> antennas, char c, Pair pair) {
        if (!antennas.containsKey(c)) {
            antennas.put(c, new ArrayList<>());
        }
        antennas.get(c).add(pair);
    }

    public void solveAntinodes(char[][] antinodes, Pair first, Pair second) {
        int xDiff = first.x - second.x;
        int yDiff = first.y - second.y;

        antinodes[first.y][first.x] = '#';
        antinodes[second.y][second.x] = '#';
        try {
            int i = 1;
            while (true) {
                antinodes[first.y + yDiff * i][first.x + xDiff * i] = '#';
                i++;
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
        try {
            int i = 1;
            while (true) {
                antinodes[second.y - yDiff * i][second.x - xDiff * i] = '#';
                i++;
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
    }
}
