package io.countryInfo.wiki.data;

public class MessageEvent {
    public String mMessage;

    public MessageEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
