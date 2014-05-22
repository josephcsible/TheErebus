package erebus.world.biomes.decorators;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenHugeTrees;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import erebus.ModBlocks;
import erebus.block.BlockDoubleHeightPlant;
import erebus.block.BlockLeavesErebus;
import erebus.block.BlockLogErebus;
import erebus.world.biomes.decorators.data.OreSettings;
import erebus.world.biomes.decorators.data.SurfaceType;
import erebus.world.biomes.decorators.data.OreSettings.OreType;
import erebus.world.feature.decoration.WorldGenAmberGround;
import erebus.world.feature.decoration.WorldGenAmberUmberstone;
import erebus.world.feature.decoration.WorldGenPonds;
import erebus.world.feature.decoration.WorldGenQuickSand;
import erebus.world.feature.plant.WorldGenBamboo;
import erebus.world.feature.plant.WorldGenMelon;
import erebus.world.feature.plant.WorldGenTurnips;
import erebus.world.feature.structure.WorldGenWaspDungeon;
import erebus.world.feature.tree.WorldGenAsperTree;
import erebus.world.feature.tree.WorldGenErebusHugeTree;
import erebus.world.feature.tree.WorldGenErebusTrees;
import erebus.world.feature.tree.WorldGenEucalyptusTree;
import erebus.world.feature.tree.WorldGenMossbarkTree;
import erebus.world.feature.tree.WorldGenTallJungleTree;

//@formatter:off
public class BiomeDecoratorUndergroundJungle extends BiomeDecoratorBaseErebus{
	private final WorldGenWaspDungeon genWaspDungeon = new WorldGenWaspDungeon();
	private final WorldGenQuickSand genQuickSand = new WorldGenQuickSand();
	private final WorldGenPonds genPonds = new WorldGenPonds();
	private final WorldGenAmberGround genAmberGround = new WorldGenAmberGround();
	private final WorldGenAmberUmberstone genAmberUmberstone = new WorldGenAmberUmberstone();

	private final WorldGenFlowers genMushroomsBrown = new WorldGenFlowers(Blocks.brown_mushroom);
	private final WorldGenFlowers genMushroomsRed = new WorldGenFlowers(Blocks.red_mushroom);
	private final WorldGenBigMushroom genBigMushroomRed = new WorldGenBigMushroom(0);
	private final WorldGenBigMushroom genBigMushroomBrown = new WorldGenBigMushroom(1);

	private final WorldGenTallGrass genFerns = new WorldGenTallGrass(ModBlocks.fern,1);
	private final WorldGenTallGrass genFiddleheads = new WorldGenTallGrass(ModBlocks.fiddlehead,1);
	private final WorldGenTallGrass genGrass = new WorldGenTallGrass(ModBlocks.erebusGrass,1);

	private final WorldGenerator genTreeMahogany = new WorldGenErebusTrees(true,5,BlockLogErebus.dataMahogany,BlockLeavesErebus.dataMahoganyDecay,false,ModBlocks.logErebusGroup1,ModBlocks.leavesErebus,ModBlocks.thorns);
	private final WorldGenerator genTreeMahoganyLarge = new WorldGenErebusHugeTree(true,BlockLogErebus.dataMahogany,BlockLeavesErebus.dataMahoganyDecay,false,ModBlocks.logErebusGroup1,ModBlocks.leavesErebus);
	private final WorldGenerator genTreeJungle = new WorldGenTrees(true,6,3,3,true);
	private final WorldGenerator genTreeMossbark = new WorldGenMossbarkTree();
	private final WorldGenerator genTreeAsper = new WorldGenAsperTree();
	private final WorldGenerator genTreeJungleTall = new WorldGenTallJungleTree();
	private final WorldGenerator genTreeEucalyptus = new WorldGenEucalyptusTree();

	private final WorldGenerator genBamboo = new WorldGenBamboo(13,false);
	private final WorldGenerator genTurnips = new WorldGenTurnips();
	private final WorldGenerator genMelons = new WorldGenMelon();

	@Override
	protected void populate(){
		for(attempt = 0; attempt < 35; attempt++){
			xx = x+16;
			yy = rand.nextInt(120);
			zz = z+16;

			if (checkSurface(SurfaceType.GRASS,xx,yy,zz)){
				genPonds.prepare((rand.nextDouble()+0.7D)*1.5D);
				genPonds.generate(world,rand,xx,yy,zz);
			}
		}
	}

