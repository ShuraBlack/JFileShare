package de.shurablack.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestTest {

    private Request request;

    @Mock
    private HttpExchange origin;

    @Mock
    private InputStream requestBody;

    @Mock
    private OutputStream responseBody;

    @BeforeEach
    void setUp() throws URISyntaxException, IOException {
        when(origin.getRequestMethod()).thenReturn("GET");
        when(origin.getRequestURI()).thenReturn(new URI("http://localhost:8080/path?key1=value1&key2=value2"));

        request = new Request(origin);
    }

    @Test
    void getMethod_shouldReturnMethod() {
        assertEquals(Method.GET, request.getMethod());
    }

    @Test
    void onRequestWithQuery_shouldParseQuery() {
        assertEquals("value1", request.getQuery("key1"));
        assertEquals("value2", request.getQuery("key2"));
    }

    @Test
    void onRequestWithoutQuery_shouldReturnEmpty() {
        assertNull(request.getQuery("key3"));
    }

    @Test
    void bodyAsString_shouldReturnBody() throws IOException {
        when(origin.getRequestBody()).thenReturn(requestBody);
        when(requestBody.readAllBytes()).thenReturn("body".getBytes());
        assertEquals("body", request.bodyAsString().get());
    }

    @Test
    void bodyAsJson_shouldReturnJson() throws IOException {
        when(origin.getRequestBody()).thenReturn(requestBody);
        when(requestBody.readAllBytes()).thenReturn("{\"key\":\"value\"}".getBytes());
        assertEquals("value", request.bodyAsJson().get().getString("key"));
    }

    @Test
    void sendJsonWithClose_shouldSendJson() throws IOException {
        final JSONObject json = new JSONObject(Map.of("key", "value"));

        when(origin.getResponseHeaders()).thenReturn(new Headers());
        when(origin.getResponseBody()).thenReturn(responseBody);

        request.sendJsonWithClose(200, json);
        verify(origin).sendResponseHeaders(200, json.toString().length());
    }

    @Test
    void nullObject_sendJsonWithClose_throwException() {
        assertThrows(Exception.class, () -> request.sendJsonWithClose(200, null));
    }

    @Test
    void sendEmptyResponse_shouldSendEmptyResponse() throws IOException {
        request.sendEmptyResponse(200);
        verify(origin).sendResponseHeaders(200, -1);
    }
}