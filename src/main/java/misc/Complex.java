package misc;

import java.math.BigDecimal;

public class Complex {

    public BigDecimal r;
    public BigDecimal i;

    public static BigDecimal one = new BigDecimal("1");

    public Complex(float _r, float _i) {
//        try {
            r = new BigDecimal(Float.toString(_r));
            i = new BigDecimal(Float.toString(_i));
//        } catch (java.lang.NumberFormatException e) {
//            System.err.println(_r + " " + _i);
//        }
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

    public Complex copy() {
        Complex tempComplex = new Complex();
        tempComplex.i = i.multiply(one);
        tempComplex.r = r.multiply(one);
        return tempComplex;
    }

    public BigDecimal abs() {
        return this.r.abs().add(this.i.abs());
    }
}
