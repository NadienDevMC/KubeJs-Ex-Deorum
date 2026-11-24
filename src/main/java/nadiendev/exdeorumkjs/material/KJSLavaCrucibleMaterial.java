package nadiendev.exdeorumkjs.material;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import thedarkcolour.exdeorum.block.LavaCrucibleBlock;
import thedarkcolour.exdeorum.material.AbstractCrucibleMaterial;

public class KJSLavaCrucibleMaterial extends AbstractCrucibleMaterial {
    public KJSLavaCrucibleMaterial(SoundType soundType, float strength, boolean needsCorrectTool, int mapColor, String requiredModId, boolean transparent) {
        super(soundType, strength, needsCorrectTool, mapColor, requiredModId, transparent);
    }

    @Override
    protected Block createBlock() {
        return new LavaCrucibleBlock(props().noOcclusion());
    }
}
