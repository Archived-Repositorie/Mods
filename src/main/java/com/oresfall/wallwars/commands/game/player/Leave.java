package com.oresfall.wallwars.commands.game.player;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oresfall.wallwars.Game;
import com.oresfall.wallwars.IEntityDataSaver;
import com.oresfall.wallwars.commands.argumenttype.GameArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class Leave {
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = context.getSource().getPlayer();
        IEntityDataSaver targetData = (IEntityDataSaver)target;
        Game game = GameArgumentType.getGame("game", context);

        if(!targetData.getPersistentData().getBoolean("JoinedGame") || (game.leavePlayer(target) == -1)) {
            target.sendMessage(Text.empty().append("You are not in game!").formatted(Formatting.RED));
            targetData.getPersistentData().putBoolean("JoinedGame",false);
            return -1;
        }

        target.sendMessage(Text.of("Left the game!"));
        game.leavePlayer(target);
        return 0;
    }
}
