package com.example.creditchatbot.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String generatedUniqueName;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String middleName;

    @Column
    private LocalDate birthDate;

    @Column
    private String phone;

    @Column
    private String passportNum;

    @Column
    private LocalDate passportDate;

    @Column
    private String passportOrg;

    @Column
    private String address;

    @Column
    private String jobPlace;

    @Column
    private String jobPosition;

    @Column
    private String jobExp;

    @Column
    private int creditAmount;

    @Column
    private int creditTerm;

    @Column
    private double creditRate;

    @Column
    private String city;

    public Client() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGeneratedUniqueName() {
        return generatedUniqueName;
    }

    public void setGeneratedUniqueName(String generatedUniqueName) {
        this.generatedUniqueName = generatedUniqueName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassportNum() {
        return passportNum;
    }

    public void setPassportNum(String passportNum) {
        this.passportNum = passportNum;
    }

    public LocalDate getPassportDate() {
        return passportDate;
    }

    public void setPassportDate(LocalDate passportDate) {
        this.passportDate = passportDate;
    }

    public String getPassportOrg() {
        return passportOrg;
    }

    public void setPassportOrg(String passportOrg) {
        this.passportOrg = passportOrg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJobPlace() {
        return jobPlace;
    }

    public void setJobPlace(String jobPlace) {
        this.jobPlace = jobPlace;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getJobExp() {
        return jobExp;
    }

    public void setJobExp(String jobExp) {
        this.jobExp = jobExp;
    }

    public int getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(int creditAmount) {
        this.creditAmount = creditAmount;
    }

    public int getCreditTerm() {
        return creditTerm;
    }

    public void setCreditTerm(int creditTerm) {
        this.creditTerm = creditTerm;
    }

    public double getCreditRate() {
        return creditRate;
    }

    public void setCreditRate(double creditRate) {
        this.creditRate = creditRate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return creditAmount == client.creditAmount &&
                creditTerm == client.creditTerm &&
                Double.compare(client.creditRate, creditRate) == 0 &&
                id.equals(client.id) &&
                generatedUniqueName.equals(client.generatedUniqueName) &&
                Objects.equals(firstName, client.firstName) &&
                Objects.equals(lastName, client.lastName) &&
                Objects.equals(middleName, client.middleName) &&
                Objects.equals(birthDate, client.birthDate) &&
                Objects.equals(phone, client.phone) &&
                Objects.equals(passportNum, client.passportNum) &&
                Objects.equals(passportDate, client.passportDate) &&
                Objects.equals(passportOrg, client.passportOrg) &&
                Objects.equals(address, client.address) &&
                Objects.equals(jobPlace, client.jobPlace) &&
                Objects.equals(jobPosition, client.jobPosition) &&
                Objects.equals(jobExp, client.jobExp) &&
                Objects.equals(city, client.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, generatedUniqueName, firstName, lastName, middleName, birthDate, phone, passportNum, passportDate, passportOrg, address, jobPlace, jobPosition, jobExp, creditAmount, creditTerm, creditRate, city);
    }
}
