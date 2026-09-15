package io.github.kathukyabrian.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthToken {
    private LocalDateTime nextRefreshTime;
    private String accessToken;
}
