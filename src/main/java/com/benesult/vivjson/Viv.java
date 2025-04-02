/* API of VivJson.
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.annotation.Nullable;

/**
 * API of VivJson.
 * <ul>
 *   <li> Running/Deserialization function
 *     <ul>
 *       <li>{@link #run(Object...) Viv.run } or
 *           {@link #runEx(Object...) Viv.runEx } :
 *                                Run VivJson's code or deserialize
 *                                JSON objects.
 *       <li>{@link #parse(Object...) Viv.parse } or
 *           {@link #parseEx(Object...) Viv.parseEx } :
 *                                Parse VivJson's code and JSON object.
 *       <li>{@link #parseFile(String, Config) Viv.parseFile } or
 *           {@link #parseFileEx(String, Config) Viv.parseFileEx } :
 *                                Parse a file that contains VivJson's
 *                                code or JSON object.
 *       <li>{@link #parseText(String, Config) Viv.parseText } or
 *           {@link #parseTextEx(String, Config) Viv.parseTextEx } :
 *                                Parse a text that is VivJson's code
 *                                or JSON object.
 *       <li>{@link #makeInstance(Object...) Viv.makeInstance } or
 *           {@link #makeInstanceEx(Object...) Viv.makeInstanceEx } :
 *                                Makes a class instance.
 *     </ul>
 *   <li> Monolithic conversion (with running/deserialization function)
 *     <ul>
 *       <li>{@link #getBoolean(Object...) Viv.getBoolean } or
 *           {@link #getBooleanEx(Object...) Viv.getBooleanEx } :
 *                                Get a boolean.
 *       <li>{@link #getInteger(Object...) Viv.getInteger } or
 *           {@link #getIntegerEx(Object...) Viv.getIntegerEx } :
 *                                Get an integer.
 *       <li>{@link #getLong(Object...) Viv.getLong } or
 *           {@link #getLongEx(Object...) Viv.getLongEx } :
 *                                Get a long integer.
 *       <li>{@link #getFloat(Object...) Viv.getFloat } or
 *           {@link #getFloatEx(Object...) Viv.getFloatEx } :
 *                                Get a floating-point number.
 *       <li>{@link #getDouble(Object...) Viv.getDouble } or
 *           {@link #getDoubleEx(Object...) Viv.getDoubleEx } :
 *                                Get a double-precision floating-point
 *                                number.
 *       <li>{@link #getString(Object...) Viv.getString } or
 *           {@link #getStringEx(Object...) Viv.getStringEx } :
 *                                Get a string.
 *     </ul>
 *   <li> Array conversion (with running/deserialization function)
 *     <ul>
 *       <li>{@link #getBooleans(Object...) Viv.getBooleans } or
 *           {@link #getBooleansEx(Object...) Viv.getBooleansEx } :
 *                                Get an array of boolean.
 *       <li>{@link #getBooleanOrNulls(Object...) Viv.getBooleanOrNulls } or
 *           {@link #getBooleanOrNullsEx(Object...) Viv.getBooleanOrNullsEx } :
 *                                Get an array of {@code @Nullable Boolean}.
 *       <li>{@link #getIntegers(Object...) Viv.getIntegers } or
 *           {@link #getIntegersEx(Object...) Viv.getIntegersEx } :
 *                                Get an array of integer.
 *       <li>{@link #getIntegerOrNulls(Object...) Viv.getIntegerOrNulls } or
 *           {@link #getIntegerOrNullsEx(Object...) Viv.getIntegerOrNullsEx } :
 *                                Get an array of {@code @Nullable Integer}.
 *       <li>{@link #getLongs(Object...) Viv.getLongs } or
 *           {@link #getLongsEx(Object...) Viv.getLongsEx } :
 *                                Get an array of long integer.
 *       <li>{@link #getLongOrNulls(Object...) Viv.getLongOrNulls } or
 *           {@link #getLongOrNullsEx(Object...) Viv.getLongOrNullsEx } :
 *                                Get an array of {@code @Nullable Long}.
 *       <li>{@link #getFloats(Object...) Viv.getFloats } or
 *           {@link #getFloatsEx(Object...) Viv.getFloatsEx } :
 *                                Get an array of floating-point number.
 *       <li>{@link #getFloatOrNulls(Object...) Viv.getFloatOrNulls } or
 *           {@link #getFloatOrNullsEx(Object...) Viv.getFloatOrNullsEx } :
 *                                Get an array of {@code @Nullable Float}.
 *       <li>{@link #getDoubles(Object...) Viv.getDoubles } or
 *           {@link #getDoublesEx(Object...) Viv.getDoublesEx } :
 *                                Get an array of double-precision
 *                                floating-point number.
 *       <li>{@link #getDoubleOrNulls(Object...) Viv.getDoubleOrNulls } or
 *           {@link #getDoubleOrNullsEx(Object...) Viv.getDoubleOrNullsEx } :
 *                                Get an array of {@code @Nullable Double}.
 *       <li>{@link #getStrings(Object...) Viv.getStrings } or
 *           {@link #getStringsEx(Object...) Viv.getStringsEx } :
 *                                Get an array of String.
 *       <li>{@link #getStringOrNulls(Object...) Viv.getStringOrNulls } or
 *           {@link #getStringOrNullsEx(Object...) Viv.getStringOrNullsEx } :
 *                                Get an array of {@code @Nullable String}.
 *       <li>{@link #getObjects(Object...) Viv.getObjects } or
 *           {@link #getObjectsEx(Object...) Viv.getObjectsEx } :
 *                                Get an array of Object.
 *       <li>{@link #getObjectOrNulls(Object...) Viv.getObjectOrNulls } or
 *           {@link #getObjectOrNullsEx(Object...) Viv.getObjectOrNullsEx } :
 *                                Get an array of {@code @Nullable Object}.
 *     </ul>
 *   <li> Key-Value pairs conversion (with running/deserialization function)
 *     <ul>
 *       <li>{@link #getKeyValueBooleans(Object...) Viv.getKeyValueBooleans } or
 *           {@link #getKeyValueBooleansEx(Object...) Viv.getKeyValueBooleansEx } :
 *                                Get key-value pairs as an array of
 *                                {@code Viv.KeyValue<Boolean> }.
 *       <li>{@link #getKeyValueBooleanOrNulls(Object...) Viv.getKeyValueBooleanOrNulls } or
 *           {@link #getKeyValueBooleanOrNullsEx(Object...) Viv.getKeyValueBooleanOrNullsEx } :
 *                                Get key-value pairs as an array of
 *                                {@code Viv.KeyValue<@Nullable Boolean> }.
 *       <li>{@link #getKeyValueIntegers(Object...) Viv.getKeyValueIntegers } or
 *           {@link #getKeyValueIntegersEx(Object...) Viv.getKeyValueIntegersEx } :
 *                                Get key-value pairs as an array of
 *                                {@code Viv.KeyValue<Integer> }.
 *       <li>{@link #getKeyValueIntegerOrNulls(Object...) Viv.getKeyValueIntegerOrNulls } or
 *           {@link #getKeyValueIntegerOrNullsEx(Object...) Viv.getKeyValueIntegerOrNullsEx } :
 *                                Get key-value pairs as an array of
 *                                {@code Viv.KeyValue<@Nullable Integer> }.
 *       <li>{@link #getKeyValueLongs(Object...) Viv.getKeyValueLongs } or
 *           {@link #getKeyValueLongsEx(Object...) Viv.getKeyValueLongsEx } :
 *                                Get key-value pairs as an array of
 *                                {@code Viv.KeyValue<Long> }.
 *       <li>{@link #getKeyValueLongOrNulls(Object...) Viv.getKeyValueLongOrNulls } or
 *           {@link #getKeyValueLongOrNullsEx(Object...) Viv.getKeyValueLongOrNullsEx } :
 *                                Get key-value pairs as an array of
 *                                {@code Viv.KeyValue<@Nullable Long> }.
 *       <li>{@link #getKeyValueFloats(Object...) Viv.getKeyValueFloats } or
 *           {@link #getKeyValueFloatsEx(Object...) Viv.getKeyValueFloatsEx } :
 *                                Get key-value pairs as an array of
 *                                {@code Viv.KeyValue<Float> }.
 *       <li>{@link #getKeyValueFloatOrNulls(Object...) Viv.getKeyValueFloatOrNulls } or
 *           {@link #getKeyValueFloatOrNullsEx(Object...) Viv.getKeyValueFloatOrNullsEx } :
 *                                Get key-value pairs as an array of
 *                                {@code Viv.KeyValue<@Nullable Float> }.
 *       <li>{@link #getKeyValueDoubles(Object...) Viv.getKeyValueDoubles } or
 *           {@link #getKeyValueDoublesEx(Object...) Viv.getKeyValueDoublesEx } :
 *                                Get key-value pairs as an array of
 *                                {@code Viv.KeyValue<Double> }.
 *       <li>{@link #getKeyValueDoubleOrNulls(Object...) Viv.getKeyValueDoubleOrNulls } or
 *           {@link #getKeyValueDoubleOrNullsEx(Object...) Viv.getKeyValueDoubleOrNullsEx } :
 *                                Get key-value pairs as an array of
 *                                {@code Viv.KeyValue<@Nullable Double> }.
 *       <li>{@link #getKeyValueStrings(Object...) Viv.getKeyValueStrings } or
 *           {@link #getKeyValueStringsEx(Object...) Viv.getKeyValueStringsEx } :
 *                                Get key-value pairs as an array of
 *                                {@code Viv.KeyValue<String> }.
 *       <li>{@link #getKeyValueStringOrNulls(Object...) Viv.getKeyValueStringOrNulls } or
 *           {@link #getKeyValueStringOrNullsEx(Object...) Viv.getKeyValueStringOrNullsEx } :
 *                                Get key-value pairs as an array of
 *                                {@code Viv.KeyValue<@Nullable String> }.
 *       <li>{@link #getKeyValueObjects(Object...) Viv.getKeyValueObjects } or
 *           {@link #getKeyValueObjectsEx(Object...) Viv.getKeyValueObjectsEx } :
 *                                Get key-value pairs as an array of
 *                                {@code Viv.KeyValue<Object> }.
 *       <li>{@link #getKeyValueObjectOrNulls(Object...) Viv.getKeyValueObjectOrNulls } or
 *           {@link #getKeyValueObjectOrNullsEx(Object...) Viv.getKeyValueObjectOrNullsEx } :
 *                                Get key-value pairs as an array of
 *                                {@code Viv.KeyValue<@Nullable Object> }.
 *     </ul>
 *   <li> Other conversion (with running/deserialization function)
 *     <ul>
 *       <li>{@link #getArrayList(Object...) Viv.getArrayList } or
 *           {@link #getArrayListEx(Object...) Viv.getArrayListEx } :
 *                                Get an {@code ArrayList<@Nullable Object>}.
 *       <li>{@link #getHashMap(Object...) Viv.getHashMap } or
 *           {@link #getHashMapEx(Object...) Viv.getHashMapEx } :
 *                                Get a {@code HashMap<String, @Nullable Object>}.
 *     </ul>
 *   <li> String conversion (without running/deserialization function)
 *     <ul>
 *       <li>{@link #makeString(Object) Viv.makeString } :
 *                                Convert into String. Serialize into JSON string.
 *     </ul>
 * </ul>
 *
 * <p>The relationship of this script's value and Java's value are
 * described as below.<br>
 * Note that the following "block" is equivalent to JSON's object.
 * <pre>{@code
 * | This script's value | Java's value                      |
 * |---------------------|-----------------------------------|
 * | boolean             | Boolean                           |
 * | int                 | Long                              |
 * | float               | Double                            |
 * | string              | String                            |
 * | array               | ArrayList<@Nullable Object>       |
 * | block               | HashMap<String, @Nullable Object> |
 * | null                | null                              |
 * }</pre>
 * "{@code @Nullable Object}" of ArrayList/HashMap is also the above
 * right-hand sided value.<br>
 * In {@link #runEx(Object...) Viv.runEx} method, the argument is
 * the above left-hand sided value, script's code, or parsed statement
 * (Refer to {@link #parse(Object...)} Viv.parse). And its returned
 * value is the above right-hand sided value.<br>
 * Then the exception is thrown if error occurs.<br>
 * On the other hand, {@link #run(Object...) Viv.run} method does not
 * throw the exception even if error occurs. Because its returned value
 * is a class instance that has not only the value but also error
 * message.<br>
 * To tell the truth, the returned value of
 * {@link #runEx(Object...) Viv.runEx} method is {@code Object}
 * even if the actual value is {@code Boolean}, {@code Long}, and so
 * on. So conversion functions are available. They can be accepted
 * the above left-hand sided value, script's code, or parsed statement
 * as argument. In other words, they can be used instead of
 * {@link #runEx(Object...) Viv.runEx} method. 
 *
 * <p>Environment: Java 9 or later
 *
 * <p>Last modified: 2025-03-30
 *
 * @author Fumiaki Motegi (motegi@benesult.com)
 */
public class Viv {
  /** The effective file extensions. */
  static final String[] EXTENSIONS = {"viv", "json"};
  /** Number suffixes. */
  static final String[] SUFFIXES = {"st", "nd", "rd"};

  /**
   * JSON data class.
   * In default, both of script's code and JSON value are accepted.
   * This class is used if you want to accept as JSON value rather
   * than script's code.
   * <pre>{@code
   *   String value = "{\"a\": 3}";
   *   String code = "return(a)";
   *   Viv.Result result = Viv.run(new Viv.Json(value), code);
   *   System.out.println(result.value);  // 3
   * }</pre>
   */
  public static class Json {
    /** JSON value. */
    public final String value;

    /**
     * Constructs JSON data class instance.
     *
     * @param value JSON value.
     */
    public Json(String value) {
      this.value = value;
    }
  }

  /** Result data class. */
  public static class Result {
    /**
     * The returned value if the task is succeeded.<br>
     * It will be {@code null} if the task is failed.<br>
     * Note that the returned value may be {@code null}.
     */
    public final @Nullable Object value;
    /**
     * Error message if the task is failed.<br>
     * It will be "" if the task is succeeded.
     */
    public final String errorMessage;

    /** Constructor for Result data class. */
    public Result(@Nullable Object value, String errorMessage) {
      this.value = value;
      this.errorMessage = errorMessage;
    }
  }

  /** Parsed data class. */
  public static class Parsed {
    /**
     * Statements of the given codes if parsing is succeeded.<br>
     * It will be {@code null} if parsing is failed.
     */
    public final @Nullable ArrayList<Statement> statements;
    /** Class instance if it is given, {@code null} otherwise. */
    public final @Nullable Instance instance;
    /**
     * Error message if parsing is failed.<br>
     * It will be "" if parsing is succeeded.
     */
    public final String errorMessage;

    /** Constructor for Parsed data class. */
    public Parsed(@Nullable ArrayList<Statement> statements,
                  @Nullable Instance instance,
                  String errorMessage) {
      this.statements = statements;
      this.instance = instance;
      this.errorMessage = errorMessage;
    }

    /** Constructor for Parsed data class. */
    public Parsed(ArrayList<Statement> statements,
                  @Nullable Instance instance) {
      this.statements = statements;
      this.instance = instance;
      this.errorMessage = "";
    }

    /** Constructor for Parsed data class. */
    public Parsed(ArrayList<Statement> statements) {
      this.statements = statements;
      this.instance = null;
      this.errorMessage = "";
    }

    /** Constructor for Parsed data class. */
    public Parsed(String errorMessage) {
      this.statements = null;
      this.instance = null;
      this.errorMessage = errorMessage;
    }
  }

  /** Instance data class. */
  public static class Instance {
    /**
     * Class instance of VivJson's code if construction is
     * succeeded.<br>
     * It will be {@code null} if construction is failed.
     */
    public final @Nullable Evaluator evaluator;
    /**
     * Error message if construction is failed.<br>
     * It will be "" if construction is succeeded.
     */
    public final String errorMessage;

    public Instance(@Nullable Evaluator evaluator, String errorMessage) {
      this.evaluator = evaluator;
      this.errorMessage = errorMessage;
    }

    public Instance(Evaluator evaluator) {
      this.evaluator = evaluator;
      this.errorMessage = "";
    }

    public Instance(String errorMessage) {
      this.evaluator = null;
      this.errorMessage = errorMessage;
    }
  }

  /**
   * Key-Value pair data class.
   * Mostly, this will be elements of the array.
   * Then it is used to represent HashMap.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 10, "b": 20}
   *   String code = "{\"a\": 10, \"b\": 20}";
   *   try {
   *     Object object = Viv.runEx(code);
   *     Viv.KeyValue<Integer>[] pairs = Viv.getKeyValueIntegersEx(object);
   *     for (Viv.KeyValue<Integer> pair : pairs) {
   *       System.out.println(pair.key + ": " + pair.value);
   *     }
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   */
  public static class KeyValue<T> {
    /** Key of key-value pair. It is so called Name. */
    public final String key;
    /**
     * Value of key-value pair.
     * Its value type is generic. It is assumed that Boolean, Integer,
     * Long, Float, Double, or String.<br>
     * In addition, {@code @Nullable} may be added.
     */
    public final T value;

    public KeyValue(String key, T value) {
      this.key = key;
      this.value = value;
    }
  }

  /**
   * Runs VivJson's code or deserialize JSON objects.
   *
   * <p>For example,
   * <pre>{@code
   *   Viv.Result result = Viv.run("a:3,b:2,return(a+b)");
   *   if (result.errorMessage.isEmpty()) {
   *     System.out.println((Long) result.value);  // 5
   *   }
   *   else {
   *     System.err.println(result.errorMessage);
   *   }
   *
   *   // {"a": 3, "b": [2, 1]}
   *   result = Viv.run("{\"a\": 3, \"b\": [2, 1]}");
   *   if (result.errorMessage.isEmpty()) {
   *     HashMap<String, @Nullable Object> map =
   *         (HashMap<String, @Nullable Object>) result.value;
   *     System.out.println(map.get("a"));  // 3
   *     ArrayList<@Nullable Object> list =
   *         (ArrayList<@Nullable Object>) map.get("b");
   *     System.out.println(list.get(0));  // 2
   *   }
   *   else {
   *     System.err.println(result.errorMessage);
   *   }
   * }</pre>
   *
   * <p>Multiple arguments can be accepted as below.
   * <pre>{@code
   *   - Viv.run("{a:3,b:2}", "return(a+b)");  // 5
   *   - Viv.run("x=", "+", "{a:3,b:2}", "return(x.a+x.b)");  // 5
   *   - Viv.run("test.viv");
   *   - Viv.run("data.json", "calc.viv");
   *   - Viv.run(new Viv.Json("{a:3,b:2}"), "calc.viv");
   *   - ArrayList<Object> array = new ArrayList<>();
   *     array.add(1); array.add(2);
   *     HashMap<String, Object> data = new HashMap<>();
   *     data.put("x", array); data.put("y", true);  // {"x":[1,2],"y":true}
   *     Viv.run(data,"return(if(y){:=x[0]}else{:=x[1]})");  // 1
   *   - Config config = new Config(); config.enableStderr();
   *     Viv.run("x/", config);  // Error
   * }</pre>
   *
   * <p>The above examples are suitable if running times is only one.
   * Otherwise, running after parsing is suitable. Parsing is done with
   * {@link #parse(Object...)}. Then the parsed statement is given as
   * the argument of this method.
   * <pre>{@code
   *   Viv.Parsed parsed = Viv.parse("return(a+b)");
   *
   *   Viv.Result result = Viv.run("{a:3,b:2}", parsed);
   *   System.out.println((Long) result.value);  // 5
   *
   *   HashMap<String, Integer> variables = new HashMap<>();
   *   variables.put("a", 5);
   *   variables.put("b", 7);
   *   result = Viv.run(variables, parsed);
   *   System.out.println((Long) result.value);  // 12
   *
   *   variables.replace("b", 10);
   *   result = Viv.run(variables, parsed);
   *   System.out.println((Long) result.value);  // 15
   * }</pre>
   * Alternatively, {@link #makeInstance(Object...)} is suitable.
   * The example is described in the description of
   * {@link #makeInstance(Object...)}.
   *
   * <p>Note that the returned value is the following Java's Object.
   * <ul>
   *   <li> {@code Boolean }
   *   <li> {@code Long }
   *   <li> {@code Double }
   *   <li> {@code String }
   *   <li> {@code ArrayList<@Nullable Object> }
   *   <li> {@code HashMap<String, @Nullable Object> }
   *   <li> {@code null }
   * </ul>
   * "{@code @Nullable Object}" of ArrayList/HashMap is also the above
   * Object.
   *
   * @param parameters VivJson's codes, JSON values, file paths,
   *                   variables, a Configuration, parsed statements,
   *                   a class instance, a class member, or a calling
   *                   class-method
   *                   <ul>
   *                     <li> A VivJson's code, a JSON value, or
   *                          a file path is given as String.
   *                     <li> Some VivJson's codes, JSON values, file
   *                          paths, variables, or Parsed objects are
   *                          given as {@code ArrayList<Object>}.
   *                     <li> Some variables (name/value pairs) are
   *                          given as
   *                          {@code HashMap<String, @Nullable Object>}.
   *                     <li> A JSON value is given as {@link Json Json}.
   *                     <li> Some configurations are given as
   *                          {@link Config Config}.
   *                     <li> Some parsed statements that are generated
   *                          with {@link #parse(Object...)}.
   *                     <li> A class instance is given as
   *                          {@link Instance Instance}.
   *                     <li> A class member is given as the array of
   *                          Object ({@code Object[]}).<br>
   *                          For example, {@code new Object[]{"foo", "bar"}},
   *                          {@code "return(foo.bar)"}, and
   *                          {@code "return(foo['bar'])"} are equivalent.
   *                          Refer to {@link #makeInstance(Object...)}.
   *                     <li> A calling class-method is given as the
   *                          array of Object ({@code Object[]}).<br>
   *                          Its 1st element is the method name as
   *                          String.<br>
   *                          The following elements are its argument.<br>
   *                          The arguments and the returned value is
   *                          {@code Boolean}, {@code Long},
   *                          {@code Double}, {@code String},
   *                          {@code ArrayList<@Nullable Object>},
   *                          {@code HashMap<String, @Nullable Object>},
   *                          or {@code null}.
   *                          Refer to {@link #makeInstance(Object...)}.
   *                   </ul>
   * @return result and Error message
   *         <ul>
   *           <li> Member's variable {@link Result#value value} has
   *                a result of the given codes or the deserialized
   *                JSON object if success, {@code null} otherwise.
   *           <li> Member's variable {@link Result#errorMessage errorMessage}
   *                has "" if success, the error message otherwise.
   *         </ul>
   */
  public static Result run(Object... parameters) {
    return run(parameters, null);
  }

  /**
   * Runs VivJson's code or deserialize JSON objects.
   *
   * <p>For example,
   * <pre>{@code
   *   String[] strings = new String[] {"{a:3,b:2}", "return(a+b)"};
   *   Viv.Result result = Viv.run(strings, null);
   *   if (result.errorMessage.isEmpty()) {
   *     System.out.println((Long) result.value);  // 5
   *   }
   *   else {
   *     System.err.println(result.errorMessage);
   *   }
   *
   *   // {"a": 3, "b": [2, 1]}
   *   strings = new String[] {"{\"a\": 3, \"b\": [2, 1]}"};
   *   result = Viv.run(strings, null);
   *   if (result.errorMessage.isEmpty()) {
   *     HashMap<String, @Nullable Object> map =
   *         (HashMap<String, @Nullable Object>) result.value;
   *     System.out.println(map.get("a"));  // 3
   *     ArrayList<@Nullable Object> list =
   *         (ArrayList<@Nullable Object>) map.get("b");
   *     System.out.println(list.get(0));  // 2
   *   }
   *   else {
   *     System.err.println(result.errorMessage);
   *   }
   * }</pre>
   *
   * <p>Two arguments are needed. 1st argument is an array of Objects
   * as below. 2nd argument is configuration. {@code null} is given as
   * 2nd argument if configuration is not needed.
   * <pre>{@code
   *   - String[] statements =
   *         new String[] {"x=", "+", "{a:3,b:2}", "return(x.a+x.b)"};
   *     Viv.run(statements, null);
   *   - String[] statements = new String[] {"test.viv"};
   *     Viv.run(statements, null);
   *   - String[] statements = new String[] {"data.json", "calc.viv"};
   *     Viv.run(statements, null);
   *   - Object[] statements = new Object[] {new Viv.Json("{a:3,b:2}"),
   *                                         "calc.viv"};
   *     Viv.run(statements, null);
   *   - ArrayList<Integer> array = new ArrayList<>();
   *     array.add(1); array.add(2);
   *     HashMap<String, Object> data = new HashMap<>();
   *     data.put("x", array); data.put("y", true);  // {"x":[1,2],"y":true}
   *     Object[] objects = new Object[] {data,
   *                                  "return(if(y){:=x[0]}else{:=x[1]})"};
   *     Viv.run(objects, null);
   *   - String[] statements = new String[] {"x/"};
   *     Config config = new Config(); config.enableStderr();
   *     Viv.run(statements, config);  // Error
   * }</pre>
   *
   * <p>The above examples are suitable if running times is only one.
   * Otherwise, running after parsing is suitable. Parsing is done with
   * {@link #parse(Object[], Config)}. Then the parsed statement is
   * given as the argument of this method.
   * <pre>{@code
   *   String[] statements = new String[] {"a*=10,b+=1", "return(a+b)"};
   *   Viv.Parsed parsed = Viv.parse(statements, null);
   *
   *   Object[] objects = new Object[] {"{a:3,b:2}", parsed};
   *   Viv.Result result = Viv.run(objects, null);
   *   System.out.println((Long) result.value);  // 33
   *
   *   HashMap<String, Integer> variables = new HashMap<>();
   *   variables.put("a", 5);
   *   variables.put("b", 7);
   *   objects[0] = variables;
   *   result = Viv.run(objects, null);
   *   System.out.println((Long) result.value);  // 58
   *
   *   variables.replace("b", 10);
   *   result = Viv.run(objects, null);
   *   System.out.println((Long) result.value);  // 61
   * }</pre>
   * Alternatively, {@link #makeInstance(Object[], Config)} is suitable.
   * The example is described in the description of
   * {@link #makeInstance(Object[], Config)}.
   *
   * <p>Note that the returned value is the following Java's Object.
   * <ul>
   *   <li> {@code Boolean }
   *   <li> {@code Long }
   *   <li> {@code Double }
   *   <li> {@code String }
   *   <li> {@code ArrayList<@Nullable Object> }
   *   <li> {@code HashMap<String, @Nullable Object> }
   *   <li> {@code null }
   * </ul>
   * {@code @Nullable Object} of ArrayList/HashMap is also the above
   * Object.
   *
   * @param parameters VivJson's codes, JSON values, file paths,
   *                   variables, parsed statements, a class instance,
   *                   a class member, or a calling class-method
   *                   <ul>
   *                     <li> A VivJson's code, a JSON value, or
   *                          a file path is given as String.
   *                     <li> Some VivJson's codes, JSON values, file
   *                          paths, variables, or Parsed objects are
   *                          given as {@code ArrayList<Object>}.
   *                     <li> Some variables (name/value pairs) are
   *                          given as
   *                          {@code HashMap<String, @Nullable Object>}.
   *                     <li> A JSON value is given as {@link Json Json}.
   *                     <li> Some parsed statements that are generated
   *                          with {@link #parse(Object[], Config)}.
   *                     <li> A class instance is given as
   *                          {@link Instance Instance}.
   *                     <li> A class member is given as the array of
   *                          Object ({@code Object[]}).<br>
   *                          For example, {@code new Object[]{"foo", "bar"}},
   *                          {@code "return(foo.bar)"}, and
   *                          {@code "return(foo['bar'])"} are equivalent.
   *                          Refer to {@link #makeInstance(Object[], Config)}.
   *                     <li> A calling class-method is given as the
   *                          array of Object ({@code Object[]}).<br>
   *                          Its 1st element is the method name as
   *                          String.<br>
   *                          The following elements are its argument.<br>
   *                          The arguments and the returned value is
   *                          {@code Boolean}, {@code Long},
   *                          {@code Double}, {@code String},
   *                          {@code ArrayList<@Nullable Object>},
   *                          {@code HashMap<String, @Nullable Object>},
   *                          or {@code null}.
   *                          Refer to {@link #makeInstance(Object[], Config)}.
   *                   </ul>
   * @param config configuration if it is needed, {@code null} otherwise
   * @return result and Error message
   *         <ul>
   *           <li> Member's variable {@link Result#value value} has
   *                a result of the given codes or the deserialized
   *                JSON object if success, {@code null} otherwise.
   *           <li> Member's variable {@link Result#errorMessage errorMessage}
   *                has "" if success, the error message otherwise.
   *         </ul>
   */
  @SuppressWarnings("unused")
  public static Result run(Object[] parameters, @Nullable Config config) {
    if (parameters == null) {
      String errorMessage = reportError("There is no parameter", false);
      return new Result(null, errorMessage);
    }
    Object[] objects = parseInternal(parameters, config);
    Parsed parsed = (Parsed) objects[0];
    if (!parsed.errorMessage.isEmpty()) {
      return new Result(null, parsed.errorMessage);
    }
    if (config == null) {
      config = (Config) objects[1];
    }

    Result result = runInternal(parsed, config);
    if (!result.errorMessage.isEmpty()) {
      return result;
    }
    @Nullable Object value = Viv.collectOnlyValidElements(result.value);
    if (value == Environment.UNDEFINED) {
      String errorMessage =
              reportError("The returned value is invalid", false);
      return new Result(null, errorMessage);
    }
    return new Result(value, "");
  }

