package net.corespring.csaugmentations.Utility;

public interface IOrganTiers {
    Double getDoubleAttribute(CSOrganTiers.Attribute attribute);

    Integer getIntAttribute(CSOrganTiers.Attribute attribute);

    default boolean isAboveOrEqual(IOrganTiers otherTier) {
        return this.getTierLevel() >= otherTier.getTierLevel();
    }

    String getName();

    int getTierLevel();

    double getHealth();

    double getSpeed();

    double getFallDamageReduction();

    double getAttackDamage();

    double getAttackSpeed();

    int getAirTime();

    double getKidneyEfficiency();

    double getLiverEfficiency();

    double getRibsArmor();

    double getGeneralArmor();

    int getStomachHunger();

    double getStomachSat();
}
