import java.util.Scanner;

public class Main {
    private long[][][] result;

    public static void main(String... args) {
        Main mainObject = new Main();

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
