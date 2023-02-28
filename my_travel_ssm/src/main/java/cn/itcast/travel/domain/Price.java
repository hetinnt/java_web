package cn.itcast.travel.domain;

public class Price {
    private int lowprice;
    private int highprice;

    public Price() {
        lowprice = 0;
        highprice = Integer.MAX_VALUE;
    }

    public int getLowprice() {
        return lowprice;
    }

    public void setLowprice(int lowprice) {
        this.lowprice = lowprice;
    }

    public int getHighprice() {
        return highprice;
    }

    public void setHighprice(int highprice) {
        this.highprice = highprice;
    }
}
