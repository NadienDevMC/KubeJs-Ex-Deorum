# Ex Deorum KJS — Recipe Types

This addon (**Ex Deorum KJS**) exposes every Ex Deorum recipe type to KubeJS, so you can create them
from a script instead of writing the JSON by hand.

All the recipe scripts below go in the `kubejs/server_scripts` folder. A ready-to-run script with
one working example of every recipe type lives in [`examples/exdeorum_examples.js`](examples/exdeorum_examples.js)
(and in `run/kubejs/server_scripts/` for the dev environment) — none of those recipes exist in
Ex Deorum, so dropping the file in adds new content rather than overwriting anything.

> **Why this addon?** Ex Deorum ships a built-in KubeJS schema for `exdeorum:sieve`, but it only
> knows about `result` and `ingredient` — it has no `mesh` and no `result_amount`, so it cannot
> produce a valid sieve recipe. This addon replaces that schema with a complete one and adds the
> twelve recipe types Ex Deorum does not expose at all.

---

## Table of contents

| Recipe type | Function |
|---|---|
| Sieve | `event.recipes.exdeorum.sieve(...)` |
| Compressed sieve | `event.recipes.exdeorum.compressed_sieve(...)` |
| Hammer | `event.recipes.exdeorum.hammer(...)` |
| Compressed hammer | `event.recipes.exdeorum.compressed_hammer(...)` |
| Crook | `event.recipes.exdeorum.crook(...)` |
| Crucible heat source | `event.recipes.exdeorum.crucible_heat_source(...)` |
| Lava crucible | `event.recipes.exdeorum.lava_crucible(...)` |
| Water crucible | `event.recipes.exdeorum.water_crucible(...)` |
| Barrel compost | `event.recipes.exdeorum.barrel_compost(...)` |
| Barrel mixing | `event.recipes.exdeorum.barrel_mixing(...)` |
| Barrel fluid mixing | `event.recipes.exdeorum.barrel_fluid_mixing(...)` |
| Barrel fluid transformation | `event.recipes.exdeorum.barrel_fluid_transformation(...)` |
| Ore chunk | `event.recipes.exdeorum.ore_chunk(...)` |

---

## Shared value types

### `resultAmount` — number provider

Sieve and hammer recipes roll their output count from a vanilla loot **number provider**. The
clearest way to set it is a chained method on the recipe:

| Call | Meaning |
|---|---|
| `.constant(4)` | always 4 |
| `.chance(0.25)` | one roll at 25% — 0 or 1 |
| `.uniform(1, 3)` | a flat roll between 1 and 3 |
| `.binomial(4, 0.25)` | **4** rolls, each at **25%** — anywhere from 0 to 4 |

In `binomial(n, p)`, `n` is how many times it rolls and `p` is the chance of each roll — that is how
one sieve action can drop several items. If all you want is a plain drop chance, that is `n = 1`,
which is exactly what `.chance(p)` does for you.

They chain freely and anything else can follow, including `.id(...)`:

```javascript
event.recipes.exdeorum.sieve(result, ingredient, mesh).binomial(3, 0.2).id('mypack:my_recipe')
event.recipes.exdeorum.sieve(result, ingredient, mesh).chance(0.04).byHandOnly(true)
```

You can still pass the amount as a positional argument instead — a plain number,
`{ type: 'minecraft:binomial', n: 4, p: 0.25 }`, or `ExDeorumKJS.binomial(4, 0.25)`.
If you leave it out entirely it defaults to exactly `1`.

### `blockPredicate` — block matcher

Crook, crucible heat source and fluid transformation recipes match against a **block predicate**.
To pin it to a block state, chain the property onto the recipe:

```javascript
event.recipes.exdeorum.crucible_heat_source('minecraft:furnace', 4).lit(true)
event.recipes.exdeorum.crook('minecraft:stick', '#minecraft:leaves', 0.1).state('distance', 1)
```

Named shortcuts: `.lit()`, `.powered()`, `.open()`, `.waterlogged()`, `.axis()`, `.facing()`,
`.half()`, `.age()`, `.level()`. For any other property use `.state(property, value)`. These only
work on a single block — a block *tag* has no state to constrain.

