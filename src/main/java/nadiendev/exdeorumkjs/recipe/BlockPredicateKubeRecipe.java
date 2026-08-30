package nadiendev.exdeorumkjs.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import thedarkcolour.exdeorum.recipe.BlockPredicate;

public class BlockPredicateKubeRecipe extends KubeRecipe {
    private final RecipeKey<BlockPredicate> predicate;

    public BlockPredicateKubeRecipe(RecipeKey<BlockPredicate> predicate) {
        this.predicate = predicate;
    }

    public KubeRecipe state(String property, Object value) {
        BlockPredicate current = getValue(predicate);

        if (current == null) {
            throw new IllegalStateException("Set '" + predicate.name + "' before calling state('" + property + "', ...)");
        }

        JsonElement encoded = BlockPredicate.CODEC.encodeStart(JsonOps.INSTANCE, current).getOrThrow();

        if (!(encoded instanceof JsonObject json) || !json.has("block")) {
            throw new IllegalStateException("Cannot set block state property '" + property + "' on " + current + " - block states only apply to a single block, not to a block tag");
        }

        JsonObject state = json.has("state") ? json.getAsJsonObject("state") : new JsonObject();
        state.addProperty(property, String.valueOf(value));
        json.add("state", state);

        return setValue(predicate, BlockPredicate.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow());
    }

    public KubeRecipe lit(boolean value) {
        return state("lit", value);
    }

    public KubeRecipe powered(boolean value) {
        return state("powered", value);
    }

    public KubeRecipe open(boolean value) {
        return state("open", value);
    }

    public KubeRecipe waterlogged(boolean value) {
        return state("waterlogged", value);
    }

    public KubeRecipe axis(String value) {
        return state("axis", value);
    }

    public KubeRecipe facing(String value) {
        return state("facing", value);
    }

    public KubeRecipe half(String value) {
        return state("half", value);
    }

    public KubeRecipe age(int value) {
        return state("age", value);
    }

    public KubeRecipe level(int value) {
        return state("level", value);
    }
}
