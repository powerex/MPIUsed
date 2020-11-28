package com.mathpar.Graphic2D.tangent.lexerParser;

/**
 * A token that is produced by fviz.lexerParser.Tokenizer and fed into fviz.lexerParser.Parser.parse
 * <p>
 * A token consists of a token identifier, a string that the token was
 * created from and the position in the input string that the token was found.
 * <p>
 * The token id must be one of a number of pre-defined values
 */
public class Token {
    /**
     * fviz.lexerParser.Token id for the epsilon terminal
     */
    public static final int EPSILON = 0;
    /**
     * fviz.lexerParser.Token id for plus or minus
     */
    public static final int PLUSMINUS = 1;
    /**
     * fviz.lexerParser.Token id for multiplication or division
     */
    public static final int MULTDIV = 2;
    /**
     * fviz.lexerParser.Token id for the exponentiation symbol
     */
    public static final int RAISED = 3;
    /**
     * fviz.lexerParser.Token id for function names
     */
    public static final int FUNCTION = 4;
    /**
     * fviz.lexerParser.Token id for opening brackets
     */
    public static final int OPEN_BRACKET = 5;
    /**
     * fviz.lexerParser.Token id for closing brackets
     */
    public static final int CLOSE_BRACKET = 6;
    /**
     * fviz.lexerParser.Token id for numbers
     */
    public static final int NUMBER = 7;
    /**
     * fviz.lexerParser.Token id for variable names
     */
    public static final int VARIABLE = 8;

    /**
     * the token identifier
     */
    public final int token;
    /**
     * the string that the token was created from
     */
    public final String sequence;
    /**
     * the position of the token in the input string
     */
    public final int pos;

    /**
     * Construct the token with its values
     *
     * @param token    the token identifier
     * @param sequence the string that the token was created from
     * @param pos      the position of the token in the input string
     */
    public Token(int token, String sequence, int pos) {
        super();
        this.token = token;
        this.sequence = sequence;
        this.pos = pos;
    }


}