package lk.ijse.gdse.carrentalsystem.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

import java.awt.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartTM {
    private String vehicle_id;
    private String quantity;
    private String PackageId;
    private Button removeBtn;
}
