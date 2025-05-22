const buttons = document.querySelectorAll(".plans-container button");
buttons.forEach(button => {
  button.addEventListener("click", async function (e)  {
    e.preventDefault();
    try {
      const responseSignPlan = await fetch(`/api/signplan/${button.id}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      });

      if (responseSignPlan.status === 401 || responseSignPlan.status === 403) {
        // Mostra o modal de login
        document.getElementById("id-for-loginForm").classList.remove("hidden");
        return;
      }

      const data = await responseSignPlan.json();

      if (data.paymentLink) {
        window.location.href = data.paymentLink;
      } 
      
      else if (data.Error) {
        alert(data.Error); // Ou mostre uma mensagem amigável
      }

    } catch (error) {
      alert("Erro ao tentar assinar o plano. Tente novamente.");
    }
  });
});