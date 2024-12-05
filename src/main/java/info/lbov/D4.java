package info.lbov;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class D4 {

    public static void main(String[] args) throws IOException {
//        findXmasRegex("src/main/resources/day4/test.txt");
        findXmasPt2("src/main/resources/day4/test.txt");
    }

    public static void findXmasPt2(String filePath) throws IOException {
        List<String> horizontal = Files.readAllLines(Path.of(filePath));

        int height = horizontal.size();
        int width = horizontal.getFirst().length();


        int sum = 0;
        for (int i = 1; i < width-1; i++) {
            for (int j = 1; j < height-1; j++) {
                if (getChar(horizontal, i, j) == 'A') {
                    int funnyNumber = getChar(horizontal, i - 1, j - 1) == 'M' ? 1 : getChar(horizontal, i - 1, j - 1) == 'S' ? -1 : 929;
                    funnyNumber += getChar(horizontal, i + 1, j + 1) == 'M' ? 1 : getChar(horizontal, i + 1, j + 1) == 'S' ? -1 : 929;
                    int funnyNumber_2 = getChar(horizontal, i - 1, j + 1) == 'M' ? 1 : getChar(horizontal, i - 1, j + 1) == 'S' ? -1 : 929;
                    funnyNumber_2 += getChar(horizontal, i + 1, j - 1) == 'M' ? 1 : getChar(horizontal, i + 1, j - 1) == 'S' ? -1 : 929;

                    if (funnyNumber_2 == 0 && funnyNumber == 0) {
                        System.out.println("found " + i + " " + j);
                        sum++;
                    }
                }
            }
        }
        System.out.println(sum);
    }

    public static char getChar(List<String> lines, int x, int y) {
        return lines.get(y).charAt(x);
    }

    public static void findXmasRegex(String filePath) throws IOException {
        Pattern XMASpattern = Pattern.compile("XMAS");
        Pattern SAMXpattern = Pattern.compile("SAMX");
        List<String> horizontal = Files.readAllLines(Path.of(filePath));

        List<String> vertical = IntStream.range(0, horizontal.getFirst().length())
                .boxed()
                .map(index -> horizontal
                        .stream().map(line -> line.charAt(index))
                        .map(String::valueOf)
                        .collect(Collectors.joining())
                ).toList();

        int height = horizontal.size();
        int width = horizontal.getFirst().length();

        List<String> diagonal = Stream.concat(
            IntStream.range(0, horizontal.getFirst().length())
                .boxed()
                .map(start -> {
                    StringBuilder result = new StringBuilder();
                    StringBuilder backwardsResult = new StringBuilder();
                    int x = start;
                    int y = 0;
                    int yback = horizontal.size() - 1;
                    int steps = width - start;
                    for (int i = 0; i < steps; i++) {
                        result.append(horizontal.get(y + i).charAt(x + i));

                    }
                    for (int i = 0; i < start+1; i++) {
                        backwardsResult.append(horizontal.get(y + i).charAt(x - i));
                    }

                    return List.of(result.toString(), backwardsResult.toString());
                }),
            IntStream.range(1, horizontal.size())
                .boxed()
                .map(start -> {
                    StringBuilder result = new StringBuilder();
                    StringBuilder backwardsResult = new StringBuilder();
                    int x = 0;
                    int y = start;
                    int xback = width - 1;
                    int steps = height - start;
                    for (int i = 0; i < steps; i++) {
                        result.append(horizontal.get(y + i).charAt(x + i));
                    }
                    for (int i = 0; i < steps ; i++) {
                        backwardsResult.append(horizontal.get(y + i).charAt(xback - i));
                    }
                    return List.of(result.toString(), backwardsResult.toString());
                })
            ).flatMap(Collection::stream).toList();


        Long result = Stream.concat(
            Stream.concat(horizontal.stream(), vertical.stream()),
            diagonal.stream())
            .map(
                    line -> XMASpattern.matcher(line).results().count() + SAMXpattern.matcher(line).results().count()
            ).reduce(Long::sum).orElse(0L);

        System.out.println(result);
    }



}
