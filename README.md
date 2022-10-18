# Jasypt-example


Jasypt-SpringBoot-Java-Example

* 더 자세한 설명은 블로그에 있습니다. - https://0soo.tistory.com/m/73 

1. 의존성 추가
2. 암호화
3. Config Class 작성을 통한 복호화 예제 



# 1. 의존성 추가


* 현재 기준 최신 버전 3.0.4
#### Maven

```xml
<!-- https://mvnrepository.com/artifact/com.github.ulisesbocchio/jasypt-spring-boot-starter -->
<dependency>
    <groupId>com.github.ulisesbocchio</groupId>
    <artifactId>jasypt-spring-boot-starter</artifactId>
    <version>3.0.4</version>
</dependency>
```

#### Gradle

```groovy
// https://mvnrepository.com/artifact/com.github.ulisesbocchio/jasypt-spring-boot-starter
implementation 'com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.4'
```

# 2. Key 값을 정한 후 다음 2가지 방법 중 하나를 정하여 암호화

### 1 사이트를 통하여 암호화 하기

key-value로 암복호화 할 수 있는 jasypt는 다양한 사이트에서 암호화를 지원한다

* https://www.devglan.com/online-tools/jasypt-online-encryption-decryption



### 2 테스트 코드를 돌려서 만든 암호화된 값으로 프로퍼티 채우기

암 복호화를 위한 Secret Key를 설정
* 예제는 Secret Key 값을 `my-secret-password` 로 설정


* Junit5 테스트로 암호화한 예제

```java
public class JasyptTest {

    private String encryptKey = "my-secret-password"; // 키 값

    @Test
    void encryptTest() {
        String plainText = "plainText"; // 암호화할 평문

        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
        jasypt.setPassword(encryptKey); // 암호화할 키 값 파라미터로 전달
        jasypt.setAlgorithm("PBEWithMD5AndDES"); // 암호화 알고리즘

        String encryptText = jasypt.encrypt(plainText); // 암호화

        System.out.printf("encryptText : %s", encryptText); // 암호화한 문자열 출력
    }
}
```

* 결과

```
encryptText : VZXPnGGkbmfvJ8Bwp1N8/Yko9tPda11O
```



출력된 암호문을 프로퍼티에 등록해야 한다

* 등록할 때 주의사항은 반드시 암호문을 `ENC(...)` 로 감싸서 등록

* Jasypt는 복호화할 대상을 `ENC(암호화한 값)`으로 인식


# 3. 복호화를 위한 ConfigClass 작성 예제

```java
@Configuration
public class JasyptConfig {

    @Value("${jasypt.encryptor.password}") // 이 값을 property 파일이나 외부에서 주입받는다.  외부에서 넣어서 사용하는게 좋다.
    private String encryptKey;

    @Bean
    public StringEncryptor jasyptStringEncryptor() { // Bean 이름 
        PooledPBEStringEncryptor pbeStringEncryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(encryptKey); // 암호화 키
        config.setAlgorithm("PBEWithMD5AndDES"); // 암호화 알고리즘
        config.setKeyObtentionIterations("1000"); // 반복할 해싱 함수
        config.setPoolSize("1"); // 인스턴스 pool
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // salt 생성 클래스
        config.setStringOutputType("base64"); //인코딩 방식
        pbeStringEncryptor.setConfig(config);
        return pbeStringEncryptor;
    }
}
```


# 4. 복호화를 위한  Key 값 주입

### key 값을 어떻게 관리?

키 값을 관리하는 방법은 2가지가 있다.

1. property 파일에 직접 넣어 사용하기
2. 외부에서 `-jar 명령어`로 어플리케이션 실행시 전달
    * Password값을 application.yml에 넣는다면 누구나 복호화할 수 있게 되므로 의미가 없어진다.





### 1. property 파일에 직접 키 넣어 사용하기

