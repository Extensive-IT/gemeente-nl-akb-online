package gemeente.nlakbonline.controller.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Registration {

    @NotNull
    @NotEmpty
    private String registrationNumber;

    @NotNull
    @NotEmpty
    private String salutation;

    @NotNull
    @NotEmpty
    private String street;

    @NotNull
    @NotEmpty
    private String postalCode;

    @NotNull
    @NotEmpty
    private String city;

    @NotNull
    @NotEmpty
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
