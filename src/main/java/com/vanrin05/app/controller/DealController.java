package com.vanrin05.app.controller;

import com.vanrin05.app.model.Deal;
import com.vanrin05.app.service.DealService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/deals")
public class DealController {
    DealService dealService;

    @PostMapping
    public ResponseEntity<Deal> createDeal(@RequestBody Deal deal) {
        return ResponseEntity.ok(dealService.createDeal(deal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Deal> updateDeal(@PathVariable Long id, @RequestBody Deal deal) {
        return ResponseEntity.ok(dealService.updateDeal(deal, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Deal> deleteDeal(@PathVariable Long id) {
        dealService.deleteDeal(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
