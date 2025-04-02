/* Unit-test for order of operations in Evaluator
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.benesult.vivjson.Viv.Result;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Evaluator operation-order test class.
 * Unit-test for +, -, *, /, and, or, ...
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
public class EvaluatorOrderTest {
  @ParameterizedTest
  @CsvSource({
    "'', '1 + 2 * 5', 11, int, false",
    "'', '(1 + 2) * 5', 15, int, false",
    "'', '2 * 3 + 8', 14, int, false",
    "'', '3 / 5 + 10', 10.6, float, false",
    "'', '3 / (5 + 3 + 3 + 3 + 1)', 0.2, float, false",
    "'', '12 % 5 + 1', 3, int, false",
    "'', '12 % (5 + 1)', 0, int, false",
    "'', '3 / 5 * 3', 1.8, float, false",
    "'', '3 / (5 * 3)', 0.2, float, false",
    "'', '3 / (4 + 5 + 6)', 0.2, float, false",
    "'', '6 / ((4 + 5 + 6))', 0.4, float, false",
    "'', '(6 / ((4 + 5 + 6)))', 0.4, float, false",
    "'', '1 + (-6 / (4 + 5 + 6))', 0.6, float, false",
    "'', '-1 / 10', -0.1, float, false",
    "'', '3 +-1 / 10', 2.9, float, false",
    "'', '3 /-1', -3, int, false",
    // "'', '(\"a,b\" + \"c\") / \",\"', ["a", "bc"], false",
    // "'', '\"a,b\" + \"c\" / \",\"', ["a,b", "c"], false",
    // "'', '3 + [] + true', [3, true], false",
    // "'', '3 + ["x"] + true', [3, "x", true], false",
    "'', 'false or true and true or false', true, boolean, false",
    "'', 'false or not false', true, boolean, false",
    "'', 'not false or false', true, boolean, false",
    "'', 'not true and true', false, boolean, false",
    "'', 'not(true and true)', false, boolean, false",
    "'', 'not (3 != 3)', true, boolean, false",
    "'', 'not (true)', false, boolean, false",
    "'', '(true)', true, boolean, false",
    "'', '3 > 2 and 0 > -1', true, boolean, false",
    "'', '3 > (2 and 0) > -1', null, null, true",  // 3 > false > -1 is invalid.
    "'', '3 > 2 and 2 > -1', true, boolean, false",
    "'a = 5; a += 3 * 2', 'a', 11, int, false",
    "'', '\"a\" and \"b\" == \"c\"', false, boolean, false",  // --> true and false --> false
    "'', '(\"a\" and \"b\") == \"c\"', true, boolean, false",  // --> true == true --> true
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
  public void testArray() {
    // ("a,b" + "c") / "," --> ["a", "bc"]
    String[] strings = Viv.getStrings("return((\"a,b\" + \"c\") / \",\")");
    assertNotNull(strings);
    assertEquals(strings.length, 2);
    assertEquals(strings[0], "a");
    assertEquals(strings[1], "bc");

    // "a,b" + "c" / "," --> ["a,b", "c"]
    strings = Viv.getStrings("return(\"a,b\" + \"c\" / \",\")");
    assertNotNull(strings);
    assertEquals(strings.length, 2);
    assertEquals(strings[0], "a,b");
    assertEquals(strings[1], "c");

    // 3 + [] + true --> [3, true]
    Object[] objects = Viv.getObjects("return(3 + [] + true)");
    assertNotNull(objects);
    assertEquals(objects.length, 2);
    assertEquals(objects[0], 3L);
    assertEquals(objects[1], true);

    // 3 + ["x"] + true --> [3, "x", true]
    objects = Viv.getObjects("return(3 + ['x'] + true)");
    assertNotNull(objects);
    assertEquals(objects.length, 3);
    assertEquals(objects[0], 3L);
    assertEquals(objects[1], "x");
    assertEquals(objects[2], true);
  }
}
