import java.util.Scanner;

public class Main {
    private long[][][] result;

    final static boolean TEST = false;
    private boolean[][][] calculated;
    private long[][][] testCache;

    public static void main(String... args) {
        Main mainObject = new Main();

        if (TEST) {
            mainObject.test();

            return;
        }

        Scanner scanner = new Scanner(System.in);

        for (String line = scanner.nextLine(); ; line = scanner.nextLine()) {
            if (line.equals("-1 -1 -1")) {
                break;
            }

            String[] inputs = line.split(" ");

            int a = Integer.parseInt(inputs[0]);
            int b = Integer.parseInt(inputs[1]);
            int c = Integer.parseInt(inputs[2]);

            long result = mainObject.getResultWithDP(a, b, c);

            System.out.println("w(" + String.join(", ", inputs) + ") = " + result);
        }

        scanner.close();

        return;
    }

    private void test() {
        System.out.println("Test Start");

        testCache = new long[21][][];
        calculated = new boolean[21][][];
        for (int i = 0; i <= 20; i++) {
            testCache[i] = new long[21][];
            calculated[i] = new boolean[21][];
            for (int j = 0; j <= 20; j++) {
                testCache[i][j] = new long[21];
                calculated[i][j] = new boolean[21];
            }
        }

        for (int i = -50; i <= 50; i++) {
            System.out.println("i: " + i);
            for (int j = -50; j <= 50; j++) {
                System.out.println("    j: " + j);
                for (int k = -50; k <= 50; k++) {

                    long result = getResultWithDP(i, j, k);
                    long expected = getResultWithRecursive(i, j, k);

                    if (result != expected) {
                        System.out.println("(i, k, k) : (" + i + ", " + j + ", " + k);
                        System.out.println("result : " + result + "  expected : " + expected);

                        return;
                    }
                }
            }
        }

        return;
    }

    private long getResultWithRecursive(int a, int b, int c) {
        if (
            a >= 0 && b >= 0 && c>= 0 &&
            a <= 20 && b <= 20 && c <= 20 &&
            calculated[a][b][c]
        ) {
            return testCache[a][b][c];
        }

        if (violateFloorConstraints(a, b, c)) {
            return 1;
        }
        else if (a > 20 || b > 20 || c > 20) {
            return getResultWithRecursive(20, 20, 20);
        }
        else if (a < b && b < c) {
            long result = getResultWithRecursive(a, b, c - 1) + getResultWithRecursive(a, b - 1, c - 1) - getResultWithRecursive(a, b - 1, c);

            if (
                a >= 0 && b >= 0 && c >= 0 &&
                a <= 20 && b <= 20 && c <= 20
            ) {
                testCache[a][b][c] = result;
                calculated[a][b][c] = true;
            }

            return result;
        }
        else {
            long result = getResultWithRecursive(a - 1, b, c) + getResultWithRecursive(a - 1, b - 1, c) + getResultWithRecursive(a - 1, b, c - 1) - getResultWithRecursive(a - 1, b - 1, c - 1);

            if (
                a >= 0 && b >= 0 && c >= 0 &&
                a <= 20 && b <= 20 && c <= 20
            ) {
                testCache[a][b][c] = result;
                calculated[a][b][c] = true;
            }

            return result;
        }
    }

    private long getResultWithDP(int a, int b, int c) {
        if (violateFloorConstraints(a, b, c)) {
            return 1;
        }

        // subtitute inputs with ceiling value
        if (a > 20 || b > 20 || c > 20) {
            a = 20;
            b = 20;
            c = 20;
        }

        initializeResultsArray(a, b, c);

        calculateResultWithDP(a, b, c);

        return result[a][b][c];
    }

    private boolean violateFloorConstraints(int a, int b, int c) {
        return a <= 0 || b <= 0 || c <= 0;
    }

    private void initializeResultsArray(int a, int b, int c) {
        result = new long[a + 1][][];

        for (int i = 0; i <= a; i++) {
            result[i] = new long[b + 1][];

            for (int j = 0; j <= b; j++) {
                result[i][j] = new long[c + 1];
            }
        }
    }

    private void calculateResultWithDP(int a, int b, int c) {
        for (int i = 0; i <= a; i++) {
            for (int j = 0; j <= b; j++) {
                for (int k = 0; k <= c; k++) {
                    long result;
                    
                    if (violateFloorConstraints(i, j, k)) {
                        result = 1;
                    }
                    else if (i < j && j < k) {
                        result = this.result[i][j][k - 1] + this.result[i][j - 1][k - 1] - this.result[i][j - 1][k];
                    }
                    else {
                        result = this.result[i - 1][j][k] + this.result[i - 1][j - 1][k] + this.result[i - 1][j][k - 1] - this.result[i - 1][j - 1][k - 1];
                    }

                    this.result[i][j][k] = result;
                }
            }
        }
    }
}
