package com.oresfall.wallwars.commands.game.admin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oresfall.wallwars.Game;
import com.oresfall.wallwars.db.Database;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Command for creating new game
 * Usage: `/game admin creategame {game} {world}`
 */
public class CreateGame {
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String gameName = StringArgumentType.getString(context,"name");
        ServerWorld world = DimensionArgumentType.getDimensionArgument(context,"world");
        ServerPlayerEntity target = context.getSource().getPlayer();

        if(Database.ifGameExist(Database.getGameByName(gameName))) {
            target.sendMessage(Text.empty().append("Game already exist!").formatted(Formatting.RED));
            return -1;
        }
        Game game = new Game(gameName,world);
        Database.addGame(game);
        target.sendMessage(Text.of(String.format("Created new game %s", game)));
        return 0;
    }
}
