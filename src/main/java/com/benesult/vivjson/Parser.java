/* Parser for VivJson.
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

import java.util.ArrayList;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Parser for VivJson.
 * <ul>
 *   <li> {@link #Parser(String, String, Config) Parser } :
 *                          Constructor. Its argument is source code
 *                          as string.
 *   <li> {@link #parse() Parser#parse } :
 *                          Parse source code and get statements.
 * </ul>
 * <pre>{@code
 *   <program> ::= [ "{" [ <nl>+ ] ]
 *       [ <statement> { <end> <statement> } [ <end> ] ]
 *       [ [ <nl>+ ] "}" ]
 *   <block> ::= "{" [ <nl>+ ]
 *       [ <statement> { <end> <statement> } [ <end> ] ]
 *       [ <nl>+ ] "}"
 *   <statement> ::= "break" | "continue" | <return> | <remove>
 *       | <expression> | <call_if> | <call_for>
 *       | <call_primitive> | <call_extended> | <declaration>
 *   <return> ::= "return" [ <nl>+ ] [ "(" [ <nl>+ ] <or> [ <nl>+ ] ")" ]
 *   <remove> ::= "remove" [ <nl>+ ] "(" [ <nl>+ ] <element> [ <nl>+ ] ")"
 *   <declaration> ::= <callee>
 *   <callee> ::= <modifier> [ <nl>+ ] <identifier> [ <nl>+ ]
 *       [ "(" [ <nl> ] [ <parameter> { <end> <parameter> } ] [ <nl> ] ")" ]
 *       [ <nl>+ ] <block>
 *   <call_extended> ::= <element> [ <nl>+ ]
 *       [ "(" [ <nl> ] [ <argument> { <end> <argument> } ] [ <nl> ]
 *       [ <block> [ <nl> ] ] ")" ] [ <nl>+ ] [ <block> ]
 *   <call_primitive> ::= <element> [ <nl>+ ]
 *       "(" [ <nl> ] [ <argument> { <end> <argument> } ] [ <nl> ] ")"
 *   <call_for> ::= "for" [ <nl>+ ] "(" [ <nl> ]
 *       ( [ <argument_for> ] [ <end> ] [ <or> ] [ <end> ]
 *         [ <argument_for> ] [ <end> ] [ <block> ] )
 *       [ <nl> ] ")" [ <nl>+ ] [ <block> ]
 *   <call_if> ::= "if" [ <nl>+ ] "(" [ <nl>+ ] <argument>
 *         [ <end> [ <nl>+ ] <block> ] [ <nl> ] ")" [ <nl>+ ] [ <block> ]
 *       { [ <nl>+ ] "elseif" [ <nl>+ ] "(" [ <nl>+ ] <argument>
 *         [ <end> [ <nl>+ ] <block> ] [ <nl> ] ")" [ <nl>+ ] [ <block> ] }
 *       [ [ <nl>+ ] "else" [ <nl>+ ] [ "("
 *         [ [ <nl>+ ] <block> ] [ <nl> ] ")" ] [ <nl>+ ] [ <block> ] ]
 *   <parameter> ::= [ <modifier> [ <nl>+ ] ] <identifier>
 *   <argument> ::= <or>
 *   <argument_for> ::= <assignment> | <call_primitive> | <call_extended>
 *   <expression> ::= <assignment> | <result>
 *   <result> ::= ":=" [ <nl>+ ] <or>
 *   <assignment> ::= <element> [ <nl>+ ]
 *       ( "=" | ":" | "+=" | "-=" | "*=" | "/=" | "%=" ) [ <nl>+ ] <or>
 *   <group> :: = "(" [ <nl>+ ] <or> [ <nl>+ ] ")"
 *   <or> ::= <and> { [ <nl>+ ] "or" [ <nl>+ ] <and> }
 *   <and> ::= <equality> { [ <nl>+ ] "and" [ <nl>+ ] <equality> }
 *   <equality> ::= <comparison>
 *       { [ <nl>+ ] ( "==" | "!=" | "in" ) [ <nl>+ ] <comparison> }
 *   <comparison> ::= <term>
 *       { [ <nl>+ ] ( "<" | "<=" | ">" | ">=" ) [ <nl>+ ] <term> }
 *   <term> ::= <factor> { [ <nl>+ ] ( "+" | "-" ) [ <nl>+ ] <factor> }
 *   <factor> ::= <thing>
 *       { [ <nl>+ ] ( "*" | "/" | "%" ) [ <nl>+ ] <thing> }
 *   <thing> ::= <unary> | <array> | <block>
 *   <unary> ::= [ "+" | "-" | "not" ] ( <primary> | <element>
 *       | <call_extended> | <call_if> | <call_for> )
 *   <element> ::=
 *       ( ( <identifier> | <call_primitive> )
 *         {
 *           ( [ <nl>+ ] "." [ <nl>+ ]
 *               ( <identifier> | <call_primitive> |
 *                 ( [ "+" | "-" ] <digit>+ ) ) )
 *           | ( "[" [ <nl>+ ] <term> [ <nl>+ ] "]" )
 *         }
 *       )
 *   <array> :: = "[" [ <nl> ] 
 *         [ <argument> { <end> <argument> } [ <end> ] ] [ <nl> ]
 *       "]"
 *   <primary> ::= <number> | <string> | <boolean> | "null" | <group>
 *   <modifier> ::= "function" | "reference"
 *   <identifier> ::= ( "_" | <alphabet> ) { "_" | <alphabet> | <digit> }
 *   <boolean> ::= "true" | "false"
 *   <number> ::= <digit>+ [ "." <digit>+ ]
 *       [ ( "e" | "E" ) ( "+" | "-" ) <digit>+ ]
 *   <digit> ::= "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
 *   <alphabet> ::= "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H"
 *       | "I" | "J" | "K" | "L" | "M" | "N" | "O" | "P" | "Q"
 *       | "R" | "S" | "T" | "U" | "V" | "W" | "X" | "Y" | "Z"
 *       | "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h"
 *       | "i" | "j" | "k" | "l" | "m" | "n" | "o" | "p" | "q"
 *       | "r" | "s" | "t" | "u" | "v" | "w" | "x" | "y" | "z"
 *   <end> ::= <nl> | ";" | ","
 *   <nl> ::= "\n"
 *   
 *   <string> is any characters as UTF-8. It is surrounded with "" or ''.
 *   
 *   []: 0 or 1 time (? is not used here. But it has same meaning.)
 *   {}: 0 or more times (* is not used here. But it has same meaning.)
 *   +: 1 or more times
 *   (): group
 *   |: or
 * }</pre>
 * "." is allowed as the right-hand sided operand of "in".
 *
 * <p>Refer to:
 * <ul>
 *   <li> <a href="https://austinhenley.com/blog/teenytinycompiler2.html">Let's make a Teeny Tiny compiler</a>
 *   <li> <a href="https://craftinginterpreters.com/">Crafting Interpreters</a>
 * </ul>
 * Note that this code is made from scratch. The source code
 * of the above WEB sites is not used.
 *
 * <p>Environment: Java 9 or later
 *
 * <p>Last modified: 2025-04-04
 *
 * @author Fumiaki Motegi (motegi@benesult.com)
 */
class Parser {
  /**
   * Configuration.
   * {@code null} if it is not needed.
   */
  @Nullable private final Config config;
  /**
   * Enabling only JSON.
   * When true is given, the given data is parsed as JSON.
   * In other words, script is disabled.
   */
  private final boolean isEnableOnlyJson;

  /** Lexer instance that is used to get each token. */
  private final Lexer lexer;

  /** Tokens of the current statement. */
  private final ArrayList<Token> tokens;
  /** The current index of the above array list "tokens". */
  private int index;
  /**
   * When the given source code is not JSON object (a.k.a. map, hash,
   * dictionary), this is true.
   * The default is false.
   */
  private boolean isImplicitAssign;

