package ca.gbc.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class OrderPlacedEvent { // Renamed from OrderPlaceEvent to OrderPlacedEvent
    private String orderNumber;
    private String email;
}
