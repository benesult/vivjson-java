/* Unit-test for sample codes
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

import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Test;

/**
 * Sample codes test class.
 * Unit-test for sample codes
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
public class SampleCodesTest {
  @Test
  public void test() {
    @Nullable HashMap<String, @Nullable Object> result =
        Viv.getHashMap("src/test/sample_codes.viv");
    assertNotNull(result);

    Object object = result.get("array_2d");
    assertTrue(object instanceof ArrayList);
    String text = Viv.makeString(object);
    assertEquals(text, "[[0, 1, 2], [true, \"text\", null], [3, 4, 5], [6, 7, 8]]");

    object = result.get("array_init");
    assertTrue(object instanceof ArrayList);
    text = Viv.makeString(object);
    assertEquals(text, "[null, null, null, null, null]");

    object = result.get("delegate_do");
    assertTrue(object instanceof String);
    assertEquals(object, "1xxx");

    object = result.get("operated_1");
    assertTrue(object instanceof Long);
    assertEquals(object, 3L);

    object = result.get("operated_2");
    assertTrue(object instanceof ArrayList);
    double[] doubles = Viv.getDoubles(object);
    assertNotNull(doubles);
    assertEquals(doubles.length, 4);
    int index = 0;
    for (double expected : new double[] {7, 3, 10, 2.5}) {
      assertEquals(doubles[index++], expected);
    }
    text = Viv.makeString(object);
    assertEquals(text, "[7, 3, 10, 2.5]");

    object = result.get("month");
    assertTrue(object instanceof String);
    assertEquals(object, "May");

    object = result.get("day_of_week");
    assertTrue(object instanceof String);
    assertEquals(object, "Sat");
  }
}
