/* Unit-test for Viv class
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.benesult.vivjson.Viv.Json;
import com.benesult.vivjson.Viv.Result;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Viv test class.
 * Unit-test for Viv class.
 *
 * <p>Environment
 * <ul>
 *   <li> Java 9 or later
 *   <li> JUnit 5.11 or later
 * </ul>
 *
 * <p>Last modified: 2025-03-29
 *
 * @author Fumiaki Motegi (motegi@benesult.com)
 */
public class VivTest {
  /** Unit-test for "Viv.run" method */
  @SuppressWarnings("null")
  @Test
  public void testRun() {
    // 10
    Result result = Viv.run("5", "return(_*2)");
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof Long);
    assertEquals((Long) result.value, 10);

      {
        // {"a": 3, "b": [2, 1]}
        result = Viv.run("{\"a\": 3, \"b\": [2, 1]}");
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        assertTrue(result.value instanceof HashMap);
        @SuppressWarnings("unchecked")
        HashMap<String, @Nullable Object> map = (HashMap<String, @Nullable Object>) result.value;
        assertEquals(map.get("a"), 3L);
        // System.out.println(map.get("a"));
        @SuppressWarnings("unchecked")
        ArrayList<@Nullable Object> list = (ArrayList<@Nullable Object>) map.get("b");
        assertEquals(list.get(0), 2L);
        // System.out.println(list.get(0));
      }

      {
        // {"a": 3, "b": [2, 1]}
        String[] strings = new String[] {"{\"a\": 3, \"b\": [2, 1]}"};
        result = Viv.run(strings, null);
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        assertTrue(result.value instanceof HashMap);
        @SuppressWarnings("unchecked")
        HashMap<String, @Nullable Object> map = (HashMap<String, @Nullable Object>) result.value;
        assertEquals(map.get("a"), 3L);
        // System.out.println(map.get("a"));
        @SuppressWarnings("unchecked")
        ArrayList<@Nullable Object> list = (ArrayList<@Nullable Object>) map.get("b");
        assertEquals(list.get(0), 2L);
        // System.out.println(list.get(0));
      }

    String[] strings;
    ArrayList<String> array = null;
    Object[] objects;

    // 5
    String command = "a:3,b:2,return(a+b)";
    for (int i = 0; i < 4; i++) {
      switch (i) {
        case 0:
          result = Viv.run(command);
          break;
        case 1:
          strings = new String[] {command};
          result = Viv.run(strings, null);
          break;
        case 2:
          array = new ArrayList<>();
          array.add(command);
          result = Viv.run(array);
          break;
        case 3:
          objects = new Object[] {array};
          result = Viv.run(objects);
          break;
        default:
          break;
      }
      assertNotNull(result);
      assertTrue(result.errorMessage.isEmpty());
      assertTrue(result.value instanceof Long);
      assertEquals((Long) result.value, 5);
    }

    // 5
    strings = new String[] {"3", "2", "return(_[0] + _[1])"};
    for (int i = 0; i < 3; i++) {
      switch (i) {
        case 0:
          result = Viv.run(strings, null);
          break;
        case 1:
          array = new ArrayList<>();
          for (String string : strings) {
            array.add(string);
          }
          result = Viv.run(array);
          break;
        case 2:
          objects = new Object[] {array};
          result = Viv.run(objects);
          break;
        default:
          break;
      }
      assertNotNull(result);
      assertTrue(result.errorMessage.isEmpty());
      assertTrue(result.value instanceof Long);
      assertEquals((Long) result.value, 5);
    }

