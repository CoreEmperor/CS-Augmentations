package net.corespring.csaugmentations.Registry.Utility;

import net.corespring.csaugmentations.Augmentations.Base.Limbs.SimpleArm;
import net.corespring.csaugmentations.Augmentations.Base.Limbs.SimpleLeg;
import net.corespring.csaugmentations.Augmentations.Base.Organs.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CSOrganClasses {

    private final Map<Integer, Class<? extends SimpleOrgan>> validClasses = new ConcurrentHashMap<>();

    public CSOrganClasses() {
        validClasses.put(0, SimpleBrain.class);
        validClasses.put(1, SimpleEyes.class);
        validClasses.put(2, SimpleRibs.class);
        validClasses.put(3, SimpleSpine.class);
        validClasses.put(4, SimpleArm.class);
        validClasses.put(5, SimpleArm.class);
        validClasses.put(6, SimpleLeg.class);
        validClasses.put(7, SimpleLeg.class);
        validClasses.put(8, SimpleLungs.class);
        validClasses.put(9, SimpleKidney.class);
        validClasses.put(10, SimpleKidney.class);
        validClasses.put(11, SimpleLiver.class);
        validClasses.put(12, SimpleHeart.class);
        validClasses.put(13, SimpleStomach.class);
        validClasses.put(14, SimpleSkin.class);
        validClasses.put(15, SimplePowerSource.class);
    }

    public Class<? extends SimpleOrgan> getOrganClass(int slot) {
        return validClasses.get(slot);
    }
}
