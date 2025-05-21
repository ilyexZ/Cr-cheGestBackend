package org.ilyes.crechegest.model;


import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "medical_info")
public class MedicalInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", referencedColumnName = "id")
    private Child child;

    @Column(name = "blood_type")
    private String bloodType;

    @Column(name = "allergies", length = 1000)
    private String allergies;

    @Column(name = "medical_conditions", length = 1000)
    private String medicalConditions;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "doctor_phone")
    private String doctorPhone;

    @Column(name = "insurance_info", length = 1000)
    private String insuranceInfo;

    @Column(name = "emergency_protocol", length = 2000)
    private String emergencyProtocol;

    @ElementCollection
    @CollectionTable(name = "vaccinations", joinColumns = @JoinColumn(name = "medical_info_id"))
    private List<String> vaccinations;

    public MedicalInfo() {}

    public MedicalInfo(Child child) {
        this.child = child;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getMedicalConditions() {
        return medicalConditions;
    }

    public void setMedicalConditions(String medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
    }

    public String getInsuranceInfo() {
        return insuranceInfo;
    }

    public void setInsuranceInfo(String insuranceInfo) {
        this.insuranceInfo = insuranceInfo;
    }

    public String getEmergencyProtocol() {
        return emergencyProtocol;
    }

    public void setEmergencyProtocol(String emergencyProtocol) {
        this.emergencyProtocol = emergencyProtocol;
    }

    public List<String> getVaccinations() {
        return vaccinations;
    }

    public void setVaccinations(List<String> vaccinations) {
        this.vaccinations = vaccinations;
    }
}
