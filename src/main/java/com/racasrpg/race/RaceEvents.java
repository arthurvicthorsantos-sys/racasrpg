package com.racasrpg.race;

import com.racasrpg.RacasRpg;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = RacasRpg.MODID)
public class RaceEvents {

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            RaceManager.applyEffects(player);
            if (!RaceManager.get(player).hasRace()) {
                player.sendSystemMessage(Component.literal(
                        "Escolha sua raca com /raca escolher <humano|elfo|anao|orc>. Veja detalhes com /raca info.")
                        .withStyle(ChatFormatting.GOLD));
            }
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            RaceManager.applyEffects(player);
        }
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            RaceManager.applyEffects(player);
        }
    }

    @SubscribeEvent
    public static void onKill(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Enemy)) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) {
            return;
        }

        RaceManager.addProgress(player, Race.MissionType.KILL_HOSTILE);

        Entity direct = event.getSource().getDirectEntity();
        if (direct instanceof Projectile) {
            RaceManager.addProgress(player, Race.MissionType.KILL_RANGED);
        } else if (direct == player) {
            RaceManager.addProgress(player, Race.MissionType.KILL_MELEE);
        }
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player && event.getState().is(Tags.Blocks.ORES)) {
            RaceManager.addProgress(player, Race.MissionType.MINE_ORE);
        }
    }
}
