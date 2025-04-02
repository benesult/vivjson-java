/* Utilities for Unit-test
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

import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Unit-test Utilities class.
 * Utilities for Unit-test
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
public class UtilsForTest {
  public static boolean isEqualMap(String map1, String map2) {
    String[] keyValues1 = getKeyValuesFromMap(map1);
    String[] keyValues2 = getKeyValuesFromMap(map2);

    if (keyValues1 == null) {
      return (keyValues2 == null);
    } else if (keyValues2 == null) {
      return false;
    }

    if (keyValues1.length != keyValues2.length) {
      return false;
    }

    for (String keyValue1 : keyValues1) {
      boolean isFound = false;
      for (String keyValue2 : keyValues2) {
        if (keyValue1.equals(keyValue2)) {
          isFound = true;
          break;
        }
      }
      if (!isFound) {
        return false;
      }
    }
    return true;
  }

  private static String @Nullable [] getKeyValuesFromMap(String map) {
    if (map.length() < 2) {
      return null;
    }

    if (map.charAt(0) != '{' || map.charAt(map.length() - 1) != '}') {
      return null;
    }

    String text = map.substring(1, map.length() - 1);
    return text.split(", ");
  }

  public static @Nullable String getType(@Nullable Object value) {
    if (value == null) {
      return "null";
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
}
