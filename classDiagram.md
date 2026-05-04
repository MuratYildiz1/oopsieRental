```mermaid

classDiagram
    direction TB

    class Rentable {
        <<interface>>
        +rent() void
        +returnVehicle() void
    }

    class Vehicle {
        <<abstract>>
        -plate : String
        -brand : String
        #dailyRate : double
        -isRented : boolean
        -rentedDays : int
        -branch : Branch
        -isUnderMaintenance : boolean
        -mileage : int
        +calculateRent(days: int)* double
        +calculateRent(days: int, discountPercent: double) double
        +addMileage(km: int) void
        +rent() void
        +returnVehicle() void
    }

    class Economy {
        +calculateRent(days: int) double
    }
    class SUV {
        +calculateRent(days: int) double
    }
    class Luxury {
        +calculateRent(days: int) double
    }
    class Van {
        +calculateRent(days: int) double
    }

    Rentable <|.. Vehicle : implements
    Vehicle <|-- Economy : extends
    Vehicle <|-- SUV : extends
    Vehicle <|-- Luxury : extends
    Vehicle <|-- Van : extends

    class Employee {
        <<abstract>>
        -employeeId : String
        -fullName : String
        -username : String
        -password : String
        -branch : Branch
        +getRolePermissions()* String
    }

    class BranchManager {
        +getRolePermissions() String
    }
    class RentalAgent {
        +getRolePermissions() String
        +processReturn(reservation: Reservation, drivenKm: int, hasDamage: boolean, damageFee: double) Invoice
    }
    class Mechanic {
        +getRolePermissions() String
    }

    Employee <|-- BranchManager : extends
    Employee <|-- RentalAgent : extends
    Employee <|-- Mechanic : extends

    class Branch {
        -branchId : String
        -branchName : String
        -city : String
        +findById(branches: ArrayList, id: String)$ Branch
    }

    Branch "1" o-- "many" Vehicle : contains
    Branch "1" o-- "many" Employee : contains

    class Customer {
        -id : String
        -name : String
        -surname : String
        -email : String
        -password : String
        -loyaltyTier : String
        -loyaltyPoints : int
        +getDiscountRate() double
    }

    class Reservation {
        -reservationId : String
        -customer : Customer
        -vehicle : Vehicle
        -days : int
        -totalPrice : double
        -insuranceType : String
        -insuranceDailyCost : double
        +checkAvailability() void
        -calculateFinalAmount() double
    }

    Reservation "1" --> "1" Customer : has-a
    Reservation "1" --> "1" Vehicle : has-a

    class Invoice {
        -invoiceId : String
        -reservation : Reservation
        -generationDate : Date
        -RentalDays : int
        -damageFee : double
        -totalAmount : double
        -discountAmount : double
        +getFormattedInvoice() String
    }

    Invoice "1" --> "1" Reservation : has-a

    class FileManager {
        +saveVehicles(vehicles: ArrayList)$ void
        +loadVehicles(branches: ArrayList)$ ArrayList
        +saveBranches(branches: ArrayList)$ void
        +loadBranches()$ ArrayList
        +saveEmployees(employees: ArrayList)$ void
        +loadEmployees(branches: ArrayList)$ ArrayList
        +saveCustomer(c: Customer)$ void
        +loadCustomers()$ ArrayList
        +saveReservation(res: Reservation)$ void
        +saveInvoice(invoice: Invoice)$ void
    }