package me.td.cm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class ConsumableMeth implements ModInitializer {
    public static SoundEvent SNIFFING_METH = null;

    public static ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public boolean isBrokenBadLoaded = false;

    public static final Logger logger = LoggerFactory.getLogger("consumablemeth");

    @Override
    public void onInitialize() {
        isBrokenBadLoaded = FabricLoader.getInstance().isModLoaded("createbb");

        if(!isBrokenBadLoaded)
        {
            logger.error("Create: Broken Bad is not loaded. Exiting now.");
            System.exit(1);
        }

          SNIFFING_METH = Registry.register(Registries.SOUND_EVENT, new Identifier("consumablemeth", "sniffing_meth"),
                SoundEvent.of(new Identifier("consumablemeth", "sniffing_meth")));
    }
}
