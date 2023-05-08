package com.thnkscj.toolkit.modules.modules.client;

import com.thnkscj.toolkit.Toolkit;
import com.thnkscj.toolkit.event.events.block.PlaceBlockEvent;
import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.setting.settings.IntegerSetting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class BlockSwitcher extends Module {

    private static final IntegerSetting delayTicks = new IntegerSetting("Delay", "Delay in ticks after placement", 0, 1, 10);

    public BlockSwitcher(){
        super("BlockSwitcher", "Automatically switch to a random block after placing one", Category.CLIENT);
        addSettings(delayTicks);
    }

    private final Random random = new Random();

    @SubscribeEvent
    public void onPlaceBlock(PlaceBlockEvent event){
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(delayTicks.getValue() * 50);
            } catch (InterruptedException e) {
                Toolkit.log.error(e);
            }
            int[] slots = getValidSlots();
            int slot = random.nextInt(slots.length);
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slots[slot]));
            mc.player.inventory.currentItem = slots[slot];
        });
        thread.start();
    }

    private int[] getValidSlots(){
        int[] slots = new int[9];
        int index = 0;
        for(int i = 0; i < 9; i++){
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if(item instanceof ItemBlock)
                slots[index++] = i;
        }
        int[] finalSlots = new int[index];
        System.arraycopy(slots, 0, finalSlots, 0, index);
        return finalSlots;
    }
}
