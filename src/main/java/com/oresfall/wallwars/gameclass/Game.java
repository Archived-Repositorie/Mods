package com.oresfall.wallwars.gameclass;

import com.oresfall.wallwars.Main;
import com.oresfall.wallwars.db.Database;
import com.oresfall.wallwars.playerclass.Player;
import com.oresfall.wallwars.utls.Utils;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

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

    private ArrayList<TeamBase> teams = new ArrayList<>();
    private final GroupBase playerGroup;

    private boolean gameStarted = false;

    private Clipboard map;
    private String file;

    public boolean getGameStarted() {
        return gameStarted;
    }

    public @Nullable TeamBase getTeam(String teamString) {
        for(TeamBase team : teams) {
            if(Objects.equals(team.toString(), teamString)) {
                return team;
            }
        }
        return null;
    }

    public boolean setMap(Clipboard clipboard, String file) {
        if(map == clipboard) return false;
        this.file = file;
        map = clipboard;
        return true;
    }
    
    

    public boolean generateMap() {
        if(getSWorld() == null) return false;
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(FabricAdapter.adapt(world))) {
            Operation operation = new ClipboardHolder(map)
                    .createPaste(editSession)
                    .to(BlockVector3.at(getSPlace().x,getSPlace().y,getSPlace().z))
                    .copyEntities(true)
                    .build();
            Operations.complete(operation);
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public MinecraftServer getServer() {
        return server;
    }

    public String getMapFile() {
        return file;
    }

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
        if(StartSpawn.world == null) StartSpawn.world = server.getOverworld();
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
        if(world != null && Objects.equals(worldName, world.getRegistryKey().getValue().toString())) return false;
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
        Main.LOGGER.info(String.valueOf(getGameStarted()));
        if(this.playerGroup.getPlayers().size() == maxPlayers || getGameStarted()) return false;
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
        Database.tpPlayerToLobby(player, server);
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
    
    public void sendToGroup(Text message) {
        playerGroup.sendMessage(message);
    }

    public void sendToGroup(SignedMessage message, MessageType.Parameters params) {
        playerGroup.sendMessage(message,params);
    }
    private final Stages s = new Stages();
    public void onTick(MinecraftServer server) {
        if(phase >= 2) {
            s.win();
        }
        switch(phase) {
            case -3 -> s.generateMap();
            case -2 -> s.waitingForPlayers();
            case -1 -> s.startingGame();
            case 0 -> s.selectingTeams();
            case 1 -> s.teleporting();
            case 2 -> s.wallsDown();
            case 3 -> s.glow();
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
        limeTeam.setGame(this);
        pinkTeam.setGame(this);
        cyanTeam.setGame(this);
        grayTeam.setGame(this);
        teams.add(limeTeam);
        teams.add(pinkTeam);
        teams.add(cyanTeam);
        teams.add(grayTeam);
    }

    private class Stages {
        public void win() {
            ArrayList<TeamBase> teamsAlive = new ArrayList<>(teams
            );
            teams.forEach(team -> {
                if (team.getPlayers().size() <= 0) {
                    teamsAlive.remove(team);
                }
            });
            if (teamsAlive.size() <= 1) {
                phase = Integer.MIN_VALUE;
                TeamBase team = teamsAlive.get(0);
                teamsAlive.get(0).sendMessage(Text.literal("You win!").formatted(Formatting.BOLD, Formatting.GOLD));
                sendToGroup(Text.literal("Team " + team.toString() + " won the game!").formatted(Formatting.DARK_PURPLE, Formatting.BOLD));
                playerGroup.removePlayers();
            }
            gameStarted = false;
            phase = -3;
        }

        public void waitingForPlayers() {
            if(playerGroup.getPlayers().size() < minPlayers) {
                time = 0;
                phase = -2;
            } else {
                phase = -1;
            }
        }

        public void startingGame() {
            waitingForPlayers();
            if(getPlayers().size() >= 1 && time < 4 * Utils.MIN + 50 * Utils.SEC) {
                time = 4 * Utils.MIN + 50 * Utils.SEC;
                sendToGroup(Utils.defaultMsg("Game is full! Starting faster"));
            }
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
                    playerGroup.setPhaseToPlayers(phase++);
                    sendToGroup(Utils.defaultMsg("Start!"));
                    phase = 0;
                }
            }
        }

        public void selectingTeams() {
            int i = 1;
            for(Player player : playerGroup.getPlayers()) {
                Database.removePlayerToDefaultTeam(player);
                if(i % 5 == 0) i = 1;
                teams.get(i-1).addPlayer(player);
                i++;
            }
            gameStarted = true;
            playerGroup.setPhaseToPlayers(phase++);
            phase = 1;
        }

        public void teleporting() {
            sendToGroup(Utils.defaultMsg("Teleporting to teams"));
            for(TeamBase team : teams) {
                team.teleportPlayers();
            }
            time = 0;
            playerGroup.setPhaseToPlayers(phase++);
            phase = 2;
        }

        public void wallsDown() {
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
                    playerGroup.setPhaseToPlayers(phase++);
                    phase = 3;
                    time = 0;
                }
            }
        }

        public void glow() {
            if(time == 5*Utils.MIN) {
                sendToGroup(Utils.defaultMsg("Everyone got glowing effect!"));
                for(Player player : getPlayers()) {
                    player.getPlayerEntity(server).setGlowing(true);
                }
                playerGroup.setPhaseToPlayers(phase++);
                phase = 4;
                time = 0;
            }
        }

        public void generateMap() {
            Game.this.generateMap();
            phase = -2;
        }
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
