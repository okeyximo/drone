package com.okey.drone.service.impl;

import com.okey.drone.dto.BaseResponse;
import com.okey.drone.dto.BoxRequest;
import com.okey.drone.dto.BoxResponse;
import com.okey.drone.dto.ItemRequest;
import com.okey.drone.dto.ItemResponse;
import com.okey.drone.entity.Box;
import com.okey.drone.entity.BoxState;
import com.okey.drone.entity.Item;
import com.okey.drone.exception.ResourceNotFoundException;
import com.okey.drone.repository.BoxRepository;
import com.okey.drone.repository.ItemRepository;
import com.okey.drone.utils.BoxMaper;
import com.okey.drone.utils.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoxServiceImplTest {

    @Mock
    private BoxMaper boxMaper;

    @Mock
    private BoxRepository boxRepository;

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    private BoxServiceImpl boxService;
    @Test
    public void testRegisterDrone_Success() {
        // Arrange
        BoxRequest boxRequest = TestData.getDroneRequest();
        Box box = TestData.getDrone();


        when(boxMaper.mapToBoxEntity(boxRequest)).thenReturn(box);
        when(boxMaper.mapToBoxResponse(box)).thenReturn(TestData.getDroneResponse(box));


        // Act
        BaseResponse<BoxResponse> response = boxService.registerBox(boxRequest);

        // Assert
        verify(boxRepository, times(1)).save(box);
        assertEquals(201, response.getStatus());
        assertEquals("Box registered successfully", response.getMessage());
        assertEquals(box.getTxRef(), response.getData().getTxRef());
    }

    @Test
    public void loadItems_whenDroneBatteryIsSufficientAndMedicationWeightIsLessThanDroneWeightLimit_loadsItemsSuccessfully() {
        // Given
        var boxSerialNumber = UUID.randomUUID().toString();
        var box = Box.builder()
                .txRef(boxSerialNumber)
                .batteryCapacity(100)
                .weightLimit(100)
                .state(BoxState.IDLE)
                .loadedItems(new ArrayList<>())
                .build();
        boxRepository.save(box);

        // When
        var medicationsToBeLoaded = List.of(
                ItemRequest.builder()
                        .name("Medication 1")
                        .weight(10.0)
                        .build(),
                ItemRequest.builder()
                        .name("Medication 2")
                        .weight(10.0)
                        .build()
        );

        when(boxRepository.findBytxRef(boxSerialNumber)).thenReturn(java.util.Optional.of(box));
        when(boxMaper.mapToItemsList(medicationsToBeLoaded, box)).thenReturn(List.of());
        when(boxMaper.mapToBoxResponse(box)).thenReturn(TestData.getDroneResponse(box));
        when(boxRepository.save(box)).thenReturn(box);
        when(boxRepository.save(box)).thenReturn(box);

        // Then
        var response = boxService.loadItems(boxSerialNumber, medicationsToBeLoaded);

        assertEquals(201, response.getStatus());
        assertEquals("Items loaded successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(BoxState.LOADED, box.getState());

        verify(boxRepository, times(1)).findBytxRef(boxSerialNumber);
        verify(boxMaper, times(1)).mapToItemsList(medicationsToBeLoaded, box);
        verify(boxMaper, times(1)).mapToBoxResponse(box);


    }

    @Test
    public void testGetLoadedItems_WithValidDroneSerialNumber_ShouldReturnMedicationResponseList() {
        String boxSerialNumber = UUID.randomUUID().toString();
        Box box = new Box();
        box.setTxRef(boxSerialNumber);
        Item item1 = new Item();
        item1.setId(1L);
        Item item2 = new Item();
        item2.setId(2L);
        List<Item> boxItems = new ArrayList<>();
        boxItems.add(item1);
        boxItems.add(item2);
        box.setLoadedItems(boxItems);
        List<ItemResponse> expectedResponse = new ArrayList<>();
        expectedResponse.add(TestData.getMedicationResponse(1L));
        expectedResponse.add(TestData.getMedicationResponse(2L));

        when(boxRepository.findBytxRef(boxSerialNumber)).thenReturn(Optional.of(box));
        when(boxMaper.mapToItemResponseList(boxItems)).thenReturn(expectedResponse);

        List<ItemResponse> actualResponse = boxService.getLoadedItems(boxSerialNumber);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testGetDroneBatteryLevel() {
        String boxSerialNumber = UUID.randomUUID().toString();
        int batteryCapacity = 80;

        Box box = new Box();
        box.setTxRef(boxSerialNumber);
        box.setBatteryCapacity(batteryCapacity);

        when(boxRepository.findBytxRef(boxSerialNumber)).thenReturn(Optional.of(box));

        BaseResponse<Integer> response = boxService.getBoxBatteryLevel(boxSerialNumber);

        assertEquals(200, response.getStatus());
        assertEquals("Box battery level", response.getMessage());
        assertEquals(batteryCapacity, response.getData());
    }

    @Test
    public void testGetBoxBatteryLevel_BoxNotFound() {
        String boxSerialNumber = UUID.randomUUID().toString();

        when(boxRepository.findBytxRef(boxSerialNumber)).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () -> {
            boxService.getBoxBatteryLevel(boxSerialNumber);
        });
    }
}