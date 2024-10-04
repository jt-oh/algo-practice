import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
// import java.util.Scanner;

public class Main {
    private enum RelativeDirection {
        STRAIGHT, LEFT, RIGHT
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    };

    private class BoardPosition {
        private int x, y;

        public BoardPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (! (other instanceof BoardPosition)) {
                return false;
            }

            BoardPosition otherBoardPosition = (BoardPosition) other;

            return x == otherBoardPosition.getX() && y == otherBoardPosition.getY();
        }

        @Override
        public int hashCode() {
            return x * 101 + y;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("(" + x + ", " + y + ")");

            return sb.toString();
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    private class Dummy {
        private Direction direction;
        private Deque<BoardPosition> dummyBodies;

        public Dummy(Direction direction, BoardPosition initialPosition) {
            this.direction = direction;

            dummyBodies = new LinkedList<BoardPosition>();
            dummyBodies.add(initialPosition);
        }

        public boolean move(RelativeDirection rDir, boolean isAppleEaten) {
            BoardPosition newHead = getNewHead(rDir);

            for (BoardPosition body : dummyBodies) {
                if (body.equals(newHead)) {
                    return false;
                }
            }

            addDummyHead(newHead);
            if (! isAppleEaten) {
                removeDummyTail();
            }
            updateDummyDirection(rDir);

            return true;
        }

        public BoardPosition getNewHead(RelativeDirection rDir) {
            Direction dir = getDirectionFromRelativeDirection(rDir);

            BoardPosition head = getDummyHead();
            int x = head.getX();
            int y = head.getY();
            int newX, newY;

            switch (dir) {
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
                    throw new IllegalArgumentException("Invalid direction");
            }

            return new BoardPosition(newX, newY);
        }

        private BoardPosition getDummyHead() {
            return dummyBodies.getFirst();
        }

        private void addDummyHead(BoardPosition position) {
            dummyBodies.addFirst(position);
        }

        private void removeDummyTail() {
            dummyBodies.removeLast();
        }

        private Direction getDirectionFromRelativeDirection(RelativeDirection rDir) {
            if (rDir == RelativeDirection.STRAIGHT) {
                return direction;
            }

            if (rDir == RelativeDirection.LEFT) {
                switch (direction) {
                    case UP:
                        return Direction.LEFT;
                    case DOWN:
                        return Direction.RIGHT;
                    case LEFT:
                        return Direction.DOWN;
                    case RIGHT:
                        return Direction.UP;
                }
            }

            switch (direction) {
                case UP:
                    return Direction.RIGHT;
                case DOWN:
                    return Direction.LEFT;
                case LEFT:
                    return Direction.UP;
                case RIGHT:
                    return Direction.DOWN;
            }

            throw new IllegalArgumentException("Invalid relative direction");
        }

        private void updateDummyDirection(RelativeDirection rDirection) {
            direction = getDirectionFromRelativeDirection(rDirection);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("=> dummy, direction: " + direction + "\n");
            sb.append("body: ");
            for (BoardPosition body : dummyBodies) {
                sb.append(body.toString() + ",");
            }
            sb.append("\n");

            return sb.toString();
        }
    }


    BufferedReader br;
    // private BufferedReader scanner;
    private int boardSize;
    private Map<Integer, BoardPosition> apples;
    private Map<Integer, RelativeDirection> commands;


    public static void main(String... args) {
        Main mainObject = new Main();

        mainObject.initializeResources();

        try {
            mainObject.getInputs();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(mainObject.playDummyGame());

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
        
        line = br.readLine();
        boardSize = Integer.parseInt(line);

        line = br.readLine();
        int appleCount = Integer.parseInt(line);
        apples = new HashMap<Integer, BoardPosition>();

        for (int i = 0; i < appleCount; i++) {
            line = br.readLine();
            String[] tokens = line.split(" ");

            BoardPosition position = new BoardPosition(Integer.parseInt(tokens[0]) - 1, Integer.parseInt(tokens[1]) - 1);
            apples.put(position.hashCode(), position);

            // System.out.println("apple: " + position.hashCode() + ", " + position.toString());
        }

        line = br.readLine();
        int commandCount = Integer.parseInt(line);
        commands = new HashMap<Integer, RelativeDirection>();

        for (int i = 0; i < commandCount; i++) {
            line = br.readLine();
            String[] tokens = line.split(" ");

            int time = Integer.parseInt(tokens[0]) + 1;
            RelativeDirection rDir = tokens[1].equals("L") ? RelativeDirection.LEFT : RelativeDirection.RIGHT;
            commands.put(time, rDir);
        }
    }

    private int playDummyGame() {
        int time = 0;
        Dummy dummy = new Dummy(Direction.RIGHT, new BoardPosition(0, 0));

        
        while (true) {
            time++;

            RelativeDirection rDir = commands.getOrDefault(time, RelativeDirection.STRAIGHT);

            // System.out.println("------- t: " + (time) + ", next rel dir: " + rDir + " -------\n");
            // System.out.println("current dummy:");
            // System.out.println(dummy.toString());

            BoardPosition newHead = dummy.getNewHead(rDir);
            if (isDummyCrashedToWall(newHead)) {
                break;
            }

            boolean isAppleEaten = apples.remove(newHead.hashCode()) != null;
            if (! dummy.move(rDir, isAppleEaten)) {
                break;
            }

            // System.out.println("new dummy:");
            // System.out.println(dummy.toString());
        }

        return time;
    }

    private boolean isDummyCrashedToWall(BoardPosition head) {
        if (head.getX() < 0 || head.getX() >= boardSize || head.getY() < 0 || head.getY() >= boardSize) {
            return true;
        }

        return false;
    }
}