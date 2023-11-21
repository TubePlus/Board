package com.tubeplus.board_service.application.posting.port.out;

public interface CommentEventPublishable {
    void publishCommented(Long communityId);
}
