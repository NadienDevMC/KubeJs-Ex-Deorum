package nadiendev.exdeorumkjs.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.kubejs.util.JsonIO;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.recipe.BlockPredicate;

/**
 * Reads and writes Ex Deorum block predicates, in the shape Ex Deorum 1.20.1 reads them:
 * a block object, a block object with a state object, or a block tag object.
 * Scripts can pass a block, a block state, an id, a #tag, an id[prop=value] string or a raw object.
 */
public final class BlockPredicateComponent implements RecipeComponent<BlockPredicate> {
    public static final BlockPredicateComponent BLOCK_PREDICATE = new BlockPredicateComponent();

    private BlockPredicateComponent() {
    }

    @Override
    public String componentType() {
        return "exdeorum_block_predicate";
    }

    @Override
    public ComponentRole role() {
        return ComponentRole.INPUT;
    }

    @Override
    public Class<?> componentClass() {
        return BlockPredicate.class;
    }

    @Override
    public TypeDescJS constructorDescription(DescriptionContext ctx) {
        return TypeDescJS.STRING.or(ctx.javaType(BlockPredicate.class));
    }

    @Override
    public JsonElement write(RecipeJS recipe, BlockPredicate value) {
        return value.toJson();
    }

    @Override
    public boolean hasPriority(RecipeJS recipe, Object from) {
        return from instanceof BlockPredicate || from instanceof Block || from instanceof BlockState;
    }

    @Override
    public BlockPredicate read(RecipeJS recipe, Object from) {
        if (from instanceof BlockPredicate predicate) {
            return predicate;
        }

        if (from instanceof Block block) {
            return BlockPredicate.singleBlock(block);
        }

        if (from instanceof BlockState state) {
            return BlockPredicate.singleBlock(state.getBlock());
        }

        JsonElement json = from instanceof JsonElement element ? element : JsonIO.of(from);

        if (json instanceof JsonPrimitive primitive && primitive.isString()) {
            return of(primitive.getAsString());
        }

        if (json instanceof JsonObject object) {
            return fromJson(object);
        }

        throw new IllegalArgumentException("Cannot read a block predicate from " + from);
    }

    public static BlockPredicate of(String value) {
        return fromJson(toJson(value));
    }

    public static BlockPredicate fromJson(JsonObject json) {
        BlockPredicate predicate = BlockPredicate.fromJson(json);

        if (predicate == null) {
            throw new IllegalArgumentException("Invalid block predicate " + json + " (unknown block, or neither 'block' nor 'block_tag' is set)");
        }

        return predicate;
    }

    /**
     * Turns the script string forms into the JSON object Ex Deorum reads.
     */
    public static JsonObject toJson(String value) {
        String trimmed = value.trim();
        JsonObject json = new JsonObject();

        if (trimmed.startsWith("#")) {
            json.addProperty("block_tag", trimmed.substring(1));
            return json;
        }

        int bracket = trimmed.indexOf('[');

        if (bracket < 0) {
            json.addProperty("block", trimmed);
            return json;
        }

        int end = trimmed.lastIndexOf(']');

        if (end < bracket) {
            throw new IllegalArgumentException("Unclosed block state in block predicate '" + value + "'");
        }

        json.addProperty("block", trimmed.substring(0, bracket).trim());

        JsonObject state = new JsonObject();

        for (String pair : trimmed.substring(bracket + 1, end).split(",")) {
            if (pair.isBlank()) {
                continue;
            }

            String[] split = pair.split("=", 2);

            if (split.length != 2) {
                throw new IllegalArgumentException("Invalid block state property '" + pair + "' in block predicate '" + value + "'");
            }

            state.addProperty(split[0].trim(), split[1].trim());
        }

        json.add("state", state);
        return json;
    }

    @Override
    public String toString() {
        return componentType();
    }
}
