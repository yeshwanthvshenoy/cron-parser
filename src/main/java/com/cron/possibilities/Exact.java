package com.cron.possibilities;

import java.util.List;

public class Exact extends Base {

    public Exact(com.cron.parser.Base segment) {
        super(segment);
    }

    @Override
    public List<Integer> generatePossibilities() {
        Integer value = Integer.valueOf(this.segment.getExpression());

        // Validate if exact is below or above min/max respectively
        if (value > this.segment.getMaximum()) {
            throw new RuntimeException("The value is more than the maximum allowed");
        }

        if (value < this.segment.getMinimum()) {
            throw new RuntimeException("The value is less than the minimum allowed");
        }

        return List.of(value);
    }

}