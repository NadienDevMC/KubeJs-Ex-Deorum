package nadiendev.exdeorumkjs.recipe;

import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

/**
 * Recipe with a {@code result_amount} number provider (sieve, compressed sieve, hammer, compressed hammer).
 * Adds the chained setters {@code constant}, {@code chance}, {@code uniform} and {@code binomial}.
 */
public class ProbabilityKubeRecipe extends RecipeJS {
    private final RecipeKey<NumberProvider> resultAmount;

    public ProbabilityKubeRecipe(RecipeKey<NumberProvider> resultAmount) {
        this.resultAmount = resultAmount;
    }

    public RecipeJS constant(float value) {
        return setValue(resultAmount, ConstantValue.exactly(value));
    }

    public RecipeJS chance(float probability) {
        return setValue(resultAmount, BinomialDistributionGenerator.binomial(1, probability));
    }

    public RecipeJS uniform(float min, float max) {
        return setValue(resultAmount, UniformGenerator.between(min, max));
    }

    public RecipeJS binomial(int rolls, float probability) {
        return setValue(resultAmount, BinomialDistributionGenerator.binomial(rolls, probability));
    }
}
