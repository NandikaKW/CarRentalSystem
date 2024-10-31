package lk.ijse.gdse.carrentalsystem.dto.tm;



import java.util.Date;

public class RentTM {
    private String rentId; // changed to camelCase
    private Date startDate; // changed to camelCase
    private Date endDate; // changed to camelCase
    private String custId; // changed to camelCase
    private String agreementId; // added agreementId field

    public RentTM() {
    }

    public RentTM(String rentId, Date startDate, Date endDate, String custId, String agreementId) {
        this.rentId = rentId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.custId = custId;
        this.agreementId = agreementId; // initializing the new field
    }

    public String getRentId() {
        return rentId;
    }

    public void setRentId(String rentId) {
        this.rentId = rentId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getAgreementId() { // added getter for agreementId
        return agreementId;
    }

    public void setAgreementId(String agreementId) { // added setter for agreementId
        this.agreementId = agreementId;
    }

    @Override
    public String toString() {
        return "RentTM{" +
                "rentId='" + rentId + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", custId='" + custId + '\'' +
                ", agreementId='" + agreementId + '\'' + // included in toString
                '}';
    }
}
