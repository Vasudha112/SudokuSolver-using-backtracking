public class BitmaskSudokuSolver {
    private static final int SIZE = 9;
    private int[][] board;
    private int[] rowMask = new int[SIZE];
    private int[] colMask = new int[SIZE];
    private int[][] boxMask = new int[3][3];

    public BitmaskSudokuSolver(int[][] board) {
        this.board = board;
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++) {
                int val = board[r][c];
                if (val != 0) {
                    int mask = 1 << val;
                    rowMask[r] |= mask;
                    colMask[c] |= mask;
                    boxMask[r / 3][c / 3] |= mask;
                }
            }
    }

    public boolean solve() {
        return solveRec(0, 0);
    }

    private boolean solveRec(int r, int c) {
        if (r == SIZE) return true;
        if (c == SIZE) return solveRec(r + 1, 0);
        if (board[r][c] != 0) return solveRec(r, c + 1);

        for (int num = 1; num <= SIZE; num++) {
            int mask = 1 << num;
            if ((rowMask[r] & mask) == 0 &&
                (colMask[c] & mask) == 0 &&
                (boxMask[r / 3][c / 3] & mask) == 0) {
                board[r][c] = num;
                rowMask[r] |= mask;
                colMask[c] |= mask;
                boxMask[r / 3][c / 3] |= mask;

                if (solveRec(r, c + 1)) return true;

                board[r][c] = 0;
                rowMask[r] ^= mask;
                colMask[c] ^= mask;
                boxMask[r / 3][c / 3] ^= mask;
            }
        }
        return false;
    }

    public int[][] getBoard() {
        return board;
    }
}
