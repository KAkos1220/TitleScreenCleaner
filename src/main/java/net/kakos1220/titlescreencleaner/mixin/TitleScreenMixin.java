package net.kakos1220.titlescreencleaner.mixin;

import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.friends.FriendsOverlayScreen;
import net.minecraft.client.gui.screens.options.OnlineOptionsScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(value = TitleScreen.class, priority = 900)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Shadow @Nullable private SplashRenderer splash;
    @Shadow @Nullable private FriendsButton friends;
    @Shadow @Nullable private RealmsNotificationsScreen realmsNotificationsScreen;

    @Shadow private int createNormalMenuOptions(int topPos, int spacing) { throw new AssertionError(); }
    @Shadow private int createDemoMenuOptions(int topPos, int spacing) { throw new AssertionError(); }
    @Shadow private boolean realmsNotificationsEnabled() { throw new AssertionError(); }
    @Shadow private int getHorizontalPosition(int currentButton, int numberOfButtons, int buttonWidth) { throw new AssertionError(); }

    @Overwrite
    protected void init() {
        if (this.splash == null) {
            this.splash = this.minecraft.gui.splashManager().getSplash();
        }


        int topPos = this.height / 4 + 48;
        if (this.minecraft.isDemo()) {
            topPos = this.createDemoMenuOptions(topPos, 24);
        } else {
            topPos = this.createNormalMenuOptions(topPos, 24);
        }
        topPos += 36;


        int buttonWidth = 20;
        int numberOfButtons = 1;
        int currentButton = 0;


        this.friends = this.addRenderableWidget(
                CommonButtons.friends(
                        buttonWidth,
                        button -> OnlineOptionsScreen.confirmFriendsListEnabled(this.minecraft, () -> this.minecraft.gui.setScreen(new FriendsOverlayScreen(this)), this),
                        !this.minecraft.isDemo()
                )
        );
        this.friends.setPosition(this.getHorizontalPosition(++currentButton, numberOfButtons, buttonWidth), topPos);


        int totalWidth = numberOfButtons * buttonWidth + (numberOfButtons - 1) * 4;

        this.addRenderableWidget(
                Button.builder(Component.translatable("menu.options"),
                                button -> this.minecraft.gui.setScreen(new OptionsScreen(this, this.minecraft.options, false)))
                        .bounds(this.width / 2 - 100, topPos, 100 - (totalWidth / 2 + 4), 20)
                        .build()
        );

        this.addRenderableWidget(
                Button.builder(Component.translatable("menu.quit"),
                                button -> this.minecraft.stop())
                        .bounds(this.width / 2 + totalWidth / 2 + 4, topPos, 100 - (totalWidth / 2 + 4), 20)
                        .build()
        );


        if (this.realmsNotificationsScreen == null) {
            this.realmsNotificationsScreen = new RealmsNotificationsScreen();
        }

        if (this.realmsNotificationsEnabled()) {
            this.realmsNotificationsScreen.init(this.width, this.height);
        }
    }

    @Inject(
            method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"
            ),
            cancellable = true
    )
    private void removeVersionText(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(
            method = "realmsNotificationsEnabled",
            at = @At("HEAD"),
            cancellable = true
    )
    private void removeRealmsNotifications(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
