package com.aereoporto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "AEROPORTI")
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AEROPORTO")
    Integer id;

    @Column(name = "CITTA")
    String city;

    @Column(name = "NAZIONE")
    String nation;

    @Column(name = "NUM_PISTE")
    Integer numRunway;

    public Airport(){}

    public Airport(Integer id, String city, String nation, Integer numRunway) {
        this.id = id;
        this.city = city;
        this.nation = nation;
        this.numRunway = numRunway;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public Integer getNumRunway() {
        return numRunway;
    }

    public void setNumRunway(Integer numRunway) {
        this.numRunway = numRunway;
    }

    public String toString() {
        return "Airport [id=" + id + ", city=" + city + ", nation=" + nation + ", numRunway=" + numRunway + "]";
    }
    
}
