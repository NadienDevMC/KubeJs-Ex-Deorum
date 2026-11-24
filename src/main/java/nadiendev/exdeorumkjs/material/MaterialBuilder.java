package nadiendev.exdeorumkjs.material;

import com.google.gson.JsonObject;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.MapColor;

import java.util.Locale;

public class MaterialBuilder {
    public final String name;
    public final JsonObject json = new JsonObject();
    public String mineableWith = null;
    public String craftMain = null;
    public String craftSlab = null;
    public String craftRod = "#forge:rods/wooden";
    public String craftBinder = "#forge:ingots/iron";

    public MaterialBuilder(String name) {
        this.name = name;
        json.addProperty("sound_type", "wood");
        json.addProperty("strength", 2.0F);
    }

    public MaterialBuilder soundType(String soundType) {
        json.addProperty("sound_type", soundType);
        return this;
    }

    public MaterialBuilder strength(float strength) {
        json.addProperty("strength", strength);
        return this;
    }

    public MaterialBuilder mapColor(Object mapColor) {
        json.addProperty("map_color", resolveMapColor(mapColor));
        return this;
    }

    /**
     * Generates the crafting recipe, laid out the way Ex Deorum lays out its own for this kind of
     * block. {@code main} is the bulk material - planks for a sieve or barrel, a log for a crucible
     * or compressed sieve - and {@code slab} is the slab that goes in the middle.
     */
    public MaterialBuilder craftedFrom(String main) {
        this.craftMain = main;
        return this;
    }

    public MaterialBuilder craftedFrom(String main, String slab) {
        this.craftMain = main;
        this.craftSlab = slab;
        return this;
    }

    public MaterialBuilder rod(String rod) {
        this.craftRod = rod;
        return this;
    }

    public MaterialBuilder binder(String binder) {
        this.craftBinder = binder;
        return this;
    }

    public MaterialBuilder noRecipe() {
        this.craftMain = null;
        this.craftSlab = null;
        return this;
    }

    public MaterialBuilder mineableWith(String tool) {
        this.mineableWith = tool;
        return this;
    }

    public MaterialBuilder needsCorrectTool(boolean value) {
        json.addProperty("needs_correct_tool", value);
        return this;
    }

    public MaterialBuilder fireproof(boolean value) {
        json.addProperty("fireproof", value);
        return this;
    }

    public MaterialBuilder transparent(boolean value) {
        json.addProperty("transparent", value);
        return this;
    }

    public MaterialBuilder requiredMod(String modId) {
        json.addProperty("required_mod_id", modId);
        return this;
    }

    private static int resolveMapColor(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }

        if (value instanceof MapColor color) {
            return color.id;
        }

        String name = String.valueOf(value).trim();

        try {
            return Integer.parseInt(name);
        } catch (NumberFormatException ignored) {
        }

        String upper = name.toUpperCase(Locale.ROOT);

        try {
            if (MapColor.class.getField(upper).get(null) instanceof MapColor color) {
                return color.id;
            }
        } catch (ReflectiveOperationException ignored) {
        }

        DyeColor dye = DyeColor.byName(name.toLowerCase(Locale.ROOT), null);

        if (dye != null) {
            return dye.getMapColor().id;
        }

        throw new IllegalArgumentException("Unknown map color '" + name + "' - use a MapColor name such as 'stone' or 'color_black', a dye colour name, or a numeric id");
    }
}
