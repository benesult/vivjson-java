/* Unit-test for Standard library
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
 * Standard library test class.
 * Unit-test for Standard library.
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
public class StandardTest {
  @ParameterizedTest
  @CsvSource({
    "a = 10; if (a > 5) { a += 100 }, a, 110, int, false",
    "a = 10; if (a > 5) { a += 100 } else { a -= 100 }, a, 110, int, false",
    "a = 1; if (a > 5) { a += 100 } else { a -= 100 }, a, -99, int, false",
    "a = 1; if (a > 5) { a += 100 } elseif (a < 0) { a -= 100 }, a, 1, int, false",
    "x = if (true) { a = 100 } else { a = -100 }, x, {\"a\": 100}, block, false",
    "x = if (true) { := 100 } else { := -100 }, x, 100, int, false",
    "'a = -1, x = if (a < 0) {:= \"-\"} elseif (a == 0) {:=\"zero\"} else {:=\"+\"}', x, -, string, false",
    "'a = 0, x = if (a < 0) {:= \"-\"} elseif (a == 0) {:=\"0\" :=\"zero\" x=0} else {:=\"+\"}', x, zero, string, false",
    "'a = 1.5, x = if (a < 0) {:= \"-\"} elseif (a == 0) {:=\"zero\"} else {:=\"+\"}', x, +, string, false",
    "'a = -0.01, b = -1, result = \"\""
      + "if (a < 0) { if (b < 0) { result = \"< <\" } else { result = \"< >=\" } } "
      + "else { if (b < 0) { result = \">= <\" } else { result = \">= >=\" } }', "
      + "result, '< <', string, false",
      "'a = -10, b = 100000, result = \"\" "
      + "if (a < 0) { if (b < 0) { result = \"< <\" } else { result = \"< >=\" } } "
      + "else { if (b < 0) { result = \">= <\" } else { result = \">= >=\" } }', "
      + "result, '< >=', string, false",
    "'a = 0, b = -1e-3, result = \"\" "
      + "if (a < 0) { if (b < 0) { result = \"< <\" } else { result = \"< >=\" } } "
      + "else { if (b < 0) { result = \">= <\" } else { result = \">= >=\" } }', "
      + "result, '>= <', string, false",
      "'a = -1, b = -1e-8, result = \"\" "
      + "if (a < 0) { if (b < 0) { result = \"< <\" } else { result = \"< >=\" } } "
      + "else { if (b < 0) { result = \">= <\" } else { result = \">= >=\" } }', "
      + "result, '< <', string, false",
    "'a = -10, b = 100000, result = \"\" "
      + "if (a < 0) { if (b < 0) { result = \"< <\" } /* else { result = \"< >=\" } */ } "
      + "else { if (b < 0) { result = \">= <\" } else { result = \">= >=\" } }', "
      + "result, '', string, false",
    "'a = -0.01, b = -1, result = \"\", "
      + "if (a < 0 and b < 0) { result = \"< <\" } "
      + "elseif (a < 0 and b >= 0) { result = \"< >=\" } "
      + "elseif (a >= 0 and b < 0) { result = \">= <\" } "
      + "else { result = \">= >=\" }', "
      + "result, '< <', string, false",

    "'a = [] do {a += 1, a += 2}', a, '[1, 2]', array, false",
    "'a = [] do {a += 1, break, a += 2}', a, '[1]', array, false",
    "'i=0, do {i += 1, continue}', i, null, null, true",
    "'i=0, do {i += 1, if (i <= 10) {continue}}', i, 11, int, false",
    "'x = do {i = 100, a = true, b = null}', \"\"+x.i+x.a+string(x.b), 100truenull, string, false",

    "'i = 0; while (i < 10) { a = i;  i += 1}', 'i', 10, int, false",
    "'_i = 0; while (_i < 10) { a = _i;  _i += 1}', '_i', 10, int, false",
    "'i = 0; while (true) { i+=1 }', 'i', null, null, true",  // Infinite loop
    "'i = 0; while (true) { i+=1 if (i >= 3) {break} }', 'i', 3, int, false",
    "'i = 0, x = [], while (i < 10) { i += 1 if (i < 5) {continue} x += i }', 'x', '[5, 6, 7, 8, 9, 10]', array, false",
    "'i = 1 a = while (i < 10) { return(100) }', '[i, a]', 100, int, false",  // 100 is returned because {i = 1, a = while (i < 10) { return(100) }, return([i, a])}
    "'i = 1 a = while (i < 10) { i += 1 }', '[i, a]', '[10, {}]', array, false",
    "'i = 1 a = while (i < 10 { i += 1 })', '[i, a]', '[10, {}]', array, false",
    "'i = 1; a = while (i < 10; { i += 1 })', '[i, a]', '[10, {}]', array, false",
    "'i = 1 a = while (i < 10) { last = i, i += 1 }', '[i, a]', '[10, {\"last\": 9}]', array, false",
    "'i = 1 a = while (i < 10) { := i, i += 1 }', '[i, a]', '[10, 9]', array, false",

    "'i = 10; for (; i >= 0; i -= 2) { a = i }', 'i', -2, int, false",
    "'for (i = 0; i < 20; i += 1) { a = i; }', '[a, i]', '[null, null]', array, false",
    "'a = null, for (i = 0; i < 20; ; ; ) { i += 1, a = i }', '[i, a]', null, null, true",  // Arguments is too much.
    "'a = null, for (i = 0; i < 20; ; ) { i += 1, a = i }', '[i, a]', '[null, 20]', array, false",
    "'a = null, for (i = 0; i < 20; ) { i += 1, a = i }', '[i, a]', '[null, 20]', array, false",
    "'a = null, for (i = 0; i < 20) { i += 1, a = i }', '[i, a]', '[null, 20]', array, false",
    "'i = 0; for () { i+=1 }', 'i', null, null, true",  // Infinite loop
    "'i = 0; for (;;) { i+=1 }', 'i', null, null, true",  // Infinite loop
    "'i = 0; for (;;i+=1) { i+=0.5 }', 'i', null, null, true",  // Infinite loop
    "'i = 0; for (;;) { i = 1 break i = 2 }', 'i', 1, int, false",
    "'i = 0; for (;;) { i += 1 continue i = 2 break }', 'i', null, null, true",  // Infinite loop
    "'i = 1 a = for (; i < 10; i += 1) { return(100) }', '[i, a]', 100, int, false",  // 100 is returned because {i = 1, a = for (; i < 10; i += 1) { return(100) }, return([i, a])}
    "'i = 1 a = for (; i < 10; i += 1; { return(100) })', '[i, a]', 100, int, false",  // 100 is returned because {i = 1, a = for (; i < 10; i += 1; { return(100) }), return([i, a])}
    "'a = for (i = 0; i < 10; i += 1) {}', '[i, a]', '[null, {\"i\": 10}]', array, false",
    "'a = for (i = 0; i < 10; i += 1) {:=false}', '[i, a]', '[null, false]', array, false",
    "'i = null, for (i = 0) {break}', 'i', 0, int, false",
    "'i = 0, for (i < 0) {break}', 'i', 0, int, false",

    "'x = [], for (a in [3, 5, 1]) {x += a * 2}', 'x', '[6, 10, 2]', array, false",
    "'x = \"\", for (a in [\"foo\", \"bar\", \"baz\"]) {x += a + \",\" }', 'x', 'foo,bar,baz,', string, false",
    "'x = [], list = [3, 5, 1], for (a in list) {x += a * 2}', '[x, list]', '[[6, 10, 2], [3, 5, 1]]', array, false",
    "'x = {}, list = {x: 3, y: 5, z: 1}, for (a in list) {x[a[0]] = a[1]*2}', string(x.x)+string(x.y)+string(x.z), 6102, string, false",
    "'list = [{x: 3, y: 5}], z = null, for (a in list) {z = a.x}', z, 3, int, false",
    "'list = [{x: 3, y: 5}], for (a in list) {a.x = 2}', list.0.x, 3, int, false",
    "'list = [{x: 3, y: 5}], z = null, for (a in list) {a.x = 2, z = a.x}', z, 2, int, false",
    "'list = {\"x\": {\"y\": 5}}, for (a in list) {a[1][\"y\"] = 1}', list, '{\"x\": {\"y\": 5}}', block, false",
    "'x = [], list = 1, for (a in list) {x += a * 2}', x, null, null, true",  // Array or Block is permitted.
    "'\"foo\": 10, \"bar\": 30, \"baz\": 20, max=-1, for (pair in .) {if (max < pair[1]) {max = pair[1]}}', max, 30, int, false",

    "'', '[1.0, int(1.0), int(2.8), int(\"10.3\"), int(\"1e3\")]', '[1.0, 1, 2, 10, 1000]', array, false",
    "x = \"3.5\", int(x), 3, int, false",
    "'', 'int()', null, null, true",  // The argument is necessary.
    "'', 'int(\"x\")', null, null, true",  // The argument must be number.
    "'', 'int(true)', null, null, true",  // The argument must be number.
    "'', 'int([1])', null, null, true",  // The argument must be number.
    // {"a": float('nan')}, 'int(a)', null, null, true,  // NaN can not be converted.

    "'', '[10, float(10)]', '[10, 10.0]', array, false",
    "x = '3.5', float(x), 3.5, float, false",
    "'', float(), null, null, true",  // The argument is necessary.
    "'', float('x'), null, null, true",  // The argument must be number.
    "'', float(true), null, null, true",  // The argument must be number.

    "'', 'string(3 + 2)', 5, string, false",
    "'x = 1.5', 'string(x)', 1.5, string, false",
    "'', 'string(true)', true, string, false",
    "'', 'string(false)', false, string, false",
    "'', 'string(null)', null, string, false",
    "'', 'string()', null, null, true",  // The argument is necessary.

    "'', len(''), 0, int, false",
    "'', len('abc'), 3, int, false",
    "a = len('xyz'), a, 3, int, false",
    "a = 'xyz', len(a), 3, int, false",
    "a = 'αβz', len(a), 3, int, false",  // multi-byte character "αβ"
    "'', len([]), 0, int, false",
    "'', 'len([\"abc\", \"12345\"])', 2, int, false",
    "'a = [\"abc\", \"12345\"]', len(a), 2, int, false",
    "'', len({}), 0, int, false",
    "'', 'len({\"a\": 1, \"b\": true, \"c\": null})', 3, int, false",
    "'a = {\"a\": 1, \"b\": true, \"c\": null}', len(a), 3, int, false",
    "'', len(), null, null, true",  // The argument is necessary.
    "'', len(2), null, null, true",  // The argument must be array, block, string
    "'', len(true), null, null, true",  // The argument must be array, block, string
    "'', len(null), null, null, true",  // The argument must be array, block, string

    "'a = [], insert(a, 0, 10)', 'a', '[10]', array, false",
    "'a = [1, 2, 3, 4, 5], insert(a, len(a), [null, true, false, \"text\"])', 'a', '[1, 2, 3, 4, 5, [null, true, false, \"text\"]]', array, false",
    "'a = [1, 2, 3, 4, 5], insert(a, len(a), {\"x\": null})', 'a', '[1, 2, 3, 4, 5, {\"x\": null}]', array, false",
    "'a = [1, 2, 3], insert(a, len(a), 10)', 'a', '[1, 2, 3, 10]', array, false",
    "'a = [1, 2, 3], insert(a, 3, 10)', 'a', '[1, 2, 3, 10]', array, false",
    "'a = [1, 2, 3], insert(a, 1, 10)', 'a', '[1, 10, 2, 3]', array, false",
    "'a = [1, 2, 3], insert(a, 0, 10)', 'a', '[10, 1, 2, 3]', array, false",
    "'a = [1, 2, 3], insert(a, -0, 10)', 'a', '[10, 1, 2, 3]', array, false",
    "'a = [1, 2, 3], insert(a, -1, 10)', 'a', '[1, 2, 10, 3]', array, false",
    "'a = [1, 2, 3], insert(a, -3, 10)', 'a', '[10, 1, 2, 3]', array, false",
    "'a = [1, 2, 3], insert(a, 4, 10)', 'a', null, null, true",  // Out of range
    "'a = [1, 2, 3], insert(a, -4, 10)', 'a', null, null, true",  // Out of range
    "'a = [1, 2, 3], insert(a, true, 10)', 'a', null, null, true",  // Index must be number.
    "'a = [1, 2, 3], insert(a, 3)', 'a', null, null, true",  // Lack the inserted value.
    "'a = [1, 2, 3], insert(a)', 'a', null, null, true",  // Lack index and the inserted value.
    "'a = 1, insert(a, 0, 10)', 'a', null, null, true",  // Array is needed.
    "'a = [1, 2, 3, 4, 5, 6], insert(a, len(a), 10)', 'a', null, null, true",  // Limit of max array size

    "'', strip('abc'), abc, string, false",
    "a = 'abc', strip(a), abc, string, false",
    // "a = '\n 　\r abc \n\t', strip(a), 'abc', string, false",  // multi-byte character "　"
    // "a = '\n 　\r αβ \n\t', strip(a), 'αβ', string, false",  // multi-byte character "　" and "αβ"
    "a = ' 　 abc \t', strip(a), 'abc', string, false",  // multi-byte character "　"
    "a = ' 　 αβ \t', strip(a), 'αβ', string, false",  // multi-byte character "　" and "αβ"
    // "'', strip('\n 　\r abc \n\t'), 'abc', string, false",
    "'', strip(), null, null, true",  // The argument is necessary.

    "'', type(1), int, string, false",
    "'a = 1.5', type(a), float, string, false",
    "'', type(1.5), float, string, false",
    "'', type(1e3), float, string, false",
    "'', type('1'), string, string, false",
    "'', type(true), boolean, string, false",
    "'', type(false), boolean, string, false",
    "'', type(null), null, string, false",
    "'', 'type([1,2,3])', array, string, false",
    "'x = [1, [2, 3]]', type(x[1]), array, string, false",
    "'', type([]), array, string, false",
    "'', 'type({\"a\":1,\"b\":2})', block, string, false",
    "'x = [1, {\"a\":1,\"b\":2}]', type(x[1]), block, string, false",
    "'', type({}), block, string, false",
    "'function enclosure(a) {x = a, function closure(y) {return(x + y)}, return(closure)}, z1 = enclosure(100)', type(z1), function, string, false",
    "'function x2(a) {return(a*2)}', type(x2), function, string, false",
    "'function x2(a) {return(a*2)}, foo = x2', type(foo), function, string, false",
    "'function x2(a) {return(a*2)}, foo = [x2]', type(foo.0), function, string, false",
    "'', type(len), function, string, false",
    "'', type(print), function, string, false",
    "'', type(for), null, null, true",  // Parse error
    "'', type(break), null, null, true",  // Parse error
    "'', type(), null, null, true",  // The argument is necessary.
    "'', 'type(1, 2)', null, null, true",  // Only one argument is necessary.
  })
  public void test(String preparedExpression, String targetExpression,
                   String expectedValue, String expectedType,
                   boolean isError) {
    Config config = new Config();
    config.setMaxDepth(10);
    config.setMaxArraySize(6);

    String text = "return(" + targetExpression + ")";
    Result result = Viv.run(preparedExpression, text, config);
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty() != isError);
    String value = Viv.makeString(result.value);
    assertEquals(value, expectedValue);
    @Nullable String type = UtilsForTest.getType(result.value);
    assertEquals(type, expectedType);
  }

  @Test
  public void testIntForNanAndInfinity() {
      {
        HashMap<String, Float> map = new HashMap<>();
        map.put("a", 3.5F);
        Result result = Viv.run(map, "return(int(a))");
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        assertEquals(result.value, 3L);

        map.replace("a", Float.NaN);
        result = Viv.run(map, "return(int(a))");
        assertNotNull(result);
        assertFalse(result.errorMessage.isEmpty());
        assertNull(result.value);

        map.replace("a", Float.POSITIVE_INFINITY);
        result = Viv.run(map, "return(int(a))");
        assertNotNull(result);
        assertFalse(result.errorMessage.isEmpty());
        assertNull(result.value);

        map.replace("a", Float.NEGATIVE_INFINITY);
        result = Viv.run(map, "return(int(a))");
        assertNotNull(result);
        assertFalse(result.errorMessage.isEmpty());
        assertNull(result.value);
      }

      {
        HashMap<String, Double> map = new HashMap<>();
        map.put("a", 3.5);
        Result result = Viv.run(map, "return(int(a))");
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        assertEquals(result.value, 3L);

        map.replace("a", Double.NaN);
        result = Viv.run(map, "return(int(a))");
        assertNotNull(result);
        assertFalse(result.errorMessage.isEmpty());
        assertNull(result.value);

        map.replace("a", Double.POSITIVE_INFINITY);
        result = Viv.run(map, "return(int(a))");
        assertNotNull(result);
        assertFalse(result.errorMessage.isEmpty());
        assertNull(result.value);

        map.replace("a", Double.NEGATIVE_INFINITY);
        result = Viv.run(map, "return(int(a))");
        assertNotNull(result);
        assertFalse(result.errorMessage.isEmpty());
        assertNull(result.value);
      }
  }
}
