package com.oresfall.commands.game;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oresfall.Info;
import net.minecraft.server.command.ServerCommandSource;

public class TestGame {
    static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String game = StringArgumentType.getString(context,"game");
        //if(Info.games.
        return 0;
    }
}