* application.yml에 직접 key 값 넣어 사용하기 (public repository에서는 노출되므로 의미가 없다. )
* application.yml 예제처럼 key 값을 property 파일에 작성하고 Config 클래스에서 지정해주면 된다

```yaml
jasypt:
  encryptor:
    password: my-secret-password # 키 값. 이 값은 외부에서 넣어서 사용하는게 좋다.
    bean: jasyptStringEncryptor

example:
  encrypt-value : ENC(VZXPnGGkbmfvJ8Bwp1N8/Yko9tPda11O)
```


## 2. 외부에서 `-jar 명령어`로 어플리케이션 실행 시 전달

- java -jar 명령어로 어플리케이션 실행 시 전달

```
--jasypt.encryptor.password=testkey
```

`java명령어`를 사용해 어플리케이션을 실행하는 경우엔 맨 마지막에 **--** 를 사용하여 패스워드를 전달해준다.

'--' 앞의 **jasypt.encryptor.password** 는

복호화를 위한 `Config 클래스 설정` 에서 **@Value** 안에 넣어준 값과 동일해야 한다.



1. java 명령어를 사용해 실행하는법
2. IntelliJ에서 VmOption을 주고 실행하는법
3. gradle build 시 -P 옵션을 주고 실행하는법



### 2. IntelliJ 에서 로컬에서 VmOption을 주고 실행하는법

<img src="https://blog.kakaocdn.net/dn/l1f9D/btrOZJbf1BR/GVqpeMPaVKfkrmnrHbdXY1/img.png" width=700 height=300>

1. EditConfigruations 
2. Environment -> VM options에 -D@Value에 적은 값 - (@Value로 주입받는 프로퍼티  값이 같아야 한다. )
   * 예제에서는 jasypt.encryptor.password 로 했으므로
   * -Djasypt.encryptor.password=my-secret-password 를 넣어주고 실행



<img src="https://blog.kakaocdn.net/dn/dtzLOC/btrO0HDKzu8/FH5hWUiDSoyYcWcSDkxKDK/img.png" width=750 height=650>



---



## 3. gradle에서 build시 프로퍼티로 전달 - test property

- gradle에서 build 시 test의 프로퍼티로 전달

gradle에서 build를 진행하게되면 기본적으로 test도 같이 실행된다.

이때 암호화를 이용하여 같이 테스트를 하려 한다면 build 실행 시 Password를 함께 전달해주어야 한다.

```
-Pjasypt.encryptor.password=my-secret-password
```

우선 build명령어 실행 시 **-P**를 사용하여 gradle의 환경변수로 넘겨준다.

```
test {
    useJUnitPlatform()
    systemProperty 'jasypt.encryptor.password', findProperty("jasypt.encryptor.password")
}
```

* systemProperty는 키, 값 쌍을 인자로 테스트 수행 JVM의 시스템 프로퍼티 지정하는 메소드이다.

* findProperty를 통해 '-P' 이하로 입력된 속성을 찾아서 JVM의 시스템 프로퍼티로 지정해준다.

* 위와 같이 사용하면 public repository에서도 application.yml을 안전하게 보관할 수 있다.


# Gradle Build시

```shell
./gradlew clean build \
-Pjasypt.encryptor.password=${JASYPT_PASSWORD} \
-Pprofile=${SPRING_ACTIVE_PROFILE}
```

* -P옵션으로 넘겨주면 된다
* 위에서 ${JASYPT_PASSWORD} 로 되어있는 이유는 젠킨스에서 빌드하였기 때문 
  * 평문값으로 설정해줘도 된다. 


# Docker Build 시



```shell
docker build \
--build-arg SPRING_ACTIVE_PROFILE=${SPRING_ACTIVE_PROFILE} \
--build-arg JASYPT_PASSWORD=${JASYPT_PASSWORD} \
.

...
```

* --build-arg로 넘겨주면 된다.
* 위에서 ${JASYPT_PASSWORD} 로 되어있는 이유는 젠킨스에서 빌드하였기 때문
  * 평문값으로 설정해줘도 된다. 