The predicate argument itself accepts:

| Form | Meaning |
|---|---|
| `'minecraft:magma_block'` | that exact block, any state |
| `'#minecraft:leaves'` | any block in that block tag |
| `'minecraft:furnace[lit=true]'` | that block, restricted to those state properties |
| `{ block: 'minecraft:furnace', state: { lit: 'true' } }` | same, in raw JSON form |
| `{ block_tag: 'minecraft:leaves' }` | same as `'#minecraft:leaves'` |
| `ExDeorumKJS.blockTag('minecraft:leaves')` | same as `'#minecraft:leaves'` |

### Fluids

`Fluid.of('minecraft:water', 250)` builds a fluid stack with an amount. Where only a fluid *type*
is needed you can pass a plain id string such as `'minecraft:lava'`.

---

## Sieve
`exdeorum:sieve`

**KubeJS:**
```javascript
event.recipes.exdeorum.sieve(result, ingredient, mesh)
event.recipes.exdeorum.sieve(result, ingredient, mesh, resultAmount)
event.recipes.exdeorum.sieve(result, ingredient, mesh, resultAmount, byHandOnly)
```

**Example:**
```javascript
event.recipes.exdeorum.sieve('minecraft:prismarine_shard', 'minecraft:clay', 'exdeorum:string_mesh').binomial(3, 0.2)
event.recipes.exdeorum.sieve('minecraft:nautilus_shell', 'minecraft:clay', 'exdeorum:diamond_mesh').chance(0.04).byHandOnly(true)
```

| Field | Description |
|---|---|
| `result` | Output item stack |
| `ingredient` | Block item placed in the sieve |
| `mesh` | Mesh required in the sieve (`exdeorum:string_mesh`, `flint_mesh`, `iron_mesh`, `golden_mesh`, `diamond_mesh`, `netherite_mesh`) |
| `resultAmount` | Number provider rolled for the output count — defaults to `1` |
| `byHandOnly` | When `true` the drop only happens when a player sifts by hand, never in a mechanical sieve — defaults to `false` |

---

## Compressed sieve
`exdeorum:compressed_sieve`

Identical to the sieve, but for the compressed sieve block. Amounts are usually much larger because
one compressed block stands in for nine.

**KubeJS:**
```javascript
event.recipes.exdeorum.compressed_sieve(result, ingredient, mesh)
event.recipes.exdeorum.compressed_sieve(result, ingredient, mesh, resultAmount)
event.recipes.exdeorum.compressed_sieve(result, ingredient, mesh, resultAmount, byHandOnly)
```

**Example:**
```javascript
event.recipes.exdeorum.compressed_sieve('minecraft:amethyst_shard', '#exdeorum:compressed/cobblestone', 'exdeorum:iron_mesh').binomial(9, 0.12)
```

| Field | Description |
|---|---|
| `result` | Output item stack |
| `ingredient` | Compressed block item placed in the compressed sieve |
| `mesh` | Mesh required in the sieve |
| `resultAmount` | Number provider rolled for the output count — defaults to `1` |
| `byHandOnly` | Restrict the drop to sifting by hand — defaults to `false` |

---

## Hammer
`exdeorum:hammer`

**KubeJS:**
```javascript
event.recipes.exdeorum.hammer(result, ingredient)
event.recipes.exdeorum.hammer(result, ingredient, resultAmount)
```

**Example:**
```javascript
event.recipes.exdeorum.hammer('minecraft:amethyst_shard', 'minecraft:amethyst_block').constant(4)
event.recipes.exdeorum.hammer('minecraft:gravel', 'minecraft:tuff').uniform(1, 2)
```

| Field | Description |
|---|---|
| `result` | Output item stack |
| `ingredient` | Block broken with a hammer |
| `resultAmount` | Number provider rolled for the output count — defaults to `1` |

---

## Compressed hammer
`exdeorum:compressed_hammer`

