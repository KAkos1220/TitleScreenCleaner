package net.kakos1220.titlescreencleaner.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SplashTextRenderer.class)
public class SplashTextRendererMixin {
    /**
     * @author KAkos1220
     * @reason Removes SplashText rendering entirely
     */
    @Overwrite
    public void render(DrawContext context, int screenWidth, TextRenderer textRenderer, int alpha) {

    }
}
