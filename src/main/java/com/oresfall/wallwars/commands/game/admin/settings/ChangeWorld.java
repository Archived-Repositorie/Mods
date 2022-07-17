package com.oresfall.wallwars.commands.game.admin.settings;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oresfall.wallwars.Game;
import com.oresfall.wallwars.commands.argumenttype.GameArgumentType;
import com.oresfall.wallwars.db.Database;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;


/**
 * COmmand for changing world of game
 * Usage: `/game admin settings changeworld {game} {world}`
 */
public class ChangeWorld {
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Game game = GameArgumentType.getGame(context,"game");
        ServerWorld world = DimensionArgumentType.getDimensionArgument(context,"world");
        ServerPlayerEntity target = context.getSource().getPlayer();

        if(!Database.ifGameExist(game)) {
            target.sendMessage(Text.empty().append("Game doesn't exist!").formatted(Formatting.RED));
            return -1;
        }
        if(game.setWorld(world) == -1) {
            target.sendMessage(Text.empty().append("This world is already set").formatted(Formatting.RED));
            return game.setWorld(world);
        }
        target.sendMessage(Text.of(String.format("Changed world %s", world.getRegistryKey().getValue().toString())));
        return 0;
    }
}
