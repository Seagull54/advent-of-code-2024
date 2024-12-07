package info.lbov.y2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class D7 {

    public static void main(String[] args) throws IOException {
        D7 d7 = new D7();
        System.out.println(d7.solve("src/main/resources/y2024/d7_in.txt"));
    }

    public Long solve(String mapPath) throws IOException {
        return Files.readAllLines(Path.of(mapPath)).stream()
            .map(line -> {
                String[] parts = line.split(": ");
                Long target = Long.parseLong(parts[0]);

                List<String> items = Arrays.asList(parts[1].split(" "));
                return solve(target, 0L, items.getFirst(), items.subList(1, items.size()));
            })
            .reduce(Long::sum).orElse(0L);
    }

    public long solve(Long goal, Long cur, String itemStr, List<String> items){
        Long item = Long.parseLong(itemStr);
        if (items.isEmpty()) {
            return goal.equals(cur + item) || goal.equals(cur * item) || goal.equals(cur * powerOfTen(itemStr.length()) + item) ? goal : 0;
        }
        if (cur > goal) { return 0; }

        String newItem = items.getFirst();

        if ((solve(goal, cur + item, newItem, items.subList(1, items.size())) > 0) ||
            (solve(goal, Math.max(cur, 1) * item, newItem, items.subList(1, items.size())) > 0) ||
            (solve(goal, cur * powerOfTen(itemStr.length()) + item, newItem, items.subList(1, items.size())) != 0)) return goal;

        return 0L;
    }

    private Long powerOfTen(int b) {
        long res = 1L;
        for (int i = 0; i < b; i++) {
            res = res * 10L;
        }
        return res;
    }
}
