package com.daniel.rpc.common.protocol.transform;

import java.io.Serializable;

/**
 * 响应状态
 *
 * @author daniel
 */
public enum Status implements Serializable {

    SUCCESS(200, "success"), ERROR(500, "error"), NOT_FOUND(404, "not found"), OTHER(255, "not know");

    private final int code;

    private final String message;


    Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Status get(int code) {
        Status status;
        switch (code) {
            case 200:
                status = SUCCESS;
                break;
            case 500:
                status = ERROR;
                break;
            case 404:
                status = NOT_FOUND;
                break;
            default:
                status = OTHER;
        }
        return status;
    }


}
