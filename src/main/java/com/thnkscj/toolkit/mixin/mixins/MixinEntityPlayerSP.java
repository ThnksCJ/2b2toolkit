package com.thnkscj.toolkit.mixin.mixins;

import com.thnkscj.toolkit.event.events.entity.LivingUpdateEvent;
import com.thnkscj.toolkit.event.events.entity.PlayerMoveEvent;
import com.thnkscj.toolkit.mixin.Precedence;
import com.thnkscj.toolkit.modules.ModuleManager;
import com.thnkscj.toolkit.modules.modules.ElytraFly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.thnkscj.toolkit.util.Wrapper.mc;

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

    @Inject(method={"move"}, at={@At(value="HEAD")}, cancellable=true)
    public void move(MoverType moverType, double x, double y, double z, CallbackInfo cb) {
        if (ModuleManager.getModule(ElytraFly.class).isEnabled()) {
            cb.cancel();
            EntityPlayerSP player = mc.player;

            if (player.isElytraFlying()) {
                ModuleManager.getModule(ElytraFly.class).move(player);
                super.move(moverType, player.motionX, 0.0, player.motionZ);
            }
        }
    }

    @Inject(method={"onLivingUpdate"}, at={@At(value="RETURN")})
    public void onLivingUpdate(CallbackInfo cb) {
        LivingUpdateEvent livingUpdateEvent = new LivingUpdateEvent();
        MinecraftForge.EVENT_BUS.post(livingUpdateEvent);
    }
}
