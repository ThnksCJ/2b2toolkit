package com.thnkscj.toolkit.modules.modules.client;

import com.thnkscj.toolkit.command.Command;
import com.thnkscj.toolkit.event.events.entity.LivingUpdateEvent;
import com.thnkscj.toolkit.event.events.render.Render2DEvent;
import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.setting.settings.BooleanSetting;
import com.thnkscj.toolkit.setting.settings.DoubleSetting;
import com.thnkscj.toolkit.setting.settings.IntegerSetting;
import com.thnkscj.toolkit.util.misc.Timer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;

public class ElytraFly extends Module {
    public ElytraFly() {
        super("ElytraFly", "Fly with elytra", Category.CLIENT);

        addSettings(speed, step, redeployDelay, boostOnDisable, timerTakeoff, autoJump, timerSpeed);
    }

    private static final float MOVE_FRICTION = 4.02f;
    private static final float LIVING_UPDATE_FRICTION = 3.170326f;

    public static IntegerSetting speed = new IntegerSetting("Speed", "How fast u wanna go", 1, 10, 20);
    public static DoubleSetting step = new DoubleSetting("Step", "", 1.0, 5.0, 10.0);
    public static IntegerSetting redeployDelay = new IntegerSetting("RedeployDelay", "Delay for redeploying elytra", 1, 3, 20);

    public static BooleanSetting boostOnDisable = new BooleanSetting("BoostOnDisable", "Boost on disable", false);
    public static BooleanSetting timerTakeoff = new BooleanSetting("TimerTakeoff", "Auto takeoff with timer", false);
    public static BooleanSetting autoJump = new BooleanSetting("AutoJump", "Jump automatically when enabling efly", true);
    public static DoubleSetting timerSpeed = new DoubleSetting("TimerSpeed", "The timer speed for takeoff", 0.05, 0.05, 1.0);

    final DecimalFormat formatter = new DecimalFormat("#.#");
    private final Timer timer = new Timer();
    private final Timer takeOffTimer = new Timer();

    public boolean tookOff = false;
    private boolean checkTime = false;

    private static long lastOpenElytra = 0L;
    private long ticks = 0;

    private double prevPosX;
    private double prevPosZ;
    private double prevBPS;
    private double fricLiv = LIVING_UPDATE_FRICTION;
    private double fricMove = MOVE_FRICTION;
    private double max = 0;

    @Override
    protected void onEnable() {
        tookOff = false;
        checkTime = false;
        ticks = 0;
        prevBPS = 0;
        max = 0;
        if(mc.player == null || mc.world == null)
            return;
        if(timerTakeoff.isEnabled() && mc.player.onGround && !tookOff && autoJump.isEnabled())
            mc.player.jump();
    }

    @Override
    protected void onDisable() {
        Command.sendMessage("Max Delta: " + max);

        if (!boostOnDisable.isEnabled()) {
            mc.player.motionX = 0;
            mc.player.motionY = 0;
            mc.player.motionZ = 0;
            mc.timer.tickLength = 50f;
        }
    }

    @Override
    public void onUpdate() {
        if (timer.passed(1000)) {
            prevPosX = mc.player.prevPosX;
            prevPosZ = mc.player.prevPosZ;
        }

        if (timerTakeoff.isEnabled() && !tookOff && (mc.player.isElytraFlying() || takeOffTimer.passed(3000) || mc.player.onGround) && checkTime) {
            mc.timer.tickLength = 50f;
            tookOff = true;
            checkTime = false;
        }

        if (timerTakeoff.isEnabled() && mc.player.motionY < 0 && !tookOff) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            mc.timer.tickLength = 50f / timerSpeed.getValue().floatValue();
            checkTime = true;
        }

        if (!mc.world.isBlockLoaded(new BlockPos(mc.player.posX + mc.player.motionX, mc.player.posY, mc.player.posZ + mc.player.motionZ), false)) {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }

        if (mc.player.moveForward > 0.0f) {
            ticks++;
            if (ticks % 4 == 0 && ticks % 16 != 0) {
                fricLiv += (step.getValue().floatValue() / 100f);
                fricMove += (step.getValue().floatValue() / 100f);
            }
        } else {
            ticks = 0;
        }

        mc.gameSettings.gammaSetting = 1000f;
    }


    @Override
    public void onRender2D(Render2DEvent event) {
        final double deltaX = mc.player.posX - prevPosX;
        final double deltaZ = mc.player.posZ - prevPosZ;

        float distance = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        double BPS = distance * 20;
        double KMH = Math.floor((distance / 1000.0f) / (0.05f / 3600.0f));

        mc.fontRenderer.drawStringWithShadow("Speed: " + this.formatter.format(BPS) + " BPS", 2, 2, 0xffffff);
        mc.fontRenderer.drawStringWithShadow("Speed " + this.formatter.format(KMH) + "km/h", 2, 12, 0xffffff);
        mc.fontRenderer.drawStringWithShadow("Delta: " + this.formatter.format(BPS - prevBPS), 2, 22, 0xffffff);

        if(tookOff)
            max = Math.max(max, BPS - prevBPS);

        prevBPS = BPS;
    }

    public void move(EntityPlayerSP player) {
        if(timerTakeoff.isEnabled() && !tookOff)
            return;

        player.motionX = 0.0;
        player.motionY = 0.0;
        player.motionZ = 0.0;
        player.moveForward = player.moveForward > 0.0f ? 1.0f : 0.0f;
        float friction = (float) fricMove;
        player.moveRelative(0.0f, 0.0f, player.moveForward, friction);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
        if(timerTakeoff.isEnabled() && !tookOff)
            return;

        if (mc.player.moveForward > 0.0f) {
            mc.player.motionX = 0.0;
            mc.player.motionY = (-0.03094695885314991);
            mc.player.motionZ = 0.0;
            float friction = (float) fricLiv;
            mc.player.moveRelative(0.0f, 0.0f, mc.player.moveForward, friction);
        }

        mc.player.prevRotationPitch = -2.0f;
        mc.player.rotationPitch = -2.0f;

        if (!mc.player.isElytraFlying() && System.currentTimeMillis() - lastOpenElytra > redeployDelay.getValue() * 1000) {
            mc.timer.tickLength = 50f / timerSpeed.getValue().floatValue();

            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            lastOpenElytra = System.currentTimeMillis();

            fricLiv = LIVING_UPDATE_FRICTION + 0.05;
            fricMove = MOVE_FRICTION + 0.05;
        }

        // if you want control then comment this out
        if (mc.player.ticksExisted % 2.5 == 0) {
            mc.timer.tickLength = 50f;
        }
    }
}
