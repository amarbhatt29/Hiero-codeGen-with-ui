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
        context.addModelsForClasses(HeroCarouselSlide.class);
    }

    @Test
    void testSlideWithImage() {
        Resource resource = context.create().resource(
            "/content/slide-with-image",
            "sling:resourceType", "hiero-design/components/carousel/slide",
            "desktopImage", "/content/dam/carousel/slide1-desktop.jpg",
            "mobileImage", "/content/dam/carousel/slide1-mobile.jpg",
            "heading", "Slide Heading",
            "description", "Slide description text",
            "ctaLabel", "Learn More",
            "ctaLink", "/content/page",
            "ctaTarget", "_blank"
        );

        slide = resource.adaptTo(HeroCarouselSlide.class);
        assertNotNull(slide);
        assertTrue(slide.hasImage());
        assertFalse(slide.hasVideo());
    }

    @Test
    void testSlideWithVideo() {
        Resource resource = context.create().resource(
            "/content/slide-with-video",
            "sling:resourceType", "hiero-design/components/carousel/slide",
            "desktopVideo", "/content/dam/carousel/video.mp4",
            "heading", "Video Slide",
            "description", "Video slide description",
            "ctaLabel", "Watch",
            "ctaLink", "/content/video-page",
            "ctaTarget", "_self"
        );

        slide = resource.adaptTo(HeroCarouselSlide.class);
        assertNotNull(slide);
        assertTrue(slide.hasVideo());
    }

    @Test
    void testSlideWithContent() {
        Resource resource = context.create().resource(
            "/content/slide-with-content",
            "sling:resourceType", "hiero-design/components/carousel/slide",
            "heading", "Content Slide",
            "description", "Content slide description",
            "ctaLabel", "Click Here",
            "ctaLink", "/content/details",
            "ctaTarget", "_blank"
        );

        slide = resource.adaptTo(HeroCarouselSlide.class);
        assertNotNull(slide);
        assertTrue(slide.hasContent());
        assertNotNull(slide.getHeading());
        assertNotNull(slide.getCtaLabel());
    }

    @Test
    void testSlideProperties() {
        Resource resource = context.create().resource(
            "/content/slide-properties",
            "sling:resourceType", "hiero-design/components/carousel/slide",
            "desktopImage", "/content/dam/carousel/slide1-desktop.jpg",
            "mobileImage", "/content/dam/carousel/slide1-mobile.jpg",
            "heading", "Slide Heading",
            "description", "Slide description text",
            "ctaLabel", "Learn More",
            "ctaLink", "/content/page",
            "ctaTarget", "_blank"
        );

        slide = resource.adaptTo(HeroCarouselSlide.class);

        assertNotNull(slide.getHeading());
        assertNotNull(slide.getDescription());
        assertNotNull(slide.getCtaLabel());
        assertNotNull(slide.getCtaLink());
        assertNotNull(slide.getCtaTarget());
    }

    @Test
    void testMissingMobileImage() {
        Resource resource = context.create().resource(
            "/content/slide-desktop-only",
            "sling:resourceType", "hiero-design/components/carousel/slide",
            "desktopImage", "/content/dam/carousel/desktop-only.jpg",
            "heading", "Desktop Only Slide",
            "description", "Desktop only slide description",
            "ctaLabel", "View",
            "ctaLink", "/content/desktop",
            "ctaTarget", "_self"
        );

        slide = resource.adaptTo(HeroCarouselSlide.class);

        assertNotNull(slide.getDesktopImage());
        assertNull(slide.getMobileImage());
        assertTrue(slide.hasImage());
    }
}
