// Aplica fade-out, muda texto, depois aplica fade-in
const messages = [
  "Preparando o ambiente",
  "Liberando o conteúdo...",
  "Tudo pronto!"
];

const fadeDuration = 600; // tempo do fade out/in
const interval = 3000;    // intervalo entre mensagens (3 segundos)
const messageEl = document.querySelector('#message'); // exemplo, ajuste conforme seu código

function changeMessage(index) {
  if (index >= messages.length) return;

  // inicia fade out
  if (messageEl.classList.contains("fade-in")) {
    messageEl.classList.remove("fade-in");
  }
  messageEl.classList.add("fade");

  setTimeout(() => {
    // muda texto após fade out
    messageEl.textContent = messages[index];

    // remove fade e aplica fade-in
    messageEl.classList.remove("fade");
    messageEl.classList.add("fade-in");
  }, fadeDuration);
}

// Dispara as mensagens escalonadas a cada 3 segundos, com fade antes de mudar texto
for (let i = 0; i < messages.length; i++) {
  setTimeout(() => {
    changeMessage(i);
  }, i * interval);
}



// setTimeout(() => {
//   window.location.href = "https://seusite.com/confirmacao"; // Troque aqui para o destino real
// }, 5000);