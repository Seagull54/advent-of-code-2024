package info.lbov.y2024;

import java.util.*;

public class Day17_2 {

    public static void main(String[] args) {
        List<Long> nums = findOut(3, 0b110L);

        LinkedList<Integer> list = new LinkedList<>(List.of(5, 5, 1, 4, 5, 1, 3, 0, 5, 7, 3, 1, 4, 2));

        while (!list.isEmpty()) {
            Integer next = list.removeFirst();
            nums = nums.stream().map(num -> findOut(next, num)).flatMap(Collection::stream).toList();
        }
        nums.forEach(System.out::println);
    }

    public static List<Long> findOut(long result, long start) {
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            long number = (start << 3) + i;
            long b = i;
            b = b ^ 3;
            long c = number >> b;
            b = b ^ 5;
            b = b ^ c;
            if (b % 8 == result) {
                list.add(number);
            }
        }
        return list;
    }
}
