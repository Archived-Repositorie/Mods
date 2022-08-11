package com.oresfall.wallwars.gameclass;

import com.oresfall.wallwars.playerclass.Player;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Random;

public class TeamBase {
    private ArrayList<Player> players = new ArrayList<>();
    private int max = 5;
    private String name = "default";
    private Formatting color = Formatting.WHITE;
    private Vec3d spawnCoords = new Vec3d(0,60,0);
    private Team team;
    private Formatting[] colors = new Formatting[] {
            Formatting.WHITE,
            Formatting.RED,
            Formatting.GREEN,
            Formatting.AQUA,
            Formatting.BLACK,
            Formatting.BLUE,
            Formatting.DARK_AQUA,
            Formatting.DARK_BLUE,
            Formatting.DARK_GRAY,
            Formatting.DARK_GREEN,
            Formatting.DARK_PURPLE,
            Formatting.DARK_RED,
            Formatting.GOLD,
            Formatting.GRAY,
            Formatting.LIGHT_PURPLE,
            Formatting.YELLOW,
    };

    public TeamBase(MinecraftServer server, String name) {
        this.team = new Team(server.getScoreboard(), name);
        this.name = name;
    }

    public void setColor(Formatting color) {
        team.setColor(color);
    }

    public void setRandomColor() {
        Random generator = new Random();
        int i = generator.nextInt(colors.length);
        team.setColor(colors[i]);
    }

    public void setSpawnCoords(double x, double y, double z) {
        spawnCoords = new Vec3d(x,y,z);
    }

    public void enablePvp(boolean enable) {
        team.setFriendlyFireAllowed(enable);
    }

    public void setPrefix(Text text) {
        team.setPrefix(text);
    }

    public void addPlayer(Player player) {
        this.players.add(player);
        if(player.getTeam() != null) player.getTeam().removePlayer(player);
        this.team.getScoreboard().addPlayerToTeam(player.getName(),this.team);
        player.setTeam(this);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
        player.setTeam(null);
        this.team.getScoreboard().removePlayerFromTeam(player.getName(), this.team);
    }

    public void teleportPlayers() {
        //TODO: Teleport players to place
    }

    public void sendMessage(Text message) {
        players.forEach(player -> {
            player.getPlayerEntity().sendMessage(message);
        });
    }

    public void sendMessage(SignedMessage message, MessageType.Parameters params) {
        SentMessage sentMessage = SentMessage.of(message);
        players.forEach(player -> {
            player.getPlayerEntity().sendChatMessage(sentMessage, false, params);
        });
    }
}
