package io.github.thebusybiscuit.cscorelib2.data;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import lombok.Cleanup;

public final class PersistentYAMLDataType implements PersistentDataType<String, FileConfiguration> {

	public static final PersistentYAMLDataType CONFIG = new PersistentYAMLDataType();
	
	private PersistentYAMLDataType() {}
	
	@Override
	public Class<String> getPrimitiveType() {
		return String.class;
	}

	@Override
	public Class<FileConfiguration> getComplexType() {
		return FileConfiguration.class;
	}

	@Override
	public String toPrimitive(FileConfiguration complex, PersistentDataAdapterContext context) {
		return complex.saveToString();
	}
	
	@Override
	public FileConfiguration fromPrimitive(String primitive, PersistentDataAdapterContext context) {
		try {
			@Cleanup
			Reader reader = new StringReader(primitive);
			return YamlConfiguration.loadConfiguration(reader);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
