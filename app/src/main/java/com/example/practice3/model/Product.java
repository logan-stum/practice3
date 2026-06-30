package com.example.practice3.model;

import java.io.Serializable;

/**
 * Represents a single product in the catalog.
 * Implements Serializable so a list of Products can be passed
 * between activities via Intent extras.
 */
public class Product implements Serializable {

    /** Unique identifier for the product. */
    private int id;
    
    /** Name of the product. */
    private String name;
    
    /** Detailed description of the product. */
    private String description;
    
    /** Name of the company or individual selling the product. */
    private String seller;
    
    /** Price of the product in the local currency. */
    private double price;
    
    /** Name of a drawable resource bundled with the app (e.g. "ic_product_shoe"). */
    private String imageResName;

    /**
     * Default constructor for Product.
     */
    public Product() {
    }

    /**
     * Constructs a new Product with the specified details.
     *
     * @param id           The unique identifier.
     * @param name         The name of the product.
     * @param description  The product description.
     * @param seller       The seller's name.
     * @param price        The product price.
     * @param imageResName The resource name for the product's image.
     */
    public Product(int id, String name, String description, String seller,
                   double price, String imageResName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.seller = seller;
        this.price = price;
        this.imageResName = imageResName;
    }

    /**
     * @return The product ID.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The product ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The product name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The product name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The product description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The product description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The seller's name.
     */
    public String getSeller() {
        return seller;
    }

    /**
     * @param seller The seller's name to set.
     */
    public void setSeller(String seller) {
        this.seller = seller;
    }

    /**
     * @return The product price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price The product price to set.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return The resource name of the product image.
     */
    public String getImageResName() {
        return imageResName;
    }

    /**
     * @param imageResName The resource name of the product image to set.
     */
    public void setImageResName(String imageResName) {
        this.imageResName = imageResName;
    }
}
