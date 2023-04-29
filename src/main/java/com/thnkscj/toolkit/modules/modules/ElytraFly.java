package com.thnkscj.toolkit.modules.modules;

import com.thnkscj.toolkit.command.Command;
import com.thnkscj.toolkit.event.events.entity.LivingUpdateEvent;
import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.setting.settings.IntegerSetting;
import com.thnkscj.toolkit.util.misc.Timer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;

public class ElytraFly extends Module {
    public ElytraFly() {
        super("ElytraFly", "Fly with elytra", Category.CLIENT);

        addSettings(speed);
    }

    public static IntegerSetting speed = new IntegerSetting("Speed", "How fast u wanna go", 1, 2, 20);

    private static long lastOpenElytra = 0L;
    final DecimalFormat formatter = new DecimalFormat("#.#");
    private final Timer timer = new Timer();
    private double PrevPosX;
    private double PrevPosZ;

    @Override
    public void onUpdate() {
        if (timer.passed(1000)) {
            PrevPosX = mc.player.prevPosX;
            PrevPosZ = mc.player.prevPosZ;
        }

        final double deltaX = mc.player.posX - PrevPosX;
        final double deltaZ = mc.player.posZ - PrevPosZ;

        float distance = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        double BPS = distance * 20;
        double KMH = Math.floor((distance / 1000.0f) / (0.05f / 3600.0f));

        Command.sendMessage("Speed: " + this.formatter.format(BPS) + " BPS");
        Command.sendMessage("Speed " + this.formatter.format(KMH) + "km/h");
    }

    public void move(EntityPlayerSP player) {
        player.motionX = 0.0;
        player.motionY = 0.0;
        player.motionZ = 0.0;
        player.moveForward = player.moveForward > 0.0f ? 1.0f : 0.0f;
        player.moveRelative(0.0f, 0.0f, player.moveForward, 4.02f);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (mc.player.moveForward > 0.0f) {
            mc.player.motionX = 0.0;
            mc.player.motionY = (-0.03094695885314991);
            mc.player.motionZ = 0.0;
            mc.player.moveRelative(0.0f, 0.0f, mc.player.moveForward, (3.170326f));
        }

        mc.player.prevRotationPitch = -2.0f;
        mc.player.rotationPitch = -2.0f;
        if (!mc.player.isElytraFlying() && System.currentTimeMillis() - lastOpenElytra > 2000L) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            lastOpenElytra = System.currentTimeMillis();
        }
    }
}
