const mySales = document.querySelector("#show-sales");
const salesModalContainer = document.getElementById("sales-modal");
const salesModalContent = document.getElementById("sales-list");
const closeSalesBtn = document.getElementById("close-sales-modal");

function formatDate(dateString) {
    const date = new Date(dateString);
    if (isNaN(date)) return "";
    return date.toLocaleDateString('pt-BR');
}

async function openSalesModal() {
    try {
        const response = await fetch("/profile/afilliate-sales", { method: "GET" });
        if (!response.ok) throw new Error("Erro ao buscar vendas");

        const data = await response.json();
        const sales = data.sales || [];

        salesModalContainer.classList.remove("hidden");
        salesModalContent.innerHTML = '';

        if (sales.length === 0) {
            salesModalContent.innerHTML = '<span style="color:#fff;display:block;text-align:center;margin-top:1.5rem;">Nenhuma venda encontrada.</span>';
            return;
        }

        sales.forEach(item => {
            const row = document.createElement('div');
            row.classList.add('withdraw-row');
            row.innerHTML = `
                <span class="withdraw-date">${formatDate(item.date)}</span>
                <span class="withdraw-client">${item.firstName}</span>
                <span class="withdraw-value">R$ ${item.comission.toFixed(2)}</span>
                <span class="withdraw-plan">${item.plan}</span>
            `;
            salesModalContent.appendChild(row);
        });

    } catch (error) {
        console.error('Erro ao buscar vendas:', error);
        salesModalContent.innerHTML = '<span style="color:#f87171;">Erro ao carregar vendas.</span>';
    }
}

mySales.addEventListener('click', openSalesModal);
closeSalesBtn.addEventListener('click', () => {
    salesModalContainer.classList.add("hidden");
});