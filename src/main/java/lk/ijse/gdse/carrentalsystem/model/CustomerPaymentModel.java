package lk.ijse.gdse.carrentalsystem.model;

import lk.ijse.gdse.carrentalsystem.dto.CustomerPaymentDto;
import lk.ijse.gdse.carrentalsystem.util.CrudUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerPaymentModel {

    public static boolean saveCustomerPaymentList(ArrayList<CustomerPaymentDto> customerPaymentDtos) throws SQLException, ClassNotFoundException {
        for (CustomerPaymentDto customerPaymentDto : customerPaymentDtos) {
            boolean isCustomerPaymentSaved = saveCustomerPayment(customerPaymentDto);
            if (!isCustomerPaymentSaved) {
                return false;
            }
            boolean isPaymentUpdated=PaymentModel.reducePaymentAmount(customerPaymentDto);
            if (!isPaymentUpdated) {
                return false;
            }
        }
        return true;

    }

    private static boolean saveCustomerPayment(CustomerPaymentDto customerPaymentDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO customerpayment VALUES (?,?,?,?)", customerPaymentDto.getCust_id(), customerPaymentDto.getPay_id(), customerPaymentDto.getPayment_date(), customerPaymentDto.getAmount());

    }
}
