import com.googlecode.lanterna.gui2.TextBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Player {
    String nick;
    String time;

    public Player(String nick, String time) {
        this.nick = nick;
        this.time = time;
    }

    public void sort(String filePath, TextBox textBox) throws IOException
    {
        int i=1;

        BufferedReader reader = new BufferedReader(new FileReader(filePath));


        ArrayList<Player> playersRecords = new ArrayList<Player>();


        String currentLine = reader.readLine();

        while (currentLine != null)
        {
            String[] playerDetail = currentLine.split(" ");

            String name = playerDetail[0];

            String time = String.valueOf(playerDetail[1]);

            playersRecords.add(new Player(name, time));

            currentLine = reader.readLine();
        }

        Collections.sort(playersRecords, new timeCompare());

        for (Player player : playersRecords)
        {
            textBox.addLine(i + ". " + player.nick + "  " + player.time + "\n");
            i++;
        }

        reader.close();
    }
}

class timeCompare implements Comparator<Player> {
    @Override
    public int compare(Player p1, Player p2) {
        return p1.time.compareTo(p2.time);
    }
}
