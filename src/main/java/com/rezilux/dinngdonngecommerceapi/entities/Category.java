package com.rezilux.dinngdonngecommerceapi.entities;

//import com.rezilux.yupay.entities.AbstractAuditingEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
public class Category  extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String image;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private  Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> subCategories;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
