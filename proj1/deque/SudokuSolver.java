package deque;

public class SudokuSolver {
    private static final int ROWS = 6;
    private static final int COLS = 6;
    private static final int BLOCK_ROWS = 2;
    private static final int BLOCK_COLS = 3;
    private static final int TOTAL_BLOCKS = (ROWS / BLOCK_ROWS) * (COLS / BLOCK_COLS);

    public static void main(String[] args) {
        int[][] sudoku = {
                {0, 4, 0, 0, 5, 0},
                {0, 0, 0, 4, 0, 0},
                {0, 1, 4, 2, 0, 0},
                {0, 0, 2, 1, 6, 0},
                {0, 0, 1, 0, 0, 0},
                {0, 5, 0, 0, 1, 0}
        };

        if (solveSudoku(sudoku)) {
            System.out.println("Sudoku solved successfully:");
            printSudoku(sudoku);
        } else {
            System.out.println("No solution exists.");
        }
    }

    private static boolean solveSudoku(int[][] sudoku) {
        int row = -1;
        int col = -1;
        boolean isEmpty = true;

        // Find an empty cell
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (sudoku[i][j] == 0) {
                    row = i;
                    col = j;
                    isEmpty = false;
                    break;
                }
            }
            if (!isEmpty) {
                break;
            }
        }

        // If no empty cell found, the sudoku is solved
        if (isEmpty) {
            return true;
        }

        // Try each number from 1 to 6
        for (int num = 1; num <= ROWS; num++) {
            if (isValid(sudoku, row, col, num)) {
                sudoku[row][col] = num;
                if (solveSudoku(sudoku)) {
                    return true;
                }
                sudoku[row][col] = 0; // Backtrack
            }
        }
        return false;
    }

    private static boolean isValid(int[][] sudoku, int row, int col, int num) {
        // Check if num already exists in the row
        for (int j = 0; j < COLS; j++) {
            if (sudoku[row][j] == num) {
                return false;
            }
        }

        // Check if num already exists in the column
        for (int i = 0; i < ROWS; i++) {
            if (sudoku[i][col] == num) {
                return false;
            }
        }

        // Check if num already exists in the block
        int blockRowStart = (row / BLOCK_ROWS) * BLOCK_ROWS;
        int blockColStart = (col / BLOCK_COLS) * BLOCK_COLS;
        for (int i = blockRowStart; i < blockRowStart + BLOCK_ROWS; i++) {
            for (int j = blockColStart; j < blockColStart + BLOCK_COLS; j++) {
                if (sudoku[i][j] == num) {
                    return false;
                }
            }
        }

        return true; // Valid move
    }

    private static void printSudoku(int[][] sudoku) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(sudoku[i][j] + " ");
            }
            System.out.println();
        }
    }
}