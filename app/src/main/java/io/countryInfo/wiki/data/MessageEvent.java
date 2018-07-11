package io.countryInfo.wiki.data;

public class MessageEvent {
    private String mMessage;

    public MessageEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
