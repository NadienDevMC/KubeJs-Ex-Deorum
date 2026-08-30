package nadiendev.exdeorumkjs.material;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import thedarkcolour.exdeorum.block.WaterCrucibleBlock;
import thedarkcolour.exdeorum.material.AbstractCrucibleMaterial;

public class KJSWaterCrucibleMaterial extends AbstractCrucibleMaterial {
    public KJSWaterCrucibleMaterial(SoundType soundType, float strength, boolean needsCorrectTool, int mapColor, String requiredModId) {
        super(soundType, strength, needsCorrectTool, mapColor, requiredModId);
    }

    @Override
    protected Block createBlock(Identifier id) {
        return new WaterCrucibleBlock(props(id).noOcclusion());
    }
}
