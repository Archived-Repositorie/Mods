package com.oresfall.db;

import com.oresfall.Game;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;

class SaveGame extends SaveGameBase {
    SaveGame(ArrayList<Game> games) {
        this.games = new String[games.size()][2];
        for(Game game : games) {
            if(game == null) continue;
            this.games[games.indexOf(game)][0] = game.toString();
            ServerWorld world = game.getWorld();
            this.games[games.indexOf(game)][1] = world.getRegistryKey().getValue().toString();
        }
    }
}
