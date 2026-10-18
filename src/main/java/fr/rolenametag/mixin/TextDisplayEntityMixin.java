package fr.rolenametag.mixin;

import fr.rolenametag.RoleConfig;
import fr.rolenametag.ServerTagRoles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Server "text display" nametags: adds the icon and makes them render in front of objects. */
@Mixin(DisplayEntity.TextDisplayEntity.class)
public abstract class TextDisplayEntityMixin {
    @Shadow
    protected abstract Text getText();

    /** Adds the role icon to the text. */
    @Inject(method = "getText()Lnet/minecraft/text/Text;", at = @At("RETURN"), cancellable = true)
    private void rolenametag$addRole(CallbackInfoReturnable<Text> cir) {
        Text modified = ServerTagRoles.process((Entity) (Object) this, cir.getReturnValue());
        if (modified != null) cir.setReturnValue(modified);
    }

    /** Always on: "see through" flag (2), so the name no longer goes behind frames / blocks. */
    @Inject(method = "getDisplayFlags()B", at = @At("RETURN"), cancellable = true)
    private void rolenametag$seeThrough(CallbackInfoReturnable<Byte> cir) {
        RoleConfig cfg = RoleConfig.INSTANCE;
        if (!cfg.enabled) return;
        try {
            if (ServerTagRoles.match((Entity) (Object) this, this.getText()) != null) {
                cir.setReturnValue((byte) (cir.getReturnValue() | 2));
            }
        } catch (Throwable ignored) {
        }
    }
}
