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

        System.out.println(mainObject.getMaximumSubsequenceSum());

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

    private int getMaximumSubsequenceSum() {
        int[] sequence = Arrays.copyOfRange(inputs, 0, n);

        return calculateMaximumSubsequenceSumWithDivideAndConquer(sequence);
    }

    private int calculateMaximumSubsequenceSumWithDivideAndConquer(int[] sequence) {
        if (sequence.length == 1) {
            return sequence[0];
        }

        int length = sequence.length;
        
        int formerLength = length/2;
        int[] formerChunk = new int[formerLength];
        formerChunk = Arrays.copyOfRange(sequence, 0, formerLength);

        int latterLength = length - formerLength;
        int[] latterChunk = new int[latterLength];
        latterChunk = Arrays.copyOfRange(sequence, formerLength, length);


        int formerChunkMaxRightMostSubsequenceSum = calculateMaxRightMostSubsequenceSum(formerChunk);
        int latterChunkMaxLeftMostSubsequenceSum = calculateMaxLeftMostSubsequenceSum(latterChunk);

        
        return max(
            calculateMaximumSubsequenceSumWithDivideAndConquer(formerChunk), 
            calculateMaximumSubsequenceSumWithDivideAndConquer(latterChunk),
            formerChunkMaxRightMostSubsequenceSum + latterChunkMaxLeftMostSubsequenceSum
        );
    }

    private int calculateMaxRightMostSubsequenceSum(int[] sequence) {
        int max = Integer.MIN_VALUE;

        int sum = 0;
        for (int i = sequence.length - 1; i >= 0; i--) {
            sum += sequence[i];

            if (sum > max)
                max = sum;
        }

        return max;
    }

    private int calculateMaxLeftMostSubsequenceSum(int[] sequence) {
        int max = Integer.MIN_VALUE;

        int sum = 0;
        for (int i = 0; i < sequence.length; i++) {
            sum += sequence[i];

            if (sum > max)
                max = sum;
        }

        return max;
    }

    private int max(int a, int b, int c) {
        int max = a;

        if (b > max)
            max = b;

        if (c > max)
            max = c;

        return max;
    }
}