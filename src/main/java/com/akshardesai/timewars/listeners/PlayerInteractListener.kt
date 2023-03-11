package com.akshardesai.timewars.listeners

import com.akshardesai.timewars.TimeWars
import com.akshardesai.timewars.team.TeamManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType


class PlayerInteractListener : Listener {
    private var woolCost: Int = 4
    private var shieldCost: Int = 20
    private var gearCost: Int = 32
    private var swordCost: Int = 10
    private val kbStickCost: Int = 40
    private val invisPotCost: Int = 3
    private val jumpPotCost: Int = 2
    private val speedPotCost: Int = 2
    private val woolShopItem: ItemStack = ItemStack(Material.WHITE_WOOL, 16)
    private val kbStick: ItemStack = ItemStack(Material.STICK)
    private val kbStickItemGiveable: ItemStack = ItemStack(Material.STICK)
    private val invisPot: ItemStack = ItemStack(Material.POTION)
    private val invisPotGiveable: ItemStack = ItemStack(Material.POTION)
    private val jumpPot: ItemStack = ItemStack(Material.POTION)
    private val jumpPotGiveable: ItemStack = ItemStack(Material.POTION)
    private val speedPot: ItemStack = ItemStack(Material.POTION)
    private val speedPotGiveable: ItemStack = ItemStack(Material.POTION)
    private val shieldShopItem: ItemStack = ItemStack(Material.SHIELD)
    private val woolItem: ItemStack = ItemStack(Material.WHITE_WOOL, 16)
    private val shieldItem: ItemStack = ItemStack(Material.SHIELD)
    private val ironIngot: ItemStack = ItemStack(Material.IRON_INGOT)
    private val emerald: ItemStack = ItemStack(Material.EMERALD)

    @EventHandler
    fun onHungerDeplete(e: FoodLevelChangeEvent) {
        e.isCancelled = true
        (e.entity as? Player)?.foodLevel = 20
    }

    private fun getWoolType(player: Player, stack: ItemStack) : ItemStack {
        val wool = stack.clone()
        when (TeamManager.getTeam(player)?.name) {
            "RED" -> {
                wool.type = Material.RED_WOOL
            }
            "YELLOW" -> {
                wool.type = Material.YELLOW_WOOL
            }
            "GREEN" -> {
                wool.type = Material.LIME_WOOL
            }
            "BLUE" -> {
                wool.type = Material.BLUE_WOOL
            }
            "PINK" -> {
                wool.type = Material.PINK_WOOL
            }
            "GRAY" -> {
                wool.type = Material.GRAY_WOOL
            }
            "WHITE" -> {
                wool.type = Material.WHITE_WOOL
            }
            "CYAN" -> {
                wool.type = Material.CYAN_WOOL
            }
        }
        return wool
    }
    @EventHandler
    fun onRightClickBlock(event: PlayerInteractEvent) {
        val block = event.clickedBlock
        if (block?.type == Material.END_PORTAL_FRAME && (event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.LEFT_CLICK_BLOCK || event.action == Action.LEFT_CLICK_AIR) && event.player.gameMode != GameMode.CREATIVE) {
            val gui = Bukkit.createInventory(event.player, 18, Component.text("Shop", NamedTextColor.DARK_RED))

            //Menu Options(Items)
            val wool = getWoolType(event.player,woolShopItem)
            val shield = shieldShopItem
            var gearItem = ItemStack(Material.LEATHER_HELMET)
            var swordItem = ItemStack(Material.WOODEN_SWORD)
            val kbStickItem = kbStick
            val invisPotItem = invisPot
            val jumpPotItem = jumpPot
            val speedPotItem = speedPot
            val invisPotMeta = invisPotItem.itemMeta as PotionMeta
            invisPotMeta.displayName(Component.text("Potion of Invisibility (45 Seconds)",NamedTextColor.DARK_AQUA).decoration(TextDecoration.ITALIC,false))
            invisPotMeta.color = Color.RED
            invisPotMeta.basePotionData = PotionData(PotionType.INVISIBILITY)
            invisPotGiveable.itemMeta = invisPotMeta
            invisPotGiveable.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
            val invisPotLore = ArrayList<Component>()
            invisPotLore.add(Component.text(ChatColor.DARK_PURPLE.toString() + "Cost:" + ChatColor.GOLD.toString() + " $invisPotCost Emeralds"))
            if (!event.player.inventory.containsAtLeast(emerald, invisPotCost)) {
                invisPotLore.add(Component.text(" "))
                invisPotLore.add(Component.text(ChatColor.RED.toString() + "You don't have enough materials to buy this!"))
            }
            val newMetaPot = invisPotMeta.clone()
            newMetaPot.lore(invisPotLore)
            invisPotGiveable.lore(null)
            invisPot.itemMeta = newMetaPot
            invisPot.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)




            val jumpPotMeta = jumpPotItem.itemMeta as PotionMeta
            jumpPotMeta.displayName(Component.text("Potion of Jump Boost (45 Seconds)",NamedTextColor.DARK_RED).decoration(TextDecoration.ITALIC,false))
            jumpPotMeta.color = Color.PURPLE
            jumpPotMeta.basePotionData = PotionData(PotionType.JUMP)
            jumpPotGiveable.itemMeta = jumpPotMeta
            jumpPotGiveable.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
            val jumpPotLore = ArrayList<Component>()
            jumpPotLore.add(Component.text(ChatColor.DARK_PURPLE.toString() + "Cost:" + ChatColor.GOLD.toString() + " $jumpPotCost Emeralds"))
            if (!event.player.inventory.containsAtLeast(emerald, jumpPotCost)) {
                jumpPotLore.add(Component.text(" "))
                jumpPotLore.add(Component.text(ChatColor.RED.toString() + "You don't have enough materials to buy this!"))
            }
            val newMetaPotJump = jumpPotMeta.clone()
            newMetaPotJump.lore(jumpPotLore)
            jumpPotGiveable.lore(null)
            jumpPot.itemMeta = newMetaPotJump
            jumpPot.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)


