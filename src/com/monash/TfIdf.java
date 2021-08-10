package com.monash;

import java.math.BigDecimal;

public class TfIdf {

    public TfIdf()
    {
    }

    public double calculateTfIdf(double tf, double idf)
    {
        double tfIdf = new BigDecimal(tf * idf).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();

        return tfIdf;
    }
}
