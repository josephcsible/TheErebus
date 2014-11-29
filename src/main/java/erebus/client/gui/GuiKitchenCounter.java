package erebus.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import erebus.inventory.ContainerKitchenCounter;
import erebus.tileentity.TileEntityKitchenCounter;

public class GuiKitchenCounter extends GuiContainer {
	private TileEntityKitchenCounter kitchen;

	private static final ResourceLocation gui = new ResourceLocation("erebus:textures/gui/container/kitchenCounter.png");

	public GuiKitchenCounter(InventoryPlayer inv, TileEntityKitchenCounter tile) {
		super(new ContainerKitchenCounter(inv, tile));
		kitchen = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(gui);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		int size = kitchen.getScaledHoneyAmount(65);
		drawTexturedModalRect(x + 10, y + 75 - size, 176, 96 - size, 18, 100);

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {

	}
}