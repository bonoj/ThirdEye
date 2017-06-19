package com.hk47.realityoverlay.data;

public class CustomerReview {
    private String author_name = "";
    private int customer_rating = 0;
    private String relative_time_description = "";
    private String review_text = "";


    public CustomerReview(String author_name,
                          int customer_rating,
                          String relative_time_description,
                          String review_text) {
        this.author_name = author_name;
        this.customer_rating = customer_rating;
        this.relative_time_description = relative_time_description;
        this.review_text = review_text;
    }

    public String[] getCustomerReview() {
        return new String[] {
                author_name,
                String.valueOf(customer_rating),
                review_text,
                relative_time_description };
    }

}
