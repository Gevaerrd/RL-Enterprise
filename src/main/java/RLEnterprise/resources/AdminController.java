package RLEnterprise.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import RLEnterprise.entities.AfilliateSelling;
import RLEnterprise.entities.Plan;
import RLEnterprise.entities.User;
import RLEnterprise.entities.WithdrawRequest;
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
            if (plan != null && !plan.isBlank()) {
                user.setPlan(planService.findByName(plan));
            }
            userService.save(user);
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/remove-plan-affiliate")
    public String removeUserPlanAndAffiliate(@RequestParam Long id) {
        User user = userService.findById(id);
        if (user != null) {
            user.setPlan(null);
            user.setAfilliateCode(null); // Troque para o nome real do campo do código de afiliado
            userService.save(user);
        }
        return "redirect:/admin/users";
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
        return "redirect:/admin/withdrawals";
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