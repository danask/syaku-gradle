# # Spring Hibernate ORM : 스프링 하이버네이트

### 개발환경

> Mac OS X 10.11.4  
Java 8  
Spring 4.2.4.RELEASE  
Hibernate 5.1.0.Final  
MariaDB 10.1.13

나는 여태껏 myBATIS를 사용해왔다. 요즘은 다들 ORM 프레임워크 하이버네이트나 Spring JPA를 많이 사용하는 것 같아 이참에 한번 공부해보기로 맘먹고 포스팅을 준비해보았다. 예전에 개인적인 프로젝트로 MongoDB를 사용하면서 Spring JPA를 사용했는 데, 객체를 이용해한 쿼리 구문이 참 매력적이였다. 

ORM 이름만봐도 대충 뭐 객체를 이용하는 것이구나라고 생각만 했는 데 직접 사용해보니 현대적 방식의 패턴임은 분명한 것 같다. 객체로 릴레이션을 구현하면서 자동적이로 쿼리가 완성되니 어떤 DBMS를 사용한다 하더라도 개발자는 SQL을 신경쓰지 않아도 된다. 그렇다고 SQL을 모르는 사람이 사용할 수 있다고 말하는 것은 아니다.

아~ ORM(Object-Relation Mapping)는 DBMS의 객체와 관계를 연결해주는?? 객체지향 프로그램을 하기 위한 기술이다. ( 참고 : http://debop.blogspot.kr/2012/02/orm-object-relational-mapping.html)  
JPA(Java Persistent API)는 ORM을 위한 표준 기술이다. ( 참고 : http://blog.woniper.net/255 ) 마지막으로 하이버네이트(Hibernate)는 JBoss에서 개발한 ORM프레임워크이다. Spring JPA는 Spring 프레임워크 하위 프로젝트인 Spring Data에 속해있는 JPA 라이브러리이다. ( Spring JPA 한글 번역 : http://arahansa.github.io/docs_spring/jpa.html )

그래서 오늘의 주제는 하이버네이트이다. 사용하기전에 한번 읽어보면 좋은 내용이 있어 링크를 해두었다.
http://www.moreagile.net/2013/11/blog-post_26.html

그리고 관련이 없는 내용이지만 이번에는 꼭 알아보고 넘어갔으면 하는 라이브러리가 있다. 그것은 로그 관리 라이브러리 slf4j이다. 좋다는 이야기는 많이 들었지만 무엇이 좋은지 한번도 알아본적이 없었다. 무지 바빠다... ;;; 여튼 이것저것 찾아보면서 좋은 내용을 링크해보았다.

#### slf4j와 LOGBack이 무엇이며 어떻게 좋은가?
https://beyondj2ee.wordpress.com/2012/11/09/logback-%EC%82%AC%EC%9A%A9%ED%95%B4%EC%95%BC-%ED%95%98%EB%8A%94-%EC%9D%B4%EC%9C%A0-reasons-to-prefer-logback-over-log4j/

#### slf4j 설정 방식이 조금씩 다른데 왜 그런지?
http://whiteship.tistory.com/2541

#### slf4j와 LOGBack 설정방법
http://www.mkyong.com/spring-mvc/spring-mvc-logback-slf4j-example  
http://saltnlight5.blogspot.kr/2013/08/how-to-configure-slf4j-with-different.html

서론이 너무 길어진 것 같다. 이제 본격적인 하이버네이트 세상에 첫발을 내딛어보자. 우선 개발환경을 구축해야 한다. 최근에 빌드툴을 Gradle을 사용하고 있다. 하지만 Maven하고 설정은 크게 다르지 않기때문에 의존성만 잘 설정해주면 될 것 같다. 자세한 소스는 Github를 확인하기 바란다.

**# build.gradle**

```
... 생략 ...

configurations.all {
    exclude group: "commons-logging", module: "commons-logging"
}

def version = [
        slf4j: '1.7.12',
        junit: '4.9',
        spring: '4.2.4.RELEASE',
        aspectj: '1.8.8',
        jackson: '2.1.0',
        mariadb: '1.4.0',
        hibernate: '5.1.0.Final'
]

dependencies {
    compile "org.springframework:spring-webmvc:${version.spring}"

    compile "org.aspectj:aspectjweaver:${version.aspectj}"

    runtime "org.slf4j:jcl-over-slf4j:${version.slf4j}"
    compile "ch.qos.logback:logback-classic:1.1.7"

    testCompile "org.springframework:spring-test:${version.spring}"
    testCompile "junit:junit:${version.junit}"

    compile "org.hibernate:hibernate-core:${version.hibernate}"

    compile "org.mariadb.jdbc:mariadb-java-client:${version.mariadb}"
    compile "org.springframework:spring-jdbc:${version.spring}",
            "org.springframework:spring-orm:${version.spring}",
            "org.springframework:spring-tx:${version.spring}",
            "commons-dbcp:commons-dbcp:1.4"
}

... 생략 ...

```

다음은 스프링 프레임워크 애플리케이션 설정부분이다.

**src/main/resources/org/syaku/config/hibernate-context.xml**

```xml
... skip ...

	<!-- 하이버네이트 인스턴스 생성 -->
	<bean id="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean" >
		<property name="dataSource" ref="dataSource"/>
		<property name="packagesToScan" value="org.syaku.**.domain" /> <!-- 대상이 될 모든 엔티티 위치 설정 -->
		<property name="hibernateProperties">
			<props>
				<prop key="hibernate.dialect">org.hibernate.dialect.MySQL5Dialect</prop>
				<prop key="hibernate.format_sql">true</prop>
				<prop key="hibernate.show_sql">true</prop><!-- 실행된 쿼리 출력 여부 -->
			</props>
		</property>
	</bean>

	<bean id="transactionManager" class="org.springframework.orm.hibernate5.HibernateTransactionManager">
		<property name="sessionFactory" ref="sessionFactory" />
	</bean>

	<!-- 어노테이션 트랜잭션 사용 -->
	<tx:annotation-driven transaction-manager="transactionManager"/>
</beans>
```

packagesToScan 프로퍼티에 퍼시스턴트 클래스? 엔티티클래스? 가 위치한 패키지를 입력하면 된다. Ant style pattern으로 입력이 가능하다. ( 퍼시스턴트란? http://egloos.zum.com/stringargs/v/1794387 )

퍼시스턴트 클래스 혹은 엔티티 클래스란 테이블 정의 클래스라고 한다. 즉 DB 테이블을 객체화 한 클래스라고 보면 된다. 해당 클래스에 `@Entity` 어노테이션이 있어야 자동으로 로드된다.

hibernateProperties 프로퍼티는 하이버네이트 설정을 수동으로 관리할 수 있다.

- hibernate.dialect:
- hibernate.format_sql:
- hibernate.show_sql: 실행된 쿼리를 디버깅할 수 있게 로그를 출력한다.

	```
	2016-04-11 14:21:41 INFO  o.s.o.h.HibernateTransactionManager - Using DataSource [org.apache.commons.dbcp.BasicDataSource@27cab24c] of Hibernate SessionFactory for HibernateTransactionManager
	Hibernate: 
	    delete 
	    from
	        demo
	Hibernate: 
	    insert 
	    into
	        DEMO
	        (NAME, VALUE) 
	    values
	        (?, ?)
	```


하이버네이트를 사용하기 위한 테이블을 만들어보겠다. 아래의 스키마를 이용해 생성하고 CRUD 기능을 구현해보자.

**데모 테이블 스키마**

```
CREATE TABLE `demo` (
  `demo_idx` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `value` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`demo_idx`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;

```

### 테스트

**src/test/java/org/syaku/demo/dao/DemoDAOTest.java**

```java
package org.syaku.demo.dao;

... skip ...

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:org/syaku/config/*-context.xml"
})
@Transactional
public class DemoDAOTest {
	@Autowired private SessionFactory sessionFactory;
	@Resource(name = "demoDAO") private DemoDAO demoDAO;

	@Ignore
	public void insertToDelete() {
		Demo demo = new Demo("최석균", "syaku");
		demoDAO.insert(demo);
		demoDAO.delete(demo.getDemo_idx());
		sessionFactory.getCurrentSession().flush();

		Demo demo2 = new Demo("최석균", "syaku");
		demoDAO.insert(demo2);
		demoDAO.delete(demo2);
		sessionFactory.getCurrentSession().flush();
	}

	@Test
	public void update() {
		Demo demo1 = new Demo("최석균", "syaku");
		demoDAO.updateBySave(demo1);
		System.out.println(demo1.toString());
		System.out.println("updateBySave ========> " + demoDAO.findAll().size());
		demoDAO.updateBySave(demo1);
		System.out.println("updateBySave2 ========> " + demoDAO.findAll().size());
		sessionFactory.getCurrentSession().flush();

		Demo demo = demoDAO.findOne(demo1.getDemo_idx());
		demo.setName("변경됨");
		demo.setValue("change");
		demoDAO.update(demo);
		System.out.println("updateBySave ========> " + demo.toString());
		sessionFactory.getCurrentSession().flush();

		Demo demo2 = new Demo("최석균2", "syaku2");
		demoDAO.updateByMerge(demo2);
		System.out.println(demo2.toString());
		System.out.println("updateByMerge ========> " + demoDAO.findAll().size());
		sessionFactory.getCurrentSession().flush();
	}
}
```

처음이라 그런지 테스트를 만들면서도 하이버네이트를 단순하게 생각하고 사용하면 안될것 같다는 생각이 든다. 꼭 꼭 하이버네이트의 라이프사이클은 이해하고 사용하기 바란다.
( 참고: http://dev.anyframejava.org/anyframe/doc/core/3.2.0/corefw/guide/hibernate-introduction.html )

테스트 소스에는 CRUD에 관한 작업으로 모두 담았다. 하이버네이트는 배치를 이용하는 것 같고 그래서 그런지 처리 순서가 위에서 아래로 순차적으로 흐르지 않고 하이버네이트에 의해 순서가 조절되는 것 같다. 작업 하나하나가 바로 디비로 전달되는 것이 아니라 메모리에 기억해두었다가 커밋이 일어나면 처리가 되는 것 같다. 그래서 순차적으로 처리하고 싶다면 중간중간에 flush를 실행해서 detached 상태를 만들어 주면 된다. 하이버네이트에는 3가지 퍼시스턴스 객체 상태를 가질 수 있다.

- Transient: 퍼시스턴스 객체가 생성된 상태지만 하이버네이트에서 관리되지 않는 상태.
- Persistent: 하이버네이트에서 관리되는 상태.
- Detached: 하이버네이트에서 관리가 제외된 상태.

이제 테스트소스를 보도록하자. 원래 DAO를 직접 사용하는 것 보다 Service를 통해 사용하는 것이 바람직하지만 테스트를 위해 직접 DAO를 사용하여 테스트를 구현하였다. 그리고 DAO에는 트랜잭션이 걸려있지 않아 직접 어노테이션을 이용해 트랜잭션을 걸어주었다. 꼭 트랜잭션 설정이 있어야 오류가 발생하지 않는 다. `@Transactional`

`insertToDelete` 테스트는 insert 후 delete를 실행하는 데 2가지 방법으로 구성했다. 첫번째는 쿼리를 직접 타이핑한 작업이고 두번째는 delete를 사용한 것이다. 여기서 두번째 delete는 퍼시스턴트 상태의 객체를 사용해야 삭제가 된다.

첫번째 쿼리를 직접 타이핑하는 방식에 하이버네이트에서 지원하는 3가지 기능을 설명하겠다.

- createQuery: HQL(Hibernate Query 언어)을 사용할 수 있다.
- createSQLQuery: native SQL을 사용할 수 있다.
- createCriteria: Criteria API를 사용할 수 있다. 단 읽기만 가능하다.

[참고] http://stackoverflow.com/questions/8636806/difference-between-hibernate-createcriteria-createquery-createsqlquery-functio

`update` 테스트는 업데이트와 그리고 인서트도 같이 해주는 3가지 방법이 있다. save와 merge의 역활은 비슷해보인다. 둘다 데이터가 있는 경우 업데이트 없는 경우 인서트를 처리한다. 하지만 결과가 다른다.

save log

```
Hibernate: 
    insert 
    into
        DEMO
        (NAME, VALUE) 
    values
        (?, ?)
org.syaku.demo.domain.Demo@58ad7ba[demo_idx=111,name=최석균,value=syaku]
```

merge log

```
Hibernate: 
    insert 
    into
        DEMO
        (NAME, VALUE) 
    values
        (?, ?)
org.syaku.demo.domain.Demo@32bde6a[demo_idx=0,name=최석균2,value=syaku2]
```

save는 demo_idx가 있고 merge는 demo_idx가 없다. 즉 save는 실행후 `persistent` 상태를 유지하고 merge는 `detached` 상태가 된다. 좀 더 정확한 이해를 위해 테스트를 하나 더 해보겠다.

```java
	@Test
	public void persistent() {
		Demo demo1 = new Demo("최석균", "syaku");
		demoDAO.insert(demo1);
		System.out.println(demo1.toString());
		
		demo1.setName("최석균2");
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();

		Demo demo = demoDAO.findOne(demo1.getDemo_idx());
		System.out.println(demo.toString());

		demo1.setName("최석균3");

		Demo demo3 = demoDAO.findOne(demo.getDemo_idx());
		System.out.println(demo3.toString());
	}
```

위 소스는 persistent 상태와 detached 상태인 경우를 이해하기 위한 테스트이다.

우선 demo1을 인서트한 후 demo1의 이름을 수정하고 플래쉬를 했다. 이럴경우 insert 요청만 했는데 업데이트까지 자동으로 진행한다. 정말 신기하다. 그리고 flush&clear를 요청하여 detached 상태를 만든 후 다시 호출하고 변경해 보았다. 변화가 없다. 아래 로그를 보면 더 정확하게 확인 할 수 있다.

[참고] http://stackoverflow.com/questions/20395543/how-to-change-the-ordering-of-sql-execution-in-hibernate

**실행 로그**

```
Hibernate: 
    insert 
    into
        DEMO
        (NAME, VALUE) 
    values
        (?, ?)
org.syaku.demo.domain.Demo@7da945af[demo_idx=117,name=최석균,value=syaku]
Hibernate: 
    update
        DEMO 
    set
        NAME=?,
        VALUE=? 
    where
        DEMO_IDX=?
Hibernate: 
    select
        demo0_.DEMO_IDX as DEMO_IDX1_0_0_,
        demo0_.NAME as NAME2_0_0_,
        demo0_.VALUE as VALUE3_0_0_ 
    from
        DEMO demo0_ 
    where
        demo0_.DEMO_IDX=?
org.syaku.demo.domain.Demo@37e1e13[demo_idx=117,name=최석균2,value=syaku]
org.syaku.demo.domain.Demo@37e1e13[demo_idx=117,name=최석균2,value=syaku]
```

이런식이라면 코딩라인이 엄청 줄어들 것 같다. 그리고 한번 요청한 쿼리는 다시 요청없이 그대로 사용할 수 있어 매우 효율적이다. 사용하면 할 수록 참 매력적이다. 많은 처리 작업으로 인한 충돌이 생기지 않을까? 의문이 생기지만 이미 훌륭한 오픈소스로 인증된 프레임워크이기에 의심할 필요는 없을 것 같다. 음 더 많은 내용을 작성하고 싶지만 아직 아는 것이 많지 않기때문에 잘못된 정보로 혼란을 줄 수 있으니 여기까지만 하도록하겠다. 다음에는 Spring JPA와 QueryDSL을 사용해 볼 것이다.

#### Spring AOP 트랜잭션 설정

```xml
<tx:advice id="adviceService" transaction-manager="transactionManager">
	<tx:attributes>
		<tx:method name="get*" read-only="true"/>
		<tx:method name="*" />
	</tx:attributes>
</tx:advice>

<aop:config>
	<aop:pointcut id="pointcutService" expression="execution(* org.syaku..service.*.*(..))" />

	<aop:advisor pointcut-ref="pointcutService" advice-ref="adviceService" />
</aop:config>
```

#### 공유자료
- Post : 
- Github : https://github.com/syakuis/syaku-gradle/tree/spring-hibernate5

#### 추천 참고자료
- http://whiteship.tistory.com/category/Hibernate
- entityManagerFactory 사용하기
	- http://www.baeldung.com/2011/12/13/the-persistence-layer-with-spring-3-1-and-jpa/ 
- 그외 참고자료
	- http://www.journaldev.com/3524/spring-hibernate-integration-example-tutorial-spring-4-hibernate-3-and-hibernate-4
	- http://websystique.com/spring/spring4-hibernate4-mysql-maven-integration-example-using-annotations/
	- http://www.journaldev.com/7772/spring-orm-example-with-jpa-hibernate-aop-transactions
	- http://myjourneyonjava.blogspot.kr/2015/09/spring-4-mvc-hibernate-4-mysql-5-maven.html
