async function iniciar() {
    try {
        const res = await fetch("/api/plano"); // Conecta ao rest api plano
        const data = await res.json(); // Pega a resposta que vem um objeto: {"plano": 3};
        const plano = data.plano // Pega o plano do usuario
        console.log(plano);

        const modulos = document.querySelectorAll(".modal-or-plan-container"); // Pega os modulos
        modulos.forEach(modulo => { // Para cada modulo
            const id = parseInt(modulo.dataset.id); // Pega o id do modulo
            if (id <= plano) { // Se o id do modulo for menor ou igual ao limite do plano do usuario 
                modulo.classList.remove("locked"); // Remove o locked

                modulo.addEventListener("click", async(event) => {
                    event.preventDefault(); // Adiciona a função de click nesse mesmo módulo
                    await carregarVideo(id); // Que vai chamar o carregar video com o id do módulo
                });
            }
        });
    } 
    
    catch (error) {
        console.error("Erro ao buscar plano:", error);
    }
}

async function carregarVideo(id) {
  try {
    const response = await fetch(`/api/video/${id}`);

    if (response.status === 401) {
      alert('Você precisa estar logado para acessar este vídeo.');
      window.location.href = '/'; // Redireciona para login
      return;
    }

    if (response.status === 403) {
      alert('Seu plano não permite acesso a este vídeo.');
      window.location.href = '/#planss'; // Redireciona para página de planos
      return;
    }

    if (response.status === 404) {
      alert('Vídeo não encontrado.');
      return;
    }

    if (!response.ok) {
      throw new Error('Erro ao carregar o vídeo');
    }

    const videoBlob = await response.blob(); // .blob sinaliza que espera um dado bruto (imagem, audio, etc..)
    const videoUrl = URL.createObjectURL(videoBlob); // Cria uma url temporario para o arquivo

    const containerForVideo = document.querySelector(".noplan-message");
    containerForVideo.innerHTML = "";

    const videoElement = document.createElement("video");
    videoElement.src = videoUrl;
    videoElement.classList.add("videoElement");
    videoElement.classList.add("videoFadeIn");
    videoElement.controls = true;

    containerForVideo.appendChild(videoElement);

  } catch (error) {
    alert('Erro inesperado ao carregar o vídeo.');
    console.error(error);
  }
}

document.addEventListener("DOMContentLoaded", () => {
    iniciar();
  });