  /**
   * Runs VivJson's code or deserialize JSON objects. (Permit exception)
   *
   * <p>For example,
   * <pre>{@code
   *   try {
   *     Object result = Viv.runEx("a:3,b:2,return(a+b)");
   *     System.out.println((Long) result);  // 5
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   *
   *   try {
   *     // {"a": 3, "b": [2, 1]}
   *     Object result = Viv.runEx("{\"a\": 3, \"b\": [2, 1]}");
   *     HashMap<String, @Nullable Object> map =
   *         (HashMap<String, @Nullable Object>) result;
   *     System.out.println(map.get("a"));  // 3
   *     ArrayList<@Nullable Object> list =
   *         (ArrayList<@Nullable Object>) map.get("b");
   *     System.out.println(list.get(0));  // 2
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   *
   * <p>The detail of the argument and the returned value is described
   * in {@link #run(Object...)}.
   *
   * <p>{@link #parseEx(Object...)} or {@link #makeInstanceEx(Object...)}
   * may be useful if you access member's value/method sometimes.
   *
   * @param parameters VivJson's codes, JSON values, file paths,
   *                   variables, a Configuration, parsed statements,
   *                   a class instance, a class member, or a calling
   *                   class-method
   *                   <ul>
   *                     <li> A VivJson's code, a JSON value, or
   *                          a file path is given as String.
   *                     <li> Some VivJson's codes, JSON values, file
   *                          paths, variables, or Parsed objects are
   *                          given as {@code ArrayList<Object>}.
   *                     <li> Some variables (name/value pairs) are
   *                          given as
   *                          {@code HashMap<String, @Nullable Object>}.
   *                     <li> A JSON value is given as {@link Json Json}.
   *                     <li> a configuration is given as
   *                          {@link Config Config}.
   *                     <li> Some parsed statements that are generated
   *                          with {@link #parseEx(Object...)}.
   *                     <li> A class instance is given as
   *                          {@link Instance Instance}.
   *                     <li> A class member is given as the array of
   *                          Object ({@code Object[]}).<br>
   *                          For example, {@code new Object[]{"foo", "bar"}},
   *                          {@code "return(foo.bar)"}, and
   *                          {@code "return(foo['bar'])"} are equivalent.
   *                          Refer to {@link #makeInstanceEx(Object...)}.
   *                     <li> A calling class-method is given as the
   *                          array of Object ({@code Object[]}).<br>
   *                          Its 1st element is the method name as
   *                          String.<br>
   *                          The following elements are its argument.<br>
   *                          The arguments and the returned value is
   *                          {@code Boolean}, {@code Long},
   *                          {@code Double}, {@code String},
   *                          {@code ArrayList<@Nullable Object>},
   *                          {@code HashMap<String, @Nullable Object>},
   *                          or {@code null}.
   *                          Refer to {@link #makeInstanceEx(Object...)}.
   *                   </ul>
   * @return a result of the given codes if success,
   *         {@code null} otherwise
   * @throws VivException it is thrown if parsing or evaluation is
   *                          failed.
   */
  public static @Nullable Object runEx(Object... parameters)
                                       throws VivException {
    return runEx(parameters, null);
  }

  /**
   * Runs VivJson's code or deserialize JSON objects. (Permit exception)
   *
   * <p>For example,
   * <pre>{@code
   *   String[] strings = new String[] {"{a:3,b:2}", "return(a+b)"};
   *   try {
   *     Object result = Viv.runEx(strings, null);
   *     System.out.println((Long) result);  // 5
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   *
   *   // {"a": 3, "b": [2, 1]}
   *   strings = new String[] {"{\"a\": 3, \"b\": [2, 1]}"};
   *   try {
   *     Object result = Viv.runEx(strings, null);
   *     HashMap<String, @Nullable Object> map =
   *         (HashMap<String, @Nullable Object>) result;
   *     System.out.println(map.get("a"));  // 3
   *     ArrayList<@Nullable Object> list =
   *         (ArrayList<@Nullable Object>) map.get("b");
   *     System.out.println(list.get(0));  // 2
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   *
   * <p>The detail of the argument and the returned value is described
   * in {@link #run(Object[], Config)}.
   *
   * <p>{@link #parseEx(Object[], Config)} or
   * {@link #makeInstanceEx(Object[], Config)} may be useful if
   * you access member's value/method sometimes.
   *
   * @param parameters VivJson's codes, JSON values, file paths,
   *                   variables, parsed statements, a class instance,
   *                   a class member, or a calling class-method
   *                   <ul>
   *                     <li> A VivJson's code, a JSON value, or
   *                          a file path is given as String.
   *                     <li> Some VivJson's codes, JSON values, file
   *                          paths, variables, or Parsed objects are
   *                          given as {@code ArrayList<Object>}.
   *                     <li> Some variables (name/value pairs) are
   *                          given as
   *                          {@code HashMap<String, @Nullable Object>}.
   *                     <li> A JSON value is given as {@link Json Json}.
   *                     <li> Some parsed statements that are generated
   *                          with {@link #parseEx(Object[], Config)}.
   *                     <li> A class instance is given as
   *                          {@link Instance Instance}.
   *                     <li> A class member is given as the array of
   *                          Object ({@code Object[]}).<br>
   *                          For example, {@code new Object[]{"foo", "bar"}},
   *                          {@code "return(foo.bar)"}, and
   *                          {@code "return(foo['bar'])"} are equivalent.
   *                          Refer to {@link #makeInstanceEx(Object[], Config)}.
   *                     <li> A calling class-method is given as the
   *                          array of Object ({@code Object[]}).<br>
   *                          Its 1st element is the method name as
   *                          String.<br>
   *                          The following elements are its argument.<br>
   *                          The arguments and the returned value is
   *                          {@code Boolean}, {@code Long},
   *                          {@code Double}, {@code String},
   *                          {@code ArrayList<@Nullable Object>},
   *                          {@code HashMap<String, @Nullable Object>},
   *                          or {@code null}.
   *                          Refer to {@link #makeInstanceEx(Object[], Config)}.
   *                   </ul>
   * @param config configuration if it is needed, {@code null} otherwise
   * @return a result of the given codes if success,
   *         {@code null} otherwise
   * @throws VivException it is thrown if parsing or evaluation is
   *                          failed.
   */
  public static @Nullable Object runEx(Object[] parameters,
                                       @Nullable Config config)
                                       throws VivException {
    Result result = run(parameters, config);
    if (result.errorMessage.isEmpty()) {
      return result.value;
    }
    throw new VivException(result.errorMessage);
  }

  /**
   * Parses VivJson's code and JSON object.
   *
   * <p>For example,
   * <pre>{@code
   *   Viv.Parsed parsed = Viv.parse("return(a+b)");
   *
   *   Viv.Result result = Viv.run("{a:3,b:2}", parsed);
   *   System.out.println((Long) result.value);  // 5
   * }</pre>
   *
   * <p>Multiple arguments can be accepted as below.
   * <pre>{@code
   *   - Viv.parse("x=", "+", "{a:3,b:2}", "return(x.a+x.b)");
   *   - Viv.parse("test.viv");
   *   - Viv.parse("data.json", "calc.viv");
   *   - Viv.parse(new Viv.Json("{a:3,b:2}"), "calc.viv");
   *   - ArrayList<Integer> array = new ArrayList<>();
   *     array.add(1); array.add(2);
   *     HashMap<String, Object> data = new HashMap<>();
   *     data.put("x", array); data.put("y", true);  // {"x":[1,2],"y":true}
   *     Viv.parse(data,"return(if(y){:=x[0]}else{:=x[1]})");
   *   - Config config = new Config(); config.enableStderr();
   *     Viv.parse("x/", config);  // Error
   * }</pre>
   *
   * @param parameters VivJson's codes, JSON values, file paths,
   *                   variables, a Configuration, parsed statements,
   *                   a class instance, a class member, or a calling
   *                   class-method
   *                   <ul>
   *                     <li> A VivJson's code, a JSON value, or
   *                          a file path is given as String.
   *                     <li> Some VivJson's codes, JSON values, file
   *                          paths, variables, or Parsed objects are
   *                          given as {@code ArrayList<Object>}.
   *                     <li> Some variables (name/value pairs) are
   *                          given as
   *                          {@code HashMap<String, @Nullable Object>}.
   *                     <li> A JSON value is given as {@link Json Json}.
   *                     <li> Some configurations are given as
   *                          {@link Config Config}.
   *                     <li> Some parsed statements ({@link Parsed Parsed})
   *                          that are generated with this method.
   *                          It is passed through toward output.
   *                     <li> A class instance ({@link Instance Instance})
   *                          that is generated with
   *                          {@link #makeInstance(Object...)}.
   *                          It is passed through toward output.
   *                     <li> A class member is given as the array of
   *                          Object ({@code Object[]}).<br>
   *                          For example, {@code new Object[]{"foo", "bar"}},
   *                          {@code "return(foo.bar)"}, and
   *                          {@code "return(foo['bar'])"} are equivalent.
   *                          Refer to {@link #makeInstance(Object...)}.
   *                     <li> A calling class-method is given as the
   *                          array of Object ({@code Object[]}).<br>
   *                          Its 1st element is the method name as
   *                          String.<br>
   *                          The following elements are its argument.<br>
   *                          The arguments and the returned value is
   *                          {@code Boolean}, {@code Long},
   *                          {@code Double}, {@code String},
   *                          {@code ArrayList<@Nullable Object>},
   *                          {@code HashMap<String, @Nullable Object>},
   *                          or {@code null}.
   *                          Refer to {@link #makeInstance(Object...)}.
   *                   </ul>
   * @return statements of script and Error message
   *         <ul>
   *           <li> Member's variable {@link Parsed#statements statements}
   *                has statements of the given codes if success,
   *                {@code null} otherwise.
   *           <li> Member's variable {@link Parsed#errorMessage errorMessage}
   *                has "" if success, the error message otherwise.
   *         </ul>
   */
  public static Parsed parse(Object... parameters) {
    return parse(parameters, null);
  }

  /**
   * Parses VivJson's code and JSON object.
   *
   * <p>For example,
   * <pre>{@code
   *   String[] statements = new String[] {"a*=10,b+=1", "return(a+b)"};
   *   Viv.Parsed parsed = Viv.parse(statements, null);
   *   {
   *     Viv.Result result = Viv.run("{a:3,b:2}", parsed);
   *     System.out.println((Long) result.value);  // 33
   *   }
   *   {
   *     Object[] objects = new Object[] {"{a:3,b:2}", parsed};
   *     Viv.Result result = Viv.run(objects, null);
   *     System.out.println((Long) result.value);  // 33
   *   }
   * }</pre>
   *
   * <p>Two arguments are needed. 1st argument is an array of Objects
   * as below. 2nd argument is configuration. {@code null} is given as
   * 2nd argument if configuration is not needed.<br>
   * For example,
   * <pre>{@code
   *   - String[] statements = new String[] {"{a:3,b:2}", "{return(a+b)}"};
   *     Viv.parse(statements, null);
   *   - String[] statements = new String[] {"x=", "+", "{a:3,b:2}",
   *                                         "return(x.a+x.b)"};
   *     Viv.parse(statements, null);
   *   - String[] statements = new String[] {"test.viv"};
   *     Viv.parse(statements, null);
   *   - String[] statements = new String[] {"data.json", "calc.viv"};
   *     Viv.parse(statements, null);
   *   - Object[] statements = new Object[] {new Viv.Json("{a:3,b:2}"),
   *                                         "calc.viv"};
   *     Viv.parse(statements, null);
   *   - ArrayList<Integer> array = new ArrayList<>();
   *     array.add(1); array.add(2);
   *     HashMap<String, Object> data = new HashMap<>();
   *     data.put("x", array); data.put("y", true);  // {"x":[1,2],"y":true}
   *     Object[] objects = new Object[] {data,
   *                                      "return(if(y){:=x[0]}else{:=x[1]})"};
   *     Viv.parse(objects, null);
   *   - String[] statements = new String[] {"x/"};
   *     Config config = new Config(); config.enableStderr();
   *     Viv.parse(statements, config);  // Error
   * }</pre>
   *
   * @param parameters VivJson's codes, JSON values, file paths,
   *                   variables, parsed statements, a class instance,
   *                   a class member, or a calling class-method
   *                   <ul>
   *                     <li> A VivJson's code, a JSON value, or
   *                          a file path is given as String.
   *                     <li> Some VivJson's codes, JSON values, file
   *                          paths, variables, or Parsed objects are
   *                          given as {@code ArrayList<Object>}.
   *                     <li> Some variables (name/value pairs) are
   *                          given as
   *                          {@code HashMap<String, @Nullable Object>}.
   *                     <li> A JSON value is given as {@link Json Json}.
   *                     <li> Some parsed statements ({@link Parsed Parsed})
   *                          that are generated with this method.
   *                          It is passed through toward output.
   *                     <li> A class instance ({@link Instance Instance})
   *                          that is generated with
   *                          {@link #makeInstance(Object[], Config)}.
   *                          It is passed through toward output.
   *                     <li> A class member is given as the array of
   *                          Object ({@code Object[]}).<br>
   *                          For example, {@code new Object[]{"foo", "bar"}},
   *                          {@code "return(foo.bar)"}, and
   *                          {@code "return(foo['bar'])"} are equivalent.
   *                          Refer to {@link #makeInstance(Object[], Config)}.
   *                     <li> A calling class-method is given as the
   *                          array of Object ({@code Object[]}).<br>
   *                          Its 1st element is the method name as
   *                          String.<br>
   *                          The following elements are its argument.<br>
   *                          The arguments and the returned value is
   *                          {@code Boolean}, {@code Long},
   *                          {@code Double}, {@code String},
   *                          {@code ArrayList<@Nullable Object>},
   *                          {@code HashMap<String, @Nullable Object>},
   *                          or {@code null}.
   *                          Refer to {@link #makeInstance(Object[], Config)}.
   *                   </ul>
   * @param config configuration if it is needed, {@code null} otherwise
   * @return statements of script and Error message
   *         <ul>
   *           <li> Member's variable {@link Parsed#statements statements}
   *                has statements of the given codes if success,
   *                {@code null} otherwise.
   *           <li> Member's variable {@link Parsed#errorMessage errorMessage}
   *                has "" if success, the error message otherwise.
   *         </ul>
   */
  @SuppressWarnings("unused")
  public static Parsed parse(Object[] parameters, @Nullable Config config) {
    if (parameters == null) {
      String errorMessage = reportError("There is no parameter.", false);
      return new Parsed(errorMessage);
    }
    Object[] objects = parseInternal(parameters, config);
    return (Parsed) objects[0];
  }

  /**
   * Parses VivJson's code and JSON object. (Permit exception)
   *
   * <p>For example,
   * <pre>{@code
   *   try {
   *     Viv.Parsed parsed = Viv.parseEx("return(a+b)");
   * 
   *     Object result = Viv.runEx("{a:3,b:2}", parsed);
   *     System.out.println((Long) result);  // 5
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   *
   * <p>The detail of the argument and the returned value is described
   * in {@link #parse(Object...)}.
   *
   * @param parameters VivJson's codes, JSON values, file paths,
   *                   variables, a Configuration, parsed statements,
   *                   a class instance, a class member, or a calling
   *                   class-method
   *                   <ul>
   *                     <li> A VivJson's code, a JSON value, or
   *                          a file path is given as String.
   *                     <li> Some VivJson's codes, JSON values, file
   *                          paths, variables, or Parsed objects are
   *                          given as {@code ArrayList<Object>}.
   *                     <li> Some variables (name/value pairs) are
   *                          given as
   *                          {@code HashMap<String, @Nullable Object>}.
   *                     <li> A JSON value is given as {@link Json Json}.
   *                     <li> Some configurations are given as
   *                          {@link Config Config}.
   *                     <li> Some parsed statements ({@link Parsed Parsed})
   *                          that are generated with this method.
   *                          It is passed through toward output.
   *                     <li> A class instance ({@link Instance Instance})
   *                          that is generated with
   *                          {@link #makeInstanceEx(Object...)}.
   *                          It is passed through toward output.
   *                     <li> A class member is given as the array of
   *                          Object ({@code Object[]}).<br>
   *                          For example, {@code new Object[]{"foo", "bar"}},
   *                          {@code "return(foo.bar)"}, and
   *                          {@code "return(foo['bar'])"} are equivalent.
   *                          Refer to {@link #makeInstanceEx(Object...)}.
   *                     <li> A calling class-method is given as the
   *                          array of Object ({@code Object[]}).<br>
   *                          Its 1st element is the method name as
   *                          String.<br>
   *                          The following elements are its argument.<br>
   *                          The arguments and the returned value is
   *                          {@code Boolean}, {@code Long},
   *                          {@code Double}, {@code String},
   *                          {@code ArrayList<@Nullable Object>},
   *                          {@code HashMap<String, @Nullable Object>},
   *                          or {@code null}.
   *                          Refer to {@link #makeInstanceEx(Object...)}.
   *                   </ul>
   * @return statements of script and Error message
   *         <ul>
   *           <li> Member's variable {@link Parsed#statements statements}
   *                has statements of the given codes if success,
   *                {@code null} otherwise.
   *           <li> Member's variable {@link Parsed#errorMessage errorMessage}
   *                has "" if success, the error message otherwise.
   *         </ul>
   * @throws VivException it is thrown if parsing is failed.
   */
  public static Parsed parseEx(Object... parameters) throws VivException {
    return parseEx(parameters, null);
  }

  /**
   * Parses VivJson's code and JSON object. (Permit exception)
   *
   * <p>For example,
   * <pre>{@code
   *   String[] statements = new String[] {"a*=10,b+=1", "return(a+b)"};
   *   try {
   *     Viv.Parsed parsed = Viv.parseEx(statements, null);
   *
   *       {
   *         Object result = Viv.runEx("{a:3,b:2}", parsed);
   *         System.out.println((Long) result);  // 33
   *       }
   *       {
   *         Object[] objects = new Object[] {"{a:3,b:2}", parsed};
   *         Object result = Viv.runEx(objects, null);
   *         System.out.println((Long) result);  // 33
   *       }
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   *
   * <p>The detail of the argument and the returned value is described
   * in {@link #parse(Object[], Config)}.
   *
   * @param parameters VivJson's codes, JSON values, file paths,
   *                   variables, parsed statements, a class instance,
   *                   a class member, or a calling class-method
   *                   <ul>
   *                     <li> A VivJson's code, a JSON value, or
   *                          a file path is given as String.
   *                     <li> Some VivJson's codes, JSON values, file
   *                          paths, variables, or Parsed objects are
   *                          given as {@code ArrayList<Object>}.
   *                     <li> Some variables (name/value pairs) are
   *                          given as
   *                          {@code HashMap<String, @Nullable Object>}.
   *                     <li> A JSON value is given as {@link Json Json}.
   *                     <li> Some parsed statements ({@link Parsed Parsed})
   *                          that are generated with this method.
   *                          It is passed through toward output.
   *                     <li> A class instance ({@link Instance Instance})
   *                          that is generated with
   *                          {@link #makeInstanceEx(Object[], Config)}.
   *                          It is passed through toward output.
   *                     <li> A class member is given as the array of
   *                          Object ({@code Object[]}).<br>
   *                          For example, {@code new Object[]{"foo", "bar"}},
   *                          {@code "return(foo.bar)"}, and
   *                          {@code "return(foo['bar'])"} are equivalent.
   *                          Refer to {@link #makeInstanceEx(Object[], Config)}.
   *                     <li> A calling class-method is given as the
   *                          array of Object ({@code Object[]}).<br>
   *                          Its 1st element is the method name as
   *                          String.<br>
   *                          The following elements are its argument.<br>
   *                          The arguments and the returned value is
   *                          {@code Boolean}, {@code Long},
   *                          {@code Double}, {@code String},
   *                          {@code ArrayList<@Nullable Object>},
   *                          {@code HashMap<String, @Nullable Object>},
   *                          or {@code null}.
   *                          Refer to {@link #makeInstanceEx(Object[], Config)}.
   *                   </ul>
   * @param config configuration if it is needed, {@code null} otherwise
   * @return statements of script and Error message
   *         <ul>
   *           <li> Member's variable {@link Parsed#statements statements}
   *                has statements of the given codes if success,
   *                {@code null} otherwise.
   *           <li> Member's variable {@link Parsed#errorMessage errorMessage}
   *                has "" if success, the error message otherwise.
   *         </ul>
   * @throws VivException it is thrown if parsing is failed.
   */
  public static Parsed parseEx(Object[] parameters, @Nullable Config config)
                                                    throws VivException {
    Parsed parsed = parse(parameters, config);
    if (parsed.errorMessage.isEmpty()) {
      return parsed;
    }
    throw new VivException(parsed.errorMessage);
  }

  /**
   * Parses VivJson's code and JSON object.
   *
   * @param parameters VivJson's codes, JSON values, file paths,
   *                   variables, a configuration, parsed statements,
   *                   a class instance, a class member, or a calling
   *                   class-method
   *                   <ul>
   *                     <li> A VivJson's code, a JSON value, or
   *                          a file path is given as String.
   *                     <li> Some VivJson's codes, JSON values, file
   *                          paths, variables, or Parsed objects are
   *                          given as {@code ArrayList<Object>}.
   *                     <li> Some variables (name/value pairs) are
   *                          given as
   *                          {@code HashMap<String, @Nullable Object>}.
   *                     <li> A JSON value is given as {@link Json Json}.
   *                     <li> Some configurations are given as
   *                          {@link Config Config}.
   *                     <li> Some parsed statements ({@link Parsed Parsed})
   *                          that are generated with this method.
   *                          It is passed through toward output.
   *                     <li> A class instance ({@link Instance Instance})
   *                          that is generated with
   *                          {@link #makeInstance(Object...)}.
   *                          It is passed through toward output.
   *                     <li> A class member is given as the array of
   *                          Object ({@code Object[]}).<br>
   *                          For example, {@code new Object[]{"foo", "bar"}},
   *                          {@code "return(foo.bar)"}, and
   *                          {@code "return(foo['bar'])"} are equivalent.
   *                          Refer to {@link #makeInstance(Object...)}.
   *                     <li> A calling class-method is given as the
   *                          array of Object ({@code Object[]}).<br>
   *                          Its 1st element is the method name as
   *                          String.<br>
   *                          The following elements are its argument.<br>
   *                          The arguments and the returned value is
   *                          {@code Boolean}, {@code Long},
   *                          {@code Double}, {@code String},
   *                          {@code ArrayList<@Nullable Object>},
   *                          {@code HashMap<String, @Nullable Object>},
   *                          or {@code null}.
   *                          Refer to {@link #makeInstance(Object...)}.
   *                   </ul>
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array that has 2 objects.<br>
   *         1st object is {@link Parsed Parsed}.<br>
   *         2nd object is {@link Config Config} or {@code null}.
   *         This is given from the arguments. When both of "parameters"
   *         and "config" have {@link Config Config} instance, the later
   *         is used.
   */
  private static Object[] parseInternal(Object[] parameters,
                                        @Nullable Config config) {
    Object[] extracted = extractParameters(parameters);

    if (extracted[0] == null) {
      String errorMessage = reportError("Invalid parameter", false);
      return new Object[] {new Parsed(errorMessage), null};
    }
    @SuppressWarnings("unchecked")
    ArrayList<Object> extractedParameters = (ArrayList<Object>) extracted[0];

    Instance instance  = (Instance) extracted[1];
    if ((instance != null)
        && (!instance.errorMessage.isEmpty() || instance.evaluator == null)) {
      String errorMessage = reportError("Illegal instance", false);
      return new Object[] {new Parsed(errorMessage), null};
    }

    if (config == null) {
      config = (Config) extracted[2];
    }

    // Concatenate if + option is given.
    Result concatenated = concatenateParameters(extractedParameters);
    if (!(concatenated.errorMessage.isEmpty())) {
      return new Object[] {new Parsed(concatenated.errorMessage), config};
    }
    @SuppressWarnings("unchecked")
    ArrayList<Object[]> targets = (ArrayList<Object[]>) concatenated.value;
    if (targets == null) {
      String errorMessage =
                reportError("Interpreter side error in Viv.parseInternal",
                            false);
      return new Object[] {new Parsed(errorMessage), config};
    }

    // Parse the given target.
    ArrayList<ArrayList<Statement>> statementsArray = new ArrayList<>();
    for (Object[] target : targets) {
      // target[0]: index of the given argument
      // target[1]: parameter
      ArrayList<Statement> statements = null;
      if (target[1] instanceof Parsed) {  // It is already parsed.
        Parsed parsed = (Parsed) target[1];
        if (!(parsed.errorMessage.isEmpty())) {
          return new Object[] {parsed, config};
        }
        statements = parsed.statements;
      } else if (target[1] instanceof ArrayList) {
        ArrayList<Statement> array = castArrayListStatement(target[1]);
        if (array == null || array.size() != 1) {
          String errorMessage =
                reportError("Interpreter side error in Viv.parseInternal",
                            false);
          return new Object[] {new Parsed(errorMessage), config};
        }
        statements = array;
      } else if (target[1] instanceof HashMap) {  // Java's value
        String location = getArgumentText((Integer) target[0]);
        statements = new ArrayList<>();
        HashMap<String, @Nullable Object> object = castHashMap(target[1]);
        if (object == null) {
          String errorMessage = reportError("Invalid HashMap is found.",
                                            false);
          return new Object[] {new Parsed(errorMessage), config};
        }
        Iterator<Map.Entry<String, @Nullable Object>> iterator =
                                                object.entrySet().iterator();
        while (iterator.hasNext()) {
          @SuppressWarnings("null")
          Map.Entry<String, @Nullable Object> entry = iterator.next();
          @SuppressWarnings("null")
          String key = entry.getKey();
          @Nullable Object value = entry.getValue();
          statements.add(new Injection(key, value, location));
        }
      } else if (getFileExtension(target[1]) != null) {  // File path
        Parsed parsed = parseFile((String) target[1], config);
        if (!(parsed.errorMessage.isEmpty())) {
          return new Object[] {parsed, config};
        }
        statements = parsed.statements;
      } else {  // Text
        Integer index = targets.size() == 1 ? null : (Integer) target[0];
        String text;
        Config configExt;
        if (target[1] instanceof Json) {
          text = ((Json) target[1]).value;
          configExt = (config == null) ? new Config() : new Config(config);
          configExt.enableOnlyJson();
        } else {
          text = (String) target[1];
          configExt = config;
        }
        Parsed parsed = parseText(text, configExt, index);
        if (!(parsed.errorMessage.isEmpty())) {
          return new Object[] {parsed, config};
        }
        statements = parsed.statements;
      }
      if (statements == null) {
        String errorMessage = reportError("No statement", false);
        return new Object[] {new Parsed(errorMessage), config};
      }
      statementsArray.add(statements);
    }

    // Collect implicit variables and fix them.
    ArrayList<Integer> implicitVariables = new ArrayList<>();
    Integer index = 0;
    for (ArrayList<Statement> statements : statementsArray) {
      if (isSettingImplicitVariable(statements)) {
        implicitVariables.add(index);
      }
      index++;
    }
    int count = implicitVariables.size();
    if (count >= 2) {
      index = 0;
      int newIndex = 0;
      for (ArrayList<Statement> statements : statementsArray) {
        if (implicitVariables.contains(index)) {
          fixSettingImplicitVariable(statements, newIndex++);
        }
        index++;
      }

      // Insert "_: [null, ...]" before each assignment, such as "_[0] = 1".
      Identifier identifier = new Identifier(new Token(Token.Type.IDENTIFIER,
                                                       "_"));
      ArrayList<Statement> members = new ArrayList<>();
      members.add(identifier);

      ArrayList<Statement> elements = new ArrayList<>();
      Literal nullLiteral = new Literal(new Token(Token.Type.NULL));
      for (int i = 0; i < count; i++) {
        elements.add(nullLiteral);
      }
      Array value = new Array(elements);

      Set set = new Set(members, new Token(Token.Type.SET), value);

      ArrayList<Statement> statements = new ArrayList<>();
      statements.add(set);
      statementsArray.add(0, statements);
    }

    // Concatenate statements.
    ArrayList<Statement> wholeStatements = new ArrayList<>();
    for (ArrayList<Statement> statements : statementsArray) {
      wholeStatements.addAll(statements);
    }

    // printStatements(wholeStatements);

    return new Object[] {new Parsed(wholeStatements, instance), config};
  }

  /**
   * Parse a file that contains VivJson's code or JSON object.
   * When the file extension is "json", only JSON value is allowed as
   * contents.
   *
   * @param filePath the path of file that has VivJson's code or
   *                 JSON object
   * @param config configuration if needed, {@code null} otherwise
   * @return statements of script and Error message
   *         <ul>
   *           <li> Member's variable {@link Parsed#statements statements}
   *                has statements of the given codes if success,
   *                {@code null} otherwise.
   *           <li> Member's variable {@link Parsed#errorMessage errorMessage}
   *                has "" if success, the error message otherwise.
   *         </ul>
   */
  public static Parsed parseFile(String filePath, @Nullable Config config) {
    String extension = getFileExtension(filePath);
    if (extension == null) {
      String errorMessage =
                      reportError("\"" + filePath + "\" is invalid.", false);
      return new Parsed(errorMessage);
    }
    File file = new File(filePath);
    String fileName = file.getName();
    Path path = Paths.get(filePath);

    String code = null;
    try {
      List<String> lines = Files.readAllLines(path);
      code = String.join("\n", lines);
    } catch (IOException e) {
      ;
    }
    if (code == null) {
      String errorMessage =
          reportError("Cannot read code from \"" + fileName + "\".", false);
      return new Parsed(errorMessage);
    }

    if (extension.equals("json")) {
      Config newConfig = (config == null) ? new Config() : new Config(config);
      newConfig.enableOnlyJson();
      config = newConfig;
    }

    Parser parser = new Parser(code, fileName, config);
    String errorMessage = "";
    try {
      ArrayList<Statement> statements = parser.parse();
      return new Parsed(statements);
    } catch (LexException e) {
      errorMessage = e.getMessage();
    } catch (ParseException e) {
      errorMessage = e.getMessage();
    } catch (Exception e) {
      errorMessage = reportError(e.getMessage(), false);
    }
    return new Parsed(errorMessage);
  }

