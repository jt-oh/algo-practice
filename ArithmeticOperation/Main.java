import java.util.Scanner;

public class Main {
    public static void main(String... args) {
        Scanner scanner = new Scanner(System.in);

        String[] operands = scanner.nextLine().split(" ");
        
        int A = Integer.parseInt(operands[0]);
        int B = Integer.parseInt(operands[1]);

        Main mainObject = new Main();

        ArithmeticFactory factory = mainObject.new ArithmeticFactory();

        char[] operators = {'+', '-', '*', '/', '%'};
        for (char operator : operators) {
            System.out.println(factory.instance(operator).process(A, B));
        }

        scanner.close();
    }

    public class ArithmeticFactory {
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

    abstract private class Arithmetic {
        abstract public int process(int A, int B);
    }

    private class Addition extends Arithmetic {
        @Override
        public int process(int A, int B) {
            return A + B;
        }
    }

    private class Subtraction extends Arithmetic {
        @Override
        public int process(int A, int B) {
            return A - B;
        }
    }

    private class Multiplication extends Arithmetic {
        @Override
        public int process(int A, int B) {
            return A * B;
        }
    }

    private class Division extends Arithmetic {
        @Override
        public int process(int A, int B) {
            return A / B;
        }
    }

    private class Modulus extends Arithmetic {
        @Override
        public int process(int A, int B) {
            return A % B;
        }
    }
}