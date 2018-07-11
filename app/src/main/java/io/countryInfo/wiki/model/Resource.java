package io.countryInfo.wiki.model;

public final class Resource {
    private CountryInfo countryInfo;
    private Status status;
    private String message;

    public Resource(CountryInfo countryInfo, Status status, String message) {
        this.countryInfo = countryInfo;
        this.status = status;
        this.message = message;
    }

    public CountryInfo getCountryInfo() {
        return countryInfo;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
