(function() {
  class HeroCarousel {
    constructor(container) {
      this.container = container;
      this.viewport = container.querySelector('.hero-carousel__viewport');
      this.slides = Array.from(container.querySelectorAll('.hero-carousel__slide'));
      this.prevBtn = container.querySelector('.hero-carousel__button--prev');
      this.nextBtn = container.querySelector('.hero-carousel__button--next');
      this.dots = Array.from(container.querySelectorAll('.hero-carousel__dot'));
      this.config = this.parseConfig();
      
      this.currentIndex = 0;
      this.autoRotateInterval = null;
      this.prefersReducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
      
      if (this.slides.length > 1) {
        this.init();
      }
    }
    
    parseConfig() {
      const configEl = this.container.querySelector('.hero-carousel__config');
      if (configEl) {
        try {
          return JSON.parse(configEl.textContent);
        } catch (e) {
          console.warn('Failed to parse carousel config', e);
        }
      }
      return {};
    }
    
    init() {
      this.setActiveSlide(0);
      this.attachEventListeners();
      this.startAutoRotate();
    }
    
    attachEventListeners() {
      if (this.prevBtn) {
        this.prevBtn.addEventListener('click', () => this.prevSlide());
      }
      if (this.nextBtn) {
        this.nextBtn.addEventListener('click', () => this.nextSlide());
      }
      
      this.dots.forEach((dot, index) => {
        dot.addEventListener('click', () => this.goToSlide(index));
      });
      
      document.addEventListener('keydown', (e) => this.handleKeyboard(e));
      this.container.addEventListener('mouseenter', () => this.stopAutoRotate());
      this.container.addEventListener('mouseleave', () => this.startAutoRotate());
      
      this.attachTouchListeners();
    }
    
    handleKeyboard(e) {
      if (e.key === 'ArrowLeft') {
        this.prevSlide();
      } else if (e.key === 'ArrowRight') {
        this.nextSlide();
      }
    }
    
    attachTouchListeners() {
      let touchStartX = 0;
      let touchEndX = 0;
      
      this.viewport.addEventListener('touchstart', (e) => {
        touchStartX = e.changedTouches[0].screenX;
      }, false);
      
      this.viewport.addEventListener('touchend', (e) => {
        touchEndX = e.changedTouches[0].screenX;
        this.handleSwipe(touchStartX, touchEndX);
      }, false);
    }
    
    handleSwipe(startX, endX) {
      const threshold = 50;
      const diff = startX - endX;
      
      if (Math.abs(diff) > threshold) {
        if (diff > 0) {
          this.nextSlide();
        } else {
          this.prevSlide();
        }
      }
    }
    
    setActiveSlide(index) {
      this.slides.forEach((slide, i) => {
        slide.classList.remove('is-active', 'is-prev');
        if (i === index) {
          slide.classList.add('is-active');
        } else {
          slide.classList.add('is-prev');
        }
      });
      
      this.dots.forEach((dot, i) => {
        dot.classList.toggle('is-active', i === index);
        dot.setAttribute('aria-pressed', i === index ? 'true' : 'false');
      });
      
      this.currentIndex = index;
      
      if (this.prevBtn) {
        this.prevBtn.disabled = index === 0 && this.slides.length <= 1;
      }
      if (this.nextBtn) {
        this.nextBtn.disabled = index === this.slides.length - 1 && this.slides.length <= 1;
      }
    }
    
    nextSlide() {
      this.stopAutoRotate();
      const nextIndex = (this.currentIndex + 1) % this.slides.length;
      this.setActiveSlide(nextIndex);
      this.trackEvent('next');
      this.startAutoRotate();
    }
    
    prevSlide() {
      this.stopAutoRotate();
      const prevIndex = (this.currentIndex - 1 + this.slides.length) % this.slides.length;
      this.setActiveSlide(prevIndex);
      this.trackEvent('prev');
      this.startAutoRotate();
    }
    
    goToSlide(index) {
      if (index !== this.currentIndex) {
        this.stopAutoRotate();
        this.setActiveSlide(index);
        this.trackEvent('dot', index);
        this.startAutoRotate();
      }
    }
    
    startAutoRotate() {
      if (this.config.autoRotateInterval && !this.prefersReducedMotion) {
        if (this.autoRotateInterval) {
          clearInterval(this.autoRotateInterval);
        }
        this.autoRotateInterval = setInterval(() => {
          this.nextSlide();
        }, this.config.autoRotateInterval * 1000);
      }
    }
    
    stopAutoRotate() {
      if (this.autoRotateInterval) {
        clearInterval(this.autoRotateInterval);
        this.autoRotateInterval = null;
      }
    }
    
    trackEvent(action, value) {
      const ctaLink = this.slides[this.currentIndex]?.querySelector('.hero-carousel__cta');
      if (window.dataLayer && ctaLink) {
        window.dataLayer.push({
          event: 'hero_carousel_interaction',
          action: action,
          slide_index: this.currentIndex,
          slide_value: value,
          cta_destination: ctaLink.href
        });
      }
    }
    
    destroy() {
      this.stopAutoRotate();
      if (this.prevBtn) this.prevBtn.removeEventListener('click', () => this.prevSlide());
      if (this.nextBtn) this.nextBtn.removeEventListener('click', () => this.nextSlide());
      document.removeEventListener('keydown', (e) => this.handleKeyboard(e));
    }
  }
  
  // Auto-initialize all carousel instances
  function initCarousels() {
    document.querySelectorAll('.hero-carousel').forEach(container => {
      if (!container.dataset.carouselInitialized) {
        new HeroCarousel(container);
        container.dataset.carouselInitialized = 'true';
      }
    });
  }
  
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initCarousels);
  } else {
    initCarousels();
  }
  
  // Support for dynamic carousel loading
  if (window.MutationObserver) {
    const observer = new MutationObserver(() => {
      initCarousels();
    });
    observer.observe(document.body, { childList: true, subtree: true });
  }
})();