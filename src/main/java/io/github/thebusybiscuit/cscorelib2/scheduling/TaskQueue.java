package io.github.thebusybiscuit.cscorelib2.scheduling;

import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import lombok.Data;
import lombok.NonNull;

/**
 * This class provides useful utilities to schedule Tasks (sync and async).
 * Tasks are added into a Queue and then run sequentially via {@link TaskQueue#execute(Plugin)}
 * You can provide a delay between the individual tasks via the ticks argument in {@link TaskQueue#thenRun(int, Runnable)}
 * If you need to access the index of your current task (whether it is the first, last or xth task) you can use
 * the methods with {@link Consumer} as an argument, otherwise just use the ones with {@link Runnable}
 * 
 * @author TheBusyBiscuit
 *
 */
public class TaskQueue {
	
	@Data
	private class Node {
		
		private final Consumer<Integer> runnable;
		private final boolean asynchronously;
		private int delay = 0;
		private Node nextNode;
		
		public Node(@NonNull Consumer<Integer> consumer, boolean async) {
			this.runnable = consumer;
			this.asynchronously = async;
		}

		public boolean hasNextNode() {
			return nextNode != null;
		}
		
	}
	
	private Node head;
	
	/**
	 * Use this method to execute the final Task Queue.
	 * You should add the tasks before-hand.
	 * An {@link IllegalStateException} will be thrown if the queue is empty.
	 * 
	 * @param plugin	The plugin that is performing this execution
	 */
	public void execute(@NonNull Plugin plugin) {
		if (head == null) {
			throw new IllegalStateException("Cannot execute TaskQueue, no head was found");
		}
		
		run(plugin, head, 0);
	}
	
