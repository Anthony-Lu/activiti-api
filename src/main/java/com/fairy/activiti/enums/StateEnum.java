package com.fairy.activiti.enums;

/**
 * 请假单的状态
 *
 * @author luxuebing
 * @date 2018/02/10下午5:09:19
 */
public enum StateEnum {
    STRAT_ENTRY(0, "初始录入"),
    AUDIT_ING(1, "审核中"),
    AUDIT_COMPLETE(2, "审核完成");
    private int key;
    private String desc;

    StateEnum(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return desc;
    }

    public static StateEnum findByKey(int key) {
        for (StateEnum stateEnum : StateEnum.values()) {
            if (stateEnum.getKey() == key) {
                return stateEnum;
            }
        }
        return null;
    }
}
