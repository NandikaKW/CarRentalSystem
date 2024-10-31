package lk.ijse.gdse.carrentalsystem.dto.tm;

import java.math.BigDecimal;

public class EmployeeTM {
    private String emp_id;
    private String emp_name;
    private String address;
    private String job;
    private BigDecimal salary;
    private String admin_id;

    public  EmployeeTM() {
    }

    public EmployeeTM(String emp_id, String emp_name, String address, String job, BigDecimal  salary, String admin_id) {
        this.emp_id = emp_id;
        this.emp_name = emp_name;
        this.address = address;
        this.job = job;
        this.salary = salary;
        this.admin_id = admin_id;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public BigDecimal  getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal  salary) {
        this.salary = salary;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }
}