	@Override
	protected void decorate(){
		if (rand.nextInt(3) == 0){
			for(attempt = 0; attempt < 5; attempt++){
				if (genAmberUmberstone.generate(world,rand,x+offsetXZ(),rand.nextInt(120),z+offsetXZ()))break;
			}
		}

		if (rand.nextInt(6) == 0){
			for(attempt = 0; attempt < 4; attempt++){
				if (genAmberGround.generate(world,rand,x+offsetXZ(),10+rand.nextInt(40),z+offsetXZ()))break;
			}
		}

		if (rand.nextInt(37) == 0){
			for(attempt = 0; attempt < 5; attempt++){
				if (genWaspDungeon.generate(world,rand,x+offsetXZ(),127,z+offsetXZ()))break;
			}
		}

		for(attempt = 0; attempt < 10; attempt++){
			xx = x+offsetXZ();
			yy = rand.nextInt(120);
			zz = z+offsetXZ();

			if (checkSurface(SurfaceType.GRASS,xx,yy,zz)){
				genQuickSand.generate(world,rand,xx,yy,zz);
			}
		}

		for(attempt = 0; attempt < 2200; attempt++){
			xx = x+offsetXZ();
			yy = 15+rand.nextInt(90);
			zz = z+offsetXZ();

			if (checkSurface(SurfaceType.GRASS,xx,yy,zz)){
				WorldGenerator treeGen = null;
				int r = rand.nextInt(31);

				if (r <= 6){
					xx = x+9+rand.nextInt(14);
					zz = z+9+rand.nextInt(14);
					treeGen = new WorldGenHugeTrees(true,4+rand.nextInt(40),3,3);
				}
				else if (r <= 11) treeGen = genTreeMahogany;
				else if (r <= 16){
					xx = x+9+rand.nextInt(14);
					zz = z+9+rand.nextInt(14);
					((WorldGenErebusHugeTree)genTreeMahoganyLarge).prepare(20+rand.nextInt(5));
					treeGen = genTreeMahoganyLarge;
				}
				else if (r <= 20) treeGen = genTreeAsper;
				else if (r <= 23) treeGen = genTreeJungle;
				else if (r <= 26) treeGen = genTreeMossbark;
				else if (r <= 28) treeGen = genTreeJungleTall;
				else treeGen = genTreeEucalyptus;

				if (treeGen!=null) treeGen.generate(world,rand,xx,yy,zz);
			}
		}

		genMushroomsBrown.generate(world,rand,x+offsetXZ(),rand.nextInt(128),z+offsetXZ());
		genMushroomsRed.generate(world,rand,x+offsetXZ(),rand.nextInt(128),z+offsetXZ());

		for(attempt = 0; attempt < 12; attempt++){
			xx = x+offsetXZ();
			yy = 15+rand.nextInt(90);
			zz = z+offsetXZ();

			if (checkSurface(SurfaceType.GRASS,xx,yy,zz)){
				genBigMushroomRed.generate(world,rand,xx,yy,zz);
			}
		}

		for(attempt = 0; attempt < 20; attempt++){
			xx = x+offsetXZ();
			yy = 15+rand.nextInt(90);
			zz = z+offsetXZ();

			if (checkSurface(SurfaceType.GRASS,xx,yy,zz)){
				genBigMushroomBrown.generate(world,rand,xx,yy,zz);
			}
		}

		if (rand.nextInt(11) == 0){
			xx = x+offsetXZ();
			zz = z+offsetXZ();

			for(yy = 90; yy>20; yy--){
				if (checkSurface(SurfaceType.GRASS,xx,yy,zz)){
					if (genBamboo.generate(world,rand,xx,yy,zz))break;
				}
			}
		}

		for(attempt = 0; attempt < 40; attempt++){
			xx = x+offsetXZ();
			yy = 20+rand.nextInt(80);
			zz = z+offsetXZ();

			if (checkSurface(SurfaceType.GRASS,xx,yy,zz)){
				genFerns.generate(world,rand,xx,yy,zz);
			}
		}

		for(attempt = 0; attempt < 16; attempt++){
			xx = x+offsetXZ();
			yy = 20+rand.nextInt(80);
			zz = z+offsetXZ();

			if (checkSurface(SurfaceType.GRASS,xx,yy,zz)){
				genFiddleheads.generate(world,rand,xx,yy,zz);
			}
		}

		for(attempt = 0; attempt < 180; attempt++){
			xx = x+offsetXZ();
			yy = 20+rand.nextInt(80);
			zz = z+offsetXZ();

			if (checkSurface(SurfaceType.GRASS,xx,yy,zz) && world.isAirBlock(xx,yy+1,zz)){
				boolean fern = rand.nextInt(4) == 0;
				world.setBlock(xx,yy,zz,ModBlocks.doubleHeightPlant,fern ? BlockDoubleHeightPlant.dataFernBottom : BlockDoubleHeightPlant.dataTallGrassBottom,2);
				world.setBlock(xx,yy+1,zz,ModBlocks.doubleHeightPlant,fern ? BlockDoubleHeightPlant.dataFernTop : BlockDoubleHeightPlant.dataTallGrassTop,2);
			}
		}

		for(attempt = 0; attempt < 850; attempt++){
			xx = x+offsetXZ();
			yy = 20+rand.nextInt(80);
			zz = z+offsetXZ();

			if (checkSurface(SurfaceType.GRASS,xx,yy,zz)){
				genGrass.generate(world,rand,xx,yy,zz);
			}
		}

		Block block;
		int offset;
		for(attempt = 0; attempt < 800; attempt++){
			xx = x+offsetXZ();
			yy = 30+rand.nextInt(80);
			zz = z+offsetXZ();

			if (world.isAirBlock(xx,yy,zz)){
				block = rand.nextBoolean() ? Blocks.vine : ModBlocks.thorns;
				offset = rand.nextInt(4);

				if (!world.getBlock(xx+Direction.offsetX[offset],yy,zz+Direction.offsetZ[offset]).isNormalCube())continue;

				for(int vineY = rand.nextInt(30); vineY>0; vineY--){
					if (world.isAirBlock(xx+Direction.offsetX[offset],yy-vineY,zz+Direction.offsetZ[offset])){
						world.setBlock(xx+Direction.offsetX[offset],yy-vineY,zz+Direction.offsetZ[offset],Blocks.vine,(offset == 3 ? 1 : offset == 2 ? 4 : offset == 1 ? 0 : 2),3);
					}
				}
			}
		}

		if (rand.nextInt(3) == 0){
			for(attempt = 0; attempt < 6; ++attempt){
				xx = x+offsetXZ();
				yy = 15+rand.nextInt(90);
				zz = z+offsetXZ();

				if (checkSurface(SurfaceType.GRASS,xx,yy,zz)){
					genTurnips.generate(world,rand,xx,yy,zz);
				}
			}
		}
		else if (rand.nextBoolean() || rand.nextBoolean()){
			for(attempt = 0; attempt < 3; ++attempt){
				xx = x+offsetXZ();
				yy = 15+rand.nextInt(90);
				zz = z+offsetXZ();

				if (checkSurface(SurfaceType.GRASS,xx,yy,zz)){
					genMelons.generate(world,rand,xx,yy,zz);
				}
			}
		}
	}

	@Override
	@SuppressWarnings("incomplete-switch")
	protected void modifyOreGen(OreSettings oreGen, OreType oreType, boolean extraOres){
		switch(oreType){
			case COAL:
				oreGen.setIterations(extraOres ? 3 : 4).setY(27,48).generate(world,rand,x,z); // generate first half above caves
				oreGen.setIterations(extraOres ? 4 : 6).setOreAmount(12,14).setY(6,24); // setup more & bigger clusters below caves, let the base code generate
				break;
			case DIAMOND: oreGen.setChance(0.1F).setY(6,16); break; // ~7 times smaller area, thus lowered chance
			case JADE: oreGen.setOreAmount(5); break; // 1 more vein
			case PETRIFIED_WOOD: oreGen.setChance(0.65F).setY(6,64); break; // ~2 times smaller area, thus lowered chance
			case FOSSIL: oreGen.setChance(0.041F); break; // ~ 1/3 chance
		}
	}
}
// @formatter:on
