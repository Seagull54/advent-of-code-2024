package info.lbov.y2024;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day11 {
    public static String input = "1750884 193 866395 7 1158 31 35216 0";
    public static String testInput = "125 17";
    public static String expected = "1750884 193 866395 7 1158 31 35216 0";

    public static void main(String[] args) {
        Map<String, Long> currentStones = Arrays.stream(testInput.split(" ")).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<String, Long> expectedStones = Arrays.stream(expected.split(" ")).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for (int i = 0; i < 75; i++) {
            Map<String, Long> newStones = new HashMap<>();
            currentStones.forEach((key, value) -> {
                List<String> stones = applyTransform(key);
                stones.forEach(s -> newStones.merge(s, value, Long::sum));
            });
            currentStones = newStones;
            System.out.println("day " + i + ": " + currentStones.values().stream().mapToLong(Long::longValue).sum());

        }
        System.out.println(currentStones.values().stream().mapToLong(Long::longValue).sum());
    }

    public static List<String> applyTransform(String stone){
        stone = cutStone(stone);
        while (stone.charAt(0) == '0' && stone.length() > 1) {
            stone = stone.substring(1);
        }
        if (stone.length() % 2 == 0) {
            return List.of(stone.substring(0, stone.length() / 2), stone.substring(stone.length() / 2));
        }

        if ("0".equals(stone)) {
            return List.of("1");
        }
        return List.of(String.valueOf(Long.parseLong(stone) * 2024));
    }

    public static String cutStone(String stone) {
        while (stone.charAt(0) == '0' && stone.length() > 1) {
            stone = stone.substring(1);
        }
        return stone;
    }
}
