package codes.jellyrekt.gconomy.util.yaml;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

public class Messages extends CustomConfig {
	public Messages(JavaPlugin plugin, String filename) {
		super(plugin, filename);
	}
	
	public String get(String key) {
		return getYaml().getString(key);
	}
	
	public List<String> getList(String key) {
		return getYaml().getStringList(key);
	}
}
