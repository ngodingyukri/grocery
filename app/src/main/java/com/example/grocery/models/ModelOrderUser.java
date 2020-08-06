package com.example.grocery.models;

public class ModelOrderUser {

    String orderId, orderTime, orderStatus, orderCost, orderBy, orderToo;

    public ModelOrderUser() {
    }

    public ModelOrderUser(String orderId, String orderTime, String orderStatus, String orderCost, String orderToo) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
        this.orderCost = orderCost;
        this.orderBy = orderBy;
        this.orderToo = orderToo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(String orderCost) {
        this.orderCost = orderCost;
    }

    public String getOrderBy() {
        return orderCost;
    }

    public void setOrderBy(String orderCost) {
        this.orderCost = orderCost;
    }

    public String getOrderToo() {
        return orderToo;
    }

    public void setOrderToo(String orderToo) {
        this.orderToo = orderToo;
    }
}
