const myWithdrawals = document.querySelector("#show-withdrawls");
const mySales = document.querySelector("#show-sales");
const modalContainer = document.getElementById("withdrawals-modal");
const modalContent = document.getElementById("withdrawals-list");
const closeWithdrawalsBtn = document.getElementById("close-withdrals-modal");

function formatDate(dateString) {
    // Espera formato ISO, ex: "2025-05-29T13:45:00"
    const date = new Date(dateString);
    if (isNaN(date)) return "";
    // Retorna apenas o dia (DD/MM/YYYY)
    return date.toLocaleDateString('pt-BR');
}

function getStatusName(status) {
    switch (status) {
        case 0: return "PENDENTE";
        case 1: return "APROVADO";
        case 2: return "RECUSADO";
        default: return "DESCONHECIDO";
    }
}

async function openWithdrawalsModal() {
    try {
        const response = await fetch("/profile/withdrawals", { method: "GET" });
        if (!response.ok) throw new Error("Erro ao buscar saques");

        const data = await response.json();
        const withdrawals = data.withdrawals || [];

        modalContainer.classList.remove("hidden");
        modalContent.innerHTML = '';

        if (withdrawals.length === 0) {
            modalContent.innerHTML = '<span style="font-size:1.5rem;color:#fff;display:block;text-align:center;margin-top:2rem;">Nenhum saque encontrado.</span>';
            return;
        }

        withdrawals.forEach(item => {
            const row = document.createElement('div');
            row.classList.add('withdraw-row');

            // Converte status numérico para texto
            let statusText = getStatusName(item.status);
            let statusClass = "withdraw-status";
            if (statusText === "PENDENTE") statusClass += " pendente";
            else if (statusText === "RECUSADO") statusClass += " recusado";

            row.innerHTML = `
                <span class="withdraw-status">${statusText}</span>
                <span class="withdraw-date">${formatDate(item.requestedAt)}</span>
                <span class="withdraw-value">R$ ${item.value.toFixed(2)}</span>
            `;
            modalContent.appendChild(row);
        });

    } catch (error) {
        console.error('Erro ao buscar saques:', error);
        modalContent.innerHTML = '<span style="color:#f87171;">Erro ao carregar saques.</span>';
    }
}

myWithdrawals.addEventListener('click', openWithdrawalsModal);
if (mySales) mySales.addEventListener('click', openWithdrawalsModal);

closeWithdrawalsBtn.addEventListener('click', () => {
    modalContainer.classList.add("hidden");
});