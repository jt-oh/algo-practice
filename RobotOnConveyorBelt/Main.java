import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private class BeltElement {
        private boolean loaded;
        private int durability;
        
        public BeltElement(int durability) {
            loaded = false;
            this.durability = durability;
        }

        private int getDurability() {
            return durability;
        }

        private boolean isExausted() {
            return durability == 0;
        }

        private boolean isLoaded() {
            return loaded;
        }

        private boolean isLoadable() {
            return !isLoaded() && !isExausted();
        }

        private boolean putRobot() {
            if (!isLoadable()) {
                return false;
            }

            loaded = true;
            durability--;

            return true;
        }

        public void removeRobot() {
            loaded = false;
        }
    }

    private class Conveyor {
        private int size;
        private BeltElement[] belt;
        private int beltHeadIdx;

        public Conveyor(int n, int[] durabilities) {
            size = n;
            int beltSize = getBeltSize();

            belt = new BeltElement[beltSize];
            for (int i = 0; i < beltSize; i++) {
                belt[i] = new BeltElement(durabilities[i]);
            }

            beltHeadIdx = 0;
        }

        private int getBeltSize() {
            return 2 * size;
        }

        private int getBeltTailIdx() {
            return (beltHeadIdx + size - 1) % getBeltSize();
        }

        private void moveBelt() {
            rotateConveyorBelt();
            moveRobotsBySelf();
            putRobotOnBeltHead();
        }

        private void rotateConveyorBelt() {
            decrementBeltHead();

            BeltElement tailElement = belt[getBeltTailIdx()];
            if (tailElement.isLoaded()) {
                tailElement.removeRobot();
            }
        }

        private void decrementBeltHead() {
            int beltSize = getBeltSize();

            beltHeadIdx = ((beltHeadIdx - 1) % beltSize + beltSize) % beltSize;;
        }

        private void moveRobotsBySelf() {
            int beltSize = getBeltSize();
            int beltTailIdx = getBeltTailIdx();

            BeltElement prevElement = null;
            for (int i = 0; i < size - 2; i++) {
                int idx = ((beltTailIdx - i) % beltSize + beltSize) % beltSize;
                BeltElement currentElement = prevElement == null ?
                    belt[idx] : prevElement;
                
                prevElement = belt[idx == 0 ? beltSize - 1 : idx - 1];

                if (prevElement.isLoaded() && currentElement.isLoadable()) {
                    prevElement.removeRobot();
                    currentElement.putRobot();

                    if (idx == beltTailIdx) {
                        currentElement.removeRobot();
                    }
                }
            }
        }

        private void putRobotOnBeltHead() {
            BeltElement headElement = belt[beltHeadIdx];

            if (headElement.isLoadable()) {
                headElement.putRobot();
            }
        }

        private int getExaustedElementCount() {
            int count = 0;

            for (int i = 0; i < getBeltSize(); i++) {
                if (belt[i].isExausted()) {
                    count++;
                }
            }

            return count;
        }

        public int run(int maxExaustedBelElementCount) {
            int count = 0;

            do {
                // System.out.println("--------- " + count + " ---------");
                // System.out.println(toString());
                // System.out.println("\n");

                moveBelt();

                count++;

                // System.out.println("after move");
                // System.out.println(toString());
            }
            while (getExaustedElementCount() < maxExaustedBelElementCount);

            return count;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            int beltSize = getBeltSize();
            int beltTailIdx = getBeltTailIdx();

            for (int i = 0; i < beltSize; i++) {
                sb.append((i + 1) % 10);
            }
            sb.append("\n");
            sb.append("\n");

            for (int i = 0; i < beltSize; i++) {
                sb.append(i == beltHeadIdx ? "H" : (i == beltTailIdx ? "T" : " "));
            }
            sb.append("\n");

            for (int i = 0; i < beltSize; i++) {
                sb.append(belt[i].isLoaded() ? "R" : " ");
            }
            sb.append("\n");

            for (int i = 0; i < beltSize; i++) {
                sb.append(belt[i].isExausted() ? "X" : belt[i].getDurability());
            }

            return sb.toString();
        }
    }

    BufferedReader br;
    private int n, k;
    private int[] durabilities;


    public static void main(String... args) {
        Main mainObject = new Main();

        mainObject.initializeResources();

        try {
            mainObject.getInputs();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(mainObject.runConveyorBelt());

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
        k = Integer.parseInt(tokens[1]);

        durabilities = new int[2 * n];
        line = br.readLine();
        tokens = line.split(" ");

        for (int i = 0; i < 2 * n; i++) {
            durabilities[i] = Integer.parseInt(tokens[i]);
        }
    }

    private int runConveyorBelt() {
        Conveyor conveyor = new Conveyor(n, durabilities);

        return conveyor.run(k);
    }
}