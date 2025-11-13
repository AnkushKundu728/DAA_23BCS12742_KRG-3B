public class sudokuSolver {

    public boolean isSafe(char[][] board, int row, int col, int number) {
        char numChar = (char) (number + '0');

        // Row and Column Check
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == numChar || board[row][i] == numChar) {
                return false;
            }
        }

        // 3x3 Grid Check
        int sr = (row / 3) * 3;
        int sc = (col / 3) * 3;
        for (int i = sr; i < sr + 3; i++) {
            for (int j = sc; j < sc + 3; j++) {
                if (board[i][j] == numChar) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean helper(char[][] board, int row, int col) {
        if (row == board.length) {
            return true;
        }

        int nrow = (col == board.length - 1) ? row + 1 : row;
        int ncol = (col == board.length - 1) ? 0 : col + 1;

        if (board[row][col] != '.') {
            return helper(board, nrow, ncol);
        } else {
            for (int i = 1; i <= 9; i++) {
                if (isSafe(board, row, col, i)) {
                    board[row][col] = (char) (i + '0');

                    if (helper(board, nrow, ncol)) {
                        return true;
                    }

                    board[row][col] = '.'; // backtrack
                }
            }
        }

        return false;
    }

    public void solveSudoku(char[][] board) {
        helper(board, 0, 0);
    }
    public static void main(String args[]){
        
        char[][] board = {
            {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
            {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
            {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
            {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
            {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
            {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
            {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
            {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
            {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
        };
        System.out.println("Unsolved Sudoku:\n");
    for (char[] row : board) {
        for (char c : row) {
            System.out.print(c + " ");
        }
        System.out.println();
    }
        System.out.println("Solved Sudoku:\n ");
        sudokuSolver solver= new sudokuSolver();
        solver.solveSudoku(board);
        for (char[] row: board){
            for(char c: row){
                System.out.print(c+" ");
            }
            System.out.println();
        }
        
    }
    
}
