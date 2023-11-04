package com.cron.possibilities;

import java.util.stream.Collectors;

public class List extends Base {

    public List(com.cron.parser.Base segment) {
        super(segment);
    }

    @Override
    public java.util.List<Integer> generatePossibilities() {
        String[] lists = this.segment.getExpression().split(",");

        if (lists.length != 2) {
            throw new RuntimeException("List does not have valid expression : " + this.segment.getExpression());
        }

        // Validate given input if it doesn't go below or above min/max respectively
        return java.util.List.of(lists).stream()
                .flatMap(l -> {
                    try {
                        return Base.get(new com.cron.parser.Base(l) {
                            @Override
                            public Integer getMinimum() {
                                return segment.getMinimum();
                            }

                            @Override
                            public Integer getMaximum() {
                                return segment.getMaximum();
                            }

                            @Override
                            public String getExpression() {
                                return l;
                            }
                        }).generatePossibilities().stream();
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }).distinct().sorted().collect(Collectors.toList());
    }

}