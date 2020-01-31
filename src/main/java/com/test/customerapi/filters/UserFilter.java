package com.test.customerapi.filters;

public class UserFilter {
    public enum OrderTypeEnum {
        ASC, DESC
    }
    private boolean activeOnly;
    private String city;
    private String orderBy;
    private OrderTypeEnum orderType = OrderTypeEnum.ASC;

    public boolean hasFilters() {
        return activeOnly || city != null;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isActiveOnly() {
        return activeOnly;
    }

    public void setActiveOnly(boolean activeOnly) {
        this.activeOnly = activeOnly;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public OrderTypeEnum getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderTypeEnum orderType) {
        this.orderType = orderType;
    }
    
}