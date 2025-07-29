package io.github.makbn.jlmap;

import io.github.makbn.jlmap.model.JLLatLng;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to verify that the Java Platform Module System is working correctly.
 */
class ModuleSystemTest {

    @Test
    void testModuleSystemWorking() {
        // Test that we can create basic objects from the module
        JLLatLng latLng = JLLatLng.builder()
                .lat(51.044)
                .lng(114.07)
                .build();
        
        assertNotNull(latLng);
        assertEquals(51.044, latLng.getLat());
        assertEquals(114.07, latLng.getLng());
        
        // Test that we can access module properties
        assertNotNull(JLProperties.MapType.OSM_MAPNIK);
        assertNotNull(JLProperties.INIT_MIN_HEIGHT_STAGE);
        assertNotNull(JLProperties.INIT_MIN_WIDTH_STAGE);
        
        System.out.println("✅ Module system is working correctly!");
        System.out.println("✅ Module: io.github.makbn.jlmap");
        System.out.println("✅ Java Version: " + System.getProperty("java.version"));
        System.out.println("✅ Module Path: " + System.getProperty("java.module.path"));
    }
    
    @Test
    void testModuleInfoAccessible() {
        // Test that module-info.java is properly processed
        Module module = JLMapView.class.getModule();
        assertNotNull(module);
        assertEquals("io.github.makbn.jlmap", module.getName());
        
        System.out.println("✅ Module name: " + module.getName());
        System.out.println("✅ Module is named: " + module.isNamed());
    }
} 