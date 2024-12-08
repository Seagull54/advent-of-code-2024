package info.lbov.y2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class D6 {
    Map<Direction, Direction> nextDirections = Map.of(Direction.UP, Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.LEFT, Direction.LEFT, Direction.UP);
    char[][] map;
    Map<Integer, Map<Integer, Set<Direction>>> pathsMap = new HashMap<>();
    int x = 0, y = 0;
    int path = 0;
    Direction direction;

    public static void main(String[] args) throws IOException {
        D6 d6 = new D6();

        char[][] map = d6.readMap("src/main/resources/y2024/d6in.txt");
        char[][] cleanMap = deepCopy(map);
        int startX = d6.x, startY = d6.y;
        d6.iterateOverMapPt1();

        List<char[][]> maps = new ArrayList<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 'F') {
                    char[][] newMap = deepCopy(cleanMap);
                    newMap[i][j] = '#';
                    maps.add(newMap);
                }
            }
        }

        long cycled = maps.stream().parallel().map(
            arr -> {
                D6 d = new D6();
                d.map = arr;
                d.x = startX;
                d.y = startY;
                d.direction = Direction.UP;
                return d;
            }
        ).filter(D6::iterateOverMapPt2)
                .count();
        System.out.println(cycled);
    }

    public char[][] readMap(String mapPath) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(mapPath));
        map = new char[lines.size()][lines.getFirst().length()];

        for (int i = 0; i < lines.size(); i++) {
            map[i] = lines.get(i).toCharArray();
        }

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == '^') {
                    y = i;
                    x = j;
                    direction = Direction.UP;
                }
            }
        }
        return map;
    }

    public void iterateOverMapPt1() {
        boolean stillOnMap = true;
        while (stillOnMap) {
            if (map[y][x] != 'F') {
                map[y][x] = 'F';
                path++;
            }

            int newX = x + direction.x;
            int newY = y + direction.y;

            if (newX < 0 || newX >= map[0].length || newY < 0 || newY >= map.length) return;
            if (map[newY][newX] == '#') {
                direction = nextDirections.get(direction);

                for (int i = 0; i < map.length; i++) {
                    System.out.println(Arrays.toString(map[i]));
                }
                System.out.println();
                continue;
            }
            x = newX;
            y = newY;
        }
    }

    public boolean iterateOverMapPt2() {
        while (true) {
            if (!pathsMap.containsKey(y)) {
                pathsMap.put(y, new HashMap<>());
            }
            if (!pathsMap.get(y).containsKey(x)) {
                pathsMap.get(y).put(x, new HashSet<>());
            }
            if (pathsMap.get(y).get(x).contains(direction)) {
                System.out.println("found loop");
                return true;
            }
            pathsMap.get(y).get(x).add(direction);

            int newX = x + direction.x;
            int newY = y + direction.y;

            if (newX < 0 || newX >= map[0].length || newY < 0 || newY >= map.length) return false;
            if (map[newY][newX] == '#') {
                direction = nextDirections.get(direction);
                continue;
            }
            x = newX;
            y = newY;
        }
    }


    private enum Direction {
        UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

        final int x;
        int y;
        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

    public static char[][] deepCopy(char[][] original) {
        char[][] copy = new char[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[0].length);
        }
        return copy;
    }
}
