const buttons = document.querySelectorAll(".plans-container button");
buttons.forEach(button => {
  button.addEventListener("click", async function (e)  {
    try {

      e.preventDefault()

      const responseSignPlan = await fetch(`/api/signplan/${button.id}`, {
        method: "POST",
        headers: {
                "Content-Type": "application/json",  // Define o tipo de conteúdo como JSON
            },
      });
      
      const data = await responseSignPlan.json();
      
      const linkToPayment = data.paymentLink;
      window.location.href = linkToPayment;
      
      console.log(data);
      console.log(data.message);      // "Plano assinado com sucesso."
      console.log(data.paymentLink);  // link do pagamento
      

    } 
    
    catch (error) {

    }
  });
});

