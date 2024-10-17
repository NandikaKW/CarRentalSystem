package lk.ijse.gdse.carrentalsystem.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PackageDto {
    private String packageId;
    private String packageName;
    private BigDecimal totalCost;
    private boolean insuranceIncluded;
    private String rentalDuration;
    private Date rentDate;
    private String mileageLimit;
    private String description;

    public PackageDto() {
    }

    public PackageDto(String packageId, String packageName, BigDecimal totalCost, boolean insuranceIncluded, String rentalDuration, Date rentDate, String mileageLimit, String description) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.totalCost = totalCost;
        this.insuranceIncluded = insuranceIncluded;
        this.rentalDuration = rentalDuration;
        this.rentDate = rentDate;
        this.mileageLimit = mileageLimit;
        this.description = description;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public boolean isInsuranceIncluded() {
        return insuranceIncluded;
    }

    public void setInsuranceIncluded(boolean insuranceIncluded) {
        this.insuranceIncluded = insuranceIncluded;
    }

    public String getRentalDuration() {
        return rentalDuration;
    }

    public void setRentalDuration(String rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    public Date getRentDate() {
        return rentDate;
    }

    public void setRentDate(Date rentDate) {
        this.rentDate = rentDate;
    }

    public String getMileageLimit() {
        return mileageLimit;
    }

    public void setMileageLimit(String mileageLimit) {
        this.mileageLimit = mileageLimit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PackageDto{" +
                "packageId='" + packageId + '\'' +
                ", packageName='" + packageName + '\'' +
                ", totalCost=" + totalCost +
                ", insuranceIncluded=" + insuranceIncluded +
                ", rentalDuration='" + rentalDuration + '\'' +
                ", rentDate=" + rentDate +
                ", mileageLimit='" + mileageLimit + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
