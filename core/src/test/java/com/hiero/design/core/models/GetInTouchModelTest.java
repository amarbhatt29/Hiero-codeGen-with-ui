package com.hiero.design.core.models;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.hiero.design.core.testcontext.AppAemContext;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AemContextExtension.class)
class GetInTouchModelTest {

    private static final String PAGE = "/content/hiero-design/en";
    private static final String COMPONENT_PATH = PAGE + "/jcr:content/getintouch";
    private static final String EMPTY_COMPONENT_PATH = PAGE + "/jcr:content/getintouchEmpty";

    private final AemContext context = AppAemContext.newAemContext();

    @BeforeEach
    void setUp() {
        context.create().page(PAGE);

        Resource component = context.create().resource(COMPONENT_PATH,
                "sling:resourceType", "hiero-design/components/getintouch",
                "heading", "Get in touch",
                "locatorTitle", "Find a branch or ATM",
                "locatorDescription", "Over 500 branches nationwide",
                "locatorCtaLabel", "Find a branch",
                "locatorCtaLink", "/content/hiero-design/en/locator",
                "email", "support@hiero.com");

        context.create().resource(component.getPath() + "/statistics/item0",
                "value", "500+", "label", "Branches");
        context.create().resource(component.getPath() + "/statistics/item1",
                "value", "1200+", "label", "ATMs");

        context.create().resource(component.getPath() + "/phoneNumbers/item0",
                "label", "Customer Support", "number", "+1 800 555 0100");
        context.create().resource(component.getPath() + "/phoneNumbers/item1",
                "label", "Lost Card", "number", "+1 800 555 0199");

        context.create().resource(EMPTY_COMPONENT_PATH,
                "sling:resourceType", "hiero-design/components/getintouch");
    }

    @Test
    void testFieldsArePopulated() {
        context.currentResource(COMPONENT_PATH);
        GetInTouchModel model = context.request().adaptTo(GetInTouchModel.class);

        assertEquals("Get in touch", model.getHeading());
        assertEquals("Find a branch or ATM", model.getLocatorTitle());
        assertTrue(model.isLocatorCtaPresent());
        assertEquals("/content/hiero-design/en/locator", model.getLocatorCtaLink());
        assertTrue(model.isEmailPresent());
        assertEquals("support@hiero.com", model.getEmail());
    }

    @Test
    void testStatisticsAreCollected() {
        context.currentResource(COMPONENT_PATH);
        GetInTouchModel model = context.request().adaptTo(GetInTouchModel.class);

        List<GetInTouchModel.Statistic> statistics = model.getStatistics();
        assertEquals(2, statistics.size());
        assertEquals("500+", statistics.get(0).getValue());
        assertEquals("Branches", statistics.get(0).getLabel());
    }

    @Test
    void testPhoneNumbersAreCollectedAndFormatted() {
        context.currentResource(COMPONENT_PATH);
        GetInTouchModel model = context.request().adaptTo(GetInTouchModel.class);

        List<GetInTouchModel.PhoneNumber> phoneNumbers = model.getPhoneNumbers();
        assertEquals(2, phoneNumbers.size());
        assertEquals("tel:+18005550100", phoneNumbers.get(0).getTelHref());
        assertTrue(model.isPhoneNumbersPresent());
    }

    @Test
    void testMissingOptionalFieldsDoNotBreak() {
        context.currentResource(EMPTY_COMPONENT_PATH);
        GetInTouchModel model = context.request().adaptTo(GetInTouchModel.class);

        assertFalse(model.isEmailPresent());
        assertFalse(model.isPhoneNumbersPresent());
        assertFalse(model.isStatisticsPresent());
        assertFalse(model.isLocatorCtaPresent());
        assertFalse(model.isContactDetailsPresent());
    }

    @Test
    void testIdIsUniquePerResourcePath() {
        context.currentResource(COMPONENT_PATH);
        GetInTouchModel populated = context.request().adaptTo(GetInTouchModel.class);

        context.currentResource(EMPTY_COMPONENT_PATH);
        GetInTouchModel empty = context.request().adaptTo(GetInTouchModel.class);

        assertFalse(populated.getId().equals(empty.getId()));
    }
}
