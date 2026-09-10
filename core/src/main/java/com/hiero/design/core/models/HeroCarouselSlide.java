package com.hiero.design.core.models;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class HeroCarouselSlide {
    private static final Logger LOG = LoggerFactory.getLogger(HeroCarouselSlide.class);
    private Resource resource;
    private ValueMap properties;
    
    public HeroCarouselSlide(Resource resource) {
        this.resource = resource;
        this.properties = resource.getValueMap();
    }
    
    public String getHeading() {
        return properties.get("heading", String.class);
    }
    
    public String getDescription() {
        return properties.get("description", String.class);
    }
    
    public String getHighlightText() {
        return properties.get("highlightText", String.class);
    }
    
    public String getCtaLabel() {
        return properties.get("ctaLabel", String.class);
    }
    
    public String getCtaUrl() {
        String ctaLink = properties.get("ctaLink", String.class);
        if (StringUtils.isNotBlank(ctaLink)) {
            if (ctaLink.startsWith("/")) {
                return ctaLink + ".html";
            }
            return ctaLink;
        }
        return null;
    }
    
    public String getDisclaimer() {
        return properties.get("disclaimer", String.class);
    }
    
    public String getDesktopImageUrl() {
        return getAssetUrl("desktopImage");
    }
    
    public String getMobileImageUrl() {
        return getAssetUrl("mobileImage");
    }
    
    public String getDesktopVideoUrl() {
        return getAssetUrl("desktopVideo");
    }
    
    public String getMobileVideoUrl() {
        return getAssetUrl("mobileVideo");
    }
    
    private String getAssetUrl(String propertyName) {
        String assetPath = properties.get(propertyName, String.class);
        if (StringUtils.isNotBlank(assetPath)) {
            try {
                Resource assetResource = resource.getResourceResolver().getResource(assetPath);
                if (assetResource != null) {
                    Asset asset = DamUtil.resolveToAsset(assetResource);
                    if (asset != null) {
                        return asset.getPath() + "/jcr:content/renditions/original";
                    }
                }
            } catch (Exception e) {
                LOG.debug("Could not resolve asset: " + assetPath, e);
            }
        }
        return null;
    }
    
    public boolean isValid() {
        return StringUtils.isNotBlank(getHeading()) && 
               (StringUtils.isNotBlank(getDesktopImageUrl()) || StringUtils.isNotBlank(getDesktopVideoUrl()));
    }
}