/* Unit-test for function in Evaluator
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.benesult.vivjson.Viv.Result;
import java.util.ArrayList;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Evaluator function test class.
 * Unit-test for function call and its definition
 *
 * <p>Environment
 * <ul>
 *   <li> Java 9 or later
 *   <li> JUnit 5.11 or later
 * </ul>
 *
 * <p>Last modified: 2025-03-30
 *
 * @author Fumiaki Motegi (motegi@benesult.com)
 */
public class EvaluatorFunctionTest {
  @ParameterizedTest
  @CsvSource({
    "'function x2(a) {return(a * 2)}', 'x2(1.5)', 3.0, float, false",
    "'function x2(a) {return(a*2)}', 'x2(3)', 6, int, false",
    "'x = test(1), function test(a) {return(a*100)}', 'x', null, null, true",  // Error because certain function is executed before its definition.
    "'function test(a) {return(a*100)}, x = [test(1)], function test(a) {return(a+10)}, x += test(1)', 'x', '[100, 11]', array, false",
    "'function test() {for (i = 0; i < 10; i += 1) { if (i > 5) {return(i)}}}', 'test()', 6, int, false",
    "'function test() {i = 0, for (; i < 10; i += 1) { if (i > 5) {return(i)}}}', 'test()', 6, int, false",
    "'function test() {i = 0, for (; i < 10; i += 1) { if (i > 5) {break}}, return(i)}', 'test()', 6, int, false",
    "'function test() {i = 0, for (; i < 10; i += 1) { if (i > 5) {break}}}', 'test()', {\"i\": 6}, block, false",
    "'function test(a) {for (i = 0; i < 10; i += 1) {a /= 2}, return(a)}', 'test(100)', 0.09765625, float, false",
    "'function test(a) {for (i = 0; i < 10; i += 1) { if (i >= 5) {continue}, a /= 2}, return(a)}', 'test(100)', 3.125, float, false",
    "'function test(a) {for (i = 0; i < 10; i += 1) { if (i >= 5) {break}, a /= 2}, return(a)}', 'test(100)', 3.125, float, false",
    "'function test(a) {i = 0, for (; i < 10; i += 1) { if (i >= 5) {continue}, a /= 2}, b = a}', 'test(100)', '{\"i\": 10, \"b\": 3.125}', block, false",
    "'function test(a) {i = 0, for (; i < 10; i += 1) { if (i >= 5) {break}, a /= 2}, b = a}', 'test(100)', '{\"i\": 5, \"b\": 3.125}', block, false",
    "'function test(a) {i = 0, for (; i < 10; i += 1) { if (i >= 5) {return(\"foo\")}, a /= 2}, b = a}', 'test(100)', 'foo', string, false",
    "'function test() {sum = 0 for (i = 0; i < 10; i += 1) {sum += i}}, a = test()', 'a', '{\"sum\": 45}', block, false",
    "'function test() {sum = 0 for (i = 0; i < 10; i += 1) {sum += i} := sum}, a = test()', 'a', 45, int, false",
    "'function test() {sum = 0 for (i = 0; i < 10; i += 1) {sum += i} := sum sum += 100}, a = test()', 'a', 45, int, false",
    "'function test() {sum = 0 for (i = 0; i < 10; i += 1) {sum += i} := sum sum += 100 return(sum)}, a = test()', 'a', 145, int, false",
    "'function test() {a = 10}, x = test()', 'x', {\"a\": 10}, block, false",
    "'function test() {a = 10, a = 20}, x = test()', 'x', {\"a\": 20}, block, false",
    "'function test() {a = 10, a += 20}, x = test()', 'x', {\"a\": 30}, block, false",
    "'function test() {a = 10, break, a += 20}, x = test()', 'x', null, null, true",  // It is invalid that there is "break" just under the function.
    "'function test() {a = 10, continue, a += 20}, x = test()', 'x', null, null, true",  // It is invalid that there is "continue" just under the function.
    "'function test() {a = 10, return, a += 20}, x = test()', 'x', {\"a\": 10}, block, false",
    "'function add(a, b) {return(a + b)} x = add(3, 2, 1)', 'x', 5, int, false",
    "'function add_dirty(a, b) {return(a + b + _[2])}, x = add_dirty(3, 2, 1)', 'x', 6, int, false",
    "'function add_whole() { sum = 0 for (value in _) { sum += value } return(sum) }, x = add_whole(-5, 10, 1.5)', 'x', 6.5, float, false",
    "'function test(a, b) { return (string(a) + \", \" + string(b)) }, x = test(100)', 'x', '100, null', string, false",
    "'function enclosure(a) {x = a, function closure(y) {return(x + y)}, return(closure)}, z1 = enclosure(100), z2 = enclosure(200), a = z1(5), b = z2(10)', '[a, b]', '[105, 210]', array, false",  // Closure
    "'a = \">\", b= null, function run(function worker) {a=\"::\", worker()}, run({b = a + \" test\"})', 'b', ':: test', string, false",  // Anonymous function in argument.
    "'a = \">\", function run(function worker) {worker()}, b= null, a=\"::\", run({b = a + \" test\"})', 'b', ':: test', string, false",  // Anonymous function in argument.
    "'a = \">\", b= null, function run(function worker) {a=\"::\", worker()}, run() {b = a + \" test\"}', 'b', ':: test', string, false",  // Anonymous function in argument.
    "'a = \">\", b= null, function run(function worker) {a=\"::\", worker()}, run {b = a + \" test\"}', 'b', ':: test', string, false",  // Anonymous function in argument.
    "'a = \">\", b= null, function run(function worker) {a=\"::\", worker()}, c = run {b = a + \" test\"}', 'c', {}, block, false",  // Anonymous function in argument.
    "'a = \">\", b= null, function run(function worker) {a=\"::\", worker(), :=100}, c = run {b = a + \" test\"}', 'c', 100, int, false",  // Anonymous function in argument.
    "'a = [1, 2, 3], function x2(before, list, after) {for (i = 0; i < len(list); i += 1) {list[i] *= 2}, return([before, list, after])}, b = x2(a, a, a)', '[a, b]', '[[1, 2, 3], [[1, 2, 3], [2, 4, 6], [1, 2, 3]]]', array, false",  // Call by value
    "'a = [1, 2, 3], function x2(before, reference list, after) {for (i = 0; i < len(list); i += 1) {list[i] *= 2}, return([before, list, after])}, b = x2(a, a, a)', '[a, b]', '[[2, 4, 6], [[1, 2, 3], [2, 4, 6], [1, 2, 3]]]', array, false",  // Call by reference
    "'a = {\"x\": 10, \"y\": 20}, function x2(before, map, after, k) {for (pair in map) {map[pair[0]] = pair[1] * 2}, k *= 2, return([before, map, after, k])}, b = 30, c = x2(a, a, a, b)', '[a, b, c]', '[{\"x\": 10, \"y\": 20}, 30, [{\"x\": 10, \"y\": 20}, {\"x\": 20, \"y\": 40}, {\"x\": 10, \"y\": 20}, 60]]', array, false",  // Call by value
    "'a = {\"x\": 10, \"y\": 20}, function x2(before, reference map, after, reference k) {for (pair in map) {map[pair[0]] = pair[1] * 2}, k *= 2, return([before, map, after, k])}, b = 30, c = x2(a, a, a, b)', '[a, b, c]', '[{\"x\": 20, \"y\": 40}, 30, [{\"x\": 10, \"y\": 20}, {\"x\": 20, \"y\": 40}, {\"x\": 10, \"y\": 20}, 60]]', array, false",  // Call by reference
    "'function import(a) {return(a*2)}', 'import(3)', null, null, true",  // "import" is reserved word.
    "'function super(a) {return(a*2)}', 'super(3)', null, null, true",  // "super" is reserved word.
    "'', 'fake()', null, null, true",  // fake function is not existed.
  })
  public void test(String preparedExpression, String targetExpression,
                   String expectedValue, String expectedType,
                   boolean isError) {
    Config config = new Config();
    config.setMaxDepth(14);

    String text = "return(" + targetExpression + ")";
    Result result = Viv.run(preparedExpression, text, config);
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty() != isError);
    String value = Viv.makeString(result.value);
    if (expectedType.equals("float")) {
      Double d1 = Double.valueOf(value);
      Double d2 = Double.valueOf(expectedValue);
      assertTrue(Math.abs((d2 == 0.0) ? (d1 - d2) : (1.0 - d1 / d2)) < 0.00001);
    } else if (expectedType.equals("block")) {
      assertTrue(UtilsForTest.isEqualMap(value, expectedValue));
    } else {
      assertEquals(value, expectedValue);
    }
    @Nullable String type = UtilsForTest.getType(result.value);
    assertEquals(type, expectedType);
  }

  @Test
  public void testDepth() {
    String text =
        "function test() {for (i = 0; i < 10; i += 1) { if (i > 5) {return(i)}}}, return(test())";

    Config config = new Config();
    config.setMaxDepth(9);
    try {
      Viv.getIntegerEx(text, config);
      fail("Exception is not happened in testDepth");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }

    config.setMaxDepth(10);
    try {
      Integer value = Viv.getIntegerEx(text, config);
      assertEquals(value, 6);
    } catch (VivException e) {
      fail("Exception happen in testDepth");
    }
  }

  @Test
  public void testFirstClass() {
    Result result = Viv.run("src/test/func_array.viv");
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof ArrayList);
    @SuppressWarnings("unchecked")
    ArrayList<Object> array = (ArrayList<Object>) result.value;
    @Nullable Object object = array.get(0);
    assertTrue(object instanceof Long);
    assertEquals((Long) object, 15L);
    object = array.get(1);
    assertTrue(object instanceof ArrayList);
    @SuppressWarnings("unchecked")
    ArrayList<Long> values = (ArrayList<Long>) object;
    assertEquals(values.size(), 4);
    assertEquals(values.get(0), 15);
    assertEquals(values.get(1), 5);
    assertEquals(values.get(2), 50);
    assertEquals(values.get(3), 2);
    String string = Viv.makeString(result.value);
    assertEquals(string, "[15, [15, 5, 50, 2], [15, 5, 50, 2], [15, 5, 50, 2], 3, [3, 100, 100.0, \"100\"], [3, 100, 100.0, \"100\"], [3, 100, 100.0, \"100\"]]");
  }
}
