package io.github.thebusybiscuit.cscorelib2.debugging;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import lombok.Getter;
import lombok.NonNull;

public class MemoryDump {
	
	@Getter
	Predicate<String> namespaces;
	
	@Getter
	private final Map<String, Integer> heatmap = new HashMap<>();
	private final Set<Integer> hashes = new HashSet<>();
	
	public MemoryDump(@NonNull String file, @NonNull String namespace, Object... objects) throws FileNotFoundException {
		this(file, str -> str.startsWith(namespace), objects);
	}
	
	public MemoryDump(@NonNull String file, @NonNull Predicate<String> includeNamespaces, Object... objects) throws FileNotFoundException {
		namespaces = includeNamespaces;
		
		if (objects.length == 0) {
			throw new IllegalArgumentException("You need to provide at least one Object!");
		}
		
		try (PrintStream stream = new PrintStream(file)) {
			for (Object obj: objects) {
				dump(stream, "", "", obj);
			}
			
			stream.println();
			stream.println();
			stream.println("---- Heatmap ----");
			heatmap.entrySet().stream().sorted((a, b) -> Integer.compare(b.getValue(), a.getValue())).forEach(entry -> {
				stream.println(entry.getValue() + "x " + entry.getKey());
			});
		}
		finally {
			// No need to keep those in memory...
			hashes.clear();
		}
	}

	private void dump(@NonNull PrintStream stream, @NonNull String prefix, @NonNull String name, Object obj) {
		if (obj == null) {
			stream.println(prefix + "null");
		}
		else if (hashes.add(obj.hashCode())) {
			try {
				heatmap.merge(obj.getClass().getName(), 1, Integer::sum);
				stream.println(prefix + name + ": [" + obj.hashCode() + "] " + obj.getClass().getName() + " : " + obj.toString());
				
				if (obj instanceof Collection) {
					if (!((Collection<?>) obj).isEmpty()) {
						AtomicInteger integer = new AtomicInteger(0);
						((Collection<?>) obj).iterator().forEachRemaining(item -> {
							dump(stream, prefix + " ", String.valueOf(integer.incrementAndGet()), item);
						});
					}
				}
				else if (obj instanceof Map) {
					if (!((Map<?, ?>) obj).isEmpty()) {
						((Map<?,?>) obj).entrySet().forEach(entry -> {
							dump(stream, prefix + " ", entry.getKey() + "", entry.getValue());
						});
					}
				}
				else if (namespaces.test(obj.getClass().getName())) {
					for (Field field: obj.getClass().getDeclaredFields()) {
						field.setAccessible(true);
						try {
							dump(stream, prefix + "  ", field.getName(), field.get(obj));
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
			catch(NoClassDefFoundError x) {
				System.err.println(x.getClass().getSimpleName() + ": " + x.getMessage());
			}
		}
	}

}
