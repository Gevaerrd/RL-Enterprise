const loginForm = document.querySelector("#id-for-loginForm");
const spaceOfLoginForm = document.querySelector(".space-of-login-form");
const buttonToShowLF = document.querySelector("#getLoginForm");

// Quando clicar no botão para mostrar o formulário
buttonToShowLF.addEventListener('click', (e) => {
    e.stopPropagation(); // Impede a propagação do clique para o document

    // Verifica se o formulário está oculto antes de exibir com fade-in
    if (loginForm.classList.contains('hidden')) {
        loginForm.classList.remove('hidden'); // Remove a classe hidden
        loginForm.classList.add('fade-in'); // Aplica a transição fade-in
    }
});


// Evento para fechar o formulário quando clicar fora dele
document.addEventListener('click', (e) => {
    if (
        !spaceOfLoginForm.contains(e.target) &&
        e.target !== buttonToShowRF &&
        e.target.id !== "link-to-login" && // Impede conflito na troca
        e.target.id !== "link-to-register"
    ) {
        // Aplica a transição fade-out quando o formulário for escondido
        if (!loginForm.classList.contains('hidden')) {
            loginForm.classList.add('fade-out'); // Inicia a animação fade-out
            loginForm.classList.remove('fade-in'); // Remove a animação fade-in

            // Esconde o formulário após a animação de fade-out
            setTimeout(() => {
                loginForm.classList.add('hidden');
                loginForm.classList.remove('fade-out');
            }, 1000); // Espera o tempo de animação de 1 segundo para esconder completamente
        }
    }
});