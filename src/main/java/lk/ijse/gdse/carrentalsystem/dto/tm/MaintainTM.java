package lk.ijse.gdse.carrentalsystem.dto.tm;

import java.math.BigDecimal;
import java.util.Date;

public class MaintainTM {
    private String maintain_id;
    private BigDecimal cost;
    private Date maintain_date;
    private String description;
    private String duration;
    private String vehicle_id;

    public MaintainTM() {
    }

    public MaintainTM(String maintain_id, BigDecimal cost, Date maintain_date, String description, String duration, String vehicle_id) {
        this.maintain_id = maintain_id;
        this.cost = cost;
        this.maintain_date = maintain_date;
        this.description = description;
        this.duration = duration;
        this.vehicle_id = vehicle_id;
    }

    public String getMaintain_id() {
        return maintain_id;
    }

    public void setMaintain_id(String maintain_id) {
        this.maintain_id = maintain_id;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Date getMaintain_date() {
        return maintain_date;
    }

    public void setMaintain_date(Date maintain_date) {
        this.maintain_date = maintain_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    @Override
    public String toString() {
        return "MaintainDto{" +
                "maintain_id='" + maintain_id + '\'' +
                ", cost=" + cost +
                ", maintain_date=" + maintain_date +
                ", description='" + description + '\'' +
                ", duration='" + duration + '\'' +
                ", vehicle_id='" + vehicle_id + '\'' +
                '}';
    }
}
