// login.js
const formForLogin = document.querySelector(".organization-off-login-form");  // Formulário de login

formForLogin.addEventListener("submit", async function (e) {
    e.preventDefault();

    const formDataForLogin = new FormData(formForLogin);
    const jsonDataForLogin = {};
    formDataForLogin.forEach((value, key) => (jsonDataForLogin[key] = value));

    const msgForResult = document.querySelector("#formMessageLogin");
    msgForResult.classList.remove("success", "error", "hidden");

    try {
        const responseLogin = await fetch("/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(jsonDataForLogin),
            credentials: "include" // <-- ESSENCIAL!
        });

        let result = {};
        try {
            result = await responseLogin.json();
        } catch {
            result = {};
        }

        if (responseLogin.ok) {
            msgForResult.innerText = "Login bem-sucedido!";
            msgForResult.classList.add("success");
            window.location.href = result.redirect;
        } else {
            msgForResult.innerText = result.Error || "Email ou senha incorretos";
            msgForResult.classList.add("error");
            if (window.grecaptcha && document.querySelector('.g-recaptcha')) {
                grecaptcha.reset();
            }
        }
    } catch (error) {
        msgForResult.innerText = "Erro ao conectar ao servidor!";
        msgForResult.classList.add("error");
        if (window.grecaptcha && document.querySelector('.g-recaptcha')) {
            grecaptcha.reset();
        }
    }
});