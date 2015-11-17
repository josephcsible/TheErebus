package erebus.item;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import erebus.ModBlocks;
import erebus.ModItems;
import erebus.ModMaterials;
import erebus.ModTabs;
import erebus.client.model.armor.ModelArmorGlider;
import erebus.client.model.armor.ModelArmorPowered;
import erebus.core.handler.KeyBindingHandler;
import erebus.item.ItemMaterials.DATA;
import erebus.network.PacketPipeline;
import erebus.network.server.PacketGlider;
import erebus.network.server.PacketGliderPowered;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class ItemArmorGlider extends ItemArmor {

	public ItemArmorGlider() {
		super(ModMaterials.armorREINEXOSPECIAL, 2, 1);
		setCreativeTab(ModTabs.gears);
	}

	@Override
	public boolean getIsRepairable(ItemStack armour, ItemStack material) {
		return material.getItem() == ModItems.materials && material.getItemDamage() == DATA.gliderWing.ordinal();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getArmorTexture(ItemStack is, Entity entity, int slot, String type) {
		return "erebus:textures/models/armor/glider.png";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase player, ItemStack stack, int slot) {
		if (canFly()) {
			ModelArmorPowered model = new ModelArmorPowered();
			model.bipedHead.showModel = false;
			model.bipedHeadwear.showModel = false;
			model.bipedBody.showModel = false;
			model.bipedRightArm.showModel = false;
			model.bipedLeftArm.showModel = false;
			model.bipedRightLeg.showModel = false;
			model.bipedLeftLeg.showModel = false;

			if (stack.hasTagCompound()) {
				model.isGliding = stack.getTagCompound().getBoolean("isGliding");
				model.isPowered = stack.getTagCompound().getBoolean("isPowered");
			}

			return model;
		} else {
			ModelArmorGlider model = new ModelArmorGlider();

			model.bipedHead.showModel = false;
			model.bipedHeadwear.showModel = false;
			model.bipedBody.showModel = false;
			model.bipedRightArm.showModel = false;
			model.bipedLeftArm.showModel = false;
			model.bipedRightLeg.showModel = false;
			model.bipedLeftLeg.showModel = false;

			if (stack.hasTagCompound())
				model.isGliding = stack.getTagCompound().getBoolean("isGliding");

			return model;
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
		if (world.isRemote) {
			if (!stack.hasTagCompound())
				stack.stackTagCompound = new NBTTagCompound();

			if (stack.getTagCompound().getBoolean("isGliding") && !KeyBindingHandler.glide.getIsKeyPressed()) {
				stack.getTagCompound().setBoolean("isGliding", false);
				PacketPipeline.sendToServer(new PacketGlider(false));
			}

			if (canFly())
				if (stack.getTagCompound().getBoolean("isPowered") && !KeyBindingHandler.poweredGlide.getIsKeyPressed()) {
					stack.getTagCompound().setBoolean("isPowered", false);
					PacketPipeline.sendToServer(new PacketGliderPowered(false));
				}
		}
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		onUpdate(stack, world, player, 0, false);

		player.fallDistance = 0.0F;

		if (!stack.hasTagCompound()) {
			stack.stackTagCompound = new NBTTagCompound();
			return;
		}
		NBTTagCompound nbt = stack.getTagCompound();

		if (nbt.getBoolean("isGliding"))
			if (!player.onGround) {
				player.motionX *= 1.05D;
				player.motionZ *= 1.05D;
				player.motionY *= 0.5D;
			}

		if (nbt.getBoolean("isPowered") && canFly() && hasGemOrIsCreative(player))
			if (!player.onGround) {
				player.motionX *= 1.05D;
				player.motionZ *= 1.05D;
				player.motionY += 0.1D;

				if (!player.capabilities.isCreativeMode) {
					nbt.setInteger("fuelTicks", nbt.getInteger("fuelTicks") + 1);
					if (nbt.getInteger("fuelTicks") >= 80) {
						nbt.setInteger("fuelTicks", 0);
						player.inventory.consumeInventoryItem(Item.getItemFromBlock(ModBlocks.redGem));
					}
				}
			}
	}

	private boolean hasGemOrIsCreative(EntityPlayer player) {
		return player.capabilities.isCreativeMode || player.inventory.hasItem(Item.getItemFromBlock(ModBlocks.redGem));
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		stack.stackTagCompound.setBoolean("isGliding", false);
		stack.stackTagCompound.setBoolean("isPowered", false);
		stack.stackTagCompound.setInteger("fuelTicks", 0);

	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onPlayerRenderPre(RenderPlayerEvent.Pre event) {
		GL11.glPushMatrix();

		EntityPlayer player = event.entityPlayer;
		ItemStack chestPlate = player.inventory.armorInventory[2];
		if (chestPlate != null && chestPlate.getItem() instanceof ItemArmorGlider && chestPlate.hasTagCompound())
			if (chestPlate.getTagCompound().getBoolean("isGliding") && !player.onGround || chestPlate.getTagCompound().getBoolean("isPowered") && !player.onGround) {
				float yaw = player.rotationYaw;
				float x = (float) Math.cos(Math.PI * yaw / 180F);
				float y = (float) Math.sin(Math.PI * yaw / 180F);
				GL11.glRotatef(60.0F, x, 0.0F, y);
				player.limbSwingAmount = 0.1F;
			}
	}

	public boolean canFly() {
		return this == ModItems.armorGliderPowered;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onPlayerRenderPost(RenderPlayerEvent.Post event) {
		GL11.glPopMatrix();
	}
}