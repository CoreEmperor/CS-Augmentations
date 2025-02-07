package net.corespring.csaugmentations.Utility;

import java.util.EnumMap;
import java.util.Map;

public enum CSOrganTiers implements IOrganTiers {
    REMOVED(0, 2.0, 0.0, 0.0, 0.0, 0.0, 0, 0.0, 0.0, 0.0, 0.0, -5, -20.0),
    NATURAL(1, 20.0, 0.0500000007451, 0.0, 0.5, 2.0, 0, 0.5, 1.0, 0.0, 0.0, 0, 0.0),
    PROSTHETIC(2, 24.0, 0.0600000007451, 0.05, 1.0, 2.5, 60, 0.65, 1.25, 4.0, 1.0, 2, 0.2),
    CYBERNETIC(3, 32.0, 0.0700000007451, 0.1, 2.0, 3.0, 120, 0.75, 1.5, 8.0, 2.0, 3, 0.6);

    private final int tierLevel;
    private final Map<CSOrganTiers.Attribute, Double> doubleAttributes = new EnumMap<>(CSOrganTiers.Attribute.class);
    private final Map<CSOrganTiers.Attribute, Integer> intAttributes = new EnumMap<>(CSOrganTiers.Attribute.class);

    CSOrganTiers(int tierLevel, double health, double speed, double pFallDamageReduction, double attackDamage,
                 double attackSpeed, int pAirTime, double pKidneyEfficiency, double pLiverEfficiency,
                 double pRibsArmor, double pGeneralArmor, int pStomachHunger, double pStomachSat) {
        this.tierLevel = tierLevel;
        doubleAttributes.put(CSOrganTiers.Attribute.HEALTH, health);
        doubleAttributes.put(CSOrganTiers.Attribute.SPEED, speed);
        doubleAttributes.put(CSOrganTiers.Attribute.FALL_DAMAGE_REDUCTION, pFallDamageReduction);
        doubleAttributes.put(CSOrganTiers.Attribute.ATTACK_DAMAGE, attackDamage);
        doubleAttributes.put(CSOrganTiers.Attribute.ATTACK_SPEED, attackSpeed);
        doubleAttributes.put(CSOrganTiers.Attribute.KIDNEY_EFFICIENCY, pKidneyEfficiency);
        doubleAttributes.put(CSOrganTiers.Attribute.LIVER_EFFICIENCY, pLiverEfficiency);
        doubleAttributes.put(CSOrganTiers.Attribute.RIBS_ARMOR, pRibsArmor);
        doubleAttributes.put(CSOrganTiers.Attribute.GENERAL_ARMOR, pGeneralArmor);
        doubleAttributes.put(CSOrganTiers.Attribute.STOMACH_SAT, pStomachSat);
        intAttributes.put(CSOrganTiers.Attribute.AIR_TIME, pAirTime);
        intAttributes.put(CSOrganTiers.Attribute.STOMACH_HUNGER, pStomachHunger);
    }

    @Override
    public Double getDoubleAttribute(CSOrganTiers.Attribute attribute) {
        return doubleAttributes.get(attribute);
    }

    @Override
    public Integer getIntAttribute(CSOrganTiers.Attribute attribute) {
        return intAttributes.get(attribute);
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public int getTierLevel() {
        return tierLevel;
    }

    public double getHealth() {
        return doubleAttributes.get(CSOrganTiers.Attribute.HEALTH);
    }

    public double getSpeed() {
        return doubleAttributes.get(CSOrganTiers.Attribute.SPEED);
    }

    public double getFallDamageReduction() {
        return doubleAttributes.get(CSOrganTiers.Attribute.FALL_DAMAGE_REDUCTION);
    }

    public double getAttackDamage() {
        return doubleAttributes.get(CSOrganTiers.Attribute.ATTACK_DAMAGE);
    }

    public double getAttackSpeed() {
        return doubleAttributes.get(CSOrganTiers.Attribute.ATTACK_SPEED);
    }

    public int getAirTime() {
        return intAttributes.get(CSOrganTiers.Attribute.AIR_TIME);
    }

    public double getKidneyEfficiency() {
        return doubleAttributes.get(CSOrganTiers.Attribute.KIDNEY_EFFICIENCY);
    }

    public double getLiverEfficiency() {
        return doubleAttributes.get(CSOrganTiers.Attribute.LIVER_EFFICIENCY);
    }

    public double getRibsArmor() {
        return doubleAttributes.get(CSOrganTiers.Attribute.RIBS_ARMOR);
    }

    public double getGeneralArmor() {
        return doubleAttributes.get(CSOrganTiers.Attribute.GENERAL_ARMOR);
    }

    public int getStomachHunger() {
        return intAttributes.get(CSOrganTiers.Attribute.STOMACH_HUNGER);
    }

    public double getStomachSat() {
        return doubleAttributes.get(CSOrganTiers.Attribute.STOMACH_SAT);
    }

    public enum Attribute {
        HEALTH, SPEED, FALL_DAMAGE_REDUCTION, ATTACK_DAMAGE, ATTACK_SPEED,
        AIR_TIME, KIDNEY_EFFICIENCY, LIVER_EFFICIENCY, RIBS_ARMOR, GENERAL_ARMOR,
        STOMACH_HUNGER, STOMACH_SAT
    }
}
