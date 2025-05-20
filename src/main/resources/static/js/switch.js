// Seleciona os dois formulários
const loginFormSwitch = document.querySelector("#id-for-loginForm");
const registerFormSwitch = document.querySelector("#id-for-registerForm");

// Botões de troca de formulário
const linkToRegister = document.querySelector("#link-to-register");
const linkToLogin = document.querySelector("#link-to-login");


// Troca de login para registro
linkToRegister.addEventListener("click", (e) => {
    e.preventDefault();
    e.stopPropagation(); // Impede o clique de afetar outros elementos

    // Aplica a animação de fade-out no formulário de login
    loginFormSwitch.classList.add("fade-out");

    setTimeout(() => {
        // Esconde o formulário de login
        loginFormSwitch.classList.add("hidden");
        loginFormSwitch.classList.remove("fade-out");

        // Mostra o formulário de registro com uma animação de fade-in
        registerFormSwitch.classList.remove("hidden");
        registerFormSwitch.classList.add("form-transition");

        // Aplica o fade-in com delay após o registro ter sido mostrado
        setTimeout(() => {
            registerFormSwitch.classList.remove("form-transition");
            registerFormSwitch.classList.add("fade-in");
        }, 50);
    }, 500); // Tempo de transição do fade-out (500ms)
});

// Troca de registro para login
linkToLogin.addEventListener("click", (e) => {
    e.preventDefault();
    e.stopPropagation(); // Impede o clique de afetar outros elementos

    // Aplica a animação de fade-out no formulário de registro
    registerFormSwitch.classList.add("fade-out");

    setTimeout(() => {
        // Esconde o formulário de registro
        registerFormSwitch.classList.add("hidden");
        registerFormSwitch.classList.remove("fade-out");

        // Mostra o formulário de login com uma animação de fade-in
        loginFormSwitch.classList.remove("hidden");
        loginFormSwitch.classList.add("form-transition");

        // Aplica o fade-in com delay após o login ter sido mostrado
        setTimeout(() => {
            loginFormSwitch.classList.remove("form-transition");
            loginFormSwitch.classList.add("fade-in");
        }, 50);
    }, 500); // Tempo de transição do fade-out (500ms)
});