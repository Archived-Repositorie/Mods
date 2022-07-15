package com.oresfall.wallwars.mixin;

import com.oresfall.wallwars.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ModEntityDataSaver implements IEntityDataSaver {
    private NbtCompound persistentData;

    @Override
    public NbtCompound getPersistentData() {
        if(this.persistentData == null) {
            this.persistentData = new NbtCompound();
        }
        return persistentData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable info) {
        if(persistentData != null) {
            nbt.put("wallwar.oresfall_data", persistentData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("wallwar.oresfall_data", 10)) {
            persistentData = nbt.getCompound("wallwar.oresfall_data");
        }
    }
}