package codes.jellyrekt.gconomy.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import codes.jellyrekt.gconomy.gConomy;

public class Messages extends CustomConfig {
	private static Messages instance;
	
	private Messages(JavaPlugin plugin, String filename) throws IOException {
		super(plugin, filename);
	}
	
	private String getMessage(String key) {
		return ChatColor.translateAlternateColorCodes('&', getYaml().getString(key));
	}
	
	public List<String> getMessageList(String key) {
		ArrayList<String> list = new ArrayList<>(getYaml().getStringList(key));
		ArrayList<String> result = new ArrayList<>();
		for (String s : list)
			result.add(ChatColor.translateAlternateColorCodes('&', s));
		return result;
	}
	
	public static String get(String key) {
		return instance.getMessage(key);
	}
	
	public static List<String> getList(String key) {
		return instance.getMessageList(key);
	}
	
	public static void load(gConomy plugin, String filename) throws IOException {
		instance = new Messages(plugin, filename);
	}
}
