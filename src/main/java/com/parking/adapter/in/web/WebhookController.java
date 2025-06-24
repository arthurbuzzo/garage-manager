package com.parking.adapter.in.web;

import com.parking.adapter.in.web.api.WebhookApi;
import com.parking.adapter.in.web.model.WebhookEvent;
import com.parking.application.usecase.ParkingUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebhookController implements WebhookApi {
    
    private final ParkingUseCase parkingUseCase;
    
    @Override
    public ResponseEntity<Void> _receiveWebhookEvent(WebhookEvent webhookEvent) {
        log.info("Received webhook event: {}", webhookEvent);
        
        try {
            switch (webhookEvent.getEventType()) {
                case ENTRY:
                    parkingUseCase.handleVehicleEntry(
                        webhookEvent.getLicensePlate(),
                        webhookEvent.getEntryTime()
                    );
                    break;
                    
                case PARKED:
                    parkingUseCase.handleVehicleParking(
                        webhookEvent.getLicensePlate(),
                        webhookEvent.getLat(),
                        webhookEvent.getLng()
                    );
                    break;
                    
                case EXIT:
                    parkingUseCase.handleVehicleExit(
                        webhookEvent.getLicensePlate(),
                        webhookEvent.getExitTime()
                    );
                    break;
                    
                default:
                    log.warn("Unknown event type: {}", webhookEvent.getEventType());
                    return ResponseEntity.badRequest().build();
            }
            
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            log.error("Error processing webhook event", e);
            return ResponseEntity.badRequest().build();
        }
    }
}