package com.aventstack.extentreports.reporter.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aventstack.extentreports.Status;

abstract class BasicConfiguration {

	List<Status> levels;
	String reportName;
	Map<String, String> usedConfigs;
	
	public BasicConfiguration() {
		usedConfigs = new HashMap<>();
	}
	
	public Map<String, String> getConfigMap() {
		return usedConfigs;
	}
	
	public void setLevel(Status... level) {
		if (levels == null) {
			levels=new ArrayList<>();
		}
		Arrays.stream(level).forEach(levels::add);
	}
	
	public List<Status> getLevel() { return levels; }
	
	public void setReportName(String reportName) {
		usedConfigs.put("reportName", reportName);
	}
	
	public String getReportName() { return reportName; }
	
	public void setTimeStampFormat(String format) {
		usedConfigs.put("timeStampFormat", format);
	}
	
	public String getTimeStampFormat() {
		return usedConfigs.get("timeStampFormat");
	}
	
	public void setVersion(String version) {
		usedConfigs.put("version", version);
	}
	
	public String getVersion() {
		return usedConfigs.get("version");
	}
}
