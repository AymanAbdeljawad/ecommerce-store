package com.order_service.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfoDTO implements Serializable {
    private static long SerialVersionID = 1L;
    private Integer clientId;
    private String tracingId;
    private String errorCode;
    private String errorDesc;
    private String token;
    public InfoDTO(){}
    public InfoDTO(Integer clientId, String tracingId, String errorCode, String errorDesc, String token) {
        this.clientId = clientId;
        this.tracingId = tracingId;
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.token = token;
    }

    public InfoDTO(Integer clientId, String tracingId, String errorCode, String errorDesc) {
        this.clientId = clientId;
        this.tracingId = tracingId;
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }
}
