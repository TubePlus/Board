package com.tubeplus.board_service.application.posting.port.out;

import com.tubeplus.board_service.application.posting.domain.comment.Comment;

public interface CommentEventPublishable {
    void publishCommented(Comment savedComment);
}
