package fr.eljugador.domain;

public class CourseSum {
    private int month;
    private int year;
    private float amount;


    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format("%d/%d -> %.2f €", month, year, amount);
    }
}
