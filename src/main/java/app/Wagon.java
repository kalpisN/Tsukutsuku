package app;

public class Wagon {
    private String wagonType;
    private int location;
    private int salesNumber;
    private int length;
    private boolean pet;
    private boolean catering;

    public String getWagonType() {
        return wagonType;
    }

    public int getLocation() {
        return location;
    }

    public int getSalesNumber() {
        return salesNumber;
    }

    public int getLength() {
        return length;
    }

    public Boolean getPet() {
        return pet;
    }

    public void setWagonType(String wagonType) {
        this.wagonType = wagonType;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public void setSalesNumber(int salesNumber) {
        this.salesNumber = salesNumber;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setPet(boolean pet) {
        this.pet = pet;
    }

    public void setCatering(boolean catering) {
        this.catering = catering;
    }

    public boolean getCatering() {
        return catering;


    }


}
