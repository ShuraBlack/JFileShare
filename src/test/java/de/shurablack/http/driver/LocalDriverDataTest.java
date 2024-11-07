package de.shurablack.http.driver;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LocalDriverDataTest {

    @InjectMocks
    private LocalDriverData localDriverData = new LocalDriverData("drive",100L, 20L);

    @Test
    void toJson_returnJsonWithAllFields() {
        final JSONObject result = localDriverData.toJson();

        assertEquals("drive", result.getString("name"));
        assertEquals(100L, result.getLong("totalSpace"));
        assertEquals(20L, result.getLong("usedSpace"));
    }
}