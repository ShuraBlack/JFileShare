package de.shurablack.http;

import com.sun.net.httpserver.HttpExchange;
import de.shurablack.util.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.InetSocketAddress;
import java.net.URI;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestHandlerTest {

    @Spy
    private RequestHandler requestHandler;

    @Mock
    private HttpExchange exchange = mock(HttpExchange.class);

    @Mock
    private URI uri = mock(URI.class);

    @Mock
    private InetSocketAddress inetAddress = mock(InetSocketAddress.class);


    @BeforeEach
    void setUp() {
        Config.init();
        Config.set("VERBOSE", "true");

        when(uri.getQuery()).thenReturn("");
        when(exchange.getRemoteAddress()).thenReturn(inetAddress);

        when(exchange.getRequestURI()).thenReturn(uri);
    }

    @Test
    void onGet_handle_redirectToGet() {
        when(exchange.getRequestMethod()).thenReturn("GET");

        requestHandler.handle(exchange);

        verify(requestHandler).get(any());
    }

    @Test
    void onPost_handle_redirectToPost() {
        when(exchange.getRequestMethod()).thenReturn("POST");

        requestHandler.handle(exchange);

        verify(requestHandler).post(any());
    }

    @Test
    void onPut_handle_redirectToPut() {
        when(exchange.getRequestMethod()).thenReturn("PUT");

        requestHandler.handle(exchange);

        verify(requestHandler).put(any());
    }

    @Test
    void onDelete_handle_redirectToDelete() {
        when(exchange.getRequestMethod()).thenReturn("DELETE");

        requestHandler.handle(exchange);

        verify(requestHandler).delete(any());
    }

    @Test
    void onHead_handle_redirectToHead() {
        when(exchange.getRequestMethod()).thenReturn("HEAD");

        requestHandler.handle(exchange);

        verify(requestHandler).head(any());
    }

    @Test
    void onUnsupportedMethod_handle_logInvalidRequest() {
        when(exchange.getRequestMethod()).thenReturn("INVALID");

        requestHandler.handle(exchange);

        verify(requestHandler).logInvalidRequest(any());
    }
}