```mermaid

classDiagram
    class Rentable {
        <<interface>>
        +rent()
        +returnVehicle()
    }

    class Vehicle {
        <<abstract>>
        -plate: String
        -brand: String
        #dailyRate: double
        -isRented: boolean
        -rentedDays: int
        -isUnderMaintenance: boolean
        -mileage: int
        -branch: Branch
        +calculateRent(days: int) double
        +calculateRent(days: int, discount: double) double
        +addMileage(km: int)
        +rent()
        +returnVehicle()
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

    class Employee {
        <<abstract>>
        -employeeId: String
        -fullName: String
        -username: String
        -password: String
        -branch: Branch
        +getRolePermissions() String
        +getName() String
    }

    class Mechanic {
        +getRolePermissions() String
    }
    class BranchManager {
        +getRolePermissions() String
    }
    class RentalAgent {
        +getRolePermissions() String
        +processReturn(reservation: Reservation, drivenKm: int, hasDamage: boolean, hasWashingFee: boolean, hasMissingObj: boolean) Invoice
    }

    class Customer {
        -id: String
        -name: String
        -surname: String
        -email: String
        -password: String
        -loyaltyTier: String
        +getDiscountRate() double
    }

    class Reservation {
        -reservationId: String
        -customer: Customer
        -vehicle: Vehicle
        -days: int
        -totalPrice: double
        -insuranceType: String
        -insuranceDailyCost: double
        -pickUpLocation: String
        -returnLocation: String
        -employee: String
        -DEPOSIT_AMOUNT: double
        +calculateFinalAmount() double
    }

    class Invoice {
        -invoiceId: String
        -totalDeductions: double
        -finalRefund: double
        +getFormattedInvoice() String
    }

    class Branch {
        -branchId: String
        -branchName: String
        -city: String
    }

    Rentable <|.. Vehicle
    Vehicle <|-- Economy
    Vehicle <|-- SUV
    Vehicle <|-- Luxury
    Vehicle <|-- Van

    Employee <|-- Mechanic
    Employee <|-- BranchManager
    Employee <|-- RentalAgent

    Vehicle "*" --> "1" Branch : belongs to
    Employee "*" --> "1" Branch : works at
    Reservation "1" --> "1" Customer : made by
    Reservation "1" --> "1" Vehicle : reserves
    Invoice "1" --> "1" Reservation : generates