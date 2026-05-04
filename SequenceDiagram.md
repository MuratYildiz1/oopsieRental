```mermaid

sequenceDiagram
    actor Agent as RentalAgent (GUI)
    participant Res as Reservation
    participant Veh as Vehicle
    participant FM as FileManager
    participant Inv as Invoice

    Agent->>GUI: Selects Customer, Vehicle, Days and clicks Button
    GUI->>Res: new Reservation(Customer, Vehicle, Days)
    activate Res
    Res->>Veh: checkAvailability()
    Veh-->>Res: (If vehicle is available, no error thrown)
    Res->>Veh: calculateRent(days)
    Veh-->>Res: totalRentAmount (Returns price)
    Res->>Veh: setRented(true), setRentedDays(days)
    Res-->>GUI: Reservation Object Created
    deactivate Res

    GUI->>FM: saveReservation(res)
    GUI->>Inv: new Invoice(res, days, damageFee=0)
    activate Inv
    Inv-->>GUI: Invoice Object Created
    deactivate Inv
    GUI->>FM: saveInvoice(inv)
    GUI->>FM: saveVehicles(vehicles)
    GUI-->>Agent: Shows "Reservation completed successfully" message