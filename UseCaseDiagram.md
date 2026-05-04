```mermaid

flowchart LR
    agent[Rental Agent]
    mechanic[Mechanic]
    admin[System Admin / Manager]

    subgraph OOPSIE_RENTAL_SYSTEM
        UC1((Login / Register))
        UC2((View Cars & Apply Filters))
        UC3((Create Reservation Rent))
        UC4((Process Return & Generate Invoice))
        UC5((Send Car to Maintenance))
        UC6((Resolve Maintenance Add Notes))
        UC7((View Maintenance History))
        UC8((Calculate Deductions & Refund))
    end

    agent --> UC1
    agent --> UC2
    agent --> UC3
    agent --> UC4
    agent --> UC5

    mechanic --> UC1
    mechanic --> UC2
    mechanic --> UC6
    mechanic --> UC7

    admin --> UC1
    admin --> UC2
    admin --> UC7

    UC4 -.->|<<includes>>| UC8