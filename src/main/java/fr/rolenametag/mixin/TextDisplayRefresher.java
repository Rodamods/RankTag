package fr.rolenametag.mixin;

import net.minecraft.entity.decoration.DisplayEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Lets us recompute the nametag text when a setting changes. */
@Mixin(DisplayEntity.TextDisplayEntity.class)
public interface TextDisplayRefresher {
    @Invoker("refreshData")
    void rolenametag$refreshData(boolean shouldLerp, float lerpProgress);
}
