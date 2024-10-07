import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
// import java.util.Scanner;

public class Main {
    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    };

    private class Dice {
        private int top, bottom, up, down, left, right;

        public Dice() {
            top = 0;
            bottom = 0;
            up = 0;
            down = 0;
            left = 0;
            right = 0;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("T: " + top + ", ");
            sb.append("B: " + bottom + ", ");
            sb.append("U: " + up + ", ");
            sb.append("D: " + down + ", ");
            sb.append("L: " + left + ", ");
            sb.append("R: " + right + "\n");

            return sb.toString();
        }

        public void rollDice(Direction direction) {
            int prevBottom = bottom;

            switch (direction) {
                case UP:
                    bottom = up;

                    up = top;
                    top = down;
                    down = prevBottom;
                    break;
                case DOWN:
                    bottom = down;

                    down = top;
                    top = up;
                    up = prevBottom;
                    break;
                case LEFT:
                    bottom = left;

                    left = top;
                    top = right;
                    right = prevBottom;
                    break;
                case RIGHT:
                    bottom = right;

                    right = top;
                    top = left;
                    left = prevBottom;
                    break;
            }
        }

        public int getBottom() {
            return bottom;
        }

        public int getTop() {
            return top;
        }

        public void setBotton(int value) {
            bottom = value;
        }
    }

    BufferedReader br;
    private int n, m;
    private int x, y;
    private int[][] board;
    private int commandCount;
    private Direction[] commands;


    public static void main(String... args) {
        Main mainObject = new Main();

        mainObject.initializeResources();

        try {
            mainObject.getInputs();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mainObject.rollDiceAlongCommands();

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
        m = Integer.parseInt(tokens[1]);
        x = Integer.parseInt(tokens[2]);
        y = Integer.parseInt(tokens[3]);
        commandCount = Integer.parseInt(tokens[4]);

        board = new int[n][m];    
        for (int i = 0; i < n; i++) {
            line = br.readLine();
            tokens = line.split(" ");

            for (int j = 0; j < m; j++) {
                board[i][j] = Integer.parseInt(tokens[j]);
            }
        }

        commands = new Direction[commandCount];
        line = br.readLine();
        tokens = line.split(" ");
        for (int i = 0; i < commandCount; i++) {
            int command = Integer.parseInt(tokens[i]);

            Direction dir;
            switch (command) {
                case 1:
                    dir = Direction.RIGHT;
                    break;
                case 2:
                    dir = Direction.LEFT;
                    break;
                case 3:
                    dir = Direction.UP;
                    break;
                case 4:
                    dir = Direction.DOWN;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid command: " + command);
            }

            commands[i] = dir;
        }
    }

    private void rollDiceAlongCommands() {
        Dice dice = new Dice();

        for (int i = 0; i < commandCount; i++) {
            int newX, newY;

            // System.out.println("----- (" + x + ", " + y + ") -----\n");
            // System.out.println("Current Dice: " + dice.toString());

            Direction command = commands[i];
            switch (command) {
                case UP:
                    newX = x - 1;
                    newY = y;
                    break;
                case DOWN:
                    newX = x + 1;
                    newY = y;
                    break;
                case LEFT:
                    newX = x;
                    newY = y - 1;
                    break;
                case RIGHT:
                    newX = x;
                    newY = y + 1;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid command: " + command);
            }

            if (verifyRollable(newX, newY)) {
                x = newX;
                y = newY;

                dice.rollDice(command);

                if (board[x][y] == 0) {
                    board[x][y] = dice.getBottom();
                } else {
                    dice.setBotton(board[x][y]);
                    board[x][y] = 0;
                }

                // System.out.println("New Dice: (" + x + ", " + y + "), " + dice.toString() + "\n");

                System.out.println(dice.getTop());
            }
        }


        return;
    }

    private boolean verifyRollable(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < m;
    }
}