const formToSendUser = document.querySelector("#id-for-registerForm");


formToSendUser.addEventListener('submit', async function (e) {
    e.preventDefault(); // Impede o envio do formulário tradicional (sem recarregar a página)

    const formData = new formData(this); // Cria um FormData a partir do formulário (vai pegar os dados preenchidos)
    const jsonData = {};

    formData.forEach((value, key) => (jsonData[key] = value)); // Itera sobre os dados do formulário e os adiciona ao objeto jsonData
    
    try {
        // Envia os dados para o backend (Spring Boot) usando fetch API
        const response = await fetch("/api/users/register", {
            method: "POST", // Define o método HTTP como POST (enviar dados)
            headers: {
                "Content-Type": "application/json", // Define o tipo de conteúdo como JSON
              },
            body: JSON.stringify(jsonData),
        })

        // Aguarda a resposta do servidor (backend)
        const result = await response.json(); // Converte a resposta em JSON
        const msg = document.querySelector("#formMessage");

        if (response.ok) { // Se a resposta for OK (status 200)
            msg.innerText = "Registrado com sucesso!";  // Exibe a mensagem de sucesso
            msg.style.color = "green"; // Coloca a mensagem de sucesso em verde
        } 

        else {
            msg.innerText = result.error || "Erro ao registrar"; // Exibe a mensagem de erro
            msg.style.color = "red"; // Coloca a mensagem de erro em vermelho
          }
    } 
    
    catch (error) {
        const msg = document.getElementById("formMessage"); // Localiza a div de mensagem
        msg.innerText = "Erro ao conectar ao servidor!"; // Exibe uma mensagem genérica de erro
        msg.style.color = "red"; // Coloca a mensagem em vermelho
    }


})