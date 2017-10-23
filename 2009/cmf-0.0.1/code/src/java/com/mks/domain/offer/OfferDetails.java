package com.mks.domain.offer;

import com.mks.domain.Identifiable;
import com.mks.domain.annotation.XEntity;
import com.mks.domain.annotation.XField;

import javax.persistence.*;

@Entity @Table(name = "OFFER_DETAILS")
@XEntity(topLevel = true, url = "offer-details", displayProperty = "ad")
public class OfferDetails implements Identifiable<Long> {

    @Id @GeneratedValue
    @Column(name = "OFFER_DETAILS_ID")
    @XField
    private Long id;
    @Column(name = "AD", nullable = false)
    @XField
    private String ad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }
}