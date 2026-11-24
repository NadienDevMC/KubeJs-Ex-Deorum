package nadiendev.exdeorumkjs;

import nadiendev.exdeorumkjs.component.BlockPredicateComponent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import thedarkcolour.exdeorum.recipe.BlockPredicate;

public class ExDeorumKJSBindings {
    public NumberProvider constant(float value) {
        return ConstantValue.exactly(value);
    }

    public NumberProvider uniform(float min, float max) {
        return UniformGenerator.between(min, max);
    }

    public NumberProvider binomial(int n, float p) {
        return BinomialDistributionGenerator.binomial(n, p);
    }

    public BlockPredicate block(Block block) {
        return BlockPredicate.singleBlock(block);
    }

    public BlockPredicate blockTag(String tag) {
        return BlockPredicate.blockTag(TagKey.create(Registries.BLOCK, new ResourceLocation(tag.startsWith("#") ? tag.substring(1) : tag)));
    }

    public BlockPredicate blockPredicate(String value) {
        return BlockPredicateComponent.of(value);
    }
}
