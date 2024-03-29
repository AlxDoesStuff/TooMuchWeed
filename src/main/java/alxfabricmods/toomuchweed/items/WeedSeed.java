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
            if (stack.getNbt().getString(StrainManager.NBTKeyItemStrain) != null) {
                String type = StrainManager.strainTypeToString(StrainManager.getStrain(stack.getNbt().getString(StrainManager.NBTKeyItemStrain)).getType());
                stack.getNbt().putString(StrainManager.NBTKeyItemType, type);
                stack.getNbt().putBoolean("hasNBT", true);
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        //display strain if has
        if(stack.getNbt() != null) {
            if (stack.getNbt().get(StrainManager.NBTKeyItemStrain) != null) {
                //TOOLTIP SHIT
                //get the strain as weedStrain
                weedStrain strain = StrainManager.getStrain(stack.getNbt().getString(StrainManager.NBTKeyItemStrain));
                String strainType = StrainManager.strainTypeToString(strain.getType());
                String strainDisplayName = strain.getDisplayName();
                String strainTHC = Integer.toString(strain.getMaxPotentialTHC());
                String strainCBD = Integer.toString(strain.getMaxPotentialCBD());
                //Add Tooltips
                tooltip.add(1, Text.literal(strainDisplayName).formatted(Formatting.GREEN));
                tooltip.add(2, Text.literal(strainType).formatted(Formatting.DARK_GREEN));
                tooltip.add(3, Text.literal("Max THC: " + strainTHC).formatted(Formatting.DARK_RED));
                tooltip.add(4, Text.literal("Max CBD: " + strainCBD).formatted(Formatting.DARK_RED));
            }
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}

