/* Unit-test for Parser
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
import java.util.ArrayList;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Parser test class.
 * Unit-test for Parser with Lexer.
 *
 * <p>Environment
 * <ul>
 *   <li> Java 9 or later
 *   <li> JUnit 5.11 or later
 * </ul>
 *
 * <p>Last modified: 2025-04-04
 *
 * @author Fumiaki Motegi (motegi@benesult.com)
 */
public class ParserTest {
  @ParameterizedTest
  @CsvSource({
    "a = 1.5, a, 1.5, float, false",
    "'a= 1.5e1, b=3', a, 15.0, float, false",
    "a = 1., a, null, null, true",  // Digit is needed after "."
    "a =, a, null, null, true",  // Missing value
    "'a = {}, a.1 = {}, a.1.2 = true', a, {\"1\": {\"2\": true}}, block, false",
    "'a = {}, a.1/2 = {}', a, null, null, true",  // Invalid member
    "'[a, b] = [1, 3]', a, null, null, true",  // Invalid assignment
    "a + 1 = 1, a, null, null, true",  // Invalid assignment
    "a: \"\", a, '', string, false",
    "a = \"text', a, null, null, true",  // Different quotation mark is invalid
    "a = \"text\\\" b = 3, a, null, null, true",  // Missing end of "
    "a = \"text\\\", a, null, null, true",  // Missing end of "
    "a = \"text\\\\text\", a, text\\\\text, string, false",
    "a = \"text\ttext\", a, text\\ttext, string, false",
    "a = '\\u3042\\u3044\\u3046\\u3048\\u304a\\u304B', a, あいうえおか, string, false",
    "a = '\\u4a\\u3044', a, null, null, true",  // Invalid unicode
    "a = '\\u4a', a, null, null, true",  // Invalid unicode
    "a = '\\u', a, null, null, true",  // Invalid unicode
    "a = \"x\\\"y\", a, x\\\"y, string, false",
    "a = 'x\\'y', a, x'y, string, false",
    "a = 'x\\\\y', a, x\\\\y, string, false",
    "a = 'x\\/y', a, x/y, string, false",
    "a = 'x\\by', a, x\\by, string, false",
    "a = 'x\\fy', a, x\\fy, string, false",
    "a = 'x\\ny', a, x\\ny, string, false",
    "a = 'x\\ry', a, x\\ry, string, false",
    "a = 'x\\ty', a, x\\ty, string, false",
    "a : true, a, true, boolean, false",
    "a := 1.5', a, null, null, true",  // := is invalid after Identifier.
    "x = if (true) {:=10}, x, 10, int, false",
    "x = if (true) {a:=10}, x, null, null, true",  // := is invalid after Identifier.
    "x = if (true) {:=}, x, null, null, true",  // Missing value
    "a = [1 5 {b: {c: 10} d: -20}] e = -100, a[-1].b.c + e, -90, int, false",
    "a=[1 5 {b:{c:10} d:-20}] e=-100, a[-1].b.c+e, -90, int, false",
    "a=[1 5 {b:{c:10} d:-20}] e=-100, a.-1.b.c+e, -90, int, false",
    "a=[1 5 {b:{c:10} d:-20}] e=-100, a.-2, 5, int, false",
    "a=[1 5 {b:{c:10} d:-20}] e=-100, a.-2., null, null, true",  // It is invalid that a dot is the tail.
    "a=[1 5 {b:{c:10} d:-20}] e=-100, 'a . \n -2', 5, int, false",
    "a=[1 5 {b:{c:10} d:-20}] e=-100, a.2.b.c+e, -90, int, false",
    "a=[1 5 {b:{c:10} d:-20}] e=-100, a.2. b.c+e, -90, int, false",
    "a=[1 5 {b:{c:10} d:-20}] e=-100, a . 2 . b.c+e, -90, int, false",
    "'a\n=\n[1\n\n{b:{c:10} d:-20}]\ne\n\n=\n-100', 'a[-1]\n. \nb.c+e', -90, int, false",
    "'', -3+1e-4*1000, -2.9, float, false",
    "a = [5], a[0, null, null, true",  // It is invalid that the right bracket is lacked.
    "{a = [5 }, 3, null, null, true",  // It is invalid that the right bracket is lacked.
    "'', 3 *+ 4, 12, int, false",
    "'', 3 +* 4, null, null, true",  // +* is invalid
    "'', (3 + 4 {1 + 2}, null, null, true",  // It is invalid that the right bracket is lacked.
    "'', () + 2, null, null, true",  // It is invalid that there is no expression in group.
    "a := 2, a, null, null, true",
    "'', 3 > 2 > -1, null, null, true",  // It should be fixed as 3 > 2 and 2 > -1.
    "a = not false, a, true, boolean, false",
    "a = not(false), a, true, boolean, false",
    "a = true and not false, a, true, boolean, false",
    "a = !false, a, null, null, true",  // ! is invalid
    "function x2(a) {return(a * 2)}, x2(4), 8, int, false",
    "function x2(a){return(a*2)}, x2(4+2), 12, int, false",
    "function x2() {}, x2(1), {}, block, false",
    "function x2() a = 3, a, null, null, true",  // Function needs block (statements).
    "function test(a + b) {}, test(), null, null, true",  // Invalid parameter
    "function test(break a) {}, test(), null, null, true",  // Invalid modifier
    "function run(function worker) {worker()}, a, null, null, false",
    "function run(function) {return}, a, null, null, true",  // Identifier is needed after argument's modifier.
    "function (a) {return}, a, null, null, true",  // Identifier is needed after function's modifier.
    "'a = 0\n\nif\n\n(\n\ntrue\n\n)\n\n{\n\na = 3\n\n}', a, 3, int, false",
    "a = 0 if (true) {}, a, 0, int, false",
    "'a = -10, b = if (a < 0) {:= \"-\"} elseif (a == 0) {:= \"0\"} else {:= \"+\"}', 'b', '-', string, false",
    "'a = 1, b = if \n (a == 0) \n {:= \"zero\"} \n elseif \n (a == 1) \n {:= \"one\"} \n elseif \n (a == 2) \n {:= \"two\"} \n else \n {:= \"other\"}', 'b', 'one', string, false",
    "if () { a = 3 }, a, null, null, true",  // Condition is necessary.
    "'if (true, a = 3', a, null, null, true",  // Block (operations) is necessary.
    "'if (true, a = 3)', a, null, null, true",  // Block (operations) is necessary.
    "while () {i=0}, i, null, null, true",  // Condition is necessary.
    "while (true), i, null, null, true",  // Block (operations) is necessary.
    "while {i = 0}, i, null, null, true",  // Condition (parenthesis) is necessary.
    "for {i = 0}, i, null, null, true",  // Condition (parenthesis) is necessary.
    "'a = 0, for (i = 0; i < 20; i += 1; { a = 2 })', a, 2, int, false",
    "'for (i = 0; i < 20; i += 1; { a = 2 }', i, null, null, true",  // Both parentheses are necessary.
    "for (i = 0; i < 20; i += 1), i, null, null, true",  // Block (operations) is necessary.
    "'for (i = 0; i < 20; i += 1, a = 3)', a, null, null, true",  // Block (operations) is necessary.
    "for (), i, null, null, true",  // Block (operations) is necessary.
    "'for (3 in [2, 3]) {break}', i, null, null, true",  // Iterator needs Identifier for left-hand side.
    "'sum = 0, for (i = 5, i < 7, i += 1) {sum += i}', sum, 11, int, false",
    "'sum = 0, for (i = 5, i < 7, i += 1, i += 1) {sum += i}', sum, null, null, true",  // Number of argument is invalid.
    "'remove, a = 3', a, null, null, true",  // remove function needs argument.
    "'remove(, a = 3', a, null, null, true",  // remove function needs argument.
    "remove(a, a, null, null, true",  // Missing ")"
    "'a = 3,a *= 5;a += 2\na = a - 27 a /= 4\tb = 3   a+=b', a, 0.5, float, false",
    "'a = 20, a = 30 # ', a, 30, int, false",
    "'a = 20 # , a = 30', a, 20, int, false",
    "'a = 20 // , a = 30', a, 20, int, false",
    "'a = /* 20, a = */ 30', a, 30, int, false",
    "'a = /* 20, a = 30', a, null, null, true",  // Missing "*/"
    "'a = 20 # abc\na = 30', a, 30, int, false",
    "'a /* = 20\na */ = 30', a, 30, int, false",
    "'a = 20 /* \na = 30 */', a, 20, int, false",
    "'a = [\n\n1\n\n2\n\n3\n\n]', a, '[1, 2, 3]', array, false",
    "'a = [\n1,\n2,\n3\n]', a, '[1, 2, 3]', array, false",
    "'a = [\n1,\n,\n3\n]', a, null, null, true",  // Missing 2nd value though delimiter is existed
    "'function test(\na\nb\nc\n)\n{return(a+b+c)}\nx=test(10, 100, 1000)', 'x', 1110, int, false",
    "'function test(\n\na\n#comment\nb\n\nc\n\n)\n{return(a+b+c)}\nx=test(10, 100, 1000)', 'x', 1110, int, false",
    "'function test(a, , c)\n{return(a+b+c)}\nx=test(10, 100, 1000)', 'x', null, null, true",  // Missing 2nd parameter though delimiter is existed
    "'function test(a, b, c)\n{return(a+b+c)}\nx=test(10, , 1000)' , 'x', 1010, int, false",  // Missing 2nd argument will be null.
  })
  public void test(String preparedExpression, String targetExpression,
                   String expectedValue, String expectedType,
                   boolean isError) {
    Config config = new Config();
    config.setMaxDepth(11);

    String text = "return(" + targetExpression + ")";
    Result result = Viv.run(preparedExpression, text, config);
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty() != isError);
    String value = Viv.makeString(result.value);
    assertEquals(value, expectedValue);
    @Nullable String type = UtilsForTest.getType(result.value);
    assertEquals(type, expectedType);
  }

  @ParameterizedTest
  @CsvSource({
    "true, '{\"a\": 3, \"b\": [true, null, 1.5e3]}', '{\"a\": 3, \"b\": [true, null, 1500.0]}', block, false",
    "false, '{\"a\": 3, \"b\": [true, null, 1.5e3]}', '{\"a\": 3, \"b\": [true, null, 1500.0]}', block, false",

    "true, '{\"a\": 3, \"b\": true}', '{\"a\": 3, \"b\": true}', block, false",
    "false, '{\"a\": 3, \"b\": true}', '{\"a\": 3, \"b\": true}', block, false",

    "true, '\"a\": -3, \"b\": true', '{\"a\": -3, \"b\": true}', block, false",
    "false, '\"a\": -3, \"b\": true', '{\"a\": -3, \"b\": true}', block, false",

    "true, '\"a\": 3, \"b\": true, return(a)', null, null, true",
    "false, '\"a\": 3, \"b\": true, return(a)', '3', int, false",

    "true, '{\"a\": 3 + 2, \"b\": true}', null, null, true",
    "false, '{\"a\": 3 + 2, \"b\": true}', '{\"a\": 5, \"b\": true}', block, false",

    "true, 10, '{}', block, false",
    "false, 10, '{}', block, false",
  })
  public void testJson(boolean isOnlyJson,
                       String expression,
                       String expectedValue, String expectedType,
                       boolean isError) {
    Config config = new Config();
    config.enableOnlyJson(isOnlyJson);
    Result result = Viv.run(expression, config);
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty() != isError);
    String value = Viv.makeString(result.value);
    if (expectedType.equals("block")) {
      assertTrue(UtilsForTest.isEqualMap(value, expectedValue));
    } else {
      assertEquals(value, expectedValue);
    }
    @Nullable String type = UtilsForTest.getType(result.value);
    assertEquals(type, expectedType);
  }

  @ParameterizedTest
  @CsvSource({
    "'\"a\": 3, \"b\": true', 'return(a)', 3, int, false",
    "'\"a\": 3, \"b\": true', '', '{\"a\": 3, \"b\": true}', block, false",
    "'\"a\": 3, \"b\": true', 'return(\"\")', '', string, false",
    "'\"a\": 3, \"b\": true', 'return(a + 2)', 5, int, false",
    "'\"a\": 3 + 2, \"b\": true', 'return(a)', null, null, true",  // "+" operator is not allowed in JSON.
    "'\"a\": 3, \"b\": true', 'a += 2, b = not b', '{\"a\": 5, \"b\": false}', block, false",

    "10, '', '{}', block, false",  // The number(, string, boolean, null, array) can't be returned without script.
    "10, 'return(_)', 10, int, false",

    "false, '', '{}', block, false",  // The number(, string, boolean, null, array) can't be returned without script.
    "false, 'return(_)', false, boolean, false",

    "\"test\", '', '{}', block, false",  // The number(, string, boolean, null, array) can't be returned without script.
    "\"test\", 'return(_)', 'test', string, false",

    "test, '', '{}', block, false",  // The number(, string, boolean, null, array) can't be returned without script.
    "test, 'return(_)', 'test', string, false",

    "null, '', '{}', block, false",  // The number(, string, boolean, null, array) can't be returned without script.
    "null, 'return(_)', null, null, false",

    "'[1, 2, 3]', '', '{}', block, false",  // The number(, string, boolean, null, array) can't be returned without script.
    "'[1, 2, 3]', 'return(_)', '[1, 2, 3]', array, false",
  })
  public void testJsonAndScript(String json, String script,
                                String expectedValue, String expectedType,
                                boolean isError) {
    Config config = new Config();
    config.enableOnlyJson();
    Viv.Parsed parsed = Viv.parse(json, config);
    assertTrue(parsed.errorMessage.isEmpty() != isError);
    if (parsed.errorMessage.isEmpty()) {
      assertNotNull(parsed.statements);
    } else {
      assertNull(parsed.statements);
    }
    Result result = Viv.run(parsed, script);
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty() != isError);
    String value = Viv.makeString(result.value);
    if (expectedType.equals("block")) {
      assertTrue(UtilsForTest.isEqualMap(value, expectedValue));
    } else {
      assertEquals(value, expectedValue);
    }
    @Nullable String type = UtilsForTest.getType(result.value);
    assertEquals(type, expectedType);
  }

  @Test
  public void testFile() {
    Viv.Parsed parsed = Viv.parse("src/test/invalid_as_json.json");
    assertFalse(parsed.errorMessage.isEmpty());
    assertNull(parsed.statements);
    parsed = Viv.parse("src/test/invalid_as_json.viv");
    assertTrue(parsed.errorMessage.isEmpty());
    assertNotNull(parsed.statements);

    parsed = Viv.parseFile("src/test/invalid_as_json.json", null);
    assertFalse(parsed.errorMessage.isEmpty());
    assertNull(parsed.statements);
    parsed = Viv.parseFile("src/test/invalid_as_json.viv", null);
    assertTrue(parsed.errorMessage.isEmpty());
    assertNotNull(parsed.statements);

    Config config = new Config();
    assertFalse(config.getEnableOnlyJson());
    parsed = Viv.parseFile("src/test/invalid_as_json.json", config);
    assertFalse(parsed.errorMessage.isEmpty());
    assertNull(parsed.statements);
    assertFalse(config.getEnableOnlyJson());

    Viv.KeyValue<@Nullable Object>[] keyValues =
        Viv.getKeyValueObjectOrNulls("src/test/array.viv");
    assertNotNull(keyValues);
    int count = 0;
    for (Viv.KeyValue<@Nullable Object> keyValue : keyValues) {
      assertTrue(keyValue.value instanceof ArrayList);
      @SuppressWarnings("unchecked")
      ArrayList<@Nullable Object> array = (ArrayList<@Nullable Object>) keyValue.value;
      assertEquals(array.size(), 3);
      int number = 1;
      for (@Nullable Object object : array) {
        assertTrue(object instanceof Long);
        assertEquals((Long) object, number);
        number++;
      }
      count++;
    }
    assertEquals(count, 8);
  }
}
