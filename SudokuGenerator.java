import java.util.*;

public class SudokuGenerator {
    private static final int SIZE = 9;
    private int[][] board = new int[SIZE][SIZE];
    private Random rand = new Random();

    public int[][] generatePuzzle(String difficulty) {
        fillDiagonalBoxes();
        solveBoard(0, 0);
        removeNumbers(getRemovalCount(difficulty));
        return board;
    }

    private void fillDiagonalBoxes() {
        for (int i = 0; i < SIZE; i += 3)
            fillBox(i, i);
    }

    private void fillBox(int row, int col) {
        Set<Integer> used = new HashSet<>();
        for (int i = 0; i < 9;) {
            int num = rand.nextInt(9) + 1;
            if (used.add(num)) {
                board[row + i / 3][col + i % 3] = num;
                i++;
            }
        }
    }

    private boolean solveBoard(int r, int c) {
        if (r == SIZE) return true;
        if (c == SIZE) return solveBoard(r + 1, 0);
        if (board[r][c] != 0) return solveBoard(r, c + 1);
        for (int num = 1; num <= SIZE; num++) {
            if (isSafe(r, c, num)) {
                board[r][c] = num;
                if (solveBoard(r, c + 1)) return true;
                board[r][c] = 0;
            }
        }
        return false;
    }

    private boolean isSafe(int r, int c, int num) {
        for (int i = 0; i < SIZE; i++)
            if (board[r][i] == num || board[i][c] == num) return false;
        int sr = r - r % 3, sc = c - c % 3;
        for (int i = sr; i < sr + 3; i++)
            for (int j = sc; j < sc + 3; j++)
                if (board[i][j] == num) return false;
        return true;
    }

    private int getRemovalCount(String diff) {
        return switch (diff) {
            case "easy" -> 30;
            case "medium" -> 45;
            case "hard" -> 55;
            default -> 45;
        };
    }

    private void removeNumbers(int count) {
        while (count > 0) {
            int r = rand.nextInt(SIZE), c = rand.nextInt(SIZE);
            if (board[r][c] != 0) {
                board[r][c] = 0;
                count--;
            }
        }
    }
}

