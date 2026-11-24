package nadiendev.exdeorumkjs.recipe;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import nadiendev.exdeorumkjs.component.BlockPredicateComponent;
import thedarkcolour.exdeorum.recipe.BlockPredicate;

/**
 * Recipe with a block predicate (crook, crucible heat source, fluid transformation catalyst).
 * Adds chained setters that narrow the predicate to specific block state properties.
 */
public class BlockPredicateKubeRecipe extends RecipeJS {
    private final RecipeKey<BlockPredicate> predicate;

    public BlockPredicateKubeRecipe(RecipeKey<BlockPredicate> predicate) {
        this.predicate = predicate;
    }

    public RecipeJS state(String property, Object value) {
        BlockPredicate current = getValue(predicate);

        if (current == null) {
            throw new IllegalStateException("Set '" + predicate.name + "' before calling state('" + property + "', ...)");
        }

        JsonObject json = current.toJson();

        if (!json.has("block")) {
            throw new IllegalStateException("Cannot set block state property '" + property + "' on " + json + " - block states only apply to a single block, not to a block tag");
        }

        JsonObject state = json.has("state") ? json.getAsJsonObject("state") : new JsonObject();
        state.addProperty(property, String.valueOf(value));
        json.add("state", state);

        return setValue(predicate, BlockPredicateComponent.fromJson(json));
    }

    public RecipeJS lit(boolean value) {
        return state("lit", value);
    }

    public RecipeJS powered(boolean value) {
        return state("powered", value);
    }

    public RecipeJS open(boolean value) {
        return state("open", value);
    }

    public RecipeJS waterlogged(boolean value) {
        return state("waterlogged", value);
    }

    public RecipeJS axis(String value) {
        return state("axis", value);
    }

    public RecipeJS facing(String value) {
        return state("facing", value);
    }

    public RecipeJS half(String value) {
        return state("half", value);
    }

    public RecipeJS age(int value) {
        return state("age", value);
    }

    public RecipeJS level(int value) {
        return state("level", value);
    }
}
