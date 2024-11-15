package lk.ijse.gdse.carrentalsystem.dto;

import java.math.BigDecimal;
import java.util.Date;

public class CustomerPaymentDto {
    private String cust_id;
    private String pay_id;
    private Date payment_date;
    private BigDecimal amount;

    public CustomerPaymentDto() {
    }

    public CustomerPaymentDto(String cust_id, String pay_id, Date payment_date, BigDecimal amount) {
        this.cust_id = cust_id;
        this.pay_id = pay_id;
        this.payment_date = payment_date;
        this.amount = amount;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "CustomerPaymentDto{" +
                "cust_id='" + cust_id + '\'' +
                ", pay_id='" + pay_id + '\'' +
                ", payment_date=" + payment_date +
                ", amount=" + amount +
                '}';
    }
}
