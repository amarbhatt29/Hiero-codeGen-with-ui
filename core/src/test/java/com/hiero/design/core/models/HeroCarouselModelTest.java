package com.hiero.design.core.models;

import static org.junit.jupiter.api.Assertions.*;
import com.hiero.design.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;

@ExtendWith(AemContextExtension.class)
class HeroCarouselModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(HeroCarouselModel.class, HeroCarouselSlide.class);
    }

    @Test
    void testHeroCarouselWithNoSlides() {
        Resource resource = context.create().resource("/content/test/hero-carousel",
            "sling:resourceType", "hiero-design/components/hero-carousel",
            "enableAutoRotate", false
        );

        HeroCarouselModel model = resource.adaptTo(HeroCarouselModel.class);
        assertNotNull(model);
        assertFalse(model.hasSlides());
        assertTrue(model.getSlides().isEmpty());
    }

    @Test
    void testHeroCarouselWithSingleSlide() {
        Resource resource = context.create().resource("/content/test/hero-carousel",
            "sling:resourceType", "hiero-design/components/hero-carousel",
            "enableAutoRotate", true,
            "autoRotateInterval", 5000L
        );

        context.create().resource(resource, "slides/item0",
            "heading", "Test Heading",
            "description", "Test Description",
            "ctaLabel", "Click Me",
            "ctaLink", "/content/test/page"
        );

        HeroCarouselModel model = resource.adaptTo(HeroCarouselModel.class);
        assertNotNull(model);
        assertTrue(model.hasSlides());
        assertEquals(1, model.getSlides().size());
        assertTrue(model.getEnableAutoRotate());
        assertEquals(5000L, model.getAutoRotateInterval());
    }

    @Test
    void testHeroCarouselDefaultAutoRotateInterval() {
        Resource resource = context.create().resource("/content/test/hero-carousel",
            "sling:resourceType", "hiero-design/components/hero-carousel"
        );

        HeroCarouselModel model = resource.adaptTo(HeroCarouselModel.class);
        assertNotNull(model);
        assertEquals(5000L, model.getAutoRotateInterval());
    }

    @Test
    void testSlideWithAllFields() {
        Resource resource = context.create().resource("/content/test/hero-carousel",
            "sling:resourceType", "hiero-design/components/hero-carousel"
        );

        context.create().resource(resource, "slides/item0",
            "desktopImage", "/content/dam/image-desktop.jpg",
            "mobileImage", "/content/dam/image-mobile.jpg",
            "heading", "Campaign Heading",
            "description", "Campaign Description",
            "rateHighlight", "5% APR",
            "ctaLabel", "Learn More",
            "ctaLink", "/content/test/offer",
            "ctaNewTab", true,
            "disclaimer", "*Terms and conditions apply"
        );

        HeroCarouselModel model = resource.adaptTo(HeroCarouselModel.class);
        assertTrue(model.hasSlides());
        HeroCarouselSlide slide = model.getSlides().get(0);
        
        assertEquals("/content/dam/image-desktop.jpg", slide.getDesktopImage());
        assertEquals("/content/dam/image-mobile.jpg", slide.getMobileImage());
        assertEquals("Campaign Heading", slide.getHeading());
        assertEquals("Campaign Description", slide.getDescription());
        assertEquals("5% APR", slide.getRateHighlight());
        assertEquals("Learn More", slide.getCtaLabel());
        assertTrue(slide.getCtaNewTab());
        assertTrue(slide.hasDisclaimer());
    }
}
