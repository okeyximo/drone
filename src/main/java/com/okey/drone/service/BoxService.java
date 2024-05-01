package com.okey.drone.service;

import com.okey.drone.dto.BaseResponse;
import com.okey.drone.dto.BoxRequest;
import com.okey.drone.dto.BoxResponse;
import com.okey.drone.dto.ItemRequest;
import com.okey.drone.dto.ItemResponse;

import java.util.List;

public interface BoxService {
    BaseResponse<BoxResponse> registerBox(BoxRequest boxRequest);

    BaseResponse<BoxResponse> loadItems(String txRef, List<ItemRequest> itemsRequest);

    List<ItemResponse> getLoadedItems(String txRef);

    List<BoxResponse> getAvailableBoxForLoading();

    BaseResponse<Integer> getBoxBatteryLevel(String txRef);
}
