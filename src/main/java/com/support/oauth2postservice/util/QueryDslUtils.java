package com.support.oauth2postservice.util;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryDslUtils {

    public static BooleanBuilder nullSafeBuilder(Supplier<? extends BooleanExpression> supplier) {
        try {
            return new BooleanBuilder(supplier.get());
        } catch (IllegalArgumentException | NullPointerException e) {
            return new BooleanBuilder();
        }
    }
}
