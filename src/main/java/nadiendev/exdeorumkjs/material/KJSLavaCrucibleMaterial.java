package nadiendev.exdeorumkjs.material;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import thedarkcolour.exdeorum.block.LavaCrucibleBlock;
import thedarkcolour.exdeorum.material.AbstractCrucibleMaterial;

public class KJSLavaCrucibleMaterial extends AbstractCrucibleMaterial {
    public KJSLavaCrucibleMaterial(SoundType soundType, float strength, boolean needsCorrectTool, int mapColor, String requiredModId) {
        super(soundType, strength, needsCorrectTool, mapColor, requiredModId);
    }

    @Override
    protected Block createBlock() {
        return new LavaCrucibleBlock(props().noOcclusion());
    }
}
