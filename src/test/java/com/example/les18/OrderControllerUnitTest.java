package com.example.les18;

import com.example.les18.controller.OrderController;
import com.example.les18.dto.OrderDto;
import com.example.les18.security.JwtService;
import com.example.les18.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.is;

@WebMvcTest(OrderController.class)
@ActiveProfiles("test")
class OrderControllerUnitTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtService jwtService;

    @MockBean
    OrderService orderService;

    @Test
    @WithMockUser(username="testuser", roles="USER")       // check authorization, not authentication
    void shouldRetrieveCorrectOrder() throws Exception {

        OrderDto odto = new OrderDto();
        odto.productname = "Batavus fiets";
        odto.unitprice = 1500;
        odto.quantity = 5;

        Mockito.when(orderService.getOrder(123)).thenReturn(odto);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/orders/123"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productname", is("Batavus fiets")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.unitprice", is(1500.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity", is(5)));
    }
}
