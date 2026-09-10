export class HeroCarousel {
  constructor(element) {
    this.element = element;
    this.container = element.querySelector('.hero-carousel__container');
    this.slides = Array.from(element.querySelectorAll('.hero-carousel__slide'));
    this.buttons = element.querySelectorAll('[data-slide-nav]');
    this.indicators = element.querySelectorAll('.hero-carousel__indicator');
    this.prevBtn = element.querySelector('[data-slide-nav="prev"]');
    this.nextBtn = element.querySelector('[data-slide-nav="next"]');
    this.carouselId = element.getAttribute('data-carousel-id') || `carousel-${Math.random().toString(36).substr(2, 9)}`;
    this.autoRotate = element.getAttribute('data-auto-rotate') === 'true';
    this.rotateInterval = parseInt(element.getAttribute('data-rotate-interval'), 10) || 5000;
    this.currentIndex = 0;
    this.autoRotateTimer = null;
    this.prefersReducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    this.init();
  }

  init() {
    if (this.slides.length <= 1) {
      return;
    }

    this.setActiveSlide(0);
    this.attachEventListeners();

    if (this.autoRotate && !this.prefersReducedMotion) {
      this.startAutoRotate();
    }

    this.element.addEventListener('mouseenter', () => this.stopAutoRotate());
    this.element.addEventListener('mouseleave', () => {
      if (this.autoRotate && !this.prefersReducedMotion) {
        this.startAutoRotate();
      }
    });
  }

  attachEventListeners() {
    if (this.prevBtn) {
      this.prevBtn.addEventListener('click', () => this.goToPrevious());
    }

    if (this.nextBtn) {
      this.nextBtn.addEventListener('click', () => this.goToNext());
    }

    this.indicators.forEach((indicator, index) => {
      indicator.addEventListener('click', () => this.goToSlide(index));
    });

    this.element.addEventListener('keydown', (event) => this.handleKeyboard(event));

    if ('ontouchstart' in window) {
      this.attachTouchHandlers();
    }
  }

  handleKeyboard(event) {
    switch (event.key) {
      case 'ArrowLeft':
        this.goToPrevious();
        break;
      case 'ArrowRight':
        this.goToNext();
        break;
      default:
        break;
    }
  }

  attachTouchHandlers() {
    let touchStartX = 0;
    let touchEndX = 0;

    this.container.addEventListener('touchstart', (event) => {
      touchStartX = event.changedTouches[0].screenX;
    });

    this.container.addEventListener('touchend', (event) => {
      touchEndX = event.changedTouches[0].screenX;
      this.handleSwipe(touchStartX, touchEndX);
    });
  }

  handleSwipe(startX, endX) {
    const threshold = 50;
    const diff = startX - endX;

    if (Math.abs(diff) > threshold) {
      if (diff > 0) {
        this.goToNext();
      } else {
        this.goToPrevious();
      }
    }
  }

  goToSlide(index) {
    if (index < 0 || index >= this.slides.length || index === this.currentIndex) {
      return;
    }

    this.stopAutoRotate();
    this.setActiveSlide(index);

    if (this.autoRotate && !this.prefersReducedMotion) {
      this.startAutoRotate();
    }
  }

  goToNext() {
    const nextIndex = (this.currentIndex + 1) % this.slides.length;
    this.goToSlide(nextIndex);
  }

  goToPrevious() {
    const prevIndex = (this.currentIndex - 1 + this.slides.length) % this.slides.length;
    this.goToSlide(prevIndex);
  }

  setActiveSlide(index) {
    this.slides.forEach((slide) => {
      slide.classList.remove('is-active');
    });

    this.indicators.forEach((indicator, i) => {
      indicator.setAttribute('aria-pressed', i === index ? 'true' : 'false');
    });

    this.slides[index].classList.add('is-active');
    this.currentIndex = index;

    this.trackAnalytics(index);
  }

  startAutoRotate() {
    this.autoRotateTimer = setInterval(() => {
      this.goToNext();
    }, this.rotateInterval);
  }

  stopAutoRotate() {
    if (this.autoRotateTimer) {
      clearInterval(this.autoRotateTimer);
      this.autoRotateTimer = null;
    }
  }

  trackAnalytics(slideIndex) {
    if (window.dataLayer) {
      window.dataLayer.push({
        event: 'hero_carousel_slide_change',
        carousel_id: this.carouselId,
        slide_index: slideIndex,
        timestamp: new Date().toISOString(),
      });
    }
  }

  destroy() {
    this.stopAutoRotate();
    if (this.prevBtn) {
      this.prevBtn.removeEventListener('click', () => this.goToPrevious());
    }
    if (this.nextBtn) {
      this.nextBtn.removeEventListener('click', () => this.goToNext());
    }
  }
}

function initHeroCarousels() {
  const carousels = document.querySelectorAll('.hero-carousel');
  carousels.forEach((carousel) => {
    if (!carousel.dataset.initialized) {
      new HeroCarousel(carousel);
      carousel.dataset.initialized = 'true';
    }
  });
}

if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', initHeroCarousels);
} else {
  initHeroCarousels();
}
