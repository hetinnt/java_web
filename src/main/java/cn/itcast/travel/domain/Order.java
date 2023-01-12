package cn.itcast.travel.domain;

public class Order {
    private String type;
    private String a_d;

    public Order() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getA_d() {
        return a_d;
    }

    public void setA_d(String a_d) {
        if(a_d.equals("asc") || a_d.equals("desc")){
            this.a_d = a_d;
        }
    }
}
