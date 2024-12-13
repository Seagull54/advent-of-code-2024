package info.lbov.y2024;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 {
    String pattern = "(\\d+)";
    Pattern pat = Pattern.compile(pattern);

    private static String testFile = "src/main/resources/y2024/d11_test.txt";
    private static String inputFile = "src/main/resources/y2024/d11_input.txt";

    public static void main(String[] args) throws IOException {
        Day13 d = new Day13();
//        List<Case> cases = d.readFile(testFile);
        List<Case> cases = d.readFile(inputFile);

        List<Pair> results = cases.stream()
                .peek(c -> {c.targetX += 10000000000000L; c.targetY += 10000000000000L;})
                .map(c -> c.solveCramer()).toList();
        Long result = cases.stream()
                .peek(s -> s.toString())
                .map(c -> c.solveCramer().cost).reduce(0L, Long::sum);
        System.out.println(result);
//        System.out.println(d.solvePart1(cases));
    }

    public List<Case> readFile(String fileName) throws IOException {
        Iterator<String> iterator = Files.readAllLines(Path.of(fileName)).iterator();
        List<Case> result = new ArrayList<>();
        Case temp = new Case();
        int order = 0;
        while (iterator.hasNext()) {
            String line = iterator.next();
            Matcher matcher = pat.matcher(line);
            int x = 0;
            int y = 0;
            if (matcher.find()) {
                x = Integer.parseInt(matcher.group(1));
            }
            if (matcher.find()) {
                y = Integer.parseInt(matcher.group(1));
            }
            switch (order) {
                case 0:
                    temp.x1 = x;
                    temp.y1 = y;
                    break;
                case 1:
                    temp.x2 = x;
                    temp.y2 = y;
                    break;
                case 2:
                    temp.targetX = x;
                    temp.targetY = y;
                    result.add(temp);
                    temp = new Case();
                    break;
                default:
                    order = -1;
                    break;
            }
            order++;
        }
        return result;
    }

    public long solvePart1(List<Case> cases) {
        return cases.stream().parallel().map(this::solveCase).reduce(0L, Long::sum);
    }

    public long solveCase(Case clawCase) {
        boolean found = false;
        int minResult = 0;
        List<Pair> pairs = new ArrayList<>();

        long maxI = clawCase.targetX / clawCase.x1;
        long maxJ = clawCase.targetX / clawCase.x2;

//        System.out.println(maxI + "-" + maxJ);

        for (long i = 0; i < maxI; i++) {
            for (long j = 0; j < maxJ; j++) {
                if (i * clawCase.x1 + j* clawCase.x2 == clawCase.targetX && i * clawCase.y1 + j* clawCase.y2 == clawCase.targetY) {
                    long cost = 3 * i + j;

                    return cost;
                }
            }
        }

        if (!found) {
            System.out.println(clawCase);
        }

        System.out.println("case solved");
        return pairs.stream().map(pair -> pair.cost).min(Long::compareTo).orElse(0L);
    }
// 37528 high
// 36571


    public class Case {
        public int x1, x2, y1, y2;
        public long targetX, targetY;

        public Pair solveCramer() {
            BigDecimal xd1 = BigDecimal.valueOf(x1);
            BigDecimal xd2 = BigDecimal.valueOf(x2);
            BigDecimal yd1 = BigDecimal.valueOf(y1);
            BigDecimal yd2 = BigDecimal.valueOf(y2);
            BigDecimal tx = BigDecimal.valueOf(targetX);
            BigDecimal ty = BigDecimal.valueOf(targetY);


            BigDecimal det = xd1.multiply(yd2).subtract(xd2.multiply(yd1));
            BigDecimal det1 = xd1.multiply(ty).subtract(tx.multiply(yd1));
            BigDecimal det2 = tx.multiply(yd2).subtract(xd2.multiply(ty));


            try {
                BigDecimal x2 = det1.divide(det, RoundingMode.UNNECESSARY);
                BigDecimal x1 = det2.divide(det, RoundingMode.UNNECESSARY);


                return new Pair(x2.longValue(), x1.longValue(), x1.multiply(BigDecimal.valueOf(3)).add(x2).longValue());
            } catch (ArithmeticException e) {
                return new Pair(0, 0, 0L);
            }
//            long result = det1 % det != 0 || det2 % det != 0 ? 0 : (det1/det)*3 + (det2/det);

//            return new /Pair(0, 0, result.longValue());
        }

        @Override
        public String toString() {
            return "A" + x1 + "," + y1 + " B" + x2 + "," + y2 + " Target" + targetX + "," + targetY;
        }
    }

    public record Pair(long x, long y, long cost) {};
}
