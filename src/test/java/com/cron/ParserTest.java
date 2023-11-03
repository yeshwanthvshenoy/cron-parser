package com.cron;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testCronExpressionWithWrongArgument() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("There must be 6 parts in the expression");
        new Parser("").parseExpression();
    }

    @Test
    public void testCronExpressionWithWrongEvery() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("Every size is more than maximum value");
        new Parser("*/105 0 1,5 * 1-5 /usr/bin/find").parseExpression();
    }

    @Test
    public void testCronExpressionWithMaxEvery() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("Every start is more than maximum value");
        new Parser("105/10 0 1,5 * 1-5 /usr/bin/find").parseExpression();
    }

    @Test
    public void testCronExpressionWithInvalidInputEvery() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("Every does not have valid expression : abc/10");
        new Parser("abc/10 0 1,5 * 1-5 /usr/bin/find").parseExpression();
    }

    @Test
    public void testCronExpressionWithInvalidEvery() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("Every does not have valid expression : 10/10/10");
        new Parser("10/10/10 0 1,5 * 1-5 /usr/bin/find").parseExpression();
    }

    @Test
    public void testCronExpressionWithInvalidList() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("List does not have valid expression : ,");
        new Parser("*/15 0 , * 1-5 /usr/bin/find").parseExpression();
    }

    @Test
    public void testSimpleCronExpression() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        assertEquals(new Parser("*/15 0 1,5 * 1-5 /usr/bin/find").parseExpression(), "minute        0 15 30 45\n"
                + "hour          0\n"
                + "day of month  1 5\n"
                + "month         1 2 3 4 5 6 7 8 9 10 11 12\n"
                + "day of week   1 2 3 4 5\n"
                + "command       /usr/bin/find");
    }

    @Test
    public void testCronExpressionWithComplexLists() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        assertEquals(new Parser("*/15 0 1-5,1-15 * 1-5 /usr/bin/find").parseExpression(), "minute        0 15 30 45\n"
                + "hour          0\n"
                + "day of month  1 2 3 4 5 6 7 8 9 10 11 12 13 14 15\n"
                + "month         1 2 3 4 5 6 7 8 9 10 11 12\n"
                + "day of week   1 2 3 4 5\n"
                + "command       /usr/bin/find");
    }

    @Test
    public void testCronExpressionWithComplexListsWithProgramArguments() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        assertEquals(new Parser("*/15 0 1-5,1-15 * 1-5 /usr/bin/find hello .txt").parseExpression(), "minute        0 15 30 45\n"
                + "hour          0\n"
                + "day of month  1 2 3 4 5 6 7 8 9 10 11 12 13 14 15\n"
                + "month         1 2 3 4 5 6 7 8 9 10 11 12\n"
                + "day of week   1 2 3 4 5\n"
                + "command       /usr/bin/find hello .txt");
    }

    @Test
    public void testCronExpressionWithMaxRange() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("Range maximum is not valid. Given : 32 Max allowed : 31");
        new Parser("*/15 0 1-5,1-32 * 1-5 /usr/bin/find").parseExpression();
    }

    @Test
    public void testCronExpressionWithMinRange() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("Range minimum is not valid. Given : 0 Min allowed : 1");
        new Parser("*/15 0 1-5,1-15 0-2 1-5 /usr/bin/find").parseExpression();
    }

    @Test
    public void testCronExpressionWithMinRange2() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("Range minimum is not valid. Given : 13 Max allowed : 12");
        new Parser("*/15 0 1-5,1-15 13-14 1-5 /usr/bin/find").parseExpression();
    }

    @Test
    public void testCronExpressionWithWrongRange() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("Invalid expression : -1-15");
        new Parser("*/15 0 1-5,-1-15 * 1-5 /usr/bin/find").parseExpression();
    }

    @Test
    public void testCronExpressionWithMixedRange() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("Range minimum/maximum are in wrong order. maximum should be : 15 and minimum should be : 1");
        new Parser("*/15 0 1-5,15-1 * 1-5 /usr/bin/find").parseExpression();
    }

    @Test
    public void testCronExpressionWithMaxExact() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("The value is more than the maximum allowed");
        new Parser("*/15 0 1-5,32 * 1-5 /usr/bin/find").parseExpression();
    }

    @Test
    public void testCronExpressionWithMinExact() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("The value is less than the minimum allowed");
        new Parser("*/15 0 1-5,3 0 1-5 /usr/bin/find").parseExpression();
    }

    @Test
    public void testCronExpressionWithWrongMain() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("There should be only 1 argument: */15 0 1-5,32 * 1-5 /usr/bin/find;*/15 0 1-5,32 * 1-5 /usr/bin/find");
        Parser.main(new String[]{ "*/15 0 1-5,32 * 1-5 /usr/bin/find", "*/15 0 1-5,32 * 1-5 /usr/bin/find" });
    }

    @Test
    public void testCronExpressionWithMain() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Parser.main(new String[]{ "*/15 0 1-5,1-15 * 1-5 /usr/bin/find" });
    }

}