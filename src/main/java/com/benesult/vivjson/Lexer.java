/* Lexer for VivJson.
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
 * Lexer for VivJson.
 * <ul>
 *   <li> {@link #Lexer(String, String, Config) Lexer } :
 *                         Constructor. Its argument is source code as
 *                         string.
 *   <li> {@link #getToken() Lexer#getToken } : 
 *                         Extract a token from the current position of
 *                         source code.
 * </ul>
 *
 * <p>For example, the given source code is extracted as below.
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
 * <p>Last modified: 2025-03-20
 *
 * @author Fumiaki Motegi (motegi@benesult.com)
 */
class Lexer {
  /**
   * A marker for end of string.
   * It indicates that all letters are consumed.
   */
  private static final char EOS = '\0';
  /** Token types of 1 letter's operations. */
  private static final Token.Type[] ONE_LETTER_OPERATORS = {
    Token.Type.DOT,
    Token.Type.LEFT_PARENTHESIS,
    Token.Type.RIGHT_PARENTHESIS,
    Token.Type.LEFT_SQUARE_BRACKET,
    Token.Type.RIGHT_SQUARE_BRACKET,
    Token.Type.LEFT_CURLY_BRACKET,
    Token.Type.RIGHT_CURLY_BRACKET
  };
  /**
   * 2 dimensional array of Token type.
   * 1st value is Token type of 2 letter's operation.
   * 2nd value is Token type of first one letter.
   */
  private static final Token.Type[][] TWO_LETTERS_OPERATORS = {
    // {for 2 letters, for first 1 letter}
    {Token.Type.EQ, Token.Type.ASSIGN},  // ==, =
    {Token.Type.LE, Token.Type.LT},  // <=, <
    {Token.Type.GE, Token.Type.GT},  // >=, >
    {Token.Type.NE, Token.Type.ERROR},  // !=, (unexpected)
    {Token.Type.PLUS_ASSIGN, Token.Type.PLUS},  // +=, +
    {Token.Type.MINUS_ASSIGN, Token.Type.MINUS},  // -=, -
    {Token.Type.MULTIPLY_ASSIGN, Token.Type.MULTIPLY},  // *=, *
    {Token.Type.DIVIDE_ASSIGN, Token.Type.DIVIDE},  // /=, /
    {Token.Type.MODULO_ASSIGN, Token.Type.MODULO},  // %=, %
    {Token.Type.RESULT, Token.Type.SET}  // :=, :
  };
  /**
   * White-spaces except line break (new line code).
   * Space, Tab, and Carriage Return.
   */
  private static final char[] WHITE_SPACES = {' ', '\t', '\r'};

  /**
   * Printable letters in escaped sequence.
   * Quotation mark, Reverse solidus, Solidus
   */
  private static final char[] PRINTABLE_LETTERS = {'"', '\'', '\\', '/'};

  /** Token types of terminator. */
  private static final Map<Character, Object> TERMINATORS = new HashMap<>();

  static {
    TERMINATORS.put('\n', Token.Type.NEW_LINE);
    TERMINATORS.put(';', Token.Type.SEMICOLON);
    TERMINATORS.put(',', Token.Type.COMMA);
    TERMINATORS.put(EOS, Token.Type.EOS);
  }

  /** Replaced letters. */
  private static final Map<Character, Character> REPLACED_LETTERS = new HashMap<>();

  static {
    REPLACED_LETTERS.put('b', (char) 0x08);  // BS
    REPLACED_LETTERS.put('f', (char) 0x0C);  // Form-feed
    REPLACED_LETTERS.put('n', (char) 0x0A);  // LF
    REPLACED_LETTERS.put('r', (char) 0x0D);  // CR
    REPLACED_LETTERS.put('t', (char) 0x09);  // Tab
  }

  /**
   * The name of object that has source code. It is used to report error.
   * For example, "test.viv", "1st argument".
   * May be {@code null}.
   */
  @Nullable private final String medium;

  /** Source code as text. It is given from caller. */
  private final String sourceCode;

  /**
   * Configuration.
   * {@code null} if it is not needed.
   */
  @Nullable private final Config config;

