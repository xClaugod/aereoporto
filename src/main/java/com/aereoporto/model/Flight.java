package com.aereoporto.model;

import java.sql.Date;
import java.sql.Timestamp;

import com.aereoporto.Converter.CustomDateConverter;
import com.aereoporto.Converter.CustomTimestampConverter;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "VOLI")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VOLO")
    Integer id;

    @Convert(converter = CustomDateConverter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "GIORNO")
    Date day;
    
    @Column(name = "CITTA_PARTENZA")
    String departureCity;

    @Column(name = "CITTA_ARRIVO")
    String arrivalCity;

    @Convert(converter = CustomTimestampConverter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "ORA_PARTENZA")
    Timestamp departureTime;

    @Convert(converter = CustomTimestampConverter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "ORA_ARRIVO")
    Timestamp arrivalTime;

    @Column(name = "TIPO_AEREO")
    String idAirplane;

    @Column(name = "PASSEGGERI")
    Integer passengers;

    @Column(name = "MERCI")
    Integer goods;

    public Flight() {}

    public Flight(Integer id, Date day, String departureCity, String arrivalCity, Timestamp departureTime,
            Timestamp arrivalTime, String idAirplane, Integer passengers, Integer goods) {
        this.id = id;
        this.day = day;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.idAirplane = idAirplane;
        this.passengers = passengers;
        this.goods = goods;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public Timestamp getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Timestamp departureTime) {
        this.departureTime = departureTime;
    }

    public Timestamp getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Timestamp arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getIdAirplane() {
        return idAirplane;
    }

    public void setIdAirplane(String idAirplane) {
        this.idAirplane = idAirplane;
    }

    public Integer getPassengers() {
        return passengers;
    }

    public void setPassengers(Integer passengers) {
        this.passengers = passengers;
    }

    public Integer getGoods() {
        return goods;
    }

    public void setGoods(Integer goods) {
        this.goods = goods;
    }

    public String toString() {
        return "Flight [id=" + id + ", day=" + day + ", departureCity=" + departureCity + ", arrivalCity=" + arrivalCity
                + ", departureTime=" + departureTime + ", arrivalTime=" + arrivalTime + ", idAirplane=" + idAirplane
                + ", passengers=" + passengers + ", goods=" + goods + "]";
    }
    
}
