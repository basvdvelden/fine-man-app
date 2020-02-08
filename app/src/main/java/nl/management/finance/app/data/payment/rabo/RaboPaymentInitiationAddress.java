package nl.management.finance.app.data.payment.rabo;

import androidx.annotation.NonNull;

public class RaboPaymentInitiationAddress {
    private String buildingNumber;
    private String country;
    private String postcode;
    private String streetName;
    private String townName;

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    @Override
    @NonNull
    public String toString() {
        return String.format("[\nbuildingNumber=%s, \ncountry=%s, \npostcode=%s, \nstreetName=%s, " +
                "\ntownName=%s\n]", buildingNumber, country, postcode, streetName, townName);
    }
}
