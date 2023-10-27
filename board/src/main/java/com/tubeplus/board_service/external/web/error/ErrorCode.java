package com.tubeplus.board_service.external.web.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    NOT_FOUND_RESOURCE(HttpStatus.NOT_FOUND, "C001", "해당 자원이 존재하지 않습니다."),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "C002", "이미 존재하는 데이터입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C003", "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C004", "Internal Server Error"),
    SAVE_ENTITY_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "C005", "db 테이블 엔티티 저장 실패"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "C006", "잘못된 요청입니다."),
    INVALID_TYPE_VALUE(HttpStatus.NOT_FOUND, "C007", " Invalid Type Value"),
    FIND_ENTITY_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "C008", "db 테이블 엔티티 조회 실패"),
    UPDATE_ENTITY_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "C009", "db 테이블 엔티티 업데이트 실패"),
    DELETE_ENTITY_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "C010", "db 테이블 엔티티 삭제 실패"),
    SOFT_DELETE_ENTITY_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "C011", "db 테이블 엔티티 soft delete 처리 실패"),

    /*로그인*/
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "L001", "로그인이 필요합니다."),
    FAIL_LOGIN(HttpStatus.BAD_REQUEST, "L002", "로그인 실패"),
    NEED_INTEGRATED_LOGIN(HttpStatus.NOT_FOUND, "L003", "통합ID 로그인이 필요합니다"),

    /*유저*/
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "U001", "존재하지 않는 유저입니다."),
    FIND_SELF(HttpStatus.BAD_REQUEST, "U002", "나에게는 선물할 수 없어요!"),

//    /*sms message*/
//    EXTERNAL_NCP_SERVER_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "F001", "인증번호 발송에 실패하였습니다"),
//    CERT_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "F002", "인증번호가 만료되었습니다"),
//    CERT_CODE_INVALID(HttpStatus.BAD_REQUEST, "F002", "인증번호가 유효하지 않습니다");
;

    private final HttpStatus status;
    private final String code;
    private final String message;


    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }


    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