            val speedPotMeta = speedPotItem.itemMeta as PotionMeta
            speedPotMeta.displayName(Component.text("Potion of Speed (45 Seconds)",NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC,false))
            speedPotMeta.color = Color.YELLOW
            speedPotMeta.basePotionData = PotionData(PotionType.SPEED)
            speedPotGiveable.itemMeta = speedPotMeta
            speedPotGiveable.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
            val speedPotLore = ArrayList<Component>()
            speedPotLore.add(Component.text(ChatColor.DARK_PURPLE.toString() + "Cost:" + ChatColor.GOLD.toString() + " $speedPotCost Emeralds"))
            if (!event.player.inventory.containsAtLeast(emerald, speedPotCost)) {
                speedPotLore.add(Component.text(" "))
                speedPotLore.add(Component.text(ChatColor.RED.toString() + "You don't have enough materials to buy this!"))
            }
            val newMetaPotSpeed = speedPotMeta.clone()
            newMetaPotSpeed.lore(speedPotLore)
            speedPotGiveable.lore(null)
            speedPot.itemMeta = newMetaPotSpeed
            speedPot.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)









            kbStickItem.addUnsafeEnchantment(Enchantment.KNOCKBACK,3)
            val stickMeta = kbStickItem.itemMeta
            stickMeta.displayName(Component.text(ChatColor.YELLOW.toString()+"Knockback Stick"))
            kbStickItemGiveable.itemMeta = stickMeta
            val newMeta = stickMeta.clone()
            kbStickItemGiveable.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            val stickLore = ArrayList<Component>()
            stickLore.add(Component.text(ChatColor.DARK_PURPLE.toString() + "Cost:" + ChatColor.GOLD.toString() + " $kbStickCost Iron"))
            if (!event.player.inventory.containsAtLeast(ironIngot, kbStickCost)) {
                stickLore.add(Component.text(" "))
                stickLore.add(Component.text(ChatColor.RED.toString() + "You don't have enough materials to buy this!"))
            }
            newMeta.lore(stickLore)
            kbStickItem.itemMeta = newMeta
            kbStickItem.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            if (TimeWars.years > 2700) {
                gearItem = ItemStack(Material.NETHERITE_HELMET)
                swordItem = ItemStack(Material.NETHERITE_SWORD)
                shield.addUnsafeEnchantment(Enchantment.DURABILITY,4)
                shieldItem.addUnsafeEnchantment(Enchantment.DURABILITY,4)
                gearCost = 128
                shieldCost = 110
                swordCost = 60
            } else if (TimeWars.years > 1900) {
                gearItem = ItemStack(Material.DIAMOND_HELMET)
                swordItem = ItemStack(Material.DIAMOND_SWORD)
                shield.addEnchantment(Enchantment.DURABILITY,3)
                shieldItem.addEnchantment(Enchantment.DURABILITY,3)
                gearCost = 96
                shieldCost = 80
                swordCost = 50
                woolShopItem.amount = 64
                woolItem.amount = 64
            } else if (TimeWars.years > 1200) {
                gearItem = ItemStack(Material.IRON_HELMET)
                swordItem = ItemStack(Material.IRON_SWORD)
                shield.addEnchantment(Enchantment.DURABILITY,2)
                shieldItem.addEnchantment(Enchantment.DURABILITY,2)
                gearCost = 64
                shieldCost = 60
                swordCost = 40
                woolShopItem.amount = 48
                woolItem.amount = 48
            } else if (TimeWars.years > 500) {
                gearItem = ItemStack(Material.CHAINMAIL_HELMET)
                swordItem = ItemStack(Material.STONE_SWORD)
                shield.addEnchantment(Enchantment.DURABILITY,1)
                shieldItem.addEnchantment(Enchantment.DURABILITY,1)
                shieldCost = 40
                gearCost = 48
                swordCost = 25
                woolShopItem.amount = 32
                woolItem.amount = 32
            }

            //Edit the items
            val wool_meta = wool.itemMeta
            val woolMeta = woolItem.itemMeta
            woolItem.itemMeta = woolMeta
            val wool_lore = ArrayList<Component>()
            wool_lore.add(Component.text(ChatColor.DARK_PURPLE.toString() + "Cost:" + ChatColor.GOLD.toString() + " $woolCost Iron"))
            if (!event.player.inventory.containsAtLeast(ironIngot, woolCost)) {
                wool_lore.add(Component.text(" "))
                wool_lore.add(Component.text(ChatColor.RED.toString() + "You don't have enough materials to buy this!"))
            }
            wool_meta.lore(wool_lore)


            wool.itemMeta = wool_meta


            val shield_meta = shield.itemMeta
            val shieldMeta = shieldItem.itemMeta
            shieldMeta.isUnbreakable = true
            shieldMeta.displayName(Component.text(ChatColor.RED.toString()+"Shield"))
            shieldItem.itemMeta = shieldMeta
            shield_meta.displayName(Component.text(ChatColor.RED.toString() + "Industrial Shield"))
            val shield_lore = ArrayList<Component>()
            shield_lore.add(Component.text(ChatColor.DARK_PURPLE.toString() + "Cost:" + ChatColor.GOLD.toString() + " $shieldCost Iron"))
            if (!event.player.inventory.containsAtLeast(ironIngot, shieldCost)) {
                shield_lore.add(Component.text(" "))
                shield_lore.add(Component.text(ChatColor.RED.toString() + "You don't have enough materials to buy this!"))
            }
            shield_meta.lore(shield_lore)


            shield.itemMeta = shield_meta

            val gearMeta = gearItem.itemMeta
            gearMeta.displayName(Component.text(ChatColor.RED.toString() + "THE NEWEST TECHNOLOGY OF " + ChatColor.BOLD.toString() + "MILITARY ARMOR"))
            val gear_lore = ArrayList<Component>()
            gear_lore.add(Component.text(ChatColor.DARK_PURPLE.toString() + "Cost:" + ChatColor.GOLD.toString() + " $gearCost Iron"))
            when (gearItem.type) {
                Material.LEATHER_HELMET -> {
                    if (checkGear(event.player,Material.LEATHER_BOOTS,Material.LEATHER_LEGGINGS,Material.LEATHER_CHESTPLATE,Material.LEATHER_HELMET)) {
                        gear_lore.add(Component.text(" "))
                        gear_lore.add(Component.text(ChatColor.RED.toString() + "You already have this item!"))
                    } else if (!event.player.inventory.containsAtLeast(ironIngot, gearCost)) {
                        gear_lore.add(Component.text(" "))
                        gear_lore.add(Component.text(ChatColor.RED.toString() + "You don't have enough materials to buy this!"))
                    }
                }
                Material.CHAINMAIL_HELMET -> {
                    if (checkGear(event.player,Material.CHAINMAIL_BOOTS,Material.CHAINMAIL_LEGGINGS,Material.CHAINMAIL_CHESTPLATE,Material.CHAINMAIL_HELMET)) {
                        gear_lore.add(Component.text(" "))
                        gear_lore.add(Component.text(ChatColor.RED.toString() + "You already have this item!"))
                    } else if (!event.player.inventory.containsAtLeast(ironIngot, gearCost)) {
                        gear_lore.add(Component.text(" "))
                        gear_lore.add(Component.text(ChatColor.RED.toString() + "You don't have enough materials to buy this!"))
                    }
                }
                Material.IRON_HELMET -> {
                    if (checkGear(event.player,Material.IRON_BOOTS,Material.IRON_LEGGINGS,Material.IRON_CHESTPLATE,Material.IRON_HELMET)) {
                        gear_lore.add(Component.text(" "))
                        gear_lore.add(Component.text(ChatColor.RED.toString() + "You already have this item!"))
                    } else if (!event.player.inventory.containsAtLeast(ironIngot, gearCost)) {
                        gear_lore.add(Component.text(" "))
                        gear_lore.add(Component.text(ChatColor.RED.toString() + "You don't have enough materials to buy this!"))
                    }
                }
                Material.DIAMOND_HELMET -> {
                    if (checkGear(event.player,Material.DIAMOND_BOOTS,Material.DIAMOND_LEGGINGS,Material.DIAMOND_CHESTPLATE,Material.DIAMOND_HELMET)) {
                        gear_lore.add(Component.text(" "))
                        gear_lore.add(Component.text(ChatColor.RED.toString() + "You already have this item!"))
                    } else if (!event.player.inventory.containsAtLeast(ironIngot, gearCost)) {
                        gear_lore.add(Component.text(" "))
                        gear_lore.add(Component.text(ChatColor.RED.toString() + "You don't have enough materials to buy this!"))
                    }
                }
                Material.NETHERITE_HELMET -> {
                    if (checkGear(event.player,Material.NETHERITE_BOOTS,Material.NETHERITE_LEGGINGS,Material.NETHERITE_CHESTPLATE,Material.NETHERITE_HELMET)) {
                        gear_lore.add(Component.text(" "))
                        gear_lore.add(Component.text(ChatColor.RED.toString() + "You already have this item!"))
                    } else if (!event.player.inventory.containsAtLeast(ironIngot, gearCost)) {
                        gear_lore.add(Component.text(" "))
                        gear_lore.add(Component.text(ChatColor.RED.toString() + "You don't have enough materials to buy this!"))
                    }
                }
                else -> {
                    
                }
            }
            gearMeta.lore(gear_lore)
            gearItem.itemMeta = gearMeta


            val swordMeta = swordItem.itemMeta
            swordMeta.displayName(Component.text(ChatColor.RED.toString() + "THE NEWEST TECHNOLOGY OF " + ChatColor.BOLD.toString() + "MILITARY WEAPONS"))
            val sword_lore = ArrayList<Component>()
            sword_lore.add(Component.text(ChatColor.DARK_PURPLE.toString() + "Cost:" + ChatColor.GOLD.toString() + " $swordCost Iron"))
            if (!event.player.inventory.containsAtLeast(ironIngot, swordCost)) {
                sword_lore.add(Component.text(" "))
                sword_lore.add(Component.text(ChatColor.RED.toString() + "You don't have enough materials to buy this!"))
            }
            swordMeta.lore(sword_lore)
            swordItem.itemMeta = swordMeta

            //Put the items in the inventory
            val menu_items = arrayOf<ItemStack?>(wool, swordItem,shield,gearItem,kbStickItem,invisPotItem,jumpPotItem,speedPotItem)
            gui.contents = menu_items
            event.player.openInventory(gui)
        }
        else if (block?.type == Material.POLISHED_DEEPSLATE && (event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.LEFT_CLICK_BLOCK || event.action == Action.LEFT_CLICK_AIR) && event.player.gameMode != GameMode.CREATIVE /*&& TimeWars.years >= 800*/) {
            val gui = Bukkit.createInventory(event.player, 18, Component.text("Upgrades", NamedTextColor.DARK_AQUA))
            var swordItem = ItemStack(Material.WOODEN_SWORD)
            if (TimeWars.years > 2700) {
                swordItem = ItemStack(Material.NETHERITE_SWORD)
            } else if (TimeWars.years > 1900) {
                swordItem = ItemStack(Material.DIAMOND_SWORD)
            } else if (TimeWars.years > 1200) {
                swordItem = ItemStack(Material.IRON_SWORD)
            } else if (TimeWars.years > 500) {
                swordItem = ItemStack(Material.STONE_SWORD)
            }
            val swordMeta = swordItem.itemMeta
            swordMeta.displayName(Component.text("Sharpness Upgrade", NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false))
            val swordLore = ArrayList<Component>()
            swordLore.add(Component.text(ChatColor.GOLD.toString()+ "> "+ChatColor.GRAY.toString()+"Tier 1: "+ChatColor.GOLD.toString()+"4"+ ChatColor.AQUA.toString() +" Diamonds"))
            swordLore.add(Component.text(ChatColor.GRAY.toString()+"Tier 2: "+ChatColor.GOLD.toString()+"8"+ ChatColor.AQUA.toString() +" Diamonds"))
            swordLore.add(Component.text(ChatColor.GRAY.toString()+"Tier 3: "+ChatColor.GOLD.toString()+"16"+ ChatColor.AQUA.toString() +" Diamonds"))
            swordLore.add(Component.text(ChatColor.GRAY.toString()+"Tier 4: "+ChatColor.GOLD.toString()+"32"+ ChatColor.AQUA.toString() +" Diamonds"))
            swordMeta.lore(swordLore)
            swordItem.itemMeta = swordMeta
            val menu_items = arrayOf<ItemStack?>(swordItem)
            gui.contents = menu_items
            event.player.openInventory(gui)
        }
    }

    private fun checkGear(player:Player, boots: Material,leggings: Material,chestplate: Material,helmet: Material) : Boolean {
        return (player.inventory.boots == ItemStack(boots) || player.inventory.leggings == ItemStack(leggings) || player.inventory.chestplate == ItemStack(chestplate) || player.inventory.helmet == ItemStack(helmet))
    }

    private fun notEnoughMoney(player: Player) {
        player.sendMessage(ChatColor.RED.toString() + "You do not have enough materials to buy this!")
        player.playSound(player.location, Sound.ENTITY_ENDERMITE_HURT, 1f, 1f)
    }

    private fun itemAcquiredBefore(player: Player) {
        player.sendMessage(ChatColor.RED.toString() + "You already have this item!")
        player.playSound(player.location, Sound.ENTITY_ENDERMAN_HURT, 1f, 1f)
    }

    private fun buyGear(player: Player, boots: ItemStack, leggings: ItemStack, chestplate: ItemStack, helmet: ItemStack, event: InventoryClickEvent):Boolean {
        // will always be the third item or 2
        if (player.inventory.boots == ItemStack(boots) || player.inventory.leggings == ItemStack(leggings) || player.inventory.chestplate == ItemStack(chestplate) || player.inventory.helmet == ItemStack(helmet)) {
            itemAcquiredBefore(player)
            event.isCancelled = true
            return false
        }
        else if (!player.inventory.containsAtLeast(ironIngot, gearCost)) {
            notEnoughMoney(player)
            event.isCancelled = true
            return false

        } else {
            if ((event.whoClicked as Player).hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                for (online in Bukkit.getOnlinePlayers()) {
                    if (online == (event.whoClicked as Player)) continue
                    online.sendEquipmentChange((event.whoClicked as Player), EquipmentSlot.HEAD, ItemStack(Material.AIR))
                    online.sendEquipmentChange((event.whoClicked as Player), EquipmentSlot.CHEST, ItemStack(Material.AIR))
                    online.sendEquipmentChange((event.whoClicked as Player), EquipmentSlot.LEGS, ItemStack(Material.AIR))
                    online.sendEquipmentChange((event.whoClicked as Player), EquipmentSlot.FEET, ItemStack(Material.AIR))
                    online.sendEquipmentChange((event.whoClicked as Player), EquipmentSlot.HAND, ItemStack(Material.AIR))
                }
            }
            ironIngot.amount = gearCost
            player.inventory.removeItem(ironIngot)
            val hMeta = helmet.itemMeta; hMeta.isUnbreakable = true; helmet.itemMeta = hMeta
            helmet.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
            player.inventory.helmet = helmet
            val cMeta = chestplate.itemMeta; cMeta.isUnbreakable = true; chestplate.itemMeta = cMeta
            chestplate.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
            player.inventory.chestplate = chestplate
            val lMeta = leggings.itemMeta; lMeta.isUnbreakable = true; leggings.itemMeta = lMeta
            leggings.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
            player.inventory.leggings = leggings
            val bMeta = boots.itemMeta; bMeta.isUnbreakable = true; boots.itemMeta = bMeta
            boots.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
            player.inventory.boots = boots
            val itemMeta = event.inventory.contents[3]?.itemMeta
            val currentLore = itemMeta?.lore()
            currentLore?.add(Component.text(" "))
            currentLore?.add(Component.text(ChatColor.RED.toString() + "You already have this item!"))
            itemMeta?.lore(currentLore)
            event.inventory.contents[3]?.itemMeta = itemMeta
            return true
        }
    }
    private fun buyItem(player: Player,item:ItemStack,itemCost: Int,event: InventoryClickEvent,offhand:Boolean=false,currency: ItemStack=ironIngot) : Boolean {

        if (!player.inventory.containsAtLeast(currency, itemCost)) {
            notEnoughMoney(player)
            event.isCancelled = true
            return false

        } else {
            if ((event.whoClicked as Player).hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                for (online in Bukkit.getOnlinePlayers()) {
                    if (online == (event.whoClicked as Player)) continue
                    online.sendEquipmentChange(
                        (event.whoClicked as Player),
                        EquipmentSlot.HAND,
                        ItemStack(Material.AIR)
                    )
                    online.sendEquipmentChange(
                        (event.whoClicked as Player),
                        EquipmentSlot.OFF_HAND,
                        ItemStack(Material.AIR)
                    )
                }
            }
            currency.amount = itemCost
            player.inventory.removeItem(currency)
            when (item.type) {
                Material.NETHERITE_SWORD, Material.DIAMOND_SWORD,Material.IRON_SWORD,Material.WOODEN_SWORD,Material.STONE_SWORD, Material.SHIELD -> {
                    val iMeta = item.itemMeta
                    iMeta.isUnbreakable = true
                    item.itemMeta = iMeta
                    item.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
                } else -> {

                }
            }
            if (offhand) {
                player.inventory.setItem(40, item)
            }
            else {
                player.inventory.addItem(item)
            }

                for (obj:ItemStack? in event.inventory.contents) {
                    val text = obj?.itemMeta?.lore()?.get(0) ?: continue
                    val cost = (text as TextComponent).content().replace(ChatColor.DARK_PURPLE.toString()+"Cost:"+ChatColor.GOLD.toString()+" ","").replace(" Iron","").replace(" Emeralds","").toInt()
                    if (!player.inventory.containsAtLeast(ironIngot, cost) && obj.itemMeta?.lore()?.get(obj.itemMeta?.lore()?.size!!.minus(1)) !=Component.text(ChatColor.RED.toString() + "You already have this item!") && obj.itemMeta?.lore()?.get(obj.itemMeta?.lore()?.size!!.minus(1)) !=Component.text(ChatColor.RED.toString() + "You don't have enough materials to buy this!") && obj.type != Material.POTION) {
                        val itemMeta = obj.itemMeta
                        val currentLore = itemMeta?.lore()
                        currentLore?.add(Component.text(" "))
                        currentLore?.add(Component.text(ChatColor.RED.toString() + "You don't have enough materials to buy this!"))
                        itemMeta?.lore(currentLore)
                        event.inventory.contents[event.inventory.contents.indexOf(obj)]?.itemMeta = itemMeta
                    }
                    if (!player.inventory.containsAtLeast(emerald, cost) && obj.itemMeta?.lore()?.get(obj.itemMeta?.lore()?.size!!.minus(1)) !=Component.text(ChatColor.RED.toString() + "You already have this item!") && obj.itemMeta?.lore()?.get(obj.itemMeta?.lore()?.size!!.minus(1)) !=Component.text(ChatColor.RED.toString() + "You don't have enough materials to buy this!") && obj.type == Material.POTION) {
                        val itemMeta = obj.itemMeta
                        val currentLore = itemMeta?.lore()
                        currentLore?.add(Component.text(" "))
                        currentLore?.add(Component.text(ChatColor.RED.toString() + "You don't have enough materials to buy this!"))
                        itemMeta?.lore(currentLore)
                        event.inventory.contents[event.inventory.contents.indexOf(obj)]?.itemMeta = itemMeta
                    }
                }
            return true
        }
    }
    @EventHandler
    fun hotkeyOffhandEvent(e: PlayerSwapHandItemsEvent) {
        if (e.player.gameMode != GameMode.CREATIVE) e.isCancelled = true
    }
    @EventHandler
    fun clickEvent(e: InventoryClickEvent) {

        //Check to see if it's the GUI menu
        var purchaseSuccess = false
        if((e.slotType == InventoryType.SlotType.ARMOR || e.slot == 40) && e.clickedInventory?.type == InventoryType.PLAYER && (e.whoClicked as Player).gameMode != GameMode.CREATIVE)
        {
            e.view.cursor = null
            e.isCancelled = true
            return
        }
        if (e.click == ClickType.NUMBER_KEY && e.view.title() ==Component.text("Shop", NamedTextColor.DARK_RED)) {
            e.isCancelled = true
        }
        if (e.view.title() == Component.text("Shop", NamedTextColor.DARK_RED)) {
            if (e.clickedInventory?.type == InventoryType.PLAYER) {
                e.isCancelled = true
                return
            }
            val player = e.whoClicked as Player
            when (e.currentItem?.type) {
                // Blocks/Items
                Material.WHITE_WOOL, Material.GRAY_WOOL, Material.RED_WOOL,Material.BLUE_WOOL,Material.LIME_WOOL,Material.CYAN_WOOL,Material.YELLOW_WOOL,Material.PINK_WOOL -> {
                    purchaseSuccess = buyItem(player,getWoolType(player,woolItem),woolCost,e)
                }
                Material.SHIELD -> {
                    purchaseSuccess = buyItem(player,shieldItem,shieldCost,e,true)
                }
                Material.POTION -> {
                    when ((e.currentItem?.itemMeta as PotionMeta).basePotionData.type) {
                        PotionType.INVISIBILITY -> {
                            purchaseSuccess = buyItem(player, invisPotGiveable, invisPotCost, e, false, emerald)
                        }
                        PotionType.JUMP -> {
                            purchaseSuccess = buyItem(player, jumpPotGiveable, jumpPotCost, e, false, emerald)
                        }
                        PotionType.SPEED -> {
                            purchaseSuccess = buyItem(player, speedPotGiveable, speedPotCost, e, false, emerald)
                        }
                        else -> {

                        }
                    }
                }
                // Weapons
                Material.WOODEN_SWORD -> {
                    purchaseSuccess = buyItem(player,ItemStack(Material.WOODEN_SWORD),swordCost,e)
                }
                Material.STONE_SWORD -> {
                    purchaseSuccess = buyItem(player,ItemStack(Material.STONE_SWORD),swordCost,e)
                }
                Material.IRON_SWORD -> {
                    purchaseSuccess = buyItem(player,ItemStack(Material.IRON_SWORD),swordCost,e)
                }
                Material.DIAMOND_SWORD -> {
                    purchaseSuccess = buyItem(player,ItemStack(Material.DIAMOND_SWORD),swordCost,e)
                }
                Material.NETHERITE_SWORD -> {
                    purchaseSuccess = buyItem(player,ItemStack(Material.NETHERITE_SWORD),swordCost,e)
                }
                
                // Armor
                Material.LEATHER_HELMET -> {
                   purchaseSuccess = buyGear(player, ItemStack(Material.LEATHER_BOOTS), ItemStack(Material.LEATHER_LEGGINGS), ItemStack(Material.LEATHER_CHESTPLATE),ItemStack(Material.LEATHER_HELMET),e)
                }
                Material.CHAINMAIL_HELMET -> {
                    purchaseSuccess = buyGear(player, ItemStack(Material.CHAINMAIL_BOOTS), ItemStack(Material.CHAINMAIL_LEGGINGS), ItemStack(Material.CHAINMAIL_CHESTPLATE),ItemStack(Material.CHAINMAIL_HELMET),e)
                }
                Material.IRON_HELMET -> {
                    purchaseSuccess = buyGear(player, ItemStack(Material.IRON_BOOTS), ItemStack(Material.IRON_LEGGINGS), ItemStack(Material.IRON_CHESTPLATE),ItemStack(Material.IRON_HELMET),e)
                }
                Material.DIAMOND_HELMET -> {
                    purchaseSuccess = buyGear(player, ItemStack(Material.DIAMOND_BOOTS), ItemStack(Material.DIAMOND_LEGGINGS), ItemStack(Material.DIAMOND_CHESTPLATE),ItemStack(Material.DIAMOND_HELMET),e)
                }
                Material.NETHERITE_HELMET -> {
                    purchaseSuccess = buyGear(player, ItemStack(Material.NETHERITE_BOOTS), ItemStack(Material.NETHERITE_LEGGINGS), ItemStack(Material.NETHERITE_CHESTPLATE),ItemStack(Material.NETHERITE_HELMET),e)
                }
                Material.STICK -> {
                    purchaseSuccess = buyItem(player, kbStickItemGiveable, kbStickCost, e)
                }

                else -> {}
            }
            if (purchaseSuccess) {
                player.spawnParticle(Particle.DRAGON_BREATH,player.getTargetBlock(4)!!.location.add(0.5,1.0,0.5) , 200, 0.4, 0.4, 0.4, 0.2)
                player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1.5f)
                e.isCancelled = true //So they cant take the items
            }
        }
        if (e.view.title() == Component.text("Upgrades", NamedTextColor.DARK_AQUA)) {
            if (e.clickedInventory?.type == InventoryType.PLAYER) {
                e.isCancelled = true
                return
            }
            val player = e.whoClicked as Player
            when (e.currentItem?.type) {


                else -> {
                }
            }

            if (purchaseSuccess) {
                player.spawnParticle(
                    Particle.DRAGON_BREATH,
                    player.getTargetBlock(4)!!.location.add(0.5, 1.0, 0.5),
                    200,
                    0.4,
                    0.4,
                    0.4,
                    0.2
                )
                player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1.5f)

            }
            e.isCancelled = true //So they cant take the items
        }
    }

}