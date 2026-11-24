package nadiendev.exdeorumkjs.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import thedarkcolour.exdeorum.item.MeshItem;
import thedarkcolour.exdeorum.tag.EItemTags;

import java.util.List;
import java.util.Map;

public class MeshItemBuilder extends ItemBuilder implements ExDeorumRecipeGen.Source {
    private transient String material;
    private transient String binder = "#forge:string";

    public MeshItemBuilder(ResourceLocation id) {
        super(id);
        unstackable();
        tag(EItemTags.SIEVE_MESHES.location());
        ExDeorumRecipeGen.add(this);
    }

    /**
     * Generates the vanilla-style mesh crafting recipe from this material, laid out the way
     * Ex Deorum's own meshes are. Without it no crafting recipe is generated.
     */
    public MeshItemBuilder material(String material) {
        this.material = material;
        return this;
    }

    public MeshItemBuilder binder(String binder) {
        this.binder = binder;
        return this;
    }

    public MeshItemBuilder noRecipe() {
        this.material = null;
        return this;
    }

    @Override
    public void generateRecipes(Map<ResourceLocation, JsonElement> out) {
        if (material == null) {
            return;
        }

        JsonObject key = new JsonObject();
        key.add("#", ExDeorumRecipeGen.ingredient(material));
        key.add("S", ExDeorumRecipeGen.ingredient(binder));

        out.put(ExDeorumRecipeGen.recipeId(id, ""), ExDeorumRecipeGen.shaped("misc", key, List.of("S#S", "#S#", "S#S"), id));
    }

    @Override
    public Item createObject() {
        return new MeshItem(createItemProperties());
    }
}
