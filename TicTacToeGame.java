package Tugas;

import java.util.Scanner;
import java.util.Random;

public class TicTacToeGame {
    private char[][] board;
    private char currentPlayer;
    private boolean isBotEnabled;
    private boolean isTwoPlayerMode;
    private InputHandler inputHandler;
    private OutputHandler outputHandler;

    private static final int BOARD_SIZE = 3;
    private static final char PLAYER_X = 'X';
    private static final char PLAYER_O = 'O';

    public TicTacToeGame() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        currentPlayer = PLAYER_X;
        isBotEnabled = true;

        initializeBoard();
        inputHandler = new InputHandler();
        outputHandler = new OutputHandler();
    }

    public static void main(String[] args) {
        TicTacToeGame game = new TicTacToeGame();
        game.selectGameMode();
        game.play();
    }

    private void initializeBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = ' ';
            }
        }
    }

    private void selectGameMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Pilih Mode:");
        System.out.println("1. Solo PLayer");
        System.out.println("2. Multi Player");
        int choice = inputHandler.readInt(scanner, "Masukkan pilihan anda: ");

        if (choice == 1) {
            isBotEnabled = true;
            isTwoPlayerMode = false;
        } else if (choice == 2) {
            isBotEnabled = false;
            isTwoPlayerMode = true;
        }
    }

    private void play() {
        outputHandler.printBoard(board);

        while (!isGameOver()) {
            if (isBotEnabled && currentPlayer == PLAYER_O) {
                makeBotMove();
            } else {
                makePlayerMove();
            }

            outputHandler.printBoard(board);

            if (!isGameOver()) {
                switchPlayer();
            }
        }

        char winner = getWinner();
        outputHandler.printResult(winner);
    }

    private void makePlayerMove() {
        int row, col;
        do {
            try {
                outputHandler.printPrompt(currentPlayer);
                row = inputHandler.readInt(new Scanner(System.in), "Masukkan arah horizontal (0-" + (BOARD_SIZE - 1) + "): ");
                col = inputHandler.readInt(new Scanner(System.in), "Masukkan arah Vertikal (0-" + (BOARD_SIZE - 1) + "): ");

                if (!isValidMove(row, col)) {
                    outputHandler.printInvalidMove();
                    continue;
                }

                board[row][col] = currentPlayer;
                break;
            } catch (NumberFormatException e) {
                outputHandler.printInvalidInput();
            }
        } while (true);
    }

    private void makeBotMove() {
        Random random = new Random();
        int row, col;

        do {
            row = random.nextInt(BOARD_SIZE);
            col = random.nextInt(BOARD_SIZE);
        } while (!isValidMove(row, col));

        board[row][col] = currentPlayer;
    }

    private boolean isValidMove(int row, int col) {
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return false;
        }

        if (board[row][col] != ' ') {
            return false;
        }

        return true;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X;
    }

    private boolean isGameOver() {
        return getWinner() != ' ' || isBoardFull();
    }

    private char getWinner() {
        // Check horizontal
        for (int row = 0; row < BOARD_SIZE; row++) {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2] && board[row][0] != ' ') {
                return board[row][0];
            }
        }

        // Check vertikal
        for (int col = 0; col < BOARD_SIZE; col++) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col] && board[0][col] != ' ') {
                return board[0][col];
            }
        }

        // Check diagonal
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != ' ') {
            return board[0][0];
        }

        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != ' ') {
            return board[0][2];
        }

        return ' ';
    }

    private boolean isBoardFull() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
}

class InputHandler {
    public int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine());
    }
}

class OutputHandler {
    public void printBoard(char[][] board) {
        System.out.println("-------------");
        for (int row = 0; row < board.length; row++) {
            System.out.print("| ");
            for (int col = 0; col < board[row].length; col++) {
                System.out.print(board[row][col] + " | ");
            }
            System.out.println("\n-------------");
        }
    }

    public void printInvalidMove() {
        System.out.println("Gerakkan tidak valid, coba lagi.");
    }

    public void printInvalidInput() {
        System.out.println("Input tidak valid, coba lagi.");
    }

    public void printResult(char winner) {
        if (winner == ' ') {
            System.out.println("SERI!");
        } else {
            System.out.println("Pemain " + winner + " MENANG!");
        }
    }

    public void printPrompt(char currentPlayer) {
        System.out.println("Giliran Pemain " + currentPlayer);
    }
}