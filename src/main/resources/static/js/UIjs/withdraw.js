document.getElementById('withdraw-btn').onclick = () => {
  document.getElementById('withdraw-modal').classList.remove('hidden');
};

document.getElementById('close-withdraw-modal').onclick = () => {
  document.getElementById('withdraw-modal').classList.add('hidden');
};

// Esconde o campo CPF se já estiver cadastrado (backend pode renderizar isso com um atributo data-cpf-set="true")
window.addEventListener('DOMContentLoaded', () => {
  const cpfInput = document.getElementById('withdraw-cpf');
  const cpfGroup = cpfInput.closest('label') || cpfInput;
  if (cpfInput && cpfInput.hasAttribute('data-cpf-set')) {
    cpfGroup.style.display = 'none';
  }
});

document.getElementById('withdraw-form').onsubmit = async function(e) {
  e.preventDefault();
  const btn = this.querySelector('button[type="submit"]');
  btn.disabled = true;

  const cpfInput = document.getElementById('withdraw-cpf');
  const cpf = cpfInput && cpfInput.value ? cpfInput.value : undefined;
  const msg = document.getElementById('withdraw-message');
  msg.innerText = "";

  // Só envia CPF se o campo estiver visível
  const payload = {};
  if (cpfInput && cpfInput.offsetParent !== null) {
    payload.cpf = cpf;
  }

  try {
    const resp = await fetch('/profile/withdraw', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(payload)
    });
    const data = await resp.json();
    if (data.success) {
      msg.style.color = "#27ae60";
      msg.innerText = data.message || "Saque solicitado!";
      setTimeout(() => {
        document.getElementById('withdraw-modal').classList.add('hidden');
        msg.innerText = "";
      }, 2000);
    } else {
      msg.style.color = "#e74c3c";
      msg.innerText = data.error || "Erro ao solicitar saque.";
    }
  } catch (err) {
    msg.style.color = "#e74c3c";
    msg.innerText = "Erro de conexão.";
  }
  btn.disabled = false;
};