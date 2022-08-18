import java.util.Scanner;

public class Main {
    public static void main(String... args) {
        Scanner scanner = new Scanner(System.in);

        int N = scanner.nextInt();

        Main mainInstance = new Main();

        System.out.println(mainInstance.getNthFibonacci(N));

        scanner.close();
    }

    private int getNthFibonacci(int N) {
        int[] fibonacci = new int[N + 1];

        for (int i = 0; i <= N; i++) {
            if (i == 0 || i == 1) {
                fibonacci[i] = 1;
            } else {
                fibonacci[i] = (fibonacci[i - 1] + fibonacci[i - 2]) % 15746;
            }
        }

        return fibonacci[N];
    }
}