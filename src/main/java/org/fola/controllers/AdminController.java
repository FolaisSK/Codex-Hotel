package org.fola.controllers;

import org.fola.dtos.requests.AddRoomRequest;
import org.fola.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/codex-hotel/v1/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @PostMapping("/add-room")
    public ResponseEntity<?> addRoom(@RequestBody AddRoomRequest request){
        try{
            return ResponseEntity.ok(adminService.addRoom(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/view-rooms")
    public ResponseEntity<?> viewAllRooms(){
        try{
            return ResponseEntity.ok(adminService.viewAllRooms());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/mark-room-for-maintenance/{roomNumber}")
    public ResponseEntity<?> markRoomForMaintenance(@PathVariable("roomNumber") String roomNumber){
        try {
            return ResponseEntity.ok(adminService.markRoomForMaintenance(roomNumber));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("unmark-room-for-maintenance/{roomNumber}")
    public ResponseEntity<?> unmarkRoomForMaintenance(@PathVariable("roomNumber") String roomNumber){
        try{
            return ResponseEntity.ok(adminService.unmarkRoomForMaintenance(roomNumber));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
