document.addEventListener('DOMContentLoaded', () => {
    const observer = new IntersectionObserver((entries) => {
      entries.forEach(({ target, isIntersecting }) => {
        if (isIntersecting) {
          target.classList.add('animate');
        }
      });
    }, {
      threshold: 0.1
    });
  
    const target = document.querySelector('.highlighted-word');
    if (target) {
      observer.observe(target);
    }
  });