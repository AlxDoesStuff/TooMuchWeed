package alxfabricmods.toomuchweed.items;

import alxfabricmods.toomuchweed.StrainManager;
import alxfabricmods.toomuchweed.weedStrain;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Weed1G extends Item {

    public Weed1G (Settings settings) {
        super(settings);
    }
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
            if (!stack.getOrCreateNbt().getBoolean("hasNBT")) {
                //generate random strain if it doesnt have one yet
                switch(Random.create(Random.create().nextLong()).nextBetween(0,2)){
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

}
