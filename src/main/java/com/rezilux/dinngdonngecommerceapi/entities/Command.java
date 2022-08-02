package com.rezilux.dinngdonngecommerceapi.entities;

//import com.rezilux.yupay.entities.AbstractAuditingEntity;
//import com.rezilux.yupay.entities.auth.User;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "command")
public class Command extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @Column(unique = true)
    private String code;
    private Double total;
    private Timestamp date;
    private int status;
   /* @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;*/
    @OneToMany(mappedBy = "command")
    private Set<DetailCommand> details = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    private ShippingAddress shippingAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

   /* public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }*/

    public Set<DetailCommand> getDetails() {
        return details;
    }

    public void setDetails(Set<DetailCommand> details) {
        this.details = details;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
