import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Main {
    abstract private class Fish {
        protected int size;

        public Fish(int size) {
            this.size = size;
        }

        public int getSize() {
            return size;
        }
    }
    private class BabyShark extends Fish {
        int eatCount;
        Position position;

        public BabyShark(Position position) {
            super(2);

            this.position = position;
            eatCount = 0;
        }

        public boolean eatAndGrowIfPossible() {
            eatCount++;
            if (eatCount == size) {
                size++;
                eatCount = 0;

                return true;
            }

            return false;
        }

        public Position getPosition() {
            return position;
        }

        public void setPosition(Position position) {
            this.position = position;
        }
    }

    private class Prey extends Fish {
        public Prey(int size) {
            super(size);
        }
    }

    private class Position {
        int x, y;
        BabyShark babyShark;
        Prey prey;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setBabyShark(BabyShark babyShark) {
            this.babyShark = babyShark;
        }

        public BabyShark getBabyShark() {
            return babyShark;
        }

        public void setPrey(Prey prey) {
            this.prey = prey;
        }

        public Prey getPrey() {
            return prey;
        }
    }

    private class FishTank {
        private class PreyPositionWithDistance {
            public Position position;
            public int distance;

            public PreyPositionWithDistance(Position position, int distance) {
                this.position = position;
                this.distance = distance;
            }
        }

        private Position[][] positions;
        private int n;

        private BabyShark babyShark;
        private Set<Prey> ediblePreys;
        private Set<Prey> inediblePreys;

        public FishTank(int n, int[][] fishes) {
            this.n = n;

            ediblePreys = new HashSet<Prey> ();
            inediblePreys = new HashSet<Prey> ();

            positions = new Position[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    Position position = new Position(i, j);

                    switch (fishes[i][j]) {
                        case 9:
                            babyShark = new BabyShark(position);
                            position.setBabyShark(babyShark);
                        
                            break;
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                            Prey prey = new Prey(fishes[i][j]);
                            if (prey.getSize() > 2) {
                                inediblePreys.add(prey);
                            }
                            else {
                                ediblePreys.add(prey);
                            }

                            position.setPrey(prey);
                            break; 
                    }

                    this.positions[i][j] = position;
                }
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("shark size: " + babyShark.getSize() + "\n");

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (positions[i][j].getBabyShark() != null) {
                        sb.append("S");
                    }
                    else {
                        Prey prey = positions[i][j].getPrey();
                        if (prey != null) {
                            sb.append(prey.getSize());
                        }
                        else {
                            sb.append("0");
                        }
                    }
                }
                sb.append("\n");
            }

            return sb.toString();
        }

        public int getMaxBabySharkSurvivalTime() {
            int elapsedTimes = 0;

            PreyPositionWithDistance shortestPrey;
            while (ediblePreys.size() > 0) {
                // System.out.println("--------- " + elapsedTimes + " ---------");
                // System.out.println(this);

                shortestPrey = findShortestPreyFromBabyShark();
                if (shortestPrey == null) {
                    break;
                }

                eatShortestPrey(shortestPrey);
                elapsedTimes += shortestPrey.distance;

                // System.out.println(this);
            }

            return elapsedTimes;
        }

        private PreyPositionWithDistance findShortestPreyFromBabyShark() {
            int[][] delta = new int[][] {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            Queue<PreyPositionWithDistance> queue = new LinkedList<> ();
            boolean[][] visited = new boolean[n][n];

            Position sharkPosition = babyShark.getPosition();
            queue.add(new PreyPositionWithDistance(sharkPosition, 0));

            List<PreyPositionWithDistance> sameDistancePreys = new LinkedList<> ();
            List<PreyPositionWithDistance> candidates = new LinkedList<> ();
            while (!queue.isEmpty()) {
                sameDistancePreys.clear();
                for (Iterator<PreyPositionWithDistance> it = queue.iterator(); it.hasNext();) {
                    sameDistancePreys.add(it.next());
                    it.remove();
                }

                candidates.clear();
                for (PreyPositionWithDistance e: sameDistancePreys) {
                    Prey prey = e.position.getPrey();
                    if (prey != null && prey.getSize() < babyShark.getSize()) {
                        candidates.add(e);
                    }
                }

                if (! candidates.isEmpty()) {
                    return candidates.stream().sorted((a, b) -> {
                        if (a.position.getX() == b.position.getX()) {
                            return a.position.getY() - b.position.getY();
                        }

                        return a.position.getX() - b.position.getX();
                    }).findFirst().get();
                }


                for (PreyPositionWithDistance e: sameDistancePreys) {
                    Position position = e.position;
                    int x = position.getX();
                    int y = position.getY();
                    Prey prey = position.getPrey();

                    if (visited[x][y]) {
                        // System.out.println("Visited (" + x + ", " + y + ")");
                        continue;
                    }
                    visited[x][y] = true;

                    if (prey != null && prey.getSize() > babyShark.getSize()) {
                        // System.out.println("Inedible (" + x + ", " + y + ")");
                        continue;
                    }

                    
                    // System.out.println("current: (" + x + ", " + y + ")");

                    for (int[] d: delta) {
                        int tX = x + d[0];
                        int tY = y + d[1];
        
                        if (tX < 0 || tX >= n || tY < 0 || tY >= n) {
                            // System.out.println("Out of bound: (" + x + ", " + y + ")");
                            continue;
                        }
        
                        Position adjacentPosition = positions[tX][tY];
        
                        // System.out.println("Add: (" + x + ", " + y + ") with distance: " + (e.distance + 1));
                        queue.add(new PreyPositionWithDistance(adjacentPosition, e.distance + 1));
                    }
                }
            }


            // System.out.println("No prey found");

            return null;
        }

        private void eatShortestPrey(PreyPositionWithDistance shortestPrey) {
            Position preyPosition = shortestPrey.position;
            Prey prey = preyPosition.getPrey();


            Position sharkPosition = babyShark.getPosition();
            sharkPosition.setBabyShark(null);
            
            preyPosition.setBabyShark(babyShark);
            babyShark.setPosition(preyPosition);
            

            preyPosition.setPrey(null);
            ediblePreys.remove(prey);


            boolean grown = babyShark.eatAndGrowIfPossible();
            if (grown) {
                updateEdiblePreys();
            }
        }

        private void updateEdiblePreys() {
            for (Iterator<Prey> it = inediblePreys.iterator(); it.hasNext();) {
                Prey prey = it.next();
                if (prey.getSize() < babyShark.getSize()) {
                    ediblePreys.add(prey);
                    it.remove();
                }
            }
        }
    }

    BufferedReader br;
    private int n;
    private int[][] fishes;


    public static void main(String... args) {
        Main mainObject = new Main();

        mainObject.initializeResources();

        try {
            mainObject.getInputs();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(mainObject.runFishTank());

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

        fishes = new int[n][n];
        for (int i = 0; i < n; i++) {
            line = br.readLine();
            tokens = line.split(" ");

            for (int j = 0; j < n; j++) {
                fishes[i][j] = Integer.parseInt(tokens[j]);
            }
        }
    }

    private int runFishTank() {
        FishTank fishTank = new FishTank(n, fishes);

        return fishTank.getMaxBabySharkSurvivalTime();
    }
}