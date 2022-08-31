import java.util.Scanner;
import java.util.Arrays;

public class Main {
    /* resources */
    Scanner scanner;
    int n;
    private int[] inputs;

    public static void main(String... args) {
        Main mainObject = new Main();

        mainObject.initializeResources();

        mainObject.getInputs();

        System.out.println(mainObject.getMaximumSubsequenceSumWithDP());

        mainObject.releaseResources();
    }

    private void initializeResources() {
        scanner = new Scanner(System.in);
        inputs = new int[100000];
    }

    private void releaseResources() {
        scanner.close();
    }

    private void getInputs() {
        n = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            inputs[i] = scanner.nextInt();
        }
    }

    private int getMaximumSubsequenceSumWithDP() {
        int[] dp = new int[n];

        int max = dp[0] = inputs[0];

        for (int i = 1; i < n; i++) {
            dp[i] = Integer.max(dp[i - 1] + inputs[i], inputs[i]);

            if (dp[i] > max)
                max = dp[i];
        }

        return max;
    }
}