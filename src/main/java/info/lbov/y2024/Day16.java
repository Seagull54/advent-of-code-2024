package info.lbov.y2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day16 {

    private static final String testFile = "src/main/resources/y2024/day16_test.txt";
    private static final String inputFile = "src/main/resources/y2024/day16_input.txt";
    private static int[][] globalMap;
    private static char[][] globalOMap;

    public static void main(String[] args) throws IOException {
        char[][] map = readMap(testFile);
//        char[][] map = readMap(inputFile);

        int result = solvePart1(map);

        System.out.println(result);
        printMap(globalOMap);

        System.out.println();
        printIntMap(globalMap);

        int counter = 1;
        for (int i = 0; i < globalOMap.length; i++) {
            for (int j = 0; j < globalOMap[i].length; j++) {
                if (globalOMap[i][j] == 'O') counter++;
            }
        }
        System.out.println(counter);
    }

    private static void printMap(char[][] map) {
        for (int j = 0; j < map.length; j++) {
            System.out.println(new String(map[j]));
        }
    }


    private static void printIntMap(int[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static char[][] readMap(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(fileName));
        char[][] map = new char[lines.size()][lines.get(0).length()];
        globalMap = new int[map.length][map[0].length];
        globalOMap = new char[map.length][map[0].length];

        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(0).length(); j++) {
                globalMap[i][j] = -1;
                globalOMap[i][j] = '.';
            }
        }

        for (int i = 0; i < lines.size(); i++) {
            map[i] = lines.get(i).toCharArray();
        }
        return map;
    }

    private static int solvePart1(char[][] map) {
        Pair selfPos = new Pair(0, 0);
        Pair endPos = new Pair(map.length, map[0].length);

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 'S') {
                    selfPos = new Pair(j, i);
                }
                if (map[i][j] == 'E') {
                    endPos = new Pair(j, i);
                }
            }
        }

        QueueEntry start = new QueueEntry(0, selfPos, Collections.emptySet(), Direction.RIGHT);
        PriorityQueue<QueueEntry> pq = new PriorityQueue<>();

        pq.add(start);

        int iter = 0;
        int bestPath = -1;
        boolean pathFound = false;
        while (!pq.isEmpty()) {
            iter++;
            QueueEntry entry = pq.poll();

            if (entry.pos.equals(endPos) && (!pathFound || bestPath == entry.score)) {
                pathFound = true;
                bestPath = entry.score;
                System.out.println("best path found: " + bestPath);
                entry.visited.forEach(
                    pair -> globalOMap[pair.y][pair.x] = 'O'
                );
            }

            if (!entry.pos.equals(endPos)) {
                pq.addAll(getAvailPaths(map, entry));
            }
        }
        System.out.println("iterations " + iter);
        return bestPath;
    }

    private static List<QueueEntry> getAvailPaths(char[][] map, QueueEntry entry) {
        if (entry.score > globalMap[entry.pos.y][entry.pos.x] && globalMap[entry.pos.y][entry.pos.x] != -1) {
            if (map[entry.pos.y+entry.direction.y][entry.pos.x+entry.direction.x] != '#' &&
                    (entry.score + 1 == globalMap[entry.pos.y+entry.direction.y][entry.pos.x+entry.direction.x] || globalMap[entry.pos.y+entry.direction.y][entry.pos.x+entry.direction.x]==-1)) {
                int score = entry.score + 1;
                Set<Pair> visited = new HashSet<>(entry.visited);
                visited.add(entry.pos);
                QueueEntry newEntry = new QueueEntry(score, new Pair(entry.pos.x + entry.direction.x, entry.pos.y + entry.direction.y), visited, entry.direction);
                return List.of(newEntry);
            }
            return Collections.emptyList();
        }
        globalMap[entry.pos.y][entry.pos.x] = entry.score;

        Pair position = entry.pos;
        int x = entry.pos.x;
        int y = entry.pos.y;

        List<QueueEntry> paths = new ArrayList<>();

        if (map[y][x+1] != '#' && !entry.visited.contains(new Pair(x+1, y))) {
            int score = entry.score + 1 + Direction.turnCost(entry.direction, Direction.RIGHT);
            Set<Pair> visited = new HashSet<>(entry.visited);
            visited.add(entry.pos);
            QueueEntry newEntry = new QueueEntry(score, new Pair(x+1, y), visited, Direction.RIGHT);
            paths.add(newEntry);
        }
        if (map[y][x-1] != '#' && !entry.visited.contains(new Pair(x-1, y))) {
            int score = entry.score + 1 + Direction.turnCost(entry.direction, Direction.LEFT);
            Set<Pair> visited = new HashSet<>(entry.visited);
            visited.add(entry.pos);
            QueueEntry newEntry = new QueueEntry(score, new Pair(x-1, y), visited, Direction.LEFT);
            paths.add(newEntry);
        }
        if (map[y+1][x] != '#' && !entry.visited.contains(new Pair(x, y+1))) {
            int score = entry.score + 1 + Direction.turnCost(entry.direction, Direction.DOWN);
            Set<Pair> visited = new HashSet<>(entry.visited);
            visited.add(entry.pos);
            QueueEntry newEntry = new QueueEntry(score, new Pair(x, y+1), visited, Direction.DOWN);
            paths.add(newEntry);
        }
        if (map[y-1][x] != '#' && !entry.visited.contains(new Pair(x, y-1))) {
            int score = entry.score + 1 + Direction.turnCost(entry.direction, Direction.UP);
            Set<Pair> visited = new HashSet<>(entry.visited);
            visited.add(entry.pos);
            QueueEntry newEntry = new QueueEntry(score, new Pair(x, y-1), visited, Direction.UP);
            paths.add(newEntry);
        }
        return paths;
    }


    private static class QueueEntry implements Comparable<QueueEntry> {
        int score;
        Direction direction;
        Pair pos;
        Set<Pair> visited = new HashSet<>();

        public QueueEntry(int score, Pair pos, Set<Pair> visited, Direction direction) {
            this.score = score;
            this.pos = pos;
            this.visited.addAll(visited);
            this.direction = direction;
        }

        @Override
        public int compareTo(QueueEntry o) {
            return this.score - o.score;
        }
    }

    private record Pair (int x, int y){

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null) {
                return false;
            }
            if (!(other instanceof Pair otherPair)) {
                return false;
            }
            return this.x == otherPair.x && this.y == otherPair.y;
        }
    }

    private enum Direction {
        UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

        int x, y, angle;

        Direction (int x, int y) {
            this.x = x;
            this.y = y;
        }

        public static int turnCost(Direction from, Direction to) {
            if (from == to) {
                return 0;
            }
            if (from == UP && to == DOWN ||
                from == DOWN && to == UP ||
                from == LEFT && to == RIGHT ||
                from == RIGHT && to == LEFT ) {
                return 1000 * 2;
            }
            return 1000;
        }
    }
}
