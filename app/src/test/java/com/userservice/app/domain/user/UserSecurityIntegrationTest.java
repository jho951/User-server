package com.userservice.app.domain.user;

import com.userservice.app.TestInfrastructureConfig;
import com.userservice.app.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestInfrastructureConfig.class)
class UserSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("공개_signup은 인증 없이 허용된다")
    void signupIsPublic() throws Exception {
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@example.com\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("내 정보 조회는 active JWT만 허용된다")
    void meAllowsActiveJwt() throws Exception {
        when(userService.get(any())).thenReturn(null);

        mockMvc.perform(get("/users/me")
                        .with(jwt().jwt(jwt -> jwt
                                .subject("123e4567-e89b-12d3-a456-426614174000")
                                .claim("status", "A"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("내 정보 조회는 비활성 상태 JWT를 거부한다")
    void meRejectsInactiveJwt() throws Exception {
        mockMvc.perform(get("/users/me")
                        .with(jwt().jwt(jwt -> jwt
                                .subject("123e4567-e89b-12d3-a456-426614174000")
                                .claim("status", "S"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("내부 사용자 생성은 internal scope가 있어야 한다")
    void internalCreateRequiresScope() throws Exception {
        when(userService.create(any())).thenReturn(null);

        mockMvc.perform(post("/internal/users")
                        .with(jwt()
                                .jwt(jwt -> jwt.subject("auth-service"))
                                .authorities(new SimpleGrantedAuthority("SCOPE_internal")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"internal-user@example.com\"}"))
                .andExpect(status().isCreated());
        verify(userService).create(any());
    }

    @Test
    @DisplayName("내부 사용자 생성은 internal scope가 없으면 거부된다")
    void internalCreateRejectsWithoutScope() throws Exception {
        mockMvc.perform(post("/internal/users")
                        .with(jwt().jwt(jwt -> jwt.subject("auth-service")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"internal-user@example.com\"}"))
                .andExpect(status().isForbidden());
    }
}
