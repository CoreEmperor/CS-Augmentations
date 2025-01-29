package net.corespring.csaugmentations.Utility;

import java.util.EnumMap;
import java.util.Map;

public enum CSOrganTiers {
    REMOVED(2.0, 0.0, 0.0, 0.0, 0.0, 0, 0.0, 0.0, 0.0, 0.0, -5, -20.0),
    NATURAL(20.0, 0.0500000007451, 0.0, 0.5, 2.0, 0, 0.5, 1.0, 0.0, 0.0, 0, 0.0),
    PROSTHETIC(24.0, 0.0600000007451, 0.05, 1.0, 2.5, 60, 0.65, 1.25, 4.0, 1.0, 2, 0.2),
    CYBERNETIC(28.0, 0.0700000007451, 0.1, 2.0, 3.0, 120, 0.75, 1.5, 8.0, 2.0, 4, 0.8);

    public enum Attribute {
        HEALTH, SPEED, FALL_DAMAGE_REDUCTION, ATTACK_DAMAGE, ATTACK_SPEED,
        AIR_TIME, KIDNEY_EFFICIENCY, LIVER_EFFICIENCY, RIBS_ARMOR, GENERAL_ARMOR,
        STOMACH_HUNGER, STOMACH_SAT;
    }

    private final Map<Attribute, Double> doubleAttributes = new EnumMap<>(Attribute.class);
    private final Map<Attribute, Integer> intAttributes = new EnumMap<>(Attribute.class);

    CSOrganTiers(double health, double speed, double pFallDamageReduction, double attackDamage,
                 double attackSpeed, int pAirTime, double pKidneyEfficiency, double pLiverEfficiency,
                 double pRibsArmor, double pGeneralArmor, int pStomachHunger, double pStomachSat) {
        doubleAttributes.put(Attribute.HEALTH, health);
        doubleAttributes.put(Attribute.SPEED, speed);
        doubleAttributes.put(Attribute.FALL_DAMAGE_REDUCTION, pFallDamageReduction);
        doubleAttributes.put(Attribute.ATTACK_DAMAGE, attackDamage);
        doubleAttributes.put(Attribute.ATTACK_SPEED, attackSpeed);
        doubleAttributes.put(Attribute.KIDNEY_EFFICIENCY, pKidneyEfficiency);
        doubleAttributes.put(Attribute.LIVER_EFFICIENCY, pLiverEfficiency);
        doubleAttributes.put(Attribute.RIBS_ARMOR, pRibsArmor);
        doubleAttributes.put(Attribute.GENERAL_ARMOR, pGeneralArmor);
        doubleAttributes.put(Attribute.STOMACH_SAT, pStomachSat);
        intAttributes.put(Attribute.AIR_TIME, pAirTime);
        intAttributes.put(Attribute.STOMACH_HUNGER, pStomachHunger);
    }

    public Double getDoubleAttributes(Attribute attribute) {
        return doubleAttributes.get(attribute);
    }

    public Integer getIntAttributes(Attribute attribute) {
        return intAttributes.get(attribute);
    }

    public boolean isAboveOrEqual(CSOrganTiers altTier) {
        return this.compareTo(altTier) >= 0;
    }

    public double getHealth() {
        return  doubleAttributes.get(Attribute.HEALTH);
    }

    public double getSpeed() {
        return  doubleAttributes.get(Attribute.SPEED);
    }

    public double getFallDamageReduction() {
        return  doubleAttributes.get(Attribute.FALL_DAMAGE_REDUCTION);
    }

    public double getAttackDamage() {
        return  doubleAttributes.get(Attribute.ATTACK_DAMAGE);
    }

    public double getAttackSpeed() {
        return  doubleAttributes.get(Attribute.ATTACK_SPEED);
    }

    public int getAirTime() {
        return intAttributes.get(Attribute.AIR_TIME);
    }

    public double getKidneyEfficiency() {
        return  doubleAttributes.get(Attribute.KIDNEY_EFFICIENCY);
    }

    public double getLiverEfficiency() {
        return  doubleAttributes.get(Attribute.LIVER_EFFICIENCY);
    }

    public double getRibsArmor() {
        return  doubleAttributes.get(Attribute.RIBS_ARMOR);
    }

    public double getGeneralArmor() {
        return  doubleAttributes.get(Attribute.GENERAL_ARMOR);
    }

    public int getStomachHunger() {
        return intAttributes.get(Attribute.STOMACH_HUNGER);
    }

    public double getStomachSat() {
        return  doubleAttributes.get(Attribute.STOMACH_SAT);
    }
}
