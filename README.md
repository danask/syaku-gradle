# # Spring Hibernate ORM : 스프링 하이버네이트

### 개발환경

> Mac OS X 10.11.4  
Java 8  
Spring 4.2.4.RELEASE  
Hibernate 5.1.0.Final  
MariaDB 10.1.13

자바 ORM 프레임워크 하이버네이트에 대해서 알아본다. 나는 여태껏 myBATIS 프레임워크를 사용해 왔는 데 몽고DB를 사용하기 위해서 Spring JPA를 접하게 되었는 다. 객체를 이용해서 쿼리를 완성하는 것이 참 매력적이였다. 이렇게 되면 어떤 DBMS(NoSQL 제외)를 사용하든 개발자는 신경쓰지 않아도 된다. 개발자는 ORM 메뉴얼에 있는 설명대로 프로그램을 코딩하면 ORM 프레임워크 혹은 JPA가 알아서 척척 쿼리를 완성한다는 것이다. 하지만 완벽하게 쿼리를 척척 만들지 못할 수 있기 때문에 직접 타이핑할 수 있게 기능도 제공한다.

프레임워크를 사용하기 전에!!! 올바른 자바 프레임워크 선택하기 포스팅을 한번 읽어보고 시작하는 것이 좋을 것 같다.
http://www.moreagile.net/2013/11/blog-post_26.html

하이버네이트와 좀 별개의 내용이지만 이제와서??? slf4j에 대해 한번 검토하고 넘어가야 할것같다. 알면 스킵~
난 Log4j보다 slf4j가 좋다는 이야기만 듣고 사용했었는 데... 왜? 대체 무엇이? 라는 생각을 할 시간이 없었다... 그래서 이번에는 이것저것 찾아보고 혹시나 나같은 사람이 있지 않을까 생각하여 좋은 포스팅을 링크하였다. 읽어보면 도움이 많이 될 것이다.

### slf4j와 LOGBack이 무엇이며 어떻게 좋은가?
https://beyondj2ee.wordpress.com/2012/11/09/logback-%EC%82%AC%EC%9A%A9%ED%95%B4%EC%95%BC-%ED%95%98%EB%8A%94-%EC%9D%B4%EC%9C%A0-reasons-to-prefer-logback-over-log4j/

### slf4j 설정 방식이 조금씩 다른데 왜 그런지?
http://whiteship.tistory.com/2541

### slf4j와 LOGBack 설정방법
http://www.mkyong.com/spring-mvc/spring-mvc-logback-slf4j-example  
[참고] http://saltnlight5.blogspot.kr/2013/08/how-to-configure-slf4j-with-different.html

이제 본격적으로 하이버네이트를 사용해보자. 최근 gradle을 사용하고 있어서 빌드는 gradle로 하지만 maven과는 크게 다르지 않다~ 자세한 소스는 Github를 통해 확인하기 바란다.

**# build.gradle**

