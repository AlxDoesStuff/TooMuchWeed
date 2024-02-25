package alxfabricmods.toomuchweed.blocks;

import alxfabricmods.toomuchweed.StrainManager;
import alxfabricmods.toomuchweed.TooMuchWeed;
import alxfabricmods.toomuchweed.blockEntities.CannabisBlockEntity;
import alxfabricmods.toomuchweed.weedStrain;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.Property;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import static alxfabricmods.toomuchweed.StrainManager.*;

public class CannabisBlock extends BlockWithEntity implements BlockEntityProvider {
    public int age = 0;
    public static final int FIRST_STAGE_MAX_AGE = 7;
    public static final int SECOND_STAGE_MAX_AGE = 10;
    public static IntProperty AGE = IntProperty.of("age", 0, 10);
    public static IntProperty STRAIN_TYPE = IntProperty.of("straintype", 0, 2);

    //Hitbox from age
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
            Block.createCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D),
            Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 6.0D, 12.0D),
            Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D),
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D),
            Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 10.0D, 15.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };

    public CannabisBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(AGE, 0));
        setDefaultState(getDefaultState().with(STRAIN_TYPE, 2));
    }

    //Stolen from cropBlock
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        CannabisBlockEntity blockEntity = (CannabisBlockEntity) world.getBlockEntity(pos);
        //Aging logic applies only if bottom most block
        if (!world.getBlockState(pos.down(1)).isOf(TooMuchWeed.CANNABIS_BLOCK)) {
            if (world.getBaseLightLevel(pos, 0) >= 9) { //Check if enough light for growth
                //Update age
                age = blockEntity.getAge();
                if (age < blockEntity.getMaxAge()) {
                    //Copied from CropBlock
                    float f = getAvailableMoisture(state.getBlock(), world, pos);
                    if (random.nextInt((int) (25.0F / f) + 1) == 0) {
                        if (age == FIRST_STAGE_MAX_AGE) {
                            if (world.getBlockState(pos.up(1)).isOf(Blocks.AIR)) {
                                //If age turns beyond 7, generate top block
                                blockEntity.age();
                                age = blockEntity.getAge();
                                world.setBlockState(pos, state.with(AGE, age));
                                world.setBlockState(pos.up(1), state.with(AGE, age),2);
                            } else {
                                //No growth beyond 7 if obstructed
                            }
                        } else {
                            //normal aging if not 7
                            blockEntity.age();
                            age = blockEntity.getAge();
                            world.setBlockState(pos, state.with(AGE, age));
                        }
                    }
                }
            }
        }else{
            //Change age to be the same as bottom block if top block of the plant
            age = world.getBlockState(pos.down(1)).get(AGE);
            world.setBlockState(pos, state.with(AGE, age));
        }
        super.randomTick(state, world, pos, random);
    }


    //Inherit NBT from seed
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (stack.getNbt() != null) {
            if (stack.getNbt().getBoolean("hasNBT")) {
                //get the strain as weedStrain
                weedStrain strain = StrainManager.getStrain(stack.getNbt().getString(StrainManager.NBTKeyItemStrain));
                //infos
                int strainType = strain.getType();
                CannabisBlockEntity blockEntity = TooMuchWeed.CANNABIS_BLOCK_ENTITY.get(world, pos);
                if (blockEntity != null) {
                    blockEntity.setStrain(strain.getName());
                    blockEntity.setStrainType(strain.getType());
                    blockEntity.setMaxCBD(strain.getMaxPotentialCBD());
                    blockEntity.setMaxTHC(strain.getMaxPotentialTHC());
                }
                world.setBlockState(pos, state.with(STRAIN_TYPE, strainType));
            }
        }
        super.onPlaced(world, pos, state, placer, stack);
    }

    //Check for farmland
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down(1)).isOf(Blocks.FARMLAND) || world.getBlockState(pos.down(1)).isOf(TooMuchWeed.CANNABIS_BLOCK) && world.getBlockState(pos.down(1)).get(AGE) > FIRST_STAGE_MAX_AGE;
    }

    //Stolen from PlantBlock
    //On neighbourupdate check for farmland
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return !canPlaceAt(state, world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
        builder.add(STRAIN_TYPE);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CannabisBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, TooMuchWeed.CANNABIS_BLOCK_ENTITY, (world1, pos, state1,blockEntity) -> blockEntity.tick(world1, pos, state1));
    }

    //stolen from cropBlock
    protected static float getAvailableMoisture(Block block, BlockView world, BlockPos pos) {
        float f = 1.0F;
        BlockPos blockPos = pos.down();
        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                float g = 0.0F;
                BlockState blockState = world.getBlockState(blockPos.add(i, 0, j));
                if (blockState.isOf(Blocks.FARMLAND)) {
                    g = 1.0F;
                    if ((Integer)blockState.get(FarmlandBlock.MOISTURE) > 0) {
                        g = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    g /= 4.0F;
                }

                f += g;
            }
        }
        BlockPos blockPos2 = pos.north();
        BlockPos blockPos3 = pos.south();
        BlockPos blockPos4 = pos.west();
        BlockPos blockPos5 = pos.east();
        boolean bl = world.getBlockState(blockPos4).isOf(block) || world.getBlockState(blockPos5).isOf(block);
        boolean bl2 = world.getBlockState(blockPos2).isOf(block) || world.getBlockState(blockPos3).isOf(block);
        if (bl && bl2) {
            f /= 2.0F;
        } else {
            boolean bl3 = world.getBlockState(blockPos4.north()).isOf(block) || world.getBlockState(blockPos5.north()).isOf(block) || world.getBlockState(blockPos5.south()).isOf(block) || world.getBlockState(blockPos4.south()).isOf(block);
            if (bl3) {
                f /= 2.0F;
            }
        }

        return f;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return AGE_TO_SHAPE[age];
    }
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
