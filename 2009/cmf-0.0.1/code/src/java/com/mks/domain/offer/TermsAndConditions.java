package com.mks.domain.offer;

import com.mks.domain.Identifiable;
import com.mks.domain.annotation.XEntity;
import com.mks.domain.annotation.XField;

import javax.persistence.*;

@Entity @Table(name = "TERMS_AND_CONDITIONS")
@XEntity(topLevel = true, url = "terms-and-conditions", displayProperty = "name")
public class TermsAndConditions implements Identifiable<Long> {

    @Id
    @GeneratedValue
    @Column(name = "TERMS_AND_CONDITIONS_ID")
    @XField
    private Long id;
    @Column(name = "NAME", nullable = false)
    @XField
    private String name;
    @Column(name = "DESCRIPTION")
    @XField
    private String description;

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

    public void setDescription(String description) {
        this.description = description;
    }
}
