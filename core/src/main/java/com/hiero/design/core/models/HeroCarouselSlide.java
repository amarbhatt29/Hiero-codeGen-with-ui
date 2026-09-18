package com.hiero.design.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
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
    private String ctaTarget;

    @ValueMapValue
    private String legalCopy;

    @ValueMapValue
    private String slideOrder;

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

    public String getCtaTarget() {
        return ctaTarget;
    }

    public String getLegalCopy() {
        return legalCopy;
    }

    public String getSlideOrder() {
        return slideOrder;
    }

    public boolean hasVideo() {
        return (desktopVideo != null && !desktopVideo.isEmpty()) ||
               (mobileVideo != null && !mobileVideo.isEmpty());
    }

    public boolean hasImage() {
        return (desktopImage != null && !desktopImage.isEmpty()) ||
               (mobileImage != null && !mobileImage.isEmpty());
    }

    public boolean hasContent() {
        return (heading != null && !heading.isEmpty()) ||
               (description != null && !description.isEmpty()) ||
               (ctaLabel != null && !ctaLabel.isEmpty());
    }
}
