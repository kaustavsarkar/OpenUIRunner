package org.our.configuration;

public enum ConfigOptions {
	USER_CONFIG("user.config"), PROFILE_CONFIG("profile.config");

	private String configName;

	ConfigOptions(String configName) {
		this.configName = configName;
	}

	public String getConfigName() {
		return this.configName;
	}
}
