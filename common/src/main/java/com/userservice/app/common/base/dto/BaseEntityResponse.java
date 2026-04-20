package com.userservice.app.common.base.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseEntityResponse {

	private Long version;

	private LocalDateTime createdAt;

	private LocalDateTime modifiedAt;
}
