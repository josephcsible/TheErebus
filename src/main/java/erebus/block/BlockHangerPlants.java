package erebus.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import erebus.ModBlocks.ISubBlocksBlock;
import erebus.ModItems;
import erebus.core.helper.Utils;
import erebus.item.ErebusFood;
import erebus.item.ErebusMaterial.DATA;
import erebus.item.block.ItemBlockErebusPlantSmall;

public class BlockHangerPlants extends BlockBush implements ISubBlocksBlock {

	public static final String[] iconPaths = new String[] { "hanger0", "hanger1", "hanger2", "hanger3", "hanger4", "hangerFruit", "hangerSeed" };

	public static final int dataHanger0 = 0, dataHanger1 = 1, dataHanger2 = 2, dataHanger3 = 3, dataHanger4 = 4, dataHangerFruit = 5, dataHangerSeed = 6;

	@SideOnly(Side.CLIENT)
	public IIcon[] icons;

	public BlockHangerPlants() {
		setTickRandomly(true);
	}
	
	@Override
	public int tickRate(World world) {
		return 5;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, int x, int y, int z) {
		int meta = access.getBlockMetadata(x, y, z);
		float widthReduced = 0, heightReduced = 0;

		switch (meta) {
		case dataHanger0:
			widthReduced = 0.375F;
			heightReduced = 0.875F;
			break;
		case dataHanger1:
			widthReduced = 0.1875F;
			heightReduced = 0.625F;
			break;
		case dataHanger2:
			widthReduced = 0.1875F;
			heightReduced = 0.375F;
			break;
		case dataHanger3:
			widthReduced = 0.1875F;
			heightReduced = 0.1875F;
			break;
		case dataHanger4:
			widthReduced = 0.1875F;
			heightReduced = 0F;
			break;
		case dataHangerSeed:
			widthReduced = 0.125F;
			heightReduced = 0F;
			break;
		case dataHangerFruit:
			widthReduced = 0.0625F;
			heightReduced = 0F;
			break;
		}
		setBlockBounds(widthReduced, heightReduced, widthReduced, 1F - widthReduced, 1F, 1F - widthReduced);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		int meta = world.getBlockMetadata(x, y, z);
			int yy = y - 1;
			// New shoot
			if (world.isAirBlock(x, yy, z) && this.canBlockStay(world, x, yy, z)) {
				switch (meta) {
				case dataHanger4:
				case dataHangerFruit:
				case dataHangerSeed:
					world.setBlock(x, yy, z, this, dataHanger0, 2);
					break;
				}
			}
			
		// Shoot maturity
			switch (meta) {
			case dataHanger0:
				world.setBlock(x, y, z, this, dataHanger1, 2);
				break;
			case dataHanger1:
				world.setBlock(x, y, z, this, dataHanger2, 2);
				break;
			case dataHanger2:
				world.setBlock(x, y, z, this, dataHanger3, 2);
				break;
			case dataHanger3:
				world.setBlock(x, y, z, this, dataHanger4, 2);
				break;
			}
			
		// Fruit
		if (rand.nextInt(6) == 0 && meta == dataHanger4)
			world.setBlock(x, y, z, this, dataHangerFruit, 2);
		
		// Seeds
		if (rand.nextInt(10) == 0 && meta == dataHangerFruit)
			world.setBlock(x, y, z, this, dataHangerSeed, 2);
	}

	@Override
	public void registerBlockIcons(IIconRegister reg) {
		icons = new IIcon[iconPaths.length];
		int i = 0;
		for (String path : iconPaths)
			icons[i++] = reg.registerIcon("erebus:" + path);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if (meta < 0 || meta >= icons.length)
			return null;
		return icons[meta];
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getSubBlocks(Item id, CreativeTabs tab, List list) {
		for (int i = 0; i < icons.length; i++)
			list.add(new ItemStack(id, 1, i));
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		return 0;
	}

	@Override
	public Item getItemDropped(int id, Random random, int fortune) {
		return null;
	}

	@Override
	public int getDamageValue(World world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		int meta = world.getBlockMetadata(x, y, z);
		ItemStack item = null;
		if(meta == dataHangerFruit) {
			item = new ItemStack(ModItems.erebusFood, 1, ErebusFood.dataMiddleFruit);
			Utils.dropStack(world, (int) (x + 0.5D), (int) (y + 0.5D), (int) (z + 0.5D), item);
			world.setBlock(x, y, z, this, dataHanger4, 2);
			return true;
		}
		
		if(meta == dataHangerSeed) {
			item = new ItemStack(ModItems.erebusMaterials, 1, DATA.middleFruitSeeds.ordinal());
			Utils.dropStack(world, (int) (x + 0.5D), (int) (y + 0.5D), (int) (z + 0.5D), item);
			world.setBlock(x, y, z, this, dataHanger4, 2);
			return true;
		}
		return true;
    }
	
	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int id, EntityPlayer player) {
		int meta = world.getBlockMetadata(x, y, z);
		ItemStack item = null;
		if(meta == dataHangerFruit) {
			item = new ItemStack(ModItems.erebusFood, 2, ErebusFood.dataMiddleFruit);
			Utils.dropStack(world, (int) (x + 0.5D), (int) (y + 0.5D), (int) (z + 0.5D), item);
		}
		
		if(meta == dataHangerSeed) {
			item = new ItemStack(ModItems.erebusMaterials, 2, DATA.middleFruitSeeds.ordinal());
			Utils.dropStack(world, (int) (x + 0.5D), (int) (y + 0.5D), (int) (z + 0.5D), item);
		}
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return isValidBlock(world.getBlock(x, y + 1, z)) && canBlockStay(world, x, y, z);
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		return isValidBlock(world.getBlock(x, y + 1, z));
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbour) {
		int meta = world.getBlockMetadata(x, y, z);
		ItemStack item = null;
		if (world.isAirBlock(x, y + 1, z)) {
			if(meta == dataHangerFruit) {
				item = new ItemStack(ModItems.erebusFood, 1, ErebusFood.dataMiddleFruit);
				Utils.dropStack(world, (int) (x + 0.5D), (int) (y + 0.5D), (int) (z + 0.5D), item);	
			}
			if(meta == dataHangerSeed) {
				item = new ItemStack(ModItems.erebusMaterials, 1, DATA.middleFruitSeeds.ordinal());
				Utils.dropStack(world, (int) (x + 0.5D), (int) (y + 0.5D), (int) (z + 0.5D), item);
			}
			world.setBlockToAir(x, y, z);
		}
		canBlockStay(world, x, y, z);
	}
	
	private boolean isValidBlock(Block block) {
		return block.getMaterial().blocksMovement() || block == this;
	}

	@Override
	public Class<? extends ItemBlock> getItemBlockClass() {
		return ItemBlockErebusPlantSmall.class;
	}
}