  /** A current letter in the text. */
  private char currentLetter;
  /** The current position in the text. */
  private int currentPosition;
  /** The position of the current token's head in the text. */
  private int tokenHeadPosition;
  /**
   * Line number in the text.
   * It is saved into Token and is used for error information.
   */
  private int lineNumber;
  /**
   * The position of the current line's head in the text.
   * It is used to calculate column number that is saved into Token.
   * The column number is used for error information. 
   */
  private int lineHeadPosition;
  /**
   * Previous token.
   * {@code null} if first parsing.
   */
  @Nullable private Token previousToken;

  /**
   * Makes Lexer.
   *
   * @param sourceCode a source code as text
   * @param medium the name of object that has source code. It is used to
   *               report error. For example, "test.viv", "1st argument".
   *               May be {@code null}.
   * @param config configuration. {@code null} if it is not needed.
   */
  Lexer(String sourceCode, @Nullable String medium, @Nullable Config config) {
    this.medium = medium;
    this.config = config;

    this.sourceCode = sourceCode + "\n";  // Add for convenient reason

    this.currentLetter = '\0';
    this.currentPosition = -1;  // It becomes 0 in the following function.
    goToNextLetter();
    tokenHeadPosition = currentPosition;

    lineNumber = 0;  // It becomes 1 in the following function.
    recordNewLine();

    previousToken = null;
  }

  /**
   * Gets the current token.
   *
   * @return the current token.
   * @throws LexException this exception is thrown if invalid token is
   *                      found.
   */
  @SuppressWarnings("null")
  Token getToken() throws LexException {
    while (skipWhiteSpace() || skipComment()) {
      ;
    }

    tokenHeadPosition = currentPosition;
    Token token = null;

    // null is returned when these method can't treat the current lexeme.
    do {
      token = maybeGet1LetterOperation();
      if (token != null) {
        break;
      }
      token = maybeGet2LettersOperation();
      if (token != null) {
        break;
      }
      token = maybeGetString();
      if (token != null) {
        break;
      }
      token = maybeGetNumber();
      if (token != null) {
        break;
      }
      token = maybeGetIdentifierOrKeyword();
      if (token != null) {
        break;
      }
      token = maybeGetTerminator();
    } while (false);

    // Error if all methods can't treat the current lexeme.
    if (token == null || token.type == Token.Type.ERROR) {
      abort("Unknown token");
    }

    previousToken = token;
    return token;
  }

  /**
   * Records about new line.
   */
  private void recordNewLine() {
    lineNumber++;
    lineHeadPosition = currentPosition;
  }

  /**
   * Calculates column number.
   *
   * @return the column number. Its minimum value is 1.
   */
  private int getColumnNumber() {
    return tokenHeadPosition - lineHeadPosition + 1;
  }

  /**
   * Seeks and get a letter.
   * The current position is moved to the given offset.
   * Then the current letter is updated. It becomes EOS marker when the
   * position is larger than source code's size. In other words, it 
   * indicates that all letters are consumed.
   *
   * @param offset the offset of position in the text.
   */
  private void goTo(int offset) {
    currentPosition += offset;
    if (currentPosition < 0) {
      currentPosition = 0;
    }

    currentLetter = getLetter();
  }

  /**
   * Processes a next letter.
   * The current position is moved to next letter.
   * Then the current letter is updated. It becomes EOS marker when
   * all letters are consumed.
   */
  private void goToNextLetter() {
    goTo(1);
  }

  /**
   * Peek a next letter.
   *
   * @return a letter of next position.
   *         EOS if the next position is out of range.
   */
  private char peekNextLetter() {
    return getLetter(1);
  }

  /**
   * Gets a letter of current position.
   *
   * @return a letter of current position.
   *         EOS if the current position is out of range.
   */
  private char getLetter() {
    return getLetter(0);
  }

  /**
   * Gets a letter of the given offset.
   *
   * @param offset the offset of position in the text.
   *               It must be positive number.
   * @return a letter of the given offset.
   *         EOS if the given position is out of range.
   */
  private char getLetter(int offset) {
    int position = currentPosition + offset;
    return position < sourceCode.length() ? sourceCode.charAt(position) : EOS;
  }

