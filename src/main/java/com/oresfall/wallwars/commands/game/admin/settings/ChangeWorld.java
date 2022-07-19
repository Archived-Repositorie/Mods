package com.oresfall.wallwars.commands.game.admin.settings;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.oresfall.wallwars.gameclass.Game;
import com.oresfall.wallwars.commands.argumenttype.GameArgumentType;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import static com.oresfall.wallwars.utls.Utils.defaultMsg;


/**
 * COmmand for changing world of game
 * Usage: `/game admin settings changeworld {game} {world}`
 */
public class ChangeWorld {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("changeworld")
                .then(
                        CommandManager.argument("game", GameArgumentType.game())
                                .then(
                                        CommandManager.argument("world", DimensionArgumentType.dimension())
                                                .executes(ChangeWorld::run)
                                )
                );
    }
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Game game = GameArgumentType.getGame(context,"game");
        ServerWorld world = DimensionArgumentType.getDimensionArgument(context,"world");
        ServerPlayerEntity target = context.getSource().getPlayer();

        if(!game.setWorld(world)) {
            throw new SimpleCommandExceptionType(Text.literal("Values are already set.")).create();
        }

        target.sendMessage(defaultMsg(String.format("Changed world %s", world.getRegistryKey().getValue().toString())));
        return 0;
    }
}
