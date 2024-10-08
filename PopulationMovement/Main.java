import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Main {
    private class Nation {
        private int x, y;
        private int population;
        private Set<Nation> union;

        public Nation(int x, int y, int population) {
            this.x = x;
            this.y = y;
            this.population = population;
        }

        public void setUnion(Set<Nation> union) {
            this.union = union;
        }

        public Set<Nation> getUnion() {
            return union;
        }

        public void changePopulation(int population) {
            this.population = population;
        }

        public int getPopulation() {
            return population;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void refreshNation() {
            union = null;
        }
    }

    BufferedReader br;
    private int n, m, M;
    private Nation[][] nations;
    private List<Set<Nation>> unionList;


    public static void main(String... args) {
        Main mainObject = new Main();

        mainObject.initializeResources();

        try {
            mainObject.getInputs();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(mainObject.calculateMaxMovements());

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
        M = Integer.parseInt(tokens[2]);

        nations = new Nation[n][n];
        for (int i = 0; i < n; i++) {
            line = br.readLine();
            tokens = line.split(" ");

            for (int j = 0; j < n; j++) {
                nations[i][j] = new Nation(i, j, Integer.parseInt(tokens[j]));
            }
        }
    }

    private int calculateMaxMovements() {
        unionList = new LinkedList<>();
        int count = 0;

        // System.out.println("======== trial " + count + " =========");

        while (movePopulation()) {
            count++;

            // for (int i = 0; i < n; i++) {
            //     for (int j = 0; j < n; j++) {
            //         System.out.print(nations[i][j].getPopulation() + ", ");
            //     }
            //     System.out.println("");
            // }
            // System.out.println("======== trial " + count + " =========");
        }

        return count;
    }

    private boolean movePopulation() {
        inspectUnions();
        
        return movePopuliations();
    }

    /**
     * ToDo: BFS/DFS 로 연합을 찾도록 변경하면 국가의 연합 여부 판단 동작의 중복을 줄일 수 있다.
     */
    private void inspectUnions() {
        int[][] delta = new int[][] {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};


        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Nation nation = nations[i][j];
                Set<Nation> nationsUnion = nation.getUnion();

                for (int[] d: delta) {
                    int x = i + d[0];
                    int y = j + d[1];

                    if (x < 0 || x >= n || y < 0 || y >= n) {
                        continue;
                    }

                    Nation adjacentNation = nations[x][y];
                    int diff = Math.abs(nation.getPopulation() - adjacentNation.getPopulation());
                    if (diff < m || diff > M) {
                        continue;
                    }

                    Set<Nation> adjacentNationsUnion = adjacentNation.getUnion();
                    if (nationsUnion!= null && adjacentNationsUnion == nationsUnion) {
                        continue;
                    }


                    if (nationsUnion == null) {
                        nationsUnion = new HashSet<Nation>();
                        nationsUnion.add(nation);

                        unionList.add(nationsUnion);
                    }

                    if (adjacentNationsUnion != null) {
                        nationsUnion.addAll(adjacentNationsUnion);
                        for (Nation connectedNation: adjacentNationsUnion) {
                            connectedNation.setUnion(nationsUnion);
                        }
                        unionList.remove(adjacentNationsUnion);
                    } else {
                        nationsUnion.add(adjacentNation);
                        adjacentNation.setUnion(nationsUnion);
                    }
                }
            }
        }
    }

    private boolean movePopuliations() {
        boolean moved = false;

        for (Set<Nation> union: unionList) {
            int populationInUnion = 0;
            // System.out.println("--------- new union ---------");

            for (Nation nation: union) {
                populationInUnion += nation.getPopulation();

                // System.out.println("(" + nation.getX() + ", " + nation.getY() + "), Population: " + nation.getPopulation());
            }

            int newPopulation = populationInUnion / union.size();
            for (Nation nation: union) {
                nation.changePopulation(newPopulation);
                nation.refreshNation();
            }

            // System.out.println("new population: " + newPopulation);

            moved = true;
        }

        unionList.clear();


        return moved;
    }
}