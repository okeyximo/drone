package com.okey.drone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okey.drone.dto.BaseResponse;
import com.okey.drone.dto.BoxRequest;
import com.okey.drone.dto.BoxResponse;
import com.okey.drone.dto.ItemRequest;
import com.okey.drone.dto.ItemResponse;
import com.okey.drone.entity.BoxState;
import com.okey.drone.service.BoxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BoxControllerTests {


    private MockMvc mockMvc;

    @Mock
    private BoxService boxService;

    @InjectMocks
    private BoxController boxController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(boxController).build();
    }


    @Test
    public void testRegisterDrone() throws Exception {
        BoxRequest boxRequest = BoxRequest.builder().txRef("VALID123").state(BoxState.IDLE).batteryCapacity(20).weightLimit(50).model("Heavyweight").build();
        BoxResponse boxResponse = BoxResponse.builder().state(BoxState.IDLE).batteryCapacity(20).weightLimit(50).model("Heavyweight").build();
        when(boxService.registerBox(any(BoxRequest.class))).thenReturn(
                BaseResponse.<BoxResponse>builder()
                        .message("Box registered successfully")
                        .status(201)
                        .data(boxResponse)
                .build());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/box")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(boxRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(BaseResponse.<BoxResponse>builder()
                        .message("Box registered successfully")
                        .status(201)
                        .data(boxResponse)
                        .build())));
    }

    @Test
    public void testRegisterDrone_WhenTxRefExceedsMaxLength_ShouldReturnBadRequest() throws Exception {
        BoxRequest boxRequest = BoxRequest.builder().txRef("valid12345678901") // txRef exceeding max length
                .state(BoxState.IDLE)
                .batteryCapacity(20)
                .weightLimit(50)
                .model("Heavyweight")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/box")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(boxRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void testLoadItems() throws Exception {
        String boxSerialNumber = "123";
        List<ItemRequest> itemsToBeLoaded = Collections.singletonList(ItemRequest.builder().build());
        when(boxService.loadItems(boxSerialNumber, itemsToBeLoaded)).thenReturn(BaseResponse.<BoxResponse>builder().build());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/box/{boxSerialNumber}/load", boxSerialNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(itemsToBeLoaded)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetLoadedItems() throws Exception {
        String boxSerialNumber = "123";
        List<ItemResponse> itemResponses = Collections.singletonList(ItemResponse.builder().name("test").weight(20.0).build());
        when(boxService.getLoadedItems(boxSerialNumber)).thenReturn(itemResponses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/box/{boxSerialNumber}/items", boxSerialNumber))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(itemResponses)));
    }

    @Test
    public void testGetAvailableBoxesForLoading() throws Exception {
        when(boxService.getAvailableBoxForLoading()).thenReturn(Collections.singletonList(BoxResponse.builder().build()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/box/available"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetBoxBatteryLevel() throws Exception {
        String boxSerialNumber = "123";
        when(boxService.getBoxBatteryLevel(boxSerialNumber)).thenReturn(BaseResponse.<Integer>builder().build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/box/{boxSerialNumber}/battery", boxSerialNumber))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
