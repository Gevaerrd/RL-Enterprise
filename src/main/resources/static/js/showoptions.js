const accountHover = document.querySelector("#accountHover");
const loginOrRegister = document.querySelector(".loginOrRegister");

let hideTimeout;

function showDropdown() {
  clearTimeout(hideTimeout);
  loginOrRegister.classList.remove('hidden');
}

function hideDropdownWithDelay() {
  hideTimeout = setTimeout(() => {
    loginOrRegister.classList.add('hidden');
  }, 200); // tempo pequeno para permitir transição entre os elementos
}

accountHover.addEventListener('mouseenter', showDropdown);
accountHover.addEventListener('mouseleave', hideDropdownWithDelay);

loginOrRegister.addEventListener('mouseenter', showDropdown);
loginOrRegister.addEventListener('mouseleave', hideDropdownWithDelay);
