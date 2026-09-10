package com.hiero.design.core.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectionstrategy.InjectionStrategyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.wcm.core.components.models.Component;
import com.adobe.cq.wcm.core.components.util.ComponentUtils;

@Model(
    adaptables = {SlingHttpServletRequest.class, Resource.class},
    adapters = {Component.class},
    resourceType = "hiero-design/components/hero-carousel",
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class HeroCarouselModel implements Component {

    private static final Logger LOG = LoggerFactory.getLogger(HeroCarouselModel.class);
    private static final String PN_AUTO_ROTATE = "autoRotate";
    private static final String PN_ROTATION_DELAY = "rotationDelay";
    private static final String PN_SHOW_NAV = "showNavigation";
    private static final String SLIDES_NODE = "slides";

    private Resource resource;
    private ResourceResolver resourceResolver;
    private SlingHttpServletRequest request;
    private List<HeroCarouselSlide> slides;

    public HeroCarouselModel(SlingHttpServletRequest request) {
        this.request = request;
        this.resource = request.getResource();
        this.resourceResolver = request.getResourceResolver();
    }

    public HeroCarouselModel(Resource resource) {
        this.resource = resource;
        this.resourceResolver = resource.getResourceResolver();
    }

    @Override
    public String getExportedType() {
        return resource.getResourceType();
    }

    @Override
    public String getId() {
        return ComponentUtils.getId(resource, null, null);
    }

    public List<HeroCarouselSlide> getSlides() {
        if (slides == null) {
            slides = new ArrayList<>();
            Resource slidesResource = resource.getChild(SLIDES_NODE);

            if (slidesResource != null) {
                for (Resource slideResource : slidesResource.getChildren()) {
                    if (slideResource.isResourceType("nt:unstructured")) {
                        HeroCarouselSlide slide = new HeroCarouselSlide(slideResource);
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

    public boolean isAutoRotate() {
        return resource.getValueMap().get(PN_AUTO_ROTATE, false);
    }

    public int getRotationDelay() {
        return resource.getValueMap().get(PN_ROTATION_DELAY, 5000);
    }

    public boolean isShowNavigation() {
        return resource.getValueMap().get(PN_SHOW_NAV, true);
    }
}
