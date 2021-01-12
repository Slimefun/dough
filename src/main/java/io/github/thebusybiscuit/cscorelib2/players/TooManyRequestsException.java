package io.github.thebusybiscuit.cscorelib2.players;

import java.net.URL;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TooManyRequestsException extends Exception {

    private static final long serialVersionUID = -7137562700404366948L;
    private final URL url;

    @Override
    public String getMessage() {
        return "Sent too many Requests to the Server! URL: " + url;
    }
}