package org.is_great.bensalcie.agrihub;

public class Cart {
    private String title,description,image,price;

    public Cart(String title, String description, String image, String price) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Cart()
    {

    }
}
