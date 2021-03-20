package ru.viterg.restavote.util.exception;

public class ErrorInfo {
    private final String    url;
    private final ErrorType type;
    private final String    typeMessage;
    private final String[]  details;

    public ErrorInfo(CharSequence url, ErrorType type, String typeMessage, String... details) {
        this.url = url.toString();
        this.type = type;
        this.typeMessage = typeMessage;
        this.details = details;
    }

    public String getUrl() {
        return url;
    }

    public ErrorType getType() {
        return type;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    public String[] getDetails() {
        return details;
    }
}