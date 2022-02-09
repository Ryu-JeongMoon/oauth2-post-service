package com.support.oauth2postservice.service.member;

import com.support.oauth2postservice.domain.member.entity.Member;
import com.support.oauth2postservice.domain.member.repository.MemberRepository;
import com.support.oauth2postservice.service.member.dto.request.MemberEditRequest;
import com.support.oauth2postservice.service.member.dto.request.MemberSignupRequest;
import com.support.oauth2postservice.service.member.dto.response.MemberReadResponse;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public MemberReadResponse findActiveMember(Long memberId) {
        return memberRepository.findActive(memberId)
                .map(MemberReadResponse::from)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.MEMBER_NOT_FOUND));
    }

    @Transactional
    public void join(MemberSignupRequest memberSignupRequest) {
        Member member = memberSignupRequest.toEntity();
        member.encodePassword(passwordEncoder, memberSignupRequest.getPassword());
        memberRepository.save(member);
    }

    @Transactional
    public void edit(Long memberId, MemberEditRequest memberEditRequest) {
        if (memberEditRequest == null)
            return;

        Member member = memberRepository.findActive(memberId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.MEMBER_NOT_FOUND));

        member.encodePassword(passwordEncoder, memberEditRequest.getPassword());
        member.editInfo(memberEditRequest.getNickname(), memberEditRequest.getRole(), memberEditRequest.getStatus());
    }

    @Transactional
    public void leave(Long memberId) {
        memberRepository.findActive(memberId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.MEMBER_NOT_FOUND))
                .leave();
    }
}
