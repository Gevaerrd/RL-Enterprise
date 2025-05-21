// Seletores principais
const loginSection = document.getElementById("id-for-loginForm");
const registerSection = document.getElementById("id-for-registerForm");
const forgotPasswordSection = document.getElementById("id-for-forgotPasswordForm");

// Botões e links
const linkToRegister = document.querySelector("#link-to-register");
const linkToLogin = document.querySelector("#link-to-login");
const forgotPasswordButtonsAll = document.querySelectorAll(".a-forgot-password");
const backToLoginLink = document.querySelector("#id-for-forgotPasswordForm .back-to-login");

// Register form (para fechar ao clicar fora)
const spaceOfRegisterForm = document.querySelector(".space-of-register-form");
const buttonToRF = document.querySelector("#getRegisterForm");

// Login form (para fechar ao clicar fora)
const spaceOfLoginForm = document.querySelector(".space-of-login-form");
const buttonToShowLF = document.querySelector("#accountHover");

// Forgot password form
const forgotPasswordFormElement = document.getElementById("forgot-password-form");
const forgotPasswordEmailInput = document.getElementById("forgot-email");
const forgotPasswordMessage = document.getElementById("forgot-message");

// Troca de login para registro
if (linkToRegister) {
    linkToRegister.addEventListener("click", (e) => {
        e.preventDefault();
        e.stopPropagation();
        loginSection.classList.add("hidden");
        registerSection.classList.remove("hidden");
        forgotPasswordSection.classList.add("hidden");
    });
}

// Troca de registro para login
if (linkToLogin) {
    linkToLogin.addEventListener("click", (e) => {
        e.preventDefault();
        e.stopPropagation();
        registerSection.classList.add("hidden");
        loginSection.classList.remove("hidden");
        forgotPasswordSection.classList.add("hidden");
    });
}

// Mostrar o formulário de recuperação ao clicar em qualquer "Esqueceu sua senha?"
forgotPasswordButtonsAll.forEach(btn => {
    btn.addEventListener('click', function(event) {
        event.preventDefault();
        loginSection.classList.add('hidden');
        registerSection.classList.add('hidden');
        forgotPasswordSection.classList.remove('hidden');
    });
});

// Voltar ao login a partir do forgot password
if (backToLoginLink) {
    backToLoginLink.addEventListener('click', function(e) {
        e.preventDefault();
        e.stopPropagation();
        forgotPasswordSection.classList.add('hidden');
        registerSection.classList.add('hidden');
        loginSection.classList.remove('hidden');
    });
}

// Fechar o formulário de registro ao clicar fora
document.addEventListener('click', (e) => {
    if (
        registerSection && !registerSection.classList.contains('hidden') &&
        spaceOfRegisterForm && !spaceOfRegisterForm.contains(e.target) &&
        e.target !== buttonToRF &&
        e.target.id !== "link-to-login" &&
        e.target.id !== "link-to-register" &&
        (forgotPasswordSection.classList.contains('hidden'))
    ) {
        registerSection.classList.add('hidden');
    }
});

// Fechar o formulário de login ao clicar fora
document.addEventListener('click', (e) => {
    if (
        loginSection && !loginSection.classList.contains('hidden') &&
        spaceOfLoginForm && !spaceOfLoginForm.contains(e.target) &&
        e.target !== buttonToShowLF &&
        e.target.id !== "link-to-login" &&
        e.target.id !== "link-to-register" &&
        (forgotPasswordSection.classList.contains('hidden'))
    ) {
        loginSection.classList.add('hidden');
    }
});

// Envio do formulário de recuperação de senha
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

// Abrir login form pelo botão
if (buttonToShowLF) {
    buttonToShowLF.addEventListener('click', (e) => {
        e.preventDefault();
        e.stopPropagation();
        loginSection.classList.remove('hidden');
        registerSection.classList.add('hidden');
        forgotPasswordSection.classList.add('hidden');
    });
}

// Abrir register form pelo botão
if (buttonToRF) {
    buttonToRF.addEventListener('click', (e) => {
        e.preventDefault();
        e.stopPropagation();
        registerSection.classList.remove('hidden');
        loginSection.classList.add('hidden');
        forgotPasswordSection.classList.add('hidden');
    });
}