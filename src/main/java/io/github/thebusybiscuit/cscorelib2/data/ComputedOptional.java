package io.github.thebusybiscuit.cscorelib2.data;

import java.util.function.Consumer;
import java.util.function.Supplier;

import lombok.NonNull;

public final class ComputedOptional<T> {
	
	private OptionalState state;
	private T value;
	
	private ComputedOptional(OptionalState state) {
		this.state = state;
	}
	
	private ComputedOptional(T value) {
		state = OptionalState.COMPUTED;
		this.value = value;
	}

	public static <T> ComputedOptional<T> empty() {
		return new ComputedOptional<>(OptionalState.EMPTY);
	}
	
	public static <T> ComputedOptional<T> createNew() {
		return new ComputedOptional<>(OptionalState.NOT_COMPUTED);
	}
	
	public static <T> ComputedOptional<T> of(@NonNull T value) {
		return new ComputedOptional<>(value);
	}
	
	public static <T> ComputedOptional<T> ofNullable(T value) {
		if (value == null) return empty();
		else return new ComputedOptional<>(value);
	}
	
	public boolean isEmpty() {
		return state == OptionalState.EMPTY;
	}
	
	public boolean isComputed() {
		return state != OptionalState.NOT_COMPUTED;
	}
	
	public boolean isPresent() {
		return state == OptionalState.COMPUTED;
	}
	
	public void ifPresent(Consumer<T> consumer) {
		if (isPresent()) consumer.accept(value);
	}
	
	public void compute(T value) {
		if (isComputed()) throw new IllegalStateException("This Optional has already been computed.");
		
		if (value == null) {
			state = OptionalState.EMPTY;
		}
		else {
			state = OptionalState.COMPUTED;
			this.value = value;
		}
	}
	
	public void compute(@NonNull Supplier<T> supplier) {
		compute(supplier.get());
	}
	
	public T get() {
		if (!isPresent()) throw new IllegalAccessError("This Optional has no value! Check .isPresent() first!");
		
		return value;
	}
	
	public T getOrElse(T value) {
		if (!isComputed()) throw new IllegalStateException("This Optional has not yet been computed!");
		
		return isPresent() ? this.value: value;
	}

}
