package RLEnterprise.resources;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import RLEnterprise.entities.AfilliateCode;
import RLEnterprise.entities.AfilliateSelling;
import RLEnterprise.entities.Plan;
import RLEnterprise.entities.User;
import RLEnterprise.entities.WithdrawRequest;
import RLEnterprise.services.AfilliateCodeService;
import RLEnterprise.services.AfilliateSellingService;
import RLEnterprise.services.PlanService;
import RLEnterprise.services.UserService;
import RLEnterprise.services.WithdrawRequestService;

@Controller
@RequestMapping("/r13nt3rp1se4dmind4shbo4rd")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private WithdrawRequestService withdrawService;
    @Autowired
    private AfilliateSellingService sellingService;
    @Autowired
    private PlanService planService;
    @Autowired
    AfilliateCodeService afilliateCodeService;

    @GetMapping("")
    public String adminHome() {
        return "admin/admin-dashboard";
    }

    // Usuários
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.findAll();
        List<Plan> plans = planService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("plans", plans);
        return "admin/admin-users";
    }

    @PostMapping("/users/update")
    public String updateUser(@RequestParam Long id,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String cpf,
            @RequestParam(required = false) String plan) {
        User user = userService.findById(id);
        if (user != null) {
            user.setName(name);
            user.setEmail(email);
            user.setCpf(cpf);

            Plan oldPlan = user.getPlan();
            Plan newPlan = (plan != null && !plan.isBlank()) ? planService.findByName(plan) : null;

            // Se o plano mudou ou o usuário não tem código de afiliado, gere um novo
            if (newPlan != null && (oldPlan == null || !oldPlan.getName().equals(newPlan.getName()))) {
                user.setPlan(newPlan);

                // Gere um novo código de afiliado
                if (user.getAfilliateCode() == null || user.getAfilliateCode().getUser() == null) {
                    AfilliateCode newCode = new AfilliateCode();
                    newCode.setCode(afilliateCodeService.generateCode());
                    newCode.setUser(user);
                    afilliateCodeService.save(newCode);
                    user.setAfilliateCode(newCode);
                }

                // Atualiza a data de início do plano ao salvar
                user.setPlanStartDate(LocalDateTime.now());
            } else if (newPlan == null) {
                user.setPlan(null);
                user.setPlanStartDate(null); // Limpa a data se o plano for removido
            }

            userService.save(user);
        }
        return "redirect:/r13nt3rp1se4dmind4shbo4rd/users";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.deleteById(id);
        return "redirect:/r13nt3rp1se4dmind4shbo4rd/users";
    }

    @PostMapping("/users/remove-plan-affiliate")
    public String removeUserPlanAndAffiliate(@RequestParam Long id) {
        User user = userService.findById(id);
        if (user != null) {
            user.setPlan(null);
            user.setAfilliateCode(null); // Troque para o nome real do campo do código de afiliado
            userService.save(user);
        }
        return "redirect:/r13nt3rp1se4dmind4shbo4rd/users";
    }

    // Saques
    @GetMapping("/withdrawals")
    public String listWithdrawals(@RequestParam(required = false) Integer status, Model model) {
        List<WithdrawRequest> withdrawals = (status == null)
                ? withdrawService.findAll()
                : withdrawService.findByStatus(status);
        model.addAttribute("withdrawals", withdrawals);
        model.addAttribute("status", status);
        return "admin/admin-withdrawals";
    }

    @PostMapping("/withdrawals/update")
    public String updateWithdrawal(@RequestParam Long id, @RequestParam int status) {
        WithdrawRequest wr = withdrawService.findById(id);
        if (wr != null && status == 1) { // 1 = aprovado
            User user = wr.getUser();
            if (user != null) {
                user.setBalance(0.0); // ou user.setSaldo(0.0); conforme o nome do campo
                userService.save(user);
            }
        }
        withdrawService.updateStatus(id, status);
        return "redirect:/r13nt3rp1se4dmind4shbo4rd/withdrawals";
    }

    // Financeiro
    @GetMapping("/finance")
    public String financePage(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Long userId,
            Model model) {

        List<User> users = userService.findAll();
        List<AfilliateSelling> sales;

        if (userId != null && month != null) {
            sales = sellingService.findAllByMonthAndUser(month, userId);
        } else if (userId != null) {
            sales = sellingService.findAllByUser(userId);
        } else if (month != null) {
            sales = sellingService.findAllByMonth(month);
        } else {
            sales = sellingService.findAll();
        }

        double totalSales = sales.stream().mapToDouble(s -> s.getComission()).sum();
        double totalAfilliate = totalSales;

        model.addAttribute("users", users);
        model.addAttribute("sales", sales);
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("totalAfilliate", totalAfilliate);
        model.addAttribute("month", month);
        model.addAttribute("userId", userId);

        return "admin/admin-finance";
    }
}