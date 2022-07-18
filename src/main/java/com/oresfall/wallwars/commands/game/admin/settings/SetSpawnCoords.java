package com.oresfall.wallwars.commands.game.admin.settings;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.oresfall.wallwars.Game;
import com.oresfall.wallwars.commands.argumenttype.GameArgumentType;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static com.oresfall.wallwars.utls.Utils.defaultMsg;

/**
 * Command for setting spawn (before game) for game
 * Usage: `/game admin settings setspawncoords {game} {x} {y} {z}`
 */
public class SetSpawnCoords {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("setspawncoords")
                .then(
                        CommandManager.argument("game", GameArgumentType.game())
                                .then(
                                        CommandManager.argument("coords", BlockPosArgumentType.blockPos())
                                                .executes(SetSpawnCoords::run)
                                )
                );
    }
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Game game = GameArgumentType.getGame(context,"game");
        BlockPos blockPos = BlockPosArgumentType.getBlockPos(context, "coords");
        ServerPlayerEntity target = context.getSource().getPlayer();

        if(!game.setSpawnCoords(blockPos.getX(), blockPos.getY(), blockPos.getZ())) {
            throw new SimpleCommandExceptionType(Text.literal("Values are already set.")).create();
        }

        target.sendMessage(defaultMsg(String.format("Coords have been changed to X:%d Y:%d Z:%d", blockPos.getX(), blockPos.getY(), blockPos.getZ())));
        return 0;
    }
}
