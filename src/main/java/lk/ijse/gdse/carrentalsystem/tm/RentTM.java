package lk.ijse.gdse.carrentalsystem.tm;



import java.util.Date;

public class RentTM {
    private  String rent_id;
    private Date StartDate;
    private Date EndDate;
    private String Cust_id;

    public RentTM() {
    }

    public RentTM(String rent_id, Date startDate, Date endDate, String cust_id) {
        this.rent_id = rent_id;
        StartDate = startDate;
        EndDate = endDate;
        Cust_id = cust_id;
    }

    public String getRent_id() {
        return rent_id;
    }

    public void setRent_id(String rent_id) {
        this.rent_id = rent_id;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        EndDate = endDate;
    }

    public String getCust_id() {
        return Cust_id;
    }

    public void setCust_id(String cust_id) {
        Cust_id = cust_id;
    }

    @Override
    public String toString() {
        return "RentDto{" +
                "rent_id='" + rent_id + '\'' +
                ", StartDate=" + StartDate +
                ", EndDate=" + EndDate +
                ", Cust_id='" + Cust_id + '\'' +
                '}';
    }
}
