/* Token for VivJson.
 *
 * License:
 * Copyright 2025 benesult
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.benesult.vivjson;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Token for VivJson.
 * For example,
 * <pre>
 *               GT  NUMBER  ASSIGN  PLUS  NUMBER
 *                |  |            |   |    |
 *     while  ( i >  3     )  { i = i +    1     }
 *     |      | |          |  | |   |            |
 * IDENTIFIER | IDENTIFIER |  | IDENTIFIER       |
 *            |            |  |                  |
 *        LEFT_PARENTHESIS |  LEFT_CURLY_BRACKET |
 *                         |                     |
 *           RIGHT_PARENTHESIS            RIGHT_CURLY_BRACKET
 * </pre>
 *
 * <p>Refer to:
 * <ul>
 *   <li> <a href="https://austinhenley.com/blog/teenytinycompiler1.html">Let's make a Teeny Tiny compiler</a>
 *   <li> <a href="https://craftinginterpreters.com/">Crafting Interpreters</a>
 * </ul>
 * Note that this code is made from scratch. The source code
 * of the above WEB sites is not used.
 *
 * <p>Environment: Java 9 or later
 *
 * <p>Last modified: 2025-03-30
 *
 * @author Fumiaki Motegi (motegi@benesult.com)
 */
class Token {
  /**
   * Token types.
   */
  static enum Type {
    // 4 arithmetic operators and so on
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    MODULO("%"),

    // Specific operator
    IN("in"),

    // Using various purpose
    DOT("."),
    LEFT_PARENTHESIS("("),
    RIGHT_PARENTHESIS(")"),
    LEFT_SQUARE_BRACKET("["),
    RIGHT_SQUARE_BRACKET("]"),
    LEFT_CURLY_BRACKET("{"),
    RIGHT_CURLY_BRACKET("}"),

    // Assignments (Substitutes)
    ASSIGN("="),
    SET(":"),
    RESULT(":="),
    PLUS_ASSIGN("+="),
    MINUS_ASSIGN("-="),
    MULTIPLY_ASSIGN("*="),
    DIVIDE_ASSIGN("/="),
    MODULO_ASSIGN("%="),

    // Conditions
    EQ("=="),
    NE("!="),
    LT("<"),
    LE("<="),
    GT(">"),
    GE(">="),

    // Logical operators
    OR("or"),
    AND("and"),
    NOT("not"),

    // Modifiers
    CLASS("class"),
    FUNCTION("function"),
    REFERENCE("reference"),

    // Statements
    BREAK("break"),
    CONTINUE("continue"),
    RETURN("return"),

    // Values
    NUMBER("number"),
    STRING("string"),
    NULL("null"),
    TRUE("true"),
    FALSE("false"),

    // Variable/Function name
    IDENTIFIER("identifier"),

    // Functions
    IF("if"),
    ELSEIF("elseif"),
    ELSE("else"),
    FOR("for"),
    REMOVE("remove"),
    INCLUDE("include"),
    IMPORT("import"),
    SUPER("super"),

    // Terminator
    NEW_LINE("new_line"),
    SEMICOLON(";"),
    COMMA(","),
    EOS("eos"),

    // Unexpected
    ERROR("error"),
    ;

    /** Text representation of Token type. */
    final String value;

    /**
     * Initializes enum of Token type.
     *
     * @param value text representation of Token type
     */
    Type(final String value) {
      this.value = value;
    }
  }

  /** Token types of Keyword. */
  private static final Type[] KEYWORD_TYPES = {
    Type.IN, Type.OR, Type.AND, Type.NOT,
    Type.CLASS, Type.FUNCTION, Type.REFERENCE,
    Type.BREAK, Type.CONTINUE, Type.RETURN,
    Type.NULL, Type.TRUE, Type.FALSE,
    Type.IF, Type.ELSEIF, Type.ELSE,
    Type.FOR, Type.REMOVE,
    Type.INCLUDE, Type.IMPORT, Type.SUPER
  };

  /** Keywords table that is generated from KEYWORD_TYPES. */
  static final Map<String, Type> keywords = new HashMap<>();

  /**
   * Token's type that is initialize in constructor.
   * However it may be changed by {@link #fixKeywordType()}.
   */
  Type type;
  /** The token's actual text. */
  final String lexeme;
  /** The name of object where the token is existed. May be {@code null}. */
  @Nullable final String medium;
  /** The line number where the token is existed. May be {@code null}. */
  @Nullable final Integer lineNumber;
  /** The column number where the token is existed. May be {@code null}. */
  @Nullable final Integer columnNumber;

  /**
   * Makes token.
   *
   * @param type the type of token
   */
  Token(Type type) {
    this(type, null, null, null, null);
  }