    // 5
    array = new ArrayList<>();
    array.add("{a:3,b:2}");
    array.add("{return(a+b)}");
    result = Viv.run(array);
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof Long);
    assertEquals((Long) result.value, 5);
    array.set(1, "return(a+b)");
    result = Viv.run(array);
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof Long);
    assertEquals((Long) result.value, 5);

    // 5
    strings = new String[] {"x=", "+", "{a:3,b:2}", "return(x.a+x.b)"};
    result = Viv.run(strings, null);
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof Long);
    assertEquals((Long) result.value, 5);

    // 5
    result = Viv.run("{\"foo\":3, \"bar\": 2}", "return(foo + bar)");
    // System.out.println(result.value);  // 5
    assertEquals(result.errorMessage, "");
    assertTrue(result.value instanceof Long);
    assertEquals(result.value, 5L);

    // null
    result = Viv.run("{\"foo\":3, \"bar\": 2}", "return(qux)");
    // System.out.println(result.errorMessage.isEmpty());  // true
    // System.out.println(result.value);  // null
    assertEquals(result.errorMessage, "");
    assertNull(result.value);
    result = Viv.run("{\"foo\":3, \"bar\": 2}", "return('qux' in .)");
    // System.out.println(result.errorMessage.isEmpty());  // true
    // System.out.println(result.value);  // false
    assertEquals(result.errorMessage, "");
    assertEquals(result.value, false);

    // {}
    result = Viv.run();
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof HashMap);
    assertTrue(((HashMap<?, ?>) result.value).size() == 0);

    // Error
    result = Viv.run("x=", 3);
    assertNotNull(result);
    assertFalse(result.errorMessage.isEmpty());
    assertNull(result.value);

    // Error
    strings = new String[] {"x=", "+"};
    result = Viv.run(strings, null);
    assertNotNull(result);
    assertFalse(result.errorMessage.isEmpty());
    assertNull(result.value);

    // Error
    result = Viv.run("x=", "+", "+", "+");
    assertNotNull(result);
    assertFalse(result.errorMessage.isEmpty());
    assertNull(result.value);

    // Error
    result = Viv.run("x=", "+", new Config());
    assertNotNull(result);
    assertFalse(result.errorMessage.isEmpty());
    assertNull(result.value);

    // Error
    HashMap<String, Object> a3 = new HashMap<>();
    a3.put("a", 3);
    result = Viv.run("x=", "+", a3);
    assertNotNull(result);
    assertFalse(result.errorMessage.isEmpty());
    assertNull(result.value);

    // Error
    a3.clear();
    a3.put("___a___", 3);
    result = Viv.run(a3);
    assertNotNull(result);
    assertFalse(result.errorMessage.isEmpty());
    assertNull(result.value);
    
    // Error
    strings = new String[] {"+", "{a:3,b:2}"};
    result = Viv.run(strings, null);
    assertNotNull(result);
    assertFalse(result.errorMessage.isEmpty());
    assertNull(result.value);

    // 6
    result = Viv.run("src/test/call_6.viv");
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof Long);
    assertEquals((Long) result.value, 6);

    // 6
    result = Viv.run("../java/src/test/call_6.viv");
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof Long);
    assertEquals((Long) result.value, 6);

    // {}
    result = Viv.run("src/test/empty.viv");
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof HashMap);
    assertTrue(((HashMap<?, ?>) result.value).size() == 0);

    // Error
    result = Viv.run("src/test/dummy.viv");
    assertNotNull(result);
    assertFalse(result.errorMessage.isEmpty());
    assertNull(result.value);

    // Error
    result = Viv.run("src/test/call_6.vev");
    assertNotNull(result);
    assertFalse(result.errorMessage.isEmpty());
    assertNull(result.value);

    // {}
    result = Viv.run("");
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof HashMap);
    assertTrue(((HashMap<?, ?>) result.value).size() == 0);

    // 26
    result = Viv.run("src/test/a5b7c9.json", "src/test/axb-c.viv");
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof Long);
    assertEquals((Long) result.value, 26);

    // 21
    result = Viv.run("src/test/a5b7c9.json", "return(a+b+c)");
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof Long);
    assertEquals((Long) result.value, 21);

    // 5
    result = Viv.run("src/test/dog2cat3.json",
                         "sum = 0; for (a in _) {sum += a.number}; return(sum)");
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof Long);
    assertEquals((Long) result.value, 5);

    // 1
    // Note that number in result as represented as Long/Double even if
    // Byte/Short/Integer is given.
    // Furthermore, Double is returned even if Float is given.
    command = "return(if(y){:=x[0]}else{:=x[1]})";
    ArrayList<Integer> ints = new ArrayList<>();
    ints.add(1);
    ints.add(2);
    HashMap<String, @Nullable Object> block1 = new HashMap<>();
    block1.put("x", ints);
    block1.put("y", true);
    result = Viv.run(block1, command);
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof Long);
    assertEquals((Long) result.value, 1);

    // 2
    block1.replace("y", false);
    result = Viv.run(block1, command);
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof Long);
    assertEquals((Long) result.value, 2);

    // 1
    block1.remove("y");
    HashMap<String, @Nullable Object> block2 = new HashMap<>();
    block2.put("y", true);
    result = Viv.run(block1, block2, command);
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof Long);
    assertEquals((Long) result.value, 1);

    // 2
    block2.replace("y", false);
    result = Viv.run(block1, block2, command);
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof Long);
    assertEquals((Long) result.value, 2);

    // 11
    block1 = new HashMap<>();
    block1.put("x", 1);
    block2 = new HashMap<>();
    block2.put("x", 10);
    //                   x: 1,    y: x,  x: 10,   y += x --> 1 += 10 --> 11
    result = Viv.run(block1, "y: x", block2, "y += x, return(y)");
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof Long);
    assertEquals((Long) result.value, 11);

    // 1
    block1 = new HashMap<>();
    block1.put("y", 1);
    block2 = new HashMap<>();
    block2.put("x", block1);
    //                   {"x": {"y": 1}}
    result = Viv.run(block2, "return(x.y)");
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof Long);
    assertEquals((Long) result.value, 1);

    // 1
    ints.clear();
    ints.add(1);
    block1 = new HashMap<>();
    block1.put("x", ints);
    //                   {"x": [1]}
    result = Viv.run(block1, "return(x.0)");
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof Long);
    assertEquals((Long) result.value, 1);

    // 1
    HashMap<@Nullable Object, @Nullable Object> block3 = new HashMap<>();
    block3.put("0", 1);
    block2 = new HashMap<>();
    block2.put("x", block3);
    //                   {"x": {"0": 1}}
    result = Viv.run(block2, "return(x.0)");
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertTrue(result.value instanceof Long);
    assertEquals((Long) result.value, 1);

    // Error
    block3 = new HashMap<>();
    block3.put(0, 1);
    block2 = new HashMap<>();
    block2.put("x", block3);
    //                   {"x": {0: 1}}
    result = Viv.run(block2, "return(x.0)");
    assertNotNull(result);
    assertFalse(result.errorMessage.isEmpty());
    assertNull(result.value);

    // Error
    block3 = new HashMap<>();
    block3.put("0", 1);
    block3.put(null, 2);
    block2 = new HashMap<>();
    block2.put("x", block3);
    //                   {"x": {"0": 1, null: 2}}
    result = Viv.run(block2, "return(x.0)");
    assertNotNull(result);
    assertFalse(result.errorMessage.isEmpty());
    assertNull(result.value);

    // Error
    block3 = new HashMap<>();
    block3.put("0", 1);
    block3.put(true, 2);
    block2 = new HashMap<>();
    block2.put("x", block3);
    //                   {"x": {"0": 1, true: 2}}
    result = Viv.run(block2, "return(x.0)");
    assertNotNull(result);
    assertFalse(result.errorMessage.isEmpty());
    assertNull(result.value);

    // Error
    block1 = new HashMap<>();
    block1.put("x", new Literal(new Token(Token.Type.NUMBER, "100")));
    result = Viv.run(block1, "return(x)");
    assertNotNull(result);
    assertFalse(result.errorMessage.isEmpty());
    assertNull(result.value);

    // Error
    result = Viv.run(null, null, null);
    assertNotNull(result);
    assertFalse(result.errorMessage.isEmpty());
    assertNull(result.value);

    // Error
    String nonObject = null;
    result = Viv.run(nonObject);
    assertNotNull(result);
    assertFalse(result.errorMessage.isEmpty());
    assertNull(result.value);
    
    // Error
    result = Viv.run(null, null);
    assertNotNull(result);
    assertFalse(result.errorMessage.isEmpty());
    assertNull(result.value);

    // Error
    String[] nonObjects = null;
    result = Viv.run(nonObjects, null);
    assertNotNull(result);
    assertFalse(result.errorMessage.isEmpty());
    assertNull(result.value);

    // Error
    Config config = new Config();
    config.setMaxArraySize(4);
    result = Viv.run("a = [1, 2, 3, 4, 5]", config);
    assertFalse(result.errorMessage.isEmpty());
    assertNull(result.value);
  }

  @SuppressWarnings("null")
  @Test
  public void testRunEx() {
    Object result = null;
    try {
      result = Viv.runEx("a:3,b:2,return(a+b)");
    } catch (VivException e) {
      fail("Exception happen in testRunEx");
    }
    assertNotNull(result);
    assertEquals(result, 5L);
 
    result = null;
    try {
      result = Viv.runEx("a ! 3");
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      result = "exception";
    }
    assertTrue(result instanceof String);
    assertEquals(result, "exception");

    result = null;
    try {
      result = Viv.runEx("src/test/inf_nan.json");
    } catch (VivException e) {
      ;
    }
    assertNull(result);

    Config config = new Config();
    config.setInfinity("Infinity");
    config.setNaN("NaN");
    result = null;
    try {
      result = Viv.runEx("src/test/inf_nan.json", config);
    } catch (VivException e) {
      fail("Exception happen in testRunEx");
    }
    assertNotNull(result);
    assertTrue(result instanceof HashMap);
    @SuppressWarnings("unchecked")
    HashMap<String, @Nullable Object> map1 = (HashMap<String, @Nullable Object>) result;
    assertEquals(map1.get("normal"), 1.5);
    assertEquals(map1.get("inf"), Double.POSITIVE_INFINITY);
    assertEquals(map1.get("negative_inf"), Double.NEGATIVE_INFINITY);
    assertEquals(map1.get("nan"), Double.NaN);
    assertEquals(map1.get("str"), "1.5");

    result = null;
    try {
      result = Viv.runEx("src/test/dummy.txt");
    } catch (VivException e) {
      assertNull(result);
      result = "exception";
    }
    assertTrue(result instanceof String);
    assertEquals(result, "exception");

    result = null;
    try {
      // {"a": 3, "b": [2, 1]}
      result = Viv.runEx("{\"a\": 3, \"b\": [2, 1]}");
    } catch (VivException e) {
      fail("Exception happen in testRunEx");
    }
    assertNotNull(result);
    assertTrue(result instanceof HashMap);
    @SuppressWarnings("unchecked")
    HashMap<String, @Nullable Object> map2 = (HashMap<String, @Nullable Object>) result;
    assertEquals(map2.get("a"), 3L);
    // System.out.println(map2.get("a"));
    @SuppressWarnings("unchecked")
    ArrayList<@Nullable Object> list = (ArrayList<@Nullable Object>) map2.get("b");
    assertEquals(list.get(0), 2L);
    // System.out.println(list.get(0));

    result = null;
    try {
      // {"a": [10, {"b": null, "c": "test"}], "d": {"e": 3.5}}
      result = Viv.runEx("{\"a\": [10, {\"b\": null, \"c\": \"test\"}], \"d\": {\"e\": 3.5}}");
    } catch (VivException e) {
      fail("Exception happen in testRunEx");
    }
    assertNotNull(result);
    assertTrue(result instanceof HashMap);
    @SuppressWarnings("unchecked")
    HashMap<String, @Nullable Object> value = (HashMap<String, @Nullable Object>) result;
    assertTrue(value.get("a") instanceof ArrayList);
    @SuppressWarnings("unchecked")
    ArrayList<@Nullable Object> array = (ArrayList<@Nullable Object>) value.get("a");
    assertTrue(array.get(0) instanceof Long);
    assertEquals(array.get(0), 10L);
    // System.out.println(array.get(0));

    ArrayList<Object> a = new ArrayList<>();
    a.add(new Json("{\"foo\": 10, \"bar\": 1.5}"));
    a.add("baz = foo + bar");
    a.add("return(baz)");
    result = null;
    try {
      result = Viv.runEx(a);
    } catch (VivException e) {
      fail("Exception happen in testRunEx");
    }
    assertNotNull(result);
    assertEquals(result, 11.5);

    config.enableTagDetail();
    try {
      result = Viv.runEx("a ! 2", config);
      fail("Exception is not happened in testRunEx");
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      assertTrue(e.getMessage().indexOf("[Viv:Lexer] Error") == 0);
    }
    try {
      result = Viv.runEx("{2}", config);
      fail("Exception is not happened in testRunEx");
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      assertTrue(e.getMessage().indexOf("[Viv:Parser] Error") == 0);
    }
    try {
      result = Viv.runEx("a = 0, b = 2 / a", config);
      fail("Exception is not happened in testRunEx");
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      assertTrue(e.getMessage().indexOf("[Viv:Evaluator] Error") == 0);
    }
  }

  @SuppressWarnings("null")
  @Test
  public void testRunExWithArray() {
    String[] strings = new String[] {"{a:3,b:2}", "return(a+b)"};
    Object result = null;
    try {
      result = Viv.runEx(strings, null);
    } catch (VivException e) {
      fail("Exception happen in testRunEx");
    }
    assertNotNull(result);
    assertEquals(result, 5L);

    strings[1] = "!";
    result = null;
    try {
      result = Viv.runEx(strings, null);
    } catch (VivException e) {
      assertNull(result);
      result = "exception";
    }
    assertTrue(result instanceof String);
    assertEquals(result, "exception");

    strings = new String[] {};
    result = null;
    try {
      result = Viv.runEx(strings, null);
    } catch (VivException e) {
      fail("Exception happen in testRunEx");
    }
    assertNotNull(result);
    assertTrue(result instanceof HashMap);
    assertTrue(((HashMap<?, ?>) result).size() == 0);

    strings = new String[] {""};
    result = null;
    try {
      result = Viv.runEx(strings, null);
    } catch (VivException e) {
      fail("Exception happen in testRunEx");
    }
    assertNotNull(result);
    assertTrue(result instanceof HashMap);
    assertTrue(((HashMap<?, ?>) result).size() == 0);

    result = null;
    try {
      // {"a": 3, "b": [2, 1]}
      strings = new String[] {"{\"a\": 3, \"b\": [2, 1]}"};
      result = Viv.runEx(strings, null);
    } catch (VivException e) {
      fail("Exception happen in testRunEx");
    }
    assertNotNull(result);
    assertTrue(result instanceof HashMap);
    @SuppressWarnings("unchecked")
    HashMap<String, @Nullable Object> map = (HashMap<String, @Nullable Object>) result;
    assertEquals(map.get("a"), 3L);
    // System.out.println(map.get("a"));
    @SuppressWarnings("unchecked")
    ArrayList<@Nullable Object> list = (ArrayList<@Nullable Object>) map.get("b");
    assertEquals(list.get(0), 2L);
    // System.out.println(list.get(0));
  }

  @Test
  public void testParseAndRun() {
      {
        Viv.Parsed parsed = Viv.parse("return(a+b)");
        assertEquals(parsed.errorMessage, "");
        assertNotNull(parsed.statements);
        {
          Viv.Result result = Viv.run("{a:3,b:2}", parsed);
          assertEquals(result.errorMessage, "");
          assertEquals(result.value, 5L);
        }
        {
          HashMap<String, Integer> variables = new HashMap<>();
          variables.put("a", 5);
          variables.put("b", 7);
          Viv.Result result = Viv.run(variables, parsed);
          assertEquals(result.errorMessage, "");
          assertEquals(result.value, 12L);

          variables.replace("b", 10);
          result = Viv.run(variables, parsed);
          assertEquals(result.errorMessage, "");
          assertEquals(result.value, 15L);
        }
      }

      {
        Viv.Parsed parsed = Viv.parse("return(foo + bar)");
        Viv.Result result = Viv.run("{\"foo\":3, \"bar\": 2}", parsed);
        // System.out.println(result.value);  // 5
        assertEquals(result.errorMessage, "");
        assertEquals(result.value, 5L);
      }

      {
        Viv.Parsed parsed = Viv.parse("{a:1,b:8}", "return(a+b)");
        assertEquals(parsed.errorMessage, "");
        assertNotNull(parsed.statements);

        Viv.Result result = Viv.run(parsed);
        assertEquals(result.errorMessage, "");
        assertEquals(result.value, 9L);
      }

      {
        Object result = null;
        try {
          Viv.Parsed parsed = Viv.parseEx("return(a+b)");
          assertNotNull(parsed.statements);
          result = Viv.runEx("{a:3,b:2}", parsed);
          assertEquals(result, 5L);
        } catch (VivException e) {
          // System.err.println(e.getMessage());
          fail("Exception happen in testRunAfterParsed");
        }
        assertNotNull(result);
        assertEquals(result, 5L);
      }

      {
        Config config = new Config();
        config.setMaxDepth(3);
        Viv.Parsed parsed = Viv.parse("3", "2", "return(_[0] + _[1])", config);
        assertEquals(parsed.errorMessage, "");
        assertNotNull(parsed.statements);

        Viv.Result result = Viv.run(parsed);
        assertEquals(result.errorMessage, "");
        assertEquals(result.value, 5L);
      }

      {
        String[] codes = new String[] {"a=", "return(2)"};
        Viv.Parsed parsed = null;
        try {
          parsed = Viv.parseEx(codes, null);
        } catch (VivException e) {
          assertFalse(e.getMessage().isEmpty());
        }
        assertNull(parsed);
      }

      {
        Viv.Instance instance = new Viv.Instance("error");
        Viv.Parsed parsed = Viv.parse(instance, "a = 2");
        assertNotNull(parsed);
        assertFalse(parsed.errorMessage.isEmpty());
        assertNull(parsed.statements);
        assertNull(parsed.instance);
      }

      {
        HashMap<Integer, Object> invalid = new HashMap<>();
        invalid.put(1, "10");
        Viv.Parsed parsed = Viv.parse("x = 2", invalid);
        assertNotNull(parsed);
        assertFalse(parsed.errorMessage.isEmpty());
        assertNull(parsed.statements);
        assertNull(parsed.instance);
      }

      {
        Viv.Parsed parsed = Viv.parseText(null, null);
        assertNotNull(parsed);
        assertFalse(parsed.errorMessage.isEmpty());
        assertNull(parsed.statements);
        assertNull(parsed.instance);
      }

      {
        Viv.Parsed parsed = null;
        try {
          parsed = Viv.parseTextEx("a => 2", null);
        } catch (VivException e) {
          assertFalse(e.getMessage().isEmpty());
        }
        assertNull(parsed);
      }

      {
        Viv.Parsed parsed = null;
        try {
          parsed = Viv.parseTextEx("a = 2", null);
        } catch (VivException e) {
          fail("Exception happen in testRunAfterParsed");
        }
        assertNotNull(parsed);
        assertTrue(parsed.errorMessage.isEmpty());
        assertNotNull(parsed.statements);
        assertNull(parsed.instance);
      }
  }

  @Test
  public void testParseAndRunWithArray() {
    String[] statements = new String[] {"a*=10,b+=1", "return(a+b)"};
    Viv.Parsed parsed = Viv.parse(statements, null);
    assertEquals(parsed.errorMessage, "");
    assertNotNull(parsed.statements);

    Object[] objects = new Object[] {"{a:3,b:2}", parsed};
    Viv.Result result = Viv.run(objects, null);
    assertEquals(result.errorMessage, "");
    assertEquals(result.value, 33L);

    HashMap<String, Integer> variables = new HashMap<>();
    variables.put("a", 5);
    variables.put("b", 7);
    objects[0] = variables;
    result = Viv.run(objects, null);
    assertEquals(result.errorMessage, "");
    assertEquals(result.value, 58L);

    variables.replace("b", 10);
    result = Viv.run(objects, null);
    assertEquals(result.errorMessage, "");
    assertEquals(result.value, 61L);

    Object value = null;
    try {
      parsed = Viv.parseEx(statements, null);
      assertNotNull(parsed.statements);
      objects = new Object[] {"{a:3,b:2}", parsed};
      value = Viv.runEx(objects, null);
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testRunWithArrayAfterParsed");
    }
    assertNotNull(value);
    assertEquals(value, 33L);
  }

  @Test
  public void testParseFileAndRun() {
    Viv.Parsed parsed = Viv.parseFile("src/test/call_6.viv", null);
    assertEquals(parsed.errorMessage, "");
    assertNotNull(parsed.statements);

    Viv.Result result = Viv.run(parsed);
    assertEquals(result.errorMessage, "");
    assertEquals(result.value, 6L);

    try {
      parsed = Viv.parseFileEx("src/test/empty.viv", null);
      assertEquals(parsed.errorMessage, "");
      assertNotNull(parsed.statements);
    } catch (VivException e) {
      fail("Exception happen in testRunAfterParseFile");
    }
    result = Viv.run(parsed);
    assertEquals(result.errorMessage, "");
    assertTrue(result.value instanceof HashMap);
    assertEquals(((HashMap<?, ?>) result.value).size(), 0);

    parsed = Viv.parseFile("src/test/invalid.viv", null);
    assertNotEquals(parsed.errorMessage, "");
    assertNull(parsed.statements);
  }

  @Test
  public void testParseTextAndRun() {
    Viv.Parsed parsed = Viv.parseText("return(3+2)", null);
    assertEquals(parsed.errorMessage, "");
    assertNotNull(parsed.statements);

    Viv.Result result = Viv.run(parsed);
    assertEquals(result.errorMessage, "");
    assertEquals(result.value, 5L);

    parsed = Viv.parseText("return(3", null);
    assertNotEquals(parsed.errorMessage, "");
    assertNull(parsed.statements);

    parsed = Viv.parseText("3", null);
    assertEquals(parsed.errorMessage, "");
    assertNotNull(parsed.statements);
  }

  @SuppressWarnings("null")
  @Test
  public void testRunWithVariable() {
    Viv.Parsed parsed = Viv.parseText("return(x.a+x.b)", null);
    assertEquals(parsed.errorMessage, "");
    assertNotNull(parsed.statements);
    assertTrue(parsed.statements instanceof ArrayList);
    ArrayList<Statement> statements = parsed.statements;
    assertNotNull(statements);
    assertEquals(statements.size(), 1);
    assertTrue(statements.get(0) instanceof Return);
    String text = statements.get(0).toString();
    assertEquals(text, "return(x[\"a\"] + x[\"b\"])");

    HashMap<String, @Nullable Object> members = new HashMap<>();
    members.put("a", 70);
    members.put("b", 30);
    HashMap<String, @Nullable Object> variable = new HashMap<>();
    variable.put("x", members); // {"x": {"a": 70, "b": 30}}
    Viv.Result result = Viv.run(variable, parsed);
    assertEquals(result.errorMessage, "");
    assertEquals(result.value, 100L);

    result = Viv.run(1, parsed);
    assertNotEquals(result.errorMessage, "");
    assertNull(result.value);

    result = Viv.run(variable, 3);
    assertNotEquals(result.errorMessage, "");
    assertNull(result.value);

    parsed = Viv.parseText("return(if(y){:=x[0]}else{:=x[1]})", null);
    assertEquals(parsed.errorMessage, "");
    assertNotNull(parsed.statements);

    ArrayList<Integer> array = new ArrayList<>();
    array.add(1);
    array.add(2);
    variable = new HashMap<>();
    variable.put("x", array);
    variable.put("y", true);  // {"x":[1,2],"y":true}
    result = Viv.run(variable, parsed);
    assertEquals(result.errorMessage, "");
    assertEquals(result.value, 1L);

    variable.put("y", false);
    result = Viv.run(variable, parsed);
    assertEquals(result.errorMessage, "");
    assertEquals(result.value, 2L);

    variable = new HashMap<>();
    variable.put("x", 10);
    //                                           {"x": 10}
    parsed = Viv.parse("y: x", variable, "y += x, return(y)");
    assertEquals(parsed.errorMessage, "");
    assertNotNull(parsed.statements);

    variable.put("x", 100);  // {"x": 100}
    result = Viv.run(variable, parsed);
    assertEquals(result.errorMessage, "");
    assertEquals(result.value, 110L);

    variable.put("x", -100);  // {"x": -100}
    result = Viv.run(variable, parsed);
    assertEquals(result.errorMessage, "");
    assertEquals(result.value, -90L);

    variable.put("x", (short) 10);  // {"x": 10}
    result = Viv.run(variable, parsed);
    assertEquals(result.errorMessage, "");
    assertEquals(result.value, 20L);

    variable.put("x", (byte) 1);  // {"x": 11}
    result = Viv.run(variable, parsed);
    assertEquals(result.errorMessage, "");
    assertEquals(result.value, 11L);

    parsed = Viv.parseText("return(x.0)", null);
    assertEquals(parsed.errorMessage, "");
    assertNotNull(parsed.statements);

    array.clear();
    array.add(100);
    variable.put("x", array);  // {"x": [100]}
    result = Viv.run(variable, parsed);
    assertEquals(result.errorMessage, "");
    assertEquals(result.value, 100L);

    ArrayList<Object> arrayObj = new ArrayList<>();
    arrayObj.add(new Date());
    variable.put("x", arrayObj);  // {"x": new Date()}
    result = Viv.run(variable, parsed);
    assertNotEquals(result.errorMessage, "");
    assertNull(result.value);

    members = new HashMap<>();
    members.put("0", 100);
    variable.put("x", members);  // {"x": {"0": 100}}
    result = Viv.run(variable, parsed);
    assertEquals(result.errorMessage, "");
    assertEquals(result.value, 100L);

    HashMap<Long, @Nullable Object> member = new HashMap<>();
    member.put(0L, 100);
    variable.put("x", member);  // {"x": {0: 100}}
    result = Viv.run(variable, parsed);
    assertNotEquals(result.errorMessage, "");
    assertNull(result.value);
  }

  @Test
  public void testMakeInstanceAndRun() {
      {
        String code = "_x = 3 "
                    + "function add(a, b) { "
                    + "  return(a + b) "
                    + "} "
                    + "y = true";

          {
            Viv.Instance instance = Viv.makeInstance(code);
            assertEquals(instance.errorMessage, "");
            assertNotNull(instance.evaluator);

            Viv.Result result = Viv.run(instance, new Object[] {"add", 3, 8});
            assertEquals(result.errorMessage, "");
            assertEquals(result.value, 11L);

            result = Viv.run(instance, new Object[] {"add", 3, null});
            assertEquals(result.errorMessage, "");
            assertEquals(result.value, 3L);

            result = Viv.run(instance, new Object[] {"add", true, false});
            assertEquals(result.errorMessage, "");
            assertEquals(result.value, true);

            result = Viv.run(instance, new Object[] {"add", "foo", "bar"});
            assertEquals(result.errorMessage, "");
            assertEquals(result.value, "foobar");

            result = Viv.run(new Object[] {"add", "foo", "bar"}, instance);
            assertEquals(result.errorMessage, "");
            assertEquals(result.value, "foobar");

            Integer integer = Viv.getInteger(instance, new Object[] {"add", 100, -10});
            assertNotNull(integer);
            assertEquals(integer, 90);

            // ['foo', 'bar'] + 'baz' --> ['foo', 'bar', 'baz']
            ArrayList<String> argumentStrings = new ArrayList<>();
            argumentStrings.add("foo");
            argumentStrings.add("bar");
            result = Viv.run(instance, new Object[] {"add", argumentStrings, "baz"});
            assertEquals(result.errorMessage, "");
            assertTrue(result.value instanceof ArrayList);
            String[] returnedStrings =
                Viv.getStrings(instance, new Object[] {"add", argumentStrings, "baz"});
            assertNotNull(returnedStrings);
            assertEquals(returnedStrings[0], "foo");
            assertEquals(returnedStrings[1], "bar");
            assertEquals(returnedStrings[2], "baz");

            // {'foo': 2, 'bar': 5} + {'baz': 10} --> {'foo': 2, 'bar': 5, 'baz': 10}
            HashMap<String, Integer> argumentStrInts1 = new HashMap<>();
            argumentStrInts1.put("foo", 2);
            argumentStrInts1.put("bar", 5);
            HashMap<String, Integer> argumentStrInts2 = new HashMap<>();
            argumentStrInts2.put("baz", 10);
            Viv.KeyValue<Integer>[] keyValueInts = Viv.getKeyValueIntegers(
                    instance, new Object[] {"add", argumentStrInts1, argumentStrInts2});
            assertNotNull(keyValueInts);
            assertEquals(keyValueInts.length, 3);
            Object[][] data = new Object[][] {{"foo", 2}, {"bar", 5}, {"baz", 10}};
            for (int i = 0; i < keyValueInts.length; i++) {
              assertTrue(isContained(new Object[] {keyValueInts[i].key, keyValueInts[i].value},
                                     data));
            }

            // {'foo': 2, 'bar': 5} + {1: 10} --> Error
            HashMap<Integer, Integer> argumentIntInts = new HashMap<>();
            argumentIntInts.put(1, 10);
            keyValueInts = Viv.getKeyValueIntegers(
                    instance, new Object[] {"add", argumentStrInts1, argumentIntInts});
            assertNull(keyValueInts);
            result = Viv.run(instance, new Object[] {"add", argumentStrInts1, argumentIntInts});
            assertNotEquals(result.errorMessage, "");

            // 1 + Date --> Error
            Date date = new Date();
            result = Viv.run(instance, new Object[] {"add", 1, date});
            assertNotEquals(result.errorMessage, "");

            // {'foo': 2, 'bar': 5} + {'baz': Date} --> Error
            keyValueInts = Viv.getKeyValueIntegers(
                    instance, new Object[] {"add", argumentStrInts1, date});
            assertNull(keyValueInts);
            result = Viv.run(instance, new Object[] {"add", argumentStrInts1, date});
            assertNotEquals(result.errorMessage, "");
          }

          {
            Viv.Parsed parsed = Viv.parseText(code, null);
            assertEquals(parsed.errorMessage, "");
            assertNotNull(parsed.statements);

            Viv.Instance instance = Viv.makeInstance(parsed);
            assertEquals(instance.errorMessage, "");
            assertNotNull(instance.evaluator);

            Viv.Result result = Viv.run(instance, new Object[] {"add", 3, 8});
            assertEquals(result.errorMessage, "");
            assertEquals(result.value, 11L);
          }

          {
            Viv.Instance instance = Viv.makeInstance(code);
            assertEquals(instance.errorMessage, "");
            assertNotNull(instance.evaluator);

            Viv.Result result = Viv.run(instance, "return(_x)");
            assertEquals(result.errorMessage, "");
            assertEquals(result.value, 3L);

            result = Viv.run(instance, "return(y)");
            assertEquals(result.errorMessage, "");
            assertEquals(result.value, true);

            result = Viv.run(instance, "z = add(10, 30), return(z)");
            assertEquals(result.errorMessage, "");
            assertEquals(result.value, 40L);

            // {} if the argument is only instance.
            result = Viv.run(instance);
            assertEquals(result.errorMessage, "");
            assertTrue(result.value instanceof HashMap);
            @SuppressWarnings("unchecked")
            HashMap<String, @Nullable Object> value =
                (HashMap<String, @Nullable Object>) result.value;
            assertEquals(value.size(), 0);
          }
      }

      {
        Viv.Parsed parsed = Viv.parseText("function add(a, b) {"
                                                + "  return (a + b)"
                                                + "} "
                                                + "y = 3 / 0",
                                                  null);
        assertEquals(parsed.errorMessage, "");
        assertNotNull(parsed.statements);

        Viv.Instance instance = Viv.makeInstance(parsed);
        assertNotEquals(instance.errorMessage, "");
        assertNull(instance.evaluator);
      }

      {
        Viv.Parsed parsed = Viv.parseText("function div(a, b) {"
                                                + "  return(a / b)"
                                                + "}"
                                                + "function _div(a, b) {"
                                                + "  return(a / b)"
                                                + "}"
                                                + "y = 3 / 2",
                                                  null);
        assertEquals(parsed.errorMessage, "");
        assertNotNull(parsed.statements);

        Viv.Instance instance = Viv.makeInstance(parsed);
        assertEquals(instance.errorMessage, "");
        assertNotNull(instance.evaluator);

        assertEquals(Viv.getFloat(instance, "return(y)"), 1.5F);
        assertEquals(Viv.getFloat(instance, new Object[] {"y"}), 1.5F);

        assertNull(Viv.getDouble(instance, new Object[] {"div", 10, 0}));
        assertEquals(Viv.getInteger(instance, new Object[] {"div", 10, 2}), 5);
        assertEquals(Viv.getDouble(instance, new Object[] {"div", 10, 2}), 5.0);

        assertNull(Viv.getDouble(instance, new Object[] {"_div", 10, 2}));

        Date date = new Date();
        assertNull(Viv.getDouble(date, new Object[] {"div", 10, 2}));
        Viv.Result result = Viv.run(date, new Object[] {"div", 10, 2});
        assertNotEquals(result.errorMessage, "");

        assertNull(Viv.getDouble(instance, new Object[] {"add", 10, 2}));

        // 1 / [3, 2] --> Error
        ArrayList<Integer> argumentInts = new ArrayList<>();
        argumentInts.add(3);
        argumentInts.add(2);
        result = Viv.run(instance, new Object[] {"div", 1, argumentInts});
      }

      {
        String code = "function test(x, y) {"
                    + "  z = x.a + x.b.1 + y"
                    + "  return(z)"
                    + "}";
        // {"a": 100, "b": [1.0, 2.0]}
        ArrayList<Double> list = new ArrayList<>();
        list.add(1.0);
        list.add(2.0);
        HashMap<String, Object> map = new HashMap<>();
        map.put("a", 100);
        map.put("b", list);

        {
          Viv.Instance instance = Viv.makeInstance(code);
          assertEquals(instance.errorMessage, "");
          assertNotNull(instance.evaluator);

          Viv.Result result = Viv.run(instance, new Object[] {"test", map, 3});
          assertEquals(result.errorMessage, "");
          assertEquals(result.value, 105.0);
        }

        {
          try {
            Viv.Instance instance = Viv.makeInstanceEx(code);
            assertEquals(instance.errorMessage, "");
            assertNotNull(instance.evaluator);

            Object result = Viv.runEx(instance, new Object[] {"test", map, 3});
            assertEquals(result, 105.0);
            // System.out.println(result);
          } catch (VivException e) {
            fail("Exception happen in testMakeInstanceAndRun");
          }
        }
      }


      {
        String[] codes = new String[] {
          "function add3(x, y, z) {return(x + y + z)}",
          "function test(x, y) {return(add3(x.a, x.b.1, y))}"
        };
        // {"a": 100, "b": [1.0, 2.0]}
        ArrayList<Double> list = new ArrayList<>();
        list.add(1.0);
        list.add(2.0);
        HashMap<String, Object> map = new HashMap<>();
        map.put("a", 100);
        map.put("b", list);

        {
          Viv.Instance instance = Viv.makeInstance(codes, null);
          assertEquals(instance.errorMessage, "");
          assertNotNull(instance.evaluator);

          Viv.Result result = Viv.run(instance, new Object[] {"test", map, 3});
          assertEquals(result.errorMessage, "");
          assertEquals(result.value, 105.0);
        }

        {
          try {
            Viv.Instance instance = Viv.makeInstanceEx(codes, null);
            assertEquals(instance.errorMessage, "");
            assertNotNull(instance.evaluator);

            Object result = Viv.runEx(instance, new Object[] {"test", map, 3});
            assertEquals(result, 105.0);
            // System.out.println(result);
          } catch (VivException e) {
            fail("Exception happen in testMakeInstanceAndRun");
          }
        }
      }

      {
        Viv.Instance instance = Viv.makeInstance("src/test/dog2cat3.json");
        assertEquals(instance.errorMessage, "");
        assertNotNull(instance.evaluator);

        try {
          assertEquals(Viv.runEx(instance, "return(_[0]['name'])"), "dog");
          assertEquals(Viv.runEx(instance, "return(_[0]['number'])"), 2L);
          assertEquals(Viv.runEx(instance, "return(_[1]['name'])"), "cat");
          assertEquals(Viv.runEx(instance, "return(_[1]['number'])"), 3L);
        } catch (VivException e) {
          fail("Exception happen in testMakeInstanceAndRun");
        }

        String[] keys = new String[] {"name", "number"};
        Object[] variable = new Object[] {"_", null, null};
        Object[][] values = new Object[][] {
          {"dog", 2L},
          {"cat", 3L}
        };
        try {
          for (int i = 0; i < values.length; i++) {
            variable[1] = i;
            for (int j = 0; j < keys.length; j++) {
              variable[2] = keys[j];
              assertEquals(Viv.runEx(instance, variable), values[i][j]);
            }
          }
        } catch (VivException e) {
          fail("Exception happen in testMakeInstanceAndRun");
        }
      }

      {
        Viv.Instance instance = Viv.makeInstance("src/test/a5b7c9.json");
        assertEquals(instance.errorMessage, "");
        assertNotNull(instance.evaluator);

        try {
          assertEquals(Viv.runEx(instance, "return(a)"), 5L);
          assertEquals(Viv.runEx(instance, "return(b)"), 7L);
          assertEquals(Viv.runEx(instance, "return(c)"), 9L);
        } catch (VivException e) {
          fail("Exception happen in testMakeInstanceAndRun");
        }

        try {
          assertEquals(Viv.runEx(instance, new Object[]{"a"}), 5L);
          assertEquals(Viv.runEx(instance, new Object[]{"b"}), 7L);
          assertEquals(Viv.runEx(instance, new Object[]{"c"}), 9L);
        } catch (VivException e) {
          fail("Exception happen in testMakeInstanceAndRun");
        }
      }

      {
        // {"a": [10, {"b": null, "c": "test"}], "d": {"e": 3.5}}
        Viv.Instance instance = Viv.makeInstance(
            "{\"a\": [10, {\"b\": null, \"c\": \"test\"}], \"d\": {\"e\": 3.5}}");
        assertEquals(instance.errorMessage, "");
        assertNotNull(instance.evaluator);

        try {
          assertEquals(Viv.runEx(instance, "return(a.0)"), 10L);
          assertEquals(Viv.runEx(instance, "return(a.1.b)"), null);
          assertEquals(Viv.runEx(instance, "return(a.1.c)"), "test");
          assertEquals(Viv.runEx(instance, "return(d.e)"), 3.5);
        } catch (VivException e) {
          fail("Exception happen in testMakeInstanceAndRun");
        }

        try {
          assertEquals(Viv.runEx(instance, new Object[]{"a", 0}), 10L);
          assertEquals(Viv.runEx(instance, new Object[]{"a", 1, "b"}), null);
          assertEquals(Viv.runEx(instance, new Object[]{"a", 1, "c"}), "test");
          assertEquals(Viv.runEx(instance, new Object[]{"d", "e"}), 3.5);
        } catch (VivException e) {
          fail("Exception happen in testMakeInstanceAndRun");
        }
      }

      {
        // ["a", 10, [{"b": null, "c": "test"}, false]]
        Viv.Instance instance = Viv.makeInstance(
            "[\"a\", 10, [{\"b\": null, \"c\": \"test\"}, false]]");
        assertEquals(instance.errorMessage, "");
        assertNotNull(instance.evaluator);

        try {
          assertEquals(Viv.runEx(instance, "return(_[0])"), "a");
          assertEquals(Viv.runEx(instance, "return(_.1)"), 10L);
          assertEquals(Viv.runEx(instance, "return(_[2][0]['b'])"), null);
          assertEquals(Viv.runEx(instance, "return(_[2][-2]['b'])"), null);
          assertEquals(Viv.runEx(instance, "return(_[2][0].c)"), "test");
          assertEquals(Viv.runEx(instance, "return(_.2.-2.c)"), "test");
          assertEquals(Viv.runEx(instance, "return(_[2].1)"), false);
        } catch (VivException e) {
          fail("Exception happen in testMakeInstanceAndRun");
        }

        try {
          assertEquals(Viv.runEx(instance, new Object[]{"_", 0}), "a");
          assertEquals(Viv.runEx(instance, new Object[]{"_", 1}), 10L);
          assertEquals(Viv.runEx(instance, new Object[]{"_", 2, 0, "b"}), null);
          assertEquals(Viv.runEx(instance, new Object[]{"_", 2, -2, "b"}), null);
          assertEquals(Viv.runEx(instance, new Object[]{"_", 2, 0, "c"}), "test");
          assertEquals(Viv.runEx(instance, new Object[]{"_", 2, -2, "c"}), "test");
          assertEquals(Viv.runEx(instance, new Object[]{"_", 2, 1}), false);
        } catch (VivException e) {
          fail("Exception happen in testMakeInstanceAndRun");
        }
      }

      {
        Viv.Instance instance = Viv.makeInstance("100");
        assertEquals(instance.errorMessage, "");
        assertNotNull(instance.evaluator);

        try {
          assertEquals(Viv.runEx(instance, "return(_)"), 100L);
        } catch (VivException e) {
          fail("Exception happen in testMakeInstanceAndRun");
        }

        try {
          assertEquals(Viv.runEx(instance, new Object[]{"_"}), 100L);
        } catch (VivException e) {
          fail("Exception happen in testMakeInstanceAndRun");
        }
      }

      {
        Viv.Instance instance = Viv.makeInstance("1000", "2000");
        assertEquals(instance.errorMessage, "");
        assertNotNull(instance.evaluator);

        try {
          assertEquals(Viv.runEx(instance, "return(_[0])"), 1000L);
          assertEquals(Viv.runEx(instance, "return(_.1)"), 2000L);
        } catch (VivException e) {
          fail("Exception happen in testMakeInstanceAndRun");
        }

        try {
          assertEquals(Viv.runEx(instance, new Object[]{"_", 0}), 1000L);
          assertEquals(Viv.runEx(instance, new Object[]{"_", 1}), 2000L);
        } catch (VivException e) {
          fail("Exception happen in testMakeInstanceAndRun");
        }
      }

      {
        Viv.Instance instance = Viv.makeInstance("foo", "bar");
        assertEquals(instance.errorMessage, "");
        assertNotNull(instance.evaluator);

        try {
          assertEquals(Viv.runEx(instance, "return(_[0])"), "foo");
          assertEquals(Viv.runEx(instance, "return(_.1)"), "bar");
        } catch (VivException e) {
          fail("Exception happen in testMakeInstanceAndRun");
        }

        try {
          assertEquals(Viv.runEx(instance, new Object[]{"_", 0}), "foo");
          assertEquals(Viv.runEx(instance, new Object[]{"_", 1}), "bar");
        } catch (VivException e) {
          fail("Exception happen in testMakeInstanceAndRun");
        }
      }

      {
        Viv.Instance instance = Viv.makeInstance("foo, bar");
        assertEquals(instance.errorMessage, "");
        assertNotNull(instance.evaluator);

        try {
          assertEquals(Viv.runEx(instance, "return(_[0])"), "foo");
          assertEquals(Viv.runEx(instance, "return(_.1)"), "bar");
        } catch (VivException e) {
          fail("Exception happen in testMakeInstanceAndRun");
        }

        try {
          assertEquals(Viv.runEx(instance, new Object[]{"_", 0}), "foo");
          assertEquals(Viv.runEx(instance, new Object[]{"_", 1}), "bar");
        } catch (VivException e) {
          fail("Exception happen in testMakeInstanceAndRun");
        }
      }

      {
        Object[] values = new Object[] {true, false, null, "text", 100L, 1.0};
        Viv.Instance instance;
        for (int i = 0; i < 2; i++) {
          instance = (i == 0)
              ? Viv.makeInstance("true", "false", "null", "text", "100", "1.0")
              : Viv.makeInstance(new String[] {"true", "false", "null", "text", "100", "1.0"},
                                     null);
          assertEquals(instance.errorMessage, "");
          assertNotNull(instance.evaluator);

          try {
            for (int j = 0; j < values.length; j++) {
              assertEquals(Viv.runEx(instance, "return(_[" + j + "])"), values[j]);
              // System.out.println(Viv.runEx(instance, "return(_[" + j + "])"));
            }
          } catch (VivException e) {
            fail("Exception happen in testMakeInstanceAndRun");
          }

          try {
            Object[] variable = new Object[] {"_", null};
            for (int j = 0; j < values.length; j++) {
              variable[1] = j;
              assertEquals(Viv.runEx(instance, variable), values[j]);
              // System.out.println(Viv.runEx(instance, variable));
            }
          } catch (VivException e) {
            fail("Exception happen in testMakeInstanceAndRun");
          }
        }
      }

      {
        // {"a": 3, "b": 2}
        String code = "{\"a\": 3, \"b\": 2}";
        Viv.Instance instance;
        
        for (int i = 0; i < 2; i++) {
          instance = (i == 0)
                     ? Viv.makeInstance(code)
                     : Viv.makeInstance(new String[]{code}, null);
          assertEquals(instance.errorMessage, "");
          assertNotNull(instance.evaluator);

          try {
            assertEquals(Viv.runEx(instance, "return(a)"), 3L);
            // System.out.println(Viv.runEx(instance, "return(a)"));
            assertEquals(Viv.runEx(instance, "return(a + b)"), 5L);
            // System.out.println(Viv.runEx(instance, "return(a + b)"));
            assertEquals(Viv.runEx(instance, "c = a * b, return(c)"), 6L);
            // System.out.println(Viv.runEx(instance, "c = a * b, return(c)"));
            assertNull(Viv.runEx(instance, "return(c)"));
            // System.out.println(Viv.runEx(instance, "return(c)"));
            assertEquals(Viv.runEx(instance, "a += 100, return(a)"), 103L);
            // System.out.println(Viv.runEx(instance, "a += 100, return(a)"));
            assertEquals(Viv.runEx(instance, "return(a)"), 103L);
            // System.out.println(Viv.runEx(instance, "return(a)"));
          } catch (VivException e) {
            fail("Exception happen in testMakeInstanceAndRun");
          }
        }
      }

      {
        // ["foo", 10, [{"bar": null, "baz": "test"}, false]]
        Viv.Instance instance = Viv.makeInstance(
            "[\"foo\", 10, [{\"bar\": null, \"baz\": \"test\"}, false]]");
        try {
          // System.out.println(Viv.runEx(instance, new Object[]{"_", 2, 0, "bar"}));  // null
          // System.out.println(Viv.runEx(instance, "return(_[2][0]['bar'])"));  // null
          // System.out.println(Viv.runEx(instance, new Object[]{"_", 2, -2, "baz"}));  // "test"
          // System.out.println(Viv.runEx(instance, "return(_.2.-2.baz)"));  // "test"
          assertNull(Viv.runEx(instance, new Object[]{"_", 2, 0, "bar"}));
          assertNull(Viv.runEx(instance, "return(_[2][0]['bar'])"));
          assertEquals(Viv.runEx(instance, new Object[]{"_", 2, -2, "baz"}), "test");
          assertEquals(Viv.runEx(instance, "return(_.2.-2.baz)"), "test");
        } catch (VivException e) {
          // System.err.println(e.getMessage());
          fail("Exception happen in testMakeInstanceAndRun");
        }
      }

      {
        // ["foo", 10, [{"bar": null, "baz": "test"}, false]]
        Viv.Instance instance = Viv.makeInstance(
            "[\"foo\", 10, [{\"bar\": null, \"baz\": \"test\"}, false]]");
        try {
          Viv.runEx(instance, new Object[]{"_", 2.2, 0, "bar"});
          fail("Exception is not happened in testMakeInstanceAndRun");
        } catch (VivException e) {
          assertFalse(e.getMessage().isEmpty());
        }

        try {
          Viv.runEx(instance, new Object[]{0, 2});
          fail("Exception is not happened in testMakeInstanceAndRun");
        } catch (VivException e) {
          assertFalse(e.getMessage().isEmpty());
        }

        try {
          Viv.runEx(instance, new Object[]{"_", true, 0, "bar"});
          fail("Exception is not happened in testMakeInstanceAndRun");
        } catch (VivException e) {
          assertFalse(e.getMessage().isEmpty());
        }
      }

      {
        String code = "function add(a, b) {"
                    + "  return(a + b)"
                    + "}"
                    + "c = [20, false]";
        Viv.Instance instance = Viv.makeInstance(code);
        Integer value =
            Viv.getInteger(instance, "{\"foo\":3, \"bar\": 2}", "return(add(foo, bar))");
        // System.out.println(value);  // 5
        assertEquals(value, 5);
        value = Viv.getInteger(instance, new Object[] {"add", 3, 2});
        // System.out.println(value);  // 5
        assertEquals(value, 5);

        value = Viv.getInteger(instance, "return(c[0])");
        // System.out.println(value);  // 20
        assertEquals(value, 20);
        value = Viv.getInteger(instance, new Object[] {"c", 0});
        // System.out.println(value);  // 20
        assertEquals(value, 20);
      }

      {
        String code = "function add(a, b) {"
                    + "  return(a + b)"
                    + "}";
        Viv.Instance instance = Viv.makeInstance(code);
        Integer value = Viv.getInteger(instance, "return(add(10, 20))");
        // System.out.println(value);  // 30
        assertEquals(value, 30);
        value = Viv.getInteger(instance, new Object[] {"add", 10, 20});
        // System.out.println(value);  // 30
        assertEquals(value, 30);
      }

      {
        Viv.Instance instance = Viv.makeInstance();
        assertEquals(instance.errorMessage, "");
        assertNotNull(instance.evaluator);

        instance = Viv.makeInstance(true);
        assertNotEquals(instance.errorMessage, "");
        assertNull(instance.evaluator);
      }

      {
        Viv.Instance instance = null;
        try {
          instance = Viv.makeInstanceEx("a -- 2");
        } catch (VivException e) {
          assertFalse(e.getMessage().isEmpty());
        }
        assertNull(instance);
      }
  }

  /** Unit-test for "Viv.run" method and infinity/nan */
  @ParameterizedTest
  @CsvSource({
    "Infinity, NaN, '\"a\": {\"x\": Infinity}', 'a.x', positiveInf, float, False",
    "Infinity, NaN, '\"a\": {\"x\": Infinity}', 'a.x + 1', positiveInf, float, False",
    "Infinity, NaN, '\"a\": {\"x\": -Infinity}', 'a.x', negativeInf, float, False",
    "Infinity, NaN, '\"a\": {\"x\": -Infinity}', 'a.x + 1', negativeInf, float, False",
    "Infinity, NaN, '\"a\": {\"x\": NaN}', 'a.x', nan, float, False",
    "Infinity, NaN, '\"a\": {\"x\": NaN}', 'a.x + 1', nan, float, False",
    "'-', '-', '\"a\": {\"x\": Infinity}', 'a.x', null, null, False",
    "'-', '-', '\"a\": {\"x\": Infinity}', 'a.x + 1', 1, int, False",
    "'-', '-', '\"a\": {\"x\": -Infinity}', 'a.x', null, null, False",
    "'-', '-', '\"a\": {\"x\": -Infinity}', 'a.x + 1', 1, int, False",
    "'-', '-', '\"a\": {\"x\": NaN}', 'a.x', null, null, False",
    "'-', '-', '\"a\": {\"x\": NaN}', 'a.x + 1', 1, int, False",
  })
  public void testRunInfNan(String inf, String nan,
                            String preparedExpression, String targetExpression,
                            String expectedValue, String expectedType,
                            boolean isError) {
    Config config = new Config();
    if (!inf.equals("-")) {
      config.setInfinity(inf);
    }
    if (!nan.equals("-")) {
      config.setNaN(nan);
    }

    Double expectedDoubleValue = null;
    if (expectedValue.equals("positiveInf")) {
      expectedDoubleValue = Double.POSITIVE_INFINITY;
    } else if (expectedValue.equals("negativeInf")) {
      expectedDoubleValue = Double.NEGATIVE_INFINITY;
    } else if (expectedValue.equals("nan")) {
      expectedDoubleValue = Double.NaN;
    }

    Result result = Viv.run(preparedExpression, "return(" + targetExpression + ")", config);
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    if (expectedType.equals("float")) {
      assertEquals(result.value, expectedDoubleValue);
    } else if (expectedType.equals("int")) {
      assertEquals(result.value, Long.valueOf(expectedValue));
    } else {
      assertNull(result.value);
    }
    assertEquals(UtilsForTest.getType(result.value), expectedType);
  }

  /** Unit-test for "Viv.runEx" method and infinity/nan with a JSON file */
  @Test
  public void testRunExInfNanWithFile() {
    HashMap<String, @Nullable Object> data = null;
    try {
      data = Viv.getHashMap(Viv.runEx("src/test/inf_nan.json"));
    } catch (VivException e) {
      ;
    }
    assertNull(data);

    Config config = new Config();
    config.setInfinity("Infinity");
    config.setNaN("NaN");
    data = null;
    try {
      data = Viv.getHashMap(Viv.runEx("src/test/inf_nan.json", config));
    } catch (VivException e) {
      fail("Exception happen in testRunExInfNanWithFile");
    }
    assertNotNull(data);
    @Nullable Object value = data.get("normal");
    assertEquals(value, 1.5);
    assertEquals(UtilsForTest.getType(value), "float");
    value = data.get("inf");
    assertEquals(value, Double.POSITIVE_INFINITY);
    assertEquals(UtilsForTest.getType(value), "float");
    value = data.get("negative_inf");
    assertEquals(value, Double.NEGATIVE_INFINITY);
    assertEquals(UtilsForTest.getType(value), "float");
    value = data.get("nan");
    assertEquals(value, Double.NaN);
    assertEquals(UtilsForTest.getType(value), "float");
    value = data.get("str");
    assertEquals(data.get("str"), "1.5");
    assertEquals(UtilsForTest.getType(value), "string");
  }

  @Test
  public void testJson() {
      {
        String text1 = "{\"a\": 5, \"b\": [2, 1]}";
        // Valid as Script's code and JSON's value
        Result result = Viv.run(text1);
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        String value = Viv.makeString(result.value);
        assertTrue(UtilsForTest.isEqualMap(value, text1));
        // Valid as JSON's value
        result = Viv.run(new Viv.Json(text1));
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        value = Viv.makeString(result.value);
        assertTrue(UtilsForTest.isEqualMap(value, text1));

        String text2 = "{\"a\": 3 + 2, \"b\": [2, 1]}";
        // Valid as Script's code and JSON's value
        result = Viv.run(text2);
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        value = Viv.makeString(result.value);
        assertTrue(UtilsForTest.isEqualMap(value, text1));
        // Invalid as JSON's value: 3 + 2
        result = Viv.run(new Viv.Json(text2));
        assertNotNull(result);
        assertFalse(result.errorMessage.isEmpty());  // Error
        assertNull(result.value);  // Result is nothing
      }

      {
        String code = "return(a)";
        String text1 = "{\"a\": 5, \"b\": [2, 1]}";
        // Valid as Script's code and JSON's value
        Result result = Viv.run(text1, code);
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        assertEquals(result.value, 5L);
        // Valid as JSON's value
        result = Viv.run(new Viv.Json(text1), code);
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        assertEquals(result.value, 5L);

        String text2 = "{\"a\": 3 + 2, \"b\": [2, 1]}";
        // Valid as Script's code and JSON's value
        result = Viv.run(text2, code);
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        assertEquals(result.value, 5L);
        // Invalid as JSON's value: 3 + 2
        result = Viv.run(new Viv.Json(text2), code);
        assertNotNull(result);
        assertFalse(result.errorMessage.isEmpty());  // Error
        assertNull(result.value);  // Result is nothing
      }

      {
        String code = "return(_)";
        String text1 = "5";
        // Valid as Script's code and JSON's value
        Result result = Viv.run(text1, code);
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        assertEquals(result.value, 5L);
        // Valid as JSON's value
        result = Viv.run(new Viv.Json(text1), code);
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        assertEquals(result.value, 5L);

        String text2 = "10 / 2";
        // Valid as Script's code and JSON's value
        result = Viv.run(text2, code);
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        assertEquals(result.value, 5L);
        // Invalid as JSON's value: 10 / 2
        result = Viv.run(new Viv.Json(text2), code);
        assertNotNull(result);
        assertFalse(result.errorMessage.isEmpty());  // Error
        assertNull(result.value);  // Result is nothing
      }

      {
        String code = "return(a)";
        String text1 = "{\"a\": Infinity}";
        // *** With configuration ***
        Config config = new Config();
        config.setInfinity("Infinity");
        // Valid as Script's code and JSON's value
        Result result = Viv.run(text1, code, config);
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        assertEquals(result.value, Double.POSITIVE_INFINITY);
        // Valid as JSON's value
        result = Viv.run(new Viv.Json(text1), code, config);
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        assertEquals(result.value, Double.POSITIVE_INFINITY);

        // *** Without configuration ***
        // Valid as Script's code and JSON's value
        // However, Infinity is treated as the undefined variable.
        // So the evaluated result will be null.
        result = Viv.run(text1, code);
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        assertNull(result.value);  // null
        // Invalid as JSON's value because the value of key-value is not
        // number, string, boolean, object, array, or null.
        // The above Infinity is not string because it is not surrounded
        // with quotation marks.
        result = Viv.run(new Viv.Json(text1), code);
        assertNotNull(result);
        assertFalse(result.errorMessage.isEmpty());  // Error
        assertNull(result.value);  // Result is nothing
      }

      {
        String code = "return(_)";
        String text1 = "Infinity";
        // *** With configuration ***
        Config config = new Config();
        config.setInfinity("Infinity");
        // Valid as Script's code and JSON's value
        Result result = Viv.run(text1, code, config);
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        assertEquals(result.value, Double.POSITIVE_INFINITY);
        // Valid as JSON's value
        result = Viv.run(new Viv.Json(text1), code, config);
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        assertEquals(result.value, Double.POSITIVE_INFINITY);

        // *** Without configuration ***
        // Valid as Script's code and JSON's value
        // However, it is treated as String.
        result = Viv.run(text1, code);
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        assertEquals(result.value, text1);
        // Valid as JSON's value
        // However, it is treated as String.
        result = Viv.run(new Viv.Json(text1), code);
        assertNotNull(result);
        assertTrue(result.errorMessage.isEmpty());
        assertEquals(result.value, text1);
      }
  }

  /** Unit-test for "Viv.getBoolean" method */
  @Test
  public void testBoolean() throws VivException {
    Result result = Viv.run("a=2, return(a==2)");
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertEquals(result.value, true);
    assertEquals(Viv.getBoolean(result.value), true);
    assertEquals(Viv.getBoolean(result), true);

    assertEquals(Viv.getBoolean("a=2, return(a==2)"), true);
    assertEquals(Viv.getBoolean("a=2", "return(a==2)"), true);
    assertEquals(Viv.getBoolean(new String[] {"a=2", "return(a==2)"}, null), true);
    assertEquals(Viv.getBoolean(Boolean.valueOf(true)), true);
    assertEquals(Viv.getBoolean(true), true);
    assertEquals(Viv.getBoolean("src/test/a5b7c9.json", "return(a==5)"), true);
    ArrayList<String> texts = new ArrayList<>();
    texts.add("a=2");
    texts.add("return(a==2)");
    assertEquals(Viv.getBoolean(texts), true);
    texts.clear();
    texts.add("src/test/a5b7c9.json");
    texts.add("return(a==5)");
    assertEquals(Viv.getBoolean(texts), true);
    HashMap<String, Boolean> map = new HashMap<>();
    map.put("a", true);
    assertEquals(Viv.getBoolean(map, "return(a)"), true);

    assertEquals(Viv.getBoolean("a=2, return(a!=2)"), false);
    assertEquals(Viv.getBoolean(Boolean.valueOf(false)), false);
    assertEquals(Viv.getBoolean(false), false);

    try {
      assertEquals(Viv.getBooleanEx("a=2, return(a==2)"), true);
    } catch (VivException e) {
      fail("Exception happen in testBoolean");
    }
    try {
      Viv.getBooleanEx("return(2)");
      fail("Exception is not happened in testBoolean");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getBoolean("return(2)"));

      {
        Boolean src = true;
        Object obj = src;
        Boolean value = Viv.getBoolean(obj);
        assertEquals(value, true);
      }

      {
        Object obj = Viv.runEx("a=2", "return(a==2)");
        Boolean value = Viv.getBoolean(obj);
        assertEquals(value, true);
      }

      {
        Boolean value = Viv.getBoolean("a=2", "return(a==2)");
        assertEquals(value, true);
      }

      {
        String data = "{\"foo\": 10, \"bar\": 30, \"baz\": 20}";
        Boolean value = Viv.getBoolean(data, "return('foo' in .)");
        // System.out.println(value);  // true
        assertTrue(value);
        value = Viv.getBoolean(data, "return('qux' in .)");
        // System.out.println(value);  // false
        assertFalse(value);
      }
  }

  /** Unit-test for "Viv.getBooleans" method */
  @Test
  public void testBooleans() {
    ArrayList<Object> array = new ArrayList<>();
    array.add(true);
    array.add(false);
    array.add(true);
    boolean[] booleans = Viv.getBooleans(array);
    assertNotNull(booleans);
    assertEquals(booleans.length, 3);
    assertTrue(booleans[0]);
    assertFalse(booleans[1]);
    assertTrue(booleans[2]);

    array.set(1, 0);
    booleans = Viv.getBooleans(array);
    assertNull(booleans);

    HashMap<String, Object> block = new HashMap<>();
    block.put("a", true);
    block.put("b", false);
    booleans = Viv.getBooleans(block);
    assertNotNull(booleans);
    assertEquals(booleans.length, 2);
    assertTrue(booleans[0]);
    assertFalse(booleans[1]);

    booleans = Viv.getBooleans("{\"a\": true, \"b\": false}");
    assertNotNull(booleans);
    assertEquals(booleans.length, 2);
    assertTrue(booleans[0]);
    assertFalse(booleans[1]);

    booleans = Viv.getBooleans("return([true, false])");
    assertNotNull(booleans);
    assertEquals(booleans.length, 2);
    assertTrue(booleans[0]);
    assertFalse(booleans[1]);

    booleans = Viv.getBooleans("return([true, 1])");
    assertNull(booleans);

    booleans = Viv.getBooleans("return([1])");
    assertNull(booleans);

    booleans = Viv.getBooleans("return([false])");
    assertNotNull(booleans);
    assertEquals(booleans.length, 1);
    assertFalse(booleans[0]);

    Result result = Viv.run("{\"a\": true, \"b\": false}");
    booleans = Viv.getBooleans(result);
    assertNotNull(booleans);
    assertEquals(booleans.length, 2);
    assertTrue(booleans[0]);
    assertFalse(booleans[1]);

    result = Viv.run("return([true, false])");
    booleans = Viv.getBooleans(result);
    assertNotNull(booleans);
    assertEquals(booleans.length, 2);
    assertTrue(booleans[0]);
    assertFalse(booleans[1]);

    try {
      booleans = Viv.getBooleansEx("return([true, false])");
      assertNotNull(booleans);
      assertEquals(booleans.length, 2);
      assertTrue(booleans[0]);
      assertFalse(booleans[1]);
    } catch (VivException e) {
      fail("Exception happen in testBooleans");
    }
    try {
      Viv.getBooleansEx("return([1])");
      fail("Exception is not happened in testBooleans");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getBooleans("a = "));
  }

  /** Unit-test for "Viv.getBooleanOrNulls" method */
  @Test
  public void testBooleanOrNulls() {
    ArrayList<@Nullable Object> array = new ArrayList<>();
    array.add(true);
    array.add(null);
    array.add(false);
    Boolean[] booleans = Viv.getBooleanOrNulls(array);
    assertNotNull(booleans);
    assertEquals(booleans.length, 3);
    assertTrue(booleans[0]);
    assertNull(booleans[1]);
    assertFalse(booleans[2]);

    array.set(1, 0);
    booleans = Viv.getBooleanOrNulls(array);
    assertNotNull(booleans);
    assertEquals(booleans.length, 3);
    assertTrue(booleans[0]);
    assertNull(booleans[1]);
    assertFalse(booleans[2]);

    HashMap<String, Object> block = new HashMap<>();
    block.put("a", true);
    block.put("b", false);
    booleans = Viv.getBooleanOrNulls(block);
    assertNotNull(booleans);
    assertEquals(booleans.length, 2);
    assertTrue(booleans[0]);
    assertFalse(booleans[1]);

    booleans = Viv.getBooleanOrNulls("{\"a\": true, \"b\": false}");
    assertNotNull(booleans);
    assertEquals(booleans.length, 2);
    assertTrue(booleans[0]);
    assertFalse(booleans[1]);

    booleans = Viv.getBooleanOrNulls("return([true, false])");
    assertNotNull(booleans);
    assertEquals(booleans.length, 2);
    assertTrue(booleans[0]);
    assertFalse(booleans[1]);

    booleans = Viv.getBooleanOrNulls("return([true, 1])");
    assertNotNull(booleans);
    assertEquals(booleans.length, 2);
    assertTrue(booleans[0]);
    assertNull(booleans[1]);

    booleans = Viv.getBooleanOrNulls("return([1])");
    assertNotNull(booleans);
    assertEquals(booleans.length, 1);
    assertNull(booleans[0]);

    booleans = Viv.getBooleanOrNulls("return([false])");
    assertNotNull(booleans);
    assertEquals(booleans.length, 1);
    assertFalse(booleans[0]);

    Result result = Viv.run("{\"a\": true, \"b\": false}");
    booleans = Viv.getBooleanOrNulls(result);
    assertNotNull(booleans);
    assertEquals(booleans.length, 2);
    assertTrue(booleans[0]);
    assertFalse(booleans[1]);

    result = Viv.run("return([true, false])");
    booleans = Viv.getBooleanOrNulls(result);
    assertNotNull(booleans);
    assertEquals(booleans.length, 2);
    assertTrue(booleans[0]);
    assertFalse(booleans[1]);

    try {
      booleans = Viv.getBooleanOrNullsEx("return([true, false])");
      assertNotNull(booleans);
      assertEquals(booleans.length, 2);
      assertTrue(booleans[0]);
      assertFalse(booleans[1]);
    } catch (VivException e) {
      fail("Exception happen in testBooleanOrNulls");
    }
    try {
      Viv.getBooleanOrNullsEx("a =");
      fail("Exception is not happened in testBooleanOrNulls");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getBooleanOrNulls("a = "));
  }

  /** Unit-test for "Viv.getInteger" method */
  @Test
  public void testInteger() {
    Result result = Viv.run("return(2)");
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertEquals(result.value, 2L);  // Long
    assertEquals(Viv.getInteger(result.value), 2);
    assertEquals(Viv.getInteger(result), 2);

    assertEquals(Viv.getInteger("a=2, return(a)"), 2);
    assertEquals(Viv.getInteger("a=2", "return(a)"), 2);
    assertEquals(Viv.getInteger(new String[] {"a=2", "return(a)"}, null), 2);
    assertEquals(Viv.getInteger(Integer.valueOf(2)), 2);
    assertEquals(Viv.getInteger(2), 2);
    assertEquals(Viv.getInteger("src/test/a5b7c9.json", "return(a)"), 5);

    ArrayList<String> texts = new ArrayList<>();
    texts.add("a=2");
    texts.add("return(a)");
    assertEquals(Viv.getInteger(texts), 2);
    texts.clear();
    texts.add("src/test/a5b7c9.json");
    texts.add("return(b)");
    assertEquals(Viv.getInteger(texts), 7);

    HashMap<String, Integer> map1 = new HashMap<>();
    map1.put("a", 10);
    assertEquals(Viv.getInteger(map1, "return(a)"), 10);

    HashMap<String, Double> map2 = new HashMap<>();
    map2.put("a", 1.5);
    assertEquals(Viv.getInteger(map2, "return(a)"), 1);

    assertNull(Viv.getInteger("true"));
    assertNull(Viv.getInteger("return('1')"));

    Integer value =
        Viv.getInteger("{\"foo\": 10, \"bar\": 30, \"baz\": 20}", "return(qux)");
    // System.out.println(value);  // null
    assertNull(value);

    try {
      assertEquals(Viv.getIntegerEx("return(2)"), 2);
    } catch (VivException e) {
      fail("Exception happen in testInteger");
    }
    try {
      Viv.getIntegerEx("return(true)");
      fail("Exception is not happened in testInteger");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getInteger("return(true)"));
  }

  /** Unit-test for "Viv.getIntegers" method */
  @Test
  public void testIntegers() {
    ArrayList<Object> array = new ArrayList<>();
    array.add(3);
    array.add(5);
    array.add(7);
    int[] numbers = Viv.getIntegers(array);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 3);
    assertEquals(numbers[1], 5);
    assertEquals(numbers[2], 7);

    array.set(1, false);
    numbers = Viv.getIntegers(array);
    assertNull(numbers);

    HashMap<String, Object> block = new HashMap<>();
    block.put("a", 8);
    block.put("b", 9);
    numbers = Viv.getIntegers(block);
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 8);
    assertEquals(numbers[1], 9);

    numbers = Viv.getIntegers("{\"a\": 1, \"b\": 2}");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 1);
    assertEquals(numbers[1], 2);

    numbers = Viv.getIntegers("return([-100, 0])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], -100);
    assertEquals(numbers[1], 0);

    numbers = Viv.getIntegers("return([true, 0])");
    assertNull(numbers);

    numbers = Viv.getIntegers("return([true])");
    assertNull(numbers);

    numbers = Viv.getIntegers("return([0])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 1);
    assertEquals(numbers[0], 0);

    Result result = Viv.run("{\"a\": 1.5, \"b\": -2.7, \"c\": int(-2.7)}");
    numbers = Viv.getIntegers(result);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 1);
    assertEquals(numbers[1], -2);
    assertEquals(numbers[2], -2);

    result = Viv.run("return([0.00001, 1])");
    numbers = Viv.getIntegers(result);
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 0);
    assertEquals(numbers[1], 1);

    try {
      numbers = Viv.getIntegersEx("return([0.00001, 1])");
      assertNotNull(numbers);
      assertEquals(numbers.length, 2);
      assertEquals(numbers[0], 0);
      assertEquals(numbers[1], 1);
    } catch (VivException e) {
      fail("Exception happen in testIntegers");
    }
    try {
      Viv.getIntegersEx("return([true])");
      fail("Exception is not happened in testIntegers");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getIntegers("a = "));
  }

  /** Unit-test for "Viv.getIntegerOrNulls" method */
  @Test
  public void testIntegerOrNulls() {
    ArrayList<Object> array = new ArrayList<>();
    array.add(3);
    array.add(5);
    array.add(7);
    Integer[] numbers = Viv.getIntegerOrNulls(array);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 3);
    assertEquals(numbers[1], 5);
    assertEquals(numbers[2], 7);

    array.set(1, false);
    numbers = Viv.getIntegerOrNulls(array);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 3);
    assertNull(numbers[1]);
    assertEquals(numbers[2], 7);

    HashMap<String, Object> block = new HashMap<>();
    block.put("a", 8);
    block.put("b", false);
    numbers = Viv.getIntegerOrNulls(block);
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 8);
    assertNull(numbers[1]);

    numbers = Viv.getIntegerOrNulls("{\"a\": 1, \"b\": false}");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 1);
    assertNull(numbers[1]);

    numbers = Viv.getIntegerOrNulls("return([-100, false])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], -100);
    assertNull(numbers[1]);

    numbers = Viv.getIntegerOrNulls("return([true, 0])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertNull(numbers[0]);
    assertEquals(numbers[1], 0);

    numbers = Viv.getIntegerOrNulls("return([true])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 1);
    assertNull(numbers[0]);
 
    numbers = Viv.getIntegerOrNulls("return([0])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 1);
    assertEquals(numbers[0], 0);
 
    Result result = Viv.run("{\"a\": 1.5, \"b\": -2.7, \"c\": \"alpha\"}");
    numbers = Viv.getIntegerOrNulls(result);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 1);
    assertEquals(numbers[1], -2);
    assertNull(numbers[2]);

    result = Viv.run("return([0.00001, null])");
    numbers = Viv.getIntegerOrNulls(result);
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 0);
    assertNull(numbers[1]);

    try {
      numbers = Viv.getIntegerOrNullsEx("return([0.00001, null])");
      assertNotNull(numbers);
      assertEquals(numbers.length, 2);
      assertEquals(numbers[0], 0);
      assertNull(numbers[1]);
    } catch (VivException e) {
      fail("Exception happen in testIntegerOrNulls");
    }
    try {
      Viv.getIntegerOrNullsEx("a =");
      fail("Exception is not happened in testIntegerOrNulls");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getIntegerOrNulls("a = "));
  }

  /** Unit-test for "Viv.getLong" method */
  @Test
  public void testLong() {
    Result result = Viv.run("return(2)");
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertEquals(result.value, 2L);
    assertEquals(Viv.getLong(result.value), 2L);
    assertEquals(Viv.getLong(result), 2L);

    assertEquals(Viv.getLong("a=2, return(a)"), 2L);
    assertEquals(Viv.getLong("a=2", "return(a)"), 2L);
    assertEquals(Viv.getLong(new String[] {"a=2", "return(a)"}, null), 2L);
    assertEquals(Viv.getLong(Long.valueOf(2)), 2L);
    assertEquals(Viv.getLong(2), 2L);
    assertEquals(Viv.getLong(2L), 2L);
    assertEquals(Viv.getLong("src/test/a5b7c9.json", "return(a)"), 5L);

    ArrayList<String> texts = new ArrayList<>();
    texts.add("a=2");
    texts.add("return(a)");
    assertEquals(Viv.getLong(texts), 2L);
    texts.clear();
    texts.add("src/test/a5b7c9.json");
    texts.add("return(b)");
    assertEquals(Viv.getLong(texts), 7L);

    HashMap<String, Long> map1 = new HashMap<>();
    map1.put("a", 10L);
    assertEquals(Viv.getLong(map1, "return(a)"), 10L);

    HashMap<String, Double> map2 = new HashMap<>();
    map2.put("a", 1.5);
    assertEquals(Viv.getLong(map2, "return(a)"), 1L);

    assertNull(Viv.getLong("true"));
    assertNull(Viv.getLong("return('1')"));

    try {
      assertEquals(Viv.getLongEx("return(2)"), 2L);
    } catch (VivException e) {
      fail("Exception happen in testLong");
    }
    try {
      Viv.getLongEx("return(true)");
      fail("Exception is not happened in testLong");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getLong("return(true)"));
  }

  /** Unit-test for "Viv.getLongs" method */
  @Test
  public void testLongs() {
    ArrayList<Object> array = new ArrayList<>();
    array.add(3);
    array.add(5);
    array.add(7);
    long[] numbers = Viv.getLongs(array);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 3L);
    assertEquals(numbers[1], 5L);
    assertEquals(numbers[2], 7L);

    array.set(1, false);
    numbers = Viv.getLongs(array);
    assertNull(numbers);

    HashMap<String, Object> block = new HashMap<>();
    block.put("a", 8);
    block.put("b", 9);
    numbers = Viv.getLongs(block);
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 8L);
    assertEquals(numbers[1], 9L);

    numbers = Viv.getLongs("{\"a\": 1, \"b\": 2}");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 1L);
    assertEquals(numbers[1], 2L);

    numbers = Viv.getLongs("return([-100, 0])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], -100L);
    assertEquals(numbers[1], 0L);

    numbers = Viv.getLongs("return([true, 0])");
    assertNull(numbers);

    numbers = Viv.getLongs("return([true])");
    assertNull(numbers);

    numbers = Viv.getLongs("return([0])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 1);
    assertEquals(numbers[0], 0L);

    Result result = Viv.run("{\"a\": 1.5, \"b\": -2.7, \"c\": int(-2.7)}");
    numbers = Viv.getLongs(result);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 1L);
    assertEquals(numbers[1], -2L);
    assertEquals(numbers[2], -2L);

    result = Viv.run("return([0.00001, 1])");
    numbers = Viv.getLongs(result);
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 0L);
    assertEquals(numbers[1], 1L);

    try {
      numbers = Viv.getLongsEx("return([0.00001, 1])");
      assertNotNull(numbers);
      assertEquals(numbers.length, 2);
      assertEquals(numbers[0], 0L);
      assertEquals(numbers[1], 1L);
    } catch (VivException e) {
      fail("Exception happen in testLongs");
    }
    try {
      Viv.getLongsEx("return([true])");
      fail("Exception is not happened in testLongs");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getLongs("a = "));
  }

  /** Unit-test for "Viv.getLongOrNulls" method */
  @Test
  public void testLongOrNulls() {
    ArrayList<Object> array = new ArrayList<>();
    array.add(3L);
    array.add(5L);
    array.add(7L);
    Long[] numbers = Viv.getLongOrNulls(array);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 3L);
    assertEquals(numbers[1], 5L);
    assertEquals(numbers[2], 7L);

    array.set(1, false);
    numbers = Viv.getLongOrNulls(array);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 3L);
    assertNull(numbers[1]);
    assertEquals(numbers[2], 7L);

    HashMap<String, Object> block = new HashMap<>();
    block.put("a", 8);
    block.put("b", false);
    numbers = Viv.getLongOrNulls(block);
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 8L);
    assertNull(numbers[1]);

    numbers = Viv.getLongOrNulls("{\"a\": 1, \"b\": false}");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 1L);
    assertNull(numbers[1]);

    numbers = Viv.getLongOrNulls("return([-100, false])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], -100L);
    assertNull(numbers[1]);

    numbers = Viv.getLongOrNulls("return([true, 0])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertNull(numbers[0]);
    assertEquals(numbers[1], 0L);

    numbers = Viv.getLongOrNulls("return([true])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 1);
    assertNull(numbers[0]);
 
    numbers = Viv.getLongOrNulls("return([0])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 1);
    assertEquals(numbers[0], 0L);
 
    Result result = Viv.run("{\"a\": 1.5, \"b\": -2.7, \"c\": \"alpha\"}");
    numbers = Viv.getLongOrNulls(result);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 1L);
    assertEquals(numbers[1], -2L);
    assertNull(numbers[2]);

    result = Viv.run("return([0.00001, null])");
    numbers = Viv.getLongOrNulls(result);
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 0L);
    assertNull(numbers[1]);

    try {
      numbers = Viv.getLongOrNullsEx("return([0.00001, null])");
      assertNotNull(numbers);
      assertEquals(numbers.length, 2);
      assertEquals(numbers[0], 0L);
      assertNull(numbers[1]);
    } catch (VivException e) {
      fail("Exception happen in testLongOrNulls");
    }
    try {
      Viv.getLongOrNullsEx("a =");
      fail("Exception is not happened in testLongOrNulls");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getLongOrNulls("a = "));
  }

  /** Unit-test for "Viv.getFloat" method */
  @Test
  public void testFloat() {
    Result result = Viv.run("return(2.0)");
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertEquals(result.value, 2.0);  // Double
    assertEquals(Viv.getFloat(result.value), 2.0F);
    assertEquals(Viv.getFloat(result), 2.0F);

    assertEquals(Viv.getFloat("a=2, return(a)"), 2.0F);
    assertEquals(Viv.getFloat("a=2", "return(a)"), 2.0F);
    assertEquals(Viv.getFloat(new String[] {"a=2", "return(a)"}, null), 2.0F);
    assertEquals(Viv.getFloat(Float.valueOf(2)), 2.0F);
    assertEquals(Viv.getFloat(2.0), 2.0F);
    assertEquals(Viv.getFloat(2.0F), 2.0F);
    assertEquals(Viv.getFloat("src/test/a5b7c9.json", "return(a)"), 5.0F);

    ArrayList<String> texts = new ArrayList<>();
    texts.add("a=2.5");
    texts.add("return(a)");
    assertEquals(Viv.getFloat(texts), 2.5F);
    texts.clear();
    texts.add("src/test/a5b7c9.json");
    texts.add("return(b)");
    assertEquals(Viv.getFloat(texts), 7.0F);

    HashMap<String, Float> map1 = new HashMap<>();
    map1.put("a", 1F);
    assertEquals(Viv.getFloat(map1, "return(a)"), 1.0F);

    HashMap<String, Integer> map2 = new HashMap<>();
    map2.put("a", 10);
    assertEquals(Viv.getFloat(map2, "return(a)"), 10.0F);

    assertNull(Viv.getFloat("true"));
    assertNull(Viv.getFloat("return('1')"));

    try {
      assertEquals(Viv.getFloatEx("return(2)"), 2.0F);
    } catch (VivException e) {
      fail("Exception happen in testFloat");
    }
    try {
      Viv.getFloatEx("return(true)");
      fail("Exception is not happened in testFloat");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getFloat("return(true)"));
  }

  /** Unit-test for "Viv.getFloats" method */
  @Test
  public void testFloats() {
    ArrayList<Object> array = new ArrayList<>();
    array.add(3.0F);
    array.add(5.0F);
    array.add(7.0F);
    float[] numbers = Viv.getFloats(array);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 3.0F);
    assertEquals(numbers[1], 5.0F);
    assertEquals(numbers[2], 7.0F);

    array.set(1, false);
    numbers = Viv.getFloats(array);
    assertNull(numbers);

    HashMap<String, Object> block = new HashMap<>();
    block.put("a", 8.0F);
    block.put("b", 9.0F);
    numbers = Viv.getFloats(block);
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 8.0F);
    assertEquals(numbers[1], 9.0F);

    numbers = Viv.getFloats("{\"a\": 1, \"b\": 2.5}");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 1.0F);
    assertEquals(numbers[1], 2.5F);

    numbers = Viv.getFloats("return([-100.2, 0])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], -100.2F);
    assertEquals(numbers[1], 0F);

    numbers = Viv.getFloats("return([true, 0])");
    assertNull(numbers);

    numbers = Viv.getFloats("return([true])");
    assertNull(numbers);

    numbers = Viv.getFloats("return([0])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 1);
    assertEquals(numbers[0], 0.0F);

    Result result = Viv.run("{\"a\": 1.5, \"b\": -2.7, \"c\": float(-2.7)}");
    numbers = Viv.getFloats(result);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 1.5F);
    assertEquals(numbers[1], -2.7F);
    assertEquals(numbers[2], -2.7F);

    result = Viv.run("return([0.00001, 1])");
    numbers = Viv.getFloats(result);
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 0.00001F);
    assertEquals(numbers[1], 1F);

    try {
      numbers = Viv.getFloatsEx("return([0.00001, 1])");
      assertNotNull(numbers);
      assertEquals(numbers.length, 2);
      assertEquals(numbers[0], 0.00001F);
      assertEquals(numbers[1], 1F);
    } catch (VivException e) {
      fail("Exception happen in testFloats");
    }
    try {
      Viv.getFloatsEx("return([true])");
      fail("Exception is not happened in testFloats");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getFloats("a = "));
  }

  /** Unit-test for "Viv.getFloatOrNulls" method */
  @Test
  public void testFloatOrNulls() {
    ArrayList<Object> array = new ArrayList<>();
    array.add(3);
    array.add(5.0F);
    array.add(7.0F);
    Float[] numbers = Viv.getFloatOrNulls(array);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 3.0F);
    assertEquals(numbers[1], 5.0F);
    assertEquals(numbers[2], 7.0F);

    array.set(1, false);
    numbers = Viv.getFloatOrNulls(array);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 3.0F);
    assertNull(numbers[1]);
    assertEquals(numbers[2], 7.0F);

    HashMap<String, Object> block = new HashMap<>();
    block.put("a", 8.0F);
    block.put("b", false);
    numbers = Viv.getFloatOrNulls(block);
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 8.0F);
    assertNull(numbers[1]);

    numbers = Viv.getFloatOrNulls("{\"a\": 1, \"b\": false}");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 1.0F);
    assertNull(numbers[1]);

    numbers = Viv.getFloatOrNulls("return([-100.0, false])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], -100.0F);
    assertNull(numbers[1]);

    numbers = Viv.getFloatOrNulls("return([true, 0.0])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertNull(numbers[0]);
    assertEquals(numbers[1], 0.0F);

    numbers = Viv.getFloatOrNulls("return([true])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 1);
    assertNull(numbers[0]);
 
    numbers = Viv.getFloatOrNulls("return([0.0])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 1);
    assertEquals(numbers[0], 0.0F);
 
    Result result = Viv.run("{\"a\": 1.5, \"b\": -2.7, \"c\": \"alpha\"}");
    numbers = Viv.getFloatOrNulls(result);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 1.5F);
    assertEquals(numbers[1], -2.7F);
    assertNull(numbers[2]);

    result = Viv.run("return([0.00001, null])");
    numbers = Viv.getFloatOrNulls(result);
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 0.00001F);
    assertNull(numbers[1]);

    try {
      numbers = Viv.getFloatOrNullsEx("return([0.00001, null])");
      assertNotNull(numbers);
      assertEquals(numbers.length, 2);
      assertEquals(numbers[0], 0.00001F);
      assertNull(numbers[1]);
    } catch (VivException e) {
      fail("Exception happen in testFloatOrNulls");
    }
    try {
      Viv.getFloatOrNullsEx("a =");
      fail("Exception is not happened in testFloatOrNulls");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getFloatOrNulls("a = "));
  }

  /** Unit-test for "Viv.getDouble" method */
  @Test
  public void testDouble() {
    Result result = Viv.run("return(2.0)");
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertEquals(result.value, 2.0);
    assertEquals(Viv.getDouble(result.value), 2.0);
    assertEquals(Viv.getDouble(result), 2.0);

    assertEquals(Viv.getDouble("a=2, return(a)"), 2.0);
    assertEquals(Viv.getDouble("a=2", "return(a)"), 2.0);
    assertEquals(Viv.getDouble(new String[] {"a=2", "return(a)"}, null), 2.0);
    assertEquals(Viv.getDouble(Double.valueOf(2)), 2.0);
    assertEquals(Viv.getDouble(2.0), 2.0);
    assertEquals(Viv.getDouble(2.0F), 2.0);
    assertEquals(Viv.getDouble("src/test/a5b7c9.json", "return(a)"), 5.0);

    ArrayList<String> texts = new ArrayList<>();
    texts.add("a=2.5");
    texts.add("return(a)");
    assertEquals(Viv.getDouble(texts), 2.5);
    texts.clear();
    texts.add("src/test/a5b7c9.json");
    texts.add("return(b)");
    assertEquals(Viv.getDouble(texts), 7.0);

    HashMap<String, Double> map1 = new HashMap<>();
    map1.put("a", 1.0);
    assertEquals(Viv.getDouble(map1, "return(a)"), 1.0);

    HashMap<String, Integer> map2 = new HashMap<>();
    map2.put("a", 10);
    assertEquals(Viv.getDouble(map2, "return(a)"), 10.0);

    assertNull(Viv.getDouble("true"));
    assertNull(Viv.getDouble("return('1')"));

    try {
      assertEquals(Viv.getDoubleEx("return(2)"), 2.0);
    } catch (VivException e) {
      fail("Exception happen in testDouble");
    }
    try {
      Viv.getDoubleEx("return(true)");
      fail("Exception is not happened in testDouble");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getDouble("return(true)"));
  }

  /** Unit-test for "Viv.getDoubles" method */
  @Test
  public void testDoubles() {
    ArrayList<Object> array = new ArrayList<>();
    array.add(3.0);
    array.add(5.0);
    array.add(7.0);
    double[] numbers = Viv.getDoubles(array);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 3.0);
    assertEquals(numbers[1], 5.0);
    assertEquals(numbers[2], 7.0);

    array.set(1, false);
    numbers = Viv.getDoubles(array);
    assertNull(numbers);

    HashMap<String, Object> block = new HashMap<>();
    block.put("a", 8.0);
    block.put("b", 9.0);
    numbers = Viv.getDoubles(block);
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 8.0);
    assertEquals(numbers[1], 9.0);

    numbers = Viv.getDoubles("{\"a\": 1, \"b\": 2.5}");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 1.0);
    assertEquals(numbers[1], 2.5);

    numbers = Viv.getDoubles("return([-100.2, 0])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], -100.2);
    assertEquals(numbers[1], 0);

    numbers = Viv.getDoubles("return([true, 0])");
    assertNull(numbers);

    numbers = Viv.getDoubles("return([true])");
    assertNull(numbers);

    numbers = Viv.getDoubles("return([0])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 1);
    assertEquals(numbers[0], 0.0F);

    Result result = Viv.run("{\"a\": 1.5, \"b\": -2.7, \"c\": float(-2.7)}");
    numbers = Viv.getDoubles(result);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 1.5);
    assertEquals(numbers[1], -2.7);
    assertEquals(numbers[2], -2.7);

    result = Viv.run("return([0.00001, 1])");
    numbers = Viv.getDoubles(result);
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 0.00001);
    assertEquals(numbers[1], 1);

    try {
      numbers = Viv.getDoublesEx("return([0.00001, 1])");
      assertNotNull(numbers);
      assertEquals(numbers.length, 2);
      assertEquals(numbers[0], 0.00001);
      assertEquals(numbers[1], 1);
    } catch (VivException e) {
      fail("Exception happen in testDoubles");
    }
    try {
      Viv.getDoublesEx("return([true])");
      fail("Exception is not happened in testDoubles");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getDoubles("a = "));
  }

  /** Unit-test for "Viv.getDoubleOrNulls" method */
  @Test
  public void testDoubleOrNulls() {
    ArrayList<Object> array = new ArrayList<>();
    array.add(3);
    array.add(5.0F);
    array.add(7.0);
    Double[] numbers = Viv.getDoubleOrNulls(array);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 3.0);
    assertEquals(numbers[1], 5.0);
    assertEquals(numbers[2], 7.0);

    array.set(1, false);
    numbers = Viv.getDoubleOrNulls(array);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 3.0);
    assertNull(numbers[1]);
    assertEquals(numbers[2], 7.0);

    HashMap<String, Object> block = new HashMap<>();
    block.put("a", 8.0);
    block.put("b", false);
    numbers = Viv.getDoubleOrNulls(block);
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 8.0);
    assertNull(numbers[1]);

    numbers = Viv.getDoubleOrNulls("{\"a\": 1, \"b\": false}");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 1.0);
    assertNull(numbers[1]);

    numbers = Viv.getDoubleOrNulls("return([-100.0, false])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], -100.0);
    assertNull(numbers[1]);

    numbers = Viv.getDoubleOrNulls("return([true, 0.0])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertNull(numbers[0]);
    assertEquals(numbers[1], 0.0);

    numbers = Viv.getDoubleOrNulls("return([true])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 1);
    assertNull(numbers[0]);
 
    numbers = Viv.getDoubleOrNulls("return([0.0])");
    assertNotNull(numbers);
    assertEquals(numbers.length, 1);
    assertEquals(numbers[0], 0.0);
 
    Result result = Viv.run("{\"a\": 1.5, \"b\": -2.7, \"c\": \"alpha\"}");
    numbers = Viv.getDoubleOrNulls(result);
    assertNotNull(numbers);
    assertEquals(numbers.length, 3);
    assertEquals(numbers[0], 1.5);
    assertEquals(numbers[1], -2.7);
    assertNull(numbers[2]);

    result = Viv.run("return([0.00001, null])");
    numbers = Viv.getDoubleOrNulls(result);
    assertNotNull(numbers);
    assertEquals(numbers.length, 2);
    assertEquals(numbers[0], 0.00001);
    assertNull(numbers[1]);

    try {
      numbers = Viv.getDoubleOrNullsEx("return([0.00001, null])");
      assertNotNull(numbers);
      assertEquals(numbers.length, 2);
      assertEquals(numbers[0], 0.00001);
      assertNull(numbers[1]);
    } catch (VivException e) {
      fail("Exception happen in testDoubleOrNulls");
    }
    try {
      Viv.getDoubleOrNullsEx("a =");
      fail("Exception is not happened in testDoubleOrNulls");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getDoubleOrNulls("a = "));
  }

  /** Unit-test for "Viv.getString" method */
  @Test
  public void testString() {
    Result result = Viv.run("return('2.0')");
    assertNotNull(result);
    assertTrue(result.errorMessage.isEmpty());
    assertEquals(result.value, "2.0");
    assertEquals(Viv.getString(result.value), "2.0");
    assertEquals(Viv.getString(result), "2.0");

    assertEquals(Viv.getString("return('test')"), "return('test')");  // pass through
    assertEquals(Viv.getString("a='test', return(a)"), "a='test', return(a)");  // pass through
    assertEquals(Viv.getString("a='test'", "return(a)"), "test");
    assertEquals(Viv.getString(new String[] {"a='test'", "return(a)"}, null), "test");
    assertEquals(Viv.getString(String.valueOf("test")), "test");
    assertEquals(Viv.getString("test"), "test");
    assertEquals(Viv.getString("src/test/a5b7c9.json", "return(string(a))"), "5");

    ArrayList<String> texts = new ArrayList<>();
    texts.add("a='test'");
    texts.add("return(a)");
    assertEquals(Viv.getString(texts), "test");
    texts.clear();
    texts.add("src/test/a5b7c9.json");
    texts.add("return(string(b))");
    assertEquals(Viv.getString(texts), "7");

    HashMap<String, String> map1 = new HashMap<>();
    map1.put("a", "test");
    assertEquals(Viv.getString(map1, "return(a)"), "test");

    HashMap<String, Integer> map2 = new HashMap<>();
    map2.put("a", 10);
    assertEquals(Viv.getString(map2, "return(string(a))"), "10");

    assertNull(Viv.getString(true));
    assertEquals(Viv.getString("return(1)"), "return(1)");  // pass through
    assertNull(Viv.getString("a=1", "return(a)"));

    try {
      assertEquals(Viv.getStringEx("a='test'", "return(a)"), "test");
    } catch (VivException e) {
      fail("Exception happen in testString");
    }
    try {
      Viv.getStringEx("a=true", "return(a)");
      fail("Exception is not happened in testString");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getString("a=true", "return(a)"));
  }

  /** Unit-test for "Viv.getStrings" method */
  @Test
  public void testStrings() {
    ArrayList<Object> array = new ArrayList<>();
    array.add("alpha");
    array.add("beta");
    array.add("gamma");
    String[] texts = Viv.getStrings(array);
    assertNotNull(texts);
    assertEquals(texts.length, 3);
    assertEquals(texts[0], "alpha");
    assertEquals(texts[1], "beta");
    assertEquals(texts[2], "gamma");

    array.set(1, false);
    texts = Viv.getStrings(array);
    assertNull(texts);

    HashMap<String, Object> block = new HashMap<>();
    block.put("a", "alpha");
    block.put("b", "beta");
    texts = Viv.getStrings(block);
    assertNotNull(texts);
    assertEquals(texts.length, 2);
    assertEquals(texts[0], "alpha");
    assertEquals(texts[1], "beta");

    texts = Viv.getStrings("{\"a\": \"alpha\", \"b\": \"beta\"}");
    assertNotNull(texts);
    assertEquals(texts.length, 2);
    assertEquals(texts[0], "alpha");
    assertEquals(texts[1], "beta");

    texts = Viv.getStrings("return([\"alpha\", \"beta\"])");
    assertNotNull(texts);
    assertEquals(texts.length, 2);
    assertEquals(texts[0], "alpha");
    assertEquals(texts[1], "beta");

    texts = Viv.getStrings("return([true, \"alpha\"])");
    assertNull(texts);

    texts = Viv.getStrings("return([true])");
    assertNull(texts);

    texts = Viv.getStrings("return([\"alpha\"])");
    assertNotNull(texts);
    assertEquals(texts.length, 1);
    assertEquals(texts[0], "alpha");

    Result result = Viv.run("{\"a\": \"alpha\", \"b\": \"beta\", \"c\": string(-2.7)}");
    texts = Viv.getStrings(result);
    assertNotNull(texts);
    assertEquals(texts.length, 3);
    assertEquals(texts[0], "alpha");
    assertEquals(texts[1], "beta");
    assertEquals(texts[2], "-2.7");

    result = Viv.run("return([\"alpha\", \"beta\"])");
    texts = Viv.getStrings(result);
    assertNotNull(texts);
    assertEquals(texts.length, 2);
    assertEquals(texts[0], "alpha");
    assertEquals(texts[1], "beta");

    try {
      texts = Viv.getStringsEx("return([\"alpha\", \"beta\"])");
      assertNotNull(texts);
      assertEquals(texts.length, 2);
      assertEquals(texts[0], "alpha");
      assertEquals(texts[1], "beta");
    } catch (VivException e) {
      fail("Exception happen in testStrings");
    }
    try {
      Viv.getStringsEx("return([true])");
      fail("Exception is not happened in testStrings");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getStrings("a = "));
  }

  /** Unit-test for "Viv.getStringOrNulls" method */
  @Test
  public void testStringOrNulls() {
    ArrayList<Object> array = new ArrayList<>();
    array.add("alpha");
    array.add("beta");
    array.add("gamma");
    String[] texts = Viv.getStringOrNulls(array);
    assertNotNull(texts);
    assertEquals(texts.length, 3);
    assertEquals(texts[0], "alpha");
    assertEquals(texts[1], "beta");
    assertEquals(texts[2], "gamma");

    array.set(1, false);
    texts = Viv.getStringOrNulls(array);
    assertNotNull(texts);
    assertEquals(texts.length, 3);
    assertEquals(texts[0], "alpha");
    assertNull(texts[1]);
    assertEquals(texts[2], "gamma");


    HashMap<String, Object> block = new HashMap<>();
    block.put("a", "alpha");
    block.put("b", false);
    texts = Viv.getStringOrNulls(block);
    assertNotNull(texts);
    assertEquals(texts.length, 2);
    assertEquals(texts[0], "alpha");
    assertNull(texts[1]);

    texts = Viv.getStringOrNulls("{\"a\": \"alpha\", \"b\": false}");
    assertNotNull(texts);
    assertEquals(texts.length, 2);
    assertEquals(texts[0], "alpha");
    assertNull(texts[1]);

    texts = Viv.getStringOrNulls("return([\"alpha\", false])");
    assertNotNull(texts);
    assertEquals(texts.length, 2);
    assertEquals(texts[0], "alpha");
    assertNull(texts[1]);

    texts = Viv.getStringOrNulls("return([true, \"alpha\"])");
    assertNotNull(texts);
    assertEquals(texts.length, 2);
    assertNull(texts[0]);
    assertEquals(texts[1], "alpha");

    texts = Viv.getStringOrNulls("return([true])");
    assertNotNull(texts);
    assertEquals(texts.length, 1);
    assertNull(texts[0]);

    texts = Viv.getStringOrNulls("return([\"alpha\"])");
    assertNotNull(texts);
    assertEquals(texts.length, 1);
    assertEquals(texts[0], "alpha");

    Result result = Viv.run("{\"a\": \"alpha\", \"b\": 1.5, \"c\": string(-2.7)}");
    texts = Viv.getStringOrNulls(result);
    assertNotNull(texts);
    assertEquals(texts.length, 3);
    assertEquals(texts[0], "alpha");
    assertNull(texts[1]);
    assertEquals(texts[2], "-2.7");

    result = Viv.run("return([\"alpha\", null])");
    texts = Viv.getStringOrNulls(result);
    assertNotNull(texts);
    assertEquals(texts.length, 2);
    assertEquals(texts[0], "alpha");
    assertNull(texts[1]);

    try {
      texts = Viv.getStringOrNullsEx("return([\"alpha\", null])");
      assertNotNull(texts);
      assertEquals(texts.length, 2);
      assertEquals(texts[0], "alpha");
      assertNull(texts[1]);
    } catch (VivException e) {
      fail("Exception happen in testStringOrNulls");
    }
    try {
      Viv.getStringOrNullsEx("a =");
      fail("Exception is not happened in testStringOrNulls");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getStringOrNulls("a = "));
  }

  /** Unit-test for "Viv.getObjects" method */
  @Test
  public void testObjects() {
    ArrayList<@Nullable Object> array = new ArrayList<>();
    array.add("alpha");
    array.add(true);
    array.add(100);
    array.add(1.5F);
    Object[] objects = Viv.getObjects(array);
    assertNotNull(objects);
    assertEquals(objects.length, 4);
    assertEquals(objects[0], "alpha");
    assertEquals(objects[1], true);
    assertEquals(objects[2], 100L);
    assertEquals(objects[3], 1.5);

    array.set(1, null);
    objects = Viv.getObjects(array);
    assertNull(objects);

    HashMap<Object, Object> block = new HashMap<>();
    block.put("a", "alpha");
    block.put("b", true);
    objects = Viv.getObjects(block);
    assertNotNull(objects);
    assertEquals(objects.length, 2);
    assertEquals(objects[0], "alpha");
    assertEquals(objects[1], true);

    objects = Viv.getObjects("{\"a\": \"alpha\", \"b\": true}");
    assertNotNull(objects);
    assertEquals(objects.length, 2);
    assertEquals(objects[0], "alpha");
    assertEquals(objects[1], true);

    objects = Viv.getObjects("return([\"alpha\", true])");
    assertNotNull(objects);
    assertEquals(objects.length, 2);
    assertEquals(objects[0], "alpha");
    assertEquals(objects[1], true);

    objects = Viv.getObjects("return([null, \"alpha\"])");
    assertNull(objects);

    objects = Viv.getObjects("return([null])");
    assertNull(objects);

    objects = Viv.getObjects("return([\"alpha\"])");
    assertNotNull(objects);
    assertEquals(objects.length, 1);
    assertEquals(objects[0], "alpha");

    Result result = Viv.run("{\"a\": \"alpha\", \"b\": -2.7, \"c\": string(-2.7)}");
    objects = Viv.getObjects(result);
    assertNotNull(objects);
    assertEquals(objects.length, 3);
    assertEquals(objects[0], "alpha");
    assertEquals(objects[1], -2.7);
    assertEquals(objects[2], "-2.7");

    result = Viv.run("return([\"alpha\", true])");
    objects = Viv.getObjects(result);
    assertNotNull(objects);
    assertEquals(objects.length, 2);
    assertEquals(objects[0], "alpha");
    assertEquals(objects[1], true);

    try {
      objects = Viv.getObjectsEx("return([\"alpha\", true])");
      assertNotNull(objects);
      assertEquals(objects.length, 2);
      assertEquals(objects[0], "alpha");
      assertEquals(objects[1], true);
    } catch (VivException e) {
      fail("Exception happen in testObjects");
    }
    try {
      Viv.getObjectsEx("a = ");
      fail("Exception is not happened in testObjects");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getObjects("a = "));
  }

  /** Unit-test for "Viv.getObjectOrNulls" method */
  @Test
  public void testObjectOrNulls() {
    ArrayList<@Nullable Object> array = new ArrayList<>();
    array.add("alpha");
    array.add(true);
    array.add(100);
    array.add(1.5F);
    Object[] objects = Viv.getObjectOrNulls(array);
    assertNotNull(objects);
    assertEquals(objects.length, 4);
    assertEquals(objects[0], "alpha");
    assertEquals(objects[1], true);
    assertEquals(objects[2], 100L);
    assertEquals(objects[3], 1.5);

    array.set(1, null);
    objects = Viv.getObjectOrNulls(array);
    assertNotNull(objects);
    assertEquals(objects.length, 4);
    assertEquals(objects[0], "alpha");
    assertNull(objects[1]);
    assertEquals(objects[2], 100L);
    assertEquals(objects[3], 1.5);

    HashMap<Object, @Nullable Object> block = new HashMap<>();
    block.put("a", "alpha");
    block.put("b", null);
    objects = Viv.getObjectOrNulls(block);
    assertNotNull(objects);
    assertEquals(objects.length, 2);
    assertEquals(objects[0], "alpha");
    assertNull(objects[1]);

    objects = Viv.getObjectOrNulls("{\"a\": \"alpha\", \"b\": null}");
    assertNotNull(objects);
    assertEquals(objects.length, 2);
    assertEquals(objects[0], "alpha");
    assertNull(objects[1]);

    objects = Viv.getObjectOrNulls("return([\"alpha\", null])");
    assertNotNull(objects);
    assertEquals(objects.length, 2);
    assertEquals(objects[0], "alpha");
    assertNull(objects[1]);

    objects = Viv.getObjectOrNulls("return([null, \"alpha\"])");
    assertNotNull(objects);
    assertEquals(objects.length, 2);
    assertNull(objects[0]);
    assertEquals(objects[1], "alpha");

    objects = Viv.getObjectOrNulls("return([null])");
    assertNotNull(objects);
    assertEquals(objects.length, 1);
    assertNull(objects[0]);

    Result result = Viv.run("{\"a\": -2.7, \"b\": null, \"c\": \"null\"}");
    objects = Viv.getObjectOrNulls(result);
    assertNotNull(objects);
    assertEquals(objects.length, 3);
    assertEquals(objects[0], -2.7);
    assertEquals(objects[1], null);
    assertEquals(objects[2], "null");

    try {
      objects = Viv.getObjectOrNullsEx("{\"a\": -2.7, \"b\": null, \"c\": \"null\"}");
      assertNotNull(objects);
      assertEquals(objects.length, 3);
      assertEquals(objects[0], -2.7);
      assertEquals(objects[1], null);
      assertEquals(objects[2], "null");
    } catch (VivException e) {
      fail("Exception happen in testObjectOrNulls");
    }
    try {
      Viv.getObjectOrNullsEx("a =");
      fail("Exception is not happened in testObjectOrNulls");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }
    assertNull(Viv.getObjectOrNulls("a = "));
  }

  @Test
  public void testHashMap() {
    Object str = "return('test')";
    HashMap<String, @Nullable Object> value = Viv.getHashMap(str);
    assertNull(value);

    Object integer = 10;
    value = Viv.getHashMap(integer);
    assertNull(value);

    HashMap<Integer, String> intStr = new HashMap<>();
    intStr.put(1, "alpha");
    intStr.put(2, "beta");
    value = Viv.getHashMap(intStr);
    assertNull(value);

    HashMap<@Nullable String, Boolean> strBool = new HashMap<>();
    strBool.put("x", true);
    strBool.put("y", false);
    value = Viv.getHashMap(strBool);
    assertNotNull(value);
    assertTrue(value instanceof HashMap);
    assertTrue(strBool.get("x"));
    assertFalse(strBool.get("y"));

    strBool = new HashMap<>();
    strBool.put("x", true);
    strBool.put(null, false);
    value = Viv.getHashMap(strBool);
    assertNull(value);

    HashMap<Object, @Nullable Integer> objInt = new HashMap<>();
    objInt.put("a", 10);
    objInt.put("b", 20);
    objInt.put("c", null);
    value = Viv.getHashMap(objInt);
    assertNotNull(value);
    assertTrue(value instanceof HashMap);
    assertEquals(value.get("a"), 10);
    assertEquals(value.get("b"), 20);
    assertNull(value.get("c"));

    try {
      value = Viv.getHashMapEx(objInt);
    } catch (VivException e) {
      fail("Exception happen in testHashMap");
    }
    assertNotNull(value);
    assertTrue(value instanceof HashMap);
    assertEquals(value.get("a"), 10);
    assertEquals(value.get("b"), 20);
    assertNull(value.get("c"));

    objInt = new HashMap<>();
    objInt.put("a", 10);
    objInt.put(100, 20);
    value = Viv.getHashMap(objInt);
    assertNull(value);
  }

  @SuppressWarnings("null")
  @Test
  public void testKeyValueBooleans() {
    String[] names = new String[] {"a", "b", "c"};
    boolean[] booleans = new boolean[] {true, false, true};
    HashMap<Object, @Nullable Object> objBool = new HashMap<>();
    for (int index = 0; index < names.length; index++) {
      objBool.put(names[index], booleans[index]);
    }
    Viv.KeyValue<Boolean>[] keyValues = Viv.getKeyValueBooleans(objBool);
    assertNotNull(keyValues);
    assertEquals(keyValues.length, 3);
    int index = 0;
    for (Viv.KeyValue<Boolean> keyValue : keyValues) {
      assertEquals(keyValue.key, names[index]);
      assertEquals(keyValue.value, booleans[index]);
      index++;
    }

    objBool.replace("b", null);
    keyValues = Viv.getKeyValueBooleans(objBool);
    assertNull(keyValues);

    objBool.remove("b");

    objBool.put(100, 100);
    keyValues = Viv.getKeyValueBooleans(objBool);
    assertNull(keyValues);

    // {"a": true, "b": false}
    String code = "{\"a\": true, \"b\": false}";
      {
        Result result = Viv.run(code);
        Viv.KeyValue<Boolean>[] pairs = Viv.getKeyValueBooleans(result);
        index = 0;
        for (Viv.KeyValue<Boolean> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, booleans[index]);
          index++;
        }

        pairs = Viv.getKeyValueBooleans("{\"a\": true, \"b\": null}");
        // System.out.println(pairs == null);
        assertNull(pairs);
      }

    try {
      Object object = Viv.runEx(code);
      Viv.KeyValue<Boolean>[] pairs = Viv.getKeyValueBooleansEx(object);
      index = 0;
      for (Viv.KeyValue<Boolean> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, booleans[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueBooleans");
    }

    try {
      Viv.getKeyValueBooleansEx("{\"a\": true, \"b\": 100}");
      fail("Exception is not happened in testKeyValueBooleans");
    } catch (VivException e) {
      assertFalse(e.getMessage().isEmpty());
    }

    try {
      Viv.KeyValue<Boolean>[] pairs = Viv.getKeyValueBooleansEx(code);
      index = 0;
      for (Viv.KeyValue<Boolean> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, booleans[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueBooleans");
    }

      {
        // {"a": true, "b": false}
        String[] codes = {"\"a\": true", "\"b\": false"};
        Viv.KeyValue<Boolean>[] pairs = Viv.getKeyValueBooleans(codes, null);
        index = 0;
        for (Viv.KeyValue<Boolean> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, booleans[index]);
          index++;
        }

        codes[1] = "\"b\": null";
        pairs = Viv.getKeyValueBooleans(codes, null);
        // System.out.println(pairs == null);
        assertNull(pairs);

        codes[1] = "\"b\": 10";
        pairs = Viv.getKeyValueBooleans(codes, null);
        // System.out.println(pairs == null);
        assertNull(pairs);
      }
  }

  @SuppressWarnings("null")
  @Test
  public void testKeyValueBooleanOrNulls() {
    String[] names = new String[] {"a", "b", "c"};
    Boolean[] booleans = new Boolean[] {true, null, true};
    HashMap<Object, @Nullable Object> objBool = new HashMap<>();
    for (int index = 0; index < names.length; index++) {
      objBool.put(names[index], booleans[index]);
    }
    Viv.KeyValue<@Nullable Boolean>[] keyValues = Viv.getKeyValueBooleanOrNulls(objBool);
    assertNotNull(keyValues);
    assertEquals(keyValues.length, 3);
    int index = 0;
    for (Viv.KeyValue<@Nullable Boolean> keyValue : keyValues) {
      assertEquals(keyValue.key, names[index]);
      assertEquals(keyValue.value, booleans[index]);
      index++;
    }

    objBool.put(100, 100);
    keyValues = Viv.getKeyValueBooleanOrNulls(objBool);
    assertNull(keyValues);

    // {"a": true, "b": null}
    String code = "{\"a\": true, \"b\": null}";
      {
        Result result = Viv.run(code);
        Viv.KeyValue<@Nullable Boolean>[] pairs = Viv.getKeyValueBooleanOrNulls(result);
        index = 0;
        for (Viv.KeyValue<@Nullable Boolean> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, booleans[index]);
          index++;
        }
      }

    try {
      Object object = Viv.runEx(code);
      Viv.KeyValue<@Nullable Boolean>[] pairs = Viv.getKeyValueBooleanOrNullsEx(object);
      index = 0;
      for (Viv.KeyValue<@Nullable Boolean> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, booleans[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueBooleans");
    }

    try {
      Viv.KeyValue<@Nullable Boolean>[] pairs = Viv.getKeyValueBooleanOrNullsEx(code);
      index = 0;
      for (Viv.KeyValue<@Nullable Boolean> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, booleans[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueBooleans");
    }

      {
        // {"a": true, "b": null}
        String[] codes = {"\"a\": true", "\"b\": null"};
        Viv.KeyValue<Boolean>[] pairs = Viv.getKeyValueBooleanOrNulls(codes, null);
        index = 0;
        for (Viv.KeyValue<Boolean> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, booleans[index]);
          index++;
        }
      }

      {
        // {"a": true, "b": 10}
        String[] codes = {"\"a\": true", "\"b\": 10"};
        Viv.KeyValue<Boolean>[] pairs = Viv.getKeyValueBooleanOrNulls(codes, null);
        index = 0;
        for (Viv.KeyValue<Boolean> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, booleans[index]);
          index++;
        }
      }
  }

  @SuppressWarnings("null")
  @Test
  public void testKeyValueIntegers() {
    String[] names = new String[] {"a", "b", "c"};
    int[] ints = new int[] {10, 20, 30};
    HashMap<Object, @Nullable Object> objInt = new HashMap<>();
    for (int index = 0; index < names.length; index++) {
      objInt.put(names[index], ints[index]);
    }
    Viv.KeyValue<Integer>[] keyValues = Viv.getKeyValueIntegers(objInt);
    assertNotNull(keyValues);
    assertEquals(keyValues.length, 3);
    int index = 0;
    for (Viv.KeyValue<Integer> keyValue : keyValues) {
      assertEquals(keyValue.key, names[index]);
      assertEquals(keyValue.value, ints[index]);
      index++;
    }

    objInt.replace("b", null);
    keyValues = Viv.getKeyValueIntegers(objInt);
    assertNull(keyValues);

    objInt.remove("b");

    objInt.put(100, 100);
    keyValues = Viv.getKeyValueIntegers(objInt);
    assertNull(keyValues);

    // {"a": 10, "b": 20}
    String code = "{\"a\": 10, \"b\": 20}";
      {
        Result result = Viv.run(code);
        Viv.KeyValue<Integer>[] pairs = Viv.getKeyValueIntegers(result);
        index = 0;
        for (Viv.KeyValue<Integer> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, ints[index]);
          index++;
        }

        pairs = Viv.getKeyValueIntegers("{\"a\": 10, \"b\": null}");
        // System.out.println(pairs == null);
        assertNull(pairs);
      }

    try {
      Object object = Viv.runEx(code);
      Viv.KeyValue<Integer>[] pairs = Viv.getKeyValueIntegersEx(object);
      index = 0;
      for (Viv.KeyValue<Integer> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, ints[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueIntegers");
    }

    try {
      Viv.KeyValue<Integer>[] pairs = Viv.getKeyValueIntegersEx(code);
      index = 0;
      for (Viv.KeyValue<Integer> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, ints[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueIntegers");
    }

      {
        // {"a": 10, "b": 20}
        String[] codes = {"\"a\": 10", "\"b\": 20"};
        Viv.KeyValue<Integer>[] pairs = Viv.getKeyValueIntegers(codes, null);
        index = 0;
        for (Viv.KeyValue<Integer> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, ints[index]);
          index++;
        }

        codes[1] = "\"b\": null";
        pairs = Viv.getKeyValueIntegers(codes, null);
        // System.out.println(pairs == null);
        assertNull(pairs);

        codes[1] = "\"b\": true";
        pairs = Viv.getKeyValueIntegers(codes, null);
        // System.out.println(pairs == null);
        assertNull(pairs);

        codes[1] = "\"b\": \"alpha\"";
        pairs = Viv.getKeyValueIntegers(codes, null);
        // System.out.println(pairs == null);
        assertNull(pairs);
      }

      {
        // {"foo": 10, "bar": 30, "baz": 20}
        String data = "{\"foo\": 10, \"bar\": 30, \"baz\": 20}";
        String[] keys = new String[] {"foo", "bar", "baz"};
        Integer[] integers = new Integer[] {10, 30, 20};

        // Key-Value pairs
        Viv.KeyValue<Integer>[] pairs = Viv.getKeyValueIntegers(data);
        int flag = 0;
        for (Viv.KeyValue<Integer> pair : pairs) {  // String key, Integer value
          // System.out.println(pair.key + ": " + pair.value);  // foo: 10
          //                                                    // bar: 30
          //                                                    // baz: 20
          for (int i = 0; i < keys.length; i++) {
            if (pair.key.equals(keys[i]) && pair.value == integers[i]) {
              flag |= 1 << i;
              break;
            }
          }
        }
        assertEquals(flag, 7);

        // Values
        int[] values = Viv.getIntegers(data);
        flag = 0;
        for (int value : values) {
          // System.out.println(value);  // 10
          //                             // 30
          //                             // 20
          for (int i = 0; i < integers.length; i++) {
            if (value == integers[i]) {
              flag |= 1 << i;
              break;
            }
          }
        }
        assertEquals(flag, 7);

        // The specific variable's value
        int foo = Viv.getInteger(data, "return(foo)");
        // System.out.println(foo);  // 10
        assertEquals(foo, 10);

        // The calculated value
        int add = Viv.getInteger(data, "return(foo + bar + baz)");
        // System.out.println(add);  // 60
        assertEquals(add, 60);

        // Find maximum value.
        // 1. pairs = {"foo": 10, "bar": 30, "baz": 20}
        // 2. for-loop with iterator: for (pair in pairs) {...}
        //    - pair[0] is the above key.
        //    - pair[1] is the above value.
        // 3. Update max.
        // 4. Return it.
        String c = "max=-1, for (pair in pairs) {if (max < pair[1]) {max = pair[1]}}, return(max)";
        int max = Viv.getInteger("pairs = ", "+", data, c);
        // System.out.println(max);  // 30
        assertEquals(max, 30);

        max = Viv.getInteger("pairs = " + data, c);
        // System.out.println(max);  // 30
        assertEquals(max, 30);

        String w = "max=-1, for (pair in .) {if (max < pair[1]) {max = pair[1]}}, return(max)";
        max = Viv.getInteger(data, w);
        // System.out.println(max);  // 30
        assertEquals(max, 30);
      }
  }

  @SuppressWarnings("null")
  @Test
  public void testKeyValueIntegerOrNulls() {
    String[] names = new String[] {"a", "b", "c"};
    Integer[] ints = new Integer[] {10, null, 30};
    HashMap<Object, @Nullable Object> objInt = new HashMap<>();
    for (int index = 0; index < names.length; index++) {
      objInt.put(names[index], ints[index]);
    }
    Viv.KeyValue<@Nullable Integer>[] keyValues = Viv.getKeyValueIntegerOrNulls(objInt);
    assertNotNull(keyValues);
    assertEquals(keyValues.length, 3);
    int index = 0;
    for (Viv.KeyValue<@Nullable Integer> keyValue : keyValues) {
      assertEquals(keyValue.key, names[index]);
      assertEquals(keyValue.value, ints[index]);
      index++;
    }

    objInt.put(100, 100);
    keyValues = Viv.getKeyValueIntegerOrNulls(objInt);
    assertNull(keyValues);

    // {"a": 10, "b": null}
    String code = "{\"a\": 10, \"b\": null}";
      {
        Result result = Viv.run(code);
        Viv.KeyValue<@Nullable Integer>[] pairs = Viv.getKeyValueIntegerOrNulls(result);
        index = 0;
        for (Viv.KeyValue<@Nullable Integer> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, ints[index]);
          index++;
        }
      }

    try {
      Object object = Viv.runEx(code);
      Viv.KeyValue<@Nullable Integer>[] pairs = Viv.getKeyValueIntegerOrNullsEx(object);
      index = 0;
      for (Viv.KeyValue<@Nullable Integer> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, ints[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueIntegerOrNulls");
    }

    try {
      Viv.KeyValue<@Nullable Integer>[] pairs = Viv.getKeyValueIntegerOrNullsEx(code);
      index = 0;
      for (Viv.KeyValue<@Nullable Integer> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, ints[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueIntegerOrNulls");
    }

      {
        // {"a": 10, "b": null}
        String[] codes = {"\"a\": 10", "\"b\": null"};
        Viv.KeyValue<@Nullable Integer>[] pairs =
            Viv.getKeyValueIntegerOrNulls(codes, null);
        index = 0;
        for (Viv.KeyValue<@Nullable Integer> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, ints[index]);
          index++;
        }
      }

      {
        // {"a": 10, "b": true}
        String[] codes = {"\"a\": 10", "\"b\": true"};
        Viv.KeyValue<@Nullable Integer>[] pairs =
            Viv.getKeyValueIntegerOrNulls(codes, null);
        index = 0;
        for (Viv.KeyValue<@Nullable Integer> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, ints[index]);
          index++;
        }
      }
  }

  @SuppressWarnings("null")
  @Test
  public void testKeyValueLongs() {
    String[] names = new String[] {"a", "b", "c"};
    long[] longs = new long[] {10L, 20L, 30L};
    HashMap<Object, @Nullable Object> objLong = new HashMap<>();
    for (int index = 0; index < names.length; index++) {
      objLong.put(names[index], longs[index]);
    }
    Viv.KeyValue<Long>[] keyValues = Viv.getKeyValueLongs(objLong);
    assertNotNull(keyValues);
    assertEquals(keyValues.length, 3);
    int index = 0;
    for (Viv.KeyValue<Long> keyValue : keyValues) {
      assertEquals(keyValue.key, names[index]);
      assertEquals(keyValue.value, longs[index]);
      index++;
    }

    objLong.replace("b", null);
    keyValues = Viv.getKeyValueLongs(objLong);
    assertNull(keyValues);

    objLong.remove("b");

    objLong.put(100L, 100L);
    keyValues = Viv.getKeyValueLongs(objLong);
    assertNull(keyValues);

    // {"a": 10, "b": 20}
    String code = "{\"a\": 10, \"b\": 20}";
      {
        Result result = Viv.run(code);
        Viv.KeyValue<Long>[] pairs = Viv.getKeyValueLongs(result);
        index = 0;
        for (Viv.KeyValue<Long> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, longs[index]);
          index++;
        }

        pairs = Viv.getKeyValueLongs("{\"a\": 10, \"b\": null}");
        // System.out.println(pairs == null);
        assertNull(pairs);
      }

    try {
      Object object = Viv.runEx(code);
      Viv.KeyValue<Long>[] pairs = Viv.getKeyValueLongsEx(object);
      index = 0;
      for (Viv.KeyValue<Long> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, longs[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueLongs");
    }

    try {
      Viv.KeyValue<Long>[] pairs = Viv.getKeyValueLongsEx(code);
      index = 0;
      for (Viv.KeyValue<Long> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, longs[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueLongs");
    }

      {
        // {"a": 10, "b": 20}
        String[] codes = {"\"a\": 10", "\"b\": 20"};
        Viv.KeyValue<Long>[] pairs = Viv.getKeyValueLongs(codes, null);
        index = 0;
        for (Viv.KeyValue<Long> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, longs[index]);
          index++;
        }

        codes[1] = "\"b\": null";
        pairs = Viv.getKeyValueLongs(codes, null);
        // System.out.println(pairs == null);
        assertNull(pairs);

        codes[1] = "\"b\": true";
        pairs = Viv.getKeyValueLongs(codes, null);
        // System.out.println(pairs == null);
        assertNull(pairs);
      }
  }

  @SuppressWarnings("null")
  @Test
  public void testKeyValueLongOrNulls() {
    String[] names = new String[] {"a", "b", "c"};
    Long[] longs = new Long[] {10L, null, 30L};
    HashMap<Object, @Nullable Object> objLong = new HashMap<>();
    for (int index = 0; index < names.length; index++) {
      objLong.put(names[index], longs[index]);
    }
    Viv.KeyValue<@Nullable Long>[] keyValues = Viv.getKeyValueLongOrNulls(objLong);
    assertNotNull(keyValues);
    assertEquals(keyValues.length, 3);
    int index = 0;
    for (Viv.KeyValue<@Nullable Long> keyValue : keyValues) {
      assertEquals(keyValue.key, names[index]);
      assertEquals(keyValue.value, longs[index]);
      index++;
    }

    objLong.put(100L, 100L);
    keyValues = Viv.getKeyValueLongOrNulls(objLong);
    assertNull(keyValues);

    // {"a": 10, "b": null}
    String code = "{\"a\": 10, \"b\": null}";
      {
        Result result = Viv.run(code);
        Viv.KeyValue<@Nullable Long>[] pairs = Viv.getKeyValueLongOrNulls(result);
        index = 0;
        for (Viv.KeyValue<@Nullable Long> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, longs[index]);
          index++;
        }
      }

    try {
      Object object = Viv.runEx(code);
      Viv.KeyValue<@Nullable Long>[] pairs = Viv.getKeyValueLongOrNullsEx(object);
      index = 0;
      for (Viv.KeyValue<@Nullable Long> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, longs[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueLongOrNulls");
    }

    try {
      Viv.KeyValue<@Nullable Long>[] pairs = Viv.getKeyValueLongOrNullsEx(code);
      index = 0;
      for (Viv.KeyValue<@Nullable Long> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, longs[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueLongOrNulls");
    }

      {
        // {"a": 10, "b": null}
        String[] codes = {"\"a\": 10", "\"b\": null"};
        Viv.KeyValue<@Nullable Long>[] pairs =
            Viv.getKeyValueLongOrNulls(codes, null);
        index = 0;
        for (Viv.KeyValue<@Nullable Long> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, longs[index]);
          index++;
        }
      }

      {
        // {"a": 10, "b": true}
        String[] codes = {"\"a\": 10", "\"b\": true"};
        Viv.KeyValue<@Nullable Long>[] pairs =
            Viv.getKeyValueLongOrNulls(codes, null);
        index = 0;
        for (Viv.KeyValue<@Nullable Long> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, longs[index]);
          index++;
        }
      }
  }

  @SuppressWarnings("null")
  @Test
  public void testKeyValueFloats() {
    String[] names = new String[] {"a", "b", "c"};
    float[] floats = new float[] {1.0F, 2.5F, 3.0F};
    HashMap<Object, @Nullable Object> objFloat = new HashMap<>();
    for (int index = 0; index < names.length; index++) {
      objFloat.put(names[index], floats[index]);
    }
    Viv.KeyValue<Float>[] keyValues = Viv.getKeyValueFloats(objFloat);
    assertNotNull(keyValues);
    assertEquals(keyValues.length, 3);
    int index = 0;
    for (Viv.KeyValue<Float> keyValue : keyValues) {
      assertEquals(keyValue.key, names[index]);
      assertEquals(keyValue.value, floats[index]);
      index++;
    }

    objFloat.replace("b", null);
    keyValues = Viv.getKeyValueFloats(objFloat);
    assertNull(keyValues);

    objFloat.remove("b");

    objFloat.put(100.0F, 100.0F);
    keyValues = Viv.getKeyValueFloats(objFloat);
    assertNull(keyValues);

    // {"a": 1.0, "b": 2.5}
    String code = "{\"a\": 1.0, \"b\": 2.5}";
      {
        Result result = Viv.run(code);
        Viv.KeyValue<Float>[] pairs = Viv.getKeyValueFloats(result);
        index = 0;
        for (Viv.KeyValue<Float> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, floats[index]);
          index++;
        }

        pairs = Viv.getKeyValueFloats("{\"a\": 1.0, \"b\": null}");
        // System.out.println(pairs == null);
        assertNull(pairs);
      }

    try {
      Object object = Viv.runEx(code);
      Viv.KeyValue<Float>[] pairs = Viv.getKeyValueFloatsEx(object);
      index = 0;
      for (Viv.KeyValue<Float> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, floats[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueFloats");
    }

    try {
      Viv.KeyValue<Float>[] pairs = Viv.getKeyValueFloatsEx(code);
      index = 0;
      for (Viv.KeyValue<Float> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, floats[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueFloats");
    }

      {
        // {"a": 1.0, "b": 2.5}
        String[] codes = {"\"a\": 1.0", "\"b\": 2.5"};
        Viv.KeyValue<Float>[] pairs = Viv.getKeyValueFloats(codes, null);
        index = 0;
        for (Viv.KeyValue<Float> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, floats[index]);
          index++;
        }

        codes[1] = "\"b\": null";
        pairs = Viv.getKeyValueFloats(codes, null);
        // System.out.println(pairs == null);
        assertNull(pairs);

        codes[1] = "\"b\": true";
        pairs = Viv.getKeyValueFloats(codes, null);
        // System.out.println(pairs == null);
        assertNull(pairs);
      }
  }

  @SuppressWarnings("null")
  @Test
  public void testKeyValueFloatOrNulls() {
    String[] names = new String[] {"a", "b", "c"};
    Float[] floats = new Float[] {1.0F, null, 3.0F};
    HashMap<Object, @Nullable Object> objFloat = new HashMap<>();
    for (int index = 0; index < names.length; index++) {
      objFloat.put(names[index], floats[index]);
    }
    Viv.KeyValue<@Nullable Float>[] keyValues = Viv.getKeyValueFloatOrNulls(objFloat);
    assertNotNull(keyValues);
    assertEquals(keyValues.length, 3);
    int index = 0;
    for (Viv.KeyValue<@Nullable Float> keyValue : keyValues) {
      assertEquals(keyValue.key, names[index]);
      assertEquals(keyValue.value, floats[index]);
      index++;
    }

    objFloat.put(100.0F, 100.0F);
    keyValues = Viv.getKeyValueFloatOrNulls(objFloat);
    assertNull(keyValues);

    // {"a": 1.0, "b": null}
    String code = "{\"a\": 1.0, \"b\": null}";
      {
        Result result = Viv.run(code);
        Viv.KeyValue<@Nullable Float>[] pairs = Viv.getKeyValueFloatOrNulls(result);
        index = 0;
        for (Viv.KeyValue<@Nullable Float> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, floats[index]);
          index++;
        }
      }

    try {
      Object object = Viv.runEx(code);
      Viv.KeyValue<@Nullable Float>[] pairs = Viv.getKeyValueFloatOrNullsEx(object);
      index = 0;
      for (Viv.KeyValue<@Nullable Float> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, floats[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueFloatOrNulls");
    }

    try {
      Viv.KeyValue<@Nullable Float>[] pairs = Viv.getKeyValueFloatOrNullsEx(code);
      index = 0;
      for (Viv.KeyValue<@Nullable Float> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, floats[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueFloatOrNulls");
    }

      {
        // {"a": 1.0, "b": null}
        String[] codes = {"\"a\": 1.0", "\"b\": null"};
        Viv.KeyValue<@Nullable Float>[] pairs =
            Viv.getKeyValueFloatOrNulls(codes, null);
        index = 0;
        for (Viv.KeyValue<@Nullable Float> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, floats[index]);
          index++;
        }
      }

      {
        // {"a": 1.0, "b": true}
        String[] codes = {"\"a\": 1.0", "\"b\": true"};
        Viv.KeyValue<@Nullable Float>[] pairs =
            Viv.getKeyValueFloatOrNulls(codes, null);
        index = 0;
        for (Viv.KeyValue<@Nullable Float> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, floats[index]);
          index++;
        }
      }
  }

  @SuppressWarnings("null")
  @Test
  public void testKeyValueDoubles() {
    String[] names = new String[] {"a", "b", "c"};
    double[] doubles = new double[] {1.0, 2.5, 3.0};
    HashMap<Object, @Nullable Object> objDouble = new HashMap<>();
    for (int index = 0; index < names.length; index++) {
      objDouble.put(names[index], doubles[index]);
    }
    Viv.KeyValue<Double>[] keyValues = Viv.getKeyValueDoubles(objDouble);
    assertNotNull(keyValues);
    assertEquals(keyValues.length, 3);
    int index = 0;
    for (Viv.KeyValue<Double> keyValue : keyValues) {
      assertEquals(keyValue.key, names[index]);
      assertEquals(keyValue.value, doubles[index]);
      index++;
    }

    objDouble.replace("b", null);
    keyValues = Viv.getKeyValueDoubles(objDouble);
    assertNull(keyValues);

    objDouble.remove("b");

    objDouble.put(100.0F, 100.0F);
    keyValues = Viv.getKeyValueDoubles(objDouble);
    assertNull(keyValues);

    // {"a": 1.0, "b": 2.5}
    String code = "{\"a\": 1.0, \"b\": 2.5}";
      {
        Result result = Viv.run(code);
        Viv.KeyValue<Double>[] pairs = Viv.getKeyValueDoubles(result);
        index = 0;
        for (Viv.KeyValue<Double> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, doubles[index]);
          index++;
        }

        pairs = Viv.getKeyValueDoubles("{\"a\": 1.0, \"b\": null}");
        // System.out.println(pairs == null);
        assertNull(pairs);
      }

    try {
      Object object = Viv.runEx(code);
      Viv.KeyValue<Double>[] pairs = Viv.getKeyValueDoublesEx(object);
      index = 0;
      for (Viv.KeyValue<Double> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, doubles[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueDoubles");
    }

    try {
      Viv.KeyValue<Double>[] pairs = Viv.getKeyValueDoublesEx(code);
      index = 0;
      for (Viv.KeyValue<Double> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, doubles[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueDoubles");
    }

      {
        // {"a": 1.0, "b": 2.5}
        String[] codes = {"\"a\": 1.0", "\"b\": 2.5"};
        Viv.KeyValue<Double>[] pairs = Viv.getKeyValueDoubles(codes, null);
        index = 0;
        for (Viv.KeyValue<Double> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, doubles[index]);
          index++;
        }

        codes[1] = "\"b\": null";
        pairs = Viv.getKeyValueDoubles(codes, null);
        // System.out.println(pairs == null);
        assertNull(pairs);

        codes[1] = "\"b\": true";
        pairs = Viv.getKeyValueDoubles(codes, null);
        // System.out.println(pairs == null);
        assertNull(pairs);
      }
  }

  @SuppressWarnings("null")
  @Test
  public void testKeyValueDoubleOrNulls() {
    String[] names = new String[] {"a", "b", "c"};
    Double[] doubles = new Double[] {1.0, null, 3.0};
    HashMap<Object, @Nullable Object> objDouble = new HashMap<>();
    for (int index = 0; index < names.length; index++) {
      objDouble.put(names[index], doubles[index]);
    }
    Viv.KeyValue<@Nullable Double>[] keyValues = Viv.getKeyValueDoubleOrNulls(objDouble);
    assertNotNull(keyValues);
    assertEquals(keyValues.length, 3);
    int index = 0;
    for (Viv.KeyValue<@Nullable Double> keyValue : keyValues) {
      assertEquals(keyValue.key, names[index]);
      assertEquals(keyValue.value, doubles[index]);
      index++;
    }

    objDouble.put(100.0, 100.0);
    keyValues = Viv.getKeyValueDoubleOrNulls(objDouble);
    assertNull(keyValues);

    // {"a": 1.0, "b": null}
    String code = "{\"a\": 1.0, \"b\": null}";
      {
        Result result = Viv.run(code);
        Viv.KeyValue<@Nullable Double>[] pairs = Viv.getKeyValueDoubleOrNulls(result);
        index = 0;
        for (Viv.KeyValue<@Nullable Double> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, doubles[index]);
          index++;
        }
      }

    try {
      Object object = Viv.runEx(code);
      Viv.KeyValue<@Nullable Double>[] pairs = Viv.getKeyValueDoubleOrNullsEx(object);
      index = 0;
      for (Viv.KeyValue<@Nullable Double> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, doubles[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueDoubleOrNulls");
    }

    try {
      Viv.KeyValue<@Nullable Double>[] pairs = Viv.getKeyValueDoubleOrNullsEx(code);
      index = 0;
      for (Viv.KeyValue<@Nullable Double> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, doubles[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueDoubleOrNulls");
    }

      {
        // {"a": 1.0, "b": null}
        String[] codes = {"\"a\": 1.0", "\"b\": null"};
        Viv.KeyValue<@Nullable Double>[] pairs =
            Viv.getKeyValueDoubleOrNulls(codes, null);
        index = 0;
        for (Viv.KeyValue<@Nullable Double> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, doubles[index]);
          index++;
        }
      }

      {
        // {"a": 1.0, "b": true}
        String[] codes = {"\"a\": 1.0", "\"b\": true"};
        Viv.KeyValue<@Nullable Double>[] pairs =
            Viv.getKeyValueDoubleOrNulls(codes, null);
        index = 0;
        for (Viv.KeyValue<@Nullable Double> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, doubles[index]);
          index++;
        }
      }
  }

  @SuppressWarnings("null")
  @Test
  public void testKeyValueStrings() {
    String[] names = new String[] {"a", "b", "c"};
    String[] strings = new String[] {"alpha", "beta", "gamma"};
    HashMap<Object, @Nullable Object> objString = new HashMap<>();
    for (int index = 0; index < names.length; index++) {
      objString.put(names[index], strings[index]);
    }
    Viv.KeyValue<String>[] keyValues = Viv.getKeyValueStrings(objString);
    assertNotNull(keyValues);
    assertEquals(keyValues.length, 3);
    int index = 0;
    for (Viv.KeyValue<String> keyValue : keyValues) {
      assertEquals(keyValue.key, names[index]);
      assertEquals(keyValue.value, strings[index]);
      index++;
    }

    objString.replace("b", null);
    keyValues = Viv.getKeyValueStrings(objString);
    assertNull(keyValues);

    objString.remove("b");

    objString.put(100.0F, 100.0F);
    keyValues = Viv.getKeyValueStrings(objString);
    assertNull(keyValues);

    // {"a": "alpha", "b": "beta"}
    String code = "{\"a\": \"alpha\", \"b\": \"beta\"}";
      {
        Result result = Viv.run(code);
        Viv.KeyValue<String>[] pairs = Viv.getKeyValueStrings(result);
        index = 0;
        for (Viv.KeyValue<String> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, strings[index]);
          index++;
        }

        pairs = Viv.getKeyValueStrings("{\"a\": \"alpha\", \"b\": null}");
        // System.out.println(pairs == null);
        assertNull(pairs);
      }

    try {
      Object object = Viv.runEx(code);
      Viv.KeyValue<String>[] pairs = Viv.getKeyValueStringsEx(object);
      index = 0;
      for (Viv.KeyValue<String> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, strings[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueStrings");
    }

    try {
      Viv.KeyValue<String>[] pairs = Viv.getKeyValueStringsEx(code);
      index = 0;
      for (Viv.KeyValue<String> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, strings[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueStrings");
    }

      {
        // {"a": "alpha", "b": "beta"}
        String[] codes = {"\"a\": \"alpha\"", "\"b\": \"beta\""};
        Viv.KeyValue<String>[] pairs = Viv.getKeyValueStrings(codes, null);
        index = 0;
        for (Viv.KeyValue<String> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, strings[index]);
          index++;
        }

        codes[1] = "\"b\": null";
        pairs = Viv.getKeyValueStrings(codes, null);
        // System.out.println(pairs == null);
        assertNull(pairs);

        codes[1] = "\"b\": 10";
        pairs = Viv.getKeyValueStrings(codes, null);
        // System.out.println(pairs == null);
        assertNull(pairs);
      }
  }

  @SuppressWarnings("null")
  @Test
  public void testKeyValueStringOrNulls() {
    String[] names = new String[] {"a", "b", "c"};
    String[] strings = new String[] {"alpha", null, "gamma"};
    HashMap<Object, @Nullable Object> objString = new HashMap<>();
    for (int index = 0; index < names.length; index++) {
      objString.put(names[index], strings[index]);
    }
    Viv.KeyValue<@Nullable String>[] keyValues = Viv.getKeyValueStringOrNulls(objString);
    assertNotNull(keyValues);
    assertEquals(keyValues.length, 3);
    int index = 0;
    for (Viv.KeyValue<@Nullable String> keyValue : keyValues) {
      assertEquals(keyValue.key, names[index]);
      assertEquals(keyValue.value, strings[index]);
      index++;
    }

    objString.put(100.0, 100.0);
    keyValues = Viv.getKeyValueStringOrNulls(objString);
    assertNull(keyValues);

    // {"a": "alpha", "b": null}
    String code = "{\"a\": \"alpha\", \"b\": null}";
      {
        Result result = Viv.run(code);
        Viv.KeyValue<@Nullable String>[] pairs = Viv.getKeyValueStringOrNulls(result);
        index = 0;
        for (Viv.KeyValue<@Nullable String> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, strings[index]);
          index++;
        }
      }

    try {
      Object object = Viv.runEx(code);
      Viv.KeyValue<@Nullable String>[] pairs = Viv.getKeyValueStringOrNullsEx(object);
      index = 0;
      for (Viv.KeyValue<@Nullable String> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, strings[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueStringOrNulls");
    }

    try {
      Viv.KeyValue<@Nullable String>[] pairs = Viv.getKeyValueStringOrNullsEx(code);
      index = 0;
      for (Viv.KeyValue<@Nullable String> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, strings[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueStringOrNulls");
    }

      {
        // {"a": "alpha", "b": null}
        String[] codes = {"\"a\": \"alpha\"", "\"b\": null"};
        Viv.KeyValue<@Nullable String>[] pairs =
            Viv.getKeyValueStringOrNulls(codes, null);
        index = 0;
        for (Viv.KeyValue<@Nullable String> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, strings[index]);
          index++;
        }
      }
  }

  @SuppressWarnings("null")
  @Test
  public void testKeyValueObjects() {
    String[] names = new String[] {"a", "b", "c"};
    Object[] objects = new Object[] {"alpha", 100L, true};
    HashMap<Object, @Nullable Object> map = new HashMap<>();
    for (int index = 0; index < names.length; index++) {
      map.put(names[index], objects[index]);
    }
    Viv.KeyValue<Object>[] keyValues = Viv.getKeyValueObjects(map);
    assertNotNull(keyValues);
    assertEquals(keyValues.length, 3);
    int index = 0;
    for (Viv.KeyValue<Object> keyValue : keyValues) {
      assertEquals(keyValue.key, names[index]);
      assertEquals(keyValue.value, objects[index]);
      index++;
    }

    map.replace("b", null);
    keyValues = Viv.getKeyValueObjects(map);
    assertNull(keyValues);

    map.remove("b");

    map.put(100.0F, 100.0F);
    keyValues = Viv.getKeyValueObjects(map);
    assertNull(keyValues);

    // {"a": "alpha", "b": 100}
    String code = "{\"a\": \"alpha\", \"b\": 100}";
      {
        Result result = Viv.run(code);
        Viv.KeyValue<Object>[] pairs = Viv.getKeyValueObjects(result);
        index = 0;
        for (Viv.KeyValue<Object> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, objects[index]);
          index++;
        }

        pairs = Viv.getKeyValueObjects("{\"a\": \"alpha\", \"b\": null}");
        // System.out.println(pairs == null);
        assertNull(pairs);
      }

    try {
      Object object = Viv.runEx(code);
      Viv.KeyValue<Object>[] pairs = Viv.getKeyValueObjectsEx(object);
      index = 0;
      for (Viv.KeyValue<Object> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, objects[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueStrings");
    }

    try {
      Viv.KeyValue<Object>[] pairs = Viv.getKeyValueObjectsEx(code);
      index = 0;
      for (Viv.KeyValue<Object> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, objects[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueStrings");
    }

      {
        // {"a": "alpha", "b": 100}
        String[] codes = {"\"a\": \"alpha\"", "\"b\": 100"};
        Viv.KeyValue<Object>[] pairs = Viv.getKeyValueObjects(codes, null);
        index = 0;
        for (Viv.KeyValue<Object> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, objects[index]);
          index++;
        }

        codes[1] = "\"b\": null";
        pairs = Viv.getKeyValueObjects(codes, null);
        // System.out.println(pairs == null);
        assertNull(pairs);
      }
  }

  @SuppressWarnings("null")
  @Test
  public void testKeyValueObjectOrNulls() {
    String[] names = new String[] {"a", "b", "c"};
    Object[] objects = new Object[] {"alpha", null, true};
    HashMap<Object, @Nullable Object> map = new HashMap<>();
    for (int index = 0; index < names.length; index++) {
      map.put(names[index], objects[index]);
    }
    Viv.KeyValue<Object>[] keyValues = Viv.getKeyValueObjectOrNulls(map);
    assertNotNull(keyValues);
    assertEquals(keyValues.length, 3);
    int index = 0;
    for (Viv.KeyValue<Object> keyValue : keyValues) {
      assertEquals(keyValue.key, names[index]);
      assertEquals(keyValue.value, objects[index]);
      index++;
    }

    map.put(100.0F, 100.0F);
    keyValues = Viv.getKeyValueObjectOrNulls(map);
    assertNull(keyValues);

    // {"a": "alpha", "b": null}
    String code = "{\"a\": \"alpha\", \"b\": null}";
      {
        Result result = Viv.run(code);
        Viv.KeyValue<@Nullable Object>[] pairs = Viv.getKeyValueObjectOrNulls(result);
        index = 0;
        for (Viv.KeyValue<@Nullable Object> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, objects[index]);
          index++;
        }

        pairs = Viv.getKeyValueObjectOrNulls("{\"a\": \"alpha\", null: null}");
        // System.out.println(pairs == null);
        assertNull(pairs);
      }

    try {
      Object object = Viv.runEx(code);
      Viv.KeyValue<Object>[] pairs = Viv.getKeyValueObjectOrNullsEx(object);
      index = 0;
      for (Viv.KeyValue<Object> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, objects[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueStrings");
    }

    try {
      Viv.KeyValue<Object>[] pairs = Viv.getKeyValueObjectOrNullsEx(code);
      index = 0;
      for (Viv.KeyValue<Object> pair : pairs) {
        // System.out.println(pair.key + ": " + pair.value);
        assertEquals(pair.key, names[index]);
        assertEquals(pair.value, objects[index]);
        index++;
      }
    } catch (VivException e) {
      // System.err.println(e.getMessage());
      fail("Exception happen in testKeyValueStrings");
    }

      {
        // {"a": "alpha", "b": null}
        String[] codes = {"\"a\": \"alpha\"", "\"b\": null"};
        Viv.KeyValue<Object>[] pairs = Viv.getKeyValueObjectOrNulls(codes, null);
        index = 0;
        for (Viv.KeyValue<Object> pair : pairs) {
          // System.out.println(pair.key + ": " + pair.value);
          assertEquals(pair.key, names[index]);
          assertEquals(pair.value, objects[index]);
          index++;
        }

        codes[1] = "1: null";
        pairs = Viv.getKeyValueObjectOrNulls(codes, null);
        // System.out.println(pairs == null);
        assertNull(pairs);
      }
  }

  @Test
  public void testMakeString() {
    assertEquals(Viv.makeString(2), "2");
    assertEquals(Viv.makeString(1.5), "1.5");
    assertEquals(Viv.makeString(1.0), "1.0");

    assertEquals(Viv.makeString(Float.POSITIVE_INFINITY), "");
    Config config = new Config();
    assertEquals(Viv.makeString(Float.POSITIVE_INFINITY, config), "");
    config.setInfinity("Infinity");
    assertEquals(Viv.makeString(Float.POSITIVE_INFINITY, config), "Infinity");
    config = new Config();
    config.setNaN("NaN");
    assertEquals(Viv.makeString(Float.POSITIVE_INFINITY, config), "");
    config.setInfinity("Infinity");
    assertEquals(Viv.makeString(Float.POSITIVE_INFINITY, config), "Infinity");

    assertEquals(Viv.makeString(Float.NEGATIVE_INFINITY), "");
    config = new Config();
    assertEquals(Viv.makeString(Float.NEGATIVE_INFINITY, config), "");
    config.setInfinity("Infinity");
    assertEquals(Viv.makeString(Float.NEGATIVE_INFINITY, config), "-Infinity");
    config = new Config();
    config.setNaN("NaN");
    assertEquals(Viv.makeString(Float.NEGATIVE_INFINITY, config), "");
    config.setInfinity("Infinity");
    assertEquals(Viv.makeString(Float.NEGATIVE_INFINITY, config), "-Infinity");

    assertEquals(Viv.makeString(Float.NaN), "");
    config = new Config();
    assertEquals(Viv.makeString(Float.NaN, config), "");
    config.setInfinity("Infinity");
    assertEquals(Viv.makeString(Float.NaN, config), "");
    config = new Config();
    config.setNaN("NaN");
    assertEquals(Viv.makeString(Float.NaN, config), "NaN");
    config.setInfinity("Infinity");
    assertEquals(Viv.makeString(Float.NaN, config), "NaN");

    assertEquals(Viv.makeString(true), "true");
    assertEquals(Viv.makeString(false), "false");
    assertEquals(Viv.makeString("abcd"), "abcd");
    assertEquals(Viv.makeString(null), "null");

    ArrayList<Integer> arrayInts = new ArrayList<>();
    assertEquals(Viv.makeString(arrayInts), "[]");
    arrayInts.add(1);
    arrayInts.add(2);
    assertEquals(Viv.makeString(arrayInts), "[1, 2]");

    ArrayList<String> arrayStrings = new ArrayList<>();
    arrayStrings.add("a");
    arrayStrings.add("b");
    assertEquals(Viv.makeString(arrayStrings), "[\"a\", \"b\"]");

    HashMap<String, Object> map = new HashMap<>();
    assertEquals(Viv.makeString(map), "{}");
    map.put("a", 3.0);
    map.put("b", true);
    assertEquals(Viv.makeString(map), "{\"a\": 3.0, \"b\": true}");

    ArrayList<@Nullable Object> arrayObjects = new ArrayList<>();
    arrayObjects.add(true);
    arrayObjects.add(null);
    map.put("c", arrayObjects);
    assertEquals(Viv.makeString(map), "{\"a\": 3.0, \"b\": true, \"c\": [true, null]}");
  }

  // target[0]: key
  // target[1]: value
  //
  // data[0][0]: key of 1st data
  // data[0][1]: value of 1st data
  // data[1][0]: key of 2nd data
  // data[1][1]: value of 2nd data
  //      :               :
  //      :               :
  private static boolean isContained(Object[] target, Object[][] data) {
    //for (int i = 0; i < data.length; i++) {
    for (Object[] datum : data) {
      if (target[0].equals(datum[0]) && target[1].equals(datum[1])) {
        return true;
      }
    }
    return false;
  }
}
