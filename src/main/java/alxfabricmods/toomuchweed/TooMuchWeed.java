package alxfabricmods.toomuchweed;

import alxfabricmods.toomuchweed.blockEntities.CannabisBlockEntity;
import alxfabricmods.toomuchweed.blocks.CannabisBlock;
import alxfabricmods.toomuchweed.items.Weed1G;
import alxfabricmods.toomuchweed.items.WeedSeed;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TooMuchWeed implements ModInitializer {
	// Console Logger
    public static final Logger LOGGER = LoggerFactory.getLogger("toomuchweed");
	//Blocks
	public static final CannabisBlock CANNABIS_BLOCK = new CannabisBlock(AbstractBlock.Settings.create().nonOpaque().sounds(BlockSoundGroup.CROP).noCollision().ticksRandomly());
	public static final BlockEntityType<CannabisBlockEntity> CANNABIS_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,new Identifier("toomuchweed","cannabis_block_entity"), FabricBlockEntityTypeBuilder.create(CannabisBlockEntity::new, CANNABIS_BLOCK).build());
	//Items
	public static final Weed1G WEED_1_G = new Weed1G(new FabricItemSettings());
	public static final WeedSeed WEED_SEED = new WeedSeed(CANNABIS_BLOCK, new FabricItemSettings());

	@Override
	public void onInitialize() {
		//Initialize Strain manager
		new StrainManager();
		LOGGER.info("smoke weed every day");
		//Register Items
		Registry.register(Registries.ITEM, new Identifier("toomuchweed", "cannabis_seed"), WEED_SEED);
		Registry.register(Registries.ITEM, new Identifier("toomuchweed", "weed_1g"), WEED_1_G);
		//Register Blocks
		Registry.register(Registries.BLOCK, new Identifier("toomuchweed", "cannabis_block"),CANNABIS_BLOCK);
		//Register Block Entities

	}
}