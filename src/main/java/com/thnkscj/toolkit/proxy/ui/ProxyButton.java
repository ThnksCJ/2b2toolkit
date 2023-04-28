package com.thnkscj.toolkit.proxy.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ProxyButton extends GuiButton {
    BufferedImage image;

    public ProxyButton(int x, int y) {
        super(8068, x, y, 40, 40, "");

        try {
            image = ImageIO.read(new URL("https://i.imgur.com/sO7U61C.png"));
        } catch (IOException ignored) {
        }
    }

    @Override
    public void drawButton(@NotNull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        drawRect(this.x, this.y, this.x + this.width, this.y + this.height, 0xFF505060);

        drawRect(this.x + this.width, this.y + (this.height / 2) - 15, this.x + this.width + 50, this.y + (this.height / 2) + 15, 0xFF505060);

        // image
        {
            int scale = 1;

            while (image.getWidth() / scale > 32 || image.getHeight() / scale > 32) {
                scale++;
            }

            for (int x = 0; x < image.getWidth() / scale; x++) {
                for (int y = 0; y < image.getHeight() / scale; y++) {
                    drawRect(this.x + 4 + x, this.y + 4 + y, this.x + 5 + x, this.y + 5 + y, image.getRGB(x * scale, y * scale));
                }
            }
        }

        this.drawCenteredString(mc.fontRenderer, "Proxy's", this.x + this.width + 25, this.y + (this.height / 2) - 4, 0xFFFFFF);
        // outlines
        drawRect(this.x, this.y, this.x + this.width, this.y + 1, 0xFF000000);
        drawRect(this.x, this.y, this.x + 1, this.y + this.height, 0xFF000000);
        drawRect(this.x + this.width, this.y, this.x + this.width + 1, this.y + this.height, 0xFF000000);
        drawRect(this.x, this.y + this.height, this.x + this.width, this.y + this.height + 1, 0xFF000000);
        drawRect(this.x + this.width, this.y + (this.height / 2) - 15, this.x + this.width + 1, this.y + (this.height / 2) + 15, 0xFF000000);
        drawRect(this.x + this.width, this.y + (this.height / 2) - 15, this.x + this.width + 50, this.y + (this.height / 2) - 14, 0xFF000000);
        drawRect(this.x + this.width, this.y + (this.height / 2) + 15, this.x + this.width + 50, this.y + (this.height / 2) + 16, 0xFF000000);
        drawRect(this.x + this.width + 50, this.y + (this.height / 2) - 15, this.x + this.width + 51, this.y + (this.height / 2) + 15, 0xFF000000);
    }

    // make rectangle where text is clickable and image is clickable
    @Override
    public boolean mousePressed(@NotNull Minecraft mc, int mouseX, int mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width + 50 && mouseY >= this.y && mouseY <= this.y + this.height;
    }
}
