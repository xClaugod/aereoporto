package com.aereoporto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "AEREO")
public class Airplane {

    @Id
    @Column(name = "TIPO_AEREO")
    private String typeAirplane;

    @Column(name = "NUM_PASS")
    private Integer numPassengers;

    @Column(name = "QTA_MERCI")
    private Integer qtyGoods;

    public Airplane(String typeAirplane, Integer numPassengers, Integer qtyGoods) {
        this.typeAirplane = typeAirplane;
        this.numPassengers = numPassengers;
        this.qtyGoods = qtyGoods;
    }

    public Airplane(){}
    
    public String getTypeAirplane() {
        return typeAirplane;
    }
    public void setTypeAirplane(String typeAirplane) {
        this.typeAirplane = typeAirplane;
    }
    public Integer getNumPassengers() {
        return numPassengers;
    }
    public void setNumPassengers(Integer numPassengers) {
        this.numPassengers = numPassengers;
    }
    public Integer getQtyGoods() {
        return qtyGoods;
    }
    public void setQtyGoods(Integer qtyGoods) {
        this.qtyGoods = qtyGoods;
    }
    public String toString() {
        return "Airplane [typeAirplane=" + typeAirplane + ", numPassengers=" + numPassengers + ", qtyGoods=" + qtyGoods
                + "]";
    }
}
