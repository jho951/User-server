package com.api.config;

import com.api.user.domain.User;
import com.api.user.repository.UserRepository;
import com.core.constant.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SuperAdminInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${admin.super.username:superadmin}")
	private String superUsername;

	@Value("${admin.super.email:superadmin@example.com}")
	private String superEmail;

	@Value("${admin.super.password:superadmin1234}")
	private String superPassword;

	public SuperAdminInitializer(
		UserRepository userRepository,
		PasswordEncoder passwordEncoder
	) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public void run(String... args) {
		// 이미 SUPER_ADMIN 이 존재하면 아무 작업도 안 함
		if (userRepository.existsByRole(UserRole.SUPER_ADMIN)) {
			return;
		}

		// 혹시 이메일로도 이미 존재하면 역시 스킵
		if (userRepository.existsByEmail(superEmail)) {
			return;
		}

		User superAdmin = User.builder()
			.username(superUsername)
			.email(superEmail)
			.password(passwordEncoder.encode(superPassword))
			.role(UserRole.SUPER_ADMIN)
			.enabled(true)
			.build();

		userRepository.save(superAdmin);

		System.out.println("✅ Super admin created");
		System.out.println("   email: " + superEmail);
		System.out.println("   username: " + superUsername);
	}
}