**KubeJS:**
```javascript
event.recipes.exdeorum.compressed_hammer(result, ingredient)
event.recipes.exdeorum.compressed_hammer(result, ingredient, resultAmount)
```

**Example:**
```javascript
event.recipes.exdeorum.compressed_hammer('minecraft:coarse_dirt', '#exdeorum:compressed/dirt').constant(9)
```

| Field | Description |
|---|---|
| `result` | Output item stack |
| `ingredient` | Compressed block broken with a hammer |
| `resultAmount` | Number provider rolled for the output count — defaults to `1` |

---

## Crook
`exdeorum:crook`

**KubeJS:**
```javascript
event.recipes.exdeorum.crook(result, blockPredicate)
event.recipes.exdeorum.crook(result, blockPredicate, chance)
```

**Example:**
```javascript
event.recipes.exdeorum.crook('minecraft:brown_mushroom', 'minecraft:brown_mushroom_block', 0.3)
event.recipes.exdeorum.crook('minecraft:stick', '#minecraft:leaves', 0.1)
```

| Field | Description |
|---|---|
| `result` | Output item stack |
| `blockPredicate` | Block that drops the item when broken with a crook |
| `chance` | Drop chance, `0.0`–`1.0` — defaults to `1.0` |

---

## Crucible heat source
`exdeorum:crucible_heat_source`

Defines how fast a block underneath a crucible melts its contents. For reference, vanilla fire is
`5`, lava is `3` and a torch is `1`.

**KubeJS:**
```javascript
event.recipes.exdeorum.crucible_heat_source(blockPredicate, heatValue)
```

**Example:**
```javascript
event.recipes.exdeorum.crucible_heat_source('minecraft:magma_block', 3)
event.recipes.exdeorum.crucible_heat_source('minecraft:furnace', 4).lit(true)
```

| Field | Description |
|---|---|
| `blockPredicate` | Block placed under the crucible |
| `heatValue` | Heat units per tick contributed by that block |

---

## Lava crucible
`exdeorum:lava_crucible`

**KubeJS:**
```javascript
event.recipes.exdeorum.lava_crucible(fluid, ingredient)
```

**Example:**
```javascript
event.recipes.exdeorum.lava_crucible(Fluid.of('minecraft:lava', 500), 'minecraft:magma_block')
```

| Field | Description |
|---|---|
| `fluid` | Fluid stack produced by melting one input item |
| `ingredient` | Item melted in the lava crucible — must not depend on NBT |

---

## Water crucible
`exdeorum:water_crucible`

**KubeJS:**
```javascript
event.recipes.exdeorum.water_crucible(fluid, ingredient)
```

**Example:**
```javascript
event.recipes.exdeorum.water_crucible(Fluid.of('minecraft:water', 250), 'minecraft:dried_kelp_block')
```

| Field | Description |
|---|---|
| `fluid` | Fluid stack produced by squeezing one input item |
| `ingredient` | Item placed in the porcelain crucible — must not depend on NBT |

---

## Barrel compost
`exdeorum:barrel_compost`

**KubeJS:**
```javascript
event.recipes.exdeorum.barrel_compost(ingredient, volume)
```

**Example:**
```javascript
event.recipes.exdeorum.barrel_compost('minecraft:honeycomb', 100)
event.recipes.exdeorum.barrel_compost('minecraft:brown_mushroom_block', 250)
```

| Field | Description |
|---|---|
| `ingredient` | Item thrown into the barrel |
| `volume` | Compost volume added, out of the `1000` needed to fill a barrel with dirt |

---

## Barrel mixing
`exdeorum:barrel_mixing`

Item + fluid inside a barrel produce an item.

**KubeJS:**
```javascript
event.recipes.exdeorum.barrel_mixing(result, ingredient, fluid)
```

**Example:**
```javascript
event.recipes.exdeorum.barrel_mixing('minecraft:calcite', 'minecraft:bone_meal', Fluid.of('minecraft:water', 1000))
```

