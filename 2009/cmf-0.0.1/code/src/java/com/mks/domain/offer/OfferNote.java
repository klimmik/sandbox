package com.mks.domain.offer;

import com.mks.domain.Identifiable;
import com.mks.domain.annotation.XEntity;
import com.mks.domain.annotation.XField;

import javax.persistence.*;

@Entity @Table(name = "OFFER_NOTE")
@XEntity(topLevel = true, url= "offer-note", displayProperty = "description")
public class OfferNote implements Identifiable<Long> {

    @Id @GeneratedValue
    @Column(name = "OFFER_NOTE_ID")
    @XField
    private Long id;
    @Column(name = "DESCRIPTION", nullable = false)
    @XField(nullable = false)
    private String description;

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
}