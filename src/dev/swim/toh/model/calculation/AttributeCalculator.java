package dev.swim.toh.model.calculation;

public class AttributeCalculator {
    public static int getAttributeMod(int attributeBase) {
        return (int) Math.floor((attributeBase - 10) / 2.0);
    }
}
