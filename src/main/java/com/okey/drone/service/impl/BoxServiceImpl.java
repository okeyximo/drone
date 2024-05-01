package com.okey.drone.service.impl;

import com.okey.drone.dto.BaseResponse;
import com.okey.drone.dto.BoxRequest;
import com.okey.drone.dto.BoxResponse;
import com.okey.drone.dto.ItemRequest;
import com.okey.drone.dto.ItemResponse;
import com.okey.drone.entity.Box;
import com.okey.drone.entity.BoxState;
import com.okey.drone.entity.Item;
import com.okey.drone.exception.InvalidRequestException;
import com.okey.drone.exception.ResourceNotFoundException;
import com.okey.drone.repository.BoxRepository;
import com.okey.drone.repository.ItemRepository;
import com.okey.drone.service.BoxService;
import com.okey.drone.utils.BoxMaper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class BoxServiceImpl implements BoxService {

    private final BoxRepository boxRepository;
    private final ItemRepository itemRepository;
    private final BoxMaper boxMaper;

    @Override
    public BaseResponse<BoxResponse> registerBox(BoxRequest boxRequest) {
        Box box = boxMaper.mapToBoxEntity(boxRequest);
        boxRepository.save(box);
        return BaseResponse.<BoxResponse>builder()
                .status(201)
                .message("Box registered successfully")
                .data(boxMaper.mapToBoxResponse(box))
                .build();
    }

    @Override
    @Transactional
    public BaseResponse<BoxResponse> loadItems(String boxSerialNumber, List<ItemRequest> itemsToBeLoaded) {
        var box = boxRepository.findBytxRef(boxSerialNumber).orElseThrow(() -> new ResourceNotFoundException("Box not found"));
        if (box.getBatteryCapacity() < 25) {
            log.error("Box with serial number {} battery is low", boxSerialNumber);
            throw new InvalidRequestException("Box battery is low");
        }
        var totalItemWeight = calculateTotalWeight(itemsToBeLoaded, box.getLoadedItems());

        if (totalItemWeight > box.getWeightLimit()) {
            log.error("Item weight is more than box weight limit");
            throw new InvalidRequestException("Item weight is more than box weight limit");
        }

        List<Item> boxItems = boxMaper.mapToItemsList(itemsToBeLoaded, box);
        itemRepository.saveAll(boxItems);
        box.setState(BoxState.LOADED);
        box.setLoadedItems(boxItems);
        box = boxRepository.save(box);

        log.info("Items loaded successfully");
        return BaseResponse.<BoxResponse>builder()
                .status(201)
                .message("Items loaded successfully")
                .data(boxMaper.mapToBoxResponse(box))
                .build();
    }

    @Override
    @Transactional
    public List<ItemResponse> getLoadedItems(String boxSerialNumber) {
        var box = boxRepository.findBytxRef(boxSerialNumber).orElseThrow(() -> new ResourceNotFoundException("Box not found"));
        List<Item> boxItems = box.getLoadedItems();
        return boxMaper.mapToItemResponseList(boxItems);

    }

    @Override
    public List<BoxResponse> getAvailableBoxForLoading() {
        var availableBox = boxRepository.findBoxByStateAndBatteryCapacityGreaterThanEqual(25);
        return boxMaper.mapToBoxResponseList(availableBox);
    }

    @Override
    public BaseResponse<Integer> getBoxBatteryLevel(String boxSerialNumber) {
        var box = boxRepository.findBytxRef(boxSerialNumber).orElseThrow(() -> new ResourceNotFoundException("Box not found"));
        return BaseResponse.<Integer>builder()
                .status(200)
                .message("Box battery level")
                .data(box.getBatteryCapacity())
                .build();
    }

    private Double calculateTotalWeight(List<ItemRequest> itemRequests, List<Item> loadedItems) {
        double totalWeightItemRequests = itemRequests.stream()
                .mapToDouble(ItemRequest::getWeight)
                .sum();

        double totalWeightBoxItems = loadedItems.stream()
                .mapToDouble(Item::getWeight)
                .sum();

        return totalWeightItemRequests + totalWeightBoxItems;
    }
}
