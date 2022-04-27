package com.marketganada.producttest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketganada.api.request.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuctionControllerPositiveTest {
    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper oMapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(ProductControllerPositiveTest.class);
    private static String accessToken;
    private static MockMultipartFile mFile;
    private static AuctionInsertRequest auctionPhone;
    private static AuctionInsertRequest auctionEarphone;
    private static LikeRequest like;

    private static int auctionPhoneId;
    private static int auctionEarphoneId;

    private static final int TEST_PRODUCT_PHONE_ID = 1;
    private static final int TEST_PRODUCT_EARPHONE_ID = 1;

    @BeforeAll
    public static void setup() throws Exception {
		String name = "file";
		String originalFileName = "test.jpg";
		String fileFullPath = ".\\test.jpg";

		mFile = new MockMultipartFile(name, originalFileName, "image/jpg", new FileInputStream(fileFullPath));
		List<MultipartFile> fileList = new ArrayList<>();
		fileList.add(mFile);

		auctionPhone = new AuctionInsertRequest();
		auctionPhone.setAuctionTitle("sample title");
		auctionPhone.setAuctionImages(fileList);
		auctionPhone.setCycle(new Date());
		auctionPhone.setDepreciation(100);
		auctionPhone.setEndTime(new Date());
		auctionPhone.setStartPrice(10000);
        auctionPhone.setProductId(TEST_PRODUCT_PHONE_ID);

        auctionEarphone = new AuctionInsertRequest();
        auctionEarphone.setAuctionTitle("sample title");
        auctionEarphone.setAuctionImages(fileList);
        auctionEarphone.setCycle(new Date());
        auctionEarphone.setDepreciation(100);
        auctionEarphone.setEndTime(new Date());
        auctionEarphone.setStartPrice(10000);
        auctionEarphone.setProductId(TEST_PRODUCT_EARPHONE_ID);

        like = new LikeRequest();
    }

    @Test
    @Order(0)
    void getAccessToken() throws Exception {
        UserLoginRequest userLogin = new UserLoginRequest();
        userLogin.setUserEmail("tttt@test.com");
        userLogin.setUserPw("test123!@#");

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .header("Accept", "application/json")
                        .contentType("application/json")
                        .content((new JSONObject(oMapper.writeValueAsString(userLogin)).toString())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        accessToken = new JSONObject(result.andReturn().getResponse().getContentAsString()).getString("token");
    }

    @Test
    @Order(1)
    void insertAuctionPhoneTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auction")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken)
                        .contentType("application/json")
                        .content((new JSONObject(oMapper.writeValueAsString(auctionPhone)).toString())))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(2)
    void insertAuctionEarphoneTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auction")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken)
                        .contentType("application/json")
                        .content((new JSONObject(oMapper.writeValueAsString(auctionEarphone)).toString())))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(3)
    void getAuctionPhoneListTest() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/auction/phone?page=1" +
                                "&sort=id&brand=브랜드&model=모델&save=저장장치")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        JSONObject tmp = new JSONObject(result.andReturn().getResponse().getContentAsString());
        JSONArray resultArray = tmp.getJSONArray("auctionList");
        tmp = resultArray.getJSONObject(resultArray.length()-1);

        auctionPhoneId = tmp.getInt("auctionId");
    }

    @Test
    @Order(4)
    void getAuctionEarphoneListTest() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/auction/earphone?page=1" +
                                "&sort=id&brand=브랜드&model=모델")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        JSONObject tmp = new JSONObject(result.andReturn().getResponse().getContentAsString());
        JSONArray resultArray = tmp.getJSONArray("auctionList");
        tmp = resultArray.getJSONObject(resultArray.length()-1);

        auctionEarphoneId = tmp.getInt("auctionId");
    }

    @Test
    @Order(5)
    void insertAuctionLikeTest() throws Exception {
        like.setAuctionId(auctionPhoneId);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auction/like")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken)
                        .contentType("application/json")
                        .content((new JSONObject(oMapper.writeValueAsString(like)).toString())))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(6)
    void getAuctionDetailTest() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/auction/"+auctionPhoneId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(7)
    void deleteAuctionLikeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/auction/like/"+auctionPhoneId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken)
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(8)
    void deleteAuctionTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/auction/"+auctionPhoneId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken)
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
