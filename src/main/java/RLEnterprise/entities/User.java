package RLEnterprise.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private double balance;
    private String cpf;
    private String twoFactorCode;
    private Long twoFactorCodeGeneratedAt;
    private String role = "USER";
    private LocalDateTime planStartDate;

    @OneToMany(mappedBy = "user")
    private List<WithdrawRequest> withdrawRequests = new ArrayList<>();

    @OneToMany(mappedBy = "seller")
    private List<AfilliateSelling> afilliateSellings = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @OneToOne
    @JoinColumn(name = "code_id")
    private AfilliateCode afilliateCode;

    public User() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public AfilliateCode getAfilliateCode() {
        return afilliateCode;
    }

    public void setAfilliateCode(AfilliateCode afilliateCode) {
        this.afilliateCode = afilliateCode;
    }

    public double getBalance() {
        return balance;
    }

    public void addBalance(double balance) {
        this.balance += balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getTwoFactorCode() {
        return twoFactorCode;
    }

    public void setTwoFactorCode(String twoFactorCode) {
        this.twoFactorCode = twoFactorCode;
    }

    public Long getTwoFactorCodeGeneratedAt() {
        return twoFactorCodeGeneratedAt;
    }

    public void setTwoFactorCodeGeneratedAt(Long twoFactorCodeGeneratedAt) {
        this.twoFactorCodeGeneratedAt = twoFactorCodeGeneratedAt;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<WithdrawRequest> getWithdrawRequests() {
        return withdrawRequests;
    }

    public void addWithdrawRequest(WithdrawRequest withdrawRequest) {
        if (this.withdrawRequests != null) {
            this.withdrawRequests.add(withdrawRequest);
        }
    }

    public List<AfilliateSelling> getAfilliateSellings() {
        return afilliateSellings;
    }

    public void addAfilliateSellings(AfilliateSelling afilliateSellings) {
        if (this.afilliateSellings != null) {
            this.afilliateSellings.add(afilliateSellings);
        }
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(LocalDateTime planStartDate) {
        this.planStartDate = planStartDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", email=").append(email);
        sb.append(", password=").append(password);
        sb.append(", balance=").append(balance);
        sb.append(", twoFactorCode=").append(twoFactorCode);
        sb.append(", twoFactorCodeGeneratedAt=").append(twoFactorCodeGeneratedAt);
        sb.append(", plan=").append(plan);
        sb.append(", afilliateCode=").append(afilliateCode);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}
