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

public class MemoryDump {
	
	private String namespace;
	private Map<String, Integer> heatmap = new HashMap<>();
	private Set<Integer> hashes = new HashSet<>();
	
	public MemoryDump(String file, String namespace, Object... objects) throws FileNotFoundException, IllegalArgumentException, IllegalAccessException {
		this.namespace = namespace;
		
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
	}

	private void dump(PrintStream stream, String prefix, String name, Object obj) throws IllegalArgumentException, IllegalAccessException {
		if (obj == null) {
			stream.println(prefix + "null");
		}
		else if (hashes.add(obj.hashCode())) {
			try {
				heatmap.merge(obj.getClass().getName(), 1, (a, b) -> a + b);
				stream.println(prefix + name + ": [" + obj.hashCode() + "] " + obj.getClass().getName() + " : " + obj.toString());
				
				if (obj instanceof Collection) {
					if (!((Collection<?>) obj).isEmpty()) {
						AtomicInteger integer = new AtomicInteger(0);
						((Collection<?>) obj).iterator().forEachRemaining(item -> {
							try {
								dump(stream, prefix + " ", String.valueOf(integer.incrementAndGet()), item);
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
						});
					}
				}
				else if (obj instanceof Map) {
					if (!((Map<?, ?>) obj).isEmpty()) {
						((Map<?,?>) obj).entrySet().forEach(entry -> {
							try {
								dump(stream, prefix + " ", entry.getKey() + "", entry.getValue());
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
						});
					}
				}
				else if (obj.getClass().getName().startsWith(namespace)) {
					for (Field field: obj.getClass().getDeclaredFields()) {
						field.setAccessible(true);
						dump(stream, prefix + "  ", field.getName(), field.get(obj));
					}
				}
			}
			catch(NoClassDefFoundError x) {
				System.err.println(x.getClass().getSimpleName() + ": " + x.getMessage());
			}
		}
	}

}
