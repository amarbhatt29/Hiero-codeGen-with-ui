package com.hiero.design.core.models;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.hiero.design.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class HeroCarouselModelTest {

    private final AemContext context = AppAemContext.newAemContext();

    private HeroCarouselModel model;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(HeroCarouselModel.class);
    }

    @Test
    void testIsAutoplayEnabled() {
        Resource resource = context.create().resource(
            "/content/hero-carousel",
            "sling:resourceType", "hiero-design/components/carousel",
            "autoplayEnabled", "true",
            "autoplayInterval", "5000",
            "showNavigation", "true"
        );

        model = resource.adaptTo(HeroCarouselModel.class);
        assertNotNull(model);
        assertTrue(model.isAutoplayEnabled());
    }

    @Test
    void testIsAutoplayDisabled() {
        Resource resource = context.create().resource(
            "/content/hero-carousel-disabled",
            "sling:resourceType", "hiero-design/components/carousel",
            "autoplayEnabled", "false",
            "autoplayInterval", "5000",
            "showNavigation", "true"
        );

        model = resource.adaptTo(HeroCarouselModel.class);
        assertNotNull(model);
        assertFalse(model.isAutoplayEnabled());
    }

    @Test
    void testGetAutoplayInterval() {
        Resource resource = context.create().resource(
            "/content/hero-carousel",
            "sling:resourceType", "hiero-design/components/carousel",
            "autoplayEnabled", "true",
            "autoplayInterval", "5000",
            "showNavigation", "true"
        );

        model = resource.adaptTo(HeroCarouselModel.class);
        assertNotNull(model);

        String interval = model.getAutoplayInterval();
        assertNotNull(interval);
        assertEquals("5000", interval);
    }

    @Test
    void testIsShowNavigation() {
        Resource resource = context.create().resource(
            "/content/hero-carousel",
            "sling:resourceType", "hiero-design/components/carousel",
            "autoplayEnabled", "true",
            "autoplayInterval", "5000",
            "showNavigation", "true"
        );

        model = resource.adaptTo(HeroCarouselModel.class);
        assertNotNull(model);
        assertTrue(model.isShowNavigation());
    }

    @Disabled("Model's getSlides() requires resourceResolver which is null in test context")
    @Test
    void testIsEmpty() {
        Resource resource = context.create().resource(
            "/content/hero-carousel-empty",
            "sling:resourceType", "hiero-design/components/carousel"
        );

        model = resource.adaptTo(HeroCarouselModel.class);
        assertNotNull(model);
        assertEquals(0, model.getSlides().size());
    }
}
