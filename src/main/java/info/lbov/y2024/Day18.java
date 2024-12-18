package info.lbov.y2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static info.lbov.y2024.Day16.printIntMap;

public class Day18 {
    private static String testFile =  "src/main/resources/y2024/day18_input.txt";
    private static final int maxSize = 71;
    private static final int piecesToFall = 1024;

    public static void main(String[] args) throws IOException {
        List<Coordinate> coordinates = readInput(testFile);

        char[][] map = new char[maxSize][maxSize];

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = '.';
            }
        }


        int fallingPieces = Math.min(piecesToFall, coordinates.size());
        for (int i = 0; i < fallingPieces; i++) {
            Coordinate coordinate = coordinates.get(i);
            map[coordinate.y][coordinate.x] = '#';
        }
        int i = piecesToFall;
        Coordinate coordinate = coordinates.get(i);
        map[coordinate.y][coordinate.x] = '#';
        while (solvePt1(map) > 0) {
            i++;
            coordinate = coordinates.get(i);
            map[coordinate.y][coordinate.x] = '#';
        }
        System.out.println(coordinate.x + "," + coordinate.y);
    }

    public static char[][] deepMapCopy(char[][] map) {
        char[][] copy = new char[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                copy[i][j] = map[i][j];
            }
        }
        return copy;
    }


    private static List<Coordinate> readInput(String filename) throws IOException {
        return Files.readAllLines(Path.of(filename))
                .stream()
                .map(line -> line.split(","))
                .map(ar -> new Coordinate(Integer.parseInt(ar[0]), Integer.parseInt(ar[1]), 0)).toList();
    }

    private static int solvePt1(char[][] mapStart) {
        char[][]map = deepMapCopy(mapStart);
        int[][]scoreMap = new int[map.length][map[0].length];

        for (int i = 0; i < scoreMap.length; i++) {
            Arrays.fill(scoreMap[i], 0);
        }

        printMap(map);

        Deque<Coordinate> queue = new LinkedList<>();
        queue.offer(new Coordinate(0, 0, 0));

        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();

            if (current.x == maxSize - 1 && current.y == maxSize - 1) {
                return current.score;
            }
            if (map[current.y][current.x] != '.') {
                continue;
            }
            map[current.y][current.x] = '0';


            scoreMap[current.y][current.x] = current.score;
            if (current.x > 0 && map[current.y][current.x - 1] != '#' && scoreMap[current.y][current.x - 1] == 0) {
                queue.offer(new Coordinate(current.x - 1, current.y, current.score + 1));
            }
            if (current.x < map.length - 1 && map[current.y][current.x + 1] != '#' && scoreMap[current.y][current.x + 1] == 0) {
                queue.offer(new Coordinate(current.x + 1, current.y, current.score + 1));
            }
            if (current.y > 0 && map[current.y - 1][current.x] != '#'  && scoreMap[current.y - 1][current.x] == 0) {
                queue.offer(new Coordinate(current.x, current.y - 1, current.score + 1));
            }
            if (current.y < map[0].length-1 && map[current.y + 1][current.x] != '#' &&  scoreMap[current.y + 1][current.x] == 0) {
                queue.offer(new Coordinate(current.x, current.y + 1, current.score + 1));
            }
        }

        printIntMap(scoreMap);
        return 0;
    }


    private static void printMap(char[][] map) {
        for (int j = 0; j < map.length; j++) {
            System.out.println(new String(map[j]));
        }
    }

    private record Coordinate(int x, int y, int score) {}
}
