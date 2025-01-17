package nordmods.uselessreptile.mixin.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import nordmods.uselessreptile.common.init.URStatusEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Unique private float prevStrength;
    @Shadow @Final MinecraftClient client;

    @Shadow @Final private BufferBuilderStorage buffers;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void renderShockOverlay(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        if (client.player.hasStatusEffect(URStatusEffects.SHOCK)) {
            DrawContext drawContext = new DrawContext(client, buffers.getEntityVertexConsumers());
            float strength = MathHelper.clamp(client.player.getStatusEffect(URStatusEffects.SHOCK).getDuration()/100f, 0f, 1f);
            renderShockOverlay(drawContext, strength, tickCounter.getTickDelta(false));
            prevStrength = strength;
        } else prevStrength = 1f;
    }

    @Unique
    private void renderShockOverlay(DrawContext context, float strength, float tickDelta) {
        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

        context.getMatrices().push();
        float scale = MathHelper.clamp(1.5f - MathHelper.lerp(tickDelta, prevStrength, strength), 1f, 2f);
        context.getMatrices().translate(width/2f, height/2f, 0f);
        context.getMatrices().scale(scale, scale, scale);
        context.getMatrices().translate(-width/2f, -height/2f, 0f);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE);

        float r = 0.72f * strength;
        float g = 0.82f * strength;
        float b = 0.9f * strength;
        context.setShaderColor(r, g, b, 1f);
        context.drawTexture(Identifier.ofVanilla("textures/misc/nausea.png"), 0, 0, -90, 0.0F, 0.0F, width, height, width, height);
        context.setShaderColor(1f, 1f, 1f, 1f);

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        context.getMatrices().pop();
    }
}
