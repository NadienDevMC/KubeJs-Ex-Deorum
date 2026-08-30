package nadiendev.exdeorumkjs.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.util.JsonUtils;
import dev.latvian.mods.rhino.type.TypeInfo;
import nadiendev.exdeorumkjs.ExDeorumKJS;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.recipe.BlockPredicate;

public record BlockPredicateComponent(RecipeComponentType<?> type) implements RecipeComponent<BlockPredicate> {
    public static final RecipeComponentType<BlockPredicate> BLOCK_PREDICATE =
        RecipeComponentType.unit(ExDeorumKJS.id("block_predicate"), BlockPredicateComponent::new);

    @Override
    public Codec<BlockPredicate> codec() {
        return BlockPredicate.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.of(BlockPredicate.class).or(TypeInfo.STRING).or(TypeInfo.RAW_MAP);
    }

    @Override
    public BlockPredicate wrap(RecipeScriptContext cx, Object from) {
        if (from instanceof BlockPredicate predicate) {
            return predicate;
        }

        if (from instanceof Block block) {
            return BlockPredicate.singleBlock(block);
        }

        if (from instanceof BlockState state) {
            return BlockPredicate.singleBlock(state.getBlock());
        }

        JsonElement json = JsonUtils.of(cx.cx(), from);

        if (json instanceof JsonPrimitive primitive && primitive.isString()) {
            json = fromString(primitive.getAsString());
        }

        return BlockPredicate.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
    }

    private static JsonElement fromString(String value) {
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

        json.addProperty("block", trimmed.substring(0, bracket).trim());

        JsonObject state = new JsonObject();
        String properties = trimmed.substring(bracket + 1, trimmed.lastIndexOf(']'));

        for (String pair : properties.split(",")) {
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

    public static BlockPredicate of(String value) {
        String trimmed = value.trim();

        if (trimmed.startsWith("#")) {
            return BlockPredicate.blockTag(TagKey.create(Registries.BLOCK, ResourceLocation.parse(trimmed.substring(1))));
        }

        if (trimmed.indexOf('[') < 0) {
            return BlockPredicate.singleBlock(BuiltInRegistries.BLOCK.get(ResourceLocation.parse(trimmed)));
        }

        return BlockPredicate.CODEC.parse(JsonOps.INSTANCE, fromString(trimmed)).getOrThrow();
    }

    @Override
    public boolean isEmpty(BlockPredicate value) {
        return value == null;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
