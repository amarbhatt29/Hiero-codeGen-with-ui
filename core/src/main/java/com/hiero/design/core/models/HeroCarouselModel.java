package com.hiero.design.core.models;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.day.cq.wcm.api.Page;

@Model(
    adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class HeroCarouselModel {

    @ScriptVariable
    private ResourceResolver resourceResolver;

    @ValueMapValue
    private String autoplayEnabled;

    @ValueMapValue
    private String autoplayInterval;

    @ValueMapValue
    private String showNavigation;

    private List<HeroCarouselSlide> slides;

    public List<HeroCarouselSlide> getSlides() {
        if (slides == null) {
            slides = getChildSlides();
        }
        return slides;
    }

    private List<HeroCarouselSlide> getChildSlides() {
        Resource resource = this.resourceResolver.getResource(
            this.resourceResolver.adaptTo(org.apache.sling.api.resource.ResourceResolver.class)
                .getSearchPath()[0] + "/../hero-carousel/slides"
        );
        if (resource == null) {
            return Collections.emptyList();
        }

        return StreamSupport
            .stream(resource.getChildren().spliterator(), false)
            .map(r -> r.adaptTo(HeroCarouselSlide.class))
            .filter(slide -> slide != null)
            .collect(Collectors.toList());
    }

    public boolean isAutoplayEnabled() {
        return "true".equals(autoplayEnabled);
    }

    public String getAutoplayInterval() {
        return autoplayInterval != null ? autoplayInterval : "5000";
    }

    public boolean isShowNavigation() {
        return !"false".equals(showNavigation);
    }

    public boolean isEmpty() {
        return getSlides().isEmpty();
    }
}
