package alxfabricmods.toomuchweed.items;

import alxfabricmods.toomuchweed.StrainManager;
import alxfabricmods.toomuchweed.TooMuchWeed;
import alxfabricmods.toomuchweed.weedStrain;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WeedSeed extends AliasedBlockItem {
    public WeedSeed (Block block, Settings settings)
    {
        super(block,settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!stack.getOrCreateNbt().getBoolean("hasNBT")) {
            //generate random strain if it doesnt have one yet
            switch (Random.create(Random.create().nextLong()).nextBetween(0, 2)) {
                case 0:
                    stack.getNbt().putString(StrainManager.NBTKeyItemStrain, "SOUR_DIESEL");
                    break;
                case 1:
                    stack.getNbt().putString(StrainManager.NBTKeyItemStrain, "ACAPULCO_GOLD");
                    break;
                case 2:
                    stack.getNbt().putString(StrainManager.NBTKeyItemStrain, "ICE_CREAM_CAKE");
                    break;
            }
            String type = StrainManager.strainTypeToString(StrainManager.getStrain(stack.getNbt().getString(StrainManager.NBTKeyItemStrain)).getType());
            stack.getNbt().putString(StrainManager.NBTKeyItemType, type);
            stack.getNbt().putBoolean("hasNBT", true);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        //display strain if has
        if(stack.getNbt() != null) {
            if (stack.getNbt().getBoolean("hasNBT")) {
                //TOOLTIP SHIT
                //get the strain as weedStrain
                weedStrain strain = StrainManager.getStrain(stack.getNbt().getString(StrainManager.NBTKeyItemStrain));
                //Get the strain type (sativa... as string
                String strainType = StrainManager.strainTypeToString(strain.getType());
                //other infos
                String strainName = strain.getName();
                String strainTHC = Integer.toString(strain.getMaxPotentialTHC());
                String strainCBD = Integer.toString(strain.getMaxPotentialCBD());
                //Add Tooltips
                tooltip.add(1, Text.literal(strainName).formatted(Formatting.GREEN));
                tooltip.add(2, Text.literal(strainType).formatted(Formatting.DARK_GREEN));
                tooltip.add(3, Text.literal("Max THC: " + strainTHC).formatted(Formatting.DARK_RED));
                tooltip.add(4, Text.literal("Max CBD: " + strainCBD).formatted(Formatting.DARK_RED));
            }
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}

