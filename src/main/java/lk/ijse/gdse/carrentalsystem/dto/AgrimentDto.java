package lk.ijse.gdse.carrentalsystem.dto;

import java.math.BigDecimal;
import java.util.Date;

public class AgrimentDto {
    private String agreement_id;
    private String payment_terms;
    private Date start_date;
    private Date end_date;
    private BigDecimal deposit_amount;
    private BigDecimal total_rent_cost;


    public AgrimentDto() {
    }

    public AgrimentDto(String agreement_id, String payment_terms, Date start_date, Date end_date, BigDecimal deposit_amount, BigDecimal total_rent_cost) {
        this.agreement_id = agreement_id;
        this.payment_terms = payment_terms;
        this.start_date = start_date;
        this.end_date = end_date;
        this.deposit_amount = deposit_amount;
        this.total_rent_cost = total_rent_cost;
    }

    public String getAgreement_id() {
        return agreement_id;
    }

    public void setAgreement_id(String agreement_id) {
        this.agreement_id = agreement_id;
    }

    public String getPayment_terms() {
        return payment_terms;
    }

    public void setPayment_terms(String payment_terms) {
        this.payment_terms = payment_terms;
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

    public BigDecimal getDeposit_amount() {
        return deposit_amount;
    }

    public void setDeposit_amount(BigDecimal deposit_amount) {
        this.deposit_amount = deposit_amount;
    }

    public BigDecimal getTotal_rent_cost() {
        return total_rent_cost;
    }

    public void setTotal_rent_cost(BigDecimal total_rent_cost) {
        this.total_rent_cost = total_rent_cost;
    }

    @Override
    public String toString() {
        return "AgrimentDto{" +
                "agreement_id='" + agreement_id + '\'' +
                ", payment_terms='" + payment_terms + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", deposit_amount=" + deposit_amount +
                ", total_rent_cost=" + total_rent_cost +
                '}';
    }
}
