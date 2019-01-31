package misc;

import javax.swing.text.NumberFormatter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class BigDecimalTools {
    class BigDecimalFormatter extends NumberFormatter {
        private static final long serialVersionUID = 0; //get rid of warning

//        BigDecimalFormatter()
//        {
//            //setAllowsInvalid(false);
//        }

        public Object stringToValue(String text) { //throws ParseException
            if("".equals(text.trim()))
            {
                return null;
            }
            char ds = getDefaultLocaleDecimalSeparator();


            try
            {
                String val = text;
                if(ds != '.')
                {
                    val = val.replace(".", "").replace(ds, '.');
                }
                return new BigDecimal(val);
            } catch(NumberFormatException e)
            {
                return null;
            }
        }

        public String valueToString(Object value) { //throws ParseException
            if (value!=null)
                return value.toString();
            else
                return null;
        }

        private char getDefaultLocaleDecimalSeparator() {
            DecimalFormatSymbols symbols = new DecimalFormat("0").getDecimalFormatSymbols();
            char ds = symbols.getDecimalSeparator();
            return ds;
        }

    }


//    class BigDecimalFormat extends Format {
//        private static final long serialVersionUID = 0; //get rid of warning
//        BigDecimal mOld_value;
//        String mOld_string;
//
//        String Format(Object number) {
//            BigDecimal x = (BigDecimal) number;
//            return x.toString();
//        }
//
//        public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
//            return null;
//        }
//
//        public Object parseObject(String source) {
//            mOld_string = null;
//            try {
//                BigDecimal x = new BigDecimal(source);
//                mOld_value = x;
//                if (source.endsWith(".") || source.contentEquals("-0") || source.contains("E") || source.contains("e"))
//                    mOld_string = source;
//                return x;
//            } catch (NumberFormatException e) {
//                if (source.length() == 0) {
//                    mOld_value = null;
//                    mOld_string = null;
//                    return null;
//                }
//                if (source.equals("-") || source.endsWith("E") || source.endsWith("e") || source.endsWith("-")) {
//                    mOld_string = source;
//                    mOld_value = new BigDecimal(0);
//                    return mOld_value;
//                }
//                return mOld_value;
//            }
//        }
//
//        @Override
//        public StringBuffer format(Object arg0, StringBuffer arg1,
//                                   FieldPosition arg2) {
//            if (mOld_string != null && mOld_value.equals(arg0)) {
//                arg1.append(mOld_string);
//                return arg1;
//            }
//
//            BigDecimal x = (BigDecimal) arg0;
//            String str = x.toString();
//            arg1.append(str);
//            return arg1;
//        }
//    }
}
