package com.oresfall.wallwars.commands.game.player;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oresfall.wallwars.Game;
import com.oresfall.wallwars.IEntityDataSaver;
import com.oresfall.wallwars.commands.argumenttype.GameArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import static com.oresfall.wallwars.utls.Utils.defaultMsg;
import static com.oresfall.wallwars.utls.Utils.errorMsg;

/**
 * Command for joining game
 * Usage: `/game player join {game}`
 */
public class Join {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("join")
                .then(
                        CommandManager.argument("game", GameArgumentType.game())
                                .executes(Join::run)
                );
    }
    public static int run(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = context.getSource().getPlayer();
        IEntityDataSaver targetData = (IEntityDataSaver)target;
        Game game = GameArgumentType.getGame( context,"game");

        if(targetData.getPersistentData().getBoolean("JoinedGame")) {
            target.sendMessage(errorMsg("You are already in game!"));
            return -1;
        }

        if(!game.joinPlayer(target)) {
            target.sendMessage(errorMsg("There is too many people in game"));
            return -1;
        }

        target.sendMessage(defaultMsg("Joined the game!"));
        targetData.getPersistentData().putBoolean("JoinedGame",true);
        return 0;
    }
}
