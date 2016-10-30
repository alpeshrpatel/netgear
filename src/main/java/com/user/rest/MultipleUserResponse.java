package com.user.rest;

import java.util.List;

import com.user.model.User;

public class MultipleUserResponse {
	private boolean success;
	private List<User> users;
	
	public MultipleUserResponse(boolean success, List<User> users) {
		this.success = success;
		this.users = users;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
}
