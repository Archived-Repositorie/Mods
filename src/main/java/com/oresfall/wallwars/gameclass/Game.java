package com.oresfall.wallwars.gameclass;

import com.oresfall.wallwars.db.Database;
import com.oresfall.wallwars.playerclass.Player;
import com.oresfall.wallwars.utls.Utils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Class to create instances of games
 */
public class Game {
    /**
     * Name of game
     */
    private final String name;
    /**
     * World of game
     */
    private ServerWorld world;
    /**
     * Server of game
     */
    private final MinecraftServer server;
    /**
     * Max number of players that can join game
     */
    private int maxPlayers = 4;
    private int minPlayers = 1;
    /**
     * Max size of plot (X,Z)
     */
    private int plotXbyZ = 60 * 6;

    private TeamBase[] teams = new TeamBase[4];
    private final GroupBase playerGroup;

    public static class StartSpawn {
        private static Vec3d place = new Vec3d(0,60,0);
        private static ServerWorld world;
        static void setWorld(ServerWorld world) {
            StartSpawn.world = world;
        }
        static void setPlace(Vec3d place) {
            StartSpawn.place = place;
        }

    }

    public Game(String name, MinecraftServer server) {
        this.name = name;
        this.server = server;
        createTeams();
        this.playerGroup = new GroupBase(this);
    }

    public Vec3d getSPlace() {
        return StartSpawn.place;
    }

    public ServerWorld getSWorld() {
        return StartSpawn.world;
    }


    /**
     * @return Value of world
     */
    public ServerWorld getWorld() {
        return world;
    }

    /**
     * @return Value of players
     */
    public ArrayList<Player> getPlayers() {
        return this.playerGroup.getPlayers();
    }

    /**
     * @return List of players by their name
     */
    public ArrayList<String> getPlayersByName() {
        ArrayList<String> playersByName = new ArrayList<String>();
        for(Player player : this.playerGroup.getPlayers()) {
            if(player == null) continue;
            playersByName.add(player.getName());
        }
        return playersByName;
    }

    /**
     * Sets world of game
     * @param world World to set for game
     * @return 0 if good, -1 if it's already set
     */
    public boolean setWorld(ServerWorld world) {
        if(getWorld() == world) return false;
        this.world = world;
        return true;
    }

    public boolean setWorld(String worldName) {
        if(Objects.equals(worldName, world.getRegistryKey().getValue().toString())) return false;
        this.world = Utils.getWorldByName(server,worldName);
        return true;
    }

    public boolean setSpawnStart(ServerWorld world, double x, double y, double z) {
        var place = new Vec3d(x,y,z);
        if(place.equals(StartSpawn.place)) return false;
        if(world.equals(StartSpawn.world)) return false;
        StartSpawn.setWorld(world);
        StartSpawn.setPlace(place);
        return true;
    }

    /**
     * Adds player to list of players and teleports them to spawnPlace
     * @param player Player to join
     * @return 0 if good, -1 if there is too many players
     */
    public boolean joinPlayer(Player player) {
        if(this.playerGroup.getPlayers().size() == maxPlayers) return false;
        this.playerGroup.addPlayer(player);
        return true;
    }

    /**
     * Removes player from list of players and teleports to lobby
     * @param player Player to leave
     * @return 0 if good, -1 if player doesn't exist
     */
    public boolean leavePlayer(Player player) {
        if(!this.playerGroup.getPlayers().contains(player)) return false;
        playerGroup.removePlayer(player);
        Database.tpPlayerToLobby(player, player.getPlayerEntity().getServer());
        Database.addPlayerToDefaultTeam(player);
        return true;
    }

    /**
     * Removes game from Database and runs leavePlayer for each player
     * @return 0 if good, -1 if game doesn't exist
     */
    public boolean removeGame() {
        if(!Database.ifGameExist(this)) return false;
        Database.removeGame(this);
        playerGroup.removePlayers();
        return true;
    }


    private int time;
    //minutes - seconds - ticks
    private final int startTime = 5 * Utils.MIN;
    private int phase = -1;
    private boolean win = false;
    
