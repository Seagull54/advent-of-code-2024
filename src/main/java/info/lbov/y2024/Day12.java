package info.lbov.y2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day12 {
    private static String testFile = "src/main/resources/y2024/d12test.txt";
    private static String inputFile = "src/main/resources/y2024/d12input.txt";

    public static void main(String[] args) throws IOException {

        Day12 day12 = new Day12();
        char[][] map = day12.readFile(inputFile);
//        char[][] map = day12.readFile(testFile);
        int result = day12.solvePart1(map);
        System.out.println(result);
    }

    public char[][] readFile(String fileName) throws IOException {
        return Files.readAllLines(Path.of(fileName)).stream().map(String::toCharArray).toList().toArray(new char[0][]);
    }


    public int solvePart1(char[][] board) {

        Region[][] regions = new Region[board.length][board[0].length];
        List<Set<Region>> sets = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.println("X:" + (j+1) + "/" + board[0].length + " Y:" + (i+1) + "/" + board.length + " char " + board[i][j]);
                boolean isTopNew = i==0 || board[i][j] != board[i-1][j];
                boolean isLeftNew = j ==0 || board[i][j] != board[i][j-1];
                boolean isBottomNew = i == board.length - 1 || board[i][j] != board[i+1][j];
                boolean isRightNew = j == board[0].length - 1 || board[i][j] != board[i][j+1];

                int perimeter = 0;

                if (isRightNew) perimeter++;
                if (isLeftNew) perimeter++;
                if (isTopNew) perimeter++;
                if (isBottomNew) perimeter++;

                int sides = 0;
                if (isTopNew && isLeftNew) sides+=2;
                if (isBottomNew && isRightNew) sides+=2;
                try {
                    if (!isTopNew && !isRightNew &&
                            board[i][j] != board[i - 1][j + 1]) sides += 2;
                    if (!isBottomNew && !isLeftNew &&
                            board[i][j] != board[i + 1][j - 1]) sides += 2;
                } catch (IndexOutOfBoundsException e) {}

                if (sides > 0) {
                    System.out.println(j + "-" + i + " " + sides);
                }

                Region newR = new Region(sides, 1, board[i][j], null);
                regions[i][j] = newR;


                if (isTopNew && isLeftNew) {
                    Set<Region> set = new HashSet<>();
                    sets.add(set);
                    newR.container = set;
                    set.add(regions[i][j]);
                    continue;
                }
                if (isTopNew) {
                    Region region = regions[i][j - 1];
                    Set<Region> set = region.container;
                    set.add(newR);
                    newR.container = set;
                    continue;
                }
                if (isLeftNew) {
                    Region region = regions[i-1][j];
                    Set<Region> set = region.container;
                    set.add(newR);
                    newR.container = set;
                    continue;
                }

                Region topRegion = regions[i-1][j];
                Region leftRegion = regions[i][j - 1];

                if (topRegion.container != leftRegion.container) {
                    sets.remove(topRegion.container);
                    leftRegion.container.addAll(topRegion.container);
                    topRegion.container.forEach(region -> region.container = leftRegion.container);
                }

                Set<Region> set = leftRegion.container;
                set.add(newR);
                newR.container = set;
            }
        }

        int result = sets.stream().map(
                set -> {
                    int perimeter = 0;
                    int area = 0;
                    for (Region region : set) {

                        perimeter += region.perimeter;
                        area += region.area;
                    }
                    Region region = set.stream().findFirst().get();
                    System.out.println("Character: " + region.character + " " + area + " * " + perimeter);
                    return area * perimeter;
                }).reduce(0, Integer::sum);


        return result;
    }



    public static class Region {
        public Set<Region> container;
        public char character;
        public int perimeter;
        public int area;
        public int sides;

        public Region(int perimeter, int area, char character, Set<Region> container) {
            this.perimeter = perimeter;
            this.area = area;
            this.character = character;
            this.container = container;
        }
    }

    // 1317484 too low
    // 1363659 too low
    // 873584
    // 6612876
    // 1452678
    // 245623

    public enum Side {
        TOP, LEFT, RIGHT, BOTTOM
    }
}
