```mermaid

flowchart LR
    %% Actors
    Cust((Customer))
    Agent((Rental Agent))
    Mech((Mechanic))
    Mgr((Branch Manager))

    %% System Functions
    subgraph OOPSIE RENTAL SYSTEM
        UC1([System Login / Register])
        UC2([View Vehicle Catalog])
        UC3([Make Reservation and Rent])
        UC4([Process Vehicle Return])
        UC5([Damage Assessment and Maintenance])
        UC6([Branch Inventory and Reports])
    end

    %% Relationships
    Cust --> UC1
    Cust --> UC2

    Agent --> UC1
    Agent --> UC2
    Agent --> UC3
    Agent --> UC4

    Mech --> UC1
    Mech --> UC5

    Mgr --> UC1
    Mgr --> UC6

    %% Return and Damage Link
    UC4 -.->|If vehicle is damaged| UC5