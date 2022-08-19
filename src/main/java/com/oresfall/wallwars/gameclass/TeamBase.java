package com.oresfall.wallwars.gameclass;

import com.oresfall.wallwars.Main;
import com.oresfall.wallwars.playerclass.Player;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Random;

public class TeamBase {
    private final ArrayList<Player> players = new ArrayList<>();
    private int max = 5;
    private String name = "default";
    private Formatting color = Formatting.WHITE;
    private Vec3d spawnCoords = new Vec3d(0,60,0);
    private Team team;

    private MinecraftServer server;
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
    private Game game;
    private Text prefix;

    public Text getPrefix() {
        return prefix;
    }

    public TeamBase(MinecraftServer server, String name) {
        this.team = new Team(server.getScoreboard(), name);
        this.server = server;
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
        this.prefix = text;
        team.setPrefix(text);
    }

    public void addPlayer(Player player) {
        this.players.add(player);
        if(player.getTeam() != null) player.getTeam().removePlayer(player);
        this.team.getScoreboard().addPlayerToTeam(player.getName(),this.team);
        player.setTeam(this);
    }

    public void removePlayer(Player player) {
        players.remove(player);
        player.setTeam(null);
        this.team.getScoreboard().removePlayerFromTeam(player.getName(), this.team);
    }

    public void teleportPlayers() {
        for(Player player : players) {
            Main.LOGGER.info("teleport");
            if(player == null) continue;
            player.getPlayerEntity(server).teleport(game.getWorld(), spawnCoords.x, spawnCoords.y,spawnCoords.z, player.getPlayerEntity(server).getYaw(), player.getPlayerEntity(server).getPitch());
        }
    }

    public void sendMessage(Text message) {
        players.forEach(player -> {
            player.getPlayerEntity(server).sendMessage(message);
        });
    }

    public void sendMessage(SignedMessage message, MessageType.Parameters params) {
        SentMessage sentMessage = SentMessage.of(message);
        players.forEach(player -> {
            player.getPlayerEntity(server).sendChatMessage(sentMessage, false, params);
        });
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setSpawnPlace(BlockPos blockPos) {
        spawnCoords = new Vec3d(blockPos.getX(),blockPos.getY(),blockPos.getZ());
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public double[] getTpPlace() {
        return new double[]{
                spawnCoords.x,
                spawnCoords.y,
                spawnCoords.z
        };
    }
}
