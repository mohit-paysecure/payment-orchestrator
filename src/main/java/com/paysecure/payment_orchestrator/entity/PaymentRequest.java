package com.paysecure.payment_orchestrator.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PaymentRequest {
    private Client client;
    private Purchase purchase;
    private String brandId;
    private String paymentMethod;
    private String successRedirect;
    private String failureRedirect;
    private String successCallback;
    private String failureCallback;
    private Map<String, String> extraParam;
    private String deposit_type ;

    // Constructors
    public PaymentRequest() {}

    public PaymentRequest(Client client, Purchase purchase, String brandId, String paymentMethod,
                          String successRedirect, String failureRedirect, String successCallback,
                          String failureCallback, Map<String, String> extraParam) {
        this.client = client;
        this.purchase = purchase;
        this.brandId = brandId;
        this.paymentMethod = paymentMethod;
        this.successRedirect = successRedirect;
        this.failureRedirect = failureRedirect;
        this.successCallback = successCallback;
        this.failureCallback = failureCallback;
        this.extraParam = extraParam;
    }

    // Getters and Setters
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public String getDeposit_type() {
        return deposit_type;
    }

    public void setDeposit_type(String deposit_type) {
        this.deposit_type = deposit_type;
    }

    public Purchase getPurchase() { return purchase; }
    public void setPurchase(Purchase purchase) { this.purchase = purchase; }

    public String getBrandId() { return brandId; }
    public void setBrandId(String brandId) { this.brandId = brandId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getSuccessRedirect() { return successRedirect; }
    public void setSuccessRedirect(String successRedirect) { this.successRedirect = successRedirect; }

    public String getFailureRedirect() { return failureRedirect; }
    public void setFailureRedirect(String failureRedirect) { this.failureRedirect = failureRedirect; }

    public String getSuccessCallback() { return successCallback; }
    public void setSuccessCallback(String successCallback) { this.successCallback = successCallback; }

    public String getFailureCallback() { return failureCallback; }
    public void setFailureCallback(String failureCallback) { this.failureCallback = failureCallback; }

    public Map<String, String> getExtraParam() { return extraParam; }
    public void setExtraParam(Map<String, String> extraParam) { this.extraParam = extraParam; }

    // Nested Client class
    public static class Client {
        private String email;
        private String streetAddress;
        private String city;
        private String fullName;
        private String zipCode;
        private String country;
        private LocalDate dateOfBirth;
        private String stateCode;
        private String phone;
        private String taxNumber;
        private String bankAccountNumber;

        // Constructors
        public Client() {}

        // Getters and Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getStreetAddress() { return streetAddress; }
        public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getZipCode() { return zipCode; }
        public void setZipCode(String zipCode) { this.zipCode = zipCode; }

        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }

        public LocalDate getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

        public String getStateCode() { return stateCode; }
        public void setStateCode(String stateCode) { this.stateCode = stateCode; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getTaxNumber() { return taxNumber; }
        public void setTaxNumber(String taxNumber) { this.taxNumber = taxNumber; }

        public String getBankAccountNumber() { return bankAccountNumber; }
        public void setBankAccountNumber(String bankAccountNumber) { this.bankAccountNumber = bankAccountNumber; }


    }

    // Nested Purchase class
    public static class Purchase {

        private String id;
        private String currency;
        private List<Product> products;

        // Constructors
        public Purchase() {}

        // Getters and Setters
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }

        public List<Product> getProducts() { return products; }
        public void setProducts(List<Product> products) { this.products = products; }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Purchase{" +
                    "currency='" + currency + '\'' +
                    ", products=" + products +
                    '}';
        }
    }

    // Nested Product class
    public static class Product {
        private String name;
        private BigDecimal price;

        // Constructors
        public Product() {}

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }

        @Override
        public String toString() {
            return "Product{" +
                    "name='" + name + '\'' +
                    ", price=" + price +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "client=" + client.toString() +
                ", purchase=" + purchase.toString() +
                ", brandId='" + brandId + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", successRedirect='" + successRedirect + '\'' +
                ", failureRedirect='" + failureRedirect + '\'' +
                ", successCallback='" + successCallback + '\'' +
                ", failureCallback='" + failureCallback + '\'' +
                ", extraParam=" + extraParam.toString() +
                '}';
    }
}