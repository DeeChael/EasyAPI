package org.ezapi;

import org.bukkit.entity.Player;
import org.ezapi.chat.ChatMessage;
import org.ezapi.module.scoreboard.EzScoreboard;

public class Examples {

    private static void scoreboardExample(Player player) {
        //Create a new scoreboard
        EzScoreboard scoreboard = new EzScoreboard(new ChatMessage("&c&lExample", false));
        //Add text, the int is witch line, line number higher, position higher
        scoreboard.setText(8, new ChatMessage("&b&l| &bThanks for playing!", false));
        scoreboard.setText(7, new ChatMessage("&b&l|    ", false));
        scoreboard.setText(6, new ChatMessage("&b&l| &bPlayer", false));
        scoreboard.setText(5, new ChatMessage("&b&l| &e{display_name}", false));
        scoreboard.setText(4, new ChatMessage("&b&l|   ", false));
        scoreboard.setText(3, new ChatMessage("&b&l|  ", false));
        scoreboard.setText(2, new ChatMessage("&b&l| ", false));
        scoreboard.setText(1, new ChatMessage("&b&l|&e example.com", false));
        //Make a player can see the scoreboard
        scoreboard.addViewer(player);
        //Remove line witch is 1
        scoreboard.removeText(1);
        //Make a player cannot see the scoreboard
        scoreboard.removeViewer(player);
        //Set title display name
        scoreboard.setTitle(new ChatMessage("New Display", false));
        //Drop the scoreboard
        scoreboard.drop();
    }

}
