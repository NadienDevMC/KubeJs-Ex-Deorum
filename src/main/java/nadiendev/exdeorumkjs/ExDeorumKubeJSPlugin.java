package nadiendev.exdeorumkjs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import nadiendev.exdeorumkjs.builder.ExDeorumRecipeGen;
import nadiendev.exdeorumkjs.builder.HammerItemBuilder;
import nadiendev.exdeorumkjs.builder.MeshItemBuilder;
import nadiendev.exdeorumkjs.builder.OreChunkItemBuilder;
import nadiendev.exdeorumkjs.material.ExDeorumEvents;
import nadiendev.exdeorumkjs.material.ItemKind;
import nadiendev.exdeorumkjs.material.MaterialKind;
import nadiendev.exdeorumkjs.material.MaterialRecipeGen;
import nadiendev.exdeorumkjs.material.MaterialRegistryKubeEvent;
import nadiendev.exdeorumkjs.schema.ExDeorumSchemas;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import thedarkcolour.exdeorum.material.AbstractMaterial;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExDeorumKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void init() {
        // Lets scripts use StartupEvents.registry('item', e => e.create('x', 'exdeorum:hammer')) as well.
        RegistryInfo.ITEM.addType("exdeorum:mesh", MeshItemBuilder.class, MeshItemBuilder::new);
        RegistryInfo.ITEM.addType("exdeorum:hammer", HammerItemBuilder.Normal.class, HammerItemBuilder.Normal::new);
        RegistryInfo.ITEM.addType("exdeorum:compressed_hammer", HammerItemBuilder.Compressed.class, HammerItemBuilder.Compressed::new);
        RegistryInfo.ITEM.addType("exdeorum:ore_chunk", OreChunkItemBuilder.class, OreChunkItemBuilder::new);
    }

    @Override
    public void registerEvents() {
        ExDeorumEvents.GROUP.register();
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("ExDeorumKJS", new ExDeorumKJSBindings());
    }

    /**
     * Runs right after the startup scripts have loaded and before KubeJS and Forge fire their
     * registry events, which is the only window where new Ex Deorum materials and items can still
     * be registered.
     */
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
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        ExDeorumSchemas.register(event);
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        Map<ResourceLocation, JsonElement> recipes = new LinkedHashMap<>();
        ExDeorumRecipeGen.generateInto(recipes);
        recipes.forEach((id, json) -> generator.json(new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath()), json));

        Map<String, JsonArray> mineable = new LinkedHashMap<>();

        for (MaterialRegistryKubeEvent.Registered entry : MaterialRegistryKubeEvent.registered()) {
            AbstractMaterial material = entry.material();

            try {
                ResourceLocation id = BuiltInRegistries.BLOCK.getKey(material.getBlock());

                JsonObject recipe = MaterialRecipeGen.build(entry.kind(), entry.builder(), id);

                if (recipe != null) {
                    generator.json(new ResourceLocation(ExDeorumKJS.MOD_ID, "recipes/materials/" + id.getPath()), recipe);
                }

                if (entry.mineableWith() != null) {
                    mineable.computeIfAbsent(entry.mineableWith(), k -> new JsonArray()).add(id.toString());
                }

                generator.json(new ResourceLocation(id.getNamespace(), "loot_tables/blocks/" + id.getPath()), blockLootTable(BuiltInRegistries.ITEM.getKey(material.getItem())));
            } catch (Exception ex) {
                ExDeorumKJS.LOGGER.error("Failed to generate data for a custom Ex Deorum material", ex);
            }
        }

        for (Map.Entry<String, JsonArray> entry : mineable.entrySet()) {
            JsonObject tag = new JsonObject();
            tag.addProperty("replace", false);
            tag.add("values", entry.getValue());

            String tool = entry.getKey();
            ResourceLocation tagId = tool.indexOf(':') >= 0 ? new ResourceLocation(tool) : new ResourceLocation("minecraft", "mineable/" + tool);

            generator.json(new ResourceLocation(tagId.getNamespace(), "tags/blocks/" + tagId.getPath()), tag);
        }
    }

    private static JsonObject blockLootTable(ResourceLocation item) {
        JsonObject entry = new JsonObject();
        entry.addProperty("type", "minecraft:item");
        entry.addProperty("name", item.toString());

        JsonObject condition = new JsonObject();
        condition.addProperty("condition", "minecraft:survives_explosion");

        JsonArray entries = new JsonArray();
        entries.add(entry);

        JsonArray conditions = new JsonArray();
        conditions.add(condition);

        JsonObject pool = new JsonObject();
        pool.addProperty("rolls", 1.0F);
        pool.add("conditions", conditions);
        pool.add("entries", entries);

        JsonArray pools = new JsonArray();
        pools.add(pool);

        JsonObject table = new JsonObject();
        table.addProperty("type", "minecraft:block");
        table.add("pools", pools);
        return table;
    }
}
