/* Unit-test for Command-line
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Command-line test class.
 * Unit-test for Command-line
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
public class CommandLineTest {
  private static final String JAR_FILE_BASE_NAME = "vivjson";
  private static final String JAR_FILE_EXT = ".jar";
  private static String jarFileName = "";
  private static boolean isExistedJar = false;

  /**
   * Setup before all test.
   */
  @BeforeAll
  public static void prepare() {
    jarFileName = JAR_FILE_BASE_NAME + "-" + Config.INTERPRETER_VERSION
                  + JAR_FILE_EXT;

    isExistedJar = false;
    String[] results = run("ls", "-l", "target/" + jarFileName);
    isExistedJar = (results != null && results.length >= 1
                    && results[0].indexOf("No such file") == -1);

    if (isExistedJar) {
      results = run("cp", "-p", "target/" + jarFileName, ".");
      isExistedJar = (results != null);
    }
  }

  /**
   * Tear down after all test.
   */
  @AfterAll
  public static void cleanup() {
    if (isExistedJar) {
      run("rm", jarFileName);
    }
  }

  @Test
  public void testBasic() {
    if (!isExistedJar) {
      return;
    }

    String[] results = run("java", "-jar", jarFileName, "return(10)");
    assertNotNull(results);
    assertEquals(results.length, 1);
    assertEquals(results[0], "10");

    results = run("java", "-jar", jarFileName, "3", "2", "return(_[0] + _[1])");
    assertNotNull(results);
    assertEquals(results.length, 1);
    assertEquals(results[0], "5");

    results = run("java", "-jar", jarFileName, "5", "return(_ * 2)");
    assertNotNull(results);
    assertEquals(results.length, 1);
    assertEquals(results[0], "10");

    results = run("java", "-jar", jarFileName, "for(i in [1,2,3]){print(i*5)};return('')");
    assertNotNull(results);
    assertEquals(results.length, 4);
    assertEquals(results[0], "5");
    assertEquals(results[1], "10");
    assertEquals(results[2], "15");
    assertEquals(results[3], "");  // return('')

    results = run("java", "-jar", jarFileName, "src/test/call_6.viv");
    assertNotNull(results);
    assertEquals(results.length, 1);
    assertEquals(results[0], "6");

    results = run("java", "-jar", jarFileName, "src/test/a5b7c9.json", "src/test/axb-c.viv");
    assertNotNull(results);
    assertEquals(results.length, 1);
    assertEquals(results[0], "26");

    results = run("java", "-jar", jarFileName, "src/test/a5b7c9.json", "return(a+b+c)");
    assertNotNull(results);
    assertEquals(results.length, 1);
    assertEquals(results[0], "21");

    results = run("java", "-jar", jarFileName, "src/test/dog2cat3.json",
                  "sum = 0; for (a in _) {sum += a.number}; return(sum)");
    assertNotNull(results);
    assertEquals(results.length, 1);
    assertEquals(results[0], "5");
  }

  @Test
  public void testBasicViaCommandLineClass() {
    String[] inputs;
    @Nullable String[] outputs = new String[2];
      {
        inputs = new String[] {"return(10)"};
        CommandLine.run(inputs, outputs);
        assertNotNull(outputs[0]);
        assertNull(outputs[1]);
        assertEquals(outputs[0], "10");
      }

      {
        inputs = new String[] {"3", "2", "return(_[0] + _[1])"};
        CommandLine.run(inputs, outputs);
        assertNotNull(outputs[0]);
        assertNull(outputs[1]);
        assertEquals(outputs[0], "5");
      }

      {
        inputs = new String[] {"5", "return(_ * 2)"};
        CommandLine.run(inputs, outputs);
        assertNotNull(outputs[0]);
        assertNull(outputs[1]);
        assertEquals(outputs[0], "10");
      }

      {
        inputs = new String[] {"src/test/call_6.viv"};
        CommandLine.run(inputs, outputs);
        assertNotNull(outputs[0]);
        assertNull(outputs[1]);
        assertEquals(outputs[0], "6");
      }

      {
        inputs = new String[] {"src/test/a5b7c9.json", "src/test/axb-c.viv"};
        CommandLine.run(inputs, outputs);
        assertNotNull(outputs[0]);
        assertNull(outputs[1]);
        assertEquals(outputs[0], "26");
      }

      {
        inputs = new String[] {"src/test/a5b7c9.json", "return(a+b+c)"};
        CommandLine.run(inputs, outputs);
        assertNotNull(outputs[0]);
        assertNull(outputs[1]);
        assertEquals(outputs[0], "21");
      }

      {
        inputs = new String[] {"src/test/dog2cat3.json",
                               "sum = 0; for (a in _) {sum += a.number}; return(sum)"};
        CommandLine.run(inputs, outputs);
        assertNotNull(outputs[0]);
        assertNull(outputs[1]);
        assertEquals(outputs[0], "5");
      }
  }

  @Test
  public void testStdin() {
    if (!isExistedJar) {
      return;
    }

    String[] results;

    for (String option : new String[] {"-i", "--stdin"}) {
      for (String stdin : new String[] {"a=3", "{\"a\": 3}"}) {
        String command = "echo '" + stdin
                         + "' | java -jar " + jarFileName + " " + option + " 'return(a*2)'";
        results = run("/bin/sh", "-c", command);
        assertNotNull(results);
        assertEquals(results.length, 1);
        assertEquals(results[0], "6");
      }
    }

    results = run("/bin/sh", "-c", "echo '3' | java -jar " + jarFileName + " -i=a 'return(a+2)'");
    assertNotNull(results);
    assertEquals(results.length, 1);
    assertEquals(results[0], "5");

    results = run("/bin/sh", "-c", "echo '3' | java -jar " + jarFileName + " -i='' 'return(a+2)'");
    assertNotNull(results);
    assertEquals(results.length, 1);
    assertEquals(results[0], "2");

    results = run("/bin/sh", "-c", "echo '3' | java -jar " + jarFileName + " -i= 'return(a+2)'");
    assertNotNull(results);
    assertEquals(results.length, 1);
    assertEquals(results[0], "2");

    for (String option : new String[] {"-i", "--stdin"}) {
      for (String quotation : new String[] {"\"", "'", ""}) {
        String command = "cat src/test/dog2cat3.json | java -jar " + jarFileName + " "
                         + option + "=" + quotation + "values" + quotation + " "
                         + "'result = {}, "
                         + "for(value in values){"
                         +   "result[value.name] = value.number"
                         + "}, "
                         + "return(result)'";
        results = run("/bin/sh", "-c", command);
        assertNotNull(results);
        assertEquals(results.length, 1);
        assertTrue(UtilsForTest.isEqualMap(results[0], "{\"dog\": 2, \"cat\": 3}"));
      }
    }

    results = run("/bin/sh", "-c", "cat src/test/call_6.viv | java -jar " + jarFileName + " -i");
    assertNotNull(results);
    assertEquals(results.length, 1);
    assertEquals(results[0], "6");

    results = run("/bin/sh", "-c", "java -jar " + jarFileName + " -i src/test/call_6.viv");
    assertNotNull(results);
    assertEquals(results.length, 1);
    assertTrue(results[0].indexOf("Cannot read from stdin.") >= 0);

    results = run("/bin/sh", "-c", 
          "cat src/test/array_escaped_str.json | java -jar " + jarFileName + " -i=x 'return(x)'");
    assertNotNull(results);
    assertEquals(results.length, 1);
    // Note that U+2028 (LINE SEPARATOR) and U+2029 (PARAGRAPH SEPARATOR) are included.
    assertEquals(results[0], "[\"あいうえおか\", \"x\\ny\", \"x\\by\", \"x y\", \"x y\"]");

    results = run("/bin/sh", "-c", 
                  "cat src/test/array_escaped_str.json"
                  + " | java -jar " + jarFileName + " -i=x 'print(x), return(\"\")'");
    assertNotNull(results);
    assertEquals(results.length, 2);
    // Note that U+2028 (LINE SEPARATOR) and U+2029 (PARAGRAPH SEPARATOR) are included.
    assertEquals(results[0], "[\"あいうえおか\", \"x\\ny\", \"x\\by\", \"x y\", \"x y\"]");
    assertEquals(results[1], "");

    results = run("/bin/sh", "-c", 
                  "cat src/test/array_escaped_str.json"
                  + " | java -jar " + jarFileName + " -i=x "
                  + "'print(x[0], x[1], x[2], x[3], x[4]), return(\"\")'");
    assertNotNull(results);
    assertEquals(results.length, 3);
    // Note that U+2028 (LINE SEPARATOR) and U+2029 (PARAGRAPH SEPARATOR) are included.
    assertEquals(results[0], "あいうえおか, x");
    assertEquals(results[1], "y, x\by, x y, x y");
    assertEquals(results[2], "");
  }

  @Test
  public void testJson() {
    if (!isExistedJar) {
      return;
    }

    String[] results;

      // Formal JSON object can be always accepted.
      {
        String data = "{\"a\": 5, \"b\": [2, 1]}";
        for (String option : new String[] {" ", "-j", "--json"}) {
          String command = "java -jar " + jarFileName + " " + option + " '" + data + "'";
          results = run("/bin/sh", "-c", command);
          assertNotNull(results);
          assertEquals(results.length, 1);
          assertEquals(results[0], data);
        }

        for (String option : new String[] {" ", "-j", "--json"}) {
          String command =
              "java -jar " + jarFileName + " " + option + " '" + data + "' 'return(a)'";
          results = run("/bin/sh", "-c", command);
          assertNotNull(results);
          assertEquals(results.length, 1);
          assertEquals(results[0], "5");
        }
      }
      // Number, boolean, String, null, and Array are also valid as JSON value.
      {
        for (String data : new String[] {"5", "false", "true", "text", "null", "[1, 2]"}) {
          for (String option : new String[] {" ", "-j", "--json"}) {
            String command =
                "java -jar " + jarFileName + " " + option + " '" + data + "' 'return(_)'";
            results = run("/bin/sh", "-c", command);
            assertNotNull(results);
            assertEquals(results.length, 1);
            assertEquals(results[0], data);
          }
        }
      }

      // Even if curly brackets are lacked, it is permitted.
      {
        String data = "\"a\": 5, \"b\": [2, 1]";
        for (String option : new String[] {" ", "-j", "--json"}) {
          String command = "java -jar " + jarFileName + " " + option + " '" + data + "'";
          results = run("/bin/sh", "-c", command);
          assertNotNull(results);
          assertEquals(results.length, 1);
          assertEquals(results[0], "{" + data + "}");
        }
      }

      // This is not formal JSON object.
      // Because "+" operator is used.
      // However, this can be run as Script's code.
      {
        String data = "{\"a\": 3 + 2, \"b\": [2, 1]}";
        for (String option : new String[] {" "}) {
          String command =
              "java -jar " + jarFileName + " " + option + " '" + data + "' 'return(a)'";
          results = run("/bin/sh", "-c", command);
          assertNotNull(results);
          assertEquals(results.length, 1);
          assertEquals(results[0], "5");
        }
      }
      // Error occurs if this is run as JSON value.
      {
        String data = "{\"a\": 3 + 2, \"b\": [2, 1]}";
        for (String option : new String[] {"-j", "--json"}) {
          String command =
              "java -jar " + jarFileName + " " + option + " '" + data + "' 'return(a)'";
          results = run("/bin/sh", "-c", command);
          assertNotNull(results);
          assertEquals(results.length, 1);
          assertTrue(results[0].indexOf("Error") > 0);
        }
      }

      // Using stdin
      {
        String data = "{\"a\": 5, \"b\": [2, 1]}";
        for (String option : new String[] {" ", "-j", "--json"}) {
          String command = "echo '" + data
                           + "' | java -jar " + jarFileName + " " + option + " -i";
          results = run("/bin/sh", "-c", command);
          assertNotNull(results);
          assertEquals(results.length, 1);
          assertEquals(results[0], data);
        }

        for (String option : new String[] {" ", "-j", "--json"}) {
          String command = "echo '" + data
                           + "' | java -jar " + jarFileName + " " + option + " -i 'return(a)'";
          results = run("/bin/sh", "-c", command);
          assertNotNull(results);
          assertEquals(results.length, 1);
          assertEquals(results[0], "5");
        }

        data = "1.5";
        for (String option : new String[] {" ", "-j", "--json"}) {
          String command = "echo '" + data
                           + "' | java -jar " + jarFileName + " " + option + " -i 'return(_)'";
          results = run("/bin/sh", "-c", command);
          assertNotNull(results);
          assertEquals(results.length, 1);
          assertEquals(results[0], "1.5");
        }
      }

      // .json extension must be JSON value.
      {
        results = run("java", "-jar", jarFileName, "src/test/dog2cat3.json",
        "return(_[0].name)");
        assertNotNull(results);
        assertEquals(results.length, 1);
        assertEquals(results[0], "dog");

        results = run("java", "-jar", jarFileName, "src/test/invalid_as_json.json",
        "return(dog)");
        assertNotNull(results);
        assertEquals(results.length, 1);
        assertTrue(results[0].indexOf("Error") > 0);

        results = run("java", "-jar", jarFileName, "src/test/invalid_as_json.viv",
        "return(dog)");
        assertNotNull(results);
        assertEquals(results.length, 1);
        assertEquals(results[0], "3");
      }
  }

  @Test
  public void testJsonViaCommandLineClass() {
    String[] inputs;
    @Nullable String[] outputs = new String[2];

      // Formal JSON object can be always accepted.
      {
        String data = "{\"a\": 5, \"b\": [2, 1]}";
        for (String option : new String[] {"", "-j", "--json"}) {
          if (option.isEmpty()) {
            inputs = new String[] {data};
          } else {
            inputs = new String[] {option, data};
          }
          CommandLine.run(inputs, outputs);
          assertNotNull(outputs[0]);
          assertNull(outputs[1]);
          assertEquals(outputs[0], data);
        }

        for (String option : new String[] {"", "-j", "--json"}) {
          if (option.isEmpty()) {
            inputs = new String[] {data, "return(a)"};
          } else {
            inputs = new String[] {option, data, "return(a)"};
          }
          CommandLine.run(inputs, outputs);
          assertNotNull(outputs[0]);
          assertNull(outputs[1]);
          assertEquals(outputs[0], "5");
        }
      }
      // Number, boolean, String, null, and Array are also valid as JSON value.
      {
        for (String data : new String[] {"5", "false", "true", "text", "null", "[1, 2]"}) {
          for (String option : new String[] {"", "-j", "--json"}) {
            if (option.isEmpty()) {
              inputs = new String[] {data, "return(_)"};
            } else {
              inputs = new String[] {option, data, "return(_)"};
            }
            CommandLine.run(inputs, outputs);
            assertNotNull(outputs[0]);
            assertNull(outputs[1]);
            assertEquals(outputs[0], data);
          }
        }
      }

      // Even if curly brackets are lacked, it is permitted.
      {
        String data = "\"a\": 5, \"b\": [2, 1]";
        for (String option : new String[] {"", "-j", "--json"}) {
          if (option.isEmpty()) {
            inputs = new String[] {data};
          } else {
            inputs = new String[] {option, data};
          }
          CommandLine.run(inputs, outputs);
          assertNotNull(outputs[0]);
          assertNull(outputs[1]);
          assertEquals(outputs[0], "{" + data + "}");
        }
      }

      // This is not formal JSON object.
      // Because "+" operator is used.
      // However, this can be run as Script's code.
      {
        String data = "{\"a\": 3 + 2, \"b\": [2, 1]}";
        inputs = new String[] {data, "return(a)"};
        CommandLine.run(inputs, outputs);
        assertNotNull(outputs[0]);
        assertNull(outputs[1]);
        assertEquals(outputs[0], "5");
      }
      // Error occurs if this is run as JSON value.
      {
        String data = "{\"a\": 3 + 2, \"b\": [2, 1]}";
        for (String option : new String[] {"-j", "--json"}) {
          inputs = new String[] {option, data, "return(a)"};
          CommandLine.run(inputs, outputs);
          assertNull(outputs[0]);
          assertNotNull(outputs[1]);
          assertTrue(outputs[1].indexOf("Error") > 0);
        }
      }

      // .json extension must be JSON value.
      {
        inputs = new String[] {"src/test/dog2cat3.json", "return(_[0].name)"};
        CommandLine.run(inputs, outputs);
        assertNotNull(outputs[0]);
        assertNull(outputs[1]);
        assertEquals(outputs[0], "dog");

        inputs = new String[] {"src/test/invalid_as_json.json", "return(dog)"};
        CommandLine.run(inputs, outputs);
        assertNull(outputs[0]);
        assertNotNull(outputs[1]);
        assertTrue(outputs[1].indexOf("Error") > 0);

        inputs = new String[] {"src/test/invalid_as_json.viv", "return(dog)"};
        CommandLine.run(inputs, outputs);
        assertNotNull(outputs[0]);
        assertNull(outputs[1]);
        assertEquals(outputs[0], "3");
      }
  }

  @Test
  public void testUsage() {
    if (!isExistedJar) {
      return;
    }

    String[] results = run("java", "-jar", jarFileName, "a=3/0");
    assertNotNull(results);
    assertEquals(results.length, 1);
    assertTrue(results[0].indexOf("Error: Cannot evaluate") >= 0);

    for (String option : new String[] {"", "-help", "--invalid"}) {
      results = run("java", "-jar", jarFileName, option);
      assertNotNull(results);
      assertTrue(results.length > 1);
      int count = 0;
      for (String keyword : new String[] {"file extension", "Example 1"}) {
        for (String result : results) {
          if (result.indexOf(keyword) >= 0) {
            count++;
          }
        }
      }
      assertEquals(count, 2);
    }
  }

  @Test
  public void testUsageViaCommandLineClass() {
    String[] inputs;
    @Nullable String[] outputs = new String[2];

      {
        inputs = new String[] {"a=3/0"};
        CommandLine.run(inputs, outputs);
        assertNull(outputs[0]);
        assertNotNull(outputs[1]);
        assertTrue(outputs[1].indexOf("Error: Cannot evaluate") >= 0);
      }

      {
        for (String option : new String[] {"", "-help", "--invalid"}) {
          if (option.isEmpty()) {
            inputs = new String[] {};
          } else {
            inputs = new String[] {option};
          }
          CommandLine.run(inputs, outputs);
          assertNotNull(outputs[0]);
          assertNull(outputs[1]);
          int count = 0;
          for (String keyword : new String[] {"file extension", "Example 1"}) {
            if (outputs[0].indexOf(keyword) >= 0) {
              count++;
            }
          }
          assertEquals(count, 2);
        }
      }
  }


  @Test
  public void testVersion() {
    if (!isExistedJar) {
      return;
    }

    String[] results;

    for (String option : new String[] {"-v", "--version"}) {
      results = run("/bin/sh", "-c", "java -jar " + jarFileName + " " + option);
      assertNotNull(results);
      assertTrue(results.length > 1);
      int count = 0;
      for (String keyword : new String[] {"specification version", "interpreter version"}) {
        for (String result : results) {
          if (result.indexOf(keyword) >= 0) {
            count++;
          }
        }
      }
      assertEquals(count, 2);
    }
  }

  @Test
  public void testVersionViaCommandLineClass() {
    String[] inputs;
    @Nullable String[] outputs = new String[2];

      {
        for (String option : new String[] {"-v", "--version"}) {
          inputs = new String[] {option};
          CommandLine.run(inputs, outputs);
          assertNotNull(outputs[0]);
          assertNull(outputs[1]);
          int count = 0;
          for (String keyword : new String[] {"specification version", "interpreter version"}) {
            if (outputs[0].indexOf(keyword) >= 0) {
              count++;
            }
          }
          assertEquals(count, 2);
        }
      }
  }

  private static String @Nullable [] run(String... arguments) {
    ArrayList<String> lines = new ArrayList<>();
    try {
      ProcessBuilder builder = new ProcessBuilder(arguments);
      Process process = builder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
      if (lines.size() == 0) {
        reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        while ((line = reader.readLine()) != null) {
          lines.add(line);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

    String[] results = new String[lines.size()];
    int index = 0;
    for (String line : lines) {
      results[index] = line;
      index++;
    }
    return results;
  }
}
