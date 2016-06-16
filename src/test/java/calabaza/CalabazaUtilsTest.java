package calabaza;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.json.JSONArray;
import org.json.JSONObject;

public class CalabazaUtilsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void test00ConvertFromJson() {
        // null
        assertNull(CalabazaUtils.convertFromJson(null));
        // invalid string
        assertNull(CalabazaUtils.convertFromJson("text"));

        String src1 = "{}";
        String src2 = "[]";
        String src3 = "{\"NULL\":null,\"NO\":777,\"TEXT\":\"message\",\"OBJECT\":{},\"ARRAY\":[]}";
        String src4 = "[null,777,\"messsage\",{},[]]";

        // from string
        Object test1 = CalabazaUtils.convertFromJson(new JSONObject(src1));
        Object test2 = CalabazaUtils.convertFromJson(new JSONArray(src2));
        Object test3 = CalabazaUtils.convertFromJson(new JSONObject(src3));
        Object test4 = CalabazaUtils.convertFromJson(new JSONArray(src4));

        assertThat(test1, instanceOf(Map.class));
        assertThat(test2, instanceOf(List.class));
        assertThat(test3, instanceOf(Map.class));
        assertThat(test4, instanceOf(List.class));

        assertEquals(0, ((Map<String, Object>) test1).size());

        assertEquals(0, ((List<Object>) test2).size());

        Map<String, Object> map = (Map<String, Object>) test3;
        assertTrue(map.containsKey("NULL"));
        assertNull(map.get("NULL"));
        assertThat(map.get("NO"), instanceOf(Integer.class));
        assertThat(map.get("TEXT"), instanceOf(String.class));
        assertThat(map.get("OBJECT"), instanceOf(Map.class));
        assertThat(map.get("ARRAY"), instanceOf(List.class));

        List<Object> list = (List<Object>) test4;
        assertNull(list.get(0));
        assertThat(list.get(1), instanceOf(Integer.class));
        assertThat(list.get(2), instanceOf(String.class));
        assertThat(list.get(3), instanceOf(Map.class));
        assertThat(list.get(4), instanceOf(List.class));

        // from json
        Object test11 = CalabazaUtils.convertFromJson(new JSONObject(src1));
        Object test12 = CalabazaUtils.convertFromJson(new JSONArray(src2));
        Object test13 = CalabazaUtils.convertFromJson(new JSONObject(src3));
        Object test14 = CalabazaUtils.convertFromJson(new JSONArray(src4));

        assertThat(test11, instanceOf(Map.class));
        assertThat(test12, instanceOf(List.class));
        assertThat(test13, instanceOf(Map.class));
        assertThat(test14, instanceOf(List.class));

        assertEquals(0, ((Map<String, Object>) test11).size());

        assertEquals(0, ((List<Object>) test12).size());

        Map<String, Object> map2 = (Map<String, Object>) test13;
        assertTrue(map2.containsKey("NULL"));
        assertNull(map2.get("NULL"));
        assertThat(map2.get("NO"), instanceOf(Integer.class));
        assertThat(map2.get("TEXT"), instanceOf(String.class));
        assertThat(map2.get("OBJECT"), instanceOf(Map.class));
        assertThat(map2.get("ARRAY"), instanceOf(List.class));

        List<Object> list2 = (List<Object>) test14;
        assertNull(list2.get(0));
        assertThat(list2.get(1), instanceOf(Integer.class));
        assertThat(list2.get(2), instanceOf(String.class));
        assertThat(list2.get(3), instanceOf(Map.class));
        assertThat(list2.get(4), instanceOf(List.class));
    }

    @Test
    public void test01ConvertToJson() {
        /*
         * irregular
         */

        // null
        try {
            CalabazaUtils.convertToJson(null);
            fail();
        } catch (Exception e) {
        }

        // unsupported class
        try {
            CalabazaUtils.convertToJson("TEXT");
            fail();
        } catch (Exception e) {
        }

        /*
         * regular
         */

        // java.util.Map (empty)
        Map<String, Object> map = new HashMap<>();
        assertEquals("{}", CalabazaUtils.convertToJson(map).toString());

        // java.util.List (empty)
        List<Object> list = new ArrayList<>();
        assertEquals("[]", CalabazaUtils.convertToJson(list).toString());

        List<String> tmpList = Arrays.asList("v");
        Map<String, String> tmpMap = new HashMap<>();
        tmpMap.put("k", "v");

        // java.util.Map
        Map<String, Object> map2 = new HashMap<>();
        map2.put("NULL", null);
        map2.put("NUMERIC", 777);
        map2.put("TEXT", "hello");
        map2.put("LIST", tmpList);
        map2.put("MAP", tmpMap);
        String joStr = CalabazaUtils.convertToJson(map2).toString();
        assertTrue(joStr.startsWith("{"));
        assertTrue(joStr.endsWith("}"));
        assertTrue(joStr.contains("\"NULL\":null"));
        assertTrue(joStr.contains("\"NUMERIC\":777"));
        assertTrue(joStr.contains("\"TEXT\":\"hello\""));
        assertTrue(joStr.contains("\"LIST\":[\"v\"]"));
        assertTrue(joStr.contains("\"MAP\":{\"k\":\"v\"}"));

        // java.util.List
        List<Object> list2 = new ArrayList<>();
        list2.add(null);
        list2.add(777);
        list2.add("hello");
        list2.add(tmpList);
        list2.add(tmpMap);
        String jaStr = CalabazaUtils.convertToJson(list2).toString();
        assertEquals("[null,777,\"hello\",[\"v\"],{\"k\":\"v\"}]", jaStr);
    }

}
