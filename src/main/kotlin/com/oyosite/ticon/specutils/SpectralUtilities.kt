package com.oyosite.ticon.specutils

import com.oyosite.ticon.specutils.block.BlockRegistry
import com.oyosite.ticon.specutils.config.CommonConfig
import com.oyosite.ticon.specutils.item.EnderFlask
import com.oyosite.ticon.specutils.item.ItemRegistry
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
object SpectralUtilities: ModInitializer {

    const val MODID = "specutils"
    fun id(id: String) = if(id.contains(":"))Identifier.tryParse(id) else Identifier.tryParse(MODID, id)

    val LOGGER: Logger = LoggerFactory.getLogger("Spectrum")

    val debugLoggerEnabled = true

    fun info(message: String) = LOGGER.info("[Spectral Utilities] $message")
    fun warn(message: String) = LOGGER.warn("[Spectral Utilities] $message")
    fun error(message: String) = LOGGER.error("[Spectral Utilities] $message")
    fun debugInfo(message: String) { if (debugLoggerEnabled) info(message) }
    fun debugWarn(message: String) { if(debugLoggerEnabled) warn(message) }
    fun debugError(message: String) { if(debugLoggerEnabled) error(message) }

    override fun onInitialize() {
        ItemRegistry()
        BlockRegistry()

        AutoConfig.register(CommonConfig::class.java, ::GsonConfigSerializer)


        //The second event may be redundant, but I want to be safe.
        ServerLifecycleEvents.SERVER_STARTING.register{ EnderFlask.scoreboard = it.scoreboard}
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register{server, _, _ -> EnderFlask.scoreboard = server.scoreboard}

        // TODO: Add items to item groups
        // ItemSubGroupEvents.modifyEntriesEvent(ItemGroupIDs.SUBTAB_ENERGY).register(...)
    }
}