package fr.eljugador.domain;

import java.sql.Date;
import java.util.List;

public class Course {
    private long id;
    private Float amount;
    private Date closingDate;
    private List<Produit> products;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public Long getClosingDateTime(){
        return closingDate != null ? closingDate.getTime() : null;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public List<Produit> getProducts() {
        return products;
    }

    public void setProducts(List<Produit> products) {
        this.products = products;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
