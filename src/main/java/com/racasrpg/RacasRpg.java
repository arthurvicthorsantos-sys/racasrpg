package com.racasrpg;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.racasrpg.race.ModAttachments;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(RacasRpg.MODID)
public class RacasRpg {
    public static final String MODID = "racasrpg";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RacasRpg(IEventBus modEventBus) {
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);
        LOGGER.info("Racas RPG carregado.");
    }
}
