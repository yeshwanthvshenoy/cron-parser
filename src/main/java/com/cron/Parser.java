package com.cron;


import com.cron.parser.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Parser {

    String expression;

    HashMap<String, List> expressionMap;

    String command;

    static List<String> displayOrder = List.of("minute", "hour", "day", "month", "weekday", "year");

    static Map<String, String> displayString = Map.of("day", "day of month", "weekday", "day of week");

    // Parameterized constructor to instantiate object and also start processing of given string input
    Parser(String expression) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.expression = expression;
        this.expressionMap = new HashMap<>();
        this.parseArguments();
    }

    // Entrypoint of execution
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Check if args is given in single line
        if(args.length != 1)
            throw new RuntimeException("There should be only 1 argument: " + String.join(";", args));
        // Let's do the job!
        System.out.println(new Parser(args[0]).parseExpression());
    }

    // Extract input from args and pre-process them
    private void parseArguments() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String[] parts = this.expression.split("\\s+");

        // Validation of cron expression
        if (parts.length < 6) {
            throw new RuntimeException("There must be 6 parts in the expression");
        }

        int i = 0;
        // Get the required data from the input string
        this.expressionMap.put("minute", new Minute(parts[i++]).parse());
        this.expressionMap.put("hour", new Hour(parts[i++]).parse());
        this.expressionMap.put("day", new Day(parts[i++]).parse());
        this.expressionMap.put("month", new Month(parts[i++]).parse());
        this.expressionMap.put("weekday", new Weekday(parts[i++]).parse());

        // Stitch back the command with its args
        this.command = parts[i] + extractCommandArguments(parts, i + 1);
    }

    private String extractCommandArguments(String[] parts, int startingIndex) {
        // Get all the args together in a space separated manner
        StringBuilder arguments = new StringBuilder();
        for (int i = startingIndex; i < parts.length; i++) {
            arguments.append(" ").append(parts[i]);
        }
        return arguments.toString();
    }

    public String parseExpression() {
        StringBuilder sb = new StringBuilder();
        for (String section : displayOrder) {
            // Get each part and process it
            if (this.expressionMap.get(section) == null) {
                continue;
            }
            // If display label is different then get that
            String displayString = this.displayString.getOrDefault(section, section);
            sb.append(String.format(displayString + " ".repeat(14 - displayString.length()) + "%s",
                    this.expressionMap.get(section).stream().map(i -> i.toString()).collect(Collectors.joining(" "))));
            sb.append(System.getProperty("line.separator"));
        }
        sb.append(String.format("command       %s", this.command));
        return sb.toString();
    }

}