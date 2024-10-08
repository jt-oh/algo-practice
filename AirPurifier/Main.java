import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private class RoomPosition {
        int x, y;
        int dustAmount;
        boolean isAirPurifier;
        // 0: up, 1: down, 2: left, 3: right
        boolean[] spreadableDirections;
        int spreadableDirectionCount;

        public RoomPosition(int x, int y, int dustAmount, boolean[] spreadableDirections) {
            this.x = x;
            this.y = y;
            this.dustAmount = dustAmount;
            this.spreadableDirections = spreadableDirections;
            isAirPurifier = dustAmount == -1;
            
            spreadableDirectionCount = 0;
            for (boolean spreadable: spreadableDirections) {
                if (spreadable) {
                    spreadableDirectionCount++;
                }
            }
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setDustAmount(int dustAmount) {
            this.dustAmount = dustAmount;
        }

        public int getDustAmount() {
            return dustAmount;
        }

        public boolean isAirPurifier() {
            return isAirPurifier;
        }

        public boolean[] getSpreadableDirections() {
            return spreadableDirections;
        }

        public int getSpreadableDirectionCount() {
            return spreadableDirectionCount;
        }
    }

    private class Room {
        private RoomPosition[][] roomPositions;
        private int n, m;
        private int upperAirPurifierX = -1;

        public Room(int n, int m, int[][] dustAmounts) {
            this.n = n;
            this.m = m;

            boolean spreadableUp, spreadableDown, spreadableLeft, spreadableRight;
            roomPositions = new RoomPosition[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (upperAirPurifierX == -1 && dustAmounts[i][j] == -1) {
                        upperAirPurifierX = i;
                    }

                    spreadableUp = i != 0 &&
                        dustAmounts[i - 1][j] != -1;
                    spreadableDown = i != n - 1 &&
                        dustAmounts[i + 1][j] != -1;
                    spreadableLeft = j != 0 &&
                        dustAmounts[i][j - 1] != -1;
                    spreadableRight = j != m - 1;

                    roomPositions[i][j] = new RoomPosition(i, j, dustAmounts[i][j], new boolean[] {spreadableUp, spreadableDown, spreadableLeft, spreadableRight});
                }
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    sb.append(roomPositions[i][j].getDustAmount());
                    sb.append(", ");
                }
                sb.append("\n");
            }

            return sb.toString();
        }

        private int getUpperAirPurifierX() {
            return upperAirPurifierX;
        }

        private int getLowerAirPurifierX() {
            return upperAirPurifierX + 1;
        }

        public int runAirPurifier(int t) {
            int elapsedTimes = 0;

            while (++elapsedTimes <= t) {
                // System.out.println("------ t: " + elapsedTimes + " ------");
                // System.out.println("beforeSpreadDust:");
                // System.out.println(this);
                spreadDust();
                // System.out.println("afterSpreadDust:");
                // System.out.println(this);
                circulateAir();
                // System.out.println("afterCirculateAir:");
                // System.out.println(this);
            }

            return getDustAmount();
        }

        private void spreadDust() {
            int[][] delta = new int[][] {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            int[][] subtractedDustAmounts = new int[n][m];
            int[][] spreadedDustAmounts = new int[n][m];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    RoomPosition p = roomPositions[i][j];
                    int x = p.getX();
                    int y = p.getY();

                    int spreadAmountByDir = p.getDustAmount() / 5;
                    boolean[] spreadableDirections = p.getSpreadableDirections();
                    int idx = 0;
                    for (boolean spreadable: spreadableDirections) {
                        ++idx;
                        if (! spreadable) {
                            continue;                            
                        }


                        int adjacentX = x + delta[idx - 1][0];
                        int adjacentY = y + delta[idx - 1][1];

                        spreadedDustAmounts[adjacentX][adjacentY] += spreadAmountByDir;
                    }

                    subtractedDustAmounts[x][y] = p.getDustAmount() - spreadAmountByDir * p.getSpreadableDirectionCount();
                }
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    RoomPosition p = roomPositions[i][j];

                    if (p.isAirPurifier()) {
                        continue;
                    }

                    p.setDustAmount(subtractedDustAmounts[i][j] + spreadedDustAmounts[i][j]);
                }
            }
        }

        private void circulateAir() {
            int upperAirPurifierX = getUpperAirPurifierX();
            int lowerAirPurifierX = getLowerAirPurifierX();

            for (int i = upperAirPurifierX - 1; i > 0 ; i--) {
                roomPositions[i][0].setDustAmount(roomPositions[i - 1][0].getDustAmount());
            }
            for (int i = lowerAirPurifierX + 1; i < n - 1; i++) {
                roomPositions[i][0].setDustAmount(roomPositions[i + 1][0].getDustAmount());
            }

            for (int i: new int[] {0, n - 1}) {
                for (int j = 0; j < m - 1; j++) {
                    roomPositions[i][j].setDustAmount(roomPositions[i][j + 1].getDustAmount());
                }
            }
            
            for (int i = 0; i < upperAirPurifierX ; i++) {
                roomPositions[i][m - 1].setDustAmount(roomPositions[i + 1][m - 1].getDustAmount());
            }
            for (int i = n - 1; i > lowerAirPurifierX; i--) {
                roomPositions[i][m - 1].setDustAmount(roomPositions[i - 1][m - 1].getDustAmount());
            }

            for (int i: new int[] {upperAirPurifierX, lowerAirPurifierX}) {
                for (int j = m - 1; j > 1; j--) {
                    roomPositions[i][j].setDustAmount(roomPositions[i][j - 1].getDustAmount());
                }

                roomPositions[i][1].setDustAmount(0);
            }
        }

        private int getDustAmount() {
            int dustAmount = 0;

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (roomPositions[i][j].isAirPurifier()) {
                        continue;
                    }

                    dustAmount += roomPositions[i][j].getDustAmount();
                }
            }

            return dustAmount;
        }
    }

    BufferedReader br;
    private int n, m, t;
    private int[][] dustAmounts;


    public static void main(String... args) {
        Main mainObject = new Main();

        mainObject.initializeResources();

        try {
            mainObject.getInputs();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(mainObject.runAirPurifier());

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
        m = Integer.parseInt(tokens[1]);
        t = Integer.parseInt(tokens[2]);

        dustAmounts = new int[n][m];
        for (int i = 0; i < n; i++) {
            line = br.readLine();
            tokens = line.split(" ");

            for (int j = 0; j < m; j++) {
                dustAmounts[i][j] = Integer.parseInt(tokens[j]);
            }
        }
    }

    private int runAirPurifier() {
        Room room = new Room(n, m, dustAmounts);

        return room.runAirPurifier(t);
    }
}