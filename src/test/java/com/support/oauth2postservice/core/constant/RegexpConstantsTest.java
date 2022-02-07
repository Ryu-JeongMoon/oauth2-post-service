package com.support.oauth2postservice.core.constant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class RegexpConstantsTest {

    private static final Pattern pattern = Pattern.compile(RegexpConstants.EMAIL);

    @Test
    @DisplayName("이메일 검증 성공")
    void regexpEmailMatching() {
        Matcher rightMatcher = pattern.matcher("panda@gmail.com");
        assertThat(rightMatcher.matches()).isEqualTo(true);
    }

    @Test
    @DisplayName("이메일 검증 실패")
    void failRegexpEmailMatching() {
        Matcher wrongMatcher = pattern.matcher("panda");
        assertThat(wrongMatcher.matches()).isEqualTo(false);
    }

    @Test
    @DisplayName("이메일 검증 최소 길이 - id : 2, domain : 5")
    void lengthRegexpEmail() {
        Matcher shortMatcher = pattern.matcher("pa@g.com");
        assertThat(shortMatcher.matches()).isEqualTo(true);
    }

    @Test
    @DisplayName("이메일 검증 길이 제한 초과")
    void exceedMaxLength() {
        Matcher longMatcher = pattern.matcher(
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapandapanda" +
                "pandapandapandapandapandapandapandapandapandapandapanda@gmail.com");

        assertThat(longMatcher.matches()).isEqualTo(false);
    }
}