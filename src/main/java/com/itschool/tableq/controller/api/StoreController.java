package com.itschool.tableq.controller.api;

import com.itschool.tableq.domain.Store;
import com.itschool.tableq.network.Response.StoreResponse;
import com.itschool.tableq.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class StoreController {
    private final com.itschool.tableq.service.storeService storeService;

    @GetMapping("/{storeid}")
    public ResponseEntity<StoreResponse> findStoreById(@PathVariable("storeid") Long storeid) {
        Store store = storeService.findById(storeid);
        return ResponseEntity.ok(new StoreResponse(store));
    }

    @PutMapping("{storeid}")
    public ResponseEntity<StoreResponse> update(@PathVariable Long storeid, @RequestBody String body){//수정허용은 어디까지?
        Store store = storeService.update(storeid,body);

        return ResponseEntity.ok(new StoreResponse(store));
    }

    @DeleteMapping("{storeid}")
    public ResponseEntity<StoreResponse> delete(@PathVariable Long storeid){
        storeService.delete(storeid);

        return ResponseEntity.ok().build();
    }

    @PostMapping("")
    public ResponseEntity<StoreResponse> create(@RequestBody Store body){
        Store store = storeService.create(body);
        return ResponseEntity.ok(new StoreResponse(store));
    }
}
