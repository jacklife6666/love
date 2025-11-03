package cn.itcast.domin;

import java.util.Date;

public class emp {
    private String xm;
    private String xb;
    private Date sr;

    @Override
    public String toString() {
        return "emp{" +
                "xm='" + xm + '\'' +
                ", xb='" + xb + '\'' +
                ", sr=" + sr +
                ", xh='" + xh + '\'' +
                '}';
    }

    private String xh;

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public Date getSr() {
        return sr;
    }

    public void setSr(Date sr) {
        this.sr = sr;
    }


}
