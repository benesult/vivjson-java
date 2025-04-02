/* Unit-test for get/set/injection in Evaluator
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.benesult.vivjson.Viv.Result;
import java.util.HashMap;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Evaluator get/set/injection test class.
 *
 * <p>Environment
 * <ul>
 *   <li> Java 9 or later
 *   <li> JUnit 5.11 or later
 * </ul>
 *
 * <p>Last modified: 2025-03-20
 *
 * @author Fumiaki Motegi (motegi@benesult.com)
 */
public class EvaluatorGetSetTest {
  @ParameterizedTest
  @CsvSource({
    "'a = 1.5', 'a', 1.5, float, false",
    "'a = 1.5, a+=0.5', 'a', 2.0, float, false",
    "'a = 3 > 2', 'a', true, boolean, false",
    "'a = 3 <= 2', 'a', false, boolean, false",
    "'a = \"xyz\", b = a == \"xyz\"', 'b', true, boolean, false",
    "'a = \"xyz\", b = a != \"xyz\"', 'b', false, boolean, false",
    "'a = {\"x\": 100}', 'a.x', 100, int, false",
    "'a = {\"x\": 100}', 'a.y', null, null, false",
    "'a = {\"x\": 100, \"_y\": 20}', 'a.x', 100, int, false",
    "'a = {x: 10}', 'a.x', 10, int, false",
    "'a: {x: -1e4}', 'a[\"x\"]', -10000.0, float, false",
    "'\"a\": {\"x\": \"true\", y: \"false\"}', 'a[\"x\"] + \"&\" + a[\"y\"]', 'true&false', string, false",
    "'a = [100 10 20]', 'a[0] + a[1] + a[2]', 130, int, false",
    "'a = [100 10 20] a[1] += 1000', 'a[0] + a[1] + a[2]', 1130, int, false",
    "'a = [1 5 {b: {c: 10} d: -20}]', 'a.2.b.c', 10, int, false",
    "'a = [1 5 {b: {c: 10} d: -20}]', 'a[2][\"b\"][\"c\"]', 10, int, false",
    "'a = [1 5 {b: {c: 10} d: -20}]', 'a[2].b[\"c\"]', 10, int, false",
    "'a = [1 5 {b: {c: 10} d: -20}]', 'a[-1].b.c', 10, int, false",
    "'', 'a', null, null, false",  // The undefined variable's value is null.
    "'', 'a.b', null, null, false",  // The member's value of the undefined variable is null.
    "'', 'a[\"b\"]', null, null, false",  // The member's value of the undefined variable is null.
    "'', 'a[1]', null, null, false",  // The member's value of the undefined variable is null.
    "'a = {}', 'a', {}, block, false",
    "'a = []', 'a', [], array, false",
    "'a = [true, 100]', 'a[0]', true, boolean, false",
    "'a = [true, 100]', 'a[1]', 100, int, false",
    "'a = [true, 100]', 'a[2]', null, null, false",  // null is gotten even if out of range
    "'a = [true, 100]', 'a[-1]', 100, int, false",
    "'a = [true, 100]', 'a[-2]', true, boolean, false",
    "'a = [true, 100]', 'a[-3]', null, null, false",  // null is gotten even if out of range
    "'a = [true, 100]', 'a[false]', null, null, true",  // boolean can not be used as index.
    "'a = [true, 100], a[0] = 20', 'a[0]', 20, int, false",
    "'a = [true, 100], x = 0, a[x] = 10', 'a[0]', 10, int, false",
    "'a = [true, 100], x = 0, a[x] = 10', 'a[x]', 10, int, false",
    "'a = [true, 100], x = 0', 'a.x', null, null, true",  // a.x is invalid.
    "'a = [true, 100], b = 2.5 * 0.4', 'a[b]', 100, int, false",
    "'a = [true, 100]', 'a[2.5 * 0.4]', 100, int, false",
    "'a = [true, 100]', 'a[2.5 * 0.3]', null, null, true",  // 2.5 * 0.3 = 0.75 is invalid as index.
    "'a = [true, 100], a[2] = 20', 'a[0]', null, null, true",  // a[2] makes error.
    "'a = [true, 100], a += [false, 20]', 'a', '[true, 100, [false, 20]]', array, false",
    "'a = {x: true y: 100} a += {x: false, y: 20}', 'a', '{\"x\": true, \"y\": 120}', block, false",
    "'x = {\"a\": 100, \"b\": [true, {\"c\": 3, \"0\": \"foo\"}]}', 'x[\"b\"][0]', true, boolean, false",
    "'x = {\"a\": 100, \"b\": [true, {\"c\": 3, \"0\": \"foo\"}]}', 'x[\"b\"].0', true, boolean, false",
    "'x = {\"a\": 100, \"b\": [true, {\"c\": 3, \"0\": \"foo\"}]}', 'x.b[0]', true, boolean, false",
    "'x = {\"a\": 100, \"b\": [true, {\"c\": 3, \"0\": \"foo\"}]}', 'x.b.0', true, boolean, false",
    "'x = {\"a\": 100, \"b\": [true, {\"c\": 3, \"0\": \"foo\"}]}', 'x.b.-2', true, boolean, false",
    "'x = {\"a\": 100, \"b\": [true, {\"c\": 3, \"0\": \"foo\"}]}', 'x.b[-2]', true, boolean, false",
    "'x = {\"a\": 100, \"b\": [true, {\"c\": 3, \"0\": \"foo\"}]}', 'x.b[\"0\"]', null, null, true",  // The index of array must be number.
    "'x = {\"a\": 100, \"b\": [true, {\"c\": 3, \"0\": \"foo\"}]}', 'x.b.1.0', 'foo', string, false",
    "'x = {\"a\": 100, \"b\": [true, {\"c\": 3, \"0\": \"foo\"}]}', 'x.b.1[\"0\"]', 'foo', string, false",
    "'x = {\"a\": 100, \"b\": [true, {\"c\": 3, \"0\": \"foo\"}]}', 'x.b.1[0]', 'foo', string, false",
    "'x = {\"a\": 100, \"b\": [true, {\"c\": 3, \"0\": \"foo\"}]}', 'x.b.-1[0]', 'foo', string, false",
    "'x = {\"a\": 100, \"b\": [true, {\"c\": 3, \"0\": \"foo\"}]}', 'x.b[-1].0', 'foo', string, false",
    "'x = {\"a\": 100, \"b\": [true, {\"c\": 3, \"0\": \"foo\"}]}', 'x.b.-1.0', 'foo', string, false",
    "'a = {b: 3, c: {d: true, e: [null, \"x\", false]}}', 'a.c.e.1', 'x', string, false",
    "'a = {b: 3, c: {d: true, e: [null, \"x\", false]}}', 'a[\"c\"].e[-2]', 'x', string, false",
    "'b = 1, a = {b: 3, c: {d: true, e: [null, \"x\", false]}}', 'a[\"c\"].e[b]', 'x', string, false",
    "'a = {b: 1, \"c\": 2}; a.d = 3', 'a[\"d\"]', 3, int, false",
    "'a = [{\"b\": 1, c: 2}, 3], a[0].b = 5', 'a.0[\"b\"]', 5, int, false",
    "'a = [{\"b\": 1, c: 2}, 3], a[0].b = 5', 'a.0. b', 5, int, false",
    "'a = [{\"b\": 1, c: 2}, 3], a[0].b = 5', 'a.0.*', null, null, true",  // .* is invalid.
    "'a = [{\"b\": 1, c: 2}, 3], a[0].b = 5', 'a.0. *', null, null, true",  // .* is invalid.
    "'a = {\"0\": 100, \"1\": 50}', 'a.0', 100, int, false",
    "'a = {\"0\": 100, \"1\": 50}, a.0 += 80', 'a.0', 180, int, false",
    "'a = {\"0\": [10, 30], \"1\": 50}, a.0 = 80', 'a.0', 80, int, false",
    "'a = {\"0\": [10, 30], \"1\": 50}, a.0.1 += 80', 'a.0[1]', 110, int, false",
    "'a = {\"0\": [10, 30], \"1\": 50}, a.0.1 += 80', 'a.0.1', 110, int, false",
    "'a = {\"0\": [10, [30, 70]], \"1\": 50}, a.0.1.1 += 80', 'a.0.1', '[30, 150]', array, false",
    "'a = [1, 2]', 'a[0.5]', null, null, true",  // Index must be integer.
    "'a = [1, 2]', 'a[1.0]', 2, int, false",
    "'a = [1, 2], a[0.5] = 1.5', 'a', null, null, true",  // Index must be integer.
    "'a = [1, 2], a[1.0] = 1.5', 'a', '[1, 1.5]', array, false",
    "'x = 10, y = if (x >= 0) { s = \"+\" } else { s = \"-\" }', 'y', '{\"s\": \"+\"}', block, false",
    "'function x2(k) {return(_[0] * 2)}', 'x2(3)', 6, int, false",
    "'function x2(k) {_[0] *= 2 return(k)}', 'x2(3)', 6, int, false",
    "'function x2(k) {return(k * 2)}', 'x2(3)', 6, int, false",
    "'function x2(k) {k *= 2 return(_[0])}', 'x2(3)', 6, int, false",
    "'a = {b: 3}, function x2(k) {k.b *= 2 return(k.b)}', 'x2(a)', 6, int, false",
    "'a = {b: 3}, function x2(k) {k.b *= 2 return(_[0][\"b\"])}', 'x2(a)', 6, int, false",
    "'a = {b: 3}, function x2(k) {_[0][\"b\"] *= 2 return(k.b)}', 'x2(a)', 6, int, false",
    "'a = {b: 3}, function x2(k) {k.b *= 2 return(k.b)}, x2(a)', 'a.b', 3, int, false",
    "'a = {b: 3}, function x2(k) {return(k.b * 2)}', 'x2(a)', 6, int, false",
    "'while = 3, x = 2, while(x < 10) {x+=10}', '[while, x]', '[3, 12]', array, false",  // "while" is not reserved word.
    "'function while(a) { return(true) }', 'while(10)', true, boolean, false",  // "while" is not reserved word.
    "'for = 3', 'for', null, null, true",  // "for" is reserved word.
    "'import = 3', 'import', null, null, true",  // "import" is reserved word.
    "'super = 3', 'super', null, null, true",  // "super" is reserved word.
    "'___#RESULT#___ = 1', '___#RESULT#___', null, null, true",  // "RESULT#___ = 1" is ignored as comment.
    "'___i___ = 1', '___i___', null, null, true",  // It is invalid that both of prefix and suffix are "___".
    "'______ = 1', '______', null, null, true",  // It is invalid that both of prefix and suffix are "___".
    "'_____ = 1', '_____', 1, int, false",
    "'a = {b: 3, c:[2, 1]}, remove(a)', 'a', null, null, false",
    "'a = {b: 3, c:[2, 1]}, remove(a.b)', 'a', '{\"c\": [2, 1]}', block, false",
    "'a = {b: 3, c:[2, 1]}, remove(a.c)', 'a', '{\"b\": 3}', block, false",
    "'a = {b: 3, c:[2, 1]}, remove(a[\"b\"])', 'a', '{\"c\": [2, 1]}', block, false",
    "'a = {b: 3, c:[2, 1]}, remove(a.c.0)', 'a', '{\"b\": 3, \"c\": [1]}', block, false",
    "'a = {b: 3, c:[2, 1]}, remove(a.c.1)', 'a', '{\"b\": 3, \"c\": [2]}', block, false",
    "'a = {b: 3, c:[2, 1]}, remove(a.c.-2)', 'a', '{\"b\": 3, \"c\": [1]}', block, false",
    "'a = {b: 3, c:[2, 1]}, remove(a.c.-1)', 'a', '{\"b\": 3, \"c\": [2]}', block, false",
    "'a = {b: 3, c:[2, 1]}, remove(a.c[-1])', 'a', '{\"b\": 3, \"c\": [2]}', block, false",
    "'a = {b: 3, c:[2, 1]}, remove(a[\"c\"][-1])', 'a', '{\"b\": 3, \"c\": [2]}', block, false",
    "'a = {b: 3, c:[2, 1]}, remove(a[\"c\"])', 'a', '{\"b\": 3}', block, false",
    "'a = {b: 3, c:[2, 1]}, remove(a.c.2)', 'a', null, null, true",  // Out of range
    "'a = {b: 3, c:[2, 1]}, remove(a[\"c\"][2])', 'a', null, null, true",  // Out of range
    "'a = {b: 3, c:[2, 1]}, remove(a[\"c\"][-3])', 'a', null, null, true",  // Out of range
    "'a = {b: 3, c:[2, 1]}, remove(a.d)', 'a', '{\"b\": 3, \"c\": [2, 1]}', block, false",  // Nothing
    "'a = {b: 3, c:[2, 1], d: {e: false, remove(c[1])}}', 'a', '{\"b\": 3, \"c\": [2], \"d\": {\"e\": false}}', block, false",
    "'a = 100, do {a: 100, remove(a)}', 'a', 100, int, false",
    "'a = 100, do {remove(a)}', 'a', null, null, false",
    "'do {remove(a)}', 'a', null, null, false",
    "'a={x:[30, 2]}; b=a; b.x.1=5; remove(b.x.0)', '[a, b]', '[{\"x\": [30, 2]}, {\"x\": [5]}]', array, false",
  })
  public void test(String preparedExpression, String targetExpression,
                   String expectedValue, String expectedType,
                   boolean isError) {
    Config config = new Config();
    config.setMaxDepth(10);

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
  public void testOther() {
    HashMap<String, Object> variable = new HashMap<>();
    variable.put("a", 3);
    assertEquals(Viv.getInteger(variable, "return(a)"), 3);

    variable.replace("a", new Config());
    Viv.Result result = Viv.run(variable, "return(a)");
    assertFalse(result.errorMessage.isEmpty());

    variable.put("b", "y");
    assertNull(Viv.getString(variable, "return(b)"));  // Because "a" is invalid.

    variable.replace("a", "x");
    assertEquals(Viv.getString(variable, "return(a)"), "x");
    assertEquals(Viv.getString(variable, "return(b)"), "y");
  }
}
