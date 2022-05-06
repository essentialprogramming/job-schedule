package com.api.output;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionJSON implements Serializable {

    private String status;
    private String error;
    private String message;
    private String timestamp;

}