```json
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

maven에서는 dependencies 라이브러리만 pom.xml에 알맞게 추가해주면 될 것이다. 그리고 스프링 설정은 다음과 같이 추가한다.

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

packagesToScan에 설정된 패키지 위치에 있는 엔티티를 자동으로 주입하게 된다. 엔티티 클래스가 무엇일까??? 자바 디자인 패턴이 그렇듯이 꼭 정답은 없지만 테이블 정의 클래스라고 해야하나? 하이버네이트에서는 퍼시스턴스 클래스라고 하는 것 같다. 뭐 그렇다. 해당 엔티티 클래스에는 어노테이션 엔티티가 있어야 한다.

hibernateProperties 하이버네이트 설정을 수동으로 관리할 수 있다. 아래는 기본적으로 사용하는 3가지 설정값들이다.

- hibernate.dialect:
- hibernate.format_sql:
- hibernate.show_sql: 실행된 쿼리를 출력해준다. 디버깅할때 유용하다.

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

데모 테이블을 생성하여 CRUD 기본적인 기능을 구현해본다.

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

하암~ 테스트 소스를 만들다보니 이미 하이버네이트 경험했던 사람들이 주의하라는 말을 이해할 수 있을 것 같다. `쥐뿔도 모르면 함부로 쓰지말아라...` 하이버네이트 세상에는 그들만의 규칙이 있다. 그걸 이해해야 한다. 음... ORM, JPA라고해야하나?? 여튼 꼭 책을 사서 공부를 하든 한글 문서를 공부를 하든 라이프사이클을 이해하기 바란다.

테스트 소스에는 CRUD에 관한 작업으로 모두 담았다. 하이버네이트는 배치로 구동하는 것 같다. 그래서 처리 순서가 위에서 아래로 순차적으로 흐르지 않고 하이버네이트에 의해 순서가 처리된다. 작업 하나하나가 바로 디비로 전달되는 것이 아니라 메모리에 기억해두었다가 커밋이 일어나면 처리된다. 배치처리가 된다는 것~

그래서 순차적으로 처리하고 싶다면 중간중간에 flush를 실행해서 detached 상태를 만들어 주면 된다.

하이버네이트에는 3가지 퍼시스턴스 객체 상태를 가질 수 있다.

- Transient: 퍼시스턴스 객체가 생성된 상태지만 하이버네이트에서 관리되지 않는 상태.
- Persistent: 하이버네이트에서 관리되는 상태.
- Detached: 하이버네이트에서 관리가 제외된 상태.

[참고] http://dev.anyframejava.org/anyframe/doc/core/3.2.0/corefw/guide/hibernate-introduction.html

이제 테스트소스를 보도록하자. 원래 DAO를 바로 불러서 사용하는 것 보다 Service 를 거쳐서 사용하는 것이 바람직하다. 하지만 테스트를 위해 직접 DAO를 주입하여 사용한 테스트 클래스이다. 그래서 트랜잭션이 걸려있지 않아 직접 어노테이션을 이용해 트랜잭션을 걸어주었다. 꼭 트랜잭션이 있어야 겠다고 하이버네이트는 오류를 던저주신다. `@Transactional`

`insertToDelete` 테스트는 insert 후 delete 를 실행하는 데 2가지 방법으로 구성했다. 첫번째는 쿼리를 직접 타이핑한 작업이고 두번째는 delete를 사용한 것이다. 여기서 두번째 delete는 객체를 퍼시스턴트 상태로 만든다음에 그걸 던져줘야 삭제가 된다.

그전에 첫번째 쿼리를 직접 타이핑하는 방식을 설명하겠다. 하이버네이트에는 3가지 메서드를 지원한다.

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

save는 demo_idx가 있고 merge는 demo_idx가 없다. 즉 save는 실행후 `persistent` 상태를 유지하고 merge는 `detached` 상태가 된다.

정확하기 이해하기 위해 테스트를 하나 더 해보겠다.

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

우선 demo1을 인서트한 후 demo1의 이름을 수정하고 플래쉬를 했다. 이럴경우 insert 요청만 했는데 업데이트까지 자동으로 진행한다. 우와!!!!!!!!! 그리고 flush&clear를 요청하여 detached 상태를 만든 후 다시 호출하고 변경해 보았다. 변화가 없다. 아래 로그를 보면 더 정확하게 확인 할 수 있다.

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

일반적인 설정방법과 크게 다르지 않다.


#### 추천 참고자료
- http://whiteship.tistory.com/category/Hibernate
- entityManagerFactory 사용하기
	- http://www.baeldung.com/2011/12/13/the-persistence-layer-with-spring-3-1-and-jpa/ 
- 그외 참고자료
	- http://www.journaldev.com/3524/spring-hibernate-integration-example-tutorial-spring-4-hibernate-3-and-hibernate-4
	- http://websystique.com/spring/spring4-hibernate4-mysql-maven-integration-example-using-annotations/
	- http://www.journaldev.com/7772/spring-orm-example-with-jpa-hibernate-aop-transactions
	- http://myjourneyonjava.blogspot.kr/2015/09/spring-4-mvc-hibernate-4-mysql-5-maven.html
