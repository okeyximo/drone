package com.okey.drone.utils;

import com.okey.drone.dto.BoxRequest;
import com.okey.drone.dto.BoxResponse;
import com.okey.drone.dto.ItemRequest;
import com.okey.drone.dto.ItemResponse;
import com.okey.drone.entity.Box;
import com.okey.drone.entity.Item;
import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class BoxMaper {

    public Box mapToBoxEntity(BoxRequest boxRequest) {
        return Box.builder()
                .model(boxRequest.getModel())
                .txRef(boxRequest.getTxRef())
                .weightLimit(boxRequest.getWeightLimit())
                .state(boxRequest.getState())
                .batteryCapacity(boxRequest.getBatteryCapacity())
                .build();

    }

    public List<ItemResponse> mapToItemResponseList(List<Item> items) {
        return items.stream()
                .map(item -> mapToitemResponse(item) )
                .collect(Collectors.toList());
    }

    public List<Item> mapToItemsList(List<ItemRequest> itemsToBeLoaded, Box box) {
        return itemsToBeLoaded.stream()
                .map(itemRequest -> mapToItem(itemRequest, box))
                .collect(Collectors.toList());
    }

    private Item mapToItem(ItemRequest itemRequest, Box box) {
        return Item.builder()
                .name(itemRequest.getName())
                .weight(itemRequest.getWeight())
                .code(itemRequest.getCode())
                .box(box)
                .build();
    }

    private ItemResponse mapToitemResponse(Item item) {
        return ItemResponse.builder()
                .name(item.getName())
                .weight(item.getWeight())
                .code(item.getCode())
                .build();
    }


    public List<BoxResponse> mapToBoxResponseList(List<Box> availableBox) {
        return availableBox.stream()
                .map(this::mapToBoxResponse)
                .collect(Collectors.toList());
    }

    public BoxResponse mapToBoxResponse(Box box) {
        return BoxResponse.builder()
                .batteryCapacity(box.getBatteryCapacity())
                .model(box.getModel())
                .txRef(box.getTxRef())
                .state(box.getState())
                .weightLimit(box.getWeightLimit())
                .loadedItems(mapToItemResponseList(box.getLoadedItems()))
                .build();
    }
}
