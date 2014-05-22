package erebus.world.biomes;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import erebus.ModBiomes;
import erebus.ModBlocks;
import erebus.core.helper.TimeMeasurement;
import erebus.world.biomes.decorators.BiomeDecoratorBaseErebus;
import erebus.world.loot.IWeightProvider;

// @formatter:off
public abstract class BiomeBaseErebus extends BiomeGenBase implements IWeightProvider{
	private final BiomeDecoratorBaseErebus decorator;
	private short biomeWeight;
	private int grassColor,foliageColor;
	private short[] fogColorRGB = new short[]{ 255,255,255 };
	
	public BiomeBaseErebus(int biomeID, BiomeDecoratorBaseErebus decorator){
		super(biomeID);
		this.decorator = decorator;
		
		setDisableRain();
		
		spawnableMonsterList.clear();
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableCaveCreatureList.clear();
	}
	
	protected final BiomeBaseErebus setColors(int grassAndFoliage){
		setColors(grassAndFoliage,grassAndFoliage);
		return this;
	}
	
	protected final BiomeBaseErebus setColors(int grass, int foliage){
		setColor(grass);
		func_76733_a(grass);
		grassColor = grass;
		foliageColor = foliage;
		return this;
	}
	
	protected final BiomeBaseErebus setFog(int red, int green, int blue){
		this.fogColorRGB = new short[]{ (short)red, (short)green, (short)blue };
		return this;
	}
	
	protected final BiomeBaseErebus setWeight(int weight){
		if (this.biomeWeight != 0)throw new RuntimeException("Cannot set biome weight twice!");
		this.biomeWeight = (short)weight;
		if (getClass().getGenericSuperclass() == BiomeBaseErebus.class)ModBiomes.biomeList.add(this); // add to list once weight is known
		return this;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public final int getBiomeGrassColor(int a, int b, int c){
		return grassColor;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public final int getBiomeFoliageColor(int a, int b, int c){
		return foliageColor;
	}

	@SideOnly(Side.CLIENT)
	public final short[] getFogRGB(){
		return fogColorRGB;
	}
	
	@Override
	public final short getWeight(){
		return biomeWeight;
	}
	
	public void populate(World world, Random rand, int x, int z){
		decorator.populate(world,rand,x,z);
	}

	@Override
	public void decorate(World world, Random rand, int x, int z){
		String id = getClass().getSimpleName();
		TimeMeasurement.start(id);
		
		decorator.decorate(world,rand,x,z);
		
		TimeMeasurement.finish(id);
	}
	
	public Block placeCaveBlock(Block block, int x, int y, int z, Random rand){
		return block == ModBlocks.umberstone || block == topBlock || block == fillerBlock || block == Blocks.sandstone ? Blocks.air : block;
	}
	
	/**
	 * Every time a biome is generated, this method is called to specify sub biome to generate inside the biome.
	 * @param randomValue value between 0 and 100 (both inclusive) generated by GenLayer
	 * @return sub biome to generate, or null
	 */
	public BiomeBaseErebus getRandomSubBiome(int randomValue){
		return null;
	}
	
	protected static class SpawnEntry extends SpawnListEntry{
		protected SpawnEntry(Class<? extends EntityLiving> mobClass, int weight, int minGroupCount, int maxGroupCount){
			super(mobClass,weight,minGroupCount,maxGroupCount);
		}
	}
}
// @formatter:on
