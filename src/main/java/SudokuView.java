import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class SudokuView {
    int N;
    int K;

    public String nick;
    public int difficulty;
    public Player player = new Player("", "");

    int ms;
    int s;
    int min;
    int h;

    Button easy;
    Button medium;
    Button hard;
    TextBox textBox;

    Terminal terminal;
    Screen screen;

    int row = 2;
    int col = 3;

    boolean isEasy = false, isMedium = false , isHard = false;


    SudokuView(int N, int K) throws IOException {
        this.N = N;
        this.K = K;
        ms=0;
        s=0;
        min=0;
        h=0;
        easy = new Button("Łatwy");
        medium = new Button("Średni");
        hard = new Button("Trudny");
        terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
    }
    public void printSudoku(int board[][]) throws IOException, InterruptedException {

        TextGraphics tGraphics = screen.newTextGraphics();

        screen.startScreen();
        screen.clear();

        tGraphics.setBackgroundColor(new TextColor.RGB(182, 234, 218));
        tGraphics.setForegroundColor(new TextColor.RGB(3, 0, 28));

        String topRow = "\u2554\u2550\u2550\u2550\u2564\u2550\u2550\u2550\u2564"+
                "\u2550\u2550\u2550\u2566\u2550\u2550\u2550\u2564\u2550"+
                "\u2550\u2550\u2564\u2550\u2550\u2550\u2566\u2550\u2550"+
                "\u2550\u2564\u2550\u2550\u2550\u2564\u2550\u2550\u2550\u2557";

        String middleRow = "\u2551   \u2502   \u2502   \u2551   \u2502" +
                "   \u2502   \u2551   \u2502   \u2502   \u2551";

        String middleRow2 = "\u255f\u2500\u2500\u2500\u253c\u2500\u2500\u2500\u253c" +
                "\u2500\u2500\u2500\u256b\u2500\u2500\u2500\u253c" +
                "\u2500\u2500\u2500\u253c\u2500\u2500\u2500\u256b" +
                "\u2500\u2500\u2500\u253c\u2500\u2500\u2500\u253c" +
                "\u2500\u2500\u2500\u2562";

        String middleRow3 = "\u2560\u2550\u2550\u2550\u256a\u2550\u2550\u2550\u256a" +
                "\u2550\u2550\u2550\u256c\u2550\u2550\u2550\u256a" +
                "\u2550\u2550\u2550\u256a\u2550\u2550\u2550\u256c" +
                "\u2550\u2550\u2550\u256a\u2550\u2550\u2550\u256a" +
                "\u2550\u2550\u2550\u2563";

        String botRow = "\u255a\u2550\u2550\u2550\u2567\u2550\u2550\u2550\u2567" +
                "\u2550\u2550\u2550\u2569\u2550\u2550\u2550\u2567" +
                "\u2550\u2550\u2550\u2567\u2550\u2550\u2550\u2569" +
                "\u2550\u2550\u2550\u2567\u2550\u2550\u2550\u2567" +
                "\u2550\u2550\u2550\u255d";

        int startColumn = 1;
        int startRow = 1;

        tGraphics.putString(new TerminalPosition(startColumn,startRow++), topRow);

        for (int k = 0; k < 3; k++) {
            tGraphics.putString(new TerminalPosition(startColumn,startRow++), middleRow);
            tGraphics.putString(new TerminalPosition(startColumn,startRow++), middleRow2);
            tGraphics.putString(new TerminalPosition(startColumn,startRow++), middleRow);
            tGraphics.putString(new TerminalPosition(startColumn,startRow++), middleRow2);
            tGraphics.putString(new TerminalPosition(startColumn,startRow++), middleRow);

            if (k != 2)
                tGraphics.putString(new TerminalPosition(startColumn,startRow++), middleRow3);
        }
        tGraphics.putString(new TerminalPosition(startColumn,startRow++), botRow);

        screen.refresh();

        int row = 2;

        for (int i = 0; i < 9; i++) {
            int col = -1;
            for (int j = 0; j < 9; j++) {
                int ch = board[i][j];
                StringBuilder sb = new StringBuilder();
                sb.append(ch);
                if (ch < '1') {
                    col += 4;
                }

                if (board[i][j] != 0) {
                    tGraphics.putString(new TerminalPosition(col,row), sb.toString() + " ");
                }
                else {
                    tGraphics.putString(new TerminalPosition(col,row),  "  ");
                }

            }

            row += 2;

        }

        tGraphics.setBackgroundColor(new TextColor.RGB(3, 0, 28));
        tGraphics.setForegroundColor(new TextColor.RGB(250, 236, 214));

        tGraphics.putString(new TerminalPosition(55,7), "R", SGR.BOLD);
        tGraphics.putString(new TerminalPosition(56,7), "ozwiąż");
        tGraphics.putString(new TerminalPosition(55,9), "N", SGR.BOLD);
        tGraphics.putString(new TerminalPosition(56,9), "otatki");
        tGraphics.putString(new TerminalPosition(55,11), "P", SGR.BOLD);
        tGraphics.putString(new TerminalPosition(56,11), "odpowiedź");
        tGraphics.putString(new TerminalPosition(55,13), "Z", SGR.BOLD);
        tGraphics.putString(new TerminalPosition(56,13), "akończ");


        screen.setCursorPosition(new TerminalPosition(3,2));


        tGraphics.setBackgroundColor(new TextColor.RGB(182, 234, 218));
        tGraphics.setForegroundColor(new TextColor.RGB(3, 0, 28));


        //screen.refresh();
        for (int i = 0; i<N; i++)
        {
            for (int j = 0; j<N; j++)
                System.out.print(board[i][j] + " ");
            System.out.println();
        }
        System.out.println();

    }

    public void Timer() throws IOException, InterruptedException {

        TextGraphics textGraphics = screen.newTextGraphics();


            if(h<10) {
                if (min < 10) {
                    if (s < 10) {
                        textGraphics.putString(new TerminalPosition(51, 3), "Czas: ",SGR.BOLD);
                        textGraphics.putString(new TerminalPosition(57, 3), "0" + h + ":0" + min + ":0" + s + "," + ms);
                    } else {
                        textGraphics.putString(new TerminalPosition(51, 3), "Czas: ",SGR.BOLD);
                        textGraphics.putString(new TerminalPosition(57, 3), "0" + h + ":0" + min + ":" + s + "," + ms);
                    }
                } else {
                    if (s < 10) {
                        textGraphics.putString(new TerminalPosition(51, 3), "Czas: ",SGR.BOLD);
                        textGraphics.putString(new TerminalPosition(57, 3), "0" + h + ":" + min + ":0" + s + "," + ms);
                    } else {
                        textGraphics.putString(new TerminalPosition(51, 3), "Czas: ",SGR.BOLD);
                        textGraphics.putString(new TerminalPosition(57, 3), "0" + h + ":" + min + ":" + s + "," + ms);
                    }
                }
            }

            else {
                if (min < 10) {
                    if (s < 10) {
                        textGraphics.putString(new TerminalPosition(51, 3), "Czas: ",SGR.BOLD);
                        textGraphics.putString(new TerminalPosition(57, 3), h + "0" + min + ":0" + s + "," + ms);
                    } else {
                        textGraphics.putString(new TerminalPosition(51, 3), "Czas: ",SGR.BOLD);
                        textGraphics.putString(new TerminalPosition(57, 3), h + "0" + min + ":" + s + "," + ms);
                    }
                } else {
                    if (s < 10) {
                        textGraphics.putString(new TerminalPosition(51, 3), "Czas: ",SGR.BOLD);
                        textGraphics.putString(new TerminalPosition(57, 3), h + min + ":0" + s + "," + ms);
                    } else {
                        textGraphics.putString(new TerminalPosition(51, 3), "Czas: ",SGR.BOLD);
                        textGraphics.putString(new TerminalPosition(57, 3), h + min + ":" + s + "," + ms);
                    }
                }
            }

            screen.refresh();
        }

        public void winnerMessageWindow() throws IOException {

        WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        textGUI.setTheme(new SimpleTheme(new TextColor.RGB(3, 0, 28), new TextColor.RGB(160, 193, 184)));

        Window window = new BasicWindow();

        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        panel.setTheme(new SimpleTheme(new TextColor.RGB(3, 0, 28), new TextColor.RGB(160, 193, 184)));

        File smiley = new File("/Users/soniabecz/Desktop/school./semestr piąty/komunikacja_człowiek-komputer/sudoku_text/Unknown.png");
        BufferedImage image = ImageIO.read(smiley);
        int w = 40;
        int hi = 50;
        BufferedImage win = new BufferedImage(w, hi, BufferedImage.TYPE_INT_RGB);
        Graphics g = win.getGraphics();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        AffineTransform tx = new AffineTransform();

        double scalex = .38;
        double scaley = .28;
        tx.scale(scalex, scaley);

        graphics.setTransform(tx);
        graphics.drawImage(image,tx,null);

        TextBox textBox = new TextBox(new TerminalSize(w+5,hi+5));

        for (int i = 0; i < hi; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < w; j++) {
                sb.append(win.getRGB(j, i) == -16777216 ? " " : '*');
            }

            if (sb.toString().trim().isEmpty()) {
                continue;
            }

            textBox.addLine(sb.toString());
        }

        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        panel.addComponent(textBox);
        panel.addComponent(new TextBox("Gratulacje!!\n\nCzas gry: " + h + ":" + min + ":" + s + "," + ms), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        panel.addComponent(new Button("Wyniki", new Runnable() {
            @Override
            public void run() {
                try {
                    scoresWindow();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }),LinearLayout.createLayoutData(LinearLayout.Alignment.End));

        panel.addComponent(new EmptySpace());

        panel.addComponent(new Button("OK", new Runnable() {
            @Override
            public void run() {
                try {
                    screen.close();
                    Sudoku.main(null);
                } catch (IOException | InterruptedException | UnsupportedAudioFileException | LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
            }
        }),LinearLayout.createLayoutData(LinearLayout.Alignment.End));
        window.setComponent(panel.withBorder(Borders.doubleLine("Zwycięstwo")));


        textGUI.addWindowAndWait(window);
    }

    public void loserMessageWindow() throws IOException {

        WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        textGUI.setTheme(new SimpleTheme(new TextColor.RGB(3, 0, 28), new TextColor.RGB(160, 193, 184)));

        Window window = new BasicWindow();

        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        panel.setTheme(new SimpleTheme(new TextColor.RGB(3, 0, 28), new TextColor.RGB(160, 193, 184)));

        File sad = new File("/Users/soniabecz/Desktop/school./semestr piąty/komunikacja_człowiek-komputer/sudoku_text/Unknown2.png");
        BufferedImage image = ImageIO.read(sad);
        int w = 40;
        int hi = 50;
        BufferedImage win = new BufferedImage(w, hi, BufferedImage.TYPE_INT_RGB);
        Graphics g = win.getGraphics();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        AffineTransform tx = new AffineTransform();

        double scalex = .4;
        double scaley = .28;
        tx.scale(scalex, scaley);

        graphics.setTransform(tx);
        graphics.drawImage(image,tx,null);

        TextBox textBox = new TextBox(new TerminalSize(w+5,hi+5));

        for (int i = 0; i < hi; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < w; j++) {
                sb.append(win.getRGB(j, i) == -16777216 ? " " : '*');
            }

            if (sb.toString().trim().isEmpty()) {
                continue;
            }

            textBox.addLine(sb.toString());
        }

        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        panel.addComponent(textBox);
        panel.addComponent(new TextBox("Niestety :-(\n\nCzas gry: " + h + ":" + min + ":" + s + "," + ms), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        panel.addComponent(new Button("OK", new Runnable() {
            @Override
            public void run() {
                try {
                    screen.close();
                    Sudoku.main(null);
                } catch (IOException | InterruptedException | UnsupportedAudioFileException | LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
            }
        }),LinearLayout.createLayoutData(LinearLayout.Alignment.End));
        window.setComponent(panel.withBorder(Borders.doubleLine("Przegrana")));


        textGUI.addWindowAndWait(window);
    }

    public void exitMessageWindow() throws IOException {

        WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        textGUI.setTheme(new SimpleTheme(new TextColor.RGB(3, 0, 28), new TextColor.RGB(160, 193, 184)));

        Window window = new BasicWindow();

        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        panel.setTheme(new SimpleTheme(new TextColor.RGB(3, 0, 28), new TextColor.RGB(160, 193, 184)));

        File sad = new File("/Users/soniabecz/Desktop/school./semestr piąty/komunikacja_człowiek-komputer/sudoku_text/images.png");
        BufferedImage image = ImageIO.read(sad);
        int w = 40;
        int hi = 50;
        BufferedImage win = new BufferedImage(w, hi, BufferedImage.TYPE_INT_RGB);
        Graphics g = win.getGraphics();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        AffineTransform tx = new AffineTransform();

        double scalex = .4;
        double scaley = .28;
        tx.scale(scalex, scaley);

        graphics.setTransform(tx);
        graphics.drawImage(image,tx,null);

        TextBox textBox = new TextBox(new TerminalSize(w+5,hi+5));

        for (int i = 0; i < hi; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < w; j++) {
                sb.append(win.getRGB(j, i) == -16777216 ? " " : '*');
            }

            if (sb.toString().trim().isEmpty()) {
                continue;
            }

            textBox.addLine(sb.toString());
        }


        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        panel.addComponent(textBox);
        panel.addComponent(new TextBox("Opuściłeś grę\n\nCzas gry: " + h + ":" + min + ":" + s + "," + ms), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        panel.addComponent(new Button("OK", new Runnable() {
            @Override
            public void run() {
                try {
                    screen.close();
                    Sudoku.main(null);
                } catch (IOException | InterruptedException | UnsupportedAudioFileException | LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
            }
        }),LinearLayout.createLayoutData(LinearLayout.Alignment.End));
        window.setComponent(panel.withBorder(Borders.doubleLine("Wyjście")));


        textGUI.addWindowAndWait(window);
    }

    public void welcomeWindow () throws IOException {

        WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);

        int width = 70;
        int height = 20;

        textGUI.setTheme(new SimpleTheme(new TextColor.RGB(3, 0, 28), new TextColor.RGB(242, 222, 186)));


        Panel panel = new Panel();
        Window window = new BasicWindow();

        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        panel.setTheme(new SimpleTheme(new TextColor.RGB(3, 0, 28), new TextColor.RGB(250, 236, 214)));


        Button button = new Button("Play", new Runnable() {
            @Override
            public void run() {
                try {
                    difficultyWindow();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        button.setTheme(new SimpleTheme(new TextColor.RGB(53, 31, 57),new TextColor.RGB(250, 236, 214)));

        screen.startScreen();
        screen.clear();

        BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setFont(new Font("SansSerif",Font.PLAIN,15));

        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.drawString("SUDOKU",3,14);


        TextBox textBox = new TextBox(new TerminalSize(width+1,height+5));
        textBox.setReadOnly(true);
        textBox.addLine("Witaj w grze\n");


        for (int i = 0; i < height; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < width; j++) {
                int n = (int) Math.floor((Math.random()*10));
                sb.append(image.getRGB(j,i) == -16777216 ? " " : n);
            }

            if (sb.toString().trim().isEmpty()) {
                continue;
            }

            textBox.setTheme(new SimpleTheme(new TextColor.RGB(114, 106, 149),new TextColor.RGB(250, 236, 214)));
            textBox.addLine(sb.toString());

            screen.refresh();
        }

        TextBox textBox1 = new TextBox(new TerminalSize(width+5,1));


        window.setHints(Arrays.asList(Window.Hint.CENTERED));
        panel.addComponent(textBox, LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        panel.addComponent(textBox1);
        panel.addComponent(button.withBorder(Borders.doubleLine()), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        window.setComponent(panel.withBorder(Borders.doubleLine("SUDOKU")));


        textGUI.addWindowAndWait(window);

        window.close();
    }

    public void scoresWindow() throws IOException {

        WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        textGUI.setTheme(new SimpleTheme(new TextColor.RGB(3, 0, 28), new TextColor.RGB(242, 222, 186)));

        Panel panel = new Panel();
        Window window = new BasicWindow();

        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        window.setTheme(new SimpleTheme(new TextColor.RGB(53, 31, 57),new TextColor.RGB(113, 159, 176)));
        panel.setTheme(new SimpleTheme(new TextColor.RGB(53, 31, 57),new TextColor.RGB(113, 159, 176)));

        TextBox textBox = new TextBox(new TerminalSize(30,10));
        textBox.addLine("   Nick   Czas");
        textBox.addLine("");

        if (difficulty == 1) {
            player.sort("/Users/soniabecz/Desktop/school./semestr piąty/komunikacja_człowiek-komputer/sudoku_text/scores_easy.txt", textBox);
        }

        if (difficulty == 2) {
            player.sort("/Users/soniabecz/Desktop/school./semestr piąty/komunikacja_człowiek-komputer/sudoku_text/scores_medium.txt", textBox);
        }

        if (difficulty == 3) {
            player.sort("/Users/soniabecz/Desktop/school./semestr piąty/komunikacja_człowiek-komputer/sudoku_text/scores_hard.txt", textBox);
        }

        panel.addComponent(textBox);
        panel.addComponent(new EmptySpace(TerminalSize.ONE));
        panel.addComponent(new Button("Exit", window::close));
        window.setComponent(panel.withBorder(Borders.singleLine("Tabela wyników")));

        textGUI.addWindowAndWait(window);

        window.close();

        screen.clear();

    }

    public void difficultyWindow() throws IOException {

        WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        textGUI.setTheme(new SimpleTheme(new TextColor.RGB(3, 0, 28), new TextColor.RGB(242, 222, 186)));

        Panel panel = new Panel();
        Window window = new BasicWindow();

        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        window.setTheme(new SimpleTheme(new TextColor.RGB(53, 31, 57),new TextColor.RGB(113, 159, 176)));
        panel.setTheme(new SimpleTheme(new TextColor.RGB(53, 31, 57),new TextColor.RGB(113, 159, 176)));

        textBox = new TextBox(new TerminalSize(30,1));



        easy.setTheme(new SimpleTheme(new TextColor.RGB(78, 108, 80),new TextColor.RGB(113, 159, 176)));

        easy.addListener(easy -> {
            nick=textBox.getText();
            isEasy = true;
        });

        medium.setTheme(new SimpleTheme(new TextColor.RGB(130, 0, 0),new TextColor.RGB(113, 159, 176)));

        medium.addListener(medium -> {
            nick=textBox.getText();
            isMedium = true;
        });

        hard.setTheme(new SimpleTheme(new TextColor.RGB(130, 0, 0),new TextColor.RGB(113, 159, 176)));

        hard.addListener(hard -> {
            nick=textBox.getText();
            isHard = true;
        });

        panel.addComponent(new EmptySpace(TerminalSize.ONE));
        panel.addComponent(textBox.withBorder(Borders.singleLine("Podaj nick")));
        panel.addComponent(new EmptySpace(TerminalSize.ONE));
        panel.addComponent(easy.withBorder(Borders.singleLine()), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        panel.addComponent(medium.withBorder(Borders.singleLine()), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        panel.addComponent(hard.withBorder(Borders.singleLine()), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        panel.addComponent(new EmptySpace(TerminalSize.ONE));
        panel.addComponent(new Button("Exit", window::close));
        window.setComponent(panel.withBorder(Borders.singleLine("Wybierz nick i poziom trudności")));

        textGUI.addWindowAndWait(window);

        window.close();

    }

    public boolean isWon(int correctAnswers) {
        if (correctAnswers == K) {
            return true;
        }
        else {
            return false;
        }
    }

    public void up() throws IOException {
        if (row > 2) {
            row -= 2;
        }
        else {
            row = 18;
        }
        screen.setCursorPosition(new TerminalPosition(col,row));
        screen.refresh();
    }

    public void down() throws IOException {
        if (row < 18) {
            row += 2;
        }
        else {
            row = 2;
        }
        screen.setCursorPosition(new TerminalPosition(col,row));
        screen.refresh();
    }

    public void left() throws IOException {
        if (col > 3) {
            col -= 4;
        }
        else {
            col = 35;
        }
        screen.setCursorPosition(new TerminalPosition(col,row));
        screen.refresh();
    }

    public void right() throws IOException {
        if (col < 35) {
            col += 4;
        }
        else {
            col = 3;
        }
        screen.setCursorPosition(new TerminalPosition(col,row));
        screen.refresh();
    }

    public void insert(Character c, int col, int row, int [] filledBoard[], int mode, int board[][], int correctAnswers, int incorrectAnswers, int marks[][][]) throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        TextGraphics tGraphics = screen.newTextGraphics();



        int numRow = (row / 2) - 1;
        int numCol = (col - 3) / 4;

        tGraphics.setBackgroundColor(new TextColor.RGB(182, 234, 218));

        if(mode == 1) {

            if (board[numRow][numCol] == 0) {
                for (int i=0;i<Math.sqrt(N);i++) {
                    eraseMarks(col, row, board, marks);
                }

                if (filledBoard[numRow][numCol] == Character.getNumericValue(c)) {

                    playSound("/Users/soniabecz/Desktop/school./semestr piąty/komunikacja_człowiek-komputer/sudoku_text/src/main/resources/CorrectAnswer.wav");

                    board[numRow][numCol] = Character.getNumericValue(c);

                    StringBuilder sb = new StringBuilder();
                    sb.append(c);


                    screen.refresh();

                    tGraphics.setForegroundColor(new TextColor.RGB(78, 108, 80));

                    tGraphics.putString(new TerminalPosition(col, row), sb.toString() + ' ');

                    correctAnswers++;

                    tGraphics.setBackgroundColor(new TextColor.RGB(3, 0, 28));

                    tGraphics.putString(new TerminalPosition(7, 21), "Poprawne odpowiedzi: " + correctAnswers + "/" + K);
                    screen.refresh();


                }

                else {
                    playSound("/Users/soniabecz/Desktop/school./semestr piąty/komunikacja_człowiek-komputer/sudoku_text/src/main/resources/WrongAnswer.wav");

                    StringBuilder sb = new StringBuilder();
                    sb.append(c);

                    tGraphics.setForegroundColor(new TextColor.RGB(130, 0, 0));

                    tGraphics.putString(new TerminalPosition(col, row), sb.toString() + ' ');

                    incorrectAnswers++;

                    tGraphics.setBackgroundColor(new TextColor.RGB(3, 0, 28));

                    tGraphics.putString(new TerminalPosition(6, 23), "Niepoprawne odpowiedzi: " + incorrectAnswers + "/3");
                    screen.refresh();
                }
            }

            screen.refresh();
        }

        else if (mode == 2) {
            setMarks(c,col,row,filledBoard,board,marks);
        }

    }

    public void playSound (String soundStream) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        KeyStroke key = screen.pollInput();

        File file = new File(soundStream);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);

        clip.start();

        if (key != null) {
            clip.stop();
            clip.close();
            audioStream.close();
        }
    }

    public void setMarks(Character c, int col, int row, int [] filledBoard[], int board[][], int marks[][][]) throws IOException {

        int numRow = (row / 2) - 1;
        int numCol = (col - 3) / 4;

        TextGraphics tGraphics = screen.newTextGraphics();

        tGraphics.setBackgroundColor(new TextColor.RGB(182, 234, 218));

        StringBuilder sb = new StringBuilder();
        sb.append(c);


        if (board[numRow][numCol] == 0) {

            for (int i = 0; i < Math.sqrt(N); i++) {


                if (marks[row][col][0] == 0) {

                    tGraphics.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
                    tGraphics.putString(new TerminalPosition(col-1+i, row), sb.toString(), SGR.ITALIC);
                    int n = Character.getNumericValue(c);
                    marks[row][col][0] = n;
                    break;
                }

                if (marks[row][col][0] != 0 && marks[row][col][1] == 0) {

                    tGraphics.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);

                    int n = Character.getNumericValue(c);
                    marks[row][col][1] = n;

                    for (int j = 0; j < 2; j++){
                        tGraphics.putString(new TerminalPosition(col-1+j, row), marks[row][col][j] + "", SGR.ITALIC);
                    }
                    break;
                }

                if (marks[row][col][1] != 0 && marks[row][col][2] == 0) {

                    tGraphics.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);

                    int n = Character.getNumericValue(c);
                    marks[row][col][2] = n;

                    for (int j = 0; j < 3; j++){
                        tGraphics.putString(new TerminalPosition(col-1+j, row), marks[row][col][j] + "", SGR.ITALIC);
                    }
                    break;
                }

            }
            screen.refresh();
        }
    }

    public void eraseMarks(int col, int row, int board[][], int marks[][][]) throws IOException {

        int numRow = (row / 2) - 1;
        int numCol = (col - 3) / 4;

        TextGraphics tGraphics = screen.newTextGraphics();
        tGraphics.setBackgroundColor(new TextColor.RGB(182, 234, 218));

        if (board[numRow][numCol] == 0) {

            for (int i = 0; i < Math.sqrt(N); i++) {

                if (marks[row][col][0] != 0 && marks[row][col][1] == 0) {

                    marks[row][col][0] = 0;

                    tGraphics.putString(new TerminalPosition(col - 1, row), " ");

                    screen.refresh();

                    break;
                }

                if (marks[row][col][1] != 0 && marks[row][col][2] == 0) {

                    marks[row][col][1] = 0;

                    tGraphics.putString(new TerminalPosition(col, row), " ");

                    screen.refresh();

                    break;
                }

                if (marks[row][col][2] != 0) {

                    marks[row][col][2] = 0;

                    tGraphics.putString(new TerminalPosition(col + 1, row), " ");

                    screen.refresh();

                    break;
                }
            }
        }
    }

    public void getHint(int col, int row, int [] filledBoard[], int hintsUsed, int board[][], int marks[][][], int correctAnswers) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        TextGraphics tGraphics = screen.newTextGraphics();

        tGraphics.setBackgroundColor(new TextColor.RGB(182, 234, 218));

        int numRow = (row / 2) - 1;
        int numCol = (col - 3) / 4;


        if (hintsUsed < 3) {
            if (board[numRow][numCol] == 0) {
                playSound("/Users/soniabecz/Desktop/school./semestr piąty/komunikacja_człowiek-komputer/sudoku_text/src/main/resources/CorrectAnswer.wav");

                for (int i = 0; i < Math.sqrt(N); i++) {
                    eraseMarks(col, row, board,marks);
                }

                board[numRow][numCol] = filledBoard[numRow][numCol];

                String s = "" + board[numRow][numCol] + ' ';

                tGraphics.setForegroundColor(new TextColor.RGB(242, 222, 186));

                tGraphics.putString(new TerminalPosition(col, row), s);

                hintsUsed++;
                correctAnswers++;

                tGraphics.setBackgroundColor(new TextColor.RGB(3, 0, 28));

                tGraphics.putString(new TerminalPosition(40, 22), "Użyte podpowiedzi " + hintsUsed + "/3");
                screen.refresh();

                tGraphics.setForegroundColor(new TextColor.RGB(78, 108, 80));
                tGraphics.putString(new TerminalPosition(7, 21), "Poprawne odpowiedzi: " + correctAnswers + "/" + K);
                screen.refresh();
            }
        }
    }

    public void navigation(int [] filledBoard[], int mode, int correctAnswers, int incorrectAnswers, int hintsUsed, int board[][], int marks[][][]) throws IOException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException {

        int[] valuesInSquares[][] = new int[10][10][10];
        int x = 0;
        int y = 0;
        int i = 0;
            for (int k = 0; k<3; k++) {
                if (k!=0) {
                    x+=3;
                }
                    for (int l = 0; l<3; l++) {
                        for (int m = 0; m < 3; m++) {
                            valuesInSquares[k][m][l] = board[m+x][l];
                        }
                    }
            }

        for (int k = 3; k<6; k++) {
            if (k!=3) {
                y+=3;
            }
            for (int l = 0; l<3; l++) {
                for (int m = 0; m < 3; m++) {
                    valuesInSquares[k][m][l] = board[m+y][l+3];
                }
            }
        }

        for (int k = 6; k<9; k++) {
            if (k!=6) {
                i+=3;
            }
            for (int l = 0; l<3; l++) {
                for (int m = 0; m < 3; m++) {
                    valuesInSquares[k][m][l] = board[m+i][l+6];
                }
            }
        }

        System.out.print("VALUES:\n");
            for (int m = 0; m<3; m++) {
                for (int l = 0; l<3; l++) {
                    System.out.print(valuesInSquares[5][m][l]);

            }
                System.out.print("\n");
        }

        while (true) {
            KeyStroke key = screen.pollInput();
            if(key == null) {
                Thread.sleep(10);
                ms++;
                Timer();

                if(ms == 99) {
                    s++;
                    ms=0;
                }

                else if (s == 60) {
                    min++;
                    s=0;
                }

                else if (min == 60) {
                    h++;
                    min=0;
                }

                continue;
            }

            if(key.getKeyType() == KeyType.Escape || key.getKeyType() == KeyType.EOF) {
                break;
            }

            if(key.getKeyType() == KeyType.ArrowUp) {
                up();
            }

            if(key.getKeyType() == KeyType.ArrowDown) {
                down();
            }

            if(key.getKeyType() == KeyType.ArrowLeft) {
                left();
            }

            if(key.getKeyType() == KeyType.ArrowRight) {
                right();
            }

            if (key.getKeyType() == KeyType.Character && Character.getNumericValue(key.getCharacter()) > 0 && Character.getNumericValue(key.getCharacter()) < 10) {
                int numRow = (row / 2) - 1;
                int numCol = (col - 3) / 4;
                insert(key.getCharacter(),col,row,filledBoard, mode, board, correctAnswers, incorrectAnswers, marks);
                if(mode == 1) {

                    if (Character.getNumericValue(key.getCharacter())==filledBoard[numRow][numCol]) {
                        correctAnswers++;
                    } else {
                        incorrectAnswers++;
                    }
                }
                continue;
            }

            if (key.getKeyType() == KeyType.Character && key.getCharacter() == 'R' ) {
                System.out.println("Jesteś w trybie wprowadzania");
                mode = 1;
            }

            if (key.getKeyType() == KeyType.Character && key.getCharacter() == 'N' ) {
                System.out.println("Jesteś w trybie notatek");
                mode = 2;

            }

            if (key.getKeyType() == KeyType.Character && key.getCharacter() == 'P' ) {
                System.out.println("Jesteś w trybie podpowiedzi");
                getHint(col, row, filledBoard, hintsUsed, board, marks, correctAnswers);
                hintsUsed++;
                correctAnswers++;
            }

            if (key.getKeyType() == KeyType.Backspace) {
                eraseMarks(col,row, board, marks);
            }

            if (key.getKeyType() == KeyType.Character && key.getCharacter() == 'Z' ) {
                playSound("/Users/soniabecz/Desktop/school./semestr piąty/komunikacja_człowiek-komputer/sudoku_text/src/main/resources/Disappointed.wav");

                exitMessageWindow();

                break;
            }

            if(isWon(correctAnswers)) {
                playSound("/Users/soniabecz/Desktop/school./semestr piąty/komunikacja_człowiek-komputer/sudoku_text/src/main/resources/Applause.wav");

                if (difficulty == 1) {
                    FileWriter myWriter = new FileWriter("/Users/soniabecz/Desktop/school./semestr piąty/komunikacja_człowiek-komputer/sudoku_text/scores_easy.txt", true);
                    myWriter.write(nick + " " + h + "." + min + "." + s + "," + ms + "\n");
                    myWriter.close();
                    System.out.println("Successfully wrote to the file.");
                }

                if (difficulty == 2) {
                    FileWriter myWriter = new FileWriter("/Users/soniabecz/Desktop/school./semestr piąty/komunikacja_człowiek-komputer/sudoku_text/scores_medium.txt", true);
                    myWriter.write(nick + " " + h + "." + min + "." + s + "," + ms + "\n");
                    myWriter.close();
                    System.out.println("Successfully wrote to the file.");
                }

                if (difficulty == 3) {
                    FileWriter myWriter = new FileWriter("/Users/soniabecz/Desktop/school./semestr piąty/komunikacja_człowiek-komputer/sudoku_text/scores_hard.txt", true);
                    myWriter.write(nick + " " + h + "." + min + "." + s + "," + ms + "\n");
                    myWriter.close();
                    System.out.println("Successfully wrote to the file.");
                }

                winnerMessageWindow();

                break;
            }

            if (incorrectAnswers >=3 ) {

                playSound("/Users/soniabecz/Desktop/school./semestr piąty/komunikacja_człowiek-komputer/sudoku_text/src/main/resources/Fail.wav");

                loserMessageWindow();

                break;
            }
        }

    }
}


