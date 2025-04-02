/* Evaluator for VivJson.
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Evaluator for VivJson.
 * <ul>
 *   <li> <b> Evaluator </b> : Constructor.
 *   <li> <b> Evaluator#evaluate </b> : Evaluate the given statements.
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
 * <p>Last modified: 2025-04-01
 *
 * @author Fumiaki Motegi (motegi@benesult.com)
 */
class Evaluator {
  /** Elements of floating-point number. */
  private static final char[] FLOAT_ELEMENTS = {'.', 'e', 'E'};
  /** Keyword names of controlling function/loop. */
  private static final String[] FUNC_LOOP_CTRL_KEYWORD_NAMES = {
    Environment.FUNCTION_RETURN_NAME,
    Environment.BREAK_NAME,
    Environment.CONTINUE_NAME
  };
  /** Assignment operators and the corresponded binary operators. */
  private static final Token.Type[][] ASSIGN_OPERATORS = {
    {Token.Type.PLUS_ASSIGN, Token.Type.PLUS},  // +=, +
    {Token.Type.MINUS_ASSIGN, Token.Type.MINUS},  // -=, -
    {Token.Type.MULTIPLY_ASSIGN, Token.Type.MULTIPLY},  // *=, *
    {Token.Type.DIVIDE_ASSIGN, Token.Type.DIVIDE},  // /=, /
    {Token.Type.MODULO_ASSIGN, Token.Type.MODULO}  // %=, %
  };

  /** Replaced letters. */
  private static final Map<Character, Character> REPLACED_LETTERS = new HashMap<>();

  static {
    REPLACED_LETTERS.put((char) 0x08, 'b');  // BS
    REPLACED_LETTERS.put((char) 0x0C, 'f');  // Form-feed
    REPLACED_LETTERS.put((char) 0x0A, 'n');  // LF
    REPLACED_LETTERS.put((char) 0x0D, 'r');  // CR
    REPLACED_LETTERS.put((char) 0x09, 't');  // Tab
    REPLACED_LETTERS.put('"', '"');  // Double quotation mark
    REPLACED_LETTERS.put('\\', '\\');  // Reverse solidus
  }

  /**
   * Index of the evaluated statement's name in stack.
   * {@see Stack<String[]> stack }
   */
  private static final int NAME = 0;
  /**
   * Index of the evaluated statement's detail in stack.
   * {@see Stack<String[]> stack }
   */
  private static final int DETAIL = 1;
  /**
   * Index of the evaluated result in the returned value of
   * callFunction method.
   */
  private static final int RESULT = 0;
  /**
   * Index of the completeness in the returned value of
   * callFunction method.
   */
  private static final int IS_DONE = 1;

  /**
   * Configuration.
   * {@code null} if it is not needed.
   */
  @Nullable private final Config config;

  /** Environment instance that is used to read/write variable. */
  private Environment environment;

  /** Stack of evaluated statement.
   *
   * <p>There are two purposes.
   * <ul>
   *   <li> Keep limit of the recursive called times.
   *   <li> Avoid software runaway.
   * </ul>
   *
   * <p>The each element is an array that have 2 strings.
   * <ol>
   *   <li> Block type or the class name of evaluated statement.
   *   <li> The detail of evaluated statement.
   * </ol>
   */
  private Stack<String[]> stack;

  /** Maximum array size. */
  private int maxArraySize;
  /** Maximum recursive called times of evaluate method. */
  private int maxDepth;
  /** Maximum loop times of "for", "while", and so on. */
  private int maxLoopTimes;

  /**
   * Makes Evaluator.
   *
   * @param config configuration. {@code null} if it is not needed.
   */
  Evaluator(@Nullable Config config) {
    this.config = config;
    environment = new Environment();

    String infinity = Config.getInfinity(config);
    if (infinity != null) {
      environment.set(infinity, Double.POSITIVE_INFINITY);
    }
    String nan = Config.getNaN(config);
    if (nan != null) {
      environment.set(nan, Double.NaN);
    }

    stack = new Stack<>();
    maxArraySize = Config.getMaxArraySize(config);
    maxDepth = Config.getMaxDepth(config);
    maxLoopTimes = Config.getMaxLoopTimes(config);
  }

  /**
   * Gets a variable's value.
   *
   * @param name a variable name
   * @return its value.
   *         Environment.UNDEFINED is returned if the given name's
   *         variable is not existed.
   * @throws EvaluateException it is thrown if the given name isn't
   *                           string.
   */
  @SuppressWarnings("unused")
  @Nullable Object get(String name) throws EvaluateException {
    if (name == null) {
      abort("the given name is not string in Evaluator#get.");
      return null;  // dummy for avoiding warning
    }
    return environment.get(name, true);
  }

  /**
   * Sets a variable.
   *
   * @param name a variable name
   * @param value its value
   * @throws EvaluateException it is thrown if the given name isn't
   *                           string.
   */
  @SuppressWarnings("unused")
  void set(String name, Object value) throws EvaluateException {
    if (name == null) {
      abort("the given name is not string in Evaluator#get.");
      return;  // dummy for avoiding warning
    }
    environment.set(name, value, true);
  }

  /**
   * Gets configuration.
   *
   * @return configuration
   */
  @Nullable Config getConfig() {
    return config;
  }

  /**
   * Rewinds stack and environment after abort.
   * Fix as below. This is clean up for abort (exception).
   * When {@link Viv#run(Object...) Viv.run } is failed with
   * {@link Viv.Instance Instance}, {@link Viv.Instance Instance}
   * has unnecessary information. It may be happen unusual behavior via
   * its instance. So clean up is needed.
   * <ul>
   *   <li>Reset stack of evaluated statement "{@link #stack}".
   *   <li>Rewind environment "{@link #environment}" to first hierarchy.
   * </ul>
   * Note that it is assumed that the first call of evaluate method is
   * {@link #evaluate(Block)}.
   *
   * <p>For example,
   * <pre>
   *   - The newest Environment #1  : The present position
   *       - enclosing = #2                |
   *   - Environment #2                    |
   *       - enclosing = #3                V
   *   - Environment #3             : Rewind to this position.
   *     (This is made by Block statement of making Instance.)
   *       - enclosing = #4
   *       - Instance's member variable is available.
   *       - Instance's method is available.
   *   - Root Environment #4
   *       - enclosing = null
   * </pre>
   */
  void rewindAfterAbort() {
    // Reset stack of evaluated statement.
    stack.clear();
    // Rewind environment.
    Environment enclosing;
    while ((enclosing = environment.getEnclosing()) != null
            && enclosing.getEnclosing() != null) {
      environment = enclosing;
    }
  }

  /**
   * Evaluates statement.
   *
   * @param statement the evaluated statement
   * @return the evaluated result
   * @throws EvaluateException this exception always happen when this
   *                           method is called.
   */
  @Nullable Object evaluate(Statement statement) throws EvaluateException {
    @Nullable Object value = null;
    if (statement instanceof Literal) {
      value = evaluate((Literal) statement);
    } else if (statement instanceof Identifier) {
      value = evaluate((Identifier) statement);
    } else if (statement instanceof Keyword) {
      value = evaluate((Keyword) statement);
    } else if (statement instanceof Blank) {
      value = evaluate((Blank) statement);
    } else if (statement instanceof Array) {
      value = evaluate((Array) statement);
    } else if (statement instanceof Block) {
      value = evaluate((Block) statement);
    } else if (statement instanceof Binary) {
      value = evaluate((Binary) statement);
    } else if (statement instanceof Parameter) {
      value = evaluate((Parameter) statement);
    } else if (statement instanceof Callee) {
      value = evaluate((Callee) statement);
    } else if (statement instanceof CalleeRegistry) {
      value = evaluate((CalleeRegistry) statement);
    } else if (statement instanceof Call) {
      value = evaluate((Call) statement);
    } else if (statement instanceof Loop) {
      value = evaluate((Loop) statement);
    } else if (statement instanceof Get) {
      value = evaluate((Get) statement);
    } else if (statement instanceof Set) {
      value = evaluate((Set) statement);
    } else if (statement instanceof Remove) {
      value = evaluate((Remove) statement);
    } else if (statement instanceof Return) {
      value = evaluate((Return) statement);
    } else if (statement instanceof Injection) {
      value = evaluate((Injection) statement);
    } else if (statement instanceof Value) {
      value = evaluate((Value) statement);
    } else {
      abort("Invalid statement \"" + statement + "\"", statement); 
    }
    return value;
  }

  /**
   * Evaluates literal.
   *
   * @param literal the evaluated literal statement
   * @return the literal value as evaluated result.
   *         Double, Long, String, Boolean, or {@code null}.
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  private @Nullable Object evaluate(Literal literal) throws EvaluateException {
    enterEvaluate(literal);
    Object value = null;
    switch (literal.token.type) {
      case NUMBER:
        try {
          for (char element : FLOAT_ELEMENTS) {
            if (literal.token.lexeme.indexOf(element) != -1) {
              value = Double.valueOf(literal.token.lexeme);
              break;
            }
          }
          if (value == null) {
            value = Long.valueOf(literal.token.lexeme);
          }
        } catch (NumberFormatException e) {
          abort("Invalid number \"" + literal + "\"", literal);
        }
        break;
      case STRING:
        value = literal.token.lexeme;
        break;
      case TRUE:
        value = Boolean.valueOf(true);
        break;
      case FALSE:
        value = Boolean.valueOf(false);
        break;
      default:
        break;
    }
    leaveEvaluate(literal);
    return value;
  }

  /**
   * Evaluates identifier.
   *
   * @param identifier the evaluated identifier statement
   * @return variable's value is returned mostly.
   *         Identifier statement is returned if the given identifier
   *         is Standard method name.
   *         {@code null} if variable is undefined.
   * @throws EvaluateException it is thrown if the unexpected token is
   *                           included.
   */
  private @Nullable Object evaluate(Identifier identifier)
                                                  throws EvaluateException {
    enterEvaluate(identifier);
    boolean isStdMethod = Standard.getMethod(identifier.name.lexeme) != null;

    if (identifier.name.type != Token.Type.IDENTIFIER && !isStdMethod) {
      abort("Cannot evaluate \"" + identifier + "\"", identifier);
    }

    Object value = environment.get((identifier.name.lexeme.equals("."))
                                   ? null
                                   : identifier.name.lexeme);
    if (value instanceof Get) {       // When it is alias,
      value = evaluate((Get) value);  // read original variable's value.
    }
    if (value == Environment.UNDEFINED) {
      if (isStdMethod) {
        value = identifier;
      } else {
        value = null;  // The undefined variable's value is null.
      }
    }
    leaveEvaluate(identifier);
    return value;
  }

  /**
   * Evaluates keyword.
   * This should not be called.
   * Keyword statement is used in Block/Loop statement. Because
   * Keyword statement is "break" or "continue".
   * So This is not used by evaluate method directly.
   *
   * @param keyword the evaluated keyword statement
   * @return {@code null}. However it is not returned because the
   *                       exception always happens.
   * @throws EvaluateException this exception always happen when this
   *                           method is called.
   */
  private @Nullable Object evaluate(Keyword keyword) throws EvaluateException {
    abort("Cannot evaluate \"" + keyword + "\"", keyword);
    return null;  // dummy for avoiding error
  }

  /**
   * Evaluates blank.
   *
   * @param blank the evaluated blank statement
   * @return {@code null} is always returned.
   */
  private @Nullable Object evaluate(Blank blank) {
    return null;
  }

  /**
   * Evaluates array.
   *
   * @param array the evaluated array statement
   * @return an array as evaluated result
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  private ArrayList<@Nullable Object> evaluate(Array array)
                                                  throws EvaluateException {
    enterEvaluate(array);
    if (array.values.size() > maxArraySize) {
      abort("Array size is too large.", array);
    }
    ArrayList<@Nullable Object> values = new ArrayList<>();
    for (Statement statement : array.values) {
      Object value = evaluate(statement);
      values.add(value);
    }
    leaveEvaluate(array);
    return values;
  }

  /**
   * Evaluates block.
   *
   * @param block the evaluated block statement
   * @return {@code HashMap<String, Object>} as the evaluated block is
   *         returned mostly. However various value may be returned by
   *         ":=" and "return()".
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  @Nullable Object evaluate(Block block) throws EvaluateException {
    enterEvaluate(block);

    // printStack();

    Environment previousEnvironment = environment;
    environment = new Environment(previousEnvironment);  // Add scope
    /* When "return" statement is executed later, it should be recorded.
     * The certain variable is need in order to recording.
     * It is created at starting constructor or anonymous/pure function.
     */
    boolean isRecording = (block.type == Block.CLASS_CONSTRUCTOR
                           || block.type == Block.ANONYMOUS_FUNCTION
                           || block.type == Block.PURE_FUNCTION);
    if (isRecording) {
      environment.set(Environment.FUNCTION_RETURN_NAME,
                      Environment.UNDEFINED, true);
    }

    for (Statement value : block.values) {
      String reason = setReasonGoingBack(value);
      if (reason != null) {
        break;
      }

      evaluate(value);

      // Finish if break/continue/return happen in the above evaluation.
      reason = getReasonGoingBack();
      if (reason != null) {
        break;
      }
    }

