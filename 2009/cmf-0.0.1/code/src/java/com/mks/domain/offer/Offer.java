package com.mks.domain.offer;

import com.mks.domain.Identifiable;
import com.mks.domain.util.listener.LoggerListener;
import com.mks.domain.util.validation.ValidationMask;
import com.mks.domain.annotation.*;
import com.mks.domain.discount.Discount;
import com.mks.domain.discount.DiscountVisitorAdapter;
import com.mks.domain.discount.AmountDiscount;
import com.mks.domain.discount.PercentDiscount;

import javax.persistence.*;
import static javax.persistence.EnumType.*;
import static javax.persistence.CascadeType.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "OFFER")
@EntityListeners(LoggerListener.class)
@XEntity(topLevel = true, url = "offer", displayProperty = "name")
public class Offer implements Identifiable<Long> {

    @Id @GeneratedValue
    @Column(name = "OFFER_ID")
    @XField
    private Long id;
    @Column(name = "NAME", nullable = false, length = 30, unique = true)
    @XField(nullable = false, length = 30, unique = true)
    private String name;
    @Column(name = "START_DATE", nullable = false)
    @XField(validationMask = ValidationMask.DATE, nullable = false)
    private Date startDate;
    @Column(name = "END_DATE")
    @XField(validationMask = ValidationMask.DATE)
    private Date endDate;
    @Column(name = "HOT_OFFER")
    @XField
    private Boolean hotOffer = Boolean.TRUE;
    @Column(name = "TRANSFER_INCLUDED")
    @XField
    private boolean transferIncluded;

    @Enumerated(STRING)
    @Column(name = "OFFER_STATUS", nullable = false)
    @XField(nullable = false)
    private OfferStatus offerStatus = OfferStatus.ACTIVE;

    @OneToOne(cascade = ALL)
    @JoinColumn(name = "OFFER_DETAILS_ID", unique = true)
    @XOneToOne(composition = true, displayProperty = "ad")
    private OfferDetails offerDetails;

    @OneToMany(cascade = ALL)
    @JoinColumn(name = "OFFER_ID")
    @XOneToMany(targetType = OfferNote.class, composition = true, displayProperty = "description")
    private Set<OfferNote> offerNotes = new HashSet<OfferNote>();

    @ManyToOne
    @JoinColumn(name = "TERMS_ID")
    @XManyToOne(displayProperty = "name")
    private TermsAndConditions termsAndConditions;

    @ManyToMany
    @JoinTable(
            name = "OFFER_OFFER_ITEM",
            joinColumns = @JoinColumn(name = "OFFER_ID"),
            inverseJoinColumns = @JoinColumn(name = "OFFER_ITEM_ID"))
    @XManyToMany(targetType = OfferItem.class, displayProperty = "name")
    private Set<OfferItem> offerItems = new HashSet<OfferItem>();

    @ManyToMany
    @JoinTable(
            name = "OFFER_DISCOUNT",
            joinColumns = @JoinColumn(name = "OFFER_ID"),
            inverseJoinColumns = @JoinColumn(name = "DISCOUNT_ID"))
    @XManyToMany(targetType = Discount.class, displayProperty = "name")
    private Set<Discount> discounts = new HashSet<Discount>();

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getHotOffer() {
        return hotOffer;
    }

    public void setHotOffer(Boolean hotOffer) {
        this.hotOffer = hotOffer;
    }

    public boolean isTransferIncluded() {
        return transferIncluded;
    }

    public void setTransferIncluded(boolean transferIncluded) {
        this.transferIncluded = transferIncluded;
    }

    public OfferStatus getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(OfferStatus offerStatus) {
        this.offerStatus = offerStatus;
    }

    public OfferDetails getOfferDetails() {
        return offerDetails;
    }

    public void setOfferDetails(OfferDetails offerDetails) {
        this.offerDetails = offerDetails;
    }

    public Set<OfferNote> getOfferNotes() {
        return offerNotes;
    }

    public void setOfferNotes(Set<OfferNote> offerNotes) {
        this.offerNotes = offerNotes;
    }

    public TermsAndConditions getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(TermsAndConditions termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public Set<OfferItem> getOfferItems() {
        return offerItems;
    }

    public void setOfferItems(Set<OfferItem> offerItems) {
        this.offerItems = offerItems;
    }

    public Set<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Set<Discount> discounts) {
        this.discounts = discounts;
    }

    public Double getPrice() {
        Double result = 0d;
        for (OfferItem item : getOfferItems()) {
            result += item.getPrice();
        }
        return result;
    }

    public Double getTotalPrice() {
        return new DiscountVisitorAdapter<Void>() {
            private Double amount = 0d;
            private Float percent = 0f;

            @Override
            public Void visit(AmountDiscount discount) {
                amount += discount.getAmount();
                return null;
            }

            @Override
            public Void visit(PercentDiscount discount) {
                percent += discount.getPercent();
                return null;
            }

            public Double calculatePrice(Double price, Set<Discount> discounts) {
                for (Discount discount : discounts) {
                    discount.accept(this);
                }
                if (amount < price) {
                    price -= amount;
                }
                price -= price * percent / 100;
                return price;
            }
        }.calculatePrice(getPrice(), getDiscounts());
    }
}
