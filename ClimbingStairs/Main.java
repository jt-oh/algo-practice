import java.util.Scanner;

public class Main {
    /* resources */
    Scanner scanner;
    int n;
    private int[] inputs;

    public static void main(String... args) {
        Main mainObject = new Main();

        mainObject.initializeResources();

        mainObject.getInputs();

        System.out.println(mainObject.calculateClimbingStairsMaximumScore());

        mainObject.releaseResources();
    }

    private void initializeResources() {
        scanner = new Scanner(System.in);
        inputs = new int[300];
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

    private int calculateClimbingStairsMaximumScore() {
        if (n == 1)
            return inputs[0];

        int[] scoreFromOneStepBefore = new int [n];
        int[] scoreFromTwoStepsBefore = new int [n];

        scoreFromOneStepBefore[0] = inputs[0];
        scoreFromTwoStepsBefore[0] = 0;
        scoreFromOneStepBefore[1] = inputs[0] + inputs[1];
        scoreFromTwoStepsBefore[1] = inputs[1];

        for (int i = 2; i < n; i++) {
            scoreFromOneStepBefore[i] = scoreFromTwoStepsBefore[i - 1] + inputs[i];
            scoreFromTwoStepsBefore[i] = Integer.max(scoreFromOneStepBefore[i - 2], scoreFromTwoStepsBefore[i - 2]) + inputs[i];
        }

        return Integer.max(scoreFromOneStepBefore[n - 1], scoreFromTwoStepsBefore[n - 1]);
    }
}