package com.fusionflux.originsumbrellas;

import com.fusionflux.originsumbrellas.items.UmbrellaItems;
import com.fusionflux.originsumbrellas.state.property.Properties;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.StatePredicate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OriginsUmbrellas implements ModInitializer {

    public static final String MOD_ID = "originsumbrellas";

    @Override
    public void onInitialize() {
        UmbrellaItems.registerItems();

        // All flowers... that we know about
        List<Block> flowers = Collections.singletonList(Blocks.RED_TULIP);

        LootTableEvents.REPLACE.register(((resourceManager, lootManager, id, original, source) -> {
            // Handle replacing flower loot tables, so the drops actually look sane.
            for (Block flower : flowers) {
                if (id.equals(flower.getLootTableId())) {
                    // Looks a little intense, but essentially, return as many flowers as you had placed
                    // while handling things like explosion decay (similar to Sea Pickles).
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .with(BlockLootTableGenerator.applyExplosionDecay(flower, ItemEntry.builder(flower)
                                    .apply(Arrays.asList(2, 3, 4), integer -> SetCountLootFunction.builder(ConstantLootNumberProvider.create(integer))
                                            .conditionally(BlockStatePropertyLootCondition.builder(flower)
                                                    .properties(StatePredicate.Builder.create().exactMatch(Properties.FLOWERS, integer))))));

                    return LootTable.builder().pool(poolBuilder).build();
                }
            }
            // Otherwise, just return the loot table unmodified.
            return original;
        }));
    }
}
