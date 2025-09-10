package project.model;

/**
 * Representa una venta de un producto con su cantidad.
 */
public class Sale {
    private String productId;
    private int quantity;

    public Sale(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
