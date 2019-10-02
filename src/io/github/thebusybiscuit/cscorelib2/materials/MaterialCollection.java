package io.github.thebusybiscuit.cscorelib2.materials;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.bukkit.Material;

import lombok.Getter;
import lombok.NonNull;

/**
 * A read-only collection of {@link Material}.
 * Internally represented by an Array of {@link Material}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class MaterialCollection {
	
	@Getter
	private final Material[] asArray;
	
	public MaterialCollection(Set<Material> materials) {
		this(materials.stream());
	}
	
	public MaterialCollection(Material... materials) {
		this(Arrays.stream(materials));
	}
	
	public MaterialCollection(@NonNull Stream<Material> stream) {
		this.asArray = stream
				.distinct()
				.filter(m -> m != null)
				.toArray(Material[]::new);
	}
	
	public MaterialCollection merge(@NonNull MaterialCollection collection) {
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
	
	public Material get(int index) {
		return asArray[index];
	}

	public boolean contains(Material type) {
		if (type == null) return false;
		return stream().anyMatch(material -> material == type);
	}
	
	public boolean containsAll(@NonNull Collection<Material> materials) {
		return materials.stream().allMatch(this::contains);
	}
	
	public boolean containsAll(@NonNull MaterialCollection materials) {
		return materials.stream().allMatch(this::contains);
	}
	
	public Iterator<Material> iterator() {
		return stream().iterator();
	}
	
	public void forEach(@NonNull Consumer<Material> consumer) {
		stream().forEach(consumer);
	}

}
