package com.config;

import javax.persistence.EntityManagerFactory;

import org.jbpm.runtime.manager.impl.RuntimeEnvironmentBuilder;
import org.jbpm.services.task.identity.DBUserGroupCallbackImpl;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.task.TaskService;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.kie.internal.task.api.UserGroupCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class JbpmConfig {
	
	@Value("${jbmp.bpmn2file.path}")
	private String bpmn2filePath;
	
	@Bean
	RuntimeEnvironment runtimeEnvironment(EntityManagerFactory entityManagerFactory) {
		org.kie.api.runtime.manager.RuntimeEnvironmentBuilder builder= RuntimeEnvironmentBuilder.Factory.get().newDefaultBuilder().entityManagerFactory(entityManagerFactory);
		
		for(String bpmn:bpmn2filePath.split(",")) {
			builder.addAsset(ResourceFactory.newClassPathResource(bpmn), ResourceType.BPMN2);
		}
		
		return builder.get();
	}
	
	@Bean
	RuntimeManager runtimeManager(RuntimeManagerFactory runtimeManagerFactory,RuntimeEnvironment runtimeEnvironment) {
		RuntimeManager runtimeManager = runtimeManagerFactory.newSingletonRuntimeManager(runtimeEnvironment);
		return runtimeManager;
	}
	@Bean
	RuntimeEngine runtimeEngine(RuntimeManager runtimeManager) {
		RuntimeEngine runtimeEngine = runtimeManager.getRuntimeEngine(EmptyContext.get());
		return runtimeEngine;
	}
	@Bean
	KieSession kieSession(RuntimeEngine runtimeEngine) {
		KieSession kieSession = runtimeEngine.getKieSession();
		return kieSession;
	}
	@Bean
	TaskService taskService(RuntimeEngine runtimeEngine) {
		TaskService taskService = runtimeEngine.getTaskService();
		return taskService;
	}
   @Bean
	@DependsOn("jndiDataSource")
	UserGroupCallback userGroupCallback() {
		return new DBUserGroupCallbackImpl(true);
	}
	
	
}