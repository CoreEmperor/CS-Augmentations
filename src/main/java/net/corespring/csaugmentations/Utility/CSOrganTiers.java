package net.corespring.csaugmentations.Utility;

import java.util.EnumMap;
import java.util.Map;

public enum CSOrganTiers {
    REMOVED(2.0, 0.0, 0.0, 0.0, 0.0, 0, 0.0, 0.0, 0.0, 0.0, -5, -20.0),
    NATURAL(20.0, 0.0500000007451, 0.0, 0.5, 2.0, 0, 0.5, 1.0, 0.0, 0.0, 0, 0.0),
    PROSTHETIC(24.0, 0.0600000007451, 0.05, 1.0, 2.5, 60, 0.65, 1.5, 4.0, 1.0, 2, 0.2),
    CYBERNETIC(28.0, 0.0700000007451, 0.1, 2.0, 3.0, 120, 0.75, 2.0, 8.0, 2.0, 4, 0.8);

    public enum Attribute {
        HEALTH, SPEED, FALL_DAMAGE_REDUCTION, ATTACK_DAMAGE, ATTACK_SPEED,
        AIR_TIME, KIDNEY_EFFICIENCY, LIVER_EFFICIENCY, RIBS_ARMOR, GENERAL_ARMOR,
        STOMACH_HUNGER, STOMACH_SAT;
    }

    private final Map<Attribute, Object> attributes = new EnumMap<>(Attribute.class);

    CSOrganTiers(double health, double speed, double pFallDamageReduction, double attackDamage,
                 double attackSpeed, int pAirTime, double pKidneyEfficiency, double pLiverEfficiency,
                 double pRibsArmor, double pGeneralArmor, int pStomachHunger, double pStomachSat) {
        attributes.put(Attribute.HEALTH, health);
        attributes.put(Attribute.SPEED, speed);
        attributes.put(Attribute.FALL_DAMAGE_REDUCTION, pFallDamageReduction);
        attributes.put(Attribute.ATTACK_DAMAGE, attackDamage);
        attributes.put(Attribute.ATTACK_SPEED, attackSpeed);
        attributes.put(Attribute.AIR_TIME, pAirTime);
        attributes.put(Attribute.KIDNEY_EFFICIENCY, pKidneyEfficiency);
        attributes.put(Attribute.LIVER_EFFICIENCY, pLiverEfficiency);
        attributes.put(Attribute.RIBS_ARMOR, pRibsArmor);
        attributes.put(Attribute.GENERAL_ARMOR, pGeneralArmor);
        attributes.put(Attribute.STOMACH_HUNGER, pStomachHunger);
        attributes.put(Attribute.STOMACH_SAT, pStomachSat);
    }

    public Object getAttribute(Attribute attribute) {
        return attributes.get(attribute);
    }

    public boolean isAboveOrEqual(CSOrganTiers altTier) {
        return this.compareTo(altTier) >= 0;
    }

    public double getHealth() {
        return (double) attributes.get(Attribute.HEALTH);
    }

    public double getSpeed() {
        return (double) attributes.get(Attribute.SPEED);
    }

    public double getFallDamageReduction() {
        return (double) attributes.get(Attribute.FALL_DAMAGE_REDUCTION);
    }

    public double getAttackDamage() {
        return (double) attributes.get(Attribute.ATTACK_DAMAGE);
    }

    public double getAttackSpeed() {
        return (double) attributes.get(Attribute.ATTACK_SPEED);
    }

    public int getAirTime() {
        return (int) attributes.get(Attribute.AIR_TIME);
    }

    public double getKidneyEfficiency() {
        return (double) attributes.get(Attribute.KIDNEY_EFFICIENCY);
    }

    public double getLiverEfficiency() {
        return (double) attributes.get(Attribute.LIVER_EFFICIENCY);
    }

    public double getRibsArmor() {
        return (double) attributes.get(Attribute.RIBS_ARMOR);
    }

    public double getGeneralArmor() {
        return (double) attributes.get(Attribute.GENERAL_ARMOR);
    }

    public int getStomachHunger() {
        return (int) attributes.get(Attribute.STOMACH_HUNGER);
    }

    public double getStomachSat() {
        return (double) attributes.get(Attribute.STOMACH_SAT);
    }
}
