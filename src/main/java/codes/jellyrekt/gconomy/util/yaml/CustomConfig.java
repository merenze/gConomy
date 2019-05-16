package codes.jellyrekt.gconomy.util.yaml;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Wrapper class for org.bukkit.configuration.file.FileConfiguration
 * 
 * @author JellyRekt
 *
 */
public abstract class CustomConfig {
	private String path;
	private File file;
	private YamlConfiguration config;
	private JavaPlugin plugin;
	/**
	 * Creates a new YAML file or loads the existing one.
	 * 
	 * @param plugin Plugin the file is being created for
	 * @param name   Name of the file. Exclude extension.
	 * @throws IOException 
	 */
	protected CustomConfig(JavaPlugin p, String filename) throws IOException {
		this (p, "", filename);
	}
	
	protected CustomConfig(JavaPlugin p, String dirpath, String filename) throws IOException {
		plugin = p;
		path = plugin.getDataFolder().toString();
		if (!dirpath.isEmpty())
			path += File.separator + dirpath;
		File dir = new File(path);
		if (!dir.exists())
			dir.mkdirs();
		path += File.separator + filename + ".yml";
		file = new File(path);
		file.createNewFile();
		config = YamlConfiguration.loadConfiguration(file);
	}
	/**
	 * Returns the running instance of this plugin.
	 * @return instance of gConomy
	 */
	protected JavaPlugin plugin() {
		return plugin;
	}
	/**
	 * Returns the YamlConfiguration for the CustomConfig
	 * @return config
	 */
	public YamlConfiguration getYaml() {
		return config;
	}
	/**
	 * Reload defaults from resource
	 * @param filepath Path to default file  
	 */
	public void reloadDefaults(String filepath) {
		plugin.saveResource(filepath, true);
	}
}
