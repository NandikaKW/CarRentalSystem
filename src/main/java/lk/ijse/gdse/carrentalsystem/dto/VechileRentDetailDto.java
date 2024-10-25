package lk.ijse.gdse.carrentalsystem.dto;

import java.math.BigDecimal;
import java.util.Date;

public class VechileRentDetailDto {
    private String vehicle_id;
    private String rent_id;
    private Date start_date;
    private Date end_date;
    private Date rent_date;
    private BigDecimal cost;
    private String vehicle_condition;

    public VechileRentDetailDto() {
    }

    public VechileRentDetailDto(String vehicle_id, String rent_id, Date start_date, Date end_date, Date rent_date, BigDecimal cost, String vehicle_condition) {
        this.vehicle_id = vehicle_id;
        this.rent_id = rent_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.rent_date = rent_date;
        this.cost = cost;
        this.vehicle_condition = vehicle_condition;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getRent_id() {
        return rent_id;
    }

    public void setRent_id(String rent_id) {
        this.rent_id = rent_id;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Date getRent_date() {
        return rent_date;
    }

    public void setRent_date(Date rent_date) {
        this.rent_date = rent_date;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getVehicle_condition() {
        return vehicle_condition;
    }

    public void setVehicle_condition(String vehicle_condition) {
        this.vehicle_condition = vehicle_condition;
    }

    @Override
    public String toString() {
        return "VechileRentDetailDto{" +
                "vehicle_id='" + vehicle_id + '\'' +
                ", rent_id='" + rent_id + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", rent_date=" + rent_date +
                ", cost=" + cost +
                ", vehicle_condition='" + vehicle_condition + '\'' +
                '}';
    }
}
