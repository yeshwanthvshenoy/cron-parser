package com.cron.possibilities;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Any extends Base {

    public Any(com.cron.parser.Base segment) {
        super(segment);
    }

    @Override
    public List<Integer> generatePossibilities() {
        // Generate all possible values between min and max
        return Arrays.stream(IntStream.range(this.segment.getMinimum(), this.segment.getMaximum() + 1).toArray()).
                boxed().collect(Collectors.toList());
    }

}