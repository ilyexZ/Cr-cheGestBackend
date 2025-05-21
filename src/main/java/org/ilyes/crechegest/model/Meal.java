package org.ilyes.crechegest.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "meals")
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date;

    private LocalTime time;

    @Column(length = 2000)
    private String description;

    @Column(name = "nutritional_info", length = 1000)
    private String nutritionalInfo;

    @Column(name = "meal_type")
    private String mealType; // Breakfast, Lunch, Snack, etc.

    @Column(name = "allergies_info", length = 1000)
    private String allergiesInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    public Meal() {}

    public Meal(String name, LocalDate date, LocalTime time, String mealType) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.mealType = mealType;
    }

    // Getters and Setters
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNutritionalInfo() {
        return nutritionalInfo;
    }

    public void setNutritionalInfo(String nutritionalInfo) {
        this.nutritionalInfo = nutritionalInfo;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getAllergiesInfo() {
        return allergiesInfo;
    }

    public void setAllergiesInfo(String allergiesInfo) {
        this.allergiesInfo = allergiesInfo;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}
