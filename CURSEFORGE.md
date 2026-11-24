# CurseForge project text

## Summary (the short field)

Full KubeJS support for Ex Deorum — every recipe type, plus custom meshes, hammers, ore chunks, sieves, barrels and crucibles.

---

## Description (the project page)

## Ex Deorum KJS

Ex Deorum is great. Scripting it is not — until now.

Ex Deorum ships a KubeJS integration, but it does not go far. Its own plugin registers only a minimal `exdeorum:sieve` schema that knows about `result` and `ingredient` — no `mesh`, no `result_amount` — so it cannot actually produce a working sieve recipe.

**Ex Deorum KJS** replaces that with a complete implementation and adds the twelve recipe types that were never exposed, then goes further and lets you register new Ex Deorum content from a script.

## Every recipe type

All thirteen, from a server script:

`sieve` · `compressed_sieve` · `hammer` · `compressed_hammer` · `crook` · `crucible_heat_source` · `lava_crucible` · `water_crucible` · `barrel_compost` · `barrel_mixing` · `barrel_fluid_mixing` · `barrel_fluid_transformation` · `ore_chunk`

```javascript
ServerEvents.recipes(event => {
    event.recipes.exdeorum.sieve('minecraft:prismarine_shard', 'minecraft:clay', 'exdeorum:string_mesh')
        .binomial(3, 0.2)

    event.recipes.exdeorum.crucible_heat_source('minecraft:furnace', 4).lit(true)

    event.recipes.exdeorum.barrel_mixing('minecraft:calcite', 'minecraft:bone_meal', Fluid.of('minecraft:water', 1000))
})
```

No more hand-writing loot number providers or block state predicates. Chain what you mean:

- `.constant(4)` — always 4
- `.chance(0.25)` — one roll at 25%
- `.uniform(1, 3)` — a flat roll between 1 and 3
- `.binomial(9, 0.12)` — 9 rolls at 12% each
- `.lit(true)`, `.axis('y')`, `.state('distance', 1)` — pin a block predicate to a state

## Register your own content

Nine targets, one event:

```javascript
ExDeorumEvents.registry('sieve', event => {
    event.create('obsidian')
        .soundType('stone').strength(4.0).needsCorrectTool(true).mineableWith('pickaxe')
        .craftedFrom('minecraft:obsidian', 'minecraft:polished_blackstone_slab')
})

ExDeorumEvents.registry('mesh', event => {
    event.create('copper_mesh').material('#forge:ingots/copper')
})
```

**Blocks:** `sieve`, `compressed_sieve`, `barrel`, `lava_crucible`, `water_crucible`
**Items:** `mesh`, `hammer`, `compressed_hammer`, `ore_chunk`

One `create(...)` call gives you the block or item **plus**:

- its crafting recipe, using the exact layout Ex Deorum uses for that kind of block
- its loot table, so it drops itself when broken
- its `mineable/` tag entry, so "needs the correct tool" actually works
- for items: the model, lang entry and creative tab, courtesy of KubeJS's own item builders

New meshes go into `#exdeorum:sieve_meshes` and are enchantable with Efficiency and Fortune. New hammers go into `#exdeorum:hammers` and mine what a hammer mines. Ore chunks derive their ore tag from their own name and carry the same "only if the tag has something in it" condition Ex Deorum puts on its own.

You still supply block textures and models — Ex Deorum's own templates do the shaping, so it is a blockstate and a two-line model each. A complete worked example for all five block types ships with the addon.

## Requirements

- **KubeJS 6** (2001)
- **Ex Deorum 1.53+**

Supported: **Minecraft 1.20.1**, **Forge 47+**.

Note: Ex Deorum 1.20.1 has no dedicated ore chunk recipe type, so `ore_chunk` recipes are written as `exdeorum:tag_result`.

## Documentation

Every recipe type and every builder method is documented with a working example in `wiky.md` in the repository, along with a ready-to-run example script covering all thirteen recipe types and all nine registry targets.
