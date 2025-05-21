const forgotPasswordSection = document.getElementById("id-for-forgotPasswordForm");
const forgotPasswordFormElement = document.getElementById("forgot-password-form");
const forgotPasswordEmailInput = document.getElementById("forgot-email");
const forgotPasswordMessage = document.getElementById("forgot-message");
const backToLoginLink = document.querySelector("#id-for-forgotPasswordForm .back-to-login");
const forgotPasswordButtonsAll = document.querySelectorAll(".a-forgot-password");
const loginSection = document.getElementById("id-for-loginForm");      // Corrigido: sem #
const registerSection = document.getElementById("id-for-registerForm");// Corrigido: sem #

// Mostrar o formulário de recuperação ao clicar em qualquer "Esqueceu sua senha?"
forgotPasswordButtonsAll.forEach(btn => {
    btn.addEventListener('click', function(event) {
        event.preventDefault();
        if (loginSection) loginSection.classList.add('hidden');
        if (registerSection) registerSection.classList.add('hidden');
        forgotPasswordSection.classList.remove('hidden');
    });
});

// Voltar ao login
if (backToLoginLink) {
    backToLoginLink.addEventListener('click', function(e) {
        e.preventDefault();
        e.stopPropagation(); // <-- ESSA LINHA É FUNDAMENTAL!
        forgotPasswordSection.classList.add('hidden');
        if (loginSection) loginSection.classList.remove('hidden');
    });
}

// Envio do formulário de recuperação
if (forgotPasswordFormElement) {
    forgotPasswordFormElement.addEventListener('submit', async function(e) {
        e.preventDefault();
        const email = forgotPasswordEmailInput.value;
        if (!email) return;
        forgotPasswordMessage.textContent = '';
        forgotPasswordMessage.classList.remove('success', 'error');
        try {
            const response = await fetch('/api/send-email', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ to: email })
            });
            const data = await response.json();
            forgotPasswordMessage.textContent = data.message || 'Se o e-mail existir, um código foi enviado!';
            forgotPasswordMessage.classList.add('success');
        } catch (error) {
            forgotPasswordMessage.textContent = 'Erro ao enviar e-mail. Tente novamente.';
            forgotPasswordMessage.classList.add('error');
        }
    });
}