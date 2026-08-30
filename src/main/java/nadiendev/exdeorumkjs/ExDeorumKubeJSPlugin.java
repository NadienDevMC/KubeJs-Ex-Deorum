package nadiendev.exdeorumkjs;

import com.google.gson.JsonElement;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.RecipesKubeEvent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentTypeRegistry;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.recipe.schema.RecipeNamespace;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaType;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.core.RecipeManagerKJS;
import nadiendev.exdeorumkjs.builder.ExDeorumRecipeGen;
import nadiendev.exdeorumkjs.builder.HammerItemBuilder;
import nadiendev.exdeorumkjs.builder.MeshItemBuilder;
import nadiendev.exdeorumkjs.builder.OreChunkItemBuilder;
import nadiendev.exdeorumkjs.component.BlockPredicateComponent;
import nadiendev.exdeorumkjs.component.FluidComponent;
import nadiendev.exdeorumkjs.component.NumberProviderComponent;
import nadiendev.exdeorumkjs.component.WeightedBlockStatesComponent;
import nadiendev.exdeorumkjs.material.ExDeorumEvents;
import nadiendev.exdeorumkjs.material.ItemKind;
import nadiendev.exdeorumkjs.material.MaterialKind;
import nadiendev.exdeorumkjs.material.MaterialRecipeGen;
import nadiendev.exdeorumkjs.material.MaterialRegistryKubeEvent;
import nadiendev.exdeorumkjs.schema.ExDeorumSchemas;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.util.ID;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import thedarkcolour.exdeorum.material.AbstractMaterial;
import net.minecraft.resources.ResourceLocation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExDeorumKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerBindings(BindingRegistry bindings) {
        bindings.add("ExDeorumKJS", new ExDeorumKJSBindings());
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(ExDeorumEvents.GROUP);
    }

    @Override
    public void initStartup() {
        try {
            for (MaterialKind kind : MaterialKind.values()) {
                MaterialRegistryKubeEvent event = new MaterialRegistryKubeEvent(kind);
                ExDeorumEvents.REGISTRY.post(ScriptType.STARTUP, kind.id, event);
                event.registerAll();
            }

            for (ItemKind kind : ItemKind.values()) {
                ExDeorumEvents.REGISTRY.post(ScriptType.STARTUP, kind.id, new MaterialRegistryKubeEvent(kind));
            }
        } catch (Exception ex) {
            ExDeorumKJS.LOGGER.error("Failed to register Ex Deorum materials from startup scripts", ex);
        }
    }

    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.of(Registries.ITEM, reg -> {
            reg.add(ExDeorumKJS.exdeorum("mesh"), MeshItemBuilder.class, MeshItemBuilder::new);
            reg.add(ExDeorumKJS.exdeorum("hammer"), HammerItemBuilder.Normal.class, HammerItemBuilder.Normal::new);
            reg.add(ExDeorumKJS.exdeorum("compressed_hammer"), HammerItemBuilder.Compressed.class, HammerItemBuilder.Compressed::new);
            reg.add(ExDeorumKJS.exdeorum("ore_chunk"), OreChunkItemBuilder.class, OreChunkItemBuilder::new);
        });
    }

    @Override
    public void generateData(KubeDataGenerator generator) {
        Map<String, JsonArray> mineable = new LinkedHashMap<>();

        for (MaterialRegistryKubeEvent.Registered entry : MaterialRegistryKubeEvent.registered()) {
            AbstractMaterial material = entry.material();

            try {
                ResourceLocation id = BuiltInRegistries.BLOCK.getKey(material.getBlock());

                JsonObject recipe = MaterialRecipeGen.build(entry.kind(), entry.builder(), id);

                if (recipe != null) {
                    generator.json(ExDeorumKJS.id("recipe/materials/" + id.getPath()), recipe);
                }

                if (entry.mineableWith() != null) {
                    mineable.computeIfAbsent(entry.mineableWith(), k -> new JsonArray()).add(id.toString());
                }
                LootTable table = LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1F))
                        .when(ExplosionCondition.survivesExplosion())
                        .add(LootItem.lootTableItem(material.getItem())))
                    .build();

                generator.json(id.withPath(ID.BLOCK_LOOT_TABLE), generator.getRegistries().json().withEncoder(LootTable.CODEC).apply(new Holder.Direct<>(table)).getOrThrow());
            } catch (Exception ex) {
                ExDeorumKJS.LOGGER.error("Failed to generate a loot table for a custom Ex Deorum material", ex);
            }
        }

        for (Map.Entry<String, JsonArray> entry : mineable.entrySet()) {
            JsonObject tag = new JsonObject();
            tag.addProperty("replace", false);
            tag.add("values", entry.getValue());

            String tool = entry.getKey();
            ResourceLocation tagId = tool.indexOf(58) >= 0
                ? ResourceLocation.parse(tool)
                : ResourceLocation.withDefaultNamespace("mineable/" + tool);

            generator.json(tagId.withPath(s -> "tags/block/" + s), tag);
        }
    }

    @Override
    public void registerRecipeComponents(RecipeComponentTypeRegistry registry) {
        registry.register(NumberProviderComponent.NUMBER_PROVIDER);
        registry.register(BlockPredicateComponent.BLOCK_PREDICATE);
        registry.register(FluidComponent.FLUID);
        registry.register(WeightedBlockStatesComponent.WEIGHTED_BLOCK_STATES);
    }

    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        RecipeNamespace namespace = registry.namespace(ExDeorumKJS.EXDEORUM_ID);

        namespace.register("sieve", ExDeorumSchemas.SIEVE);
        namespace.register("compressed_sieve", ExDeorumSchemas.COMPRESSED_SIEVE);
        namespace.register("hammer", ExDeorumSchemas.HAMMER);
        namespace.register("compressed_hammer", ExDeorumSchemas.COMPRESSED_HAMMER);
        namespace.register("crook", ExDeorumSchemas.CROOK);
        namespace.register("crucible_heat_source", ExDeorumSchemas.CRUCIBLE_HEAT_SOURCE);
        namespace.register("lava_crucible", ExDeorumSchemas.LAVA_CRUCIBLE);
        namespace.register("water_crucible", ExDeorumSchemas.WATER_CRUCIBLE);
        namespace.register("barrel_compost", ExDeorumSchemas.BARREL_COMPOST);
        namespace.register("barrel_mixing", ExDeorumSchemas.BARREL_MIXING);
        namespace.register("barrel_fluid_mixing", ExDeorumSchemas.BARREL_FLUID_MIXING);
        namespace.register("barrel_fluid_transformation", ExDeorumSchemas.BARREL_FLUID_TRANSFORMATION);
        namespace.register("ore_chunk", ExDeorumSchemas.ORE_CHUNK);
    }

    /**
     * Ex Deorum ships its own minimal {@code exdeorum:sieve} schema that only knows about
     * {@code result} and {@code ingredient}, which cannot produce a valid sieve recipe.
     * Whichever plugin registers last wins, and the mod file order is not guaranteed, so the
     * complete schema is re-asserted here once every plugin has registered.
     */
    @Override
    public void beforeRecipeLoading(RecipesKubeEvent event, RecipeManagerKJS manager, Map<ResourceLocation, JsonElement> recipeJsons) {
        ExDeorumRecipeGen.injectInto(recipeJsons);

        RecipeNamespace namespace = event.recipeSchemaStorage.namespace(ExDeorumKJS.EXDEORUM_ID);
        RecipeSchemaType current = namespace.get("sieve");

        if (current != null && !describesMesh(current.schema)) {
            namespace.register("sieve", ExDeorumSchemas.SIEVE);
        }
    }

    private static boolean describesMesh(RecipeSchema schema) {
        return schema.keys.stream().anyMatch(key -> key.names.contains("mesh"));
    }
}
