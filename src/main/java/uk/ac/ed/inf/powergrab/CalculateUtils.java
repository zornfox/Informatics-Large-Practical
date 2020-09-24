package uk.ac.ed.inf.powergrab;

import java.math.BigDecimal;

/*
 * avoid losing precision of data
 */
public class CalculateUtils {

    public static double DoubleAdd(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    public static double DoubleSubtract(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    public static double DoubleMult(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }


    public static double DoubleDivide(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal result = b1.divide(b2,8, BigDecimal.ROUND_HALF_UP);// round the result to nearest 8 decimal places
        return result.doubleValue();
		
    }

}
