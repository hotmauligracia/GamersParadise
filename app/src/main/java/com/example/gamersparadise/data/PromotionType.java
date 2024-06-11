package com.example.gamersparadise.data;

public class PromotionType {
    private int id;
    private String name;
    private String operator;

    public PromotionType(String name, String operator) {
        this.name = name;
        this.operator = operator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
