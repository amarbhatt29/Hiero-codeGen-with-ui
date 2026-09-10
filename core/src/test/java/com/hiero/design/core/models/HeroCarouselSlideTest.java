package com.hiero.design.core.models;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.hiero.design.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class HeroCarouselSlideTest {

    private final AemContext context = AppAemContext.newAemContext();

    private HeroCarouselSlide slide;

    @BeforeEach
    void setUp() {
        context.load().json("/com/hiero/design/core/models/HeroCarouselSlideTest.json", "/content");
    }

    @Test
    void testSlideWithImage() {
        Resource resource = context.resourceResolver().getResource("/content/slide-with-image");
        assertNotNull(resource);

        slide = resource.adaptTo(HeroCarouselSlide.class);
        assertNotNull(slide);
        assertTrue(slide.hasImage());
        assertFalse(slide.hasVideo());
    }

    @Test
    void testSlideWithVideo() {
        Resource resource = context.resourceResolver().getResource("/content/slide-with-video");
        assertNotNull(resource);

        slide = resource.adaptTo(HeroCarouselSlide.class);
        assertNotNull(slide);
        assertTrue(slide.hasVideo());
    }

    @Test
    void testSlideWithContent() {
        Resource resource = context.resourceResolver().getResource("/content/slide-with-content");
        assertNotNull(resource);

        slide = resource.adaptTo(HeroCarouselSlide.class);
        assertNotNull(slide);
        assertTrue(slide.hasContent());
        assertNotNull(slide.getHeading());
        assertNotNull(slide.getCtaLabel());
    }

    @Test
    void testSlideProperties() {
        Resource resource = context.resourceResolver().getResource("/content/slide-with-image");
        slide = resource.adaptTo(HeroCarouselSlide.class);

        assertNotNull(slide.getHeading());
        assertNotNull(slide.getDescription());
        assertNotNull(slide.getCtaLabel());
        assertNotNull(slide.getCtaLink());
        assertNotNull(slide.getCtaTarget());
    }

    @Test
    void testMissingMobileImage() {
        Resource resource = context.resourceResolver().getResource("/content/slide-desktop-only");
        slide = resource.adaptTo(HeroCarouselSlide.class);

        assertNotNull(slide.getDesktopImage());
        assertNull(slide.getMobileImage());
        assertTrue(slide.hasImage());
    }
}
