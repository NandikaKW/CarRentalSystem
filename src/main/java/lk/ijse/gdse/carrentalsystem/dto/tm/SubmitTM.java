package lk.ijse.gdse.carrentalsystem.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubmitTM {
    private  String paymentId;
   private String rentId;
   private String custId;
   private BigDecimal amount;
   private Button removeBtn;


}
