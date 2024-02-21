package com.oyosite.ticon.specutils.block

import net.fabricmc.fabric.api.item.v1.FabricItemSettings

interface BlockWithItemSettings {

    val itemSettings: FabricItemSettings?
}