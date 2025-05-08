// const registerForm = document.querySelector("#id-for-registerForm");
// const spaceOfRegisterForm = document.querySelector(".space-of-register-form");
// const buttonToShowRF = document.querySelector("#getRegisterForm");

// // Quando clicar no botão para mostrar o formulário
// buttonToShowRF.addEventListener('click', (e) => {
//     e.stopPropagation(); // Impede a propagação do clique para o document

//     // Verifica se o formulário está oculto antes de exibir com fade-in
//     if (registerForm.classList.contains('hidden')) {
//         registerForm.classList.remove('hidden'); // Remove a classe hidden
//         setTimeout(() => {
//             registerForm.classList.add('fade-in'); // Aplica a transição fade-in
//         }, 100); // Aguarda 100ms antes de aplicar o fade-in
//     }
// });


// // Evento para fechar o formulário quando clicar fora dele
// document.addEventListener('click', (e) => {
//     if (
//         !spaceOfRegisterForm.contains(e.target) &&
//         e.target !== buttonToShowRF &&
//         e.target.id !== "link-to-login" && // Impede conflito na troca
//         e.target.id !== "link-to-register"
//     ) {
//         // Aplica a transição fade-out quando o formulário for escondido
//         if (!registerForm.classList.contains('hidden')) {
//             registerForm.classList.add('fade-out'); // Inicia a animação fade-out
//             registerForm.classList.remove('fade-in'); // Remove a animação fade-in

//             // Esconde o formulário após a animação de fade-out
//             setTimeout(() => {
//                 registerForm.classList.add('hidden');
//                 registerForm.classList.remove('fade-out');
//             }, 1000); // Espera o tempo de animação de 1 segundo para esconder completamente
//         }
//     }
// });
