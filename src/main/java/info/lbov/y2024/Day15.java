package info.lbov.y2024;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day15 {

    private static String testMap = "src/main/resources/y2024/day15map_test.txt";
    private static String testExplodedMap = "src/main/resources/y2024/day15map_exploded_test";
    private static String testPath = "src/main/resources/y2024/day15path_test.txt";
    private static String inputMap = "src/main/resources/y2024/day15map_input.txt";
    private static String inputPath = "src/main/resources/y2024/day15path_input.txt";

    private static Map<Character, Direction> directionMap = Map.of('<', Direction.LEFT, '>', Direction.RIGHT, '^', Direction.TOP, 'v', Direction.BOTTOM);


    public static void main(String[] args) throws IOException {
//        solvePt1();
        solvePt2();
    }

    private static void solvePt2() throws IOException {
        Day15 day15 = new Day15();
//        char[][] map = day15.readMap(testExplodedMap);
//        char[][] map = day15.readMapPt2(testMap);
        char[][] map = day15.readMapPt2(inputMap);
//        char[] inputs = day15.readInputs(testPath);
        char[] inputs = day15.readInputs(inputPath);

        int selfX = 0;
        int selfY = 0;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == '@') {
                    selfX = j;
                    selfY = i;
                }
            }
        }

        printMap(map);
        boolean moved = false;
        for (int i = 0; i < inputs.length; i++) {
            Direction direction = directionMap.get(inputs[i]);
            System.out.println(direction.toString());
            moved = day15.tryMoveRobot(map, selfX, selfY, direction);
            if (moved) {
                selfX += direction.x;
                selfY += direction.y;
            }
            printMap(map);
        }


        long sum = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == '[') sum+=i*100L+j;
            }
        }
        System.out.println(sum);

