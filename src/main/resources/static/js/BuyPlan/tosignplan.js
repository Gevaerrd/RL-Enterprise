const buttons = document.querySelectorAll(".plans-container button");
console.log(buttons);
buttons.forEach(button => {
  button.addEventListener("click", () => {
    console.log(`Botão clicado: ${button.id}`);
  });
});