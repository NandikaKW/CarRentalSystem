package lk.ijse.gdse.carrentalsystem.dto.tm;


public class CustomerTM {
    private String cust_id;
    private  String cust_name;
    private String address;
    private String contact;
    private String nic;
    private String admin_id;

    public CustomerTM() {
    }

    public CustomerTM(String cust_id, String cust_name, String address, String contact, String nic, String admin_id) {
        this.cust_id = cust_id;
        this.cust_name = cust_name;
        this.address = address;
        this.contact = contact;
        this.nic = nic;
        this.admin_id = admin_id;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }
}
