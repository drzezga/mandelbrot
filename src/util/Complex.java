package util;

public class Complex {
    public DoubleDouble r;
    public DoubleDouble i;
    public Complex(float _r, float _i) {
        r = new DoubleDouble(Float.toString(_r));
        i = new DoubleDouble(Float.toString(_i));
    }
    public Complex(DoubleDouble _r, DoubleDouble _i) {
        r = _r;
        i = _i;
    }
    public Complex() {}
    public void print() {
        System.out.println(r.toString() + " " + i.toString());
    }
}
