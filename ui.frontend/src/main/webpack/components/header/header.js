class HeaderComponent {
  constructor(element) {
    this.headerElement = element;
    this.navToggle = element.querySelector('.header__nav-toggle');
    this.navMenu = element.querySelector('.header__nav-menu');
    this.searchForm = element.querySelector('.header__search-form');
    this.stickyBehavior = element.getAttribute('data-sticky-behavior');
    
    this.init();
  }

  init() {
    this.bindEvents();
    this.setupAccessibility();
    this.trackInitialization();
  }

  bindEvents() {
    if (this.navToggle) {
      this.navToggle.addEventListener('click', (e) => this.toggleMobileMenu(e));
      this.navToggle.addEventListener('keydown', (e) => this.handleNavKeydown(e));
    }

    if (this.searchForm) {
      this.searchForm.addEventListener('submit', (e) => this.trackSearchSubmit(e));
    }

    document.addEventListener('click', (e) => this.handleDocumentClick(e));
    
    if (this.headerElement.classList.contains('header--sticky')) {
      window.addEventListener('scroll', () => this.handleStickyScroll());
    }
  }

  toggleMobileMenu(event) {
    event.preventDefault();
    const isExpanded = this.navToggle.getAttribute('aria-expanded') === 'true';
    this.navToggle.setAttribute('aria-expanded', !isExpanded);
    
    if (!isExpanded) {
      this.navMenu.style.display = 'flex';
      this.navToggle.classList.add('header__nav-toggle--active');
      this.trackEvent('header:mobile_menu_opened');
    } else {
      this.navMenu.style.display = 'none';
      this.navToggle.classList.remove('header__nav-toggle--active');
      this.trackEvent('header:mobile_menu_closed');
    }
  }

  handleNavKeydown(event) {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      this.toggleMobileMenu(event);
    }
    if (event.key === 'Escape') {
      if (this.navToggle.getAttribute('aria-expanded') === 'true') {
        this.toggleMobileMenu(event);
      }
    }
  }

  handleDocumentClick(event) {
    if (this.navToggle && this.navMenu) {
      const isClickInsideMenu = this.navMenu.contains(event.target);
      const isClickOnToggle = this.navToggle.contains(event.target);
      
      if (!isClickInsideMenu && !isClickOnToggle && 
          this.navToggle.getAttribute('aria-expanded') === 'true') {
        this.toggleMobileMenu(event);
      }
    }
  }

  handleStickyScroll() {
    if (window.scrollY > 0) {
      this.headerElement.classList.add('header--scrolled');
    } else {
      this.headerElement.classList.remove('header--scrolled');
    }
  }

  trackSearchSubmit(event) {
    const searchInput = event.target.querySelector('.header__search-input');
    const query = searchInput ? searchInput.value : '';
    
    this.trackEvent('header:search_submitted', {
      search_query: query
    });
  }

  setupAccessibility() {
    const links = this.headerElement.querySelectorAll('a');
    links.forEach(link => {
      if (!link.textContent.trim() && !link.hasAttribute('aria-label')) {
        console.warn('Anchor tag missing accessible label:', link);
      }
    });

    const buttons = this.headerElement.querySelectorAll('button');
    buttons.forEach(button => {
      if (!button.textContent.trim() && !button.hasAttribute('aria-label')) {
        console.warn('Button missing accessible label:', button);
      }
    });
  }

  trackEvent(eventName, data = {}) {
    if (typeof window.dataLayer !== 'undefined') {
      window.dataLayer.push({
        event: eventName,
        ...data
      });
    }
  }

  trackInitialization() {
    this.trackEvent('header:initialized', {
      sticky_enabled: this.headerElement.classList.contains('header--sticky'),
      sticky_behavior: this.stickyBehavior
    });
  }
}

/* Initialize all header instances on page */
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', () => {
    initializeHeaders();
  });
} else {
  initializeHeaders();
}

function initializeHeaders() {
  const headers = document.querySelectorAll('[role="banner"].header');
  headers.forEach(headerElement => {
    if (!headerElement.hasAttribute('data-header-initialized')) {
      new HeaderComponent(headerElement);
      headerElement.setAttribute('data-header-initialized', 'true');
    }
  });
}

/* Re-initialize headers when new content is dynamically added */
if (typeof MutationObserver !== 'undefined') {
  const observer = new MutationObserver(() => {
    initializeHeaders();
  });
  
  observer.observe(document.body, {
    childList: true,
    subtree: true
  });
}

export { HeaderComponent };
