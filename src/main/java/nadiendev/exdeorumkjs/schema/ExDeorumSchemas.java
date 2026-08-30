package nadiendev.exdeorumkjs.schema;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.FluidIngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.FluidStackComponent;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.SizedFluidIngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.TagKeyComponent;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import nadiendev.exdeorumkjs.ExDeorumKJS;
import nadiendev.exdeorumkjs.component.BlockPredicateComponent;
import nadiendev.exdeorumkjs.component.FluidComponent;
import nadiendev.exdeorumkjs.component.NumberProviderComponent;
import nadiendev.exdeorumkjs.component.WeightedBlockStatesComponent;
import nadiendev.exdeorumkjs.recipe.BlockPredicateKubeRecipe;
import nadiendev.exdeorumkjs.recipe.ProbabilityKubeRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import thedarkcolour.exdeorum.recipe.BlockPredicate;
import thedarkcolour.exdeorum.recipe.WeightedList;

public interface ExDeorumSchemas {
    NumberProvider ONE = ConstantValue.exactly(1F);

    static KubeRecipeFactory probability(String name, RecipeKey<NumberProvider> key) {
        return new KubeRecipeFactory(ExDeorumKJS.id(name), ProbabilityKubeRecipe.class, () -> new ProbabilityKubeRecipe(key));
    }

    static KubeRecipeFactory blockPredicate(String name, RecipeKey<BlockPredicate> key) {
        return new KubeRecipeFactory(ExDeorumKJS.id(name), BlockPredicateKubeRecipe.class, () -> new BlockPredicateKubeRecipe(key));
    }

