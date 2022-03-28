package com.hotel.test;

import lombok.RequiredArgsConstructor;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

import static com.hotel.test.FileReader.readFile;

@RequiredArgsConstructor
public class JSONAssertMatcher extends BaseMatcher<String> {

    private final String expected;
    private final boolean isStrict;

    public static JSONAssertMatcher jsonMatcher(String path, Object... placeholders) throws IOException {
        return matcher(path, true, placeholders);
    }

    public static JSONAssertMatcher lenientJsonMatcher(String path, Object... placeholders) throws IOException {
        return matcher(path, false, placeholders);
    }

    private static JSONAssertMatcher matcher(String path, boolean isStrict, Object... placeholders) throws IOException {
        return new JSONAssertMatcher(readFile(path, placeholders), isStrict);
    }

    @Override
    public boolean matches(Object actual) {
        try {
            JSONAssert.assertEquals(expected, (String) actual, isStrict);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {}
}
