package com.hiero.design.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

public class HeroCarouselSlide {

    private static final String PN_HEADING = "heading";
    private static final String PN_DESCRIPTION = "description";
    private static final String PN_RATE = "rateText";
    private static final String PN_CTA_LABEL = "ctaLabel";
    private static final String PN_CTA_LINK = "ctaLink";
    private static final String PN_LEGAL = "legalText";
    private static final String PN_DESKTOP_IMAGE = "desktopImage";
    private static final String PN_MOBILE_IMAGE = "mobileImage";
    private static final String PN_VIDEO = "video";
    private static final String PN_VIDEO_POSTER = "videoPoster";
    private static final String PN_ALT_TEXT = "altText";

    private Resource resource;
    private ValueMap valueMap;

    public HeroCarouselSlide(Resource resource) {
        this.resource = resource;
        this.valueMap = resource.getValueMap();
    }

    public String getHeading() {
        return valueMap.get(PN_HEADING, String.class);
    }

    public String getDescription() {
        return valueMap.get(PN_DESCRIPTION, String.class);
    }

    public String getRateText() {
        return valueMap.get(PN_RATE, String.class);
    }

    public String getCtaLabel() {
        return valueMap.get(PN_CTA_LABEL, String.class);
    }

    public String getCtaLink() {
        return valueMap.get(PN_CTA_LINK, String.class);
    }

    public String getLegalText() {
        return valueMap.get(PN_LEGAL, String.class);
    }

    public String getDesktopImage() {
        return valueMap.get(PN_DESKTOP_IMAGE, String.class);
    }

    public String getMobileImage() {
        return valueMap.get(PN_MOBILE_IMAGE, String.class);
    }

    public String getVideo() {
        return valueMap.get(PN_VIDEO, String.class);
    }

    public String getVideoPoster() {
        return valueMap.get(PN_VIDEO_POSTER, String.class);
    }

    public String getAltText() {
        return valueMap.get(PN_ALT_TEXT, String.class);
    }

    public boolean hasVideo() {
        String video = getVideo();
        return video != null && !video.isEmpty();
    }

    public boolean hasMobileImage() {
        String mobileImage = getMobileImage();
        return mobileImage != null && !mobileImage.isEmpty();
    }

    public String getEffectiveImage() {
        String desktopImage = getDesktopImage();
        return desktopImage != null ? desktopImage : "";
    }
}
