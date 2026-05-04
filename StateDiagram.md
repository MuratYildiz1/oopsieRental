```mermaid

stateDiagram-v2
    [*] --> Available : System Initialization
    
    Available --> Rented : rent() [Reservation Created]
    Rented --> Available : processReturn() [hasDamage = false]
    
    Rented --> UnderMaintenance : processReturn() [hasDamage = true]
    Available --> UnderMaintenance : markUnavailable() [Sent to Mechanic]
    
    UnderMaintenance --> Available : resolveUnavailableCar() [Mechanic Notes Added]