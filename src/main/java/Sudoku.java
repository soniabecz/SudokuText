import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;


public class Sudoku {

    public static void main(String[] args) throws IOException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException {
        int N = 9, K = 0;


        SudokuModel model = new SudokuModel(N,K);
        SudokuView view1 = new SudokuView(N,K);

        SudokuController sudoku = new SudokuController(model,view1);

        sudoku.buttons(model.board);
        view1.welcomeWindow();

    }
}
