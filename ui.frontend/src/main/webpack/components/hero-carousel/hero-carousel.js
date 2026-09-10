class HeroCarousel {
  constructor(element) {
    this.element = element;
    this.track = element.querySelector('.hero-carousel__slides');
    this.slides = element.querySelectorAll('.hero-carousel__slide');
    this.prevBtn = element.querySelector('[data-hero-carousel-prev]');
    this.nextBtn = element.querySelector('[data-hero-carousel-next]');
    this.indicators = element.querySelectorAll('.hero-carousel__indicator');

    this.currentIndex = 0;
    this.autoplayEnabled = element.dataset.autoplay === 'true';
    this.autoplayInterval = parseInt(element.dataset.autoplayInterval, 10) || 5000;
    this.autoplayTimer = null;
    this.touchStartX = 0;
    this.touchEndX = 0;
    this.isTransitioning = false;

    if (this.slides.length <= 1) {
      this.element.style.display = 'none';
      return;
    }

    this.init();
  }

  init() {
    this.attachEventListeners();
    this.startAutoplay();
  }

  attachEventListeners() {
    if (this.prevBtn) {
      this.prevBtn.addEventListener('click', () => this.prev());
    }

    if (this.nextBtn) {
      this.nextBtn.addEventListener('click', () => this.next());
    }

    this.indicators.forEach((indicator, index) => {
      indicator.addEventListener('click', () => this.goToSlide(index));
      indicator.addEventListener('keydown', (e) => {
        if (e.key === 'ArrowLeft' && index > 0) {
          e.preventDefault();
          this.goToSlide(index - 1);
        } else if (e.key === 'ArrowRight' && index < this.slides.length - 1) {
          e.preventDefault();
          this.goToSlide(index + 1);
        }
      });
    });

    this.slides.forEach((slide) => {
      slide.addEventListener('keydown', (e) => {
        if (e.key === 'ArrowLeft') {
          e.preventDefault();
          this.prev();
        } else if (e.key === 'ArrowRight') {
          e.preventDefault();
          this.next();
        }
      });
    });

    this.element.addEventListener('touchstart', (e) => {
      this.touchStartX = e.changedTouches[0].screenX;
    });

    this.element.addEventListener('touchend', (e) => {
      this.touchEndX = e.changedTouches[0].screenX;
      this.handleSwipe();
    });

    this.element.addEventListener('mouseenter', () => {
      this.stopAutoplay();
    });

    this.element.addEventListener('mouseleave', () => {
      this.startAutoplay();
    });

    window.addEventListener('focus', () => {
      if (this.autoplayEnabled) {
        this.startAutoplay();
      }
    });

    window.addEventListener('blur', () => {
      this.stopAutoplay();
    });
  }

  handleSwipe() {
    const swipeThreshold = 50;
    const diff = this.touchStartX - this.touchEndX;

    if (Math.abs(diff) > swipeThreshold) {
      if (diff > 0) {
        this.next();
      } else {
        this.prev();
      }
    }
  }

  updateSlidePosition() {
    if (this.isTransitioning) return;

    this.isTransitioning = true;
    const offset = -this.currentIndex * 100;
    this.track.style.transform = `translateX(${offset}%)`;

    setTimeout(() => {
      this.isTransitioning = false;
    }, 600);

    this.updateSlideStates();
    this.fireAnalyticsEvent();
  }

  updateSlideStates() {
    this.slides.forEach((slide, index) => {
      if (index === this.currentIndex) {
        slide.setAttribute('tabindex', '0');
        slide.setAttribute('aria-label', `Slide ${index + 1} of ${this.slides.length}`);
        slide.focus();
      } else {
        slide.setAttribute('tabindex', '-1');
      }
    });

    this.indicators.forEach((indicator, index) => {
      const isActive = index === this.currentIndex;
      indicator.setAttribute('aria-selected', isActive);
      indicator.setAttribute('tabindex', isActive ? '0' : '-1');
    });
  }

  prev() {
    this.stopAutoplay();
    this.currentIndex = (this.currentIndex - 1 + this.slides.length) % this.slides.length;
    this.updateSlidePosition();
    this.startAutoplay();
  }

  next() {
    this.stopAutoplay();
    this.currentIndex = (this.currentIndex + 1) % this.slides.length;
    this.updateSlidePosition();
    this.startAutoplay();
  }

  goToSlide(index) {
    this.stopAutoplay();
    this.currentIndex = Math.max(0, Math.min(index, this.slides.length - 1));
    this.updateSlidePosition();
    this.startAutoplay();
  }

  startAutoplay() {
    if (!this.autoplayEnabled || this.autoplayTimer) {
      return;
    }

    if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
      return;
    }

    this.autoplayTimer = setInterval(() => {
      this.currentIndex = (this.currentIndex + 1) % this.slides.length;
      this.updateSlidePosition();
    }, this.autoplayInterval);
  }

  stopAutoplay() {
    if (this.autoplayTimer) {
      clearInterval(this.autoplayTimer);
      this.autoplayTimer = null;
    }
  }

  fireAnalyticsEvent() {
    const slide = this.slides[this.currentIndex];
    const heading = slide.querySelector('.hero-carousel__heading')?.textContent || '';
    const cta = slide.querySelector('[data-analytics-label]')?.getAttribute('data-analytics-label') || '';

    if (window.dataLayer) {
      window.dataLayer.push({
        event: 'hero_carousel_slide_viewed',
        slideIndex: this.currentIndex,
        slideHeading: heading,
        ctaLabel: cta,
      });
    }
  }

  destroy() {
    this.stopAutoplay();
    // Event listeners cleanup would require storing references
  }
}

export function initHeroCarousels() {
  const carousels = document.querySelectorAll('[data-hero-carousel]');
  const instances = new Map();

  carousels.forEach((carousel) => {
    const instance = new HeroCarousel(carousel);
    instances.set(carousel, instance);
  });

  // Cleanup on page unload
  window.addEventListener('beforeunload', () => {
    instances.forEach((instance) => {
      instance.destroy();
    });
    instances.clear();
  });
}

// Auto-initialize when DOM is ready
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', initHeroCarousels);
} else {
  initHeroCarousels();
}
