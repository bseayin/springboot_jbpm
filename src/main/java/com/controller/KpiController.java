package com.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.jsoup.helper.StringUtil;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entity.User;
import com.model.ControllerResponse;
import com.repository.UserRepository;

@RestController
@RequestMapping(value = "/kpi")
@Configuration
public class KpiController {
	@Value("${jbmp.default.processId}")
	private String defaultProcessId;
	
	@Autowired
	private KieSession kieSession;
	@Autowired
	private TaskService taskService;
	@Autowired
	private UserRepository userRepository;
	
	private static final String CODE_SUCCESS="SUCCESS";
	private static final String CODE_FAILED="FAILED";
	
	
	public Long startProcess(String userId) {
		Map<String, Object> data = new HashMap<String, Object>(16);
		data.put("Manager", userId);
		return kieSession.startProcess(defaultProcessId, data).getId();//KieServer-SpringBoot   SpringBoot
	}
	//for post not proccessId
	public Long startProcess(String userId,Map<String, Object> data) {
		return kieSession.startProcess(defaultProcessId, data).getId();//KieServer-SpringBoot   SpringBoot
	}
	
	public Long startProcess(String userId,String processId) {
		if(StringUtil.isBlank(processId)) {
			return startProcess(userId);
		}
		
		Map<String, Object> data = new HashMap<String, Object>(16);
		data.put("Manager", userId);		
		return kieSession.startProcess(processId, data).getId();//KieServer-SpringBoot   SpringBoot
	}
	//for post have proccessId
	public Long startProcess(String userId,String processId,Map<String, Object> data) {
		if(data==null) {
			data = new HashMap<String, Object>(16);
		}
		data.put("Manager", userId);	
		
		if(StringUtil.isBlank(processId)) {
			return startProcess(userId,data);
		}
			
		return kieSession.startProcess(processId, data).getId();//KieServer-SpringBoot   SpringBoot
	}
	
	
	@PostMapping(value = "startProcess")
	public ControllerResponse startProcessPost(String userId,String processId,@Valid @RequestBody Map<String,Object> data) {
		
		ControllerResponse cr=null;
		try {		
			long obj = startProcess(userId,processId,data) ;			
			cr=new ControllerResponse(CODE_SUCCESS,null,obj);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		
		return cr;
	}
	
	@PostMapping(value = "startProcessNew")	
	public ControllerResponse startProcessNewPost(String userId,String processId,@Valid @RequestBody Map<String,Object> data) {
		
		ControllerResponse cr=null;
		try {		

			long processInstanceId = startProcess(userId,processId,data) ;
			
			List<TaskSummary> list=taskService.getTasksByStatusByProcessInstanceId(processInstanceId, null,userId);
			
			System.out.println(list.size());
			
			Map<String,Object> map=new HashMap<String,Object>();
			
			map.put("processInstanceId", processInstanceId);
			map.put("taskList", list);
				
			cr=new ControllerResponse(CODE_SUCCESS,null,map);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		
		return cr;
	}
	
	/*
	@GetMapping(value = "startProcess")
	public ControllerResponse startProcess1(String userId,String processId) {
		
		ControllerResponse cr=null;
		try {		
			long obj = startProcess(userId,processId) ;			
			cr=new ControllerResponse(CODE_SUCCESS,null,obj);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		
		return cr;
	}
	
	@GetMapping(value = "startProcessNew")	
	public ControllerResponse startProcessNew(String userId,String processId) {
		
		ControllerResponse cr=null;
		try {		

			long processInstanceId = startProcess(userId,processId) ;
			
			List<TaskSummary> list=taskService.getTasksByStatusByProcessInstanceId(processInstanceId, null,userId);
			
			System.out.println(list.size());
			
			Map<String,Object> map=new HashMap<String,Object>();
			
			map.put("processInstanceId", processInstanceId);
			map.put("taskList", list);
				
			cr=new ControllerResponse(CODE_SUCCESS,null,map);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		
		return cr;
	}
	*/
	
	
	@GetMapping(value="getMyAssignedTask")
	public ControllerResponse getMyAssignedTask(String userId) {
		
		ControllerResponse cr=null;
		try {		
			List<TaskSummary> obj = taskService.getTasksAssignedAsPotentialOwner(userId, "en_US");
			
			cr=new ControllerResponse(CODE_SUCCESS,null,obj);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		
		return cr;
	}
	
	
	
	
	@GetMapping(value="getProcessTaskList")
	public ControllerResponse getProcessTaskList(Long processInstanceId) {
		
		ControllerResponse cr=null;
		try {		
			List<TaskSummary> obj=taskService.getTasksByStatusByProcessInstanceId(processInstanceId, null, "en_US");
			cr=new ControllerResponse(CODE_SUCCESS,null,obj);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		
		return cr;
	}
	
	@GetMapping(value="getProcessTaskListByStatus")
	public ControllerResponse getProcessTaskListByStatus(Long processInstanceId,Status status) {
		
		ControllerResponse cr=null;
		try {		
			List<Status> statusList=new ArrayList<Status>();
			statusList.add(status);
			List<TaskSummary> obj = taskService.getTasksByStatusByProcessInstanceId(processInstanceId, statusList, "en_US");
			cr=new ControllerResponse(CODE_SUCCESS,null,obj);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		
		return cr;
	
	}
	
	@GetMapping(value="getTaskById")
	public ControllerResponse getTaskById(Long taskId) {	
		
		ControllerResponse cr=null;
		try {
			Task obj=taskService.getTaskById(taskId);
			cr=new ControllerResponse(CODE_SUCCESS,null,obj);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		
		return cr;
	}
	/*
	@GetMapping(value="getMyOwnedTask")
	public ControllerResponse getMyOwnedTask() {
		ControllerResponse cr=null;
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			List<TaskSummary> obj= taskService.getTasksOwned(auth.getName(), "en_US");
			cr=new ControllerResponse(CODE_SUCCESS,null,obj);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		
		return cr;
		
	}
	*/
	@GetMapping(value="getMyOwnedTask")
	public ControllerResponse getMyOwnedTask(String userId) {
		ControllerResponse cr=null;
		try {
			List<TaskSummary> obj= taskService.getTasksOwned(userId, "en_US");
			cr=new ControllerResponse(CODE_SUCCESS,null,obj);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		
		return cr;
		
	}
	
	@GetMapping(value="getMyOwnedTaskByStatus")
	public ControllerResponse getMyOwnedTask(String userId,Status status) {
		ControllerResponse cr=null;
		try {	
			List<Status> statusList=new ArrayList<Status>();
			statusList.add(status);
			List<TaskSummary> obj=  taskService.getTasksOwnedByStatus(userId,statusList, "en_US");
			cr=new ControllerResponse(CODE_SUCCESS,null,obj);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		
		return cr;
	}
	
	
	
	@GetMapping("startTask")
	public ControllerResponse startTask(String userId,Long taskId) {	
		ControllerResponse cr=null;
		try {
			taskService.start(taskId, userId);
			cr=new ControllerResponse(CODE_SUCCESS,null,null);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		return cr;
	}
	@PostMapping("completeTask")
	public ControllerResponse completeTask(String userId,Long taskId,@Valid @RequestBody Map<String, Object> data) {		
		ControllerResponse cr=null;
		try {
			taskService.complete(taskId, userId, data);
			cr=new ControllerResponse(CODE_SUCCESS,null,null);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		return cr;
	}
	
	/*
	@GetMapping("completeTask")
	public ControllerResponse completeTask(String userId,Long taskId) {		
		ControllerResponse cr=null;
		try {
			taskService.complete(taskId, userId, null);
			cr=new ControllerResponse(CODE_SUCCESS,null,null);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		return cr;
	}
	*/
	@GetMapping("claimTask")
	public ControllerResponse calimTask(String userId,Long taskId) {
		
		ControllerResponse cr=null;
		try {
			taskService.claim(taskId, userId);
			cr=new ControllerResponse(CODE_SUCCESS,null,null);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		return cr;
	}
	
	@GetMapping("assignTask")
	public ControllerResponse assignTask(String userId,Long taskId,String targetUserId) {	
		
		ControllerResponse cr=null;
		try {
			taskService.delegate(taskId, userId, targetUserId);
			cr=new ControllerResponse(CODE_SUCCESS,null,null);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		return cr;
		
	}
	
	//用于测试 jpa是否配置生效
	@GetMapping("getUserByUsername/{username}")
	public ControllerResponse getUserName(@PathVariable("username") String username) {
		ControllerResponse cr=null;
		try {
			User obj=userRepository.findUserByName(username);
			cr=new ControllerResponse(CODE_SUCCESS,null,obj);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		return cr;
		
	}
	/*
	@GetMapping(value = "getTaskContent")	
	public ControllerResponse getTaskContent(Long taskId) {
		
		ControllerResponse cr=null;
		try {		
			
			Map<String,Object> map=taskService.getTaskContent(taskId);
			cr=new ControllerResponse(CODE_SUCCESS,null,map);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		
		return cr;
	}
	
	@GetMapping(value = "getProcessData")	
	public ControllerResponse getProcessData(Long processInstanceId) {
		
		ControllerResponse cr=null;
		try {		
			
			ProcessInstance obj=kieSession.getProcessInstance(processInstanceId);
			
			cr=new ControllerResponse(CODE_SUCCESS,null,obj);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		
		return cr;
	}
	
	@GetMapping(value = "getAllProcess")	
	public ControllerResponse getAllProcess() {
		
		ControllerResponse cr=null;
		try {		
			
			Object obj=kieSession.getProcessInstances();
			
			cr=new ControllerResponse(CODE_SUCCESS,null,obj);
		}catch(Exception e) {
			cr=new ControllerResponse(CODE_FAILED,e.getMessage(),null);
		}
		
		return cr;
	}
	*/
}