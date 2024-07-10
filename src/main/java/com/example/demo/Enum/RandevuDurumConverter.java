package com.example.demo.Enum;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RandevuDurumConverter implements AttributeConverter<RandevuDurum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(RandevuDurum randevuDurum) {
        if (randevuDurum == null) {
            return null;
        }
        return randevuDurum.getDurum();
    }

    @Override
    public RandevuDurum convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        return RandevuDurum.fromDurum(dbData);
    }
}