  /**
   * Makes token.
   *
   * @param type the type of token
   * @param lexeme the token's actual text. It is so called "lexeme".
   *               When this is {@code null}, the assigned value of Token
   *               type is used.
   */
  Token(Type type, @Nullable String lexeme) {
    this(type, null, null, null, lexeme);
  }

  /**
   * Makes token.
   *
   * @param type the type of token
   * @param medium the name of object where the token is existed.
   *               For example, "test.viv", "1st argument".
   *               May be {@code null}, in which case a token is created by
   *               Lexer, Parser, or Evaluator.
   * @param lineNumber the line number where the token is existed.
   *                   May be {@code null}, in which case a token is created
   *                   by Lexer, Parser, or Evaluator.
   * @param columnNumber the column number where the token is existed.
   *                     May be {@code null}, in which case a token is
   *                     created by Lexer, Parser, or Evaluator.
   * @param lexeme the token's actual text. It is so called "lexeme".
   *               When this is {@code null}, the assigned value of Token
   *               type is used.
   */
  Token(Type type, @Nullable String medium,
        @Nullable Integer lineNumber, @Nullable Integer columnNumber,
        @Nullable String lexeme) {
    this.type = type;
    this.medium = medium;
    this.lineNumber = lineNumber;
    this.columnNumber = columnNumber;
    this.lexeme = lexeme != null ? lexeme : type.value;

    for (Type keywordType : KEYWORD_TYPES) {
      keywords.put(keywordType.value, keywordType);
    }
  }

  /**
   * Fixes type if this is Keyword.
   *
   * @return true if it is fixed, false otherwise.
   */
  boolean fixKeywordType() {
    @SuppressWarnings("null")
    Type type = keywords.get(lexeme);
    boolean isFixed = type != null;
    if (isFixed) {
      this.type = type;
    }
    return isFixed;
  }

  /**
   * Gets as string without type name.
   *
   * @return type name and lexeme
   */
  @Override
  public String toString() {
    return toString(false);
  }

  /**
   * Gets as string.
   *
   * @param omitTypeName type name is omitted when it is true.
   * @return type name and lexeme
   */
  String toString(boolean omitTypeName) {
    StringBuilder sb = new StringBuilder();

    if (!omitTypeName) {
      sb.append(type.name());
    }
    sb.append(omitTypeName ? "\"" : " (");
    sb.append(lexeme.equals("\n") ? "LF" : lexeme);
    sb.append(omitTypeName ? "\"" : ")");

    appendLocation(sb, medium, lineNumber, columnNumber, true);
    return sb.toString();
  }

  /**
   * Gets a location of token.
   * Mostly, it contains line number and column number.
   *
   * @return a location of token
   */
  String getLocation() {
    return getLocation(medium, lineNumber, columnNumber);
  }

  /**
   * Gets a location of token.
   * Mostly, it contains line number and column number.
   *
   * @param medium the name of object where the token is existed.
   *               May be {@code null}.
   * @param lineNumber the line number where the token is existed.
   *                   May be {@code null}.
   * @param columnNumber the column number where the token is existed.
   *                     May be {@code null}.
   * @return a location of token
   */
  static String getLocation(@Nullable String medium,
                            @Nullable Integer lineNumber,
                            @Nullable Integer columnNumber) {
    StringBuilder sb = new StringBuilder();
    appendLocation(sb, medium, lineNumber, columnNumber, false);
    return sb.toString();
  }

  /**
   * Appends the location into StringBuilder.
   *
   * @param sb StringBuilder instance.
   * @param medium the name of object where the token is existed.
   *               May be {@code null}.
   * @param lineNumber the line number where the token is existed.
   *                   May be {@code null}.
   * @param columnNumber the column number where the token is existed.
   *                     May be {@code null}.
   * @param isAdditionalIn selection of " in ".
   *                       " in " is appended if this is true.
   */
  private static void appendLocation(StringBuilder sb,
                                     @Nullable String medium,
                                     @Nullable Integer lineNumber,
                                     @Nullable Integer columnNumber,
                                     boolean isAdditionalIn) {
    if (medium == null && lineNumber == null && columnNumber == null) {
      return;
    }

    sb.append(isAdditionalIn ? " in (" : "(");

    if (medium != null) {
      sb.append(medium);
    }

    if (lineNumber != null) {
      if (medium != null) {
        sb.append(", ");
      }
      sb.append("line: ");
      sb.append(lineNumber);
    }

    if (columnNumber != null) {
      if (medium != null || lineNumber != null) {
        sb.append(", ");
      }
      sb.append("column: ");
      sb.append(columnNumber);
    }

    sb.append(")");
  }
}
