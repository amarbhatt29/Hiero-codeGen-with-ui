package com.hiero.design.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.adobe.cq.testing.mock.acs.commons.wcm.properties.AcsCommonsPropertyNameConstants;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AemContextExtension.class)
class GlobalHeaderModelTest {

    private AemContext aemContext = new AemContext();
    private GlobalHeaderModel model;

    @BeforeEach
    void setUp() {
        aemContext.addModelsForClasses(GlobalHeaderModel.class);
    }

    @Test
    void testDefaultValues() {
        Resource resource = aemContext.create().resource(
            "/content/test/header",
            "sling:resourceType", "hiero-design/components/header"
        );
        SlingHttpServletRequest request = aemContext.request();
        request.setResource(resource);
        model = request.adaptTo(GlobalHeaderModel.class);

        assertEquals("YES BANK", model.getLogoAltText());
        assertFalse(model.isEnableSticky());
        assertTrue(model.isEnableSearch());
        assertTrue(model.isEnableMobileMenu());
        assertFalse(model.isAuthenticated());
        assertFalse(model.hasLogoImage());
    }

    @Test
    void testLogoConfiguration() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("logoPath", "/content/dam/logo.png");
        properties.put("logoAltText", "YES BANK Logo");
        properties.put("sling:resourceType", "hiero-design/components/header");

        Resource resource = aemContext.create().resource(
            "/content/test/header",
            properties
        );
        SlingHttpServletRequest request = aemContext.request();
        request.setResource(resource);
        model = request.adaptTo(GlobalHeaderModel.class);

        assertEquals("/content/dam/logo.png", model.getLogoPath());
        assertEquals("YES BANK Logo", model.getLogoAltText());
        assertTrue(model.hasLogoImage());
    }

    @Test
    void testStickyHeaderConfiguration() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("enableSticky", true);
        properties.put("sling:resourceType", "hiero-design/components/header");

        Resource resource = aemContext.create().resource(
            "/content/test/header",
            properties
        );
        SlingHttpServletRequest request = aemContext.request();
        request.setResource(resource);
        model = request.adaptTo(GlobalHeaderModel.class);

        assertTrue(model.isEnableSticky());
    }

    @Test
    void testAuthenticationConfiguration() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("isAuthenticated", true);
        properties.put("profileName", "John Doe");
        properties.put("loginLink", "/auth/login");
        properties.put("logoutLink", "/auth/logout");
        properties.put("sling:resourceType", "hiero-design/components/header");

        Resource resource = aemContext.create().resource(
            "/content/test/header",
            properties
        );
        SlingHttpServletRequest request = aemContext.request();
        request.setResource(resource);
        model = request.adaptTo(GlobalHeaderModel.class);

        assertTrue(model.isAuthenticated());
        assertEquals("John Doe", model.getProfileName());
        assertEquals("/auth/login", model.getLoginLink());
        assertEquals("/auth/logout", model.getLogoutLink());
    }

    @Test
    void testSearchAndMobileConfiguration() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("enableSearch", false);
        properties.put("enableMobileMenu", false);
        properties.put("sling:resourceType", "hiero-design/components/header");

        Resource resource = aemContext.create().resource(
            "/content/test/header",
            properties
        );
        SlingHttpServletRequest request = aemContext.request();
        request.setResource(resource);
        model = request.adaptTo(GlobalHeaderModel.class);

        assertFalse(model.isEnableSearch());
        assertFalse(model.isEnableMobileMenu());
    }

    @Test
    void testNavigationReferenceConfiguration() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("navigationReference", "/content/components/navigation");
        properties.put("sling:resourceType", "hiero-design/components/header");

        Resource resource = aemContext.create().resource(
            "/content/test/header",
            properties
        );
        SlingHttpServletRequest request = aemContext.request();
        request.setResource(resource);
        model = request.adaptTo(GlobalHeaderModel.class);

        assertEquals("/content/components/navigation", model.getNavigationReference());
    }

    @Test
    void testHelpAndUtilityLinksConfiguration() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("helpLink", "/support/help-center");
        properties.put("sling:resourceType", "hiero-design/components/header");

        Resource resource = aemContext.create().resource(
            "/content/test/header",
            properties
        );
        SlingHttpServletRequest request = aemContext.request();
        request.setResource(resource);
        model = request.adaptTo(GlobalHeaderModel.class);

        assertEquals("/support/help-center", model.getHelpLink());
    }
}
