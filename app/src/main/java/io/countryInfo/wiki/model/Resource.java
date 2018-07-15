package io.countryInfo.wiki.model;

public final class Resource {
    private CountryInfo countryInfo;
    private Status status;

    public Resource(CountryInfo countryInfo, Status status) {
        this.countryInfo = countryInfo;
        this.status = status;
    }

    public CountryInfo getCountryInfo() {
        return countryInfo;
    }

    public Status getStatus() {
        return status;
    }

}
