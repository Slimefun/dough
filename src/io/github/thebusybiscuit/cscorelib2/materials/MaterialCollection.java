package io.github.thebusybiscuit.cscorelib2.materials;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.bukkit.Material;

import lombok.Getter;

public class MaterialCollection {
	
	@Getter
	private final Material[] asArray;
	
	public MaterialCollection(Set<Material> materials) {
		this(materials.stream());
	}
	
	public MaterialCollection(Material... materials) {
		this(Arrays.stream(materials));
	}
	
	public MaterialCollection(Stream<Material> stream) {
		this.asArray = stream.distinct().toArray(Material[]::new);
	}
	
	public MaterialCollection merge(MaterialCollection collection) {
		return new MaterialCollection(Stream.concat(stream(), collection.stream()));
	}
	
	public Stream<Material> stream() {
		return Arrays.stream(asArray);
	}

	public int size() {
		return asArray.length;
	}
	
	public boolean isEmpty() {
		return asArray.length == 0;
	}

	public boolean contains(Material type) {
		return stream().anyMatch(material -> material == type);
	}
	
	public boolean containsAll(Collection<Material> materials) {
		return materials.stream().allMatch(this::contains);
	}
	
	public Iterator<Material> iterator() {
		return stream().iterator();
	}
	
	public void forEach(Consumer<Material> consumer) {
		stream().forEach(consumer);
	}

}
