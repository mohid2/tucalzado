package com.tucalzado.service.impl;

import com.tucalzado.models.CustomUserDetails;
import com.tucalzado.models.entity.Role;
import com.tucalzado.models.entity.User;
import com.tucalzado.models.enums.RoleEnum;
import com.tucalzado.repository.IRoleRepository;
import com.tucalzado.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return saveOrUpdateUser(oAuth2User, userRequest);
    }

    private CustomUserDetails saveOrUpdateUser(OAuth2User oAuth2User, OAuth2UserRequest userRequest) {
        String clientName = userRequest.getClientRegistration().getClientName();
        String email = null;
        String firstName = null;
        String lastName = null;
        String username = null;

        if (clientName.equalsIgnoreCase("Google")) {
            email = oAuth2User.getAttribute("email");
            firstName = oAuth2User.getAttribute("given_name");
            lastName = oAuth2User.getAttribute("family_name");
            username = oAuth2User.getAttribute("name");
        } else if (clientName.equalsIgnoreCase("GitHub")) {
            email = getGitHubEmail(oAuth2User, userRequest.getAccessToken().getTokenValue());
            username = oAuth2User.getAttribute("login");
            String fullName = oAuth2User.getAttribute("name");
            if (fullName != null) {
                String[] names = fullName.split(" ", 2);
                firstName = names.length > 0 ? names[0] : null;
                lastName = names.length > 1 ? names[1] : null;
            }
        }
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setFirstname(firstName);
            user.setLastname(lastName);
            user.setUsername(username);
            Role roleUser = roleRepository.findByRole(RoleEnum.USER).orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRoles(List.of(roleUser));
            userRepository.save(user);
        } else {
            user.setFirstname(firstName);
            user.setLastname(lastName);
            user.setUsername(username);
            userRepository.save(user);
        }
        Collection<? extends GrantedAuthority> authorities = oAuth2User.getAuthorities();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return new CustomUserDetails(attributes, authorities, email, user.getId(), null, username);
    }

    private String getGitHubEmail(OAuth2User oAuth2User, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                "https://api.github.com/user/emails",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        List<Map<String, Object>> emails = response.getBody();
        for (Map<String, Object> email : emails) {
            Boolean primary = (Boolean) email.get("primary");
            Boolean verified = (Boolean) email.get("verified");
            if (primary != null && primary && verified != null && verified) {
                return (String) email.get("email");
            }
        }
        return null;
    }
}
