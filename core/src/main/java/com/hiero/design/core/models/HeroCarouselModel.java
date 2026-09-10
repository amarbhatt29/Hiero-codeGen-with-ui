package com.hiero.design.core.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeroCarouselModel {

    @ValueMapValue
    private String id;

    @ValueMapValue
    private Long autoRotateInterval;

    @ValueMapValue
    private Boolean enableAutoRotate;

    @ChildResource
    private Resource slides;

    private List<HeroCarouselSlide> slideList;

    @PostConstruct
    protected void init() {
        slideList = new ArrayList<>();
        if (slides != null && slides.hasChildren()) {
            for (Resource child : slides.getChildren()) {
                HeroCarouselSlide slide = child.adaptTo(HeroCarouselSlide.class);
                if (slide != null) {
                    slideList.add(slide);
                }
            }
        }
    }

    public String getId() {
        return id;
    }

    public Long getAutoRotateInterval() {
        return autoRotateInterval != null ? autoRotateInterval : 5000L;
    }

    public Boolean getEnableAutoRotate() {
        return enableAutoRotate != null ? enableAutoRotate : false;
    }

    public List<HeroCarouselSlide> getSlides() {
        return slideList;
    }

    public Boolean hasSlides() {
        return slideList != null && !slideList.isEmpty();
    }
}
