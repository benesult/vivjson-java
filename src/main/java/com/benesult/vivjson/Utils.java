/* Utilities of VivJson.
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
import org.eclipse.jdt.annotation.Nullable;

/**
 * Utilities of VivJson.
 *
 * <p>Environment: Java 9 or later
 *
 * <p>Last modified: 2025-03-20
 *
 * @author Fumiaki Motegi (motegi@benesult.com)
 */
class Utils {
  /**
   * Judges whether the given letter is contained or not.
   *
   * @param letter a letter
   * @param compares an array of compared letters.
   * @return true if it is contained, false otherwise
   */
  static boolean isContained(char letter, char[] compares) {
    for (char compare : compares) {
      if (letter == compare) {
        return true;
      }
    }
    return false;
  }

  /**
   * Concatenates the given strings.
   *
   * @param strings some strings that are concatenated.
   * @return a concatenated string
   */
  static String concatenate(@Nullable String... strings) {
    StringBuilder sb = new StringBuilder();
    for (String string : strings) {
      if (string != null && !string.isEmpty()) {
        sb.append(string);
      }
    }
    return sb.toString();
  }

  /**
   * Concatenates the given list with head/tail.
   *
   * @param head the prefix of list. For example, "(", "{", "test(".
   * @param statements statements
   * @param tail the suffix of list. For example, ")", "}".
   * @return a concatenated string
   */
  static String concatenate(String head, ArrayList<Statement> statements,
                            String tail) {
    StringBuilder sb = new StringBuilder(head);
    int count = 0;
    for (Statement statement : statements) {
      if (count > 0) {
        sb.append(", ");
      }
      sb.append(statement.toString());
      count++;
    }
    sb.append(tail);
    return sb.toString();
  }
}
