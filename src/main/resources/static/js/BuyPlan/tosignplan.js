const buttons = document.querySelectorAll(".plans-container button");
buttons.forEach(button => {
  button.addEventListener("click", async function (e)  {
    try {

      e.preventDefault()

      console.log(button.id);
      const responseSignPlan = await fetch(`/api/signplan/${button.id}`, {
        method: "POST",
        headers: {
                "Content-Type": "application/json",  // Define o tipo de conteúdo como JSON
            },
      });
      
      const data = await responseSignPlan.json();
      console.log(data);

    } 
    
    catch (error) {

    }
  });
});