    private void sendToGroup(Text message) {
        playerGroup.sendMessage(message);
    }
    public void onTick(MinecraftServer server) {
        if(win) {
            phase = Integer.MIN_VALUE;
        }
        if(this.playerGroup.getPlayers().size() < minPlayers) {
            time = 0;
            return;
        }else if(phase == -1) {
            switch (time) {
                case 0 ->
                        sendToGroup(Utils.defaultMsg("5 minutes"));
                case Utils.MIN ->
                        sendToGroup(Utils.defaultMsg("4 minutes"));
                case 2 * Utils.MIN ->
                        sendToGroup(Utils.defaultMsg("3 minutes"));
                case 3 * Utils.MIN ->
                        sendToGroup(Utils.defaultMsg("2 minutes"));
                case 4 * Utils.MIN ->
                        sendToGroup(Utils.defaultMsg("1 minute"));
                case 4 * Utils.MIN + 30 * Utils.SEC ->
                        sendToGroup(Utils.defaultMsg("30"));
                case 4 * Utils.MIN + 45 * Utils.SEC ->
                        sendToGroup(Utils.defaultMsg("15"));
                case 4 * Utils.MIN + 50 * Utils.SEC ->
                        sendToGroup(Utils.defaultMsg("10"));
                case 4 * Utils.MIN + 55 * Utils.SEC ->
                        sendToGroup(Utils.defaultMsg("5"));
                case 4 * Utils.MIN + 56 * Utils.SEC ->
                        sendToGroup(Utils.defaultMsg("4"));
                case 4 * Utils.MIN + 57 * Utils.SEC ->
                        sendToGroup(Utils.defaultMsg("3"));
                case 4 * Utils.MIN + 58 * Utils.SEC ->
                        sendToGroup(Utils.defaultMsg("2"));
                case 4 * Utils.MIN + 59 * Utils.SEC ->
                        sendToGroup(Utils.defaultMsg("1"));
                case startTime -> {
                    sendToGroup(Utils.defaultMsg("Start!"));
                    phase = 0;
                }
            }
        } else if (phase == 0) {
            int i = 1;
            for(Player player : playerGroup.getPlayers()) {
                Database.removePlayerToDefaultTeam(player);
                if(i % 5 == 0) i = 1;
                teams[i-1].addPlayer(player);
                i++;
            }
            phase = 1;
        } else if(phase == 1) {
            sendToGroup(Utils.defaultMsg("Teleporting to teams"));
            for(TeamBase team : teams) {
                team.teleportPlayers();
            }
            phase = 2;
        } else if(phase == 2) {
            switch (time) {
                case 0 ->
                        sendToGroup(Utils.defaultMsg("Walls will go down in 5 minutes!"));
                case Utils.MIN ->
                        sendToGroup(Utils.defaultMsg("4 minutes"));
                case 2 * Utils.MIN ->
                        sendToGroup(Utils.defaultMsg("3 minutes"));
                case 3 * Utils.MIN ->
                        sendToGroup(Utils.defaultMsg("2 minutes"));
                case 4 * Utils.MIN ->
                        sendToGroup(Utils.defaultMsg("1 minute"));
                case 4 * Utils.MIN + 30 * Utils.SEC ->
                        sendToGroup(Utils.defaultMsg("30"));
                case 4 * Utils.MIN + 45 * Utils.SEC ->
                        sendToGroup(Utils.defaultMsg("15"));
                case 4 * Utils.MIN + 50 * Utils.SEC ->
                        sendToGroup(Utils.defaultMsg("10"));
                case 4 * Utils.MIN + 55 * Utils.SEC ->
                        sendToGroup(Utils.defaultMsg("5"));
                case 4 * Utils.MIN + 56 * Utils.SEC ->
                        sendToGroup(Utils.defaultMsg("4"));
                case 4 * Utils.MIN + 57 * Utils.SEC ->
                        sendToGroup(Utils.defaultMsg("3"));
                case 4 * Utils.MIN + 58 * Utils.SEC ->
                        sendToGroup(Utils.defaultMsg("2"));
                case 4 * Utils.MIN + 59 * Utils.SEC ->
                        sendToGroup(Utils.defaultMsg("1"));
                case startTime -> {
                    sendToGroup(Utils.defaultMsg("Walls go down!"));

                    phase = 3;
                }
            }
        }
        time++;
    }
    private void createTeams() {
        TeamBase limeTeam = new TeamBase(server,"lime_".concat(name));
        TeamBase pinkTeam = new TeamBase(server,"pink_".concat(name));
        TeamBase cyanTeam = new TeamBase(server,"cyan_".concat(name));
        TeamBase grayTeam = new TeamBase(server,"gray_".concat(name));
        limeTeam.setPrefix(Text.literal("LIME ").formatted(Formatting.BOLD));
        pinkTeam.setPrefix(Text.literal("PINK ").formatted(Formatting.BOLD));
        cyanTeam.setPrefix(Text.literal("CYAN ").formatted(Formatting.BOLD));
        grayTeam.setPrefix(Text.literal("GRAY ").formatted(Formatting.BOLD));
        limeTeam.setColor(Formatting.GREEN);
        pinkTeam.setColor(Formatting.LIGHT_PURPLE);
        cyanTeam.setColor(Formatting.AQUA);
        grayTeam.setColor(Formatting.GRAY);
        limeTeam.enablePvp(false);
        pinkTeam.enablePvp(false);
        cyanTeam.enablePvp(false);
        grayTeam.enablePvp(false);
        teams[0] = limeTeam;
        teams[1] = pinkTeam;
        teams[2] = cyanTeam;
        teams[3] = grayTeam;
    }

    public void wallsDown() {
    //TODO
    }

    /**
     * @return Name of game
     */
    @Override
    public String toString() {
        return name;
    }
}
