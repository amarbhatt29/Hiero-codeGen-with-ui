package com.hiero.design.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injected.Self;
import org.apache.sling.models.annotations.injected.ValueMapValue;

import javax.annotation.Nonnull;
import java.util.Optional;

@Model(
    adaptables = {SlingHttpServletRequest.class},
    adapters = {GlobalHeaderModel.class, ComponentExporter.class},
    resourceType = {"hiero-design/components/header"},
    defaultInjectionStrategy = org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL
)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXPORTER_EXTENSION_JSON
)
public class GlobalHeaderModel extends AbstractComponentImpl implements ComponentExporter {

    @Self
    private SlingHttpServletRequest request;

    @ValueMapValue
    private String logoPath;

    @ValueMapValue
    private String logoAlt;

    @ValueMapValue
    private String logoLink;

    @ValueMapValue
    private String navigationPath;

    @ValueMapValue
    private String navigationLabel;

    @ValueMapValue
    private Boolean enableSearch;

    @ValueMapValue
    private String searchAction;

    @ValueMapValue
    private String searchLabel;

    @ValueMapValue
    private String languageLink;

    @ValueMapValue
    private String languageLabel;

    @ValueMapValue
    private String helpLink;

    @ValueMapValue
    private String helpLabel;

    @ValueMapValue
    private String loginLink;

    @ValueMapValue
    private String loginLabel;

    @ValueMapValue
    private Boolean enableSticky;

    @ValueMapValue
    private String stickyBehavior;

    public String getLogoPath() {
        return logoPath;
    }

    public String getLogoAlt() {
        return logoAlt != null ? logoAlt : "Logo";
    }

    public String getLogoLink() {
        return logoLink != null ? logoLink : "/";
    }

    public String getNavigationPath() {
        return navigationPath;
    }

    public String getNavigationLabel() {
        return navigationLabel != null ? navigationLabel : "Main Navigation";
    }

    public Boolean isSearchEnabled() {
        return enableSearch != null ? enableSearch : true;
    }

    public String getSearchAction() {
        return searchAction;
    }

    public String getSearchLabel() {
        return searchLabel != null ? searchLabel : "Search";
    }

    public String getLanguageLink() {
        return languageLink;
    }

    public String getLanguageLabel() {
        return languageLabel != null ? languageLabel : "Select Language";
    }

    public String getHelpLink() {
        return helpLink;
    }

    public String getHelpLabel() {
        return helpLabel != null ? helpLabel : "Help and Support";
    }

    public String getLoginLink() {
        return loginLink;
    }

    public String getLoginLabel() {
        return loginLabel != null ? loginLabel : "Login";
    }

    public Boolean isStickyEnabled() {
        return enableSticky != null ? enableSticky : false;
    }

    public String getStickyBehavior() {
        return stickyBehavior != null ? stickyBehavior : "sticky";
    }

    @JsonIgnore
    public boolean isValid() {
        return logoPath != null && !logoPath.isEmpty() && 
               navigationPath != null && !navigationPath.isEmpty() && 
               loginLink != null && !loginLink.isEmpty();
    }

    @Nonnull
    @Override
    public String getExportedType() {
        return request.getResource().getResourceType();
    }
}
