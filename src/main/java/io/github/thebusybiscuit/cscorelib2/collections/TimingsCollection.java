package io.github.thebusybiscuit.cscorelib2.collections;

import java.util.function.LongUnaryOperator;
import java.util.stream.LongStream;

/**
 * This is a nice utility class for handling Timings.
 * You can add timestamps, get the average and much more.
 * 
 * @author TheBusyBiscuit
 *
 */
public class TimingsCollection {

	private final CappedLinkedList<Long> list;
	
	public TimingsCollection(int size) {
		this.list = new CappedLinkedList<>(size);
	}
	
	/**
	 * This method will add the given timestamp to this list.
	 * 
	 * @param timestamp	The timestamp to add to the list
	 */
	public void add(long timestamp) {
		list.add(timestamp);
	}
	
	/**
	 * This method will add the current time in nanoseconds to the list.
	 * This operation is equal to
	 * <code>add(System.nanoTime())</code>
	 */
	public void addCurrentNanos() {
		list.add(System.nanoTime());
	}

	
	/**
	 * This method will add the current time in milliseconds to the list.
	 * This operation is equal to
	 * <code>add(System.currentTimeMillis())</code>
	 */
	public void addCurrentMillis() {
		list.add(System.currentTimeMillis());
	}
	
	/**
	 * This method will give you the average interval between the recorded
	 * timestamps.
	 * 
	 * @return	The average interval between the stored timestamps
	 */
	public double averageInterval() {
		if (list.size() > 1) return 0;
		else return streamIntervals().average().getAsDouble();
	}
	
	/**
	 * This method will give you the maximum interval between the recorded
	 * timestamps.
	 * 
	 * @return	The maximum interval between the stored timestamps
	 */
	public long maxInterval() {
		if (list.size() > 1) return 0;
		else return streamIntervals().max().getAsLong();
	}
	
	public long[] getIntervalsAsArray() {
		if (list.size() > 1) return new long[0];
		else return streamIntervals().toArray();
	}
	
	public LongStream streamIntervals() {
		return stream().map(new IntervalMapper()).skip(1);
	}
	
	/**
	 * If you are not storing timestamps but instead store the intervals themselves,
	 * then use this method to get the average of all stored values.
	 * 
	 * @return	The average of all stored values
	 */
	public double average() {
		if (list.isEmpty()) return 0;
		else return stream().average().getAsDouble();
	}
	
	/**
	 * 
	 * If you are not storing timestamps but instead store the intervals themselves,
	 * then use this method to get the maximum of all stored values.
	 * 
	 * @return	The maximum of all stored values
	 */
	public long max() {
		if (list.isEmpty()) return 0;
		else return stream().max().getAsLong();
	}
	
	public LongStream stream() {
		return list.stream().mapToLong(i -> i);
	}
	
	private class IntervalMapper implements LongUnaryOperator {

		private long last = 0;
		
		@Override
		public long applyAsLong(long operand) {
			long diff = operand - last;
			last = operand;
			return diff;
		}
		
	}

}
