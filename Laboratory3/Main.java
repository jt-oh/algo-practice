import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Queue;
import java.util.stream.Collectors;


public class Main {
    final int[][] DELTA = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    private enum LabPositionType {
        EMPTY, WALL, VIRUS;
    };

    private class LabPosition {
        private int x, y;
        private LabPositionType type;

        public LabPosition(int x, int y, LabPositionType type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean isEmpty() {
            return type == LabPositionType.EMPTY;
        }

        public boolean isWall() {
            return type == LabPositionType.WALL;
        }

        public boolean isVirus() {
            return type == LabPositionType.VIRUS;
        }
    }


    private class Lab {
        private class LabPositionWithSpreadTime {
            public LabPosition position;
            public int spreadTime;

            public LabPositionWithSpreadTime(LabPosition position, int spreadTime) {
                this.position = position;
                this.spreadTime = spreadTime;
            }
        }


        private int size;
        private LabPosition[][] map;
        private int emptyPositionCount = 0;
        private int virusPositionCount = 0;
        private LabPosition[] virusPositions = new LabPosition[10];

        public Lab(int size, int[][] rawMap) {
            this.size = size;
            
            map = new LabPosition[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    LabPosition p;
                    switch (rawMap[i][j]) {
                        case 0:
                            p = new LabPosition(i, j, LabPositionType.EMPTY);
                            emptyPositionCount++;
                            break;
                        case 1:
                            p = new LabPosition(i, j, LabPositionType.WALL);
                            break;
                        case 2:
                            p = new LabPosition(i, j, LabPositionType.VIRUS);
                            virusPositions[virusPositionCount++] = p;
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid map value");
                    }

                    map[i][j] = p;
                }
            }
        }

        public int getFastestSpreadTime(int activeVirusCount) {
            int fastestSpreadTime = -1;

            List<Integer[]> initialActiveCombinations = CombinationsGenerator.generate(virusPositionCount - 1, activeVirusCount);

            // for (Integer[] activationIdxs: initialActiveCombinations) {
            //     System.out.println(Arrays.toString(activationIdxs));
            // }

            Queue<LabPositionWithSpreadTime> queue = new LinkedList<LabPositionWithSpreadTime> ();
            for (Integer[] activationIdxs: initialActiveCombinations) {
                int spreadTime = -1;
                int spreadedEmptyPositionCount = 0;
                boolean[][] visited = new boolean[size][size];
                queue.clear();

                queue.addAll(Arrays.asList(activationIdxs)
                    .stream()
                    .map(idx -> new LabPositionWithSpreadTime(virusPositions[idx.intValue()], 0))
                    .collect(Collectors.toList())
                );
                
                while (!queue.isEmpty()) {
                    LabPositionWithSpreadTime e = queue.poll();
                    LabPosition p = e.position;
                    int x = p.getX();
                    int y = p.getY();

                    if (visited[x][y]) {
                        continue;
                    }
                    visited[x][y] = true;


                    if (p.isWall()) {
                        continue;
                    }


                    if (p.isEmpty()) {
                        spreadedEmptyPositionCount++;
                    }
                    

                    if (spreadedEmptyPositionCount == emptyPositionCount) {
                        spreadTime = e.spreadTime;
                        break;
                    }


                    for (int[] d: DELTA) {
                        int adjacentX = x + d[0];
                        int adjacentY = y + d[1];

                        if (adjacentX < 0 || adjacentX >= size || adjacentY < 0 || adjacentY >= size) {
                            continue;
                        }

                        queue.add(new LabPositionWithSpreadTime(map[adjacentX][adjacentY], e.spreadTime + 1));
                    }
                }


                if (spreadTime != -1 && (fastestSpreadTime == -1 || spreadTime < fastestSpreadTime)) {
                    // System.out.println("fastestSpreadTime changed");
                    // System.out.println(Arrays.toString(activationIdxs) + ": " + spreadTime);
                    fastestSpreadTime = spreadTime;                        
                }
            }


            return fastestSpreadTime;
        }
    }


    BufferedReader br;
    private int n, activeVirusCount;
    private int[][] rawMap;

    public static void main(String... args) {
        Main mainObject = new Main();

        mainObject.initializeResources();

        try {
            mainObject.getInputs();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(mainObject.findFastestSpreadTime());

        try {
            mainObject.releaseResources();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initializeResources() {
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    private void releaseResources() throws IOException {
        br.close();
    }

    private void getInputs() throws IOException {
        String line;
        String[] tokens;
        
        line = br.readLine();
        tokens = line.split(" ");

        n = Integer.parseInt(tokens[0]);
        activeVirusCount = Integer.parseInt(tokens[1]);

        rawMap = new int[n][n];
        for (int i = 0; i < n; i++) {
            line = br.readLine();
            tokens = line.split(" ");

            for (int j = 0; j < n; j++) {
                rawMap[i][j] = Integer.parseInt(tokens[j]);
            }
        }
    }

    private int findFastestSpreadTime() {
        Lab lab = new Lab(n, rawMap);

        return lab.getFastestSpreadTime(activeVirusCount);
    }
}

class CombinationsGenerator {
    public static List<Integer[]> generate(int maxIdx, int targetSize) {
        return selectRecursivelry(maxIdx, targetSize, new Integer[targetSize], -1, targetSize);
    }

    private static List<Integer[]> selectRecursivelry(int maxIdx, int targetSize, Integer[] selectedArr, int lastIdx, int remainedSize) {
        if (remainedSize == 0) {
            return Arrays.asList(new Integer[][] {selectedArr});
        }


        List<Integer[]> combinations = new LinkedList<Integer[]> ();
        for (int e = lastIdx + 1; e <= maxIdx; e++) {
            Integer[] newSelectedArr = Arrays.copyOf(selectedArr, targetSize);

            newSelectedArr[targetSize - remainedSize] = e;

            combinations.addAll(selectRecursivelry(maxIdx, targetSize, newSelectedArr, e, remainedSize - 1));
        }

        return combinations;
    }
}