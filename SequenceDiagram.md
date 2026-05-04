```mermaid

sequenceDiagram
    actor Agent as RentalAgent GUI_User
    participant GUI as RentalGUI
    participant Logic as RentalAgent Domain
    participant Veh as Vehicle
    participant Inv as Invoice
    participant DB as FileManager

    Agent->>GUI: Click Process Return
    GUI->>GUI: populateReturnDetails()
    GUI->>Logic: processReturn(reservation, km, damage, washing, missing)
    activate Logic
    Logic->>Veh: addMileage(km)

    alt hasDamage == true
        Logic->>Veh: setUnderMaintenance(true)
    else hasDamage == false
        Logic->>Veh: setUnderMaintenance(false)
    end

    Logic->>Veh: setRented(false)
    Logic->>Veh: setRentedDays(0)

    Logic->>Inv: new Invoice(reservation, washing, missing)
    activate Inv
    Inv-->>Logic: finalInvoice
    deactivate Inv

    Logic-->>GUI: return finalInvoice
    deactivate Logic
    
    GUI->>DB: saveInvoice(finalInvoice)
    GUI->>Veh: setBranch(dropoffCity)
    GUI->>DB: saveVehicles(vehicles)
    GUI->>Agent: Show Invoice Summary