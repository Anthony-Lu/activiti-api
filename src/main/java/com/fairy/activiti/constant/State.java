package com.fairy.activiti.constant;

import java.util.stream.Stream;

/**
 * 请假单的状态
 *
 * @author luxuebing
 * @date 2018/02/10下午5:09:19
 */
public enum State {
    STRAT_ENTRY(0, "初始录入"),
    AUDIT_ING(1, "审核中"),
    AUDIT_COMPLETE(2, "审核完成");
    private int key;
    private String value;

    State(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static State findByKey(int key) {
        return Stream.of(State.values())
                .filter(e -> e.getKey() == key)
                .findFirst()
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        String.format("can not find State by key:[%s]", key)
                                ));
    }
}
