package com.example.creditchatbot.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String generatedUniqueName;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String middleName;

    @Column
    private Date birthDate;

    @Column
    private String phone;

    @Column
    private String passportData;

    @Column
    private String address;

    @Column
    private String jobInfo;

    @Column
    private short creditAmount;

    @Column
    private int creditTerm;

    @Column
    private double creditRate;

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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassportData() {
        return passportData;
    }

    public void setPassportData(String passportData) {
        this.passportData = passportData;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJobInfo() {
        return jobInfo;
    }

    public void setJobInfo(String jobInfo) {
        this.jobInfo = jobInfo;
    }

    public short getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(short creditAmount) {
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
                Objects.equals(passportData, client.passportData) &&
                Objects.equals(address, client.address) &&
                Objects.equals(jobInfo, client.jobInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, generatedUniqueName, firstName, lastName, middleName, birthDate, phone, passportData, address, jobInfo, creditAmount, creditTerm, creditRate);
    }
}
