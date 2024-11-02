package lk.ijse.gdse.carrentalsystem.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentDto {
    private  String pay_id;
    private BigDecimal amount;
    private Date date;
    private  String invoice;
    private  String method;
    private String transaction_reference;
    private BigDecimal tax;
    private BigDecimal Discount;

    public PaymentDto() {
    }

    public PaymentDto(String pay_id, BigDecimal amount, Date date, String invoice, String method, String transaction_reference, BigDecimal tax, BigDecimal Discount) {
        this.pay_id = pay_id;
        this.amount = amount;
        this.date = date;
        this.invoice = invoice;
        this.method = method;
        this.transaction_reference = transaction_reference;
        this.tax = tax;
        this.Discount = Discount;
    }

    public String getPay_id() {
        return pay_id;
    }

    public void setPay_id(String pay_id) {
        this.pay_id = pay_id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTransaction_reference() {
        return transaction_reference;
    }

    public void setTransaction_reference(String transaction_reference) {
        this.transaction_reference = transaction_reference;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getDiscount_applied() {
        return Discount;
    }

    public void setDiscount_applied(BigDecimal Discount) {
        this.Discount = Discount;
    }

    @Override
    public String toString() {
        return "PaymentDto{" +
                "pay_id='" + pay_id + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", invoice='" + invoice + '\'' +
                ", method='" + method + '\'' +
                ", transaction_reference='" + transaction_reference + '\'' +
                ", tax=" + tax +
                ", discount_applied=" + Discount +
                '}';
    }
}
