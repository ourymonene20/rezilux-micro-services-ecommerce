package com.rezilux.dinngdonngecommerceapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "yupay_user")
public class User extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;
    @Column(name = "last_Name")
    protected String lastName;
    @Column(name = "first_Name")
    protected String firstName;
    @Column(unique = true)
    protected String phone;
    protected Boolean active;

    // @Pattern(regexp = "([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")
    protected String email;

    protected String avatar;
    @Column(name = "lang_key")
    protected String langKey;
    protected String identification;
    @Column(name = "creation_step", columnDefinition="int default 1")
    protected int creationStep = 1;

    protected String login;
    @Column(name = "password_hash")
    // @JsonIgnore
    protected String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_profile",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id"))
    protected List<Profile> profiles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Merchant> merchants;

    /*@JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<VerificationCode> verificationCodes;
*/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String nom) {
        this.lastName = nom;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String telephone) {
        this.phone = telephone;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public List<Merchant> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<Merchant> merchants) {
        this.merchants = merchants;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public int getCreationStep() {
        return creationStep;
    }

    public void setCreationStep(int creationStep) {
        this.creationStep = creationStep;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   /* public List<VerificationCode> getVerificationCodes() {
        return verificationCodes;
    }

    public void setVerificationCodes(List<VerificationCode> verificationCodes) {
        this.verificationCodes = verificationCodes;
    }*/
}
