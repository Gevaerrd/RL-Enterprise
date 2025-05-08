const formToSendUser = document.querySelector(".organization-off-register-form");

formToSendUser.addEventListener('submit', async function (e) {
    e.preventDefault();

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

            // Redireciona após pequeno delay (pode remover o setTimeout se quiser redirecionar instantaneamente)
            setTimeout(() => {
                window.location.href = result.redirect;
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