    RecipeKey<ItemStack> SIEVE_RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT);
    RecipeKey<Ingredient> SIEVE_INGREDIENT = IngredientComponent.INGREDIENT.key("ingredient", ComponentRole.INPUT);
    RecipeKey<Ingredient> SIEVE_MESH = IngredientComponent.INGREDIENT.key("mesh", ComponentRole.INPUT);
    RecipeKey<NumberProvider> SIEVE_RESULT_AMOUNT = NumberProviderComponent.NUMBER_PROVIDER.key("result_amount", ComponentRole.OTHER).optional(ONE);
    RecipeKey<Boolean> SIEVE_BY_HAND_ONLY = BooleanComponent.BOOLEAN.key("by_hand_only", ComponentRole.OTHER).optional(Boolean.FALSE);

    RecipeSchema SIEVE = new RecipeSchema(SIEVE_RESULT, SIEVE_INGREDIENT, SIEVE_MESH, SIEVE_RESULT_AMOUNT, SIEVE_BY_HAND_ONLY)
        .factory(probability("sieve", SIEVE_RESULT_AMOUNT))
        .constructor(SIEVE_RESULT, SIEVE_INGREDIENT, SIEVE_MESH)
        .constructor(SIEVE_RESULT, SIEVE_INGREDIENT, SIEVE_MESH, SIEVE_RESULT_AMOUNT)
        .constructor(SIEVE_RESULT, SIEVE_INGREDIENT, SIEVE_MESH, SIEVE_RESULT_AMOUNT, SIEVE_BY_HAND_ONLY)
        .uniqueId(SIEVE_RESULT);

    RecipeKey<ItemStack> COMPRESSED_SIEVE_RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT);
    RecipeKey<Ingredient> COMPRESSED_SIEVE_INGREDIENT = IngredientComponent.INGREDIENT.key("ingredient", ComponentRole.INPUT);
    RecipeKey<Ingredient> COMPRESSED_SIEVE_MESH = IngredientComponent.INGREDIENT.key("mesh", ComponentRole.INPUT);
    RecipeKey<NumberProvider> COMPRESSED_SIEVE_RESULT_AMOUNT = NumberProviderComponent.NUMBER_PROVIDER.key("result_amount", ComponentRole.OTHER).optional(ONE);
    RecipeKey<Boolean> COMPRESSED_SIEVE_BY_HAND_ONLY = BooleanComponent.BOOLEAN.key("by_hand_only", ComponentRole.OTHER).optional(Boolean.FALSE);

    RecipeSchema COMPRESSED_SIEVE = new RecipeSchema(COMPRESSED_SIEVE_RESULT, COMPRESSED_SIEVE_INGREDIENT, COMPRESSED_SIEVE_MESH, COMPRESSED_SIEVE_RESULT_AMOUNT, COMPRESSED_SIEVE_BY_HAND_ONLY)
        .factory(probability("compressed_sieve", COMPRESSED_SIEVE_RESULT_AMOUNT))
        .constructor(COMPRESSED_SIEVE_RESULT, COMPRESSED_SIEVE_INGREDIENT, COMPRESSED_SIEVE_MESH)
        .constructor(COMPRESSED_SIEVE_RESULT, COMPRESSED_SIEVE_INGREDIENT, COMPRESSED_SIEVE_MESH, COMPRESSED_SIEVE_RESULT_AMOUNT)
        .constructor(COMPRESSED_SIEVE_RESULT, COMPRESSED_SIEVE_INGREDIENT, COMPRESSED_SIEVE_MESH, COMPRESSED_SIEVE_RESULT_AMOUNT, COMPRESSED_SIEVE_BY_HAND_ONLY)
        .uniqueId(COMPRESSED_SIEVE_RESULT);

    RecipeKey<ItemStack> HAMMER_RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT);
    RecipeKey<Ingredient> HAMMER_INGREDIENT = IngredientComponent.INGREDIENT.key("ingredient", ComponentRole.INPUT);
    RecipeKey<NumberProvider> HAMMER_RESULT_AMOUNT = NumberProviderComponent.NUMBER_PROVIDER.key("result_amount", ComponentRole.OTHER).optional(ONE);

    RecipeSchema HAMMER = new RecipeSchema(HAMMER_RESULT, HAMMER_INGREDIENT, HAMMER_RESULT_AMOUNT)
        .factory(probability("hammer", HAMMER_RESULT_AMOUNT))
        .constructor(HAMMER_RESULT, HAMMER_INGREDIENT)
        .constructor(HAMMER_RESULT, HAMMER_INGREDIENT, HAMMER_RESULT_AMOUNT)
        .uniqueId(HAMMER_RESULT);

    RecipeKey<ItemStack> COMPRESSED_HAMMER_RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT);
    RecipeKey<Ingredient> COMPRESSED_HAMMER_INGREDIENT = IngredientComponent.INGREDIENT.key("ingredient", ComponentRole.INPUT);
    RecipeKey<NumberProvider> COMPRESSED_HAMMER_RESULT_AMOUNT = NumberProviderComponent.NUMBER_PROVIDER.key("result_amount", ComponentRole.OTHER).optional(ONE);

    RecipeSchema COMPRESSED_HAMMER = new RecipeSchema(COMPRESSED_HAMMER_RESULT, COMPRESSED_HAMMER_INGREDIENT, COMPRESSED_HAMMER_RESULT_AMOUNT)
        .factory(probability("compressed_hammer", COMPRESSED_HAMMER_RESULT_AMOUNT))
        .constructor(COMPRESSED_HAMMER_RESULT, COMPRESSED_HAMMER_INGREDIENT)
        .constructor(COMPRESSED_HAMMER_RESULT, COMPRESSED_HAMMER_INGREDIENT, COMPRESSED_HAMMER_RESULT_AMOUNT)
        .uniqueId(COMPRESSED_HAMMER_RESULT);

    RecipeKey<ItemStack> CROOK_RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT);
    RecipeKey<BlockPredicate> CROOK_BLOCK_PREDICATE = BlockPredicateComponent.BLOCK_PREDICATE.key("block_predicate", ComponentRole.INPUT);
    RecipeKey<Float> CROOK_CHANCE = NumberComponent.FLOAT.key("chance", ComponentRole.OTHER).optional(1F);

    RecipeSchema CROOK = new RecipeSchema(CROOK_RESULT, CROOK_BLOCK_PREDICATE, CROOK_CHANCE)
        .factory(blockPredicate("crook", CROOK_BLOCK_PREDICATE))
        .constructor(CROOK_RESULT, CROOK_BLOCK_PREDICATE)
        .constructor(CROOK_RESULT, CROOK_BLOCK_PREDICATE, CROOK_CHANCE)
        .uniqueId(CROOK_RESULT);

    RecipeKey<BlockPredicate> HEAT_BLOCK_PREDICATE = BlockPredicateComponent.BLOCK_PREDICATE.key("block_predicate", ComponentRole.INPUT);
    RecipeKey<Integer> HEAT_VALUE = NumberComponent.INT.key("heat_value", ComponentRole.OTHER);

    RecipeSchema CRUCIBLE_HEAT_SOURCE = new RecipeSchema(HEAT_BLOCK_PREDICATE, HEAT_VALUE)
        .factory(blockPredicate("crucible_heat_source", HEAT_BLOCK_PREDICATE))
        .constructor(HEAT_BLOCK_PREDICATE, HEAT_VALUE);

    RecipeKey<FluidStack> LAVA_CRUCIBLE_FLUID = FluidStackComponent.FLUID_STACK.key("fluid", ComponentRole.OUTPUT);
    RecipeKey<Ingredient> LAVA_CRUCIBLE_INGREDIENT = IngredientComponent.INGREDIENT.key("ingredient", ComponentRole.INPUT);

    RecipeSchema LAVA_CRUCIBLE = new RecipeSchema(LAVA_CRUCIBLE_FLUID, LAVA_CRUCIBLE_INGREDIENT)
        .constructor(LAVA_CRUCIBLE_FLUID, LAVA_CRUCIBLE_INGREDIENT);

    RecipeKey<FluidStack> WATER_CRUCIBLE_FLUID = FluidStackComponent.FLUID_STACK.key("fluid", ComponentRole.OUTPUT);
    RecipeKey<Ingredient> WATER_CRUCIBLE_INGREDIENT = IngredientComponent.INGREDIENT.key("ingredient", ComponentRole.INPUT);

    RecipeSchema WATER_CRUCIBLE = new RecipeSchema(WATER_CRUCIBLE_FLUID, WATER_CRUCIBLE_INGREDIENT)
        .constructor(WATER_CRUCIBLE_FLUID, WATER_CRUCIBLE_INGREDIENT);

    RecipeKey<Ingredient> COMPOST_INGREDIENT = IngredientComponent.INGREDIENT.key("ingredient", ComponentRole.INPUT);
    RecipeKey<Integer> COMPOST_VOLUME = NumberComponent.INT.key("volume", ComponentRole.OTHER);

    RecipeSchema BARREL_COMPOST = new RecipeSchema(COMPOST_INGREDIENT, COMPOST_VOLUME)
        .constructor(COMPOST_INGREDIENT, COMPOST_VOLUME);

    RecipeKey<ItemStack> MIXING_RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT);
    RecipeKey<Ingredient> MIXING_INGREDIENT = IngredientComponent.INGREDIENT.key("ingredient", ComponentRole.INPUT);
    RecipeKey<SizedFluidIngredient> MIXING_FLUID = SizedFluidIngredientComponent.FLAT.key("fluid", ComponentRole.INPUT);

    RecipeSchema BARREL_MIXING = new RecipeSchema(MIXING_RESULT, MIXING_INGREDIENT, MIXING_FLUID)
        .constructor(MIXING_RESULT, MIXING_INGREDIENT, MIXING_FLUID)
        .uniqueId(MIXING_RESULT);

    RecipeKey<ItemStack> FLUID_MIXING_RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT);
    RecipeKey<SizedFluidIngredient> FLUID_MIXING_BASE = SizedFluidIngredientComponent.FLAT.key("base_fluid", ComponentRole.INPUT);
    RecipeKey<FluidIngredient> FLUID_MIXING_ADDITIVE = FluidIngredientComponent.FLUID_INGREDIENT.key("additive_fluid", ComponentRole.INPUT);
    RecipeKey<Boolean> FLUID_MIXING_CONSUMES_ADDITIVE = BooleanComponent.BOOLEAN.key("consumes_additive", ComponentRole.OTHER).optional(Boolean.FALSE);

    RecipeSchema BARREL_FLUID_MIXING = new RecipeSchema(FLUID_MIXING_RESULT, FLUID_MIXING_BASE, FLUID_MIXING_ADDITIVE, FLUID_MIXING_CONSUMES_ADDITIVE)
        .constructor(FLUID_MIXING_RESULT, FLUID_MIXING_BASE, FLUID_MIXING_ADDITIVE)
        .constructor(FLUID_MIXING_RESULT, FLUID_MIXING_BASE, FLUID_MIXING_ADDITIVE, FLUID_MIXING_CONSUMES_ADDITIVE)
        .uniqueId(FLUID_MIXING_RESULT);

    RecipeKey<Fluid> TRANSFORM_RESULT_FLUID = FluidComponent.FLUID.key("result_fluid", ComponentRole.OUTPUT);
    RecipeKey<FluidIngredient> TRANSFORM_BASE_FLUID = FluidIngredientComponent.FLUID_INGREDIENT.key("base_fluid", ComponentRole.INPUT);
    RecipeKey<BlockPredicate> TRANSFORM_CATALYST = BlockPredicateComponent.BLOCK_PREDICATE.key("catalyst", ComponentRole.INPUT);
    RecipeKey<Integer> TRANSFORM_DURATION = NumberComponent.INT.key("duration", ComponentRole.OTHER);
    RecipeKey<Integer> TRANSFORM_RESULT_COLOR = NumberComponent.INT.key("result_color", ComponentRole.OTHER).optional(0xFFFFFF);
    RecipeKey<WeightedList<BlockState>> TRANSFORM_BYPRODUCTS = WeightedBlockStatesComponent.WEIGHTED_BLOCK_STATES.key("byproducts", ComponentRole.OTHER).optional(WeightedList.empty());

    RecipeSchema BARREL_FLUID_TRANSFORMATION = new RecipeSchema(TRANSFORM_RESULT_FLUID, TRANSFORM_BASE_FLUID, TRANSFORM_CATALYST, TRANSFORM_DURATION, TRANSFORM_RESULT_COLOR, TRANSFORM_BYPRODUCTS)
        .factory(blockPredicate("barrel_fluid_transformation", TRANSFORM_CATALYST))
        .constructor(TRANSFORM_RESULT_FLUID, TRANSFORM_BASE_FLUID, TRANSFORM_CATALYST, TRANSFORM_DURATION)
        .constructor(TRANSFORM_RESULT_FLUID, TRANSFORM_BASE_FLUID, TRANSFORM_CATALYST, TRANSFORM_DURATION, TRANSFORM_RESULT_COLOR)
        .constructor(TRANSFORM_RESULT_FLUID, TRANSFORM_BASE_FLUID, TRANSFORM_CATALYST, TRANSFORM_DURATION, TRANSFORM_RESULT_COLOR, TRANSFORM_BYPRODUCTS);

    RecipeKey<TagKey<Item>> ORE_CHUNK_ORE = TagKeyComponent.ITEM.key("ore", ComponentRole.OUTPUT);
    RecipeKey<Ingredient> ORE_CHUNK_INPUT = IngredientComponent.INGREDIENT.key("ore_chunk", ComponentRole.INPUT);

    RecipeSchema ORE_CHUNK = new RecipeSchema(ORE_CHUNK_ORE, ORE_CHUNK_INPUT)
        .constructor(ORE_CHUNK_ORE, ORE_CHUNK_INPUT);
}
