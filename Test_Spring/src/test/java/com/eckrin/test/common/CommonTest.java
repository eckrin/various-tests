package com.eckrin.test.common;

import com.eckrin.test.config.DatasourceConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

// @SpringBootTest는 테스트에 사용될 스프링 구성 클래스를 지정하는 데 사용한다.
// 현재 @SpringBootTest의 classes로 아무 class나 지정해주고, @EnableAutoConfiguration을 켜주면 밑의 DB 커넥션 테스트 성공
// 이유는 정확히 모르겠는데, main 디렉토리의 파일을 지정하고 AutoConfiguration을 하면 컴포넌트 스캔 범위가 확장되기 때문일수도?
// + baseTest가 성공해도 CommonTest를 상속받는 테스트 클래스의 메서드는 실패한다... 왜 그러지?
//@ExtendWith(SpringExtension.class) // 테스트에 Spring의 컨텍스트를 활성화하고 Spring의 기능을 Junit5 테스트에 통합시키는 역할을 한다 - Junit 5에서는 필요 없다는 얘기도 있음
@SpringBootTest(classes = {DatasourceConfig.class}) // 애플리케이션의 통합 테스트를 위한 설정을 제공한다. 즉 애플리케이션 컨텍스트를 활성화하고 필요한 빈들을 자동으로 로드한다.
@AutoConfigureMockMvc
@Transactional
@EnableAutoConfiguration
public class CommonTest {
    @Test
    @DisplayName("DB 커넥션 테스트")
    public void baseTest() {
    }
}
