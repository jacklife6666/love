import java.util.Date;

public class Emp {

    private int cc;
    private double bb;
    private Date f;
    public String name;

    @Override
    public String toString() {
        return "Emp{" +
                "cc=" + cc +
                ", bb=" + bb +
                ", f=" + f +
                ", name='" + name + '\'' +
                '}';
    }

    public int getCc() {
        return cc;
    }

    public void setCc(int cc) {
        this.cc = cc;
    }

    public double getBb() {
        return bb;
    }

    public void setBb(double bb) {
        this.bb = bb;
    }

    public Date getF() {
        return f;
    }

    public void setF(Date f) {
        this.f = f;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Emp(int cc, double bb, Date f, String name) {
        this.cc = cc;
        this.bb = bb;
        this.f = f;
        this.name = name;
    }
}
