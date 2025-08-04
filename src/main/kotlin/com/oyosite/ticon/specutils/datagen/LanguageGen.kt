package com.oyosite.ticon.specutils.datagen

import com.oyosite.ticon.specutils.item.ItemRegistry
import com.oyosite.ticon.specutils.block.BlockRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.block.Block
import net.minecraft.item.Item
import java.util.*

class LanguageGen(output: FabricDataOutput): FabricLanguageProvider(output) {

    val BLOCK_NAME_OVERRIDES: Map<Block, String> = mapOf()
    val ITEM_NAME_OVERRIDES: Map<Item, String> = mapOf()


    override fun generateTranslations(translationBuilder: TranslationBuilder) {
        ItemRegistry.enderFlasks.forEach {
            translationBuilder.add(it, "${it.color.dyeColor.name.toEnglishName} Ender Flask")
            translationBuilder.add("item.specutils.${it.color.dyeColor.name.lowercase()}_ender_flask.tooltip", "§7Stores up to %d§7 ${it.color.dyeColor.name.toEnglishName} Ink")
        }
        translationBuilder.add("item.specutils.ender_flask.tooltip.owner", "Owner: %s")
        translationBuilder.add("item.specutils.ender_flask.tooltip.unlinked_0", "Owner: Nobody")
        translationBuilder.add("item.specutils.ender_flask.tooltip.unlinked_1", "Right-Click to bind.")
        translationBuilder.add("item.specutils.ender_flask.tooltip.unknown_player", "Unknown Player")

        translationBuilder.add("item.specutils.binding_tool.tooltip.unlinked", "Not Bound.")
        translationBuilder.add("item.specutils.binding_tool.tooltip.linked_to", "Bound to x=%s, y=%s, z=%s.")

        for(prop in BlockRegistry::class.declaredMemberProperties){
            val block: Block = prop.get(BlockRegistry) as? Block ?: continue
            translationBuilder.add(block, if(BLOCK_NAME_OVERRIDES.containsKey(block))BLOCK_NAME_OVERRIDES[block] else prop.name.toEnglishName)
        }
        for(prop in ItemRegistry::class.declaredMemberProperties){
            val item: Item = prop.get(ItemRegistry) as? Item ?: continue
            translationBuilder.add(item, if(ITEM_NAME_OVERRIDES.containsKey(item))ITEM_NAME_OVERRIDES[item] else prop.name.toEnglishName)
        }



        /*BlockRegistry.run{
            ItemRegistry.run{
                translationBuilder.blockAndItemTranslations()
            }
        }*/
    }

    /*context(ItemRegistry, BlockRegistry)
    private fun TranslationBuilder.blockAndItemTranslations(){
        //add(AUXILIARY_INK_SUPPLIER, )

        //addTranslation(BlockRegistry::AUXILIARY_INK_SUPPLIER)


        BlockRegistry::class.declaredMemberProperties
    }*/

    //context(O, TranslationBuilder)
    //inline fun <reified O: ModRegistry, reified T: Block> KProperty1<O, T>.unaryPlus(): Unit = add(get(this@O), name.toEnglishName)

    //context(TranslationBuilder)
    /*private fun addAllTranslations(vararg arr : Any) = arr.forEach{
        when(it){
            is Block -> BlockRegistry
        }
    }

    private inline fun <reified T, reified R: ModRegistry<T>> R.addTranslations(consumer: (T,String)->Unit, vararg props: KProperty<T>) = props.forEach { consumer(it.getter.call(R::class.objectInstance), it.nameFor) }

    context(T, TranslationBuilder)
    private fun <T: ModRegistry<Block>> addTranslation(vararg blocks: KProperty<Block>) = blocks.forEach { add(it.getter.call(this@T), it.nameFor) }

    context(T, TranslationBuilder)
    private fun <T: ModRegistry<Item>> addTranslation(vararg items: KProperty<Item>) = items.forEach { add(it.getter.call(this@T), it.nameFor) }

    val KProperty<*>.nameFor get() = name.toEnglishName*/

    val String.toEnglishName get() = lowercase().split("_").joinToString(" ") { it.capitalized }

    val String.capitalized get() = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}