  /**
   * Tries to get a token of 1 letter's operation.
   *
   * <p>When Token is returned, the current position and letter are
   * progressed. Otherwise, they are kept.
   *
   * @return a token of 1 letter's operation if it is existed in
   *         the current position.
   *         Otherwise, {@code null} is returned.
   */
  private @Nullable Token maybeGet1LetterOperation() {
    Token token = null;
    for (Token.Type type : ONE_LETTER_OPERATORS) {
      if (currentLetter == type.value.charAt(0)) {
        token = makeToken(type);
        goToNextLetter();
        break;
      }
    }
    return token;
  }

  /**
   * Tries to get a token of two letters operation.
   *
   * <p>When Token is returned, the current position and letter are
   * progressed. Otherwise, they are kept.
   *
   * @return a token of "==", "=", "&lt;=", "&lt;", "&gt;=", "&gt;",
   *         or "!=" if it is existed in the current position.
   *         Otherwise, {@code null} is returned.
   * @throws LexException this exception is thrown if invalid token is
   *                      found.
   */
  private @Nullable Token maybeGet2LettersOperation() throws LexException {
    Token token = null;
    for (Token.Type[] types : TWO_LETTERS_OPERATORS) {
      String operator = types[0].value;
      if (currentLetter == operator.charAt(0)) {
        int length = 1;
        if (peekNextLetter() == operator.charAt(1)) {
          length = 2;
        }
        Token.Type type = types[2 - length];
        operator = operator.substring(0, length);
        if (type == Token.Type.ERROR) {
          abort("Operation " + operator + " is not allowed here");
        }
        token = makeToken(type, operator);
        goTo(length);
        break;
      }
    }
    return token;
  }

  /**
   * Tries to get a token of string.
   * string is any characters as UTF-8. It is surrounded with "" or ''.
   *
   * <p>When Token is returned, the current position and letter are
   * progressed. Otherwise, they are kept.
   *
   * @return a token of string if it is existed in the current position.
   *         Otherwise, {@code null} is returned.
   * @throws LexException this exception is thrown if invalid token is
   *                      found.
   */
  private @Nullable Token maybeGetString() throws LexException {
    // Need a quotation mark for start.
    if (currentLetter != '"' && currentLetter != '\'') {
      return null;
    }
    char surroundMark = currentLetter;

    // Remove the above quotation mark.
    goToNextLetter();
    String lexeme = "";

    // Loop until reaching a quotation mark or end of string.
    while (currentLetter != surroundMark && currentLetter != '\n'
           && currentLetter != EOS) {
      // 4 hex-digits unicode
      Character letter = get4HexDigitsUnicode();
      if (letter != null) {
        lexeme += letter;
        goTo(6);
        continue;
      }

      // Escape sequence
      if (currentLetter == '\\') {
        char nextLetter = peekNextLetter();
        // Quotation mark, Reverse solidus, Solidus
        if (Utils.isContained(nextLetter, PRINTABLE_LETTERS)) {
          lexeme += nextLetter;
          goTo(2);
          continue;
        }
        // BS, Form-feed, LF, CR, Tab
        @Nullable Character replaced = REPLACED_LETTERS.get(nextLetter);
        if (replaced != null) {
          lexeme += replaced;
          goTo(2);
          continue;
        }
      }

      lexeme += currentLetter;
      // Progress
      goToNextLetter();
    }

    if (currentLetter != surroundMark) {
      abort("Missing the end of quotation mark");
    }

    // Remove the quotation mark.
    goToNextLetter();

    return makeToken(Token.Type.STRING, lexeme);
  }

  /**
   * Gets a letter from 4 hex-digits unicode from current position.
   *
   * @return a letter if it is valid, {@code null} otherwise
   * @throws LexException this exception is thrown if invalid token is
   *                      found.
   */
  private @Nullable Character get4HexDigitsUnicode() throws LexException {
    // Start of 4 hex-digits unicode must be "\" and "u".
    if (currentLetter != '\\' || peekNextLetter() != 'u') {
      return null;
    }
    if (sourceCode.length() < currentPosition + 6) {
      abort("4 hex-digits unicode is invalid.");
    }
    int code = 0;
    for (int offset = 2; offset < 6; offset++) {
      char letter = sourceCode.charAt(currentPosition + offset);
      int digit = Character.digit(letter, 16);
      if (digit < 0 || digit > 15) {
        abort("4 hex-digits unicode is invalid.");
      }
      code <<= 4;
      code |= digit;
    }
    return (char) code;
  }

