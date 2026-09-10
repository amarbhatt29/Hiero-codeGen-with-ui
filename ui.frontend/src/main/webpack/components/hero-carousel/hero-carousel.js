export class HeroCarousel {
  constructor(element) {
    this.element = element;
    this.slides = this.element.querySelectorAll('[data-carousel-slide]');
    this.dotsContainer = this.element.querySelector('[role="group"][aria-label="Slide navigation"]');
    this.prevBtn = this.element.querySelector('[data-carousel-prev]');
    this.nextBtn = this.element.querySelector('[data-carousel-next]');
    this.configScript = this.element.querySelector('[data-carousel-config]');
    
    this.currentIndex = 0;
    this.autoRotateEnabled = false;
    this.autoRotateInterval = 5000;
    this.autoRotateTimer = null;
    this.touchStartX = 0;
    this.touchEndX = 0;
    
    this.init();
  }

  init() {
    if (this.slides.length < 2) return;

    this.setupAutoRotate();
    this.addEventListeners();
    this.updateCarousel();
  }

  setupAutoRotate() {
    if (!this.configScript) return;
    
    try {
      const config = JSON.parse(this.configScript.textContent);
      this.autoRotateEnabled = config.autoRotate === true;
      this.autoRotateInterval = config.interval || 5000;
      
      if (this.autoRotateEnabled) {
        this.startAutoRotate();
        
        this.element.addEventListener('mouseenter', () => this.pauseAutoRotate());
        this.element.addEventListener('mouseleave', () => this.startAutoRotate());
      }
    } catch (e) {
      console.error('Hero Carousel: Invalid configuration', e);
    }
  }

  addEventListeners() {
    if (this.prevBtn) {
      this.prevBtn.addEventListener('click', () => this.prev());
    }
    
    if (this.nextBtn) {
      this.nextBtn.addEventListener('click', () => this.next());
    }

    if (this.dotsContainer) {
      const dots = this.dotsContainer.querySelectorAll('[data-carousel-dot]');
      dots.forEach((dot, index) => {
        dot.addEventListener('click', () => this.goToSlide(index));
      });
    }

    this.element.addEventListener('touchstart', (e) => this.handleTouchStart(e));
    this.element.addEventListener('touchend', (e) => this.handleTouchEnd(e));

    document.addEventListener('keydown', (e) => this.handleKeyPress(e));
  }

  prev() {
    this.currentIndex = (this.currentIndex - 1 + this.slides.length) % this.slides.length;
    this.resetAutoRotate();
    this.updateCarousel();
    this.trackInteraction('prev');
  }

  next() {
    this.currentIndex = (this.currentIndex + 1) % this.slides.length;
    this.resetAutoRotate();
    this.updateCarousel();
    this.trackInteraction('next');
  }

  goToSlide(index) {
    if (index >= 0 && index < this.slides.length) {
      this.currentIndex = index;
      this.resetAutoRotate();
      this.updateCarousel();
      this.trackInteraction('dot', index);
    }
  }

  handleTouchStart(e) {
    this.touchStartX = e.changedTouches[0].clientX;
  }

  handleTouchEnd(e) {
    this.touchEndX = e.changedTouches[0].clientX;
    this.handleSwipe();
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

  handleKeyPress(e) {
    if (!this.element.contains(document.activeElement)) return;

    if (e.key === 'ArrowLeft') {
      this.prev();
      e.preventDefault();
    } else if (e.key === 'ArrowRight') {
      this.next();
      e.preventDefault();
    }
  }

  updateCarousel() {
    this.slides.forEach((slide, index) => {
      const isActive = index === this.currentIndex;
      slide.setAttribute('aria-current', isActive);
    });

    if (this.dotsContainer) {
      const dots = this.dotsContainer.querySelectorAll('[data-carousel-dot]');
      dots.forEach((dot, index) => {
        dot.setAttribute('aria-current', index === this.currentIndex);
      });
    }

    if (this.prevBtn) {
      this.prevBtn.disabled = this.slides.length <= 1;
    }
    if (this.nextBtn) {
      this.nextBtn.disabled = this.slides.length <= 1;
    }

    this.playActiveSlideVideo();
  }

  playActiveSlideVideo() {
    const activeSlide = this.slides[this.currentIndex];
    const videos = this.element.querySelectorAll('[data-carousel-video]');
    
    videos.forEach((video) => {
      if (activeSlide.contains(video)) {
        video.play().catch(() => {
          // Video play failed, fallback image will show
        });
      } else {
        video.pause();
      }
    });
  }

  startAutoRotate() {
    if (!this.autoRotateEnabled || this.autoRotateTimer) return;
    
    this.autoRotateTimer = setInterval(() => {
      this.next();
    }, this.autoRotateInterval);
  }

  pauseAutoRotate() {
    if (this.autoRotateTimer) {
      clearInterval(this.autoRotateTimer);
      this.autoRotateTimer = null;
    }
  }

  resetAutoRotate() {
    this.pauseAutoRotate();
    if (this.autoRotateEnabled) {
      this.startAutoRotate();
    }
  }

  trackInteraction(action, slideIndex = this.currentIndex) {
    if (!window.dataLayer) return;
    
    window.dataLayer.push({
      event: 'hero_carousel_interaction',
      carousel_action: action,
      carousel_slide: slideIndex + 1,
      carousel_total_slides: this.slides.length
    });
  }
}

// Auto-initialize all carousels on page
document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('[data-carousel-root]').forEach((element) => {
    new HeroCarousel(element);
  });
});

// Support dynamic component loading
if (window.Granite && window.Granite.author && window.Granite.author.edit) {
  window.Granite.author.edit.registerContentLoaded(() => {
    document.querySelectorAll('[data-carousel-root]').forEach((element) => {
      if (!element._heroCarouselInitialized) {
        new HeroCarousel(element);
        element._heroCarouselInitialized = true;
      }
    });
  });
}
