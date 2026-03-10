package com.simplesystem.todo.exception;

import java.time.LocalDateTime;

public record ErrorResponse(int status,
                            String code,
                            String message,
                            LocalDateTime timestamp) {

}