  /**
   * Parse a file that contains VivJson's code or JSON object. (Permit exception)
   *
   * @param filePath the path of file that has VivJson's code or
   *                 JSON object
   * @param config configuration if needed, {@code null} otherwise
   * @return statements of script and Error message
   *         <ul>
   *           <li> Member's variable {@link Parsed#statements statements}
   *                has statements of the given codes if success,
   *                {@code null} otherwise.
   *           <li> Member's variable {@link Parsed#errorMessage errorMessage}
   *                has "" if success, the error message otherwise.
   *         </ul>
   * @throws VivException it is thrown if parsing is failed.
   */
  public static Parsed parseFileEx(String filePath, @Nullable Config config)
                                                    throws VivException {
    Parsed parsed = parseFile(filePath, config);
    if (parsed.errorMessage.isEmpty()) {
      return parsed;
    }
    throw new VivException(parsed.errorMessage);
  }

  /**
   * Parses a text that is VivJson's code or JSON object.
   *
   * @param text text that is VivJson's code or JSON object
   * @param config configuration if needed, {@code null} otherwise
   * @return statements of script and Error message
   *         <ul>
   *           <li> Member's variable {@link Parsed#statements statements}
   *                has statements of the given codes if success,
   *                {@code null} otherwise.
   *           <li> Member's variable {@link Parsed#errorMessage errorMessage}
   *                has "" if success, the error message otherwise.
   *         </ul>
   */
  public static Parsed parseText(String text, @Nullable Config config) {
    return parseText(text, config, null);
  }

  /**
   * Parses a text that is VivJson's code or JSON object.
   *
   * @param text text that is VivJson's code or JSON object
   * @param config configuration if needed, {@code null} otherwise
   * @param argumentIndex argument index (0~). {@code null} if there is
   *                      only one argument.
   * @return statements of script and Error message
   *         <ul>
   *           <li> Member's variable {@link Parsed#statements statements}
   *                has statements of the given codes if success,
   *                {@code null} otherwise.
   *           <li> Member's variable {@link Parsed#errorMessage errorMessage}
   *                has "" if success, the error message otherwise.
   *         </ul>
   */
  @SuppressWarnings("unused")
  private static Parsed parseText(String text, @Nullable Config config,
                                  @Nullable Integer argumentIndex) {
    if (text == null) {
      String errorMessage = reportError("There is no code.", false);
      return new Parsed(errorMessage);
    }
    String medium = (argumentIndex == null)
                     ? null
                     : getArgumentText(argumentIndex);
    Parser parser = new Parser(text, medium, config);
    String errorMessage = "";
    try {
      ArrayList<Statement> statements = parser.parse();
      return new Parsed(statements);
    } catch (LexException e) {
      errorMessage = e.getMessage();
    } catch (ParseException e) {
      errorMessage = e.getMessage();
    } catch (Exception e) {
      errorMessage = reportError(e.getMessage(), false);
    }
    return new Parsed(errorMessage);
  }

  /**
   * Parses a text that is VivJson's code or JSON object. (Permit exception)
   *
   * @param text text that is VivJson's code or JSON object
   * @param config configuration if needed, {@code null} otherwise
   * @return statements of script and Error message
   *         <ul>
   *           <li> Member's variable {@link Parsed#statements statements}
   *                has statements of the given codes if success,
   *                {@code null} otherwise.
   *           <li> Member's variable {@link Parsed#errorMessage errorMessage}
   *                has "" if success, the error message otherwise.
   *         </ul>
   * @throws VivException it is thrown if parsing is failed.
   */
  public static Parsed parseTextEx(String text, @Nullable Config config)
                                                    throws VivException {
    Parsed parsed = parseText(text, config, null);
    if (parsed.errorMessage.isEmpty()) {
      return parsed;
    }
    throw new VivException(parsed.errorMessage);
  }

  /**
   * Makes a class instance.
   * This method runs the given parameters as Constructor.
   * Then its result will be class instance.
   *
   * <p>For example, JSON object's member is extracted as below.
   * <pre>{@code
   *   // {"a": [10, {"b": null, "c": "test"}], "d": {"e": 3.5}}
   *   Viv.Instance instance = Viv.makeInstance(
   *     "{\"a\": [10, {\"b\": null, \"c\": \"test\"}], \"d\": {\"e\": 3.5}}");
   *   try {
   *     System.out.println(
   *       Viv.runEx(instance, new Object[]{"a",1,"c"}));  // "test"
   *     System.out.println(
   *       Viv.runEx(instance, "return(d.e)"));  // 3.5
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   * {@code new Object[]{"foo", "bar"}}, {@code "return(foo.bar)"},
   * and {@code "return(foo['bar'])"} are equivalent.<br>
   * The valid element of the above Object's array is integer or String.
   * The each hierarchy of JSON object/array is decomposed into the array.
   *
   * <p>Array, boolean, number, or string can be accessed via the implicit
   * variable "_".
   * <pre>{@code
   *   Viv.Instance instance = Viv.makeInstance("100");
   *   try {
   *     System.out.println(Viv.runEx(instance, new Object[]{"_"})); // 100
   *     System.out.println(Viv.runEx(instance, "return(_)")); // 100
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   * When there are some values, {@code _[0], _[1], ...} are assigned.
   * <pre>{@code
   *   // true, false, null, "text", 100L, 1.0
   *   Viv.Instance instance = Viv.makeInstance(
   *               "true", "false", "null", "text", "100", "1.0");
   *   try {
   *     Object[] variable = new Object[] {"_", null};
   *     for (int i = 0; i < 6; i++) {
   *       variable[1] = i;
   *       System.out.println(Viv.runEx(instance, variable));
   *       System.out.println(Viv.runEx(instance, "return(_[" + i + "])"));
   *     }
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   * <pre>{@code
   *   // ["a", 10, [{"b": null, "c": "test"}, false]]
   *   Viv.Instance instance = Viv.makeInstance(
   *           "[\"a\", 10, [{\"b\": null, \"c\": \"test\"}, false]]");
   *   try {
   *     System.out.println(
   *       Viv.runEx(instance, new Object[]{"_", 2, 0, "b"}));  // null
   *     System.out.println(
   *       Viv.runEx(instance, "return(_[2][0]['b'])"));  // null
   *     System.out.println(
   *       Viv.runEx(instance, new Object[]{"_", 2, -2, "c"}));  // "test"
   *     System.out.println(
   *       Viv.runEx(instance, "return(_.2.-2.c)"));  // "test"
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   * Note that the negative index is used for backward access in array.
   *
   * <p>When whole extracted value is needed,
   * {@link #run(Object...) Viv.run } or
   * {@link #runEx(Object...) Viv.runEx } is used without this
   * method.
   * <pre>{@code
   *   try {
   *     // {"a": [10, {"b": null, "c": "test"}], "d": {"e": 3.5}}
   *     Object result = Viv.runEx(
   *       "{\"a\": [10, {\"b\": null, \"c\": \"test\"}], \"d\": {\"e\": 3.5}}");
   *     HashMap<String, @Nullable Object> value =
   *       (HashMap<String, @Nullable Object>) result;
   *     ArrayList<@Nullable Object> array =
   *       (ArrayList<@Nullable Object>) value.get("a");
   *     System.out.println(array.get(0));  // 10
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   *
   * <p>Note that the returned value is the following Java's Object.
   * <ul>
   *   <li> {@code Boolean }
   *   <li> {@code Long }
   *   <li> {@code Double }
   *   <li> {@code String }
   *   <li> {@code ArrayList<@Nullable Object> }
   *   <li> {@code HashMap<String, @Nullable Object> }
   *   <li> {@code null }
   * </ul>
   * "{@code @Nullable Object}" of ArrayList/HashMap is also the above
   * Object.
   *
   * <p>The above "return()", the following "c = a * b, return(c)",
   * and so on are the additional anonymous function.
   * <pre>{@code
   *   // {"a": 3, "b": 2}
   *   Viv.Instance instance = Viv.makeInstance("{\"a\": 3, \"b\": 2}");
   *   try {
   *     System.out.println(Viv.runEx(instance, "return(a)"));  // 3
   *     System.out.println(Viv.runEx(instance, "return(a + b)"));  // 5
   *     // Local variable "c" is created. However it is lost after running task.
   *     System.out.println(Viv.runEx(instance, "c = a * b, return(c)"));  // 6
   *     System.out.println(Viv.runEx(instance, "return(c)"));  // null ("c" is missing.)
   *     // Class member "a" is rewritten. So it is remained after running task.
   *     System.out.println(Viv.runEx(instance, "a += 100, return(a)"));  // 103
   *     System.out.println(Viv.runEx(instance, "return(a)"));  // 103
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   *
   * <p>The class-method can be called as below.
   * <pre>{@code
   *    String code = "function test(x, y) {"
   *                + "  z = x.a + x.b.1 + y"
   *                + "  return(z)"
   *                + "}";
   *    // {"a": 100, "b": [1.0, 2.0]}
   *    ArrayList<Double> list = new ArrayList<>();
   *    list.add(1.0);
   *    list.add(2.0);
   *    HashMap<String, Object> map = new HashMap<>();
   *    map.put("a", 100);
   *    map.put("b", list);
   *
   *    Viv.Instance instance = Viv.makeInstance(code);
   *    try {
   *      Object result = Viv.runEx(instance, new Object[] {"test", map, 3});
   *      System.out.println(result);  // 105.0
   *    } catch (VivException e) {
   *      System.err.println(e.getMessage());
   *    }
   * }</pre>
   * The 1st element of the given array is the method name as String.<br>
   * The following elements are its argument.<br>
   * The arguments and the returned value is the above Java's Object.<br>
   * In arguments of {@link #run(Object...)} and {@link #runEx(Object...)},
   * instance object must be given before the array of Object
   * ({@code Object[]}).
   *
   * @param parameters VivJson's codes, JSON values, file paths,
   *                   variables, a Configuration, or the parsed
   *                   statement
   *                   <ul>
   *                     <li> A VivJson's code, a JSON value, or
   *                          a file path is given as String.
   *                     <li> Some VivJson's codes, JSON values, file
   *                          paths, variables, or Parsed objects are
   *                          given as {@code ArrayList<Object>}.
   *                     <li> Some variables (name/value pairs) are
   *                          given as
   *                          {@code HashMap<String, @Nullable Object>}.
   *                     <li> A JSON value is given as {@link Json Json}.
   *                     <li> Configuration is given as {@link Config Config}.
   *                     <li> The parsed statement that is generated
   *                          with {@link #parse(Object...)}.
   *                   </ul>
   *                   Even if the class instance is contained, it is
   *                   ignored.
   * @return a class instance and Error message
   *         <ul>
   *           <li> Member's variable {@link Instance#evaluator evaluator}
   *                has a class instance if success, {@code null}
   *                otherwise.
   *           <li> Member's variable {@link Instance#errorMessage errorMessage}
   *                has "" if success, the error message otherwise.
   *         </ul>
   */
  public static Instance makeInstance(Object... parameters) {
    return makeInstance(parameters, null);
  }

  /**
   * Makes a class instance.
   * This method runs the given parameters as Constructor.
   * Then its result will be class instance.
   *
   * <p>For example, JSON object's member is extracted as below.
   * <pre>{@code
   *   // {"a": [10, {"b": null, "c": "test"}], "d": {"e": 3.5}}
   *   String[] strings = new String[] {
   *     "{\"a\": [10, {\"b\": null, \"c\": \"test\"}], \"d\": {\"e\": 3.5}}"};
   *   Viv.Instance instance = Viv.makeInstance(strings, null);
   *   try {
   *     System.out.println(
   *       Viv.runEx(instance, new Object[]{"a",1,"c"}));  // "test"
   *     System.out.println(
   *       Viv.runEx(instance, "return(d.e)"));  // 3.5
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   * {@code new Object[]{"foo", "bar"}}, {@code "return(foo.bar)"},
   * and {@code "return(foo['bar'])"} are equivalent.<br>
   * The valid element of the above Object's array is integer or String.
   * The each hierarchy of JSON object/array is decomposed into the array.
   *
   * <p>Array, boolean, number, or string can be accessed via the implicit
   * variable "_".
   * <pre>{@code
   *   String[] strings = new String[] {"100"};
   *   Viv.Instance instance = Viv.makeInstance(strings, null);
   *   try {
   *     System.out.println(Viv.runEx(instance, new Object[]{"_"})); // 100
   *     System.out.println(Viv.runEx(instance, "return(_)")); // 100
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   * When there are some values, {@code _[0], _[1], ...} are assigned.
   * <pre>{@code
   *   // true, false, null, "text", 100L, 1.0
   *   String[] strings =
   *           new String[] {"true", "false", "null", "text", "100", "1.0"};
   *   Viv.Instance instance = Viv.makeInstance(strings, null);
   *   try {
   *     Object[] variable = new Object[] {"_", null};
   *     for (int i = 0; i < 6; i++) {
   *       variable[1] = i;
   *       System.out.println(Viv.runEx(instance, variable));
   *       System.out.println(Viv.runEx(instance, "return(_[" + i + "])"));
   *     }
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   * <pre>{@code
   *   // ["a", 10, [{"b": null, "c": "test"}, false]]
   *   String[] strings =
   *       new String[] {"[\"a\", 10, [{\"b\": null, \"c\": \"test\"}, false]]"};
   *   Viv.Instance instance = Viv.makeInstance(strings, null);
   *   try {
   *     System.out.println(
   *       Viv.runEx(instance, new Object[]{"_", 2, 0, "b"}));  // null
   *     System.out.println(
   *       Viv.runEx(instance, "return(_[2][0]['b'])"));  // null
   *     System.out.println(
   *       Viv.runEx(instance, new Object[]{"_", 2, -2, "c"}));  // "test"
   *     System.out.println(
   *       Viv.runEx(instance, "return(_.2.-2.c)"));  // "test"
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   * Note that the negative index is used for backward access in array.
   *
   * <p>When whole extracted value is needed,
   * {@link #run(Object[], Config) Viv.run } or
   * {@link #runEx(Object[], Config) Viv.runEx } is used without
   * this method.
   * <pre>{@code
   *   // {"a": [10, {"b": null, "c": "test"}], "d": {"e": 3.5}}
   *   String[] strings = new String[] {
   *     "{\"a\": [10, {\"b\": null, \"c\": \"test\"}], \"d\": {\"e\": 3.5}}"};
   *   try {
   *     Object result = Viv.runEx(strings, null);
   *     HashMap<String, @Nullable Object> value =
   *       (HashMap<String, @Nullable Object>) result;
   *     ArrayList<@Nullable Object> array =
   *       (ArrayList<@Nullable Object>) value.get("a");
   *     System.out.println(array.get(0));  // 10
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   *
   * <p>Note that the returned value is the following Java's Object.
   * <ul>
   *   <li> {@code Boolean }
   *   <li> {@code Long }
   *   <li> {@code Double }
   *   <li> {@code String }
   *   <li> {@code ArrayList<@Nullable Object> }
   *   <li> {@code HashMap<String, @Nullable Object> }
   *   <li> {@code null }
   * </ul>
   * "{@code @Nullable Object}" of ArrayList/HashMap is also the above
   * Object.
   *
   * <p>The above "return()", the following "c = a * b, return(c)",
   * and so on are the additional anonymous function.
   * <pre>{@code
   *   // {"a": 3, "b": 2}
   *   String[] strings = new String[] {"{\"a\": 3, \"b\": 2}"};
   *   Viv.Instance instance = Viv.makeInstance(strings, null);
   *   try {
   *     System.out.println(Viv.runEx(instance, "return(a)"));  // 3
   *     System.out.println(Viv.runEx(instance, "return(a + b)"));  // 5
   *     // Local variable "c" is created. However it is lost after running task.
   *     System.out.println(Viv.runEx(instance, "c = a * b, return(c)"));  // 6
   *     System.out.println(Viv.runEx(instance, "return(c)"));  // null ("c" is missing.)
   *     // Class member "a" is rewritten. So it is remained after running task.
   *     System.out.println(Viv.runEx(instance, "a += 100, return(a)"));  // 103
   *     System.out.println(Viv.runEx(instance, "return(a)"));  // 103
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   *
   * <p>The class-method can be called as below.
   * <pre>{@code
   *    String[] codes = new String[] {
   *      "function add3(x, y, z) {return(x + y + z)}",
   *      "function test(x, y) {return(add3(x.a, x.b.1, y))}"
   *    };
   *    // {"a": 100, "b": [1.0, 2.0]}
   *    ArrayList<Double> list = new ArrayList<>();
   *    list.add(1.0);
   *    list.add(2.0);
   *    HashMap<String, Object> map = new HashMap<>();
   *    map.put("a", 100);
   *    map.put("b", list);
   *
   *    Viv.Instance instance = Viv.makeInstance(codes, null);
   *    try {
   *      Object result = Viv.runEx(instance, new Object[] {"test", map, 3});
   *      System.out.println(result);  // 105.0
   *    } catch (VivException e) {
   *      System.err.println(e.getMessage());
   *    }
   * }</pre>
   * The 1st element of the given array is the method name as String.<br>
   * The following elements are its argument.<br>
   * The arguments and the returned value is the above Java's Object.<br>
   * In arguments of {@link #run(Object[], Config)} and
   * {@link #run(Object[], Config)}, instance object must be given
   * before the array of Object ({@code Object[]}).
   *
   * @param parameters VivJson's codes, JSON values, file paths,
   *                   variables, or the parsed statement
   *                   <ul>
   *                     <li> A VivJson's code, a JSON value, or
   *                          a file path is given as String.
   *                     <li> Some VivJson's codes, JSON values, file
   *                          paths, variables, or Parsed objects are
   *                          given as {@code ArrayList<Object>}.
   *                     <li> Some variables (name/value pairs) are
   *                          given as
   *                          {@code HashMap<String, @Nullable Object>}.
   *                     <li> A JSON value is given as {@link Json Json}.
   *                     <li> The parsed statement that is generated
   *                          with {@link #parse(Object[], Config)}.
   *                   </ul>
   *                   Even if the class instance is contained, it is
   *                   ignored.
   * @param config configuration if needed, {@code null} otherwise
   * @return a class instance and Error message
   *         <ul>
   *           <li> Member's variable {@link Instance#evaluator evaluator}
   *                has a class instance if success, {@code null}
   *                otherwise.
   *           <li> Member's variable {@link Instance#errorMessage errorMessage}
   *                has "" if success, the error message otherwise.
   *         </ul>
   */
  @SuppressWarnings("unused")
  public static Instance makeInstance(Object[] parameters,
                                      @Nullable Config config) {
    if (parameters == null) {
      String errorMessage = reportError("There is no parameter.", false);
      return new Instance(errorMessage);
    }
    Object[] objects = parseInternal(parameters, config);
    Parsed parsed = (Parsed) objects[0];
    if (!parsed.errorMessage.isEmpty()) {
      return new Instance(parsed.errorMessage);
    }
    if (config == null) {
      config = (Config) objects[1];
    }

    ArrayList<Statement> statements = parsed.statements;
    if (statements == null) {
      String errorMessage =
                reportError("Interpreter side error in Viv.run", false);
      return new Instance(errorMessage);
    }
                                    
    return makeInstanceInternal(statements, config);
  }

  /**
   * Makes a class instance. (Permit exception)
   * This method runs the given parameters as Constructor.
   * Then its result will be class instance.
   *
   * <p>For example, JSON object's member is extracted as below.
   * <pre>{@code
   *   try {
   *     // {"a": [10, {"b": null, "c": "test"}], "d": {"e": 3.5}}
   *     Viv.Instance instance = Viv.makeInstanceEx(
   *       "{\"a\": [10, {\"b\": null, \"c\": \"test\"}], \"d\": {\"e\": 3.5}}");
   *     System.out.println(
   *       Viv.runEx(instance, new Object[]{"a",1,"c"}));  // "test"
   *     System.out.println(
   *       Viv.runEx(instance, "return(d.e)"));  // 3.5
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   * {@code new Object[]{"foo", "bar"}}, {@code "return(foo.bar)"},
   * and {@code "return(foo['bar'])"} are equivalent.<br>
   * The valid element of the above Object's array is integer or String.
   * The each hierarchy of JSON object/array is decomposed into the array.
   *
   * <p>The class-method can be called as below.
   * <pre>{@code
   *    String code = "function test(x, y) {"
   *                + "  z = x.a + x.b.1 + y"
   *                + "  return(z)"
   *                + "}";
   *    // {"a": 100, "b": [1.0, 2.0]}
   *    ArrayList<Double> list = new ArrayList<>();
   *    list.add(1.0);
   *    list.add(2.0);
   *    HashMap<String, Object> map = new HashMap<>();
   *    map.put("a", 100);
   *    map.put("b", list);
   *
   *    try {
   *      Viv.Instance instance = Viv.makeInstanceEx(code);
   *      Object result = Viv.runEx(instance, new Object[] {"test", map, 3});
   *      System.out.println(result);  // 105.0
   *    } catch (VivException e) {
   *      System.err.println(e.getMessage());
   *    }
   * }</pre>
   * The 1st element of the given array is the method name as String.<br>
   * The following elements are its argument.<br>
   * The arguments and the returned value is the above Java's Object.<br>
   * In arguments of {@link #run(Object...)} and {@link #runEx(Object...)},
   * instance object must be given before the array of Object
   * ({@code Object[]}).
   *
   * <p>The detail of the argument and the returned value is described
   * in {@link #makeInstance(Object...)}.
   *
   * @param parameters VivJson's codes, JSON values, file paths,
   *                   variables, a Configuration, or the parsed
   *                   statement
   *                   <ul>
   *                     <li> A VivJson's code, a JSON value, or
   *                          a file path is given as String.
   *                     <li> Some VivJson's codes, JSON values, file
   *                          paths, variables, or Parsed objects are
   *                          given as {@code ArrayList<Object>}.
   *                     <li> Some variables (name/value pairs) are
   *                          given as
   *                          {@code HashMap<String, @Nullable Object>}.
   *                     <li> A JSON value is given as {@link Json Json}.
   *                     <li> Configuration is given as {@link Config Config}.
   *                     <li> The parsed statement that is generated
   *                          with {@link #parse(Object...)}.
   *                   </ul>
   *                   Even if the class instance is contained, it is
   *                   ignored.
   * @return a class instance and Error message
   *         <ul>
   *           <li> Member's variable {@link Instance#evaluator evaluator}
   *                has a class instance if success, {@code null}
   *                otherwise.
   *           <li> Member's variable {@link Instance#errorMessage errorMessage}
   *                has "" if success, the error message otherwise.
   *         </ul>
   * @throws VivException it is thrown if making is failed.
   */
  public static Instance makeInstanceEx(Object... parameters)
                                        throws VivException {
    return makeInstanceEx(parameters, null);
  }

  /**
   * Makes a class instance. (Permit exception)
   * This method runs the given parameters as Constructor.
   * Then its result will be class instance.
   *
   * <p>For example, JSON object's member is extracted as below.
   * <pre>{@code
   *   // {"a": [10, {"b": null, "c": "test"}], "d": {"e": 3.5}}
   *   String[] strings = new String[] {
   *     "{\"a\": [10, {\"b\": null, \"c\": \"test\"}], \"d\": {\"e\": 3.5}}"};
   *   try {
   *     Viv.Instance instance = Viv.makeInstanceEx(strings, null);
   *     System.out.println(
   *       Viv.runEx(instance, new Object[]{"a",1,"c"}));  // "test"
   *     System.out.println(
   *       Viv.runEx(instance, "return(d.e)"));  // 3.5
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   * {@code new Object[]{"foo", "bar"}}, {@code "return(foo.bar)"},
   * and {@code "return(foo['bar'])"} are equivalent.<br>
   * The valid element of the above Object's array is integer or String.
   * The each hierarchy of JSON object/array is decomposed into the array.
   *
   * <p>The class-method can be called as below.
   * <pre>{@code
   *    String[] codes = new String[] {
   *      "function add3(x, y, z) {return(x + y + z)}",
   *      "function test(x, y) {return(add3(x.a, x.b.1, y))}"
   *    };
   *    // {"a": 100, "b": [1.0, 2.0]}
   *    ArrayList<Double> list = new ArrayList<>();
   *    list.add(1.0);
   *    list.add(2.0);
   *    HashMap<String, Object> map = new HashMap<>();
   *    map.put("a", 100);
   *    map.put("b", list);
   *
   *    try {
   *      Viv.Instance instance = Viv.makeInstanceEx(codes, null);
   *      Object result = Viv.runEx(instance, new Object[] {"test", map, 3});
   *      System.out.println(result);  // 105.0
   *    } catch (VivException e) {
   *      System.err.println(e.getMessage());
   *    }
   * }</pre>
   * The 1st element of the given array is the method name as String.<br>
   * The following elements are its argument.<br>
   * The arguments and the returned value is the above Java's Object..<br>
   * In arguments of {@link #run(Object[], Config)} and
   * {@link #run(Object[], Config)}, instance object must be given
   * before the array of Object ({@code Object[]}).
   *
   * <p>The detail of the argument and the returned value is described
   * in {@link #makeInstance(Object[], Config)}.
   *
   * @param parameters VivJson's codes, JSON values, file paths,
   *                   variables, or the parsed statement
   *                   <ul>
   *                     <li> A VivJson's code, a JSON value, or
   *                          a file path is given as String.
   *                     <li> Some VivJson's codes, JSON values, file
   *                          paths, variables, or Parsed objects are
   *                          given as {@code ArrayList<Object>}.
   *                     <li> Some variables (name/value pairs) are
   *                          given as
   *                          {@code HashMap<String, @Nullable Object>}.
   *                     <li> A JSON value is given as {@link Json Json}.
   *                     <li> The parsed statement that is generated
   *                          with {@link #parse(Object[], Config)}.
   *                   </ul>
   *                   Even if the class instance is contained, it is
   *                   ignored.
   * @param config configuration if needed, {@code null} otherwise
   * @return a class instance and Error message
   *         <ul>
   *           <li> Member's variable {@link Instance#evaluator evaluator}
   *                has a class instance if success, {@code null}
   *                otherwise.
   *           <li> Member's variable {@link Instance#errorMessage errorMessage}
   *                has "" if success, the error message otherwise.
   *         </ul>
   * @throws VivException it is thrown if making is failed.
   */
  public static Instance makeInstanceEx(Object[] parameters,
                                        @Nullable Config config)
                                        throws VivException {
    Instance instance = makeInstance(parameters, config);
    if (instance.errorMessage.isEmpty()
        && instance.evaluator != null) {
      return instance;
    }
    throw new VivException(instance.errorMessage);
  }

  /**
   * Runs VivJson with statements.
   *
   * @param parsed parsed data
   * @param config configuration if needed, {@code null} otherwise
   * @return result and Error message
   *         <ul>
   *           <li> Member's variable {@link Result#value value} has
   *                a result of the given codes or the deserialized
   *                JSON object if success, {@code null} otherwise.
   *           <li> Member's variable {@link Result#errorMessage errorMessage}
   *                has "" if success, the error message otherwise.
   *         </ul>
   */
  private static Result runInternal(Parsed parsed, @Nullable Config config) {
    ArrayList<Statement> statements = parsed.statements;
    if (statements == null) {
      String errorMessage =
          reportError("Interpreter side error in Viv.runInternal", false);
      return new Result(null, errorMessage);
    }

    Block block = new Block(statements, Block.ANONYMOUS_FUNCTION);
    @SuppressWarnings("null")
    Evaluator evaluator = (parsed.instance != null
                           && parsed.instance.evaluator != null)
                          ? parsed.instance.evaluator
                          : new Evaluator(config);
    @Nullable Object result = null;
    String errorMessage = "";
    try {
      result = evaluator.evaluate(block);
    } catch (EvaluateException e) {
      evaluator.rewindAfterAbort();
      errorMessage = e.getMessage();
    } catch (Exception e) {
      evaluator.rewindAfterAbort();
      errorMessage = reportError(e.getMessage(), false);
    }
    return new Result(result, errorMessage);
  }

  /**
   * Makes a class instance.
   * This method runs the given statements as Constructor.
   * Then its result will be class instance.
   *
   * @param statements statements of script
   * @param config configuration if needed, {@code null} otherwise
   * @return a class instance and Error message
   *         <ul>
   *           <li> Member's variable {@link Instance#evaluator evaluator}
   *                has a class instance if success, {@code null}
   *                otherwise.
   *           <li> Member's variable {@link Instance#errorMessage errorMessage}
   *                has "" if success, the error message otherwise.
   *         </ul>
   */
  private static Instance makeInstanceInternal(ArrayList<Statement> statements,
                                              @Nullable Config config) {
    Block block = new Block(statements, Block.CLASS_CONSTRUCTOR);
    @Nullable Evaluator evaluator = new Evaluator(config);
    String errorMessage = "";
    try {
      evaluator.evaluate(block);
    } catch (EvaluateException e) {
      evaluator = null;
      errorMessage = e.getMessage();
    } catch (Exception e) {
      evaluator = null;
      errorMessage = reportError(e.getMessage(), false);
    }
    return new Instance(evaluator, errorMessage);
  }

  /**
   * Prints statements.
   *
   * @param statements statements of script.
   *                   {@link Parsed Parsed}, {@code Block},
   *                   or {@code ArrayList<Statement>}.
   */
  public static void printStatements(Object statements) {
    printStatements(statements, false, null);
  }