	private void run(Plugin plugin, Node node, int index) {
		if (node == null) return;
		
		Runnable runnable = () -> {
			node.getRunnable().accept(index);
			run(plugin, node.getNextNode(), index + 1);
		};
		
		if (node.isAsynchronously()) {
			if (node.getDelay() > 0) {
				Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, node.getDelay());
			}
			else {
				Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
			}
		}
		else {
			if (node.getDelay() > 0) {
				Bukkit.getScheduler().runTaskLater(plugin, runnable, node.getDelay());
			}
			else {
				Bukkit.getScheduler().runTask(plugin, runnable);
			}
		}
	}
	
	/**
	 * This method will schedule the given Task with no delay and <strong>synchronously</strong>.
	 * Use the {@link Integer} parameter in your {@link Consumer} to determine the task's index.
	 * 
	 * @param consumer	The callback to run
	 * @return			The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRun(@NonNull Consumer<Integer> consumer) {
		return append(new Node(consumer, false));
	}

	/**
	 * This method will schedule the given Task with no delay and <strong>synchronously</strong>.
	 * 
	 * @param runnable	The callback to run
	 * @return			The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRun(@NonNull Runnable runnable) {
		return thenRun(i -> runnable.run());
	}
	
	/**
	 * This method will schedule the given Task with no delay and <strong>asynchronously</strong>.
	 * Use the {@link Integer} parameter in your {@link Consumer} to determine the task's index.
	 * 
	 * @param consumer	The callback to run
	 * @return			The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRunAsynchronously(@NonNull Consumer<Integer> consumer) {
		return append(new Node(consumer, true));
	}

	/**
	 * This method will schedule the given Task with no delay and <strong>asynchronously</strong>.
	 * 
	 * @param runnable	The callback to run
	 * @return			The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRunAsynchronously(@NonNull Runnable runnable) {
		return thenRunAsynchronously(i -> runnable.run());
	}

	/**
	 * This method will schedule the given Task with the given delay and <strong>synchronously</strong>.
	 * Use the {@link Integer} parameter in your {@link Consumer} to determine the task's index.
	 * 
	 * @param ticks		The time to wait before running this task after the previous one.
	 * @param consumer	The callback to run
	 * @return			The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRun(int ticks, @NonNull Consumer<Integer> consumer) {
		if (ticks < 1) {
			throw new IllegalArgumentException("thenAfter() must be given a time that is greater than zero!");
		}
		
		Node node = new Node(consumer, false);
		node.setDelay(ticks);
		return append(node);
	}
	
	/**
	 * This method will schedule the given Task with the given delay and <strong>synchronously</strong>.
	 * 
	 * @param ticks		The time to wait before running this task after the previous one.
	 * @param runnable	The callback to run
	 * @return			The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRun(int ticks,@NonNull Runnable runnable) {
		return thenRun(ticks, i -> runnable.run());
	}
	
	/**
	 * This method will schedule the given Task with the given delay and <strong>asynchronously</strong>.
	 * Use the {@link Integer} parameter in your {@link Consumer} to determine the task's index.
	 * 
	 * @param ticks		The time to wait before running this task after the previous one.
	 * @param consumer	The callback to run
	 * @return			The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRunAsynchronously(int ticks, @NonNull Consumer<Integer> consumer) {
		if (ticks < 1) {
			throw new IllegalArgumentException("thenAfter() must be given a time that is greater than zero!");
		}
		
		Node node = new Node(consumer, true);
		node.setDelay(ticks);
		return append(node);
	}

	/**
	 * This method will schedule the given Task with the given delay and <strong>synchronously</strong>.
	 * 
	 * @param ticks		The time to wait before running this task after the previous one.
	 * @param runnable	The callback to run
	 * @return			The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRunAsynchronously(int ticks,@NonNull Runnable runnable) {
		return thenRunAsynchronously(ticks, i -> runnable.run());
	}

	/**
	 * This method will schedule the given Task with no delay and <strong>synchronously</strong>.
	 * The task will be repeated for the given amount of iterations.
	 * Use the {@link Integer} parameter in your {@link Consumer} to determine the task's index.
	 * 
	 * @param iterations	The amount of times to repeat this task
	 * @param consumer		The callback to run
	 * @return				The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRepeat(int iterations, @NonNull Consumer<Integer> consumer) {
		for (int i = 0; i < iterations; i++) {
			append(new Node(consumer, false));
		}
		
		return this;
	}
	
	/**
	 * This method will schedule the given Task with no delay and <strong>synchronously</strong>.
	 * The task will be repeated for the given amount of iterations.
	 * 
	 * @param iterations	The amount of times to repeat this task
	 * @param runnable		The callback to run
	 * @return				The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRepeat(int iterations, @NonNull Runnable runnable) {
		return thenRepeat(iterations, i -> runnable.run());
	}
	
	/**
	 * This method will schedule the given Task with no delay and <strong>asynchronously</strong>.
	 * The task will be repeated for the given amount of iterations.
	 * Use the {@link Integer} parameter in your {@link Consumer} to determine the task's index.
	 * 
	 * @param iterations	The amount of times to repeat this task
	 * @param consumer		The callback to run
	 * @return				The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRepeatAsynchronously(int iterations, @NonNull Consumer<Integer> consumer) {
		for (int i = 0; i < iterations; i++) {
			append(new Node(consumer, true));
		}
		
		return this;
	}
	
	/**
	 * This method will schedule the given Task with no delay and <strong>asynchronously</strong>.
	 * The task will be repeated for the given amount of iterations.
	 * 
	 * @param iterations	The amount of times to repeat this task
	 * @param runnable		The callback to run
	 * @return				The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRepeatAsynchronously(int iterations, @NonNull Runnable runnable) {
		return thenRepeatAsynchronously(iterations, i -> runnable.run());
	}

	/**
	 * This method will make the Queue just do nothing for the given amount of ticks.
	 * You should not really be using this method but it exists.
	 * 
	 * @param ticks
	 * @return
	 */
	public TaskQueue thenWait(int ticks) {
		Node node = new Node(i -> {}, false);
		node.setDelay(ticks);
		return append(node);
	}
	
	private TaskQueue append(Node node) {
		if (head == null) {
			head = node;
		}
		else {
			Node current = head;

			while (current.hasNextNode()) current = current.getNextNode();
			
			current.setNextNode(node);
		}
		
		return this;
	}

}
