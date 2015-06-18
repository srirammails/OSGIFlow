package com.flow.shellx.api;

import java.util.Dictionary;
import java.util.List;

public interface IConfigurationService {

	public abstract List<Dictionary<String, String>> getScopedConfigurations(
			String scopename, String scopevalue);

}