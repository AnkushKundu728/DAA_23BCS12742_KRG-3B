import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class SudokuGUI extends JFrame {
    private JTextField[][] cells = new JTextField[9][9];
    private int[][] solution;
    private int[][] puzzle;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private Timer timer;
    private int timeElapsed = 0;
    private int score = 1000;

    public SudokuGUI() {
        setTitle("Sudoku Game");
        setSize(600, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel boardPanel = new JPanel(new GridLayout(9, 9));

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Arial", Font.BOLD, 20));

                int top = (row % 3 == 0) ? 2 : 1;
                int left = (col % 3 == 0) ? 2 : 1;
                int bottom = (row == 8) ? 2 : ((row + 1) % 3 == 0 ? 2 : 1);
                int right = (col == 8) ? 2 : ((col + 1) % 3 == 0 ? 2 : 1);
                cell.setBorder(new MatteBorder(top, left, bottom, right, Color.BLACK));

                cells[row][col] = cell;
                boardPanel.add(cell);

                int r = row, c = col;
                cell.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) { validateInput(r, c); }
                    public void removeUpdate(DocumentEvent e) { validateInput(r, c); }
                    public void changedUpdate(DocumentEvent e) { validateInput(r, c); }
                });
            }
        }

        JPanel controlPanel = new JPanel();
        scoreLabel = new JLabel("Score: " + score);
        timerLabel = new JLabel("Time: 0s");
        JButton resetButton = new JButton("Reset");
        JButton hintButton = new JButton("Hint");
        JButton solveButton = new JButton("Solve");

        resetButton.addActionListener(e -> resetGame());
        hintButton.addActionListener(e -> giveHint());
        solveButton.addActionListener(e -> solveAndDisplay());

        controlPanel.add(scoreLabel);
        controlPanel.add(timerLabel);
        controlPanel.add(hintButton);
        controlPanel.add(resetButton);
        controlPanel.add(solveButton);

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        resetGame();
    }

    private void validateInput(int row, int col) {
        String text = cells[row][col].getText();
        if (text.length() != 1 || !text.matches("[1-9]")) {
            cells[row][col].setBackground(Color.PINK);
            return;
        }
        int value = Integer.parseInt(text);
        if (value == solution[row][col]) {
            cells[row][col].setBackground(Color.WHITE);
            checkCompletion();
        } else {
            cells[row][col].setBackground(Color.RED);
            score -= 10;
            scoreLabel.setText("Score: " + score);
        }
    }

    private void checkCompletion() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (!cells[row][col].getText().equals(String.valueOf(solution[row][col]))) {
                    return;
                }
            }
        }
        timer.stop();
        JOptionPane.showMessageDialog(this, "Congratulations! Sudoku Solved in " + timeElapsed + " seconds.\nScore: " + score);
    }

    private void giveHint() {
        Random rand = new Random();
        while (true) {
            int r = rand.nextInt(9);
            int c = rand.nextInt(9);
            if (cells[r][c].getText().isEmpty()) {
                cells[r][c].setText(String.valueOf(solution[r][c]));
                cells[r][c].setEditable(false);
                cells[r][c].setBackground(new Color(200, 255, 200));
                score -= 20;
                scoreLabel.setText("Score: " + score);
                break;
            }
        }
    }

    private void resetGame() {
        puzzle = generatePuzzle();
        solution = new int[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                solution[r][c] = puzzle[r][c];
            }
        }
        solveSudoku(solution, 0, 0);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (puzzle[row][col] != 0) {
                    cells[row][col].setText(String.valueOf(puzzle[row][col]));
                    cells[row][col].setEditable(false);
                    cells[row][col].setBackground(Color.LIGHT_GRAY);
                } else {
                    cells[row][col].setText("");
                    cells[row][col].setEditable(true);
                    cells[row][col].setBackground(Color.WHITE);
                }
            }
        }

        score = 1000;
        timeElapsed = 0;
        scoreLabel.setText("Score: " + score);
        timerLabel.setText("Time: 0s");

        if (timer != null) timer.stop();
        timer = new Timer(1000, e -> {
            timeElapsed++;
            timerLabel.setText("Time: " + timeElapsed + "s");
        });
        timer.start();
    }

    private boolean solveSudoku(int[][] board, int row, int col) {
        if (row == 9) return true;
        int nrow = (col == 8) ? row + 1 : row;
        int ncol = (col == 8) ? 0 : col + 1;

        if (board[row][col] != 0)
         return solveSudoku(board, nrow, ncol);

        for (int num = 1; num <= 9; num++) {
            if (isSafe(board, row, col, num)) {
                board[row][col] = num;
                if (solveSudoku(board, nrow, ncol)) return true;
                board[row][col] = 0;
            }
        }
        return false;
    }

    private boolean isSafe(int[][] board, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == num || board[row][i] == num) return false;
        }
        int sr = (row / 3) * 3;
        int sc = (col / 3) * 3;
        for (int i = sr; i < sr + 3; i++) {
            for (int j = sc; j < sc + 3; j++) {
                if (board[i][j] == num) return false;
            }
        }
        return true;
    }

    private int[][] generatePuzzle() {
        int[][] board = new int[9][9];
        solveSudoku(board, 0, 0);
        Random rand = new Random();
        for (int i = 0; i < 40; i++) {
            int r = rand.nextInt(9);
            int c = rand.nextInt(9);
            board[r][c] = 0;
        }
        return board;
    }

    private void solveAndDisplay() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cells[row][col].setText(String.valueOf(solution[row][col]));
                cells[row][col].setBackground(new Color(220, 255, 220));
                cells[row][col].setEditable(false);
            }
        }
        timer.stop();
        JOptionPane.showMessageDialog(this, "Sudoku Solved Automatically\nScore: " + score);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SudokuGUI().setVisible(true));
    }
}
