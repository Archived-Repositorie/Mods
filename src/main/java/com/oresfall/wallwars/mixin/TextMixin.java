package com.oresfall.wallwars.mixin;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Text.class)
public interface TextMixin {
    @Inject(method = "translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;", at = @At("HEAD"))
    private static void translatable(String key, Object[] args, CallbackInfoReturnable<MutableText> cir) {
        if(key.equals("chat.type.team.sent") || key.equals("chat.type.team.text")) {
            var text = (MutableText) Text.of("");
            args[0] = text;
        }
    }
}
