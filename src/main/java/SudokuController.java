import javax.sound.sampled.*;
import java.io.*;

public class SudokuController {

    SudokuModel model;
    SudokuView sudokuView;

    public SudokuController(SudokuModel model, SudokuView view) {
        this.model = model;
        this.sudokuView = view;
    }

    public void buttons(int[][] board) {
        sudokuView.easy.addListener(easy -> {
            sudokuView.nick= sudokuView.textBox.getText();
            try {
                clickedDifficultyButton(9,10,1,board);
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        sudokuView.medium.addListener(medium -> {
            try {
                sudokuView.nick= sudokuView.textBox.getText();
                clickedDifficultyButton(9,20,2,board);
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        sudokuView.hard.addListener(hard -> {
            try {
                sudokuView.nick= sudokuView.textBox.getText();
                clickedDifficultyButton(9,30,3,board);
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void play (int N, int K, int board[][]) throws IOException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException {

        model.N = N;
        model.K = K;

        model.fillValues();

        int [] filledBoard[] = new int[N][N];

        for (int i = 0; i<N; i++)
        {
            for (int j = 0; j<N; j++) {
                filledBoard[i][j] = board[i][j];
            }

        }

        model.removeKDigits();
        sudokuView.printSudoku(board);


        for (int i = 0; i<N; i++)
        {
            for (int j = 0; j<N; j++)
                System.out.print(filledBoard[i][j] + " ");
            System.out.println();
        }
        System.out.println();

        sudokuView.navigation(filledBoard, model.mode, model.correctAnswers, model.incorrectAnswers, model.hintsUsed, board, model.marks);
    }

    public void clickedDifficultyButton(int N, int K, int diff, int board[][]) throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException {
        sudokuView.N = N;
        sudokuView.K = K;
        sudokuView.difficulty = diff;
        System.out.println(sudokuView.nick);
        play(N,K, model.board);
    }
}
