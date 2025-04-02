/* Command-line client of VivJson.
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

import com.benesult.vivjson.Viv.Result;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Command-line client of VivJson.
 * <pre>{@code
 * Usage: java -jar vivjson.jar [ [option] <code or file> ... ]
 *
 * option: - "+" is concatenated the previous code and the following
 *           code, such as
 *             "a=" + "[1, 2]" "return(a[0]+a[1])"
 *         - "-i" or "--stdin" reads from PIPE.
 *           For example, the following command shows "6".
 *             echo '{"a":3}' | java -jar vivjson.jar -i "return(a*2)"
 *           "-i=<name>" or "--stdin=<name>" gives variable name for
 *           value that read from PIPE.
 *           For example, the following command shows
 *           {"dog": 2, "cat": 3}.
 *             echo '[{"x":"dog","y":2},{"x":"cat","y":3}]' | \
 *             java -jar vivjson.jar -i=data \
 *             "z={},for(v in data){z[v.x]=v.y},print(z)"
 *         - "-j" or "--json" indicates that the following code is JSON.
 *           For example, error occurs because the following value is
 *           invalid as JSON.
 *             java -jar vivjson.jar -j '{"a":1+2}'
 *         - "-v" or "--version" shows version.
 * }</pre>
 *
 * <p>Environment: Java 9 or later
 *
 * <p>Last modified: 2025-03-28
 *
 * @author Fumiaki Motegi (motegi@benesult.com)
 */
public class CommandLine {
  /**
   * Gets usage of VivJson in command line.
   */
  private static String getUsage() {
    return
        "VivJson\n"
      + "Usage: java -jar vivjson.jar [ [option] [ <code or file> ] ] ...\n"
      + "\n"
      + "option: - \"+\" is concatenated the previous code and the following\n"
      + "          code, such as\n"
      + "            \"a=\" + \"[1, 2]\" \"return(a[0]+a[1])\"\n"
      + "        - \"-i\" or \"--stdin\" reads from PIPE.\n"
      + "          For example, the following command shows \"6\".\n"
      + "            echo \'{\"a\":3}\' | java -jar vivjson.jar -i \"return(a*2)\"\n"
      + "          \"-i=<name>\" or \"--stdin=<name>\" gives variable name for\n"
      + "          value that read from PIPE.\n"
      + "          For example, the following command shows\n"
      + "          {\"dog\": 2, \"cat\": 3}.\n"
      + "            echo \'[{\"x\":\"dog\",\"y\":2},{\"x\":\"cat\",\"y\":3}]\' | \\\n"
      + "            java -jar vivjson.jar -i=data \\\n"
      + "            \"z={},for(v in data){z[v.x]=v.y},print(z)\"\n"
      + "        - \"-j\" or \"--json\" indicates that the following code is JSON.\n"
      + "          For example, error occurs because the following value is\n"
      + "          invalid as JSON.\n"
      + "            java -jar vivjson.jar -j '{\"a\":1+2}'\n"
      + "        - \"-v\" or \"--version\" shows version.\n"
      + "\n"
      + "       Note that the file extension must be "
                + "\"" + Viv.EXTENSIONS[0] + "\" or \""
                + Viv.EXTENSIONS[1] + "\".\n"
      + "\n"
      + "Example 1. The following codes show same result (5).\n"
      + "           - java -jar vivjson.jar \"a:3,b:2,return(a+b)\"\n"
      + "           - java -jar vivjson.jar \"{a:3,b:2,return(a+b)}\"\n"
      + "           - java -jar vivjson.jar \"{a:3,b:2}\" \"{return(a+b)}\"\n"
      + "           - java -jar vivjson.jar \"{a:3,b:2}\" \"return(a+b)\"\n"
      + "           - java -jar vivjson.jar \"x=\" + \"{a:3,b:2}\" "
                                                + "\"return(x.a+x.b)\"\n"
      + "           - java -jar vivjson.jar \"3,2\" \"return(_[0]+_[1])\"\n"
      + "           - java -jar vivjson.jar 3 2 \"return(_[0]+_[1])\"\n"
      + "           - java -jar vivjson.jar 3 \"return(_ + 2)\"\n"
      + "Example 2. Using file.\n"
      + "           - java -jar vivjson.jar test.viv\n"
      + "           - java -jar vivjson.jar data.json calc.viv\n"
      + "Example 3. Using code and file.\n"
      + "           - java -jar vivjson.jar \"{a:3,b:2}\" calc.viv\n"
      + "Example 4. Using PIPE.\n"
      + "           - echo \"return(3*5)\" | java -jar vivjson.jar -i\n"
      + "           - echo \"a=3\" "
                            + "| java -jar vivjson.jar -i \"return(a*2)\"\n"
      + "           - echo \'{\"a\":3}\' "
                            + "| java -jar vivjson.jar -i \"return(a*2)\"\n"
      + "           - cat test.viv | java -jar vivjson.jar -i\n"
      + "           - cat data.json "
                            + "| java -jar vivjson.jar -i \"return(a*b)\"\n"
      + "Example 5. Parsing data as JSON.\n"
      + "           - java -jar vivjson.jar -j '{\"a\":3,\"b\":2}' "
                                                    + "\"return(a+b)\"\n"
      + "           - echo '{\"a\":3,\"b\":2}' | \\\n"
      + "             java -jar vivjson.jar -j -i \"return(a+b)\"\n"
      + "\n"
      + "       Note that the combined option, such as \"-ji\", isn't "
                            + "allowed.";
  }

