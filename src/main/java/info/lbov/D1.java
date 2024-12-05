package info.lbov;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class D1 {


    public static void main(String[] args) throws IOException {
        System.out.println(similarity("src/main/resources/day1_2.txt"));
    }

    public static Integer difference() throws IOException {
        Path path = Paths.get("src/main/resources/day1.txt");
        List<Integer> first = new ArrayList<>(1000);
        List<Integer> second = new ArrayList<>(1000);

        Files.readAllLines(path).forEach(
                line -> {
                    String[] splitted = line.split("   ");
                    first.add(Integer.parseInt(splitted[0]));
                    second.add(Integer.parseInt(splitted[1]));
                }
        );
        first.sort(Integer::compareTo);
        second.sort(Integer::compareTo);
        int diff = 0;
        for (int i = 0; i < second.size(); i++) {
            diff += Math.abs(first.get(i) - second.get(i));
        }
        System.out.println(diff);
        return diff;
    }



    public static Integer similarity(String pathS) throws IOException {
        Map<Integer, Integer> map = new HashMap<>();

        Path path = Paths.get(pathS);
        List<Integer> first = new ArrayList<>(1000);
        List<Integer> second = new ArrayList<>(1000);

        Files.readAllLines(path).forEach(
                line -> {
                    String[] splitted = line.split("   ");
                    first.add(Integer.parseInt(splitted[0]));
                    second.add(Integer.parseInt(splitted[1]));
                }
        );

        second.forEach(
            number -> map.merge(number, 1, Integer::sum)
        );


        return first.stream()
                .map(number -> {
                    if (map.containsKey(number)) {
                        return map.get(number) * number;
                    }
                    return 0;
                })
                .reduce(0, Integer::sum);
    }
}
