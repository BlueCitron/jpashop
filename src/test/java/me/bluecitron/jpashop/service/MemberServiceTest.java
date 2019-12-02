package me.bluecitron.jpashop.service;

import me.bluecitron.jpashop.domain.Member;
import me.bluecitron.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    @Rollback(false)
    void 회원가입() {
        // given
        Member member = new Member();
        member.setName("sds");

        // when
        Long join = memberService.join(member);

        // then
        Assertions.assertThat(member).isEqualTo(memberRepository.findOne(join));
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}