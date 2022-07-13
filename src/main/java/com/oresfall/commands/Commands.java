package com.oresfall.commands;

import com.oresfall.commands.argumenttype.GameArgumentType;
import com.oresfall.commands.game.Game;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;

import static com.oresfall.Main.modid;

public class Commands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(Game::register);
        ArgumentTypeRegistry.registerArgumentType(new Identifier(modid,"games"), GameArgumentType.class,  ConstantArgumentSerializer.of(GameArgumentType::game));
    }
}
