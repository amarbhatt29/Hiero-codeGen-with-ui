package com.hiero.design.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeroCarouselSlide {

    @ValueMapValue
    private String desktopImage;

    @ValueMapValue
    private String mobileImage;

    @ValueMapValue
    private String desktopVideo;

    @ValueMapValue
    private String mobileVideo;

    @ValueMapValue
    private String heading;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String rateHighlight;

    @ValueMapValue
    private String ctaLabel;

    @ValueMapValue
    private String ctaLink;

    @ValueMapValue
    private Boolean ctaNewTab;

    @ValueMapValue
    private String disclaimer;

    public String getDesktopImage() {
        return desktopImage;
    }

    public String getMobileImage() {
        return mobileImage;
    }

    public String getDesktopVideo() {
        return desktopVideo;
    }

    public String getMobileVideo() {
        return mobileVideo;
    }

    public String getHeading() {
        return heading;
    }

    public String getDescription() {
        return description;
    }

    public String getRateHighlight() {
        return rateHighlight;
    }

    public String getCtaLabel() {
        return ctaLabel;
    }

    public String getCtaLink() {
        return ctaLink;
    }

    public Boolean getCtaNewTab() {
        return ctaNewTab != null ? ctaNewTab : false;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public Boolean hasHeading() {
        return heading != null && !heading.trim().isEmpty();
    }

    public Boolean hasDescription() {
        return description != null && !description.trim().isEmpty();
    }

    public Boolean hasRateHighlight() {
        return rateHighlight != null && !rateHighlight.trim().isEmpty();
    }

    public Boolean hasCta() {
        return ctaLabel != null && !ctaLabel.trim().isEmpty() && ctaLink != null && !ctaLink.trim().isEmpty();
    }

    public Boolean hasDisclaimer() {
        return disclaimer != null && !disclaimer.trim().isEmpty();
    }
}
