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
        event.stopPropagation();
        loginSection.classList.add('hidden');
        registerSection.classList.add('hidden');
        forgotPasswordSection.classList.remove('hidden');
    });
});

// Voltar ao login a partir do forgot password (primeira etapa)
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

// Fechar o formulário de forgot password ao clicar fora
document.addEventListener('click', (e) => {
    if (
        forgotPasswordSection && !forgotPasswordSection.classList.contains('hidden') &&
        forgotPasswordSection.querySelector('.space-of-forgot-password-form') &&
        !forgotPasswordSection.querySelector('.space-of-forgot-password-form').contains(e.target) &&
        e.target !== backToLoginLink &&
        e.target.id !== "link-to-login" &&
        e.target.id !== "link-to-register"
    ) {
        forgotPasswordSection.classList.add('hidden');
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

// Recuperação de senha - fluxo completo
if (forgotPasswordFormElement) {
    forgotPasswordFormElement.addEventListener('submit', async function handleEmailSubmit(e) {
        e.preventDefault();
        const email = forgotPasswordEmailInput.value;
        if (!email) return;

        // Checa se já foi enviado há menos de 1 hora
        const lastRequest = localStorage.getItem('lastForgotRequest');
        if (lastRequest && (Date.now() - Number(lastRequest)) < 3600000) {
            const tempoRestante = 3600 - Math.floor((Date.now() - Number(lastRequest)) / 1000);
            const min = Math.floor(tempoRestante / 60);
            forgotPasswordMessage.innerHTML = `Nova solicitação em: ${min}m<br><br>Tente novamente mais tarde.`;
            forgotPasswordMessage.classList.add('error');
            return;
        }

        forgotPasswordMessage.textContent = '';
        forgotPasswordMessage.classList.remove('success', 'error');
        try {
            const response = await fetch('/api2fa/request', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ to: email })
            });
            const data = await response.json();

            // Salva o horário do envio
            localStorage.setItem('lastForgotRequest', Date.now().toString());

            // Troca o conteúdo mantendo a estrutura e o CSS
            forgotPasswordSection.querySelector('.space-of-forgot-password-form').innerHTML = `
                <div class="name-of-enterprise">
                    <h1>RL</h1>
                    <h2>Enterprise</h2>
                </div>
                <div id="forgot-message" class="forgot-message"></div>
                <form id="forgot-password-form">
                    <div class="input-group">
                        <div class="input-icon">
                            <i class="fa-solid fa-key"></i>
                            <input type="text" id="input-2fa-code" name="2facode" required placeholder="Digite o código recebido" maxlength="10" />
                        </div>
                    </div>
                    <div class="input-group">
                        <div class="input-icon">
                            <i class="fa-solid fa-lock"></i>
                            <input type="password" id="input-new-password" name="newPassword" required placeholder="Digite a nova senha" minlength="8" />
                        </div>
                    </div>
                    <button type="submit" id="reset-password-btn">Redefinir senha</button>
                    <button type="button" id="resend-code-btn" disabled">Reenviar código</button>
                    <div id="resend-timer-message" class="forgot-message"></div>
                </form>
                <a href="#" class="back-to-login">Voltar ao login</a>
            `;

            // Selecione o novo form e elementos
            const resetForm = document.getElementById('forgot-password-form');
            const input2fa = document.getElementById('input-2fa-code');
            const inputNewPassword = document.getElementById('input-new-password');
            const resendBtn = document.getElementById('resend-code-btn');
            const forgotMessage = document.getElementById('forgot-message');
            const resendTimerMessage = document.getElementById('resend-timer-message');
            const backToLogin = forgotPasswordSection.querySelector('.space-of-forgot-password-form .back-to-login');

            // Mensagem de sucesso após envio do código
            forgotMessage.textContent = 'Se o e-mail existir, um código foi enviado!';
            forgotMessage.classList.add('success');

            // Timer para reenviar código (1 hora)
            let timer = Math.floor((Number(localStorage.getItem('lastForgotRequest')) + 3600000 - Date.now()) / 1000);
            if (timer > 0) {
                resendBtn.disabled = true;
                updateResendTimerMessage();
                let interval = setInterval(() => {
                    timer--;
                    updateResendTimerMessage();
                    if (timer <= 0) {
                        clearInterval(interval);
                        resendBtn.disabled = false;
                        resendTimerMessage.textContent = "";
                    }
                }, 1000);
            } else {
                resendBtn.disabled = false;
                resendTimerMessage.textContent = "";
            }

            function updateResendTimerMessage() {
                const min = Math.floor(timer / 60);
                const seg = timer % 60;
                resendTimerMessage.innerHTML = `Nova solicitação em: ${min}m ${seg < 10 ? '0' : ''}${seg}s`;
            }

            // Evento para reenviar código
            resendBtn.addEventListener('click', async function() {
                const lastRequest = localStorage.getItem('lastForgotRequest');
                if (lastRequest && (Date.now() - Number(lastRequest)) < 3600000) {
                    timer = 3600 - Math.floor((Date.now() - Number(lastRequest)) / 1000);
                    resendBtn.disabled = true;
                    updateResendTimerMessage();
                    let interval2 = setInterval(() => {
                        timer--;
                        updateResendTimerMessage();
                        if (timer <= 0) {
                            clearInterval(interval2);
                            resendBtn.disabled = false;
                            resendTimerMessage.textContent = "";
                        }
                    }, 1000);
                    return;
                }
                resendBtn.disabled = true;
                timer = 3600;
                updateResendTimerMessage();
                try {
                    await fetch('/api2fa/request', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ to: email })
                    });
                    localStorage.setItem('lastForgotRequest', Date.now().toString());
                    forgotMessage.textContent = "Novo código enviado!";
                    forgotMessage.classList.add('success');
                } catch {
                    forgotMessage.textContent = "Erro ao reenviar código.";
                    forgotMessage.classList.add('error');
                }
                let interval2 = setInterval(() => {
                    timer--;
                    updateResendTimerMessage();
                    if (timer <= 0) {
                        clearInterval(interval2);
                        resendBtn.disabled = false;
                        resendTimerMessage.textContent = "";
                    }
                }, 1000);
            });

            // Novo listener para o submit do código e senha (segunda etapa)
            resetForm.addEventListener('submit', async function(ev) {
                ev.preventDefault();
                const code = input2fa.value;
                const newPassword = inputNewPassword.value;
                forgotMessage.textContent = '';
                forgotMessage.classList.remove('success', 'error');
                try {
                    const resp = await fetch('/api2fa/reset-password', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ "2facode": code, "newPassword": newPassword })
                    });
                    const result = await resp.json();
                    if (result.success) {
                        forgotMessage.textContent = "Senha redefinida com sucesso! Redirecionando...";
                        forgotMessage.classList.add('success');
                        setTimeout(() => {
                            forgotPasswordSection.classList.add('hidden');
                            registerSection.classList.add('hidden');
                            loginSection.classList.remove('hidden');
                        }, 2000);
                    } else {
                        if (result["Error"]) {
                            forgotMessage.innerHTML = "A senha deve ter no mínimo 8 caracteres<br>1 letra maiúscula<br>1 caractere especial.";
                        } else {
                            forgotMessage.textContent = result["not-verified"] || "Código ou senha inválidos.";
                        }
                        forgotMessage.classList.add('error');
                    }
                } catch {
                    forgotMessage.textContent = "Erro ao redefinir senha.";
                    forgotMessage.classList.add('error');
                }
            });

            // Evento para voltar ao login (segunda etapa)
            if (backToLogin) {
                backToLogin.addEventListener('click', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    forgotPasswordSection.classList.add('hidden');
                    registerSection.classList.add('hidden');
                    loginSection.classList.remove('hidden');
                });
            }

        } catch (error) {
            forgotPasswordMessage.textContent = 'Erro ao enviar e-mail. Tente novamente.';
            forgotPasswordMessage.classList.add('error');
        }
    });
}