  /**
   * Tries to get a token of number.
   * Get a token of number as the following format.
   * As you can see, the head of sign (+/-) is not given in order to
   * parse later.
   * For example, 2, 5.7, 100.5e30, 0.8e+2, 4E-100.
   *
   * <p>When previous token is "." and "." exists after number, this
   * number is treated as int. To tell the truth, this judgement is
   * too much as role of Lexer. But it is needed here.
   * <pre>{@code
   *   <number> ::= <digit>+ [ "." <digit>+ ]
   *       [ ( "e" | "E" ) [ "+" | "-" ] <digit>+ ]
   *   <digit> ::= "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8"
   *       | "9"
   * }</pre>
   *
   * <p>When Token is returned, the current position and letter are
   * progressed. Otherwise, they are kept.
   *
   * @return a token of number if it is existed in the current position.
   *         Otherwise, {@code null} is returned.
   * @throws LexException this exception is thrown if invalid token is
   *                      found.
   */
  @SuppressWarnings("null")
  private @Nullable Token maybeGetNumber() throws LexException {
    int startPosition = currentPosition;

    boolean isSkipped = skipDigit();  // Check and proceed for 0-9.
    if (!isSkipped) {
      return null;
    }

    Token.Type type = Token.Type.NUMBER;

    if (currentLetter == '.') {  // Check a decimal point.
      // For access of array/block's member with "." operator.
      // For example, the above skipped digits are 10 and the
      // current letter is 2nd dot in "foo.10.3".
      // Avoid to confuse as floating-number.
      if (previousToken != null && previousToken.type == Token.Type.DOT) {
        String lexeme = sourceCode.substring(startPosition, currentPosition);
        return makeToken(type, lexeme);
      }
      // Is there number after dot (decimal point)?
      if (Character.isDigit(peekNextLetter())) {
        // For floating-point number
        // For example, the above skipped digits are 10,
        // the current letter is dot, and peeked letter
        // is 3 in "10.35".
        goToNextLetter();  // Skip a decimal point.
        isSkipped = skipDigit();  // Check and proceed for 0-9.
        if (!isSkipped) {
          abort("Interpreter side error in Lexer#maybeGetNumber");
        }
      } else {
        // For access of array/block's member with "." operator.
        // Check the following token may be identifier.
        // Note that white spaces (includes line-break) around dot
        // are permitted, such as "foo.3. bar".
        int offset = 1;
        char letter;
        while ((letter = getLetter(offset)) != EOS) {
          if (letter == '_' || isAlphabet(letter)) {
            String lexeme =
                sourceCode.substring(startPosition, currentPosition);
            return makeToken(type, lexeme);
          }
          if (letter != '\n' && !Utils.isContained(letter, WHITE_SPACES)) {
            break;
          }
          offset += 1;
        }
        abort("There is no digit after decimal point");
      }
    }

    if (currentLetter == 'e'
        || currentLetter == 'E') { // Check a exponential mark.
      goToNextLetter();  // Skip a exponential mark.
      if (currentLetter == '+' || currentLetter == '-') { // Check a sign.
        goToNextLetter();  // Skip a sign.
      }
      isSkipped = skipDigit();  // Check and proceed for 0-9.
      if (!isSkipped) {
        abort("There is no digit after exponential mark");
      }
    }

    // When this line is reached, the current position goes over number.
    // In other words, the current letter is not number.
    String lexeme = sourceCode.substring(startPosition, currentPosition);
    return makeToken(type, lexeme);
  }

  /**
   * Tries to get a token of identifier/keyword.
   * <pre>{@code
   * | Category          | 1st letter | The following letters |
   * |-------------------|------------|-----------------------|
   * | Alphabet [a-zA-Z] | Valid      | Valid                 |
   * | Digit [0-9]       | Invalid    | Valid                 |
   * | Under score "_"   | Valid      | Valid                 |
   * | Others            | Invalid    | Invalid               |
   * }</pre>
   *
   * <p>When Token is returned, the current position and letter are
   * progressed. Otherwise, they are kept.
   *
   * @return a token of identifier/keyword if it is existed in the
   *         current position. Otherwise, {@code null} is returned.
   */
  private @Nullable Token maybeGetIdentifierOrKeyword() {
    int startPosition = currentPosition;
    boolean isProcessed = false;
    while (isAlphabet(currentLetter) || currentLetter == '_'
           || (isProcessed && Character.isDigit(currentLetter))) {
      goToNextLetter();
      isProcessed = true;
    }

    if (!isProcessed) {
      return null;
    }

    // When this line is reached, the current position goes over
    // the identifier. In other words, the current letter is not
    // identifier.
    String lexeme = sourceCode.substring(startPosition, currentPosition);

    Token token = makeToken(Token.Type.IDENTIFIER, lexeme);
    token.fixKeywordType();
    return token;
  }

