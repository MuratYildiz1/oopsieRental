```mermaid


flowchart LR
    %% Actor
    Admin[System Admin]

    %% System
    subgraph Rental_System_Management
        UC1((Login & Authentication))
        UC2((Manage Fleet\nFilter/Sort))
        UC3((Rent Vehicle\nCalc Price + Insurance))
        UC4((Process Return\nUpdate Location))
        UC5((Handle Vehicle Failure\nManual Add))
        UC6((Log Mechanic Notes))
        UC7((Monitor Maintenance History))
        UC8((Generate Refund Invoice))
    end

    %% Relationships
    Admin --> UC1
    Admin --> UC2
    Admin --> UC3
    Admin --> UC4
    Admin --> UC5
    Admin --> UC7

    %% Include / Extend relations
    UC4 -.->|<<include>>| UC8
    UC5 -.->|<<include>>| UC6
    UC7 -.->|<<uses>>| UC6