package codes.jellyrekt.gconomy.util.yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Wrapper class for org.bukkit.configuration.file.FileConfiguration
 * 
 * @author JellyRekt
 *
 */
public class CustomConfig {
	private String path;
	private File file;
	private YamlConfiguration config;
	private JavaPlugin plugin;

	/**
	 * Creates a new YAML file
	 * 
	 * @param plugin Plugin the file is being created for
	 * @param name   Name of the file; must include ".yml"
	 */
	public CustomConfig(JavaPlugin plugin, File file) {
		this.plugin = plugin;
		this.file = file;
		this.config = YamlConfiguration.loadConfiguration(file);
		this.path = plugin.getDataFolder() + File.separator + file.getName();
	}

	/**
	 * Saves the YAML file
	 */
	public void save() {
		if (config == null || file == null)
			return;
		try {
			config.save(file);
		} catch (IOException ex) {
			plugin.getLogger().info(ex.getStackTrace().toString());
		}
	}

	/**
	 * Loads the config.
	 */
	public void load() {
		if (file == null)
			file = new File(path);
		if (config == null)
			config = YamlConfiguration.loadConfiguration(file);
		Reader defConfigStream;
		try {
			defConfigStream = new InputStreamReader(plugin.getResource(path), "UTF8");
			if (defConfigStream != null)
				config.setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));
		} catch (UnsupportedEncodingException ex) {
			plugin.getLogger().info(ex.getStackTrace().toString());
		}
	}

	public void reset() {
		plugin.saveResource(file.getName(), true);
	}

	/**
	 * Returns the YamlConfiguration for the CustomConfig
	 */
	public YamlConfiguration getYaml() {
		return config;
	}

	/**
	 * Gets the CustomConfig or creates a new one.
	 * 
	 * @param plugin   The plugin getting the config.
	 * @param filename The name of the file; ends in .yml
	 */
	public static CustomConfig get(JavaPlugin plugin, String filename) {
		String path = plugin.getDataFolder() + File.separator + filename;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
			plugin.saveResource(filename, true);
		}
		return new CustomConfig(plugin, file);
	}
}
