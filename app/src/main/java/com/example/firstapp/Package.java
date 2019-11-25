package com.example.firstapp;
enum PackageType{ENVELOPE,SMALL_PACKAGE,BIG_PACKAGE};
enum PackageWeight{UNTIL_500_GR,UNTIL_1_KG,UNTIL_5_KG,UNTIL_20_KG};
public class Package {
    private PackageType packageType;
    private boolean isFragile;
    private PackageWeight packageWeight;
    private String warehouseLocation;
    private String recipientName;
    private String recipientAddress;
    private String phoneNumber;


    //-------------Ctors--------------------//
    public Package() {
    }

    public Package(PackageType packageType, boolean isFragile, PackageWeight packageWeight, String warehouseLocation, String recipientName, String recipientAddress, String phoneNumber) {
        this.packageType = packageType;
        this.isFragile = isFragile;
        this.packageWeight = packageWeight;
        this.warehouseLocation = warehouseLocation;
        this.recipientName = recipientName;
        this.recipientAddress = recipientAddress;
        this.phoneNumber = phoneNumber;
    }
    //-------------------------------------//


    //--------------Ges&Set-----------------//
    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public boolean isFragile() {
        return isFragile;
    }

    public void setFragile(boolean fragile) {
        isFragile = fragile;
    }

    public PackageWeight getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(PackageWeight packageWeight) {
        this.packageWeight = packageWeight;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    //------------------------------------------//
}
