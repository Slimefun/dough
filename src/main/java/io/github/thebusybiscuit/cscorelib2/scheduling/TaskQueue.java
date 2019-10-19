package io.github.thebusybiscuit.cscorelib2.scheduling;

import java.util.function.IntConsumer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import lombok.Data;
import lombok.NonNull;

/**
 * This class provides useful utilities to schedule Tasks (sync and async).
 * Tasks are added into a Queue and then run sequentially via {@link TaskQueue#execute(Plugin)}
 * You can provide a delay between the individual tasks via the ticks argument in {@link TaskQueue#thenRun(int, Runnable)}
 * If you need to access the index of your current task (whether it is the first, last or xth task) you can use
 * the methods with {@link IntConsumer} as an argument, otherwise just use the ones with {@link Runnable}
 * 
 * @author TheBusyBiscuit
 *
 */
public class TaskQueue {
	
	@Data
	private class Node {
		
		private final IntConsumer runnable;
		private final boolean asynchronously;
		private int delay = 0;
		private Node nextNode;
		
		public Node(@NonNull IntConsumer consumer, boolean async) {
			this.runnable = consumer;
			this.asynchronously = async;
		}
		
		public Node(@NonNull IntConsumer consumer, int delay, boolean async) {
			this.runnable = consumer;
			this.delay = delay;
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
	 * Use the {@link Integer} parameter in your {@link IntConsumer} to determine the task's index.
	 * 
	 * @param consumer	The callback to run
	 * @return			The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRun(@NonNull IntConsumer consumer) {
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
	 * Use the {@link Integer} parameter in your {@link IntConsumer} to determine the task's index.
	 * 
	 * @param consumer	The callback to run
	 * @return			The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRunAsynchronously(@NonNull IntConsumer consumer) {
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
	 * Use the {@link Integer} parameter in your {@link IntConsumer} to determine the task's index.
	 * 
	 * @param ticks		The time to wait before running this task after the previous one.
	 * @param consumer	The callback to run
	 * @return			The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRun(int ticks, @NonNull IntConsumer consumer) {
		if (ticks < 1) {
			throw new IllegalArgumentException("thenAfter() must be given a time that is greater than zero!");
		}
		
		return append(new Node(consumer, ticks, false));
	}
	
	/**
	 * This method will schedule the given Task with the given delay and <strong>synchronously</strong>.
	 * 
	 * @param ticks		The time to wait before running this task after the previous one.
	 * @param runnable	The callback to run
	 * @return			The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRun(int ticks, @NonNull Runnable runnable) {
		return thenRun(ticks, i -> runnable.run());
	}
	
	/**
	 * This method will schedule the given Task with the given delay and <strong>asynchronously</strong>.
	 * Use the {@link Integer} parameter in your {@link IntConsumer} to determine the task's index.
	 * 
	 * @param ticks		The time to wait before running this task after the previous one.
	 * @param consumer	The callback to run
	 * @return			The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRunAsynchronously(int ticks, @NonNull IntConsumer consumer) {
		if (ticks < 1) {
			throw new IllegalArgumentException("thenAfter() must be given a time that is greater than zero!");
		}
		
		return append(new Node(consumer, ticks, true));
	}

	/**
	 * This method will schedule the given Task with the given delay and <strong>synchronously</strong>.
	 * 
	 * @param ticks		The time to wait before running this task after the previous one.
	 * @param runnable	The callback to run
	 * @return			The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRunAsynchronously(int ticks, @NonNull Runnable runnable) {
		return thenRunAsynchronously(ticks, i -> runnable.run());
	}

	/**
	 * This method will schedule the given Task with no delay and <strong>synchronously</strong>.
	 * The task will be repeated for the given amount of iterations.
	 * Use the {@link Integer} parameter in your {@link IntConsumer} to determine the task's index.
	 * 
	 * @param iterations	The amount of times to repeat this task
	 * @param consumer		The callback to run
	 * @return				The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRepeat(int iterations, @NonNull IntConsumer consumer) {
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
	 * Use the {@link Integer} parameter in your {@link IntConsumer} to determine the task's index.
	 * 
	 * @param iterations	The amount of times to repeat this task
	 * @param consumer		The callback to run
	 * @return				The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRepeatAsynchronously(int iterations, @NonNull IntConsumer consumer) {
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
	 * This method will schedule the given Task with the given delay and <strong>synchronously</strong>.
	 * The task will be repeated for the given amount of iterations.
	 * Use the {@link Integer} parameter in your {@link IntConsumer} to determine the task's index.
	 * 
	 * @param ticks			The delay between executions (including the start delay)
	 * @param iterations	The amount of times to repeat this task
	 * @param consumer		The callback to run
	 * @return				The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRepeatEvery(int ticks, int iterations, @NonNull IntConsumer consumer) {
		if (ticks < 1) {
			throw new IllegalArgumentException("thenRepeatEvery() must be given a time that is greater than zero!");
		}
		
		for (int i = 0; i < iterations; i++) {
			append(new Node(consumer, ticks, false));
		}
		
		return this;
	}
	
	/**
	 * This method will schedule the given Task with the given delay and <strong>synchronously</strong>.
	 * The task will be repeated for the given amount of iterations.
	 * 
	 * @param ticks			The delay between executions (including the start delay)
	 * @param iterations	The amount of times to repeat this task
	 * @param runnable		The callback to run
	 * @return				The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRepeatEvery(int ticks, int iterations, @NonNull Runnable runnable) {
		return thenRepeatEvery(ticks, iterations, i -> runnable.run());
	}
	
	/**
	 * This method will schedule the given Task with the given delay and <strong>asynchronously</strong>.
	 * The task will be repeated for the given amount of iterations.
	 * Use the {@link Integer} parameter in your {@link IntConsumer} to determine the task's index.
	 * 
	 * @param delay			The delay between executions (including the start delay)
	 * @param iterations	The amount of times to repeat this task
	 * @param consumer		The callback to run
	 * @return				The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRepeatEveryAsynchronously(int ticks, int iterations, @NonNull IntConsumer consumer) {
		if (ticks < 1) {
			throw new IllegalArgumentException("thenRepeatEveryAsynchronously() must be given a time that is greater than zero!");
		}
		
		for (int i = 0; i < iterations; i++) {
			append(new Node(consumer, ticks, true));
		}
		
		return this;
	}
	
	/**
	 * This method will schedule the given Task with the given delay and <strong>asynchronously</strong>.
	 * The task will be repeated for the given amount of iterations.
	 * 
	 * @param ticks			The delay between executions (including the start delay)
	 * @param iterations	The amount of times to repeat this task
	 * @param runnable		The callback to run
	 * @return				The current instance of {@link TaskQueue}
	 */
	public TaskQueue thenRepeatEveryAsynchronously(int ticks, int iterations, @NonNull Runnable runnable) {
		return thenRepeatEveryAsynchronously(ticks, iterations, i -> runnable.run());
	}
	
	/**
	 * This method will make the task run the given callback until eternity.
	 * The task will be run with no delay and <strong>synchronously</strong>.
	 * Do not add other tasks after calling this method.
	 * 
	 * @param consumer	The callback to run
	 */
	public void thenLoop(@NonNull IntConsumer consumer) {
		Node node = new Node(consumer, false);
		node.setNextNode(node);
		append(node);
	}
	
	/**
	 * This method will make the task run the given callback until eternity.
	 * The task will be run with no delay and <strong>synchronously</strong>.
	 * Do not add other tasks after calling this method.
	 * 
	 * @param runnable	The callback to run
	 */
	public void thenLoop(@NonNull Runnable runnable) {
		thenLoop(i -> runnable.run());
	}
	
	/**
	 * This method will make the task run the given callback until eternity.
	 * The task will be run with no delay and <strong>asynchronously</strong>.
	 * Do not add other tasks after calling this method.
	 * 
	 * @param consumer	The callback to run
	 */
	public void thenLoopAsynchronously(@NonNull IntConsumer consumer) {
		Node node = new Node(consumer, true);
		node.setNextNode(node);
		append(node);
	}
	
	/**
	 * This method will make the task run the given callback until eternity.
	 * The task will be run with no delay and <strong>asynchronously</strong>.
	 * Do not add other tasks after calling this method.
	 * 
	 * @param runnable	The callback to run
	 */
	public void thenLoopAsynchronously(@NonNull Runnable runnable) {
		thenLoopAsynchronously(i -> runnable.run());
	}
	
	/**
	 * This method will make the task run the given callback until eternity.
	 * The task will be run with the given delay and <strong>synchronously</strong>.
	 * Do not add other tasks after calling this method.
	 * 
	 * @param ticks		The delay between executions (including the start delay)
	 * @param consumer	The callback to run
	 */
	public void thenLoopEvery(int ticks, @NonNull IntConsumer consumer) {
		if (ticks < 1) {
			throw new IllegalArgumentException("thenLoopEvery() must be given a time that is greater than zero!");
		}
		
		Node node = new Node(consumer, ticks, false);
		node.setNextNode(node);
		append(node);
	}
	
	/**
	 * This method will make the task run the given callback until eternity.
	 * The task will be run with the given delay and <strong>synchronously</strong>.
	 * Do not add other tasks after calling this method.
	 * 
	 * @param ticks		The delay between executions (including the start delay)
	 * @param runnable	The callback to run
	 */
	public void thenLoopEvery(int ticks, @NonNull Runnable runnable) {
		thenLoopEvery(ticks, i -> runnable.run());
	}
	
	/**
	 * This method will make the task run the given callback until eternity.
	 * The task will be run with the given delay and <strong>asynchronously</strong>.
	 * Do not add other tasks after calling this method.
	 * 
	 * @param ticks		The delay between executions (including the start delay)
	 * @param consumer	The callback to run
	 */
	public void thenLoopEveryAsynchronously(int ticks, @NonNull IntConsumer consumer) {
		if (ticks < 1) {
			throw new IllegalArgumentException("thenLoopEveryAsynchronously() must be given a time that is greater than zero!");
		}
		
		Node node = new Node(consumer, ticks, true);
		node.setNextNode(node);
		append(node);
	}
	
	/**
	 * This method will make the task run the given callback until eternity.
	 * The task will be run with the given delay and <strong>asynchronously</strong>.
	 * Do not add other tasks after calling this method.
	 * 
	 * @param ticks		The delay between executions (including the start delay)
	 * @param runnable	The callback to run
	 */
	public void thenLoopEveryAsynchronously(int ticks, @NonNull Runnable runnable) {
		thenLoopEveryAsynchronously(ticks, i -> runnable.run());
	}

	/**
	 * This method will make the Queue just do nothing for the given amount of ticks.
	 * You should not really be using this method but it exists.
	 * 
	 * @param ticks		The amount of ticks to wait for
	 * @return			The current instance of {@link TaskQueue}
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

			while (current.hasNextNode()) {
				if (current == current.getNextNode()) {
					throw new IllegalAccessError("You cannot append to a TaskQueue that contains a loop");
				}
				
				current = current.getNextNode();
			}
			
			current.setNextNode(node);
		}
		
		return this;
	}

}
