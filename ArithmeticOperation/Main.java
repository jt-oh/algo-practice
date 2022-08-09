import java.util.Scanner;

public class Main {
    public static void main(String... args) {
        Scanner scanner = new Scanner(System.in);

        String[] operands = scanner.nextLine().split(" ");
        
        int A = Integer.parseInt(operands[0]);
        int B = Integer.parseInt(operands[1]);

        ArithmeticFactory factory = new ArithmeticFactory();

        char[] operators = {'+', '-', '*', '/', '%'};
        for (char operator : operators) {
            System.out.println(factory.instance(operator).process(A, B));
        }

        scanner.close();
    }

    public static class ArithmeticFactory {
        public Arithmetic instance(char operand) throws RuntimeException {
            switch (operand) {
                case '+':
                    return new Addition();
                case '-':
                    return new Subtraction();
                case '*':
                    return new Multiplication();
                case '/':
                    return new Division();
                case '%':
                    return new Modulus();
                default:
                    throw new RuntimeException();
            }
        }
    }

    abstract static private class Arithmetic {
        abstract public int process(int A, int B);
    }

    private static class Addition extends Arithmetic {
        @Override
        public int process(int A, int B) {
            return A + B;
        }
    }

    private static class Subtraction extends Arithmetic {
        @Override
        public int process(int A, int B) {
            return A - B;
        }
    }

    private static class Multiplication extends Arithmetic {
        @Override
        public int process(int A, int B) {
            return A * B;
        }
    }

    private static class Division extends Arithmetic {
        @Override
        public int process(int A, int B) {
            return A / B;
        }
    }

    private static class Modulus extends Arithmetic {
        @Override
        public int process(int A, int B) {
            return A % B;
        }
    }
}