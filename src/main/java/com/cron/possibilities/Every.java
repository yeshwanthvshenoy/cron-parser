package com.cron.possibilities;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Every extends Base {

    public Every(com.cron.parser.Base segment) {
        super(segment);
    }

    @Override
    public List<Integer> generatePossibilities() {
        String[] steps = this.segment.getExpression().split("/");

        // Validate given every expression
        if (steps.length != 2) {
            throw new RuntimeException("Every does not have valid expression : " + this.segment.getExpression());
        }

        // Means every of minimum value
        if (steps[0].equals("*")) {
            steps[0] = this.segment.getMinimum().toString();
        }

        Integer[] stepSegments;
        try {
            // Validate if input is integer
            stepSegments = new Integer[]{Integer.valueOf(steps[0]), Integer.valueOf(steps[1])};
        } catch (Exception e) {
            throw new RuntimeException("Every does not have valid expression : " + this.segment.getExpression());
        }

        if (stepSegments[1] > this.segment.getMaximum()) {
            throw new RuntimeException("Every size is more than maximum value");
        }

        if (stepSegments[0] > this.segment.getMaximum()) {
            throw new RuntimeException("Every start is more than maximum value");
        }

        // Generate every x count from min to max
        return IntStream.iterate(stepSegments[0], n -> n + stepSegments[1])
                .limit((this.segment.getMaximum() - stepSegments[0])/ stepSegments[1] + 1).
                boxed().collect(Collectors.toList());
    }

}