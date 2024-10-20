package lk.ijse.gdse.carrentalsystem.tm;

public class VehicleTM {
    private  String vehicle_id;
    private  String model;
    private String colour;
    private String category;
    private int quantity;
    private String package_id;

    public VehicleTM() {
    }

    public VehicleTM(String vehicle_id, String model, String colour, String category, int quantity, String package_id) {
        this.vehicle_id = vehicle_id;
        this.model = model;
        this.colour = colour;
        this.category = category;
        this.quantity = quantity;
        this.package_id = package_id;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    @Override
    public String toString() {
        return "VehicleDto{" +
                "vehicle_id='" + vehicle_id + '\'' +
                ", model='" + model + '\'' +
                ", colour='" + colour + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", package_id='" + package_id + '\'' +
                '}';
    }

}