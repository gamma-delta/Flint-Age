package ca.xuul.flint.client.screen;

import ca.xuul.flint.Flint;
import ca.xuul.flint.block.entity.FoundryBlockEntity;
import ca.xuul.flint.gui.FoundryMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

public class FoundryScreen extends AbstractContainerScreen<FoundryMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Flint.MOD_ID, "textures/gui/foundry.png");

    private @Nullable Integer heatReqdToCookInput;

    public FoundryScreen(FoundryMenu pMenu, Inventory pPlayerInventory,
                         Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.heatReqdToCookInput = this.menu.heatReqdToCookInput();
    }

    @Override
    protected void containerTick() {
        this.heatReqdToCookInput = this.menu.heatReqdToCookInput();
    }

    @Override
    public void render(PoseStack ps, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(ps);
        super.render(ps, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(ps, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(PoseStack ps, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(ps, x, y, 0, 0, imageWidth, imageHeight);

        var fuelProp = this.menu.fuelProportion();
        if (fuelProp > 0) {
            var scaledFuelProp = (int) (fuelProp * 13f);
            blit(ps, x + 56, y + 36 + 12 - scaledFuelProp, 176, 12 - scaledFuelProp, 14, scaledFuelProp + 1);
        }

        var progressProp = this.menu.progressProportion();
        if (progressProp > 0) {
            var scaledProgressProp = (int) (progressProp * 24);
            blit(ps, x + 79, y + 34, 176, 14, scaledProgressProp + 1, 16);
        }

        var heatProp = this.menu.heatProportion();
        if (heatProp > 0) {
            var scaledHeatProp = Mth.ceil(heatProp * 42f);
            blit(ps, x + 45, y + 22 + 42 - scaledHeatProp, 176, 31 + 42 - scaledHeatProp, 6, scaledHeatProp);
        }

        if (this.isTooColdToCook()) {
            blit(ps, x + 76, y + 19, 176, 79, 12, 12);
        }
    }

    @Override
    protected void renderTooltip(PoseStack ps, int mx, int my) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        if (mx >= x + 45 && mx <= x + 50 && my >= y + 22 && my <= y + 63) {
            this.renderTooltip(ps, Component.translatable("container.flint.foundry.heat", this.menu.heat()), mx, my);
        } else if (mx >= x + 56 && mx <= x + 70 && my >= y + 36 && my <= y + 50 && this.menu.isBurningFuel()) {
            this.renderTooltip(ps,
                    Component.translatable("container.flint.foundry.heat.target", this.menu.targetHeat()),
                    mx, my);
        } else if (mx >= x + 76 && mx <= x + 87 && my >= y + 19 && my <= y + 30 && this.isTooColdToCook()) {
            this.renderTooltip(ps,
                    Component.translatable("container.flint.foundry.heat.too_cold", this.heatReqdToCookInput),
                    mx, my);
        } else if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            this.renderTooltip(ps, this.hoveredSlot.getItem(), mx, my);
        } else if (!this.menu.getCarried().isEmpty()
                && this.hoveredSlot != null && this.hoveredSlot.index == FoundryBlockEntity.SLOT_FUEL) {
            var fuelTemp = this.menu.maxHeatOf(this.menu.getCarried());
            if (fuelTemp != null) {
                this.renderTooltip(ps,
                        Component.translatable("container.flint.foundry.heat.target", fuelTemp),
                        mx, my);
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private boolean isTooColdToCook() {
        if (this.heatReqdToCookInput == null) {
            return false;
        }
        return this.heatReqdToCookInput > this.menu.heat();
    }
}
