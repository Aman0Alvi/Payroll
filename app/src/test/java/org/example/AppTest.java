package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test
    void grossHandlesOvertime() {
        double g = PayrollCalculator.gross(45, 10); // 40*10 + 5*10*1.5 = 475
        assertEquals(475.00, g, 0.001);
    }

    @Test
    void negativePayModeTriggersWhenAfterTaxTooSmall() { // Tiny hours -> afterTax < union+insurance
        PayrollCalculator.Result r = PayrollCalculator.compute(2, 16.78, 4, LifeInsurancePlan.NONE);
        assertTrue(r.negativeDeductionsMode);
        assertEquals(PayrollCalculator.round(r.gross - r.socSec - r.fedTax - r.stateTax), r.net, 0.001);
    }
}
