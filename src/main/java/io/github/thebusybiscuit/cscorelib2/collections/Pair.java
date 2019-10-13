package io.github.thebusybiscuit.cscorelib2.collections;

import java.util.Map;

import lombok.Data;

@Data
public class Pair<P, S> {
	
	private P firstValue;
	private S secondValue;
	
	public Pair(P a, S b) {
		this.firstValue = a;
		this.secondValue = b;
	}
	
	public Pair(Map.Entry<P, S> mapEntry) {
		this(mapEntry.getKey(), mapEntry.getValue());
	}

}
