package info.lbov;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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


}
