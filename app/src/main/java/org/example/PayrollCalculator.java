package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PayrollCalculator {
    public static final double OT_MULTIPLIER = 1.5;
    public static final double SOC_SEC = 0.06;
    public static final double FED_TAX = 0.14;
    public static final double STATE_TAX = 0.05;
    public static final double UNION_DUES = 10.00;

    public static Result compute(double hours, double rate, int dependents, LifeInsurancePlan plan) {
        hours = Math.max(0, hours);

        double gross = gross(hours, rate);

        double socSec = round(gross*SOC_SEC);
        double fedTax = round(gross*FED_TAX);
        double stateTax = round(gross*STATE_TAX);

        double healthIns = (dependents >= 3) ? 35.00 : 15.00;

        double lifeIns = (plan == null) ? 0 : plan.cost(dependents);

        double afterTax = round(gross - socSec - fedTax - stateTax);
        double extraDeductions = UNION_DUES + healthIns + lifeIns;

        boolean negativeDeductionsMode = false;
        double net;
        if (afterTax >= extraDeductions) {
            net = round(afterTax - extraDeductions);
        } else {
            negativeDeductionsMode = true;
            net = afterTax;
        }

        return new Result(hours, rate, dependents, gross, socSec, fedTax, stateTax, UNION_DUES, healthIns, lifeIns, net, negativeDeductionsMode);
    }

    public static double gross(double hours, double rate) {
        if (hours <= 40) return round(hours*rate);
        double regular = 40*rate;
        double ot = (hours - 40)*rate*OT_MULTIPLIER;
        return round(regular + ot);
    }

    public static double round(double v) {
        return new BigDecimal(v).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public static class Result {
        public final double hours, rate;
        public final int dependents;
        public final double gross, socSec, fedTax, stateTax, unionDues, healthIns, lifeIns, net;
        public final boolean negativeDeductionsMode;

        Result(double hours, double rate, int dependents, double gross, double socSec, double fedTax, double stateTax, double unionDues, double healthIns, double lifeIns, double net, boolean negativeDeductionsMode) {
            this.hours = hours;
            this.rate = rate;
            this.dependents = dependents;
            this.gross = gross;
            this.socSec = socSec;
            this.fedTax = fedTax;
            this.stateTax = stateTax;
            this.unionDues = unionDues;
            this.healthIns = healthIns;
            this.lifeIns = lifeIns;
            this.net = net;
            this.negativeDeductionsMode = negativeDeductionsMode;
        }
    }
}
