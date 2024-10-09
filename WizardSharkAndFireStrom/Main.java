import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Math;
import java.util.Stack;

public class Main {
    final int[][] DELTA = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    private class Position {
        public int x, y;

        public Position(int x, int y) {
            super();
            this.x = x;
            this.y = y;
        }
    }

    private class Storm {
        private int size;
        private int[][] iceAmounts;

        public Storm(int size, int[][] iceAmounts) {
            super();
            this.size = size;
            this.iceAmounts = iceAmounts;
        }

        public int[][] rotate() {
            int[][] rotatedIceAmounts = new int[size][size];
        
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    int newX = j;
                    int newY = size - 1 - i;

                    rotatedIceAmounts[newX][newY] = iceAmounts[i][j];
                }
            }

            return rotatedIceAmounts;
        }
    }

    private class IceBoard {
        private int size;
        private int[][] iceAmounts;

        public IceBoard(int size, int[][] iceAmounts) {
            super();
            this.size = size;
            this.iceAmounts = iceAmounts;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    sb.append(iceAmounts[i][j] + ", ");
                }

                sb.append("\n");
            }
            

            return sb.toString();
        }

        public void spellFireStorms(int[] stormSizes) {
            for (int stormSize: stormSizes) {
                System.out.println("--------- " + stormSize + " ---------");
                System.out.println("beforeRotate:");
                System.out.println(this);

                rotateIcesByStorm(stormSize);

                System.out.println("beforeRotate:");
                System.out.println(this);

                meltIcesByFire();

                System.out.println("afterMelt:");
                System.out.println(this);
            }
        }

        private void rotateIcesByStorm(int stormSize) {
            for (int i = 0; i < size; i += stormSize) {
                for (int j = 0; j < size; j += stormSize) {
                    int[][] subIceAmounts = new int[stormSize][stormSize];
                    for (int k = 0; k < stormSize; k++) {
                        for (int l = 0; l < stormSize; l++) {
                            subIceAmounts[k][l] = iceAmounts[i + k][j + l];
                        }
                    }

                    Storm storm = new Storm(stormSize, subIceAmounts);
                    int[][] rotatedSubIceAmounts = storm.rotate();

                    for (int k = 0; k < stormSize; k++) {
                        for (int l = 0; l < stormSize; l++) {
                            iceAmounts[i + k][j + l] = rotatedSubIceAmounts[k][l];
                        }
                    }
                }
            }
        }

        private void meltIcesByFire() {
            boolean[][] melt = new boolean[size][size];

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    int adjacentIceCount = 0;

                    for (int[] d: DELTA) {
                        int adjacentX = i + d[0];
                        int adjacentY = j + d[1];

                        if (!isAccessibleBoard(adjacentX, adjacentY)) {
                            continue;
                        }
                        
                        if (iceAmounts[adjacentX][adjacentY] == 0) {
                            continue;
                        }

                        adjacentIceCount++;
                    }


                    if (iceAmounts[i][j] > 0 && adjacentIceCount < 3) {
                        melt[i][j] = true;
                    }
                }
            }

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (melt[i][j]) {
                        iceAmounts[i][j]--;
                    }
                }
            }
        }

        public int getTotalIceAmounts() {
            int totalIceAmounts = 0;

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    totalIceAmounts += iceAmounts[i][j];
                }
            }

            return totalIceAmounts;
        }

        public int getMaxIceChunkSize() {
            boolean[][] visited = new boolean[size][size];

            int maxChunkSize = 0;

            Stack<Position> stack = new Stack<Position> ();
            int chunkSize;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (iceAmounts[i][j] != 0 && !visited[i][j]) {
                        chunkSize = 0;
                        stack.push(new Position(i, j));

                        while (!stack.empty()) {
                            Position p = stack.pop();

                            if (visited[p.x][p.y]) {
                                continue;
                            }
                            visited[p.x][p.y] = true;
                            chunkSize++;

                            for (int[] d: DELTA) {
                                int adjacentX = p.x + d[0];
                                int adjacentY = p.y + d[1];

                                if (!isAccessibleBoard(adjacentX, adjacentY)) {
                                    continue;
                                }

                                if (iceAmounts[adjacentX][adjacentY] == 0) {
                                    continue;
                                }

                                stack.push(new Position(adjacentX, adjacentY));
                            }
                        }

                        if (chunkSize > maxChunkSize) {
                            maxChunkSize = chunkSize;
                        }
                    }
                }
            }

            return maxChunkSize;
        }

        private boolean isAccessibleBoard(int x, int y) {
            return x >= 0 && x < size && y >= 0 && y < size;
        }
    }

    BufferedReader br;
    private int n, stormSizesCount;
    private int[][] iceAmounts;
    private int[] stormSizes;

    public Main() {
        super();
    }

    public static void main(String... args) {
        Main mainObject = new Main();

        mainObject.initializeResources();

        try {
            mainObject.getInputs();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mainObject.spellFireStorms();

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

        n = (int) Math.pow(2, Integer.parseInt(tokens[0]));
        stormSizesCount = Integer.parseInt(tokens[1]);
        stormSizes = new int[stormSizesCount];

        iceAmounts = new int[n][n];
        for (int i = 0; i < n; i++) {
            line = br.readLine();
            tokens = line.split(" ");

            for (int j = 0; j < n; j++) {
                iceAmounts[i][j] = Integer.parseInt(tokens[j]);
            }
        }

        line = br.readLine();
        tokens = line.split(" ");
        for (int i = 0; i < stormSizesCount; i++) {
            stormSizes[i] = (int) Math.pow(2, Integer.parseInt(tokens[i]));
        }
    }

    private void spellFireStorms() {
        IceBoard board = new IceBoard(n, iceAmounts);

        board.spellFireStorms(stormSizes);

        System.out.println(board.getTotalIceAmounts());
        System.out.println(board.getMaxIceChunkSize());

        return;
    }
}