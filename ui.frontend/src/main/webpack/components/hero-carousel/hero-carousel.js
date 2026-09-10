document.addEventListener('DOMContentLoaded', () => {
  const carousels = document.querySelectorAll('[data-cmp-is="hero-carousel"]');
  carousels.forEach(carouselElement => {
    new HeroCarousel(carouselElement);
  });
});

class HeroCarousel {
  constructor(element) {
    this.element = element;
    this.slides = element.querySelectorAll('.hero-carousel__slide');
    this.prevBtn = element.querySelector('.hero-carousel__nav-btn--prev');
    this.nextBtn = element.querySelector('.hero-carousel__nav-btn--next');
    this.indicators = element.querySelectorAll('.hero-carousel__indicator');
    this.currentIndex = 0;
    this.autoRotate = element.dataset.cmpAutorotate === 'true';
    this.rotationDelay = parseInt(element.dataset.cmpRotationDelay, 10) || 5000;
    this.rotationInterval = null;
    this.prefersReducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    if (this.slides.length === 0) return;

    this.init();
  }

  init() {
    this.showSlide(0);

    if (this.prevBtn) {
      this.prevBtn.addEventListener('click', () => this.prevSlide());
    }

    if (this.nextBtn) {
      this.nextBtn.addEventListener('click', () => this.nextSlide());
    }

    this.indicators.forEach((indicator, index) => {
      indicator.addEventListener('click', () => this.showSlide(index));
    });

    this.element.addEventListener('keydown', (event) => this.handleKeyboard(event));

    if (this.autoRotate && !this.prefersReducedMotion) {
      this.startAutoRotation();
    }

    this.element.addEventListener('mouseenter', () => this.pauseAutoRotation());
    this.element.addEventListener('mouseleave', () => {
      if (this.autoRotate && !this.prefersReducedMotion) {
        this.startAutoRotation();
      }
    });

    // Pause autorotate on interaction
    this.slides.forEach(slide => {
      const video = slide.querySelector('.hero-carousel__video');
      if (video) {
        video.addEventListener('play', () => this.pauseAutoRotation());
        video.addEventListener('pause', () => {
          if (this.autoRotate && !this.prefersReducedMotion) {
            this.startAutoRotation();
          }
        });
      }
    });
  }

  showSlide(index) {
    // Validate index
    if (index < 0) {
      index = this.slides.length - 1;
    } else if (index >= this.slides.length) {
      index = 0;
    }

    this.currentIndex = index;

    // Update slides
    this.slides.forEach((slide, i) => {
      slide.classList.toggle('is-active', i === index);
    });

    // Update indicators
    this.indicators.forEach((indicator, i) => {
      const isActive = i === index;
      indicator.classList.toggle('is-active', isActive);
      indicator.setAttribute('aria-pressed', isActive ? 'true' : 'false');
    });

    // Update button states
    if (this.slides.length === 1) {
      if (this.prevBtn) this.prevBtn.disabled = true;
      if (this.nextBtn) this.nextBtn.disabled = true;
    }
  }

  nextSlide() {
    this.pauseAutoRotation();
    this.showSlide(this.currentIndex + 1);
    if (this.autoRotate && !this.prefersReducedMotion) {
      this.startAutoRotation();
    }
  }

  prevSlide() {
    this.pauseAutoRotation();
    this.showSlide(this.currentIndex - 1);
    if (this.autoRotate && !this.prefersReducedMotion) {
      this.startAutoRotation();
    }
  }

  handleKeyboard(event) {
    switch (event.key) {
      case 'ArrowRight':
      case ' ':
        event.preventDefault();
        this.nextSlide();
        break;
      case 'ArrowLeft':
        event.preventDefault();
        this.prevSlide();
        break;
      case 'Home':
        event.preventDefault();
        this.showSlide(0);
        break;
      case 'End':
        event.preventDefault();
        this.showSlide(this.slides.length - 1);
        break;
    }
  }

  startAutoRotation() {
    if (this.rotationInterval) return; // Already running
    this.rotationInterval = setInterval(() => {
      this.currentIndex = (this.currentIndex + 1) % this.slides.length;
      this.showSlide(this.currentIndex);
    }, this.rotationDelay);
  }

  pauseAutoRotation() {
    if (this.rotationInterval) {
      clearInterval(this.rotationInterval);
      this.rotationInterval = null;
    }
  }
}
