package com.vlotar.demo.mockserver.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Class describes a User as a resource
 *
 * @author vlotar
 */
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {

	private static final long serialVersionUID = -8053077483648367539L;

	private Long userId;

	private String firstName;

	private String lastName;

	private String email;
}
