package com.thnkscj.toolkit.mixin.mixins;

import com.thnkscj.toolkit.event.events.entity.PlayerMoveEvent;
import com.thnkscj.toolkit.mixin.Precedence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EntityPlayerSP.class, priority = Precedence.MAXIMUM)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {
    public MixinEntityPlayerSP() {
        super(Minecraft.getMinecraft().world, Minecraft.getMinecraft().getSession().getProfile());
    }

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
    public void move(AbstractClientPlayer player, MoverType type, double x, double y, double z) {
        PlayerMoveEvent moveEvent = new PlayerMoveEvent(type, x, y, z);
        MinecraftForge.EVENT_BUS.post(moveEvent);
        super.move(type, moveEvent.getX(), moveEvent.getY(), moveEvent.getZ());
    }
}
