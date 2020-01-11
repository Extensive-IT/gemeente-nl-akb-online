package gemeente.nlakbonline.controller.model;

import gemeente.nlakbonline.domain.PaymentType;

// GeregistreerdeNummer	Gebruiken om aan te schrijven	Straat	nr	toev	Postcode	Plaats	Toegezegd 2019		E-Mail
public class ImportRegistrationsRecord {
    private String registrationNumber;
    private String salutation;
    private String street;
    private String number;
    private String addition;
    private String postalCode;
    private String city;
    private String country;
    private String email;
    private Double previousYearAmount;
    private PaymentType previousYearPaymentType;

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getPreviousYearAmount() {
        return previousYearAmount;
    }

    public void setPreviousYearAmount(Double previousYearAmount) {
        this.previousYearAmount = previousYearAmount;
    }

    public PaymentType getPreviousYearPaymentType() {
        return previousYearPaymentType;
    }

    public void setPreviousYearPaymentType(PaymentType previousYearPaymentType) {
        this.previousYearPaymentType = previousYearPaymentType;
    }
}
