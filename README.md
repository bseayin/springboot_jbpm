# springboot_jbpm

#### 介绍
springboot启动jbpm流程，并提供api接口

#### 软件架构
软件架构说明:  
   开发工具:eclipse+maven+jdk1.8  
   框架技术:springboot/jbpm7(kieserver)/mysql/jpa/spring security



#### 安装教程

1.  git下载project
2.  eclpise导入maven project
3.  配置mysql,并执行tomcat_user.sql创建用户权限表(供spring security使用)
4.  配置bpmn文件(已提供hello.bpmn和Test.bpmn两个文件 两个workflow/process)
    也可使用自己的bpmn文件
5.  启动Application
6.  访问swagger查看api  http://localhost:8080/jbpm/swagger-ui.html  
    访问账号密码:  
    账号:Nero/JOJO/Test/Administrator  (也可在kpi_user表配置)
    密码都是123456 

#### 使用说明

1.  api startProcess/startProcessNew  
    startProcess:   只会返回processInstanceId  
    startProcessNew:还会返回第一个Task的信息
2.  Task api:(Task生命周期)
    claimTask
    assignTask
    startTask
    completeTask
3.  search api:(查看Process/Task的信息)  
    自行研究
4.  可使用HttpClientUtil.java测试

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
