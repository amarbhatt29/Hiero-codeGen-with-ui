package com.hiero.design.core.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
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
        context.load().json("/com/hiero/design/core/models/HeroCarouselModelTest.json", "/content");
    }

    @Test
    void testIsAutoplayEnabled() {
        Resource resource = context.resourceResolver().getResource("/content/hero-carousel");
        assertNotNull(resource);

        model = resource.adaptTo(HeroCarouselModel.class);
        assertNotNull(model);
        assertTrue(model.isAutoplayEnabled());
    }

    @Test
    void testIsAutoplayDisabled() {
        context.load().json("/com/hiero/design/core/models/HeroCarouselDisabledTest.json", "/content");
        Resource resource = context.resourceResolver().getResource("/content/hero-carousel-disabled");
        assertNotNull(resource);

        model = resource.adaptTo(HeroCarouselModel.class);
        assertNotNull(model);
        assertFalse(model.isAutoplayEnabled());
    }

    @Test
    void testGetAutoplayInterval() {
        Resource resource = context.resourceResolver().getResource("/content/hero-carousel");
        model = resource.adaptTo(HeroCarouselModel.class);
        assertNotNull(model);

        String interval = model.getAutoplayInterval();
        assertNotNull(interval);
        assertEquals("5000", interval);
    }

    @Test
    void testIsShowNavigation() {
        Resource resource = context.resourceResolver().getResource("/content/hero-carousel");
        model = resource.adaptTo(HeroCarouselModel.class);
        assertNotNull(model);
        assertTrue(model.isShowNavigation());
    }

    @Test
    void testIsEmpty() {
        Resource resource = context.resourceResolver().getResource("/content/hero-carousel");
        model = resource.adaptTo(HeroCarouselModel.class);
        assertNotNull(model);
        assertFalse(model.isEmpty());
    }
}
