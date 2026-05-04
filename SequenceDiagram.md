```mermaid

sequenceDiagram
    actor Admin
    participant L as LoginGUI
    participant R as RentalGUI
    participant DB as FileSystem
    participant V as Vehicle
    participant RES as Reservation
    participant INV as Invoice

    Admin->>L: Enter credentials
    L->>DB: loadCustomers()
    DB-->>L: user list
    L->>R: init GUI

    Note over R: System initialized

    Admin->>R: Search and rent vehicle
    R->>RES: create reservation
    activate RES
    RES->>V: setRented(true)
    deactivate RES

    R->>DB: saveReservation
    R->>DB: saveVehicles

    Note over Admin,V: Return process

    Admin->>R: Process return
    R->>INV: create invoice
    activate INV
    INV-->>R: return amount
    deactivate INV

    R->>V: update location
    R->>V: setRented(false)
    R->>DB: saveInvoice
    R->>DB: saveVehicles