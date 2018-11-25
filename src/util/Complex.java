package util;

import java.math.BigDecimal;

public class Complex {
    public BigDecimal r;
    public BigDecimal i;
    public Complex(float _r, float _i) {
        r = new BigDecimal(Float.toString(_r));
        i = new BigDecimal(Float.toString(_i));
    }
    public Complex(BigDecimal _r, BigDecimal _i) {
        r = _r;
        i = _i;
    }
    public Complex() {}

    public Complex(double r, double i) {
        this.r = new BigDecimal(r);
        this.i = new BigDecimal(i);
    }

    public BigDecimal abs() {
        return this.r.abs().add(this.i.abs());
    }
}