  /**
   * Prints statements.
   *
   * @param statements statements of script.
   *                   {@link Parsed Parsed}, {@code Block},
   *                   or {@code ArrayList<Statement>}.
   * @param addClassName Class name is added to each statement if this
   *                     is true.
   * @param config configuration if needed, {@code null} otherwise
   */
  public static void printStatements(Object statements, boolean addClassName,
                                     @Nullable Config config) {
    if (statements instanceof Parsed) {
      @Nullable ArrayList<Statement> array = ((Parsed) statements).statements;
      if (array == null) {
        return;
      }
      statements = array;
    } else if (statements instanceof Block) {
      statements = ((Block) statements).values;
    }
    ArrayList<Statement> array = castArrayListStatement(statements);
    if (array == null) {
      return;
    }

    for (Statement statement : array) {
      if (statement instanceof Block) {
        printStatements(statement, addClassName, config);
        continue;
      }
      if (statement instanceof Blank) {
        continue;
      }

      String string = makeString(statement, config);

      if (addClassName) {
        StringBuilder sb = new StringBuilder();
        sb.append(statement.getClass().getSimpleName());
        sb.append("(");
        sb.append(string);
        sb.append(")");
        string = sb.toString();
      }

      System.out.println(string);
    }
  }

  /**
   * Gets a boolean.
   *
   * <p>When the entity of the object is expected as boolean, it can be
   * converted with this method.<br>
   * However, if the evaluated result is not boolean, {@code null} is
   * returned.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   Boolean src = true;
   *   Object obj = src;
   *   // Example
   *   Boolean value = Viv.getBoolean(obj);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("a=2", "return(a==2)");
   *   // Example
   *   Boolean value = Viv.getBoolean(result);
   * }</pre>
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   Boolean value = Viv.getBoolean("a=2", "return(a==2)");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects object that may be a boolean value
   * @return a boolean value.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not boolean.
   */
  public static @Nullable Boolean getBoolean(@Nullable Object... objects) {
    return getBoolean(objects, null);
  }

