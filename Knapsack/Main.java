import java.util.Scanner;

public class Main {
    /* resources */
    private Scanner scanner;
    private int n;
    private int maxWeight;
    private int[] weights;
    private int[] values;

    public static void main(String... args) {
        Main mainObject = new Main();

        mainObject.initializeResources();

        mainObject.getInputs();

        System.out.println(mainObject.calculateKnapsackProblem());

        mainObject.releaseResources();
    }

    private void initializeResources() {
        scanner = new Scanner(System.in);

        weights = new int[100];
        values = new int[100];
    }

    private void releaseResources() {
        scanner.close();
    }

    private void getInputs() {
        n = scanner.nextInt();

        maxWeight = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            weights[i] = scanner.nextInt();
            values[i] = scanner.nextInt();
        }
    }

    private int calculateKnapsackProblem() {
        int[][] maxValues = new int[n + 1][maxWeight + 1];

        for (int i = 0; i <= n; i++)
            maxValues[i][0] = 0;
        for (int i = 0; i <= maxWeight; i++)
            maxValues[0][i] = 0;

        int maxValue;
        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= maxWeight; w++) {
                if (w < weights[i - 1]) {
                    maxValue = maxValues[i - 1][w];
                } else {
                    maxValue = Integer.max(maxValues[i - 1][w], maxValues[i - 1][w - weights[i - 1]] + values[i - 1]);
                }

                maxValues[i][w] = maxValue;
            }
        }

        return maxValues[n][maxWeight];
    }
}