//        printMap(map);


    }



    private boolean tryMoveRobot(char[][] map, int x, int y, Direction direction) {
        int newX = x + direction.x;
        int newY = y + direction.y;

        if (map[newY][newX] == '#') return false;

        if (map[newY][newX] == '.') {
            map[newY][newX] = '@';
            map[y][x] = '.';
            return true;
        }

        if (map[newY][newX] == '[') {
            boolean moved = tryMoveBox(map, newX, newY, direction);
            if (moved) {
                moveBox(map, newX, newY, direction);
                map[newY][newX] = '@';
                map[y][x] = '.';
            }
            return moved;
        }

        if (map[newY][newX] == ']') {
            boolean moved = tryMoveBox(map, newX - 1, newY, direction);
            if (moved) {
                moveBox(map, newX - 1, newY, direction);
                map[newY][newX] = '@';
                map[y][x] = '.';
            }
            return moved;
        }

        return false;
    }

    private void moveBox(char[][] map, int x, int y, Direction direction) {
        if (Direction.verticalDirections.contains(direction)) {
            // x y always is start of box '['
            int vertLeft = x + direction.x;
            int vertRight = x + 1 + direction.x;
            int newY = y + direction.y;

            if (map[newY][vertLeft] == '[') {
                moveBox(map, vertLeft, newY, direction);
                map[newY][vertLeft] = '[';
                map[newY][vertRight] = ']';
                map[y][x] = '.';
                map[y][x + 1] = '.';
           }
            if (map[newY][vertLeft] == ']') {
                moveBox(map, vertLeft - 1, newY, direction);

                if (map[newY][vertRight] == '[') {
                    moveBox(map, vertRight, newY, direction);
                }
                map[newY][vertLeft] = '[';
                map[newY][vertRight] = ']';
                map[y][x] = '.';
                map[y][x + 1] = '.';
            }

            if (map[newY][vertRight] == '[') {
                moveBox(map, vertRight, newY, direction);
                map[newY][vertLeft] = '[';
                map[newY][vertRight] = ']';
                map[y][x] = '.';
                map[y][x + 1] = '.';
            }

            if (map[newY][vertLeft] == '.' || map[newY][vertRight] == '.') {
                map[newY][vertLeft] = '[';
                map[newY][vertRight] = ']';
                map[y][x] = '.';
                map[y][x + 1] = '.';
            }
        } else {
            int newX = x + direction.x;
            int newY = y + direction.y;
            if (Direction.LEFT == direction) {
                if (map[newY][newX] == '.') {
                    map[newY][newX] = '[';
                    map[newY][x] = ']';
                    map[newY][x + 1] = '.';
                }
                if (map[newY][newX] == ']') {
                    moveBox(map, newX - 1, newY, direction);
                    map[newY][newX] = '[';
                    map[newY][x] = ']';
                    map[newY][x + 1] = '.';

                }
            } else {
                int newX1 = newX + 1;
                if (map[newY][newX1] == '.') {
                    map[newY][newX] = '[';
                    map[newY][newX1] = ']';
                    map[newY][x] = '.';
                }
                if (map[newY][newX1] == '[') {
                    moveBox(map, newX1, newY, direction);
                    map[newY][newX] = '[';
                    map[newY][newX1] = ']';
                    map[newY][x] = '.';
                }
            }
        }
    }

    private boolean tryMoveBox(char[][] map, int x, int y, Direction direction) {
        if (Direction.verticalDirections.contains(direction)) {
            // x y always is start of box '['
            int vertLeft = x + direction.x;
            int vertRight = x + 1 + direction.x;
            int newY = y + direction.y;

            if (map[newY][vertLeft] == '#' || map[newY][vertRight] == '#') { return false; }

            if (map[newY][vertLeft] == '[') {
                boolean moved = tryMoveBox(map, vertLeft, newY, direction);
                return moved;
            }
            if (map[newY][vertLeft] == ']') {
                boolean left = tryMoveBox(map, vertLeft - 1, newY, direction);
                boolean right = false;
                if (map[newY][vertRight] == '[') {
                    right = tryMoveBox(map, vertRight, newY, direction);
                }
                if (map[newY][vertRight] == '.') {
                    right = true;
                }
                return left && right;
            }

            if (map[newY][vertRight] == '[') {
                boolean moved = tryMoveBox(map, vertRight, newY, direction);
                return moved;
            }

            if (map[newY][vertLeft] == '.' || map[newY][vertRight] == '.') {
                return true;
            }


        } else {
            int newX = x + direction.x;
            int newY = y + direction.y;
            if (Direction.LEFT == direction) {
                if (map[newY][newX] == '#') {
                    return false;
                }
                if (map[newY][newX] == '.') {
                    return true;
                }
                if (map[newY][newX] == ']') {
                    boolean moved = tryMoveBox(map, newX - 1, newY, direction);
                    return moved;
                }
            } else {
                int newX1 = newX + 1;
                if (map[newY][newX1] == '#') {
                    return false;
                }
                if (map[newY][newX1] == '.') {
                    return true;
                }
                if (map[newY][newX1] == '[') {
                    boolean moved = tryMoveBox(map, newX1, newY, direction);
                    return moved;
                }
            }

        }
        return false;
    }


    private static void solvePt1() throws IOException {
        Day15 day15 = new Day15();
//        char[][] map = day15.readMap(testMap);
        char[][] map = day15.readMap(inputMap);
//        char[] inputs = day15.readInputs(testPath);
        char[] inputs = day15.readInputs(inputPath);

        int selfX = 0;
        int selfY = 0;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == '@') {
                    selfX = j;
                    selfY = i;
                }
            }
        }
        for (int j = 0; j < map.length; j++) {
            System.out.println(new String(map[j]));
        }

        for (int i = 0; i < inputs.length; i++) {
            Direction direction = directionMap.get(inputs[i]);
            System.out.println(direction.toString());
            if (day15.tryMove(map, selfX, selfY, direction)) {
                selfX += direction.x;
                selfY += direction.y;
            };

            for (int j = 0; j < map.length; j++) {
                System.out.println(new String(map[j]));
            }
        }


        long gps = 0;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[1].length; j++) {
                if (map[i][j] == 'O') {
                    gps += i * 100L + j;
                }
            }
        }
        System.out.println("gps = " + gps);
    }

    private boolean tryMove(char[][] map, int x, int y, Direction direction) {
        if (map[y + direction.y][x + direction.x] == '.') {
            map[y + direction.y][x + direction.x] = map[y][x];
            map[y][x] = '.';
            return true;
        }
        if (map[y + direction.y][x + direction.x] == '#') {
            return false;
        }
        if (map[y + direction.y][x + direction.x] == 'O') {
            if (tryMove(map, x + direction.x, y + direction.y, direction)) {
                map[y + direction.y][x + direction.x] = map[y][x];
                map[y][x] = '.';
                return true;
            }
        }
        return false;
    }


    private char[][] readMap(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(fileName));
        char[][] map = new char[lines.size()][lines.get(0).length()];

        for (int i = 0; i < lines.size(); i++) {
            map[i] = lines.get(i).toCharArray();
        }
        return map;
    }

    private char[][] readMapPt2(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(fileName)).stream().map(
            line -> {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == '#') {
                        builder.append("##");
                    }
                    if (line.charAt(i) == '.') {
                        builder.append("..");
                    }
                    if (line.charAt(i) == 'O') {
                        builder.append("[]");
                    }
                    if (line.charAt(i) == '@') {
                        builder.append("@.");
                    }
                }
                return builder.toString();
            }
        ).toList();
        char[][] map = new char[lines.size()][lines.get(0).length()];

        for (int i = 0; i < lines.size(); i++) {
            map[i] = lines.get(i).toCharArray();
        }

        return map;
    }


    private char[] readInputs(String fileName) throws IOException {
        return String.join("", Files.readAllLines(Path.of(fileName))).toCharArray();
    }


    private static enum Direction {
        TOP(0, -1), BOTTOM(0, 1), LEFT(-1, 0), RIGHT(1, 0);

        public final static Set<Direction> verticalDirections = Set.of(TOP, BOTTOM);
        int x, y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static void printMap(char[][] map) {
        for (int j = 0; j < map.length; j++) {
            System.out.println(new String(map[j]));
        }
    }
}
// 1424798 low
// 1432898
// 1030635