  /**
   * Tries to get a token of terminator.
   *
   * <p>When Token is returned, the current position and letter are
   * progressed. Otherwise, they are kept.
   *
   * @return a token of terminator if it is existed in the current position.
   *         Otherwise, {@code null} is returned.
   */
  @SuppressWarnings("unused")
  private @Nullable Token maybeGetTerminator() {
    @Nullable Object type = TERMINATORS.get(currentLetter);
    if (type == null) {
      return null;
    }
    Token token = makeToken((Token.Type) type,
                            Character.toString(currentLetter));
    char letter = currentLetter;
    goToNextLetter();
    if (letter == '\n') {
      recordNewLine();
    }
    return token;
  }

  /**
   * Makes a token.
   * Token's lexeme is assigned value of Token type.
   *
   * @param type the type of token
   * @return a token
   */
  private Token makeToken(Token.Type type) {
    return makeToken(type, null);
  }

  /**
   * Makes a token.
   *
   * @param type the type of token
   * @param lexeme the token's actual text. It is so called "lexeme".
   *               When this is {@code null}, the assigned value of
   *               Token type is used instead it.
   * @return a token.
   */
  private Token makeToken(Token.Type type, @Nullable String lexeme) {
    return new Token(type, medium, lineNumber, getColumnNumber(), lexeme);
  }

  /**
   * Skips digit.
   *
   * @return true if digit is skipped, false otherwise.
   */
  private boolean skipDigit() {
    boolean isSkipped = false;
    while (Character.isDigit(currentLetter)) {
      isSkipped = true;
      goToNextLetter();
    }
    return isSkipped;
  }

  /**
   * Skips white space.
   * However newlines are not skipped because we will use it as the end
   * of a statement.
   *
   * @return true if white spaces are skipped, false otherwise
   */
  private boolean skipWhiteSpace() {
    boolean isSkipped = false;
    while (Utils.isContained(currentLetter, WHITE_SPACES)) {
      goToNextLetter();
      isSkipped = true;
    }
    return isSkipped;
  }

  /**
   * Skips comment in the code.
   * The following comment is skipped.
   * <ul>
   *   <li>It is constructed from "#" to the end of line.
   *   <li>It is constructed from "//" to the end of line.
   *   <li>It is constructed from "&sol;*" to "*&sol;".
   *       It can be over multi-line.
   * </ul>
   *
   * @return true if comment is skipped, false otherwise
   * @throws LexException this exception is thrown if invalid token is
   *                      found.
   */
  private boolean skipComment() throws LexException {
    if ((currentLetter == '/' && peekNextLetter() == '/')
        || currentLetter == '#') {
      while (currentLetter != '\n') {
        goToNextLetter();
      }
      return true;
    }

    if (currentLetter != '/' || peekNextLetter() != '*') {
      return false;
    }
    goTo(2);
    while (currentLetter != '*' || peekNextLetter() != '/') {
      if (currentLetter == EOS) {
        abort("Missing the the end of \"*/\"");
      }
      char letter = currentLetter;
      goToNextLetter();
      if (letter == '\n') {
        recordNewLine();
      }
    }
    goTo(2);
    return true;
  }

  /**
   * Judges whether the given letter is alphabet or not.
   *
   * @param letter a letter
   * @return true if it is alphabet, false otherwise
   */
  private static boolean isAlphabet(char letter) {
    return ((letter >= 'a' && letter <= 'z')
            || (letter >= 'A' && letter <= 'Z'));
  }

  /**
   * Aborts Lexer.
   * Print an error message and throw Exception.
   *
   * @param message error message
   * @throws LexException this exception always happen when this method
   *                      is called.
   */
  private void abort(String message) throws LexException {
    throw LexException.makeLexException(message, medium, lineNumber,
                                        getColumnNumber(), config);
  }
}
