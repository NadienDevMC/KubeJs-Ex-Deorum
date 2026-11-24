package nadiendev.exdeorumkjs.material;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import nadiendev.exdeorumkjs.builder.ExDeorumRecipeGen;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * Builds the crafting recipe for a custom material, using the same layout Ex Deorum uses for its
 * own blocks of that kind.
 */
public final class MaterialRecipeGen {
    private MaterialRecipeGen() {
    }

    public static JsonObject build(MaterialKind kind, MaterialBuilder builder, ResourceLocation block) {
        // A compressed sieve uses an ingot in the middle instead of a slab, so it needs no slab.
        if (builder.craftMain == null || (builder.craftSlab == null && kind != MaterialKind.COMPRESSED_SIEVE)) {
            return null;
        }

        JsonObject key = new JsonObject();
        List<String> pattern;

        switch (kind) {
            case SIEVE -> {
                key.add("O", ExDeorumRecipeGen.ingredient(builder.craftMain));
                key.add("_", ExDeorumRecipeGen.ingredient(builder.craftSlab));
                key.add("I", ExDeorumRecipeGen.ingredient(builder.craftRod));
                pattern = List.of("O O", "O_O", "I I");
            }
            case COMPRESSED_SIEVE -> {
                key.add("O", ExDeorumRecipeGen.ingredient(builder.craftMain));
                key.add("_", ExDeorumRecipeGen.ingredient(builder.craftBinder));
                key.add("I", ExDeorumRecipeGen.ingredient(builder.craftRod));
                pattern = List.of("O O", "O_O", "I I");
            }
            case BARREL, LAVA_CRUCIBLE, WATER_CRUCIBLE -> {
                key.add("s", ExDeorumRecipeGen.ingredient(builder.craftMain));
                key.add("m", ExDeorumRecipeGen.ingredient(builder.craftSlab));
                pattern = List.of("s s", "s s", "sms");
            }
            default -> {
                return null;
            }
        }

        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shaped");
        json.addProperty("category", "misc");
        json.add("key", key);

        JsonArray rows = new JsonArray();
        pattern.forEach(rows::add);
        json.add("pattern", rows);

        JsonObject result = new JsonObject();
        result.addProperty("item", block.toString());
        result.addProperty("count", 1);
        json.add("result", result);

        return json;
    }
}
