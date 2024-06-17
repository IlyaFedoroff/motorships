package classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Ticket implements Serializable {
    private int id;
    private String number;
    private double price;
    private LocalDate dateDeparture;
    private LocalTime timeDeparture;
    private LocalDate dateOfSale;
    private int cabinNumber;
    private int individualId;
    private int laborContractId;
    private int voyageId;
    private int serviceId;
    private int measureId;

    public Ticket(int id, String number, double price, LocalDate dateDeparture, LocalTime timeDeparture, LocalDate dateOfSale,
                  int cabinNumber, int individualId, int laborContractId, int voyageId, int serviceId, int measureId) {
        this.id = id;
        this.number = number;
        this.price = price;
        this.dateDeparture = dateDeparture;
        this.timeDeparture = timeDeparture;
        this.dateOfSale = dateOfSale;
        this.cabinNumber = cabinNumber;
        this.individualId = individualId;
        this.laborContractId = laborContractId;
        this.voyageId = voyageId;
        this.serviceId = serviceId;
        this.measureId = measureId;
    }

    // Геттеры и сеттеры


    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", price=" + price +
                ", dateDeparture=" + dateDeparture +
                ", timeDeparture=" + timeDeparture +
                ", dateOfSale=" + dateOfSale +
                ", cabinNumber=" + cabinNumber +
                ", individualId=" + individualId +
                ", laborContractId=" + laborContractId +
                ", voyageId=" + voyageId +
                ", serviceId=" + serviceId +
                ", measureId=" + measureId +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getDateDeparture() {
        return dateDeparture;
    }

    public void setDateDeparture(LocalDate dateDeparture) {
        this.dateDeparture = dateDeparture;
    }

    public LocalTime getTimeDeparture() {
        return timeDeparture;
    }

    public void setTimeDeparture(LocalTime timeDeparture) {
        this.timeDeparture = timeDeparture;
    }

    public LocalDate getDateOfSale() {
        return dateOfSale;
    }

    public void setDateOfSale(LocalDate dateOfSale) {
        this.dateOfSale = dateOfSale;
    }

    public int getCabinNumber() {
        return cabinNumber;
    }

    public void setCabinNumber(int cabinNumber) {
        this.cabinNumber = cabinNumber;
    }

    public int getIndividualId() {
        return individualId;
    }

    public void setIndividualId(int individualId) {
        this.individualId = individualId;
    }

    public int getLaborContractId() {
        return laborContractId;
    }

    public void setLaborContractId(int laborContractId) {
        this.laborContractId = laborContractId;
    }

    public int getVoyageId() {
        return voyageId;
    }

    public void setVoyageId(int voyageId) {
        this.voyageId = voyageId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getMeasureId() {
        return measureId;
    }

    public void setMeasureId(int measureId) {
        this.measureId = measureId;
    }
}
