package com.oresfall.wallwars.gameclass;

import com.oresfall.wallwars.db.Database;
import com.oresfall.wallwars.playerclass.Player;
import com.oresfall.wallwars.utls.Utils;
import net.minecraft.network.message.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

/**
 * Class to create instances of games
 */
public class Game {
    /**
     * Name of game
     */
    private String name;
    /**
     * World of game
     */
    private ServerWorld world;
    /**
     * Server of game
     */
    private MinecraftServer server;
    /**
     * List of players that joined game
     */
    private ArrayList<Player> players = new ArrayList<>();
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

    public class StartSpawn {
        private static Vec3d place = new Vec3d(0,60,0);
        private static ServerWorld world;
        static void setWorld(ServerWorld world) {
            StartSpawn.world = world;
        }
        static void setPlace(Vec3d place) {
            StartSpawn.place = place;
        }

    }

    /**
     * @param name Name of game
     * @param world World of game
     */
    public Game(String name, MinecraftServer server) {
        this.name = name;
        this.server = server;
        createTeams();
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
        return players;
    }

    /**
     * @return List of players by their name
     */
    public ArrayList<String> getPlayersByName() {
        ArrayList<String> playersByName = new ArrayList<String>();
        for(Player player : players) {
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
        if(Utils.getWorldByName(server,worldName) == world) return false;
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
        if(players.size() == maxPlayers) return false;
        players.add(player);
        return true;
    }
    public boolean joinPlayer(ServerPlayerEntity target) {
        if(players.size() == maxPlayers) return false;
        players.add(Database.getPlayer(target));
        return true;
    }

    /**
     * Removes player from list of players and teleports to lobby
     * @param player Player to leave
     * @return 0 if good, -1 if player doesn't exist
     */
    public boolean leavePlayer(Player player) {
        if(!players.contains(player)) return false;
        players.remove(player);
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
        players.forEach(this::leavePlayer);
        return true;
    }


    private int time;
    //minutes - seconds - ticks
    private final int startTime = 5 * Utils.MIN;
    private int phase = -1;
    public void onTick(MinecraftServer server) {
        if(phase == -1) {
            if(players == null) return;
            if(players.size() < minPlayers) {
                time = 0;
                return;
            }
            switch (time) {
                case 0 ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("5 minutes"), MessageType.SYSTEM);
                case Utils.MIN ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("4 minutes"), MessageType.SYSTEM);
                case 2 * Utils.MIN ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("3 minutes"), MessageType.SYSTEM);
                case 3 * Utils.MIN ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("2 minutes"), MessageType.SYSTEM);
                case 4 * Utils.MIN ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("1 minute"), MessageType.SYSTEM);
                case 4 * Utils.MIN + 30 * Utils.SEC ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("30"), MessageType.SYSTEM);
                case 4 * Utils.MIN + 45 * Utils.SEC ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("15"), MessageType.SYSTEM);
                case 4 * Utils.MIN + 50 * Utils.SEC ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("10"), MessageType.SYSTEM);
                case 4 * Utils.MIN + 55 * Utils.SEC ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("5"), MessageType.SYSTEM);
                case 4 * Utils.MIN + 56 * Utils.SEC ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("4"), MessageType.SYSTEM);
                case 4 * Utils.MIN + 57 * Utils.SEC ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("3"), MessageType.SYSTEM);
                case 4 * Utils.MIN + 58 * Utils.SEC ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("2"), MessageType.SYSTEM);
                case 4 * Utils.MIN + 59 * Utils.SEC ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("1"), MessageType.SYSTEM);
                case startTime -> {
                    server.getPlayerManager().broadcast(Utils.defaultMsg("Start!"), MessageType.SYSTEM);
                    phase = 0;
                }
            }
        } else if (phase == 0) {
            int i = 1;
            for(Player player : players) {
                Database.removePlayerToDefaultTeam(player);
                if(i % 5 == 0) i = 1;
                teams[i-1].addPlayer(player);
                i++;
            }
            phase = 1;
        } else if(phase == 1) {
            for(TeamBase team : teams) {
                team.teleportPlayers();
            }
            phase = 2;
        } else if(phase == 2) {
            switch (time) {
                case 0 ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("Walls will go down in 5 minutes!"), MessageType.SYSTEM);
                case Utils.MIN ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("4 minutes"), MessageType.SYSTEM);
                case 2 * Utils.MIN ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("3 minutes"), MessageType.SYSTEM);
                case 3 * Utils.MIN ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("2 minutes"), MessageType.SYSTEM);
                case 4 * Utils.MIN ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("1 minute"), MessageType.SYSTEM);
                case 4 * Utils.MIN + 30 * Utils.SEC ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("30"), MessageType.SYSTEM);
                case 4 * Utils.MIN + 45 * Utils.SEC ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("15"), MessageType.SYSTEM);
                case 4 * Utils.MIN + 50 * Utils.SEC ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("10"), MessageType.SYSTEM);
                case 4 * Utils.MIN + 55 * Utils.SEC ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("5"), MessageType.SYSTEM);
                case 4 * Utils.MIN + 56 * Utils.SEC ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("4"), MessageType.SYSTEM);
                case 4 * Utils.MIN + 57 * Utils.SEC ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("3"), MessageType.SYSTEM);
                case 4 * Utils.MIN + 58 * Utils.SEC ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("2"), MessageType.SYSTEM);
                case 4 * Utils.MIN + 59 * Utils.SEC ->
                        server.getPlayerManager().broadcast(Utils.defaultMsg("1"), MessageType.SYSTEM);
                case startTime -> {
                    server.getPlayerManager().broadcast(Utils.defaultMsg("Walls go down!"), MessageType.SYSTEM);

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
