package com.oresfall.wallwars.db;

import com.oresfall.wallwars.Game;
import com.oresfall.wallwars.SaveGameBase;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;

class SaveGame extends SaveGameBase {
    public SaveGame(ArrayList<Game> games) {
        super();
        this.games = new String[games.size()][2];
        for(Game game : games) {
            if(game == null) continue;
            this.games[games.indexOf(game)][0] = game.toString();
            ServerWorld world = game.getWorld();
            this.games[games.indexOf(game)][1] = world.getRegistryKey().getValue().toString();
        }
    }
}
