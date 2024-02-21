package com.oyosite.ticon.specutils.config

import com.oyosite.ticon.specutils.SpectralUtilities
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config

@Config(name = SpectralUtilities.MODID)
class CommonConfig: ConfigData {

    var jadeVinesDropJadeJellyWhenRevived = true


}