  /**
   * Gets version of VivJson in command line.
   */
  private static String getVersion() {
    return "VivJson's\n"
         + "    specification version: " + Config.SPEC_VERSION + "\n"
         + "    interpreter version:   " + Config.INTERPRETER_VERSION;
  }

  /**
   * Gets data or command from stdin.
   *
   * @return data/command if it is given, {@code null} otherwise.
   */
  private static @Nullable String getStdin() {
    String input = null;
    try {
      if (System.in.available() != 0) {
        InputStreamReader stdin = new InputStreamReader(System.in, "UTF-8");
        BufferedReader reader = new BufferedReader(stdin);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
          if (sb.length() > 0) {
            sb.append("\n");
          }
          sb.append(line);
        }
        input = sb.toString();
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    return input;
  }

  /**
   * Detects whether the given text is number or not.
   *
   * @param text a text that may be number.
   * @return true if the given text is number, false otherwise
   */
  private static boolean isNumber(String text) {
    try {
      Double.valueOf(text);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  /**
   * Runs VivJson in command-line.
   *
   * @param arguments arguments from command-line
   * @param outputs stdout and stderr.<br>
   *                1st element is a string of stdout if success,
   *                null otherwise.<br>
   *                2nd element is null if success, a string of
   *                stderr otherwise.
   */
  public static void run(String[] arguments, @Nullable String[] outputs) {
    outputs[0] = null;
    outputs[1] = null;

    if (arguments.length < 1) {
      outputs[0] = getUsage();
      return;
    }

    for (String argument : arguments) {
      if (argument.equals("-v") || argument.equals("--version")) {
        outputs[0] = getVersion();
        return;
      }
    }

    int stdinIndex = -1;
    String stdinName = null;
    int index = 0;
    for (String argument : arguments) {
      if (argument.indexOf("-i") == 0 || argument.indexOf("--stdin") == 0) {
        stdinIndex = index;
        if (argument.indexOf('=') > 0) {
          String[] data = argument.split("=", 3);
          if (data.length == 2 && data[1].length() > 0) {
            stdinName = data[1];
          }
        }
        break;
      }
      index++;
    }
    if (stdinIndex >= 0) {
      String input = getStdin();
      if (input == null) {
        outputs[1] = Viv.reportError("Cannot read from stdin.", false);
        return;
      }
      if (stdinName != null) {
        input = stdinName + ":" + input;
      }

      for (index = stdinIndex; index > 0; index--) {
        arguments[index] = arguments[index - 1];
      }
      arguments[0] = input;  // #1
    }

    /* | Original | Stdin | arguments     | stdinIndex | array as Result   |
     * |----------|-------|---------------|------------|-------------------|
     * | "a:3"    | ----  | ["a:3"]       | -1         | ["a:3"]           |
     * | -j "a:3" | ----  | ["-j", "a:3"] | -1         | [new Json("a:3")] |
     * | -i       | "a:3" | ["a:3"]       |  0         | ["a:3"]           |
     * | -j -i    | "a:3" | ["a:3", "-j"] |  1         | [new Json("a:3")] |
     */
    ArrayList<Object> array = new ArrayList<>();
    for (index = 0; index < arguments.length; index++) {
      String argument = arguments[index];
      if (argument.length() >= 1 && argument.charAt(0) == '#') {
        break;
      }

      if (argument.equals("-j") || argument.equals("--json")) {
        if (index == stdinIndex) {
          if (index == 0) {
            outputs[1] = Viv.reportError("Unexpected behavior.", false);
            return;
          }
          array.set(0, new Viv.Json(arguments[0]));  // #1
          continue;
        }
        if (index + 1 < arguments.length) {
          index++;
          array.add(new Viv.Json(arguments[index]));
          continue;
        }
      }
      array.add(argument);
    }

    if (array.size() == 1) {
      @Nullable Object object = array.get(0);
      if (object instanceof String) {
        String text = (String) object;
        if (text.length() == 0
            || (text.charAt(0) == '-' && !isNumber(text))) {
          outputs[0] = getUsage();
          return;
        }
      }
    }

    Result result = Viv.run(array);
    if (!result.errorMessage.isEmpty()) {
      outputs[1] = result.errorMessage;
      return;
    }

    outputs[0] = Viv.makeString(result.value);
  }

  /**
   * Runs VivJson in command-line.
   *
   * @param arguments arguments from command-line
   */
  public static void main(String[] arguments) {
    @Nullable String[] outputs = new String[2];
    CommandLine.run(arguments, outputs);
    if (outputs[1] != null) {
      System.err.println(outputs[1]);
    } else if (outputs[0] != null) {
      System.out.println(outputs[0]);
    }
  }
}
