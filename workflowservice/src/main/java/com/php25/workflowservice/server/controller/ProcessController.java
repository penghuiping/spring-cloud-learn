package com.php25.workflowservice.server.controller;

import com.php25.common.flux.web.JSONController;
import com.php25.common.flux.web.JSONResponse;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author penghuiping
 * @date 2019/12/3 16:58
 */
@Slf4j
@RestController
@RequestMapping("/process")
public class ProcessController extends JSONController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;


    @GetMapping("/holidayRequest")
    public JSONResponse holidayRequest() {
//        RepositoryService repositoryService = processEngine.getRepositoryService();
//        Deployment deployment = repositoryService.createDeployment()
//                .addClasspathResource("holiday-request.bpmn20.xml")
//                .deploy();

//        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
//                .deploymentId(deployment.getId())
//                .singleResult();
//        System.out.println("Found process definition : " + processDefinition.getName());

        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().active().list();
        for (ProcessDefinition processDefinition : processDefinitions) {
            log.info(processDefinition.getName());
//            repositoryService.deleteDeployment(processDefinition.getDeploymentId(),true);
        }

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employee", "jack");
        variables.put("nrOfHolidays", 2);
        variables.put("description", "家里有事情");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayRequest", variables);

        //查询经理级别的组的任务
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            Map<String, Object> vars = taskService.getVariables(task.getId());
            String employeeName = vars.get("employee").toString();
            String holidayNumbers = vars.get("nrOfHolidays").toString();
            String description = vars.get("description").toString();

            //经理查看请假内容
            String content = String.format("姓名为:%s,想要请假%s天,原因是:%s", employeeName, holidayNumbers, description);
            log.info("请假条的内容为:{}", content);

            //经理同意请假申请
            variables = new HashMap<String, Object>();
            variables.put("approved", true);
            taskService.complete(task.getId(), variables);
        }

        //jack查看请假申请结果
        List<Task> tasks1 = taskService.createTaskQuery().taskAssignee("jack").list();
        for (int i = 0; i < tasks1.size(); i++) {
            Task task = tasks1.get(i);
            log.info(task.getName());
            taskService.complete(task.getId());
        }

        return succeed(true);
    }

    @GetMapping("/financialReportProcess")
    public JSONResponse financialReportProcess() {
        // Create Flowable process engine
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneProcessEngineConfiguration()
                .buildProcessEngine();

        // Get Flowable services
        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        // Deploy the process definition
        repositoryService.createDeployment()
                .addClasspathResource("FinancialReportProcess.bpmn20.xml")
                .deploy();

        // Start a process instance
        String procId = runtimeService.startProcessInstanceByKey("financialReport").getId();

        // Get the first task
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("accountancy").list();
        for (Task task : tasks) {
            System.out.println("Following task is available for accountancy group: " + task.getName());

            // claim it
            taskService.claim(task.getId(), "fozzie");
        }

        // Verify Fozzie can now retrieve the task
        tasks = taskService.createTaskQuery().taskAssignee("fozzie").list();
        for (Task task : tasks) {
            System.out.println("Task for fozzie: " + task.getName());

            // Complete the task
            taskService.complete(task.getId());
        }

        System.out.println("Number of tasks for fozzie: "
                + taskService.createTaskQuery().taskAssignee("fozzie").count());

        // Retrieve and claim the second task
        tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        for (Task task : tasks) {
            System.out.println("Following task is available for management group: " + task.getName());
            taskService.claim(task.getId(), "kermit");
        }

        // Completing the second task ends the process
        for (Task task : tasks) {
            taskService.complete(task.getId());
        }

        // verify that the process is actually finished
        HistoryService historyService = processEngine.getHistoryService();
        HistoricProcessInstance historicProcessInstance =
                historyService.createHistoricProcessInstanceQuery().processInstanceId(procId).singleResult();
        System.out.println("Process instance end time: " + historicProcessInstance.getEndTime());
        return succeed(true);
    }


}
