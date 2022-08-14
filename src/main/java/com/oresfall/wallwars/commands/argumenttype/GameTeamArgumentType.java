package com.oresfall.wallwars.commands.argumenttype;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.oresfall.wallwars.gameclass.Game;
import com.oresfall.wallwars.gameclass.TeamBase;
import net.minecraft.text.Text;

public class GameTeamArgumentType implements ArgumentType<TeamBase> {
    public static GameTeamArgumentType gameTeam() {
        return new GameTeamArgumentType();
    }

    private static CommandContext context;

    private static Game game;

    public static <S> TeamBase getGameTeam(CommandContext<S> context, String name) {
        GameTeamArgumentType.context = context;
        game = GameArgumentType.getGame(context, "game");
        return context.getArgument(name, TeamBase.class);
    }


    @Override
    public TeamBase parse(StringReader reader) throws CommandSyntaxException {
        int argBeginning = reader.getCursor();
        if (!reader.canRead()) {
            reader.skip();
        }
        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }
        String teamString = reader.getString().substring(argBeginning, reader.getCursor());

        TeamBase teamBase = GameTeamArgumentType.game.getTeam(teamString);
        if(teamBase == null) {
            throw new SimpleCommandExceptionType(Text.literal("Team doesn't exist.")).createWithContext(reader);
        }
        return teamBase;
    }
}

