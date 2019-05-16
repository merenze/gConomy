package codes.jellyrekt.gconomy.util.yaml;

import java.io.File;

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
	 */
	protected CustomConfig(JavaPlugin plugin, String filename) {
		this.path = plugin.getDataFolder() + File.separator + "data" + filename + ".yml";
		// Remaining instantiations
		path += File.separator + filename + ".yml";
		this.plugin = plugin;
		this.file = new File(path);
		this.config = YamlConfiguration.loadConfiguration(file);
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
	protected YamlConfiguration getYaml() {
		return config;
	}
}
