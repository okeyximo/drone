package com.okey.drone.controller;

import com.okey.drone.dto.BaseResponse;
import com.okey.drone.dto.BoxRequest;
import com.okey.drone.dto.BoxResponse;
import com.okey.drone.dto.ItemRequest;
import com.okey.drone.dto.ItemResponse;
import com.okey.drone.service.BoxService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/box")
@RequiredArgsConstructor
public class BoxController {

    private final BoxService boxService;

    @PostMapping
    public ResponseEntity<BaseResponse<BoxResponse>> registerDrone(@Valid @RequestBody BoxRequest boxRequest) {
        return new ResponseEntity<>(boxService.registerBox(boxRequest), HttpStatus.CREATED);
    }

    @PostMapping("/{txRef}/load")
    public ResponseEntity<?> loadItems(@PathVariable String txRef, @Valid @RequestBody List<ItemRequest> itemsToBeLoaded) {
        return new ResponseEntity<>(boxService.loadItems(txRef, itemsToBeLoaded), HttpStatus.OK);
    }

    @GetMapping("/{txRef}/items")
    public ResponseEntity<List<ItemResponse>> getLoadedItems(@PathVariable String txRef) {
        return new ResponseEntity<>(boxService.getLoadedItems(txRef), HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableBoxesForLoading() {
        return new ResponseEntity<>(boxService.getAvailableBoxForLoading(), HttpStatus.OK);
    }

    @GetMapping("/{txRef}/battery")
    public ResponseEntity<BaseResponse<Integer>> getBoxBatteryLevel(@PathVariable String txRef) {
        return new ResponseEntity<>(boxService.getBoxBatteryLevel(txRef), HttpStatus.OK);
    }

}
