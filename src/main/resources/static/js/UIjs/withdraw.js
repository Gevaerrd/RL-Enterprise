document.getElementById('withdraw-btn').onclick = () => {
  // Preenche o saldo automaticamente e bloqueia edição
  const saldoSpan = document.getElementById('real-balance');
  const valorInput = document.getElementById('withdraw-amount');
  if (saldoSpan && valorInput) {
    valorInput.value = saldoSpan.innerText.replace(',', '.');
    valorInput.readOnly = true;
  }

  // Preenche CPF se já existir e bloqueia edição
  const cpfInput = document.getElementById('withdraw-cpf');
  if (cpfInput) {
    const cpfConta = cpfInput.getAttribute('data-cpf');
    if (cpfConta && cpfConta.length >= 11) {
      cpfInput.value = cpfConta;
      cpfInput.readOnly = true;
    } else {
      cpfInput.value = "";
      cpfInput.readOnly = false;
    }
  }

  document.getElementById('withdraw-modal').classList.remove('hidden');
};

document.getElementById('close-withdraw-modal').onclick = () => {
  document.getElementById('withdraw-modal').classList.add('hidden');
};

document.getElementById('withdraw-form').onsubmit = async function(e) {
  e.preventDefault();
  const btn = this.querySelector('button[type="submit"]');
  btn.disabled = true;

  const cpfInput = document.getElementById('withdraw-cpf');
  const cpf = cpfInput && cpfInput.value ? cpfInput.value : undefined;
  const msg = document.getElementById('withdraw-message');
  msg.innerText = "";

  // Só envia CPF se o campo não estiver readonly
  const payload = {};
  if (cpfInput && !cpfInput.readOnly) {
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