package io.github.medjed.jsonpathcompiler.expressions.filter;

import io.github.medjed.jsonpathcompiler.InvalidPathException;

public enum RelationalOperator {

    GTE(">="),
    LTE("<="),
    EQ("=="),

    /**
     * Type safe equals
     */
    TSEQ("==="),
    NE("!="),

    /**
     * Type safe not equals
     */
    TSNE("!=="),
    LT("<"),
    GT(">"),
    REGEX("=~"),
    NIN("NIN"),
    IN("IN"),
    CONTAINS("CONTAINS"),
    ALL("ALL"),
    SIZE("SIZE"),
    EXISTS("EXISTS"),
    TYPE("TYPE"),
    MATCHES("MATCHES"),
    EMPTY("EMPTY");

    private final String operatorString;

    RelationalOperator(String operatorString) {
        this.operatorString = operatorString;
    }

    public static RelationalOperator fromString(String operatorString){
        for (RelationalOperator operator : RelationalOperator.values()) {
            if(operator.operatorString.equals(operatorString.toUpperCase()) ){
                return operator;
            }
        }
        throw new InvalidPathException("Filter operator " + operatorString + " is not supported!");
    }

    @Override
    public String toString() {
        return operatorString;
    }
}
