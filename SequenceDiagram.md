```mermaid

sequenceDiagram
    actor User as Rental Agent
    participant GUI as RentalGUI
    participant Veh as Vehicle
    participant Inv as Invoice
    participant DB as FileManager

    User->>GUI: Click Process Return
    GUI->>GUI: populateReturnDetails()
    GUI->>GUI: processReturnAction()
    GUI->>Veh: setBranch(dropoffCity)
    GUI->>Veh: setRented(false)
    GUI->>Veh: setRentedDays(0)

    alt hasDamage == true
        GUI->>Veh: setUnderMaintenance(true)
    else hasDamage == false
        GUI->>Veh: setUnderMaintenance(false)
    end

    GUI->>Inv: new Invoice(reservation, washing, missing)
    activate Inv
    Inv-->>GUI: finalInvoice
    deactivate Inv

    GUI->>DB: saveInvoice(finalInvoice)
    GUI->>DB: saveVehicles(vehicles)
    GUI-->>User: Show Invoice Summary