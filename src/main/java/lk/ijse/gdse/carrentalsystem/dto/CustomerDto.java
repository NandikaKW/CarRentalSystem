package lk.ijse.gdse.carrentalsystem.dto;

import java.util.ArrayList;

public class CustomerDto {
    private String cust_id;
    private  String cust_name;
    private String address;
    private String email;
    private String nic;
    private String admin_id;
    private ArrayList<CustomerPaymentDto> customerPaymentDtos=new ArrayList<>();


    public CustomerDto() {
    }

    public CustomerDto(String cust_id, String cust_name, String address, String email, String nic, String admin_id) {
        this.cust_id = cust_id;
        this.cust_name = cust_name;
        this.address = address;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public ArrayList<CustomerPaymentDto> getCustomerPaymentDtos() {
        return customerPaymentDtos;
    }

    public void setCustomerPaymentDtos(ArrayList<CustomerPaymentDto> customerPaymentDtos) {
        this.customerPaymentDtos = customerPaymentDtos;
    }

    @Override
    public String toString() {
        return "CustomerDto{" +
                "cust_id='" + cust_id + '\'' +
                ", cust_name='" + cust_name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", nic='" + nic + '\'' +
                ", admin_id='" + admin_id + '\'' +
                ", customerPaymentDtos=" + customerPaymentDtos +
                '}';
    }
}