    Object result = Environment.UNDEFINED;
    if (isRecording) {
      Object value = environment.get(Environment.FUNCTION_RETURN_NAME, true);
      if (value != Environment.FUNCTION_RETURN_NAME) {
        result = value;
      }
    }
    if (result == Environment.UNDEFINED) {
      result = environment.get();
    }

    if (block.type != Block.CLASS_CONSTRUCTOR) {
      environment = previousEnvironment;  // Restore scope
    }
    leaveEvaluate(block);
    return result;
  }

  /**
   * Evaluates binary.
   * For example, 3 + 2 is evaluated.
   * Note that short-circuit evaluation (minimal evaluation) is happen
   * for {@code <and>} or {@code <or>}.
   *
   * @param binary the evaluated binary statement
   * @return the result of binary operation if it is success, {@code null}
   *         otherwise.
   * @throws EvaluateException it is thrown by some reason.
   *                           <ul>
   *                             <li> The operator/operand is invalid.
   *                             <li> Array size reaches limit.
   *                             <li> Divide by 0.
   *                           </ul>
   */
  private @Nullable Object evaluate(Binary binary) throws EvaluateException {
    enterEvaluate(binary);

    Object left = evaluate(binary.left);

    Token.Type operatorType = binary.operator.type;

    Object value = null;
    /* 1. Short-circuit evaluation (minimal evaluation) for <and>
     * 2. Short-circuit evaluation (minimal evaluation) for <or>
     * 3. Other evaluation
     */
    if (operatorType == Token.Type.AND && !isTruthy(left)) {
      value = false;
    } else if (operatorType == Token.Type.OR && isTruthy(left)) {
      value = true;
    } else {
      Object right = evaluate(binary.right);
      value = calculateBinary(operatorType, left, right, binary, config);
    }

    leaveEvaluate(binary);
    return value;
  }

  /**
   * Evaluates parameter.
   * This should not be called.
   * Parameter statement is used in Call statement. Because
   * Parameter statement is "class" or "function".
   * So This is not used by evaluate method directly.
   *
   * @param parameter the evaluated parameter statement
   * @return {@code null}. However it is not returned because the
   *                       exception always happens.
   * @throws EvaluateException this exception always happen when this
   *                           method is called.
   */
  private @Nullable Object evaluate(Parameter parameter) throws EvaluateException {
    abort("Cannot evaluate \"" + parameter + "\"", parameter);
    return null;  // dummy for avoiding warning
  }

  /**
   * Evaluates callee.
   *
   * <p>Callee statement is the definition (entity) of function.
   * For example, "function test() {print(10)}" is represented with
   * Callee statement.
   *
   * <p>This is wrapped with CalleeRegistry statement. And it is stored
   * into Environment in order to register function.
   *
   * @param callee the evaluated callee statement
   * @return {@code null} is always return. It is meaningless.
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  private @Nullable Object evaluate(Callee callee) throws EvaluateException {
    enterEvaluate(callee);
    environment.set(callee.name.name.name.lexeme,
        new CalleeRegistry(callee, null, false));  // definition of function
    leaveEvaluate(callee);
    return null;  // null is meaningless.
  }

  /**
   * Evaluates CalleeRegistry.
   * This should not be called.
   * CalleeRegistry statement is used to register function. This is
   * not used by evaluate method directly.
   *
   * @param calleeRegistry the evaluated CalleeRegistry statement
   * @return {@code null}. However it is not returned because the
   *                       exception always happens.
   * @throws EvaluateException this exception always happen when this
   *                           method is called.
   */
  private @Nullable Object evaluate(CalleeRegistry calleeRegistry)
                                                  throws EvaluateException {
    abort("Cannot evaluate \"" + calleeRegistry + "\"", calleeRegistry);
    return null;  // dummy for avoiding error
  }

  /**
   * Evaluates call.
   * Function call is done.
   *
   * @param call the evaluated call statement
   * @return the returned value of function
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  @Nullable Object evaluate(Call call) throws EvaluateException {
    enterEvaluate(call);

    @Nullable Object result = null;
    boolean isCompleted = false;
    do {
      if (!(call.name instanceof Identifier) && !(call.name instanceof Get)) {
        break;
      }

      try {
        // User defined function
        Object[] results = callFunction(call);
        if (((Boolean) results[IS_DONE])) {
          result = results[RESULT];
          isCompleted = true;
          break;
        }

        // Standard library's method
        @Nullable Object name = call.name;
        Object value = evaluate(call.name);
        if (value instanceof Identifier) {
          name = ((Identifier) value).name.lexeme;
        } else {
          if (name instanceof Get) {
            if (((Get) name).members.size() == 1) {
              name = ((Get) name).members.get(0);
            }
          }
          if (name instanceof Identifier) {
            name = ((Identifier) name).name.lexeme;
          }
        }
        if (!(name instanceof String)) {
          break;
        }
        @Nullable Method method = Standard.getMethod((String) name);
        if (method == null) {
          break;
        }
        result = (@Nullable Object) method.invoke(null, this, call, config);
        isCompleted = true;
      } catch (EvaluateException e) {
        String message = e.getMessage();
        boolean isBaton = !message.isEmpty();
        Statement statementForLocation = null;
        if (!isBaton) {
          message = call + " is failed.";
          statementForLocation = call;
        }
        abort(message, statementForLocation, isBaton);
      } catch (IllegalAccessException e) {
        ;
      } catch (IllegalArgumentException e) {
        ;
      } catch (InvocationTargetException e) {
        ;
      } catch (NullPointerException e) {
        ;
      } catch (ExceptionInInitializerError e) {
        ;
      }
    } while (false);
    if (!isCompleted) {
      abort("Cannot call for " + call, call);
    }

    leaveEvaluate(call);
    return result;
  }

  /**
   * Evaluates loop.
   *
   * @param loop the evaluated loop statement
   * @return the returned value of Loop.
   *         Loop has Block. Block is the function.
   *         The function returns value.
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  @Nullable Object evaluate(Loop loop) throws EvaluateException {
    enterEvaluate(loop);

    // Make sure that initial/continuous condition is given as list.
    if (!(loop.initial instanceof ArrayList)
        || !(loop.continuous instanceof ArrayList)) {
      abort("Loop condition of " + loop.call + " is invalid", loop.call);
    }
    // Make sure that correct iterator is given.
    ArrayList<@Nullable Object> iterator = null;
    if (loop.each != null || loop.iterator != null) {
      if (!(loop.each instanceof Identifier)
          || (!(loop.iterator instanceof ArrayList)
              && !(loop.iterator instanceof HashMap))) {
        abort("Iterator of " + loop.call + " is invalid", loop.call);
      }
      // Deep copy is needed instead of Shallow copy.
      // Shallow copied value may be broken. For example, the value of "list"
      // is broken as below.
      // list=[{"x":3, "y":5}]; for (a in list) {a.x=2} --> [{"x":2, "y":5}]
      // list={"x":{"y":5}}, for (a in list) {a[1]["y"]=1} --> {"x":{"y":1}}
      if (loop.iterator instanceof ArrayList) {
        iterator = copy(getArrayList(loop.iterator));
      } else {
        // HashMap --> ArrayList
        iterator = new ArrayList<>();
        HashMap<String, @Nullable Object> pairs =
                                            copy(getHashMap(loop.iterator));
        Iterator<Map.Entry<String, @Nullable Object>> itr =
                                                pairs.entrySet().iterator();
        while (itr.hasNext()) {
          @SuppressWarnings("null")
          Map.Entry<String, @Nullable Object> entry = itr.next();
          @SuppressWarnings("null")
          String key = entry.getKey();
          @Nullable Object value = entry.getValue();
          ArrayList<@Nullable Object> item = new ArrayList<>();
          item.add(key);
          item.add(value);
          iterator.add(item);
        }
      }
    }

    // Add new environment
    Environment previousEnvironment = environment;
    environment = new Environment(previousEnvironment);  // Add scope
    environment.set(Environment.BREAK_NAME,
                    Environment.UNDEFINED, true);

    boolean isSatisfied = true;
    for (int loopTimes = 0; loopTimes < maxLoopTimes; loopTimes++) {
      ArrayList<Statement> statements = (loopTimes == 0)
                                        ? loop.initial : loop.continuous;
      Object result = null;
      for (Statement statement : statements) {
        result = evaluate(statement);
      }
      isSatisfied = isTruthy(result);
      if (!isSatisfied) {
        break;
      }

      environment.set(Environment.CONTINUE_NAME,
                      Environment.UNDEFINED, true);

      if (iterator != null) {
        if (iterator.size() == 0) {
          isSatisfied = false;
          break;
        }
        @Nullable Object datum = iterator.remove(0);

        if (loop.each instanceof Identifier) {  // for avoiding warning
          Identifier each = loop.each;
          environment.set(each.name.lexeme, datum, true);
        }
      }

      String reason = null;
      for (Statement statement : loop.statements) {
        reason = setReasonGoingBack(statement);
        if (reason != null) {
          break;
        }

        evaluate(statement);

        // Terminate current loop if break/continue/return happen
        // in the above evaluation.
        reason = getReasonGoingBack();
        if (reason != null) {
          break;
        }
      }

      if (reason != null
          && (reason.equals(Environment.BREAK_NAME)
              || reason.equals(Environment.FUNCTION_RETURN_NAME))) {
        isSatisfied = false;
        break;
      }
    }

    if (isSatisfied) {
      abort("Loop times reach maximum (" + maxLoopTimes + ")", loop.call);
    }

    @Nullable Object result = environment.get();

    // Discard current environment
    environment = previousEnvironment;  // Restore scope

    leaveEvaluate(loop);
    return result;
  }

  /**
   * Evaluates get.
   * This (Statement "Get") is familiar with Statement "Identifier".
   * If variable is simple, such as foo, its value is given by
   * evaluating Statement "Identifier" of foo.
   * This (Statement "Get") is used to obtain the value of array and
   * block, such as foo[2] and bar.baz.
   *
   * @param get the evaluated get statement
   * @return variable's value
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  private @Nullable Object evaluate(Get get) throws EvaluateException {
    enterEvaluate(get);

    if (get.members.size() == 0) {
      abort("Cannot get " + get, get);
    }
    @Nullable Object variable = Environment.UNDEFINED;
    for (Statement memberOrg : get.members) {
      if (variable == null) {  // The undefined variable
        break;
      }
      Object member = evaluate(memberOrg);
      if (variable == Environment.UNDEFINED) {
        variable = member;
        continue;
      }
      if (variable instanceof ArrayList || variable instanceof HashMap) {
        if (member instanceof Boolean) {  // Avoid boolean
          abort("Cannot get " + get, get);
        }
        if (member instanceof Double) {
          if (((Double) member) % 1.0 != 0.0) {
            abort("Cannot get " + get, get);
          }
          member = (Long) ((Double) member).longValue();
        }
        // for foo[bar] or foo.bar as array
        if (member instanceof Long && variable instanceof ArrayList) {
          int index = ((Long) member).intValue();
          ArrayList<@Nullable Object> array = getArrayList(variable);
          int length = array.size();
          if (index < 0) {  // Backward access
            index += length;
          }
          if (0 <= index && index < length) {
            variable = array.get(index);
            continue;
          }
          // null because out of range
          variable = null;
          break;
        }
        // for foo[bar] of foo.bar as block
        if (variable instanceof HashMap) {
          if (member instanceof Long) {
            member = String.valueOf(member);
          }
          if (member instanceof String) {
            HashMap<String, @Nullable Object> block = getHashMap(variable);
            if (block.containsKey(member)) {
              variable = block.get(member);
              continue;
            }
          }
          // null because member is not found.
          variable = null;
          break;
        }
      }
      abort("Cannot get " + get, get);
    }

    leaveEvaluate(get);
    return (variable == Environment.UNDEFINED) ? null : variable;
  }

  /**
   * Evaluates set.
   * For assignment, various situations are available as below.
   * <ol>
   *   <li> Simple assignment: x = 3
   *   <li> Assignment for alias:
   *        function test(a) { a = 3, ... } --> _[0] = 3
   *   <li> Assignment for Object's member:
   *         a = {b: [2, 3]}, a.b[1] = 10 --> a = {b: [2, 10]}
   *   <li> Removing: It is done with Remove statement.
   * </ol>
   *
   * <p>"Set" statement has parts of the above variable.
   * For example, x["y"]["z"] is represented as ["x", "y", "z"].
   *
   * <p>Variable may be alias.
   * For example, x is alias of _[0] and x["y"]["z"] is given,
   * _[0]["y"]["z"] is evaluated.
   *
   * @param set the evaluated set statement
   * @return {@code null} is always return. It is meaningless.
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  private @Nullable Object evaluate(Set set) throws EvaluateException {
    do {
      enterEvaluate(set);

      @Nullable Object value = evaluate(set.value);
      // Deep-copy
      if (value instanceof ArrayList) {
        value = copy(getArrayList(value));
      } else if (value instanceof HashMap) {
        value = copy(getHashMap(value));
      }

      // The returned value of ":="
      if (set.operator.type == Token.Type.RESULT) {
        environment.set(null, value);  // null indicates the returned value.
        leaveEvaluate(set);
        return null;  // null is meaningless.
      }

      boolean isLocal = set.operator.type == Token.Type.SET;  // ":" makes local.

      if (set.members.size() == 0) {
        break;  // abort
      }

      // Get the head. For example, "x" of x["y"]["z"]
      @SuppressWarnings("null")
      Statement head = set.members.get(0);
      if (!(head instanceof Identifier)) {
        Object object = evaluate(head);
        if (!(object instanceof Identifier)) {
          break;  // abort
        }
        head = (Identifier) object;
      }

      // Avoid system variable.
      String name = ((Identifier) head).name.lexeme;
      if (name.length() >= 6 && name.substring(0, 3).equals("___")
          && name.substring(name.length() - 3).equals("___")) {
        abort("Cannot assign " + set
              + " because variable's prefix/suffix is \"___\"", set);
      }

      ArrayList<Statement> members;
      Object maybeAlias = environment.get(name);
      if (maybeAlias instanceof Get) {  // for alias
        // For example, maybeAlias = ["_", 1]
        //              "_" is Identifier and 1 is Literal
        if (((Get) maybeAlias).members.size() <= 1) {
          break;  // abort
        }
        @SuppressWarnings("null")
        Statement statement = ((Get) maybeAlias).members.get(0);
        if (!(statement instanceof Identifier)) {
          break;  // abort
        }
        name = ((Identifier) statement).name.lexeme;  // Is is "_".
        // Get the following member. For example, ["y"]["z"] of x["y"]["z"]
        members =  new ArrayList<>(set.members);
        members.remove(0);
        // Conjoin.
        // For example, ["_", 1, "y", "z"]
        int index = 0;
        for (Statement member : ((Get) maybeAlias).members) {
          members.add(index, member);
          index++;
        }
      } else {  // except alias
        if (set.members.size() == 1) {  // for simple variable, such as i, num.
          for (Token.Type[] types : ASSIGN_OPERATORS) {
            if (types[0] == set.operator.type) {
              Object originalValue = environment.get(name, isLocal);
              value = calculateBinary(types[1], originalValue, value, set,
                                      config);
              break;
            }
          }
          if (set.operator.type == Token.Type.REMOVE) {
            environment.remove(name);
          } else {
            if (value instanceof CalleeRegistry) {
              CalleeRegistry calleeRegistry = (CalleeRegistry) value;
              value = new CalleeRegistry(calleeRegistry.callee,
                                         calleeRegistry.environment,
                                         true);  // Reference
            }
            environment.set(name, value, isLocal);
          }
          leaveEvaluate(set);
          return null;  // null is meaningless.
        }
        members =  new ArrayList<>(set.members);
      }
      members.set(0, new Literal(new Token(Token.Type.STRING, name)));
      HashMap<String, @Nullable Object> variable = new HashMap<>();
      variable.put(name, environment.get(name, isLocal));
      setMember(variable, members, value, set.operator.type);
      environment.set(name, variable.get(name), isLocal);

      leaveEvaluate(set);
      return null;  // null is meaningless.
    } while (false);

    abort("Cannot assign " + set, set);
    return null;  // dummy for avoiding error
  }

  /**
   * Evaluates remove.
   *
   * @param remove the evaluated remove statement
   * @return {@code null} is always return. It is meaningless.
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  private @Nullable Object evaluate(Remove remove) throws EvaluateException {
    enterEvaluate(remove);

    Set set = new Set(remove.members, remove.token,
                      new Literal(new Token(Token.Type.NULL)));
    try {
      evaluate(set);
    } catch (EvaluateException e) {
      String message = e.getMessage();
      boolean isBaton = !message.isEmpty();
      Statement statementForLocation = null;
      if (!isBaton) {
        message = remove + " is failed.";
        statementForLocation = remove;
      } else {
        message = message.replace("assign", "remove");
      }
      abort(message, statementForLocation, isBaton);
    }

    leaveEvaluate(remove);
    return null;  // null is meaningless.
  }

  /**
   * Evaluates Return.
   * This should not be called.
   * Return statement is used in Block/Loop statement. This is
   * not used by evaluate method directly.
   *
   * @param r the evaluated Return statement
   * @return {@code null}. However it is not returned because the
   *                       exception always happens.
   * @throws EvaluateException this exception always happen when this
   *                           method is called.
   */
  private @Nullable Object evaluate(Return r) throws EvaluateException {
    abort("Cannot evaluate \"" + r + "\"", r);
    return null;  // dummy for avoiding error
  }

  /**
   * Evaluates injection.
   * The following values are accepted.
   * <ul>
   *   <li> {@code null}
   *   <li> Boolean
   *   <li> Number (Byte, Short, Integer, Long, Float, Double)
   *   <li> String
   *   <li> {@code ArrayList<@Nullable Object> }
   *   <li> {@code HashMap<String, @Nullable Object> }
   * </ul>
   *
   * @param injection the evaluated injection statement
   * @return {@code null} is always return. It is meaningless.
   * @throws EvaluateException it is thrown if the prefix/suffix of
   *                           variable name is "___" or value is
   *                           illegal.
   */
  @Nullable Object evaluate(Injection injection) throws EvaluateException {
    enterEvaluate(injection);

    @Nullable Object value = getNormalizedValue(injection, config);

    int length = injection.variable.length();
    if (length >= 6
        && injection.variable.substring(0, 3).equals("___")
        && injection.variable.substring(length - 3).equals("___")) {
      abort("Cannot use a value of \"" + injection
            + "\" because variable's prefix/suffix is '___'", injection);
    }

    environment.set(injection.variable, value);

    leaveEvaluate(injection);
    return null;  // null is meaningless.
  }

  /**
   * Evaluates value.
   * The following values are accepted.
   * <ul>
   *   <li> {@code null}
   *   <li> Boolean
   *   <li> Number (Byte, Short, Integer, Long, Float, Double)
   *   <li> String
   *   <li> {@code ArrayList<@Nullable Object> }
   *   <li> {@code HashMap<String, @Nullable Object> }
   * </ul>
   *
   * @param value the evaluated value statement
   * @return the evaluated value
   * @throws EvaluateException it is thrown if value is illegal.
   */
  private @Nullable Object evaluate(Value value) throws EvaluateException {
    enterEvaluate(value);
    @Nullable Object object = getNormalizedValue(value, config);
    leaveEvaluate(value);
    return object;
  }

  /**
   * Records to enter new evaluation.
   *
   * @param statement the evaluated statement.
   *                  {@code null} if there is no particular statement.
   * @throws EvaluateException it is thrown if the recursive call times
   *                           reach limit.
   */
  private void enterEvaluate(@Nullable Statement statement)
                                                throws EvaluateException {
    if (stack.size() >= maxDepth) {
      abort("Recursive call times reach maximum (" + maxDepth + ").");
    }
    String[] evaluated = new String[2];
    evaluated[NAME] = (statement == null)
                      ? "----"
                      : ((statement instanceof Block)
                         ? ((Block) statement).type
                         : statement.getClass().getSimpleName());
    evaluated[DETAIL] = (statement == null) ? "" : statement.toString();
    stack.push(evaluated);
  }

  /**
   * Records to leave current evaluation.
   *
   * @param statement the evaluated statement.
   *                  {@code null} if there is no particular statement.
   * @throws EvaluateException it is thrown if the order of
   *                           evaluating/leaving is invalid
   *                           or recorded data is invalid.
   */
  private void leaveEvaluate(@Nullable Statement statement)
                                                throws EvaluateException {
    String[] evaluated = null;
    try {
      evaluated = stack.pop();
    } catch (EmptyStackException e) {
      ;
    }
    String name = (statement == null)
                  ? "----"
                  : ((statement instanceof Block)
                     ? ((Block) statement).type
                     : statement.getClass().getSimpleName());
    if (evaluated == null || evaluated.length != 2
        || !evaluated[NAME].equals(name)) {
      abort("Interpreter side error in Evaluator#leaveEvaluate");
    }
  }

  /**
   * Prints stack.
   *
   * @throws EvaluateException it is thrown if recorded data is invalid.
   */
  @SuppressWarnings("unused")
  private void printStack() throws EvaluateException {
    for (int i = 0; i < stack.size(); i++) {
      String[] evaluated = stack.get(i);
      if (evaluated.length != 2) {
        abort("Interpreter side error in Evaluator#printStack");
      }
      StringBuffer sb = new StringBuffer();
      sb.append("> ");
      sb.append(i);
      sb.append(": ");
      sb.append(evaluated[NAME]);
      sb.append(" (");
      sb.append(evaluated[DETAIL]);
      sb.append(")");
      System.out.println(sb.toString());
    }
  }

  /**
   * Gets reason of going back until function/loop statement.
   *
   * @return Reason if going back is needed, {@code null} otherwise.
   *         Reason is which one:
   *         Environment.BREAK_NAME,
   *         Environment.CONTINUE_NAME,
   *         Environment.FUNCTION_RETURN_NAME .
   */
  private @Nullable String getReasonGoingBack() {
    for (String name : FUNC_LOOP_CTRL_KEYWORD_NAMES) {
      Object value = environment.get(name);
      if (value != Environment.UNDEFINED) {
        return name;
      }
    }
    return null;
  }

  /**
   * Sets reason of going back until function/loop statement.
   *
   * @param statement Statement that may be return/break/continue.
   * @return Reason if going back is needed, {@code null} otherwise.
   *         Reason is which one:
   *         Environment.BREAK_NAME,
   *         Environment.CONTINUE_NAME,
   *         Environment.FUNCTION_RETURN_NAME .
   * @throws EvaluateException it is thrown if the returned value is
   *                           invalid or break/continue is used
   *                           without loop.
   */
  private @Nullable String setReasonGoingBack(Statement statement)
                                                  throws EvaluateException {
    // for "return"
    if (statement instanceof Return) {
      String reason = Environment.FUNCTION_RETURN_NAME;
      Object value = reason;
      @Nullable Statement returnedValue = ((Return) statement).value;
      if (returnedValue != null) {
        // When function object is returned, the persistent
        // environment is given for closure.
        value = evaluate(returnedValue);
        if (value instanceof CalleeRegistry) {
          value = new CalleeRegistry(((CalleeRegistry) value).callee,
                                     environment,  // Closure
                                     true);  // Reference
        }
      }
      environment.set(reason, value);
      return reason;
    }

    // for "break"/"continue"
    String reason = null;
    if (statement instanceof Keyword) {
      if (((Keyword) statement).token.type == Token.Type.BREAK) {
        reason = Environment.BREAK_NAME;
      } else if (((Keyword) statement).token.type == Token.Type.CONTINUE) {
        reason = Environment.CONTINUE_NAME;
      }
    }
    if (reason == null) {
      return null;
    }
    // "break"/"continue" is allowed for Loop.
    // So we find Loop. However function is found, finding is terminated.
    boolean isFound = false;
    int index = stack.size();
    while (--index >= 0) {
      String[] evaluated = stack.get(index);
      if (evaluated.length != 2) {
        break;
      }
      if (evaluated[NAME].equals("Loop")) {
        isFound = true;
        break;
      }
      if (evaluated[NAME] == Block.CLASS_CONSTRUCTOR
          || evaluated[NAME] == Block.ANONYMOUS_FUNCTION
          || evaluated[NAME] == Block.PURE_FUNCTION) {
        break;
      }
    }
    if (!isFound) {
      abort(((Keyword) statement).token.lexeme + " is found without loop",
            statement);
    }
    environment.set(reason, true);
    return reason;
  }

  /**
   * Call a function.
   *
   * @param call the information that contains function name,
   *             arguments, and so on.
   * @return an array that has 2 objects.
   *         1st object as Object is the result of function.
   *         2nd object as Boolean indicates whether processing is
   *         done or not.
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  private @Nullable Object[] callFunction(Call call) throws EvaluateException {
    // Get function entity.
    Object statement = call.name;
    if (statement instanceof Identifier) {
      statement = this.environment.get(((Identifier) statement).name.lexeme);
    }
    if (statement instanceof Get) {           // for alias of argument or
      statement = evaluate((Get) statement);  // variable that is assigned
    }                                         // function
    if (statement instanceof Block) {  // for anonymous function
      return new Object[] {evaluate((Block) statement), true};
    }
    if (!(statement instanceof CalleeRegistry)) {
      return new Object[] {null, false};
    }
    Callee callee = ((CalleeRegistry) statement).callee;

    // Check its modifier.
    Token modifier = callee.name.modifier;
    if (modifier == null || modifier.type != Token.Type.FUNCTION) {
      return new Object[] {null, false};
    }

    // Get parameter's definition and function's statements of Callee.
    // They are contained within an Array.
    // For example, there are 2 parameters: This array is
    // [1st parameter, 2nd parameter, A block of statements].
    int parameterLength = callee.parameters.values.size();
    if (parameterLength < 1) {  // One parameter of block is needed at least
      throw new EvaluateException();
    }
    @Nullable Statement statements =
                  callee.parameters.values.get(parameterLength - 1);  // Block
    if (!(statements instanceof Block)) {
      throw new EvaluateException();
    }
    parameterLength--;  // Number of actual parameters except block

    // Add scope for pure function.
    // Reuse environment for closure.
    Environment environment = ((CalleeRegistry) statement).environment;
    Environment previousEnvironment = this.environment;
    this.environment = (environment != null)
                       ? environment
                       : new Environment(previousEnvironment);

    // Make implicit variable from arguments.
    ArrayList<@Nullable Object> implicit = new ArrayList<>();
    for (int i = 0; i < call.arguments.values.size(); i++) {
      boolean isFunction = false;
      boolean isReference = false;
      if (i < callee.parameters.values.size()) {
        @Nullable Statement parameter = callee.parameters.values.get(i);
        if (parameter instanceof Parameter) {
          modifier = ((Parameter) parameter).modifier;
          if (modifier != null) {
            isFunction = modifier.type == Token.Type.FUNCTION;
            isReference = modifier.type == Token.Type.REFERENCE;
          }
        }
      }
      @Nullable Statement argument = call.arguments.values.get(i);
      Object value = argument;
      if (!isFunction) {
        value = evaluate(argument);
      }
      if (!isReference) {
        if (value instanceof ArrayList) {
          value = copy(getArrayList(value));  // Deep-copy
        } else if (value instanceof HashMap) {
          value = copy(getHashMap(value));  // Deep-copy
        }
      }
      implicit.add(value);
    }
    this.environment.set("_", implicit, true);

    // Set parameter as alias of implicit variable.
    for (int i = 0; i < parameterLength; i++) {
      @SuppressWarnings("null")
      Statement parameter = callee.parameters.values.get(i);
      if (!(parameter instanceof Parameter)) {
        throw new EvaluateException();
      }
      Token name = ((Parameter) parameter).name.name;
      if (name.type != Token.Type.IDENTIFIER) {
        throw new EvaluateException();
      }
      @Nullable Object value = null;
      if (i < implicit.size()) {
        ArrayList<Statement> members = new ArrayList<>();
        members.add(new Identifier(new Token(Token.Type.IDENTIFIER, "_")));
        members.add(new Literal(new Token(Token.Type.NUMBER, String.valueOf(i))));
        value = new Get(members);
      }
      this.environment.set(name.lexeme, value, true);
    }

    // Evaluate
    Object result = evaluate(statements);

    this.environment = previousEnvironment;  // Restore scope

    return new Object[] {result, true};
  }

  /**
   * Sets/Modifies/Removes a member within variable.
   * <pre>
   * For example, a.c.1 = 20 is set in
   *              {"a": {"b": true, "c": [false, 100]}} .
   *
   * 1. Caller has members and value.
   *    - Members: ["a", "c", 1]
   *    - Value: 20
   *
   * 2. Caller gets original whole value of "a" from Environment.
   *    - Original whole value: {"b": true, "c": [false, 100]}
   *
   * 3. Caller makes block that corresponds to members ["a","c",1].
   *    That is, it is made that key-value pairs of "a" and its value.
   *    - Block: {"a": {"b": true, "c": [false, 100]}}
   *
   * 4. Caller call this method.
   *    - target: {"a": {"b": true, "c": [false, 100]}}
   *
   *    This method is called as below.
   *    1st call is done by the above 4th step.
   *    2nd-4th call is done itself recursively.
   *    | #   | target                                | members       |
   *    |-----|---------------------------------------|---------------|
   *    | 1st | {"a": {"b": true, "c": [false, 100]}} | ["a", "c", 1] |
   *    | 2nd | {"b": true, "c": [false, 100]}        | ["c", 1]      |
   *    | 3rd | [false, 100]                          | [1]           |
   *    | 4th | 100                                   | []            |
   *
   *    | #   | Returned value                       |
   *    |-----|--------------------------------------|
   *    | 4th | 20                                   |
   *    | 3rd | [false, 20]                          |
   *    | 2nd | {"b": true, "c": [false, 20]}        |
   *    | 1st | {"a": {"b": true, "c": [false, 20]}} |
   *
   * 5. Caller gets the new value of "a" from the returned value:
   *    {"b": true, "c": [false, 20]}
   *
   * 6. Caller put it into Environment.
   * </pre>
   *
   * @param target a target value.
   *               When this method is called from outside,
   *               it must be {@code HashMap<Object, @Nullable Object>}.
   *               When this method is called from itself recursively,
   *               it is {@code HashMap<String, @Nullable Object}
   *               or {@code ArrayList<@Nullable Object>} except last
   *               call.
   *               It must be {@code @Nullable Object} of the latest
   *               member at last call.
   *               {@code null} is not expected. EvaluateException may
   *               be thrown if {@code null} is given.
   * @param members members of the above target
   * @param value the latest member's value
   * @param operator the operator's token type
   * @return the modified value
   * @throws EvaluateException it is thrown if evaluation is failed.
   */
  private @Nullable Object setMember(@Nullable Object target,
                                     ArrayList<Statement> members,
                                     @Nullable Object value,
                                     Token.Type operator)
                                     throws EvaluateException {
    enterEvaluate(null);

    if ((!(target instanceof HashMap) && !(target instanceof ArrayList))
        || members.size() == 0) {
      if (members.size() > 0) {
        @SuppressWarnings("null")
        Statement member = members.get(0);
        abort("Cannot assign for \"" + member + "\"", member);
      }
      for (Token.Type[] types : ASSIGN_OPERATORS) {
        if (types[0] == operator) {
          // Note that the argument "statement" is not given.
          // So error message can not be obtained even if error occurs.
          // When error occurs, EvaluateException is happen without
          // error message from the following function.
          // This is resolved in the previous recursive caller.
          value = calculateBinary(types[1], target, value, null, config);
          break;
        }
      }
      if (operator == Token.Type.REMOVE) {
        value = Environment.UNDEFINED;  // #1
      }
      if (value instanceof CalleeRegistry) {
        CalleeRegistry calleeRegistry = (CalleeRegistry) value;
        value = new CalleeRegistry(calleeRegistry.callee,
                                   calleeRegistry.environment,
                                   true);  // Reference
      }
      leaveEvaluate(null);
      return value;
    }

    @SuppressWarnings("null")
    Statement member = members.remove(0);
    @Nullable Object evaluatedMember = evaluate(member);

    if (evaluatedMember instanceof Boolean) {  //  Avoid boolean
      abort("Boolean \"" + member + " is not suitable", member);
    }

    if (evaluatedMember instanceof Double) {
      if ((Double) evaluatedMember % 1.0 != 0.0) {
        abort("Floating-point number \"" + member + "\" is invalid", member);
      }
      evaluatedMember = (Long) ((Double) evaluatedMember).longValue();
    }
    if (evaluatedMember instanceof Long && target instanceof ArrayList) {
      int index = ((Long) evaluatedMember).intValue();
      ArrayList<@Nullable Object> array = getArrayList(target);
      int length = array.size();
      if (index < 0) {  // Backward access
        index += length;
      }
      if (index < 0 || index >= length) {
        abort("Index \"" + index + "\" is out of range", member);
      }
      try {
        value = setMember(array.get(index), members, value, operator);
      } catch (EvaluateException e) {
        abort("Cannot evaluate \"" + member + "\"", member);
      }
      if (value == Environment.UNDEFINED) {  // #1
        array.remove(index);
      } else {
        array.set(index, value);
      }
      leaveEvaluate(null);
      return array;
    }

    if (!(target instanceof HashMap)) {
      abort("Invalid access for " + member, member);
    }
    if (evaluatedMember instanceof Long) {
      evaluatedMember = ((Long) evaluatedMember).toString();
    }
    if (!(evaluatedMember instanceof String)) {
      abort("Cannot assign for \"" + member + "\"", member);
    }
    String name = (String) evaluatedMember;
    HashMap<String, @Nullable Object> block = getHashMap(target);
    if (!(block.containsKey(name))) {
      if (members.size() > 0) {
        @SuppressWarnings("null")
        Statement memberTop = members.get(0);
        abort("Cannot assign for \"" + memberTop + "\"", memberTop);
      }
      if (operator != Token.Type.REMOVE
          && name != null /* for avoiding warning */) {
        block.put(name, value);
      }
    } else {
      try {
        value = setMember(block.get(name), members, value, operator);
      } catch (EvaluateException e) {
        abort("Cannot evaluate \"" + member + "\"", member);
      }
      if (value == Environment.UNDEFINED) {  // #1
        block.remove(name);
      } else if (name != null /* for avoiding warning */) {
        block.put(name, value);
      }
    }
    leaveEvaluate(null);
    return target;
  }

  /**
   * Aborts Evaluator.
   * Print an error message and throw Exception.
   *
   * @param message error message
   * @throws EvaluateException this exception always happen when this
   *                           method is called.
   */
  private void abort(String message) throws EvaluateException {
    abort(message, null, false);
  }

  /**
   * Aborts Evaluator.
   * Print an error message and throw Exception.
   *
   * @param message error message
   * @param statement statement where error occurs. It is used to get
   *                  location. {@code null} is given when cause is not
   *                  statement.
   * @throws EvaluateException this exception always happen when this
   *                           method is called.
   */
  private void abort(String message, @Nullable Statement statement)
                                                throws EvaluateException {
    abort(message, statement, false);
  }

  /**
   * Aborts Evaluator.
   * Print an error message and throw Exception.
   *
   * @param message error message
   * @param statement statement where error occurs. It is used to get
   *                  location. {@code null} is given when cause is not
   *                  statement.
   * @param eliminateTag eliminate Tag or not.
   *                     true if tag is not needed, false otherwise.
   * @throws EvaluateException this exception always happen when this
   *                           method is called.
   */
  private void abort(String message, @Nullable Statement statement,
                     boolean eliminateTag) throws EvaluateException {
    abort(message, statement, config, eliminateTag);
  }

  /**
   * Aborts Evaluator.
   * Print an error message and throw Exception.
   *
   * @param message error message
   * @param statement statement where error occurs. It is used to get
   *                  location. {@code null} is given when cause is not
   *                  statement.
   * @param config configuration if it is needed, {@code null} otherwise
   * @param eliminateTag eliminate Tag or not.
   *                     true if tag is not needed, false otherwise.
   * @throws EvaluateException this exception always happen when this
   *                           method is called.
   */
  private static void abort(String message, @Nullable Statement statement,
                            @Nullable Config config, boolean eliminateTag)
                            throws EvaluateException {
    throw EvaluateException.makeEvaluateException(message, statement, config,
                                                  eliminateTag);
  }

  /**
   * Adds.
   * <pre>{@code
   *   | left \\ right | block | array | string | int     | float   | boolean | null    |
   *   |---------------|-------|-------|--------|---------|---------|---------|---------|
   *   | block         | block | array | ERROR  | ERROR   | ERROR   | ERROR   | block   |
   *   | array         | array | array | array  | array   | array   | array   | array   |
   *   | string        | ERROR | array | string | string  | string  | string  | string  |
   *   | int           | ERROR | array | string | int     | float   | boolean | int     |
   *   | float         | ERROR | array | string | float   | float   | boolean | float   |
   *   | boolean       | ERROR | array | string | boolean | boolean | boolean | boolean |
   *   | null          | block | array | string | int     | float   | boolean | null    |
   *
   *   For example, {"a": 3, "b": 100} + {"0": "x"}
   *                                  --> {"a": 3, "b": 100, "0": "x"}
   *                {"a": 3, "b": 100} + {"a": -5}
   *                                  --> {"a": -2, "b": 100}
   *                {"a": 3} + [3, "a"] --> [{"a": 3}, 3, "a"]
   *                [3, "a"] + {"a": 3} --> [3, "a", {"a": 3}]
   *                {"a": 3} + "a" --> ERROR
   *                [3, "a"] + true --> [3, "a", true]
   *                true + [3, "a"] --> [true, 3, "a"]
   *                [3, "a"] + [true, false] --> [3, "a", [true, false]]
   *                3 + null --> 3
   *                3 + "a" --> "3a"
   *                3 + false --> true  # 3 is equivalent to true.
   *                            # Then true + false --> true or false.
   *                3 + 2 --> 5
   * }</pre>
   *
   * @param left left-hand sided value
   * @param right right-hand sided value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return the evaluated result
   * @throws EvaluateException it is thrown if the operator/operand is
   *                           invalid or array/block size reaches
   *                           limit.
   */
  private static @Nullable Object add(@Nullable Object left,
                                      @Nullable Object right,
                                      @Nullable Config config)
                                      throws EvaluateException {
    int maxArraySize = Config.getMaxArraySize(config);

    // Result is array.
    boolean isLeftArray = (left instanceof ArrayList);
    boolean isRightArray = (right instanceof ArrayList);
    if (isLeftArray) {
      ArrayList<@Nullable Object> leftArray = getArrayList(left);
      if (leftArray.size() + 1 > maxArraySize) {
        throw new EvaluateException();
      }
      ArrayList<@Nullable Object> newArray = new ArrayList<>(leftArray);
      newArray.add(right);
      return newArray;
    }
    if (isRightArray) {
      ArrayList<@Nullable Object> rightArray = getArrayList(right);
      if (1 + rightArray.size() > maxArraySize) {
        throw new EvaluateException();
      }
      ArrayList<@Nullable Object> newArray = new ArrayList<>(rightArray);
      newArray.add(0, left);
      return newArray;
    }

    // Result is another operand.
    if (left == null) {
      return right;
    }
    if (right == null) {
      return left;
    }

    // Result is block.
    boolean isLeftBlock = (left instanceof HashMap);
    boolean isRightBlock = (right instanceof HashMap);
    if (isLeftBlock && isRightBlock) {
      HashMap<String, @Nullable Object> leftBlock = getHashMap(left);
      HashMap<String, @Nullable Object> rightBlock = getHashMap(right);
      HashMap<String, @Nullable Object> newBlock = new HashMap<>(leftBlock);
      int length = newBlock.size();
      Iterator<Map.Entry<String, @Nullable Object>> iterator =
                                          rightBlock.entrySet().iterator();
      while (iterator.hasNext()) {
        @SuppressWarnings("null")
        Map.Entry<String, @Nullable Object> entry = iterator.next();
        @SuppressWarnings("null")
        String key = entry.getKey();
        @Nullable Object value = entry.getValue();
        if (newBlock.containsKey(key)) {
          newBlock.put(key, add(newBlock.get(key), value, config));
        } else {
          length++;
          if (length > maxArraySize) {
            throw new EvaluateException();
          }
          newBlock.put(key, value);
        }
      }
      return newBlock;
    }
    if (isLeftBlock || isRightBlock) {
      throw new EvaluateException();
    }

    // Result is string.
    if (left instanceof String) {
      return left + makeString(right, config);
    }
    if (right instanceof String) {
      return makeString(left, config) + right;
    }

    // Result is boolean.
    if (left instanceof Boolean || right instanceof Boolean) {
      return isTruthy(left) || isTruthy(right);
    }

    // Result is int (Long) or float (Double).
    if (isNumbers(left, right)) {
      if (left instanceof Double || right instanceof Double) {
        return (Double) (((Number) left).doubleValue()
                         + ((Number) right).doubleValue());
      }
      return (Long) (((Number) left).longValue()
                     + ((Number) right).longValue());
    }

    throw new EvaluateException();
  }

  /**
   * Subtracts.
   * <pre>{@code
   *   | left \\ right | block | array  | string | int   | float | boolean | null    |
   *   |---------------|-------|--------|--------|-------|-------|---------|---------|
   *   | block         | block | #      | block  | ERROR | ERROR | ERROR   | block   |
   *   | array         | array | array  | array  | array | array | array   | array   |
   *   | string        | ERROR | *      | string | ERROR | ERROR | ERROR   | string  |
   *   | int           | ERROR | ERROR  | ERROR  | int   | float | ERROR   | int     |
   *   | float         | ERROR | ERROR  | ERROR  | float | float | ERROR   | float   |
   *   | boolean       | ERROR | ERROR  | ERROR  | ERROR | ERROR | ERROR   | boolean |
   *   | null          | ERROR | ERROR  | ERROR  | ERROR | ERROR | ERROR   | null    |
   *
   *   # is block or ERROR
   *   * is string or ERROR
   *
   *   For example, {"a": 10, "b": 20, "c": 30} - {"b": 5, "c": 10}
   *                               --> {"a": 10, "b": 15, "c": 20}
   *                {"a": 10, "b": 20} - {"b": 5, "c": 10}
   *                               --> {"a": 10, "b": 15, "c": -10}
   *                {"a": 10, "b": 20, "c": 30} - ["b", "c"]
   *                               --> {"a": 10}
   *                {"a": 10, "b": 20, "c": 30} - ["b", "c", 3]
   *                               --> ERROR because there is number in array
   *                {"a": 10, "b": 20, "c": 30} - "b"
   *                               --> {"a": 10, "c": 30}
   *                {"a": 10, "b": 20, "c": 30} - "d"
   *                               --> {"a": 10, "b": 20, "c": 30}
   *                [3, "a", "a"] - "a" --> [3]
   *                [3, [100, true]] - [100, true] --> [3]
   *                [3, {"a": null}] - {"a": null} --> [3]
   *                [3, false, "a"] - true --> [3, false, "a"]
   *                "large-dog&small-dog&2cat" - "dog"
   *                               --> "large-&small-&2cat"
   *                "large-dog&small-dog&2cat" - ["large-", "small-"]
   *                               --> "dog&dog&2cat"
   *                "large-dog&small-dog&2cat" - 2
   *                               --> ERROR
   *                "large-dog&small-dog&2cat" - ["large-", 2]
   *                               --> ERROR
   *                {"a": null} - null --> {"a": null}
   *                100 - null --> 100
   *                true - null --> true
   *                null - "a" --> ERROR
   * }</pre>
   *
   * @param left left-hand sided value
   * @param right right-hand sided value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return the evaluated result
   * @throws EvaluateException it is thrown if the operator/operand is
   *                           invalid or array/block size reaches
   *                           limit.
   */
  private static @Nullable Object subtract(@Nullable Object left,
                                           @Nullable Object right,
                                           @Nullable Config config)
                                           throws EvaluateException {
    if (left == null && right != null) {
      throw new EvaluateException();
    }

    // Result is array.
    boolean isLeftArray = (left instanceof ArrayList);
    boolean isRightArray = (right instanceof ArrayList);
    if (isLeftArray) {
      ArrayList<@Nullable Object> leftArray = getArrayList(left);
      ArrayList<@Nullable Object> newArray = new ArrayList<>(leftArray);
      removeElementFromArray(newArray, right);
      return newArray;
    }

    // Result is another operand.
    if (right == null) {
      return left;
    }

    // Result is block.
    boolean isLeftBlock = (left instanceof HashMap);
    boolean isRightBlock = (right instanceof HashMap);
    if (isLeftBlock) {
      if (isRightBlock) {
        int maxArraySize = Config.getMaxArraySize(config);
        HashMap<String, @Nullable Object> leftBlock = getHashMap(left);
        HashMap<String, @Nullable Object> rightBlock = getHashMap(right);
        HashMap<String, @Nullable Object> newBlock = new HashMap<>(leftBlock);
        int length = newBlock.size();
        Iterator<Map.Entry<String, @Nullable Object>> iterator =
                                            rightBlock.entrySet().iterator();
        while (iterator.hasNext()) {
          @SuppressWarnings("null")
          Map.Entry<String, @Nullable Object> entry = iterator.next();
          @SuppressWarnings("null")
          String key = entry.getKey();
          @Nullable Object value = entry.getValue();
          if (newBlock.containsKey(key)) {
            newBlock.put(key, subtract(newBlock.get(key), value, config));
          } else {
            length++;
            if (length > maxArraySize) {
              throw new EvaluateException();
            }
            newBlock.put(key, multiply(-1, value, config));
          }
        }
        return newBlock;
      }
      if (isRightArray) {
        HashMap<String, @Nullable Object> leftBlock = getHashMap(left);
        HashMap<String, @Nullable Object> newBlock = new HashMap<>(leftBlock);
        ArrayList<@Nullable Object> rightArray = getArrayList(right);
        for (@Nullable Object name : rightArray) {
          if (!(name instanceof String)) {
            throw new EvaluateException();
          } 
          if (newBlock.containsKey(name)) {
            newBlock.remove(name);
          }
        }
        return newBlock;
      }
      if (right instanceof String) {
        HashMap<String, @Nullable Object> leftBlock = getHashMap(left);
        if (!leftBlock.containsKey(right)) {
          return leftBlock;
        }
        HashMap<String, @Nullable Object> newBlock = new HashMap<>(leftBlock);
        newBlock.remove(right);
        return newBlock;
      }
    } else if (isRightBlock) {
      throw new EvaluateException();
    }

    // Result is string.
    if (left instanceof String) {
      String text = (String) left;
      ArrayList<@Nullable Object> elements;
      if (isRightArray) {
        elements = getArrayList(right);
      } else {
        elements = new ArrayList<>();
        elements.add(right);
      }
      for (@Nullable Object element : elements) {
        if (!(element instanceof String)) {
          throw new EvaluateException();
        }
        text = text.replace((String) element, "");
      }
      return text;
    }

    // Result is int (Long) or float (Double).
    if (left != null) {  // for avoiding warning
      if (isNumbers(left, right)) {
        if (left instanceof Double || right instanceof Double) {
          return (Double) (((Number) left).doubleValue()
                           - ((Number) right).doubleValue());
        }
        return (Long) (((Number) left).longValue()
                       - ((Number) right).longValue());
      }
    }

    throw new EvaluateException();
  }

  /**
   * Multiplies.
   * <pre>{@code
   *   | left \\ right | block | array  | string | int    | float  | boolean | null |
   *   |---------------|-------|--------|--------|--------|--------|---------|------|
   *   | block         | block | ERROR  | ERROR  | ERROR  | ERROR  | ERROR   | null |
   *   | array         | ERROR | ERROR  | string | array  | array  | ERROR   | null |
   *   | string        | ERROR | string | ERROR  | string | string | ERROR   | null |
   *   | int           | ERROR | array  | string | int    | float  | ERROR   | null |
   *   | float         | ERROR | array  | string | float  | float  | ERROR   | null |
   *   | boolean       | ERROR | ERROR  | ERROR  | ERROR  | ERROR  | ERROR   | null |
   *   | null          | null  | null   | null   | null   | null   | null    | null |
   *
   *   For example, {"a": 2} * {"a": 10, "b": 3} --> {"a": 20, "b": null}
   *                {"a": 2} * {"b": 3} --> {"a": null, "b": null}
   *                [3, "a"] * 2 --> [3, "a", 3, "a"]
   *                [3, "a"] * "|" --> '3|a'
   *                [3, "a"] * true -> ERROR
   *                false * [3, "a"] --> ERROR
   *                "3a" * 2  --> "3a3a"
   *                100 * null --> null
   *                [3, "a"] * [1, 2] --> ERROR
   * }</pre>
   *
   * @param left left-hand sided value
   * @param right right-hand sided value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return the evaluated result
   * @throws EvaluateException it is thrown if the operator/operand is
   *                           invalid or array/block size reaches
   *                           limit.
   */
  private static @Nullable Object multiply(@Nullable Object left,
                                           @Nullable Object right,
                                           @Nullable Config config)
                                           throws EvaluateException {
    // Result is null.
    if (left == null || right == null) {
      return null;
    }

    // For boolean
    if (left instanceof Boolean || right instanceof Boolean) {
      throw new EvaluateException();
    }

    // Result is block.
    int maxArraySize = Config.getMaxArraySize(config);
    if (left instanceof HashMap && right instanceof HashMap) {
      HashMap<String, @Nullable Object> leftBlock = getHashMap(left);
      HashMap<String, @Nullable Object> rightBlock = getHashMap(right);
      HashMap<String, @Nullable Object> newBlock = new HashMap<>();
      ArrayList<String> either = new ArrayList<>();
      Iterator<String> iterator = leftBlock.keySet().iterator();
      while (iterator.hasNext()) {
        @SuppressWarnings("null")
        String key = iterator.next();
        if (!rightBlock.containsKey(key)) {
          either.add(key);
          continue;
        }
        newBlock.put(key,
                  multiply(leftBlock.get(key), rightBlock.get(key), config));
      }
      iterator = rightBlock.keySet().iterator();
      while (iterator.hasNext()) {
        @SuppressWarnings("null")
        String key = iterator.next();
        if (!leftBlock.containsKey(key)) {
          either.add(key);
        }
      }
      if (newBlock.size() + either.size() > maxArraySize) {
        throw new EvaluateException();
      }
      for (String key : either) {
        newBlock.put(key, null);
      }
      return newBlock;
    }

    // Result is string x n.
    if (left instanceof String && isNumber(right)) {
      int number = ((Number) right).intValue();
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < number; i++) {
        sb.append((String) left);
      }
      return sb.toString();
    }
    if (right instanceof String && isNumber(left)) {
      int number = ((Number) left).intValue();
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < number; i++) {
        sb.append((String) right);
      }
      return sb.toString();
    }

    // Result is array (elements x n).
    if (left instanceof ArrayList && isNumber(right)) {
      ArrayList<@Nullable Object> leftArray = getArrayList(left);
      int size = leftArray.size();
      int number = ((Number) right).intValue();
      if (size * number <= maxArraySize) {
        ArrayList<@Nullable Object> newArray = new ArrayList<>();
        for (int i = 0; i < number; i++) {
          newArray.addAll(leftArray);
        }
        return newArray;
      }
    }
    if (right instanceof ArrayList && isNumber(left)) {
      ArrayList<@Nullable Object> rightArray = getArrayList(right);
      int size = rightArray.size();
      int number = ((Number) left).intValue();
      if (size * number <= maxArraySize) {
        ArrayList<@Nullable Object> newArray = new ArrayList<>();
        for (int i = 0; i < number; i++) {
          newArray.addAll(rightArray);
        }
        return newArray;
      }
    }

    // Result is string.
    if (left instanceof ArrayList && right instanceof String) {
      ArrayList<@Nullable Object> leftArray = getArrayList(left);
      String delimiter = (String) right;
      return String.join(delimiter, makeStringArray(leftArray, config));
    }
    if (right instanceof ArrayList && left instanceof String) {
      ArrayList<@Nullable Object> rightArray = getArrayList(right);
      String delimiter = (String) left;
      return String.join(delimiter, makeStringArray(rightArray, config));
    }

    // Result is int (Long) or float (Double).
    if (isNumbers(left, right)) {
      if (left instanceof Double || right instanceof Double) {
        return (Double) (((Number) left).doubleValue()
                         * ((Number) right).doubleValue());
      }
      return (Long) (((Number) left).longValue()
                     * ((Number) right).longValue());
    }

    throw new EvaluateException();
  }

  /**
   * Divide.
   * <pre>{@code
   *   | left \\ right | block | array | string | int   | float | boolean | null  |
   *   |---------------|-------|-------|--------|-------|-------|---------|-------|
   *   | block         | #     | ERROR | ERROR  | ERROR | ERROR | ERROR   | ERROR |
   *   | array         | ERROR | ERROR | ERROR  | ERROR | ERROR | ERROR   | ERROR |
   *   | string        | ERROR | ERROR | array  | ERROR | ERROR | ERROR   | ERROR |
   *   | int           | ERROR | ERROR | ERROR  | *     | *     | ERROR   | ERROR |
   *   | float         | ERROR | ERROR | ERROR  | *     | *     | ERROR   | ERROR |
   *   | boolean       | ERROR | ERROR | ERROR  | ERROR | ERROR | ERROR   | ERROR |
   *   | null          | null  | null  | null   | x     | x     | null    | ERROR |
   *
   *   # is block or ERROR
   *   * is int or float or ERROR
   *   x is null or ERROR
   *
   *   For example, {"a": 2} / {"a": 10, "b": 3} --> {"a": 0.2, "b": null}
   *                {"a": 2} / {"b": 3} --> ERROR
   *                "a,b,c" / "," --> ["a", "b", "c"]
   *                "a,b,c," / "," --> ["a", "b", "c", ""]
   *                "a,b,c" / "" --> ["a", ",", "b", ",", "c"]
   *                3 / 2 --> 1.5
   *                3 / 0 --> ERROR
   *                3 / null --> ERROR
   *                null / 3 --> null
   *                null / 0 --> ERROR
   * }</pre>
   *
   * @param left left-hand sided value
   * @param right right-hand sided value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return the evaluated result
   * @throws EvaluateException it is thrown if the operator/operand is
   *                           invalid or array/block size reaches
   *                           limit.
   */
  private static @Nullable Object divide(@Nullable Object left,
                                         @Nullable Object right,
                                         @Nullable Config config)
                                         throws EvaluateException {
    // Result is ERROR.
    if (right == null
        || (isNumber(right) && ((Number) right).doubleValue() == 0.0)) {
      throw new EvaluateException();
    }

    // Result is null.
    if (left == null) {
      return null;
    }

    // Result is block.
    int maxArraySize = Config.getMaxArraySize(config);
    if (left instanceof HashMap && right instanceof HashMap) {
      HashMap<String, @Nullable Object> leftBlock = getHashMap(left);
      HashMap<String, @Nullable Object> rightBlock = getHashMap(right);
      HashMap<String, @Nullable Object> newBlock = new HashMap<>();
      Iterator<String> iterator = leftBlock.keySet().iterator();
      while (iterator.hasNext()) {
        @SuppressWarnings("null")
        String key = iterator.next();
        if (!rightBlock.containsKey(key)) {
          throw new EvaluateException();
        }
        newBlock.put(key,
                  divide(leftBlock.get(key), rightBlock.get(key), config));
      }
      int length = newBlock.size();
      iterator = rightBlock.keySet().iterator();
      while (iterator.hasNext()) {
        @SuppressWarnings("null")
        String key = iterator.next();
        if (!leftBlock.containsKey(key)) {
          length++;
          if (length > maxArraySize) {
            throw new EvaluateException();
          }
          newBlock.put(key, null);
        }
      }
      return newBlock;
    }

    // Result is array of string.
    if (left instanceof String && right instanceof String) {
      String leftString = (String) left;
      String rightString = (String) right;
      ArrayList<@Nullable Object> newArray = new ArrayList<>();
      if (rightString.isEmpty()) {
        if (leftString.length() > maxArraySize) {
          throw new EvaluateException();
        }
        for (int i = 0; i < leftString.length(); i++) {
          String letter = String.valueOf(leftString.charAt(i));
          newArray.add(letter);
        }
      } else {
        // Note that String#split is not used in order to avoid
        // the unexpected behavior by regex that is included in delimiter.
        int delimiterLength = rightString.length();
        int count = 0;
        int tokenIndex = 0;
        // Case 1. "x,y" / "," : [] --> ["x"]
        // Case 2. "x," / "," : [] --> ["x"]
        // Case 3. "x" / "," : [] --> []
        // Case 4. "," / "," : [] --> [""]
        while (tokenIndex < leftString.length()) {
          int delimiterIndex = leftString.indexOf(rightString, tokenIndex);
          if (delimiterIndex == -1) {
            break;
          }
          count++;
          if (count > maxArraySize) {
            throw new EvaluateException();
          }
          newArray.add(leftString.substring(tokenIndex, delimiterIndex));
          tokenIndex = delimiterIndex + delimiterLength;
        }
        // Case 1. "x,y" / "," : ["x"] --> ["x","y"]
        // Case 2. "x," / "," : ["x"] --> ["x", ""]
        // Case 3. "x" / "," : [] --> ["x"]
        // Case 4. "," / "," : [""] --> ["", ""]
        if (count + 1 > maxArraySize) {
          throw new EvaluateException();
        }
        if (tokenIndex < leftString.length()) {
          newArray.add(leftString.substring(tokenIndex));
        } else {
          newArray.add("");
        }
      }
      return newArray;
    }

    // Result is int (Long) or float (Double).
    if (isNumbers(left, right)) {
      Double number = (Double) (((Number) left).doubleValue()
                                / ((Number) right).doubleValue());
      if (number % 1.0 == 0.0) {
        return (Long) number.longValue();
      }
      return number;
    }

    throw new EvaluateException();
  }

  /**
   * Modulo.
   * <pre>{@code
   *   | left \\ right | block | array | string | int  | float | boolean | null  |
   *   |---------------|-------|-------|--------|------|-------|---------|-------|
   *   | block         | #     | ERROR | ERROR  | ERROR| ERROR | ERROR   | ERROR |
   *   | array         | ERROR | ERROR | ERROR  | ERROR| ERROR | ERROR   | ERROR |
   *   | string        | ERROR | ERROR | ERROR  | ERROR| ERROR | ERROR   | ERROR |
   *   | int           | ERROR | ERROR | ERROR  | *    | *     | ERROR   | ERROR |
   *   | float         | ERROR | ERROR | ERROR  | *    | *     | ERROR   | ERROR |
   *   | boolean       | ERROR | ERROR | ERROR  | ERROR| ERROR | ERROR   | ERROR |
   *   | null          | null  | null  | null   | x    | x     | null    | ERROR |
   *
   *   # is block or ERROR
   *   * is int or float or ERROR
   *   x is null or ERROR
   *
   *   For example, {"a": 20} % {"a": 6, "b": 3} --> {"a": 2, "b": null}
   *                {"a": 2} % {"b": 3} --> ERROR
   *                15 % 6 --> 3
   *                15 % 0 --> ERROR
   *                null % 2 --> null
   *                null / 0 --> ERROR
   *                5 % null --> ERROR
   * }</pre>
   *
   * @param left left-hand sided value
   * @param right right-hand sided value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return the evaluated result
   * @throws EvaluateException it is thrown if the operator/operand is
   *                           invalid or array/block size reaches
   *                           limit.
   */
  private static @Nullable Object modulo(@Nullable Object left,
                                         @Nullable Object right,
                                         @Nullable Config config)
                                         throws EvaluateException {
    // Result is ERROR.
    if (right == null
        || (isNumber(right) && ((Number) right).doubleValue() == 0.0)) {
      throw new EvaluateException();
    }

    // Result is null.
    if (left == null) {
      return null;
    }

    // Result is block.
    if (left instanceof HashMap && right instanceof HashMap) {
      HashMap<String, @Nullable Object> leftBlock = getHashMap(left);
      HashMap<String, @Nullable Object> rightBlock = getHashMap(right);
      HashMap<String, @Nullable Object> newBlock = new HashMap<>();
      Iterator<String> iterator = leftBlock.keySet().iterator();
      while (iterator.hasNext()) {
        @Nullable String key = iterator.next();
        if (!rightBlock.containsKey(key)) {
          throw new EvaluateException();
        }
        newBlock.put(key,
                  modulo(leftBlock.get(key), rightBlock.get(key), config));
      }
      int maxArraySize = Config.getMaxArraySize(config);
      int length = newBlock.size();
      iterator = rightBlock.keySet().iterator();
      while (iterator.hasNext()) {
        @Nullable String key = iterator.next();
        if (!leftBlock.containsKey(key)) {
          length++;
          if (length > maxArraySize) {
            throw new EvaluateException();
          }
          newBlock.put(key, null);
        }
      }
      return newBlock;
    }

    // Result is int (Long) or float (Double).
    if (isNumbers(left, right)) {
      double l = ((Number) left).doubleValue();
      double r = ((Number) right).doubleValue();
      double number = l % r;
      if (number < 0.0) {
        if (r >= 0.0) {
          number += r;
        }
      } else {
        if (r < 0.0) {
          number += r;
        }
      }
      if (number % 1.0 == 0.0) {
        return (Long) ((long) number);
      }
      return (Double) number;
    }

    throw new EvaluateException();
  }

  /**
   * Invert for "not" operator.
   * <pre>{@code
   *   not true --> false
   *   not false --> true
   *   not 0 --> true
   *   not 10 --> false
   *   not null --> true
   *   not "" --> false
   * }</pre>
   *
   * <p>Note that {@code not {"a": 3} } is confused as function "not"
   * in Parser. So error occurs.
   * On the other hand, {@code a = {"a": 3}; return(not a) } can be
   * evaluated as false.
   *
   * <p>Note that {@code not [3] } is confused as variable "not"
   * in Parser. So error occurs.
   * On the other hand, {@code a = [3]; return(not a) } can be
   * evaluated as false.
   *
   * @param left {@code null} should be given.
   * @param right an operand that is inverted.
   * @return the evaluated result
   * @throws EvaluateException it is thrown if left-hand sided operand
   *                           is not {@code null}.
   */
  private static Boolean logicalInvert(@Nullable Object left,
                                       @Nullable Object right)
                                       throws EvaluateException {
    if (left == null) {
      return !isTruthy(right);
    }
    throw new EvaluateException();
  }

  /**
   * Evaluate for "and" operator.
   * <pre>{@code
   *   false and false --> false
   *   false and true --> false
   *   true and false --> false
   *   true and true --> true
   *   0 and true --> false
   *   10.5 and true --> true
   *   "a" and true --> true
   *   null and true --> false
   *   true and {"a": 3} --> true
   *   true and {} --> true
   *   true and [3] --> true
   *   true and [] --> true
   * }</pre>
   *
   * @param left left-hand sided value
   * @param right right-hand sided value
   * @return the evaluated result
   */
  private static Boolean logicalAnd(@Nullable Object left,
                                    @Nullable Object right) {
    return isTruthy(left) && isTruthy(right);
  }

  /**
   * Evaluate for "or" operator.
   * <pre>{@code
   *   false or false --> false
   *   false or true --> true
   *   true or false --> true
   *   true or true --> true
   *   1 or false --> true
   *   "a" or false --> true
   *   null or false --> false
   *   false or {} --> true
   *   false or {"a": 3} --> true
   *   false or [] --> true
   *   false or [3] --> true
   * }</pre>
   *
   * @param left left-hand sided value
   * @param right right-hand sided value
   * @return the evaluated result
   */
  private static Boolean logicalOr(@Nullable Object left,
                                   @Nullable Object right) {
    return isTruthy(left) || isTruthy(right);
  }

  /**
   * Judge for "==" operator.
   * <pre>{@code
   *   "a" == "a" --> true
   *   [1, 2] == [1, 2] --> true
   *   [1, 2] == [2, 1] --> false
   *   {"a": 1, "b": 2} == {"b": 2, "a": 1} --> true
   *   {"a": 1, "b": 2} == {"a": 1} --> false
   *   {"a": 1, "b": 2} == [1, 2] --> false
   *   {"a": 1, "b": 2} == true --> true (using truthy)
   * }</pre>
   *
   * @param left left-hand sided value
   * @param right right-hand sided value
   * @return the evaluated result
   * @throws EvaluateException it is thrown if interpreter's
   *                           implementation has problem.
   */
  private static Boolean isEqual(@Nullable Object left,
                                 @Nullable Object right)
                                 throws EvaluateException {
    return isEqual(left, right, false);
  }

  /**
   * Judge for "==" operator.
   * <pre>{@code
   *   "a" == "a" --> true
   *   [1, 2] == [1, 2] --> true
   *   [1, 2] == [2, 1] --> false
   *   {"a": 1, "b": 2} == {"b": 2, "a": 1} --> true
   *   {"a": 1, "b": 2} == {"a": 1} --> false
   *   {"a": 1, "b": 2} == [1, 2] --> false
   *   {"a": 1, "b": 2} == true --> true (using truthy)
   * }</pre>
   *
   * @param left left-hand sided value
   * @param right right-hand sided value
   * @param isStrict selection of strict mode.
   *                 Truthy is not used if it is true.
   * @return the evaluated result
   * @throws EvaluateException it is thrown if interpreter's
   *                           implementation has problem.
   */
  private static Boolean isEqual(@Nullable Object left,
                                 @Nullable Object right,
                                 boolean isStrict)
                                 throws EvaluateException {
    if (isSameBothType(left, right)) {
      if (left == null) {
        return true;
      }
      if (left instanceof ArrayList) {
        ArrayList<@Nullable Object> leftArray = getArrayList(left);
        ArrayList<@Nullable Object> rightArray = getArrayList(right);
        if (leftArray.size() != rightArray.size()) {
          return false;
        }
        for (int i = 0; i < leftArray.size(); i++) {
          if (!isEqual(leftArray.get(i), rightArray.get(i), true)) {
            return false;
          }
        }
        return true;
      }
      if (left instanceof HashMap) {
        HashMap<String, @Nullable Object> leftBlock = getHashMap(left);
        HashMap<String, @Nullable Object> rightBlock = getHashMap(right);
        if (leftBlock.size() != rightBlock.size()) {
          return false;
        }
        Iterator<String> iterator = leftBlock.keySet().iterator();
        while (iterator.hasNext()) {
          @SuppressWarnings("null")
          String key = iterator.next();
          if (!rightBlock.containsKey(key)) {
            return false;
          }
          if (!isEqual(leftBlock.get(key), rightBlock.get(key), true)) {
            return false;
          }
        }
        iterator = rightBlock.keySet().iterator();
        while (iterator.hasNext()) {
          @SuppressWarnings("null")
          String key = iterator.next();
          if (!leftBlock.containsKey(key)) {
            return false;
          }
        }
        return true;
      }
      if (left instanceof CalleeRegistry && right instanceof CalleeRegistry) {
        Callee leftCallee = ((CalleeRegistry) left).callee;
        Callee rightCallee = ((CalleeRegistry) right).callee;
        return leftCallee == rightCallee;
      }

      return left.equals(right);
    }

    if (left instanceof Number && right instanceof Number) {
      return ((Number) left).doubleValue() == ((Number) right).doubleValue();
    }

    if (!isStrict
        && (left instanceof Boolean || right instanceof Boolean)) {
      return isTruthy(left) == isTruthy(right);
    }
    return false;
  }

  /**
   * Judge for "!=" operator.
   *
   * @param left left-hand sided value
   * @param right right-hand sided value
   * @return the evaluated result
   * @throws EvaluateException it is thrown if interpreter's
   *                           implementation has problem.
   */
  private static Boolean isNotEqual(@Nullable Object left,
                                    @Nullable Object right)
                                    throws EvaluateException {
    return !isEqual(left, right);
  }

  /**
   * Judge for {@code "<" } operator.
   *
   * @param left left-hand sided value
   * @param right right-hand sided value
   * @return the evaluated result
   * @throws EvaluateException it is thrown if the given values are not
   *                           number.
   */
  @SuppressWarnings("null")
  private static Boolean isLessThan(@Nullable Object left,
                                    @Nullable Object right)
                                    throws EvaluateException {
    makeSureNumbers(left, right);
    return ((Number) left).doubleValue() < ((Number) right).doubleValue();
  }

  /**
   * Judge for {@code "<=" } operator.
   *
   * @param left left-hand sided value
   * @param right right-hand sided value
   * @return the evaluated result
   * @throws EvaluateException it is thrown if the given values are not
   *                           number.
   */
  @SuppressWarnings("null")
  private static Boolean isLessThanOrEqual(@Nullable Object left,
                                           @Nullable Object right)
                                           throws EvaluateException {
    makeSureNumbers(left, right);
    return ((Number) left).doubleValue() <= ((Number) right).doubleValue();
  }

  /**
   * Judge for {@code ">" } operator.
   *
   * @param left left-hand sided value
   * @param right right-hand sided value
   * @return the evaluated result
   * @throws EvaluateException it is thrown if the given values are not
   *                           number.
   */
  @SuppressWarnings("null")
  private static Boolean isGreaterThan(@Nullable Object left,
                                       @Nullable Object right)
                                       throws EvaluateException {
    makeSureNumbers(left, right);
    return ((Number) left).doubleValue() > ((Number) right).doubleValue();
  }

  /**
   * Judge for {@code ">=" } operator.
   *
   * @param left left-hand sided value
   * @param right right-hand sided value
   * @return the evaluated result
   * @throws EvaluateException it is thrown if the given values are not
   *                           number.
   */
  @SuppressWarnings("null")
  private static Boolean isGreaterThanOrEqual(@Nullable Object left,
                                              @Nullable Object right)
                                              throws EvaluateException {
    makeSureNumbers(left, right);
    return ((Number) left).doubleValue() >= ((Number) right).doubleValue();
  }

  /**
   * Judge for "in" operator.
   * <pre>{@code
   *   "b" in ["a", "b", "c"] --> true
   *   "d" in ["a", "b", "c"] --> false
   *   1 in [1, 2, "c"] --> true
   *   true in [1, true, "c"] --> true
   *   [2, 3] in [1, [2,3], "c"] --> true
   *   {"a": 2, "b": false} in [1, {"b": false, "a": 2}, "c"] --> true
   *   "x" in {"x": 3, "y": 10} --> true
   *   {"x": 3} in {"x": 3, "y": 10} --> true
   *   {"x": 3, "y": 10} in {"x": 3, "y": 10} --> true
   *   "dog" in "cat&dog&bird" --> true
   *   "23" in "123" --> true
   *   23 in "123" --> ERROR
   *   1 in 5 --> ERROR
   *   [1, 2, "c"] in 1 --> ERROR
   *   true in true --> ERROR
   *   null in null --> ERROR
   * }</pre>
   *
   * @param element left-hand sided element of "in"-operation
   * @param elements right-hand sided elements of "in"-operation.
   *                 This should be {@code ArrayList<@Nullable Object>},
   *                 {@code HashMap<String, @Nullable Object>},
   *                 {@code String}.
   * @return true if it is contained, false otherwise
   * @throws EvaluateException it is thrown if illegal objects are given
   */
  static Boolean isContained(@Nullable Object element,
                             @Nullable Object elements)
                             throws EvaluateException {
    if (elements instanceof ArrayList) {
      ArrayList<@Nullable Object> array = getArrayList(elements);
      for (@Nullable Object value : array) {
        if (isEqual(value, element, true)) {
          return true;
        }
      }
      return false;
    } else if (elements instanceof HashMap) {
      HashMap<String, @Nullable Object> block = getHashMap(elements);
      if (element instanceof String) {
        String name = (String) element;
        return block.containsKey(name);
      } else if (element instanceof HashMap) {
        HashMap<String, @Nullable Object> pairs = getHashMap(element);
        Iterator<Map.Entry<String, @Nullable Object>> iterator =
                                              pairs.entrySet().iterator();
        while (iterator.hasNext()) {
          @SuppressWarnings("null")
          Map.Entry<String, @Nullable Object> entry = iterator.next();
          @Nullable String key = entry.getKey();
          @Nullable Object value = entry.getValue();
          if (!block.containsKey(key)) {
            return false;
          }
          if (!isEqual(block.get(key), value, true)) {
            return false;
          }
        }
        return true;
      }
    } else if (element instanceof String && elements instanceof String) {
      return ((String) elements).indexOf((String) element) != -1;
    }
    throw new EvaluateException();
  }

  /**
   * Calculates binary operation.
   *
   * @param operator Token type of operator
   * @param left left-hand sided value
   * @param right right-hand sided value
   * @param statement a statement that contains this operation.
   *                  This is used to get information when exception is
   *                  thrown.
   * @param config configuration if it is needed, {@code null} otherwise
   * @return the evaluated result
   * @throws EvaluateException it is thrown if the operator/operand is
   *                           invalid or array/block size reaches
   *                           limit.
   */
  static @Nullable Object calculateBinary(Token.Type operator,
                                          @Nullable Object left,
                                          @Nullable Object right,
                                          @Nullable Statement statement,
                                          @Nullable Config config)
                                          throws EvaluateException {
    Object value = Environment.UNDEFINED;
    try {
      switch (operator) {
        case PLUS:
          value = add(left, right, config);
          break;
        case MINUS:
          value = subtract(left, right, config);
          break;
        case MULTIPLY:
          value = multiply(left, right, config);
          break;
        case DIVIDE:
          value = divide(left, right, config);
          break;
        case MODULO:
          value = modulo(left, right, config);
          break;
        case NOT:
          value = logicalInvert(left, right);
          break;
        case AND:
          value = logicalAnd(left, right);
          break;
        case OR:
          value = logicalOr(left, right);
          break;
        case EQ:
          value = isEqual(left, right);
          break;
        case NE:
          value = isNotEqual(left, right);
          break;
        case LT:
          value = isLessThan(left, right);
          break;
        case LE:
          value = isLessThanOrEqual(left, right);
          break;
        case GT:
          value = isGreaterThan(left, right);
          break;
        case GE:
          value = isGreaterThanOrEqual(left, right);
          break;
        case IN:
          value = isContained(left, right);
          break;
        default:
          break;
      }
    } catch (EvaluateException e) {
      ;
    }
    if (value == Environment.UNDEFINED) {
      if (statement != null) {
        abort("Cannot evaluate \"" + statement + "\"", statement, config, false);
      } else {
        throw new EvaluateException();
      }
    }
    return value;
  }

  /**
   * Normalizes a value of Injection/Value statement.
   *
   * @param statement a statement that has a value.
   *                  Its value is Java oriented. So it is normalized.
   *                  The given statement must be Injection or Value.
   * @param config configuration if it is needed, {@code null} otherwise
   * @return the normalized value for this script.
   * @throws EvaluateException it is thrown if the illegal
   *                           statement/value is given.
   */
  private static @Nullable Object getNormalizedValue(Statement statement,
                                                     @Nullable Config config)
                                                    throws EvaluateException {
    @Nullable Object value = null;
    if (statement instanceof Injection) {
      value = ((Injection) statement).value;
    } else if (statement instanceof Value) {
      value = ((Value) statement).value;
    } else {
      abort("Invalid statement \"" + statement
            + "\" in Evaluator#getNormalizedValue",
            statement, config, false);
    }

    try {
      value = getNormalizedValue(value);
    } catch (EvaluateException e) {
      abort("Cannot use a value of \"" + statement + "\"",
            statement, config, false);
    }
    return value;
  }

  /**
   * Normalizes a value.
   * <pre>
   * | The given value | The returned value |
   * |-----------------|--------------------|
   * | Boolean         | Boolean            |
   * | Byte            | Long               |
   * | Short           | Long               |
   * | Integer         | Long               |
   * | Long            | Long               |
   * | Float           | Double             |
   * | Double          | Double             |
   * | String          | String             |
   * | ArrayList       | ArrayList          |
   * | HashMap         | HashMap            |
   * | null            | null               |
   * </pre>
   *
   * <p>ArrayList must be {@code ArrayList<@Nullable Object> }.
   *
   * <p>HashMap must be {@code HashMap<String, @Nullable Object> }.
   *
   * <p>The relationship of this script's value and Java's value are
   * described as below.
   * <pre>
   * | This script's value | Java's value |
   * |---------------------|--------------|
   * | boolean             | Boolean      |
   * | int                 | Long         |
   * | float               | Double       |
   * | string              | String       |
   * | array               | ArrayList    |
   * | block               | HashMap      |
   * | null                | null         |
   * </pre>
   * Dynamic typing is used in this script. So the object, such as
   * Boolean, is used instead of the primitive value in Java.
   *
   * <p>Carefully, Block/Array statement and block/array value is
   * different. Block/Array statement is intermediate object in this
   * script.
   * <ul>
   *   <li> Block statement: Block class's object
   *   <li> Array statement: Array class's object
   *   <li> block value: HashMap class's object
   *   <li> array value: ArrayList class's object
   * </ul>
   * For example, the following "[foo, 5]" is represented as
   * array. However it is not simple value because it contains the
   * variable "foo". In run-time, the value of the variable "foo"
   * should be assigned into the array "[foo, 5]". For this purpose,
   * Array statement is used instead of array value.
   * <pre>
   *   foo = 3
   *   bar = [foo, 5]
   * </pre>
   *
   * @param value a value that may be unsuitable object
   * @return a normalized value
   * @throws EvaluateException it is thrown if the given value is unsuitable.
   */
  static @Nullable Object getNormalizedValue(@Nullable Object value)
                                                    throws EvaluateException {
    if (value == null
        || value instanceof Boolean
        || value instanceof Long
        || value instanceof Double
        || value instanceof String) {
      return value;
    }
    if (value instanceof Byte || value instanceof Short
        || value instanceof Integer) {
      return (Long) (((Number) value).longValue());
    }
    if (value instanceof Float) {
      return (Double) (((Number) value).doubleValue());
    }

    if (value instanceof ArrayList) {
      // Normalize after deep-copy
      ArrayList<@Nullable Object> objects = copy(getArrayList(value));
      for (int i = 0; i < objects.size(); i++) {
        @Nullable Object object = getNormalizedValue(objects.get(i));
        objects.set(i, object);
      }
      return objects;
    }

    if (value instanceof HashMap) {
      // Check that key is valid.
      if (!((HashMap<?, ?>) value).keySet().stream()
          .allMatch(String.class::isInstance)) {
        throw new EvaluateException();
      }
      // Normalize after deep-copy.
      HashMap<String, @Nullable Object> objects = copy(getHashMap(value));
      Iterator<String> iterator = objects.keySet().iterator();
      while (iterator.hasNext()) {
        @SuppressWarnings("null")
        String key = iterator.next();
        @Nullable Object object = getNormalizedValue(objects.get(key));
        objects.replace(key, object);
      }
      return objects;
    }

    throw new EvaluateException();
  }

  /**
   * Gets an ArrayList if the given objects entity is ArrayList.
   * Note that its component is assumed that Object (includes
   * {@code null}).
   *
   * @param object an object whose entity is ArrayList.
   * @return an ArrayList
   * @throws EvaluateException it is thrown if the given object's
   *                           entity is not ArrayList
   */
  static ArrayList<@Nullable Object> getArrayList(@Nullable Object object)
                                                    throws EvaluateException {
    ArrayList<@Nullable Object> array = getArrayListOrNull(object);
    if (array == null) {
      throw new EvaluateException();
    }
    return array;
  }

  /**
   * Gets an ArrayList if the given objects entity is ArrayList.
   * Note that its component is assumed that Object (includes
   * {@code null}).
   *
   * @param object an object whose entity is ArrayList.
   * @return an ArrayList if the given object's entity is ArrayList,
   *         {@code null} otherwise.
   */
  @SuppressWarnings("unchecked")
  static @Nullable ArrayList<@Nullable Object>
                                getArrayListOrNull(@Nullable Object object) {
    if (object instanceof ArrayList) {
      return (ArrayList<@Nullable Object>) object;
    }
    return null;
  }

  /**
   * Gets a HashMap if the given object's entity is HashMap.
   * Note that its key and value are assumed that String and Object
   * (includes {@code null}).
   *
   * @param object an object whose entity is HashMap.
   * @return an HashMap
   * @throws EvaluateException it is thrown if the given object's
   *                           entity is not HashMap
   */
  static HashMap<String, @Nullable Object> getHashMap(@Nullable Object object)
                                                    throws EvaluateException {
    HashMap<String, @Nullable Object> map = getHashMapOrNull(object);
    if (map == null) {
      throw new EvaluateException();
    }
    return map;
  }

  /**
   * Gets a HashMap if the given object's entity is HashMap.
   * Note that its key and value are assumed that String and Object
   * (includes {@code null}).
   *
   * @param object an object whose entity is HashMap.
   * @return an HashMap if the given object's entity is HashMap,
   *         {@code null} otherwise.
   */
  @SuppressWarnings("unchecked")
  static @Nullable HashMap<String, @Nullable Object>
                                  getHashMapOrNull(@Nullable Object object) {
    if (object instanceof HashMap) {
      return (HashMap<String, @Nullable Object>) object;
    }
    return null;
  }

  /**
   * Copies a ArrayList.
   * This is so called deep-copy or clone.
   *
   * @param source an  ArrayList as source
   * @return a copied ArrayList
   */
  @SuppressWarnings("unchecked")
  static ArrayList<@Nullable Object> copy(ArrayList<@Nullable Object> source) {
    ArrayList<@Nullable Object> destination = new ArrayList<>();
    for (@Nullable Object value : source) {
      if (value instanceof String) {
        value = new String(((String) value));
      } else if (value instanceof HashMap) {
        value = copy((HashMap<String, @Nullable Object>) value);
      } else if (value instanceof ArrayList) {
        value = copy((ArrayList<@Nullable Object>) value);
      }
      destination.add(value);
    }
    return destination;
  }

  /**
   * Copies a HashMap.
   * This is so called deep-copy or clone.
   *
   * @param source a HashMap as source
   * @return a copied HashMap
   */
  @SuppressWarnings("unchecked")
  static HashMap<String, @Nullable Object>
                            copy(HashMap<String, @Nullable Object> source) {
    HashMap<String, @Nullable Object> destination = new HashMap<>();
    Iterator<Map.Entry<String, @Nullable Object>> iterator =
                                              source.entrySet().iterator();
    while (iterator.hasNext()) {
      @SuppressWarnings("null")
      Map.Entry<String, @Nullable Object> entry = iterator.next();
      @SuppressWarnings("null")
      String key = entry.getKey();
      @Nullable Object value = entry.getValue();
      if (value instanceof String) {
        value = new String(((String) value));
      } else if (value instanceof HashMap) {
        value = copy((HashMap<String, @Nullable Object>) value);
      } else if (value instanceof ArrayList) {
        value = copy((ArrayList<@Nullable Object>) value);
      }
      destination.put(key, value);
    }
    return destination;
  }

  /**
   * Remove the given element from array.
   *
   * @param elements an array
   * @param element a element that is removed from the array
   * @throws EvaluateException it is thrown if interpreter's
   *                           implementation has problem.
   */
  private static void removeElementFromArray(
                                        ArrayList<@Nullable Object> elements,
                                        @Nullable Object element)
                                        throws EvaluateException {
    boolean isRemoved = true;
    while (isRemoved) {
      isRemoved = false;
      int index = 0;
      for (@Nullable Object value : elements) {
        if (isEqual(value, element, true)) {
          elements.remove(index);
          isRemoved = true;
          break;
        }
        index++;
      }
    }
  }

  /**
   * Evaluate as boolean.
   *
   * @param value a value that is evaluated as boolean.
   * @return The evaluated result
   *         <ul>
   *           <li> {@code null}, false, 0, 0.0 --> false
   *           <li> Other --> true
   *                (Note that "0" is true.)
   *         </ul>
   */
  static boolean isTruthy(@Nullable Object value) {
    if (value == null) {
      return false;
    }
    if (value instanceof Boolean) {
      return (Boolean) value;
    }
    if (value instanceof Long) {
      return (Long) value != 0;
    }
    if (value instanceof Double) {
      return (Double) value != 0.0;
    }
    return true;
  }

  /**
   * Judge wether both object are same class's instance or not.
   *
   * @param left left-hand sided object
   * @param right right-hand sided object
   * @return both objects are same class's instance or not
   */
  @SuppressWarnings("null")
  private static boolean isSameBothType(@Nullable Object left,
                                        @Nullable Object right) {
    int flag = (left == null) ? 2 : 0;
    flag |= (right == null) ? 1 : 0;
    if (flag == 3) {
      return true;
    } else if (flag != 0) {
      return false;
    }

    return left.getClass() == right.getClass();
  }

  /**
   * Judge whether the given object is number or not.
   *
   * @param value an object
   * @return the given object is number or not
   */
  private static boolean isNumber(@Nullable Object value) {
    return value instanceof Number;
  }

  /**
   * Judge whether both objects are number or not.
   *
   * @param left left-hand sided object
   * @param right right-hand sided object
   * @return both objects are number or not
   */
  private static boolean isNumbers(@Nullable Object left,
                                   @Nullable Object right) {
    return isNumber(left) && isNumber(right);
  }

  /**
   * Makes sure that the arguments are number.
   * When the arguments, left and right, aren't number, EvaluateException
   * is thrown.
   *
   * @param left left-hand sided object
   * @param right right-hand sided object
   * @throws EvaluateException it is thrown if they aren't number
   */
  private static void makeSureNumbers(@Nullable Object left,
                                      @Nullable Object right)
                                      throws EvaluateException {
    if (!isNumbers(left, right)) {
      throw new EvaluateException();
    }
  }

  /**
   * Makes an array of string with makeString method.
   *
   * @param values an array of values
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of string
   * @throws EvaluateException it is thrown if number is Infinity or NaN
   *                           without permission of Configuration
   */
  private static String[] makeStringArray(ArrayList<@Nullable Object> values,
                                          @Nullable Config config)
                                          throws EvaluateException {
    return makeStringArray(values, config, false);
  }

  /**
   * Makes an array of string with makeString method.
   *
   * @param values an array of values
   * @param config configuration if it is needed, {@code null} otherwise
   * @param isMember true if the above value is a member of block or
   *                 array, false otherwise
   * @return an array of string
   * @throws EvaluateException it is thrown if number is Infinity or NaN
   *                           without permission of Configuration
   */
  private static String[] makeStringArray(ArrayList<@Nullable Object> values,
                                          @Nullable Config config,
                                          boolean isMember)
                                          throws EvaluateException {
    String[] strings = new String[values.size()];
    for (int i = 0; i < values.size(); i++) {
      strings[i] = makeString(values.get(i), config, isMember, true);
    }
    return strings;
  }

  /**
   * Makes escape sequence in string.
   *
   * @param value the original string
   * @return the modified string
   */
  private static String makeEscapeSequence(String value) {
    String string = value;
    int index = 0;
    int length = value.length();
    while (index < length) {
      char letter = string.charAt(index);
      @Nullable Character replaced = REPLACED_LETTERS.get(letter);
      if (replaced != null) {
        StringBuilder sb = new StringBuilder();
        sb.append(string.substring(0, index));
        sb.append("\\");
        sb.append(replaced);
        index++;
        if (index < length) {
          sb.append(string.substring(index));
        }
        string = sb.toString();
        length++;
      }
      index++;
    }
    return string;
  }

  /**
   * Makes string.
   * In this class, values are treated as Java native values/classes.
   * There are the following difference between VivJson and
   * Java.
   * <ul>
   *   <li> Array, such as [1, 2, ... ] : ArrayList
   *   <li> Block, such as {"a": 1, "b": 2, ... } : HashMap
   * </ul>
   * Thus, this method is used instead of toString method of Java.
   *
   * @param value an object that may be Byte, Short, Integer, Long,
   *              Float, Double, Boolean, String,
   *              {@code ArrayList<@Nullable Object>},
   *              {@code HashMap<String, @Nullable Object>},
   *              or null
   * @param config configuration if it is needed, {@code null} otherwise
   * @return the given value as string
   * @throws EvaluateException it is thrown if number is Infinity or NaN
   *                           without permission of Configuration
   */
  static String makeString(@Nullable Object value,
                           @Nullable Config config)
                           throws EvaluateException {
    return makeString(value, config, false, true);
  }

  /**
   * Makes string from any value.
   * In this class, values are treated as Java native values/classes.
   * There are the following difference between VivJson and
   * Java.
   * <ul>
   *   <li> Array, such as [1, 2, ... ] : ArrayList
   *   <li> Block, such as {"a": 1, "b": 2, ... } : HashMap
   * </ul>
   * Thus, this method is used instead of toString method of Java.
   *
   * @param value an object that may be Byte, Short, Integer, Long,
   *              Float, Double, Boolean, String,
   *              {@code ArrayList<@Nullable Object>},
   *              {@code HashMap<String, @Nullable Object>},
   *              {@code null},
   *              or Statement.
   * @param config configuration if it is needed, {@code null} otherwise
   * @param isMember true if the above value is a member of block or
   *                 array, false otherwise
   * @param isEscape true if escape sequence is enabled, false
   *                 otherwise. When this is true, control character,
   *                 the double quotation mark, or reverse solidus is
   *                 modified as escape sequence.
   * @return the given value as string
   * @throws EvaluateException it is thrown if number is Infinity or NaN
   *                           without permission of Configuration
   */
  static String makeString(@Nullable Object value,
                           @Nullable Config config,
                           boolean isMember,
                           boolean isEscape) throws EvaluateException {
    if (value == null) {
      return "null";
    }

    if (value instanceof Boolean) {
      return ((Boolean) value) ? "true" : "false";
    }

    if (value instanceof ArrayList) {
      ArrayList<@Nullable Object> array = getArrayList(value);
      String[] data = makeStringArray(array, config, true);
      return "[" + String.join(", ", data) + "]";
    }

    if (value instanceof HashMap) {
      HashMap<String, @Nullable Object> block = getHashMap(value);
      StringBuilder sb = new StringBuilder("{");
      boolean isFirst = true;
      Iterator<Map.Entry<String, @Nullable Object>> iterator =
                                                block.entrySet().iterator();
      while (iterator.hasNext()) {
        @SuppressWarnings("null")
        Map.Entry<String, @Nullable Object> entry = iterator.next();
        @SuppressWarnings("null")
        String key = entry.getKey();
        @Nullable Object object = entry.getValue();
        if (key != null) {
          if (!isFirst) {
            sb.append(", ");
          }
          sb.append(makeString(key, config, true, true));
          sb.append(": ");
          sb.append(makeString(object, config, true, true));
          isFirst = false;
        }
      }
      sb.append("}");
      return sb.toString();
    }

    if (value instanceof String) {
      String text = (String) value;
      if (isEscape) {
        text = makeEscapeSequence(text);
      }
      if (isMember) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        sb.append(text);
        sb.append("\"");
        text = sb.toString();
      }
      return text;
    }

    if (value instanceof Float) {
      value = (Double) (((Float) value).doubleValue());
    }
    if (value instanceof Double) {
      Double number = (Double) value;
      if (number.isInfinite()) {
        String infinity = Config.getInfinity(config);
        if (infinity instanceof String) {
          return (number < 0) ? ("-" + infinity) : infinity;
        }
        throw new EvaluateException();
      }
      if (number.isNaN()) {
        String nan = Config.getNaN(config);
        if (nan instanceof String) {
          return nan;
        }
        throw new EvaluateException();
      }
    }

    return value.toString();
  }
}
