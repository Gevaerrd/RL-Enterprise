const gear = document.getElementById('gear-for-settings');
const menu = document.getElementById('settings-menu');
const changePasswordOption = document.getElementById('change-password-option');
const modal = document.getElementById('change-password-modal');
const closeModal = document.getElementById('close-password-modal');
const form = document.getElementById('change-password-form');
const message = document.getElementById('change-password-message');

// Alterna menu de configurações
gear.addEventListener('click', (e) => {
  e.stopPropagation();
  menu.classList.toggle('hidden');
});

// Fecha menu ao clicar fora
document.addEventListener('click', (e) => {
  if (!menu.classList.contains('hidden') && !menu.contains(e.target) && e.target !== gear) {
    menu.classList.add('hidden');
  }
});

// Abrir modal de troca de senha
changePasswordOption.addEventListener('click', () => {
  menu.classList.add('hidden');
  modal.classList.remove('hidden');
});

// Fechar modal
closeModal.addEventListener('click', () => {
  modal.classList.add('hidden');
  form.reset();
  message.textContent = '';
});

// Fechar modal ao clicar fora do conteúdo
modal.addEventListener('click', (e) => {
  if (e.target === modal) {
    modal.classList.add('hidden');
    form.reset();
    message.textContent = '';
  }
});

// Exemplo de submit (AJAX para backend)
form.addEventListener('submit', async (e) => {
  e.preventDefault();
  message.textContent = '';
  const current = document.getElementById('current-password').value;
  const nova = document.getElementById('new-password').value;
  const repete = document.getElementById('repeat-password').value;

  if (nova !== repete) {
    message.textContent = "As novas senhas não coincidem.";
    return;
  }
  if (nova.length < 8) {
    message.textContent = "A nova senha deve ter pelo menos 8 caracteres.";
    return;
  }

  try {
    // Troque a URL abaixo para o endpoint real do seu backend
    const resp = await fetch('/api/user/change-password', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({currentPassword: current, newPassword: nova})
    });
    const data = await resp.json();
    if (data.success) {
      message.style.color = "#22c55e";
      message.innerHTML = "Senha alterada com sucesso!";
      setTimeout(() => {
        modal.classList.add('hidden');
        form.reset();
        message.innerHTML = '';
        message.style.color = "#f87171";
      }, 1500);
    } else {
      message.innerHTML = data.error || "Erro ao trocar senha.";
    }
  } catch {
    message.textContent = "Erro ao trocar senha.";
  }
});

// Mostrar/ocultar senha ao clicar no olho
document.querySelectorAll('.toggle-password').forEach(icon => {
  icon.addEventListener('click', function() {
    const targetId = this.getAttribute('data-target');
    const input = document.getElementById(targetId);
    if (input.type === "password") {
      input.type = "text";
      this.classList.remove('fa-eye');
      this.classList.add('fa-eye-slash');
    } else {
      input.type = "password";
      this.classList.remove('fa-eye-slash');
      this.classList.add('fa-eye');
    }
  });
});