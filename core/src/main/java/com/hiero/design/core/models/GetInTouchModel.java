package com.hiero.design.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Sling Model for the "Get in Touch" component: renders the ATM/Branch
 * Locator summary and the Contact Us details (phone numbers, email, CTAs).
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class GetInTouchModel {

    @SlingObject
    private Resource resource;

    @ValueMapValue
    private String heading;

    @ValueMapValue
    private String locatorTitle;

    @ValueMapValue
    private String locatorDescription;

    @ValueMapValue
    private String locatorImageReference;

    @ValueMapValue
    private String locatorImageAlt;

    @ValueMapValue
    private String locatorCtaLabel;

    @ValueMapValue
    private String locatorCtaLink;

    @ValueMapValue
    private String contactCtaLabel;

    @ValueMapValue
    private String contactCtaLink;

    @ValueMapValue
    private String email;

    @ChildResource
    private Resource statistics;

    @ChildResource
    private Resource phoneNumbers;

    private List<Statistic> statisticList;

    private List<PhoneNumber> phoneNumberList;

    @PostConstruct
    protected void init() {
        statisticList = toList(statistics, Statistic.class);
        phoneNumberList = toList(phoneNumbers, PhoneNumber.class);
    }

    private <T> List<T> toList(Resource container, Class<T> type) {
        if (container == null) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>();
        container.getChildren().forEach(child -> {
            T item = child.adaptTo(type);
            if (item != null) {
                result.add(item);
            }
        });
        return result;
    }

    /**
     * Stable, resource-path derived identifier so multiple instances of this
     * component on the same page never collide on generated markup ids.
     */
    public String getId() {
        if (resource == null) {
            return "getintouch";
        }
        return "getintouch-" + Integer.toHexString(resource.getPath().hashCode());
    }

    public String getHeading() {
        return StringUtils.defaultString(heading);
    }

    public String getLocatorTitle() {
        return StringUtils.defaultString(locatorTitle);
    }

    public String getLocatorDescription() {
        return StringUtils.defaultString(locatorDescription);
    }

    public String getLocatorImageReference() {
        return StringUtils.defaultString(locatorImageReference);
    }

    public String getLocatorImageAlt() {
        return StringUtils.defaultString(locatorImageAlt);
    }

    public boolean isLocatorImagePresent() {
        return StringUtils.isNotBlank(locatorImageReference);
    }

    public String getLocatorCtaLabel() {
        return StringUtils.defaultString(locatorCtaLabel);
    }

    public String getLocatorCtaLink() {
        return StringUtils.defaultString(locatorCtaLink);
    }

    public boolean isLocatorCtaPresent() {
        return StringUtils.isNotBlank(locatorCtaLabel) && StringUtils.isNotBlank(locatorCtaLink);
    }

    public String getContactCtaLabel() {
        return StringUtils.defaultString(contactCtaLabel);
    }

    public String getContactCtaLink() {
        return StringUtils.defaultString(contactCtaLink);
    }

    public boolean isContactCtaPresent() {
        return StringUtils.isNotBlank(contactCtaLabel) && StringUtils.isNotBlank(contactCtaLink);
    }

    public String getEmail() {
        return StringUtils.defaultString(email);
    }

    public boolean isEmailPresent() {
        return StringUtils.isNotBlank(email);
    }

    public List<Statistic> getStatistics() {
        return statisticList == null ? Collections.emptyList() : statisticList;
    }

    public boolean isStatisticsPresent() {
        return !getStatistics().isEmpty();
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumberList == null ? Collections.emptyList() : phoneNumberList;
    }

    public boolean isPhoneNumbersPresent() {
        return !getPhoneNumbers().isEmpty();
    }

    public boolean isContactDetailsPresent() {
        return isPhoneNumbersPresent() || isEmailPresent() || isContactCtaPresent();
    }

    @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class Statistic {

        @ValueMapValue
        private String value;

        @ValueMapValue
        private String label;

        public String getValue() {
            return StringUtils.defaultString(value);
        }

        public String getLabel() {
            return StringUtils.defaultString(label);
        }

        public boolean isPresent() {
            return StringUtils.isNotBlank(value) || StringUtils.isNotBlank(label);
        }
    }

    @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class PhoneNumber {

        @ValueMapValue
        private String label;

        @ValueMapValue
        private String number;

        public String getLabel() {
            return StringUtils.defaultString(label);
        }

        public String getNumber() {
            return StringUtils.defaultString(number);
        }

        public String getTelHref() {
            return "tel:" + getNumber().replaceAll("[^+0-9]", "");
        }

        public boolean isPresent() {
            return StringUtils.isNotBlank(number);
        }
    }
}
