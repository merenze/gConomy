package codes.jellyrekt.gconomy.util.yaml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Messages extends CustomConfig {
	public Messages(JavaPlugin plugin, String filename) throws IOException {
		super(plugin, filename);
	}
	
	public String get(String key) {
		return ChatColor.translateAlternateColorCodes('&', (getYaml().getString(key)));
	}
	
	public List<String> getList(String key) {
		ArrayList<String> list = new ArrayList<>(getYaml().getStringList(key));
		ArrayList<String> result = new ArrayList<>();
		for (String s : list)
			result.add(ChatColor.translateAlternateColorCodes('&', s));
		return result;
	}
}
