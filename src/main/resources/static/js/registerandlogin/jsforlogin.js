// login.js
const formForLogin = document.querySelector(".organization-off-login-form");  // Formulário de login

formForLogin.addEventListener("submit", async function (e) {
    e.preventDefault();  // Previne o envio tradicional do formulário

    const formDataForLogin = new FormData(formForLogin);  // Coleta os dados do formulário
    const jsonDataForLogin = {};

    formDataForLogin.forEach((value, key) => (jsonDataForLogin[key] = value));  // Cria o objeto JSON

    try {
        // Envia os dados para o backend
        const responseLogin = await fetch("/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",  // Define o tipo de conteúdo como JSON
            },
            body: JSON.stringify(jsonDataForLogin),  // Envia os dados do formulário
        });

        const result = await responseLogin.json();  // Converte a resposta do servidor

        const msgForResult = document.querySelector("#formMessageLogin");  // Localiza o elemento para mostrar a mensagem
        msgForResult.classList.remove("success", "error");  // Limpa classes de sucesso ou erro

        if (responseLogin.ok) {  // Se login for bem-sucedido
            msgForResult.classList.remove('hidden');
            msgForResult.innerText = "Login bem-sucedido!";
            msgForResult.classList.add("success");

            // Redireciona para a página de dashboard do usuário
            window.location.href = result.redirect;  // Usando a URL de redirecionamento retornada pelo backend
        } else {  // Caso contrário, exibe mensagem de erro
            msgForResult.classList.remove('hidden');
            msgForResult.innerText = result.Error;
            msgForResult.classList.add("error");
        }
    } catch (error) {
        const msgForResult = document.querySelector("#formMessageLogin");
        msgForResult.classList.remove('hidden');
        msgForResult.innerText = "Erro ao conectar ao servidor!";  // Exibe erro genérico
        msgForResult.classList.add("error");
    }
});
