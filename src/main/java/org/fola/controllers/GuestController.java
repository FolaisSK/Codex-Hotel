package org.fola.controllers;

import org.fola.dtos.requests.BookRoomRequest;
import org.fola.dtos.requests.CalculatePaymentRequest;
import org.fola.services.GuestService;
import org.fola.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/codex-hotel/v1/guest")
public class GuestController {
    @Autowired
    GuestService guestService;
    @Autowired
    PaymentService paymentService;

    @GetMapping("/available-rooms")
    public ResponseEntity<?> viewAvailableRooms(){
        try{
            return ResponseEntity.ok(guestService.viewAllAvailableRooms());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/book-room")
    public ResponseEntity<?> bookRoom(@RequestBody BookRoomRequest request){
        try{
            return ResponseEntity.ok(guestService.bookRoom(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/view-reservation/{referenceNo}")
    public ResponseEntity<?> viewBooking(@PathVariable("referenceNo") String referenceNo){
        try{
            return ResponseEntity.ok(guestService.viewBooking(referenceNo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/cancel-reservation/{referenceNo}")
    public ResponseEntity<?> cancelBooking(@PathVariable("referenceNo") String referenceNo){
        try{
            return ResponseEntity.ok(guestService.cancelReservation(referenceNo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/check-availability/{roomNumber}")
    public ResponseEntity<?> viewRoomAvailability(@PathVariable("roomNumber") String roomNumber){
        try{
            return ResponseEntity.ok(guestService.checkRoomAvailability(roomNumber));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/festive-period-prices")
    public ResponseEntity<?> viewFestivePeriodPrices(){
        return ResponseEntity.ok(paymentService.viewFestivePeriodPrices());
    }

    @PostMapping("/calculate-payment")
    public ResponseEntity<?> calculatePayment(@RequestBody CalculatePaymentRequest request){
        try{
            return ResponseEntity.ok(paymentService.calculatePayment(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
