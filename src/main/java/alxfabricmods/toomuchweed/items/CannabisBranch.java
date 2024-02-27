package alxfabricmods.toomuchweed.items;

import alxfabricmods.toomuchweed.StrainManager;
import alxfabricmods.toomuchweed.weedStrain;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CannabisBranch extends Item {
    public CannabisBranch (Settings settings) { super(settings);}

    private int yield = 0;

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!stack.getOrCreateNbt().getBoolean("hasNBT")) {
            //generate random strain if it doesnt have one yet
            if (stack.getNbt().getString(StrainManager.NBTKeyItemStrain) != null && stack.getNbt().get(StrainManager.NBTKeyItemYield) != null) {
                yield = stack.getNbt().getInt(StrainManager.NBTKeyItemYield);
                stack.getNbt().putBoolean("hasNBT", true);
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        //display strain if has
        if(stack.getNbt() != null) {
            //TOOLTIP SHIT
            //get the strain as weedStrain
            weedStrain strain = StrainManager.getStrain(stack.getNbt().getString(StrainManager.NBTKeyItemStrain));
            String strainType = StrainManager.strainTypeToString(strain.getType());
            String strainDisplayName = strain.getDisplayName();
            String THC = Integer.toString(stack.getNbt().getInt(StrainManager.NBTKeyItemTHC));
            String CBD = Integer.toString(stack.getNbt().getInt(StrainManager.NBTKeyItemCBD));
                //Add Tooltips
            tooltip.add(1, Text.literal(strainDisplayName).formatted(Formatting.GREEN));
            tooltip.add(2, Text.literal(strainType).formatted(Formatting.DARK_GREEN));
            tooltip.add(3, Text.literal("THC: " + THC).formatted(Formatting.DARK_RED));
            tooltip.add(4, Text.literal("CBD: " + CBD).formatted(Formatting.DARK_RED));
            tooltip.add(5, Text.literal("Yield: " + yield + "G").formatted(Formatting.DARK_RED));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
