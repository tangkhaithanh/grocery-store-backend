    package org.api.grocerystorebackend.security;

    import org.api.grocerystorebackend.entity.Account;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;
    import java.util.Collection;
    import java.util.Collections;

    public class AccountDetails implements UserDetails{

        private final Account account;

        public AccountDetails(Account account) {
            this.account = account;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.emptyList(); // Nếu có roles thì xử lý ở đây
        }

        @Override
        public String getPassword() {
            return account.getPassword();
        }

        @Override
        public String getUsername() {
            return account.getEmail();
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
            return account.getIsActive() != null && account.getIsActive();
        }

        public Account getAccount() {
            return account;
        }
    }
