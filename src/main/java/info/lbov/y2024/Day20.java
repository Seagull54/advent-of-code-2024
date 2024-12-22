package info.lbov.y2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static info.lbov.y2024.Day16.printIntMap;
import static info.lbov.y2024.Day16.printMap;

public class Day20 {

    private static String testFile = "src/main/resources/y2024/d20_test.txt";
    private static String inputFile = "";

    public static void main(String[] args) throws IOException {

        char[][]map = readMap(testFile);
        char[][]wallsAroundMap = new char[map.length][map[0].length];
        int[][]pathMap = new int[map.length][map[0].length];

        Pair startPos = null;
        Pair endPos = null;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 'S') {
                    startPos = new Pair(j, i, 0);
                }
                if (map[i][j] == 'E') {
                    endPos = new Pair(j, i, 0);
                }
                wallsAroundMap[i][j] = '.';
                pathMap[i][j] = -1;
            }
        }

        while (!(startPos.x == endPos.x && startPos.y == endPos.y)) {
            pathMap[startPos.y][startPos.x] = startPos.score;
            Pair next = null;
            if (startPos.y + 1 < map.length) {
                if (map[startPos.y + 1][startPos.x] == '#') {
                    wallsAroundMap[startPos.y+1][startPos.x] = '#';
                } else if (pathMap[startPos.y + 1][startPos.x] == -1) {
                    next = new Pair(startPos.x, startPos.y + 1, startPos.score +  1);
                }
            }
            if (startPos.y - 1 >= 0) {
                if (map[startPos.y - 1][startPos.x] == '#') {
                    wallsAroundMap[startPos.y-1][startPos.x] = '#';
                } else if (pathMap[startPos.y - 1][startPos.x] == -1) {
                    next = new Pair(startPos.x, startPos.y - 1, startPos.score +  1);
                }
            }
            if (startPos.x + 1 < map[0].length) {
                if (map[startPos.y][startPos.x + 1] == '#') {
                    wallsAroundMap[startPos.y][startPos.x + 1] = '#';
                } else if (pathMap[startPos.y][startPos.x + 1] == -1) {
                    next = new Pair(startPos.x + 1, startPos.y, startPos.score +  1);
                }
            }
            if (startPos.x - 1 >= 0) {
                if (map[startPos.y][startPos.x - 1] == '#') {
                    wallsAroundMap[startPos.y][startPos.x - 1] = '#';
                } else if (pathMap[startPos.y][startPos.x - 1] == -1) {
                    next = new Pair(startPos.x - 1, startPos.y, startPos.score +  1);
                }
            }
            startPos = next;
        }

        pathMap[endPos.y][endPos.x] = startPos.score;
        printMap(wallsAroundMap);


        int counter = 0;

        Map<Integer, Integer> cheats = new HashMap<>();
        for (int y = 1; y < map.length - 1; y++) {
            for (int x = 1; x < map.length - 1; x++) {
                if (wallsAroundMap[y][x] != '#') continue;
                int cheat = 0;
                List<Integer> neighbours = List.of(pathMap[y + 1][x], pathMap[y - 1][x], pathMap[y][x + 1], pathMap[y][x - 1]);
                int maxPath = neighbours.stream().filter(n -> n >= 0).max(Integer::compareTo).orElse(0);
                int minPath = neighbours.stream().filter(n -> n >= 0).min(Integer::compareTo).orElse(0);

                if (pathMap[y][x-1] == 84) {
                    System.out.println();
                    System.out.println(maxPath);
                    System.out.println(minPath);
                }
                cheat = maxPath - minPath - 2;

                if (cheat > 0) cheats.merge(cheat, 1, Integer::sum);
                if (cheat >= 100) counter++;
            }
        }

//        printIntMap(pathMap);
        System.out.println(counter);
        // pt2
        cheats = new HashMap<>();
        counter = 0;


        for (int y = 1; y < map.length - 1; y++) {
            for (int x = 1; x < map.length - 1; x++) {
                if (pathMap[y][x] < 0) continue;

                int minYWindow = Math.max(0, y - 20);
                int maxYWindow = Math.min(pathMap.length - 1, y + 20);
                for (int i = y - 20; i < y + 21; i++) {
                    int diff = 20 - Math.abs(i - y);
                    for (int j = x - diff; j <= x + diff; j++) {
                        if (i < 0 || i > pathMap.length - 1) continue;
                        if (j < 0 || j > pathMap[0].length - 1) continue;
                        if (pathMap[i][j] <= pathMap[y][x]) continue;

                        int cheat = pathMap[i][j] - pathMap[y][x];
                        int stepsToCheat = Math.abs(i - y) + Math.abs(j - x);

                        cheat -= stepsToCheat;

//                        if (cheat >= 50) cheats.merge(cheat, 1, Integer::sum);
                        if (cheat >= 100) counter++;
                    }
                }
            }
        }
        System.out.println(counter);

        System.out.println(cheats);
    }

    // high 1268645

    private static int findMaxCheat(Pair pos, int[][] pathMap) {


        return 0;
    }

    private static char[][] readMap(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(fileName));
        char[][] map = new char[lines.size()][lines.get(0).length()];

        for (int i = 0; i < lines.size(); i++) {
            map[i] = lines.get(i).toCharArray();
        }
        return map;
    }


    private record Pair(int x, int y, int score){}
}
