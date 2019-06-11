package io.github.thebusybiscuit.cscorelib2.updater;

@FunctionalInterface
public interface UpdateCheck {
	
	boolean hasUpdate(String local, String remote);

}
