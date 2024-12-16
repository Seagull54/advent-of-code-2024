package info.lbov.y2024;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day14 {

    int secondsToPredict = 100;// tree!!!! 1111111111111111111111111111111
    private static String regex = "p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)";
    private static Pattern pattern = Pattern.compile(regex);
    private static String treeRegex = "1111111111111111111111111111111";
    private static Pattern treePattern = Pattern.compile(treeRegex);
    private static final int width = 101;
    private static final int height = 103;
    private static final String testFile = "src/main/resources/y2024/d14_test.txt";
    private static final String inputFile = "src/main/resources/y2024/d14_input.txt";

    public static void main(String[] args) throws IOException {

        Day14 day14 = new Day14();
//        List<RobotCase> cases = day14.readFile(testFile);
        List<RobotCase> cases = day14.readFile(inputFile);
//        System.out.println(day14.part1(cases, day14.secondsToPredict));
        int treeSeconds = day14.solvePtFile2(cases);
        System.out.println("tree found " + treeSeconds + " seconds");
    }

    private List<RobotCase> readFile(String fileName) throws IOException {
        return Files.readAllLines(Path.of(fileName)).stream().map(
                line -> {
                    Matcher matcher = pattern.matcher(line);
                    matcher.find();
                    return new RobotCase(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
                }).toList();
    }

    public int solvePt2(List<RobotCase> input) throws IOException {
        char[][] matrix = part2(input, 0);
        int i = 1;

        while (!validateMatrix(matrix)) {
            matrix = part2(input, i);
            i++;
        }

        return i - 1;
    }

    public int solvePtFile2(List<RobotCase> input) throws IOException {
        char[][] matrix = part2(input, 0);
        int i = 1;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("results3.txt", true))){
            while (i < 10000) {
                writer.write(String.valueOf(i));
                writer.newLine();
                matrix = part2(input, i);
                i++;
                for (int j = 0; j < matrix.length; j++) {
                    writer.write(matrix[j]);
                    writer.newLine();
                }
                writer.newLine();
            }
        }

        return i;
    }


    public int part1(List<RobotCase> input, int daysToPredict) throws IOException {
        Map<Boolean, Map<Boolean, Integer>> map = new HashMap<>();

        map.putIfAbsent(true, new HashMap<>(Map.of(true, 0, false, 0)));
        map.putIfAbsent(false, new HashMap<>(Map.of(true, 0, false, 0)));

        List<RobotPostition> postitions = input.stream().map(
            robotCase ->  {
                int newX = (robotCase.px + robotCase.vx() * daysToPredict) % width;
                int newY = (robotCase.py + robotCase.vy() * daysToPredict) % height;

                newX = newX == width ? 0 : newX;
                newY = newY == height ? 0 : newY;

                newX = newX >= 0 ? newX : width + newX;
                newY = newY >= 0 ? newY : height + newY;

                return new RobotPostition(newX, newY);
//
//                if (newX == (width-1)/2 || newY == (height-1)/2) return;
//                map.get(newY < (height-1)/2).merge(newX < (width-1)/2, 1, Integer::sum);
            }
        ).toList();

        char[][] matrix = new char[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y == (height-1)/2 || x == (width-1)/2) {
                    matrix[y][x] = '#';
                    continue;
                }
                matrix[y][x] = '.';
            }
        }

        postitions.forEach(
            postition -> {
                if (matrix[postition.y][postition.x] != '.' && matrix[postition.y][postition.x] != '#') {
                    matrix[postition.y][postition.x]++;
                } else {
                    matrix[postition.y][postition.x] = '1';
                }

                if (postition.x == (width-1)/2 || postition.y == (height-1)/2) return;
                map.get(postition.y < (height-1)/2).merge(postition.x < (width-1)/2, 1, Integer::sum);
            }
        );

        for (int i = 0; i < matrix.length; i++) {
            System.out.println(matrix[i]);
        }

        return map.values().stream().flatMap(m -> m.values().stream()).reduce(1, (a, b) -> a * b);

    }

    public char[][] part2(List<RobotCase> input, int daysToPredict) throws IOException {
        Map<Boolean, Map<Boolean, Integer>> map = new HashMap<>();

        map.putIfAbsent(true, new HashMap<>(Map.of(true, 0, false, 0)));
        map.putIfAbsent(false, new HashMap<>(Map.of(true, 0, false, 0)));

        List<RobotPostition> postitions = input.stream().map(
                robotCase ->  {
                    int newX = (robotCase.px + robotCase.vx() * daysToPredict) % width;
                    int newY = (robotCase.py + robotCase.vy() * daysToPredict) % height;

                    newX = newX == width ? 0 : newX;
                    newY = newY == height ? 0 : newY;

                    newX = newX >= 0 ? newX : width + newX;
                    newY = newY >= 0 ? newY : height + newY;

                    return new RobotPostition(newX, newY);
                }
        ).toList();

        char[][] matrix = new char[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y == (height-1)/2 || x == (width-1)/2) {
                    matrix[y][x] = '#';
                    continue;
                }
                matrix[y][x] = '.';
            }
        }

        postitions.forEach(
                postition -> {
                    if (matrix[postition.y][postition.x] != '.' && matrix[postition.y][postition.x] != '#') {
                        matrix[postition.y][postition.x]++;
                    } else {
                        matrix[postition.y][postition.x] = '1';
                    }

                    if (postition.x == (width-1)/2 || postition.y == (height-1)/2) return;
                    map.get(postition.y < (height-1)/2).merge(postition.x < (width-1)/2, 1, Integer::sum);
                }
        );

//        for (int i = 0; i < matrix.length; i++) {
//            System.out.println(matrix[i]);
//        }

        return matrix;
    }

    public boolean validateMatrix(char[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            Matcher matcher = treePattern.matcher(new String(matrix[i]));
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    // too low 90285120

    private record RobotCase (int px, int py, int vx, int vy) {}

    private record RobotPostition (int x, int y) {}
}
