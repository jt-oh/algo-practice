import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    };

    private static class BoardSnapShot {
        int trial;
        int n;
        private int[][] board;
        private int maxResult = 0;
        private Direction prevSiwpeDirection;

        private BoardSnapShot (int trial, int n, int[][] board, Direction prevSiwpeDirection) {
            this.trial = trial;
            this.n = n;
            this.board = board;
            this.prevSiwpeDirection = prevSiwpeDirection;

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (board[i][j] > maxResult) {
                        maxResult = board[i][j];
                    }
                }
            }
        }

        public static BoardSnapShot createBoardWithRows(int trial, int n, int[][] board, Direction prevSiwpeDirection) {
            return new BoardSnapShot(trial, n, board, prevSiwpeDirection);
        }

        public static BoardSnapShot createBoardWithCols(int trial, int n, int[][] board, Direction prevSiwpeDirection) {
            int[][] arrayBoard = new int[n][n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    arrayBoard[i][j] = board[j][i];
                }
            }

            return new BoardSnapShot(trial, n, arrayBoard, prevSiwpeDirection);
        }

        public int[] getRow(int rowIdx) {
            return board[rowIdx];
        }

        public int[] getCol(int colIdx) {
            int[] col = new int[n];
            
            for (int i = 0; i < n; i++) {
                col[i] = board[i][colIdx];
            }

            return col;
        }

        public int getTrial() {
            return trial;
        }

        public int getMaxResult() {
            return maxResult;
        }

        public Direction getPrevSwipeDirection() {
            return prevSiwpeDirection;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("----- " + trial + ", max: " + maxResult + " -----\n");

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    sb.append(board[i][j] + " ");
                }
                sb.append("\n");
            }



            return sb.toString();
        }
    }

    private class SwipeResult {
        private BoardSnapShot boardSnapShot;
        private boolean isCompressed;

        private SwipeResult(BoardSnapShot boardSnapShot, boolean isCompressed) {
            this.boardSnapShot = boardSnapShot;
            this.isCompressed = isCompressed;
        }

        public BoardSnapShot getBoardSnapShot() {
            return boardSnapShot;
        }

        public boolean getIsCompressed() {
            return isCompressed;
        }
    }


    /* resources */
    private Scanner scanner;
    private int n;
    private BoardSnapShot givenBoard;

    public static void main(String... args) {
        Main mainObject = new Main();

        mainObject.initializeResources();

        mainObject.getInputs();

        System.out.println(mainObject.calculateMaximumResult());

        mainObject.releaseResources();
    }

    private void initializeResources() {
        scanner = new Scanner(System.in);
    }

    private void releaseResources() {
        scanner.close();
    }

    private void getInputs() {
        String line = scanner.nextLine();

        n = Integer.parseInt(line);

        int[][] arrayBoard = new int[n][n];

        for (int i = 0; i < n; i++) {
            line = scanner.nextLine();
            String[] tokens = line.split(" ");

            for (int j = 0; j < n; j++) {
                arrayBoard[i][j] = Integer.parseInt(tokens[j]);
            }
        }

        givenBoard = BoardSnapShot.createBoardWithRows(0, n, arrayBoard, null);
    }

    private int calculateMaximumResult() {
        Queue<BoardSnapShot> queue = new LinkedList<>();
        queue.add(givenBoard);


        int maxResult = 0;
        SwipeResult swipeResult;
        BoardSnapShot boardSnapShot;
        Direction prevSwipeDirection;
        while (! queue.isEmpty()) {
            BoardSnapShot current = queue.poll();

            int trial = current.getTrial();
            if (trial == 5) {
                continue;
            }


            if (trial < 3) {
                // System.out.println("===============================");
                // System.out.println(current.toString());
            }

            prevSwipeDirection = current.getPrevSwipeDirection();

            swipeResult = getResultAfterSwipingUp(current);
            boardSnapShot = swipeResult.getBoardSnapShot();
            if (trial < 3) {
                // System.out.println(boardSnapShot.toString());
            }
            if (boardSnapShot.getMaxResult() > maxResult) {
                maxResult = boardSnapShot.getMaxResult();
            }
            if (prevSwipeDirection != boardSnapShot.getPrevSwipeDirection() || swipeResult.getIsCompressed()) {
                queue.add(boardSnapShot);
            }

            swipeResult = getResultAfterSwipingDown(current);
            boardSnapShot = swipeResult.getBoardSnapShot();
            if (trial < 3) {
                // System.out.println(boardSnapShot.toString());
            }
            if (boardSnapShot.getMaxResult() > maxResult) {
                maxResult = boardSnapShot.getMaxResult();
            }
            if (prevSwipeDirection != boardSnapShot.getPrevSwipeDirection() ||swipeResult.getIsCompressed()) {
                queue.add(boardSnapShot);
            }

            swipeResult = getResultAfterSwipingLeft(current);
            boardSnapShot = swipeResult.getBoardSnapShot();
            if (trial < 3) {
                // System.out.println(boardSnapShot.toString());
            }
            if (boardSnapShot.getMaxResult() > maxResult) {
                maxResult = boardSnapShot.getMaxResult();
            }
            if (prevSwipeDirection != boardSnapShot.getPrevSwipeDirection() ||swipeResult.getIsCompressed()) {
                queue.add(boardSnapShot);
            }

            swipeResult = getResultAfterSwipingRight(current);
            boardSnapShot = swipeResult.getBoardSnapShot();
            if (trial < 3) {
                // System.out.println(boardSnapShot.toString());
            }
            if (boardSnapShot.getMaxResult() > maxResult) {
                maxResult = boardSnapShot.getMaxResult();
            }
            if (prevSwipeDirection != boardSnapShot.getPrevSwipeDirection() ||swipeResult.getIsCompressed()) {
                queue.add(boardSnapShot);
            }
        }


        return maxResult;
    }

    private SwipeResult getResultAfterSwipingUp(BoardSnapShot board) {
        int[][] arrayBoard = new int[n][n];
        boolean isCompressed = false;

        int[] currentCol;
        for (int i = 0; i < n; i ++) {
            int[] newCol = new int[n];
            int currentColIdx = 0;

            currentCol = board.getCol(i);
            int prevValue = 0;
            for (int j = 0; j < n; j++) {
                if (currentCol[j] == 0) {
                    continue;
                }

                if (prevValue == 0) {
                    prevValue = currentCol[j];
                    continue;
                }


                if (prevValue == currentCol[j]) {
                    newCol[currentColIdx] = prevValue * 2;
                    prevValue = 0;
                    isCompressed = true;
                } else {
                    newCol[currentColIdx] = prevValue;
                    prevValue = currentCol[j];
                }
                currentColIdx++;
            }

            if (prevValue != 0) {
                newCol[currentColIdx++] = prevValue;
            }

            for (int j = currentColIdx; j < n; j++) {
                newCol[j] = 0;
            }

            arrayBoard[i] = newCol;
        }


        return new SwipeResult(BoardSnapShot.createBoardWithCols(board.getTrial() + 1, n, arrayBoard, Direction.UP), isCompressed);
    }

    private SwipeResult getResultAfterSwipingDown(BoardSnapShot board) {
        int[][] arrayBoard = new int[n][n];
        boolean isCompressed = false;

        int[] currentCol;
        for (int i = 0; i < n; i ++) {
            int[] newCol = new int[n];
            int currentColIdx = n - 1;

            currentCol = board.getCol(i);
            int prevValue = 0;
            for (int j = n - 1; j >= 0; j--) {
                if (currentCol[j] == 0) {
                    continue;
                }

                if (prevValue == 0) {
                    prevValue = currentCol[j];
                    continue;
                }


                if (prevValue == currentCol[j]) {
                    newCol[currentColIdx] = prevValue * 2;
                    prevValue = 0;
                    isCompressed = true;
                } else {
                    newCol[currentColIdx] = prevValue;
                    prevValue = currentCol[j];
                }
                currentColIdx--;
            }

            if (prevValue != 0) {
                newCol[currentColIdx--] = prevValue;
            }

            for (int j = currentColIdx; j >= 0; j--) {
                newCol[j] = 0;
            }

            arrayBoard[i] = newCol;
        }


        return new SwipeResult(BoardSnapShot.createBoardWithCols(board.getTrial() + 1, n, arrayBoard, Direction.DOWN), isCompressed);
    }

    private SwipeResult getResultAfterSwipingLeft(BoardSnapShot board) {
        int[][] arrayBoard = new int[n][n];
        boolean isCompressed = false;

        int[] currentRow;
        for (int i = 0; i < n; i ++) {
            int[] newRow = new int[n];
            int currentRowIdx = 0;

            currentRow = board.getRow(i);
            int prevValue = 0;
            for (int j = 0; j < n; j++) {
                if (currentRow[j] == 0) {
                    continue;
                }

                if (prevValue == 0) {
                    prevValue = currentRow[j];
                    continue;
                }


                if (prevValue == currentRow[j]) {
                    newRow[currentRowIdx] = prevValue * 2;
                    prevValue = 0;
                    isCompressed = true;
                } else {
                    newRow[currentRowIdx] = prevValue;
                    prevValue = currentRow[j];
                }
                currentRowIdx++;
            }

            if (prevValue != 0) {
                newRow[currentRowIdx++] = prevValue;
            }

            for (int j = currentRowIdx; j < n; j++) {
                newRow[j] = 0;
            }

            arrayBoard[i] = newRow;
        }


        return new SwipeResult(BoardSnapShot.createBoardWithRows(board.getTrial() + 1, n, arrayBoard, Direction.LEFT), isCompressed);
    }

    private SwipeResult getResultAfterSwipingRight(BoardSnapShot board) {
        int[][] arrayBoard = new int[n][n];
        boolean isCompressed = false;

        int[] currentRow;
        for (int i = 0; i < n; i ++) {
            int[] newRow = new int[n];
            int currentRowIdx = n - 1;

            currentRow = board.getRow(i);
            int prevValue = 0;
            for (int j = n - 1; j >= 0; j--) {
                if (currentRow[j] == 0) {
                    continue;
                }

                if (prevValue == 0) {
                    prevValue = currentRow[j];
                    continue;
                }


                if (prevValue == currentRow[j]) {
                    newRow[currentRowIdx] = prevValue * 2;
                    prevValue = 0;
                    isCompressed = true;
                } else {
                    newRow[currentRowIdx] = prevValue;
                    prevValue = currentRow[j];
                }
                currentRowIdx--;
            }

            if (prevValue != 0) {
                newRow[currentRowIdx--] = prevValue;
            }

            for (int j = currentRowIdx; j >= 0; j--) {
                newRow[j] = 0;
            }

            arrayBoard[i] = newRow;
        }


        return new SwipeResult(BoardSnapShot.createBoardWithRows(board.getTrial() + 1, n, arrayBoard, Direction.RIGHT), isCompressed);
    }
}