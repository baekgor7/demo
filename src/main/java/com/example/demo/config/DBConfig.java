package com.example.demo.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@PropertySource("classpath:/application.properties")	//properties 파일의 위치를 지정
@EnableTransactionManagement	//애너테이션 기반 트랜잭션을 활성화
@Configuration	//설정 파일로 인식
public class DBConfig {
	
	//빈(Bean)으로 등록된 인스턴스(이하 객체)를 클래스에 주입할 때 사용(@Autowired 이외에도 @Resource, @Inject 등이 존재)
	@Autowired
	private ApplicationContext applicationContext;

	//Configuration 클래스에서 메서드 레벨에만 지정이 가능하며 빈(Bean)으로 등록
	@Bean
	//application.properties 파일에서 spring.datasource.hikari로 시작하는 설정들을 모두 읽어 들여서 해당 메서드에 바인딩
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		
		//히카리CP 객체를 생성합니다. 히카리CP는 커넥션 풀(Connection Pool) 라이브러리 중 하나
		return new HikariConfig();
	}

	@Bean
	public DataSource dataSource() {
		
		//데이터 소스 객체를 생성
		return new HikariDataSource(hikariConfig());
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		
		//SqlSessionFactoryBean은 마이바티스(MyBatis)와 스프링의 연동 모듈
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		
		factoryBean.setDataSource(dataSource());
		factoryBean.setMapperLocations(applicationContext.getResources("classpath:/mappers/**/*Mapper.xml"));
		factoryBean.setTypeAliasesPackage("com.example.demo");
		factoryBean.setConfiguration(mybatisConfg());
		
		return factoryBean.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSession() throws Exception {
		
		return new SqlSessionTemplate(sqlSessionFactory());
	}
	
	//application.properties에서 mybatis.configuration으로 시작하는 모든 설정을 읽어 들여서 빈(Bean)으로 등록
	@Bean
	@ConfigurationProperties(prefix = "mybatis.configuration")
	public org.apache.ibatis.session.Configuration mybatisConfg() {
		
		return new org.apache.ibatis.session.Configuration();
	}

	//트랜잭션 매니저를 빈(Bean)으로 등록
	@Bean
	public PlatformTransactionManager transactionManager() {
		
		return new DataSourceTransactionManager(dataSource());
	}
}
