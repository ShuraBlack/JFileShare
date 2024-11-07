package de.shurablack.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConfigTest {

    @BeforeEach
    void init() {
        Config.init();
    }

    @Test
    void afterInit_returnDefault() {
        assertEquals("0.0.0.0", Config.getIpAddress());
        assertEquals(80, Config.getPort());
        assertEquals(2, Config.getThreadPoolSize());
        assertEquals(true, Config.isRootRestricted());
        assertEquals(false, Config.isVerbose());
        assertEquals(System.getProperty("user.dir"), Config.getRootDirectory());
        assertEquals(false, Config.isUploadAllowed());
        assertEquals(false, Config.isShowHiddenFiles());
    }

    @Test
    void afterInit_getPort_returnPort() {
        final int expected = 80;

        final int actual = Config.getPort();

        assertEquals(expected, actual);
    }

    @Test
    void validChange_getPort_returnChangedPort() {
        final int expected = 8080;
        Config.set("PORT", "8080");

        final int actual = Config.getPort();

        assertEquals(expected, actual);
    }

    @Test
    void invalidChange_getPort_returnDefaultPort() {
        Config.set("PORT", "abc");

        assertThrows(NumberFormatException.class, Config::getPort);
    }

    @Test
    void withInital_getProperties_returnDefaultString() {
        final String result = Config.getProperties();

        assertTrue(result.contains("Verbose: \033[0;31mfalse\033[0m"));
        assertTrue(result.contains("Root Restriction: \033[0;32mtrue\033[0m"));
        assertTrue(result.contains("Upload Allowed: \033[0;31mfalse\033[0m"));
        assertTrue(result.contains("Show Hidden Files: \033[0;31mfalse\033[0m"));
    }
}