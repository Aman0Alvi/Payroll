package org.example;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        NumberFormat money = NumberFormat.getCurrencyInstance(Locale.US);

        System.out.println("Welcome to the Payroll Program!\n");

        double hours = promptDouble(in, "How many hours did you work this week? ", 0.0, null, true);
        int children = (int) promptDouble(in, "How many children do you have?  ", null, null, true);

        if (children < 0) {
            System.out.println("Negative number of children detected; treating as 0.");
            children = 0;
        }

        double rate = promptOptionalRate(in, 16.78);
        LifeInsurancePlan plan = LifeInsurancePlan.promptAndValidate(in, children);

        PayrollCalculator.Result r = PayrollCalculator.compute(hours, rate, children, plan);

        System.out.println("\nPayroll Stub:\n");

        final String leftLabelFmt  = "   %-6s %8.1f%n";
        final String leftRateFmt   = "   %-6s %8.2f $/hr%n";
        final String moneyLineFmt  = "  %-6s %12s%n";
        final String moneyLineFmt2 = " %-7s %12s%n";

        System.out.printf(leftLabelFmt, "Hours:", r.hours);
        System.out.printf(leftRateFmt, "Rate:", r.rate);
        System.out.printf(moneyLineFmt, "Gross:", money.format(r.gross));
        System.out.println();

        System.out.printf(moneyLineFmt, "SocSec:", money.format(r.socSec));
        System.out.printf(moneyLineFmt, "FedTax:", money.format(r.fedTax));
        System.out.printf(moneyLineFmt, "StTax:", money.format(r.stateTax));

        if (!r.negativeDeductionsMode) {
            System.out.printf(moneyLineFmt, "Union:", money.format(r.unionDues));
            System.out.printf(moneyLineFmt, "Ins:", money.format(r.healthIns));
            if (r.lifeIns > 0) {
                System.out.printf(moneyLineFmt2, "LifeIns:", money.format(r.lifeIns));
            }
            System.out.println();
            System.out.printf(moneyLineFmt, "Net:", money.format(r.net));
            System.out.println();
        } else {
            System.out.println();
            System.out.printf(moneyLineFmt, "Net:", money.format(r.net));
            System.out.println();
            System.out.println("The employee still owes:\n");
            System.out.printf(moneyLineFmt, "Union:", money.format(r.unionDues));
            System.out.printf(moneyLineFmt, "Ins:", money.format(r.healthIns));
            if (r.lifeIns > 0) {
                System.out.printf(moneyLineFmt2, "LifeIns:", money.format(r.lifeIns));
            }
        }

        System.out.println("\nThank you for using the Payroll Program!");
        in.close();
    }

    private static double promptDouble(Scanner in, String prompt, Double min, Double max, boolean wholeLine) {
        while (true) {
            System.out.print(prompt);
            try {
                String s = wholeLine ? in.nextLine().trim() : in.next().trim();
                double v = Double.parseDouble(s);
                if (min != null && v < min) {
                    System.out.println("Please enter a value >= " + min + ".");
                    continue;
                }
                if (max != null && v > max) {
                    System.out.println("Please enter a value <= " + max + ".");
                    continue;
                }
                return v;
            } catch (Exception e) {
                System.out.println("Please enter a valid number.");
                if (!wholeLine) in.nextLine();
            }
        }
    }

    private static double promptOptionalRate(Scanner in, double defaultRate) {
        while (true) {
            System.out.print("Enter your pay rate (press Enter for default " + defaultRate + "): ");
            String line = in.nextLine().trim();
            if (line.isEmpty()) return defaultRate;
            try {
                double v = Double.parseDouble(line);
                if (v < 0) {
                    System.out.println("Pay rate cannot be negative. Please re-enter.");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number (or press Enter for default).");
            }
        }
    }
}
