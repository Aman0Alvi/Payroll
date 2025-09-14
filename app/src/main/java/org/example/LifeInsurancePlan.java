package org.example;

import java.util.Scanner;

public enum LifeInsurancePlan {
    NONE(0),
    SINGLE(5),
    MARRIED(10),
    MARRIED_WITH_CHILDREN(15);

    private final int cost;
    LifeInsurancePlan(int cost) { this.cost = cost; }

    public double cost(int dependents) {
        if (this == MARRIED_WITH_CHILDREN && dependents <= 0) return 0; 
        return cost;
    }

    public static LifeInsurancePlan promptAndValidate(Scanner in, int dependents) {
        while (true) {
            System.out.println("Which life insurance plan do you want to select?\n");
            System.out.println("  (1) no plan");
            System.out.println("  (2) single plan");
            System.out.println("  (3) married plan");
            System.out.println("  (4) married with children plan\n");
            String choice = in.nextLine().trim();

            switch (choice) {
                case "1": return NONE;
                case "2": return SINGLE;
                case "3": return MARRIED;
                case "4":
                    if (dependents <= 0) {
                        System.out.println("\nSorry! You need at least one child to select that plan.\n");
                        break;
                    }
                    return MARRIED_WITH_CHILDREN;
                default:
                    System.out.println("Please enter 1, 2, 3, or 4.\n");
            }
        }
    }
}
