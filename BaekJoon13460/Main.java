import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private class Position {
        private int x;
        private int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (! (o instanceof Position)) {
                return false;
            }

            Position position = (Position) o;
            return x == position.getX() && y == position.getY();
        }
    }

    private class BallPositions {
        private Position red;
        private Position blue;

        public BallPositions(Position red, Position blue) {
            this.red = red;
            this.blue = blue;
        }

        public Position getRed() {
            return red;
        }

        public Position getBlue() {
            return blue;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (! (o instanceof BallPositions)) {
                return false;
            }

            BallPositions ballPositions = (BallPositions) o;
            return red.equals(ballPositions.getRed()) && blue.equals(ballPositions.getBlue());
        }
    }

    private class SnapShot {
        private BallPositions ballPositions;
        private int trial;
        private Direction prevBehavior;

        public SnapShot(BallPositions ballPositions, int trial, Direction prevBehavior) {
            this.ballPositions = ballPositions;
            this.trial = trial;
            this.prevBehavior = prevBehavior;
        }

        public BallPositions getBallPositions() {
            return ballPositions;
        }

        public int getTrial() {
            return trial;
        }

        public Direction getPrevBehavior() {
            return prevBehavior;
        }

        @Override
        public String toString() {
            return "T:" + trial + "/P:" + prevBehavior + "/R:" + ballPositions.getRed().getX() + "," + ballPositions.getRed().getY() + "/B:" + ballPositions.getBlue().getX() + "," + ballPositions.getBlue().getY();
        }
    }

    /* resources */
    private Scanner scanner;
    private int height;
    private int width;
    private boolean[][] board;
    private Position initialRedPosition, initialBluePosition;
    private Position hole;
    private Queue<SnapShot> queue;

    public static void main(String... args) {
        Main mainObject = new Main();

        mainObject.initializeResources();

        mainObject.getInputs();

        System.out.println(mainObject.calculateMinimumTrial());

        mainObject.releaseResources();
    }

    private void initializeResources() {
        scanner = new Scanner(System.in);
        queue = new LinkedList<>();
    }

    private void releaseResources() {
        scanner.close();
    }

    private void getInputs() {
        String line = scanner.nextLine();
        String[] tokens = line.split(" ");

        height = Integer.parseInt(tokens[0]);
        width = Integer.parseInt(tokens[1]);

        board = new boolean[height][width];

        for (int i = 0; i < height; i++) {
            line = scanner.nextLine();
            for (int j = 0; j < width; j++) {
                char currentChar = line.charAt(j);

                if (currentChar == '#') {
                    board[i][j] = true;
                } else {
                    board[i][j] = false;
    
                    if (currentChar == 'O') {
                        hole = new Position(i, j);
                    } else if (currentChar == 'R') {
                        initialRedPosition = new Position(i, j);
                    } else if (currentChar == 'B') {
                        initialBluePosition = new Position(i, j);
                    }
                }
            }
        }
    }

    private int calculateMinimumTrial() {
        queue.add(new SnapShot(new BallPositions(initialRedPosition, initialBluePosition), 0, null));


        while (! queue.isEmpty()) {
            SnapShot current = queue.poll();
            // System.out.println(current.toString());

            if (current.getTrial() == 10) {
                continue;
            }


            int trial = current.getTrial();

            Direction prevBehavior = current.getPrevBehavior();
            BallPositions ballPositions = current.getBallPositions();
            BallPositions movedBallPositions;
            if (prevBehavior == null) {
                movedBallPositions = getBallPositionsAfterTiltingUp(ballPositions);
                if (movedBallPositions.getBlue() == null || ballPositions.equals(movedBallPositions)) {

                }
                else if (movedBallPositions.getRed() == null) {
                    return trial + 1;
                }
                else {
                    queue.add(new SnapShot(movedBallPositions, trial + 1, Direction.UP));
                }

                movedBallPositions = getBallPositionsAfterTiltingDown(ballPositions);
                if (movedBallPositions.getBlue() == null || ballPositions.equals(movedBallPositions)) {
            
                }
                else if (movedBallPositions.getRed() == null) {
                    return trial + 1;
                }
                else {
                    queue.add(new SnapShot(movedBallPositions, trial + 1, Direction.DOWN));
                }

                movedBallPositions = getBallPositionsAfterTiltingRight(ballPositions);
                if (movedBallPositions.getBlue() == null || ballPositions.equals(movedBallPositions)) {
            
                }
                else if (movedBallPositions.getRed() == null) {
                    return trial + 1;
                }
                else {
                    queue.add(new SnapShot(movedBallPositions, trial + 1, Direction.RIGHT));
                }

                movedBallPositions = getBallPositionsAfterTiltingLeft(ballPositions);
                if (movedBallPositions.getBlue() == null || ballPositions.equals(movedBallPositions)) {
                    
                }
                else if (movedBallPositions.getRed() == null) {
                    return trial + 1;
                }
                else {
                    queue.add(new SnapShot(movedBallPositions, trial + 1, Direction.LEFT));
                }
            } else if (prevBehavior == Direction.UP || prevBehavior == Direction.DOWN) {
                movedBallPositions = getBallPositionsAfterTiltingRight(ballPositions);
                if (movedBallPositions.getBlue() == null || ballPositions.equals(movedBallPositions)) {
                    
                }
                else if (movedBallPositions.getRed() == null) {
                    return trial + 1;
                }
                else {
                    queue.add(new SnapShot(movedBallPositions, trial + 1, Direction.RIGHT));
                }

                movedBallPositions = getBallPositionsAfterTiltingLeft(ballPositions);
                if (movedBallPositions.getBlue() == null || ballPositions.equals(movedBallPositions)) {
                    
                }
                else if (movedBallPositions.getRed() == null) {
                    return trial + 1;
                }
                else {
                    queue.add(new SnapShot(movedBallPositions, trial + 1, Direction.LEFT));
                }
            } else if (prevBehavior == Direction.RIGHT || prevBehavior == Direction.LEFT) {
                movedBallPositions = getBallPositionsAfterTiltingUp(ballPositions);
                if (movedBallPositions.getBlue() == null || ballPositions.equals(movedBallPositions)) {
                    
                }
                else if (movedBallPositions.getRed() == null) {
                    return trial + 1;
                }
                else {
                    queue.add(new SnapShot(movedBallPositions, trial + 1, Direction.UP));
                }

                movedBallPositions = getBallPositionsAfterTiltingDown(ballPositions);
                if (movedBallPositions.getBlue() == null || ballPositions.equals(movedBallPositions)) {
                    
                }
                else if (movedBallPositions.getRed() == null) {
                    return trial + 1;
                }
                else {
                    queue.add(new SnapShot(movedBallPositions, trial + 1, Direction.DOWN));
                }
            }
        }


        return -1;
    }

    private BallPositions getBallPositionsAfterTiltingUp(BallPositions ballPositions) {
        Position afterRed = null, currentRed = ballPositions.getRed();
        Position afterBlue = null, currentBlue = ballPositions.getBlue();


        int lastObstaclePosition;
        Position movedPosition;
        int currentY;
        if (currentRed.getY() != currentBlue.getY()) {
            lastObstaclePosition = -1;
            currentY = currentRed.getY();
            for (int i = 0; i < height; i++) {
                if (board[i][currentY]) {
                    lastObstaclePosition = i;
                }
                else if (i == currentRed.getX()) {
                    movedPosition = new Position(lastObstaclePosition + 1, currentY);
                    if (! (currentY == hole.getY() && hole.getX() < currentRed.getX() && movedPosition.getX() <= hole.getX())) {
                        afterRed = movedPosition;
                    }

                    break;
                }
            }

            lastObstaclePosition = -1;
            currentY = currentBlue.getY();
            for (int i = 0; i < height; i++) {
                if (board[i][currentY]) {
                    lastObstaclePosition = i;
                }
                else if (i == currentBlue.getX()) {
                    movedPosition = new Position(lastObstaclePosition + 1, currentY);
                    if (! (currentY == hole.getY() && hole.getX() < currentBlue.getX() && movedPosition.getX() <= hole.getX())) {
                        afterBlue = movedPosition;
                    }

                    break;
                }
            }
        }
        else {
            currentY = currentRed.getY();
            lastObstaclePosition = -1;
            for (int i = 0; i < height; i++) {
                if (board[i][currentY]) {
                    lastObstaclePosition = i;
                }
                else if (i == currentRed.getX()) {
                    movedPosition = new Position(lastObstaclePosition + 1, currentY);
                    if (! (currentY == hole.getY() && hole.getX() < currentRed.getX() && movedPosition.getX() <= hole.getX())) {
                        afterRed = movedPosition;
                        lastObstaclePosition = movedPosition.getX();
                    }
                }
                else if (i == currentBlue.getX()) {
                    movedPosition = new Position(lastObstaclePosition + 1, currentY);
                    if (currentY == hole.getY() && hole.getX() < currentBlue.getX() && movedPosition.getX() <= hole.getX()) {
                        break;
                    }
                    else {
                        afterBlue = movedPosition;
                        lastObstaclePosition = movedPosition.getX();
                    }
                }
            }
        }

        System.out.println("UP: " + afterRed + " " + afterBlue);

        return new BallPositions(afterRed, afterBlue);
    }

    private BallPositions getBallPositionsAfterTiltingDown(BallPositions ballPositions) {
        Position afterRed = null, currentRed = ballPositions.getRed();
        Position afterBlue = null, currentBlue = ballPositions.getBlue();

        
        int lastObstaclePosition;
        Position movedPosition;
        int currentY;
        if (currentRed.getY() != currentBlue.getY()) {
            lastObstaclePosition = height;
            currentY = currentRed.getY();
            for (int i = height - 1; i >= 0; i--) {
                if (board[i][currentY]) {
                    lastObstaclePosition = i;
                }
                else if (i == currentRed.getX()) {
                    movedPosition = new Position(lastObstaclePosition - 1, currentY);
                    if (! (currentY == hole.getY() && currentRed.getX() < hole.getX() && hole.getX() <= movedPosition.getX())) {
                        afterRed = movedPosition;
                    }

                    break;
                }
            }

            lastObstaclePosition = height;
            currentY = currentBlue.getY();
            for (int i = height - 1; i >= 0; i--) {
                if (board[i][currentY]) {
                    lastObstaclePosition = i;
                }
                else if (i == currentBlue.getX()) {
                    movedPosition = new Position(lastObstaclePosition - 1, currentY);
                    if (! (currentY == hole.getY() && currentBlue.getX() < hole.getX() && hole.getX() <= movedPosition.getX())) {
                        afterBlue = movedPosition;
                    }

                    break;
                }
            }
        }
        else {
            currentY = currentRed.getY();
            lastObstaclePosition = height;
            for (int i = height - 1; i >= 0; i--) {
                if (board[i][currentY]) {
                    lastObstaclePosition = i;
                }
                else if (i == currentRed.getX()) {
                    movedPosition = new Position(lastObstaclePosition - 1, currentY);
                    if (! (currentY == hole.getY() && currentRed.getX() < hole.getX() && hole.getX() <= movedPosition.getX())) {
                        afterRed = movedPosition;
                        lastObstaclePosition = movedPosition.getX();
                    }
                }
                else if (i == currentBlue.getX()) {
                    movedPosition = new Position(lastObstaclePosition - 1, currentY);
                    if (currentY == hole.getY() && currentBlue.getX() < hole.getX() && hole.getX() <= movedPosition.getX()) {
                        break;
                    }
                    else {
                        afterBlue = movedPosition;
                        lastObstaclePosition = movedPosition.getX();
                    }
                }
            }
        }

        System.out.println("DOWN: " + afterRed + " " + afterBlue);

        return new BallPositions(afterRed, afterBlue);
    }

    private BallPositions getBallPositionsAfterTiltingRight(BallPositions ballPositions) {
        Position afterRed = null, currentRed = ballPositions.getRed();
        Position afterBlue = null, currentBlue = ballPositions.getBlue();


        int lastObstaclePosition;
        Position movedPosition;
        int currentX;
        if (currentRed.getX() != currentBlue.getX()) {
            lastObstaclePosition = width;
            currentX = currentRed.getX();
            for (int i = width - 1; i >= 0; i--) {
                if (board[currentX][i]) {
                    lastObstaclePosition = i;
                }
                else if (i == currentRed.getY()) {
                    movedPosition = new Position(currentX, lastObstaclePosition - 1);
                    if (! (currentX == hole.getX() && currentRed.getY() < hole.getY() && hole.getY() <= movedPosition.getY())) {
                        afterRed = movedPosition;
                    }

                    break;
                }
            }

            lastObstaclePosition = width;
            currentX = currentBlue.getX();
            for (int i = width - 1; i >= 0; i--) {
                if (board[currentX][i]) {
                    lastObstaclePosition = i;
                }
                else if (i == currentBlue.getY()) {
                    movedPosition = new Position(currentX, lastObstaclePosition - 1);
                    if (! (currentX == hole.getX() && currentBlue.getY() < hole.getY() && hole.getY() <= movedPosition.getY())) {
                        afterBlue = movedPosition;
                    }

                    break;
                }
            }
        }
        else {
            currentX = currentRed.getX();
            lastObstaclePosition = width;
            for (int i = width - 1; i >= 0; i--) {
                if (board[currentX][i]) {
                    lastObstaclePosition = i;
                }
                else if (i == currentRed.getY()) {
                    movedPosition = new Position(currentX, lastObstaclePosition - 1);
                    if (! (currentX == hole.getX() && currentRed.getY() < hole.getY() && hole.getY() <= movedPosition.getY())) {
                        afterRed = movedPosition;
                        lastObstaclePosition = movedPosition.getY();
                    }
                }
                else if (i == currentBlue.getY()) {
                    movedPosition = new Position(currentX, lastObstaclePosition - 1);
                    if (currentX == hole.getX() && currentBlue.getY() < hole.getY() && hole.getY() <= movedPosition.getY()) {
                        break;
                    }
                    else {
                        afterBlue = movedPosition;
                        lastObstaclePosition = movedPosition.getY();
                    }
                }
            }
        }

        System.out.println("RIGHT: " + afterRed + " " + afterBlue);

        return new BallPositions(afterRed, afterBlue);
    }

    private BallPositions getBallPositionsAfterTiltingLeft(BallPositions ballPositions) {
        Position afterRed = null, currentRed = ballPositions.getRed();
        Position afterBlue = null, currentBlue = ballPositions.getBlue();


        int lastObstaclePosition;
        Position movedPosition;
        int currentX;
        if (currentRed.getX() != currentBlue.getX()) {
            lastObstaclePosition = -1;
            currentX = currentRed.getX();
            for (int i = 0; i < width; i++) {
                if (board[currentX][i]) {
                    lastObstaclePosition = i;
                }
                else if (i == currentRed.getY()) {
                    movedPosition = new Position(currentX, lastObstaclePosition + 1);
                    if (! (currentX == hole.getX() && hole.getY() < currentRed.getY() && movedPosition.getY() <= hole.getY())) {
                        afterRed = movedPosition;
                    }

                    break;
                }
            }

            lastObstaclePosition = -1;
            currentX = currentBlue.getX();
            for (int i = 0; i < width; i++) {
                if (board[currentX][i]) {
                    lastObstaclePosition = i;
                }
                else if (i == currentBlue.getY()) {
                    movedPosition = new Position(currentX, lastObstaclePosition + 1);
                    if (! (currentX == hole.getX() && hole.getY() < currentBlue.getY() && movedPosition.getY() <= hole.getY())) {
                        afterBlue = movedPosition;
                    }

                    break;
                }
            }
        }
        else {
            currentX = currentRed.getX();
            lastObstaclePosition = -1;
            for (int i = 0; i < width; i++) {
                if (board[currentX][i]) {
                    lastObstaclePosition = i;
                }
                else if (i == currentRed.getY()) {
                    movedPosition = new Position(currentX, lastObstaclePosition + 1);
                    if (! (currentX == hole.getX() && hole.getY() < currentRed.getY() && movedPosition.getY() <= hole.getY())) {
                        afterRed = movedPosition;
                        lastObstaclePosition = movedPosition.getY();
                    }
                }
                else if (i == currentBlue.getY()) {
                    movedPosition = new Position(currentX, lastObstaclePosition + 1);
                    if (currentX == hole.getX() && hole.getY() < currentBlue.getY() && movedPosition.getY() <= hole.getY()) {
                        break;
                    }
                    else {
                        afterBlue = movedPosition;
                        lastObstaclePosition = movedPosition.getY();
                    }
                }
            }
        }

        System.out.println("LEFT: " + afterRed + " " + afterBlue);

        return new BallPositions(afterRed, afterBlue);
    }
}