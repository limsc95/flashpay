package com.flashpay.orderapi.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flashpay.orderapi.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
