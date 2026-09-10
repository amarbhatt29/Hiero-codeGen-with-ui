package com.hiero.design.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = Resource.class)
public class HeroCarouselModel {
    private static final Logger LOG = LoggerFactory.getLogger(HeroCarouselModel.class);
    private static final String SLIDE_RESOURCE_TYPE = "hiero-design/components/hero-carousel/slide";
    
    private Resource resource;
    private List<HeroCarouselSlide> slides;
    
    @ValueMapValue
    private Boolean autoRotate;
    
    @ValueMapValue
    private Long autoRotateInterval;
    
    public HeroCarouselModel(Resource resource) {
        this.resource = resource;
        this.autoRotate = resource.getValueMap().get("autoRotate", Boolean.FALSE);
        this.autoRotateInterval = resource.getValueMap().get("autoRotateInterval", 5L);
    }
    
    public List<HeroCarouselSlide> getSlides() {
        if (slides == null) {
            slides = new ArrayList<>();
            Resource slidesContainer = resource.getChild("slides");
            
            if (slidesContainer != null) {
                Iterator<Resource> slideIterator = slidesContainer.listChildren();
                while (slideIterator.hasNext()) {
                    Resource slideResource = slideIterator.next();
                    HeroCarouselSlide slide = new HeroCarouselSlide(slideResource);
                    if (slide.isValid()) {
                        slides.add(slide);
                    }
                }
            }
        }
        return slides.isEmpty() ? null : slides;
    }
    
    public Boolean getAutoRotate() {
        return autoRotate != null && autoRotate;
    }
    
    public Long getAutoRotateInterval() {
        return autoRotateInterval != null ? autoRotateInterval : 5L;
    }
    
    public int getSlideCount() {
        return getSlides() != null ? getSlides().size() : 0;
    }
}