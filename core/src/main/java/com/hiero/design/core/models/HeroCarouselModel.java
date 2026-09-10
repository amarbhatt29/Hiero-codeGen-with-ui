package com.hiero.design.core.models;

import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import com.adobe.cq.wcm.core.components.models.Component;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeroCarouselModel implements Component {

    @ValueMapValue
    private String jcr_title;

    @ChildResource(name = "slides")
    private Resource slidesResource;

    @ValueMapValue
    private Boolean autoRotate;

    @ValueMapValue
    private Long autoRotateInterval;

    private List<HeroCarouselSlide> slides;

    public List<HeroCarouselSlide> getSlides() {
        if (slides == null) {
            slides = new ArrayList<>();
            if (slidesResource != null) {
                for (Resource child : slidesResource.getChildren()) {
                    HeroCarouselSlide slide = child.adaptTo(HeroCarouselSlide.class);
                    if (slide != null) {
                        slides.add(slide);
                    }
                }
            }
        }
        return slides;
    }

    public boolean hasSlides() {
        return !getSlides().isEmpty();
    }

    public int getSlideCount() {
        return getSlides().size();
    }

    public boolean shouldAutoRotate() {
        return autoRotate != null && autoRotate && getSlideCount() > 1;
    }

    public long getAutoRotateInterval() {
        return autoRotateInterval != null ? autoRotateInterval : 5000;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public ComponentData getComponentData() {
        return null;
    }
}
