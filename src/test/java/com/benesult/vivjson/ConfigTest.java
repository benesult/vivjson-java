/* Unit-test for Config
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

import org.junit.jupiter.api.Test;

/**
 * Config test class.
 * Unit-test for getter/setter in Config
 *
 * <p>Environment
 * <ul>
 *   <li> Java 9 or later
 *   <li> JUnit 5.11 or later
 * </ul>
 *
 * <p>Last modified: 2025-03-24
 *
 * @author Fumiaki Motegi (motegi@benesult.com)
 */
public class ConfigTest {
  @Test
  public void test() {
    assertEquals(Config.getEnableStderr(null), Config.ENABLE_STDERR_DEFAULT);
    assertEquals(Config.getEnableTagDetail(null), Config.ENABLE_TAG_DETAIL_DEFAULT);
    assertEquals(Config.getEnableOnlyJson(null), Config.ENABLE_ONLY_JSON);
    assertEquals(Config.getInfinity(null), Config.INFINITY_DEFAULT);
    assertEquals(Config.getNaN(null), Config.NAN_DEFAULT);
    assertEquals(Config.getMaxArraySize(null), Config.MAX_ARRAY_SIZE_DEFAULT);
    assertEquals(Config.getMaxDepth(null), Config.MAX_DEPTH_DEFAULT);
    assertEquals(Config.getMaxLoopTimes(null), Config.MAX_LOOP_TIMES_DEFAULT);

    Config config = new Config();

    assertEquals(config.getEnableStderr(), Config.ENABLE_STDERR_DEFAULT);
    assertEquals(Config.getEnableStderr(config), Config.ENABLE_STDERR_DEFAULT);
    assertEquals(config.getEnableTagDetail(), Config.ENABLE_TAG_DETAIL_DEFAULT);
    assertEquals(Config.getEnableTagDetail(config), Config.ENABLE_TAG_DETAIL_DEFAULT);
    assertEquals(config.getEnableOnlyJson(), Config.ENABLE_ONLY_JSON);
    assertEquals(Config.getEnableOnlyJson(config), Config.ENABLE_ONLY_JSON);
    assertEquals(config.getInfinity(), Config.INFINITY_DEFAULT);
    assertEquals(Config.getInfinity(config), Config.INFINITY_DEFAULT);
    assertEquals(config.getNaN(), Config.NAN_DEFAULT);
    assertEquals(Config.getNaN(config), Config.NAN_DEFAULT);
    assertEquals(config.getMaxArraySize(), Config.MAX_ARRAY_SIZE_DEFAULT);
    assertEquals(Config.getMaxArraySize(config), Config.MAX_ARRAY_SIZE_DEFAULT);
    assertEquals(config.getMaxDepth(), Config.MAX_DEPTH_DEFAULT);
    assertEquals(Config.getMaxDepth(config), Config.MAX_DEPTH_DEFAULT);
    assertEquals(config.getMaxLoopTimes(), Config.MAX_LOOP_TIMES_DEFAULT);
    assertEquals(Config.getMaxLoopTimes(config), Config.MAX_LOOP_TIMES_DEFAULT);

    config = new Config(true,  // enableStderr
                        true,  // enableTagDetail
                        true,  // enableOnlyJson
                        "Infinity",  // infinity
                        "NaN",  // nan
                        3,  // maxArraySize
                        3,  // maxDepth
                        1  // maxLoopTimes
    );

    assertEquals(config.getEnableStderr(), true);
    assertEquals(Config.getEnableStderr(config), true);
    assertEquals(Config.getEnableStderr(null), Config.ENABLE_STDERR_DEFAULT);
    assertEquals(config.getEnableTagDetail(), true);
    assertEquals(Config.getEnableTagDetail(config), true);
    assertEquals(Config.getEnableTagDetail(null), Config.ENABLE_TAG_DETAIL_DEFAULT);
    assertEquals(config.getEnableOnlyJson(), true);
    assertEquals(Config.getEnableOnlyJson(config), true);
    assertEquals(Config.getEnableOnlyJson(null), Config.ENABLE_ONLY_JSON);
    assertEquals(config.getInfinity(), "Infinity");
    assertEquals(Config.getInfinity(config), "Infinity");
    assertEquals(Config.getInfinity(null), Config.INFINITY_DEFAULT);
    assertEquals(config.getNaN(), "NaN");
    assertEquals(Config.getNaN(config), "NaN");
    assertEquals(Config.getNaN(null), Config.NAN_DEFAULT);
    assertEquals(config.getMaxArraySize(), 3);
    assertEquals(Config.getMaxArraySize(config), 3);
    assertEquals(Config.getMaxArraySize(null), Config.MAX_ARRAY_SIZE_DEFAULT);
    assertEquals(config.getMaxDepth(), 3);
    assertEquals(Config.getMaxDepth(config), 3);
    assertEquals(Config.getMaxDepth(null), Config.MAX_DEPTH_DEFAULT);
    assertEquals(config.getMaxLoopTimes(), 1);
    assertEquals(Config.getMaxLoopTimes(config), 1);
    assertEquals(Config.getMaxLoopTimes(null), Config.MAX_LOOP_TIMES_DEFAULT);

    config = new Config();

    assertEquals(config.getEnableStderr(), Config.ENABLE_STDERR_DEFAULT);
    assertEquals(Config.getEnableStderr(config), Config.ENABLE_STDERR_DEFAULT);
    assertEquals(Config.getEnableStderr(null), Config.ENABLE_STDERR_DEFAULT);
    config.enableStderr();
    assertEquals(config.getEnableStderr(), true);
    assertEquals(Config.getEnableStderr(config), true);
    assertEquals(Config.getEnableStderr(null), Config.ENABLE_STDERR_DEFAULT);
    config.enableStderr(false);
    assertEquals(config.getEnableStderr(), false);
    assertEquals(Config.getEnableStderr(config), false);
    assertEquals(Config.getEnableStderr(null), Config.ENABLE_STDERR_DEFAULT);
    config.enableStderr(true);
    assertEquals(config.getEnableStderr(), true);
    assertEquals(Config.getEnableStderr(config), true);
    assertEquals(Config.getEnableStderr(null), Config.ENABLE_STDERR_DEFAULT);

    assertEquals(config.getEnableTagDetail(), Config.ENABLE_TAG_DETAIL_DEFAULT);
    assertEquals(Config.getEnableTagDetail(config), Config.ENABLE_TAG_DETAIL_DEFAULT);
    assertEquals(Config.getEnableTagDetail(null), Config.ENABLE_TAG_DETAIL_DEFAULT);
    config.enableTagDetail();
    assertEquals(config.getEnableTagDetail(), true);
    assertEquals(Config.getEnableTagDetail(config), true);
    assertEquals(Config.getEnableTagDetail(null), Config.ENABLE_TAG_DETAIL_DEFAULT);
    config.enableTagDetail(false);
    assertEquals(config.getEnableTagDetail(), false);
    assertEquals(Config.getEnableTagDetail(config), false);
    assertEquals(Config.getEnableTagDetail(null), Config.ENABLE_TAG_DETAIL_DEFAULT);
    config.enableTagDetail(true);
    assertEquals(config.getEnableTagDetail(), true);
    assertEquals(Config.getEnableTagDetail(config), true);
    assertEquals(Config.getEnableTagDetail(null), Config.ENABLE_TAG_DETAIL_DEFAULT);

    assertEquals(config.getEnableOnlyJson(), Config.ENABLE_ONLY_JSON);
    assertEquals(Config.getEnableOnlyJson(config), Config.ENABLE_ONLY_JSON);
    assertEquals(Config.getEnableOnlyJson(null), Config.ENABLE_ONLY_JSON);
    config.enableOnlyJson();
    assertEquals(config.getEnableOnlyJson(), true);
    assertEquals(Config.getEnableOnlyJson(config), true);
    assertEquals(Config.getEnableOnlyJson(null), Config.ENABLE_ONLY_JSON);
    config.enableOnlyJson(false);
    assertEquals(config.getEnableOnlyJson(), false);
    assertEquals(Config.getEnableOnlyJson(config), false);
    assertEquals(Config.getEnableOnlyJson(null), Config.ENABLE_ONLY_JSON);
    config.enableOnlyJson(true);
    assertEquals(config.getEnableOnlyJson(), true);
    assertEquals(Config.getEnableOnlyJson(config), true);
    assertEquals(Config.getEnableOnlyJson(null), Config.ENABLE_ONLY_JSON);

    assertEquals(config.getInfinity(), Config.INFINITY_DEFAULT);
    assertEquals(Config.getInfinity(config), Config.INFINITY_DEFAULT);
    assertEquals(Config.getInfinity(null), Config.INFINITY_DEFAULT);
    config.setInfinity("abc");
    assertEquals(config.getInfinity(), "abc");
    assertEquals(Config.getInfinity(config), "abc");
    assertEquals(Config.getInfinity(null), Config.INFINITY_DEFAULT);
    config.setInfinity(null);
    assertNull(config.getInfinity());
    assertNull(Config.getInfinity(config));
    assertEquals(Config.getInfinity(null), Config.INFINITY_DEFAULT);

    assertEquals(config.getNaN(), Config.NAN_DEFAULT);
    assertEquals(Config.getNaN(config), Config.NAN_DEFAULT);
    assertEquals(Config.getNaN(null), Config.NAN_DEFAULT);
    config.setNaN("Xyz");
    assertEquals(config.getNaN(), "Xyz");
    assertEquals(Config.getNaN(config), "Xyz");
    assertEquals(Config.getNaN(null), Config.NAN_DEFAULT);
    config.setNaN(null);
    assertNull(config.getNaN());
    assertNull(Config.getNaN(config));
    assertEquals(Config.getNaN(null), Config.NAN_DEFAULT);

    assertEquals(config.getMaxArraySize(), Config.MAX_ARRAY_SIZE_DEFAULT);
    assertEquals(Config.getMaxArraySize(config), Config.MAX_ARRAY_SIZE_DEFAULT);
    assertEquals(Config.getMaxArraySize(null), Config.MAX_ARRAY_SIZE_DEFAULT);
    config.setMaxArraySize(20);
    assertEquals(config.getMaxArraySize(), 20);
    assertEquals(Config.getMaxArraySize(config), 20);
    assertEquals(Config.getMaxArraySize(null), Config.MAX_ARRAY_SIZE_DEFAULT);

    assertEquals(config.getMaxDepth(), Config.MAX_DEPTH_DEFAULT);
    assertEquals(Config.getMaxDepth(config), Config.MAX_DEPTH_DEFAULT);
    assertEquals(Config.getMaxDepth(null), Config.MAX_DEPTH_DEFAULT);
    config.setMaxDepth(8);
    assertEquals(config.getMaxDepth(), 8);
    assertEquals(Config.getMaxDepth(config), 8);
    assertEquals(Config.getMaxDepth(null), Config.MAX_DEPTH_DEFAULT);

    assertEquals(config.getMaxLoopTimes(), Config.MAX_LOOP_TIMES_DEFAULT);
    assertEquals(Config.getMaxLoopTimes(config), Config.MAX_LOOP_TIMES_DEFAULT);
    assertEquals(Config.getMaxLoopTimes(null), Config.MAX_LOOP_TIMES_DEFAULT);
    config.setMaxLoopTimes(15);
    assertEquals(config.getMaxLoopTimes(), 15);
    assertEquals(Config.getMaxLoopTimes(config), 15);
    assertEquals(Config.getMaxLoopTimes(null), Config.MAX_LOOP_TIMES_DEFAULT);
  }

  @Test
  public void testErrorTag() {
    String[] texts = new String[] {"a = 3 ! b = 5", "a = ", "a = 0, b = 2 / a"};
    for (String text : texts) {
      Viv.Result result = Viv.run(text);
      assertNotNull(result);
      assertFalse(result.errorMessage.isEmpty());
      assertTrue(result.errorMessage.indexOf("[" + VivException.TAG + "]") == 0);
    }

    Config config = new Config();
    config.enableTagDetail();
    String[] tagDetails = new String[] {"Lexer", "Parser", "Evaluator"};
    for (int i = 0; i < 3; i++) {
      Viv.Result result = Viv.run(texts[i], config);
      assertNotNull(result);
      assertFalse(result.errorMessage.isEmpty());
      assertTrue(result.errorMessage.indexOf(
              "[" + VivException.TAG + ":" + tagDetails[i] + "]") == 0);
    }
  }
}
