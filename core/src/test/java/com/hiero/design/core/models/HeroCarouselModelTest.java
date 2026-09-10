package com.hiero.design.core.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
public class HeroCarouselModelTest {

    private AemContext aemContext;
    private Resource resource;

    @BeforeEach
    void setUp(AemContext context) {
        this.aemContext = context;
        aemContext.load().json("/com/hiero/design/core/models/test-carousel.json", "/content");
    }

    @Test
    void testGetSlides() {
        resource = aemContext.resourceResolver().getResource("/content/hero-carousel");
        HeroCarouselModel model = resource.adaptTo(HeroCarouselModel.class);

        assertNotNull(model);
        assertTrue(model.hasSlides());
        assertEquals(2, model.getSlideCount());
    }

    @Test
    void testGetSlidesEmpty() {
        resource = aemContext.resourceResolver().getResource("/content/empty-carousel");
        HeroCarouselModel model = resource.adaptTo(HeroCarouselModel.class);

        assertNotNull(model);
        assertFalse(model.hasSlides());
        assertEquals(0, model.getSlideCount());
    }

    @Test
    void testAutoRotateDefault() {
        resource = aemContext.resourceResolver().getResource("/content/hero-carousel");
        HeroCarouselModel model = resource.adaptTo(HeroCarouselModel.class);

        assertFalse(model.isAutoRotate());
    }

    @Test
    void testRotationDelayDefault() {
        resource = aemContext.resourceResolver().getResource("/content/hero-carousel");
        HeroCarouselModel model = resource.adaptTo(HeroCarouselModel.class);

        assertEquals(5000, model.getRotationDelay());
    }

    @Test
    void testShowNavigationDefault() {
        resource = aemContext.resourceResolver().getResource("/content/hero-carousel");
        HeroCarouselModel model = resource.adaptTo(HeroCarouselModel.class);

        assertTrue(model.isShowNavigation());
    }

    @Test
    void testSlideProperties() {
        resource = aemContext.resourceResolver().getResource("/content/hero-carousel");
        HeroCarouselModel model = resource.adaptTo(HeroCarouselModel.class);

        List<HeroCarouselSlide> slides = model.getSlides();
        assertFalse(slides.isEmpty());

        HeroCarouselSlide firstSlide = slides.get(0);
        assertNotNull(firstSlide.getHeading());
    }

    @Test
    void testGetId() {
        resource = aemContext.resourceResolver().getResource("/content/hero-carousel");
        HeroCarouselModel model = resource.adaptTo(HeroCarouselModel.class);

        assertNotNull(model.getId());
    }
}
