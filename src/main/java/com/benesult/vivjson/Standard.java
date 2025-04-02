/* Standard library for VivJson.
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Standard library for VivJson.
 * <ul>
 *   <li> {@link #std_if(Evaluator, Call, Config) std_if } :
 *                                   if (condition) { operations }
 *   <li> {@link #std_do(Evaluator, Call, Config) std_do } :
 *                                   do { operations }
 *   <li> {@link #std_while(Evaluator, Call, Config) std_while } :
 *                                   while (condition) { operations }
 *   <li> {@link #std_for(Evaluator, Call, Config) std_for } :
 *                                   for ( ... ) { operations }
 *   <li> {@link #std_int(Evaluator, Call, Config) std_int } :
 *                                   int(value)
 *   <li> {@link #std_float(Evaluator, Call, Config) std_float } :
 *                                   float(value)
 *   <li> {@link #std_string(Evaluator, Call, Config) std_string } :
 *                                   string(value)
 *   <li> {@link #std_len(Evaluator, Call, Config) std_len } :
 *                                   len(value)
 *   <li> {@link #std_insert(Evaluator, Call, Config) std_insert } :
 *                                   insert(array, index, value)
 *   <li> {@link #std_strip(Evaluator, Call, Config) std_strip }:
 *                                   strip(value)
 *   <li> {@link #std_type(Evaluator, Call, Config) std_type } :
 *                                   type(value)
 *   <li> {@link #std_print(Evaluator, Call, Config) std_print } :
 *                                   print(value [, value, ...])
 * </ul>
 *
 * <p>Refer to:
 * <ul>
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
class Standard {
  /**
   * The prefix of method. It is defined as "std_".
   * Almost methods of this library have general name like
   * "print", "if", and so on. It is confused host language's
   * functions and statements.
   * So this prefix is added: std_print, std_if, ...
   */
  private static final String PREFIX = "std_";
  /**
   * Removed letters by strip function.
   * Double-byte space (Full-width space) is included.
   */
  private static final char[] STRIPPED_LETTERS = {' ', '\t', '\r', '\n', '　'};

  /**
   * if/elseif/else.
   * <pre>{@code
   *   if (...) {...} [ elseif (...) {...} ... ] else {...}
   *
   *   For example,
   *       if (...) {...}
   *          ----- -----
   *            |     |
   *            |   call.arguments.values[1]
   *          call.arguments.values[0]
   *
   *       if (...) {...} else {...}
   *          ----- -----      -----
   *            |     |          |
   *            |     |        call.arguments.values[3]
   *            |     |    call.arguments.values[2] is always true.
   *            |   call.arguments.values[1]
   *          call.arguments.values[0]
   *
   *       if (...) {...} elif (...) {...} else {...}
   *          ----- -----      ----- -----      -----
   *            |     |          |     |          |
   *            |     |          |     |      call.arguments.values[5]
   *            |     |          |     |  call.arguments.values[4] is
   *            |     |          |     |  always true.
   *            |     |          |   call.arguments.values[3]
   *            |     |        call.arguments.values[2]
   *            |   call.arguments.values[1]
   *          call.arguments.values[0]
   * }</pre>
   *
   * @param evaluator Evaluator instance
   * @param call the information of calling.
   *             call.arguments.values is a list of 2 x n statements.
   *             <ul>
   *               <li> 2 x n: condition
   *               <li> 2 x n + 1: A list of operations as Block
   *             </ul>
   * @param config configuration if it is needed, {@code null} otherwise
   * @return the result of Block evaluation
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  public static @Nullable Object std_if(Evaluator evaluator, Call call,
                                        @Nullable Config config)
                                        throws EvaluateException {
    ArrayList<Statement> statements = call.arguments.values;
    int length = statements.size();
    if (length == 0 || length % 2 != 0) {
      throw new EvaluateException();
    }
    for (int index = 0; index < length; index += 2) {
      Object isSatisfied = evaluator.evaluate(statements.get(index));
      if (Evaluator.isTruthy(isSatisfied)) {
        return evaluator.evaluate(statements.get(index + 1));
      }
    }
    return null;
  }

  /**
   * do { operations }.
   *
   * @param evaluator Evaluator instance
   * @param call the information of calling.
   *             call.arguments.values is a list of 1 statement.
   *             <ul>
   *               <li> 1st (Block): A list of operations as Block
   *             </ul>
   * @param config configuration if it is needed, {@code null} otherwise
   * @return the result of Block evaluation
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  public static @Nullable Object std_do(Evaluator evaluator, Call call,
                                        @Nullable Config config)
                                        throws EvaluateException {
    ArrayList<Statement> statements = call.arguments.values;
    int length = statements.size();
    if (length == 1) {
      @Nullable Statement statement = statements.get(0);
      if (statement instanceof Block) {
        Literal condition = new Literal(new Token(Token.Type.TRUE));

        ArrayList<Statement> initial = new ArrayList<>();
        initial.add(condition);

        ArrayList<Statement> continuous = new ArrayList<>();
        continuous.add(
          new Binary(
              new Identifier(new Token(Token.Type.IDENTIFIER,
                                       Environment.CONTINUE_NAME)),
              new Token(Token.Type.EQ),
              condition));

        Loop loop = new Loop(call,
                             initial,
                             continuous,
                             ((Block) statement).values,
                             null,
                             null);
        return evaluator.evaluate(loop);
      }
    }
    throw new EvaluateException();
  }

  /**
   * while (condition) { operations }.
   *
   * @param evaluator Evaluator instance
   * @param call the information of calling.
   *             call.arguments.values is a list of 2 statements.
   *             <ul>
   *               <li> 1st (Statement): condition
   *               <li> 2nd (Block): A list of operations as Block
   *             </ul>
   * @param config configuration if it is needed, {@code null} otherwise
   * @return the result of Block evaluation
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  public static @Nullable Object std_while(Evaluator evaluator, Call call,
                                           @Nullable Config config)
                                           throws EvaluateException {
    ArrayList<Statement> statements = call.arguments.values;
    int length = statements.size();
    if (length == 2) {
      @Nullable Statement statement = statements.get(1);
      if (statement instanceof Block) {
        ArrayList<Statement> condition = new ArrayList<>();
        condition.add(statements.get(0));

        Loop loop = new Loop(call,
                             condition,
                             condition,
                             ((Block) statement).values,
                             null,
                             null);
        return evaluator.evaluate(loop);
      }
    }
    throw new EvaluateException();
  }

  /**
   * for ( ... ) { operations }.
   *
   * <p>Example:
   * <ul>
   *   <li> for (init; condition; update) { operations }
   *   <li> for (; condition; update) { operations }
   *   <li> for (;; update) { operations }
   *   <li> for (;;) { operations }
   *   <li> for () { operations }
   *   <li> for (value in list) { operations }
   *   <li> for (init; condition; update; { operations })
   * </ul>
   *
   * @param evaluator Evaluator instance
   * @param call the information of calling.
   *             call.arguments.values is a list of statements.<br>
   *             When a list has 4 statements:
   *             <ul>
   *               <li> 1st (Statement): initial operation
   *               <li> 2nd (Statement): condition
   *               <li> 3rd (Statement): update operation
   *               <li> 4th (Block): A list of operations as Block
   *             </ul>
   *             When a list has 2 statements:
   *             <ul>
   *               <li> 1st (Binary): value in list
   *               <li> 2nd (Block): A list of operations as Block
   *             </ul>
   * @param config configuration if it is needed, {@code null} otherwise
   * @return the result of Block evaluation
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  public static @Nullable Object std_for(Evaluator evaluator, Call call,
                                         @Nullable Config config)
                                         throws EvaluateException {
    ArrayList<Statement> statements = call.arguments.values;
    Loop loop;
    int length = statements.size();
    if (length == 2) {
      @Nullable Statement iterator = statements.get(0);
      if (!(iterator instanceof Binary)
          || !(((Binary) iterator).left instanceof Identifier)
          || ((Binary) iterator).operator.type != Token.Type.IN) {
        throw EvaluateException.makeEvaluateException(
          iterator + " is invalid", iterator, config, false);
      }
      @Nullable Statement operations = statements.get(1);
      if (!(operations instanceof Block)) {
        throw EvaluateException.makeEvaluateException(
          operations + " is invalid", operations, config, false);
      }

      @Nullable Object data = evaluator.evaluate(((Binary) iterator).right);

      Literal condition = new Literal(new Token(Token.Type.TRUE));
      ArrayList<Statement> initial = new ArrayList<>();
      initial.add(condition);
      ArrayList<Statement> continuous = new ArrayList<>();
      continuous.add(condition);

      loop = new Loop(call,
                      initial,
                      continuous,
                      ((Block) operations).values,
                      (Identifier) (((Binary) iterator).left),
                      data);
    } else if (length == 4) {
      @Nullable Statement operations = statements.get(3);
      if (!(operations instanceof Block)) {
        throw EvaluateException.makeEvaluateException(
          operations + " is invalid", operations, config, false);
      }

      ArrayList<Statement> initial = new ArrayList<>();
      initial.add(statements.get(0));
      initial.add(statements.get(1));
      ArrayList<Statement> continuous = new ArrayList<>();
      continuous.add(statements.get(2));
      continuous.add(statements.get(1));

      loop = new Loop(call,
                      initial,
                      continuous,
                      ((Block) operations).values,
                      null,
                      null);
    } else {
      throw new EvaluateException();
    }
    return evaluator.evaluate(loop);
  }

  /**
   * int(value).
   *
   * @param evaluator Evaluator instance
   * @param call the information of calling.
   *             call.arguments.values is a list of 1 statement.
   *             <ul>
   *               <li> 1st: A number whose type is int, float,
   *                         or string.
   *             </ul>
   * @param config configuration if it is needed, {@code null} otherwise
   * @return the result of Block evaluation
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  public static @Nullable Object std_int(Evaluator evaluator, Call call,
                                         @Nullable Config config)
                                         throws EvaluateException {
    ArrayList<Statement> statements = call.arguments.values;
    do {  // It isn't loop. It is used for break.
      int length = statements.size();
      if (length != 1) {
        break;
      }
      @Nullable Object value = evaluator.evaluate(statements.get(0));
      if (value instanceof String) {
        try {
          value = Double.valueOf((String) value);
        } catch (NullPointerException e) {
          break;
        } catch (NumberFormatException e) {
          break;
        }
      }
      if (value instanceof Number) {
        Double d = ((Number) value).doubleValue();
        if (!d.isNaN() && !d.isInfinite()) {
          return (Long) (((Number) value).longValue());
        }
      }
    } while (false);
    throw new EvaluateException();
  }

  /**
   * float(value).
   *
   * @param evaluator Evaluator instance
   * @param call the information of calling.
   *             call.arguments.values is a list of 1 statement.
   *             <ul>
   *               <li> 1st: A number whose type is int, float,
   *                         or string.
   *             </ul>
   * @param config configuration if it is needed, {@code null} otherwise
   * @return the result of Block evaluation
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  public static @Nullable Object std_float(Evaluator evaluator, Call call,
                                           @Nullable Config config)
                                           throws EvaluateException {
    ArrayList<Statement> statements = call.arguments.values;
    do {  // It isn't loop. It is used for break.
      int length = statements.size();
      if (length != 1) {
        break;
      }
      @Nullable Object value = evaluator.evaluate(statements.get(0));
      if (value instanceof String) {
        try {
          return Double.valueOf((String) value);
        } catch (NullPointerException e) {
          break;
        } catch (NumberFormatException e) {
          break;
        }
      }
      if (value instanceof Number) {
        return (Double) (((Number) value).doubleValue());
      }
    } while (false);
    throw new EvaluateException();
  }

  /**
   * string(value).
   *
   * @param evaluator Evaluator instance
   * @param call the information of calling.
   *             call.arguments.values is a list of 1 statement.
   *             <ul>
   *               <li> 1st: Any value.
   *             </ul>
   * @param config configuration if it is needed, {@code null} otherwise
   * @return the result of Block evaluation
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  public static @Nullable Object std_string(Evaluator evaluator, Call call,
                                            @Nullable Config config)
                                            throws EvaluateException {
    ArrayList<Statement> statements = call.arguments.values;
    int length = statements.size();
    if (length == 1) {
      @Nullable Object value = evaluator.evaluate(statements.get(0));
      return Evaluator.makeString(value, config);
    }
    throw new EvaluateException();
  }

  /**
   * len(value).
   *
   * @param evaluator Evaluator instance
   * @param call the information of calling.
   *             call.arguments.values is a list of 1 statement.
   *             <ul>
   *               <li> 1st: An array, a block, or a string.
   *             </ul>
   * @param config configuration if it is needed, {@code null} otherwise
   * @return the result of Block evaluation
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  public static @Nullable Object std_len(Evaluator evaluator, Call call,
                                         @Nullable Config config)
                                         throws EvaluateException {
    ArrayList<Statement> statements = call.arguments.values;
    int length = statements.size();
    if (length == 1) {
      @Nullable Statement statement = statements.get(0);
      if (statement instanceof Array) {
        return (Long) ((long) ((Array) statement).values.size());
      }
      if (statement instanceof Block) {
        return (Long) ((long) ((Block) statement).values.size());
      }
      if (statement != null) {
        @Nullable Object value = evaluator.evaluate(statement);
        if (value instanceof String) {
          return (Long) ((long) ((String) value).length());
        }
        if (value instanceof ArrayList) {
          return (Long) ((long) ((ArrayList<?>) value).size());
        }
        if (value instanceof HashMap) {
          return (Long) ((long) ((HashMap<?, ?>) value).size());
        }
      }
    }
    throw new EvaluateException();
  }

  /**
   * insert(array, index, value).
   * Insert a value into the array.
   * Inserted position can be given with "index".
   *
   * @param evaluator Evaluator instance
   * @param call the information of calling.
   *             call.arguments.values is a list of 3 statements.
   *             <ul>
   *               <li> 1st (Array): An array
   *               <li> 2nd (Statement): An index
   *               <li> 3rd (Statement): A value
   *             </ul>
   * @param config configuration if it is needed, {@code null} otherwise
   * @return it is always {@code null}.
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  public static @Nullable Object std_insert(Evaluator evaluator, Call call,
                                            @Nullable Config config)
                                            throws EvaluateException {
    ArrayList<Statement> statements = call.arguments.values;
    if (statements.size() >= 3) {
      @Nullable Statement statement = statements.get(0);
      @Nullable Object object = evaluator.evaluate(statement);
      @Nullable ArrayList<@Nullable Object> array =
                                  Evaluator.getArrayListOrNull(object);
      if (array == null) {
        throw EvaluateException.makeEvaluateException(
          "insert() needs array", statement, config, false);
      }
      int length = array.size();
      if (length + 1 <= Config.getMaxArraySize(config)) {
        statement = statements.get(1);
        object = evaluator.evaluate(statement);
        if (!(object instanceof Number)) {
          throw EvaluateException.makeEvaluateException(
            "index of insert() must be number", statement, config, false);
        }
        int index = ((Number) object).intValue();
        if (index < -1 * length || index > length) {
          throw EvaluateException.makeEvaluateException(
            "out of range", statement, config, false);
        }
        if (index < 0) {
          index += length;
        }
        statement = statements.get(2);
        object = evaluator.evaluate(statement);
        array.add(index, object);
        return null;
      }
    }
    throw new EvaluateException();
  }

  /**
   * strip(value).
   * Remove white-space and new line code from the head/tail.
   * Double-byte space (Full-width space) is also removed.
   *
   * @param evaluator Evaluator instance
   * @param call the information of calling.
   *             call.arguments.values is a list of 1 statement.
   *             <ul>
   *               <li> 1st: A string.
   *             </ul>
   * @param config configuration if it is needed, {@code null} otherwise
   * @return the result of Block evaluation
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  public static @Nullable Object std_strip(Evaluator evaluator, Call call,
                                           @Nullable Config config)
                                           throws EvaluateException {
    ArrayList<Statement> statements = call.arguments.values;
    int length = statements.size();
    if (length == 1) {
      @Nullable Statement statement = statements.get(0);
      @Nullable Object value = evaluator.evaluate(statement);
      if (value instanceof String) {
        String string = (String) value; 
        length = string.length();
        int start = 0;
        int index = 0;
        while (index < length) {
          start = index;
          try {
            char letter = string.charAt(index);
            if (!Utils.isContained(letter, STRIPPED_LETTERS)) {
              break;
            }
          } catch (IndexOutOfBoundsException e) {
            throw new EvaluateException();
          }
          index++;
        }

        int end = length;
        index = length;
        while (index > 0) {
          end = index;
          index--;
          try {
            char letter = string.charAt(index);
            if (!Utils.isContained(letter, STRIPPED_LETTERS)) {
              break;
            }
          } catch (IndexOutOfBoundsException e) {
            throw new EvaluateException();
          }
        }

        if (start < end) {
          return string.substring(start, end);
        }
        return "";
      }
    }
    throw new EvaluateException();
  }

  /**
   * type(value).
   *
   * @param evaluator Evaluator instance
   * @param call the information of calling.
   *             call.arguments.values is a list of 1 statement.
   *             <ul>
   *               <li> 1st: Any value.
   *             </ul>
   * @param config configuration if it is needed, {@code null} otherwise
   * @return type is represented as text format.
   *         <ul>
   *           <li> "int"
   *           <li> "float"
   *           <li> "string"
   *           <li> "boolean"
   *           <li> "null"
   *           <li> "array"
   *           <li> "block"
   *           <li> "function"
   *         </ul>
   *         {@code null} is returned for other type.
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  public static @Nullable String std_type(Evaluator evaluator, Call call,
                                          @Nullable Config config)
                                          throws EvaluateException {
    ArrayList<Statement> statements = call.arguments.values;
    int length = statements.size();
    if (length != 1) {
      throw new EvaluateException();
    }
    @Nullable Object value = statements.get(0);
    if (value instanceof Array) {
      return "array";
    }
    if (value instanceof Block) {
      return "block";
    }
    value = evaluator.evaluate((Statement) value);
    if (value == null) {
      return "null";
    }
    if (value instanceof Identifier
        && Standard.getMethod(((Identifier) value).name.lexeme) != null) {
      return "function";
    }
    if (value instanceof CalleeRegistry) {
      return "function";
    }
    if (value instanceof ArrayList) {
      return "array";
    }
    if (value instanceof HashMap) {
      return "block";
    }
    if (value instanceof Boolean) {
      return "boolean";
    }
    if (value instanceof Long) {
      return "int";
    }
    if (value instanceof Double) {
      return "float";
    }
    if (value instanceof String) {
      return "string";
    }
    return null;
  }

  /**
   * print(value [, value, ...]).
   *
   * @param evaluator Evaluator instance
   * @param call the information of calling.
   *             call.arguments.values is a list of
   *             statements that have printable values.
   * @param config configuration if it is needed, {@code null} otherwise
   * @return it is always {@code null}.
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  public static @Nullable Object std_print(Evaluator evaluator, Call call,
                                           @Nullable Config config)
                                           throws EvaluateException {
    ArrayList<String> texts = new ArrayList<>();
    ArrayList<Statement> statements = call.arguments.values;
    for (Statement statement : statements) {
      @Nullable Object value = evaluator.evaluate(statement);
      String text = Evaluator.makeString(value, config, false, false);
      texts.add(text);
    }
    String wholeText = String.join(", ", texts);
    System.out.println(wholeText);
    return null;
  }

  /**
   * Gets standard library's method.
   *
   * @param name method name
   * @return method object if the given name's method is existed,
   *         {@code null} otherwise
   */
  static @Nullable Method getMethod(String name) {
    String methodName = PREFIX + name;
    try {
      Method method = Standard.class.getMethod(methodName,
                                               Evaluator.class,
                                               Call.class,
                                               Config.class);
      return method;
    } catch (NoSuchMethodException e) {
      ;
    } catch (NullPointerException e) {
      ;
    } catch (SecurityException e) {
      ;
    }
    return null;
  }
}
