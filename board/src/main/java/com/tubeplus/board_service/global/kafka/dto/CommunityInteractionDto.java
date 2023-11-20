package com.tubeplus.board_service.global.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommunityInteractionDto {
    private Long communityId;
    private Long point;
    private InteractionType interactionType;
}
