package com.tubeplus.board_service.adapter.rdb.common;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {


    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDateTime;


    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public LocalDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

}
