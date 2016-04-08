# #2 Gradle Spring Project & IntelliJ

이번에는 Gradle을 이용한 스프링 프레임워크 프로젝트를 만들어본다. 이론적인 부분이나 자세한 설정 설명은 모두 생략한다. 이미 좋은 포스팅들이 많기 때문에 맨하단에 링크를 참조할 수 있게 포함하고 이번 포스팅은 어떻게 프로젝트를 만들고 인텔리J에 어떻게 연동하여 구동하는 알아보겠다.

그리고 내가 작성하는 포스팅은 내가 알기때문에 작성하는 것이 아니라 나도 알기위해 정리차원에서 작성되는 포스팅이라는 것을 다시 한번 알려둔다.

인텔리J에서 프로젝트를 만드는 데 아무것도 없는 자바 프로젝트를 만들어 준다. 최상위 경로에 **build.gradle** 파일을 생성하고 아래의 내용을 작성한다.

```
apply plugin: 'java'
apply plugin: 'idea'
apply plugin: 'war'

archivesBaseName = 'syaku-gradle'
group = 'org.syaku'
version = '1.0'

def version = [
        spring: '4.2.4.RELEASE',
        slf4j: '1.7.12',
        jackson: '2.1.0',
        junit: '4.9'
]

buildDir = 'target'

sourceCompatibility = 1.8
targetCompatibility = 1.8
[compileJava, compileTestJava]*.options*.encoding = 'UTF-8'

jar {
    manifest {
        attributes 'Implementation-Title': 'Syaku Gradle',
                'Implementation-Version': version
    }
}

repositories {
    mavenCentral()
}

dependencies {

    compile "org.springframework:spring-core:${version.spring}",
            "org.springframework:spring-web:${version.spring}",
            "org.springframework:spring-webmvc:${version.spring}"

    compile("org.springframework:spring-context:${version.spring}") {
        exclude group: 'commons-logging', module: 'commons-logging'
    }

    testCompile "org.springframework:spring-test:${version.spring}"

    testCompile "junit:junit:${version.junit}"

    compile "org.slf4j:slf4j-api:${version.slf4j}"
    runtime "org.slf4j:jcl-over-slf4j:${version.slf4j}",
            "org.slf4j:slf4j-log4j12:${version.slf4j}"
    runtime("log4j:log4j:1.2.17") {
                exclude group: 'javax.mail', module: 'mail'
                exclude group: 'javax.jms', module: 'jms'
                exclude group: 'com.sun.jdmk', module: 'jmxtools'
                exclude group: 'com.sun.jmx', module: 'jmxri'
            }

    compile "javax.inject:javax.inject:1"

    providedCompile "javax.servlet:javax.servlet-api:3.0.1",
                    "javax.servlet.jsp:jsp-api:2.2"

    runtime "javax.servlet:jstl:1.1.2"
}

test {
    systemProperties 'property': 'value'
}

war {
    archiveName = "${archivesBaseName}.${extension}"
}

task build(type: Copy) {
    description = 'Generate exploded war'
    group = 'build'
    dependsOn war.dependsOn

    into "${buildDir}/${archivesBaseName}"
    with war
}
```

각 라인들은 어떤 역활을 하는 지 설명하도록 하겠다.

```
apply plugin: 'java'
apply plugin: 'idea'
apply plugin: 'war'
```

플러그인을 3개가 있는 데. war 는 웹서비스 배포를 위한 웹어플리케이션을 생성해주는 플러그인이다.

```
archivesBaseName = 'syaku-gradle'
group = 'org.syaku'
version = '1.0'
```

이번 프로젝트에 대한 설정정보를 기록해두었다. 이런 정보를 이용하여 빌드시 폴더나 파일명으로 사용되게 된다.  
**archivesBaseName** 는 메이븐에서 **artifactId** 라고 생각하면 된다. 그리고 group = groupId 일 것이다.

```
def version = [
        spring: '4.2.4.RELEASE',
        slf4j: '1.7.12',
        jackson: '2.1.0',
        junit: '4.9'
]
```

