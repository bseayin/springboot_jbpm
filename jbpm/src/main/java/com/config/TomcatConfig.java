package com.config;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jndi.JndiObjectFactoryBean;
@Configuration
public class TomcatConfig {
	
	@Autowired
	private DataSourceProperties dataSourceProperties;
	
	@Bean
	public TomcatServletWebServerFactory tomcatFactory() {
		
		

		return new TomcatServletWebServerFactory() {
			@Override
			protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
				tomcat.enableNaming();
				return super.getTomcatWebServer(tomcat);
			}
			
			@Override
			protected void postProcessContext(Context context) {
				ContextResource resource = new ContextResource();
                resource.setType(DataSource.class.getName());
                resource.setName("jdbc/kpi");
                resource.setProperty("factory", "org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory");
               /*
                resource.setProperty("driverClassName", "com.mysql.cj.jdbc.Driver");
                resource.setProperty("url", "jdbc:mysql://localhost:3306/jbpm?useUnicode=true&characterEncoding=utf8&useSSL=true&serverTimezone=Asia/Chongqing");
                resource.setProperty("username", "root");
                resource.setProperty("password", "sa");
               */
                resource.setProperty("driverClassName", dataSourceProperties.getDriverClassName());
                resource.setProperty("url", dataSourceProperties.getUrl());
                resource.setProperty("username", dataSourceProperties.getUsername());
                resource.setProperty("password", dataSourceProperties.getPassword());
                
                context.getNamingResources().addResource(resource);
                
			}
		};
	}

	@Bean(destroyMethod="")
	@DependsOn("tomcatFactory")
    public DataSource jndiDataSource() throws IllegalArgumentException, NamingException {
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName("java:comp/env/jdbc/kpi");
        bean.setProxyInterface(DataSource.class);
        bean.setLookupOnStartup(false);
        bean.afterPropertiesSet();
        return (DataSource) bean.getObject();
    }
}