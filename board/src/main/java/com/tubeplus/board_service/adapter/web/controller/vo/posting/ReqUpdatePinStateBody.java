package com.tubeplus.board_service.adapter.web.controller.vo.posting;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tubeplus.board_service.application.posting.port.in.PostingUseCase.ModifyPinStateInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Value
public class ReqUpdatePinStateBody {

    private final boolean pinned;

    @JsonCreator
    public ReqUpdatePinStateBody(@JsonProperty("pinned")
                                 @NotNull boolean pinned) {
        this.pinned = pinned;
    }

}
