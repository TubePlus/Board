package com.tubeplus.board_service.adapter.rdb.common;

import com.tubeplus.board_service.global.base.BaseEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Objects;


@Converter(autoApply = true)
public abstract class AbstractBaseEnumConverter<E extends Enum<E> & BaseEnum<T, K>, T, K> implements AttributeConverter<E, T> {

	private final Class<E> clazz;

    protected AbstractBaseEnumConverter(Class<E> clazz) {
        this.clazz = clazz;
    }

    @Override
    public final T convertToDatabaseColumn(E attribute) {
        return attribute.getCode();
    }

    @Override
    public final E convertToEntityAttribute(T dbData) {
        if (Objects.isNull(dbData)) {
            return null;
        }
        return Arrays.stream(clazz.getEnumConstants())
                .filter(e -> e.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown code: " + dbData));
    }

}