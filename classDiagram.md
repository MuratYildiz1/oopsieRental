```mermaid

classDiagram
    direction TB
    
    %% Base Classes
    class Vehicle {
        <<abstract>>
        -plate : String
        -brand : String
        -dailyRate : double
        -isRented : boolean
        -underMaintenance : boolean
        -branch : Branch
        -rentedDays : int
        +calculateRent(days: int) double
        +addMileage(km: int)
        +gettersAndSetters()
    }

    class Employee {
        <<abstract>>
        -id : String
        -name : String
        -username : String
        -password : String
        -branch : Branch
        +getRolePermissions() String
    }

    %% Vehicle Hierarchy
    class Economy { +calculateRent(days: int) double }
    class SUV { +calculateRent(days: int) double }
    class Luxury { +calculateRent(days: int) double }
    class Van { +calculateRent(days: int) double }

    Vehicle <|-- Economy
    Vehicle <|-- SUV
    Vehicle <|-- Luxury
    Vehicle <|-- Van

    %% Employee Hierarchy
    class RentalAgent {
        +processReturn() Invoice
    }
    class Mechanic {
        +performMaintenance(v: Vehicle)
    }

    Employee <|-- RentalAgent
    Employee <|-- Mechanic

    %% Core Business Classes
    class Customer {
        -customerId : String
        -name : String
        -surname : String
        -email : String
        -password : String
    }

    class Reservation {
        -reservationId : String
        -customer : Customer
        -vehicle : Vehicle
        -days : int
        -insuranceType : String
        -insuranceDailyCost : double
        -pickUpLocation : String
        -returnLocation : String
        -totalPrice : double
        -depositAmount : double
        +calculateFinalAmount() double
    }

    class Invoice {
        -invoiceId : String
        -reservation : Reservation
        -generationDate : Date
        -totalDeductions : double
        -finalRefund : double
        -deductionDetails : String
        +getFormattedInvoice() String
    }

    class Branch {
        -branchId : String
        -branchName : String
        -city : String
    }

    %% Management
    class FileManager {
        <<utility>>
        +saveVehicles(vehicles: List)
        +loadVehicles(branches: List)
        +saveReservation(res: Reservation)
        +saveInvoice(inv: Invoice)
        +saveCustomer(c: Customer)
    }

    class RentalGUI {
        -loggedInEmployeeName : String
        -unavailableReasons : Map
        -assignedMechanics : Map
        -activeReservations : List
        +refreshAllTables()
        +processReturnAction()
        +openAddUnavailableCarDialog()
        +saveMaintenanceRecord(r)
    }

    %% Relationships
    Vehicle --> Branch
    Employee --> Branch
    Reservation --> Vehicle
    Reservation --> Customer
    Invoice --> Reservation
    RentalGUI ..> FileManager
    RentalGUI --> Reservation