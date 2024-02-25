package alxfabricmods.toomuchweed;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

import static alxfabricmods.toomuchweed.TooMuchWeed.CANNABIS_BLOCK;

@Environment(EnvType.CLIENT)
public class TooMuchWeedClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		//Render shit
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), CANNABIS_BLOCK);
	}
}