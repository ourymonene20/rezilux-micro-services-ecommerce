package com.rezilux.dinngdonngecommerceapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    private String description;

    @ManyToMany(mappedBy = "profiles")
    private List<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String libelle) {
        this.name = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public List<User> getUtilisateurs() {
        return users;
    }

    public void setUtilisateurs(List<User> users) {
        this.users = users;
    }
}