  /**
   * Makes Parser.
   *
   * @param sourceCode a source code as text
   * @param medium the name of object that has source code. It is used to
   *               report error. For example, "test.viv", "1st argument".
   *               May be {@code null}.
   * @param config configuration. {@code null} if it is not needed.
   */
  Parser(String sourceCode, @Nullable String medium,
         @Nullable Config config) {
    this.config = config;
    isEnableOnlyJson = Config.getEnableOnlyJson(config);
    this.lexer = new Lexer(sourceCode, medium, config);

    tokens = new ArrayList<>();
    index = 0;
    isImplicitAssign = false;
  }

  /**
   * Parses source code and get statements that construct it.
   * <ul>
   *   <li> Parse VivJson and JSON object (with/without bracket).
   *   <li> Parse directly represented value of JSON.
   * </ul>
   *
   * @return {@code <program>}
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  ArrayList<Statement> parse() throws LexException, ParseException {
    // Parse VivJson and JSON object (with/without bracket).
    @Nullable Block statements = makeProgram();
    if (statements instanceof Block) {
      return statements.values;
    }

    // Parse directly represented value of JSON.
    // It will be an assignment of implicit variable '_'
    return parseDirectlyRepresented();
  }

  /**
   * Parse directly represented value of JSON.
   *
   * <p>Target value is number, string, boolean, array, or null.
   *
   * <p>The given value is assigned into a implicit variable '_'.<br>
   * When some values are given, the value of implicit variable '_' is
   * represented as array.<br>
   * For example,
   * <ul>
   *   <li> 3 --> _ = 3
   *   <li> [2, 1]; 3 --> _ = [[2, 1], 3]
   * </ul>
   *
   * @return {@code <program>}
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private ArrayList<Statement> parseDirectlyRepresented() throws LexException, ParseException {
    isImplicitAssign = true;

    ArrayList<Statement> arguments = new ArrayList<>();
    while (getTokenType() != Token.Type.EOS) {
      while (skipEnd() != null) {  // Skip <end>+
        ;
      }
      if (getTokenType() == Token.Type.EOS) {
        break;
      }

      if (isEnableOnlyJson) {
        Statement argument = makeThing();
        if (argument != null) {
          arguments.add(argument);
          continue;
        }
      } else {
        Statement argument = makeArgument();
        if (argument != null) {
          if (argument instanceof Get) {
            ArrayList<Statement> members = ((Get) argument).members;
            if (members.size() == 1) {
              @Nullable Statement statement = members.get(0);
              if (statement instanceof Identifier) {
                Token identifier = ((Identifier) statement).name;
                if (!isInfinityOrNaN(identifier)) {
                  Token string = new Token(Token.Type.STRING,
                                          identifier.medium,
                                          identifier.lineNumber,
                                          identifier.columnNumber,
                                          identifier.lexeme);
                  arguments.add(new Literal(string));
                  continue;
                }
              }
            }
          }

          arguments.add(argument);
          continue;
        }
      }

      String tokenInfo = getToken().toString(true);
      abort(tokenInfo + " is invalid statement/value.");
    }
    if (arguments.size() == 0) {
      abort("No effective statement/value");
    }

    Identifier identifier =
                      new Identifier(new Token(Token.Type.IDENTIFIER, "_"));
    ArrayList<Statement> members = new ArrayList<>();
    members.add(identifier);
    @SuppressWarnings("null")
    Statement value = (arguments.size() > 1)
                      ? new Array(arguments) : arguments.get(0);
    ArrayList<Statement> result = new ArrayList<>();
    result.add(new Set(members, new Token(Token.Type.SET), value));
    return result;
  }

  /**
   * Makes {@code <program>}.
   * Parse {@code <program>} into {@code <statement>}.
   * <pre>{@code
   *   <program> ::= [ "{" [ <nl>+ ] ]
   *       [ <statement> { <end> <statement> } [ <end> ] ]
   *       [ [ <nl>+ ] "}" ]
   * }</pre>
   *
   * @return {@code <program>} as Block if it is obtained, {@code null}
   *         otherwise.
   *         Block is sub class of Statement.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Block makeProgram() throws LexException, ParseException {
    return makeBlock(false, true, Block.ANONYMOUS_FUNCTION);
  }

  /**
   * Makes {@code <block>}.
   * <pre>{@code
   *   <block> ::= "{" [ <nl>+ ]
   *       [ <statement> { <end> <statement> } [ <end> ] ]
   *       [ <nl>+ ] "}"
   * }</pre>
   *
   * @param isBracketEssential left/right curly brackets are essential
   *                           or not
   * @param isConsumed When this is True and a statement is
   *                   constructed, its token are consumed.
   * @param functionType function type of Block.
   *                     Block.ANONYMOUS_FUNCTION,
   *                     Block.PURE_FUNCTION, or
   *                     Block.LIMITED_FUNCTION .
   * @return {@code <block>} as Block if it is obtained, {@code null}
   *         otherwise.
   *         Block is sub class of Statement.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Block makeBlock(boolean isBracketEssential,
                                    boolean isConsumed,
                                    String functionType)
                                    throws LexException, ParseException {
    Token.Type endOfBlock = Token.Type.EOS;
    Token leftBracket = match(Token.Type.LEFT_CURLY_BRACKET);  // "{"
    if (leftBracket == null) {
      if (isBracketEssential) {
        return null;
      }
    } else {
      goToNextToken();
      skipNewLines();  // [ <nl>+ ]
      endOfBlock = Token.Type.RIGHT_CURLY_BRACKET;  // "}"
    }

    ArrayList<Statement> statements = new ArrayList<>();
    while (getTokenType() != endOfBlock) {
      int count = statements.size();

      Statement statement =
          isEnableOnlyJson ? makeAssignment() : makeStatement();
      if (statement != null) {
        statements.add(statement);
      }

      // <end>s are translated to blank lines.
      // <nl>+ in front of "}" are treated here. Because <end> includes <nl>.
      boolean isJustBehind = (statement != null);
      @Nullable Token token;
      while ((token = skipEnd()) != null) {
        if (!isJustBehind) {
          statements.add(new Blank(token));
        }
        isJustBehind = false;
      }

      // Abort when the effective statement is not found here.
      // However, when the effective statement is completely nothing,
      // we try to assign to implicit variable.
      if (count == statements.size()) {
        if (count == 0 && isConsumed && !isImplicitAssign) {
          return null;
        }

        String tokenInfo = getToken().toString(true);
        abort(tokenInfo + " is unexpected.");
      }

      if (isConsumed) {
        consumeToken();  // Consume Tokens of the above statement.
      }
    }

    if (endOfBlock == Token.Type.RIGHT_CURLY_BRACKET) {
      goToNextToken();
    }

    return new Block(statements, functionType);
  }

  /**
   * Makes {@code <statement>}.
   * <pre>{@code
   *   <statement> ::= "break" | "continue" | <return> | <remove>
   *       | <expression> | <call_if> | <call_for>
   *       | <call_primitive> | <call_extended> | <declaration>
   * }</pre>
   *
   * @return {@code <statement>} if it is obtained, {@code null}
   *         otherwise.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Statement makeStatement()
                                        throws LexException, ParseException {
    Token token = match(Token.Type.BREAK, Token.Type.CONTINUE);
    if (token != null) {
      goToNextToken();
      return new Keyword(token);
    }

    Statement statement = null;
    do {  // This is not loop. This is used for "break".
      statement = makeReturn();
      if (statement != null) {
        break;
      }
      statement = makeRemove();
      if (statement != null) {
        break;
      }
      statement = makeExpression();
      if (statement != null) {
        break;
      }
      statement = makeCallIf();
      if (statement != null) {
        break;
      }
      statement = makeCallFor();
      if (statement != null) {
        break;
      }
      statement = makeCallExtended();
      if (statement != null) {
        break;
      }
      statement = makeCallPrimitive();
      if (statement != null) {
        break;
      }
      statement = makeDeclaration();
    } while (false);
    return statement;
  }

  /**
   * Makes {@code <return>}.
   * <pre>{@code
   *   <return> ::=
   *       "return" [ <nl>+ ] [ "(" [ <nl>+ ] <or> [ <nl>+ ] ")" ]
   * }</pre>
   *
   * @return {@code <return>} as Return if it is obtained, {@code null}
   *         otherwise.
   *         Return is sub class of Statement.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Return makeReturn() throws LexException, ParseException {
    Token token = match(Token.Type.RETURN);
    if (token == null) {
      return null;
    }
    goToNextToken();

    int maybeRestoredIndex = index;  // #1

    skipNewLines();  // [ <nl>+ ]

    Token parenthesis = match(Token.Type.LEFT_PARENTHESIS);  // "("
    if (parenthesis == null) {
      index = maybeRestoredIndex;  // Restore #1
      return new Return(token, null);
    }
    goToNextToken();

    skipNewLines();  // [ <nl>+ ]

    Statement value = makeOr();
    if (value == null) {
      String tokenInfo = parenthesis.toString(true);
      abort("No returned value after " + tokenInfo);
    }

    skipNewLines();  // [ <nl>+ ]

    parenthesis = match(Token.Type.RIGHT_PARENTHESIS);  // ")"
    if (parenthesis == null) {
      String tokenInfo = getToken().toString(true);
      abort(tokenInfo + " is unexpected.");
    }
    goToNextToken();

    return new Return(token, value);
  }

  /**
   * Makes {@code <remove>}.
   * <pre>{@code
   *   <remove> ::=
   *       "remove" [ <nl>+ ] "(" [ <nl>+ ] <element> [ <nl>+ ] ")"
   * }</pre>
   *
   * @return {@code <remove>} as Remove if it is obtained, {@code null}
   *         otherwise.
   *         Remove is sub class of Statement.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Remove makeRemove() throws LexException, ParseException {
    Token token = match(Token.Type.REMOVE);
    if (token == null) {
      return null;
    }
    goToNextToken();

    skipNewLines();  // [ <nl>+ ]

    Token parenthesis = match(Token.Type.LEFT_PARENTHESIS);  // "("
    if (parenthesis == null) {
      String tokenInfo = getToken().toString(true);
      abort(tokenInfo + " is unexpected.");
    }
    goToNextToken();

    skipNewLines();  // [ <nl>+ ]

    Get element = makeElement(true);  // <element>
    if (element == null) {
      @SuppressWarnings("null")
      String tokenInfo = parenthesis.toString(true);
      abort("No variable after " + tokenInfo);
    }

    skipNewLines();  // [ <nl>+ ]

    parenthesis = match(Token.Type.RIGHT_PARENTHESIS);  // ")"
    if (parenthesis == null) {
      String tokenInfo = getToken().toString(true);
      abort(tokenInfo + " is unexpected.");
    }
    goToNextToken();

    @SuppressWarnings("null")
    ArrayList<Statement> members = element.members;
    return new Remove(token, members);
  }

  /**
   * Makes {@code <declaration>}.
   * <pre>{@code
   *   <declaration> ::= <callee>
   * }</pre>
   *
   * @return {@code <declaration>} as Callee if it is obtained,
   *         {@code null} otherwise.
   *         Callee is sub class of Statement.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Callee makeDeclaration()
                                        throws LexException, ParseException {
    return makeCallee();
  }

  /**
   * Makes {@code <callee>}.
   * <pre>{@code
   *   <callee> ::= <modifier> [ <nl>+ ] <identifier> [ <nl>+ ]
   *       [ "(" [ <nl> ] [ <parameter> { <end> <parameter> } ]
   *       [ <nl> ] ")" ] [ <nl>+ ] <block>
   * }</pre>
   *
   * @return {@code <callee>} as Callee if it is obtained, {@code null}
   *         otherwise.
   *         Callee is sub class of Statement.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Callee makeCallee() throws LexException, ParseException {
    int maybeRestoredIndex = index;  // #1

    // <modifier> [ <nl>+ ] <identifier>
    Parameter statement = makeParameter(true);
    if (statement == null) {
      return null;
    }

    skipNewLines();  // [ <nl>+ ]

    Array parameters = makeArray(Token.Type.LEFT_PARENTHESIS,
                                 Token.Type.RIGHT_PARENTHESIS,
                                 true);
    if (parameters == null) {
      parameters = new Array();
    }

    skipNewLines();  // [ <nl>+ ]

    Block block = makeBlock(true, false, Block.PURE_FUNCTION);
    if (block == null) {
      index = maybeRestoredIndex;  // Restore #1
      return null;
    }
    parameters.values.add(block);  // It is permitted though "final" variable.

    return new Callee(statement, parameters);
  }

  /**
   * Makes {@code <call_extended>}.
   *
   * @return {@code <call_extended>} as Call if it is obtained,
   *         {@code null} otherwise.
   *         Call is sub class of Statement.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Call makeCallExtended()
                                        throws LexException, ParseException {
    return makeCall(true, false, Block.ANONYMOUS_FUNCTION);
  }

  /**
   * Makes {@code <call_primitive>}.
   *
   * @return {@code <call_primitive>} as Call if it is obtained,
   *         {@code null} otherwise.
   *         Call is sub class of Statement.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Call makeCallPrimitive()
                                        throws LexException, ParseException {
    return makeCall(false, false, Block.ANONYMOUS_FUNCTION);
  }

  /**
   * Makes {@code <call_primitive>} or {@code <call_extended>}.
   * <pre>{@code
   *   <call_primitive> ::= <element> [ <nl>+ ]
   *       "(" [ <nl> ] [ <argument> { <end> <argument> } ]
   *           [ <nl> ] ")"
   *
   *   <call_extended> ::= <element> [ <nl>+ ]
   *       [ "(" [ <nl> ] [ <argument> { <end> <argument> } ]
   *             [ <nl> ] [ <block> [ <nl> ] ] ")" ]
   *       [ <nl>+ ] [ <block> ]
   * }</pre>
   *
   * @param isExtended selection of {@code <call_extended>}/
   *                   {@code <call_primitive>}.
   *                   true for {@code <call_extended>},
   *                   false for {@code <call_primitive>}.
   * @param isKeyword selection of token.
   *                  true if function name's token type is Keyword,
   *                  false otherwise.
   * @param functionType function type of Block.
   *                     Block.ANONYMOUS_FUNCTION,
   *                     Block.PURE_FUNCTION, or
   *                     Block.LIMITED_FUNCTION .
   * @return {@code <call_primitive>} as Call or
   *         {@code <call_extended>} as Call if it is obtained,
   *         {@code null} otherwise.
   *         Call is sub class of Statement.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Call makeCall(boolean isExtended, boolean isKeyword,
                                  String functionType)
                                  throws LexException, ParseException {
    int maybeRestoredIndex = index;  // #1
    Statement element = null;
    if (isKeyword) {
      element = new Identifier(getToken());
      goToNextToken();
    } else {
      element = makeElement(true);  // <element>
      if (element == null) {
        return null;
      }
    }

    skipNewLines();  // [ <nl>+ ]

    Array arguments = makeArray(Token.Type.LEFT_PARENTHESIS,
                                Token.Type.RIGHT_PARENTHESIS,
                                false);

    // <call_primitive>
    if (!isExtended) {
      if (arguments == null) {
        index = maybeRestoredIndex;  // Restore #1
        return null;
      }
      return new Call(element, arguments);
    }

    // <call_extended>
    skipNewLines();  // [ <nl>+ ]
    Block block = makeBlock(true, false, functionType);
    if (block == null) {
      do {  // This is not loop. This is used for "break".
        if (arguments == null) {
          break;
        }
        int length = arguments.values.size();
        if (length == 0) {
          break;
        }
        @Nullable Statement argument = arguments.values.get(length - 1);
        if (!(argument instanceof Block)) {
          break;
        }
        block = (Block) argument;                      // -+ Change
        block = new Block(block.values, functionType); //  | function
        arguments.values.set(length - 1, block);       // -+ type.
        return new Call(element, arguments);
      } while (false);
      index = maybeRestoredIndex;  // Restore #1
      return null;
    }
    if (arguments != null) {
      arguments.values.add(block);  // It is permitted though "final" variable.
    } else {
      ArrayList<Statement> statements = new ArrayList<>();
      statements.add(block);
      arguments = new Array(statements);
    }
    return new Call(element, arguments);
  }

  /**
   * Makes {@code <call_for>}.
   * <pre>{@code
   *   <call_for> ::= "for" [ <nl>+ ] "(" [ <nl> ]
   *       ( [ <argument_for> ] [ <end> ] [ <or> ] [ <end> ]
   *         [<argument_for> ] [ <end> ] [ <block> ] )
   *       [ <nl> ] ")" [ <nl>+ ] [ <block> ]
   * }</pre>
   *
   * @return {@code <call_for>} as Call if it is obtained, {@code null}
   *         otherwise.
   *         Call is sub class of Statement.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Call makeCallFor() throws LexException, ParseException {
    Token callFor = match(Token.Type.FOR);  // "for"
    if (callFor == null) {
      return null;
    }
    goToNextToken();

    skipNewLines();  // [ <nl>+ ]

    Token bracket = match(Token.Type.LEFT_PARENTHESIS);  // "("
    if (bracket == null) {
      String tokenInfo = callFor.toString(true);
      abort("There is no parenthesis after " + tokenInfo + ".");
    }
    goToNextToken();

    skipNewLines();  // [ <nl>+ ]

    ArrayList<Statement> arguments = new ArrayList<>();
    int flag = 0;

    for (int step = 0; step < 5; step++) {
      Statement statement = null;
      do {  // This is not loop. This is used for "break".
        statement = makeBlock(true, false, Block.ANONYMOUS_FUNCTION);
        if (statement != null) {
          break;
        }
        statement = makeArgumentFor();
        if (statement != null) {
          break;
        }
        statement = makeOr();
      } while (false);
      if (statement != null) {
        arguments.add(statement);
      }
      Token end = skipEnd();
      if (statement == null) {
        if (end != null) {
          arguments.add(new Blank(end));
        } else {
          bracket = match(Token.Type.RIGHT_PARENTHESIS);  // ")"
          if (bracket != null) {
            goToNextToken();
            arguments.add(new Keyword(bracket));
            flag |= 1;
          }
        }
      } else if (statement instanceof Block) {
        flag |= 2;
      }
      if (flag == 3) {
        break;
      }
    }

    int length = arguments.size();
    if (length < 2) {
      String tokenInfo = callFor.toString(true);
      abort((length < 1 || arguments.get(0) == null)
            ? ("There is no operation of " + tokenInfo + ".")
            : ("There is no right parenthesis of " + tokenInfo + "."));
    }

    @Nullable Statement block = arguments.remove(--length);
    @Nullable Statement parenthesis = arguments.remove(--length);
    if (block instanceof Keyword && parenthesis instanceof Block) {
      block = parenthesis;
    } else if (!(block instanceof Block)
               || !(parenthesis instanceof Keyword)) {
      String tokenInfo = callFor.toString(true);
      abort("Invalid argument in " + tokenInfo);
    }

    if (length == 0) {  // for () { operation }
      Blank blank = new Blank(new Token(Token.Type.SEMICOLON));
      arguments.add(blank);  // 1st argument
      arguments.add(new Literal(new Token(Token.Type.TRUE)));  // 2nd
      arguments.add(blank);  // 3rd
      arguments.add(blank);  // 4th
    } else if (length == 1) {
      @Nullable Statement statement = arguments.get(0);
      if (statement instanceof Binary
          && ((Binary) statement).operator.type == Token.Type.IN) {
        // for (variable in list) { operation }
        @Nullable Statement variable = ((Binary) statement).left;
        if (variable instanceof Get && ((Get) variable).members.size() == 1) {
          variable = ((Get) variable).members.get(0);
        }
        if (!(variable instanceof Identifier)) {
          String tokenInfo = callFor.toString(true);
          abort("Invalid argument in " + tokenInfo);
        }
        arguments.set(0, new Binary(variable,
                                    ((Binary) statement).operator,
                                    ((Binary) statement).right));  // 1st
        arguments.add(block);  // 2nd
      } else {  // for (initial) { operation }
        // 1st argument is kept as initial.
        arguments.add(new Literal(new Token(Token.Type.TRUE)));  // 2nd
        arguments.add(new Blank(new Token(Token.Type.SEMICOLON)));  // 3rd
        arguments.add(block);  // 4th
      }
    } else if (length == 2) {  // for (initial; condition) { operation }
      // 1st argument is kept as initial.
      @Nullable Statement statement = arguments.get(1);
      if (statement instanceof Blank) {  // 2nd
        arguments.set(1, new Literal(new Token(Token.Type.TRUE)));
      }
      arguments.add(new Blank(new Token(Token.Type.SEMICOLON)));  // 3rd
      arguments.add(block);  // 4th
    } else if (length == 3) { // for (initial; condition; update) { operation }
      // 1st argument is kept as initial.
      @Nullable Statement statement = arguments.get(1);
      if (statement instanceof Blank) {  // 2nd
        arguments.set(1, new Literal(new Token(Token.Type.TRUE)));
      }
      // 3rd argument is kept as update.
      arguments.add(block);  // 4th
    } else {
      String tokenInfo = callFor.toString(true);
      abort("Invalid argument in " + tokenInfo);
    }

    return new Call(new Identifier(callFor), new Array(arguments));
  }

  /**
   * Makes {@code <call_if>}.
   * <pre>{@code
   *   <call_if> ::= "if" [ <nl>+ ] "(" [ <nl>+ ] <argument>
   *                    [ <end> [ <nl>+ ] <block> ] [ <nl> ] ")"
   *       [ <nl>+ ][ <block> ]
   *   { [ <nl>+ ] "elseif" [ <nl>+ ] "(" [ <nl>+ ] <argument>
   *                    [ <end> [ <nl>+ ] <block> ] [ <nl> ] ")"
   *       [ <nl>+ ][ <block> ] }
   *   [ [ <nl>+ ] "else" [ <nl>+ ] [ "("
   *                    [ [ <nl>+ ] <block> ] [ <nl> ] ")" ]
   *       [ <nl>+ ] [ <block> ] ]
   * }</pre>
   *
   * @return {@code <call_if>} as Call if it is obtained, {@code null}
   *         otherwise.
   *         Call is sub class of Statement.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Call makeCallIf() throws LexException, ParseException {
    // if
    Token token = match(Token.Type.IF);
    if (token == null) {
      return null;
    }
    Statement[] statements = makeCallConditionAndOperations(Token.Type.IF);
    if (statements[0] == null || statements[1] == null) {
      return null;
    }
    ArrayList<Statement> arguments = new ArrayList<>();
    arguments.add(statements[0]);
    arguments.add(statements[1]);

    // elseif
    while (true) {
      skipNewLines();  // [ <nl>+ ]
      statements = makeCallConditionAndOperations(Token.Type.ELSEIF);
      if (statements[0] == null || statements[1] == null) {
        break;
      }
      arguments.add(statements[0]);
      arguments.add(statements[1]);
    }

    // else
    skipNewLines();  // [ <nl>+ ]
    statements = makeCallConditionAndOperations(Token.Type.ELSE);
    if (statements[1] != null) {
      arguments.add(new Literal(new Token(Token.Type.TRUE)));
      arguments.add(statements[1]);
    }

    return new Call(new Identifier(token), new Array(arguments));
  }

  /**
   * Makes [(condition)] {operations}.
   * <ul>
   *   <li> if/elseif (condition) {operations}
   *   <li> else {operations}
   * </ul>
   *
   * @param name function name as Token.Type.
   *             Token.Type.IF, Token.Type.ELSEIF, or Token.Type.ELSE .
   * @return an array that has 2 statements/{@code null}s.
   *         1st statement is condition.
   *         2nd statement is operations.
   *         {@code null} is returned if they are not obtained.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private Statement[] makeCallConditionAndOperations(Token.Type name)
                                          throws LexException, ParseException {
    Token token = match(name);
    if (token == null) {
      return new Statement[] {null, null};
    }

    int length = (name == Token.Type.ELSE) ? 1 : 2;

    Call call = makeCall(true, true, Block.LIMITED_FUNCTION);
    do {  // This is not loop. This is used for "break".
      if (call == null) {
        break;
      }
      if (!(call.name instanceof Identifier)) {
        break;
      }
      if (((Identifier) call.name).name.type != name) {
        break;
      }
      ArrayList<Statement> values = call.arguments.values;
      int argumentsLength = values.size();
      if (argumentsLength < length) {
        break;
      }
      @Nullable Statement statement = values.get(argumentsLength - 1);
      if (!(statement instanceof Block)) {
        break;
      }

      ArrayList<Statement> arguments = new ArrayList<>();
      for (Statement value : values) {
        if (value instanceof Blank) {
          continue;
        }

        if (arguments.size() < length) {
          arguments.add(value);
        } else {
          arguments.clear();  // It indicates error.
          break;
        }
      }

      if (arguments.size() != length) {
        break;
      }
      @Nullable Statement block = arguments.get(length - 1);
      if (!(block instanceof Block)) {
        break;
      }
      if (length == 1) {
        return new @Nullable Statement[] {null, arguments.get(0)};
      }
      return new @Nullable Statement[] {arguments.get(0), arguments.get(1)};
    } while (false);
    String tokenInfo = token.toString(true);
    abort(tokenInfo + " is invalid.");
    return new Statement[] {null, null}; // dummy for avoiding warning
  }

  /**
   * Makes {@code <parameter>}.
   * <pre>{@code
   *   <parameter> ::= [ <modifier> [ <nl>+ ] ] <identifier>
   *   <modifier> ::= "function" | "reference"
   * }</pre>
   *
   * @param isModifierEssential modifier is essential or not
   * @return {@code <parameter>} as Parameter if it is obtained,
   *         {@code null} otherwise.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Parameter makeParameter(boolean isModifierEssential)
                                        throws LexException, ParseException {
    int maybeRestoredIndex = index;  // #1
    Token modifier = match(// Token.Type.CLASS,
                           Token.Type.FUNCTION,
                           Token.Type.REFERENCE);
    if (modifier == null) {
      if (isModifierEssential) {
        return null;
      }
    } else {
      goToNextToken();
      skipNewLines();  // [ <nl>+ ]
    }

    Token identifier = match(Token.Type.IDENTIFIER);
    if (identifier == null) {
      if (modifier != null) {
        String tokenInfo = modifier.toString(true);
        abort("Unexpected statement after " + tokenInfo);
      }
      index = maybeRestoredIndex;  // Restore #1
      return null;
    }
    goToNextToken();

    return new Parameter(modifier, new Identifier(identifier));
  }

  /**
   * Makes {@code <argument>}.
   * <pre>{@code
   *   <argument> ::= <or>
   * }</pre>
   *
   * @return {@code <or>} if it is obtained, {@code null} otherwise.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Statement makeArgument()
                                        throws LexException, ParseException {
    return makeOr();
  }

  /**
   * Makes {@code <argument_for>}.
   * <pre>{@code
   *   <argument_for> ::= <assignment> | <call_primitive>
   *       | <call_extended>
   * }</pre>
   *
   * @return {@code <argument_for>} if it is obtained, {@code null}
   *         otherwise.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Statement makeArgumentFor()
                                        throws LexException, ParseException {
    Statement statement = makeAssignment();
    if (statement == null) {
      statement = makeCallPrimitive();
      if (statement == null) {
        statement = makeCallExtended();
      }
    }
    return statement;
  }

  /**
   * Makes {@code <expression>}.
   * <pre>{@code
   *   <expression> ::= <assignment> | <result>
   * }</pre>
   *
   * @return {@code <expression>} if it is obtained, {@code null}
   *         otherwise.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Statement makeExpression()
                                        throws LexException, ParseException {
    Statement statement = makeAssignment();
    if (statement == null) {
      statement = makeResult();
    }
    return statement;
  }

  /**
   * Makes {@code <assignment>}.
   * <pre>{@code
   *   <assignment> ::= <element> [ <nl>+ ]
   *       ( "=" | ":" | "+=" | "-=" | "*=" | "/=" | "%=" ) [ <nl>+ ]
   *       <or>
   * }</pre>
   *
   * @return {@code <assignment>} if it is obtained, {@code null}
   *         otherwise.
   *         Set is sub class of Statement.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Set makeAssignment() throws LexException, ParseException {
    int maybeRestoredIndex = index;  // #1
    ArrayList<Statement> element;
    do {  // This is not loop. This is used for "break".
      if (!isEnableOnlyJson) {
        Statement statement = makeElement(false);
        if (statement != null) {
          // makeElement method returns an instance of Get data class.
          // But this method makes an instance of Set data class.
          // Therefore, it is extracted.
          element = ((Get) statement).members;
          break;
        }
      }

      // Allow string that is surrounded with quotation marks,
      // such as {'key': 100}.
      Token token = isEnableOnlyJson
                    ? match(Token.Type.IDENTIFIER, Token.Type.STRING)
                    : match(Token.Type.STRING);
      if (token == null) {
        return null;
      }
      token.type = Token.Type.IDENTIFIER;
      element = new ArrayList<>();
      element.add(new Identifier(token));
      goToNextToken();
    } while (false);

    skipNewLines();  // [ <nl>+ ]

    Token operator = isEnableOnlyJson
                     ? match(Token.Type.SET)
                     : match(
                        Token.Type.ASSIGN, Token.Type.SET,
                        Token.Type.PLUS_ASSIGN, Token.Type.MINUS_ASSIGN,
                        Token.Type.MULTIPLY_ASSIGN, Token.Type.DIVIDE_ASSIGN,
                        Token.Type.MODULO_ASSIGN);
    if (operator == null) {
      index = maybeRestoredIndex;
      return null;
    }
    goToNextToken();

    skipNewLines();  // [ <nl>+ ]

    Statement argument = isEnableOnlyJson
                         ? makeThing()  // <thing>
                         : makeOr();  // <or>
    if (argument == null) {
      index = maybeRestoredIndex;
      return null;
    }

    return new Set(element, operator, argument);
  }

  /**
   * Makes {@code <result>}.
   * <pre>{@code
   *   <result> ::= ":=" [ <nl>+ ] <or>
   * }</pre>
   *
   * @return {@code <result>} as Set if it is obtained, {@code null}
   *         otherwise.
   *         Set is sub class of Statement.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Set makeResult() throws LexException, ParseException {
    int maybeRestoredIndex = index;  // #1
    Token operator = match(Token.Type.RESULT);  // ":="
    if (operator == null) {
      return null;
    }
    goToNextToken();

    skipNewLines();  // [ <nl>+ ]

    Statement argument = makeOr();  // <or>
    if (argument == null) {
      index = maybeRestoredIndex;
      return null;
    }

    return new Set(new Statement[] {}, operator, argument);
  }

  /**
   * Makes {@code <group>}.
   * <pre>{@code
   *   <group> :: = "(" [ <nl>+ ] <or> [ <nl>+ ] ")"
   * }</pre>
   *
   * @return {@code <or>} or {@code <group>} if it is obtained,
   *         {@code null} otherwise.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Statement makeGroup() throws LexException, ParseException {
    int maybeRestoredIndex = index;  // #1
    Token token = match(Token.Type.LEFT_PARENTHESIS);  // "("
    if (token == null) {
      return null;
    }
    goToNextToken();

    skipNewLines();  // [ <nl>+ ]

    Statement statement = makeOr();  // <or>
    if (statement == null) {
      String tokenInfo = token.toString(true);
      abort("Bad statement after " + tokenInfo);
    }

    skipNewLines();  // [ <nl>+ ]

    token = match(Token.Type.RIGHT_PARENTHESIS);  // ")"
    if (token == null) {
      index = maybeRestoredIndex;  // Restore #1
      return null;
    }
    goToNextToken();

    return statement;
  }

  /**
   * Makes {@code <or>}.
   * <pre>{@code
   *   <or> ::= <and> { [ <nl>+ ] "or" [ <nl>+ ] <and> }
   * }</pre>
   *
   * @return {@code <and>} or {@code <or>} if it is obtained,
   *         {@code null} otherwise.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Statement makeOr() throws LexException, ParseException {
    return makeBinary(Token.Type.OR);
  }

  /**
   * Makes {@code <and>}.
   * <pre>{@code
   *   <and> ::= <equality> { [ <nl>+ ] "and" [ <nl>+ ] <equality> }
   * }</pre>
   *
   * @return {@code <equality>} or {@code <and>} if it is obtained,
   *         {@code null} otherwise.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Statement makeAnd() throws LexException, ParseException {
    return makeBinary(Token.Type.AND);
  }

  /**
   * Makes {@code <equality>}.
   * <pre>{@code
   *   <equality> ::= <comparison>
   *       { [ <nl>+ ] ( "==" | "!=" | "in" ) [ <nl>+ ] <comparison> }
   * }</pre>
   *
   * @return {@code <comparison>} or {@code <equality>} if it is
   *         obtained, {@code null} otherwise.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Statement makeEquality()
                                        throws LexException, ParseException {
    return makeBinary(Token.Type.EQ, Token.Type.NE, Token.Type.IN);
  }

  /**
   * Makes {@code <comparison>}.
   * <pre>{@code
   *   <comparison> ::= <term>
   *       { [ <nl>+ ] ( "<" | "<=" | ">" | ">=" ) [ <nl>+ ] <term> }
   * }</pre>
   *
   * @return {@code <term>} or {@code <comparison>} as Binary if it is
   *         obtained, {@code null} otherwise.
   *         Binary is sub class of Statement.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Statement makeComparison()
                                        throws LexException, ParseException {
    return makeBinary(Token.Type.LT, Token.Type.LE,
                      Token.Type.GT, Token.Type.GE);
  }

  /**
   * Makes {@code <term>}.
   * <pre>{@code
   *   <term> ::=
   *       <factor> { [ <nl>+ ] ( "+" | "-" ) [ <nl>+ ] <factor> }
   * }</pre>
   *
   * @return {@code <factor>} or {@code <term>} as Binary if it is
   *         obtained, {@code null} otherwise.
   *         Binary is sub class of Statement.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Statement makeTerm() throws LexException, ParseException {
    return makeBinary(Token.Type.PLUS, Token.Type.MINUS);
  }

  /**
   * Makes {@code <factor>}.
   * <pre>{@code
   *   <factor> ::=
   *       <thing> { [ <nl>+ ] ( "*" | "/" | "%" ) [ <nl>+ ] <thing> }
   * }</pre>
   *
   * @return {@code <thing>} or {@code <factor>} as Binary if it is
   *         obtained, {@code null} otherwise.
   *         Binary is sub class of Statement.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Statement makeFactor()
                                        throws LexException, ParseException {
    return makeBinary(Token.Type.MULTIPLY,
                      Token.Type.DIVIDE, Token.Type.MODULO);
  }

  /**
   * Makes {@code <thing>}.
   * <pre>{@code
   *   <thing> ::= <unary> | <array> | <block>
   * }</pre>
   *
   * @return {@code <unary>} or {@code <array>} or {@code <block>} if
   *         it is obtained, {@code null} otherwise.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Statement makeThing() throws LexException, ParseException {
    Statement statement = makeUnary();
    if (statement == null) {
      statement = makeArray(Token.Type.LEFT_SQUARE_BRACKET,
                            Token.Type.RIGHT_SQUARE_BRACKET,
                            false);
      if (statement == null) {
        statement = makeBlock(true, false, Block.ANONYMOUS_FUNCTION);
      }
    }
    return statement;
  }

  /**
   * Makes binary.
   * <pre>{@code
   *   binary ::= <statement> 
   *       { [ <nl>+ ] <operator> [ <nl>+ ] <statement> }
   * }</pre>
   * left-hand side and right-hand side of {@code <statement>} are made
   * as below.
   * <pre>{@code
   *   | Token.Type               | method for <statement> |
   *   |--------------------------|------------------------|
   *   | OR                       | makeAnd                |
   *   | AND                      | makeEquality           |
   *   | EQ, NE, IN               | makeComparison         |
   *   | LT, LE, GT, GE           | makeTerm               |
   *   | PLUS, MINUS              | makeFactor             |
   *   | MULTIPLY, DIVIDE, MODULO | makeThing              |
   * }</pre>
   *
   * @param types tokens of candidate operator.
   *              There are some valid operators at calling here.
   * @return {@code <statement>} or Binary if it is obtained,
   *         {@code null} otherwise.
   *         Binary is sub class of Statement.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  @SuppressWarnings("null")
  private @Nullable Statement makeBinary(Token.Type... types)
                                        throws LexException, ParseException {
    int maybeRestoredIndex = index;  // #1
    Token.Type typeDelegate = types[0];
    boolean isLeft = true;
    Token operator = null;
    @Nullable Statement statement = null;
    while (true) {
      if (!isLeft) {
        maybeRestoredIndex = index;  // #2

        skipNewLines();  // [ <nl>+ ]

        operator = match(types);  // <operator>
        if (operator == null) {
          break;
        }
        goToNextToken();

        skipNewLines();  // [ <nl>+ ]
      }

      Statement operand = null;
      switch (typeDelegate) {
        case OR:
          operand = makeAnd();
          break;
        case AND:
          operand = makeEquality();
          break;
        case EQ:
          /* FALLTHROUGH */
        case NE:
          /* FALLTHROUGH */
        case IN:
          operand = makeComparison();
          break;
        case LT:
          /* FALLTHROUGH */
        case LE:
          /* FALLTHROUGH */
        case GT:
          /* FALLTHROUGH */
        case GE:
          operand = makeTerm();
          break;
        case PLUS:
          /* FALLTHROUGH */
        case MINUS:
          operand = makeFactor();
          break;
        case MULTIPLY:
          /* FALLTHROUGH */
        case DIVIDE:
          /* FALLTHROUGH */
        case MODULO:
          operand = makeThing();
          break;
        default:
          if (operator == null) {
            abort("Interpreter side error in Parser#makeBinary");
          }
          String tokenInfo = operator.toString(true);
          abort("Bad operator: " + tokenInfo);
          break;
      }
      if (operand == null) {
        if (isLeft) {
          return null;
        }
        if (operator != null && operator.type == Token.Type.IN) {
          Token dot = match(Token.Type.DOT);
          if (dot != null) {
            dot.type = Token.Type.IDENTIFIER;
            operand = new Identifier(dot);
            goToNextToken();
          }
        }
        if (operand == null) {
          String tokenInfo = operator.toString(true);
          abort("Bad statement after " + tokenInfo);
        }
      }

      if (isLeft) {
        statement = operand;
        isLeft = false;
        continue;
      }

      statement = new Binary(statement, operator, operand);
    }

    index = maybeRestoredIndex;  // Restore #1, #2
    return statement;
  }

  /**
   * Makes {@code <unary>}.
   * <pre>{@code
   *   <unary> ::= [ "+" | "-" | "not" ] ( <primary> | <element>
   *       | <call_extended> | <call_if> | <call_for> )
   * }</pre>
   *
   * @return {@code <primary>}, {@code <element>},
   *         {@code <call_extended>}, {@code <call_if>},
   *         {@code <call_for>}, or {@code <unary>} if it is
   *         obtained, {@code null} otherwise.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Statement makeUnary() throws LexException, ParseException {
    int maybeRestoredIndex = index;  // #1
    Token prefix = isEnableOnlyJson
                   ? match(Token.Type.PLUS, Token.Type.MINUS)
                   : match(Token.Type.PLUS, Token.Type.MINUS, Token.Type.NOT);
    if (prefix != null) {
      goToNextToken();
    }

    Statement statement = null;
    do {  // This is not loop. This is used for "break".
      if (!isEnableOnlyJson) {
        statement = makeCallFor();
        if (statement != null) {
          break;
        }
        statement = makeCallIf();
        if (statement != null) {
          break;
        }
        statement = makeCallExtended();
        if (statement != null) {
          break;
        }
        statement = makeElement(false);
        if (statement != null) {
          break;
        }
      }
      statement = makePrimary();
    } while (false);
    if (statement == null) {
      index = maybeRestoredIndex;  // Restore #1
      return null;
    }

    if (prefix == null || prefix.type == Token.Type.PLUS) {
      return statement;
    }
    if (prefix.type == Token.Type.MINUS) {
      return new Binary(new Literal(new Token(Token.Type.NUMBER,
                                              prefix.medium,
                                              prefix.lineNumber,
                                              prefix.columnNumber,
                                              "-1")),
                        new Token(Token.Type.MULTIPLY),
                        statement);
    }
    return new Binary(new Literal(new Token(Token.Type.NULL)), prefix, statement);
  }

  /**
   * Makes {@code <element>}.
   * <pre>{@code
   *   <element> ::=
   *   ( ( <identifier> | <call_primitive> )
   *     {
   *       ( [ <nl>+ ] "." [ <nl>+ ]
   *           ( <identifier> | <call_primitive> | <digit>+ ) )
   *       | ( "[" [ <nl>+ ] <unary> [ <nl>+ ] "]" )
   *     }
   *   )
   * }</pre>
   *
   * @param exceptCallPrimitive permission of {@code <call_primitive>}.
   *                            true if {@code <call_primitive>} is not
   *                            allowed.
   * @return {@code <element>} as Get if it is obtained, {@code null}
   *         otherwise.
   *         Get is sub class of Statement
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Get makeElement(boolean exceptCallPrimitive)
                                        throws LexException, ParseException {
    // <identifier> | <call_primitive>
    @Nullable Statement member = exceptCallPrimitive
                                 ? null 
                                 : makeCallPrimitive();  // <call_primitive>
    if (member == null) {
      Token token = match(Token.Type.IDENTIFIER);
      if (token == null) {
        return null;
      }
      member = new Identifier(token);  // <identifier>
      goToNextToken();
    }

    ArrayList<Statement> members = new ArrayList<>();
    members.add(member);

    int maybeRestoredIndex;
    while (true) {
      maybeRestoredIndex = index;  // #1

      // "[" [ <nl>+ ] <term> [ <nl>+ ] "]"
      member = makeMemberWithBracket();
      if (member != null) {
        members.add(member);
        continue;
      }

      // [ <nl>+ ] "." [ <nl>+ ]
      // ( <identifier> | <call_primitive> | ( [ "+" | "-" ] <digit>+ ) )
      Statement[] dotMembers = makeMemberWithDot();
      if (dotMembers[0] != null) {
        members.add(dotMembers[0]);
        if (dotMembers[1] != null) {
          members.add(dotMembers[1]);
        }
        continue;
      }

      index = maybeRestoredIndex;  // Restore #1
      break;
    }

    return new Get(members);
  }

  /**
   * Makes bracket member, such as *[a], *[3].
   * <pre>{@code
   *   "[" [ <nl>+ ] <term> [ <nl>+ ] "]"
   * }</pre>
   *
   * @return {@code <unary>} if it is obtained, {@code null} otherwise.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Statement makeMemberWithBracket()
                                        throws LexException, ParseException {
    Token bracket = match(Token.Type.LEFT_SQUARE_BRACKET);  // "["
    if (bracket == null) {
      return null;
    }
    goToNextToken();

    skipNewLines();  // [ <nl>+ ]

    Statement member = makeTerm();  // <term>
    if (member == null) {
      String tokenInfo = bracket.toString(true);
      abort("Unexpected statement after " + tokenInfo);
    }

    skipNewLines();  // [ <nl>+ ]

    bracket = match(Token.Type.RIGHT_SQUARE_BRACKET);  // "]"
    if (bracket == null) {
      String tokenInfo = getToken().toString(true);
      abort(tokenInfo + " is unexpected.");
    }
    goToNextToken();
    return member;
  }

  /**
   * Makes dot member, such as *.a, *.3, *.func().
   * <pre>{@code
   *   [ <nl>+ ] "." [ <nl>+ ]
   *   ( <identifier> | <call_primitive> |
   *       ( [ "+" | "-" ] <digit>+ )
   *   )
   * }</pre>
   *
   * <p>The returned value is a list that has 2 statements.
   * However 2nd statement is null mostly. 2nd statement becomes
   * valid statement if it seems that they are floating-point
   * number.<br>
   * There are some example in the following table. To tell the
   * truth, in the last item "f.0.2", 0.2 is entered in a Token
   * that is created by Lexer. Thus, this parsing is needed.
   * <pre>
   * | Target value                 | Expression | 1st | 2nd  |
   * |------------------------------|------------|-----|------|
   * | {"a": {"x": 3}}              | a.x        | "x" | null |
   * | {"b": [7, 8, 9]}             | b.0        |  0  | null |
   * | {"c": [10, [50, 70]]}        | c.1.0      |  1  |  0   |
   * | {"d": {"2": {"6": 100}}}     | d.2.6      |  2  |  6   |
   * | {"e": {"3": [1000, 2000]}}   | e.3.1      |  3  |  1   |
   * | {"f": [{"2": 10000}, 20000]} | f.0.2      |  0  |  2   |
   * </pre>
   *
   * @return an array that has 2 statements.
   *         Each element is {@code <identifier>},
   *         {@code <call_primitive>}, or {@code <digit>+} if it is
   *         obtained, {@code null} otherwise.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private Statement[] makeMemberWithDot() throws LexException, ParseException {
    skipNewLines();  // [ <nl>+ ]

    Token dot = match(Token.Type.DOT);
    if (dot == null) {
      return new Statement[] {null, null};
    }
    goToNextToken();

    skipNewLines();  // [ <nl>+ ]

    Statement[] members = new Statement[2];
    members[0] = makeCallPrimitive();  // <call_primitive>
    members[1] = null;  // This is the additional member.
    if (members[0] == null) {
      Token token = match(Token.Type.IDENTIFIER, Token.Type.STRING);
      if (token != null) {
        // Literal data class is used instead of Identifier data class.
        // It prevents this member from evaluation as variable.
        token.type = Token.Type.STRING;
        members[0] = new Literal(token);  // <identifier>
      } else {
        Token prefix = match(Token.Type.PLUS, Token.Type.MINUS);
        if (prefix != null) {
          goToNextToken();
        }
        token = match(Token.Type.NUMBER);
        if (token == null) {
          String tokenInfo = dot.toString(true);
          abort("Unexpected member after " + tokenInfo);
        } else {
          if (token.lexeme.indexOf('.') == -1) {
            members[0] = new Literal(token);  // <digit>+
          } else {
            int index = token.lexeme.indexOf(".");
            if (index == 0 || index >= token.lexeme.length() - 1) {
              index = -1;  // Because there is "." in the head/tail.
            } else {
              int next = token.lexeme.indexOf(".", index + 1);
              if (next >= 0) {
                index = -1;  // Because there are a lot of tokens.
              }
            }
            if (index < 0) {
              String tokenInfo = token.toString(true);
              abort("Unexpected member after " + tokenInfo);
            }
            int start = 0;
            int end = index;
            for (int i = 0; i < 2; i++) {
              String part = token.lexeme.substring(start, end);
              if (!isDigit(part)) {
                String tokenInfo = token.toString(true);
                abort("Unexpected member after " + tokenInfo);
              }
              members[i] = new Literal(new Token(Token.Type.NUMBER,
                                                 token.medium,
                                                 token.lineNumber,
                                                 token.columnNumber,
                                                 part));
              start = index + 1;
              end = token.lexeme.length();
            }
          }
        }
        if (prefix != null && prefix.type == Token.Type.MINUS) {
          members[0] = new Binary(
                          new Literal(new Token(Token.Type.NUMBER,
                                                prefix.medium,
                                                prefix.lineNumber,
                                                prefix.columnNumber,
                                                "-1")),
                          new Token(Token.Type.MULTIPLY),
                          members[0]);
        }
      }
      goToNextToken();
    }
    return members;
  }

  /**
   * Checks number's string.
   *
   * @param text a string that may be number.
   * @return true if the given text is number, false otherwise.
   */
  private boolean isDigit(String text) {
    for (int i = 0; i < text.length(); i++) {
      if (!Character.isDigit(text.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Make {@code <array>}.
   * <pre>{@code
   *   Normally, <array> :: = "[" [ <nl> ] [ <argument>
   *   { <end> <argument> } [ <end> ] ] [ <nl> ] "]" is made.
   *
   *   "(" and ")" can be used instead of "[" and "]" for caller.
   *   <parameter> can be used instead of <argument> for callee.
   * }</pre>
   *
   * @param leftBracket the left side of surrounding array
   * @param rightBracket the right side of surrounding array
   * @param isParameter selection of {@code <parameter>} /
   *                    {@code <argument>}.
   *                    true if {@code <parameter>} is used,
   *                    false otherwise.
   * @return {@code <array>} as Array if it is obtained,
   *         {@code null} otherwise.
   *         Array is sub class of Statement.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  @SuppressWarnings("null")
  private @Nullable Array makeArray(Token.Type leftBracket,
                                    Token.Type rightBracket,
                                    boolean isParameter)
                                    throws LexException, ParseException {
    Token bracket = match(leftBracket);
    if (bracket == null) {
      return null;
    }
    goToNextToken();

    skipNewLine();  // [ <nl> ]

    ArrayList<Statement> values = new ArrayList<>();
    while (getTokenType() != rightBracket) {
      Statement value = isParameter
                        ? makeParameter(false)
                        : makeArgument();
      if (value != null) {
        values.add(value);
      }

      Token end = skipEnd();
      if (value == null) {
        if (end == null) {
          // There is no right bracket, no value, no ";",
          // no ",", and no new line code. It is invalid.
          String tokenInfo = getToken().toString(true);
          abort(tokenInfo + " is unexpected.");
        }

        if (end.type == Token.Type.NEW_LINE) {
          // Blank line is ignored.
          continue;
        }

        /* There is no value though delimiter is existed.
         * However it is permitted for "for (;;)",
         * for (; i < 10; i += 1), and so on.
         * In other words, it is not permitted for parameter of
         * function definition and array.
         */
        if (isParameter) {  // Parameter of function definition
          String tokenInfo = end.toString(true);
          abort(tokenInfo + " is found without parameter.");
        }
        if (leftBracket == Token.Type.LEFT_SQUARE_BRACKET) {  // array
          String tokenInfo = end.toString(true);
          abort(tokenInfo + " is found without value.");
        }
      }
    }
    goToNextToken();

    return new Array(values);
  }

  /**
   * Make {@code <primary>}.
   * <pre>{@code
        <primary> ::= <number> | <string> | <boolean> | "null"
            | <group>
   * }</pre>
   *
   * @return {@code <primary>} if it is obtained, {@code null}
   *         otherwise.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if parsing is failed.
   */
  private @Nullable Statement makePrimary()
                                        throws LexException, ParseException {
    Token token = match(Token.Type.NUMBER, Token.Type.STRING,
                        Token.Type.TRUE, Token.Type.FALSE,
                        Token.Type.NULL);
    do {  // This is not loop. This is used for "break".
      if (token != null) {
        break;
      }

      token = match(Token.Type.IDENTIFIER);
      if (token == null) {
        break;
      }

      if (isInfinityOrNaN(token)) {
        goToNextToken();
        return new Identifier(token);
      }

      if (isImplicitAssign) {
        token.type = Token.Type.STRING;
        break;
      }
      token = null;
    } while (false);

    if (token != null) {
      goToNextToken();
      return new Literal(token);
    }
    if (isEnableOnlyJson) {
      return null;
    }

    return makeGroup();
  }

  /**
   * Detects whether the given token is Infinity/NaN or not.
   *
   * @param token Token that may be Infinity/NaN.
   * @return true if the given token is Infinity/NaN, false otherwise
   */
  private boolean isInfinityOrNaN(Token token) {
    String[] identifiers = new String[] {
      Config.getInfinity(config),
      Config.getNaN(config)
    };
    for (String identifier : identifiers) {
      if (identifier != null && identifier.equals(token.lexeme)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gets token if the given types have the current token type.
   *
   * @param types Token types.
   * @return Token if the current token type is existed in the given
   *         types, {@code null} otherwise.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if the out of range is happen.
   */
  private @Nullable Token match(Token.Type... types)
                                        throws LexException, ParseException {
    Token token = getToken();

    for (Token.Type type : types) {
      if (token.type == type) {
        return token;
      }
    }
    return null;
  }

  /*
  private @Nullable Token match(Token.Type[] types)
                                        throws LexException, ParseException {
    Token token = getToken();

    for (Token.Type type : types) {
      if (token.type == type) {
        return token;
      }
    }
    return null;
  }
  */

  /**
   * Gets token if the given type and current token type is same.
   *
   * @param type the expected Token type.
   * @return Token if the current token type is same, {@code null} otherwise.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if the out of range is happen.
   */
  private @Nullable Token match(Token.Type type)
                                        throws LexException, ParseException {
    Token token = getToken();

    if (token.type == type) {
      return token;
    }
    return null;
  }

  /**
   * Processes a next token.
   *
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if the out of range is happen.
   */
  private void goToNextToken() throws LexException, ParseException {
    index++;
    getToken();
  }

  /**
   * Get current Token type.
   *
   * @return the current Token type
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if the out of range is happen.
   */
  private Token.Type getTokenType() throws LexException, ParseException {
    Token token = getToken();
    return token.type;
  }

  /**
   * Gets current Token.
   *
   * @return the current Token
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if the out of range is happen.
   */
  private Token getToken() throws LexException, ParseException {
    do {  // This is not loop. This is used for "break".
      if (index > tokens.size()) {
        break;
      }
      if (index == tokens.size()) {
        tokens.add(lexer.getToken());
      }
      try {
        @Nullable Token token = tokens.get(index);
        if (token != null) {
          return token;
        }
      } catch (IndexOutOfBoundsException ignored) {
        ;
      }
    } while (false);
    abort("Interpreter side error in Parser#getToken");
    return new Token(Token.Type.EOS);  // dummy
  }

  /**
   * Consume the current token.
   * For example, the current token is "abc" and it is consumed.
   * "index" points the head of next token.
   * <pre>
   *                         index
   *                         |
   *             0   1   2   3   4   5   6
   *           +---+---+---+---+---+---+---+
   *   tokens: | a | b | c | d | e | f | g |
   *           +---+---+---+---+---+---+---+
   *
   *                  |
   *                  V
   *   
   *             index
   *             |
   *             0   1   2   3
   *           +---+---+---+---+
   *   tokens: | d | e | f | g |
   *           +---+---+---+---+
   * </pre>
   *
   * @throws ParseException it is thrown if the out of range is happen.
   */
  private void consumeToken() throws ParseException {
    if (index > tokens.size()) {
      abort("Interpreter side error in Parser#consumeToken");
    }
    // Remove from 0 to (index - 1).
    for (int i = 0; i < index; i++) {
      try {
        tokens.remove(0);
      } catch (IndexOutOfBoundsException ignored) {
        abort("Interpreter side error in Parser#consumeToken");
      }
    }
    index = 0;
  }

  /**
   * Skips {@code <nl>}.
   * Skip new line code from the current position.
   *
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if the out of range is happen.
   */
  private void skipNewLine() throws LexException, ParseException {
    if (getTokenType() == Token.Type.NEW_LINE) {
      goToNextToken();
    }
  }

  /**
   * Skips {@code <nl>+}.
   * Skip new line codes from the current position.
   * As a result, there is other letter in the current position.
   *
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if the out of range is happen.
   */
  private void skipNewLines() throws LexException, ParseException {
    while (getTokenType() == Token.Type.NEW_LINE) {
      goToNextToken();
    }
  }

  /**
   * Skips {@code <end>}.
   * Skip the following tokens from the current position.
   * <ul>
   *   <li> ";" "\n"
   *   <li> "," "\n"
   *   <li> "\n"
   * </ul>
   *
   * @return the skipped {@code <end>} Token if {@code <end>} is
   *         skipped, {@code null} otherwise.
   * @throws LexException it is thrown if unknown token is found.
   * @throws ParseException it is thrown if the out of range is happen.
   */
  private @Nullable Token skipEnd() throws LexException, ParseException {
    boolean isEnd = false;

    Token token = getToken();
    if (token.type == Token.Type.SEMICOLON
        || token.type == Token.Type.COMMA) {
      isEnd = true;
      goToNextToken();
    }

    if (getTokenType() == Token.Type.NEW_LINE) {
      isEnd = true;
      goToNextToken();
    }

    return isEnd ? token : null;
  }

  /**
   * Aborts Parser.
   * Print an error message and throw Exception.
   *
   * @param message error message
   * @throws ParseException this exception always happen when this method
   *                        is called.
   */
  private void abort(String message) throws ParseException {
    throw ParseException.makeParseException(message, null, config);
  }
}
