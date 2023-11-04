package com.cron.parser;

public class Minute extends Base {

    public Minute(String expression) {
        super(expression);
        this.minimum = 0;
        this.maximum = 59;
    }

}