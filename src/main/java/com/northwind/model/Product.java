package com.northwind.model;

public class Product {
    private int productId;
    private String productName;
    private int supplierId;
    private int categoryId;
    private String quantityPerUnit;
    private double unitPrice;
    private int unitsInStock;
    private int unitsOnOrder;
    private int reorderLevel;
    private boolean discontinued;


    public Product() {
    }

    public Product(int productId, String productName, int supplierId, int categoryId, String quantityPerUnit, double unitPrice, int unitsInStock, int unitsOnOrder, int reorderLevel, boolean discontinued) {
        this.productId = productId;
        this.productName = productName;
        this.supplierId = supplierId;
        this.categoryId = categoryId;
        this.quantityPerUnit = quantityPerUnit;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
        this.unitsOnOrder = unitsOnOrder;
        this.reorderLevel = reorderLevel;
        this.discontinued = discontinued;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getQuantityPerUnit() {
        return quantityPerUnit;
    }

    public void setQuantityPerUnit(String quantityPerUnit) {
        this.quantityPerUnit = quantityPerUnit;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getUnitsInStock() {
        return unitsInStock;
    }

    public void setUnitsInStock(int unitsInStock) {
        this.unitsInStock = unitsInStock;
    }

    public int getUnitsOnOrder() {
        return unitsOnOrder;
    }

    public void setUnitsOnOrder(int unitsOnOrder) {
        this.unitsOnOrder = unitsOnOrder;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public boolean isDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(boolean discontinued) {
        this.discontinued = discontinued;
    }

    @Override
    public String toString() {
        return String.format("""
            ╔════════════════════════════════════════╗
            ║            PRODUCT DETAILS             ║
            ╠════════════════════════════════════════╣
            ║ ID: %-34d ║
            ║ Name: %-32s ║
            ║ Supplier ID: %-26d ║
            ║ Category ID: %-26d ║
            ║ Quantity/Unit: %-24s ║
            ║ Unit Price: $%-25.2f ║
            ║ In Stock: %-28d ║
            ║ On Order: %-28d ║
            ║ Reorder Level: %-24d ║
            ║ Discontinued: %-25s ║
            ╚════════════════════════════════════════╝
            """,
                productId,
                productName,
                supplierId,
                categoryId,
                quantityPerUnit,
                unitPrice,
                unitsInStock,
                unitsOnOrder,
                reorderLevel,
                discontinued ? "Yes" : "No"
        );
    }
}
