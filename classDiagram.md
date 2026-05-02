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
        +Vehicle(plate: String, brand: String, dailyRate: double)
        +calculateRent(days: int)* double
        +calculateRent(days: int, discountPercent: double) double
        +getPlate() String
        +getBrand() String
        +isRented() boolean
        +setRented(rented: boolean) void
        +rent() void
        +returnVehicle() void
    }

    class Economy {
        +Economy(plate: String, brand: String, dailyRate: double)
        +calculateRent(days: int) double
    }

    class SUV {
        +SUV(plate: String, brand: String, dailyRate: double)
        +calculateRent(days: int) double
    }

    class Luxury {
        +Luxury(plate: String, brand: String, dailyRate: double)
        +calculateRent(days: int) double
    }

    class Van {
        +Van(plate: String, brand: String, dailyRate: double)
        +calculateRent(days: int) double
    }

    Rentable <|.. Vehicle : implements
    Vehicle <|-- Economy : extends
    Vehicle <|-- SUV : extends
    Vehicle <|-- Luxury : extends
    Vehicle <|-- Van : extends

    class Customer {
        -id : String
        -name : String
        -surname : String
        -loyaltyTier : String
        -loyaltyPoints : int
        +Customer(id: String, name: String, surname: String)
        +getDiscountRate() double
        +getId() String
        +getName() String
        +getSurname() String
        +getLoyaltyTier() String
        +setLoyaltyTier(loyaltyTier: String) void
        +getLoyaltyPoints() int
        +addPoints(points: int) void
        +toString() String
    }

    class Reservation {
        -reservationId : String
        -customer : Customer
        -vehicle : Vehicle
        -days : int
        -totalPrice : double
        +Reservation(reservationId: String, customer: Customer, vehicle: Vehicle, days: int) RentalException
        -calculateFinalAmount() double
        +checkAvailability() void RentalException
        +getReservationId() String
        +getCustomer() Customer
        +getVehicle() Vehicle
        +getDays() int
        +getTotalPrice() double
        +toString() String
    }

    class Invoice {
        -invoiceId : String
        -reservation : Reservation
        -generationDate : Date
        +Invoice(invoiceId: String, reservation: Reservation)
        +getFormattedInvoice() String
        +getInvoiceId() String
    }

    class FileManager {
        -VEHICLE_FILE : String$
        -CUSTOMER_FILE : String$
        -RESERVATION_FILE : String$
        +saveVehicles(vehicles: ArrayList~Vehicle~) void$
        +loadVehicles() ArrayList~Vehicle~$
        +saveCustomer(c: Customer) void$
        +saveReservation(res: Reservation) void$
        +loadCustomers() ArrayList~Customer~$
        +saveInvoice(invoice: Invoice) void$
    }

    class RentalException {
        +RentalException(message: String)
    }

    class RentalGUI {
        <<extends JFrame>>
        -customers : ArrayList~Customer~
        -vehicles : ArrayList~Vehicle~
        -customerComboBox : JComboBox~Customer~
        -vehicleComboBox : JComboBox~Vehicle~
        -daysField : JTextField
        -vehicleTable : JTable
        -tableModel : DefaultTableModel
        +RentalGUI()
        -initData() void
        -createVehicleListPanel() JPanel
        -createReservationPanel() JPanel
        -refreshTable() void
        -handleReservation() void
        +main(args: String[])$ void
    }

    Reservation "1" --> "1" Customer : contains
    Reservation "1" --> "1" Vehicle : contains
    Invoice "1" --> "1" Reservation : contains
    RentalGUI ..> FileManager : uses
    RentalGUI ..> Reservation : creates