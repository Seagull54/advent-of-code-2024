package info.lbov;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class D5 {

    public static void main(String[] args) throws IOException {
        System.out.println(day5("src/main/resources/day5/rules_big.txt", "src/main/resources/day5/records_big.txt"));
    }

    public static Integer day5(String rulesString, String recordsString) throws IOException {
        Map<String, Set<String>> beforeMap = new HashMap<>();
        Map<String, Set<String>> afterMap = new HashMap<>();
        Files.readAllLines(Path.of(rulesString))
            .forEach(line -> {
                String[] split = line.split("\\|");
                addToMap(beforeMap, split[1], split[0]);
                addToMap(afterMap, split[0], split[1]);
            });

        return Files.readAllLines(Path.of(recordsString))
                .stream().parallel()
                .map(line -> line.split(","))
                .map(Arrays::asList)
                .filter(readings -> !validateLine(readings, beforeMap, afterMap))
                .map(readings -> findValue(readings, beforeMap, afterMap))
                .map(readings -> Integer.parseInt(readings.get(readings.size()/2)))
                .reduce(Integer::sum)
                .orElse(0);
    }

    public static void addToMap(Map<String, Set<String>> map, String key, String value) {
        if (!map.containsKey(key)) {
            map.put(key, new HashSet<>());
        }
        map.get(key).add(value);
    }

    public static boolean validateLine(List<String> readings, Map<String, Set<String>> beforeMap, Map<String, Set<String>> afterMap) {
        List<String> beforeSet = new ArrayList<>();
        List<String> afterSet = new ArrayList<>(readings);

        while (!afterSet.isEmpty()) {
            String reading = afterSet.getFirst();

            boolean afterIsBad = beforeSet.stream().anyMatch(
                read -> afterMap.containsKey(reading) && afterMap.get(reading).contains(read)
            );

            if (afterIsBad) {
                return false;
            }
            beforeSet.add(reading);

            afterSet.removeFirst();

            boolean beforeIsBad = afterSet.stream().anyMatch(
                read -> beforeMap.containsKey(reading) && beforeMap.get(reading).contains(read)
            );
            if (beforeIsBad) {
                return false;
            }

        }
        return true;
    }

    public static List<String> findValue(List<String> readings, Map<String, Set<String>> beforeMap, Map<String, Set<String>> afterMap) {
        while (findLineValue(readings, beforeMap, afterMap)) {}

        return readings;
    }

    public static boolean findLineValue(List<String> readings, Map<String, Set<String>> beforeMap, Map<String, Set<String>> afterMap) {
        List<String> beforeSet = new ArrayList<>();
        List<String> afterSet = new ArrayList<>(readings);

        while (!afterSet.isEmpty()) {
            String reading = afterSet.getFirst();

            String r = beforeSet.stream().filter(
                read -> afterMap.containsKey(reading) && afterMap.get(reading).contains(read)
            ).findFirst().orElse("");

            if (!r.isBlank()) {
                int index1 = readings.lastIndexOf(reading);
                int index2 = readings.indexOf(r);
                readings.set(index1, r);
                readings.set(index2, reading);
                return true;
            }
            beforeSet.add(reading);

            afterSet.removeFirst();

            r = afterSet.stream().filter(
                    read -> beforeMap.containsKey(reading) && beforeMap.get(reading).contains(read)
            ).findFirst().orElse("");

            if (!r.isBlank()) {
                int index1 = readings.indexOf(reading);
                int index2 = readings.lastIndexOf(r);
                readings.set(index1, r);
                readings.set(index2, reading);
                return true;
            }

        }
        return false;
    }
}
