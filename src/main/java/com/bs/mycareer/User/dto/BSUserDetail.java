package com.bs.mycareer.User.dto;

import com.bs.mycareer.User.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
// UserDetail과 같은경우는 현재 인터페이스로 User와 같은 entity로 보면 안된다. 따라서 @Autowired / @Component 쓰면 user와의 관계가 성립이 x
public class BSUserDetail implements UserDetails {


    private User user;


    public BSUserDetail(User user) {
        this.user = user;
    }

    public User getUser() {return user;}


    //추가로 admin / user 추후에 나누어 적용
    // Collection 안에 ?는 권한이 어떤 타입인줄 모르기에 사용 // set형식이라서 중복이 안되기에 계속 덮어쓰기 개념으로 clear를 해줄필요 없음
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return authorities;
    }
    // 1. 필요시 중복을 허용, 매번 로그인할때마다 권한을 초기화하는 로직(혹시나...)
//    public void updateAuthorities(Collection<? extends GrantedAuthority> newAuthorities) {
//        Set<GrantedAuthority> authoritiesSet = new HashSet<>(newAuthorities);
//        this.getAuthorities().clear();  // 이전 권한 초기화
//        this.user.getRole().addAll(authoritiesSet);  // 새로운 권한 추가
//    }


    @Override
        public String getUsername() {

        return user.getEmail();
        }

        @Override
        public String getPassword () {

        return user.getPassword();
        }


        @Override
        public boolean isAccountNonExpired () {
            return true;
        }

        @Override
        public boolean isAccountNonLocked () {
        return true;
        }

        @Override
        public boolean isCredentialsNonExpired () {
        return true;
        }

        @Override
        public boolean isEnabled () {return true;}


}

