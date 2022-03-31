package com.support.oauth2postservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.util.Arrays;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SortUtils {

  public static List<Pair<String, Sort.Direction>> getPairs(String[] sorts, String[] orders) {
    String[] finalOrders = getOrdersIfShorterThanSorts(sorts, orders);
    return IntStream.range(0, sorts.length)
        .mapToObj(i -> {
          String order = finalOrders[i];
          Sort.Direction direction = Sort.Direction.DESC;
          if (StringUtils.isNotBlank(order) && StringUtils.equalsIgnoreCase(order, Sort.Direction.ASC.name()))
            direction = Sort.Direction.ASC;
          return Pair.of(sorts[i], direction);
        })
        .collect(Collectors.toList());
  }

  private static String[] getOrdersIfShorterThanSorts(String[] sorts, String[] orders) {
    if (ArrayUtils.getLength(sorts) > ArrayUtils.getLength(orders)) {
      int differenceOfLength = ArrayUtils.getLength(sorts) - ArrayUtils.getLength(orders);

      for (int i = 0; i < differenceOfLength; i++) {
        orders = Arrays.append(orders, "");
      }
    }

    return orders;
  }
}
