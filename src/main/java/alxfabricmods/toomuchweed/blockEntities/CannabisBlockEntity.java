package alxfabricmods.toomuchweed.blockEntities;

import alxfabricmods.toomuchweed.StrainManager;
import alxfabricmods.toomuchweed.TooMuchWeed;
import alxfabricmods.toomuchweed.blocks.CannabisBlock;
import it.unimi.dsi.fastutil.Hash;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.entry.RegistryEntryListCodec;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSupplier;
import org.apache.http.config.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class CannabisBlockEntity extends BlockEntity {

    //NBT Current Values with defaults
    private String strain = "ERR_STRAIN";
    private int strainType = 2;
    private float maxTHC = 100;
    private float maxCBD = 100;
    private float currentTHC = 0;
    private float currentCBD  = 0;
    private int age = 0;
    private final int maxAge = 10;
    private int maxPotentialYield = 100;
    private int currentYield = 0;
    private boolean hasUV = false;

    //stupid fucking retard shit
    private final static HashMap<Integer,String> biomeMap = new HashMap<Integer,String>();


    public CannabisBlockEntity(BlockPos pos, BlockState state){
        super(TooMuchWeed.CANNABIS_BLOCK_ENTITY,pos,state);
        //im going to kill myself
        biomeMap.put(0, "the_void");
        biomeMap.put(1, "plains");
        biomeMap.put(2, "sunflower_plains");
        biomeMap.put(3, "snowy_plains");
        biomeMap.put(4, "ice_spikes");
        biomeMap.put(5, "desert");
        biomeMap.put(6, "swamp");
        biomeMap.put(7, "mangrove_swamp");
        biomeMap.put(8, "forest");
        biomeMap.put(9, "flower_forest");
        biomeMap.put(10, "birch_forest");
        biomeMap.put(11, "dark_forest");
        biomeMap.put(12, "old_growth_birch_forest");
        biomeMap.put(13, "old_growth_pine_taiga");
        biomeMap.put(14, "old_growth_spruce_taiga");
        biomeMap.put(15, "taiga");
        biomeMap.put(16, "snowy_taiga");
        biomeMap.put(17, "savanna");
        biomeMap.put(18, "savanna_plateau");
        biomeMap.put(19, "windswept_hills");
        biomeMap.put(20, "windswept_gravelly_hills");
        biomeMap.put(21, "windswept_forest");
        biomeMap.put(22, "windswept_savanna");
        biomeMap.put(23, "jungle");
        biomeMap.put(24, "sparse_jungle");
        biomeMap.put(25, "bamboo_jungle");
        biomeMap.put(26, "badlands");
        biomeMap.put(27, "eroded_badlands");
        biomeMap.put(28, "wooded_badlands");
        biomeMap.put(29, "meadow");
        biomeMap.put(30, "cherry_grove");
        biomeMap.put(31, "grove");
        biomeMap.put(32, "snowy_slopes");
        biomeMap.put(33, "frozen_peaks");
        biomeMap.put(34, "jagged_peaks");
        biomeMap.put(35, "stony_peaks");
        biomeMap.put(36, "river");
        biomeMap.put(37, "frozen_river");
        biomeMap.put(38, "beach");
        biomeMap.put(39, "snowy_beach");
        biomeMap.put(40, "stony_shore");
        biomeMap.put(41, "warm_ocean");
        biomeMap.put(42, "lukewarm_ocean");
        biomeMap.put(43, "deep_lukewarm_ocean");
        biomeMap.put(44, "ocean");
        biomeMap.put(45, "deep_ocean");
        biomeMap.put(46, "cold_ocean");
        biomeMap.put(47, "deep_cold_ocean");
        biomeMap.put(48, "frozen_ocean");
        biomeMap.put(49, "deep_frozen_ocean");
        biomeMap.put(50, "mushroom_fields");
        biomeMap.put(51, "dripstone_caves");
        biomeMap.put(52, "lush_caves");
        biomeMap.put(53, "deep_dark");
        biomeMap.put(54, "nether_wastes");
        biomeMap.put(55, "warped_forest");
        biomeMap.put(56, "crimson_forest");
        biomeMap.put(57, "soul_sand_valley");
        biomeMap.put(58, "basalt_deltas");
        biomeMap.put(59, "the_end");
        biomeMap.put(60, "end_highlands");
        biomeMap.put(61, "end_midlands");
        biomeMap.put(62, "small_end_islands");
        biomeMap.put(63, "end_barrens");
    }
    //NBT
    @Override
    public void writeNbt(NbtCompound nbt) {
        //Write NBT data
        nbt.putString(StrainManager.NBTKeyBlockStrain,strain);
        nbt.putInt(StrainManager.NBTKeyBlockType,strainType);
        nbt.putFloat(StrainManager.NBTKeyBlockMaxTHC,maxTHC);
        nbt.putFloat(StrainManager.NBTKeyBlockMaxCBD,maxCBD);
        nbt.putFloat(StrainManager.NBTKeyBlockCurrentTHC,currentTHC);
        nbt.putFloat(StrainManager.NBTKeyBlockCurrentCBD,currentCBD);
        nbt.putInt(StrainManager.NBTKeyBlockAge,age);
        nbt.putBoolean(StrainManager.NBTKeyBlockHasUV,hasUV);
        nbt.putInt(StrainManager.NBTKeyBlockYield,currentYield);
        nbt.putInt(StrainManager.NBTKeyBlockMaxYield,maxPotentialYield);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        age = nbt.getInt(StrainManager.NBTKeyBlockAge);
        strain = nbt.getString(StrainManager.NBTKeyBlockStrain);
        strainType  = nbt.getInt(StrainManager.NBTKeyBlockType);
        maxTHC  = nbt.getFloat(StrainManager.NBTKeyBlockMaxTHC);
        maxCBD  = nbt.getFloat(StrainManager.NBTKeyBlockMaxCBD);
        currentTHC  = nbt.getFloat(StrainManager.NBTKeyBlockCurrentTHC);
        currentCBD  = nbt.getFloat(StrainManager.NBTKeyBlockCurrentCBD);
        hasUV  = nbt.getBoolean(StrainManager.NBTKeyBlockHasUV);
        currentYield = nbt.getInt(StrainManager.NBTKeyBlockYield);
        maxPotentialYield = nbt.getInt(StrainManager.NBTKeyBlockMaxYield);
        super.readNbt(nbt);
    }

    //clientside stuff
    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }


    public void age(){
        //Change growthFactor
        //Factor in UV (sun)
        float growthFactor = 1F;
        if (!isHasUV()){
            growthFactor = growthFactor - 0.35F;
        }
        //Factor in biome
        //this is stupid TODO: find better solution
        RegistryEntry<Biome> currentBiome = this.getWorld().getBiome(this.getPos());
        float factorFactor = 1F;
        for (int i = 0; i <= 63; i++){
            if (currentBiome.matchesId(Identifier.of("minecraft",biomeMap.get(i)))){
                factorFactor = switch (i) {
                    case 0, 3, 4, 16, 26, 27, 31, 32, 33, 34, 35, 37, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 52, 51, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63 ->
                            0;
                    case 1, 2, 8, 9, 10, 11, 12, 13, 29, 30, 36, 50 -> 0.75F;
                    case 5 -> 0.1F;
                    case 6 -> 0.4F;
                    case 7 -> 0.6F;
                    case 14, 15, 28 -> 0.2F;
                    case 17, 18, 23, 24, 38, 25 -> 1F;
                    case 19, 20, 21 -> 0.3F;
                    case 22 -> 0.5F;
                    default -> 0;
                };
            }
        }
        growthFactor = growthFactor * factorFactor;
        if (growthFactor > 1){
            growthFactor = 1;
        }
        float thcIncrease = (maxTHC/maxAge)*growthFactor;
        float cbdIncrease = (maxCBD/maxAge)*growthFactor;
        setAge(age+1);
        setCurrentCBD(currentCBD+cbdIncrease);
        setCurrentTHC(currentTHC+thcIncrease);
        if (age > 7){
            //start actually yielding
            float yieldIncrease = ((float) maxPotentialYield / (maxAge-7)) * growthFactor;
            float rFactor = 0.01F * (100F - (float) Random.create().nextBetween(0,15));
            yieldIncrease = yieldIncrease * rFactor;
            setCurrentYield(currentYield + (int) yieldIncrease);
        }
    }

    public void tick (World world, BlockPos pos, BlockState state){
        if(world.isClient()){
            return;
        }
        //Check if is another cannabisBlocks top Half
        if (world.getBlockState(pos.down(1)).isOf(TooMuchWeed.CANNABIS_BLOCK)) {
            //Copy bottom half nbt
            CannabisBlockEntity blockEntity = TooMuchWeed.CANNABIS_BLOCK_ENTITY.get(world,pos.down(1));
            if (blockEntity != null) {
                this.setStrain(blockEntity.getStrain());
                this.setStrainType(blockEntity.getStrainType());
                this.setMaxTHC(blockEntity.getMaxTHC());
                this.setMaxCBD(blockEntity.getMaxCBD());
                this.setCurrentCBD(blockEntity.getCurrentCBD());
                this.setCurrentTHC(blockEntity.getCurrentTHC());
                this.setAge(blockEntity.getAge());
                this.setHasUV(blockEntity.isHasUV());
                this.setCurrentYield(blockEntity.getCurrentYield());
                this.setMaxPotentialYield(blockEntity.getMaxPotentialYield());
            }
        }else {
            //Main tick logic (if main half)
            int topY = world.getTopY(Heightmap.Type.MOTION_BLOCKING, pos.getX(), pos.getZ());
            //Check if sun
            if (pos.getY() == topY) {
                setHasUV(true);
            } else {
                setHasUV(false);
            }
        }
    }

    public void setAge(int age) {
        this.age = age;
        markDirty();
    }

    public void setCurrentTHC (float currentTHC) {
        this.currentTHC = currentTHC;
        markDirty();
    }

    public void setCurrentCBD(float currentCBD) {
        this.currentCBD = currentCBD;
        markDirty();
    }
    public void setHasUV (boolean hasUV){
        this.hasUV = hasUV;
        markDirty();
    }

    public void setMaxCBD(float maxCBD) {
        this.maxCBD = maxCBD;
        markDirty();
    }

    public void setMaxTHC(float maxTHC) {
        this.maxTHC = maxTHC;
        markDirty();
    }

    public void setStrain(String strain) {
        this.strain = strain;
        markDirty();
    }

    public void setStrainType(int strainType) {
        this.strainType = strainType;
        markDirty();
    }

    public void setMaxPotentialYield(int maxPotentialYield) {
        this.maxPotentialYield = maxPotentialYield;
        markDirty();
    }

    public void setCurrentYield(int currentYield) {
        this.currentYield = currentYield;
        markDirty();
    }

    public int getCurrentYield() {
        return currentYield;
    }
    public int getMaxPotentialYield() {
        return maxPotentialYield;
    }

    public boolean isHasUV() {
        return hasUV;
    }

    public int getAge() {
        return age;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public float getMaxCBD() {
        return maxCBD;
    }

    public float getMaxTHC() {
        return maxTHC;
    }

    public float getCurrentCBD() {
        return currentCBD;
    }

    public float getCurrentTHC() {
        return currentTHC;
    }

    public int getStrainType() {
        return strainType;
    }

    public String getStrain() {
        return strain;
    }
}
