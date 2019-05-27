/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mamireimuserver.login_item_fixed;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author mami
 */
public class Login_Item_fixed_main extends JavaPlugin {

    FileConfiguration config;
    String item_type;
    String item_name;
    String item_lore;
    
    /**
     * 起動時処理
     */
    @Override
    public void onEnable() {
        getLogger().info("Login_Item_Fixed enable");
        
        //Config関連
        saveDefaultConfig();
        config = getConfig();
        loadConfig();
        
    }

    /**
     * 終了時処理
     */
    @Override
    public void onDisable() {
        getLogger().info("Login_Item_Fixed disable");
    }
    
    public void loadConfig()
	{
		reloadConfig();
		if(config.get("Item.type")!=null)
		{
			item_type = config.getString("Item.type");
		}
		else
		{
			config.set("Item.type", Material.NETHER_STAR.toString());
		}

		if(config.get("Item.name")!=null)
		{
			item_name = config.getString("Item.name");
		}
		else
		{
			config.set("Item.name", "testName");
		}

		if(config.get("Item.lore")!=null)
		{
			item_lore = config.getString("Item.lore");
		}
		else
		{
			config.set("Item.lore", "lore1%nlore2%nlore3");
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("item"))
		{
			if(args.length==2)
			{
				switch(args[0])
				{
					case "type":
					{
						config.set("Item.type", args[1]);
						saveConfig();
						sender.sendMessage("item.type →" + args[1]);
						break;
					}
					case "name":
					{
						config.set("Item.name", args[1]);
						saveConfig();
						sender.sendMessage("item.name →" + args[1]);
						break;
					}
					case "lore":
					{
						config.set("Item.lore", args[1]);
						saveConfig();
						sender.sendMessage("item.lore →" + args[1]);
						break;
					}
					default:
					{
						sender.sendMessage("/item <type/name/lore> <任意の文字>");
						break;
					}
				}
			}
			else
			{
				sender.sendMessage("引数が少なすぎor多すぎます\n"
						+ "/item <type/name/lore> <任意の文字>");
			}
		}
		if (cmd.getName().equalsIgnoreCase("giveitem"))
		{
			if(args.length >= 1)
			{
				Player player = sender.getServer().getPlayer(args[0]);
				if (player!=null)
				{
					if(item_type!=null)
					{
						PlayerInventory inv = player.getInventory();
						for(int n = 0; n<=35; n++)
						{
							if(inv.getItem(n)!=null
									&& inv.getItem(n).getType().equals(Material.matchMaterial(item_type)))
							{
								inv.setItem(n, new ItemStack(Material.AIR));
							}
						}
						if(inv.getBoots()!=null
								&& inv.getBoots().getType().equals(Material.matchMaterial(item_type)))
						{
							player.getInventory().setBoots(new ItemStack(Material.AIR));
						}
						if(inv.getLeggings()!=null
								&& inv.getLeggings().getType().equals(Material.matchMaterial(item_type)))
						{
							player.getInventory().setLeggings(new ItemStack(Material.AIR));
						}
						if(inv.getChestplate()!=null
								&& inv.getChestplate().getType().equals(Material.matchMaterial(item_type)))
						{
							player.getInventory().setChestplate(new ItemStack(Material.AIR));
						}
						if(inv.getHelmet()!=null
								&& inv.getHelmet().getType().equals(Material.matchMaterial(item_type)))
						{
							player.getInventory().setHelmet(new ItemStack(Material.AIR));
						}
						if(player.getOpenInventory()!=null)
						{
							if(player.getOpenInventory().getCursor()!=null
									&& player.getOpenInventory().getCursor().getType().equals(Material.matchMaterial(item_type)))
							{
								player.getOpenInventory().setCursor(new ItemStack(Material.AIR));
							}
						}

						ItemStack giveItem = new ItemStack(Material.matchMaterial(item_type));
						ItemMeta meta = giveItem.getItemMeta();
						if(item_name!=null)
						{
							meta.setDisplayName(item_name);
						}
						if(item_lore!=null)
						{
							meta.setLore(Arrays.asList(item_lore.split("%n"))); //『%n』で改行される
						}
						giveItem.setItemMeta(meta);
						player.getInventory().addItem(giveItem);
					}
				}
				else
				{
					sender.sendMessage("§6" + args[0] + "§4 is not online.");
				}
			}
			else
			{
				sender.sendMessage("引数が少なすぎます\n"
						+ "/giveitem <player>");
			}
		}
		if (cmd.getName().equalsIgnoreCase("loadconfig"))
		{
			loadConfig();
		}
		return false;
	}
    
    
    
    /**
     * PlayerJoinイベント処理
     * @param event JOINイベント情報
     */
    /*
    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        
        //Configからデータを取得する
        String item_object = getConfig().getString("Item_object");
        String item_name = getConfig().getString("Item_name");
        String lore_text = getConfig().getString("Lore_text");
        

        // プレイヤーにアイテムを作成して渡す
        ItemStack itemStack = new ItemStack(Material.matchMaterial(item_object));
        ItemMeta itemMeta = itemStack.getItemMeta();
        //アイテム名をセット
        itemMeta.setDisplayName("メニュー[Menu]");
        List<String> lore = Arrays.asList(ChatColor.RESET + "ロビーメニューを開きます。", ChatColor.RESET + "Open Lobby Menu");
        //説明文をセット
        itemMeta.setLore(lore);
        //ItemMetaをセットしないと反映されない。
        itemStack.setItemMeta(itemMeta);
        int stack = event.getPlayer().getInventory().getContents(item_object);
        event.getPlayer().getInventory().removeItem(item_object);
        event.getPlayer().getInventory().addItem(itemStack);
    }
    */
}
