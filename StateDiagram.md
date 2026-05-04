```mermaid

stateDiagram
    [*] --> StartReturnProcess
    StartReturnProcess --> UpdateMileage : Agent enters current km
    UpdateMileage --> DamageCheck : Is there damage on the vehicle?
    
    state DamageCheck <<choice>>
    DamageCheck --> DamageAssessment : Yes (hasDamage = true)
    DamageCheck --> CalculateInvoice : No (Smooth Return)
    
    DamageAssessment --> PutUnderMaintenance : isUnderMaintenance = true
    PutUnderMaintenance --> ApplyPenalty : Add 'damageFee' to invoice
    ApplyPenalty --> CalculateInvoice
    
    CalculateInvoice --> MakeVehicleAvailable : rentedDays = 0, isRented = false
    MakeVehicleAvailable --> GenerateInvoice : Create Invoice object
    GenerateInvoice --> SaveData : Write to .txt files with FileManager
    SaveData --> [*] : Process Completed