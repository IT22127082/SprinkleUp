package com.sprinkleup.model;

import com.sprinkleup.dto.CustomOrderForm;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * A made-to-order cake. The customer describes what they want, the bakery
 * replies with a quote, and the customer accepts it. Each step is a method
 * that checks the request is at the right stage, so the status can never
 * jump somewhere it shouldn't (ENCAPSULATION).
 */
@Entity
@Table(name = "custom_orders")
public class CustomOrder extends AbstractOrder {

    /** Days the bakery needs to make a custom cake. */
    public static final int MIN_DAYS_NOTICE = 3;

    @Column(nullable = false, length = 40)
    private String occasion;

    @Column(nullable = false, length = 60)
    private String flavour;

    @Column(nullable = false)
    private double weightKg;

    @Column(nullable = false)
    private int tiers;

    @Column(nullable = false)
    private boolean eggless;

    @Column(nullable = false, length = 1000)
    private String design;

    @Column(length = 100)
    private String messageOnCake;

    @Column(nullable = false)
    private LocalDate neededBy;

    @Column(precision = 10, scale = 2)
    private BigDecimal budget;

    @Column(precision = 10, scale = 2)
    private BigDecimal quotedPrice;

    @Column(length = 500)
    private String bakerNote;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CustomOrderStatus status = CustomOrderStatus.REQUESTED;

    protected CustomOrder() {
    }

    public CustomOrder(Customer customer, CustomOrderForm form) {
        super(customer);
        applyRequest(form);
    }

    // ---------- Customer actions ----------

    /** The customer changes their request; only allowed before a quote is sent. */
    public void updateRequest(CustomOrderForm form) {
        if (status != CustomOrderStatus.REQUESTED) {
            throw new IllegalStateException("This request already has a quote, so it can't be edited. Cancel it and send a new one instead.");
        }
        applyRequest(form);
    }

    public void accept() {
        if (status != CustomOrderStatus.QUOTED) {
            throw new IllegalStateException("There is no quote to accept yet");
        }
        status = CustomOrderStatus.ACCEPTED;
    }

    public void cancel() {
        if (!status.isCancellableByCustomer()) {
            throw new IllegalStateException("This request can no longer be cancelled");
        }
        status = CustomOrderStatus.CANCELLED;
    }

    public boolean isEditable() {
        return status == CustomOrderStatus.REQUESTED;
    }

    public boolean isCancellable() {
        return status.isCancellableByCustomer();
    }

    // ---------- Bakery actions ----------

    /** The bakery sends (or revises) its price. */
    public void quote(BigDecimal price, String note) {
        if (status != CustomOrderStatus.REQUESTED && status != CustomOrderStatus.QUOTED) {
            throw new IllegalStateException("A quote can only be sent before the customer accepts");
        }
        if (price == null || price.signum() <= 0) {
            throw new IllegalArgumentException("Enter a price above zero");
        }
        quotedPrice = price.setScale(2, RoundingMode.HALF_UP);
        bakerNote = note == null || note.isBlank() ? null : note.trim();
        status = CustomOrderStatus.QUOTED;
    }

    public void changeStatus(CustomOrderStatus newStatus) {
        if (status.isClosed() && newStatus != status) {
            throw new IllegalStateException("A " + status.getLabel().toLowerCase() + " request cannot be changed");
        }
        if (newStatus.needsPrice() && quotedPrice == null) {
            throw new IllegalStateException("Send a quote before moving this request forward");
        }
        status = newStatus;
    }

    private void applyRequest(CustomOrderForm form) {
        if (form.getNeededBy() == null || form.getNeededBy().isBefore(earliestDate())) {
            throw new IllegalArgumentException("We need at least " + MIN_DAYS_NOTICE
                    + " days to make a custom cake. Choose " + earliestDate() + " or later.");
        }
        this.occasion = form.getOccasion().trim();
        this.flavour = form.getFlavour().trim();
        this.weightKg = form.getWeightKg();
        this.tiers = form.getTiers();
        this.eggless = form.isEggless();
        this.design = form.getDesign().trim();
        this.messageOnCake = form.getMessageOnCake() == null || form.getMessageOnCake().isBlank() ? null : form.getMessageOnCake().trim();
        this.neededBy = form.getNeededBy();
        this.budget = form.getBudget();
    }

    public static LocalDate earliestDate() {
        return LocalDate.now().plusDays(MIN_DAYS_NOTICE);
    }

    // ---------- AbstractOrder ----------

    @Override
    public String getKindLabel() {
        return "Custom cake";
    }

    @Override
    public String getSummary() {
        return occasion + " cake · " + weightKg + " kg · " + tiers + (tiers == 1 ? " tier" : " tiers") + " · " + flavour;
    }

    /** No price until the bakery has sent a quote. */
    @Override
    public BigDecimal getTotal() {
        return quotedPrice;
    }

    @Override
    public String getStatusLabel() {
        return status.getLabel();
    }

    @Override
    public String getStatusBadge() {
        return status.getBadge();
    }

    @Override
    public LocalDate getDueDate() {
        return neededBy;
    }

    @Override
    public String getCustomerPath() {
        return "/custom-orders/" + getId();
    }

    @Override
    public String getDisplayName() {
        return "Custom request #" + getId();
    }

    /** Fills a form with this request, for the edit page. */
    public CustomOrderForm toForm() {
        CustomOrderForm form = new CustomOrderForm();
        form.setOccasion(occasion);
        form.setFlavour(flavour);
        form.setWeightKg(weightKg);
        form.setTiers(tiers);
        form.setEggless(eggless);
        form.setDesign(design);
        form.setMessageOnCake(messageOnCake);
        form.setNeededBy(neededBy);
        form.setBudget(budget);
        return form;
    }

    // ---------- Getters ----------

    public String getOccasion() { return occasion; }
    public String getFlavour() { return flavour; }
    public double getWeightKg() { return weightKg; }
    public int getTiers() { return tiers; }
    public boolean isEggless() { return eggless; }
    public String getDesign() { return design; }
    public String getMessageOnCake() { return messageOnCake; }
    public LocalDate getNeededBy() { return neededBy; }
    public BigDecimal getBudget() { return budget; }
    public BigDecimal getQuotedPrice() { return quotedPrice; }
    public String getBakerNote() { return bakerNote; }
    public CustomOrderStatus getStatus() { return status; }
}
