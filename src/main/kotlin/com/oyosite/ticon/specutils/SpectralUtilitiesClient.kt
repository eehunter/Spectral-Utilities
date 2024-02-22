package com.oyosite.ticon.specutils

import com.oyosite.ticon.specutils.block.BlockRegistry
import com.oyosite.ticon.specutils.block.auxilary_ink_supplier.AuxiliaryInkSupplierBlockEntityRenderer
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry
import net.fabricmc.api.ClientModInitializer

@Suppress("UNUSED")
object SpectralUtilitiesClient: ClientModInitializer {


    override fun onInitializeClient() {
        BlockEntityRendererRegistry.register(BlockRegistry.BlockEntities.AUXILIARY_INK_SUPPLIER_TYPE){ AuxiliaryInkSupplierBlockEntityRenderer() }
    }
}