| Field | Description |
|---|---|
| `result` | Output item stack |
| `ingredient` | Item thrown into the barrel |
| `fluid` | Fluid in the barrel, with the amount consumed |

---

## Barrel fluid mixing
`exdeorum:barrel_fluid_mixing`

Two fluids produce an item. The additive is the fluid poured in from a bucket or an adjacent source
block; it is not consumed unless you say so.

**KubeJS:**
```javascript
event.recipes.exdeorum.barrel_fluid_mixing(result, baseFluid, additiveFluid)
event.recipes.exdeorum.barrel_fluid_mixing(result, baseFluid, additiveFluid, consumesAdditive)
```

**Example:**
```javascript
event.recipes.exdeorum.barrel_fluid_mixing('minecraft:clay', Fluid.of('exdeorum:witch_water', 1000), 'minecraft:water')
```

| Field | Description |
|---|---|
| `result` | Output item stack |
| `baseFluid` | Fluid already in the barrel, with the amount consumed |
| `additiveFluid` | Fluid added to it — must be worth a full source block (1000 mB) |
| `consumesAdditive` | Whether the additive bucket is emptied — defaults to `false` |

---

## Barrel fluid transformation
`exdeorum:barrel_fluid_transformation`

A fluid slowly turns into another fluid while a catalyst block sits under the barrel.

**KubeJS:**
```javascript
event.recipes.exdeorum.barrel_fluid_transformation(resultFluid, baseFluid, catalyst, duration)
event.recipes.exdeorum.barrel_fluid_transformation(resultFluid, baseFluid, catalyst, duration, resultColor)
event.recipes.exdeorum.barrel_fluid_transformation(resultFluid, baseFluid, catalyst, duration, resultColor, byproducts)
```

**Example:**
```javascript
event.recipes.exdeorum.barrel_fluid_transformation('minecraft:lava', 'exdeorum:witch_water', 'minecraft:magma_block', 2400, 0xD45A12, {
    'minecraft:basalt': 60,
    'minecraft:blackstone': 40
})
```

| Field | Description |
|---|---|
| `resultFluid` | Fluid id the barrel ends up holding |
| `baseFluid` | Fluid that must be in the barrel to start |
| `catalyst` | Block predicate for the block under the barrel |
| `duration` | Ticks the transformation takes |
| `resultColor` | Tint shown while the fluid changes, as an RGB integer — defaults to `0xFFFFFF` |
| `byproducts` | Weighted block states the barrel may also spit out — defaults to none |

`byproducts` accepts either a `{ blockState: weight }` object as above, or the raw list form:

```javascript
[
    { value: 'minecraft:basalt', weight: 60 },
    { value: 'minecraft:blackstone', weight: 40 }
]
```

---

## Ore chunk
`exdeorum:ore_chunk`

A 2×2 crafting recipe that turns four ore chunks into whatever ore block the pack prefers for a
given ore tag, instead of hard-coding one mod's ore.

**KubeJS:**
```javascript
event.recipes.exdeorum.ore_chunk(ore, oreChunk)
```

**Example:**
```javascript
event.recipes.exdeorum.ore_chunk('#c:ores/copper', 'exdeorum:copper_ore_chunk')
```

| Field | Description |
|---|---|
| `ore` | Item tag of the ore to produce — the preferred entry in the tag is chosen at runtime |
| `oreChunk` | Ore chunk item placed in the 2×2 grid |

---

## Removing recipes

Every type can be removed the usual KubeJS way:

```javascript
event.remove({ type: 'exdeorum:sieve' })
event.remove({ type: 'exdeorum:hammer', output: 'minecraft:gravel' })
event.remove({ type: 'exdeorum:barrel_compost', input: 'minecraft:rotten_flesh' })
```

Ex Deorum itself also provides two shortcuts through its own `exdeorum` binding:

```javascript
exdeorum.removeDefaultSieveRecipes(event)
exdeorum.removeDefaultHeatSources()
```

And it lets you filter sieve recipes by mesh:

```javascript
event.remove({ type: 'exdeorum:sieve', mesh: 'exdeorum:string_mesh' })
```

---

