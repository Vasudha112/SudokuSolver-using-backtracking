import javax.swing.*;
import java.awt.*;

public class SudokuSolverApp extends JFrame {
    private static final int SIZE = 9;
    private JTextField[][] cells = new JTextField[SIZE][SIZE];
    private int[][] board = new int[SIZE][SIZE];
    private int[][] solvedBoard = new int[SIZE][SIZE];

    public SudokuSolverApp() {
        setTitle("Sudoku Solver (GUI + Check Feature)");
        setSize(600, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
        Font font = new Font("Arial", Font.BOLD, 20);
        for (int r = 0; r < SIZE; r++) {
    for (int c = 0; c < SIZE; c++) {
        JTextField cell = new JTextField();
        cell.setHorizontalAlignment(JTextField.CENTER);
        cell.setFont(font);

        // Calculate border thickness for 3x3 box effect
        int top = (r % 3 == 0) ? 3 : 1;
        int left = (c % 3 == 0) ? 3 : 1;
        int bottom = (r == 8) ? 3 : 1;
        int right = (c == 8) ? 3 : 1;

        cell.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
        cells[r][c] = cell;
        gridPanel.add(cell);
    }
}

        JPanel controlPanel = new JPanel();
        String[] levels = {"easy", "medium", "hard"};
        JComboBox<String> levelBox = new JComboBox<>(levels);

        JButton genButton = new JButton("Generate");
        JButton solveButton = new JButton("Solve");
        JButton clearButton = new JButton("Clear");
        JButton checkButton = new JButton("Check");

        genButton.addActionListener(e -> generatePuzzle((String) levelBox.getSelectedItem()));
        solveButton.addActionListener(e -> solvePuzzle());
        clearButton.addActionListener(e -> clearBoard());
        checkButton.addActionListener(e -> checkSolution());

        controlPanel.add(levelBox);
        controlPanel.add(genButton);
        controlPanel.add(solveButton);
        controlPanel.add(clearButton);
        controlPanel.add(checkButton);

        add(gridPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void generatePuzzle(String difficulty) {
        SudokuGenerator gen = new SudokuGenerator();
        board = gen.generatePuzzle(difficulty);
        BitmaskSudokuSolver solver = new BitmaskSudokuSolver(deepCopyBoard(board));
        solver.solve();
        solvedBoard = deepCopyBoard(solver.getBoard());
        updateUIFromBoard();
    }

    private void solvePuzzle() {
        readUIToBoard();
        BitmaskSudokuSolver solver = new BitmaskSudokuSolver(board);
        if (solver.solve()) {
            board = solver.getBoard();
            solvedBoard = deepCopyBoard(board);
            updateUIFromBoard();
            JOptionPane.showMessageDialog(this, "✅ Solved successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "❌ No solution found.");
        }
    }

    private void clearBoard() {
        board = new int[SIZE][SIZE];
        solvedBoard = new int[SIZE][SIZE];
        updateUIFromBoard();
    }

    private void checkSolution() {
        readUIToBoard();
        boolean correct = true;
        for (int r = 0; r < SIZE && correct; r++) {
            for (int c = 0; c < SIZE && correct; c++) {
                if (board[r][c] != solvedBoard[r][c]) {
                    correct = false;
                }
            }
        }
        if (correct) {
            JOptionPane.showMessageDialog(this, "✅ Your solution is correct!");
        } else {
            JOptionPane.showMessageDialog(this, "❌ Incorrect solution. Try again!");
        }
    }

    private void readUIToBoard() {
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++) {
                try {
                    board[r][c] = Integer.parseInt(cells[r][c].getText());
                } catch (NumberFormatException ex) {
                    board[r][c] = 0;
                }
            }
    }

    private void updateUIFromBoard() {
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                cells[r][c].setText(board[r][c] == 0 ? "" : Integer.toString(board[r][c]));
    }

    private int[][] deepCopyBoard(int[][] src) {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
            System.arraycopy(src[i], 0, copy[i], 0, SIZE);
        return copy;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SudokuSolverApp app = new SudokuSolverApp();
            app.setVisible(true);
        });
    }
}
