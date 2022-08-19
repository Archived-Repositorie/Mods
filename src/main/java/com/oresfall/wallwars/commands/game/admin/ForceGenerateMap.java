package com.oresfall.wallwars.commands.game.admin;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oresfall.wallwars.commands.argumenttype.GameArgumentType;
import com.oresfall.wallwars.gameclass.Game;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static com.oresfall.wallwars.utls.Utils.defaultMsg;

public class ForceGenerateMap {
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Game game = GameArgumentType.getGame( context,"game");
        ServerPlayerEntity target = context.getSource().getPlayer();

        game.generateMap();

        target.sendMessage(defaultMsg(String.format("Generated map of %s", game)));
        return 0;
    }
}