## Adding custom content

Recipes are only half the story — you can also register new meshes, hammers, ore chunks, sieves,
barrels and crucibles. These go in the `kubejs/startup_scripts` folder, **not** `server_scripts`.

### Items — meshes, hammers, ore chunks

```javascript
ExDeorumEvents.registry('mesh', event => {
    event.create('copper_mesh').material('#c:ingots/copper')
})

ExDeorumEvents.registry('hammer', event => {
    event.create('copper_hammer').material('#c:ingots/copper').tier('diamond')
})

ExDeorumEvents.registry('compressed_hammer', event => {
    event.create('compressed_copper_hammer')
})

ExDeorumEvents.registry('ore_chunk', event => {
    event.create('tungsten_ore_chunk')
})
```

| Target | What it gives you |
|---|---|
| `mesh` | Enchantable with Efficiency and Fortune, added to `#exdeorum:sieve_meshes` |
| `hammer` | Digger item for `#exdeorum:mineable_with_hammer`, added to `#exdeorum:hammers` |
| `compressed_hammer` | Same, added to `#exdeorum:compressed_hammers` |
| `ore_chunk` | Plain item, with its `exdeorum:ore_chunk` recipe generated for you |

Behind the scenes these are ordinary KubeJS item builders handed to KubeJS's own item registry, so
you still get the model, lang entry and creative tab for free, and every `ItemBuilder` method works
— `texture`, `displayName`, `maxDamage`, and for hammers everything `HandheldItemBuilder` has such
as `tier`, `attackDamageBaseline` and `speedBaseline`.

An id without a namespace lands in `kubejs:`, the same as `StartupEvents.registry`. Pass
`'mypack:copper_mesh'` if you want a different one.

> The `StartupEvents.registry('item', event => event.create('x', 'exdeorum:mesh'))` form also works
> — the same builders are registered as KubeJS item types. Use whichever you prefer; the
> `ExDeorumEvents` form keeps all Ex Deorum content in one place.

#### The crafting recipe is generated for you

Each of these builders can emit the crafting recipe at the same time, laid out exactly the way Ex
Deorum lays out its own. Two of them need to be told the material; the other two work it out:

```javascript
ExDeorumEvents.registry("mesh",   event => event.create("copper_mesh").material("#c:ingots/copper"))
ExDeorumEvents.registry("hammer", event => event.create("copper_hammer").material("#c:ingots/copper"))
ExDeorumEvents.registry("compressed_hammer", event => event.create("compressed_copper_hammer"))
ExDeorumEvents.registry("ore_chunk",         event => event.create("tungsten_ore_chunk"))
```

| Target | Recipe | Where the input comes from |
|---|---|---|
| `mesh` | `S#S` / `#S#` / `S#S` | `#` from `.material(...)`, `S` from `.binder(...)` — defaults to `#c:strings` |
| `hammer` | ` m ` / ` sm` / `s  ` | `m` from `.material(...)`, `s` from `.handle(...)` — defaults to `#c:rods/wooden` |
| `compressed_hammer` | 3×3 of the normal hammer | Derived: the same id without the `compressed_` prefix |
| `ore_chunk` | `exdeorum:ore_chunk` | Derived: `c:ores/<name>`, from the id minus a trailing `_ore_chunk` |

Mesh and hammer generate nothing without `.material(...)` — a material cannot be guessed. Override
the derived values with `.material('kubejs:my_hammer')` and `.ore('#c:ores/whatever')`, and turn
generation off entirely with `.noRecipe()`.

Ore chunk recipes carry the same "only if the ore tag has something in it" condition Ex Deorum puts
on its own, so a chunk for an ore no mod provides simply has no recipe rather than breaking.

The recipes are injected as datapack recipes under `exdeorumkjs:<namespace>/<name>`, so you can drop
one with `event.remove({ id: 'exdeorumkjs:kubejs/copper_hammer' })`.

#### A custom mesh needs a second texture

This one catches people out. A mesh has **two** textures, and they are different files:

