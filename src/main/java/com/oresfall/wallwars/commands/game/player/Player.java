package com.oresfall.wallwars.commands.game.player;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class Player {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("player")
                .then(Join.register())
                .then(Leave.register())
                .then(Random.register());
    }
}
