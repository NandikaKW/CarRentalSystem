package lk.ijse.gdse.carrentalsystem.dto.tm;

import lombok.*;

import java.awt.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReserveTM {
    private  String vehicle_id;
    private  String model;
    private String colour;
    private String category;
    private int quantity;
    private String package_id;
    private Button removeBtn;

}
