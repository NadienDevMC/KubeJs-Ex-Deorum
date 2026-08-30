package nadiendev.exdeorumkjs.recipe;

import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class ProbabilityKubeRecipe extends KubeRecipe {
    private final RecipeKey<NumberProvider> resultAmount;

    public ProbabilityKubeRecipe(RecipeKey<NumberProvider> resultAmount) {
        this.resultAmount = resultAmount;
    }

    public KubeRecipe constant(float value) {
        return setValue(resultAmount, ConstantValue.exactly(value));
    }

    public KubeRecipe chance(float probability) {
        return setValue(resultAmount, BinomialDistributionGenerator.binomial(1, probability));
    }

    public KubeRecipe uniform(float min, float max) {
        return setValue(resultAmount, UniformGenerator.between(min, max));
    }

    public KubeRecipe binomial(int rolls, float probability) {
        return setValue(resultAmount, BinomialDistributionGenerator.binomial(rolls, probability));
    }
}