필수 라이브러리나 라이브러리 버전이 통일되는 것이라면 이렇게 변수로 관리하면 된다. 그래서 버전을 변경할 경우 위의 값만 변경하면 되기때문에 관리가 쉬워진다. 사용번은 **"${version.slf4j}"** 이렇게하면 된다.

```
buildDir = 'target'

sourceCompatibility = 1.8
targetCompatibility = 1.8
[compileJava, compileTestJava]*.options*.encoding = 'UTF-8'
```

**buildDir** 이용하여 빌드 경로를 변경할 수 있다. 그리고 인텔리J의 경로도 변경해주자.
`File > Project Structure` 실행 후 Project 메뉴에서  Project compiler output 의 경로는 변경하면 된다.

소스 컴파일과 대상 컴파일 자바 버전을 설정한다. 없는 경우 환경변수에 설정된 자바버전을 사용한다. 그리고 컴파일시 언어셋을 지정하는 데 좀 더 정확하게 깊숙이 찾아서 인코딩하겠다는 설정으로 보인다.

```
repositories {
    mavenCentral()
}
```

라이브러리 저장소를 설정한다. maven 기본 저장소를 설정했다. 그 아래로 추가로 작성하면 된다.

```
dependencies {

    compile "org.springframework:spring-core:${version.spring}",
            "org.springframework:spring-web:${version.spring}",
            "org.springframework:spring-webmvc:${version.spring}"

    compile("org.springframework:spring-context:${version.spring}") {
        exclude group: 'commons-logging', module: 'commons-logging'
    }

    testCompile "org.springframework:spring-test:${version.spring}"
...
    compile "org.slf4j:slf4j-api:${version.slf4j}"

    runtime("log4j:log4j:1.2.17") {
                exclude group: 'javax.mail', module: 'mail'
                exclude group: 'javax.jms', module: 'jms'
                exclude group: 'com.sun.jdmk', module: 'jmxtools'
                exclude group: 'com.sun.jmx', module: 'jmxri'
            }


    providedCompile "javax.servlet:javax.servlet-api:3.0.1",
                    "javax.servlet.jsp:jsp-api:2.2"

}
```

의존성 관리에 관한 라이브러리 설정 부분인데 `providedCompile` 은 메이븐에서 provided 와 같은 역활을 한다. 배포시 생략되는 라이브러리이다. 내용중에 exclude 를 사용하려면 위와 같이 () 안에 라이브러리를 넣고 {} 를 이용하여 추가하면 된다.

```
war {
    archiveName = "${archivesBaseName}.${extension}"
}

task build(type: Copy) {
    description = 'Generate exploded war'
    group = 'build'
    dependsOn war.dependsOn

    into "${buildDir}/${archivesBaseName}"
    with war
}
```

마지막으로 배포에 관한 설정이다. war 를 생성시 archiveName 이용하여 파일명을 직접 설정할 수 있게 했고, 빌드가 완료되면 소스를 모두 ${buildDir} 아래 프로젝트명을 이용하여 복사하라는 명령이다.

메이븐에서 install 하면 target 폴더 아래 프로젝트명을 가진 폴더에 빌드가 완료된 최종 소스를 만들어주는 역활과 같다고 보면 된다.

빌드된 최종 소스를 인텔리J에 등록하여 서버를 구동해본다 이 과정은 아래 동영상으로 확인할 수 있다. 웹으로 해당 경로를 접속하여 확인하면 된다.

<iframe width="640" height="480" src="https://www.youtube.com/embed/YPVIPY8SReo" frameborder="0" allowfullscreen></iframe>


서버를 구동하지 않고 TDD(테스트 주도 개발)를 구현하여 실행하여도 된다.

```java
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/*-servlet.xml",
		"classpath*:config/*.xml"
})
public class DemoControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void dispDemoView() throws Exception {
		this.mockMvc.perform(get("/demo")).andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}
}
```

다음 포스팅에는 멀트 프로젝트를 구현해볼것이다.

### 문서정보

- Post : http://syaku.tistory.com/314
- Github : https://github.com/syakuis/syaku-gradle/tree/spring-web


### 추천 참고자료

- gradle 한글 문서 http://kwonnam.pe.kr/wiki/gradle
- gradle 문서 https://docs.gradle.org/current/userguide/userguide.html



