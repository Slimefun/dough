package io.github.thebusybiscuit.cscorelib2.chat.json;

public enum ClickEventAction {
	
	SUGGEST_COMMAND,
	RUN_COMMAND,
	OPEN_URL;
	
	@Override
	public String toString() {
		return name().toLowerCase();
	}

}
