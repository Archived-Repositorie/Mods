package com.oresfall.wallwars.gameclass;

import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class TeamBase {
    private ArrayList<ServerPlayerEntity> players = new ArrayList<>();
    private int max = 5;
    private String name = "default";
    private Formatting color = Formatting.WHITE;
    private Vec3d spawnCoords = new Vec3d(0,60,0);
    private Team team;

    public TeamBase(MinecraftServer server, String name) {
        this.team = new Team(server.getScoreboard(), name);
        this.name = name;
    }

    public void setColor(Formatting color) {
        team.setColor(color);
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
}
