package com.bs.mycareer.service;

import com.bs.mycareer.Career.Career;
import com.bs.mycareer.Career.CareerContentRepository;
import com.bs.mycareer.Career.CareerDto;
import com.bs.mycareer.Career.CareerServiceImpl;
import com.bs.mycareer.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CareerServiceImplTest {
    private static final Logger logger = LoggerFactory.getLogger(CareerServiceImplTest.class);


    @Autowired
    private CareerContentRepository careerContentRepository;
    @Autowired
    private CareerServiceImpl careerService;

    @Autowired
    private UserRepository userRepository;



    @Test
    @DisplayName("커리어 작성하기")
    public void createCareerTest() throws Exception {
        //given
        CareerDto careerDto = new CareerDto("김보아 이력서", "자기소개서입니다~~");
        Long userId = 1L;

        //when
        Career career = careerService.createCareer(careerDto,userId);
        //then
        assertEquals(career.getUser().getUser_id(), userId);
        assertEquals(career.getTitle(),careerDto.getTitle());
        assertEquals(career.getContents(),careerDto.getContents());
        // 로그 출력
        logger.info("career.getUser() = {}", career.getUser());
        logger.info("career.getTitle() = {}", career.getTitle());
        logger.info("career.getContents() = {}", career.getContents());

    }

    @Test
    @DisplayName("커리어 id별 조회")
    @Transactional
    public void getCareerByIdTest() throws Exception {
        //given
        Long careerId = 1L;
        Career career = new Career();
        career.setId(careerId);
        career.setTitle("보아 이력서!!");
        career.setContents("저는 열심히 할수있습니다.");

        careerContentRepository.save(career);

        //when
        CareerDto careerDto = careerService.getCareerById(careerId);

        //then
        //isEqualToComparingFieldByField는 두 객체를 필드별로 비교하는 메서드!
        assertThat(careerDto).isEqualToComparingFieldByField(new CareerDto("보아 이력서!!", "저는 열심히 할수있습니다."));
        logger.info("careerDto.getTitle() = {}", careerDto.getTitle());
        logger.info("careerDto.getContents() = {}", careerDto.getContents());
    }

    @Test
    @DisplayName("존재하지 않는 ID로 개별 조회 테스트")
    public void getCareerByNonexistentIdTest() {
        //given
        Long nonexistentId = 999L;

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            careerService.getCareerById(nonexistentId);
        });

        // then
        assertEquals("해당하는 id의 career를 찾지못했습니다: " + nonexistentId, exception.getMessage());
    }

    @Test
    @DisplayName("전체 커리어 조회 테스트")
    public void getAllCareersTest() throws Exception {
        //given
        Career career1 = new Career();
        career1.setTitle("경력1");
        career1.setContents("내용1");

        Career career2 = new Career();
        career2.setTitle("경력2");
        career2.setContents("내용2");

        careerContentRepository.save(career1);
        careerContentRepository.save(career2);
        //when
        List<CareerDto> careerDtoList = careerService.getAllCareers();

        //then
        assertEquals(2, careerDtoList.size());

    }

}