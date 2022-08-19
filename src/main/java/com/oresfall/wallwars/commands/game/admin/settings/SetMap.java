package com.oresfall.wallwars.commands.game.admin.settings;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.oresfall.wallwars.commands.argumenttype.GameArgumentType;
import com.oresfall.wallwars.commands.argumenttype.MapArgumentType;
import com.oresfall.wallwars.commands.suggestion.GameSuggestions;
import com.oresfall.wallwars.commands.suggestion.MapSuggestions;
import com.oresfall.wallwars.gameclass.Game;
import com.oresfall.wallwars.gameclass.MapClass;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static com.oresfall.wallwars.utls.Utils.defaultMsg;

public class SetMap {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("setmap")
                .then(
                        CommandManager.argument("game", GameArgumentType.game())
                                .then(
                                        CommandManager.argument("map", MapArgumentType.map())
                                                .suggests(new MapSuggestions())
                                                .executes(SetMap::run)
                                ).suggests(new GameSuggestions())
                );
    }
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Game game = GameArgumentType.getGame(context,"game");
        MapClass map = MapArgumentType.getMap(context,"map");
        ServerPlayerEntity target = context.getSource().getPlayer();

        if(!game.setMap(map.getMap(), map.getFile())) {
            throw new SimpleCommandExceptionType(Text.literal("Values are already set.")).create();
        }

        target.sendMessage(defaultMsg("Changed map"));
        return 0;
    }
}
