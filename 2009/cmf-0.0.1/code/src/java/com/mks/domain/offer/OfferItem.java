package com.mks.domain.offer;

import com.mks.domain.Identifiable;
import com.mks.domain.annotation.XEntity;
import com.mks.domain.annotation.XField;
import com.mks.domain.annotation.XManyToMany;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity @Table(name = "OFFER_ITEM")
@XEntity(topLevel = true, url= "offer-item", displayProperty = "name")
public class OfferItem implements Identifiable<Long> {

    @Id @GeneratedValue
    @Column(name = "OFFER_ITEM_ID")
    @XField
    private Long id;
    @Column(name = "NAME", nullable = false)
    @XField
    private String name;
    @Column(name = "PRICE", nullable = false)
    @XField
    private Double price;

    @ManyToMany(mappedBy = "offerItems")
    @XManyToMany(targetType = Offer.class, displayProperty = "name", mappedBy = "offerItems")
    private Set<Offer> offers = new HashSet<Offer>();

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Set<Offer> getOffers() {
        return offers;
    }

    public void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }
}
