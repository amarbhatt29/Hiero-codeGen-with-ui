package com.hiero.design.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.cq.wcm.core.components.models.Image;

@Model(adaptables = SlingHttpServletRequest.class, adapters = GlobalHeaderModel.class)
public class GlobalHeaderModel {

    @Self
    private SlingHttpServletRequest request;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "")
    private String logoPath;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "YES BANK")
    private String logoAltText;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(booleanValues = false)
    private boolean enableSticky;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "")
    private String navigationReference;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(booleanValues = true)
    private boolean enableSearch;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(booleanValues = true)
    private boolean enableMobileMenu;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "/bin/login")
    private String loginLink;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "/bin/logout")
    private String logoutLink;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "/help")
    private String helpLink;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(booleanValues = false)
    private boolean isAuthenticated;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "")
    private String profileName;

    public String getLogoPath() {
        return logoPath;
    }

    public String getLogoAltText() {
        return logoAltText != null && !logoAltText.isEmpty() ? logoAltText : "YES BANK";
    }

    public boolean isEnableSticky() {
        return enableSticky;
    }

    public String getNavigationReference() {
        return navigationReference;
    }

    public boolean isEnableSearch() {
        return enableSearch;
    }

    public boolean isEnableMobileMenu() {
        return enableMobileMenu;
    }

    public String getLoginLink() {
        return loginLink;
    }

    public String getLogoutLink() {
        return logoutLink;
    }

    public String getHelpLink() {
        return helpLink;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public String getProfileName() {
        return profileName;
    }

    public boolean hasLogoImage() {
        return logoPath != null && !logoPath.isEmpty();
    }
}