  /**
   * Gets a boolean.
   *
   * <p>When the entity of the object is expected as boolean, it can be
   * converted with this method.<br>
   * However, if the evaluated result is not boolean, {@code null} is
   * returned.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects object that may be a boolean value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return a boolean value.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not boolean.
   */
  public static @Nullable Boolean getBoolean(@Nullable Object[] objects,
                                             @Nullable Config config) {
    @Nullable Boolean value = null;
    try {
      value = getBooleanEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets a boolean. (Permit exception)
   *
   * <p>When the entity of the object is expected as boolean, it can be
   * converted with this method.<br>
   * However, if the evaluated result is not boolean, the exception is
   * thrown.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   Boolean src = true;
   *   Object obj = src;
   *   // Example
   *   boolean value = Viv.getBooleanEx(obj);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("a=2", "return(a==2)");
   *   // Example
   *   boolean value = Viv.getBooleanEx(object);
   * }</pre>
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   boolean value = Viv.getBooleanEx("a=2", "return(a==2)");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects object that may be a boolean value
   * @return a boolean value
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not boolean.
   */
  public static boolean getBooleanEx(@Nullable Object... objects)
                                     throws VivException {
    return getBooleanEx(objects, null);
  }

  /**
   * Gets a boolean. (Permit exception)
   *
   * <p>When the entity of the object is expected as boolean, it can be
   * converted with this method.<br>
   * However, if the evaluated result is not boolean, the exception is
   * thrown.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects object that may be a boolean value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return a boolean value
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not boolean.
   */
  public static boolean getBooleanEx(@Nullable Object[] objects,
                                     @Nullable Config config)
                                     throws VivException {
    return (Boolean) getObjectEx(objects, config, Boolean.class);
  }

  /**
   * Gets an array of boolean.
   *
   * <p>When the entity of the object is expected as an array of boolean,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as integer, {@code null} is
   * returned.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(true);
   *   array.add(false);
   *   array.add(true);
   *   // Example
   *   boolean[] booleans = Viv.getBooleans(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("return([true, false])");
   *   // Example
   *   boolean[] booleans = Viv.getBooleans(result);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not boolean, this method returns {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   boolean[] booleans = Viv.getBooleans("{\"a\": true, \"b\": false}");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of boolean.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of boolean.
   */
  public static boolean @Nullable [] getBooleans(@Nullable Object... objects) {
    return getBooleans(objects, null);
  }

  /**
   * Gets an array of boolean.
   *
   * <p>When the entity of the object is expected as an array of boolean,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as integer, {@code null} is
   * returned.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not boolean, this method returns {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of boolean.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of boolean.
   */
  public static boolean @Nullable [] getBooleans(@Nullable Object[] objects,
                                                 @Nullable Config config) {
    boolean[] booleans = null;
    try {
      booleans = getBooleansEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return booleans;
  }

  /**
   * Gets an array of boolean. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of boolean,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as integer, the exception
   * is thrown.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(true);
   *   array.add(false);
   *   array.add(true);
   *   // Example
   *   boolean[] booleans = Viv.getBooleansEx(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("return([true, false])");
   *   // Example
   *   boolean[] booleans = Viv.getBooleansEx(object);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not boolean, this method throws the exception.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   boolean[] booleans = Viv.getBooleansEx("{\"a\": true, \"b\": false}");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of boolean
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          boolean.
   */
  public static boolean[] getBooleansEx(@Nullable Object... objects)
                                        throws VivException {
    return getBooleansEx(objects, null);
  }

  /**
   * Gets an array of boolean. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of boolean,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as integer, the exception
   * is thrown.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not boolean, this method throws the exception.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of boolean
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          boolean.
   */
  public static boolean[] getBooleansEx(@Nullable Object[] objects,
                                        @Nullable Config config)
                                        throws VivException {
    Object[] results = getObjectsEx(objects, config, Boolean.class, false);
    boolean[] booleans = new boolean[results.length];
    for (int i = 0; i < booleans.length; i++) {
      booleans[i] = (Boolean) results[i];
    }
    return booleans;
  }

  /**
   * Gets an array of boolean.
   *
   * <p>When the entity of the object is expected as an array of boolean,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as integer, it will be
   * replaced with {@code null}.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(true);
   *   array.add(0);
   *   array.add(false);
   *   // Example
   *   Boolean[] booleans = Viv.getBooleans(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("return([true, 0])");
   *   // Example
   *   Boolean[] booleans = Viv.getBooleans(result);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * returns {@code null}.<br>
   * Even if there is invalid value, such as integer, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   Boolean[] booleans = Viv.getBooleans("{\"a\": true, \"b\": 0}");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of boolean/{@code null}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of boolean.
   */
  public static @Nullable Boolean @Nullable [] getBooleanOrNulls(
                                        @Nullable Object... objects) {
    return getBooleanOrNulls(objects, null);
  }

  /**
   * Gets an array of boolean.
   *
   * <p>When the entity of the object is expected as an array of boolean,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as integer, it will be
   * replaced with {@code null}.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * returns {@code null}.<br>
   * Even if there is invalid value, such as integer, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of boolean/{@code null}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of boolean.
   */
  public static @Nullable Boolean @Nullable [] getBooleanOrNulls(
                                        @Nullable Object[] objects,
                                        @Nullable Config config) {
    Boolean[] booleans = null;
    try {
      booleans = getBooleanOrNullsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return booleans;
  }

  /**
   * Gets an array of boolean. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of boolean,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as integer, it will be
   * replaced with {@code null}.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(true);
   *   array.add(0);
   *   array.add(false);
   *   // Example
   *   Boolean[] booleans = Viv.getBooleanOrNullsEx(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("return([true, 0])");
   *   // Example
   *   Boolean[] booleans = Viv.getBooleanOrNullsEx(object);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * throws the exception.<br>
   * Even if there is invalid value, such as integer, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   Boolean[] booleans = Viv.getBooleanOrNullsEx("{\"a\": true, \"b\": 0}");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of boolean/{@code null}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          boolean.
   */
  public static @Nullable Boolean[] getBooleanOrNullsEx(
                                        @Nullable Object... objects)
                                        throws VivException {
    return getBooleanOrNullsEx(objects, null);
  }

  /**
   * Gets an array of boolean. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of boolean,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as integer, it will be
   * replaced with {@code null}.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * throws the exception.<br>
   * Even if there is invalid value, such as integer, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of boolean/{@code null}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          boolean.
   */
  public static @Nullable Boolean[] getBooleanOrNullsEx(
                                        @Nullable Object[] objects,
                                        @Nullable Config config)
                                        throws VivException {
    Object[] results = getObjectsEx(objects, config, Boolean.class, true);
    Boolean[] booleans = new Boolean[results.length];
    for (int i = 0; i < booleans.length; i++) {
      booleans[i] = (results[i] != null) ? (Boolean) results[i] : null;
    }
    return booleans;
  }

  /**
   * Gets an integer.
   *
   * <p>When the entity of the object is expected as number, it can be
   * converted into integer with this method.<br>
   * However, if the evaluated result is not integer, {@code null} is
   * returned.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   Object obj = 100;
   *   // Example
   *   Integer value = Viv.getInteger(obj);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("return(10)");
   *   // Example
   *   Integer value = Viv.getInteger(result);
   * }</pre>
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   Integer value = Viv.getInteger("return(10)");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects a value that may be number
   * @return an integer value.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of integer.
   */
  public static @Nullable Integer getInteger(@Nullable Object... objects) {
    return getInteger(objects, null);
  }

  /**
   * Gets an integer.
   *
   * <p>When the entity of the object is expected as number, it can be
   * converted into integer with this method.<br>
   * However, if the evaluated result is not integer, {@code null} is
   * returned.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects a value that may be number
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an integer value.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of integer.
   */
  public static @Nullable Integer getInteger(@Nullable Object[] objects,
                                             @Nullable Config config) {
    @Nullable Integer value = null;
    try {
      value = getIntegerEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets an integer. (Permit exception)
   *
   * <p>When the entity of the object is expected as number, it can be
   * converted into integer with this method.<br>
   * However, if the evaluated result is not integer, the exception is
   * thrown.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   Object obj = 100;
   *   // Example
   *   int value = Viv.getIntegerEx(obj);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("return(10)");
   *   // Example
   *   int value = Viv.getIntegerEx(object);
   * }</pre>
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   int value = Viv.getIntegerEx("return(10)");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects a value that may be number
   * @return an integer value
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not integer.
   */
  public static int getIntegerEx(@Nullable Object... objects)
                                 throws VivException {
    return getIntegerEx(objects, null);
  }

  /**
   * Gets an integer. (Permit exception)
   *
   * <p>When the entity of the object is expected as number, it can be
   * converted into integer with this method.<br>
   * However, if the evaluated result is not integer, the exception is
   * thrown.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be number
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an integer value
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not integer.
   */
  public static int getIntegerEx(@Nullable Object[] objects,
                                 @Nullable Config config)
                                 throws VivException {
    Number number = getNumberEx(objects, config);
    return number.intValue();
  }

  /**
   * Gets an array of integer.
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as boolean, {@code null} is
   * returned.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(1);
   *   array.add(3);
   *   array.add(5);
   *   // Example
   *   int[] ints = Viv.getIntegers(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("return([7, 9])");
   *   // Example
   *   int[] ints = Viv.getIntegers(result);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not number, this method returns {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   * <pre>{@code
   *   int[] ints = Viv.getIntegers("return([7, 9])");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of integer.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of integer.
   */
  public static int @Nullable [] getIntegers(@Nullable Object... objects) {
    return getIntegers(objects, null);
  }

  /**
   * Gets an array of integer.
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as boolean, {@code null} is
   * returned.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not number, this method returns {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of integer.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of integer.
   */
  public static int @Nullable [] getIntegers(@Nullable Object[] objects,
                                             @Nullable Config config) {
    int[] ints = null;
    try {
      ints = getIntegersEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return ints;
  }

  /**
   * Gets an array of integer. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as boolean, the exception
   * is thrown.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(1);
   *   array.add(3);
   *   array.add(5);
   *   // Example
   *   int[] ints = Viv.getIntegersEx(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("return([7, 9])");
   *   // Example
   *   int[] ints = Viv.getIntegersEx(object);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not number, this method throws the exception.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   * <pre>{@code
   *   int[] ints = Viv.getIntegersEx("return([7, 9])");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of integer
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          integer.
   */
  public static int[] getIntegersEx(@Nullable Object... objects)
                                    throws VivException {
    return getIntegersEx(objects, null);
  }

  /**
   * Gets an array of integer. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as boolean, the exception
   * is thrown.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not number, this method throws the exception.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of integer
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          integer.
   */
  public static int[] getIntegersEx(@Nullable Object[] objects,
                                    @Nullable Config config)
                                    throws VivException {
    Object[] results = getNumbersEx(objects, config, false);
    int[] numbers = new int[results.length];
    for (int i = 0; i < numbers.length; i++) {
      numbers[i] = ((Number) results[i]).intValue();
    }
    return numbers;
  }

  /**
   * Gets an array of integer.
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(1);
   *   array.add(false);  // --> null
   *   array.add(5);
   *   // Example
   *   Integer[] ints = Viv.getIntegerOrNulls(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("return([7, false])");
   *   // Example
   *   Integer[] ints = Viv.getIntegerOrNulls(result);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * returns {@code null}.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   * <pre>{@code
   *   Integer[] ints = Viv.getIntegerOrNulls("return([7, false])");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of integer/{@code null}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of integer.
   */
  public static @Nullable Integer @Nullable [] getIntegerOrNulls(
                                        @Nullable Object... objects) {
    return getIntegerOrNulls(objects, null);
  }

  /**
   * Gets an array of integer.
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * returns {@code null}.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of integer/{@code null}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of integer.
   */
  public static @Nullable Integer @Nullable [] getIntegerOrNulls(
                                        @Nullable Object[] objects,
                                        @Nullable Config config) {
    Integer[] ints = null;
    try {
      ints = getIntegerOrNullsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return ints;
  }

  /**
   * Gets an array of integer. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(1);
   *   array.add(false);  // --> null
   *   array.add(5);
   *   // Example
   *   Integer[] ints = Viv.getIntegerOrNullsEx(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("return([7, false])");
   *   // Example
   *   Integer[] ints = Viv.getIntegerOrNullsEx(object);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * throws the exception.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   * <pre>{@code
   *   Integer[] ints = Viv.getIntegerOrNullsEx("return([7, false])");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of integer/{@code null}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          integer.
   */
  public static @Nullable Integer[] getIntegerOrNullsEx(
                                        @Nullable Object... objects)
                                        throws VivException {
    return getIntegerOrNullsEx(objects, null);
  }

  /**
   * Gets an array of integer. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * throws the exception.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of integer/{@code null}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          integer.
   */
  public static @Nullable Integer[] getIntegerOrNullsEx(
                                        @Nullable Object[] objects,
                                        @Nullable Config config)
                                        throws VivException {
    Object[] results = getNumbersEx(objects, config, true);
    Integer[] numbers = new Integer[results.length];
    for (int i = 0; i < numbers.length; i++) {
      numbers[i] = (results[i] != null) ? ((Number) results[i]).intValue() : null;
    }
    return numbers;
  }

  /**
   * Gets a long integer.
   *
   * <p>When the entity of the object is expected as number, it can be
   * converted into long integer with this method.<br>
   * However, if the evaluated result is not long integer, {@code null} is
   * returned.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   Object obj = 100;
   *   // Example
   *   Long value = Viv.getLong(obj);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("return(10)");
   *   // Example
   *   Long value = Viv.getLong(result);
   * }</pre>
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   Long value = Viv.getLong("return(10)");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects a value that may be number
   * @return a long integer value.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of long integer.
   */
  public static @Nullable Long getLong(@Nullable Object... objects) {
    return getLong(objects, null);
  }

  /**
   * Gets a long integer.
   *
   * <p>When the entity of the object is expected as number, it can be
   * converted into long integer with this method.<br>
   * However, if the evaluated result is not long integer, {@code null} is
   * returned.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects a value that may be number
   * @param config configuration if it is needed, {@code null} otherwise
   * @return a long integer value.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of long integer.
   */
  public static @Nullable Long getLong(@Nullable Object[] objects,
                                       @Nullable Config config) {
    @Nullable Long value  = null;
    try {
      value = getLongEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets a long integer. (Permit exception)
   *
   * <p>When the entity of the object is expected as number, it can be
   * converted into long integer with this method.<br>
   * However, if the evaluated result is not long integer, the exception is
   * thrown.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   Object obj = 100;
   *   // Example
   *   long value = Viv.getLongEx(obj);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("return(10)");
   *   // Example
   *   long value = Viv.getLongEx(object);
   * }</pre>
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   long value = Viv.getLongEx("return(10)");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects a value that may be number
   * @return a long integer value
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not long integer.
   */
  public static long getLongEx(@Nullable Object... objects)
                               throws VivException {
    return getLongEx(objects, null);
  }

  /**
   * Gets a long integer. (Permit exception)
   *
   * <p>When the entity of the object is expected as number, it can be
   * converted into long integer with this method.<br>
   * However, if the evaluated result is not long integer, the exception is
   * thrown.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be number
   * @param config configuration if it is needed, {@code null} otherwise
   * @return a long integer value
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not long integer.
   */
  public static long getLongEx(@Nullable Object[] objects,
                               @Nullable Config config)
                               throws VivException {
    Number number = getNumberEx(objects, config);
    return number.longValue();
  }

  /**
   * Gets an array of long integer.
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as boolean, {@code null} is
   * returned.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(1);
   *   array.add(3);
   *   array.add(5);
   *   // Example
   *   long[] longs = Viv.getLongs(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("return([7, 9])");
   *   // Example
   *   long[] longs = Viv.getLongs(result);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not number, this method returns {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   * <pre>{@code
   *   long[] longs = Viv.getLongs("return([7, 9])");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of long integer
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of long integer.
   */
  public static long @Nullable [] getLongs(@Nullable Object... objects) {
    return getLongs(objects, null);
  }

  /**
   * Gets an array of long integer.
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as boolean, {@code null} is
   * returned.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not number, this method returns {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of long integer.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of long integer.
   */
  public static long @Nullable [] getLongs(@Nullable Object[] objects,
                                           @Nullable Config config) {
    long[] longs = null;
    try {
      longs = getLongsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return longs;
  }

  /**
   * Gets an array of long integer. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as boolean, the exception
   * is thrown.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(1);
   *   array.add(3);
   *   array.add(5);
   *   // Example
   *   long[] longs = Viv.getLongsEx(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("return([7, 9])");
   *   // Example
   *   long[] longs = Viv.getLongsEx(object);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not number, this method throws the exception.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   * <pre>{@code
   *   long[] longs = Viv.getLongsEx("return([7, 9])");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of long integer
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          long integer.
   */
  public static long[] getLongsEx(@Nullable Object... objects)
                                  throws VivException {
    return getLongsEx(objects, null);
  }

  /**
   * Gets an array of long integer. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as boolean, the exception
   * is thrown.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not number, this method throws the exception.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of long integer
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          long integer.
   */
  public static long[] getLongsEx(@Nullable Object[] objects,
                                  @Nullable Config config)
                                  throws VivException {
    Object[] results = getNumbersEx(objects, config, false);
    long[] numbers = new long[results.length];
    for (int i = 0; i < numbers.length; i++) {
      numbers[i] = ((Number) results[i]).longValue();
    }
    return numbers;
  }

  /**
   * Gets an array of long integer.
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(1);
   *   array.add(false);  // --> null
   *   array.add(5);
   *   // Example
   *   Long[] longs = Viv.getLongOrNulls(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("return([7, false])");
   *   // Example
   *   Long[] longs = Viv.getLongOrNulls(result);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * returns {@code null}.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   * <pre>{@code
   *   Long[] longs = Viv.getLongOrNulls("return([7, false])");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of long integer/{@code null}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of long integer.
   */
  public static @Nullable Long @Nullable[] getLongOrNulls(
                                        @Nullable Object... objects) {
    return getLongOrNulls(objects, null);
  }

  /**
   * Gets an array of long integer.
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * returns {@code null}.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of long integer/{@code null}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of long integer.
   */
  public static @Nullable Long @Nullable[] getLongOrNulls(
                                        @Nullable Object[] objects,
                                        @Nullable Config config) {
    Long[] longs = null;
    try {
      longs = getLongOrNullsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return longs;
  }

  /**
   * Gets an array of long integer. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(1);
   *   array.add(false);  // --> null
   *   array.add(5);
   *   // Example
   *   Long[] longs = Viv.getLongOrNullsEx(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("return([7, false])");
   *   // Example
   *   Long[] longs = Viv.getLongOrNullsEx(object);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * throws the exception.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   * <pre>{@code
   *   Long[] longs = Viv.getLongOrNullsEx("return([7, false])");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of long integer/{@code null}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          long integer.
   */
  public static @Nullable Long[] getLongOrNullsEx(
                                        @Nullable Object... objects)
                                        throws VivException {
    return getLongOrNullsEx(objects, null);
  }

  /**
   * Gets an array of long integer. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * throws the exception.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of long integer/{@code null}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          long integer.
   */
  public static @Nullable Long[] getLongOrNullsEx(
                                        @Nullable Object[] objects,
                                        @Nullable Config config)
                                        throws VivException {
    Object[] results = getNumbersEx(objects, config, true);
    Long[] numbers = new Long[results.length];
    for (int i = 0; i < numbers.length; i++) {
      numbers[i] = (results[i] != null) ? ((Number) results[i]).longValue() : null;
    }
    return numbers;
  }

  /**
   * Gets a floating-point number.
   *
   * <p>When the entity of the object is expected as number, it can be
   * converted into floating-point number with this method.<br>
   * However, if the evaluated result is not floating-point number,
   * {@code null} is returned.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   Object obj = 1.0;
   *   // Example
   *   Float value = Viv.getFloat(obj);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("return(1.0)");
   *   // Example
   *   Float value = Viv.getFloat(result);
   * }</pre>
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   Float value = Viv.getFloat("return(1.0)");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects a value that may be number
   * @return a floating-point number value.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of floating-point number.
   */
  public static @Nullable Float getFloat(@Nullable Object... objects) {
    return getFloat(objects, null);
  }

  /**
   * Gets a floating-point number.
   *
   * <p>When the entity of the object is expected as number, it can be
   * converted into floating-point number with this method.<br>
   * However, if the evaluated result is not floating-point number,
   * {@code null} is returned.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects a value that may be number
   * @param config configuration if it is needed, {@code null} otherwise
   * @return a floating-point number value.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of floating-point number.
   */
  public static @Nullable Float getFloat(@Nullable Object[] objects,
                                         @Nullable Config config) {
    @Nullable Float value = null;
    try {
      value = getFloatEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets a floating-point number. (Permit exception)
   *
   * <p>When the entity of the object is expected as number, it can be
   * converted into floating-point number with this method.<br>
   * However, if the evaluated result is not floating-point number,
   * {@code null} is returned.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   Object obj = 1.0;
   *   // Example
   *   float value = Viv.getFloatEx(obj);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("return(1.0)");
   *   // Example
   *   float value = Viv.getFloatEx(object);
   * }</pre>
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   float value = Viv.getFloatEx("return(1.0)");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects a value that may be number
   * @return a floating-point number value
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          floating-point number.
   */
  public static float getFloatEx(@Nullable Object... objects)
                                 throws VivException {
    return getFloatEx(objects, null);
  }

  /**
   * Gets a floating-point number. (Permit exception)
   *
   * <p>When the entity of the object is expected as number, it can be
   * converted into floating-point number with this method.<br>
   * However, if the evaluated result is not floating-point number,
   * {@code null} is returned.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be number
   * @param config configuration if it is needed, {@code null} otherwise
   * @return a floating-point number value
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          floating-point number.
   */
  public static float getFloatEx(@Nullable Object[] objects,
                                 @Nullable Config config)
                                 throws VivException {
    Number number = getNumberEx(objects, config);
    return number.floatValue();
  }

  /**
   * Gets an array of floating-point number.
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as boolean, {@code null} is
   * returned.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(1.0);
   *   array.add(3.0);
   *   array.add(5.0);
   *   // Example
   *   float[] floats = Viv.getFloats(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("return([7.0, 9.0])");
   *   // Example
   *   float[] floats = Viv.getFloats(result);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not number, this method returns {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   * <pre>{@code
   *   float[] floats = Viv.getFloats("return([7.0, 9.0])");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of floating-point number.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of floating-point number.
   */
  public static float @Nullable [] getFloats(@Nullable Object... objects) {
    return getFloats(objects, null);
  }

  /**
   * Gets an array of floating-point number.
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as boolean, {@code null} is
   * returned.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not number, this method returns {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of floating-point number.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of floating-point number.
   */
  public static float @Nullable [] getFloats(@Nullable Object[] objects,
                                             @Nullable Config config) {
    float[] floats = null;
    try {
      floats = getFloatsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return floats;
  }

  /**
   * Gets an array of floating-point number. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as boolean, the exception
   * is thrown.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(1.0);
   *   array.add(3.0);
   *   array.add(5.0);
   *   // Example
   *   float[] floats = Viv.getFloatsEx(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("return([7.0, 9.0])");
   *   // Example
   *   float[] floats = Viv.getFloatsEx(object);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not number, this method throws the exception.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   * <pre>{@code
   *   float[] floats = Viv.getFloatsEx("return([7.0, 9.0])");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of floating-point number
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          floating-point number.
   */
  public static float[] getFloatsEx(@Nullable Object... objects)
                                    throws VivException {
    return getFloatsEx(objects, null);
  }

  /**
   * Gets an array of floating-point number. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as boolean, the exception
   * is thrown.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not number, this method throws the exception.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of floating-point number
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          floating-point number.
   */
  public static float[] getFloatsEx(@Nullable Object[] objects,
                                    @Nullable Config config)
                                    throws VivException {
    Object[] results = getNumbersEx(objects, config, false);
    float[] numbers = new float[results.length];
    for (int i = 0; i < numbers.length; i++) {
      numbers[i] = ((Number) results[i]).floatValue();
    }
    return numbers;
  }

  /**
   * Gets an array of floating-point number.
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(1.0);
   *   array.add(false);  // --> null
   *   array.add(5.0);
   *   // Example
   *   Float[] floats = Viv.getFloatOrNulls(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("return([7.0, false])");
   *   // Example
   *   Float[] floats = Viv.getFloatOrNulls(result);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * returns {@code null}.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   * <pre>{@code
   *   Float[] floats = Viv.getFloatOrNulls("return([7.0, false])");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of floating-point number/{@code null}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of floating-point number.
   */
  public static @Nullable Float @Nullable [] getFloatOrNulls(
                                        @Nullable Object... objects) {
    return getFloatOrNulls(objects, null);
  }

  /**
   * Gets an array of floating-point number.
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * returns {@code null}.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of floating-point number/{@code null}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of floating-point number.
   */
  public static @Nullable Float @Nullable [] getFloatOrNulls(
                                        @Nullable Object[] objects,
                                        @Nullable Config config) {
    Float[] floats = null;
    try {
      floats = getFloatOrNullsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return floats;
  }

  /**
   * Gets an array of floating-point number. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(1.0);
   *   array.add(3.0);
   *   array.add(5.0);
   *   // Example
   *   Float[] floats = Viv.getFloatOrNullsEx(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("return([7.0, 9.0])");
   *   // Example
   *   Float[] floats = Viv.getFloatOrNullsEx(object);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * throws the exception.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   * <pre>{@code
   *   Float[] floats = Viv.getFloatOrNullsEx("return([7.0, 9.0])");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of floating-point number/{@code null}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          floating-point number.
   */
  public static @Nullable Float[] getFloatOrNullsEx(
                                        @Nullable Object... objects)
                                        throws VivException {
    return getFloatOrNullsEx(objects, null);
  }

  /**
   * Gets an array of floating-point number. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * throws the exception.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of floating-point number/{@code null}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          floating-point number.
   */
  public static @Nullable Float[] getFloatOrNullsEx(
                                        @Nullable Object[] objects,
                                        @Nullable Config config)
                                        throws VivException {
    Object[] results = getNumbersEx(objects, config, true);
    Float[] numbers = new Float[results.length];
    for (int i = 0; i < numbers.length; i++) {
      numbers[i] = (results[i] != null) ? ((Number) results[i]).floatValue() : null;
    }
    return numbers;
  }

  /**
   * Gets a double-precision floating-point number.
   *
   * <p>When the entity of the object is expected as number, it can be
   * converted into double-precision floating-point number with this
   * method.<br>
   * However, if the evaluated result is not double-precision
   * floating-point number, {@code null} is returned.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   Object obj = 1.0;
   *   // Example
   *   Double value = Viv.getDouble(obj);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("return(1.0)");
   *   // Example
   *   Double value = Viv.getDouble(result);
   * }</pre>
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   double value = Viv.getDouble("return(1.0)");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects a value that may be number
   * @return a double-precision floating-point number value.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of double-precision floating-point
   *         number.
   */
  public static @Nullable Double getDouble(@Nullable Object... objects) {
    return getDouble(objects, null);
  }

  /**
   * Gets a double-precision floating-point number.
   *
   * <p>When the entity of the object is expected as number, it can be
   * converted into double-precision floating-point number with this
   * method.<br>
   * However, if the evaluated result is not double-precision
   * floating-point number, {@code null} is returned.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects a value that may be number
   * @param config configuration if it is needed, {@code null} otherwise
   * @return a double-precision floating-point number value.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of double-precision floating-point
   *         number.
   */
  public static @Nullable Double getDouble(@Nullable Object[] objects,
                                           @Nullable Config config) {
    @Nullable Double value = null;
    try {
      value = getDoubleEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets a double-precision floating-point number. (Permit exception)
   *
   * <p>When the entity of the object is expected as number, it can be
   * converted into double-precision floating-point number with this
   * method.<br>
   * However, if the evaluated result is not double-precision
   * floating-point number, the exception is thrown.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   Object obj = 1.0;
   *   // Example
   *   double value = Viv.getDoubleEx(obj);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("return(1.0)");
   *   // Example
   *   double value = Viv.getDoubleEx(object);
   * }</pre>
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   double value = Viv.getDoubleEx("return(1.0)");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects a value that may be number
   * @return a double-precision floating-point number value
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          double-precision floating-point number.
   */
  public static double getDoubleEx(@Nullable Object... objects)
                                   throws VivException {
    return getDoubleEx(objects, null);
  }

  /**
   * Gets a double-precision floating-point number. (Permit exception)
   *
   * <p>When the entity of the object is expected as number, it can be
   * converted into double-precision floating-point number with this
   * method.<br>
   * However, if the evaluated result is not double-precision
   * floating-point number, the exception is thrown.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be number
   * @param config configuration if it is needed, {@code null} otherwise
   * @return a double-precision floating-point number value
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          double-precision floating-point number.
   */
  public static double getDoubleEx(@Nullable Object[] objects,
                                   @Nullable Config config)
                                   throws VivException {
    Number number = getNumberEx(objects, config);
    return number.doubleValue();
  }

  /**
   * Gets an array of double-precision floating-point number.
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as boolean, {@code null} is
   * returned.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(1.0);
   *   array.add(3.0);
   *   array.add(5.0);
   *   // Example
   *   double[] doubles = Viv.getDoubles(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("return([7.0, 9.0])");
   *   // Example
   *   double[] doubles = Viv.getDoubles(result);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not number, this method returns {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   * <pre>{@code
   *   double[] doubles = Viv.getDoubles("return([7.0, 9.0])");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of double-precision floating-point number.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of double-precision floating-point
   *         number.
   */
  public static double @Nullable [] getDoubles(@Nullable Object... objects) {
    return getDoubles(objects, null);
  }

  /**
   * Gets an array of double-precision floating-point number.
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as boolean, {@code null} is
   * returned.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not number, this method returns {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of double-precision floating-point number.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of double-precision floating-point
   *         number.
   */
  public static double @Nullable [] getDoubles(@Nullable Object[] objects,
                                               @Nullable Config config) {
    double[] doubles = null;
    try {
      doubles = getDoublesEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return doubles;
  }

  /**
   * Gets an array of double-precision floating-point number. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as boolean, the exception
   * is thrown.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(1.0);
   *   array.add(3.0);
   *   array.add(5.0);
   *   // Example
   *   double[] doubles = Viv.getDoublesEx(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("return([7.0, 9.0])");
   *   // Example
   *   double[] doubles = Viv.getDoublesEx(object);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not number, this method throws the exception.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   * <pre>{@code
   *   double[] doubles = Viv.getDoublesEx("return([7.0, 9.0])");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of double-precision floating-point number
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          double-precision floating-point number.
   */
  public static double[] getDoublesEx(@Nullable Object... objects)
                                      throws VivException {
    return getDoublesEx(objects, null);
  }

  /**
   * Gets an array of double-precision floating-point number. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as boolean, the exception
   * is thrown.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not number, this method throws the exception.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of double-precision floating-point number
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          double-precision floating-point number.
   */
  public static double[] getDoublesEx(@Nullable Object[] objects,
                                      @Nullable Config config)
                                      throws VivException {
    Object[] results = getNumbersEx(objects, config, false);
    double[] numbers = new double[results.length];
    for (int i = 0; i < numbers.length; i++) {
      numbers[i] = ((Number) results[i]).doubleValue();
    }
    return numbers;
  }

  /**
   * Gets an array of double-precision floating-point number.
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(1.0);
   *   array.add(false);  // --> null
   *   array.add(5.0);
   *   // Example
   *   Double[] doubles = Viv.getDoubleOrNulls(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("return([7.0, false])");
   *   // Example
   *   Double[] doubles = Viv.getDoubleOrNulls(result);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * returns {@code null}.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   * <pre>{@code
   *   Double[] doubles = Viv.getDoubleOrNulls("return([7.0, false])");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of double-precision floating-point number/{@code null}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of double-precision floating-point
   *         number.
   */
  public static @Nullable Double @Nullable [] getDoubleOrNulls(
                                        @Nullable Object... objects) {
    return getDoubleOrNulls(objects, null);
  }

  /**
   * Gets an array of double-precision floating-point number.
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * returns {@code null}.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of double-precision floating-point number/{@code null}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of double-precision floating-point
   *         number.
   */
  public static @Nullable Double @Nullable [] getDoubleOrNulls(
                                        @Nullable Object[] objects,
                                        @Nullable Config config) {
    Double[] doubles = null;
    try {
      doubles = getDoubleOrNullsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return doubles;
  }

  /**
   * Gets an array of double-precision floating-point number. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add(1.0);
   *   array.add(false);  // --> null
   *   array.add(5.0);
   *   // Example
   *   Double[] doubles = Viv.getDoubleOrNullsEx(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("return([7.0, false])");
   *   // Example
   *   Double[] doubles = Viv.getDoubleOrNullsEx(object);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * throws the exception.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   * <pre>{@code
   *   Double[] doubles = Viv.getDoubleOrNullsEx("return([7.0, false])");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of double-precision floating-point number/{@code null}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          double-precision floating-point number.
   */
  public static @Nullable Double[] getDoubleOrNullsEx(
                                        @Nullable Object... objects)
                                        throws VivException {
    return getDoubleOrNullsEx(objects, null);
  }

  /**
   * Gets an array of double-precision floating-point number. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of number,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * throws the exception.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of double-precision floating-point number/{@code null}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          double-precision floating-point number.
   */
  public static @Nullable Double[] getDoubleOrNullsEx(
                                        @Nullable Object[] objects,
                                        @Nullable Config config)
                                        throws VivException {
    Object[] results = getNumbersEx(objects, config, true);
    Double[] numbers = new Double[results.length];
    for (int i = 0; i < numbers.length; i++) {
      numbers[i] = (results[i] != null) ? ((Number) results[i]).doubleValue() : null;
    }
    return numbers;
  }

  /**
   * Gets a string.
   *
   * <p>When the entity of the object is expected as string, it can be
   * converted with this method.<br>
   * However, if the evaluated result is not string, {@code null} is
   * returned.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   Object obj = "test";
   *   // Example
   *   String value = Viv.getString(obj);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("a='test'", "return(a)");
   *   // Example
   *   String value = Viv.getString(result);
   * }</pre>
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   String value = Viv.getString("a='test'", "return(a)");
   * }</pre>
   * On the other hand, the following sample may be looked like strange.
   * But the single string is passed through simply.
   * <pre>{@code
   *   String value1 = Viv.getString("return('test')");  // "return('test')"
   *   String value2 = Viv.getString("a='test'", "return(a)");  // "a='test'", "return(a)"
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects object that may be a string value
   * @return a string value.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not string.
   */
  public static @Nullable String getString(@Nullable Object... objects) {
    return getString(objects, null);
  }

  /**
   * Gets a string.
   *
   * <p>When the entity of the object is expected as string, it can be
   * converted with this method.<br>
   * However, if the evaluated result is not string, {@code null} is
   * returned.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects object that may be a string value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return a string value.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not string.
   */
  public static @Nullable String getString(@Nullable Object[] objects,
                                           @Nullable Config config) {
    @Nullable String value = null;
    try {
      value = getStringEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets a string. (Permit exception)
   *
   * <p>When the entity of the object is expected as string, it can be
   * converted with this method.<br>
   * However, if the evaluated result is not string, the exception is
   * thrown.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   Object obj = "test";
   *   // Example
   *   String value = Viv.getStringEx(obj);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("a='test'", "return(a)");
   *   // Example
   *   String value = Viv.getStringEx(object);  // "test"
   * }</pre>
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   String value = Viv.getStringEx("a='test'", "return(a)");  // "test"
   * }</pre>
   * On the other hand, the following sample may be looked like strange.
   * But the single string is passed through simply.
   * <pre>{@code
   *   String value1 = Viv.getStringEx("return('test')");  // "return('test')"
   *   String value2 = Viv.getStringEx("a='test'", "return(a)");  // "a='test'", "return(a)"
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects object that may be a string value
   * @return a string value
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not string.
   */
  public static String getStringEx(@Nullable Object... objects)
                                   throws VivException {
    return getStringEx(objects, null);
  }

  /**
   * Gets a string. (Permit exception)
   *
   * <p>When the entity of the object is expected as string, it can be
   * converted with this method.<br>
   * However, if the evaluated result is not string, the exception is
   * thrown.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects object that may be a string value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return a string value
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not string.
   */
  public static String getStringEx(@Nullable Object[] objects,
                                   @Nullable Config config)
                                   throws VivException {
    return (String) getObjectEx(objects, config, String.class);
  }

  /**
   * Gets an array of string.
   *
   * <p>When the entity of the object is expected as an array of string,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as integer, {@code null} is
   * returned.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add("alpha");
   *   array.add("beta");
   *   array.add("gamma");
   *   // Example
   *   String[] strings = Viv.getStrings(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("return(['alpha', 'beta'])");
   *   // Example
   *   String[] strings = Viv.getStrings(result);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key or value is not String,
   * this method returns {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   String[] strings =
   *       Viv.getStrings("{\"a\": \"alpha\", \"b\": \"beta\"}");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of string.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of string.
   */
  public static String @Nullable [] getStrings(@Nullable Object... objects) {
    return getStrings(objects, null);
  }

  /**
   * Gets an array of string.
   *
   * <p>When the entity of the object is expected as an array of string,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as integer, {@code null} is
   * returned.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key or value is not String,
   * this method returns {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of string.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of string.
   */
  public static String @Nullable [] getStrings(@Nullable Object[] objects,
                                               @Nullable Config config) {
    String[] strings = null;
    try {
      strings = getStringsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return strings;
  }

  /**
   * Gets an array of string. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of string,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as integer, the exception
   * is thrown.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add("alpha");
   *   array.add("beta");
   *   array.add("gamma");
   *   // Example
   *   String[] strings = Viv.getStringsEx(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("return(['alpha', 'beta'])");
   *   // Example
   *   String[] strings = Viv.getStringsEx(object);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key or value is not String,
   * this method throws the exception.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   String[] strings =
   *       Viv.getStringsEx("{\"a\": \"alpha\", \"b\": \"beta\"}");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of string
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          string.
   */
  public static String[] getStringsEx(@Nullable Object... objects)
                                      throws VivException {
    return getStringsEx(objects, null);
  }

  /**
   * Gets an array of String. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of string,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as integer, the exception
   * is thrown.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key or value is not String,
   * this method throws the exception.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of string
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          string.
   */
  public static String[] getStringsEx(@Nullable Object[] objects,
                                      @Nullable Config config)
                                      throws VivException {
    Object[] results = getObjectsEx(objects, config, String.class, false);
    String[] strings = new String[results.length];
    for (int i = 0; i < strings.length; i++) {
      strings[i] = (String) results[i];
    }
    return strings;
  }

  /**
   * Gets an array of string.
   *
   * <p>When the entity of the object is expected as an array of string,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add("alpha");
   *   array.add(false);  // --> null
   *   array.add("gamma");
   *   // Example
   *   String[] strings = Viv.getStringOrNulls(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("return(['alpha', false])");
   *   // Example
   *   String[] strings = Viv.getStringOrNulls(result);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * returns {@code null}.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   String[] strings =
   *       Viv.getStringOrNulls("{\"a\": \"alpha\", \"b\": false}");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of string/{@code null}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of string.
   */
  public static @Nullable String @Nullable [] getStringOrNulls(
                                        @Nullable Object... objects) {
    return getStringOrNulls(objects, null);
  }

  /**
   * Gets an array of string.
   *
   * <p>When the entity of the object is expected as an array of string,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * returns {@code null}.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of string/{@code null}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of string.
   */
  public static @Nullable String @Nullable [] getStringOrNulls(
                                        @Nullable Object[] objects,
                                        @Nullable Config config) {
    String[] strings = null;
    try {
      strings = getStringOrNullsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return strings;
  }

  /**
   * Gets an array of string. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of string,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add("alpha");
   *   array.add(false);  // --> null
   *   array.add("gamma");
   *   // Example
   *   String[] strings = Viv.getStringOrNullsEx(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("return(['alpha', false])");
   *   // Example
   *   String[] strings = Viv.getStringOrNullsEx(object);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * throws the exception.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   String[] strings =
   *       Viv.getStringOrNullsEx("{\"a\": \"alpha\", \"b\": false}");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of string/{@code null}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          string.
   */
  public static @Nullable String[] getStringOrNullsEx(
                                        @Nullable Object... objects)
                                        throws VivException {
    return getStringOrNullsEx(objects, null);
  }

  /**
   * Gets an array of String. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of string,
   * it can be converted with this method.<br>
   * Even if there is invalid element, such as boolean, it will be
   * replaced with {@code null}.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * throws the exception.<br>
   * Even if there is invalid value, such as boolean, it will be replaced
   * with {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of string/{@code null}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          string.
   */
  public static @Nullable String[] getStringOrNullsEx(
                                        @Nullable Object[] objects,
                                        @Nullable Config config)
                                        throws VivException {
    Object[] results = getObjectsEx(objects, config, String.class, true);
    String[] strings = new String[results.length];
    for (int i = 0; i < strings.length; i++) {
      strings[i] = (results[i] != null) ? (String) results[i] : null;
    }
    return strings;
  }

  /**
   * Gets a Number. (Permit exception)
   *
   * <p>When the entity of the object is expected as Number, it can be
   * converted into Number object with this method.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be number
   * @param config configuration if it is needed, {@code null} otherwise
   * @return a Number object
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not number.
   */
  private static Number getNumberEx(@Nullable Object[] objects,
                                   @Nullable Config config)
                                   throws VivException {
    return (Number) getObjectEx(objects, config, Number.class);
  }

  /**
   * Gets an expected Object. (Permit exception)
   *
   * <p>When the entity of the object is expected as the certain class,
   * it can be converted with this method.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects object that may be "expectedClass"
   * @param config configuration if it is needed, {@code null} otherwise
   * @param expectedClass the expected class
   * @return a value or the expected class
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not the expected
   *                          class.
   */
  private static Object getObjectEx(@Nullable Object @Nullable [] objects,
                                    @Nullable Config config,
                                    Class<?> expectedClass)
                                    throws VivException {
    if (objects == null || objects.length == 0) {
      String errorMessage = reportError("There is no parameter.", false);
      throw new VivException(errorMessage);
    }
    if (objects.length == 1) {
      Object object = objects[0];
      if (object instanceof Result) {
        if (!((Result) object).errorMessage.isEmpty()) {
          String errorMessage =
              reportError("The given data has error.", false);
          throw new VivException(errorMessage);
        }
        object = ((Result) object).value;
      }
      if (object == null) {
        objects = null;  // The exception is thrown later.
      } else if (isValidClassType(object, expectedClass)) {
        return object;
      }
    }
    @Nullable Object object = (objects != null) ? runEx(objects, config) : null;
    if (object != null && isValidClassType(object, expectedClass)) {
      return object;
    }
    String type = (object == null) ? "null" : object.getClass().getSimpleName();
    String errorMessage = reportError(type + " is found, though "
                                      + expectedClass.getSimpleName()
                                      + " is expected.",
                                      false);
    throw new VivException(errorMessage);
  }

  /**
   * Gets an array of Number. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of Number,
   * it can be converted with this method.<br>
   * However, if there is invalid element, such as boolean, the exception
   * is thrown.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * throws the exception.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @param unexpectedIsNull the behavior for unexpected class of value.<br>
   *        When the unexpected class of value is found, its value will be
   *        null or the exception is thrown.
   *        <pre>
   *        | unexpectedIsNull | The element's returned value | Exception |
   *        |------------------|------------------------------|-----------|
   *        | false            | -----                        | throw     |
   *        | true             | null                         | not throw |
   *        </pre>
   * @return an array of Number
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          Number.
   */
  private static Object[] getNumbersEx(@Nullable Object[] objects,
                                       @Nullable Config config,
                                       boolean unexpectedIsNull)
                                       throws VivException {
    return getObjectsEx(objects, config, Number.class, unexpectedIsNull);
  }

  /**
   * Gets an array of object.
   *
   * <p>When the entity of the object is expected as an array of
   * {@code Object}, it can be converted with this method.<br>
   * However, if there is {@code null}, {@code null} is returned
   * as the whole returned value.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add("alpha");
   *   array.add(true);
   *   array.add(100);
   *   // Example
   *   Object[] objects = Viv.getObjects(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("return(['alpha', true])");
   *   // Example
   *   Object[] objects = Viv.getObjects(result);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not Object, this method returns {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   Object[] objects =
   *       Viv.getObjects("{\"a\": \"alpha\", \"b\": true}");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of Object.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of Object.
   */
  public static Object @Nullable [] getObjects(@Nullable Object... objects) {
    return getObjects(objects, null);
  }

  /**
   * Gets an array of object.
   *
   * <p>When the entity of the object is expected as an array of
   * {@code Object}, it can be converted with this method.<br>
   * However, if there is {@code null}, {@code null} is returned
   * as the whole returned value.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not Object, this method returns {@code null}.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of Object.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of Object.
   */
  public static Object @Nullable [] getObjects(@Nullable Object[] objects,
                                               @Nullable Config config) {
    Object[] result = null;
    try {
      result = getObjectsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return result;
  }

  /**
   * Gets an array of object. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of
   * {@code Object}, it can be converted with this method.<br>
   * However, if there is {@code null}, the exception is thrown.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add("alpha");
   *   array.add(true);
   *   array.add(100);
   *   // Example
   *   Object[] objects = Viv.getObjectsEx(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("return(['alpha', true])");
   *   // Example
   *   Object[] objects = Viv.getObjectsEx(object);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not Object, this method throws the exception.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   Object[] objects =
   *       Viv.getObjectsEx("{\"a\": \"alpha\", \"b\": true}");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of Object
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          Object.
   */
  public static Object[] getObjectsEx(@Nullable Object... objects)
                                      throws VivException {
    return getObjectsEx(objects, null);
  }

  /**
   * Gets an array of object. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of
   * {@code Object}, it can be converted with this method.<br>
   * However, if there is {@code null}, the exception is thrown.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String or value is
   * not Object, this method throws the exception.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of Object
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          Object.
   */
  public static Object[] getObjectsEx(@Nullable Object[] objects,
                                      @Nullable Config config)
                                      throws VivException {
    return getObjectsEx(objects, config, Object.class, false);
  }

  /**
   * Gets an array of the expected Object. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of Object,
   * it can be converted with this method.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * throws the exception.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @param expectedClass the expected class
   * @param unexpectedIsNull the behavior for unexpected class of value.<br>
   *        When the unexpected class of value is found, its value will be
   *        null or the exception is thrown.
   *        <pre>
   *        | unexpectedIsNull | The element's returned value | Exception |
   *        |------------------|------------------------------|-----------|
   *        | false            | -----                        | throw     |
   *        | true             | null                         | not throw |
   *        </pre>
   * @return an array of the expected Object
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          the expected class.
   */
  private static Object[] getObjectsEx(@Nullable Object[] objects,
                                       @Nullable Config config,
                                       Class<?> expectedClass,
                                       boolean unexpectedIsNull)
                                       throws VivException {
    if (objects == null || objects.length == 0) {
      String errorMessage = reportError("There is no parameter.", false);
      throw new VivException(errorMessage);
    }
    if (objects.length == 1) {
      Object object = objects[0];
      if (object instanceof Result) {
        if (!((Result) object).errorMessage.isEmpty()) {
          String errorMessage =
              reportError("The given data has error.", false);
          throw new VivException(errorMessage);
        }
        object = ((Result) object).value;
      }
      try {
        return getObjectsExInternal(object, expectedClass, unexpectedIsNull);
      } catch (VivException e) {
        ;
      }
    }
    @Nullable Object object = runEx(objects, config);
    return getObjectsExInternal(object, expectedClass, unexpectedIsNull);
  }

  /**
   * Gets an array of the expected Object. (Permit exception)
   *
   * @param object a value that may be an array or a block
   * @param expectedClass the expected class
   * @param unexpectedIsNull the behavior for unexpected class of value.<br>
   *        When the unexpected class of value is found, its value will be
   *        null or the exception is thrown.
   *        <pre>
   *        | unexpectedIsNull | The element's returned value | Exception |
   *        |------------------|------------------------------|-----------|
   *        | false            | -----                        | throw     |
   *        | true             | null                         | not throw |
   *        </pre>
   * @return an array of Object
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not the expected
   *                          class.
   */
  private static Object[] getObjectsExInternal(@Nullable Object object,
                                               Class<?> expectedClass,
                                               boolean unexpectedIsNull)
                                               throws VivException {
    Result result = null;
    if (object instanceof HashMap) {
      @Nullable HashMap<String, @Nullable Object> map = getHashMap(object);
      if (map != null) {
        Object[] objects = new Object[map.size()];
        Iterator<@Nullable Object> iterator = map.values().iterator();
        int index = 0;
        while (iterator.hasNext()) {
          @Nullable Object value = iterator.next();
          result = getExpectedValue(value, expectedClass, unexpectedIsNull);
          if (!result.errorMessage.isEmpty()) {
            break;
          }
          objects[index] = result.value;
          result = null;
          index++;
        }
        if (result == null) {
          return objects;
        }
      }
    } else if (object instanceof ArrayList) {
      @Nullable ArrayList<@Nullable Object> array = getArrayList(object);
      if (array != null) {
        Object[] objects = new Object[array.size()];
        int index = 0;
        for (@Nullable Object value : array) {
          result = getExpectedValue(value, expectedClass, unexpectedIsNull);
          if (!result.errorMessage.isEmpty()) {
            break;
          }
          objects[index] = result.value;
          result = null;
          index++;
        }
        if (result == null) {
          return objects;
        }
      }
    }

    throw new VivException((result == null)
                               ? "Cannot get array." : result.errorMessage);
  }

  /**
   * Gets an array of object.
   *
   * <p>When the entity of the object is expected as an array of
   * {@code Object}, it can be converted with this method.<br>
   * Even if there is {@code null}, it is permitted.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add("alpha");
   *   array.add(null);  // --> null
   *   array.add(100);
   *   // Example
   *   Object[] objects = Viv.getObjectOrNulls(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("return(['alpha', null])");
   *   // Example
   *   Object[] objects = Viv.getObjectOrNulls(result);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this
   * method returns {@code null} as the whole returned value.<br>
   * Even if there is {@code null} as value, it is permitted.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   Object[] objects =
   *       Viv.getObjectOrNulls("{\"a\": \"alpha\", \"b\": null}");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of Object/{@code null}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of Object.
   */
  public static @Nullable Object @Nullable [] getObjectOrNulls(
                                        @Nullable Object... objects) {
    return getObjectOrNulls(objects, null);
  }

  /**
   * Gets an array of object.
   *
   * <p>When the entity of the object is expected as an array of
   * {@code Object}, it can be converted with this method.<br>
   * Even if there is {@code null}, it is permitted.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this
   * method returns {@code null} as the whole returned value.<br>
   * Even if there is {@code null} as value, it is permitted.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of Object/{@code null}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not an array of Object.
   */
  public static @Nullable Object @Nullable [] getObjectOrNulls(
                                        @Nullable Object[] objects,
                                        @Nullable Config config) {
    Object[] result = null;
    try {
      result = getObjectOrNullsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return result;
  }

  /**
   * Gets an array of object. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of
   * {@code Object}, it can be converted with this method.<br>
   * Even if there is {@code null}, it is permitted.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Object> array = new ArrayList<>();
   *   array.add("alpha");
   *   array.add(null);  // --> null
   *   array.add(100);
   *   // Example
   *   Object[] objects = Viv.getObjectOrNullsEx(array);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("return(['alpha', null])");
   *   // Example
   *   Object[] objects = Viv.getObjectOrNullsEx(object);
   * }</pre>
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * throws the exception.<br>
   * Even if there is {@code null} as value, it is permitted.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   Object[] objects =
   *       Viv.getObjectOrNullsEx("{\"a\": \"alpha\", \"b\": null}");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects a value that may be an array or a block
   * @return an array of Object/{@code null}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          Object.
   */
  public static @Nullable Object[] getObjectOrNullsEx(
                                        @Nullable Object... objects)
                                        throws VivException {
    return getObjectOrNullsEx(objects, null);
  }

  /**
   * Gets an array of object. (Permit exception)
   *
   * <p>When the entity of the object is expected as an array of
   * {@code Object}, it can be converted with this method.<br>
   * Even if there is {@code null}, it is permitted.
   *
   * <p>When key-value pairs (a.k.a. block, object, map, dict, hashes,
   * or associative arrays) is given, its values are treated. In other
   * words, keys are ignored. However, if key is not String, this method
   * throws the exception.<br>
   * Even if there is {@code null} as value, it is permitted.<br>
   * The order of values is unknown. The appended order into key-value
   * pairs is ignored.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects a value that may be an array or a block
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of Object/{@code null}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          Object.
   */
  public static @Nullable Object[] getObjectOrNullsEx(
                                        @Nullable Object[] objects,
                                        @Nullable Config config)
                                        throws VivException {
    return getObjectsEx(objects, config, Object.class, true);
  }

  /**
   * Gets an {@code ArrayList<@Nullable Object>}.
   *
   * <p>When the entity of the object is expected as
   * {@code ArrayList<@Nullable Object>},
   * it can be converted with this method.<br>
   * However, if the evaluated result is not
   * {@code ArrayList<@Nullable Object>},
   * {@code null} is returned.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Integer> array = new ArrayList<>();
   *   array.add(10);
   *   array.add(20);
   *   Object obj = array;
   *   // Example
   *   ArrayList<@Nullable Object> value = Viv.getArrayList(obj);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("a=[]", "a+=10", "a+=20", "return(a)");
   *   // Example
   *   ArrayList<@Nullable Object> value = Viv.getArrayList(result);
   *   Object element1 = value.get(0);  // 10
   *   Object element2 = value.get(1);  // 20
   * }</pre>
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   ArrayList<@Nullable Object> value =
   *       Viv.getArrayList("a=[]", "a+=10", "a+=20", "return(a)");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects object that may be a {@code ArrayList<@Nullable Object>}
   *                value
   * @return an {@code ArrayList<@Nullable Object>} value.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code ArrayList<@Nullable Object>}.
   */
  public static @Nullable ArrayList<@Nullable Object> getArrayList(
                                                @Nullable Object... objects) {
    return getArrayList(objects, null);
  }

  /**
   * Gets an {@code ArrayList<@Nullable Object>}.
   *
   * <p>When the entity of the object is expected as
   * {@code ArrayList<@Nullable Object>},
   * it can be converted with this method.<br>
   * However, if the evaluated result is not
   * {@code ArrayList<@Nullable Object>},
   * {@code null} is returned.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects object that may be a {@code ArrayList<@Nullable Object>}
   *                value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an {@code ArrayList<@Nullable Object>} value.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code ArrayList<@Nullable Object>}.
   */
  public static @Nullable ArrayList<@Nullable Object> getArrayList(
                                                @Nullable Object[] objects,
                                                @Nullable Config config) {
    @Nullable ArrayList<@Nullable Object> value = null;
    try {
      value = getArrayListEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets an {@code ArrayList<@Nullable Object>}. (Permit exception)
   *
   * <p>When the entity of the object is expected as
   * {@code ArrayList<@Nullable Object>},
   * it can be converted with this method.<br>
   * However, if the evaluated result is not
   * {@code ArrayList<@Nullable Object>},
   * the exception is thrown.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   ArrayList<Integer> array = new ArrayList<>();
   *   array.add(10);
   *   array.add(20);
   *   Object obj = array;
   *   // Example
   *   ArrayList<@Nullable Object> value = Viv.getArrayListEx(obj);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("a=[]", "a+=10", "a+=20", "return(a)");
   *   // Example
   *   ArrayList<@Nullable Object> value = Viv.getArrayListEx(object);
   *   Object element1 = value.get(0);  // 10
   *   Object element2 = value.get(1);  // 20
   * }</pre>
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   ArrayList<@Nullable Object> value =
   *       Viv.getArrayListEx("a=[]", "a+=10", "a+=20", "return(a)");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects object that may be a {@code ArrayList<@Nullable Object>}
   *                value
   * @return an {@code ArrayList<@Nullable Object>} value
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code ArrayList<@Nullable Object>}.
   */
  public static ArrayList<@Nullable Object> getArrayListEx(
                                                @Nullable Object... objects)
                                                throws VivException {
    return getArrayListEx(objects, null);
  }

  /**
   * Gets an {@code ArrayList<@Nullable Object>}. (Permit exception)
   *
   * <p>When the entity of the object is expected as
   * {@code ArrayList<@Nullable Object>},
   * it can be converted with this method.<br>
   * However, if the evaluated result is not
   * {@code ArrayList<@Nullable Object>},
   * the exception is thrown.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects object that may be a {@code ArrayList<@Nullable Object>}
   *                value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an {@code ArrayList<@Nullable Object>} value
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code ArrayList<@Nullable Object>}.
   */
  public static ArrayList<@Nullable Object> getArrayListEx(
                                                @Nullable Object[] objects,
                                                @Nullable Config config)
                                                throws VivException {
    Object object = getObjectEx(objects, config, ArrayList.class);
    @SuppressWarnings("unchecked")
    ArrayList<@Nullable Object> array = (ArrayList<@Nullable Object>) object;
    return array;
  }

  /**
   * Gets a {@code HashMap<String, @Nullable Object>}.
   *
   * <p>When the entity of the object is expected as
   * {@code HashMap<String, @Nullable Object>},
   * it can be converted with this method.<br>
   * However, if the evaluated result is not
   * {@code HashMap<String, @Nullable Object>},
   * {@code null} is returned.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   HashMap<String, Integer> map = new HashMap<>();
   *   map.put("a", 70);
   *   map.put("b", 30);
   *   Object obj = map;
   *   // Example
   *   HashMap<String, @Nullable Object> value = Viv.getHashMap(obj);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Result result = Viv.run("{\"a\": 70, \"b\": 30}");
   *   // Example
   *   HashMap<String, @Nullable Object> value = Viv.getHashMap(result);
   * }</pre>
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   HashMap<String, @Nullable Object> value =
   *                            Viv.getHashMap("{\"a\": 70, \"b\": 30}");
   * }</pre>
   *
   * @see #run(Object...)
   *
   * @param objects object that may be a {@code HashMap<String, @Nullable Object>}
   *                value
   * @return a {@code HashMap<String, @Nullable Object>} value.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, @Nullable Object>}.
   */
  public static @Nullable HashMap<String, @Nullable Object> getHashMap(
                                                @Nullable Object... objects) {
    return getHashMap(objects, null);
  }

  /**
   * Gets a {@code HashMap<String, @Nullable Object>}.
   *
   * <p>When the entity of the object is expected as
   * {@code HashMap<String, @Nullable Object>},
   * it can be converted with this method.<br>
   * However, if the evaluated result is not
   * {@code HashMap<String, @Nullable Object>},
   * {@code null} is returned.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #run(Object[], Config)
   *
   * @param objects object that may be a {@code HashMap<String, @Nullable Object>}
   *                value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return a {@code HashMap<String, @Nullable Object>} value.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, @Nullable Object>}.
   */
  public static @Nullable HashMap<String, @Nullable Object> getHashMap(
                                                @Nullable Object[] objects,
                                                @Nullable Config config) {
    @Nullable HashMap<String, @Nullable Object> value = null;
    try {
      value = getHashMapEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets a {@code HashMap<String, @Nullable Object>}. (Permit exception)
   *
   * <p>When the entity of the object is expected as
   * {@code HashMap<String, @Nullable Object>},
   * it can be converted with this method.<br>
   * However, if the evaluated result is not
   * {@code HashMap<String, @Nullable Object>},
   * the exception is thrown.
   *
   * <p>In a simply way, one value is given as argument.<br>
   * For example,
   * <pre>{@code
   *   // Prepare
   *   HashMap<String, Integer> map = new HashMap<>();
   *   map.put("a", 70);
   *   map.put("b", 30);
   *   Object obj = map;
   *   // Example
   *   HashMap<String, @Nullable Object> value = Viv.getHashMapEx(obj);
   * }</pre>
   * Mostly, this is used for the returned value of
   * {@link #run(Object...) Viv.run} or
   * {@link #runEx(Object...) Viv.runEx} method.
   * <pre>{@code
   *   // Prepare
   *   Object object = Viv.runEx("{\"a\": 70, \"b\": 30}");
   *   // Example
   *   HashMap<String, @Nullable Object> value = Viv.getHashMapEx(object);
   * }</pre>
   *
   * <p>The same arguments of {@link #run(Object...) Viv.run} and
   * {@link #runEx(Object...) Viv.runEx} can be given.
   * <pre>{@code
   *   HashMap<String, @Nullable Object> value =
   *                            Viv.getHashMapEx("{\"a\": 70, \"b\": 30}");
   * }</pre>
   *
   * @see #runEx(Object...)
   *
   * @param objects object that may be a {@code HashMap<String, @Nullable Object>}
   *                value
   * @return a {@code HashMap<String, @Nullable Object>} value
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, @Nullable Object>}.
   */
  public static HashMap<String, @Nullable Object> getHashMapEx(
                                                @Nullable Object... objects)
                                                throws VivException {
    return getHashMapEx(objects, null);
  }

  /**
   * Gets a {@code HashMap<String, @Nullable Object>}. (Permit exception)
   *
   * <p>When the entity of the object is expected as
   * {@code HashMap<String, @Nullable Object>},
   * it can be converted with this method.<br>
   * However, if the evaluated result is not
   * {@code HashMap<String, @Nullable Object>},
   * the exception is thrown.
   *
   * <p>The same arguments of {@link #run(Object[], Config) Viv.run} and
   * {@link #runEx(Object[], Config) Viv.runEx} can be given.
   *
   * @see #runEx(Object[], Config)
   *
   * @param objects object that may be a {@code HashMap<String, @Nullable Object>}
   *                value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return a {@code HashMap<String, @Nullable Object>} value
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, @Nullable Object>}.
   */
  public static HashMap<String, @Nullable Object> getHashMapEx(
                                                @Nullable Object[] objects,
                                                @Nullable Config config)
                                                throws VivException {
    Object object = getObjectEx(objects, config, HashMap.class);
    @Nullable HashMap<String, @Nullable Object> map = castHashMap(object);
    if (map == null) {
      String errorMessage = reportError("Invalid HashMap is found.", false);
      throw new VivException(errorMessage);
    }
    return map;
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Boolean. When other is given,
   * {@code null} is returned.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": true, "b": false}
   *   String code = "\"a\": true, \"b\": false";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Boolean>[] pairs = Viv.getKeyValueBooleans(code);
   *   for (Viv.KeyValue<Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: false
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(code);
   *   pairs = Viv.getKeyValueBooleans(result);
   *   for (Viv.KeyValue<Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: false
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   code = "\"a\": true, \"b\": null";  // replace
   *   pairs = Viv.getKeyValueBooleans(code);
   *   System.out.println(pairs == null);  // true
   *
   *   code = "\"a\": true, \"b\": 10";  // replace
   *   pairs = Viv.getKeyValueBooleans(code);
   *   System.out.println(pairs == null);  // true
   * }</pre>
   * {@link #getKeyValueBooleanOrNulls(Object...)} is suitable if you
   * want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Boolean>}
   *                value
   * @return an array of key-value pairs as {@code KeyValue<Boolean>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, Boolean>}.
   */
  public static KeyValue<Boolean> @Nullable [] getKeyValueBooleans(
                                                @Nullable Object... objects) {
    return getKeyValueBooleans(objects, null);
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Boolean. When other is given,
   * {@code null} is returned.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": true, "b": false}
   *   String[] codes = {"\"a\": true", "\"b\": false"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Boolean>[] pairs = Viv.getKeyValueBooleans(codes, null);
   *   for (Viv.KeyValue<Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: false
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(codes, null);
   *   pairs = Viv.getKeyValueBooleans(result);
   *   for (Viv.KeyValue<Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: false
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   codes[1] = "\"b\": null";  // replace
   *   pairs = Viv.getKeyValueBooleans(codes, null);
   *   System.out.println(pairs == null);  // true
   *
   *   codes[1] = "\"b\": 10";  // replace
   *   pairs = Viv.getKeyValueBooleans(codes, null);
   *   System.out.println(pairs == null);  // true
   * }</pre>
   * {@link #getKeyValueBooleanOrNulls(Object[], Config)} is suitable
   * if you want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Boolean>}
   *                value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as {@code KeyValue<Boolean>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, Boolean>}.
   */
  public static KeyValue<Boolean> @Nullable [] getKeyValueBooleans(
                                                @Nullable Object[] objects,
                                                @Nullable Config config) {
    KeyValue<Boolean>[] value = null;
    try {
      value = getKeyValueBooleansEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Boolean. When other is given,
   * exception is thrown.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": true, "b": false}
   *   String code = "\"a\": true, \"b\": false";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Boolean>[] pairs = Viv.getKeyValueBooleansEx(code);
   *   for (Viv.KeyValue<Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: false
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(code);
   *   pairs = Viv.getKeyValueBooleansEx(object);
   *   for (Viv.KeyValue<Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: false
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   code = "\"a\": true, \"b\": null"; // replace
   *   pairs = Viv.getKeyValueBooleansEx(code);  // Exception is thrown.
   *
   *   code = "\"a\": true, \"b\": 10"; // replace
   *   pairs = Viv.getKeyValueBooleansEx(code);  // Exception is thrown.
   * }</pre>
   * {@link #getKeyValueBooleanOrNullsEx(Object...)} is suitable if you
   * want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Boolean>}
   *                value
   * @return an array of key-value pairs as {@code KeyValue<Boolean>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, Boolean>}.
   */
  public static KeyValue<Boolean>[] getKeyValueBooleansEx(
                                                @Nullable Object... objects)
                                                throws VivException {
    return getKeyValueBooleansEx(objects, null);
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Boolean. When other is given,
   * exception is thrown.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": true, "b": false}
   *   String[] codes = {"\"a\": true", "\"b\": false"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Boolean>[] pairs = Viv.getKeyValueBooleansEx(codes, null);
   *   for (Viv.KeyValue<Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: false
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(codes, null);
   *   pairs = Viv.getKeyValueBooleansEx(object);
   *   for (Viv.KeyValue<Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: false
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   codes[1] = "\"b\": null";  // replace
   *   pairs = Viv.getKeyValueBooleansEx(codes);  // Exception is thrown.
   *
   *   codes[1] = "\"b\": 10";  // replace
   *   pairs = Viv.getKeyValueBooleansEx(codes);  // Exception is thrown.
   * }</pre>
   * {@link #getKeyValueBooleanOrNullsEx(Object[], Config)} is suitable
   * if you want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Boolean>}
   *                value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as {@code KeyValue<Boolean>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, Boolean>}.
   */
  public static KeyValue<Boolean>[] getKeyValueBooleansEx(
                                                @Nullable Object[] objects,
                                                @Nullable Config config)
                                                throws VivException {
    KeyValue<@Nullable Object>[] keyValues =
        getKeyValueObjectsEx(objects, config, Boolean.class, false);

    @SuppressWarnings({"unchecked", "rawtypes"})
    KeyValue<Boolean>[] keyValueBooleans = new KeyValue[keyValues.length];
    for (int i = 0; i < keyValues.length; i++) {
      Object value = keyValues[i].value;
      if (value == null) {
        String errorMessage = reportError("Value of HashMap is null.", false);
        throw new VivException(errorMessage);
      }
      keyValueBooleans[i] = new KeyValue<>(keyValues[i].key, (Boolean) value);
    }
    return keyValueBooleans;
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Boolean. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": true, "b": null}
   *   String code = "{\"a\": true, \"b\": null}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Boolean>[] pairs =
   *           Viv.getKeyValueBooleanOrNulls(code);
   *   for (Viv.KeyValue<@Nullable Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: null
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(code);
   *   pairs = Viv.getKeyValueBooleanOrNulls(result);
   *   for (Viv.KeyValue<@Nullable Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   pairs = Viv.getKeyValueBooleanOrNulls("{\"a\": true, \"b\": 10}");
   *   for (Viv.KeyValue<@Nullable Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Boolean>} value
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Boolean>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, @Nullable Boolean>}.
   */
  public static KeyValue<@Nullable Boolean> @Nullable []
                      getKeyValueBooleanOrNulls(@Nullable Object... objects) {
    return getKeyValueBooleanOrNulls(objects, null);
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Boolean. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": true, "b": null}
   *   String[] codes = {"\"a\": true", "\"b\": null"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Boolean>[] pairs =
   *           Viv.getKeyValueBooleanOrNulls(codes, null);
   *   for (Viv.KeyValue<@Nullable Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: null
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(codes, null);
   *   pairs = Viv.getKeyValueBooleanOrNulls(result);
   *   for (Viv.KeyValue<@Nullable Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   codes[1] = "\"b\": 10";  // replace
   *   pairs = Viv.getKeyValueBooleanOrNulls(codes, null);
   *   for (Viv.KeyValue<@Nullable Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Boolean>} value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Boolean>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, @Nullable Boolean>}.
   */
  public static KeyValue<@Nullable Boolean> @Nullable []
                      getKeyValueBooleanOrNulls(@Nullable Object[] objects,
                                                @Nullable Config config) {
    KeyValue<@Nullable Boolean>[] value = null;
    try {
      value = getKeyValueBooleanOrNullsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Boolean. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": true, "b": null}
   *   String code = "{\"a\": true, \"b\": null}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Boolean>[] pairs =
   *           Viv.getKeyValueBooleanOrNullsEx(code);
   *   for (Viv.KeyValue<@Nullable Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: null
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(code);
   *   pairs = Viv.getKeyValueBooleanOrNullsEx(object);
   *   for (Viv.KeyValue<@Nullable Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   pairs = Viv.getKeyValueBooleanOrNullsEx("{\"a\": true, \"b\": 10}");
   *   for (Viv.KeyValue<@Nullable Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Boolean>} value
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Boolean>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, @Nullable Boolean>}.
   */
  public static KeyValue<@Nullable Boolean>[] getKeyValueBooleanOrNullsEx(
                                                @Nullable Object... objects)
                                                throws VivException {
    return getKeyValueBooleanOrNullsEx(objects, null);
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Boolean. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": true, "b": null}
   *   String[] codes = {"\"a\": true", "\"b\": null"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Boolean>[] pairs =
   *           Viv.getKeyValueBooleanOrNullsEx(codes, null);
   *   for (Viv.KeyValue<@Nullable Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: null
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(codes, null);
   *   pairs = Viv.getKeyValueBooleanOrNullsEx(object);
   *   for (Viv.KeyValue<@Nullable Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   codes[1] = "\"b\": 10";  // replace
   *   pairs = Viv.getKeyValueBooleanOrNullsEx(codes, null);
   *   for (Viv.KeyValue<@Nullable Boolean> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: true, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Boolean>} value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Boolean>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, @Nullable Boolean>}.
   */
  public static KeyValue<@Nullable Boolean>[] getKeyValueBooleanOrNullsEx(
                                                @Nullable Object[] objects,
                                                @Nullable Config config)
                                                throws VivException {
    KeyValue<@Nullable Object>[] keyValues =
        getKeyValueObjectsEx(objects, config, Boolean.class, true);

    @SuppressWarnings({"unchecked", "rawtypes"})
    KeyValue<@Nullable Boolean>[] keyValueBooleans = new KeyValue[keyValues.length];
    for (int i = 0; i < keyValues.length; i++) {
      Object value = keyValues[i].value;
      @Nullable Boolean bool = (value != null) ? (Boolean) value : null;
      keyValueBooleans[i] = new KeyValue<>(keyValues[i].key, bool);
    }
    return keyValueBooleans;
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Integer. When other is given,
   * {@code null} is returned.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 10, "b": 20}
   *   String code = "{\"a\": 10, \"b\": 20}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Integer>[] pairs = Viv.getKeyValueIntegers(code);
   *   for (Viv.KeyValue<Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: 20
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(code);
   *   pairs = Viv.getKeyValueIntegers(result);
   *   for (Viv.KeyValue<Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: 20
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   code = "{\"a\": 10, \"b\": null}";  // replace
   *   pairs = Viv.getKeyValueIntegers(code);
   *   System.out.println(pairs == null);  // true
   *
   *   code = "{\"a\": 10, \"b\": true}";  // replace
   *   pairs = Viv.getKeyValueIntegers(code);
   *   System.out.println(pairs == null);  // true
   * }</pre>
   * {@link #getKeyValueIntegerOrNulls(Object...)} is suitable if you
   * want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Integer>}
   *                value
   * @return an array of key-value pairs as {@code KeyValue<Integer>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, Integer>}.
   */
  public static KeyValue<Integer> @Nullable [] getKeyValueIntegers(
                                                @Nullable Object... objects) {
    return getKeyValueIntegers(objects, null);
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Integer. When other is given,
   * {@code null} is returned.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 10, "b": 20}
   *   String[] codes = {"\"a\": 10", "\"b\": 20"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Integer>[] pairs = Viv.getKeyValueIntegers(codes, null);
   *   for (Viv.KeyValue<Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: 20
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(codes, null);
   *   pairs = Viv.getKeyValueIntegers(result);
   *   for (Viv.KeyValue<Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: 20
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   codes[1] = "\"b\": null";  // replace
   *   pairs = Viv.getKeyValueIntegers(codes, null);
   *   System.out.println(pairs == null);  // true
   *
   *   codes[1] = "\"b\": true";  // replace
   *   pairs = Viv.getKeyValueIntegers(codes, null);
   *   System.out.println(pairs == null);  // true
   * }</pre>
   * {@link #getKeyValueIntegerOrNulls(Object[], Config)} is suitable
   * if you want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Integer>}
   *                value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as {@code KeyValue<Integer>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, Integer>}.
   */
  public static KeyValue<Integer> @Nullable [] getKeyValueIntegers(
                                                @Nullable Object[] objects,
                                                @Nullable Config config) {
    KeyValue<Integer>[] value = null;
    try {
      value = getKeyValueIntegersEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Integer. When other is given,
   * exception is thrown.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 10, "b": 20}
   *   String code = "{\"a\": 10, \"b\": 20}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Integer>[] pairs = Viv.getKeyValueIntegersEx(code);
   *   for (Viv.KeyValue<Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: 20
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(code);
   *   pairs = Viv.getKeyValueIntegersEx(object);
   *   for (Viv.KeyValue<Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: 20
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   code = "{\"a\": 10, \"b\": null}"; // replace
   *   pairs = Viv.getKeyValueIntegersEx(code);  // Exception is thrown.
   *
   *   code = "{\"a\": 10, \"b\": true}"; // replace
   *   pairs = Viv.getKeyValueIntegersEx(code);  // Exception is thrown.
   * }</pre>
   * {@link #getKeyValueIntegerOrNullsEx(Object...)}  is suitable if you
   * want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Integer>}
   *                value
   * @return an array of key-value pairs as {@code KeyValue<Integer>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, Integer>}.
   */
  public static KeyValue<Integer>[] getKeyValueIntegersEx(
                                                @Nullable Object... objects)
                                                throws VivException {
    return getKeyValueIntegersEx(objects, null);
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Integer. When other is given,
   * exception is thrown.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 10, "b": 20}
   *   String[] codes = {"\"a\": 10", "\"b\": 20"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Integer>[] pairs = Viv.getKeyValueIntegersEx(codes, null);
   *   for (Viv.KeyValue<Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: 20
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(codes, null);
   *   pairs = Viv.getKeyValueIntegersEx(object);
   *   for (Viv.KeyValue<Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: 20
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   codes[1] = "\"b\": null";  // replace
   *   pairs = Viv.getKeyValueIntegersEx(codes, null);  // Exception is thrown.
   *
   *   codes[1] = "\"b\": true";  // replace
   *   pairs = Viv.getKeyValueIntegersEx(codes, null);  // Exception is thrown.
   * }</pre>
   * {@link #getKeyValueIntegerOrNullsEx(Object[], Config)} is suitable
   * if you want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Integer>}
   *                value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as {@code KeyValue<Integer>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, Integer>}.
   */
  public static KeyValue<Integer>[] getKeyValueIntegersEx(
                                                @Nullable Object[] objects,
                                                @Nullable Config config)
                                                throws VivException {
    KeyValue<@Nullable Object>[] keyValues =
        getKeyValueObjectsEx(objects, config, Number.class, false);

    @SuppressWarnings({"unchecked", "rawtypes"})
    KeyValue<Integer>[] keyValueIntegers = new KeyValue[keyValues.length];
    for (int i = 0; i < keyValues.length; i++) {
      Object value = keyValues[i].value;
      if (value == null) {
        String errorMessage = reportError("Value of HashMap is null.", false);
        throw new VivException(errorMessage);
      }
      keyValueIntegers[i] = new KeyValue<>(keyValues[i].key,
                                           ((Number) value).intValue());
    }
    return keyValueIntegers;
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Integer. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 10, "b": null}
   *   String code = "{\"a\": 10, \"b\": null}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Integer>[] pairs =
   *           Viv.getKeyValueIntegerOrNulls(code);
   *   for (Viv.KeyValue<@Nullable Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(code);
   *   pairs = Viv.getKeyValueIntegerOrNulls(result);
   *   for (Viv.KeyValue<@Nullable Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   pairs = Viv.getKeyValueIntegerOrNulls("{\"a\": 10, \"b\": true}");
   *   for (Viv.KeyValue<@Nullable Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Integer>} value
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Integer>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, @Nullable Integer>}.
   */
  public static KeyValue<@Nullable Integer> @Nullable []
                    getKeyValueIntegerOrNulls(@Nullable Object... objects) {
    return getKeyValueIntegerOrNulls(objects, null);
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Integer. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 10, "b": null}
   *   String[] codes = {"\"a\": 10", "\"b\": null"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Integer>[] pairs =
   *           Viv.getKeyValueIntegerOrNulls(codes, null);
   *   for (Viv.KeyValue<@Nullable Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(codes, null);
   *   pairs = Viv.getKeyValueIntegerOrNulls(result);
   *   for (Viv.KeyValue<@Nullable Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   codes[1] = "\"b\": true";  // replace
   *   pairs = Viv.getKeyValueIntegerOrNulls(codes, null);
   *   for (Viv.KeyValue<@Nullable Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Integer>} value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Integer>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, @Nullable Integer>}.
   */
  public static KeyValue<@Nullable Integer> @Nullable []
                    getKeyValueIntegerOrNulls(@Nullable Object[] objects,
                                              @Nullable Config config) {
    KeyValue<@Nullable Integer>[] value = null;
    try {
      value = getKeyValueIntegerOrNullsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Integer. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 10, "b": null}
   *   String code = "{\"a\": 10, \"b\": null}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Integer>[] pairs =
   *           Viv.getKeyValueIntegerOrNullsEx(code);
   *   for (Viv.KeyValue<@Nullable Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(code);
   *   pairs = Viv.getKeyValueIntegerOrNullsEx(object);
   *   for (Viv.KeyValue<@Nullable Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   pairs = Viv.getKeyValueIntegerOrNullsEx("{\"a\": 10, \"b\": true}");
   *   for (Viv.KeyValue<@Nullable Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Integer>} value
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Integer>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, @Nullable Integer>}.
   */
  public static KeyValue<@Nullable Integer>[]
                    getKeyValueIntegerOrNullsEx(@Nullable Object... objects)
                                                throws VivException {
    return getKeyValueIntegerOrNullsEx(objects, null);
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Integer. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 10, "b": null}
   *   String[] codes = {"\"a\": 10", "\"b\": null"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Integer>[] pairs =
   *           Viv.getKeyValueIntegerOrNullsEx(codes, null);
   *   for (Viv.KeyValue<@Nullable Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(codes, null);
   *   pairs = Viv.getKeyValueIntegerOrNullsEx(object);
   *   for (Viv.KeyValue<@Nullable Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   codes[1] = "\"b\": true";  // replace
   *   pairs = Viv.getKeyValueIntegerOrNullsEx(codes, null);
   *   for (Viv.KeyValue<@Nullable Integer> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Integer>} value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Integer>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, @Nullable Integer>}.
   */
  public static KeyValue<@Nullable Integer>[]
                    getKeyValueIntegerOrNullsEx(@Nullable Object[] objects,
                                                @Nullable Config config)
                                                throws VivException {
    KeyValue<@Nullable Object>[] keyValues =
        getKeyValueObjectsEx(objects, config, Number.class, true);

    @SuppressWarnings({"unchecked", "rawtypes"})
    KeyValue<@Nullable Integer>[] keyValueIntegers = new KeyValue[keyValues.length];
    for (int i = 0; i < keyValues.length; i++) {
      Object value = keyValues[i].value;
      @Nullable Integer number = (value != null) ? ((Number) value).intValue() : null;
      keyValueIntegers[i] = new KeyValue<>(keyValues[i].key, number);
    }
    return keyValueIntegers;
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Long. When other is given,
   * {@code null} is returned.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 10, "b": 20}
   *   String code = "{\"a\": 10, \"b\": 20}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Long>[] pairs = Viv.getKeyValueLongs(code);
   *   for (Viv.KeyValue<Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: 20
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(code);
   *   pairs = Viv.getKeyValueLongs(result);
   *   for (Viv.KeyValue<Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: 20
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   code = "{\"a\": 10, \"b\": null}";  // replace
   *   pairs = Viv.getKeyValueLongs(code);
   *   System.out.println(pairs == null);  // true
   *
   *   code = "{\"a\": 10, \"b\": true}";  // replace
   *   pairs = Viv.getKeyValueLongs(code);
   *   System.out.println(pairs == null);  // true
   * }</pre>
   * {@link #getKeyValueLongOrNulls(Object...)} is suitable if you
   * want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Long>}
   *                value
   * @return an array of key-value pairs as {@code KeyValue<Long>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, Long>}.
   */
  public static KeyValue<Long> @Nullable [] getKeyValueLongs(
                                                @Nullable Object... objects) {
    return getKeyValueLongs(objects, null);
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Long. When other is given,
   * {@code null} is returned.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 10, "b": 20}
   *   String[] codes = {"\"a\": 10", "\"b\": 20"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Long>[] pairs = Viv.getKeyValueLongs(codes, null);
   *   for (Viv.KeyValue<Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: 20
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(codes, null);
   *   pairs = Viv.getKeyValueLongs(result);
   *   for (Viv.KeyValue<Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: 20
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   codes[1] = "\"b\": null";  // replace
   *   pairs = Viv.getKeyValueLongs(codes, null);
   *   System.out.println(pairs == null);  // true
   *
   *   codes[1] = "\"b\": true";  // replace
   *   pairs = Viv.getKeyValueLongs(codes, null);
   *   System.out.println(pairs == null);  // true
   * }</pre>
   * {@link #getKeyValueLongOrNulls(Object[], Config)} is suitable
   * if you want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Long>}
   *                value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as {@code KeyValue<Long>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, Long>}.
   */
  public static KeyValue<Long> @Nullable [] getKeyValueLongs(
                                                @Nullable Object[] objects,
                                                @Nullable Config config) {
    KeyValue<Long>[] value = null;
    try {
      value = getKeyValueLongsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Long. When other is given,
   * exception is thrown.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 10, "b": 20}
   *   String code = "{\"a\": 10, \"b\": 20}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Long>[] pairs = Viv.getKeyValueLongsEx(code);
   *   for (Viv.KeyValue<Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: 20
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(code);
   *   pairs = Viv.getKeyValueLongsEx(object);
   *   for (Viv.KeyValue<Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: 20
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   code = "{\"a\": 10, \"b\": null}"; // replace
   *   pairs = Viv.getKeyValueLongsEx(code);  // Exception is thrown.
   *
   *   code = "{\"a\": 10, \"b\": true}"; // replace
   *   pairs = Viv.getKeyValueLongsEx(code);  // Exception is thrown.
   * }</pre>
   * {@link #getKeyValueLongOrNullsEx(Object...)}  is suitable if you
   * want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Long>}
   *                value
   * @return an array of key-value pairs as {@code KeyValue<Long>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, Long>}.
   */
  public static KeyValue<Long>[] getKeyValueLongsEx(
                                                @Nullable Object... objects)
                                                throws VivException {
    return getKeyValueLongsEx(objects, null);
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Long. When other is given,
   * exception is thrown.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 10, "b": 20}
   *   String[] codes = {"\"a\": 10", "\"b\": 20"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Long>[] pairs = Viv.getKeyValueLongsEx(codes, null);
   *   for (Viv.KeyValue<Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: 20
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(codes, null);
   *   pairs = Viv.getKeyValueLongsEx(object);
   *   for (Viv.KeyValue<Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: 20
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   codes[1] = "\"b\": null";  // replace
   *   pairs = Viv.getKeyValueLongsEx(codes, null);  // Exception is thrown.
   *
   *   codes[1] = "\"b\": true";  // replace
   *   pairs = Viv.getKeyValueLongsEx(codes, null);  // Exception is thrown.
   * }</pre>
   * {@link #getKeyValueLongOrNullsEx(Object[], Config)} is suitable
   * if you want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Long>}
   *                value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as {@code KeyValue<Long>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, Long>}.
   */
  public static KeyValue<Long>[] getKeyValueLongsEx(
                                                @Nullable Object[] objects,
                                                @Nullable Config config)
                                                throws VivException {
    KeyValue<@Nullable Object>[] keyValues =
        getKeyValueObjectsEx(objects, config, Number.class, false);

    @SuppressWarnings({"unchecked", "rawtypes"})
    KeyValue<Long>[] keyValueLongs = new KeyValue[keyValues.length];
    for (int i = 0; i < keyValues.length; i++) {
      Object value = keyValues[i].value;
      if (value == null) {
        String errorMessage = reportError("Value of HashMap is null.", false);
        throw new VivException(errorMessage);
      }
      keyValueLongs[i] = new KeyValue<>(keyValues[i].key,
                                        ((Number) value).longValue());
    }
    return keyValueLongs;
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Long. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 10, "b": null}
   *   String code = "{\"a\": 10, \"b\": null}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Long>[] pairs =
   *           Viv.getKeyValueLongOrNulls(code);
   *   for (Viv.KeyValue<@Nullable Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(code);
   *   pairs = Viv.getKeyValueLongOrNulls(result);
   *   for (Viv.KeyValue<@Nullable Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   pairs = Viv.getKeyValueLongOrNulls("{\"a\": 10, \"b\": true}");
   *   for (Viv.KeyValue<@Nullable Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Long>} value
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Long>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, @Nullable Long>}.
   */
  public static KeyValue<@Nullable Long> @Nullable []
                         getKeyValueLongOrNulls(@Nullable Object... objects) {
    return getKeyValueLongOrNulls(objects, null);
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Long. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 10, "b": null}
   *   String[] codes = {"\"a\": 10", "\"b\": null"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Long>[] pairs =
   *           Viv.getKeyValueLongOrNulls(codes, null);
   *   for (Viv.KeyValue<@Nullable Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(codes, null);
   *   pairs = Viv.getKeyValueLongOrNulls(result);
   *   for (Viv.KeyValue<@Nullable Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   codes[1] = "\"b\": true";  // replace
   *   pairs = Viv.getKeyValueLongOrNulls(codes, null);
   *   for (Viv.KeyValue<@Nullable Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Long>} value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Long>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, @Nullable Long>}.
   */
  public static KeyValue<@Nullable Long> @Nullable []
                         getKeyValueLongOrNulls(@Nullable Object[] objects,
                                                @Nullable Config config) {
    KeyValue<@Nullable Long>[] value = null;
    try {
      value = getKeyValueLongOrNullsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Long. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 10, "b": null}
   *   String code = "{\"a\": 10, \"b\": null}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Long>[] pairs =
   *           Viv.getKeyValueLongOrNullsEx(code);
   *   for (Viv.KeyValue<@Nullable Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(code);
   *   pairs = Viv.getKeyValueLongOrNullsEx(object);
   *   for (Viv.KeyValue<@Nullable Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   pairs = Viv.getKeyValueLongOrNullsEx("{\"a\": 10, \"b\": true}");
   *   for (Viv.KeyValue<@Nullable Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Long>} value
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Long>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, @Nullable Long>}.
   */
  public static KeyValue<@Nullable Long>[]
                       getKeyValueLongOrNullsEx(@Nullable Object... objects)
                                                throws VivException {
    return getKeyValueLongOrNullsEx(objects, null);
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Long. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 10, "b": null}
   *   String[] codes = {"\"a\": 10", "\"b\": null"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Long>[] pairs =
   *           Viv.getKeyValueLongOrNullsEx(codes, null);
   *   for (Viv.KeyValue<@Nullable Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(codes, null);
   *   pairs = Viv.getKeyValueLongOrNullsEx(object);
   *   for (Viv.KeyValue<@Nullable Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   codes[1] = "\"b\": true";  // replace
   *   pairs = Viv.getKeyValueLongOrNullsEx(codes, null);
   *   for (Viv.KeyValue<@Nullable Long> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 10, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Long>} value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Long>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, @Nullable Long>}.
   */
  public static KeyValue<@Nullable Long>[]
                       getKeyValueLongOrNullsEx(@Nullable Object[] objects,
                                                @Nullable Config config)
                                                throws VivException {
    KeyValue<@Nullable Object>[] keyValues =
        getKeyValueObjectsEx(objects, config, Number.class, true);

    @SuppressWarnings({"unchecked", "rawtypes"})
    KeyValue<@Nullable Long>[] keyValueLongs = new KeyValue[keyValues.length];
    for (int i = 0; i < keyValues.length; i++) {
      Object value = keyValues[i].value;
      @Nullable Long number = (value != null) ? ((Number) value).longValue() : null;
      keyValueLongs[i] = new KeyValue<>(keyValues[i].key, number);
    }
    return keyValueLongs;
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Float. When other is given,
   * {@code null} is returned.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 1.0, "b": 2.5}
   *   String code = "{\"a\": 1.0, \"b\": 2.5}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Float>[] pairs = Viv.getKeyValueFloats(code);
   *   for (Viv.KeyValue<Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: 2.5
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(code);
   *   pairs = Viv.getKeyValueFloats(result);
   *   for (Viv.KeyValue<Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: 2.5
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   code = "{\"a\": 1.0, \"b\": null}";  // replace
   *   pairs = Viv.getKeyValueFloats(code);
   *   System.out.println(pairs == null);  // true
   *
   *   code = "{\"a\": 1.0, \"b\": true}";  // replace
   *   pairs = Viv.getKeyValueFloats(code);
   *   System.out.println(pairs == null);  // true
   * }</pre>
   * {@link #getKeyValueFloatOrNulls(Object...)} is suitable if you
   * want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Float>}
   *                value
   * @return an array of key-value pairs as {@code KeyValue<Float>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, Float>}.
   */
  public static KeyValue<Float> @Nullable [] getKeyValueFloats(
                                                @Nullable Object... objects) {
    return getKeyValueFloats(objects, null);
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Float. When other is given,
   * {@code null} is returned.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 1.0, "b": 2.5}
   *   String[] codes = {"\"a\": 1.0", "\"b\": 2.5"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Float>[] pairs = Viv.getKeyValueFloats(codes, null);
   *   for (Viv.KeyValue<Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: 2.5
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(codes, null);
   *   pairs = Viv.getKeyValueFloats(result);
   *   for (Viv.KeyValue<Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: 2.5
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   codes[1] = "\"b\": null";  // replace
   *   pairs = Viv.getKeyValueFloats(codes, null);
   *   System.out.println(pairs == null);  // true
   *
   *   codes[1] = "\"b\": true";  // replace
   *   pairs = Viv.getKeyValueFloats(codes, null);
   *   System.out.println(pairs == null);  // true
   * }</pre>
   * {@link #getKeyValueFloatOrNulls(Object[], Config)} is suitable
   * if you want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Float>}
   *                value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as {@code KeyValue<Float>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, Float>}.
   */
  public static KeyValue<Float> @Nullable [] getKeyValueFloats(
                                                @Nullable Object[] objects,
                                                @Nullable Config config) {
    KeyValue<Float>[] value = null;
    try {
      value = getKeyValueFloatsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Float. When other is given,
   * exception is thrown.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 1.0, "b": 2.5}
   *   String code = "{\"a\": 1.0, \"b\": 2.5}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Float>[] pairs = Viv.getKeyValueFloatsEx(code);
   *   for (Viv.KeyValue<Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: 2.5
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(code);
   *   pairs = Viv.getKeyValueFloatsEx(object);
   *   for (Viv.KeyValue<Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: 2.5
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   code = "{\"a\": 1.0, \"b\": null}"; // replace
   *   pairs = Viv.getKeyValueFloatsEx(code);  // Exception is thrown.
   *
   *   code = "{\"a\": 1.0, \"b\": true}"; // replace
   *   pairs = Viv.getKeyValueFloatsEx(code);  // Exception is thrown.
   * }</pre>
   * {@link #getKeyValueFloatOrNullsEx(Object...)}  is suitable if you
   * want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Float>}
   *                value
   * @return an array of key-value pairs as {@code KeyValue<Float>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, Float>}.
   */
  public static KeyValue<Float>[] getKeyValueFloatsEx(
                                                @Nullable Object... objects)
                                                throws VivException {
    return getKeyValueFloatsEx(objects, null);
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Float. When other is given,
   * exception is thrown.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 1.0, "b": 2.5}
   *   String[] codes = {"\"a\": 1.0", "\"b\": 2.5"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Float>[] pairs = Viv.getKeyValueFloatsEx(codes, null);
   *   for (Viv.KeyValue<Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: 2.5
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(codes, null);
   *   pairs = Viv.getKeyValueFloatsEx(object);
   *   for (Viv.KeyValue<Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: 2.5
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   codes[1] = "\"b\": null";  // replace
   *   pairs = Viv.getKeyValueFloatsEx(codes, null);  // Exception is thrown.
   *
   *   codes[1] = "\"b\": true";  // replace
   *   pairs = Viv.getKeyValueFloatsEx(codes, null);  // Exception is thrown.
   * }</pre>
   * {@link #getKeyValueFloatOrNullsEx(Object[], Config)} is suitable
   * if you want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Float>}
   *                value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as {@code KeyValue<Float>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, Float>}.
   */
  public static KeyValue<Float>[] getKeyValueFloatsEx(
                                                @Nullable Object[] objects,
                                                @Nullable Config config)
                                                throws VivException {
    KeyValue<@Nullable Object>[] keyValues =
        getKeyValueObjectsEx(objects, config, Number.class, false);

    @SuppressWarnings({"unchecked", "rawtypes"})
    KeyValue<Float>[] keyValueFloats = new KeyValue[keyValues.length];
    for (int i = 0; i < keyValues.length; i++) {
      Object value = keyValues[i].value;
      if (value == null) {
        String errorMessage = reportError("Value of HashMap is null.", false);
        throw new VivException(errorMessage);
      }
      keyValueFloats[i] = new KeyValue<>(keyValues[i].key,
                                           ((Number) value).floatValue());
    }
    return keyValueFloats;
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Float. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 1.0, "b": null}
   *   String code = "{\"a\": 1.0, \"b\": null}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Float>[] pairs =
   *           Viv.getKeyValueFloatOrNulls(code);
   *   for (Viv.KeyValue<@Nullable Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(code);
   *   pairs = Viv.getKeyValueFloatOrNulls(result);
   *   for (Viv.KeyValue<@Nullable Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   pairs = Viv.getKeyValueFloatOrNulls("{\"a\": 1.0, \"b\": true}");
   *   for (Viv.KeyValue<@Nullable Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Float>} value
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Float>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, @Nullable Float>}.
   */
  public static KeyValue<@Nullable Float> @Nullable []
                        getKeyValueFloatOrNulls(@Nullable Object... objects) {
    return getKeyValueFloatOrNulls(objects, null);
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Float. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 1.0, "b": null}
   *   String[] codes = {"\"a\": 1.0", "\"b\": null"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Float>[] pairs =
   *           Viv.getKeyValueFloatOrNulls(codes, null);
   *   for (Viv.KeyValue<@Nullable Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(codes, null);
   *   pairs = Viv.getKeyValueFloatOrNulls(result);
   *   for (Viv.KeyValue<@Nullable Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   codes[1] = "\"b\": true";  // replace
   *   pairs = Viv.getKeyValueFloatOrNulls(codes, null);
   *   for (Viv.KeyValue<@Nullable Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Float>} value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Float>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, @Nullable Float>}.
   */
  public static KeyValue<@Nullable Float> @Nullable []
                        getKeyValueFloatOrNulls(@Nullable Object[] objects,
                                                @Nullable Config config) {
    KeyValue<@Nullable Float>[] value = null;
    try {
      value = getKeyValueFloatOrNullsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Float. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 1.0, "b": null}
   *   String code = "{\"a\": 1.0, \"b\": null}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Float>[] pairs =
   *           Viv.getKeyValueFloatOrNullsEx(code);
   *   for (Viv.KeyValue<@Nullable Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(code);
   *   pairs = Viv.getKeyValueFloatOrNullsEx(object);
   *   for (Viv.KeyValue<@Nullable Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   pairs = Viv.getKeyValueFloatOrNullsEx("{\"a\": 1.0, \"b\": true}");
   *   for (Viv.KeyValue<@Nullable Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Float>} value
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Float>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, @Nullable Float>}.
   */
  public static KeyValue<@Nullable Float>[]
                      getKeyValueFloatOrNullsEx(@Nullable Object... objects)
                                                throws VivException {
    return getKeyValueFloatOrNullsEx(objects, null);
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Float. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 1.0, "b": null}
   *   String[] codes = {"\"a\": 1.0", "\"b\": null"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Float>[] pairs =
   *           Viv.getKeyValueFloatOrNullsEx(codes, null);
   *   for (Viv.KeyValue<@Nullable Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(codes, null);
   *   pairs = Viv.getKeyValueFloatOrNullsEx(object);
   *   for (Viv.KeyValue<@Nullable Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   codes[1] = "\"b\": true";  // replace
   *   pairs = Viv.getKeyValueFloatOrNullsEx(codes, null);
   *   for (Viv.KeyValue<@Nullable Float> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Float>} value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Float>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, @Nullable Float>}.
   */
  public static KeyValue<@Nullable Float>[]
                      getKeyValueFloatOrNullsEx(@Nullable Object[] objects,
                                                @Nullable Config config)
                                                throws VivException {
    KeyValue<@Nullable Object>[] keyValues =
        getKeyValueObjectsEx(objects, config, Number.class, true);

    @SuppressWarnings({"unchecked", "rawtypes"})
    KeyValue<@Nullable Float>[] keyValueFloats = new KeyValue[keyValues.length];
    for (int i = 0; i < keyValues.length; i++) {
      Object value = keyValues[i].value;
      @Nullable Float number = (value != null) ? ((Number) value).floatValue() : null;
      keyValueFloats[i] = new KeyValue<>(keyValues[i].key, number);
    }
    return keyValueFloats;
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Double. When other is given,
   * {@code null} is returned.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 1.0, "b": 2.5}
   *   String code = "{\"a\": 1.0, \"b\": 2.5}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Double>[] pairs = Viv.getKeyValueDoubles(code);
   *   for (Viv.KeyValue<Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: 2.5
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(code);
   *   pairs = Viv.getKeyValueDoubles(result);
   *   for (Viv.KeyValue<Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: 2.5
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   code = "{\"a\": 1.0, \"b\": null}";  // replace
   *   pairs = Viv.getKeyValueDoubles(code);
   *   System.out.println(pairs == null);  // true
   *
   *   code = "{\"a\": 1.0, \"b\": true}";  // replace
   *   pairs = Viv.getKeyValueDoubles(code);
   *   System.out.println(pairs == null);  // true
   * }</pre>
   * {@link #getKeyValueDoubleOrNulls(Object...)} is suitable if you
   * want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Double>}
   *                value
   * @return an array of key-value pairs as {@code KeyValue<Double>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, Double>}.
   */
  public static KeyValue<Double> @Nullable [] getKeyValueDoubles(
                                                @Nullable Object... objects) {
    return getKeyValueDoubles(objects, null);
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Double. When other is given,
   * {@code null} is returned.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 1.0, "b": 2.5}
   *   String[] codes = {"\"a\": 1.0", "\"b\": 2.5"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Double>[] pairs = Viv.getKeyValueDoubles(codes, null);
   *   for (Viv.KeyValue<Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: 2.5
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(codes, null);
   *   pairs = Viv.getKeyValueDoubles(result);
   *   for (Viv.KeyValue<Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: 2.5
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   codes[1] = "\"b\": null";  // replace
   *   pairs = Viv.getKeyValueDoubles(codes, null);
   *   System.out.println(pairs == null);  // true
   *
   *   codes[1] = "\"b\": true";  // replace
   *   pairs = Viv.getKeyValueDoubles(codes, null);
   *   System.out.println(pairs == null);  // true
   * }</pre>
   * {@link #getKeyValueDoubleOrNulls(Object[], Config)} is suitable
   * if you want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Double>}
   *                value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as {@code KeyValue<Double>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, Double>}.
   */
  public static KeyValue<Double> @Nullable [] getKeyValueDoubles(
                                                @Nullable Object[] objects,
                                                @Nullable Config config) {
    KeyValue<Double>[] value = null;
    try {
      value = getKeyValueDoublesEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Double. When other is given,
   * exception is thrown.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 1.0, "b": 2.5}
   *   String code = "{\"a\": 1.0, \"b\": 2.5}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Double>[] pairs = Viv.getKeyValueDoublesEx(code);
   *   for (Viv.KeyValue<Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: 2.5
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(code);
   *   pairs = Viv.getKeyValueDoublesEx(object);
   *   for (Viv.KeyValue<Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: 2.5
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   code = "{\"a\": 1.0, \"b\": null}"; // replace
   *   pairs = Viv.getKeyValueDoublesEx(code);  // Exception is thrown.
   *
   *   code = "{\"a\": 1.0, \"b\": true}"; // replace
   *   pairs = Viv.getKeyValueDoublesEx(code);  // Exception is thrown.
   * }</pre>
   * {@link #getKeyValueDoubleOrNullsEx(Object...)}  is suitable if you
   * want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Double>}
   *                value
   * @return an array of key-value pairs as {@code KeyValue<Double>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, Double>}.
   */
  public static KeyValue<Double>[] getKeyValueDoublesEx(
                                                @Nullable Object... objects)
                                                throws VivException {
    return getKeyValueDoublesEx(objects, null);
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Double. When other is given,
   * exception is thrown.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 1.0, "b": 2.5}
   *   String[] codes = {"\"a\": 1.0", "\"b\": 2.5"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Double>[] pairs = Viv.getKeyValueDoublesEx(codes, null);
   *   for (Viv.KeyValue<Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: 2.5
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(codes, null);
   *   pairs = Viv.getKeyValueDoublesEx(object);
   *   for (Viv.KeyValue<Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: 2.5
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   codes[1] = "\"b\": null";  // replace
   *   pairs = Viv.getKeyValueDoublesEx(codes, null);  // Exception is thrown.
   *
   *   codes[1] = "\"b\": true";  // replace
   *   pairs = Viv.getKeyValueDoublesEx(codes, null);  // Exception is thrown.
   * }</pre>
   * {@link #getKeyValueDoubleOrNullsEx(Object[], Config)} is suitable
   * if you want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Double>}
   *                value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as {@code KeyValue<Double>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, Double>}.
   */
  public static KeyValue<Double>[] getKeyValueDoublesEx(
                                                @Nullable Object[] objects,
                                                @Nullable Config config)
                                                throws VivException {
    KeyValue<@Nullable Object>[] keyValues =
        getKeyValueObjectsEx(objects, config, Number.class, false);

    @SuppressWarnings({"unchecked", "rawtypes"})
    KeyValue<Double>[] keyValueDoubles = new KeyValue[keyValues.length];
    for (int i = 0; i < keyValues.length; i++) {
      Object value = keyValues[i].value;
      if (value == null) {
        String errorMessage = reportError("Value of HashMap is null.", false);
        throw new VivException(errorMessage);
      }
      keyValueDoubles[i] = new KeyValue<>(keyValues[i].key,
                                          ((Number) value).doubleValue());
    }
    return keyValueDoubles;
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Double. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 1.0, "b": null}
   *   String code = "{\"a\": 1.0, \"b\": null}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Double>[] pairs =
   *           Viv.getKeyValueDoubleOrNulls(code);
   *   for (Viv.KeyValue<@Nullable Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(code);
   *   pairs = Viv.getKeyValueDoubleOrNulls(result);
   *   for (Viv.KeyValue<@Nullable Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   pairs = Viv.getKeyValueDoubleOrNulls("{\"a\": 1.0, \"b\": true}");
   *   for (Viv.KeyValue<@Nullable Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Double>} value
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Double>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, @Nullable Double>}.
   */
  public static KeyValue<@Nullable Double> @Nullable []
                       getKeyValueDoubleOrNulls(@Nullable Object... objects) {
    return getKeyValueDoubleOrNulls(objects, null);
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Double. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 1.0, "b": null}
   *   String[] codes = {"\"a\": 1.0", "\"b\": null"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Double>[] pairs =
   *           Viv.getKeyValueDoubleOrNulls(codes, null);
   *   for (Viv.KeyValue<@Nullable Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(codes, null);
   *   pairs = Viv.getKeyValueDoubleOrNulls(result);
   *   for (Viv.KeyValue<@Nullable Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   codes[1] = "\"b\": true";  // replace
   *   pairs = Viv.getKeyValueDoubleOrNulls(codes, null);
   *   for (Viv.KeyValue<@Nullable Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Double>} value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Double>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, @Nullable Double>}.
   */
  public static KeyValue<@Nullable Double> @Nullable []
                       getKeyValueDoubleOrNulls(@Nullable Object[] objects,
                                                @Nullable Config config) {
    KeyValue<@Nullable Double>[] value = null;
    try {
      value = getKeyValueDoubleOrNullsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Double. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 1.0, "b": null}
   *   String code = "{\"a\": 1.0, \"b\": null}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Double>[] pairs =
   *           Viv.getKeyValueDoubleOrNullsEx(code);
   *   for (Viv.KeyValue<@Nullable Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(code);
   *   pairs = Viv.getKeyValueDoubleOrNullsEx(object);
   *   for (Viv.KeyValue<@Nullable Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   pairs = Viv.getKeyValueDoubleOrNullsEx("{\"a\": 1.0, \"b\": true}");
   *   for (Viv.KeyValue<@Nullable Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Double>} value
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Double>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, @Nullable Double>}.
   */
  public static KeyValue<@Nullable Double>[]
                     getKeyValueDoubleOrNullsEx(@Nullable Object... objects)
                                                throws VivException {
    return getKeyValueDoubleOrNullsEx(objects, null);
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Double. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": 1.0, "b": null}
   *   String[] codes = {"\"a\": 1.0", "\"b\": null"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Double>[] pairs =
   *           Viv.getKeyValueDoubleOrNullsEx(codes, null);
   *   for (Viv.KeyValue<@Nullable Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(codes, null);
   *   pairs = Viv.getKeyValueDoubleOrNullsEx(object);
   *   for (Viv.KeyValue<@Nullable Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   codes[1] = "\"b\": true";  // replace
   *   pairs = Viv.getKeyValueDoubleOrNullsEx(codes, null);
   *   for (Viv.KeyValue<@Nullable Double> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: 1.0, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Double>} value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Double>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, @Nullable Double>}.
   */
  public static KeyValue<@Nullable Double>[]
                     getKeyValueDoubleOrNullsEx(@Nullable Object[] objects,
                                                @Nullable Config config)
                                                throws VivException {
    KeyValue<@Nullable Object>[] keyValues =
        getKeyValueObjectsEx(objects, config, Number.class, true);

    @SuppressWarnings({"unchecked", "rawtypes"})
    KeyValue<@Nullable Double>[] keyValueDoubles = new KeyValue[keyValues.length];
    for (int i = 0; i < keyValues.length; i++) {
      Object value = keyValues[i].value;
      @Nullable Double number = (value != null) ? ((Number) value).doubleValue() : null;
      keyValueDoubles[i] = new KeyValue<>(keyValues[i].key, number);
    }
    return keyValueDoubles;
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as String. When other is given,
   * {@code null} is returned.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": "alpha", "b": "beta"}
   *   String code = "{\"a\": \"alpha\", \"b\": \"beta\"}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<String>[] pairs = Viv.getKeyValueStrings(code);
   *   for (Viv.KeyValue<String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: beta
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(code);
   *   pairs = Viv.getKeyValueStrings(result);
   *   for (Viv.KeyValue<String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: beta
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   code = "{\"a\": \"alpha\", \"b\": null}";  // replace
   *   pairs = Viv.getKeyValueStrings(code);
   *   System.out.println(pairs == null);  // true
   *
   *   code = "{\"a\": \"alpha\", \"b\": 10}";  // replace
   *   pairs = Viv.getKeyValueStrings(code);
   *   System.out.println(pairs == null);  // true
   * }</pre>
   * {@link #getKeyValueStringOrNulls(Object...)} is suitable if you
   * want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, String>}
   *                value
   * @return an array of key-value pairs as {@code KeyValue<String>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, String>}.
   */
  public static KeyValue<String> @Nullable [] getKeyValueStrings(
                                                @Nullable Object... objects) {
    return getKeyValueStrings(objects, null);
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as String. When other is given,
   * {@code null} is returned.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": "alpha", "b": "beta"}
   *   String[] codes = {"\"a\": \"alpha\"", "\"b\": \"beta\""};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<String>[] pairs = Viv.getKeyValueStrings(codes, null);
   *   for (Viv.KeyValue<String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: beta
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(codes, null);
   *   pairs = Viv.getKeyValueStrings(result);
   *   for (Viv.KeyValue<String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: beta
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   codes[1] = "\"b\": null";  // replace
   *   pairs = Viv.getKeyValueStrings(codes, null);
   *   System.out.println(pairs == null);  // true
   *
   *   codes[1] = "\"b\": 10";  // replace
   *   pairs = Viv.getKeyValueStrings(codes, null);
   *   System.out.println(pairs == null);  // true
   * }</pre>
   * {@link #getKeyValueStringOrNulls(Object[], Config)} is suitable
   * if you want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, String>}
   *                value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as {@code KeyValue<String>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, String>}.
   */
  public static KeyValue<String> @Nullable [] getKeyValueStrings(
                                                @Nullable Object[] objects,
                                                @Nullable Config config) {
    KeyValue<String>[] value = null;
    try {
      value = getKeyValueStringsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as String. When other is given,
   * exception is thrown.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": "alpha", "b": "beta"}
   *   String code = "{\"a\": \"alpha\", \"b\": \"beta\"}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<String>[] pairs = Viv.getKeyValueStringsEx(code);
   *   for (Viv.KeyValue<String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: beta
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(code);
   *   pairs = Viv.getKeyValueStringsEx(object);
   *   for (Viv.KeyValue<String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: beta
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   code = "{\"a\": \"alpha\", \"b\": null}"; // replace
   *   pairs = Viv.getKeyValueStringsEx(code);  // Exception is thrown.
   *
   *   code = "{\"a\": \"alpha\", \"b\": 10}"; // replace
   *   pairs = Viv.getKeyValueStringsEx(code);  // Exception is thrown.
   * }</pre>
   * {@link #getKeyValueStringOrNullsEx(Object...)}  is suitable if you
   * want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, String>}
   *                value
   * @return an array of key-value pairs as {@code KeyValue<String>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, String>}.
   */
  public static KeyValue<String>[] getKeyValueStringsEx(
                                                @Nullable Object... objects)
                                                throws VivException {
    return getKeyValueStringsEx(objects, null);
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as String. When other is given,
   * exception is thrown.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": "alpha", "b": "beta"}
   *   String[] codes = {"\"a\": \"alpha\"", "\"b\": \"beta\""};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<String>[] pairs = Viv.getKeyValueStringsEx(codes, null);
   *   for (Viv.KeyValue<String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: beta
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(codes, null);
   *   pairs = Viv.getKeyValueStringsEx(object);
   *   for (Viv.KeyValue<String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: beta
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   codes[1] = "\"b\": null";  // replace
   *   pairs = Viv.getKeyValueStringsEx(codes, null);  // Exception is thrown.
   *
   *   codes[1] = "\"b\": 10";  // replace
   *   pairs = Viv.getKeyValueStringsEx(codes, null);  // Exception is thrown.
   * }</pre>
   * {@link #getKeyValueStringOrNullsEx(Object[], Config)} is suitable
   * if you want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, String>}
   *                value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as {@code KeyValue<String>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, String>}.
   */
  public static KeyValue<String>[] getKeyValueStringsEx(
                                                @Nullable Object[] objects,
                                                @Nullable Config config)
                                                throws VivException {
    KeyValue<@Nullable Object>[] keyValues =
        getKeyValueObjectsEx(objects, config, String.class, false);

    @SuppressWarnings({"unchecked", "rawtypes"})
    KeyValue<String>[] keyValueStrings = new KeyValue[keyValues.length];
    for (int i = 0; i < keyValues.length; i++) {
      Object value = keyValues[i].value;
      if (value == null) {
        String errorMessage = reportError("Value of HashMap is null.", false);
        throw new VivException(errorMessage);
      }
      keyValueStrings[i] = new KeyValue<>(keyValues[i].key, (String) value);
    }
    return keyValueStrings;
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as String. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": "alpha", "b": null}
   *   String code = "{\"a\": \"alpha\", \"b\": null}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable String>[] pairs =
   *           Viv.getKeyValueStringOrNulls(code);
   *   for (Viv.KeyValue<@Nullable String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(code);
   *   pairs = Viv.getKeyValueStringOrNulls(result);
   *   for (Viv.KeyValue<@Nullable String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   pairs = Viv.getKeyValueStringOrNulls("{\"a\": \"alpha\", \"b\": 10}");
   *   for (Viv.KeyValue<@Nullable String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable String>} value
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable String>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, @Nullable String>}.
   */
  public static KeyValue<@Nullable String> @Nullable []
                       getKeyValueStringOrNulls(@Nullable Object... objects) {
    return getKeyValueStringOrNulls(objects, null);
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as String. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": "alpha", "b": null}
   *   String[] codes = {"\"a\": \"alpha\"", "\"b\": null"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable String>[] pairs =
   *           Viv.getKeyValueStringOrNulls(codes, null);
   *   for (Viv.KeyValue<@Nullable String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(codes, null);
   *   pairs = Viv.getKeyValueStringOrNulls(result);
   *   for (Viv.KeyValue<@Nullable String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   codes[1] = "\"b\": 10";  // replace
   *   pairs = Viv.getKeyValueStringOrNulls(codes, null);
   *   for (Viv.KeyValue<@Nullable String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable String>} value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable String>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, @Nullable String>}.
   */
  public static KeyValue<@Nullable String> @Nullable []
                       getKeyValueStringOrNulls(@Nullable Object[] objects,
                                                @Nullable Config config) {
    KeyValue<@Nullable String>[] value = null;
    try {
      value = getKeyValueStringOrNullsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as String. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": "alpha", "b": null}
   *   String code = "{\"a\": \"alpha\", \"b\": null}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable String>[] pairs =
   *           Viv.getKeyValueStringOrNullsEx(code);
   *   for (Viv.KeyValue<@Nullable String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(code);
   *   pairs = Viv.getKeyValueStringOrNullsEx(object);
   *   for (Viv.KeyValue<@Nullable String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   pairs = Viv.getKeyValueStringOrNullsEx("{\"a\": \"alpha\", \"b\": 10}");
   *   for (Viv.KeyValue<@Nullable String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable String>} value
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable String>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, @Nullable String>}.
   */
  public static KeyValue<@Nullable String>[]
                     getKeyValueStringOrNullsEx(@Nullable Object... objects)
                                                throws VivException {
    return getKeyValueStringOrNullsEx(objects, null);
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as String. When other is given,
   * it is replaced with {@code null}.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": "alpha", "b": null}
   *   String[] codes = {"\"a\": \"alpha\"", "\"b\": null"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable String>[] pairs =
   *           Viv.getKeyValueStringOrNullsEx(codes, null);
   *   for (Viv.KeyValue<@Nullable String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   *
   *   // Evaluate result.
   *   Object object = Viv.runEx(codes, null);
   *   pairs = Viv.getKeyValueStringOrNullsEx(object);
   *   for (Viv.KeyValue<@Nullable String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   *
   *   // Evaluate another value directly.
   *   codes[1] = "\"b\": 10";  // replace
   *   pairs = Viv.getKeyValueStringOrNullsEx(codes, null);
   *   for (Viv.KeyValue<@Nullable String> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable String>} value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable String>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, @Nullable String>}.
   */
  public static KeyValue<@Nullable String>[]
                     getKeyValueStringOrNullsEx(@Nullable Object[] objects,
                                                @Nullable Config config)
                                                throws VivException {
    KeyValue<@Nullable Object>[] keyValues =
        getKeyValueObjectsEx(objects, config, String.class, true);

    @SuppressWarnings({"unchecked", "rawtypes"})
    KeyValue<@Nullable String>[] keyValueStrings = new KeyValue[keyValues.length];
    for (int i = 0; i < keyValues.length; i++) {
      Object value = keyValues[i].value;
      @Nullable String string = (value != null) ? (String) value : null;
      keyValueStrings[i] = new KeyValue<>(keyValues[i].key, string);
    }
    return keyValueStrings;
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Object. When there is {@code null},
   * {@code null} is returned as the whole returned value.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": "alpha", "b": 100}
   *   String code = "{\"a\": \"alpha\", \"b\": 100}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Object>[] pairs = Viv.getKeyValueObjects(code);
   *   for (Viv.KeyValue<Object> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: 100
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(code);
   *   pairs = Viv.getKeyValueObjects(result);
   *   for (Viv.KeyValue<Object> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: 100
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   code = "{\"a\": \"alpha\", \"b\": null}";  // replace
   *   pairs = Viv.getKeyValueObjects(code);
   *   System.out.println(pairs == null);  // true
   * }</pre>
   * {@link #getKeyValueObjectOrNulls(Object...)} is suitable
   * if you want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Object>}
   *                value
   * @return an array of key-value pairs as {@code KeyValue<Object>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, Object>}.
   */
  public static KeyValue<Object> @Nullable [] getKeyValueObjects(
                                                @Nullable Object... objects) {
    return getKeyValueObjects(objects, null);
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Object. When there is {@code null},
   * {@code null} is returned as the whole returned value.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": "alpha", "b": 100}
   *   String[] codes = {"\"a\": \"alpha\"", "\"b\": 100"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Object>[] pairs = Viv.getKeyValueObjects(codes, null);
   *   for (Viv.KeyValue<Object> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: 100
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(codes, null);
   *   pairs = Viv.getKeyValueObjects(result);
   *   for (Viv.KeyValue<Object> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: 100
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   codes[1] = "\"b\": null";  // replace
   *   pairs = Viv.getKeyValueObjects(codes, null);
   *   System.out.println(pairs == null);  // true
   * }</pre>
   * {@link #getKeyValueObjectOrNulls(Object[], Config)} is suitable
   * if you want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Object>}
   *                value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as {@code KeyValue<Object>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, Object>}.
   */
  public static KeyValue<Object> @Nullable [] getKeyValueObjects(
                                                @Nullable Object[] objects,
                                                @Nullable Config config) {
    KeyValue<Object>[] value = null;
    try {
      value = getKeyValueObjectsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Object. When there is {@code null},
   * exception is thrown.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": "alpha", "b": 100}
   *   String code = "{\"a\": \"alpha\", \"b\": 100}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Object>[] pairs = Viv.getKeyValueObjectsEx(code);
   *   for (Viv.KeyValue<Object> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: 100
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(code);
   *   pairs = Viv.getKeyValueObjectsEx(result);
   *   for (Viv.KeyValue<Object> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: 100
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   code = "{\"a\": \"alpha\", \"b\": null}";  // replace
   *   pairs = Viv.getKeyValueObjectsEx(code);  // Exception is thrown.
   * }</pre>
   * {@link #getKeyValueObjectOrNullsEx(Object...)} is suitable
   * if you want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Object>}
   *                value
   * @return an array of key-value pairs as {@code KeyValue<Object>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, Object>}.
   */
  public static KeyValue<Object>[] getKeyValueObjectsEx(
                                                @Nullable Object... objects)
                                                throws VivException {
    return getKeyValueObjectsEx(objects, null);
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Object. When there is {@code null},
   * exception is thrown.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": "alpha", "b": 100}
   *   String[] codes = {"\"a\": \"alpha\"", "\"b\": 100"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<Object>[] pairs = Viv.getKeyValueObjectsEx(codes, null);
   *   for (Viv.KeyValue<Object> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: 100
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(codes, null);
   *   pairs = Viv.getKeyValueObjectsEx(result);
   *   for (Viv.KeyValue<Object> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: 100
   *   }
   *
   *   // Whole values are lost even if there is only one foreign object.
   *   codes[1] = "\"b\": null";  // replace
   *   pairs = Viv.getKeyValueObjectsEx(codes, null);  // Exception is thrown.
   * }</pre>
   * {@link #getKeyValueObjectOrNullsEx(Object[], Config)} is suitable
   * if you want not to lost whole values.
   *
   * @param objects object that may be a {@code HashMap<String, Object>}
   *                value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as {@code KeyValue<Object>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, Object>}.
   */
  public static KeyValue<Object>[] getKeyValueObjectsEx(
                                                @Nullable Object[] objects,
                                                @Nullable Config config)
                                                throws VivException {
    return getKeyValueObjectsEx(objects, config, Object.class, false);
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   *
   * @param objects a value that may be HashMap.
   * @param config configuration if it is needed, {@code null} otherwise
   * @param expectedClass the expected class of value.
   *                      Note that key is always expected as String.
   * @param unexpectedIsNull the behavior for unexpected class of value.<br>
   *        When the unexpected class of value is found, its value will be
   *        null or the exception is thrown.
   *        <pre>
   *        | unexpectedIsNull | The element's returned value | Exception |
   *        |------------------|------------------------------|-----------|
   *        | false            | -----                        | throw     |
   *        | true             | null                         | not throw |
   *        </pre>
   * @return an array of key-value pairs
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not an array of
   *                          the expected key-value class.
   */
  private static KeyValue<@Nullable Object>[] getKeyValueObjectsEx(
                                                @Nullable Object[] objects,
                                                @Nullable Config config,
                                                Class<?> expectedClass,
                                                boolean unexpectedIsNull)
                                                throws VivException {
    HashMap<String, @Nullable Object> map = getHashMapEx(objects, config);

    @SuppressWarnings({"unchecked", "rawtypes"})
    KeyValue<@Nullable Object>[] keyValues = new KeyValue[map.size()];
    Iterator<Map.Entry<String, @Nullable Object>> iterator =
                                                map.entrySet().iterator();
    int index = 0;
    while (iterator.hasNext()) {
      @SuppressWarnings("null")
      Map.Entry<String, @Nullable Object> entry = iterator.next();
      @SuppressWarnings("null")
      String key = entry.getKey();
      @Nullable Object value = entry.getValue();

      Result result = getExpectedValue(value, expectedClass, unexpectedIsNull);
      if (!result.errorMessage.isEmpty()) {
        throw new VivException(result.errorMessage);
      }
      keyValues[index] = new KeyValue<>(key, result.value);
      index++;
    }
    return keyValues;
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Object. Even if there is {@code null},
   * it is permitted.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": "alpha", "b": null}
   *   String code = "{\"a\": \"alpha\", \"b\": null}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Object>[] pairs =
   *           Viv.getKeyValueObjectOrNulls(code);
   *   for (Viv.KeyValue<@Nullable Object> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(code);
   *   pairs = Viv.getKeyValueObjectOrNulls(result);
   *   for (Viv.KeyValue<@Nullable Object> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Object>} value
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Object>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, @Nullable Object>}.
   */
  public static KeyValue<@Nullable Object> @Nullable [] getKeyValueObjectOrNulls(
                                                @Nullable Object... objects) {
    return getKeyValueObjectOrNulls(objects, null);
  }

  /**
   * Gets key-value pairs as an array.
   * All values are expected as Object. Even if there is {@code null},
   * it is permitted.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": "alpha", "b": null}
   *   String[] codes = {"\"a\": \"alpha\"", "\"b\": null"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Object>[] pairs =
   *           Viv.getKeyValueObjectOrNulls(codes, null);
   *   for (Viv.KeyValue<@Nullable Object> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(codes, null);
   *   pairs = Viv.getKeyValueObjectOrNulls(result);
   *   for (Viv.KeyValue<@Nullable Object> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Object>} value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Object>[]}.
   *         {@code null} if evaluation is failed or the evaluated
   *         result is not {@code HashMap<String, @Nullable Object>}.
   */
  public static KeyValue<@Nullable Object> @Nullable [] getKeyValueObjectOrNulls(
                                                @Nullable Object[] objects,
                                                @Nullable Config config) {
    KeyValue<Object>[] value = null;
    try {
      value = getKeyValueObjectOrNullsEx(objects, config);
    } catch (VivException e) {
      /* null */ ;
    }
    return value;
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Object. Even if there is {@code null},
   * it is permitted.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": "alpha", "b": null}
   *   String code = "{\"a\": \"alpha\", \"b\": null}";
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Object>[] pairs =
   *           Viv.getKeyValueObjectOrNullsEx(code);
   *   for (Viv.KeyValue<@Nullable Object> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(code);
   *   pairs = Viv.getKeyValueObjectOrNullsEx(result);
   *   for (Viv.KeyValue<@Nullable Object> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Object>} value
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Object>[]}
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, @Nullable Object>}.
   */
  public static KeyValue<@Nullable Object>[] getKeyValueObjectOrNullsEx(
                                                @Nullable Object... objects)
                                                throws VivException {
    return getKeyValueObjectOrNullsEx(objects, null);
  }

  /**
   * Gets key-value pairs as an array. (Permit exception)
   * All values are expected as Object. Even if there is {@code null},
   * it is permitted.
   *
   * <p>For example,
   * <pre>{@code
   *   // {"a": "alpha", "b": null}
   *   String[] codes = {"\"a\": \"alpha\"", "\"b\": null"};
   *
   *   // Evaluate directly.
   *   Viv.KeyValue<@Nullable Object>[] pairs =
   *           Viv.getKeyValueObjectOrNullsEx(codes, null);
   *   for (Viv.KeyValue<@Nullable Object> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   *
   *   // Evaluate result.
   *   Result result = Viv.run(codes, null);
   *   pairs = Viv.getKeyValueObjectOrNullsEx(result);
   *   for (Viv.KeyValue<@Nullable Object> pair : pairs) {
   *     System.out.println(pair.key + ": " + pair.value);  // a: alpha, b: null
   *   }
   * }</pre>
   *
   * @param objects object that may be
   *                a {@code HashMap<String, @Nullable Object>} value
   * @param config configuration if it is needed, {@code null} otherwise
   * @return an array of key-value pairs as
   *         {@code KeyValue<@Nullable Object>[]}.
   * @throws VivException it is thrown if evaluation is failed or
   *                          the evaluated result is not
   *                          {@code HashMap<String, @Nullable Object>}.
   */
  public static KeyValue<@Nullable Object>[] getKeyValueObjectOrNullsEx(
                                                @Nullable Object[] objects,
                                                @Nullable Config config)
                                                throws VivException {
    return getKeyValueObjectsEx(objects, config, Object.class, true);
  }

  /**
   * Convert a value into string. Serialize a value into JSON string.
   *
   * @param value an object that may be Byte, Short, Integer, Long,
   *              Float, Double, Boolean, String,
   *              {@code ArrayList<@Nullable Object>},
   *              {@code HashMap<String, @Nullable Object>},
   *              or {@code null}
   * @return a converted value
   */
  public static String makeString(@Nullable Object value) {
    return makeString(value, null);
  }

  /**
   * Converts a value into string. Serialize a value into JSON string.
   *
   * <p>Configuration is available for Infinity and NaN (Not a Number).
   * When it is not setting, "" (empty) is returned.
   *
   * @param value an object that may be Byte, Short, Integer, Long,
   *              Float, Double, Boolean, String,
   *              {@code ArrayList<@Nullable Object>},
   *              {@code HashMap<String, @Nullable Object>},
   *              or {@code null}
   * @param config configuration if needed, {@code null} otherwise
   * @return a converted value
   */
  public static String makeString(@Nullable Object value,
                                  @Nullable Config config) {
    String string = "";
    try {
      string = Evaluator.makeString(value, config);
    } catch (EvaluateException e) {
      ;
    } catch (Exception e) {
      ;
    }
    return string;
  }

  /**
   * Reports Error.
   *
   * <p>The formatted error is returned.<br>
   * The formatted error contains Tag looks like [xxxx].<br>
   * It is outputted into stderr when the argument "enableStderr" is
   * true. 
   *
   * @param errorMessage an error message
   * @param enableStderr enablement of stderr. When it is true, the
   *                     formatted error is outputted into stderr.
   * @return the formatted error.
   */
  static String reportError(String errorMessage, boolean enableStderr) {
    Config config = new Config();
    config.enableStderr(enableStderr);
    return VivException.report("", errorMessage, null, config, false);
  }

  /**
   * Gets file extension if the given word is file path.
   *
   * @param word the file path or code
   * @return a file extension (one of the EXTENSIONS) if the given
   *         word is file path, {@code null} otherwise
   */
  private static @Nullable String getFileExtension(Object word) {
    if (!(word instanceof String) || (String) word == null) {
      return null;
    }

    File file = new File((String) word);

    String name = file.getName();
    String[] parts = name.split("\\.");
    if (parts.length < 2) {
      return null;
    }

    String extension = parts[parts.length - 1].toLowerCase();
    for (String candidate : EXTENSIONS) {
      if (extension.equals(candidate) && file.exists()) {
        return candidate;
      }
    }
    return null;
  }

  /**
   * Gets text message of argument index.
   *
   * @param index the argument index
   * @return text message of the given argument index
   */
  private static String getArgumentText(int index) {
    String suffix = (index < SUFFIXES.length) ? SUFFIXES[index] : "th";
    index++;
    StringBuilder sb = new StringBuilder();
    sb.append(index);
    sb.append(suffix);
    sb.append(" argument");
    return sb.toString();
  }

  /**
   * Extracts parameters.
   *
   * @param parameters VivJson's codes, JSON values, file paths,
   *                   variables, a Configuration, parsed statements,
   *                   a class instance, a class member, or a calling
   *                   class-method
   *                   <ul>
   *                     <li> A VivJson's code, a JSON value, or
   *                          a file path is given as String.
   *                     <li> Some VivJson's codes, JSON values, file
   *                          paths, variables, or Parsed objects are
   *                          given as {@code ArrayList<Object>}.
   *                     <li> Some variables (name/value pairs) are
   *                          given as
   *                          {@code HashMap<String, @Nullable Object>}.
   *                     <li> A JSON value is given as {@link Json Json}.
   *                     <li> Some configurations are given as
   *                          {@link Config Config}.
   *                     <li> Some parsed statements are given as
   *                          {@link Parsed Parsed}.
   *                     <li> A class instance is given as
   *                          {@link Instance Instance}.
   *                     <li> A class member is given as the array of
   *                          Object ({@code Object[]}).<br>
   *                          For example, {@code new Object[]{"foo", "bar"}},
   *                          {@code "return(foo.bar)"}, and
   *                          {@code "return(foo['bar'])"} are equivalent.
   *                          Refer to {@link #makeInstance(Object...)}.
   *                     <li> A calling class-method is given as the
   *                          array of Object ({@code Object[]}).<br>
   *                          Its 1st element is the method name as
   *                          String.<br>
   *                          The following elements are its argument.<br>
   *                          The arguments and the returned value is
   *                          {@code Boolean}, {@code Long},
   *                          {@code Double}, {@code String},
   *                          {@code ArrayList<@Nullable Object>},
   *                          {@code HashMap<String, @Nullable Object>},
   *                          or {@code null}.
   *                          Refer to {@link #makeInstance(Object...)}.
   *                   </ul>
   * @return an array that has 3 objects.
   *         1st object is {@code ArrayList<Object>} or {@code null}.
   *         2nd object is {@link Instance Instance} or {@code null}.
   *         3rd object is {@link Config Config} or {@code null}.
   */
  private static Object[] extractParameters(Object[] parameters) {
    @Nullable Instance instance  = null;
    for (Object parameter : parameters) {
      if (parameter instanceof Instance) {
        instance = (Instance) parameter;
        break;
      }
    }

    ArrayList<Object> extractedParameters = new ArrayList<>();
    @Nullable Config config = null;
    for (Object parameter : parameters) {
      if (parameter instanceof ArrayList) {
        @SuppressWarnings("unchecked")
        ArrayList<@Nullable Object> objects =
                                      (ArrayList<@Nullable Object>) parameter;
        for (@Nullable Object object : objects) {
          if (!(object instanceof String) && !(object instanceof Json)
              && !(object instanceof Parsed)
              && !(object instanceof HashMap && isValidType(object))) {
            return new Object[] {null, null, null};
          }
          extractedParameters.add(object);
        }
      } else if (parameter instanceof HashMap) {
        if (!isValidType(parameter)) {
          return new Object[] {null, null, null};
        }
        extractedParameters.add(parameter);
      } else if (parameter instanceof String || parameter instanceof Json
                 || parameter instanceof Parsed) {
        extractedParameters.add(parameter);
      } else if (parameter instanceof Config) {
        config = (Config) parameter;
      } else if (parameter instanceof Instance) {
        ;
      } else if (parameter instanceof Object[]) {
        Statement statement = null;
        if (instance != null) {
          Evaluator evaluator = instance.evaluator;
          if (evaluator != null
              && ((Object[]) parameter).length >= 1
              && ((Object[]) parameter)[0] instanceof String) {
            String name = (String) (((Object[]) parameter)[0]);
            if (name.charAt(0) != '_') {
              try {
                Object object = evaluator.get(name);
                if (object instanceof CalleeRegistry) {
                  statement = makeCall((Object[]) parameter);
                  if (statement == null) {
                    return new Object[] {null, null, null};
                  }
                }
              } catch (EvaluateException e) {
                ;
              }
            }
          }
        }
        if (statement != null) {
          statement = new Return(new Token(Token.Type.RETURN), statement);
        } else {
          statement = makeReturn((Object[]) parameter);
          if (statement == null) {
            return new Object[] {null, null, null};
          }
        }
        ArrayList<Statement> array = new ArrayList<>();
        array.add(statement);
        extractedParameters.add(array);
      } else {
        return new Object[] {null, null, null};
      }
    }
    return new Object[] {extractedParameters, instance, config};
  }

  /**
   * Concatenates if + option is given.
   *
   * @param parameters parameters that may be have "+"
   * @return parameters with index and Error message
   *         <ul>
   *           <li> Member's variable "value" has parameters with index
   *                that are represented as
   *                {@code ArrayList<Object[]>} if success,
   *                {@code null} otherwise.
   *                {@code ArrayList<Object[]>} has 2 objects. 1st object
   *                is index as Integer. 2nd object is parameter.
   *           <li> Member's variable "errorMessage" has "" if success,
   *                error message otherwise.
   *         </ul>
   */
  private static Result concatenateParameters(ArrayList<Object> parameters) {
    ArrayList<Object[]> concatenated = new ArrayList<>();
    Object[] ahead = null;

    int index = -1;
    for (Object parameter : parameters) {
      index++;

      if (parameter instanceof String && ((String) parameter).equals("+")) {
        if (concatenated.size() == 0) {
          String errorMessage =
                      reportError("No statement before + option", false);
          return new Result(null, errorMessage);
        }
        ahead = concatenated.remove(concatenated.size() - 1);
        continue;
      }

      Integer modifiedIndex = index;
      if (ahead != null) {
        if (!(parameter instanceof String)) {
          String errorMessage = reportError("Bad data after + option", false);
          return new Result(null, errorMessage);
        }
        modifiedIndex = (Integer) ahead[0];
        parameter = (String) ahead[1] + (String) parameter;
        ahead = null;
      }
      concatenated.add(new Object[] {modifiedIndex, parameter});
    }

    if (ahead != null) {
      String errorMessage = reportError("No statement after + option", false);
      return new Result(null, errorMessage);
    }
    return new Result(concatenated, "");
  }

  /**
   * Makes Call statement from Object's array.
   *
   * <p>For example, a class method is called as below.
   * <pre>{@code
   *    String code = "function test(x, y) {"
   *                + "  z = x.a + x.b.1 + y"
   *                + "  return(z)"
   *                + "}";
   *    // {"a": 100, "b": [1.0, 2.0]}
   *    ArrayList<Double> list = new ArrayList<>();
   *    list.add(1.0);
   *    list.add(2.0);
   *    HashMap<String, Object> map = new HashMap<>();
   *    map.put("a", 100);
   *    map.put("b", list);
   *
   *    try {
   *      Viv.Instance instance = Viv.makeInstanceEx(code);
   *      Object result = Viv.runEx(instance, new Object[] {"test", map, 3});
   *      System.out.println(result);  // 105.0
   *    } catch (VivException e) {
   *      System.err.println(e.getMessage());
   *    }
   * }</pre>
   * The 1st element of the given array is the method name as String.<br>
   * The following elements are its argument.<br>
   * The arguments and the returned value is the following Java's Object.
   * <ul>
   *   <li> {@code Boolean }
   *   <li> {@code Long }
   *   <li> {@code Double }
   *   <li> {@code String }
   *   <li> {@code ArrayList<@Nullable Object> }
   *   <li> {@code HashMap<String, @Nullable Object> }
   *   <li> {@code null }
   * </ul>
   * "{@code @Nullable Object}" of ArrayList/HashMap is also the above
   * Object.
   *
   * @param objects A method name and arguments as array of Object
   * @return {@link Call} statement if success, {@code null} otherwise
   */
  private static @Nullable Call makeCall(Object[] objects) {
    if (objects.length < 1 || !(objects[0] instanceof String)) {
      return null;
    }
    Identifier name = new Identifier(new Token(Token.Type.IDENTIFIER, (String) objects[0]));
    ArrayList<Statement> arguments = new ArrayList<>();
    for (int i = 1; i < objects.length; i++) {
      Object argument = objects[i];
      if (!isValidType(argument)) {
        return null;
      }
      Value value = new Value(argument, getArgumentText(i - 1));
      arguments.add(value);
    }
    return new Call(name, new Array(arguments));
  }

  /**
   * Makes Return statement from class member's array.
   *
   * <p>For example, JSON object's member is extracted as below.
   * The following {@code new Object[]{"a",1,"c"}} is equivalent to
   * {@code "return(a.1.c)"}. This method makes this "return"
   * statement.
   * <pre>{@code
   *   // {"a": [10, {"b": null, "c": "test"}], "d": {"e": 3.5}}
   *   Viv.Instance instance = Viv.makeInstance(
   *     "{\"a\": [10, {\"b\": null, \"c\": \"test\"}], \"d\": {\"e\": 3.5}}");
   *   try {
   *     System.out.println(
   *       Viv.runEx(instance, new Object[]{"a",1,"c"}));  // "test"
   *   } catch (VivException e) {
   *     System.err.println(e.getMessage());
   *   }
   * }</pre>
   * The elements of Array is string or number (Short, Integer, or Long).
   * The 1st element must be string.
   *
   * @param objects A class member is given as the array of Object
   * @return {@link Return} statement if success, {@code null} otherwise
   */
  private static @Nullable Return makeReturn(Object[] objects) {
    ArrayList<Statement> members = new ArrayList<>();
    boolean isFirst = true;
    for (Object object : objects) {
      Statement member;
      if (object instanceof String) {
        String text = (String) object;
        member = isFirst
                 ? new Identifier(new Token(Token.Type.IDENTIFIER, text))
                 : new Literal(new Token(Token.Type.STRING, text));
      } else if (object instanceof Number) {
        if (isFirst) {
          return null;  // Illegal
        }
        double number = ((Number) object).doubleValue();
        if (number % 1.0 != 0.0) {
          return null;  // Illegal
        }
        member = new Literal(new Token(Token.Type.NUMBER,
                                       String.valueOf((int) number)));
      } else {
        return null;  // Illegal
      }
      members.add(member);
      isFirst = false;
    }
    return new Return(new Token(Token.Type.RETURN), new Get(members));
  }

  /**
   * Collects only valid elements.
   * Valid type: {@code null}, Boolean, Number(such as Long, Double),
   *             String, {@code ArrayList<@Nullable Object>},
   *             {@code HashMap<String, @Nullable Object>}
   *
   * @param value the value for the returned value
   * @return value if success, Environment.UNDEFINED otherwise
   */
  private static @Nullable Object collectOnlyValidElements(
                                                  @Nullable Object value) {
    if (value == null
        || value instanceof Boolean
        || value instanceof Number
        || value instanceof String) {
      return value;
    }

    if (value instanceof ArrayList) {
      ArrayList<@Nullable Object> fixed = new ArrayList<>();

      @SuppressWarnings("unchecked")
      ArrayList<@Nullable Object> objects =
                                          (ArrayList<@Nullable Object>) value;
      for (@Nullable Object object : objects) {
        object = Viv.collectOnlyValidElements(object);
        if (object != Environment.UNDEFINED) {
          fixed.add(object);
        }
      }
      return fixed;
    }

    if (value instanceof HashMap) {
      HashMap<String, @Nullable Object> fixed = new HashMap<>();

      @Nullable HashMap<String, @Nullable Object> objects = castHashMap(value);
      if (objects != null) {
        Iterator<Map.Entry<String, @Nullable Object>> iterator =
                                                objects.entrySet().iterator();
        while (iterator.hasNext()) {
          @SuppressWarnings("null")
          Map.Entry<String, @Nullable Object> entry = iterator.next();
          @SuppressWarnings("null")
          String k = entry.getKey();
          @Nullable Object v = entry.getValue();
          v = Viv.collectOnlyValidElements(v);
          if (v != Environment.UNDEFINED) {
            fixed.put(k, v);
          }
        }
        return fixed;
      }
    }

    return Environment.UNDEFINED;
  }

  /**
   * Judges whether the given value is valid or not.
   * Valid type: {@code null}, Boolean, Number(such as Long, Double),
   *             String, {@code ArrayList<@Nullable Object>},
   *             {@code HashMap<String, @Nullable Object>}
   *
   * @param value the value for Injection/Value statement
   * @return true if valid, false otherwise
   */
  private static boolean isValidType(@Nullable Object value) {
    if (value == null
        || value instanceof Boolean
        || value instanceof Number
        || value instanceof String) {
      return true;
    }

    if (value instanceof ArrayList) {
      @SuppressWarnings("unchecked")
      ArrayList<@Nullable Object> objects =
                                          (ArrayList<@Nullable Object>) value;
      for (@Nullable Object object : objects) {
        if (!isValidType(object)) {
          return false;
        }
      }
      return true;
    }

    if (value instanceof HashMap) {
      @Nullable HashMap<String, @Nullable Object> objects = castHashMap(value);
      if (objects == null) {
        return false;
      }
      Iterator<@Nullable Object> iterator = objects.values().iterator();
      while (iterator.hasNext()) {
        if (!isValidType(iterator.next())) {
          return false;
        }
      }
      return true;
    }

    return false;
  }

  /**
   * Judges whether the given class is expected or not.
   *
   * @param object a target class
   * @param expectedClass the expected class
   * @return true if valid, false otherwise
   */
  private static boolean isValidClassType(@Nullable Object object,
                                          Class<?> expectedClass) {
    if (object == null) {
      return false;
    }

    if (expectedClass == Number.class) {
      return object instanceof Number;
    }

    if (expectedClass == Object.class) {
      return true;
    }

    Class<?> targetClass = object.getClass();
    return targetClass == expectedClass;
  }

  /**
   * Gets the expected value.
   *
   * @param value the original value
   * @param expectedClass the expected class
   * @param unexpectedIsNull the behavior for unexpected class of value.
   *        When the unexpected class of value is found, the value will be
   *        null and the reason is returned.
   *        <pre>
   *        | unexpectedIsNull | value | error-message   |
   *        |------------------|-------|-----------------|
   *        | false            | null  | (reason is set) |
   *        | true             | null  | (empty)         |
   *        </pre>
   * @return the expected value and error-message.
   */
  private static Result getExpectedValue(@Nullable Object value,
                                         Class<?> expectedClass,
                                         boolean unexpectedIsNull) {
    String type = null;
    if (value == null) {
      if (!unexpectedIsNull) {
        type = "null";
      }
    } else if (!isValidClassType(value, expectedClass)) {
      if (!unexpectedIsNull) {
        type = value.getClass().getSimpleName();
      }
      value = null;
    } else if (value instanceof Float) {
      value = (Double) (((Float) value).doubleValue());
    } else if (value instanceof Byte || value instanceof Short
               || value instanceof Integer) {
      value = (Long) (((Number) value).longValue());
    }
    String errorMessage = (type == null)
                          ? ""
                          : reportError(type + " is found, though "
                                        + expectedClass.getSimpleName()
                                        + " is expected.",
                                        false);
    return new Result(value, errorMessage);
  }

  /**
   * Casts as {@code ArrayList<Statement>}.
   *
   * @param object object that may be a {@code ArrayList<Statement>} value
   * @return a {@code ArrayList<Statement>} value if success,
   *         {@code null} otherwise
   */
  private static @Nullable ArrayList<Statement> castArrayListStatement(
                                                  @Nullable Object object) {
    if (!(object instanceof ArrayList)
        || !(((ArrayList<?>) object).stream()
            .allMatch(Statement.class::isInstance))) {
      return null;
    }

    @SuppressWarnings("unchecked")
    ArrayList<Statement> array = (ArrayList<Statement>) object;
    return array;
  }

  /**
   * Casts as {@code HashMap<String, @Nullable Object>}.
   *
   * @param object object that may be a {@code HashMap<String, @Nullable Object>}
   *               value
   * @return a {@code HashMap<String, @Nullable Object>} value if success,
   *         {@code null} otherwise
   */
  private static @Nullable HashMap<String, @Nullable Object> castHashMap(
                                                  @Nullable Object object) {
    if (!(object instanceof HashMap)
        || !((HashMap<?, ?>) object).keySet().stream()
            .allMatch(String.class::isInstance)) {
      return null;
    }

    @SuppressWarnings("unchecked")
    HashMap<String, @Nullable Object> map = (HashMap<String, @Nullable Object>) object;
    return map;
  }

  /**
   * Judges whether it is setting implicit variable or not.
   * The implicit variable is "_". Its setting is "_ = ...".
   *
   * @param statements statements of script
   * @return true if setting implicit variable, false otherwise
   */
  private static boolean isSettingImplicitVariable(
                                            ArrayList<Statement> statements) {
    if (statements.size() != 1) {
      return false;
    }
    @Nullable Statement statement = statements.get(0);
    if (!(statement instanceof Set)) {
      return false;
    }
    if (((Set) statement).members.isEmpty()) {
      return false;
    }
    @Nullable Statement member = ((Set) statement).members.get(0);
    if (!(member instanceof Identifier)) {
      return false;
    }
    return ((Identifier) member).name.lexeme.equals("_");
  }

  /**
   * Fix implicit variable.
   * The original implicit variable is "_". It is fixed as "_[index]".
   *
   * @param statements statements of script
   * @param index the index of implicit variable. It starts from 0.
   */
  private static void fixSettingImplicitVariable(
                                ArrayList<Statement> statements, int index) {
    if (statements.isEmpty()) {
      return;
    }
    @Nullable Statement statement = statements.get(0);
    if (!(statement instanceof Set)) {
      return;
    }
    if (((Set) statement).members.isEmpty()) {
      return;
    }
    Literal selector = new Literal(new Token(Token.Type.NUMBER,
                                             String.valueOf(index)));
    ((Set) statement).members.add(1, selector);
  }
}
