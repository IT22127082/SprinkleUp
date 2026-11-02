package com.sprinkleup.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** What the custom cake request form sends. */
public class CustomOrderForm {

    public static final List<String> OCCASIONS = List.of(
            "Birthday", "Wedding", "Anniversary", "Engagement", "Baby shower", "Graduation", "Corporate event", "Other");

    public static final List<String> FLAVOURS = List.of(
            "Chocolate", "Vanilla", "Red velvet", "Strawberry", "Butterscotch", "Coffee", "Lemon", "Ribbon (mixed)");

    @NotBlank(message = "Choose an occasion")
    @Size(max = 40)
    private String occasion;

    @NotBlank(message = "Choose a flavour")
    @Size(max = 60)
    private String flavour;

    @NotNull(message = "Enter a weight")
    @DecimalMin(value = "0.5", message = "At least 0.5 kg")
    @DecimalMax(value = "20", message = "Up to 20 kg")
    private Double weightKg = 1.0;

    @NotNull(message = "Choose the number of tiers")
    @Min(value = 1, message = "At least 1 tier")
    @Max(value = 5, message = "Up to 5 tiers")
    private Integer tiers = 1;

    private boolean eggless;

    @NotBlank(message = "Tell us about the design you want")
    @Size(max = 1000, message = "Keep the description under 1000 characters")
    private String design;

    @Size(max = 100, message = "Keep the message under 100 characters")
    private String messageOnCake;

    @NotNull(message = "Choose the date you need it")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate neededBy;

    @DecimalMin(value = "0", message = "Budget cannot be negative")
    private BigDecimal budget;

    public String getOccasion() { return occasion; }
    public void setOccasion(String occasion) { this.occasion = occasion; }
    public String getFlavour() { return flavour; }
    public void setFlavour(String flavour) { this.flavour = flavour; }
    public Double getWeightKg() { return weightKg; }
    public void setWeightKg(Double weightKg) { this.weightKg = weightKg; }
    public Integer getTiers() { return tiers; }
    public void setTiers(Integer tiers) { this.tiers = tiers; }
    public boolean isEggless() { return eggless; }
    public void setEggless(boolean eggless) { this.eggless = eggless; }
    public String getDesign() { return design; }
    public void setDesign(String design) { this.design = design; }
    public String getMessageOnCake() { return messageOnCake; }
    public void setMessageOnCake(String messageOnCake) { this.messageOnCake = messageOnCake; }
    public LocalDate getNeededBy() { return neededBy; }
    public void setNeededBy(LocalDate neededBy) { this.neededBy = neededBy; }
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
}
