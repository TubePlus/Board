package com.tubeplus.board_service.adapter.rdb.posting;

import com.tubeplus.board_service.adapter.rdb.common.AbstractBaseEnumConverter;
import com.tubeplus.board_service.application.posting.domain.vote.VoteType;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
public class VoteTypeConverter
        extends AbstractBaseEnumConverter<VoteType, Integer, String> {

    public VoteTypeConverter() {
        super(VoteType.class);
    }
}

