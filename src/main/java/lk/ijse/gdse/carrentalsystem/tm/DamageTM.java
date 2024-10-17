package lk.ijse.gdse.carrentalsystem.tm;

import java.math.BigDecimal;

public class DamageTM {
    private String damage_id;
    private BigDecimal repair_cost;
    private String detail;
    private String rent_id;

    public DamageTM() {
    }

    public DamageTM(String damage_id, BigDecimal repair_cost, String detail, String rent_id) {
        this.damage_id = damage_id;
        this.repair_cost = repair_cost;
        this.detail = detail;
        this.rent_id = rent_id;
    }

    public String getDamage_id() {
        return damage_id;
    }

    public void setDamage_id(String damage_id) {
        this.damage_id = damage_id;
    }

    public BigDecimal getRepair_cost() {
        return repair_cost;
    }

    public void setRepair_cost(BigDecimal repair_cost) {
        this.repair_cost = repair_cost;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getRent_id() {
        return rent_id;
    }

    public void setRent_id(String rent_id) {
        this.rent_id = rent_id;
    }

    @Override
    public String toString() {
        return "DamageTM{" +
                "damage_id='" + damage_id + '\'' +
                ", repair_cost=" + repair_cost +
                ", detail='" + detail + '\'' +
                ", rent_id='" + rent_id + '\'' +
                '}';
    }
}
