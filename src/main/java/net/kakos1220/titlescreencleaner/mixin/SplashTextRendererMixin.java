package net.kakos1220.titlescreencleaner.mixin;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.SplashRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SplashRenderer.class)
public class SplashTextRendererMixin {
    /**
     * @author KAkos1220
     * @reason Removes SplashText rendering entirely
     */
    @Overwrite
    public void extractRenderState(GuiGraphicsExtractor context, int screenWidth, Font textRenderer, float alpha) {

    }
}
