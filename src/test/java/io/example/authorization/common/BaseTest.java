package io.example.authorization.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.authorization.generator.PartnerGenerator;
import io.example.authorization.repository.ClientDetailRepository;
import io.example.authorization.repository.PartnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;

import static io.example.authorization.constants.TestActiveProfilesConstants.TEST;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
@ActiveProfiles(TEST)
@Disabled
@Import({PartnerGenerator.class})
public class BaseTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected PartnerRepository partnerRepository;

    @Autowired
    protected ClientDetailRepository clientDetailRepository;

    @Resource
    protected  PartnerGenerator partnerGenerator;

    @BeforeEach
    public void setup(){
        partnerRepository.deleteAll();
        clientDetailRepository.deleteAll();
    }
}
