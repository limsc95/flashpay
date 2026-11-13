package com.flashpay.orderapi.global.init;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.flashpay.orderapi.member.domain.Member;
import com.flashpay.orderapi.member.repository.MemberRepository;
import com.flashpay.orderapi.product.domain.Product;
import com.flashpay.orderapi.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 로컬 개발용 샘플 데이터. 상품이 하나도 없을 때만 넣는다.
 * flashpay.sample-data.enabled=false 로 끌 수 있고, 2주차에 Flyway를 도입하면 정리한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "flashpay.sample-data.enabled", havingValue = "true")
public class SampleDataInitializer implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final Clock clock;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (productRepository.count() > 0) {
            return;
        }
        LocalDateTime now = LocalDateTime.now(clock);

        memberRepository.save(Member.create("test@flashpay.com", "테스트회원"));
        productRepository.saveAll(List.of(
                Product.create("한정판 기계식 키보드", 129_000, 100, now.minusHours(1)),
                Product.create("선착순 무선 이어폰", 89_000, 50, now.plusDays(1)),
                Product.create("품절 테스트 상품", 10_000, 0, now.minusDays(1))
        ));
        log.info("샘플 데이터를 넣었습니다: 회원 1명, 상품 3개");
    }
}
