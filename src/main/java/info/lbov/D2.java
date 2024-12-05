package info.lbov;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class D2 {

    public static void main(String[] args) throws IOException {
        System.out.println(D2("src/main/resources/day2/1_test.txt"));
        System.out.println(D2("src/main/resources/day2/1.txt"));
//        System.out.println(D2("src/main/resources/day2/1.txt"));
    }

    public static Integer D2(String paths) throws IOException {

        Path path = Paths.get(paths);
        List<Integer> first = new ArrayList<>(1000);
        List<Integer> second = new ArrayList<>(1000);

        AtomicReference<Integer> good = new AtomicReference<>(0);

        return Files.readAllLines(path).stream().map(
            line -> {
                return validateString(new ArrayList<>(List.of(line.split(" "))), true);
            }
        ).reduce(0, Integer::sum);
    }

    public static Integer validateString(List<String> readings, boolean canFix) {
        if(readings.size() < 2) { return 1; }

        int prev = Integer.parseInt(readings.get(0));
        int curr = Integer.parseInt(readings.get(1));
        boolean decreasingFlag =  prev > curr;

        for (int i = 1; i < readings.size(); i++) {
            prev = Integer.parseInt(readings.get(i-1));
            curr = Integer.parseInt(readings.get(i));
            if (test(prev, curr, decreasingFlag)) {
                if (canFix) {
                    for (int j = 0; j < readings.size(); j++) {
                        List<String> firstCopy = new ArrayList<>(readings);
                        firstCopy.remove(j);
                        if (validateString(firstCopy, false) > 0) return 1;
                    }
                }
                return 0;
            }
        }
        return 1;
    }

    private static boolean test(Integer prev, Integer curr, boolean decreasingFlag) {
        return decreasingFlag != prev > curr ||
                prev == curr ||
                Math.abs(prev - curr) > 3;
    }
}
