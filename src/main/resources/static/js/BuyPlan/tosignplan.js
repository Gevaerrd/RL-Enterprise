const buttons = document.querySelectorAll(".plans-container button");
buttons.forEach(button => {
  button.addEventListener("click", () => {
    console.log(`Botão clicado: ${button.id}`);
  });
});

// FAZER O REST CONTROLLER DO PLANCONTROLLER PARA QAUNDO CLICAR EM ASSINAR IR PRA LÁ E ADAPTAR UM PLANO AO USUARIO
// TEMPORARIAMENTE ATÉ INSERIR O MERCADO PAGO, SÓ PARA TESTAR