package com.api.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoryJSON implements Serializable {

    @Schema(example = "r4nD0mK3yf0R5tory")
    private String key;

    @Schema(example = "Authentication flow")
    private String name;

    @Schema(example = "MERGED")
    private String status;

    @Schema(example = "Razvan Prichici")
    private String assignee;
}
