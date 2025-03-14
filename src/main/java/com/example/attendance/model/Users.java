package com.example.attendance.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user", schema = "worktime_mg")
@Getter
@Setter
@NoArgsConstructor
public class Users implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "名前を入力してください")
	@Size(max = 50, message = "名前は50文字以内で入力してください")
	@Column(nullable = false, length = 50)
	private String name;

	@NotEmpty(message = "メールアドレスを入力してください")
	@Email(message = "正しいメールアドレス形式を入力してください")
	@Column(nullable = false, unique = true, length = 255)
	private String mail;

	@NotEmpty(message = "パスワードを入力してください")
	@Size(min = 6, message = "パスワードは6文字以上で入力してください")
	@Column(name = "passwd")
	private String password;

	@Column(name = "create_date", nullable = false, updatable = false)
	private LocalDateTime createDate;

	@NotNull(message = "部署を選択してください")
	@ManyToOne
	@JoinColumn(name = "department_id", nullable = false)
	private Department department;

	@NotEmpty(message = "確認用パスワードを入力してください")
	@Transient
	private String confirmPassword; // データベースに保存しない

	// 権限情報（簡単に空リストを返す）
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}

	@Override
	public String getUsername() {
		return mail; // Spring Security のユーザー名としてメールアドレスを使用
	}

	@Override
	public String getPassword() {
		return password;
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

}
