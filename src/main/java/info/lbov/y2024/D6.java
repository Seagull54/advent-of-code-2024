package info.lbov.y2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class D6 {
    Map<Direction, Direction> nextDirections = Map.of(Direction.UP, Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.LEFT, Direction.LEFT, Direction.UP);
    char[][] map;
    int x = 0, y = 0;
    int path = 0;
    Direction direction;

    public static void main(String[] args) throws IOException {
        D6 d6 = new D6();

        d6.readMap("src/main/resources/y2024/d6in.txt");
        d6.iterateOverMapPt1();
        System.out.println(d6.path);
    }

    public void readMap(String mapPath) throws IOException {
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
                    return;
                }
            }
        }
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

    private enum Direction {
        UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

        final int x;
        int y;
        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }
}
