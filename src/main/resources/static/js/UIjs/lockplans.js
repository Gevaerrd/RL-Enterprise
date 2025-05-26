document.addEventListener("DOMContentLoaded", function() {
    const userPlanId = document.body.getAttribute("data-user-plan-id");
    if (userPlanId) {
        document.querySelectorAll('.plan-card').forEach(card => {
            const planId = card.getAttribute('data-plan');
            const btn = card.querySelector('button');
            if (parseInt(planId) < parseInt(userPlanId)) {
                if (btn) {
                    btn.disabled = true;
                    btn.innerHTML = '<i class="fa-solid fa-lock"></i> Bloqueado';
                }
            } else if (parseInt(planId) === parseInt(userPlanId)) {
                if (btn) {
                    btn.disabled = true;
                    btn.innerHTML = '<i class="fa-solid fa-check"></i> Plano Atual';
                }
            }
        });
    }
});