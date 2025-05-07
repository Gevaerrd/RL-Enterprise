const formToSendUser = document.querySelector(".organization-off-register-form"); // Pegando o formulario

formToSendUser.addEventListener('submit', async function (e) {
    e.preventDefault(); // Impede o envio do formulário tradicional (sem recarregar a página)

    const formData = new FormData(formToSendUser); // Form data pega name do input e o valor (chave, valor)
    const jsonData = {};

    formData.forEach((value, key) => (jsonData[key] = value)); // Itera sobre os dados do formulário e os adiciona ao objeto jsonData
    console.log(JSON.stringify(jsonData));
    try {

        const response = await fetch("/register", {     // Envia os dados para o backend (Spring Boot) usando fetch API
            method: "POST", // Define o método HTTP como POST (enviar dados)
            headers: {
                "Content-Type": "application/json", // Define o tipo de conteúdo como JSON
            },
            body: JSON.stringify(jsonData),
        });

        // Aguarda a resposta do servidor (backend)
        const result = await response.json(); // Converte a resposta em JSON
        const msg = document.querySelector("#formMessage"); // Acessa o elemento para colocar a mensagem


        // Limpar qualquer classe anterior
        msg.classList.remove("success", "error");

        if (response.ok) { // Se a resposta for OK (status 200)

            msg.classList.remove('hidden')
            msg.innerText = result.message;  // Exibe a mensagem de sucesso
            msg.classList.add("success"); // Adiciona a classe de sucesso
        } else {
            msg.classList.remove('hidden')
            msg.innerText = result.Error; // Exibe a mensagem de erro
            msg.classList.add("error"); // Adiciona a classe de erro
        }
    } 
    
    catch (error) {
        const msg = document.getElementById("formMessage"); // Localiza a div de mensagem
        msg.classList.remove('hidden')
        msg.innerText = "Erro ao conectar ao servidor!"; // Exibe uma mensagem genérica de erro
        msg.classList.add("error"); // Adiciona a classe de erro
    }
});
