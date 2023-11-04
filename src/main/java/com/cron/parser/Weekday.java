package com.cron.parser;

public class Weekday extends Base {

    public Weekday(String expression) {
        super(expression);
        this.minimum = 0;
        this.maximum = 6;
    }

}