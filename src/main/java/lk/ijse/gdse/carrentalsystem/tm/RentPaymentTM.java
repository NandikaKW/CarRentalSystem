package lk.ijse.gdse.carrentalsystem.tm;

import java.math.BigDecimal;
import java.util.Date;

public class RentPaymentTM {
    private String rent_id;
    private String pay_id;
    private Date payment_date;
    private Integer duration;
    private String description;
    private BigDecimal pay_amount;
    private  String payment_method;

    public RentPaymentTM() {
    }

    public RentPaymentTM(String rent_id, String pay_id, Date payment_date, Integer duration, String description, BigDecimal pay_amount, String payment_method) {
        this.rent_id = rent_id;
        this.pay_id = pay_id;
        this.payment_date = payment_date;
        this.duration = duration;
        this.description = description;
        this.pay_amount = pay_amount;
        this.payment_method = payment_method;
    }

    public String getRent_id() {
        return rent_id;
    }

    public void setRent_id(String rent_id) {
        this.rent_id = rent_id;
    }

    public String getPay_id() {
        return pay_id;
    }

    public void setPay_id(String pay_id) {
        this.pay_id = pay_id;
    }

    public Date getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(Date payment_date) {
        this.payment_date = payment_date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(BigDecimal pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    @Override
    public String toString() {
        return "RentPayemntDto{" +
                "rent_id='" + rent_id + '\'' +
                ", pay_id='" + pay_id + '\'' +
                ", payment_date=" + payment_date +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                ", pay_amount=" + pay_amount +
                ", payment_method='" + payment_method + '\'' +
                '}';
    }
}