| File | What it is |
|---|---|
| `assets/kubejs/textures/item/copper_mesh.png` | The inventory icon. KubeJS generates the item model from it, like any other item. |
| `assets/kubejs/textures/item/mesh/copper_mesh.png` | **The mesh as it looks lying inside the sieve.** |

The sieve renderer looks the second one up by the item's own id with an `item/mesh/` prefix — a
`kubejs:copper_mesh` item is drawn with the sprite `kubejs:item/mesh/copper_mesh`. You do not need
an atlas file for it: vanilla's block atlas already pulls in the whole `textures/item` directory
from every namespace, subfolders included, so dropping the PNG in that folder is enough.

If that file is missing the mesh still works — it can be inserted, it enables its recipes, it just
renders as the black-and-magenta missing texture inside the sieve. It should be a top-down view of
the mesh with a transparent background, 16×16 like the vanilla ones. The same texture is used by the
compressed sieve.

A new mesh does nothing until some sieve recipe asks for it, and a new ore chunk does nothing until
you give it a recipe:

```javascript
ServerEvents.recipes(event => {
    event.recipes.exdeorum.sieve('minecraft:raw_copper', 'minecraft:gravel', 'kubejs:copper_mesh').chance(0.1)
    event.recipes.exdeorum.ore_chunk('#c:ores/tungsten', 'kubejs:tungsten_ore_chunk')
})
```

### Blocks — sieves, barrels and crucibles

These are **materials**, not ordinary blocks: Ex Deorum builds the block, the block item and the
block entity binding for you from a small description. The addon exposes that through its own event:

```javascript
ExDeorumEvents.registry('sieve', event => {
    event.create('obsidian').soundType('stone').strength(4.0).needsCorrectTool(true)
})

ExDeorumEvents.registry('barrel', event => {
    event.create('obsidian').soundType('stone').strength(4.0).needsCorrectTool(true).mapColor('black').fireproof(true)
})

ExDeorumEvents.registry('compressed_sieve', event => {
    event.create('obsidian').soundType('stone').strength(4.0).needsCorrectTool(true)
})

ExDeorumEvents.registry('lava_crucible', event => {
    event.create('obsidian').soundType('stone').strength(4.0).mapColor('black')
})

ExDeorumEvents.registry('water_crucible', event => {
    event.create('jade').soundType('stone').strength(1.5).mapColor('green')
})
```

Valid targets: `sieve`, `compressed_sieve`, `barrel`, `lava_crucible`, `water_crucible`.

| Method | Description |
|---|---|
| `soundType(name)` | A `SoundType` field name such as `wood`, `stone`, `glass`, `metal` — defaults to `wood` |
| `strength(value)` | Block hardness — defaults to `2.0` |
| `mapColor(value)` | A `MapColor` name (`stone`, `color_black`), a dye colour name (`black`), or a numeric id |
| `needsCorrectTool(bool)` | Whether the block only drops when mined with the right tool |
| `requiredMod(modId)` | Only register the material when that mod is present |
| `mineableWith(tool)` | Which tool mines it — `pickaxe`, `axe`, `shovel`, `hoe`, or a full tag id |
| `craftedFrom(main, slab)` | Generates the crafting recipe from those two ingredients |
| `fireproof(bool)` | Barrels only — survives lava and fire |
| `transparent(bool)` | Barrels only — renders as a non-solid block |

#### The crafting recipe and loot table are generated for you

`craftedFrom(main, slab)` emits the crafting recipe with the same layout Ex Deorum uses for that
kind of block — `main` is the bulk material (planks for a sieve or barrel, a log for a crucible)
and `slab` is the slab in the middle:

| Target | Pattern | Ingredients |
|---|---|---|
| `sieve` | `O O` / `O_O` / `I I` | `O` main, `_` slab, `I` from `.rod(...)` — defaults to `#c:rods/wooden` |
| `compressed_sieve` | `O O` / `O_O` / `I I` | `O` main, `_` from `.binder(...)` — defaults to `#c:ingots/iron`, `I` rod. Takes `craftedFrom(main)` with no slab |
| `barrel` | `s s` / `s s` / `sms` | `s` main, `m` slab |
| `lava_crucible`, `water_crucible` | `s s` / `s s` / `sms` | `s` main, `m` slab |

