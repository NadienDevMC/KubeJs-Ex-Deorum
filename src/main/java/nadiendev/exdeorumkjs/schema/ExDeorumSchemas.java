package nadiendev.exdeorumkjs.schema;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.TagKeyComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeNamespace;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaType;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import nadiendev.exdeorumkjs.ExDeorumKJS;
import nadiendev.exdeorumkjs.component.BlockPredicateComponent;
import nadiendev.exdeorumkjs.component.FluidAmountComponent;
import nadiendev.exdeorumkjs.component.FluidComponent;
import nadiendev.exdeorumkjs.component.ItemIdComponent;
import nadiendev.exdeorumkjs.component.NumberProviderComponent;
import nadiendev.exdeorumkjs.component.WeightedBlockStatesComponent;
import nadiendev.exdeorumkjs.recipe.BlockPredicateKubeRecipe;
import nadiendev.exdeorumkjs.recipe.OreChunkKubeRecipe;
import nadiendev.exdeorumkjs.recipe.ProbabilityKubeRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.item.crafting.RecipeSerializer;
import thedarkcolour.exdeorum.recipe.BlockPredicate;
import thedarkcolour.exdeorum.recipe.WeightedList;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The thirteen Ex Deorum recipe schemas, written against the JSON that Ex Deorum 1.20.1 reads.
 * Call {@link #register(RegisterRecipeSchemasEvent)} from the plugin's {@code registerRecipeSchemas}.
 */
public final class ExDeorumSchemas {
    public static final NumberProvider ONE = ConstantValue.exactly(1F);

    // Sieve
    public static final RecipeKey<OutputItem> SIEVE_RESULT = ItemIdComponent.RESULT.key("result");
    public static final RecipeKey<InputItem> SIEVE_INGREDIENT = ItemComponents.INPUT.key("ingredient");
    public static final RecipeKey<OutputItem> SIEVE_MESH = ItemIdComponent.MESH.key("mesh");
    public static final RecipeKey<NumberProvider> SIEVE_RESULT_AMOUNT = NumberProviderComponent.NUMBER_PROVIDER.key("result_amount").preferred("resultAmount").optional(ONE).alwaysWrite();
    public static final RecipeKey<Boolean> SIEVE_BY_HAND_ONLY = BooleanComponent.BOOLEAN.key("by_hand_only").preferred("byHandOnly").optional(Boolean.FALSE);

    public static final RecipeSchema SIEVE = new RecipeSchema(ProbabilityKubeRecipe.class, () -> new ProbabilityKubeRecipe(SIEVE_RESULT_AMOUNT),
        SIEVE_RESULT, SIEVE_INGREDIENT, SIEVE_MESH, SIEVE_RESULT_AMOUNT, SIEVE_BY_HAND_ONLY)
        .constructor(SIEVE_RESULT, SIEVE_INGREDIENT, SIEVE_MESH)
        .constructor(SIEVE_RESULT, SIEVE_INGREDIENT, SIEVE_MESH, SIEVE_RESULT_AMOUNT)
        .constructor(SIEVE_RESULT, SIEVE_INGREDIENT, SIEVE_MESH, SIEVE_RESULT_AMOUNT, SIEVE_BY_HAND_ONLY)
        .uniqueOutputId(SIEVE_RESULT);

    // Compressed sieve
    public static final RecipeKey<OutputItem> COMPRESSED_SIEVE_RESULT = ItemIdComponent.RESULT.key("result");
    public static final RecipeKey<InputItem> COMPRESSED_SIEVE_INGREDIENT = ItemComponents.INPUT.key("ingredient");
    public static final RecipeKey<OutputItem> COMPRESSED_SIEVE_MESH = ItemIdComponent.MESH.key("mesh");
    public static final RecipeKey<NumberProvider> COMPRESSED_SIEVE_RESULT_AMOUNT = NumberProviderComponent.NUMBER_PROVIDER.key("result_amount").preferred("resultAmount").optional(ONE).alwaysWrite();
    public static final RecipeKey<Boolean> COMPRESSED_SIEVE_BY_HAND_ONLY = BooleanComponent.BOOLEAN.key("by_hand_only").preferred("byHandOnly").optional(Boolean.FALSE);

    public static final RecipeSchema COMPRESSED_SIEVE = new RecipeSchema(ProbabilityKubeRecipe.class, () -> new ProbabilityKubeRecipe(COMPRESSED_SIEVE_RESULT_AMOUNT),
        COMPRESSED_SIEVE_RESULT, COMPRESSED_SIEVE_INGREDIENT, COMPRESSED_SIEVE_MESH, COMPRESSED_SIEVE_RESULT_AMOUNT, COMPRESSED_SIEVE_BY_HAND_ONLY)
        .constructor(COMPRESSED_SIEVE_RESULT, COMPRESSED_SIEVE_INGREDIENT, COMPRESSED_SIEVE_MESH)
        .constructor(COMPRESSED_SIEVE_RESULT, COMPRESSED_SIEVE_INGREDIENT, COMPRESSED_SIEVE_MESH, COMPRESSED_SIEVE_RESULT_AMOUNT)
        .constructor(COMPRESSED_SIEVE_RESULT, COMPRESSED_SIEVE_INGREDIENT, COMPRESSED_SIEVE_MESH, COMPRESSED_SIEVE_RESULT_AMOUNT, COMPRESSED_SIEVE_BY_HAND_ONLY)
        .uniqueOutputId(COMPRESSED_SIEVE_RESULT);

    // Hammer
    public static final RecipeKey<OutputItem> HAMMER_RESULT = ItemIdComponent.RESULT.key("result");
    public static final RecipeKey<InputItem> HAMMER_INGREDIENT = ItemComponents.INPUT.key("ingredient");
    public static final RecipeKey<NumberProvider> HAMMER_RESULT_AMOUNT = NumberProviderComponent.NUMBER_PROVIDER.key("result_amount").preferred("resultAmount").optional(ONE).alwaysWrite();

    public static final RecipeSchema HAMMER = new RecipeSchema(ProbabilityKubeRecipe.class, () -> new ProbabilityKubeRecipe(HAMMER_RESULT_AMOUNT),
        HAMMER_RESULT, HAMMER_INGREDIENT, HAMMER_RESULT_AMOUNT)
        .constructor(HAMMER_RESULT, HAMMER_INGREDIENT)
        .constructor(HAMMER_RESULT, HAMMER_INGREDIENT, HAMMER_RESULT_AMOUNT)
        .uniqueOutputId(HAMMER_RESULT);

    // Compressed hammer
    public static final RecipeKey<OutputItem> COMPRESSED_HAMMER_RESULT = ItemIdComponent.RESULT.key("result");
    public static final RecipeKey<InputItem> COMPRESSED_HAMMER_INGREDIENT = ItemComponents.INPUT.key("ingredient");
    public static final RecipeKey<NumberProvider> COMPRESSED_HAMMER_RESULT_AMOUNT = NumberProviderComponent.NUMBER_PROVIDER.key("result_amount").preferred("resultAmount").optional(ONE).alwaysWrite();

    public static final RecipeSchema COMPRESSED_HAMMER = new RecipeSchema(ProbabilityKubeRecipe.class, () -> new ProbabilityKubeRecipe(COMPRESSED_HAMMER_RESULT_AMOUNT),
        COMPRESSED_HAMMER_RESULT, COMPRESSED_HAMMER_INGREDIENT, COMPRESSED_HAMMER_RESULT_AMOUNT)
        .constructor(COMPRESSED_HAMMER_RESULT, COMPRESSED_HAMMER_INGREDIENT)
        .constructor(COMPRESSED_HAMMER_RESULT, COMPRESSED_HAMMER_INGREDIENT, COMPRESSED_HAMMER_RESULT_AMOUNT)
        .uniqueOutputId(COMPRESSED_HAMMER_RESULT);

    // Crook
    public static final RecipeKey<OutputItem> CROOK_RESULT = ItemIdComponent.RESULT.key("result");
    public static final RecipeKey<BlockPredicate> CROOK_BLOCK_PREDICATE = BlockPredicateComponent.BLOCK_PREDICATE.key("block_predicate").preferred("blockPredicate");
    public static final RecipeKey<Float> CROOK_CHANCE = NumberComponent.FLOAT.key("chance").optional(1F).alwaysWrite();

    public static final RecipeSchema CROOK = new RecipeSchema(BlockPredicateKubeRecipe.class, () -> new BlockPredicateKubeRecipe(CROOK_BLOCK_PREDICATE),
        CROOK_RESULT, CROOK_BLOCK_PREDICATE, CROOK_CHANCE)
        .constructor(CROOK_RESULT, CROOK_BLOCK_PREDICATE)
        .constructor(CROOK_RESULT, CROOK_BLOCK_PREDICATE, CROOK_CHANCE)
        .uniqueOutputId(CROOK_RESULT);

    // Crucible heat source
    public static final RecipeKey<BlockPredicate> HEAT_BLOCK_PREDICATE = BlockPredicateComponent.BLOCK_PREDICATE.key("block_predicate").preferred("blockPredicate");
    public static final RecipeKey<Integer> HEAT_VALUE = NumberComponent.INT.key("heat_value").preferred("heatValue");

    public static final RecipeSchema CRUCIBLE_HEAT_SOURCE = new RecipeSchema(BlockPredicateKubeRecipe.class, () -> new BlockPredicateKubeRecipe(HEAT_BLOCK_PREDICATE),
        HEAT_BLOCK_PREDICATE, HEAT_VALUE)
        .constructor(HEAT_BLOCK_PREDICATE, HEAT_VALUE);

    // Lava crucible
    public static final RecipeKey<OutputFluid> LAVA_CRUCIBLE_FLUID = FluidComponents.OUTPUT.key("fluid");
    public static final RecipeKey<InputItem> LAVA_CRUCIBLE_INGREDIENT = ItemComponents.INPUT.key("ingredient");

    public static final RecipeSchema LAVA_CRUCIBLE = new RecipeSchema(LAVA_CRUCIBLE_FLUID, LAVA_CRUCIBLE_INGREDIENT)
        .constructor(LAVA_CRUCIBLE_FLUID, LAVA_CRUCIBLE_INGREDIENT);

    // Water crucible
    public static final RecipeKey<OutputFluid> WATER_CRUCIBLE_FLUID = FluidComponents.OUTPUT.key("fluid");
    public static final RecipeKey<InputItem> WATER_CRUCIBLE_INGREDIENT = ItemComponents.INPUT.key("ingredient");

    public static final RecipeSchema WATER_CRUCIBLE = new RecipeSchema(WATER_CRUCIBLE_FLUID, WATER_CRUCIBLE_INGREDIENT)
        .constructor(WATER_CRUCIBLE_FLUID, WATER_CRUCIBLE_INGREDIENT);

    // Barrel compost
    public static final RecipeKey<InputItem> COMPOST_INGREDIENT = ItemComponents.INPUT.key("ingredient");
    public static final RecipeKey<Integer> COMPOST_VOLUME = NumberComponent.INT.key("volume");

    public static final RecipeSchema BARREL_COMPOST = new RecipeSchema(COMPOST_INGREDIENT, COMPOST_VOLUME)
        .constructor(COMPOST_INGREDIENT, COMPOST_VOLUME);

    // Barrel mixing: the fluid is written as "fluid" and "fluid_amount"
    public static final RecipeKey<OutputItem> MIXING_RESULT = ItemIdComponent.RESULT.key("result");
    public static final RecipeKey<InputItem> MIXING_INGREDIENT = ItemComponents.INPUT.key("ingredient");
    public static final RecipeKey<FluidStackJS> MIXING_FLUID = FluidAmountComponent.FLUID_AMOUNT.key("fluid");

    public static final RecipeSchema BARREL_MIXING = new RecipeSchema(MIXING_RESULT, MIXING_INGREDIENT, MIXING_FLUID)
        .constructor(MIXING_RESULT, MIXING_INGREDIENT, MIXING_FLUID)
        .uniqueOutputId(MIXING_RESULT);

    // Barrel fluid mixing: the base fluid is written as "base_fluid" and "base_fluid_amount"
    public static final RecipeKey<OutputItem> FLUID_MIXING_RESULT = ItemIdComponent.RESULT.key("result");
    public static final RecipeKey<FluidStackJS> FLUID_MIXING_BASE = FluidAmountComponent.FLUID_AMOUNT.key("base_fluid").preferred("baseFluid");
    public static final RecipeKey<Fluid> FLUID_MIXING_ADDITIVE = FluidComponent.INPUT.key("additive_fluid").preferred("additiveFluid");
    public static final RecipeKey<Boolean> FLUID_MIXING_CONSUMES_ADDITIVE = BooleanComponent.BOOLEAN.key("consumes_additive").preferred("consumesAdditive").optional(Boolean.FALSE).alwaysWrite();

    public static final RecipeSchema BARREL_FLUID_MIXING = new RecipeSchema(FLUID_MIXING_RESULT, FLUID_MIXING_BASE, FLUID_MIXING_ADDITIVE, FLUID_MIXING_CONSUMES_ADDITIVE)
        .constructor(FLUID_MIXING_RESULT, FLUID_MIXING_BASE, FLUID_MIXING_ADDITIVE)
        .constructor(FLUID_MIXING_RESULT, FLUID_MIXING_BASE, FLUID_MIXING_ADDITIVE, FLUID_MIXING_CONSUMES_ADDITIVE)
        .uniqueOutputId(FLUID_MIXING_RESULT);

    // Barrel fluid transformation
    public static final RecipeKey<Fluid> TRANSFORM_RESULT_FLUID = FluidComponent.OUTPUT.key("result_fluid").preferred("resultFluid");
    public static final RecipeKey<Fluid> TRANSFORM_BASE_FLUID = FluidComponent.INPUT.key("base_fluid").preferred("baseFluid");
    public static final RecipeKey<BlockPredicate> TRANSFORM_CATALYST = BlockPredicateComponent.BLOCK_PREDICATE.key("catalyst");
    public static final RecipeKey<Integer> TRANSFORM_DURATION = NumberComponent.INT.key("duration");
    public static final RecipeKey<Integer> TRANSFORM_RESULT_COLOR = NumberComponent.INT.key("result_color").preferred("resultColor").optional(0xFFFFFF).alwaysWrite();
    public static final RecipeKey<WeightedList<BlockState>> TRANSFORM_BYPRODUCTS = WeightedBlockStatesComponent.WEIGHTED_BLOCK_STATES.key("byproducts").optional(WeightedBlockStatesComponent.empty()).alwaysWrite();

    public static final RecipeSchema BARREL_FLUID_TRANSFORMATION = new RecipeSchema(BlockPredicateKubeRecipe.class, () -> new BlockPredicateKubeRecipe(TRANSFORM_CATALYST),
        TRANSFORM_RESULT_FLUID, TRANSFORM_BASE_FLUID, TRANSFORM_CATALYST, TRANSFORM_DURATION, TRANSFORM_RESULT_COLOR, TRANSFORM_BYPRODUCTS)
        .constructor(TRANSFORM_RESULT_FLUID, TRANSFORM_BASE_FLUID, TRANSFORM_CATALYST, TRANSFORM_DURATION)
        .constructor(TRANSFORM_RESULT_FLUID, TRANSFORM_BASE_FLUID, TRANSFORM_CATALYST, TRANSFORM_DURATION, TRANSFORM_RESULT_COLOR)
        .constructor(TRANSFORM_RESULT_FLUID, TRANSFORM_BASE_FLUID, TRANSFORM_CATALYST, TRANSFORM_DURATION, TRANSFORM_RESULT_COLOR, TRANSFORM_BYPRODUCTS);

    // Ore chunk: serialized as an exdeorum:tag_result 2x2 shaped recipe (see OreChunkKubeRecipe)
    public static final RecipeKey<TagKey<Item>> ORE_CHUNK_ORE = TagKeyComponent.ITEM.key("ore");
    public static final RecipeKey<InputItem> ORE_CHUNK_INPUT = ItemComponents.INPUT.key("ore_chunk").preferred("oreChunk");

    public static final RecipeSchema ORE_CHUNK = new RecipeSchema(OreChunkKubeRecipe.class, () -> new OreChunkKubeRecipe(ORE_CHUNK_ORE, ORE_CHUNK_INPUT),
        ORE_CHUNK_ORE, ORE_CHUNK_INPUT)
        .constructor(ORE_CHUNK_ORE, ORE_CHUNK_INPUT);

    private ExDeorumSchemas() {
    }

    /**
     * Registers all thirteen schemas under the {@code exdeorum} namespace.
     * <p>
     * Ex Deorum registers its own minimal {@code exdeorum:sieve} schema from its own KubeJS plugin and
     * plugin order is not guaranteed, so the namespace is replaced by one that refuses to overwrite the
     * schemas registered here. Whichever plugin runs first or last, ours stays.
     */
    public static void register(RegisterRecipeSchemasEvent event) {
        OwnedNamespace namespace = new OwnedNamespace(ExDeorumKJS.EXDEORUM_ID);
        RecipeNamespace existing = event.namespaces().get(ExDeorumKJS.EXDEORUM_ID);

        if (existing instanceof OwnedNamespace owned) {
            namespace = owned;
        } else {
            if (existing != null) {
                namespace.putAll(existing);
            }

            event.namespaces().put(ExDeorumKJS.EXDEORUM_ID, namespace);
        }

        namespace.own("sieve", SIEVE);
        namespace.own("compressed_sieve", COMPRESSED_SIEVE);
        namespace.own("hammer", HAMMER);
        namespace.own("compressed_hammer", COMPRESSED_HAMMER);
        namespace.own("crook", CROOK);
        namespace.own("crucible_heat_source", CRUCIBLE_HEAT_SOURCE);
        namespace.own("lava_crucible", LAVA_CRUCIBLE);
        namespace.own("water_crucible", WATER_CRUCIBLE);
        namespace.own("barrel_compost", BARREL_COMPOST);
        namespace.own("barrel_mixing", BARREL_MIXING);
        namespace.own("barrel_fluid_mixing", BARREL_FLUID_MIXING);
        namespace.own("barrel_fluid_transformation", BARREL_FLUID_TRANSFORMATION);
        namespace.ownOreChunk("ore_chunk", ORE_CHUNK);
    }

    /**
     * Marker for a schema type registered by this mod.
     */
    private static class OwnedType extends RecipeSchemaType {
        OwnedType(RecipeNamespace namespace, String name, RecipeSchema schema) {
            super(namespace, ExDeorumKJS.exdeorum(name), schema);
        }
    }

    /**
     * Ore chunk has no serializer of its own, so it borrows the one of {@code exdeorum:tag_result}.
     */
    private static final class OreChunkType extends OwnedType {
        OreChunkType(RecipeNamespace namespace, String name, RecipeSchema schema) {
            super(namespace, name, schema);
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            RecipeSerializer<?> serializer = RegistryInfo.RECIPE_SERIALIZER.getValue(OreChunkKubeRecipe.TAG_RESULT);

            if (serializer == null) {
                throw new IllegalStateException("The recipe serializer " + OreChunkKubeRecipe.TAG_RESULT + " is not registered");
            }

            return serializer;
        }
    }

    /**
     * Namespace that ignores later attempts by other plugins to replace the schemas owned by this mod.
     */
    private static final class OwnedNamespace extends RecipeNamespace {
        private final Map<String, RecipeSchemaType> owned = new LinkedHashMap<>();

        OwnedNamespace(String name) {
            super(name);
        }

        void own(String id, RecipeSchema schema) {
            lock(id, new OwnedType(this, id, schema));
        }

        void ownOreChunk(String id, RecipeSchema schema) {
            lock(id, new OreChunkType(this, id, schema));
        }

        private void lock(String id, RecipeSchemaType type) {
            owned.put(id, type);
            super.put(id, type);
        }

        @Override
        public RecipeSchemaType put(String key, RecipeSchemaType value) {
            RecipeSchemaType ours = owned.get(key);

            if (ours != null && value != ours) {
                return ours;
            }

            return super.put(key, value);
        }
    }
}
