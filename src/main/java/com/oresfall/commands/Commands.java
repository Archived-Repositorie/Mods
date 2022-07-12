package com.oresfall.commands;

import com.oresfall.commands.game.Game;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class Commands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(Game::register);
    }
}
