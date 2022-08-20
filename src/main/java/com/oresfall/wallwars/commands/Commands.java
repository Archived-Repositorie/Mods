package com.oresfall.wallwars.commands;

import com.oresfall.wallwars.commands.game.Game;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

/**
 * Commands stuff
 */
public class Commands {
    /**
     * Register commands and commands like stuff
     */
    public static void register() {
        CommandRegistrationCallback.EVENT.register(Game::register);
    }
}
