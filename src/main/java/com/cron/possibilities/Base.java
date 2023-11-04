package com.cron.possibilities;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public abstract class Base {

    protected com.cron.parser.Base segment;

    protected static final String ANY = "*";

    protected static final String LIST = ",";

    protected static final String STEP = "/";

    protected static final String RANGE = "-";

    protected static final String EXACT = "e";

    private static final Map<String, Class> expressionParserMap = Map.of(
            ANY, Any.class,
            LIST, List.class,
            STEP, Every.class,
            RANGE, Range.class,
            EXACT, Exact.class
    );

    public Base(com.cron.parser.Base segment) {
        this.segment = segment;
    }

    public static Base get(com.cron.parser.Base segment) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String parser = null;
        // Figure out what kind of expression is given in input
        if (segment.getExpression().equals(ANY)) parser = ANY;
        if (segment.getExpression().matches(".*,.*")) parser = LIST;
        if (segment.getExpression().matches("[0-9]+-[0-9]+")) parser = RANGE;
        if (segment.getExpression().matches(".*\\/.*")) parser = STEP;
        if (segment.getExpression().matches("^[0-9]+$")) parser = EXACT;

        if (parser == null) throw new RuntimeException("Invalid expression : " + segment.getExpression());

        // Instantiate particular parser
        return (Base) expressionParserMap
                .get(parser)
                .getDeclaredConstructor(com.cron.parser.Base.class).newInstance(segment);
    }

    public abstract java.util.List<Integer> generatePossibilities();

}