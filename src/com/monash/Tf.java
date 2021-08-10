package com.monash;

import java.math.BigDecimal;

public class Tf {

    // 输入term
    // 输入某一个文档或者query，计算总词数量
    // 计算term在某个文档中出现的次数
    // 由于tf太小，总是会出现0，所以不采取四舍五入保留三位小数的方式
    // 采取直接保留三位小数的方式

    public Tf()
    {

    }

    public double calculateTf(int noOfTerm, int noOfAllTerm)
    {


        double tf = new BigDecimal((double)noOfTerm/(double)noOfAllTerm).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();

        return tf;
    }


}