Leave `craftedFrom` off and no recipe is generated; `.noRecipe()` clears one you already set. The
loot table is always generated, so the block drops itself when broken.

Five things to know before you use this:

1. **The blocks land in the `exdeorum` namespace**, not `kubejs`. `create('obsidian')` under `sieve`
   produces `exdeorum:obsidian_sieve`; under `barrel` it produces `exdeorum:obsidian_barrel`.
   That is how Ex Deorum's material registry names things and the addon does not override it.
2. **Lava and water crucibles share the `_crucible` suffix.** `lava_crucible` and `water_crucible`
   both name their block `<name>_crucible`, so the same name cannot be used in both — you will get
   `Duplicate registration`. Use different names, the way Ex Deorum uses `oak_crucible` for lava and
   `porcelain_crucible` for water.
3. **Say which tool mines it.** A block created with `needsCorrectTool(true)` only drops when mined
   with the *correct* tool, and "correct" means the block is in a `#minecraft:mineable/<tool>` tag.
   Ex Deorum's own tag files are fixed lists written when the mod was built, so a block you add
   later is in none of them — and a block in no mineable tag never drops, no matter what you swing
   at it. Use `.mineableWith('pickaxe')` (or `axe`, `shovel`, `hoe`, or a full tag id) and the tag
   entry is generated alongside the loot table. If you set `needsCorrectTool(true)` and forget,
   the addon falls back to `pickaxe` and says so in the log rather than leaving you a block that
   silently never drops.

4. **You have to supply the assets.** The loot table is generated for you — the block drops itself
   when broken, exactly like Ex Deorum's own — but unlike the item builders, materials get no
   generated model, texture or lang entry. Put those in `kubejs/assets/exdeorum/`. Ex Deorum's own
   templates do the modelling, so each block only needs a texture pointed at them:

   ```json
   // assets/exdeorum/blockstates/obsidian_sieve.json
   { "variants": { "": { "model": "exdeorum:block/obsidian_sieve" } } }

   // assets/exdeorum/models/block/obsidian_sieve.json
   { "parent": "exdeorum:block/template_sieve", "textures": { "texture": "minecraft:block/obsidian" } }

   // assets/exdeorum/models/item/obsidian_sieve.json
   { "parent": "exdeorum:block/obsidian_sieve" }
   ```

   | Material | Parent model | Texture keys |
   |---|---|---|
   | Sieve | `exdeorum:block/template_sieve` | `texture` |
   | Compressed sieve | `exdeorum:block/template_compressed_sieve` | `texture` |
   | Barrel | `exdeorum:block/template_barrel` | `barrel` |
   | Crucible | `exdeorum:block/template_crucible` | `bottom`, `inside`, `side`, `top` |

   Add a `lang` entry for `block.exdeorum.<name>` too, or the block shows a raw translation key.
   There is a complete worked example for all five under `run/kubejs/assets/` in this repo.
5. **Materials are registered during mod construction**, so unlike recipes they are *not* reloadable
   — a change to a startup script needs a full game restart, not `/reload`.

If a material fails to build, the addon logs the error with the offending description and carries
on loading; it will not crash the game.

---

## Helper bindings

The `ExDeorumKJS` binding is available in server scripts:

| Call | Returns |
|---|---|
| `ExDeorumKJS.constant(value)` | Number provider producing exactly `value` |
| `ExDeorumKJS.uniform(min, max)` | Number provider rolling uniformly between `min` and `max` |
| `ExDeorumKJS.binomial(n, p)` | Number provider rolling `n` trials at probability `p` |
| `ExDeorumKJS.block(block)` | Block predicate matching one block |
| `ExDeorumKJS.blockTag(tag)` | Block predicate matching a block tag |
| `ExDeorumKJS.blockPredicate(string)` | Block predicate parsed from `'mod:block[state=value]'` syntax |
