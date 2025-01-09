package ru.marinalyamina.vetclinic.models.enums;

import java.util.EnumMap;
import java.util.Map;

public enum AnimalGender {
    Male,
    Female,
    Other;

    private static final Map<AnimalGender, String> genderMap = new EnumMap<>(AnimalGender.class);

    static {
        genderMap.put(AnimalGender.Male, "М");
        genderMap.put(AnimalGender.Female, "Ж");
        genderMap.put(AnimalGender.Other, "-");
    }

    public String getRussianString() {
        return genderMap.get(this);
    }

}

