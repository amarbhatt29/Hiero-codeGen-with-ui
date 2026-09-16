// Global Header Component JavaScript

(function() {
  'use strict';

  const SELECTORS = {
    header: '[data-component="global-header"]',
    mobileToggle: '[data-mobile-toggle]',
    mobileMenu: '[data-mobile-menu]',
    searchToggle: '[data-search-toggle]',
    searchForm: '[data-search-form]',
    profileToggle: '[data-profile-toggle]',
    profileMenu: '[data-profile-menu]',
  };

  const CLASS_NAMES = {
    isActive: 'is-active',
    isOpen: 'is-open',
  };

  class GlobalHeader {
    constructor(headerElement) {
      this.header = headerElement;
      this.mobileToggle = this.header.querySelector(SELECTORS.mobileToggle);
      this.mobileMenu = this.header.querySelector(SELECTORS.mobileMenu);
      this.searchToggle = this.header.querySelector(SELECTORS.searchToggle);
      this.searchForm = this.header.querySelector(SELECTORS.searchForm);
      this.profileToggle = this.header.querySelector(SELECTORS.profileToggle);
      this.profileMenu = this.header.querySelector(SELECTORS.profileMenu);

      this.init();
    }

    init() {
      this.bindEvents();
      this.setupAccessibility();
    }

    bindEvents() {
      // Mobile menu toggle
      if (this.mobileToggle) {
        this.mobileToggle.addEventListener('click', () => this.toggleMobileMenu());
      }

      // Search toggle
      if (this.searchToggle) {
        this.searchToggle.addEventListener('click', (e) => this.toggleSearch(e));
      }

      // Profile menu toggle
      if (this.profileToggle) {
        this.profileToggle.addEventListener('click', (e) => this.toggleProfile(e));
      }

      // Close menus on outside click
      document.addEventListener('click', (e) => this.handleOutsideClick(e));

      // Close menus on escape key
      document.addEventListener('keydown', (e) => this.handleEscapeKey(e));

      // Close mobile menu on link click
      if (this.mobileMenu) {
        const mobileLinks = this.mobileMenu.querySelectorAll('a');
        mobileLinks.forEach(link => {
          link.addEventListener('click', () => this.closeMobileMenu());
        });
      }
    }

    setupAccessibility() {
      // Ensure proper ARIA attributes
      if (this.mobileToggle && this.mobileMenu) {
        this.mobileToggle.setAttribute('aria-controls', 'mobile-menu');
        this.mobileMenu.id = 'mobile-menu';
      }

      if (this.searchToggle && this.searchForm) {
        this.searchToggle.setAttribute('aria-controls', 'search-form');
        this.searchForm.id = 'search-form';
      }

      if (this.profileToggle && this.profileMenu) {
        this.profileToggle.setAttribute('aria-controls', 'profile-menu');
        this.profileMenu.id = 'profile-menu';
      }
    }

    toggleMobileMenu() {
      const isOpen = this.mobileToggle.getAttribute('aria-expanded') === 'true';
      this.setMobileMenuState(!isOpen);
    }

    setMobileMenuState(isOpen) {
      if (!this.mobileToggle || !this.mobileMenu) return;

      this.mobileToggle.setAttribute('aria-expanded', isOpen);
      this.mobileMenu.setAttribute('aria-hidden', !isOpen);

      if (isOpen) {
        document.body.style.overflow = 'hidden';
      } else {
        document.body.style.overflow = '';
      }
    }

    closeMobileMenu() {
      this.setMobileMenuState(false);
    }

    toggleSearch(e) {
      e.stopPropagation();
      if (!this.searchForm) return;
      this.searchForm.classList.toggle(CLASS_NAMES.isActive);
      if (this.searchForm.classList.contains(CLASS_NAMES.isActive)) {
        const input = this.searchForm.querySelector('input[type="search"]');
        if (input) input.focus();
      }
    }

    toggleProfile(e) {
      e.stopPropagation();
      if (!this.profileMenu) return;
      this.profileMenu.classList.toggle(CLASS_NAMES.isOpen);
    }

    closeProfile() {
      if (this.profileMenu) {
        this.profileMenu.classList.remove(CLASS_NAMES.isOpen);
      }
    }

    closeSearch() {
      if (this.searchForm) {
        this.searchForm.classList.remove(CLASS_NAMES.isActive);
      }
    }

    handleOutsideClick(e) {
      // Close search if clicking outside
      if (this.searchForm && !this.searchForm.contains(e.target) && !this.searchToggle.contains(e.target)) {
        this.closeSearch();
      }

      // Close profile menu if clicking outside
      if (this.profileMenu && !this.profileMenu.contains(e.target) && !this.profileToggle.contains(e.target)) {
        this.closeProfile();
      }
    }

    handleEscapeKey(e) {
      if (e.key === 'Escape') {
        this.closeSearch();
        this.closeProfile();
        this.closeMobileMenu();
      }
    }
  }

  // Initialize header on DOM ready
  function initGlobalHeaders() {
    const headers = document.querySelectorAll(SELECTORS.header);
    headers.forEach(header => {
      new GlobalHeader(header);
    });
  }

  // Initialize on DOM ready
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initGlobalHeaders);
  } else {
    initGlobalHeaders();
  }

  // Support for AEM dynamic components (for author mode)
  if (window.CQ && window.CQ.CoreComponents && window.CQ.CoreComponents.container) {
    window.addEventListener('cq-component-updated', function() {
      initGlobalHeaders();
    });
  }
})();
