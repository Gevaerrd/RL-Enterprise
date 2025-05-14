const formToSendUser = document.querySelector(".organization-off-register-form");

function passwordChecker() {
    const pas1 = document.querySelector("#password-register");
    const pas2 = document.querySelector("#password-repeat");
    const msg = document.querySelector("#formMessage");

    // Reseta a mensagem e a borda caso as senhas coincidam
    pas1.classList.remove("error");
    pas2.classList.remove("error");
    msg.classList.add("hidden");

    if (pas1.value !== pas2.value) {
        // Exibe a mensagem de erro
        msg.classList.remove("hidden");
        msg.innerText = "As senhas não coincidem!";

        // Adiciona a borda vermelha nos campos de senha
        pas1.classList.add("error");
        pas2.classList.add("error");

        // A borda vermelha vai durar 1 segundo e depois será removida
        setTimeout(() => {
            pas1.classList.remove("error");
            pas2.classList.remove("error");
        }, 1000);
        
        return false; // Impede o envio do formulário
    }

    return true; // Se as senhas coincidirem, permite o envio
}

formToSendUser.addEventListener('submit', async function (e) {
    e.preventDefault();
    const valid = passwordChecker(); // Verifica se as senhas coincidem

    if (!valid) {
        return; // Se as senhas não coincidem, impede o envio
    }

    const formData = new FormData(formToSendUser);
    const jsonData = {};

    formData.forEach((value, key) => (jsonData[key] = value));

    try {
        const response = await fetch("/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(jsonData),
        });

        const result = await response.json();
        const msg = document.querySelector("#formMessage");
        msg.classList.remove("success", "error", "hidden");

        if (response.ok) {
            msg.innerText = result.message || "Cadastro realizado com sucesso!";
            msg.classList.add("success");

            // Redireciona após pequeno delay
            setTimeout(() => {
                window.location.href = result.redirect; // Se não tiver nada do backend, ele mantém a mesma página
            }, 1000); // Espera 1s para mostrar a mensagem antes de redirecionar
        } else {
            msg.innerText = result.Error || "Erro ao registrar.";
            msg.classList.add("error");
        }

    } catch (error) {
        const msg = document.getElementById("formMessage");
        msg.classList.remove("success", "hidden");
        msg.innerText = "Erro ao conectar ao servidor!";
        msg.classList.add("error");
    }
});
