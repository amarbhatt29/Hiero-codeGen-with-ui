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
    private String desktopVideo;

    @ValueMapValue
    private String mobileImage;

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
    private String disclaimer;

    @ValueMapValue
    private String imageAltText;

    public String getDesktopImage() {
        return desktopImage;
    }

    public String getDesktopVideo() {
        return desktopVideo;
    }

    public String getMobileImage() {
        return mobileImage;
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

    public String getDisclaimer() {
        return disclaimer;
    }

    public String getImageAltText() {
        return imageAltText;
    }

    public boolean hasDesktopMedia() {
        return desktopImage != null || desktopVideo != null;
    }

    public boolean hasMobileMedia() {
        return mobileImage != null || mobileVideo != null;
    }

    public boolean isVideoSlide() {
        return desktopVideo != null || mobileVideo != null;
    }

    public boolean hasCta() {
        return ctaLabel != null && ctaLink != null;
    }
}
