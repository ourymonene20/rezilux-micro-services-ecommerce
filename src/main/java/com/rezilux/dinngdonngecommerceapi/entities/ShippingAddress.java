package com.rezilux.dinngdonngecommerceapi.entities;


import javax.persistence.*;

@Entity
@Table(name = "shipping_address")
public class ShippingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String addressLine;
    private String fullName;
    private String phone;
    private String city;
    private boolean isDefault;

   /* @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;*/



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    /*public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/

}
