package com.flashpay.orderapi.global.config;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 현재 시각을 Clock 빈으로 주입받아 쓴다.
 * 테스트에서 Clock.fixed(...)로 바꿔 끼우면 판매 시작 시각 같은 로직을 시간에 상관없이 검증할 수 있다.
 */
@Configuration
public class ClockConfig {

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("Asia/Seoul"));
    }
}
