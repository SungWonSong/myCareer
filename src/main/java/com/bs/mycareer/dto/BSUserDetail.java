package com.bs.mycareer.dto;

import com.bs.mycareer.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class BSUserDetail implements UserDetails {

    private final User user;


    //일반적으로 security는 권한을 요구하는게 필수라 구현해야되지만 필요없다면 밑에 코드사용
    //추가로 admin / user 추후에 나누어 적용


    public BSUserDetail(User user) {

        this.user = user;
    }

    // Collection 안에 ?는 권한이 어떤 타입인줄 모르기에 사용 / 추후 이로직 안에 irator.next()함수를 써서 리스트에서 다음거로 넘어가는 로직 생성하기
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add((GrantedAuthority) user::getRole);


        return collection;
    }

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
            return false;
        }

        @Override
        public boolean isAccountNonLocked () {
        return false;
        }

        @Override
        public boolean isCredentialsNonExpired () {
        return false;
        }

        @Override
        public boolean isEnabled () {
        return false;
        }
    }

